/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.TileCache;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.Interpolator2D;
import org.geotools.factory.FactoryRegistry;
import org.geotools.factory.Hints;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.LoggingKeys;
import org.geotools.resources.i18n.Loggings;
import org.opengis.coverage.Coverage;
import org.opengis.coverage.processing.Operation;
import org.opengis.coverage.processing.OperationNotFoundException;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;


/**
 * Default implementation of a {@linkplain Coverage coverage} processor.
 * This default implementation makes the following assumptions:
 * <p>
 * <ul>
 *   <li>Operations are declared in the
 *       {@code META-INF/services/org.opengis.coverage.processing.Operation} file.</li>
 *   <li>Operations are actually instances of {@link AbstractOperation} (note: this constraint
 *       may be relaxed in a future version after GeoAPI interfaces for grid coverage will be
 *       redesigned).</li>
 *   <li>Most operations are backed by <cite>Java Advanced Imaging</cite>.</li>
 * </ul>
 * <p>
 * <strong>Note:</strong> This implementation do not caches produced coverages. Since coverages
 * may be big, consider wrapping {@code DefaultProcessor} instances in {@link BufferedProcessor}.
 *
 * @since 2.2
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class DefaultProcessor extends AbstractProcessor {
    /**
     * Augments the amout of memory allocated for the JAI tile cache.
     */
    static {
        final long targetCapacity = 0x4000000; // 64 Mo.
        final long maxMemory = Runtime.getRuntime().maxMemory();
        final TileCache cache = JAI.getDefaultInstance().getTileCache();
        if (maxMemory >= 2*targetCapacity) {
            if (cache.getMemoryCapacity() < targetCapacity) {
                cache.setMemoryCapacity(targetCapacity);
            }
        }
        LOGGER.config("Java Advanced Imaging: " + JAI.getBuildVersion() +
                    ", TileCache capacity="+(float)(cache.getMemoryCapacity()/(1024*1024))+" Mb");
        /*
         * Verifies that the tile cache has some reasonable value. A lot of users seem to
         * misunderstand the memory setting in Java and set wrong values. If the user set
         * a tile cache greater than the maximum heap size, tell him that he is looking
         * for serious trouble.
         */
        if (cache.getMemoryCapacity() + (4*1024*1024) >= maxMemory) {
            final LogRecord record = Loggings.format(Level.SEVERE,
                    LoggingKeys.EXCESSIVE_TILE_CACHE_$1, maxMemory / (1024 * 1024.0));
            record.setLoggerName(LOGGER.getName());
            LOGGER.log(record);
        }
    }

    /**
     * The comparator for ordering operation names.
     */
    private static final Comparator<String> COMPARATOR = new Comparator<String>() {
        public int compare(final String name1, final String name2) {
            return name1.toLowerCase().compareTo(name2.toLowerCase());
        }
    };

    /**
     * The set of operations for this coverage processor. Keys are operation's name.
     * Values are operations and should not contains duplicated values. Note that while
     * keys are {@link String} objects, the operation name are actually case-insensitive
     * because of the comparator used in the sorted map.
     */
    private final Map<String,Operation> operations = new TreeMap<String,Operation>(COMPARATOR);

    /**
     * The rendering hints for JAI operations (never {@code null}).
     * This field is usually given as argument to {@link OperationJAI} methods.
     */
    private final Hints hints;

    /**
     * The service registry for finding {@link Operation} implementations.
     */
    private final FactoryRegistry registry;

    /**
     * Constructs a default coverage processor. The {@link #scanForPlugins} method will be
     * automatically invoked the first time an operation is required. Additional operations
     * can be added by subclasses with the {@link #addOperation} method. Rendering hints will
     * be initialized with the following hints:
     * <p>
     * <ul>
     *   <li>{@link JAI#KEY_REPLACE_INDEX_COLOR_MODEL} set to {@link Boolean#FALSE}.</li>
     *   <li>{@link JAI#KEY_TRANSFORM_ON_COLORMAP} set to {@link Boolean#FALSE}.</li>
     * </ul>
     *
     * @param hints A set of additional rendering hints, or {@code null} if none.
     */
    public DefaultProcessor(final RenderingHints hints) {
        registry = new FactoryRegistry(Arrays.asList(new Class<?>[] {
            Operation.class
        }));
        this.hints = new Hints(hints);
        this.hints.put(JAI.KEY_REPLACE_INDEX_COLOR_MODEL, Boolean.FALSE);
        this.hints.put(JAI.KEY_TRANSFORM_ON_COLORMAP,     Boolean.FALSE);
        this.hints.put(Hints.GRID_COVERAGE_PROCESSOR,     this); // Must overwrites user setting.
    }

    /**
     * Sets the GRID_COVERAGE_PROCESSOR hint to the specified value.
     * This is used by {@link BufferedProcessor} only.
     */
    final void setProcessor(final AbstractProcessor processor) {
        hints.put(Hints.GRID_COVERAGE_PROCESSOR, processor);
    }

    /**
     * Removes the GRID_COVERAGE_PROCESSOR hint. The {@link AbstractOperation#getProcessor} method
     * will automatically returns the default instance when this hint is not defined. Removing this
     * hint provides three advantages for the common case when the default processor is used:
     *
     * <ul>
     *   <li>Avoid a strong reference to this processor in {@link RenderedImage} properties.</li>
     *   <li>Avoid serialization of this processor when a {@link RenderedImage} is serialized.</li>
     *   <li>Allows {@link AbstractOperation#getProcessor} to returns the {@link BufferedProcessor}
     *       instance instead of this instance.</li>
     * </ul>
     */
    @Override
    void setAsDefault() {
        hints.remove(Hints.GRID_COVERAGE_PROCESSOR);
    }

    /**
     * Add the specified operation to this processor. This method is usually invoked
     * at construction time before this processor is made accessible.
     *
     * @param  operation The operation to add.
     * @throws IllegalStateException if an operation already exists with the same name.
     */
    protected synchronized void addOperation(final Operation operation) throws IllegalStateException {
        ensureNonNull("operation", operation);
        if (operations.isEmpty()) {
            scanForPlugins();
        }
        addOperation0(operation);
    }

    /**
     * Implementation of {@link #addOperation} method. Also used by {@link #scanForPlugins}
     * instead of the public method in order to avoid never-ending loop.
     */
    private void addOperation0(final Operation operation) throws IllegalStateException {
        final String name = operation.getName().trim();
        final Operation old = operations.put(name, operation);
        if (old!=null && !old.equals(operation)) {
            operations.put(old.getName().trim(), old);
            throw new IllegalStateException(Errors.getResources(getLocale()).getString(
                      ErrorKeys.OPERATION_ALREADY_BOUND_$1, operation.getName()));
        }
    }

    /**
     * Retrieves grid processing operations information. Each operation information contains
     * the name of the operation as well as a list of its parameters.
     */
    public synchronized Collection<Operation> getOperations() {
        if (operations.isEmpty()) {
            scanForPlugins();
        }
        return operations.values();
    }

    /**
     * Returns the operation for the specified name.
     *
     * @param  name Name of the operation (case insensitive).
     * @return The operation for the given name.
     * @throws OperationNotFoundException if there is no operation for the specified name.
     */
    public synchronized Operation getOperation(String name) throws OperationNotFoundException {
        ensureNonNull("name", name);
        name = name.trim();
        if (operations.isEmpty()) {
            scanForPlugins();
        }
        final Operation operation = operations.get(name);
        if (operation != null) {
            return operation;
        }
        throw new OperationNotFoundException(Errors.getResources(getLocale()).getString(
                ErrorKeys.OPERATION_NOT_FOUND_$1, name));
    }

    /**
     * Returns a rendering hint.
     *
     * @param  key The hint key (e.g. {@link Hints#JAI_INSTANCE}).
     * @return The hint value for the specified key, or {@code null} if there is no hint for the
     *         specified key.
     */
    public final Object getRenderingHint(final RenderingHints.Key key) {
        return hints.get(key);
    }

    /**
     * Applies a process operation to a coverage. The default implementation checks if source
     * coverages use an interpolation, and then invokes {@link AbstractOperation#doOperation}.
     * If all source coverages used the same interpolation, then this interpolation is applied
     * to the resulting coverage (except if the resulting coverage has already an interpolation).
     *
     * @param  parameters Parameters required for the operation. The easiest way to construct them
     *         is to invoke <code>operation.{@link Operation#getParameters getParameters}()</code>
     *         and to modify the returned group.
     * @return The result as a coverage.
     * @throws OperationNotFoundException if there is no operation for the parameter group name.
     */
    public synchronized Coverage doOperation(final ParameterValueGroup parameters)
            throws OperationNotFoundException
    {
        Coverage source = getPrimarySource(parameters);
        final String operationName = getOperationName(parameters);
        final Operation  operation = getOperation(operationName);
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
                        Errors.getResources(getLocale()).getString(
                        ErrorKeys.OPERATION_NOT_FOUND_$1, operationName));
            exception.initCause(cause);
            throw exception;
        }
        Coverage coverage = op.doOperation(parameters, hints);
        if (interpolations != null && (coverage instanceof GridCoverage2D) &&
                                     !(coverage instanceof Interpolator2D))
        {
            coverage = Interpolator2D.create((GridCoverage2D) coverage, interpolations);
        }
        log(source, coverage, operationName, false);
        return coverage;
    }

    /**
     * Scans for factory plug-ins on the application class path. This method is needed because the
     * application class path can theoretically change, or additional plug-ins may become available.
     * Rather than re-scanning the classpath on every invocation of the API, the class path is
     * scanned automatically only on the first invocation. Clients can call this method to prompt
     * a re-scan. Thus this method need only be invoked by sophisticated applications which
     * dynamically make new plug-ins available at runtime.
     */
    public synchronized void scanForPlugins() {
        final Iterator<Operation> it = registry.getServiceProviders(Operation.class, null, null);
        while (it.hasNext()) {
            final Operation operation = it.next();
            final String name = operation.getName().trim();
            if (!operations.containsKey(name)) {
                addOperation0(operation);
            }
        }
    }
}
