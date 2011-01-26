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
package org.geotools.factory;

import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import org.geotools.resources.Classes;
import org.geotools.resources.Arguments;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.util.logging.LoggerFactory;
import org.geotools.util.logging.Logging;
import org.geotools.util.Utilities;
import org.geotools.util.Version;


/**
 * Static methods relative to the global GeoTools configuration. GeoTools can be configured
 * in a system-wide basis through {@linkplain System#getProperties system properties}, some
 * of them are declared as {@link String} constants in this class.
 * <p>
 * There are many aspects to the configuration of GeoTools:
 * <ul>
 *   <li>Default Settings: Are handled as the Hints returned by {@link #getDefaultHints()}, the
 *       default values can be provided in application code, or specified using system properties.</li>
 *   <li>Integration JNDI: Telling the GeoTools library about the facilities of an application,
 *       or application container takes several forms. This class provides the
 *       {@link #init(InitialContext)} method allowing to tell GeoTools about the JNDI
 *       context to use.</li>
 *   <li>Integration Plugins: If hosting GeoTools in a alternate plugin system such as Spring
 *       or OSGi, application may needs to hunt down the {@code FactoryFinder}s and register
 *       additional "Factory Iterators" for GeoTools to search using the
 *       {@link #addFactoryIteratorProvider} method.</li>
 * </ul>
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Jody Garnett
 * @author Martin Desruisseaux
 */
public final class GeoTools {
    /**
     * Properties about this geotools build 
     */
    private static final Properties PROPS;  
    static {
        Properties props = new Properties();
        try {
            props.load(GeoTools.class.getResourceAsStream("GeoTools.properties"));
        }
        catch(Exception e) {}
        
        PROPS = props;
    }
    
    /**
     * The current GeoTools version. The separator character must be the dot.
     */
    private static final Version VERSION = new Version(PROPS.getProperty("version", "2.7-SNAPSHOT"));

    /**
     * The version control (svn) revision at which this version of geotools was built.
     */
    private static final String BUILD_REVISION = PROPS.getProperty("build.revision", "-1");
    
    /**
     * The timestamp at which this version of geotools was built.
     */
    private static final String BUILD_TIMESTAMP = PROPS.getProperty("build.timestamp", ""); 
        
    /**
     * Object to inform about system-wide configuration changes.
     * We use the Swing utility listener list since it is lightweight and thread-safe.
     * Note that it doesn't involve any dependency to the remaining of Swing library.
     */
    private static final EventListenerList LISTENERS = new EventListenerList();

    /**
     * The bindings between {@linkplain System#getProperties system properties} and
     * a hint key. This field must be declared before any call to the {@link #bind}
     * method.
     */
    private static final Map<String, RenderingHints.Key> BINDINGS =
            new HashMap<String, RenderingHints.Key>();

    /**
     * The {@linkplain System#getProperty(String) system property} key for the default value to be
     * assigned to the {@link Hints#CRS_AUTHORITY_EXTRA_DIRECTORY CRS_AUTHORITY_EXTRA_DIRECTORY}
     * hint.
     *
     * @see Hints#CRS_AUTHORITY_EXTRA_DIRECTORY
     * @see #getDefaultHints
     */
    public static final String CRS_AUTHORITY_EXTRA_DIRECTORY =
            "org.geotools.referencing.crs-directory";
    static {
        bind(CRS_AUTHORITY_EXTRA_DIRECTORY, Hints.CRS_AUTHORITY_EXTRA_DIRECTORY);
    }

    /**
     * The {@linkplain System#getProperty(String) system property} key for the default
     * value to be assigned to the {@link Hints#EPSG_DATA_SOURCE EPSG_DATA_SOURCE} hint.
     *
     * @see Hints#EPSG_DATA_SOURCE
     * @see #getDefaultHints
     */
    public static final String EPSG_DATA_SOURCE =
            "org.geotools.referencing.epsg-datasource";
    static {
        bind(EPSG_DATA_SOURCE, Hints.EPSG_DATA_SOURCE);
    }

    /**
     * The {@linkplain System#getProperty(String) system property} key for the default
     * value to be assigned to the {@link Hints#FORCE_LONGITUDE_FIRST_AXIS_ORDER
     * FORCE_LONGITUDE_FIRST_AXIS_ORDER} hint.
     *
     * This setting can provide a transition path for projects expecting a (<var>longitude</var>,
     * <var>latitude</var>) axis order on a system-wide level. Application developpers can set the
     * default value as below:
     *
     * <blockquote><pre>
     * System.setProperty(FORCE_LONGITUDE_FIRST_AXIS_ORDER, "true");
     * </pre></blockquote>
     *
     * Note that this system property applies mostly to the default EPSG factory. Most other
     * factories ({@code "CRS"}, {@code "AUTO"}, <cite>etc.</cite>) don't need this property
     * since they use (<var>longitude</var>, <var>latitude</var>) axis order by design.
     *
     * @see Hints#FORCE_LONGITUDE_FIRST_AXIS_ORDER
     * @see #getDefaultHints
     */
    public static final String FORCE_LONGITUDE_FIRST_AXIS_ORDER =
            "org.geotools.referencing.forceXY";
    static {
        bind(FORCE_LONGITUDE_FIRST_AXIS_ORDER, Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
    }

    /**
     * The initial context. Will be created only when first needed.
     */
    private static InitialContext context;

    /**
     * Do not allow instantiation of this class.
     */
    private GeoTools() {
    }

    /**
     * Binds the specified {@linkplain System#getProperty(String) system property}
     * to the specified key. Only one key can be binded to a given system property.
     * However the same key can be binded to more than one system property names,
     * in which case the extra system property names are aliases.
     *
     * @param  property The system property.
     * @param  key The key to bind to the system property.
     * @throws IllegalArgumentException if an other key is already bounds
     *         to the given system property.
     */
    private static void bind(final String property, final RenderingHints.Key key) {
        synchronized (BINDINGS) {
            final RenderingHints.Key old = BINDINGS.put(property, key);
            if (old == null) {
                return;
            }
            // Roll back
            BINDINGS.put(property, old);
        }
        throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2,
                "property", property));
    }

    /**
     * Reports back the version of GeoTools being used.
     *
     * @return The current GeoTools version.
     */
    public static Version getVersion(){
         return VERSION;
    }
    
    /**
     * Reports back the vcs revision at which the version of GeoTools was built. 
     * 
     * @return The svn revision.
     */
    public static String getBuildRevision() {
        return BUILD_REVISION;
    }
    
    /**
     * Reports back the timestamp at which the version of GeoTools of built. 
     * 
     * @return The build timestamp.
     */
    public static String getBuildTimestamp() {
        return BUILD_TIMESTAMP;
    }

    /**
     * Sets the global {@linkplain LoggerFactory logger factory}.
     *
     * This method is the same as {@code Logging.GEOTOOLS.setLoggerFactory(factory)}.
     * GeoTools ships with support for
     * <A HREF="http://jakarta.apache.org/commons/logging/">Commons-logging</A> and
     * <A HREF="http://logging.apache.org/log4j/">log4j</A>. This method exists to allow you
     * supply your own implementation (this is sometimes required when using a GeoTools
     * application in an exotic environment like Eclipse, OC4J or your application).
     *
     * @param factory The logger factory to use.
     *
     * @see Logging#setLoggerFactory(LoggerFactory)
     *
     * @since 2.4
     */
    public void setLoggerFactory(final LoggerFactory factory) {
        Logging.GEOTOOLS.setLoggerFactory(factory);
    }

    /**
     * Initializes GeoTools for use. This convenience method performs various tasks (more may
     * be added in the future), including setting up the {@linkplain java.util.logging Java
     * logging framework} in one of the following states:
     * <p>
     * <ul>
     *   <li>If the <A HREF="http://jakarta.apache.org/commons/logging/">Commons-logging</A>
     *       framework is available, then every logging message in the {@code org.geotools}
     *       namespace sent to the Java {@linkplain java.util.logging.Logger logger} are
     *       redirected to Commons-logging.</li>
     *
     *   <li>Otherwise if the <A HREF="http://logging.apache.org/log4j">Log4J</A> framework is
     *       available, then every logging message in the {@code org.geotools} namespace sent
     *       to the Java {@linkplain java.util.logging.Logger logger} are redirected to Log4J.</li>
     *
     *   <li>Otherwise, the Java logging {@linkplain java.util.logging.Formatter formatter} for
     *       console output is replaced by a {@linkplain org.geotools.util.logging.MonolineFormatter
     *       monoline formatter}.</li>
     * </ul>
     * <p>
     * In addition, the {@linkplain #getDefaultHints default hints} are initialized to the
     * specified {@code hints}.
     * <p>
     * Note that invoking this method is usually <strong>not</strong> needed for proper working
     * of the Geotools library. It is just a convenience method for overwriting some Java and
     * Geotools default settings in a way that seems to be common in server environment. Such
     * overwriting may not be wanted for every situations.
     * <p>
     * Example of typical invocation in a Geoserver environment:
     *
     * <blockquote><pre>
     * Hints hints = new Hints();
     * hints.put({@linkplain Hints#FORCE_LONGITUDE_FIRST_AXIS_ORDER}, Boolean.TRUE);
     * hints.put({@linkplain Hints#FORCE_AXIS_ORDER_HONORING}, "http");
     * GeoTools.init(hints);
     * </pre></blockquote>
     *
     * @param hints The hints to use.
     *
     * @see Logging#setLoggerFactory(String)
     * @see Logging#forceMonolineConsoleOutput
     * @see Hints#putSystemDefault
     * @see #getDefaultHints
     */
    public static void init(final Hints hints) {
        final Logging log = Logging.GEOTOOLS;
        try {
            log.setLoggerFactory("org.geotools.util.logging.CommonsLoggerFactory");
        } catch (ClassNotFoundException commonsException) {
            try {
                log.setLoggerFactory("org.geotools.util.logging.Log4JLoggerFactory");
            } catch (ClassNotFoundException log4jException) {
                // Nothing to do, we already tried our best.
            }
        }
        // If java logging is used, force monoline console output.
        if (log.getLoggerFactory() == null) {
            log.forceMonolineConsoleOutput();
        }
        if (hints != null) {
            Hints.putSystemDefault(hints);
            // fireConfigurationChanged() is invoked in the above method call.
        }
    }

    /**
     * Forces the initial context for test cases, or as needed.
     *
     * @param applicationContext The initial context to use.
     *
     * @see #getInitialContext
     *
     * @since 2.4
     */
    public static void init(final InitialContext applicationContext) {
        synchronized (GeoTools.class) {
            context = applicationContext;
        }
        fireConfigurationChanged();
    }

    /**
     * Scans {@linkplain System#getProperties system properties} for any property keys
     * defined in this class, and add their values to the specified map of hints. For
     * example if the {@value #FORCE_LONGITUDE_FIRST_AXIS_ORDER} system property is
     * defined, then the {@link Hints#FORCE_LONGITUDE_FIRST_AXIS_ORDER
     * FORCE_LONGITUDE_FIRST_AXIS_ORDER} hint will be added to the set of hints.
     *
     * @return {@code true} if at least one hint changed as a result of this scan,
     *         or {@code false} otherwise.
     */
    static boolean scanForSystemHints(final Hints hints) {
        assert Thread.holdsLock(hints);
        boolean changed = false;
        synchronized (BINDINGS) {
            for (final Map.Entry<String, RenderingHints.Key> entry : BINDINGS.entrySet()) {
                final String propertyKey = entry.getKey();
                final String property;
                try {
                    property = System.getProperty(propertyKey);
                } catch (SecurityException e) {
                    unexpectedException(e);
                    continue;
                }
                if (property != null) {
                    /*
                     * Converts the system property value from String to Object (java.lang.Boolean
                     * or java.lang.Number). We perform this conversion only if the key is exactly
                     * of kind Hints.Key,  not a subclass like ClassKey, in order to avoid useless
                     * class loading on  'getValueClass()'  method invocation (ClassKey don't make
                     * sense for Boolean and Number, which are the only types that we convert here).
                     */
                    Object value = property;
                    final RenderingHints.Key hintKey = entry.getValue();
                    if (hintKey.getClass().equals(Hints.Key.class)) {
                        final Class<?> type = ((Hints.Key) hintKey).getValueClass();
                        if (type.equals(Boolean.class)) {
                            value = Boolean.valueOf(property);
                        } else if (Number.class.isAssignableFrom(type)) try {
                            value = Classes.valueOf(type, property);
                        } catch (NumberFormatException e) {
                            unexpectedException(e);
                            continue;
                        }
                    }
                    final Object old;
                    try {
                        old = hints.put(hintKey, value);
                    } catch (IllegalArgumentException e) {
                        // The property value is illegal for this hint.
                        unexpectedException(e);
                        continue;
                    }
                    if (!changed && !Utilities.equals(old, value)) {
                        changed = true;
                    }
                }
            }
        }
        return changed;
    }

    /**
     * Logs an exception as if it originated from {@link Hints#scanSystemProperties},
     * since it is the public API that may invokes this method.
     */
    private static void unexpectedException(final Exception exception) {
        Logging.unexpectedException(Hints.class, "scanSystemProperties", exception);
    }

    /**
     * Returns the default set of hints used for the various utility classes.
     * This default set is determined by:
     * <p>
     * <ul>
     *   <li>The {@linplain System#getProperties system properties} available. Some property
     *       keys are enumerated in the {@link GeoTools} class.</li>
     *   <li>Any hints added by call to the {@link Hints#putSystemDefault}
     *       or {@link #init} method.</li>
     * </ul>
     * <p>
     * <b>Long term plan:</b>
     * We would like to transition the utility classes to being injected with their
     * required factories, either by taking Hints as part of their constructor, or
     * otherwise. Making this change would be a three step process 1) create instance
     * methods for each static final class method 2) create an singleton instance of the
     * class 3) change each static final class method into a call to the singleton. With
     * this in place we could then encourage client code to make use of utility class
     * instances before eventually retiring the static final methods.
     *
     * @return A copy of the default hints. It is safe to add to it.
     */
    public static Hints getDefaultHints() {
        return Hints.getDefaults(false);
    }

    /**
     * Returns the default initial context.
     *
     * @param  hints An optional set of hints, or {@code null} if none.
     * @return The initial context (never {@code null}).
     * @throws NamingException if the initial context can't be created.
     *
     * @see #init(InitialContext)
     *
     * @since 2.4
     */
    public static synchronized InitialContext getInitialContext(final Hints hints)
            throws NamingException
    {
        if (context == null) {
            context = new InitialContext();
        }
        return context;
    }

    /**
     * Converts a GeoTools name to the syntax used by the {@linkplain #getInitialContext
     * GeoTools JNDI context}. Names may be constructed in a variety of ways depending on
     * the implementation of {@link InitialContext}. GeoTools uses {@code "jdbc:EPSG"}
     * internally, but many implementaitons use the form {@code "jdbc/EPSG"}. Calling
     * this method before use will set the name right.
     *
     * @param  name Name of the form {@code "jdbc:EPSG"}, or {@code null}.
     * @return Name fixed up with {@link Context#composeName(String,String)},
     *         or {@code null} if the given name was null.
     *
     * @since 2.4
     */
    public static String fixName(final String name) {
        return fixName(null, name, null);
    }

    /**
     * Converts a GeoTools name to the syntax used by the specified JNDI context.
     * This method is similar to {@link #fixName(String)}, but uses the specified
     * context instead of the GeoTools one.
     *
     * @param  context The context to use, or {@code null} if none.
     * @param  name Name of the form {@code "jdbc:EPSG"}, or {@code null}.
     * @return Name fixed up with {@link Context#composeName(String,String)},
     *         or {@code null} if the given name was null.
     *
     * @since 2.4
     */
    public static String fixName(final Context context, final String name) {
        return (context != null) ? fixName(context, name, null) : name;
    }

    /**
     * Implementation of {@code fixName} method. If the context is {@code null}, then
     * the {@linkplain #getInitialContext GeoTools initial context} will be fetch only
     * when first needed.
     */
    private static String fixName(Context context, final String name, final Hints hints) {
        String fixed = null;
        if (name != null) {
            final StringTokenizer tokens = new StringTokenizer(name, ":/");
            while (tokens.hasMoreTokens()) {
                final String part = tokens.nextToken();
                if (fixed == null) {
                    fixed = part;
                } else try {
                    if (context == null) {
                        context = getInitialContext(hints);
                    }
                    fixed = context.composeName(fixed, part);
                } catch (NamingException e) {
                    Logging.unexpectedException(GeoTools.class, "fixName", e);
                    return name;
                }
            }
        }
        return fixed;
    }

    /**
     * Adds an alternative way to search for factory implementations. {@link FactoryRegistry} has
     * a default mechanism bundled in it, which uses the content of all {@code META-INF/services}
     * directories found on the classpath. This {@code addFactoryIteratorProvider} method allows
     * to specify additional discovery algorithms. It may be useful in the context of some
     * frameworks that use the <cite>constructor injection</cite> pattern, like the
     * <a href="http://www.springframework.org/">Spring framework</a>.
     *
     * @param provider A new provider for factory iterators.
     */
    public static void addFactoryIteratorProvider(final FactoryIteratorProvider provider) {
        FactoryIteratorProviders.addFactoryIteratorProvider(provider);
    }

    /**
     * Removes a provider that was previously {@linkplain #addFactoryIteratorProvider added}.
     * Note that factories already obtained from the specified provider will not be
     * {@linkplain FactoryRegistry#deregisterServiceProvider deregistered} by this method.
     *
     * @param provider The provider to remove.
     */
    public static void removeFactoryIteratorProvider(final FactoryIteratorProvider provider) {
        FactoryIteratorProviders.removeFactoryIteratorProvider(provider);
    }

    /**
     * Adds the specified listener to the list of objects to inform when system-wide
     * configuration changed.
     *
     * @param listener The listener to add.
     */
    public static void addChangeListener(final ChangeListener listener) {
        removeChangeListener(listener); // Ensure singleton.
        LISTENERS.add(ChangeListener.class, listener);
    }

    /**
     * Removes the specified listener from the list of objects to inform when system-wide
     * configuration changed.
     *
     * @param listener The listener to remove.
     */
    public static void removeChangeListener(final ChangeListener listener) {
        LISTENERS.remove(ChangeListener.class, listener);
    }

    /**
     * Informs every listeners that system-wide configuration changed.
     */
    public static void fireConfigurationChanged() {
        final ChangeEvent event = new ChangeEvent(GeoTools.class);
        final Object[] listeners = LISTENERS.getListenerList();
        for (int i=0; i<listeners.length; i+=2) {
            if (listeners[i] == ChangeListener.class) {
                ((ChangeListener) listeners[i+1]).stateChanged(event);
            }
        }
    }

    /**
     * Reports the GeoTools {@linkplain #getVersion version} number to the
     * {@linkplain System#out standard output stream}.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        final Arguments arguments = new Arguments(args);
        args = arguments.getRemainingArguments(0);
        arguments.out.print("GeoTools version ");
        arguments.out.println(getVersion());
        final Hints hints = getDefaultHints();
        if (hints!=null && !hints.isEmpty()) {
            arguments.out.println(hints);
        }
    }
}
