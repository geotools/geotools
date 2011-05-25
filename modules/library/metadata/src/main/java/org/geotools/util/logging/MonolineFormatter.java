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
package org.geotools.util.logging;

import java.io.IOException;
import java.io.StringWriter;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.geotools.io.LineWriter;
import org.geotools.util.Utilities;


/**
 * A formatter writting log messages on a single line. Compared to {@link SimpleFormatter}, this
 * formatter uses only one line per message instead of two. For example a message formatted by
 * {@code MonolineFormatter} looks like:
 *
 * <blockquote><pre>
 * FINE core - A log message logged with level FINE from the "org.geotools.core" logger.
 * </pre></blockquote>
 *
 * By default, {@code MonolineFormatter} displays only the level and the message. Additional
 * fields can be formatted if {@link #setTimeFormat} or {@link #setSourceFormat} methods are
 * invoked with a non-null argument. The format can also be set from the
 * {@code jre/lib/logging.properties} file. For example, user can cut and paste the following
 * properties into {@code logging.properties}:
 *
 * <blockquote><pre>
 * ############################################################
 * # Properties for the Geotools's MonolineFormatter.
 * # By default, the monoline formatter display only the level
 * # and the message. Additional fields can be specified here:
 * #
 * #   time:  If set, writes the time ellapsed since the initialization.
 * #          The argument specifies the output pattern. For example, the
 * #          pattern HH:mm:ss.SSSS displays the hours, minutes, seconds
 * #          and milliseconds.
 * #
 * #  source: If set, writes the source logger or the source class name.
 * #          The argument specifies the type of source to display. Valid
 * #          values are none, logger:short, logger:long, class:short and
 * #          class:long.
 * ############################################################
 * org.geotools.util.logging.MonolineFormatter.time = HH:mm:ss.SSS
 * org.geotools.util.logging.MonolineFormatter.source = class:short
 * </pre></blockquote>
 *
 * The example below set the {@code MonolineFormatter} for the whole system
 * with level FINE and "Cp850" page encoding (which is appropriate for some
 * DOS command lines on Windows).
 *
 * <blockquote><pre>
 * java.util.logging.ConsoleHandler.formatter = org.geotools.util.logging.MonolineFormatter
 * java.util.logging.ConsoleHandler.encoding = Cp850
 * java.util.logging.ConsoleHandler.level = FINE
 * </pre></blockquote>
 *
 * @since 2.0
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class MonolineFormatter extends Formatter {
    /**
     * The string to write at the begining of all log headers (e.g. "[FINE core]")
     */
    private static final String PREFIX = "";

    /**
     * The string to write at the end of every log header (e.g. "[FINE core]").
     * It should includes the spaces between the header and the message body.
     */
    private static final String SUFFIX = " - ";

    /**
     * The default header width.
     */
    private static final int DEFAULT_WIDTH = 9;

    /** Do not format source class name.       */ private static final int NO_SOURCE    = 0;
    /** Explicit value for 'none'.             */ private static final int NO_SOURCE_EX = 1;
    /** Format the source logger without base. */ private static final int LOGGER_SHORT = 2;
    /** Format the source logger only.         */ private static final int LOGGER_LONG  = 3;
    /** Format the class name without package. */ private static final int CLASS_SHORT  = 4;
    /** Format the fully qualified class name. */ private static final int CLASS_LONG   = 5;

    /**
     * The label to use in the {@code logging.properties} for setting the source format.
     */
    private static String[] FORMAT_LABELS = new String[6];
    static {
        FORMAT_LABELS[NO_SOURCE_EX] = "none";
        FORMAT_LABELS[LOGGER_SHORT] = "logger:short";
        FORMAT_LABELS[LOGGER_LONG ] = "logger:long";
        FORMAT_LABELS[ CLASS_SHORT] = "class:short";
        FORMAT_LABELS[ CLASS_LONG ] = "class:long";
    }

    /**
     * The line separator. This is the value of the "line.separator"
     * property at the time the {@code MonolineFormatter} was created.
     */
    private final String lineSeparator = System.getProperty("line.separator", "\n");

    /**
     * The line separator for the message body. This line always begin with
     * {@link #lineSeparator}, followed by some amount of spaces in order to
     * align the message.
     */
    private String bodyLineSeparator = lineSeparator;

    /**
     * The minimum amount of spaces to use for writting level and module name
     * before the message.  For example if this value is 12, then a message from
     * module "org.geotools.core" with level FINE would be formatted as
     * "<code>[core&nbsp;&nbsp;FINE]</code> <cite>the message</cite>"
     * (i.e. the whole <code>[&nbsp;]</code> part is 12 characters wide).
     */
    private final int margin;

    /**
     * Time of {@code MonolineFormatter} creation,
     * in milliseconds ellapsed since January 1, 1970.
     */
    private final long startMillis;

    /**
     * The format to use for formatting ellapsed time,
     * or {@code null} if there is none.
     */
    private SimpleDateFormat timeFormat = null;

    /**
     * One of the following constants: {@link #NO_SOURCE},
     * {@link #LOGGER_SHORT}, {@link #LOGGER_LONG},
     * {@link #CLASS_SHORT} or {@link #CLASS_LONG}.
     */
    private int sourceFormat = NO_SOURCE;

    /**
     * Buffer for formatting messages. We will reuse this
     * buffer in order to reduce memory allocations.
     */
    private final StringBuffer buffer;

    /**
     * The line writer. This object transform all "\r", "\n" or "\r\n" occurences
     * into a single line separator. This line separator will include space for
     * the marging, if needed.
     */
    private final LineWriter writer;

    /**
     * Constructs a default {@code MonolineFormatter}.
     */
    public MonolineFormatter() {
        this.startMillis = System.currentTimeMillis();
        this.margin      = DEFAULT_WIDTH;
        StringWriter str = new StringWriter();
        writer = new LineWriter(str);
        buffer = str.getBuffer();
        buffer.append(PREFIX);

        // Configure this formatter
        final LogManager manager = LogManager.getLogManager();
        final String   classname = MonolineFormatter.class.getName();
        try {
            setTimeFormat(manager.getProperty(classname + ".time"));
        } catch (IllegalArgumentException exception) {
            // Can't use the logging framework, since we are configuring it.
            // Display the exception name only, not the trace.
            System.err.println(exception);
        }
        try {
            setSourceFormat(manager.getProperty(classname + ".source"));
        } catch (IllegalArgumentException exception) {
            System.err.println(exception);
        }
    }

    /**
     * Sets the format for displaying ellapsed time. The pattern must matches
     * the format specified in {@link SimpleDateFormat}. For example, the
     * pattern <code>"HH:mm:ss.SSS"</code> will display the ellapsed time
     * in hours, minutes, seconds and milliseconds.
     *
     * @param pattern The time patter, or {@code null} to disable time formatting.
     */
    public synchronized void setTimeFormat(final String pattern) {
        if (pattern == null) {
            timeFormat = null;
        } else if (timeFormat == null) {
            timeFormat = new SimpleDateFormat(pattern);
            timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        } else {
            timeFormat.applyPattern(pattern);
        }
    }

    /**
     * Returns the format for displaying ellapsed time. This is the pattern specified
     * to the last call to {@link #setTimeFormat}, or the patten specified in the
     * {@code org.geotools.MonolineFormater.time} property in the
     * {@code jre/lib/logging.properties} file.
     *
     * @return The time pattern, or {@code null} if time is not formatted.
     */
    public synchronized String getTimeFormat() {
        return (timeFormat != null) ? timeFormat.toPattern() : null;
    }

    /**
     * Sets the format for displaying the source. The pattern may be one of the following:
     *
     * <code>"none"</code>,
     * <code>"logger:short"</code>,  <code>"class:short"</code>,
     * <code>"logger:long"</code> or <code>"class:long"</code>.
     *
     * The difference between a {@code null} and <code>"none"</code> is that {@code null}
     * may be replaced by a default value, while <code>"none"</code> means that the user
     * explicitly requested no source.
     *
     * @param format The format for displaying the source.
     */
    public synchronized void setSourceFormat(String format) {
        if (format != null) {
            format = format.trim().toLowerCase();
        }
        for (int i=0; i<FORMAT_LABELS.length; i++) {
            if (Utilities.equals(FORMAT_LABELS[i], format)) {
                sourceFormat = i;
                return;
            }
        }
        throw new IllegalArgumentException(format);
    }

    /**
     * Returns the format for displaying the source. This is the pattern specified
     * to the last call to {@link #setSourceFormat}, or the patten specified in the
     * {@code org.geotools.MonolineFormater.source} property in the
     * {@code jre/lib/logging.properties} file.
     *
     * @return The source pattern, or {@code null} if source is not formatted.
     */
    public String getSourceFormat() {
        return FORMAT_LABELS[sourceFormat];
    }

    /**
     * Formats the given log record and return the formatted string.
     *
     * @param  record the log record to be formatted.
     * @return a formatted log record
     */
    @SuppressWarnings("fallthrough")
    public synchronized String format(final LogRecord record) {
        buffer.setLength(PREFIX.length());
        /*
         * Formats the time (e.g. "00:00:12.365").  The time pattern can be set
         * either programmatically with a call to setTimeFormat(String), or in
         * the logging.properties file with the
         * "org.geotools.util.logging.MonolineFormatter.time" property.
         */
        if (timeFormat != null) {
            Date time = new Date(Math.max(0, record.getMillis() - startMillis));
            timeFormat.format(time, buffer, new FieldPosition(0));
            buffer.append(' ');
        }
        /*
         * Formats the level (e.g. "FINE"). We do not provide
         * the option to turn level off for now.
         */
        if (true) {
            int offset = buffer.length();
            buffer.append(record.getLevel().getLocalizedName());
            offset = buffer.length() - offset;
            buffer.append(Utilities.spaces(margin-offset));
        }
        /*
         * Adds the source. It may be either the source logger or the source class name.
         */
        String logger    = record.getLoggerName();
        String classname = record.getSourceClassName();
        switch (sourceFormat) {
            case LOGGER_SHORT: {
                int pos = logger.lastIndexOf('.');
                if (pos >= 0) {
                    logger = logger.substring(pos);
                }
                // fall through
            }
            case LOGGER_LONG: {
                buffer.append(' ');
                buffer.append(logger);
                break;
            }
            case CLASS_SHORT: {
                int dot = classname.lastIndexOf('.');
                if (dot >= 0) {
                    classname = classname.substring(dot+1);
                }
                classname = classname.replace('$','.');
                // fall through
            }
            case CLASS_LONG: {
                buffer.append(' ');
                buffer.append(classname);
                break;
            }
        }
        buffer.append(SUFFIX);
        /*
         * Now format the message. We will use a line separator made of the
         * usual EOL ("\r", "\n", or "\r\n", which is plateform specific)
         * following by some amout of space in order to align message body.
         */
        final int margin  = buffer.length();
        assert margin >= this.margin;
        if (bodyLineSeparator.length() != lineSeparator.length()+margin) {
            bodyLineSeparator = lineSeparator + Utilities.spaces(margin);
        }
        try {
            writer.setLineSeparator(bodyLineSeparator);
            writer.write(String.valueOf(formatMessage(record)));
            writer.setLineSeparator(lineSeparator);
            writer.write('\n');
            writer.flush();
        } catch (IOException exception) {
            // Should never happen, since we are writting into a StringBuffer.
            throw new AssertionError(exception);
        }
        return buffer.toString();
    }

    /**
     * Setup a {@code MonolineFormatter} for the specified logger and its children. This method
     * search for all instances of {@link ConsoleHandler} using the {@link SimpleFormatter}. If
     * such instances are found, they are replaced by a single instance of {@code MonolineFormatter}.
     * If no such {@link ConsoleHandler} are found, then a new one is created with this
     * {@code MonolineFormatter}.
     * <p>
     * In addition, this method can set the handler levels. If the level is non-null, then all
     * {@link Handler}s using the monoline formatter will be set to the specified level. This
     * is provided for convenience, but non-null {@code level} argument should be avoided as
     * much as possible because it overrides user's level settings. A user trying to configure
     * his logging properties file may find confusing to see his setting ignored.
     *
     * @param  logger The base logger to apply the change on.
     * @param  level The desired level, or {@code null} if no level should be set.
     * @return The registered {@code MonolineFormatter} (never {@code null}).
     *         The formatter output can be configured using the {@link #setTimeFormat}
     *         and {@link #setSourceFormat} methods.
     */
    public static MonolineFormatter configureConsoleHandler(final Logger logger, final Level level) {
        MonolineFormatter monoline = null;
        boolean foundConsoleHandler = false;
        Handler[] handlers = logger.getHandlers();
        for (int i=0; i<handlers.length; i++) {
            final Handler handler = handlers[i];
            if (handler.getClass().equals(ConsoleHandler.class)) {
                foundConsoleHandler = true;
                final Formatter formatter = handler.getFormatter();
                if (formatter instanceof MonolineFormatter) {
                    /*
                     * A MonolineFormatter already existed. Sets the level only for the first
                     * instance (only one instance should exists anyway) for consistency with
                     * the fact that this method returns only one MonolineFormatter for further
                     * configuration.
                     */
                    if (monoline == null) {
                        monoline = (MonolineFormatter) formatter;
                        if (level != null) {
                            handler.setLevel(level);
                        }
                    }
                } else if (formatter.getClass().equals(SimpleFormatter.class)) {
                    /*
                     * A ConsoleHandler using the SimpleFormatter has been found. Replaces
                     * the SimpleFormatter by MonolineFormatter, creating it if necessary.
                     * If the handler setting fail with an exception, then we will continue
                     * to use the old J2SE handler instead.
                     */
                    if (monoline == null) {
                        monoline = new MonolineFormatter();
                    }
                    try {
                        handler.setFormatter(monoline);
                        if (level != null) {
                            handler.setLevel(level);
                        }
                    } catch (SecurityException exception) {
                        unexpectedException(exception);
                    }
                }
            }
        }
        /*
         * If the logger uses parent handlers, copy them to the logger that we are initializing,
         * because we will not use parent handlers anymore at the end of this method.
         */
        for (Logger parent=logger; parent.getUseParentHandlers();) {
            parent = parent.getParent();
            if (parent == null) {
                break;
            }
            handlers = parent.getHandlers();
            for (int i=0; i<handlers.length; i++) {
                Handler handler = handlers[i];
                if (handler.getClass().equals(ConsoleHandler.class)) {
                    if (!foundConsoleHandler) {
                        // We have already set a ConsoleHandler and we don't want a second one.
                        continue;
                    }
                    foundConsoleHandler = true;
                    final Formatter formatter = handler.getFormatter();
                    if (formatter.getClass().equals(SimpleFormatter.class)) {
                        monoline = addHandler(logger, level);
                        continue;
                    }
                }
                logger.addHandler(handler);
            }
        }
        logger.setUseParentHandlers(false);
        if (!foundConsoleHandler) {
            monoline = addHandler(logger, level);
        }
        return monoline;
    }

    /**
     * Adds to the specified logger a {@link Handler} using a {@code MonolineFormatter}
     * set at the specified level. The formatter is returned for convenience.
     */
    private static MonolineFormatter addHandler(final Logger logger, final Level level) {
        final MonolineFormatter monoline = new MonolineFormatter();
        try {
            final Handler handler = new ConsoleHandler();
            handler.setFormatter(monoline);
            if (level != null) {
                handler.setLevel(level);
            }
            logger.addHandler(handler);
        } catch (SecurityException exception) {
            unexpectedException(exception);
            /*
             * Returns without any change to the J2SE configuration. Note that the returned
             * MonolineFormatter is really a dummy one, since we failed to register it.  It
             * will not prevent to program to work; just produces different logging outputs.
             */
        }
        return monoline;
    }

    /**
     * Invoked when an error occurs during the initialization.
     */
    private static void unexpectedException(final Exception exception) {
        Logging.unexpectedException(MonolineFormatter.class, "configureConsoleHandler", exception);
    }
}
