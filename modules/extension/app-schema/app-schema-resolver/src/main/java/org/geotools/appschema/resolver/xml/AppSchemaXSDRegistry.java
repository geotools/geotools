/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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
import java.util.HashMap;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDResourceImpl;
import org.eclipse.xsd.util.XSDSchemaLocator;
import org.geotools.xsd.Schemas;

/**
 * A singleton registry to store all XSD schema's that are created by app-schema. This registry has
 * two purposes: (1) Reusing schema's that have already been built previously, so that schema
 * content in memory isn't cluttered with multiple versions of the same schema (with respect to .
 *
 * @author Niels Charlier (Curtin University of Technology)
 */
public final class AppSchemaXSDRegistry implements XSDSchemaLocator {

    /** Lazy loaded Singleton */
    private static volatile AppSchemaXSDRegistry theXSDRegistry;

    /** Registry that maps (resolved) locations to schema's */
    private Map<String, XSDSchema> schemaRegistry = new HashMap<>();

    private AppSchemaXSDRegistry() {}

    /**
     * Get lazy loaded singleton instance
     *
     * @return singleton instance
     */
    public static AppSchemaXSDRegistry getInstance() {
        if (theXSDRegistry == null) {
            theXSDRegistry = new AppSchemaXSDRegistry();
        }
        return theXSDRegistry;
    }

    /**
     * Register schema
     *
     * @param schema schema to be registered
     */
    public synchronized void register(XSDSchema schema) {
        schemaRegistry.put(schema.getSchemaLocation(), schema);
    }

    /**
     * Look up schema in register
     *
     * @param schemaLocation (resolved) schema location
     * @return schema
     */
    public synchronized XSDSchema lookUp(String schemaLocation) {
        return schemaRegistry.get(schemaLocation);
    }

    /** Flush all schema's (remove all references to them from other schema's) and clear register */
    public synchronized void dispose() {
        for (XSDSchema schema : schemaRegistry.values()) {
            Schemas.dispose(schema);
        }
        schemaRegistry.clear();
    }

    /** Implements schema locator... creates and registers new schema if necessary */
    @Override
    public synchronized XSDSchema locateSchema(
            XSDSchema xsdSchema,
            String namespaceURI,
            String rawSchemaLocationURI,
            String resolvedSchemaLocationURI) {

        if (xsdSchema != null) {
            // first see if the schema can already be found in same resource set
            // (to avoid infinite loop)
            ResourceSet resourceSet = xsdSchema.eResource().getResourceSet();
            Resource resolvedResource =
                    resourceSet.getResource(
                            URI.createURI(
                                    resolvedSchemaLocationURI == null
                                            ? ""
                                            : resolvedSchemaLocationURI),
                            false);

            if (resolvedResource != null && resolvedResource instanceof XSDResourceImpl) {
                return ((XSDResourceImpl) resolvedResource).getSchema();
            } else {
                // try getting from registry
                XSDSchema schema = lookUp(resolvedSchemaLocationURI);
                if (schema == null) { // build new one
                    try {
                        // use same resource set to avoid infinite loop, assume schemas are not
                        // malicious
                        schema = Schemas.parse(resolvedSchemaLocationURI, resourceSet, null);
                        register(schema);
                    } catch (IOException e) {
                        schema = null;
                    }
                }

                return schema;
            }
        } else {
            return lookUp(resolvedSchemaLocationURI);
        }
    }
}
