/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ows;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.geotools.util.factory.FactoryCreator;
import org.geotools.util.factory.FactoryRegistry;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.logging.Logging;

/**
 * Scans for all available URLChecker implementations, allows to register new ones, and provides a
 * convenient method to apply them on a given location.
 */
public class URLCheckers {
    protected static final Logger LOGGER = Logging.getLogger(URLCheckers.class);

    /** Regular expression to find escaped periods/slashes in the URI before the query/fragment. */
    private static final Pattern ILLEGAL_ESCAPES = Pattern.compile("^[^?#]*%2[ef].*$", Pattern.CASE_INSENSITIVE);

    /** The service registry for {@link URLCheckers}. Will be initialized only when first needed. */
    private static FactoryRegistry registry;

    private static FactoryRegistry getServiceRegistry() {
        synchronized (URLChecker.class) {
            if (registry == null) {
                registry = new FactoryCreator(URLChecker.class);
            }
        }
        return registry;
    }

    /** Re-initializes all static state, in particular, the {@link URLChecker} service registry */
    public static synchronized void reset() {
        if (registry == null) {
            // nothing to do
            return;
        }
        registry.deregisterAll();
        registry.scanForPlugins();
    }

    /**
     * Dynamically register a new URLChecker. The operation removes all instances of the same
     * classes already present in the registry, if multiple instances are needed, one can use
     * subclasses (eventually, anonymous inner classes) so that the instances are not sharing
     * exactly the same class.
     */
    public static void register(URLChecker checker) {
        getServiceRegistry().registerFactory(checker, URLChecker.class);
    }

    /**
     * Dynamically removes a new URLChecker, that might have been registered using the {@link
     * #register(URLChecker)} method.
     */
    public static void deregister(URLChecker checker) {
        getServiceRegistry().deregisterFactory(checker, URLChecker.class);
    }

    /** @return enabled list of org.geotools.data.ows.URLChecker implementations */
    public static List<URLChecker> getEnabledURLCheckers() {
        return getServiceRegistry()
                .getFactories(URLChecker.class, null, GeoTools.getDefaultHints())
                .filter(u -> u.isEnabled())
                .collect(Collectors.toList());
    }

    /**
     * Confirm the URL against all enabled URLChecker
     *
     * @param url to confirm using all available URLCheckers
     * @throws URLCheckerException if the URL is not allowed for use
     */
    public static void confirm(URL url) throws URLCheckerException {
        confirm(url.toExternalForm());
    }

    /**
     * Confirms URI against all enabled URLChecker
     *
     * @param uri to evaluate using all available URLCheckers
     * @throws URLCheckerException if the URI is not allowed for use
     */
    public static void confirm(URI uri) throws URLCheckerException {
        confirm(uri.toString());
    }

    /**
     * Normalize location removing current directory {@code .} and parent directory {@code ..}
     * names.
     *
     * <p>Normalization uses {@link URI#normalize()} for URI and URL locations. Path locations are
     * normalized using {@link Path#normalize()}.
     *
     * <p>This normalization method forces empty hostname {@code file:///path} representation to
     * provide a consistent location to check. RFC 8089 supports both empty hostname {@code
     * file:///path} and {@code file:/path} no hostname as references to the same absolute path.
     *
     * <p>Keep in mind that file paths are standardized to {@code /} on linux and {@code \\} on
     * windows.
     *
     * @param location String(URI/URI/path)
     * @return normalized location, removing redundant {@code .} and {@code ..} path names if
     *     required
     */
    public static String normalize(String location) {
        if (location.indexOf('.') == -1) {
            return location;
        }
        boolean error = false;
        if (location.indexOf(':') != -1) {
            try {
                URI uri = new URI(location);
                URI normal = uri.normalize();
                if (normal.getScheme().equalsIgnoreCase("file") && normal.getHost() == null) {
                    // Standardize on empty hostname representation supported by RFC 8089
                    // Although file:/path is valid normalize as file:///path representation
                    normal = new URI(
                            normal.getScheme(),
                            normal.getUserInfo(),
                            "",
                            normal.getPort(),
                            normal.getPath(),
                            normal.getQuery(),
                            normal.getFragment());
                }
                return normal.toString();
            } catch (URISyntaxException e) {
                error = true;
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("URI.normalize() not available for location: " + location);
                }
            }
        }
        if (location.indexOf('/') != -1 || location.indexOf('\\') != -1) {
            try {
                Path path = Paths.get(location);
                return path.normalize().toString();
            } catch (InvalidPathException invalid) {
                error = true;
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Path.normalize() not available for location: " + location);
                }
            }
        }
        if (error) {
            throw new URLCheckerException("Unable to normalize location: " + location);
        }
        return location;
    }

    /**
     * Confirm the location against all enabled URLChecker.
     *
     * @param location String(URI/URI/path) to be normalized and evaluated using all available
     *     URLCheckers
     * @throws URLCheckerException if the location is not allowed for use
     */
    public static void confirm(String location) throws URLCheckerException {
        // get enabled URLChecker only
        List<URLChecker> checkers = getEnabledURLCheckers();
        // no enabled checkers, the system is not configured... don't do anything
        if (checkers.isEmpty()) return;

        if (location.indexOf('%') != -1) {
            try {
                URLDecoder.decode(location, "UTF-8");
            } catch (Exception e) {
                throw new URLCheckerException("The location could not be URL decoded: " + location);
            }
            if (ILLEGAL_ESCAPES.matcher(location).matches()) {
                throw new URLCheckerException(
                        "The location contains escape sequences that are not allowed: " + location);
            }
        }
        String normalized = normalize(location);

        // evaluate using all available implementations
        for (URLChecker urlChecker : checkers) {
            if (urlChecker.confirm(normalized)) return;
        }
        // no URLChecker evaluated the passed URL/URI/Path
        throw new URLCheckerException("Evaluation Failure: '" + location + "' was not accepted by external URL checks");
    }
}
