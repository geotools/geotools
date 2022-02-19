/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

public class FeatureFlatTest {

    /** Feature on which to preform tests */
    private SimpleFeature testFeature = null;

    @Before
    public void setUp() {
        testFeature = SampleFeatureFixtures.createFeature();
    }

    @Test
    public void testRetrieve() {
        GeometryFactory gf = new GeometryFactory();
        Assert.assertTrue(
                "geometry retrieval and match",
                ((Point) testFeature.getAttribute("testGeometry"))
                        .equalsExact(gf.createPoint(new Coordinate(1, 2))));
        assertEquals(
                "boolean retrieval and match",
                testFeature.getAttribute("testBoolean"),
                Boolean.TRUE);
        assertEquals(
                "character retrieval and match",
                testFeature.getAttribute("testCharacter"),
                Character.valueOf('t'));
        assertEquals(
                "byte retrieval and match",
                testFeature.getAttribute("testByte"),
                Byte.valueOf("10"));
        assertEquals(
                "short retrieval and match",
                testFeature.getAttribute("testShort"),
                Short.valueOf("101"));
        assertEquals(
                "integer retrieval and match",
                testFeature.getAttribute("testInteger"),
                Integer.valueOf(1002));
        assertEquals(
                "long retrieval and match",
                testFeature.getAttribute("testLong"),
                Long.valueOf(10003));
        assertEquals(
                "float retrieval and match",
                testFeature.getAttribute("testFloat"),
                Float.valueOf(10000.4f));
        assertEquals(
                "double retrieval and match",
                testFeature.getAttribute("testDouble"),
                Double.valueOf(100000.5));
        assertEquals(
                "string retrieval and match",
                "test string data",
                testFeature.getAttribute("testString"));
    }

    @Test
    public void testBogusCreation() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("test1");
        tb.nillable(false).add("billy", String.class);
        tb.nillable(false).add("jimmy", String.class);

        SimpleFeatureType test = tb.buildFeatureType();
        //        try {
        //            SimpleFeatureBuilder.build(test, (Object[])null, null);
        //            fail("no error");
        //        } catch (IllegalAttributeException iae) {
        //        }

        try {
            SimpleFeatureBuilder.build(test, new Object[32], null);
            Assert.fail("no error");
        } catch (Exception e) {
        }
    }

    @Test
    public void testBounds() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        Geometry[] g = new Geometry[4];
        g[0] = gf.createPoint(new Coordinate(0, 0));
        g[1] = gf.createPoint(new Coordinate(0, 10));
        g[2] = gf.createPoint(new Coordinate(10, 0));
        g[3] = gf.createPoint(new Coordinate(10, 10));

        GeometryCollection gc = gf.createGeometryCollection(g);
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("bounds");
        tb.setCRS(null);
        tb.add("p1", Point.class);
        tb.add("p2", Point.class);
        tb.add("p3", Point.class);
        tb.add("p4", Point.class);
        SimpleFeatureType t = tb.buildFeatureType();

        SimpleFeature f = SimpleFeatureBuilder.build(t, g, null);
        assertEquals(gc.getEnvelopeInternal(), f.getBounds());

        g[1].getCoordinate().y = 20;
        g[2].getCoordinate().x = 20;
        f.setAttribute(1, g[1]);
        f.setAttribute(2, g[2]);
        gc = gf.createGeometryCollection(g);
        assertEquals(gc.getEnvelopeInternal(), f.getBounds());
    }

    @Test
    public void testClone() {
        SimpleFeature f = SampleFeatureFixtures.createFeature();
        SimpleFeature c = SimpleFeatureBuilder.copy(f);
        for (int i = 0, ii = c.getAttributeCount(); i < ii; i++) {
            assertEquals(c.getAttribute(i), f.getAttribute(i));
        }
    }

    @Test
    public void testClone2() throws Exception {
        SimpleFeatureType type = SampleFeatureFixtures.createTestType();
        Object[] attributes = SampleFeatureFixtures.createAttributes();
        SimpleFeature feature = SimpleFeatureBuilder.build(type, attributes, "fid");
        SimpleFeature clone = SimpleFeatureBuilder.deep(feature);
        assertEquals("Clone was not equal", feature, clone);
    }

    @Test
    @SuppressWarnings("ReturnValueIgnored")
    public void testToStringWontThrow() throws IllegalAttributeException {
        SimpleFeature f = SampleFeatureFixtures.createFeature();
        f.setAttributes(new Object[f.getAttributeCount()]);
        f.toString();
    }

    static AttributeDescriptor newAtt(String name, Class c) {
        AttributeTypeBuilder ab = new AttributeTypeBuilder();
        ab.setBinding(c);
        return ab.buildDescriptor(name);
    }

    static AttributeDescriptor newAtt(String name, Class c, boolean nillable) {
        AttributeTypeBuilder ab = new AttributeTypeBuilder();
        ab.setNillable(nillable);
        ab.setBinding(c);
        return ab.buildDescriptor(name);
    }

    @Test
    public void testModify() throws IllegalAttributeException {
        String newData = "new test string data";
        testFeature.setAttribute("testString", newData);
        assertEquals(
                "match modified (string) attribute",
                testFeature.getAttribute("testString"),
                newData);

        GeometryFactory gf = new GeometryFactory();
        Point newGeom = gf.createPoint(new Coordinate(3, 4));
        testFeature.setAttribute("testGeometry", newGeom);
        assertEquals(
                "match modified (geometry) attribute",
                testFeature.getAttribute("testGeometry"),
                newGeom);

        testFeature.setDefaultGeometry(newGeom);
        assertEquals(
                "match modified (geometry) attribute",
                testFeature.getAttribute("testGeometry"),
                newGeom);
    }

    //    public void testAttributeAccess() throws Exception {
    //        // this ones kinda silly
    //    	SimpleFeature f = (SimpleFeature)SampleFeatureFixtures.createFeature();
    //        List atts = f.getAttributes();
    //        for (int i = 0, ii = atts.size(); i < ii; i++) {
    //            assertEquals(atts.get(i), f.getAttribute(i));
    //        }
    //        List attsAgain = f.getAttributes();
    //        assertTrue(atts != attsAgain);
    //        f.setAttributes(atts);
    //        attsAgain = f.getAttributes();
    //        assertTrue(atts != attsAgain);
    //        for (int i = 0, ii = atts.size(); i < ii; i++) {
    //            assertEquals(atts.get(i), f.getAttribute(i));
    //            assertEquals(attsAgain.get(i), f.getAttribute(i));
    //        }
    //        try {
    //            f.setAttribute(1244, "x");
    //            fail("not out of bounds");
    //        } catch (ArrayIndexOutOfBoundsException aioobe) {
    //
    //        }
    //        catch (IndexOutOfBoundsException ioobe) {
    //
    //        }
    //        try {
    //            f.setAttribute("1244", "x");
    //            fail("allowed bogus attribute setting");
    //        } catch (IllegalAttributeException iae) {
    //
    //        }
    //        try {
    //            f.setAttribute("testGeometry", "x");
    //            fail("allowed bogus attribute setting");
    //        } catch (IllegalAttributeException iae) {
    //
    //        } catch (RuntimeException rt) {
    //        }
    //    }

    // IanS - this is no longer good, cause we deal with parsing
    //    public void testEnforceType() {
    //
    //        Date d = new Date();
    //
    //        Feature f = SampleFeatureFixtures.createFeature();
    //        for (int i = 0, ii = f.getNumberOfAttributes(); i < ii; i++) {
    //            try {
    //                f.setAttribute(i, d);
    //            } catch (IllegalAttributeException iae) {
    //                continue;
    //            }
    //            fail("No error thrown during illegal set");
    //        }
    //
    //    }

    @Test
    public void testEquals() throws Exception {
        SimpleFeature f1 = SampleFeatureFixtures.createFeature();
        SimpleFeature f2 = SampleFeatureFixtures.createFeature();
        assertEquals(f1, f1);
        assertEquals(f2, f2);
        assertNotEquals(f1, f2);
        assertNotEquals(null, f1);

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("different");
        tb.add("name", String.class);
        SimpleFeatureType type = tb.buildFeatureType();

        assertNotEquals(f1, SimpleFeatureBuilder.build(type, new Object[1], null));
    }

    /*
     * This is actually a test for FeatureTypeFlat, but there is no test for that
     * written right now, so I'm just putting it here, as I just changed the
     * getDefaultGeometry method, and it should have a unit test.  It tests
     * to make sure getDefaultGeometry returns null if there is no geometry,
     * as we now allow
     */
    //    public void testDefaultGeometry() throws Exception {
    //        SimpleFeatureType testType = testFeature.getFeatureType();
    //        AttributeDescriptor geometry = testType.getAttribute("testGeometry");
    //        assertTrue(geometry == testType.getDefaultGeometry());
    //
    // assertTrue(((Geometry)testFeature.getDefaultGeometry()).getEnvelopeInternal().equals(testFeature.getBounds()));
    //
    //        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
    //        tb.setName( "different" );
    //        tb.add( "name", String.class );
    //
    //        SimpleFeatureType another = tb.buildFeatureType();
    //        SimpleFeature f1 = SimpleFeatureBuilder.build(another, new Object[1], null);
    //
    //        assertEquals(null, f1.getDefaultGeometry());
    //        try {
    //            f1.setDefaultGeometry(null);
    //            fail("allowed bogus default geometry set ");
    //        } catch (IllegalAttributeException iae) {
    //
    //        }
    //    }

}
