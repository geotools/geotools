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
import java.util.List;
import java.util.Set;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDSchemaLocator;
import org.geotools.xml.resolver.SchemaResolver;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.SchemaLocationResolver;
import org.geotools.xsd.SchemaLocator;
import org.geotools.xsd.XSD;

/**
 * {@link XSD} that uses {@link SchemaResolver} to locate schema resources in a catalog, on the
 * classpath, or in a cache.
 *
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 * @author Niels Charlier (Curtin University of Technology)
 */
public class AppSchemaXSD extends XSD {

    private final String namespaceUri;

    private final String schemaLocation;

    /** The resolver used to locate resources. */
    private final SchemaResolver resolver;

    /** The {@link Configuration} used to encode documents with this schema. */
    private AppSchemaConfiguration configuration;

    /** */
    public AppSchemaXSD(String namespaceUri, String schemaLocation, SchemaResolver resolver) {
        this.namespaceUri = namespaceUri;
        this.schemaLocation = resolver.resolve(schemaLocation);
        this.resolver = resolver;
    }

    /** @see XSD#getNamespaceURI() */
    @Override
    public String getNamespaceURI() {
        return namespaceUri;
    }

    /** @see XSD#getSchemaLocation() */
    @Override
    public String getSchemaLocation() {
        return schemaLocation;
    }

    /** @param configuration */
    public void setConfiguration(AppSchemaConfiguration configuration) {
        this.configuration = configuration;
    }

    /** @see XSD#createSchemaLocationResolver() */
    @Override
    public SchemaLocationResolver createSchemaLocationResolver() {
        return new AppSchemaLocationResolver(resolver);
    }

    /** @see XSD#addDependencies(java.util.Set) */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected void addDependencies(Set dependencies) {
        if (configuration != null) {
            for (Configuration dependency : (List<Configuration>) configuration.getDependencies()) {
                dependencies.add(dependency.getXSD());
            }
        }
    }

    @Override
    public SchemaLocator createSchemaLocator() {
        return new SchemaLocator(this) {
            public boolean canHandle(
                    XSDSchema schema,
                    String namespaceURI,
                    String rawSchemaLocationURI,
                    String resolvedSchemaLocationURI) {
                return xsd.getNamespaceURI().equals(namespaceURI)
                        && xsd.getSchemaLocation().equals(resolvedSchemaLocationURI);
            }
        };
    }

    @Override
    public XSDSchemaLocator getSupplementarySchemaLocator() {
        return AppSchemaXSDRegistry.getInstance();
    }

    @Override
    protected XSDSchema buildSchema() throws IOException {
        // check if schema already exists in registry, if so do not build
        XSDSchema schema = AppSchemaXSDRegistry.getInstance().lookUp(schemaLocation);
        if (schema == null) {
            schema = super.buildSchema();
            // register schema
            AppSchemaXSDRegistry.getInstance().register(schema);
        } else {
            // reset because included schema's are not always complete
            schema.reset();
        }
        return schema;
    }
}
