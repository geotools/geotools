/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util.factory;

import java.awt.RenderingHints;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.xml.parsers.SAXParser;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.Errors;
import org.geotools.util.Arguments;
import org.geotools.util.Classes;
import org.geotools.util.NullEntityResolver;
import org.geotools.util.PreventLocalEntityResolver;
import org.geotools.util.Utilities;
import org.geotools.util.Version;
import org.geotools.util.logging.LoggerFactory;
import org.geotools.util.logging.Logging;
import org.xml.sax.EntityResolver;

/**
 * Static methods relative to the global GeoTools configuration. GeoTools can be configured in a
 * system-wide basis through {@linkplain System#getProperties system properties}, some of them are
 * declared as {@link String} constants in this class.
 *
 * <p>There are many aspects to the configuration of GeoTools:
 *
 * <ul>
 *   <li>Default Settings: Are handled as the Hints returned by {@link #getDefaultHints()}, the
 *       default values can be provided in application code, or specified using system properties.
 *   <li>Integration JNDI: Telling the GeoTools library about the facilities of an application, or
 *       application container takes several forms. This class provides the {@link
 *       #init(InitialContext)} method allowing to tell GeoTools about the JNDI context to use.
 *   <li>Integration Plugins: If hosting GeoTools in a alternate plugin system such as Spring or
 *       OSGi, application may needs to hunt down the {@code FactoryFinder}s and register additional
 *       "Factory Iterators" for GeoTools to search using the {@link #addFactoryIteratorProvider}
 *       method.
 * </ul>
 *
 * @since 2.4
 * @version $Id$
 * @author Jody Garnett
 * @author Martin Desruisseaux
 */
public final class GeoTools {
    /** Properties about this geotools build */
    private static final Properties PROPS;

    static {
        PROPS = loadProperites("GeoTools.properties");
    }

    private static Properties loadProperites(String resource) {
        Properties props = new Properties();
        InputStream stream = GeoTools.class.getResourceAsStream(resource);
        if (stream != null) {
            try {
                props.load(stream);
            } catch (IOException ignore) {
            } finally {
                try {
                    stream.close();
                } catch (IOException ignore) {
                }
            }
        }

        return props;
    }

    /** The current GeoTools version. The separator character must be the dot. */
    private static final Version VERSION = new Version(PROPS.getProperty("version", "20-SNAPSHOT"));

    /** The version control (svn) revision at which this version of geotools was built. */
    private static final String BUILD_REVISION;

    static {
        BUILD_REVISION = PROPS.getProperty("build.revision", "-1");
    }

    /** The timestamp at which this version of geotools was built. */
    private static final String BUILD_TIMESTAMP = PROPS.getProperty("build.timestamp", "");

    /**
     * Object to inform about system-wide configuration changes. We use the Swing utility listener
     * list since it is lightweight and thread-safe. Note that it doesn't involve any dependency to
     * the remaining of Swing library.
     */
    private static final EventListenerList LISTENERS = new EventListenerList();

    /**
     * The bindings between {@linkplain System#getProperties system properties} and a hint key. This
     * field must be declared before any call to the {@link #bind} method.
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
     * The {@linkplain System#getProperty(String) system property} key for the default value to be
     * assigned to the {@link Hints#EPSG_DATA_SOURCE EPSG_DATA_SOURCE} hint.
     *
     * @see Hints#EPSG_DATA_SOURCE
     * @see #getDefaultHints
     */
    public static final String EPSG_DATA_SOURCE = "org.geotools.referencing.epsg-datasource";

    static {
        bind(EPSG_DATA_SOURCE, Hints.EPSG_DATA_SOURCE);
    }

    /**
     * The {@linkplain System#getProperty(String) system property} key for the default value to be
     * assigned to the {@link Hints#FORCE_LONGITUDE_FIRST_AXIS_ORDER
     * FORCE_LONGITUDE_FIRST_AXIS_ORDER} hint.
     *
     * <p>This setting can provide a transition path for projects expecting a (<var>longitude</var>,
     * <var>latitude</var>) axis order on a system-wide level. Application developpers can set the
     * default value as below:
     *
     * <blockquote>
     *
     * <pre>
     * System.setProperty(FORCE_LONGITUDE_FIRST_AXIS_ORDER, "true");
     * </pre>
     *
     * </blockquote>
     *
     * Note that this system property applies mostly to the default EPSG factory. Most other
     * factories ({@code "CRS"}, {@code "AUTO"}, <cite>etc.</cite>) don't need this property since
     * they use (<var>longitude</var>, <var>latitude</var>) axis order by design.
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
     * The {@linkplain System#getProperty(String) system property} key for the default value to be
     * assigned to the {@link Hints# ENTITY_RESOLVER} hint.
     *
     * <p>This setting specifies the XML Entity resolver to be used when configuring a SAXParser
     *
     * @see Hints#ENTITY_RESOLVER
     * @see #getDefaultHints
     */
    public static final String ENTITY_RESOLVER = "org.xml.sax.EntityResolver";

    static {
        bind(ENTITY_RESOLVER, Hints.ENTITY_RESOLVER);
    }

    /**
     * The {@linkplain System#getProperty(String) system property} key for the default value to be
     * assigned to the {@link Hints# RESAMPLE_TOLERANCE} hint.
     *
     * <p>This setting specifies the tolerance used when linearizing warp transformation into
     * piecewise linear ones, by default it is 0.333 pixels
     *
     * @see Hints#RESAMPLE_TOLERANCE
     * @see #getDefaultHints
     */
    public static final String RESAMPLE_TOLERANCE = "org.geotools.referencing.resampleTolerance";

    static {
        bind(RESAMPLE_TOLERANCE, Hints.RESAMPLE_TOLERANCE);
    }

    /**
     * The {@linkplain System#getProperty(String) system property} key for the default value to be
     * assigned to the {@link Hints#LOCAL_DATE_TIME_HANDLING} hint.
     *
     * <p>This setting specifies if dates shall be treated as local dates ignoring time zones.
     *
     * @see Hints#LOCAL_DATE_TIME_HANDLING
     * @see #getDefaultHints
     * @since 15.0
     */
    public static final String LOCAL_DATE_TIME_HANDLING = "org.geotools.localDateTimeHandling";

    static {
        bind(LOCAL_DATE_TIME_HANDLING, Hints.LOCAL_DATE_TIME_HANDLING);
    }

    /**
     * The {@linkplain System#getProperty(String) system property} key for the default value to be
     * assigned to the {@link Hints#DATE_TIME_FORMAT_HANDLING} hint.
     *
     * <p>This setting specifies if GML 2 temporal data shall be formatted using same approach as
     * GML 3+.
     *
     * @see Hints#DATE_TIME_FORMAT_HANDLING
     * @see #getDefaultHints
     * @since 21.0
     */
    public static final String DATE_TIME_FORMAT_HANDLING = "org.geotools.dateTimeFormatHandling";

    static {
        bind(DATE_TIME_FORMAT_HANDLING, Hints.DATE_TIME_FORMAT_HANDLING);
    }

    /**
     * The {@linkplain System#getProperty(String) system property} key for the default value to be
     * assigned to the {@link Hints#ENCODE_EWKT} hint.
     *
     * <p>This setting specifies if geometries with {@link
     * org.opengis.referencing.crs.CoordinateReferenceSystem} in the user data shall be encoded as
     * EWKT or not.
     *
     * @see Hints#ENCODE_EWKT
     * @see #getDefaultHints
     * @since 19.0
     */
    public static final String ENCODE_WKT = "org.geotools.ecql.ewkt";

    static {
        bind(ENCODE_WKT, Hints.ENCODE_EWKT);
    }

    /** The initial context. Will be created only when first needed. */
    private static InitialContext context;

    /**
     * Class loaders to be added to the list in ${link {@link FactoryRegistry#getClassLoaders()}}
     * which are used to look-up plug-ins. Class loaders are added via {@link
     * #addClassLoader(ClassLoader)}
     */
    private static final Set<ClassLoader> addedClassLoaders =
            Collections.synchronizedSet(new HashSet<ClassLoader>());

    /** Do not allow instantiation of this class. */
    private GeoTools() {}

    /**
     * Binds the specified {@linkplain System#getProperty(String) system property} to the specified
     * key. Only one key can be binded to a given system property. However the same key can be
     * binded to more than one system property names, in which case the extra system property names
     * are aliases.
     *
     * @param property The system property.
     * @param key The key to bind to the system property.
     * @throws IllegalArgumentException if an other key is already bounds to the given system
     *     property.
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
        throw new IllegalArgumentException(
                Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "property", property));
    }

    /**
     * Returns summary information about GeoTools and the current environment. Calls {@linkplain
     * #getEnvironmentInfo()} followed by {@linkplain #getGeoToolsJarInfo()} and concatenates their
     * results.
     *
     * @return requested information as a string
     */
    public static String getAboutInfo() {
        final StringBuilder sb = new StringBuilder();

        sb.append(getEnvironmentInfo());
        sb.append(String.format("%n"));
        sb.append(getGeoToolsJarInfo());

        return sb.toString();
    }

    /**
     * Returns summary information about the GeoTools version and the host environment.
     *
     * @return information as a String
     */
    public static String getEnvironmentInfo() {
        final String newline = String.format("%n");

        final StringBuilder sb = new StringBuilder();
        sb.append("GeoTools version ").append(getVersion().toString());
        if (sb.toString().endsWith("SNAPSHOT")) {
            sb.append(" (built from r").append(getBuildRevision().toString()).append(")");
        }

        sb.append(newline).append("Java version: ");
        sb.append(System.getProperty("java.version"));

        sb.append(newline).append("Operating system: ");
        sb.append(System.getProperty("os.name"))
                .append(' ')
                .append(System.getProperty("os.version"));

        return sb.toString();
    }

    /**
     * Returns the names of the GeoTools jars on the classpath.
     *
     * @return list of jars as a formatted string
     */
    public static String getGeoToolsJarInfo() {
        final StringBuilder sb = new StringBuilder();
        final String newline = String.format("%n");
        final String indent = "    ";

        sb.append("GeoTools jars on classpath:");
        for (String jarName : getGeoToolsJars()) {
            sb.append(newline).append(indent).append(jarName);
        }

        return sb.toString();
    }

    /**
     * A helper method for {@linkplain #getGeoToolsJarInfo} which scans the classpath looking for
     * GeoTools jars matching the current version.
     *
     * @return a list of jar names
     */
    private static List<String> getGeoToolsJars() {
        final Pattern pattern = Pattern.compile(".*\\/" + getVersion() + "\\/(gt-.*jar$)");
        final List<String> jarNames = new ArrayList<String>();

        String pathSep = System.getProperty("path.separator");
        String classpath = System.getProperty("java.class.path");
        StringTokenizer st = new StringTokenizer(classpath, pathSep);
        while (st.hasMoreTokens()) {
            String path = st.nextToken();
            Matcher matcher = pattern.matcher(path);
            if (matcher.find()) {
                jarNames.add(matcher.group(1));
            }
        }

        Collections.sort(jarNames);
        return jarNames;
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
     * Returns the raw properties object containing all properties about this GeoTools build.
     *
     * <p>Example from the 14.3 release:
     *
     * <ul>
     *   <li>version=14.3
     *   <li>build.revision=2298d56000bef6f526b521a480316ea544c74571
     *   <li>build.branch=rel_14.3
     *   <li>build.timestamp=21-Mar-2016 21:30
     * </ul>
     */
    public static Properties getBuildProperties() {
        Properties props = new Properties();
        props.putAll(PROPS);
        return props;
    }

    /**
     * Reports back the version of GeoTools being used.
     *
     * @return The current GeoTools version.
     */
    public static Version getVersion() {
        return VERSION;
    }

    /**
     * Lookup version for provided class.
     *
     * <p>Version number is determined by either:
     *
     * <ul>
     *   <li>Use of jar naming convention, matching jars such as jts-1.13.jar
     *   <li>Use of MANIFEST.MF (to check Implementation-Version, Project-Version)
     *   <li>
     *   <li>To assist
     *
     * @return Version (or null if unavailable)
     */
    public static Version getVersion(Class<?> type) {
        final URL classLocation = classLocation(type);
        String path = classLocation.toString();

        // try and extract from maven jar naming convention
        if (classLocation.getProtocol().equalsIgnoreCase("jar")) {
            String jarVersion = jarVersion(path);
            if (jarVersion != null) {
                return new Version(jarVersion);
            }
            // try manifest
            try {
                URL manifestLocation = manifestLocation(path);
                Manifest manifest = new Manifest();
                try (InputStream content = manifestLocation.openStream()) {
                    manifest.read(content);
                }
                for (String attribute :
                        new String[] {
                            "Implementation-Version", "Project-Version", "Specification-Version"
                        }) {
                    String value = manifest.getMainAttributes().getValue(attribute);
                    if (value != null) {
                        return new Version(value);
                    }
                }
            } catch (IOException e) {
                // unavailable
            }
        }
        String name = type.getName();
        if (name.startsWith("org.geotools") || name.startsWith("org.opengis")) {
            return GeoTools.getVersion();
        }
        return null;
    }

    /**
     * Class location.
     *
     * @return class location
     */
    static URL classLocation(Class<?> type) {
        return type.getResource(type.getSimpleName() + ".class");
    }

    /**
     * Determine jar version from static analysis of classLocation path.
     *
     * @return jar version, or null if unknown
     */
    static String jarVersion(String classLocation) {
        if (classLocation.startsWith("jar:") || classLocation.contains(".jar!")) {
            String location = classLocation.substring(0, classLocation.lastIndexOf("!") + 1);
            String file = location.substring(location.lastIndexOf(File.pathSeparator) + 1);
            int dash = file.lastIndexOf("-");
            int dot = file.lastIndexOf(".jar");
            if (dash != -1 && dot != -1) {
                String version = file.substring(dash + 1, dot);
                if (version.startsWith("RC") || version.equals("SNAPSHOT")) {
                    dash = file.lastIndexOf("-", dash - 1);
                    version = file.substring(dash + 1, dot);
                }
                return version;
            }
        }
        // handle custom protocols such as jboss "vfs:" or OSGi "resource"
        if (classLocation.contains(".jar/")) {
            String location = classLocation.substring(0, classLocation.indexOf(".jar/") + 4);
            int dash = location.lastIndexOf("-");
            int dot = location.lastIndexOf(".jar");

            if (dash != -1 && dot != -1) {
                return location.substring(dash + 1, dot);
            }
        }
        return null;
    }

    /**
     * Generate URL of MANIFEST.MF file for provided class location.
     *
     * @return MANIFEST.MF location, or null if unknown
     */
    static URL manifestLocation(String classLocation) {
        URL url;
        if (classLocation.startsWith("jar:")) {
            try {
                url =
                        new URL(
                                classLocation.substring(0, classLocation.lastIndexOf("!") + 1)
                                        + "/META-INF/MANIFEST.MF");
                return url;
            } catch (MalformedURLException e) {
                return null;
            }
        }
        // handle custom protocols such as jboss "vfs:" or OSGi "resource"
        if (classLocation.contains(".jar/")) {
            String location = classLocation.substring(0, classLocation.indexOf(".jar/") + 4);
            try {
                url = new URL(location + "/META-INF/MANIFEST.MF");
                return url;
            } catch (MalformedURLException e) {
                return null;
            }
        }
        return null;
    }
    /**
     * Lookup the MANIFEST.MF for the provided class.
     *
     * <p>This can be used to quickly verify packaging information.
     *
     * @return MANIFEST.MF contents, please note contents may be empty when running from IDE
     */
    public static Manifest getManifest(Class<?> type) {
        final URL classLocation = classLocation(type);
        Manifest manifest = new Manifest();

        URL manifestLocation = manifestLocation(classLocation.toString());
        if (manifestLocation != null) {
            try {
                try (InputStream content = manifestLocation.openStream()) {
                    manifest.read(content);
                }
            } catch (IOException ignore) {
            }
        }
        if (manifest.getMainAttributes().isEmpty()) {
            // must be running in IDE
            String name = type.getName();
            if (name.startsWith("org.geotools")
                    || name.startsWith("org.opengis")
                    || name.startsWith("net.opengis")) {
                String generated =
                        "Manifest-Version: 1.0\n" + "Project-Version: " + getVersion() + "\n";

                try {
                    manifest.read(new ByteArrayInputStream(generated.getBytes()));
                } catch (IOException e) {
                }
            }
        }
        return manifest;
    }
    /**
     * Sets the global {@linkplain LoggerFactory logger factory}.
     *
     * <p>This method is the same as {@code Logging.GEOTOOLS.setLoggerFactory(factory)}. GeoTools
     * ships with support for <A
     * HREF="http://jakarta.apache.org/commons/logging/">Commons-logging</A> and <A
     * HREF="http://logging.apache.org/log4j/">log4j</A>. This method exists to allow you supply
     * your own implementation (this is sometimes required when using a GeoTools application in an
     * exotic environment like Eclipse, OC4J or your application).
     *
     * @param factory The logger factory to use.
     * @see Logging#setLoggerFactory(LoggerFactory)
     * @since 2.4
     */
    public void setLoggerFactory(final LoggerFactory<?> factory) {
        Logging.GEOTOOLS.setLoggerFactory(factory);
    }

    /**
     * Initializes GeoTools for use. This convenience method performs various tasks (more may be
     * added in the future), including setting up the {@linkplain java.util.logging Java logging
     * framework} in one of the following states:
     *
     * <p>
     *
     * <ul>
     *   <li>If the <A HREF="http://jakarta.apache.org/commons/logging/">Commons-logging</A>
     *       framework is available, then every logging message in the {@code org.geotools}
     *       namespace sent to the Java {@linkplain java.util.logging.Logger logger} are redirected
     *       to Commons-logging.
     *   <li>Otherwise if the <A HREF="http://logging.apache.org/log4j">Log4J</A> framework is
     *       available, then every logging message in the {@code org.geotools} namespace sent to the
     *       Java {@linkplain java.util.logging.Logger logger} are redirected to Log4J.
     *   <li>Otherwise, the Java logging {@linkplain java.util.logging.Formatter formatter} for
     *       console output is replaced by a {@linkplain org.geotools.util.logging.MonolineFormatter
     *       monoline formatter}.
     * </ul>
     *
     * <p>In addition, the {@linkplain #getDefaultHints default hints} are initialized to the
     * specified {@code hints}.
     *
     * <p>Invoking this method is <strong>not</strong> required fpr the GeoTools library to
     * function. It is just a convenience method for overwriting select Java and GeoTools default
     * settings. Supplying these defaults is not desirable in all settings, such as writing test
     * cases.
     *
     * <p>Example of typical invocation in a GeoServer environment:
     *
     * <pre><code>
     * Hints hints = new Hints();
     * hints.put({@linkplain Hints#FORCE_LONGITUDE_FIRST_AXIS_ORDER}, Boolean.TRUE);
     * hints.put({@linkplain Hints#FORCE_AXIS_ORDER_HONORING}, "http");
     * GeoTools.init(hints);
     * </code></pre>
     *
     * @param hints The hints to use.
     * @see Logging#setLoggerFactory(String)
     * @see Logging#forceMonolineConsoleOutput
     * @see Hints#putSystemDefault
     * @see #getDefaultHints
     */
    public static void init(final Hints hints) {
        init();
        if (hints != null) {
            // This will trigger fireConfigurationChanged()
            Hints.putSystemDefault(hints);
        }
    }
    /**
     * Initializes GeoTools for use. This convenience method performs various tasks (more may be
     * added in the future), including setting up the {@linkplain java.util.logging Java logging
     * framework} in one of the following states:
     *
     * <p>
     *
     * <ul>
     *   <li>If the <A HREF="http://jakarta.apache.org/commons/logging/">Commons-logging</A>
     *       framework is available, then every logging message in the {@code org.geotools}
     *       namespace sent to the Java {@linkplain java.util.logging.Logger logger} are redirected
     *       to Commons-logging.
     *   <li>Otherwise if the <A HREF="http://logging.apache.org/log4j">Log4J</A> framework is
     *       available, then every logging message in the {@code org.geotools} namespace sent to the
     *       Java {@linkplain java.util.logging.Logger logger} are redirected to Log4J.
     *   <li>Otherwise, the Java logging {@linkplain java.util.logging.Formatter formatter} for
     *       console output is replaced by a {@linkplain org.geotools.util.logging.MonolineFormatter
     *       monoline formatter}.
     * </ul>
     *
     * <p>Invoking this method is <strong>not</strong> required fpr the GeoTools library to
     * function. It is just a convenience method for overwriting select Java and GeoTools default
     * settings. Supplying these defaults is not desirable in all settings, such as writing test
     * cases.
     *
     * <p>
     *
     * @see Logging#setLoggerFactory(String)
     * @see Logging#forceMonolineConsoleOutput
     * @see Hints#putSystemDefault
     * @see #getDefaultHints
     */
    public static void init() {
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
    }
    /**
     * Provides GeoTools with the JNDI context for resource lookup.
     *
     * @param applicationContext The initial context to use.
     * @see #getInitialContext
     * @since 2.4
     */
    public static void init(final InitialContext applicationContext) {
        synchronized (GeoTools.class) {
            context = applicationContext;
        }
        fireConfigurationChanged();
    }

    /**
     * Scans {@linkplain System#getProperties system properties} for any property keys defined in
     * this class, and add their values to the specified map of hints. For example if the {@value
     * #FORCE_LONGITUDE_FIRST_AXIS_ORDER} system property is defined, then the {@link
     * Hints#FORCE_LONGITUDE_FIRST_AXIS_ORDER FORCE_LONGITUDE_FIRST_AXIS_ORDER} hint will be added
     * to the set of hints.
     *
     * @return {@code true} if at least one hint changed as a result of this scan, or {@code false}
     *     otherwise.
     */
    static boolean scanForSystemHints(final Map<RenderingHints.Key, Object> hints) {
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
                        } else if (Number.class.isAssignableFrom(type))
                            try {
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
     * Logs an exception as if it originated from {@link Hints#scanSystemProperties}, since it is
     * the public API that may invokes this method.
     */
    private static void unexpectedException(final Exception exception) {
        Logging.unexpectedException(Hints.class, "scanSystemProperties", exception);
    }

    /**
     * Returns the default set of hints used for the various utility classes. This default set is
     * determined by:
     *
     * <p>
     *
     * <ul>
     *   <li>The {@linplain System#getProperties system properties} available. Some property keys
     *       are enumerated in the {@link GeoTools} class.
     *   <li>Any hints added by call to the {@link Hints#putSystemDefault} or {@link #init} method.
     * </ul>
     *
     * <p><b>Long term plan:</b> We would like to transition the utility classes to being injected
     * with their required factories, either by taking Hints as part of their constructor, or
     * otherwise. Making this change would be a three step process 1) create instance methods for
     * each static final class method 2) create an singleton instance of the class 3) change each
     * static final class method into a call to the singleton. With this in place we could then
     * encourage client code to make use of utility class instances before eventually retiring the
     * static final methods.
     *
     * @return A copy of the default hints. It is safe to add to it.
     */
    public static Hints getDefaultHints() {
        return Hints.getDefaults(false);
    }
    /** Used to combine provided hints with global GeoTools defaults. */
    public static Hints addDefaultHints(final Hints hints) {
        final Hints completed = getDefaultHints();
        if (hints != null) {
            completed.add(hints);
        }
        return completed;
    }

    /**
     * Returns the default entity resolver, used to configure {@link SAXParser}.
     *
     * @param hints An optional set of hints, or {@code null} if none, see {@link
     *     Hints#ENTITY_RESOLVER}.
     * @return An entity resolver (never {@code null})
     */
    public static EntityResolver getEntityResolver(Hints hints) {
        if (hints == null) {
            hints = getDefaultHints();
        }
        if (hints.containsKey(Hints.ENTITY_RESOLVER)) {
            Object hint = hints.get(Hints.ENTITY_RESOLVER);
            if (hint == null) {
                return NullEntityResolver.INSTANCE;
            } else if (hint instanceof EntityResolver) {
                return (EntityResolver) hint;
            } else if (hint instanceof String) {
                String className = (String) hint;
                return instantiate(
                        className, EntityResolver.class, PreventLocalEntityResolver.INSTANCE);
            }
        }
        return PreventLocalEntityResolver.INSTANCE;
    }

    /**
     * Create instance of className (or access singleton INSTANCE field).
     *
     * @param className Class name to instantiate
     * @param type Class of object created
     * @param defaultValue Default to be provided, may be null
     * @return EntityResolver, defaults to {@link PreventLocalEntityResolver#INSTANCE} if
     *     unavailable.
     */
    static <T, D extends T> T instantiate(String className, Class<T> type, D defaultValue) {
        if (className == null) {
            return defaultValue;
        }
        final Logger LOGGER = Logging.getLogger(GeoTools.class);
        try {
            Class<?> kind = Class.forName(className);
            // step 1 look for instance field
            for (Field field : kind.getDeclaredFields()) {
                int modifier = field.getModifiers();
                if ("INSTANCE".equals(field.getName())
                        && Modifier.isStatic(modifier)
                        && Modifier.isPublic(modifier)) {
                    try {
                        Object value = field.get(null);
                        if (value != null && value instanceof EntityResolver) {
                            return type.cast(value);
                        } else {
                            LOGGER.log(
                                    Level.FINER,
                                    "Unable to use ENTITY_RESOLVER: " + className + ".INSTANCE");
                        }
                    } catch (Throwable t) {
                        LOGGER.log(
                                Level.FINER,
                                "Unable to instantiate ENTITY_RESOLVER: " + className + ".INSTANCE",
                                t);
                    }
                    return defaultValue;
                }
            }
            // step 2 no argument constructor
            try {
                Object value = kind.getDeclaredConstructor().newInstance();
                if (type.isInstance(value)) {
                    return type.cast(value);
                }
            } catch (InstantiationException
                    | IllegalAccessException
                    | NoSuchMethodException
                    | InvocationTargetException e) {
                LOGGER.log(
                        Level.FINER, "Unable to instantiate ENTITY_RESOLVER: " + e.getMessage(), e);
            }
        } catch (ClassNotFoundException notFound) {
            LOGGER.log(
                    Level.FINER,
                    "Unable to instantiate ENTITY_RESOLVER: " + notFound.getMessage(),
                    notFound);
        }
        return defaultValue;
    }
    /**
     * Returns the default initial context.
     *
     * @param hints An optional set of hints, or {@code null} if none.
     * @return The initial context (never {@code null}).
     * @throws NamingException if the initial context can't be created.
     * @see #init(InitialContext)
     * @since 2.4
     */
    public static synchronized InitialContext getInitialContext(final Hints hints)
            throws NamingException {
        if (context == null) {
            context = new InitialContext();
        }
        return context;
    }

    /**
     * Clears the initial context (closes it if not null)
     *
     * @since 15.0
     */
    public static synchronized void clearInitialContext() throws NamingException {
        if (context != null) {
            context.close();
        }
        context = null;
    }

    /**
     * Converts a GeoTools name to the syntax used by the {@linkplain #getInitialContext GeoTools
     * JNDI context}. Names may be constructed in a variety of ways depending on the implementation
     * of {@link InitialContext}. GeoTools uses {@code "jdbc:EPSG"} internally, but many
     * implementaitons use the form {@code "jdbc/EPSG"}. Calling this method before use will set the
     * name right.
     *
     * @param name Name of the form {@code "jdbc:EPSG"}, or {@code null}.
     * @return Name fixed up with {@link Context#composeName(String,String)}, or {@code null} if the
     *     given name was null.
     * @since 2.4
     */
    public static String fixName(final String name) {
        return fixName(null, name, null);
    }

    /**
     * Converts a GeoTools name to the syntax used by the specified JNDI context. This method is
     * similar to {@link #fixName(String)}, but uses the specified context instead of the GeoTools
     * one.
     *
     * @param context The context to use, or {@code null} if none.
     * @param name Name of the form {@code "jdbc:EPSG"}, or {@code null}.
     * @return Name fixed up with {@link Context#composeName(String,String)}, or {@code null} if the
     *     given name was null.
     * @since 2.4
     */
    public static String fixName(final Context context, final String name) {
        return (context != null) ? fixName(context, name, null) : name;
    }

    /**
     * Implementation of {@code fixName} method. If the context is {@code null}, then the
     * {@linkplain #getInitialContext GeoTools initial context} will be fetch only when first
     * needed.
     */
    private static String fixName(Context context, final String name, final Hints hints) {
        String fixed = null;
        if (name != null) {
            final StringTokenizer tokens = new StringTokenizer(name, ":/");
            while (tokens.hasMoreTokens()) {
                final String part = tokens.nextToken();
                if (fixed == null) {
                    fixed = part;
                } else
                    try {
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
     * Adds an alternative way to search for factory implementations. {@link FactoryRegistry} has a
     * default mechanism bundled in it, which uses the content of all {@code META-INF/services}
     * directories found on the classpath. This {@code addFactoryIteratorProvider} method allows to
     * specify additional discovery algorithms. It may be useful in the context of some frameworks
     * that use the <cite>constructor injection</cite> pattern, like the <a
     * href="http://www.springframework.org/">Spring framework</a>.
     *
     * @param provider A new provider for factory iterators.
     */
    public static void addFactoryIteratorProvider(final FactoryIteratorProvider provider) {
        FactoryIteratorProviders.addFactoryIteratorProvider(provider);
    }

    /**
     * Removes a provider that was previously {@linkplain #addFactoryIteratorProvider added}. Note
     * that factories already obtained from the specified provider will not be {@linkplain
     * FactoryRegistry#deregisterFactory deregistered} by this method.
     *
     * @param provider The provider to remove.
     */
    public static void removeFactoryIteratorProvider(final FactoryIteratorProvider provider) {
        FactoryIteratorProviders.removeFactoryIteratorProvider(provider);
    }

    /**
     * Adds the specified listener to the list of objects to inform when system-wide configuration
     * changed.
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

    /** Informs every listeners that system-wide configuration changed. */
    public static void fireConfigurationChanged() {
        final ChangeEvent event = new ChangeEvent(GeoTools.class);
        final Object[] listeners = LISTENERS.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == ChangeListener.class) {
                ((ChangeListener) listeners[i + 1]).stateChanged(event);
            }
        }
    }

    /**
     * Adds a class loader to be included in the list of class loaders that are used to locate
     * GeoTools plug-ins.
     *
     * <p>Client code that calls this method may also need to call {@link
     * FactoryRegistry#scanForPlugins()} on any existing registry to force it to clear its cache and
     * use the added class loader to locate plugins.
     *
     * @param classLoader The class loader.
     */
    public static void addClassLoader(ClassLoader classLoader) {
        addedClassLoaders.add(classLoader);
        fireConfigurationChanged();
    }

    /** Returns the class loaders added via {@link #addClassLoader(ClassLoader)}. */
    static Set<ClassLoader> getClassLoaders() {
        return addedClassLoaders;
    }

    /**
     * Reports the GeoTools {@linkplain #getVersion version} number to the {@linkplain System#out
     * standard output stream}.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        final Arguments arguments = new Arguments(args);
        arguments.getRemainingArguments(0);
        arguments.out.print("GeoTools version ");
        arguments.out.println(getVersion());
        final Hints hints = getDefaultHints();
        if (hints != null && !hints.isEmpty()) {
            arguments.out.println(hints);
        }
    }
}
