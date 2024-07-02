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

import java.util.Collections;
import org.geotools.api.data.Query;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.feature.type.GeometryType;
import org.geotools.api.feature.type.Schema;
import org.geotools.api.filter.Filter;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.geotools.feature.type.SchemaImpl;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

public class SimpleTypeBuilderTest {

    static final String URI = "gopher://localhost/test";

    SimpleFeatureTypeBuilder builder;

    @Before
    public void setUp() throws Exception {
        Schema schema = new SchemaImpl("test");

        FeatureTypeFactoryImpl typeFactory = new FeatureTypeFactoryImpl();
        AttributeType pointType =
                typeFactory.createGeometryType(
                        new NameImpl("test", "pointType"),
                        Point.class,
                        null,
                        false,
                        false,
                        Collections.emptyList(),
                        null,
                        null);
        schema.put(new NameImpl("test", "pointType"), pointType);

        AttributeType intType =
                typeFactory.createAttributeType(
                        new NameImpl("test", "intType"),
                        Integer.class,
                        false,
                        false,
                        Collections.emptyList(),
                        null,
                        null);
        schema.put(new NameImpl("test", "intType"), intType);

        builder = new SimpleFeatureTypeBuilder(new FeatureTypeFactoryImpl());
        builder.setBindings(schema);
    }

    @Test
    public void testSanity() {
        builder.setName("testName");
        builder.setNamespaceURI("testNamespaceURI");
        builder.add("point", Point.class, (CoordinateReferenceSystem) null);
        builder.add("integer", Integer.class);

        SimpleFeatureType type = builder.buildFeatureType();
        Assert.assertNotNull(type);

        Assert.assertEquals(2, type.getAttributeCount());

        AttributeType t = type.getType("point");
        Assert.assertNotNull(t);
        Assert.assertEquals(Point.class, t.getBinding());

        t = type.getType("integer");
        Assert.assertNotNull(t);
        Assert.assertEquals(Integer.class, t.getBinding());

        t = type.getGeometryDescriptor().getType();
        Assert.assertNotNull(t);
        Assert.assertEquals(Point.class, t.getBinding());
    }

    @Test
    public void testCRS() {
        builder.setName("testName");
        builder.setNamespaceURI("testNamespaceURI");

        builder.setCRS(DefaultGeographicCRS.WGS84);
        builder.crs(null).add("point", Point.class);
        builder.add("point2", Point.class, DefaultGeographicCRS.WGS84);
        builder.setDefaultGeometry("point");
        SimpleFeatureType type = builder.buildFeatureType();
        Assert.assertEquals(DefaultGeographicCRS.WGS84, type.getCoordinateReferenceSystem());

        Assert.assertNull(type.getGeometryDescriptor().getType().getCoordinateReferenceSystem());
        Assert.assertEquals(
                DefaultGeographicCRS.WGS84,
                ((GeometryType) type.getType("point2")).getCoordinateReferenceSystem());
    }

    @Test
    public void testAttributeDefaultValue() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("buggy");
        builder.nillable(false).defaultValue(12).add("attrWithDefault", Integer.class);
        builder.nillable(true).defaultValue(null).add("attrWithoutDefault", Integer.class);
        SimpleFeatureType featureType = builder.buildFeatureType();
        Assert.assertFalse(featureType.getDescriptor("attrWithDefault").isNillable());
        Assert.assertEquals(12, featureType.getDescriptor("attrWithDefault").getDefaultValue());
        Assert.assertTrue(featureType.getDescriptor("attrWithoutDefault").isNillable());
        Assert.assertNull(featureType.getDescriptor("attrWithoutDefault").getDefaultValue());
    }

    @Test
    public void testMaintainDefaultGeometryOnRetype() {
        builder.setName("testGeometries");
        builder.add("geo1", Point.class, DefaultGeographicCRS.WGS84);
        builder.add("geo2", Polygon.class, DefaultGeographicCRS.WGS84);
        builder.setDefaultGeometry("geo1");
        SimpleFeatureType type = builder.buildFeatureType();

        // performing an attribute selection, even changing order, should not change
        // the default geometry, that had a special meaning in the original source
        SimpleFeatureType retyped = SimpleFeatureTypeBuilder.retype(type, "geo2", "geo1");
        Assert.assertEquals("geo1", retyped.getGeometryDescriptor().getLocalName());
    }

    @Test
    public void testRetypeQuery() throws Exception {
        builder.setName("Location");
        builder.add("geom", Point.class, DefaultGeographicCRS.WGS84);
        builder.add("name", String.class);
        builder.add("number", Integer.class);
        builder.setDefaultGeometry("geom");
        SimpleFeatureType type = builder.buildFeatureType();

        CoordinateReferenceSystem ballWorld = CRS.decode("EPSG:3857");
        // Changing projection should work when reducing number of attributes
        Query query = new Query("Location", Filter.INCLUDE, "geom", "name");
        query.setCoordinateSystem(ballWorld);

        SimpleFeatureType retyped = SimpleFeatureTypeBuilder.retype(type, query);
        Assert.assertSame("crs", ballWorld, retyped.getCoordinateReferenceSystem());
        Assert.assertEquals("geom", retyped.getGeometryDescriptor().getLocalName());
        Assert.assertSame(
                "geom crs",
                ballWorld,
                retyped.getGeometryDescriptor().getCoordinateReferenceSystem());
    }

    @Test
    public void testRetypeGeometryless() {
        builder.setName("testGeometryless");
        builder.add("geo1", Point.class, DefaultGeographicCRS.WGS84);
        builder.add("integer", Integer.class);
        builder.setDefaultGeometry("geo1");
        SimpleFeatureType type = builder.buildFeatureType();

        // performing an attribute selection, even changing order, should not change
        // the default geometry, that had a special meaning in the original source
        SimpleFeatureType retyped = SimpleFeatureTypeBuilder.retype(type, "integer");
        Assert.assertNotNull(retyped);
        Assert.assertNull(retyped.getGeometryDescriptor());
        Assert.assertEquals(1, retyped.getAttributeCount());
        Assert.assertEquals("integer", retyped.getAttributeDescriptors().get(0).getLocalName());
    }

    @Test
    public void testInitGeometryless() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("testGeometryless");
        builder.add("integer", Integer.class);
        SimpleFeatureType type1 = builder.buildFeatureType();

        builder = new SimpleFeatureTypeBuilder();
        builder.init(type1);
        SimpleFeatureType type2 = builder.buildFeatureType();

        Assert.assertEquals(type1, type2);
    }

    @Test
    public void testMaintainDefaultGeometryOnInit() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("testGeometries");
        builder.add("geo1", Point.class, DefaultGeographicCRS.WGS84);
        builder.add("geo2", Polygon.class, DefaultGeographicCRS.WGS84);
        builder.setDefaultGeometry("geo2");
        SimpleFeatureType type1 = builder.buildFeatureType();

        builder = new SimpleFeatureTypeBuilder();
        builder.init(type1);
        SimpleFeatureType type2 = builder.buildFeatureType();

        Assert.assertEquals("geo2", type1.getGeometryDescriptor().getLocalName());
        Assert.assertEquals("geo2", type2.getGeometryDescriptor().getLocalName());
    }

    @Test
    public void testRemoveDefaultGeometryAfterInit() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("testGeometries");
        builder.add("geo1", Point.class, DefaultGeographicCRS.WGS84);
        builder.add("geo2", Polygon.class, DefaultGeographicCRS.WGS84);
        builder.setDefaultGeometry("geo2");
        SimpleFeatureType type1 = builder.buildFeatureType();

        builder = new SimpleFeatureTypeBuilder();
        builder.init(type1);
        builder.remove("geo2");
        SimpleFeatureType type2 = builder.buildFeatureType();

        Assert.assertEquals("geo2", type1.getGeometryDescriptor().getLocalName());
        Assert.assertEquals("geo1", type2.getGeometryDescriptor().getLocalName());
    }
}
