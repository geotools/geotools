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

import java.io.IOException;
import java.text.MessageFormat;
import java.util.regex.Pattern;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Entity Resolver which allows access to internal files and sample-data as either {@code "file"} references to (from
 * IDE) or {@code "jar:file"} references (from Maven).
 *
 * <p>This implementation assumes standard maven layout with sample-data generated into {@code target/test-classes}
 * location.
 *
 * <p>See also {@code sample-data} module {@code SampleData}.
 */
public class InternalEntityResolver implements EntityResolver3 {

    /**
     * Recommended for local GeoTools resources and testing, allowing access to sample data and common http locations
     * via DefaultEntityResolver.
     */
    public static InternalEntityResolver INSTANCE = new InternalEntityResolver();

    /**
     * Internal class-resource reference when developing locally.
     *
     * <p>Check if {@code "file"} path refers {@code target/classes} to {@code target/test-classes} XSD or DTD.
     */
    private static final Pattern CLASS_RESOURCE_URIS =
            Pattern.compile("(?i)(file)[^?#;].*[\\\\/]target[\\\\/](test-classes|classes)[\\\\/].*\\.(xsd|dtd)$");

    /**
     * Internal uri schema references.
     *
     * <ul>
     *   <li>allow {@code xsd} schema parsing for validation
     *   <li>{@code jar:file} - internal schema reference
     *   <li>{@code jar:nested} - internal schema reference (SpringBoot)
     *   <li>{@code vfs} - internal schema reference (JBoss/WildFly)
     * </ul>
     */
    private static final Pattern INTERNAL_URIS = Pattern.compile("(?i)(jar:file|jar:nested|vfs)[^?#;]*\\.(xsd|dtd)$");

    private final EntityResolver delegate;

    /** Use of internal resources in addition to external resources allowed by DefaultEntityResolver instance. */
    protected InternalEntityResolver() {
        this(DefaultEntityResolver.INSTANCE);
    }

    /**
     * Allows use of internal resources in addition to those allowed by delegate.
     *
     * @param delegate Entity resolver to be used for non-internal resources
     */
    public InternalEntityResolver(EntityResolver delegate) {
        this.delegate = delegate != null ? delegate : PreventEntityResolver.INSTANCE;
    }

    @Override
    public String getAccess() {
        if (delegate instanceof EntityResolver3 entityResolver3) {
            if (entityResolver3.getAccess() != null
                    && !entityResolver3.getAccess().isEmpty()) {
                boolean hasFile = false;
                boolean hasJarFile = false;
                for (String protocol : entityResolver3.getAccess().split(",")) {
                    if (protocol.trim().equalsIgnoreCase("file")) {
                        hasFile = true;
                    } else if (protocol.trim().equalsIgnoreCase("jar:file")) {
                        hasJarFile = true;
                    }
                }
                StringBuilder access = new StringBuilder();
                access.append(entityResolver3.getAccess());
                if (!hasFile) {
                    access.append(",file");
                }
                if (!hasJarFile) {
                    access.append(",jar:file");
                }
                return access.toString();
            }
        }
        return "file,jar:file";
    }

    @Override
    public InputSource getExternalSubset(String name, String baseURI) throws SAXException, IOException {
        String uri = DefaultEntityResolver.toURI(null, baseURI);
        uri = DefaultEntityResolver.normalize(uri);
        if (INTERNAL_URIS.matcher(uri).matches()) {
            return null;
        }
        if (CLASS_RESOURCE_URIS.matcher(uri).matches()) {
            return null;
        }
        if (delegate instanceof EntityResolver3 entityResolver3) {
            return entityResolver3.getExternalSubset(name, baseURI);
        }
        // do not allow external entities
        throw new SAXException(MessageFormat.format(DefaultEntityResolver.ERROR_MESSAGE_BASE, baseURI));
    }

    @Override
    public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId)
            throws SAXException, IOException {
        if (systemId == null) {
            if (name != null) {
                return null;
            }
            throw new SAXException("External entity systemId not provided");
        }
        String uri = DefaultEntityResolver.toURI(baseURI, systemId);
        uri = DefaultEntityResolver.normalize(uri);
        if (INTERNAL_URIS.matcher(uri).matches()) {
            return null;
        }
        if (CLASS_RESOURCE_URIS.matcher(uri).matches()) {
            return null;
        }
        if (delegate instanceof EntityResolver3 entityResolver3) {
            return entityResolver3.resolveEntity(name, publicId, baseURI, systemId);
        }
        // do not allow external entities
        throw new SAXException(MessageFormat.format(DefaultEntityResolver.ERROR_MESSAGE_BASE, systemId));
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        String uri = DefaultEntityResolver.toURI(null, systemId);
        uri = DefaultEntityResolver.normalize(uri);

        // check if the absolute systemId is an allowed URI jar or vfs reference
        if (INTERNAL_URIS.matcher(uri).matches()) {
            return null;
        }
        // Allow select class resources for sample data and internal schema references
        if (CLASS_RESOURCE_URIS.matcher(uri).matches()) {
            return null;
        }
        return delegate.resolveEntity(publicId, systemId);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("InternalEntityResolver{");
        sb.append("delegate=").append(delegate);
        sb.append('}');
        return sb.toString();
    }
}
