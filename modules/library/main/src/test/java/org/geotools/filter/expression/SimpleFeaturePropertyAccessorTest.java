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
package org.geotools.filter.expression;

import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class SimpleFeaturePropertyAccessorTest {

    private static final String COMPLEX_PROPERTY = "pro.per.ty-G\\u00e9n\\\\u00e9rique:abc";
    SimpleFeatureType type;
    SimpleFeature feature;
    PropertyAccessor accessor = SimpleFeaturePropertyAccessorFactory.ATTRIBUTE_ACCESS;

    @Before
    public void setUp() throws Exception {
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();

        typeBuilder.setName("test");
        typeBuilder.setNamespaceURI("http://www.geotools.org/test");
        typeBuilder.add("foo", Integer.class);
        typeBuilder.add("bar", Double.class);
        typeBuilder.add(COMPLEX_PROPERTY, Double.class);

        type = typeBuilder.buildFeatureType();

        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
        builder.add(Integer.valueOf(1));
        builder.add(Double.valueOf(2.0));
        builder.add(Double.valueOf(3.0));

        feature = builder.buildFeature("fid");
        accessor = SimpleFeaturePropertyAccessorFactory.ATTRIBUTE_ACCESS;
    }

    @Test
    public void testCanHandle() {
        Assert.assertTrue(accessor.canHandle(feature, "foo", null));
        Assert.assertTrue(accessor.canHandle(feature, "bar", null));

        Assert.assertFalse(accessor.canHandle(feature, "illegal", null));
    }

    @Test
    public void testCanHandleType() {
        Assert.assertTrue(accessor.canHandle(type, "foo", null));
        Assert.assertTrue(accessor.canHandle(type, "sf:foo", null));
        Assert.assertTrue(accessor.canHandle(type, "foo[1]", null));
        Assert.assertTrue(accessor.canHandle(type, "sf:foo[1]", null));
        Assert.assertTrue(accessor.canHandle(type, "bar", null));
        Assert.assertTrue(accessor.canHandle(type, COMPLEX_PROPERTY, null));

        Assert.assertFalse(accessor.canHandle(type, "illegal", null));
        Assert.assertFalse(accessor.canHandle(type, "sf:foo[0]", null));
        Assert.assertFalse(accessor.canHandle(type, "sf:foo[2]", null));
    }

    @Test
    public void testGet() {
        Assert.assertEquals(Integer.valueOf(1), accessor.get(feature, "foo", null));
        Assert.assertEquals(Integer.valueOf(1), accessor.get(feature, "sf:foo", null));
        Assert.assertEquals(Integer.valueOf(1), accessor.get(feature, "foo[1]", null));
        Assert.assertEquals(Integer.valueOf(1), accessor.get(feature, "sf:foo[1]", null));
        Assert.assertEquals(Double.valueOf(2.0), accessor.get(feature, "bar", null));
        Assert.assertEquals(Double.valueOf(3.0), accessor.get(feature, COMPLEX_PROPERTY, null));
        Assert.assertEquals("fid", SimpleFeaturePropertyAccessorFactory.FID_ACCESS.get(feature, "@id", null));
        Assert.assertEquals("fid", SimpleFeaturePropertyAccessorFactory.FID_ACCESS.get(feature, "@gml:id", null));
        Assert.assertFalse(accessor.canHandle(feature, "illegal", null));
        Assert.assertNull(accessor.get(feature, "illegal", null));
    }

    @Test
    public void testGetType() {
        Assert.assertEquals(type.getDescriptor("foo"), accessor.get(type, "foo", null));
        Assert.assertEquals(type.getDescriptor("bar"), accessor.get(type, "bar", null));
        Assert.assertNull(accessor.get(type, "illegal", null));
    }

    @Test
    public void testSet() {
        try {
            accessor.set(feature, "foo", Integer.valueOf(2), null);
        } catch (IllegalAttributeException e) {
            Assert.fail();
        }
        Assert.assertEquals(Integer.valueOf(2), accessor.get(feature, "foo", null));

        try {
            accessor.set(feature, "bar", Double.valueOf(1.0), null);
        } catch (IllegalAttributeException e) {
            Assert.fail();
        }
        Assert.assertEquals(Double.valueOf(1.0), accessor.get(feature, "bar", null));
        try {
            accessor.set(feature, "@id", "fid2", null);
            Assert.fail("Should have thrown exception trying to set fid");
        } catch (IllegalAttributeException e) {
        }
    }

    @Test
    public void testSetType() {
        try {
            accessor.set(type, "foo", new Object(), null);
            Assert.fail("trying to set attribute type should have thrown exception");
        } catch (IllegalAttributeException e) {
        }
    }

    @Test
    public void testGetAnyGeometry() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("test");
        tb.setCRS(null);
        tb.add("g1", Point.class);
        tb.add("g2", Point.class);
        tb.setDefaultGeometry("g1");

        SimpleFeatureType type = tb.buildFeatureType();

        SimpleFeatureBuilder b = new SimpleFeatureBuilder(type);
        b.set("g1", null);

        Point p = new GeometryFactory().createPoint(new Coordinate(0, 0));
        b.set("g2", p);
        SimpleFeature feature = b.buildFeature(null);

        Assert.assertNull(feature.getDefaultGeometry());
        Assert.assertEquals(
                p, SimpleFeaturePropertyAccessorFactory.DEFAULT_GEOMETRY_ACCESS.get(feature, "", Geometry.class));
    }
}
