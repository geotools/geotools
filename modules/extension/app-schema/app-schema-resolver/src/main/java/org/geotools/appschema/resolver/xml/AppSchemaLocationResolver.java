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

import org.eclipse.xsd.XSDSchema;
import org.geotools.xml.resolver.SchemaResolver;
import org.geotools.xsd.SchemaLocationResolver;

/**
 * A {@link SchemaLocationResolver} that uses {@link SchemaResolver} to locate schema resources in a
 * catalog, on the classpath, or in a cache..
 *
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 */
public class AppSchemaLocationResolver extends SchemaLocationResolver {

    /** The resolver used to locate schemas */
    private final SchemaResolver resolver;

    /**
     * Constructor.
     *
     * @param resolver the resolver used to locate schemas
     */
    public AppSchemaLocationResolver(SchemaResolver resolver) {
        super(null);
        this.resolver = resolver;
    }

    /**
     * Resolve imports and includes to local resources.
     *
     * @param schema the parent schema from which the import/include originates
     * @param uri the namespace of an import (ignored in this implementation)
     * @param location the URL of the import or include (may be relative)
     * @see SchemaLocationResolver#resolveSchemaLocation(org.eclipse.xsd.XSDSchema,
     *     java.lang.String, java.lang.String)
     */
    @Override
    public String resolveSchemaLocation(
            final XSDSchema schema, final String uri, final String location) {
        return resolver.resolve(location, schema.getSchemaLocation());
    }

    /**
     * We override this because the parent {@link #toString()} is horribly misleading.
     *
     * @see SchemaLocationResolver#toString()
     */
    @Override
    public String toString() {
        return getClass().getCanonicalName();
    }
}
