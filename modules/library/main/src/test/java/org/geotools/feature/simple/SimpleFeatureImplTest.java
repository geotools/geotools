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

import org.geotools.data.DataUtilities;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.geotools.api.feature.GeometryAttribute;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.GeometryDescriptor;

public class SimpleFeatureImplTest {

    SimpleFeatureType schema;
    SimpleFeature feature;
    WKTReader wkt;

    @Before
    public void setUp() throws Exception {
        schema =
                DataUtilities.createType(
                        "buildings", "the_geom:Geometry,name:String,ADDRESS:String");
        feature =
                SimpleFeatureBuilder.build(
                        schema, new Object[] {null, "ABC", "Random Road, 12"}, "building.1");
        wkt = new WKTReader();
    }

    // see GEOT-2061
    @Test
    public void testGetProperty() {
        Assert.assertEquals("ABC", feature.getProperty("name").getValue());
        Assert.assertNull(feature.getProperty("NOWHERE"));
        Assert.assertEquals(0, feature.getProperties("NOWHERE").size());
    }

    @Test
    public void testGetPropertyNullValue() {
        Assert.assertNotNull(feature.getProperty("the_geom"));
        Assert.assertNull(feature.getProperty("the_geom").getValue());
    }

    @Test
    public void testGeometryPropertyType() {
        Assert.assertTrue(
                "expected GeometryAttribute, got "
                        + feature.getProperty("the_geom").getClass().getName(),
                feature.getProperty("the_geom") instanceof GeometryAttribute);
    }

    @Test
    public void testDefaultGeometryProperty() {
        Assert.assertTrue(
                "expected GeometryAttribute, got "
                        + feature.getProperty("the_geom").getClass().getName(),
                feature.getProperty("the_geom") instanceof GeometryAttribute);
        GeometryAttribute defaultGeometryProperty = feature.getDefaultGeometryProperty();
        Assert.assertNotNull(defaultGeometryProperty);
        Assert.assertNull(defaultGeometryProperty.getValue());
        Assert.assertNotNull(defaultGeometryProperty.getDescriptor());
        Assert.assertTrue(defaultGeometryProperty.getDescriptor() instanceof GeometryDescriptor);
    }

    @Test
    public void testGetName() {
        Assert.assertNotNull(feature.getName());
        Assert.assertEquals(feature.getFeatureType().getName(), feature.getName());
    }

    @Test
    public void testGetDescriptor() {
        Assert.assertNotNull(feature.getDescriptor());
        Assert.assertSame(feature.getType(), feature.getDescriptor().getType());
        Assert.assertTrue(feature.getDescriptor().isNillable());
        Assert.assertEquals(0, feature.getDescriptor().getMinOccurs());
        Assert.assertEquals(Integer.MAX_VALUE, feature.getDescriptor().getMaxOccurs());
    }

    @Test
    public void testSetValue() {

        SimpleFeature myFeature =
                SimpleFeatureBuilder.build(schema, new Object[] {null, null, null}, "building.2");

        myFeature.setValue(feature.getProperties());
        for (int i = 0; i < feature.getAttributeCount(); i++) {
            Assert.assertEquals(feature.getAttribute(i), myFeature.getAttribute(i));
        }
    }

    @Test
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
        Assert.assertEquals(f1, f2);
        assertNotEquals(f1, f3);
    }

    @Test
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
        Assert.assertEquals(f3, f4);
    }

    @Test
    public void testUserMetadata() throws ParseException {
        SimpleFeature feature =
                SimpleFeatureBuilder.build(
                        schema,
                        new Object[] {wkt.read("POINT(1 2)"), "ABC", "Random Road, 12"},
                        "building.1");
        // no user data
        Assert.assertFalse(feature.hasUserData());
        // force map creation
        Assert.assertNotNull(feature.getUserData());
        // check it's considered emtpy
        Assert.assertFalse(feature.hasUserData());
        // put something
        feature.getUserData().put("a", "b");
        Assert.assertTrue(feature.hasUserData());
    }
}
