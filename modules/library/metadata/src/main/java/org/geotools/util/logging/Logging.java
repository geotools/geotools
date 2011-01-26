/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LogRecord;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import org.geotools.resources.XArray;
import org.geotools.resources.Classes;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * A set of utilities method for configuring loggings in GeoTools. <strong>All GeoTools
 * code should fetch their logger through a call to {@link #getLogger(String)}</strong>,
 * not {@link Logger#getLogger(String)}. This is necessary in order to give GeoTools a
 * chance to redirect log events to an other logging framework, for example
 * <A HREF="http://jakarta.apache.org/commons/logging/">commons-logging</A>.
 * <p>
 * <b>Example:</b> In order to redirect every GeoTools log events to Commons-logging,
 * invoke the following once at application startup:
 *
 * <blockquote><code>
 * Logging.{@linkplain #GEOTOOLS}.{@linkplain #setLoggerFactory
 * setLoggerFactory}("org.geotools.util.logging.CommonsLoggerFactory");
 * </code></blockquote>
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class Logging {
    /**
     * Compares {@link Logging} or {@link String} objects for alphabetical order.
     */
    private static final Comparator<Object> COMPARATOR = new Comparator<Object>() {
        public int compare(final Object o1, final Object o2) {
            final String n1 = (o1 instanceof Logging) ? ((Logging) o1).name : o1.toString();
            final String n2 = (o2 instanceof Logging) ? ((Logging) o2).name : o2.toString();
            return n1.compareTo(n2);
        }
    };

    /**
     * An empty array of loggings. Also used for locks.
     */
    private static final Logging[] EMPTY = new Logging[0];

    /**
     * Logging configuration that apply to all packages.
     */
    public static final Logging ALL = new Logging();
    // NOTE: ALL must be created before any other static Logging constant.

    /**
     * Logging configuration that apply only to GeoTools packages.
     */
    public static final Logging GEOTOOLS = getLogging("org.geotools");

    /**
     * The name of the base package.
     */
    final String name;

    /**
     * The children {@link Logging} objects.
     * <p>
     * The plain array used there is not efficient for adding new items (an {@code ArrayList}
     * would be more efficient), but we assume that very few new items will be added. Furthermore
     * a plain array is efficient for reading, and the later is way more common than the former.
     */
    private Logging[] children = EMPTY;

    /**
     * The factory for creating loggers.
     *
     * @see #setLoggerFactory
     */
    private LoggerFactory factory;

    /**
     * {@code true} if every {@link Logging} instances use the same {@link LoggerFactory}.
     * This is an optimization for a very common case.
     */
    private static boolean sameLoggerFactory = true;

    /**
     * Creates an instance for the root logger. This constructor should not be used
     * for anything else than {@link #ALL} construction; use {@link #getLogging} instead.
     */
    private Logging() {
        name = "";
    }

    /**
     * Creates an instance for the specified base logger. This constructor
     * should not be public; use {@link #getLogging} instead.
     *
     * @param parent The parent {@code Logging} instance.
     * @param name   The logger name for the new instance.
     */
    private Logging(final Logging parent, final String name) {
        this.name = name;
        factory = parent.factory;
        assert name.startsWith(parent.name) : name;
    }

    /**
     * Returns a logger for the specified class. This convenience method invokes
     * {@link #getLogger(String)} with the package name as the logger name.
     *
     * @param  classe The class for which to obtain a logger.
     * @return A logger for the specified class.
     *
     * @since 2.5
     */
    public static Logger getLogger(final Class<?> classe) {
        String name = classe.getName();
        final int separator = name.lastIndexOf('.');
        name = (separator >= 1) ? name.substring(0, separator) : "";
        return getLogger(name);
    }

    /**
     * Returns a logger for the specified name. If a {@linkplain LoggerFactory logger factory} has
     * been set, then this method first {@linkplain LoggerFactory#getLogger ask to the factory}.
     * It gives GeoTools a chance to redirect logging events to
     * <A HREF="http://jakarta.apache.org/commons/logging/">commons-logging</A>
     * or some equivalent framework.
     * <p>
     * If no factory was found or if the factory choose to not redirect the loggings, then this
     * method returns the usual <code>{@linkplain Logger#getLogger Logger.getLogger}(name)</code>.
     *
     * @param  name The logger name.
     * @return A logger for the specified name.
     */
    public static Logger getLogger(final String name) {
        synchronized (EMPTY) {
            final Logging logging = sameLoggerFactory ? ALL : getLogging(name, false);
            if (logging != null) { // Paranoiac check ('getLogging' should not returns null).
                final LoggerFactory factory = logging.factory;
                assert getLogging(name, false).factory == factory : name;
                if (factory != null) {
                    final Logger logger = factory.getLogger(name);
                    if (logger != null) {
                        return logger;
                    }
                }
            }
        }
        return Logger.getLogger(name);
    }

    /**
     * Returns a {@code Logging} instance for the specified base logger. This instance is
     * used for controlling logging configuration in GeoTools. For example methods like
     * {@link #forceMonolineConsoleOutput} are invoked on a {@code Logging} instance.
     * <p>
     * {@code Logging} instances follow the same hierarchy than {@link Logger}, i.e.
     * {@code "org.geotools"} is the parent of {@code "org.geotools.referencing"},
     * {@code "org.geotools.metadata"}, <cite>etc</cite>.
     *
     * @param name The base logger name.
     */
    public static Logging getLogging(final String name) {
        synchronized (EMPTY) {
            return getLogging(name, true);
        }
    }

    /**
     * Returns a logging instance for the specified base logger. If no instance if found for
     * the specified name and {@code create} is {@code true}, then a new instance will be
     * created. Otherwise the nearest parent is returned.
     *
     * @param root The root logger name.
     * @param create {@code true} if this method is allowed to create new {@code Logging} instance.
     */
    private static Logging getLogging(final String base, final boolean create) {
        assert Thread.holdsLock(EMPTY);
        Logging logging = ALL;
        if (base.length() != 0) {
            int offset = 0;
            do {
                Logging[] children = logging.children;
                offset = base.indexOf('.', offset);
                final String name = (offset >= 0) ? base.substring(0, offset) : base;
                int i = Arrays.binarySearch(children, name, COMPARATOR);
                if (i < 0) {
                    // No exact match found.
                    if (!create) {
                        // We are not allowed to create new Logging instance.
                        // 'logging' is the nearest parent, so stop the loop now.
                        break;
                    }
                    i = ~i;
                    children = XArray.insert(children, i, 1);
                    children[i] = new Logging(logging, name);
                    logging.children = children;
                }
                logging = children[i];
            } while (++offset != 0);
        }
        return logging;
    }

    /**
     * For testing purpose only; don't make this method public.
     */
    final Logging[] getChildren() {
        synchronized (EMPTY) {
            return children.clone();
        }
    }

    /**
     * Returns the logger factory, or {@code null} if none. This method returns the logger set
     * by the last call to {@link #setLoggerFactory} on this {@code Logging} instance or on one
     * of its parent.
     */
    public LoggerFactory getLoggerFactory() {
        synchronized (EMPTY) {
            return factory;
        }
    }

    /**
     * Sets a new logger factory for this {@code Logging} instance and every children. The
     * specified factory will be used by <code>{@linkplain #getLogger(String) getLogger}(name)</code>
     * when {@code name} is this {@code Logging} name or one of its children.
     */
    public void setLoggerFactory(final LoggerFactory factory) {
        synchronized (EMPTY) {
            this.factory = factory;
            for (int i=0; i<children.length; i++) {
                children[i].setLoggerFactory(factory);
            }
            sameLoggerFactory = sameLoggerFactory(ALL.children, ALL.factory);
        }
    }

    /**
     * Returns {@code true} if all children use the specified factory.
     * Used in order to detect a possible optimization for this very common case.
     */
    private static boolean sameLoggerFactory(final Logging[] children, final LoggerFactory factory) {
        assert Thread.holdsLock(EMPTY);
        for (int i=0; i<children.length; i++) {
            final Logging logging = children[i];
            if (logging.factory != factory || !sameLoggerFactory(logging.children, factory)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets a new logger factory from a fully qualidifed class name. This method should be
     * preferred to {@link #setLoggerFactory(LoggerFactory)} when the underlying logging
     * framework is not garanteed to be on the classpath.
     *
     * @param  className The fully qualified factory class name.
     * @throws ClassNotFoundException if the specified class was not found.
     * @throws IllegalArgumentException if the specified class is not a subclass of
     *         {@link LoggerFactory}, or if no public static {@code getInstance()} method
     *         has been found or can be executed.
     */
    public void setLoggerFactory(final String className)
            throws ClassNotFoundException, IllegalArgumentException
    {
        final LoggerFactory factory;
        if (className == null) {
            factory = null;
        } else {
            final Class<?> factoryClass;
            try {
                factoryClass = Class.forName(className);
            } catch (NoClassDefFoundError error) {
                throw factoryNotFound(className, error);
            }
            if (!LoggerFactory.class.isAssignableFrom(factoryClass)) {
                throw new IllegalArgumentException(Errors.format(
                        ErrorKeys.ILLEGAL_CLASS_$2, factoryClass, LoggerFactory.class));
            }
            try {
                final Method method = factoryClass.getMethod("getInstance", (Class[]) null);
                factory = LoggerFactory.class.cast(method.invoke(null, (Object[]) null));
            } catch (Exception e) {
                /*
                 * Catching java.lang.Exception is usually bad practice, but there is really a lot
                 * of checked exceptions when using reflection. Unfortunatly there is nothing like
                 * a "ReflectionException" parent class that we could catch instead. There is also
                 * a few unchecked exception that we want to process here, like ClassCastException.
                 */
                Throwable cause = e;
                if (e instanceof InvocationTargetException) {
                    cause = e.getCause(); // Simplify the stack trace.
                }
                if (cause instanceof ClassNotFoundException) {
                    throw (ClassNotFoundException) e;
                }
                if (cause instanceof NoClassDefFoundError) {
                    throw factoryNotFound(className, (NoClassDefFoundError) cause);
                }
                throw new IllegalArgumentException(Errors.format(
                        ErrorKeys.CANT_CREATE_FACTORY_$1, className, cause));
            }
        }
        setLoggerFactory(factory);
    }

    /**
     * Wraps a unchecked {@link NoClassDefFoundError} into a checked {@link ClassNotFoundException}.
     */
    private static ClassNotFoundException factoryNotFound(String name, NoClassDefFoundError error) {
        return new ClassNotFoundException(Errors.format(ErrorKeys.FACTORY_NOT_FOUND_$1, name), error);
    }

    /**
     * Configures the default {@linkplain java.util.logging.ConsoleHandler console handler} in
     * order to log records on a single line instead of two lines. More specifically, for each
     * {@link java.util.logging.ConsoleHandler} using a {@link java.util.logging.SimpleFormatter},
     * this method replaces the simple formatter by an instance of {@link MonolineFormatter}. If
     * no {@code ConsoleHandler} are found, then a new one is created.
     * <p>
     * <b>Note:</b> this method may have no effect if the loggings are redirected to an other
     * logging framework, for example if {@link #redirectToCommonsLogging} has been invoked.
     */
    public void forceMonolineConsoleOutput() {
        forceMonolineConsoleOutput(null);
    }

    /**
     * Same as {@link #forceMonolineConsoleOutput()}, but additionnaly set an optional logging
     * level. If the specified level is non-null, then all {@link java.util.logging.Handler}s
     * using the monoline formatter will be set to the specified level.
     * <p>
     * <b>Note:</b> Avoid this method as much as possible, since it overrides user's level
     * setting. A user trying to configure his logging properties may find confusing to see
     * his setting ignored.
     *
     * @see org.geotools.factory.GeoTools#init
     */
    public void forceMonolineConsoleOutput(final Level level) {
        final Logger logger = Logger.getLogger(name); // Really Java logging, not the redirected one.
        synchronized (EMPTY) {
            final MonolineFormatter f = MonolineFormatter.configureConsoleHandler(logger, level);
            if (f.getSourceFormat() == null) {
                // Set the source format only if the user didn't specified
                // an explicit one in the jre/lib/logging.properties file.
                f.setSourceFormat("class:short");
            }
            if (level != null) {
                // If a level was specified, changes to a finer level if needed
                // (e.g. from FINE to FINER, but not the opposite).
                final Level current = logger.getLevel();
                if (current == null || current.intValue() > level.intValue()) {
                    logger.setLevel(level);
                }
            }
        }
    }

    /**
     * Invoked when an unexpected error occurs. This method logs a message at the
     * {@link Level#WARNING WARNING} level to the specified logger. The originating
     * class name and method name are inferred from the error stack trace, using the
     * first {@linkplain StackTraceElement stack trace element} for which the class
     * name is inside a package or sub-package of the logger name. For example if
     * the logger name is {@code "org.geotools.image"}, then this method will uses
     * the first stack trace element where the fully qualified class name starts with
     * {@code "org.geotools.image"} or {@code "org.geotools.image.io"}, but not
     * {@code "org.geotools.imageio"}.
     *
     * @param  logger Where to log the error.
     * @param  error  The error that occured.
     * @return {@code true} if the error has been logged, or {@code false} if the logger
     *         doesn't log anything at the {@link Level#WARNING WARNING} level.
     */
    public static boolean unexpectedException(final Logger logger, final Throwable error) {
        return unexpectedException(logger, null, null, error, Level.WARNING);
    }

    /**
     * Invoked when an unexpected error occurs. This method logs a message at the
     * {@link Level#WARNING WARNING} level to the specified logger. The originating
     * class name and method name can optionnaly be specified. If any of them is
     * {@code null}, then it will be inferred from the error stack trace as in
     * {@link #unexpectedException(Logger, Throwable)}.
     * <p>
     * Explicit value for class and method names are sometime preferred to automatic
     * inference for the following reasons:
     *
     * <ul>
     *   <li><p>Automatic inference is not 100% reliable, since the Java Virtual Machine
     *       is free to omit stack frame in optimized code.</p></li>
     *   <li><p>When an exception occured in a private method used internally by a public
     *       method, we sometime want to log the warning for the public method instead,
     *       since the user is not expected to know anything about the existence of the
     *       private method. If a developper really want to know about the private method,
     *       the stack trace is still available anyway.</p></li>
     * </ul>
     *
     * @param logger  Where to log the error.
     * @param classe  The class where the error occurred, or {@code null}.
     * @param method  The method where the error occurred, or {@code null}.
     * @param error   The error.
     * @return {@code true} if the error has been logged, or {@code false} if the logger
     *         doesn't log anything at the {@link Level#WARNING WARNING} level.
     */
    public static boolean unexpectedException(final Logger logger, final Class<?> classe,
                                              final String method, final Throwable error)
    {
        final String classname = (classe != null) ? classe.getName() : null;
        return unexpectedException(logger, classname, method, error, Level.WARNING);
    }

    /**
     * Invoked when an unexpected error occurs. This method logs a message at the
     * {@link Level#WARNING WARNING} level to the logger for the specified package
     * name. The originating class name and method name can optionnaly be specified.
     * If any of them is {@code null}, then it will be inferred from the error stack
     * trace as in {@link #unexpectedException(Logger, Throwable)}.
     *
     * @param paquet  The package where the error occurred, or {@code null}. This
     *                information is used for fetching an appropriate {@link Logger}
     *                for logging the error.
     * @param classe  The class where the error occurred, or {@code null}.
     * @param method  The method where the error occurred, or {@code null}.
     * @param error   The error.
     * @return {@code true} if the error has been logged, or {@code false} if the logger
     *         doesn't log anything at the {@link Level#WARNING WARNING} level.
     *
     * @deprecated Use one of the other {@code unexpectedException} methods instead.
     */
    public static boolean unexpectedException(final String paquet, final Class<?> classe,
                                              final String method, final Throwable error)
    {
        final Logger logger = (paquet != null) ? getLogger(paquet) : null;
        return unexpectedException(logger, classe, method, error);
    }

    /**
     * Invoked when an unexpected error occurs. This method logs a message at the
     * {@link Level#WARNING WARNING} level to a logger inferred from the given class.
     *
     * @param classe  The class where the error occurred.
     * @param method  The method where the error occurred, or {@code null}.
     * @param error   The error.
     * @return {@code true} if the error has been logged, or {@code false} if the logger
     *         doesn't log anything at the {@link Level#WARNING WARNING} level.
     *
     * @since 2.5
     */
    public static boolean unexpectedException(Class<?> classe, String method, Throwable error) {
        return unexpectedException((Logger) null, classe, method, error);
    }

    /**
     * Implementation of {@link #unexpectedException(Logger, Class, String, Throwable)}.
     *
     * @param logger  Where to log the error, or {@code null}.
     * @param classe  The fully qualified class name where the error occurred, or {@code null}.
     * @param method  The method where the error occurred, or {@code null}.
     * @param error   The error.
     * @param level   The logging level.
     * @return {@code true} if the error has been logged, or {@code false} if the logger
     *         doesn't log anything at the specified level.
     */
    private static boolean unexpectedException(Logger logger, String classe, String method,
                                               final Throwable error, final Level level)
    {
        /*
         * Checks if loggable, inferring the logger from the classe name if needed.
         */
        if (error == null) {
            return false;
        }
        if (logger == null && classe != null) {
            final int separator = classe.lastIndexOf('.');
            final String paquet = (separator >= 1) ? classe.substring(0, separator-1) : "";
            logger = getLogger(paquet);
        }
        if (logger != null && !logger.isLoggable(level)) {
            return false;
        }
        /*
         * Loggeable, so complete the null argument from the stack trace if we can.
         */
        if (logger==null || classe==null || method==null) {
            String paquet = (logger != null) ? logger.getName() : null;
            final StackTraceElement[] elements = error.getStackTrace();
            for (int i=0; i<elements.length; i++) {
                /*
                 * Searchs for the first stack trace element with a classname matching the
                 * expected one. We compare preferably against the name of the class given
                 * in argument, or against the logger name (taken as the package name) otherwise.
                 */
                final StackTraceElement element = elements[i];
                final String classname = element.getClassName();
                if (classe != null) {
                    if (!classname.equals(classe)) {
                        continue;
                    }
                } else if (paquet != null) {
                    if (!classname.startsWith(paquet)) {
                        continue;
                    }
                    final int length = paquet.length();
                    if (classname.length() > length) {
                        // We expect '.' but we accept also '$' or end of string.
                        final char separator = classname.charAt(length);
                        if (Character.isJavaIdentifierPart(separator)) {
                            continue;
                        }
                    }
                }
                /*
                 * Now that we have a stack trace element from the expected class (or any
                 * element if we don't know the class), make sure that we have the right method.
                 */
                final String methodName = element.getMethodName();
                if (method != null && !methodName.equals(method)) {
                    continue;
                }
                /*
                 * Now computes every values that are null, and stop the loop.
                 */
                if (paquet == null) {
                    final int separator = classname.lastIndexOf('.');
                    paquet = (separator >= 1) ? classname.substring(0, separator-1) : "";
                    logger = getLogger(paquet);
                    if (!logger.isLoggable(level)) {
                        return false;
                    }
                }
                if (classe == null) {
                    classe = classname;
                }
                if (method == null) {
                    method = methodName;
                }
                break;
            }
            /*
             * The logger may stay null if we have been unable to find a suitable
             * stack trace. Fallback on the global logger.
             *
             * TODO: Use GLOBAL_LOGGER_NAME constant when we will be allowed to target Java 6.
             */
            if (logger == null) {
                logger = getLogger("global");
                if (!logger.isLoggable(level)) {
                    return false;
                }
            }
        }
        /*
         * Now prepare the log message. If we have been unable to figure out a source class and
         * method name, we will fallback on Java logging default mechanism, which may returns a
         * less relevant name than our attempt to use the logger name as the package name.
         */
        final StringBuilder buffer = new StringBuilder(Classes.getShortClassName(error));
        final String message = error.getLocalizedMessage();
        if (message != null) {
            buffer.append(": ").append(message);
        }
        final LogRecord record = new LogRecord(level, buffer.toString());
        if (classe != null) {
            record.setSourceClassName(classe);
        }
        if (method != null) {
            record.setSourceMethodName(method);
        }
        if (level.intValue() > 500) {
            record.setThrown(error);
        }
        record.setLoggerName(logger.getName());
        logger.log(record);
        return true;
    }

    /**
     * Invoked when a recoverable error occurs. This method is similar to
     * {@link #unexpectedException(Logger,Class,String,Throwable) unexpectedException}
     * except that it doesn't log the stack trace and uses a lower logging level.
     *
     * @param logger  Where to log the error.
     * @param classe  The class where the error occurred.
     * @param method  The method name where the error occurred.
     * @param error   The error.
     * @return {@code true} if the error has been logged, or {@code false} if the logger
     *         doesn't log anything at the specified level.
     *
     * @since 2.5
     */
    public static boolean recoverableException(final Logger logger, final Class<?> classe,
                                               final String method, final Throwable error)
    {
        final String classname = (classe != null) ? classe.getName() : null;
        return unexpectedException(logger, classname, method, error, Level.FINE);
    }

    /**
     * Invoked when a recoverable error occurs. This method is similar to
     * {@link #unexpectedException(Class,String,Throwable) unexpectedException}
     * except that it doesn't log the stack trace and uses a lower logging level.
     *
     * @param classe  The class where the error occurred.
     * @param method  The method name where the error occurred.
     * @param error   The error.
     * @return {@code true} if the error has been logged, or {@code false} if the logger
     *         doesn't log anything at the specified level.
     *
     * @since 2.5
     */
    public static boolean recoverableException(final Class<?> classe, final String method,
                                               final Throwable error)
    {
        return recoverableException(null, classe, method, error);
    }
}
