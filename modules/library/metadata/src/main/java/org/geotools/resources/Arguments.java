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
package org.geotools.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Locale;
import java.util.regex.Pattern;

import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;


/**
 * A helper class for parsing command-line arguments. Instance of this class
 * are usually created inside {@code main} methods. For example:
 *
 * <blockquote><pre>
 * public static void main(String[] args) {
 *     Arguments arguments = new Arguments(args);
 * }
 * </pre></blockquote>
 *
 * Then, method likes {@link #getRequiredString} or {@link #getOptionalString} can be used.
 * If a parameter is badly formatted or if a required parameter is not presents, then the
 * method {@link #illegalArgument} will be invoked with a message that describes the error.
 * The default implementation print the localized error message to standard output {@link #out}
 * and exits the virtual machine with a call to {@link System#exit} with error code 1.
 *
 * @since 2.0
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class Arguments {
    /**
     * The code given to {@link System#exit} when this class exits because of an illegal
     * user argument.
     *
     * @deprecated Moved to {@link org.geotools.console.CommandLine}.
     */
    @Deprecated
    public static final int ILLEGAL_ARGUMENT_EXIT_CODE = 1;

    /**
     * Command-line arguments. Elements are set to
     * {@code null} after they have been processed.
     */
    private final String[] arguments;

    /**
     * Output stream to the console. This output stream may use encoding
     * specified in the {@code "-encoding"} argument, if presents.
     */
    public final PrintWriter out;

    /**
     * Error stream to the console.
     */
    public final PrintWriter err;

    /**
     * The locale. Locale will be fetch from the {@code "-locale"}
     * argument, if presents. Otherwise, the default locale will be used.
     */
    public final Locale locale;

    /**
     * The encoding, or {@code null} for the platform default.
     */
    private final String encoding;

    /**
     * Constructs a set of arguments.
     *
     * @param args Command line arguments. Arguments {@code "-encoding"} and {@code "-locale"}
     *             will be automatically parsed.
     */
    public Arguments(final String[] args) {
        this.arguments  = args.clone();
        this.locale     = getLocale(getOptionalString("-locale"));
        this.encoding   = getOptionalString("-encoding");
        PrintWriter out = null;
        Exception error = null;
        if (encoding != null) try {
            out = new PrintWriter(new OutputStreamWriter(System.out, encoding), true);
        } catch (IOException exception) {
            error = exception;
        }
        if (out == null) {
            out = getPrintWriter(System.out);
        }
        this.out = out;
        this.err = getPrintWriter(System.err);
        if (error != null) {
            illegalArgument(error);
        }
    }

    /**
     * Returns the specified locale.
     *
     * @param  locale The programmatic locale string (e.g. "fr_CA").
     * @return The locale, or the default one if {@code locale} was null.
     * @throws IllegalArgumentException if the locale string is invalid.
     */
    private Locale getLocale(final String locale) throws IllegalArgumentException {
        if (locale != null) {
            final String[] s = Pattern.compile("_").split(locale);
            switch (s.length) {
                case 1:  return new Locale(s[0]);
                case 2:  return new Locale(s[0], s[1]);
                case 3:  return new Locale(s[0], s[1], s[2]);
                default: illegalArgument(new IllegalArgumentException(Errors.format(
                                         ErrorKeys.BAD_LOCALE_$1, locale)));
            }
        }
        return Locale.getDefault();
    }

    /**
     * Returns an optional string value from the command line. This method should be called
     * exactly once for each parameter. Second invocation for the same parameter will returns
     * {@code null}, unless the same parameter appears many times on the command line.
     * <p>
     * Paramater may be instructions like "-encoding cp850" or "-encoding=cp850".
     * Both forms (with or without "=") are accepted. Spaces around the '=' character,
     * if any, are ignored.
     *
     * @param  name The parameter name (e.g. "-encoding"). Name are case-insensitive.
     * @return The parameter value, of {@code null} if there is no parameter
     *         given for the specified name.
     */
    public String getOptionalString(final String name) {
        for (int i=0; i<arguments.length; i++) {
            String arg = arguments[i];
            if (arg != null) {
                arg = arg.trim();
                String value = "";
                int split = arg.indexOf('=');
                if (split >= 0) {
                    value = arg.substring(split+1).trim();
                    arg = arg.substring(0, split).trim();
                }
                if (arg.equalsIgnoreCase(name)) {
                    arguments[i] = null;
                    if (value.length() != 0) {
                        return value;
                    }
                    while (++i < arguments.length) {
                        value = arguments[i];
                        arguments[i] = null;
                        if (value==null) {
                            break;
                        }
                        value = value.trim();
                        if (split>=0) {
                            return value;
                        }
                        if (!value.equals("=")) {
                            return value.startsWith("=") ? value.substring(1).trim() : value;
                        }
                        split = 0;
                    }
                    illegalArgument(new IllegalArgumentException(Errors.getResources(locale).
                                    getString(ErrorKeys.MISSING_PARAMETER_VALUE_$1, arg)));
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * Returns an required string value from the command line. This method
     * works like {@link #getOptionalString}, except that it will invokes
     * {@link #illegalArgument} if the specified parameter was not given
     * on the command line.
     *
     * @param  name The parameter name. Name are case-insensitive.
     * @return The parameter value.
     */
    public String getRequiredString(final String name) {
        final String value = getOptionalString(name);
        if (value == null) {
            illegalArgument(new IllegalArgumentException(Errors.getResources(locale).
                            getString(ErrorKeys.MISSING_PARAMETER_$1, name)));
        }
        return value;
    }

    /**
     * Returns an optional integer value from the command line. Numbers are parsed as
     * of the {@link Integer#parseInt(String)} method,  which means that the parsing
     * is locale-insensitive. Locale insensitive parsing is required in order to use
     * arguments in portable scripts.
     *
     * @param  name The parameter name. Name are case-insensitive.
     * @return The parameter value, of {@code null} if there is no parameter
     *         given for the specified name.
     */
    public Integer getOptionalInteger(final String name) {
        final String value = getOptionalString(name);
        if (value != null) try {
            return Integer.valueOf(value);
        } catch (NumberFormatException exception) {
            illegalArgument(exception);
        }
        return null;
    }

    /**
     * Returns a required integer value from the command line. Numbers are parsed as
     * of the {@link Integer#parseInt(String)} method,  which means that the parsing
     * is locale-insensitive. Locale insensitive parsing is required in order to use
     * arguments in portable scripts.
     *
     * @param  name The parameter name. Name are case-insensitive.
     * @return The parameter value.
     */
    public int getRequiredInteger(final String name) {
        final String value = getRequiredString(name);
        if (value != null) try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            illegalArgument(exception);
        }
        return 0;
    }

    /**
     * Returns an optional floating-point value from the command line. Numbers are parsed
     * as of the {@link Double#parseDouble(String)} method,  which means that the parsing
     * is locale-insensitive. Locale insensitive parsing is required in order to use
     * arguments in portable scripts.
     *
     * @param  name The parameter name. Name are case-insensitive.
     * @return The parameter value, of {@code null} if there is no parameter
     *         given for the specified name.
     */
    public Double getOptionalDouble(final String name) {
        final String value = getOptionalString(name);
        if (value != null) try {
            return Double.valueOf(value);
        } catch (NumberFormatException exception) {
            illegalArgument(exception);
        }
        return null;
    }

    /**
     * Returns a required floating-point value from the command line. Numbers are parsed
     * as of the {@link Double#parseDouble(String)} method, which means that the parsing
     * is locale-insensitive. Locale insensitive parsing is required in order to use
     * arguments in portable scripts.
     *
     * @param  name The parameter name. Name are case-insensitive.
     * @return The parameter value.
     */
    public double getRequiredDouble(final String name) {
        final String value = getRequiredString(name);
        if (value != null) try {
            return Double.parseDouble(value);
        } catch (NumberFormatException exception) {
            illegalArgument(exception);
        }
        return Double.NaN;
    }

    /**
     * Returns an optional boolean value from the command line.
     * The value, if defined, must be "true" or "false".
     *
     * @param  name The parameter name. Name are case-insensitive.
     * @return The parameter value, of {@code null} if there is no parameter
     *         given for the specified name.
     */
    public Boolean getOptionalBoolean(final String name) {
        final String value = getOptionalString(name);
        if (value != null) {
            if (value.equalsIgnoreCase("true" )) return Boolean.TRUE;
            if (value.equalsIgnoreCase("false")) return Boolean.FALSE;
            illegalArgument(new IllegalArgumentException(value));
        }
        return null;
    }

    /**
     * Returns a required boolean value from the command line.
     * The value must be "true" or "false".
     *
     * @param  name The parameter name. Name are case-insensitive.
     * @return The parameter value.
     */
    public boolean getRequiredBoolean(final String name) {
        final String value = getRequiredString(name);
        if (value != null) {
            if (value.equalsIgnoreCase("true" )) return true;
            if (value.equalsIgnoreCase("false")) return false;
            illegalArgument(new IllegalArgumentException(value));
        }
        return false;
    }

    /**
     * Returns {@code true} if the specified flag is set on the command line.
     * This method should be called exactly once for each flag. Second invocation
     * for the same flag will returns {@code false} (unless the same flag
     * appears many times on the command line).
     *
     * @param  name The flag name.
     * @return {@code true} if this flag appears on the command line, or {@code false}
     *         otherwise.
     */
    public boolean getFlag(final String name) {
        for (int i=0; i<arguments.length; i++) {
            String arg = arguments[i];
            if (arg!=null) {
                arg = arg.trim();
                if (arg.equalsIgnoreCase(name)) {
                    arguments[i] = null;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets a reader for the specified input stream.
     *
     * @param  in The input stream to wrap.
     * @return A {@link Reader} wrapping the specified input stream.
     */
    public static Reader getReader(final InputStream in) {
        if (in == System.in) {
            final Reader candidate = Java6.consoleReader();
            if (candidate != null) {
                return candidate;
            }
        }
        return new InputStreamReader(in);
    }

    /**
     * Gets a writer for the specified output stream.
     *
     * @param  out The output stream to wrap.
     * @return A {@link Writer} wrapping the specified output stream.
     */
    public static Writer getWriter(final OutputStream out) {
        if (out == System.out || out == System.err) {
            final PrintWriter candidate = Java6.consoleWriter();
            if (candidate != null) {
                return candidate;
            }
        }
        return new OutputStreamWriter(out);
    }

    /**
     * Gets a print writer for the specified print stream.
     *
     * @param  out The print stream to wrap.
     * @return A {@link PrintWriter} wrapping the specified print stream.
     */
    public static PrintWriter getPrintWriter(final PrintStream out) {
        if (out == System.out || out == System.err) {
            final PrintWriter candidate = Java6.consoleWriter();
            if (candidate != null) {
                return candidate;
            }
        }
        return new PrintWriter(out, true);
    }

    /**
     * Returns the list of unprocessed arguments. If the number of remaining arguments is
     * greater than the specified maximum, then this method invokes {@link #illegalArgument}.
     *
     * @param  max Maximum remaining arguments autorized.
     * @return An array of remaining arguments. Will never be longer than {@code max}.
     */
    public String[] getRemainingArguments(final int max) {
        int count = 0;
        final String[] left = new String[Math.min(max, arguments.length)];
        for (int i=0; i<arguments.length; i++) {
            final String arg = arguments[i];
            if (arg != null) {
                if (count >= max) {
                    illegalArgument(new IllegalArgumentException(Errors.getResources(locale).
                                    getString(ErrorKeys.UNEXPECTED_PARAMETER_$1, arguments[i])));
                }
                left[count++] = arg;
            }
        }
        return XArray.resize(left, count);
    }

    /**
     * Returns the list of unprocessed arguments, which should not begin by the specified prefix. This
     * method invokes <code>{@linkplain #getRemainingArguments(int) getRemainingArguments}(max)</code>
     * and verifies that none of the remaining arguments start with {@code forbiddenPrefix}. The
     * forbidden prefix is usually {@code '-'}, the character used for options as in
     * "{@code -locale}", <cite>etc.</cite>
     *
     * @param  max Maximum remaining arguments autorized.
     * @param  forbiddenPrefix The forbidden prefix, usually {@code '-'}.
     * @return An array of remaining arguments. Will never be longer than {@code max}.
     *
     * @since 2.4
     */
    public String[] getRemainingArguments(final int max, final char forbiddenPrefix) {
        final String[] arguments = getRemainingArguments(max);
        for (int i=0; i<arguments.length; i++) {
            String argument = arguments[i];
            if (argument != null) {
                argument = argument.trim();
                if (argument.length() != 0) {
                    if (argument.charAt(0) == forbiddenPrefix) {
                        illegalArgument(new IllegalArgumentException(Errors.getResources(locale).
                                        getString(ErrorKeys.UNKNOW_PARAMETER_$1, argument)));
                    }
                }
            }
        }
        return arguments;
    }

    /**
     * Prints a summary of the specified exception, without stack trace. This method
     * is invoked when a non-fatal (and somewhat expected) error occured, for example
     * {@link java.io.FileNotFoundException} when the file were specified in argument.
     *
     * @param exception An exception with a message describing the user's error.
     *
     * @since 2.3
     */
    public void printSummary(final Exception exception) {
        final String type = Classes.getShortClassName(exception);
        String message = exception.getLocalizedMessage();
        if (message == null) {
            message = Vocabulary.format(VocabularyKeys.NO_DETAILS_$1, type);
        } else {
            err.print(type);
            err.print(": ");
        }
        err.println(message);
        err.flush();
    }

    /**
     * Invoked when an the user has specified an illegal parameter. The default
     * implementation prints the localized error message to the standard output
     * {@link #out}, and then exit the virtual machine.  User may override this
     * method if they want a different behavior.
     * <p>
     * This method <em>is not</em> invoked when an anormal error occured (for
     * example an unexpected {@code NullPointerException} in some of developper's
     * module). If such an error occurs, the normal exception mechanism will be used.
     *
     * @param exception An exception with a message describing the user's error.
     */
    protected void illegalArgument(final Exception exception) {
        printSummary(exception);
        System.exit(ILLEGAL_ARGUMENT_EXIT_CODE);
    }
}
