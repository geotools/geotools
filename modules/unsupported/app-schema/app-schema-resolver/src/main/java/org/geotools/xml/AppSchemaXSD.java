/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.xml;

import java.util.List;
import java.util.Set;

import org.geotools.xml.SchemaLocationResolver;
import org.geotools.xml.XSD;

/**
 * {@link XSD} that uses {@link AppSchemaResolver} to locate schema resources in a catalog, on the
 * classpath, or in a cache.
 * 
 * @author Ben Caradoc-Davies, CSIRO Earth Science and Resource Engineering
 *
 * @source $URL$
 */
public class AppSchemaXSD extends XSD {

    private final String namespaceUri;

    private final String schemaLocation;

    /**
     * The resolver used to locate resources.
     */
    private final AppSchemaResolver resolver;

    /**
     * The {@link Configuration} used to encode documents with this schema.
     */
    private AppSchemaConfiguration configuration;

    /**
     * @param namespaceUri
     * @param schemaLocation
     * @param resolver
     */
    public AppSchemaXSD(String namespaceUri, String schemaLocation, AppSchemaResolver resolver) {
        this.namespaceUri = namespaceUri;
        this.schemaLocation = resolver.resolve(schemaLocation);
        this.resolver = resolver;
    }

    /**
     * @see org.geotools.xml.XSD#getNamespaceURI()
     */
    @Override
    public String getNamespaceURI() {
        return namespaceUri;
    }

    /**
     * @see org.geotools.xml.XSD#getSchemaLocation()
     */
    @Override
    public String getSchemaLocation() {
        return schemaLocation;
    }

    /**
     * @param configuration
     */
    public void setConfiguration(AppSchemaConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * @see org.geotools.xml.XSD#createSchemaLocationResolver()
     */
    @Override
    public SchemaLocationResolver createSchemaLocationResolver() {
        return new AppSchemaLocationResolver(resolver);
    }

    /**
     * @see org.geotools.xml.XSD#addDependencies(java.util.Set)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void addDependencies(Set dependencies) {
        if (configuration != null) {
            for (Configuration dependency : (List<Configuration>) configuration.getDependencies()) {
                dependencies.add(dependency.getXSD());
            }
        }
    }

}
