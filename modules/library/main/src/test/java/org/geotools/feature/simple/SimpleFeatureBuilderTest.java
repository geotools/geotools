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

import java.util.Arrays;
import junit.framework.TestCase;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.AttributeTypeBuilder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class SimpleFeatureBuilderTest extends TestCase {

    SimpleFeatureBuilder builder;

    protected void setUp() throws Exception {
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.setName("test");
        typeBuilder.add("point", Point.class, (CoordinateReferenceSystem) null);
        typeBuilder.add("integer", Integer.class);
        typeBuilder.add("float", Float.class);

        SimpleFeatureType featureType = typeBuilder.buildFeatureType();

        builder = new SimpleFeatureBuilder(featureType);
        builder.setValidating(true);
    }

    public void testSanity() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        builder.add(gf.createPoint(new Coordinate(0, 0)));
        builder.add(Integer.valueOf(1));
        builder.add(Float.valueOf(2.0f));

        SimpleFeature feature = builder.buildFeature("fid");
        assertNotNull(feature);

        assertEquals(3, feature.getAttributeCount());

        assertTrue(gf.createPoint(new Coordinate(0, 0)).equals(feature.getAttribute("point")));
        assertEquals(Integer.valueOf(1), feature.getAttribute("integer"));
        assertEquals(Float.valueOf(2.0f), feature.getAttribute("float"));
    }

    public void testTooFewAttributes() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        builder.add(gf.createPoint(new Coordinate(0, 0)));
        builder.add(Integer.valueOf(1));

        SimpleFeature feature = builder.buildFeature("fid");
        assertNotNull(feature);

        assertEquals(3, feature.getAttributeCount());

        assertTrue(gf.createPoint(new Coordinate(0, 0)).equals(feature.getAttribute("point")));
        assertEquals(Integer.valueOf(1), feature.getAttribute("integer"));
        assertNull(feature.getAttribute("float"));
    }

    public void testSetSequential() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        builder.set("point", gf.createPoint(new Coordinate(0, 0)));
        builder.set("integer", Integer.valueOf(1));
        builder.set("float", Float.valueOf(2.0f));

        SimpleFeature feature = builder.buildFeature("fid");
        assertNotNull(feature);

        assertEquals(3, feature.getAttributeCount());

        assertTrue(gf.createPoint(new Coordinate(0, 0)).equals(feature.getAttribute(0)));
        assertEquals(Integer.valueOf(1), feature.getAttribute(1));
        assertEquals(Float.valueOf(2.0f), feature.getAttribute(2));
    }

    public void testSetNonSequential() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        builder.set("float", Float.valueOf(2.0f));
        builder.set("point", gf.createPoint(new Coordinate(0, 0)));
        builder.set("integer", Integer.valueOf(1));

        SimpleFeature feature = builder.buildFeature("fid");
        assertNotNull(feature);

        assertEquals(3, feature.getAttributeCount());

        assertTrue(gf.createPoint(new Coordinate(0, 0)).equals(feature.getAttribute(0)));
        assertEquals(Integer.valueOf(1), feature.getAttribute(1));
        assertEquals(Float.valueOf(2.0f), feature.getAttribute(2));
    }

    public void testSetTooFew() throws Exception {
        builder.set("integer", Integer.valueOf(1));
        SimpleFeature feature = builder.buildFeature("fid");
        assertNotNull(feature);

        assertEquals(3, feature.getAttributeCount());

        assertNull(feature.getAttribute(0));
        assertEquals(Integer.valueOf(1), feature.getAttribute(1));
        assertNull(feature.getAttribute(2));
    }

    public void testConverting() throws Exception {
        builder.set("integer", "1");
        SimpleFeature feature = builder.buildFeature("fid");

        try {
            builder.set("integer", "foo");
            fail("should have failed");
        } catch (Exception e) {
        }
    }

    public void testCreateFeatureWithLength() throws Exception {

        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder(); // $NON-NLS-1$
        builder.setName("test");
        builder.length(5).add("name", String.class);

        SimpleFeatureType featureType = builder.buildFeatureType();
        SimpleFeature feature = SimpleFeatureBuilder.build(featureType, new Object[] {"Val"}, "ID");

        assertNotNull(feature);

        try {
            feature = SimpleFeatureBuilder.build(featureType, new Object[] {"Longer Than 5"}, "ID");
            feature.validate();
            fail("this should fail because the value is longer than 5 characters");
        } catch (Exception e) {
            // good
        }
    }

    public void testCreateFeatureWithRestriction() throws Exception {
        FilterFactory fac = CommonFactoryFinder.getFilterFactory(null);

        String attributeName = "string";
        PropertyIsEqualTo filter = fac.equals(fac.property("."), fac.literal("Value"));

        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder(); // $NON-NLS-1$
        builder.setName("test");
        builder.restriction(filter).add(attributeName, String.class);

        SimpleFeatureType featureType = builder.buildFeatureType();
        SimpleFeature feature =
                SimpleFeatureBuilder.build(featureType, new Object[] {"Value"}, "ID");

        assertNotNull(feature);

        try {
            SimpleFeature sf =
                    SimpleFeatureBuilder.build(featureType, new Object[] {"NotValue"}, "ID");
            sf.validate();
            fail("PropertyIsEqualTo filter should have failed");
        } catch (Exception e) {
            // good
        }
    }

    public void testCreateFeatureWithOptions() throws Exception {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("test");
        builder.options("a", "b", "c").add("name", String.class);
        SimpleFeatureType featureType = builder.buildFeatureType();

        // build a valid feature
        SimpleFeature feature = SimpleFeatureBuilder.build(featureType, new Object[] {"a"}, "ID");
        assertNotNull(feature);
        feature.validate();

        // try an invalid one
        try {
            feature = SimpleFeatureBuilder.build(featureType, new Object[] {"d"}, "ID");
            feature.validate();
            fail("this should fail because the value is not either a, b or c, but d");
        } catch (Exception e) {
            e.printStackTrace();
            // good
        }
    }

    /**
     * Same as {@link #testCreateFeatureWithOptions()} but using an AttributeTypeBuilder directly
     *
     * @throws Exception
     */
    public void testCreateFeatureWithOptionsOnAttributeTypeBuilder() throws Exception {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("test");
        AttributeTypeBuilder atb = new AttributeTypeBuilder();
        atb.name("name").binding(String.class).options(Arrays.asList("a", "b", "c"));
        builder.add(atb.buildDescriptor("name"));
        SimpleFeatureType featureType = builder.buildFeatureType();

        // build a valid feature
        SimpleFeature feature = SimpleFeatureBuilder.build(featureType, new Object[] {"a"}, "ID");
        assertNotNull(feature);
        feature.validate();

        // try an invalid one
        try {
            feature = SimpleFeatureBuilder.build(featureType, new Object[] {"d"}, "ID");
            feature.validate();
            fail("this should fail because the value is not either a, b or c, but d");
        } catch (Exception e) {
            e.printStackTrace();
            // good
        }
    }

    public void testUserData() throws Exception {
        SimpleFeature feature = buildUserDataFeature();
        assertNotNull(feature);
        assertEquals("bar", feature.getUserData().get("foo"));

        builder = new SimpleFeatureBuilder(feature.getFeatureType());
        builder.init(feature);
        feature = builder.buildFeature("fid");
        assertNotNull(feature);
        assertEquals("bar", feature.getUserData().get("foo"));
    }

    private SimpleFeature buildUserDataFeature() {
        GeometryFactory gf = new GeometryFactory();
        builder.add(gf.createPoint(new Coordinate(0, 0)));
        builder.add(Integer.valueOf(1));
        builder.add(Float.valueOf(2.0f));
        builder.featureUserData("foo", "bar");

        return builder.buildFeature("fid");
    }

    public void testCopyUserData() throws Exception {
        SimpleFeature template = buildUserDataFeature();
        builder = new SimpleFeatureBuilder(template.getFeatureType());
        GeometryFactory gf = new GeometryFactory();
        builder.add(gf.createPoint(new Coordinate(1, 1)));
        builder.add(Integer.valueOf(2));
        builder.add(Float.valueOf(4.0f));
        builder.featureUserData(template);
        SimpleFeature feature = builder.buildFeature("myfid");

        assertEquals("bar", feature.getUserData().get("foo"));
    }
}
