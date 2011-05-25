/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util.logging;

import java.text.AttributedCharacterIterator;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LogRecord;

import org.geotools.resources.Classes;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;


/**
 * Wraps a {@link Format} object in order to either parse fully a string, or log a warning.
 * This class provides a {@link #parse} method which performs the following tasks:
 * <p>
 * <ul>
 *   <li>Checks if the string was fully parsed and log a warning if it was not. This is
 *       different than the default {@link #parseObject(String)} behavior which check only
 *       if the <em>begining</em> of the string was parsed and ignore any remaining characters.</li>
 *   <li>Ensures that the parsed object is of some specific class specified at construction time.</li>
 *   <li>If the string can't be fully parsed or is not of the expected class, logs a warning.</li>
 * </ul>
 *
 * @since 2.4
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class LoggedFormat<T> extends Format {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = 4578880360344271325L;

    /**
     * The wrapped format.
     */
    private final Format format;

    /**
     * The expected type for the parsed values.
     */
    private final Class<T> type;

    /**
     * The level to use for the messages to be logged.
     */
    private Level level;

    /**
     * The logger where to log warnings, or {@code null} if none.
     *
     * @see #setLogger
     */
    private String logger;

    /**
     * The class to declare in as the warning emitter, or {@code null} if none.
     *
     * @see #setCaller
     */
    private String className;

    /**
     * The method to declare in as the warning emitter, or {@code null} if none.
     *
     * @see #setCaller
     */
    private String methodName;

    /**
     * Creates a new format wrapping the specified one.
     *
     * @param format The format to use for parsing and formatting.
     * @param type   The expected type of parsed values.
     */
    protected LoggedFormat(final Format format, final Class<T> type) {
        this.format = format;
        this.type   = type;
        this.level  = Level.WARNING;
    }

    /**
     * Creates a new format wrapping the specified one.
     *
     * @param format The format to use for parsing and formatting.
     * @param type   The expected type of parsed values.
     */
    public static <T> LoggedFormat<T> getInstance(final Format format, final Class<T> type) {
        return new LoggedFormat<T>(format, type);
    }

    /**
     * Sets the logger where to send the warnings eventually emitted by the {@link #parse} method.
     *
     * @param logger The logger where to log warnings, or {@code null} if none.
     */
    public void setLogger(final String logger) {
        this.logger = logger;
    }

    /**
     * Sets the logger level for the warnings eventually emitted by the {@link #parse} method.
     * The default value is {@link Level#WARNING}.
     *
     * @param level The new logging level.
     *
     * @since 2.5
     */
    public void setLevel(final Level level) {
        if (level != null) {
            this.level = level;
        }
    }

    /**
     * Sets the {@linkplain LogRecord#setSourceClassName source class name} and
     * {@linkplain LogRecord#setSourceMethodName source method name} for the warnings
     * eventually emitted by the {@link #parse} method.
     *
     * @param caller The class to declare as the warning emitter, or {@code null} if none.
     * @param method The method to declare as the warning emitter, or {@code null} if none.
     */
    public void setCaller(final Class<?> caller, final String method) {
        this.className  = (caller != null) ? caller.getName() : null;
        this.methodName = method;
    }

    /**
     * Parses the specified string. If the string can't be parsed, then this method returns
     * {@code null}. If it can be parsed at least partially and is of the kind specified at
     * construction time, then it is returned. If the string has not been fully parsed, then
     * a {@linkplain LogRecord log record} is prepared and logged.
     *
     * @param  text The text to parse, or {@code null}.
     * @return The parsed object, or {@code null} if {@code text} was null or can't be parsed.
     */
    public T parse(String text) {
        if (text == null || (text=text.trim()).length() == 0) {
            return null;
        }
        final ParsePosition position = new ParsePosition(0);
        final Object value = parseObject(text, position);
        int index = position.getIndex();
        final int error = position.getErrorIndex();
        if (error >= 0 && error < index) {
            index = error;
        }
        if (index < text.length()) {
            doLogWarning(formatUnparsable(text, 0, index, getWarningLocale(), level));
        } else if (value!=null && !type.isInstance(value)) {
            doLogWarning(Errors.getResources(getWarningLocale()).getLogRecord(level,
                    ErrorKeys.ILLEGAL_CLASS_$2, value.getClass(), type));
            return null;
        }
        return type.cast(value);
    }

    /**
     * Parses text from a string to produce an object. This method delegates the work to the
     * {@linkplain Format format} specified at construction time. This method to not perform
     * any logging.
     *
     * @param  text The text to parse.
     * @return An object parsed from the string.
     * @throws ParseException if parsing failed.
     */
    @Override
    public Object parseObject(final String text) throws ParseException {
        return format.parseObject(text);
    }

    /**
     * Parses text from a string to produce an object. This method delegates the work to the
     * {@linkplain Format format} specified at construction time. This method to not perform
     * any logging.
     *
     * @param text The text to parse.
     * @param position Index and error index information.
     * @return An object parsed from the string, or {@code null} in case of error.
     */
    public Object parseObject(final String text, final ParsePosition position) {
        return format.parseObject(text, position);
    }

    /**
     * Formats the specified object. This method delegates the work to the
     * {@linkplain Format format} specified at construction time.
     *
     * @param value      The object to format.
     * @param toAppendTo The buffer where the text is to be appended.
     * @param position   Identifies a field in the formatted text.
     * @return           The string buffer passed in with formatted text appended.
     */
    public StringBuffer format(final Object value, final StringBuffer toAppendTo,
                               final FieldPosition position)
    {
        return format.format(value, toAppendTo, position);
    }

    /**
     * Formats the specified object. This method delegates the work to the
     * {@linkplain Format format} specified at construction time.
     *
     * @param value The object to format.
     * @return The character iterator describing the formatted value.
     */
    @Override
    public AttributedCharacterIterator formatToCharacterIterator(final Object value) {
        return format.formatToCharacterIterator(value);
    }

    /**
     * Logs a warning. The caller is set before to invoke the user-overridable method.
     */
    private void doLogWarning(final LogRecord warning) {
        if (className != null) {
            warning.setSourceClassName(className);
        }
        if (methodName != null) {
            warning.setSourceMethodName(methodName);
        }
        logWarning(warning);
    }

    /**
     * Logs a warning. This method is invoked automatically by the {@link #parse parse} method
     * when a text can't be fully parsed. The default implementation logs the warning to the
     * logger specified by the last call to the {@link #setLogger setLogger} method. Subclasses
     * may override this method if they want to change the log record before the logging.
     *
     * @param warning The warning to log.
     */
    protected void logWarning(final LogRecord warning) {
        if (logger != null) {
            final Logger logger = Logging.getLogger(this.logger);
            warning.setLoggerName(logger.getName());
            logger.log(warning);
        }
    }

    /**
     * Returns the locale to use for formatting warnings. The default implementation returns
     * the {@linkplain Locale#getDefault() default locale}.
     */
    protected Locale getWarningLocale() {
        return Locale.getDefault();
    }

    /**
     * Formats an error message for an unparsable string. This method performs the same work that
     * {@link #formatUnparsable(String, int, int, Locale, Level) formatUnparsable(..., Level)},
     * except that the result is returned as a {@link String} rather than a {@link LogRecord}.
     * This is provided as a convenience method for creating the message to give to an
     * {@linkplain Exception#Exception(String) exception constructor}.
     *
     * @param  text The unparsable string.
     * @param  index The parse position. This is usually {@link ParsePosition#getIndex}.
     * @param  errorIndex The index where the error occured. This is usually
     *         {@link ParsePosition#getErrorIndex}.
     * @param  locale The locale for the message, or {@code null} for the default one.
     * @return A formatted error message.
     *
     * @since 2.5
     */
    public static String formatUnparsable(final String text, final int index,
            final int errorIndex, final Locale locale)
    {
        return (String) doFormatUnparsable(text, index, errorIndex, locale, null);
    }

    /**
     * Formats a log record for an unparsable string. This method is invoked by the
     * {@link #parse parse} method for formatting the log record to be given to the
     * {@link #logWarning} method. It is made public as a convenience for implementors
     * who wish to manage loggings outside this {@code LoggedFormat} class.
     *
     * @param  text The unparsable string.
     * @param  index The parse position. This is usually {@link ParsePosition#getIndex}.
     * @param  errorIndex The index where the error occured. This is usually
     *         {@link ParsePosition#getErrorIndex}.
     * @param  locale The locale for the log message, or {@code null} for the default one.
     * @param  level The log record level.
     * @return A formatted log record.
     *
     * @since 2.5
     */
    public static LogRecord formatUnparsable(final String text, final int index,
            final int errorIndex, final Locale locale, Level level)
    {
        if (level == null) {
            // It is necessary to ensure that the level argument is non-null,
            // otherwise we would get a ClassCastException in the code below.
            level = Level.WARNING;
        }
        return (LogRecord) doFormatUnparsable(text, index, errorIndex, locale, level);
    }

    /**
     * Implementation of {@code formatUnparsable} methods. Returns a {@link LogRecord}
     * if {@code level} is non-null, or a {@link String} otherwise.
     */
    private static Object doFormatUnparsable(String text, final int index, int errorIndex,
                                             final Locale locale, final Level level)
    {
        final Errors resources = Errors.getResources(locale);
        final int length = text.length();
        if (errorIndex < index) {
            errorIndex = index;
        }
        if (errorIndex == length) {
            if (level != null) {
                return resources.getLogRecord(level, ErrorKeys.UNEXPECTED_END_OF_STRING);
            }
            return resources.getString(ErrorKeys.UNEXPECTED_END_OF_STRING);
        }
        int upper = errorIndex;
        if (upper < length) {
            final int type = Character.getType(text.charAt(upper));
            while (++upper < length) {
                if (Character.getType(text.charAt(upper)) != type) {
                    break;
                }
            }
        }
        final String error = text.substring(errorIndex, upper);
        text = text.substring(index);
        if (level != null) {
            return resources.getLogRecord(level, ErrorKeys.UNPARSABLE_STRING_$2, text, error);
        }
        return resources.getString(ErrorKeys.UNPARSABLE_STRING_$2, text, error);
    }

    /**
     * Returns a string representation for debugging purpose.
     */
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder(Classes.getShortClassName(this))
                .append('[').append(Classes.getShortClassName(format));
        if (logger != null) {
            buffer.append(", logger=").append(logger);
        }
        return buffer.append(']').toString();
    }
}
