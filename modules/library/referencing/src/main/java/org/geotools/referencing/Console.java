/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Locale;
import java.util.StringTokenizer;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.geometry.Position;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.CoordinateOperationFactory;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.NoninvertibleTransformException;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.GeneralPosition;
import org.geotools.measure.Measure;
import org.geotools.measure.UnitFormat;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.Vocabulary;
import org.geotools.metadata.i18n.VocabularyKeys;
import org.geotools.referencing.crs.AbstractCRS;
import org.geotools.referencing.wkt.AbstractConsole;
import org.geotools.referencing.wkt.Parser;
import org.geotools.referencing.wkt.Preprocessor;
import org.geotools.util.Arguments;
import org.geotools.util.TableWriter;

/**
 * A console for executing CRS operations from the command line. Instructions are read from the {@linkplain System#in
 * standard input stream} and results are sent to the {@linkplain System#out standard output stream}. Instructions
 * include:
 *
 * <table>
 *   <tr><td nowrap valign="top">{@code SET} <var>name</var> {@code =} <var>wkt</var></td><td>
 *   Set the specified <var>name</var> as a shortcut for the specified Well Know
 *   Text (<var>wkt</var>). This WKT can contains other shortcuts defined previously.</td></tr>
 *
 *   <tr><td nowrap valign="top">{@code transform = } <var>wkt</var></td><td>
 *   Set explicitly a {@linkplain MathTransform math transform} to use for
 *   coordinate transformations. This instruction is a more direct alternative to the usage of
 *   {@code source crs} and {@code target crs} instruction.</td></tr>
 *
 *   <tr><td nowrap valign="top">{@code source crs = } <var>wkt</var></td><td>
 *   Set the source {@linkplain CoordinateReferenceSystem coordinate reference
 *   system} to the specified object. This object can be specified as a Well Know Text
 *   (<var>wkt</var>) or as a shortcut previously set.</td></tr>
 *
 *   <tr><td nowrap valign="top">{@code target crs = } <var>wkt</var></td><td>
 *   Set the target {@linkplain CoordinateReferenceSystem coordinate reference
 *   system} to the specified object. This object can be specified as a Well Know Text
 *   (<var>wkt</var>) or as a shortcut previously set. Once both source and target
 *   CRS are specified a {@linkplain MathTransform math transform} from source to
 *   target CRS is automatically infered.</td></tr>
 *
 *   <tr><td nowrap valign="top">{@code source pt = } <var>coord</var></td><td>
 *   Transforms the specified coordinates from source CRS to target CRS
 *   and prints the result.</td></tr>
 *
 *   <tr><td nowrap valign="top">{@code target pt = } <var>coord</var></td><td>
 *   Inverse transforms the specified coordinates from target CRS to source CRS
 *   and prints the result.</td></tr>
 *
 *   <tr><td nowrap valign="top">{@code test tolerance = } <var>vector</var></td><td>
 *   Set the maximum difference between the transformed source point and the
 *   target point. Once this value is set, every occurence of the {@code target pt} instruction
 *   will trig this comparaison. If a greater difference is found, an exception is thrown or a
 *   message is printed to the error stream.</td></tr>
 *
 *   <tr><td nowrap valign="top">{@code print set}</td><td>
 *   Prints the set of shortcuts defined in previous calls to {@code SET} instruction.</td></tr>
 *
 *   <tr><td nowrap valign="top">{@code print crs}</td><td>
 *   Prints the source and target {@linkplain CoordinateReferenceSystem coordinate reference system}
 *   {@linkplain MathTransform math transform} and its inverse as Well Know Text (wkt).</td></tr>
 *
 *   <tr><td nowrap valign="top">{@code print pts}</td><td>
 *   Prints the source and target points, their transformed points, and the distance between
 *   them.</td></tr>
 *
 *   <tr><td nowrap valign="top">{@code exit}</td><td>
 *   Quit the console.</td></tr>
 * </table>
 *
 * @since 2.1
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class Console extends AbstractConsole {
    /** The locale for number parser. */
    private final Locale locale = Locale.US;

    /** The number format to use for reading coordinate points. */
    private final NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);

    /**
     * The number separator in vectors. Usually {@code ,}, but could also be {@code ;} if the coma is already used as
     * the decimal separator.
     */
    private final String numberSeparator;

    /** The coordinate operation factory to use. */
    private final CoordinateOperationFactory factory = ReferencingFactoryFinder.getCoordinateOperationFactory(null);

    /** The source and target CRS, or {@code null} if not yet determined. */
    private CoordinateReferenceSystem sourceCRS, targetCRS;

    /** Source and target coordinate points, or {@code null} if not yet determined. */
    private Position sourcePosition, targetPosition;

    /** The math transform, or {@code null} if not yet determined. */
    private MathTransform transform;

    /**
     * The tolerance value. If non-null, the difference between the computed and the specified target point will be
     * compared against this tolerance threshold. If it is greater, a message will be printed.
     */
    private double[] tolerance;

    /**
     * The last error thats occured while processing an instruction. Used in order to print the stack trace on request.
     */
    private transient Exception lastError;

    /**
     * Creates a new console instance using {@linkplain System#in standard input stream}, {@linkplain System#out
     * standard output stream}, {@linkplain System#err error output stream} and the system default line separator.
     */
    public Console() {
        super(new Preprocessor(new Parser()));
        numberSeparator = getNumberSeparator(numberFormat);
    }

    /**
     * Creates a new console instance using the specified input stream.
     *
     * @param in The input stream.
     */
    public Console(final LineNumberReader in) {
        super(new Preprocessor(new Parser()), in);
        numberSeparator = getNumberSeparator(numberFormat);
    }

    /**
     * Returns the character to use as a number separator. As a side effect, this method also adjust the minimum and
     * maximum digits.
     */
    private static String getNumberSeparator(final NumberFormat numberFormat) {
        numberFormat.setGroupingUsed(false);
        numberFormat.setMinimumFractionDigits(6);
        numberFormat.setMaximumFractionDigits(6);
        if (numberFormat instanceof DecimalFormat) {
            final char decimalSeparator =
                    ((DecimalFormat) numberFormat).getDecimalFormatSymbols().getDecimalSeparator();
            if (decimalSeparator == ',') {
                return ";";
            }
        }
        return ",";
    }

    /**
     * Run the console from the command line. Before to process all instructions from the {@linkplain System#in standard
     * input stream}, this method first process the following optional command-line arguments:
     *
     * <p>
     *
     * <TABLE CELLPADDING='0' CELLSPACING='0'>
     *   <TR><TD NOWRAP><CODE>-load</CODE> <VAR>&lt;filename&gt;</VAR></TD>
     *       <TD>&nbsp;Load a definition file before to run instructions from
     *           the standard input stream.</TD></TR>
     *   <TR><TD NOWRAP><CODE>-encoding</CODE> <VAR>&lt;code&gt;</VAR></TD>
     *       <TD>&nbsp;Set the character encoding.</TD></TR>
     *   <TR><TD NOWRAP><CODE>-locale</CODE> <VAR>&lt;language&gt;</VAR></TD>
     *       <TD>&nbsp;Set the language for the output (e.g. "fr" for French).</TD></TR>
     * </TABLE>
     *
     * @param args the command line arguments
     */
    @SuppressWarnings({"PMD.SystemPrintln", "PMD.CloseResource"})
    public static void main(String... args) {
        final Arguments arguments = new Arguments(args);
        final String load = arguments.getOptionalString("-load");
        final String file = arguments.getOptionalString("-file");
        arguments.getRemainingArguments(0);
        Locale.setDefault(arguments.locale);
        final LineNumberReader input;
        final Console console;
        /*
         * The usual way to execute instructions from a file is to redirect the standard input
         * stream using the standard DOS/Unix syntax (e.g. "< thefile.txt").  However, we also
         * accept a "-file" argument for the same purpose. It is easier to debug. On DOS system,
         * it also use the system default encoding instead of the command-line one.
         */
        if (file == null) {
            input = null;
            console = new Console();
        } else
            try {
                input = new LineNumberReader(new FileReader(file, StandardCharsets.UTF_8));
                console = new Console(input);
                console.setPrompt(null);
            } catch (IOException exception) {
                System.err.println(exception.getLocalizedMessage());
                return;
            }
        /*
         * Load predefined shorcuts. The file must be in the form "name = WKT". An example
         * of such file is the property file used by the property-based authority factory.
         */
        if (load != null)
            try {
                final LineNumberReader in = new LineNumberReader(new FileReader(load, StandardCharsets.UTF_8));
                try {
                    console.loadDefinitions(in);
                } catch (ParseException exception) {
                    console.reportError(exception);
                    in.close();
                    return;
                }
                in.close();
            } catch (IOException exception) {
                console.reportError(exception);
                return;
            }
        /*
         * Run all instructions and close the stream if it was a file one.
         */
        console.run();
        if (input != null)
            try {
                input.close();
            } catch (IOException exception) {
                console.reportError(exception);
            }
    }

    /**
     * Execute the specified instruction.
     *
     * @param instruction The instruction to execute.
     * @throws IOException if an I/O operation failed while writting to the {@linkplain #out output stream}.
     * @throws ParseException if a line can't be parsed.
     * @throws FactoryException If a transform can't be created.
     * @throws TransformException if a transform failed.
     */
    @Override
    protected void execute(String instruction)
            throws IOException, ParseException, FactoryException, TransformException {
        String value = null;
        int i = instruction.indexOf('=');
        if (i >= 0) {
            value = instruction.substring(i + 1).trim();
            instruction = instruction.substring(0, i).trim();
        }
        final StringTokenizer keywords = new StringTokenizer(instruction);
        if (keywords.hasMoreTokens()) {
            final String key0 = keywords.nextToken();
            if (!keywords.hasMoreTokens()) {
                // -------------------------------
                //   exit
                // -------------------------------
                if (key0.equalsIgnoreCase("exit")) {
                    if (value != null) {
                        throw unexpectedArgument("exit");
                    }
                    stop();
                    return;
                }
                // -------------------------------
                //   stacktrace
                // -------------------------------
                if (key0.equalsIgnoreCase("stacktrace")) {
                    if (value != null) {
                        throw unexpectedArgument("stacktrace");
                    }
                    if (lastError != null) {
                        java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", lastError);
                    }
                    return;
                }
                // -------------------------------
                //   transform = <the transform>
                // -------------------------------
                if (key0.equalsIgnoreCase("transform")) {
                    transform = (MathTransform) parseObject(value, MathTransform.class);
                    sourceCRS = null;
                    targetCRS = null;
                    return;
                }
            } else {
                final String key1 = keywords.nextToken();
                if (!keywords.hasMoreTokens()) {
                    // -------------------------------
                    //   print definition|crs|points
                    // -------------------------------
                    if (key0.equalsIgnoreCase("print")) {
                        if (value != null) {
                            throw unexpectedArgument("print");
                        }
                        if (key1.equalsIgnoreCase("set")) {
                            printDefinitions();
                            return;
                        }
                        if (key1.equalsIgnoreCase("crs")) {
                            printCRS();
                            return;
                        }
                        if (key1.equalsIgnoreCase("pts")) {
                            printPts();
                            return;
                        }
                    }
                    // -------------------------------
                    //   set <name> = <wkt>
                    // -------------------------------
                    if (key0.equalsIgnoreCase("set")) {
                        addDefinition(key1, value);
                        return;
                    }
                    // -------------------------------
                    //   test tolerance = <vector>
                    // -------------------------------
                    if (key0.equalsIgnoreCase("test")) {
                        if (key1.equalsIgnoreCase("tolerance")) {
                            tolerance = parseVector(value);
                            return;
                        }
                    }
                    // -------------------------------
                    //   source|target crs = <wkt>
                    // -------------------------------
                    if (key1.equalsIgnoreCase("crs")) {
                        if (key0.equalsIgnoreCase("source")) {
                            sourceCRS = (CoordinateReferenceSystem) parseObject(value, CoordinateReferenceSystem.class);
                            transform = null;
                            return;
                        }
                        if (key0.equalsIgnoreCase("target")) {
                            targetCRS = (CoordinateReferenceSystem) parseObject(value, CoordinateReferenceSystem.class);
                            transform = null;
                            return;
                        }
                    }
                    // -------------------------------
                    //   source|target pt = <coords>
                    // -------------------------------
                    if (key1.equalsIgnoreCase("pt")) {
                        if (key0.equalsIgnoreCase("source")) {
                            sourcePosition = new GeneralPosition(parseVector(value));
                            return;
                        }
                        if (key0.equalsIgnoreCase("target")) {
                            targetPosition = new GeneralPosition(parseVector(value));
                            if (tolerance != null && sourcePosition != null) {
                                update();
                                if (transform != null) {
                                    test();
                                }
                            }
                            return;
                        }
                    }
                }
            }
        }
        throw new ParseException(MessageFormat.format(ErrorKeys.ILLEGAL_INSTRUCTION_$1, instruction), 0);
    }

    /** Executes the "{@code print crs}" instruction. */
    @SuppressWarnings("PMD.CloseResource")
    private void printCRS() throws FactoryException, IOException {
        final Locale locale = null;
        final Vocabulary resources = Vocabulary.getResources(locale);
        final TableWriter table = new TableWriter(out, TableWriter.SINGLE_VERTICAL_LINE);
        table.setMultiLinesCells(true);
        char separator = TableWriter.SINGLE_HORIZONTAL_LINE;
        if (sourceCRS != null || targetCRS != null) {
            table.writeHorizontalSeparator();
            table.write(resources.getString(VocabularyKeys.SOURCE_CRS));
            table.nextColumn();
            table.write(resources.getString(VocabularyKeys.TARGET_CRS));
            table.nextLine();
            table.writeHorizontalSeparator();
            if (sourceCRS != null) {
                table.write(parser.format(sourceCRS));
            }
            table.nextColumn();
            if (targetCRS != null) {
                table.write(parser.format(targetCRS));
            }
            table.nextLine();
            separator = TableWriter.DOUBLE_HORIZONTAL_LINE;
        }
        /*
         * Format the math transform and its inverse, if any.
         */
        update();
        if (transform != null) {
            table.nextLine(separator);
            table.write(resources.getString(VocabularyKeys.MATH_TRANSFORM));
            table.nextColumn();
            table.write(resources.getString(VocabularyKeys.INVERSE_TRANSFORM));
            table.nextLine();
            table.writeHorizontalSeparator();
            table.write(parser.format(transform));
            table.nextColumn();
            try {
                table.write(parser.format(transform.inverse()));
            } catch (NoninvertibleTransformException exception) {
                table.write(exception.getLocalizedMessage());
            }
            table.nextLine();
        }
        table.writeHorizontalSeparator();
        table.flush();
    }

    /**
     * Print the source and target point, and their transforms.
     *
     * @throws FactoryException if the transform can't be computed.
     * @throws TransformException if a transform failed.
     * @throws IOException if an error occured while writing to the output stream.
     */
    @SuppressWarnings("PMD.CloseResource")
    private void printPts() throws FactoryException, TransformException, IOException {
        update();
        Position transformedSource = null;
        Position transformedTarget = null;
        String targetException = null;
        if (transform != null) {
            if (sourcePosition != null) {
                transformedSource = transform.transform(sourcePosition, null);
            }
            if (targetPosition != null)
                try {
                    transformedTarget = transform.inverse().transform(targetPosition, null);
                } catch (NoninvertibleTransformException exception) {
                    targetException = exception.getLocalizedMessage();
                    if (sourcePosition != null) {
                        final GeneralPosition p;
                        transformedTarget = p = new GeneralPosition(sourcePosition.getDimension());
                        Arrays.fill(p.ordinates, Double.NaN);
                    }
                }
        }
        final Locale locale = null;
        final Vocabulary resources = Vocabulary.getResources(locale);
        final TableWriter table = new TableWriter(out, 0);
        table.setMultiLinesCells(true);
        table.writeHorizontalSeparator();
        table.setAlignment(TableWriter.ALIGN_RIGHT);
        if (sourcePosition != null) {
            table.write(resources.getLabel(VocabularyKeys.SOURCE_POINT));
            print(sourcePosition, table);
            print(transformedSource, table);
            table.nextLine();
        }
        if (targetPosition != null) {
            table.write(resources.getLabel(VocabularyKeys.TARGET_POINT));
            print(transformedTarget, table);
            print(targetPosition, table);
            table.nextLine();
        }
        if (sourceCRS != null && targetCRS != null) {
            table.write(resources.getLabel(VocabularyKeys.DISTANCE));
            printDistance(sourceCRS, sourcePosition, transformedTarget, table);
            printDistance(targetCRS, targetPosition, transformedSource, table);
            table.nextLine();
        }
        table.writeHorizontalSeparator();
        table.flush();
        if (targetException != null) {
            out.write(targetException);
            out.write(lineSeparator);
        }
    }

    /**
     * Print the specified point to the specified table. This helper method is for use by {@link #printPts}.
     *
     * @param point The point to print, or {@code null} if none.
     * @throws IOException if an error occured while writting to the output stream.
     */
    private void print(final Position point, final TableWriter table) throws IOException {
        if (point != null) {
            table.nextColumn();
            table.write("  (");
            final double[] coords = point.getCoordinate();
            for (int i = 0; i < coords.length; i++) {
                if (i != 0) {
                    table.write(", ");
                }
                table.nextColumn();
                table.write(numberFormat.format(coords[i]));
            }
            table.write(')');
        }
    }

    /** Print the distance between two points using the specified CRS. */
    private void printDistance(
            final CoordinateReferenceSystem crs,
            final Position position1,
            final Position position2,
            final TableWriter table)
            throws IOException {
        if (position1 == null) {
            // Note: 'position2' is checked below, *after* blank columns insertion.
            return;
        }
        for (int i = crs.getCoordinateSystem().getDimension(); --i >= 0; ) {
            table.nextColumn();
        }
        if (position2 != null) {
            if (crs instanceof AbstractCRS)
                try {
                    final Measure distance =
                            ((AbstractCRS) crs).distance(position1.getCoordinate(), position2.getCoordinate());
                    table.setAlignment(TableWriter.ALIGN_RIGHT);
                    table.write(numberFormat.format(distance.doubleValue()));
                    table.write("  ");
                    table.nextColumn();
                    table.write(String.valueOf(UnitFormat.getInstance().format(distance.getUnit())));
                    table.setAlignment(TableWriter.ALIGN_LEFT);
                    return;
                } catch (UnsupportedOperationException ignore) {
                    /*
                     * Underlying CRS do not supports distance computation.
                     * Left the column blank.
                     */
                }
        }
        table.nextColumn();
    }

    ///////////////////////////////////////////////////////////
    ////////                                           ////////
    ////////        H E L P E R   M E T H O D S        ////////
    ////////                                           ////////
    ///////////////////////////////////////////////////////////

    /**
     * Invoked automatically when the {@code target pt} instruction were executed and a {@code test tolerance} were
     * previously set. The default implementation compares the transformed source point with the expected target point.
     * If a mismatch greater than the tolerance error is found, an exception is thrown. Subclasses may overrides this
     * method in order to performs more tests.
     *
     * @throws TransformException if the source point can't be transformed, or a mistmatch is found.
     * @throws MismatchedDimensionException if the transformed source point doesn't have the expected dimension.
     */
    protected void test() throws TransformException, MismatchedDimensionException {
        final Position transformedSource = transform.transform(sourcePosition, null);
        final int sourceDim = transformedSource.getDimension();
        final int targetDim = targetPosition.getDimension();
        if (sourceDim != targetDim) {
            throw new MismatchedDimensionException(
                    MessageFormat.format(ErrorKeys.MISMATCHED_DIMENSION_$2, sourceDim, targetDim));
        }
        for (int i = 0; i < sourceDim; i++) {
            // Use '!' for catching NaN.
            if (!(Math.abs(transformedSource.getOrdinate(i) - targetPosition.getOrdinate(i))
                    <= tolerance[Math.min(i, tolerance.length - 1)])) {
                throw new TransformException("Expected " + targetPosition + " but got " + transformedSource);
            }
        }
    }

    /**
     * Check if the specified string start and end with the specified delimitors, and returns the string without the
     * delimitors.
     *
     * @param text The string to check.
     * @param start The delimitor required at the string begining.
     * @param end The delimitor required at the string end.
     */
    private static String removeDelimitors(String text, final char start, final char end) {
        text = text.trim();
        final int endPos = text.length() - 1;
        if (endPos >= 1) {
            if (text.charAt(0) == start && text.charAt(endPos) == end) {
                text = text.substring(1, endPos).trim();
            }
        }
        return text;
    }

    /**
     * Parse a vector of values. Vectors are used for coordinate points. Example:
     *
     * <pre>
     * (46.69439222, 13.91405611, 41.21)
     * </pre>
     *
     * @param text The vector to parse.
     * @return The vector as floating point numbers.
     * @throws ParseException if a number can't be parsed.
     */
    private double[] parseVector(String text) throws ParseException {
        text = removeDelimitors(text, '(', ')');
        final StringTokenizer st = new StringTokenizer(text, numberSeparator);
        final double[] values = new double[st.countTokens()];
        for (int i = 0; i < values.length; i++) {
            // Note: we need to convert the number to upper-case because
            //       NumberParser seems to accepts "1E-10" but not "1e-10".
            final String token = st.nextToken().trim().toUpperCase(locale);
            final ParsePosition position = new ParsePosition(0);
            final Number result = numberFormat.parse(token, position);
            if (position.getIndex() != token.length()) {
                throw new ParseException(
                        MessageFormat.format(ErrorKeys.UNPARSABLE_NUMBER_$1, token), position.getErrorIndex());
            }
            values[i] = result.doubleValue();
        }
        return values;
    }

    /**
     * Update the internal state after a change, before to apply transformation. The most important change is to update
     * the math transform, if needed.
     */
    private void update() throws FactoryException {
        if (transform == null && sourceCRS != null && targetCRS != null) {
            transform = factory.createOperation(sourceCRS, targetCRS).getMathTransform();
        }
    }

    /**
     * Constructs an exception saying that an argument was unexpected.
     *
     * @param instruction The instruction name.
     * @return The exception to throws.
     */
    private static ParseException unexpectedArgument(final String instruction) {
        return new ParseException(
                MessageFormat.format(ErrorKeys.UNEXPECTED_ARGUMENT_FOR_INSTRUCTION_$1, instruction), 0);
    }

    /**
     * {@inheritDoc}
     *
     * @param exception The exception to report.
     */
    @Override
    protected void reportError(final Exception exception) {
        super.reportError(exception);
        lastError = exception;
    }
}
