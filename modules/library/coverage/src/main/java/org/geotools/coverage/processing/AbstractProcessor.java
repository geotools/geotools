/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.List;
import java.util.Locale;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LogRecord;
import java.awt.RenderingHints;
import java.io.IOException;
import java.io.Writer;

import org.opengis.coverage.Coverage;
import org.opengis.coverage.processing.Operation;
import org.opengis.coverage.processing.OperationNotFoundException;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.util.InternationalString;

import org.geotools.coverage.AbstractCoverage;
import org.geotools.coverage.grid.Interpolator2D;
import org.geotools.resources.image.ImageUtilities;
import org.geotools.resources.Arguments;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Loggings;
import org.geotools.resources.i18n.LoggingKeys;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.util.logging.Logging;


/**
 * Base class for {@linkplain Coverage coverage} processor implementations.
 *
 * @since 2.2
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public abstract class AbstractProcessor {
    /**
     * The logger for coverage processing operations.
     */
    public static final Logger LOGGER = Logging.getLogger("org.geotools.coverage.processing");

    /**
     * The logging level for reporting coverage operations.
     * This level is equals or slightly lower than {@link Level#INFO}.
     */
    public static final Level OPERATION = new LogLevel("OPERATION", 780);

    /**
     * The grid coverage logging level type.
     */
    private static final class LogLevel extends Level {
        protected LogLevel(final String name, final int level) {
            super(name, level);
        }
    }

    /**
     * The default coverage processor. Will be constructed only when first requested.
     *
     * @todo This is a temporary field, to be removed when a GeoAPI interfaces for coverage
     *       processing while be redesigned along the lines of ISO 19123.
     */
    private static AbstractProcessor DEFAULT;

    /**
     * Constructs a coverage processor.
     */
    public AbstractProcessor() {
        super();
    }

    /**
     * Returns a default processor instance.
     * <p>
     * <strong>Note:</strong> this is a temporary method, until we have GeoAPI interface for
     * coverage processor and a factory finder for their implementations.
     */
    public static synchronized AbstractProcessor getInstance() {
        if (DEFAULT == null) {
            DEFAULT = new DefaultProcessor((RenderingHints) null);
            DEFAULT.setAsDefault();
        }
        return DEFAULT;
    }

    /**
     * Notifies this processor that it is going to be used as the application-wide default
     * processor.
     */
    void setAsDefault() {
    }

    /**
     * Retrieves grid processing operations information. Each operation information contains
     * the name of the operation as well as a list of its parameters.
     */
    public abstract Collection<Operation> getOperations();

    /**
     * Returns the operation for the specified name.
     *
     * @param  name Name of the operation.
     * @return The operation for the given name.
     * @throws OperationNotFoundException if there is no operation for the specified name.
     */
    public abstract Operation getOperation(String name) throws OperationNotFoundException;

    /**
     * Applies an operation.
     *
     * @param  parameters Parameters required for the operation.
     * @return The result as a coverage.
     * @throws OperationNotFoundException if there is no operation for the parameter group name.
     */
    public abstract Coverage doOperation(final ParameterValueGroup parameters)
            throws OperationNotFoundException;

    /**
     * The locale for logging message or reporting errors. The default implementations
     * returns the {@linkplain Locale#getDefault default locale}. Subclasses can override
     * this method if a different locale is wanted.
     */
    public Locale getLocale() {
        return Locale.getDefault();
    }

    /**
     * Logs a message for an operation. The message will be logged only if the source grid
     * coverage is different from the result (i.e. if the operation did some work).
     *
     * @param source The source grid coverage.
     * @param result The resulting grid coverage.
     * @param operationName the operation name.
     * @param fromCache {@code true} if the result has been fetch from the cache.
     */
    final void log(final Coverage source,
                   final Coverage result,
                   final String   operationName,
                   final boolean  fromCache)
    {
        if (source != result) {
            String interp = "Nearest";
            if (result instanceof Interpolator2D) {
                interp = ImageUtilities.getInterpolationName(
                            ((Interpolator2D) result).getInterpolation());
            }
            final Locale locale = getLocale();
            final LogRecord record = Loggings.getResources(locale).getLogRecord(
                                     OPERATION, LoggingKeys.APPLIED_OPERATION_$4,
                                     getName((source!=null) ? source : result, locale),
                                     operationName, interp, Integer.valueOf(fromCache ? 1 : 0));
            // Note: DefaultProcessor is the class that will use this method.
            record.setSourceClassName("org.geotools.coverage.processing.DefaultProcessor");
            record.setSourceMethodName("doOperation");
            record.setLoggerName(LOGGER.getName());
            LOGGER.log(record);
        }
    }

    /**
     * Returns the primary source coverage from the specified parameters, or {@code null} if none.
     */
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

    /**
     * Returns the operation name for the specified parameters.
     */
    static String getOperationName(final ParameterValueGroup parameters) {
        return parameters.getDescriptor().getName().getCode().trim();
    }

    /**
     * Returns the coverage name in the specified locale.
     */
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
     * Makes sure that an argument is non-null.
     *
     * @param  name   Argument name.
     * @param  object User argument.
     * @throws IllegalArgumentException if {@code object} is null.
     */
    static void ensureNonNull(final String name, final Object object)
            throws IllegalArgumentException
    {
        if (object == null) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1, name));
        }
    }

    /**
     * Lists a summary of all operations to the specified stream.
     *
     * @param  out The destination stream.
     * @throws IOException if an error occured will writing to the stream.
     */
    public void listOperations(final Writer out) throws IOException {
        final Collection operations = getOperations();
        final CoverageParameterWriter writer = new CoverageParameterWriter(out);
        final List descriptors = new ArrayList(operations.size());
        for (final Iterator it=operations.iterator(); it.hasNext();) {
            final Operation operation = (Operation) it.next();
            if (operation instanceof AbstractOperation) {
                descriptors.add(((AbstractOperation) operation).descriptor);
            }
        }
        writer.summary(descriptors, null);
    }

    /**
     * Prints a description of operations to the specified stream. If the {@code names} array
     * is non-null, then only the specified operations are printed. Otherwise, all operations
     * are printed. The description details include operation names and lists of parameters.
     *
     * @param  out The destination stream.
     * @param  names The operation to print, or an empty array for none, or {@code null} for all.
     * @throws IOException if an error occured will writing to the stream.
     * @throws OperationNotFoundException if an operation named in {@code names} was not found.
     */
    public void printOperations(final Writer out, final String[] names)
            throws OperationNotFoundException, IOException
    {
        final CoverageParameterWriter writer = new CoverageParameterWriter(out);
        final String lineSeparator = System.getProperty("line.separator", "\n");
        if (names != null) {
            for (int i=0; i<names.length; i++) {
                final Operation operation = getOperation(names[i]);
                if (operation instanceof AbstractOperation) {
                    out.write(lineSeparator);
                    writer.format(((AbstractOperation) operation).descriptor);
                }
            }
        } else {
            final Collection operations = getOperations();
            for (final Iterator it=operations.iterator(); it.hasNext();) {
                final Operation operation = (Operation) it.next();
                if (operation instanceof AbstractOperation) {
                    out.write(lineSeparator);
                    writer.format(((AbstractOperation) operation).descriptor);
                }
            }
        }
    }

    /**
     * Dumps to the {@linkplain System#out standard output stream} a list of operations for the
     * default processor. If no argument is provided, then only a summary of operations is printed.
     * If arguments are provided, then the operation parameters are printed for all operation names
     * given in arguments. This method can been invoked from the command line. For example:
     *
     * <blockquote><pre>
     * java org.geotools.coverage.processing.DefaultProcessor Interpolate
     * </pre></blockquote>
     *
     * <strong>Note for Windows users:</strong> If the output contains strange
     * symbols, try to supply an "{@code -encoding}" argument. Example:
     *
     * <blockquote><pre>
     * java org.geotools.coverage.processing.DefaultProcessor -encoding Cp850
     * </pre></blockquote>
     *
     * The codepage number (850 in the previous example) can be fetch from the DOS
     * command line by entering the "{@code chcp}" command with no arguments.
     */
    public static void main(String[] args) {
        final Arguments arguments = new Arguments(args);
        final boolean all = arguments.getFlag("-all");
        args = arguments.getRemainingArguments(Integer.MAX_VALUE);
        final AbstractProcessor processor = getInstance();
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
            exception.printStackTrace(arguments.out);
        }
        arguments.out.flush();
    }
}
