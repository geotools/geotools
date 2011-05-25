/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.console;

import java.util.Map;
import java.util.TreeMap;
import java.io.PrintWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Locale;
import org.geotools.io.TableWriter;
import org.geotools.resources.Classes;
import org.geotools.resources.Arguments;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * Base class for command line tools. Subclasses define fields annotated with {@link Option},
 * while will be initialized automatically by the constructor. The following options are
 * automatically recognized by this class:
 * <p>
 * <table>
 *   <tr><td>{@code -encoding} </td><td>&nbsp;Set the input and output encoding.</td></tr>
 *   <tr><td>{@code -help}     </td><td>&nbsp;Print the {@linkplain #help help} summary.</td></tr>
 *   <tr><td>{@code -locale}   </td><td>&nbsp;Set the locale for string, number and date formatting.</td></tr>
 * </table>
 *
 * @since 2.5
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 * @author Cédric Briançon
 */
public class CommandLine {
    /**
     * The prefix to prepend to option names.
     */
    private static final String OPTION_PREFIX = "--";

    // There is no clear convention on exit code, except 0 == SUCCES.
    // However a typical usage is to use higher values for more sever causes.

    /**
     * The code given to {@link System#exit} when the program failed because of an illegal
     * user argument.
     */
    public static final int ILLEGAL_ARGUMENT_EXIT_CODE = 1;

    /**
     * The code given to {@link System#exit} when the program aborted at user request.
     */
    public static final int ABORT_EXIT_CODE = 2;

    /**
     * The code given to {@link System#exit} when the program failed because of bad
     * content in a file.
     */
    public static final int BAD_CONTENT_EXIT_CODE = 3;

    /**
     * The code given to {@link System#exit} when the program failed because of an
     * {@link java.io.IOException}.
     */
    public static final int IO_EXCEPTION_EXIT_CODE = 100;

    /**
     * The code given to {@link System#exit} when the program failed because of a
     * {@link java.sql.SQLException}.
     */
    public static final int SQL_EXCEPTION_EXIT_CODE = 101;

    /**
     * Output stream to the console. This output stream may use the encoding
     * specified by the {@code "-encoding"} argument, if presents.
     */
    protected final PrintWriter out;

    /**
     * Error stream to the console.
     */
    protected final PrintWriter err;

    /**
     * The locale inferred from the {@code "-locale"} option. If no such option was
     * provided, then this field is set to the {@linkplain Locale#getDefault default locale}.
     */
    protected final Locale locale;

    /**
     * The remaining arguments after all option values have been assigned to the fields.
     */
    protected final String[] arguments;

    /**
     * Creates a new {@code CommandLine} instance from the given arguments. This constructor
     * expects no additional argument after the one annoted as {@linkplain Option}.
     *
     * @param args The command-line arguments.
     */
    protected CommandLine(final String[] args) {
        this(args, 0);
    }

    /**
     * Creates a new {@code CommandLine} instance from the given arguments. If this constructor
     * fails because of a programming error (for example a type not handled by {@link #parse
     * parse} method), then an exception is thrown like usual. If this constructor fails because
     * of some user error (e.g. if a mandatory argument is not provided) or some other external
     * conditions (e.g. an {@link IOException}), then it prints a short error message and invokes
     * {@link System#exit} with one the {@code EXIT_CODE} constants.
     *
     * @param  args The command-line arguments.
     * @param  maximumRemaining The maximum number of arguments that may remain after processing
     *         of annotated fields. This is the maximum length of the {@link #arguments} array.
     *         The default value is 0.
     */
    protected CommandLine(final String[] args, final int maximumRemaining) {
        final Arguments arguments = new Arguments(args);
        out    = arguments.out;
        err    = arguments.err;
        locale = arguments.locale;
        if (arguments.getFlag(OPTION_PREFIX + "help")) {
            help();
            System.exit(0);
        }
        setArgumentValues(getClass(), arguments);
        this.arguments = arguments.getRemainingArguments(maximumRemaining, OPTION_PREFIX.charAt(0));
    }

    /**
     * Sets the argument values for the fields of the given class.
     * The parent classes are processed before the given class.
     *
     * @throws UnsupportedOperationException if a field can not be set.
     */
    private void setArgumentValues(final Class<?> classe, final Arguments arguments)
            throws UnsupportedOperationException
    {
        final Class<?> parent = classe.getSuperclass();
        if (!CommandLine.class.equals(parent)) {
            setArgumentValues(parent, arguments);
        }
        for (final Field field : classe.getDeclaredFields()) {
            final Option option = field.getAnnotation(Option.class);
            if (option == null) {
                continue;
            }
            final boolean mandatory = option.mandatory();
            final Class<?> type = field.getType();
            String name = option.name().trim();
            if (name.length() == 0) {
                name = field.getName();
            }
            name = OPTION_PREFIX + name;
            final Object value;
            if (Boolean.class.isAssignableFrom(type) || Boolean.TYPE.equals(type)) {
                if (mandatory) {
                    value = arguments.getRequiredBoolean(name);
                } else {
                    value = arguments.getFlag(name);
                }
            } else if (Integer.class.isAssignableFrom(type) || Integer.TYPE.equals(type)) {
                if (mandatory) {
                    value = arguments.getRequiredInteger(name);
                } else {
                    value = arguments.getOptionalInteger(name);
                }
            } else if (Double.class.isAssignableFrom(type) || Double.TYPE.equals(type)) {
                if (mandatory) {
                    value = arguments.getRequiredDouble(name);
                } else {
                    value = arguments.getOptionalDouble(name);
                }
            } else if (String.class.isAssignableFrom(type)) {
                if (mandatory) {
                    value = arguments.getRequiredString(name);
                } else {
                    value = arguments.getOptionalString(name);
                }
            } else {
                final String text;
                if (mandatory) {
                    text = arguments.getRequiredString(name);
                } else {
                    text = arguments.getOptionalString(name);
                }
                value = parse(type, text);
            }
            field.setAccessible(true);
            try {
                field.set(this, value);
            } catch (IllegalAccessException e) {
                throw new UnsupportedOperationException(e);
            }
        }
    }

    /**
     * Parses the given string as a value of the given type. This method is invoked automatically
     * for values that are not of one of the pre-defined types. The default implementation thrown
     * an exception in all cases.
     *
     * @param  <T> The field type.
     * @param  type The field type.
     * @param  value The value given on the command line.
     * @return The value for the given string to parse.
     * @throws UnsupportedOperationException if the value can't be parsed.
     */
    protected <T> T parse(final Class<T> type, final String value) throws UnsupportedOperationException {
        throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNKNOW_TYPE_$1, type));
    }

    /**
     * Gets the arguments for the given class. The arguments are added in the given set.
     *
     * @param classe    The class to parse for arguments.
     * @param mantatory The set where to put mandatory arguments.
     * @param optional  The set where to put optional arguments.
     */
    private void getArguments(final Class<?> classe,
                              final Map<String,String> mandatory,
                              final Map<String,String> optional) {
        final Class<?> parent = classe.getSuperclass();
        if (!CommandLine.class.equals(parent)) {
            getArguments(parent, mandatory, optional);
        }
        for (final Field field : classe.getDeclaredFields()) {
            final Option option = field.getAnnotation(Option.class);
            if (option == null) {
                continue;
            }
            String description = option.description().trim();
            if (description.length() != 0) {
                String name = option.name().trim();
                if (name.length() == 0) {
                    name = field.getName();
                }
                final Class<?> type = Classes.primitiveToWrapper(field.getType());
                if (Number.class.isAssignableFrom(type)) {
                    name = name + "=N";
                } else if (!Boolean.class.isAssignableFrom(type)) {
                    name = name + "=S";
                }
                if (option.mandatory()) {
                    mandatory.put(name, description);
                } else {
                    optional.put(name, description);
                }
            }
        }
    }

    /**
     * Prints a description of all arguments to the {@linkplain #out standard output}.
     * This method is invoked automatically if the user provided the {@code --help}
     * argument on the command line. Subclasses can override this method in order to
     * prints a summary before the option list.
     */
    protected void help() {
        final Map<String,String> mandatory = new TreeMap<String,String>();
        final Map<String,String> optional  = new TreeMap<String,String>();
        optional.put("help",       "Print this summary.");
        optional.put("locale=S",   "Set the locale for string, number and date formatting. Examples: \"fr\", \"fr_CA\".");
        optional.put("encoding=S", "Set the input and output encoding. Examples: \"UTF-8\", \"ISO-8859-1\".");
        getArguments(getClass(), mandatory, optional);
        if (!mandatory.isEmpty()) {
            out.println("Mandatory arguments:");
            print(mandatory);
        }
        out.println("Optional arguments:");
        print(optional);
    }

    /**
     * Prints the specified options to the standard output stream.
     */
    private void print(final Map<String,String> options) {
        final TableWriter table = new TableWriter(out, "  ");
        for (final Map.Entry<String,String> entry : options.entrySet()) {
            table.write("  ");
            table.write(OPTION_PREFIX);
            table.write(entry.getKey());
            table.nextColumn();
            table.write(entry.getValue());
            table.nextLine();
        }
        try {
            table.flush();
        } catch (IOException e) {
            // Should never happen since we are flushing to a PrintWriter.
            throw new AssertionError(e);
        }
    }
}
