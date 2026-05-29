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

package org.geotools.appschema.resolver.xml;

import java.io.IOException;
import org.geotools.util.EntityResolver3;
import org.geotools.util.NullEntityResolver;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.geotools.xml.resolver.SchemaResolver;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.EntityResolver2;

/**
 * An {@link EntityResolver2} that uses the enclosing instance's {@link SchemaResolver} to look up XML entities (that
 * is, XML schemas).
 */
public class AppSchemaEntityResolver implements EntityResolver3 {
    /** EntityResolver used as a delegate if SchemaResolver is unable to locate resource. */
    EntityResolver entityResolver;

    /** The resolver used to find XML schemas */
    SchemaResolver resolver;

    /**
     * EntityResolver backed by SchemaResolver to identify and trust local resources.
     *
     * @param resolver
     */
    public AppSchemaEntityResolver(SchemaResolver resolver) {
        this.resolver = resolver;
        this.entityResolver = NullEntityResolver.INSTANCE;
    }

    /**
     * EntityResolver backed by SchemaResolver to identify and trust local resources, then GeoTools library
     * configuration to locate resources.
     *
     * @param resolver
     * @param hints
     */
    public AppSchemaEntityResolver(SchemaResolver resolver, Hints hints) {
        this.resolver = resolver;
        this.entityResolver = GeoTools.getEntityResolver(hints);
    }

    /**
     * EntityResolver backed by SchemaResolver to identify and trust local resources, then GeoTools library
     * configuration to locate resources.
     *
     * @param resolver
     * @param entityResolver
     */
    public AppSchemaEntityResolver(SchemaResolver resolver, EntityResolver entityResolver) {
        this.resolver = resolver;
        this.entityResolver = entityResolver != null ? entityResolver : NullEntityResolver.INSTANCE;
    }

    @Override
    public String getAccess() {
        return "all";
    }

    /**
     * Always throws {@link UnsupportedOperationException}. The {@link EntityResolver2} interface must be used so that
     * relative URLs are resolved correctly. If this method is called, it means that the parser is probably
     * misconfigured.
     *
     * @see EntityResolver#resolveEntity(String, String)
     */
    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        throw new UnsupportedOperationException("Misconfigured parser: EntityResolver2 interface must be used "
                + "so that relative URLs are resolved correctly");
    }

    /**
     * Delegate to library entity resolver, or {@code null} to indicate that there is no external subset.
     *
     * @see EntityResolver2#getExternalSubset(String, String)
     */
    @Override
    public InputSource getExternalSubset(String name, String baseURI) throws SAXException, IOException {
        if (entityResolver instanceof EntityResolver2 entityResolver2)
            return entityResolver2.getExternalSubset(name, baseURI);
        return null;
    }

    /**
     * Return an {@link InputSource} for the resolved schema location. Note that the {@link EntityResolver2} interface
     * must be used because baseURI is needed to resolve relative URIs. The resolver uses baseURI to find the original
     * unresolved context (which it has stored); this is then used to construct the unresolved URI of the schema. In the
     * case of downloaded schemas, the original URI is used to download the schema into the cache; the resolved URI is
     * the location of the cached schema.
     *
     * @see EntityResolver2#resolveEntity(String, String, String, String)
     */
    @Override
    public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId)
            throws SAXException, IOException {
        return new InputSource(resolver.resolve(systemId, baseURI));
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("AppSchemaEntityResolver {")
                .append(resolver)
                .append("}")
                .toString();
    }
}
