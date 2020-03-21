/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.complex.feature.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Collections;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.feature.NameImpl;
import org.geotools.gml3.complex.GmlFeatureTypeRegistryConfiguration;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.complex.FeatureTypeRegistryConfiguration;
import org.junit.Test;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.Schema;

public class FeatureTypeRegistryTest {
    private static final Name PolygonPropertyType =
            new NameImpl("http://www.opengis.net/gml", "PolygonPropertyType");

    private class EmptyFeatureTypeRegistryConfiguration
            implements FeatureTypeRegistryConfiguration {

        @Override
        public Collection<Schema> getSchemas() {
            return Collections.emptySet();
        }

        @Override
        public Collection<Configuration> getConfigurations() {
            return Collections.emptySet();
        }

        @Override
        public boolean isFeatureType(XSDTypeDefinition typeDefinition) {
            return false;
        }

        @Override
        public boolean isGeometryType(XSDTypeDefinition typeDefinition) {
            return false;
        }

        @Override
        public boolean isIdentifiable(XSDComplexTypeDefinition typeDefinition) {
            return false;
        }
    }

    @Test
    public void testMultiple() {

        FeatureTypeRegistry registry1 =
                new FeatureTypeRegistry(
                        new ComplexFeatureTypeFactoryImpl(),
                        new GmlFeatureTypeRegistryConfiguration("http://www.opengis.net/gml"));

        assertNotNull(registry1.getAttributeType(PolygonPropertyType));

        FeatureTypeRegistry registry2 =
                new FeatureTypeRegistry(
                        new ComplexFeatureTypeFactoryImpl(),
                        new EmptyFeatureTypeRegistryConfiguration());

        boolean exception;
        try {
            registry2.getAttributeType(PolygonPropertyType);
            exception = false;
        } catch (IllegalArgumentException e) {
            exception = true;
        }
        assertTrue(exception);
    }
}
