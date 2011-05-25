/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.grid;

import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Unit tests for the DefaultFeatureBuilder class.
 *
 * @author mbedward
 * @since 2.7
 *
 * @source $URL$
 * @version $Id$
 */
public class DefaultFeatureBuilderTest {

    @Test
    public void defaultConstructor() {
        DefaultFeatureBuilder setter = new DefaultFeatureBuilder();
        assertSetter(setter, DefaultFeatureBuilder.DEFAULT_TYPE_NAME, null);
    }
    
    @Test
    public void crsConstructor() {
        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        DefaultFeatureBuilder setter = new DefaultFeatureBuilder(crs);
        assertSetter(setter, DefaultFeatureBuilder.DEFAULT_TYPE_NAME, crs);
    }

    @Test
    public void nameConstructor() {
        String name = "foo";
        DefaultFeatureBuilder setter = new DefaultFeatureBuilder(name);
        assertSetter(setter, name, null);
    }

    @Test
    public void fullConstructor() {
        String name = "foo";
        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        DefaultFeatureBuilder setter = new DefaultFeatureBuilder(name, crs);
        assertSetter(setter, name, crs);
    }

    @Ignore("this is tested in the GridsTest class")
    @Test
    public void setAttributes() {
        // empty
    }

    private void assertSetter(DefaultFeatureBuilder setter, String typeName, CoordinateReferenceSystem crs) {
        SimpleFeatureType type = setter.getType();
        assertEquals(2, type.getAttributeCount());
        assertNotNull(type.getDescriptor(GridFeatureBuilder.DEFAULT_GEOMETRY_ATTRIBUTE_NAME));
        assertNotNull(type.getDescriptor(DefaultFeatureBuilder.ID_ATTRIBUTE_NAME));

        assertEquals(type.getTypeName(), typeName);
        if (crs == null) {
            assertNull(type.getCoordinateReferenceSystem());
        } else {
            assertTrue(CRS.equalsIgnoreMetadata(crs, type.getCoordinateReferenceSystem()));
        }
    }

}
