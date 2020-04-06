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

import org.geotools.xml.resolver.SchemaResolver;
import org.geotools.xsd.Configuration;

/**
 * XML encoder {@link Configuration} that uses {@link SchemaResolver} to obtain schemas.
 *
 * <p>Because we do not know the dependent GML {@link Configuration} when an instance is
 * constructed, it must be added later using {@link #addDependency(Configuration)}. Failure to do
 * this will result in bindings not being found at encode time.
 *
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 */
public class AppSchemaConfiguration extends Configuration {

    /** Original (unresolved) schema location. */
    private final String originalSchemaLocation;

    /**
     * Because we do not know the dependent GML {@link Configuration} until runtime, it must be
     * specified as a constructor argument.
     *
     * @param namespace the namespace URI
     * @param schemaLocation URL giving canonical schema location
     */
    public AppSchemaConfiguration(
            String namespace, String schemaLocation, SchemaResolver resolver) {
        super(new AppSchemaXSD(namespace, schemaLocation, resolver));
        originalSchemaLocation = schemaLocation;
        ((AppSchemaXSD) getXSD()).setConfiguration(this);
    }

    /**
     * Get the original (unresolved) schema location.
     *
     * @return the schema location
     */
    public String getSchemaLocation() {
        return originalSchemaLocation;
    }

    /**
     * Allow late addition of a dependency such as GML.
     *
     * @see Configuration#addDependency(Configuration)
     */
    @Override
    public void addDependency(Configuration dependency) {
        super.addDependency(dependency);
    }
}
