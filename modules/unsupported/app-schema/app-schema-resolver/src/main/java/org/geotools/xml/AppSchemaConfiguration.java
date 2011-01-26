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

/**
 * XML encoder {@link Configuration} that uses {@link AppSchemaResolver} to obtain schemas.
 * 
 * @author Ben Caradoc-Davies, CSIRO Earth Science and Resource Engineering
 */
public class AppSchemaConfiguration extends Configuration {

    /**
     * Because we do not know the dependent Configurations until runtime, they must be specified as
     * a constructor argument.
     * 
     * @param namespace
     *            the namespace URI
     * @param schemaLocation
     *            URL giving canonical schema location
     * @param resolver
     * @param dependencies
     *            dependent configurations
     */
    public AppSchemaConfiguration(String namespace, String schemaLocation,
            AppSchemaResolver resolver, Configuration... dependencies) {
        super(new AppSchemaXSD(namespace, schemaLocation, resolver));
        for (Configuration dependency : dependencies) {
            addDependency(dependency);
        }
        ((AppSchemaXSD) getXSD()).setConfiguration(this);
    }

}
