/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature.simple;

import static org.junit.Assert.assertNotEquals;

import junit.framework.TestCase;
import org.geotools.data.DataUtilities;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;

public class SimpleFeatureImplTest extends TestCase {

    SimpleFeatureType schema;
    SimpleFeature feature;
    WKTReader wkt;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        schema =
                DataUtilities.createType(
                        "buildings", "the_geom:Geometry,name:String,ADDRESS:String");
        feature =
                SimpleFeatureBuilder.build(
                        schema, new Object[] {null, "ABC", "Random Road, 12"}, "building.1");
        wkt = new WKTReader();
    }

    // see GEOT-2061
    public void testGetProperty() {
        assertEquals("ABC", feature.getProperty("name").getValue());
        assertNull(feature.getProperty("NOWHERE"));
        assertEquals(0, feature.getProperties("NOWHERE").size());
    }

    public void testGetPropertyNullValue() {
        assertNotNull(feature.getProperty("the_geom"));
        assertNull(feature.getProperty("the_geom").getValue());
    }

    public void testGeometryPropertyType() {
        assertTrue(
                "expected GeometryAttribute, got "
                        + feature.getProperty("the_geom").getClass().getName(),
                feature.getProperty("the_geom") instanceof GeometryAttribute);
    }

    public void testDefaultGeometryProperty() {
        assertTrue(
                "expected GeometryAttribute, got "
                        + feature.getProperty("the_geom").getClass().getName(),
                feature.getProperty("the_geom") instanceof GeometryAttribute);
        GeometryAttribute defaultGeometryProperty = feature.getDefaultGeometryProperty();
        assertNotNull(defaultGeometryProperty);
        assertNull(defaultGeometryProperty.getValue());
        assertNotNull(defaultGeometryProperty.getDescriptor());
        assertTrue(defaultGeometryProperty.getDescriptor() instanceof GeometryDescriptor);
    }

    public void testGetName() {
        assertNotNull(feature.getName());
        assertEquals(feature.getFeatureType().getName(), feature.getName());
    }

    public void testGetDescriptor() {
        assertNotNull(feature.getDescriptor());
        assertSame(feature.getType(), feature.getDescriptor().getType());
        assertTrue(feature.getDescriptor().isNillable());
        assertEquals(0, feature.getDescriptor().getMinOccurs());
        assertEquals(Integer.MAX_VALUE, feature.getDescriptor().getMaxOccurs());
    }

    public void testSetValue() {

        SimpleFeature myFeature =
                SimpleFeatureBuilder.build(schema, new Object[] {null, null, null}, "building.2");

        myFeature.setValue(feature.getProperties());
        for (int i = 0; i < feature.getAttributeCount(); i++) {
            assertEquals(feature.getAttribute(i), myFeature.getAttribute(i));
        }
    }

    public void testCompare2D() throws ParseException {
        SimpleFeature f1 =
                SimpleFeatureBuilder.build(
                        schema,
                        new Object[] {wkt.read("POINT(1 2)"), "ABC", "Random Road, 12"},
                        "building.1");
        SimpleFeature f2 =
                SimpleFeatureBuilder.build(
                        schema,
                        new Object[] {wkt.read("POINT(1 2)"), "ABC", "Random Road, 12"},
                        "building.1");
        SimpleFeature f3 =
                SimpleFeatureBuilder.build(
                        schema,
                        new Object[] {wkt.read("POINT(3 4)"), "ABC", "Random Road, 12"},
                        "building.1");
        assertEquals(f1, f2);
        assertNotEquals(f1, f3);
    }

    public void testCompare3D() throws ParseException {
        SimpleFeature f1 =
                SimpleFeatureBuilder.build(
                        schema,
                        new Object[] {wkt.read("POINT(1 2)"), "ABC", "Random Road, 12"},
                        "building.1");
        SimpleFeature f2 =
                SimpleFeatureBuilder.build(
                        schema,
                        new Object[] {wkt.read("POINT(1 2 15)"), "ABC", "Random Road, 12"},
                        "building.1");
        SimpleFeature f3 =
                SimpleFeatureBuilder.build(
                        schema,
                        new Object[] {wkt.read("POINT(1 2 18)"), "ABC", "Random Road, 12"},
                        "building.1");
        SimpleFeature f4 =
                SimpleFeatureBuilder.build(
                        schema,
                        new Object[] {wkt.read("POINT(1 2 18)"), "ABC", "Random Road, 12"},
                        "building.1");
        assertNotEquals(f1, f2);
        assertNotEquals(f1, f3);
        assertNotEquals(f2, f3);
        assertEquals(f3, f4);
    }
}
