/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xsd.complex;

import java.util.Collection;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.xsd.Configuration;
import org.opengis.feature.type.Schema;

/**
 * Configuration for a Complex Feature Type Registry (see gt-complex module), - tells the registry
 * which schemas and configurations to use - tells the registry when to create a Feature Type, a
 * Geometry Type or set Identifiable
 *
 * @author Niels Charlier
 */
public interface FeatureTypeRegistryConfiguration {

    Collection<Schema> getSchemas();

    Collection<Configuration> getConfigurations();

    boolean isFeatureType(XSDTypeDefinition typeDefinition);

    boolean isGeometryType(XSDTypeDefinition typeDefinition);

    boolean isIdentifiable(XSDComplexTypeDefinition typeDefinition);
}
