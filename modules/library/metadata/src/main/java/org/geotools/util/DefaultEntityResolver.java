/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util;

import static java.util.function.Predicate.not;
import static org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.EntityResolver2;

/**
 * Entity Resolver which allows to resolve on OGC & INSPIRE schemas, roughly inspired from the `AllowListEntityResolver`
 * class from GeoServer.
 *
 * In addition to XSD entities, we also have standards like WMS 1.1.1 making use of DTD definitions.
 *
 * @author Pierre Mauduit (Camptocamp)
 */
public class DefaultEntityResolver implements EntityResolver2, Serializable {

    /** Location of Open Geospatial Consortium schemas for OGC OpenGIS standards */
    public static String OGC1 = "schemas.opengis.net";

    public static String OGC2 = "www.opengis.net";
    public static String OGC = OGC1 + "|" + OGC2;

    /** Location of {@code http://inspire.ec.europa.eu/schemas/ } XSD documents for INSPIRE program */
    public static String INSPIRE = "inspire.ec.europa.eu/schemas";

    /** Location of W3C schema documents (for xlink, etc...) */
    public static String W3C = "www.w3.org";

    /** Prefix used for SAXException message */
    private static final String ERROR_MESSAGE_BASE = "Entity resolution disallowed for ";

    /**
     * Internal uri references.
     *
     * <ul>
     *   <li>allow schema parsing for validation.
     *   <li>jar - internal schema reference
     *   <li>vfs - internal schema reference (JBoss/WildFly)
     * </ul>
     */
    private static final Pattern INTERNAL_URIS = Pattern.compile("(?i)(jar:file|vfs)[^?#;]*\\.(xsd|dtd)$");

    /** Only allow XSD or DTD URIs that do not contain a URI query or fragment */
    private static final Pattern XSD_OR_DTD_URIS = Pattern.compile("(?i)^[^?#;]*\\.(xsd|dtd)$");

    /** Checks if a URL contains escaped periods, slashes or backslashes */
    private static final Pattern BANNED_ESCAPES = Pattern.compile("(?i)^.*%(2e|2f|5c).*$");

    /** Checks if a file path starts with a Windows driver letter */
    private static final Pattern WINDOWS_DRIVE = Pattern.compile("^/[a-zA-Z]:/.*$");

    /** Allowed http(s) locations */
    private final Pattern ALLOWED_URIS;

    /** Singleton instance of DefaultEntityResolver. */
    public static final DefaultEntityResolver INSTANCE = new DefaultEntityResolver();

    /** OGCInspireSchemasEntityResolver willing to resolve common ogc and w3c entities. */
    protected DefaultEntityResolver() {
        ALLOWED_URIS = Pattern.compile("(?i)(http|https)://("
                + Pattern.quote(W3C)
                + "|"
                + Pattern.quote(OGC1)
                + "|"
                + Pattern.quote(OGC2)
                + "|"
                + Pattern.quote(INSPIRE)
                + ")/[^?#;]*\\.(xsd|dtd)");
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        return resolveEntity(null, publicId, null, systemId);
    }

    @Override
    public InputSource getExternalSubset(String name, String baseURI) throws SAXException, IOException {
        return resolveEntity(name, null, baseURI, null);
    }

    @Override
    public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId)
            throws SAXException, IOException {
        try {
            String uri;
            if (systemId == null) {
                if (name != null) {
                    return null;
                }
                throw new SAXException("External entity systemId not provided");
            }

            if (URI.create(systemId).isAbsolute()) {
                uri = systemId;
            } else {
                // use the baseURI to convert a relative systemId to absolute
                if (baseURI == null) {
                    throw new SAXException(ERROR_MESSAGE_BASE + systemId);
                }
                if ((baseURI.endsWith(".xsd") || baseURI.endsWith(".XSD")) && baseURI.lastIndexOf('/') != -1) {
                    uri = baseURI.substring(0, baseURI.lastIndexOf('/')) + '/' + systemId;
                } else {
                    uri = baseURI + '/' + systemId;
                }
            }
            uri = normalize(uri);
            // check if the absolute systemId is an allowed URI jar or vfs reference
            if (INTERNAL_URIS.matcher(uri).matches()) {
                return null;
            }
            // Allow select external locations
            if (ALLOWED_URIS.matcher(uri).matches()) {
                return null;
            }
        } catch (Exception e) {
            // do nothing
        }

        // do not allow external entities
        throw new SAXException(ERROR_MESSAGE_BASE + systemId);
    }

    private static String normalize(String uri) throws IOException {
        if (!URI.create(uri).isAbsolute()) {
            uri = "file:" + uri;
        }
        if (!uri.startsWith("vfs:/")) {
            // verify that it is a valid URL
            new URL(uri);
        }
        String lower = uri.toLowerCase();
        if (!XSD_OR_DTD_URIS.matcher(uri).matches() || BANNED_ESCAPES.matcher(uri).matches()) {
            throw new IllegalArgumentException("Invalid XSD URI: " + uri);
        } else if (lower.startsWith("jar:file:/")) {
            uri = normalize("jar:file:", uri.substring(9), IS_OS_WINDOWS, true);
        } else if (lower.startsWith("vfs:/")) {
            uri = normalize("vfs:", uri.substring(4), IS_OS_WINDOWS, false);
        } else if (lower.startsWith("https://")) {
            uri = normalize("https:", uri.substring(6), true, false);
        } else if (lower.startsWith("http://")) {
            uri = normalize("http:", uri.substring(5), true, false);
        } else if (IS_OS_WINDOWS && lower.startsWith("file:////")) {
            uri = normalize("file:", uri.substring(7), true, false);
        } else if (lower.startsWith("file:///")) {
            uri = normalize("file:", toAbsolutePath(uri.substring(7)), false, false);
        } else if (IS_OS_WINDOWS && lower.startsWith("file://")) {
            uri = normalize("file:", uri.substring(5), true, false);
        } else if (lower.startsWith("file:")) {
            uri = normalize("file:", toAbsolutePath(uri.substring(5)), false, false);
        } else {
            throw new IllegalArgumentException("Unsupported XSD URI protocol: " + uri);
        }
        return uri;
    }

    private static String normalize(String scheme, String path, boolean allowHost, boolean archive) {
        String prefix = "/";
        if (allowHost && path.startsWith("//") && !path.startsWith("///")) {
            prefix = path.substring(0, path.indexOf('/', 2) + 1);
        } else if (IS_OS_WINDOWS && WINDOWS_DRIVE.matcher(path).find()) {
            prefix = path.substring(0, 4);
        }
        path = path.substring(prefix.length());
        String suffix = "";
        if (archive) {
            suffix = path.substring(path.indexOf('!'));
            path = path.substring(0, path.length() - suffix.length());
        }
        List<String> names = Arrays.stream(path.split("/"))
                .filter(not(String::isEmpty))
                .filter(not("."::equals))
                .collect(Collectors.toList());
        for (int index = names.indexOf(".."); index >= 0; index = names.indexOf("..")) {
            // remove the ..
            names.remove(index);
            if (index > 0) {
                // remove previous directory name
                names.remove(index - 1);
            }
        }
        return scheme + prefix + String.join("/", names) + suffix;
    }

    private static String toAbsolutePath(String path) {
        path = new File(path).getAbsolutePath().replace(File.separator, "/");
        return (path.startsWith("/") ? "" : "/") + path;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("DefaultEntityResolver:( ");
        builder.append(this.ALLOWED_URIS);
        builder.append(")");
        return builder.toString();
    }
}
