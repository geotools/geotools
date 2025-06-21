/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2015, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.coverage.processing;

import it.geosolutions.jaiext.JAIExt;
import java.awt.RenderingHints;
import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.OperationDescriptor;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.TileCache;
import org.geotools.api.coverage.Coverage;
import org.geotools.api.coverage.processing.Operation;
import org.geotools.api.coverage.processing.OperationNotFoundException;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.parameter.ParameterDescriptorGroup;
import org.geotools.api.parameter.ParameterNotFoundException;
import org.geotools.api.parameter.ParameterValue;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.util.InternationalString;
import org.geotools.coverage.AbstractCoverage;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.Interpolator2D;
import org.geotools.image.ImageWorker;
import org.geotools.image.util.ImageUtilities;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.LoggingKeys;
import org.geotools.metadata.i18n.Loggings;
import org.geotools.metadata.i18n.Vocabulary;
import org.geotools.metadata.i18n.VocabularyKeys;
import org.geotools.util.Arguments;
import org.geotools.util.SoftValueHashMap;
import org.geotools.util.Utilities;
import org.geotools.util.factory.FactoryRegistry;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;

/**
 * Base class for {@linkplain Coverage coverage} processor implementations.
 *
 * @since 2.2
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 */
public class CoverageProcessor {

    /** The logger for coverage processing operations. */
    public static final Logger LOGGER = Logging.getLogger(CoverageProcessor.class);

    /**
     * The logging level for reporting coverage operations. This level is equals or slightly lower than
     * {@link Level#INFO}.
     */
    public static final Level OPERATION = Logging.OPERATION;

    /** The comparator for ordering operation names. */
    private static final Comparator<String> COMPARATOR =
            (name1, name2) -> name1.toLowerCase().compareTo(name2.toLowerCase());

    /**
     * The default coverage processor. Will be constructed only when first requested.
     *
     * @todo This is a temporary field, to be removed when a GeoAPI interfaces for coverage processing while be
     *     redesigned along the lines of ISO 19123.
     */
    private static CoverageProcessor DEFAULT;

    private static final SoftValueHashMap<Hints, CoverageProcessor> processorsPool = new SoftValueHashMap<>();

    /**
     * Cacheable instance of the {@link CoverageProcessor}. It prevents users to add operations manually calling
     * {@link #addOperation(Operation)}.
     *
     * @author Simone Giannecchini, GeoSolutions S.A.S.
     */
    private static final class CacheableCoverageProcessor extends CoverageProcessor {

        public CacheableCoverageProcessor() {
            super();
        }

        /** @param hints */
        public CacheableCoverageProcessor(RenderingHints hints) {
            super(hints);
        }

        @Override
        protected void addOperation(Operation operation) throws IllegalStateException {
            throw new UnsupportedOperationException();
        }
    }

    /** Augments the amount of memory allocated for the JAI tile cache. */
    static {
        final long targetCapacity = 0x4000000; // 64 Mo.
        final long maxMemory = Runtime.getRuntime().maxMemory();
        final TileCache cache = JAI.getDefaultInstance().getTileCache();
        if (maxMemory >= 2 * targetCapacity) {
            if (cache.getMemoryCapacity() < targetCapacity) {
                cache.setMemoryCapacity(targetCapacity);
            }
        }
        LOGGER.config("Java Advanced Imaging: "
                + JAI.getBuildVersion()
                + ", TileCache capacity="
                + (float) (cache.getMemoryCapacity() / (1024 * 1024))
                + " Mb");
        /*
         * Verifies that the tile cache has some reasonable value. A lot of users seem to
         * misunderstand the memory setting in Java and set wrong values. If the user set
         * a tile cache greater than the maximum heap size, tell him that he is looking
         * for serious trouble.
         */
        if (cache.getMemoryCapacity() + 4 * 1024 * 1024 >= maxMemory) {
            final LogRecord record =
                    Loggings.format(Level.SEVERE, LoggingKeys.EXCESSIVE_TILE_CACHE_$1, maxMemory / (1024 * 1024.0));
            record.setLoggerName(LOGGER.getName());
            LOGGER.log(record);
        }
        boolean isJaiExtEnabled = ImageWorker.isJaiExtEnabled();
        LOGGER.config("JAI-Ext operations are " + (isJaiExtEnabled ? "enabled" : "disabled"));
    }

    /**
     * The set of operations for this coverage processor. Keys are operation's name. Values are operations and should
     * not contains duplicated values. Note that while keys are {@link String} objects, the operation name are actually
     * case-insensitive because of the comparator used in the sorted map.
     */
    private final Map<String, Operation> operations = Collections.synchronizedMap(new TreeMap<>(COMPARATOR));

    /**
     * The rendering hints for JAI operations (never {@code null}). This field is usually given as argument to
     * {@link OperationJAI} methods.
     */
    protected final Hints hints;

    /** The service registry for finding {@link Operation} implementations. */
    protected final FactoryRegistry registry;

    /** Constructs a coverage processor. */
    public CoverageProcessor() {
        this(null);
    }

    /**
     * Constructs a default coverage processor. The {@link #scanForPlugins} method will be automatically invoked the
     * first time an operation is required. Additional operations can be added by subclasses with the
     * {@link #addOperation} method. Rendering hints will be initialized with the following hints:
     *
     * <p>
     *
     * <ul>
     *   <li>{@link JAI#KEY_REPLACE_INDEX_COLOR_MODEL} set to {@link Boolean#FALSE}.
     *   <li>{@link JAI#KEY_TRANSFORM_ON_COLORMAP} set to {@link Boolean#FALSE}.
     * </ul>
     *
     * @param hints A set of additional rendering hints, or {@code null} if none.
     */
    public CoverageProcessor(final RenderingHints hints) {
        registry = new FactoryRegistry(Arrays.asList(new Class<?>[] {Operation.class}));
        this.hints = new Hints();
        this.hints.put(JAI.KEY_REPLACE_INDEX_COLOR_MODEL, Boolean.FALSE);
        this.hints.put(JAI.KEY_TRANSFORM_ON_COLORMAP, Boolean.FALSE);

        // override with user hints
        if (hints != null) this.hints.add(hints);
    }

    /**
     * Returns a default processor instance.
     *
     * <p><strong>Note:</strong> this is a temporary method, until we have GeoAPI interface for coverage processor and a
     * factory finder for their implementations.
     */
    public static synchronized CoverageProcessor getInstance() {
        return getInstance(null);
    }

    /**
     * Returns a default processor instance.
     *
     * <p><strong>Note:</strong> this is a temporary method, until we have GeoAPI interface for coverage processor and a
     * factory finder for their implementations.
     */
    public static synchronized CoverageProcessor getInstance(final Hints hints) {
        if (hints == null || hints.isEmpty()) {
            if (DEFAULT == null) {
                DEFAULT = new CacheableCoverageProcessor();
                processorsPool.put(new Hints(), DEFAULT);
            }
            return DEFAULT;
        }
        if (processorsPool.containsKey(hints)) return processorsPool.get(hints);
        final CoverageProcessor processor = new CacheableCoverageProcessor(hints);
        processorsPool.put(hints, processor);
        return processor;
    }

    /**
     * This method is called when the user has registered another {@link OperationDescriptor} for an operation and
     * requires to update the various CoverageProcessors.
     */
    public static synchronized void updateProcessors() {
        Set<Hints> keySet = processorsPool.keySet();
        for (Hints key : keySet) {
            CoverageProcessor processor = processorsPool.get(key);
            processor.scanForPlugins();
        }
    }

    /**
     * This method is called when the user has registered another {@link OperationDescriptor} and must remove the old
     * operation instance from the processors.
     *
     * @param operationName Name of the operation to remove
     */
    public static synchronized void removeOperationFromProcessors(String operationName) {
        List<String> operations = JAIExt.getJAINames(operationName);
        Set<Hints> keySet = processorsPool.keySet();
        for (Hints key : keySet) {
            for (String opName : operations) {
                CoverageProcessor processor = processorsPool.get(key);
                try {
                    Operation op = processor.getOperation(opName);
                    if (op != null) {
                        processor.removeOperation(op);
                    }
                } catch (OperationNotFoundException e) {
                    LOGGER.warning("Operation: " + opName + " not found in CoverageProcessor");
                }
            }
        }
    }

    /**
     * The locale for logging message or reporting errors. The default implementations returns the
     * {@linkplain Locale#getDefault default locale}. Subclasses can override this method if a different locale is
     * wanted.
     */
    public Locale getLocale() {
        return Locale.getDefault();
    }

    /**
     * Logs a message for an operation. The message will be logged only if the source grid coverage is different from
     * the result (i.e. if the operation did some work).
     *
     * @param source The source grid coverage.
     * @param result The resulting grid coverage.
     * @param operationName the operation name.
     * @param fromCache {@code true} if the result has been fetch from the cache.
     */
    final void log(final Coverage source, final Coverage result, final String operationName, final boolean fromCache) {
        if (source != result) {
            String interp = "Nearest";
            if (result instanceof Interpolator2D) {
                interp = ImageUtilities.getInterpolationName(((Interpolator2D) result).getInterpolation());
            }
            final Locale locale = getLocale();
            final LogRecord record = Loggings.getResources(locale)
                    .getLogRecord(
                            OPERATION,
                            LoggingKeys.APPLIED_OPERATION_$4,
                            getName(source != null ? source : result, locale),
                            operationName,
                            interp,
                            Integer.valueOf(fromCache ? 1 : 0));
            // Note: DefaultProcessor is the class that will use this method.
            record.setSourceClassName("org.geotools.coverage.processing.DefaultProcessor");
            record.setSourceMethodName("doOperation");
            record.setLoggerName(LOGGER.getName());
            LOGGER.log(record);
        }
    }

    /** Returns the primary source coverage from the specified parameters, or {@code null} if none. */
    static Coverage getPrimarySource(final ParameterValueGroup parameters) {
        try {
            return (Coverage) parameters.parameter("Source").getValue();
        } catch (ParameterNotFoundException exception) {
            /*
             * "Source" parameter may not exists. Conservatively
             * assumes that the operation will do some usefull work.
             */
            return null;
        }
    }

    /** Returns the operation name for the specified parameters. */
    static String getOperationName(final ParameterValueGroup parameters) {
        return parameters.getDescriptor().getName().getCode().trim();
    }

    /** Returns the coverage name in the specified locale. */
    private static String getName(final Coverage coverage, final Locale locale) {
        if (coverage instanceof AbstractCoverage) {
            final InternationalString name = ((AbstractCoverage) coverage).getName();
            if (name != null) {
                return name.toString(locale);
            }
        }
        return Vocabulary.getResources(locale).getString(VocabularyKeys.UNTITLED);
    }

    /**
     * Lists a summary of all operations to the specified stream.
     *
     * @param out The destination stream.
     * @throws IOException if an error occured will writing to the stream.
     */
    public void listOperations(final Writer out) throws IOException {
        final Collection<Operation> operations = getOperations();
        final CoverageParameterWriter writer = new CoverageParameterWriter(out);
        final List<ParameterDescriptorGroup> descriptors = new ArrayList<>(operations.size());
        for (final Operation operation : operations) {
            if (operation instanceof AbstractOperation) {
                descriptors.add(((AbstractOperation) operation).descriptor);
            }
        }
        writer.summary(descriptors, null);
    }

    /**
     * Prints a description of operations to the specified stream. If the {@code names} array is non-null, then only the
     * specified operations are printed. Otherwise, all operations are printed. The description details include
     * operation names and lists of parameters.
     *
     * @param out The destination stream.
     * @param names The operation to print, or an empty array for none, or {@code null} for all.
     * @throws IOException if an error occured will writing to the stream.
     * @throws OperationNotFoundException if an operation named in {@code names} was not found.
     */
    public void printOperations(final Writer out, final String[] names) throws OperationNotFoundException, IOException {
        final CoverageParameterWriter writer = new CoverageParameterWriter(out);
        final String lineSeparator = System.getProperty("line.separator", "\n");
        if (names != null) {
            for (String name : names) {
                final Operation operation = getOperation(name);
                if (operation instanceof AbstractOperation) {
                    out.write(lineSeparator);
                    writer.format(((AbstractOperation) operation).descriptor);
                }
            }
        } else {
            final Collection<Operation> operations = getOperations();
            for (final Operation operation : operations) {
                if (operation instanceof AbstractOperation) {
                    out.write(lineSeparator);
                    writer.format(((AbstractOperation) operation).descriptor);
                }
            }
        }
    }

    /**
     * Add the specified operation to this processor. This method is usually invoked at construction time before this
     * processor is made accessible.
     *
     * @param operation The operation to add.
     * @throws IllegalStateException if an operation already exists with the same name.
     */
    protected void addOperation(final Operation operation) throws IllegalStateException {
        Utilities.ensureNonNull("operation", operation);
        synchronized (operations) {
            if (operations.isEmpty()) {
                scanForPlugins();
            }
            addOperation0(operation);
        }
    }

    /**
     * Removes the specified operation to this processor. This method is usually invoked at construction time before
     * this processor is made accessible.
     *
     * @param operation The operation to remove.
     */
    protected void removeOperation(final Operation operation) {
        Utilities.ensureNonNull("operation", operation);
        synchronized (operations) {
            if (operations.isEmpty()) {
                return;
            }
            operations.remove(operation.getName().trim());
        }
    }

    /**
     * Implementation of {@link #addOperation} method. Also used by {@link #scanForPlugins} instead of the public method
     * in order to avoid never-ending loop.
     */
    private void addOperation0(final Operation operation) throws IllegalStateException {
        final String name = operation.getName().trim();
        final Operation old = operations.put(name, operation);
        if (old != null && !old.equals(operation)) {
            operations.put(old.getName().trim(), old);
            throw new IllegalStateException(
                    MessageFormat.format(ErrorKeys.OPERATION_ALREADY_BOUND_$1, operation.getName()));
        }
    }

    /**
     * Retrieves grid processing operations information. Each operation information contains the name of the operation
     * as well as a list of its parameters.
     */
    public Collection<Operation> getOperations() {
        synchronized (operations) {
            if (operations.isEmpty()) {
                scanForPlugins();
            }
            return operations.values();
        }
    }

    /**
     * Returns the operation for the specified name.
     *
     * @param name Name of the operation (case insensitive).
     * @return The operation for the given name.
     * @throws OperationNotFoundException if there is no operation for the specified name.
     */
    public Operation getOperation(String name) throws OperationNotFoundException {
        Utilities.ensureNonNull("name", name);
        name = name.trim();
        synchronized (operations) {
            if (operations.isEmpty()) {
                scanForPlugins();
            }
            final Operation operation = operations.get(name);
            if (operation != null) {
                return operation;
            }
            throw new OperationNotFoundException(MessageFormat.format(ErrorKeys.OPERATION_NOT_FOUND_$1, name));
        }
    }

    /**
     * Returns a rendering hint.
     *
     * @param key The hint key (e.g. {@link Hints#JAI_INSTANCE}).
     * @return The hint value for the specified key, or {@code null} if there is no hint for the specified key.
     */
    public final Object getRenderingHint(final RenderingHints.Key key) {
        return hints.get(key);
    }

    /**
     * Applies a process operation to a coverage. The default implementation checks if source coverages use an
     * interpolation, and then invokes {@link AbstractOperation#doOperation}. If all source coverages used the same
     * interpolation, then this interpolation is applied to the resulting coverage (except if the resulting coverage has
     * already an interpolation).
     *
     * @param parameters Parameters required for the operation. The easiest way to construct them is to invoke <code>
     *     operation.{@link Operation#getParameters getParameters}()</code> and to modify the returned group.
     * @return The result as a coverage.
     * @throws OperationNotFoundException if there is no operation for the parameter group name.
     */
    public Coverage doOperation(final ParameterValueGroup parameters, final Hints hints) {
        Coverage source = getPrimarySource(parameters);
        final String operationName = getOperationName(parameters);
        final Operation operation = getOperation(operationName);
        /*
         * Detects the interpolation type for the source grid coverage.
         * The same interpolation will be applied on the result.
         */
        Interpolation[] interpolations = null;
        if (!operationName.equalsIgnoreCase("Interpolate")) {
            for (final GeneralParameterValue param : parameters.values()) {
                if (param instanceof ParameterValue) {
                    final Object value = ((ParameterValue) param).getValue();
                    if (value instanceof Interpolator2D) {
                        // If all sources use the same interpolation, preserves the
                        // interpolation for the resulting coverage. Otherwise, uses
                        // the default interpolation (nearest neighbor).
                        final Interpolation[] interp = ((Interpolator2D) value).getInterpolations();
                        if (interpolations == null) {
                            interpolations = interp;
                        } else if (!Arrays.equals(interpolations, interp)) {
                            // Set to no interpolation.
                            interpolations = null;
                            break;
                        }
                    }
                }
            }
        }
        /*
         * Applies the operation, applies the same interpolation and log a message.
         * Note: we don't use "if (operation instanceof AbstractOperation)" below
         *       because if it is not, we want the ClassCastException as the cause
         *       for the failure.
         */
        final AbstractOperation op;
        try {
            op = (AbstractOperation) operation;
        } catch (ClassCastException cause) {
            final OperationNotFoundException exception = new OperationNotFoundException(
                    MessageFormat.format(ErrorKeys.OPERATION_NOT_FOUND_$1, operationName));
            exception.initCause(cause);
            throw exception;
        }

        // set up hints
        final Hints localMergeHints = this.hints.clone();
        if (hints != null) localMergeHints.add(hints);

        // processwith local hints
        Coverage coverage = op.doOperation(parameters, localMergeHints);
        if (interpolations != null && coverage instanceof GridCoverage2D && !(coverage instanceof Interpolator2D)) {
            coverage = Interpolator2D.create((GridCoverage2D) coverage, interpolations);
        }
        log(source, coverage, operationName, false);
        return coverage;
    }
    /**
     * Applies a process operation to a coverage. The default implementation checks if source coverages use an
     * interpolation, and then invokes {@link AbstractOperation#doOperation}. If all source coverages used the same
     * interpolation, then this interpolation is applied to the resulting coverage (except if the resulting coverage has
     * already an interpolation).
     *
     * @param parameters Parameters required for the operation. The easiest way to construct them is to invoke <code>
     *     operation.{@link Operation#getParameters getParameters}()</code> and to modify the returned group.
     * @return The result as a coverage.
     * @throws OperationNotFoundException if there is no operation for the parameter group name.
     */
    public Coverage doOperation(final ParameterValueGroup parameters) throws OperationNotFoundException {
        return doOperation(parameters, null);
    }

    /**
     * Scans for factory plug-ins on the application class path. This method is needed because the application class
     * path can theoretically change, or additional plug-ins may become available. Rather than re-scanning the classpath
     * on every invocation of the API, the class path is scanned automatically only on the first invocation. Clients can
     * call this method to prompt a re-scan. Thus this method need only be invoked by sophisticated applications which
     * dynamically make new plug-ins available at runtime.
     */
    public void scanForPlugins() {
        synchronized (operations) {
            registry.getFactories(Operation.class, null, null)
                    .filter(operation ->
                            !operations.containsKey(operation.getName().trim()))
                    .forEach(this::addOperation0);
        }
    }

    /**
     * Dumps to the {@linkplain System#out standard output stream} a list of operations for the default processor. If no
     * argument is provided, then only a summary of operations is printed. If arguments are provided, then the operation
     * parameters are printed for all operation names given in arguments. This method can been invoked from the command
     * line. For example:
     *
     * <blockquote>
     *
     * <pre>
     * java org.geotools.coverage.processing.DefaultProcessor Interpolate
     * </pre>
     *
     * </blockquote>
     *
     * <strong>Note for Windows users:</strong> If the output contains strange symbols, try to supply an
     * "{@code -encoding}" argument. Example:
     *
     * <blockquote>
     *
     * <pre>
     * java org.geotools.coverage.processing.DefaultProcessor -encoding Cp850
     * </pre>
     *
     * </blockquote>
     *
     * The codepage number (850 in the previous example) can be fetch from the DOS command line by entering the
     * "{@code chcp}" command with no arguments.
     */
    public static void main(String[] args) {
        final Arguments arguments = new Arguments(args);
        final boolean all = arguments.getFlag("-all");
        args = arguments.getRemainingArguments(Integer.MAX_VALUE);
        final CoverageProcessor processor = getInstance();
        try {
            if (args.length == 0) {
                processor.listOperations(arguments.out);
            } else {
                processor.printOperations(arguments.out, all ? null : args);
            }
        } catch (OperationNotFoundException exception) {
            arguments.out.println(exception.getLocalizedMessage());
        } catch (IOException exception) {
            // Should not occurs
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", exception);
        }
        arguments.out.flush();
    }

    public static Object getParameter(ParameterBlockJAI parameters, String paramName) {
        Object param = null;
        if (parameters != null) {
            try {
                param = parameters.getObjectParameter(paramName);
            } catch (IllegalArgumentException e) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Required parameter is unavailable: " + paramName + ". Returning null ");
                }
            }
        }
        return param;
    }
}
