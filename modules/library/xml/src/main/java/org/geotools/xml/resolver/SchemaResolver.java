/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.xml.resolver;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * XML Schema resolver that maps absolute URLs to local URL resources.
 *
 * <p>Resources are sought, in order:
 *
 * <ol>
 *   <li>In an <a href="http://www.oasis-open.org/committees/entity/spec-2001-08-06.html">OASIS
 *       Catalog</a> (with URI resolution semantics), which maps URLs to arbitrary filesystem
 *       locations.
 *   <li>On the classpath, where resources are located by their Simple HTTP Resource Path (see
 *       {@link #getSimpleHttpResourcePath(URI)}).
 *   <li>In a cache, with optional downloading support.
 * </ol>
 *
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 */
public class SchemaResolver {

    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(SchemaResolver.class);

    /** A local OASIS catalog (null if not present). */
    private SchemaCatalog catalog;

    /** True if schemas can be resolved on the classpath. */
    private boolean classpath = true;

    /** Cache of schemas with optional downloading support(null if not present). */
    private SchemaCache cache;

    /**
     * Maps a resolved location (a URL used to obtain a schema from a file or the classpath) to the
     * original HTTP URL used to obtain it. This is required so that relative imports can be
     * resolved if they cross resolution boundaries. For example, an import ../../../om/.. used to
     * import om in a schema, where one is supplied locally and the other must be downloaded and
     * cached. Another example is when the schemas are in different jar files.
     */
    private Map<String, String> resolvedLocationToOriginalLocationMap =
            new ConcurrentHashMap<String, String>();

    /**
     * Constructor.
     *
     * @param classpath whether schemas can be located on the classpath
     */
    public SchemaResolver(SchemaCatalog catalog, boolean classpath, SchemaCache cache) {
        this.catalog = catalog;
        this.classpath = classpath;
        this.cache = cache;
    }

    /** Constructor. */
    public SchemaResolver(SchemaCatalog catalog, SchemaCache cache) {
        this(catalog, true, cache);
    }

    /** Convenience constructor for a resolver with neither catalog nor cache (just classpath). */
    public SchemaResolver() {
        this(null, null);
    }

    /** Convenience constructor for a resolver with no cache. */
    public SchemaResolver(SchemaCatalog catalog) {
        this(catalog, null);
    }

    /** Convenience constructor for a resolver with no catalog. */
    public SchemaResolver(SchemaCache cache) {
        this(null, cache);
    }

    /**
     * Resolve an absolute or relative URL to a local file or jar URL. Relative URLs are resolved
     * against a context schema URL if provided.
     *
     * @param location an absolute or relative URL for a schema
     * @param context an absolute URL specifying the context schema of a relative location, or null
     *     if none
     * @return the string representation of a file or jar URL
     * @throws RuntimeException if a local resource could not be found
     */
    public String resolve(String location, String context) {
        URI locationUri;
        try {
            locationUri = new URI(location);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        if (!locationUri.isAbsolute()) {
            // Location is relative, so need to resolve against context.
            if (context == null) {
                throw new RuntimeException(
                        "Could not determine absolute schema location for "
                                + location
                                + " because context schema location is unknown");
            }
            // Find the original absolute http/https (canonical) URL used to obtain the
            // context schema, so relative imports can be honoured across resolution source
            // boundaries or jar file boundaries.
            String originalContext = resolvedLocationToOriginalLocationMap.get(context);
            if (originalContext == null) {
                // Do not know any better context, so treat as original.
                originalContext = context;
            }
            // Resolve the location URI against the context URI to make it absolute.
            URI contextUri;
            try {
                contextUri = new URI(originalContext);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            locationUri = contextUri.resolve(locationUri);
        }
        return resolve(locationUri.toString());
    }

    /**
     * Resolve an absolute URL to a local file or jar URL.
     *
     * @param location an absolute URL
     * @return the string representation of a file or jar URL
     * @throws RuntimeException if a local resource could not be found
     */
    public String resolve(String location) {
        String resolvedLocation = null;
        // Already resolved?
        if (location.startsWith("file:") || location.startsWith("jar:file:")) {
            resolvedLocation = location;
        }
        // Use catalog if one supplied.
        if (resolvedLocation == null && catalog != null) {
            resolvedLocation = catalog.resolveLocation(location);
        }
        // Look on classpath.
        if (resolvedLocation == null && classpath) {
            resolvedLocation = resolveClasspathLocation(location);
        }
        // Use download cache.
        if (resolvedLocation == null && cache != null) {
            resolvedLocation = cache.resolveLocation(location);
        }
        // Fail if still not resolved.
        if (resolvedLocation == null) {
            throw new RuntimeException(String.format("Failed to resolve %s", location));
        }
        synchronized (this) {
            if (!resolvedLocationToOriginalLocationMap.containsKey(resolvedLocation)) {
                resolvedLocationToOriginalLocationMap.put(resolvedLocation, location);
            }
        }
        LOGGER.fine(String.format("Resolved %s -> %s", location, resolvedLocation));
        return resolvedLocation;
    }

    /**
     * Return the Simple HTTP Resource Path for an absolute http/https URL. Does not include query
     * components in the path.
     *
     * @param location not null
     * @return the resource path with a leading slash
     * @see #getSimpleHttpResourcePath(URI)
     */
    public static String getSimpleHttpResourcePath(String location) {
        return getSimpleHttpResourcePath(location, false);
    }

    /**
     * Return the Simple HTTP Resource Path for an absolute http/https URL.
     *
     * @param location not null
     * @param keepQuery indicates whether or not the query components should be included in the
     *     path. If this is set to true then the query portion is converted to an MD5 message digest
     *     and that string is used to identify the file in the cache.
     * @return the resource path with a leading slash
     * @see #getSimpleHttpResourcePath(URI, boolean)
     */
    public static String getSimpleHttpResourcePath(String location, boolean keepQuery) {
        URI locationUri;
        try {
            locationUri = new URI(location);
        } catch (URISyntaxException e) {
            return null;
        }
        return getSimpleHttpResourcePath(locationUri, keepQuery);
    }

    /**
     * Return the Simple HTTP Resource Path for an absolute http/https URL.
     *
     * <p>The Simple HTTP Resource Path maps an HTTP or HTTPS URL to a path on the classpath or
     * relative to some other root. To form the Simple HTTP Resource Path from an http/https URL:
     *
     * <ol>
     *   <li>Protocol, port, fragment, and query are ignored.
     *   <li>Take the host name, split it into its components, reverse their order, prepend a
     *       forward slash to each, and concatenate them.
     *   <li>Append the path component of the URL.
     * </ol>
     *
     * For example <code>http://schemas.example.org/exampleml/exml.xsd</code> becomes <code>
     * /org/example/schemas/exampleml/exml.xsd</code> .
     *
     * <p>The Simple HTTP Resource Path always starts with a forward slash (if not null). Does not
     * include query components in the path.
     *
     * @param location not null
     * @return the Simple HTTP Resource Path as a string, or null if the URI is not an absolute
     *     HTTP/HTTPS URL.
     */
    public static String getSimpleHttpResourcePath(URI location) {
        return getSimpleHttpResourcePath(location, false);
    }

    /**
     * Return the Simple HTTP Resource Path for an absolute http/https URL.
     *
     * <p>The Simple HTTP Resource Path maps an HTTP or HTTPS URL to a path on the classpath or
     * relative to some other root. To form the Simple HTTP Resource Path from an http/https URL:
     *
     * <ol>
     *   <li>Protocol, port, fragment, and query are ignored.
     *   <li>Take the host name, split it into its components, reverse their order, prepend a
     *       forward slash to each, and concatenate them.
     *   <li>Append the path component of the URL.
     * </ol>
     *
     * For example <code>http://schemas.example.org/exampleml/exml.xsd</code> becomes <code>
     * /org/example/schemas/exampleml/exml.xsd</code> .
     *
     * <p>The Simple HTTP Resource Path always starts with a forward slash (if not null). Does not
     * include query components in the path.
     *
     * @param location not null
     * @param keepQuery indicates whether or not the query components should be included in the
     *     path. If this is set to true then the query portion is converted to an MD5 message digest
     *     and that string is used to identify the file in the cache.
     * @return the Simple HTTP Resource Path as a string, or null if the URI is not an absolute
     *     HTTP/HTTPS URL.
     */
    public static String getSimpleHttpResourcePath(URI location, boolean keepQuery) {
        String scheme = location.getScheme();
        if (scheme == null || !(scheme.equals("http") || scheme.equals("https"))) {
            return null;
        } else {
            String host = location.getHost();
            String path = location.getPath();
            String[] hostParts = host.split("\\.");
            StringBuilder builder = new StringBuilder();
            for (int i = hostParts.length - 1; i >= 0; i--) {
                builder.append("/");
                builder.append(hostParts[i]);
            }
            builder.append(path);
            String query = location.getQuery();
            if (keepQuery && query != null) {
                builder.append(".");
                builder.append(stringToMD5String(query));
                builder.append(".xsd");
            }
            return builder.toString();
        }
    }

    /**
     * Return the URL for a resource found on the classpath at the Simple HTTP Resource Path. This
     * allows (for example) schema documents in jar files to be loaded from the classpath using
     * their canonical HTTP URLs.
     *
     * @return the URL or null if not found
     */
    public static URL getClasspathResourceUrl(String location) {
        String path = getSimpleHttpResourcePath(location);
        if (path == null) {
            return null;
        } else {
            return SchemaResolver.class.getResource(path);
        }
    }

    /**
     * Return the string representation of URL for a resource found on the classpath at the Simple
     * HTTP Resource Path. This allows (for example) schema documents in jar files to be loaded from
     * the classpath using their canonical HTTP URLs.
     *
     * @return the string representation of a classpath URL, or null if not found
     */
    public static String resolveClasspathLocation(String location) {
        URL url = getClasspathResourceUrl(location);
        if (url == null) {
            return null;
        } else {
            return getClasspathResourceUrl(location).toExternalForm();
        }
    }

    /**
     * Convert a string into an MD5 digest.
     *
     * @param message The string whose MD5 digest you want to generate.
     * @return An MD5 digest generated from message, this string is always 32 characters long. Or
     *     returns null if there was an error.
     */
    private static String stringToMD5String(String message) {
        byte[] bytesOfMessage = null;
        MessageDigest md = null;
        try {
            bytesOfMessage = message.getBytes("UTF-8");
            md = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return String.format("%032x", new BigInteger(1, md.digest(bytesOfMessage)));
    }
}
