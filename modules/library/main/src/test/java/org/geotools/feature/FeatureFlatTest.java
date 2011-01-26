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

import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class FeatureFlatTest extends TestCase {

    /**
     * The logger for the default core module.
     */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.defaultcore");

    /** Feature on which to preform tests */
    private SimpleFeature testFeature = null;

    TestSuite suite = null;

    public FeatureFlatTest(String testName) {
        super(testName);
    }

    public static void main(String[] args) {
        org.geotools.util.logging.Logging.GEOTOOLS.forceMonolineConsoleOutput();
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(FeatureFlatTest.class);
        return suite;
    }

    public void setUp() {
        testFeature = SampleFeatureFixtures.createFeature();
    }

    public void testRetrieve() {
        GeometryFactory gf = new GeometryFactory();
        assertTrue(
            "geometry retrieval and match",
            ((Point) testFeature.getAttribute("testGeometry")).equals(
                gf.createPoint(new Coordinate(1, 2))));
        assertTrue(
            "boolean retrieval and match",
            ((Boolean) testFeature.getAttribute("testBoolean")).equals(new Boolean(true)));
        assertTrue(
            "character retrieval and match",
            ((Character) testFeature.getAttribute("testCharacter")).equals(new Character('t')));
        assertTrue("byte retrieval and match", ((Byte) testFeature.getAttribute("testByte")).equals(new Byte("10")));
        assertTrue(
            "short retrieval and match",
            ((Short) testFeature.getAttribute("testShort")).equals(new Short("101")));
        assertTrue(
            "integer retrieval and match",
            ((Integer) testFeature.getAttribute("testInteger")).equals(new Integer(1002)));
        assertTrue("long retrieval and match", ((Long) testFeature.getAttribute("testLong")).equals(new Long(10003)));
        assertTrue(
            "float retrieval and match",
            ((Float) testFeature.getAttribute("testFloat")).equals(new Float(10000.4)));
        assertTrue(
            "double retrieval and match",
            ((Double) testFeature.getAttribute("testDouble")).equals(new Double(100000.5)));
        assertTrue(
            "string retrieval and match",
            ((String) testFeature.getAttribute("testString")).equals("test string data"));

    }

    public void testBogusCreation() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName( "test1" );
        tb.nillable(false).add( "billy", String.class );
        tb.nillable(false).add( "jimmy", String.class );
        
        SimpleFeatureType test = tb.buildFeatureType();
//        try {
//            SimpleFeatureBuilder.build(test, (Object[])null, null);
//            fail("no error");
//        } catch (IllegalAttributeException iae) {
//        }

        try {
            SimpleFeatureBuilder.build(test, new Object[32],null);
            fail("no error");
        } catch (Exception e) {
        }

    }

    public void testBounds() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        Geometry[] g = new Geometry[4];
        g[0] = gf.createPoint(new Coordinate(0, 0));
        g[1] = gf.createPoint(new Coordinate(0, 10));
        g[2] = gf.createPoint(new Coordinate(10, 0));
        g[3] = gf.createPoint(new Coordinate(10, 10));

        GeometryCollection gc = gf.createGeometryCollection(g);
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName( "bounds" );
        
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

    public void testClone() {
        SimpleFeature f = SampleFeatureFixtures.createFeature();
        SimpleFeature c = SimpleFeatureBuilder.copy( f );
        for (int i = 0, ii = c.getAttributeCount(); i < ii; i++) {
            assertEquals(c.getAttribute(i), f.getAttribute(i));
        }
    }

    public void testClone2() throws Exception {
        SimpleFeatureType type = SampleFeatureFixtures.createTestType();
        Object[] attributes = SampleFeatureFixtures.createAttributes();
        SimpleFeature feature = SimpleFeatureBuilder.build(type, attributes, "fid");
        SimpleFeature clone = SimpleFeatureBuilder.deep(feature);
        assertTrue("Clone was not equal", feature.equals(clone));
    }

    public void testToStringWontThrow() throws IllegalAttributeException {
        SimpleFeature f = (SimpleFeature)SampleFeatureFixtures.createFeature();
        f.setAttributes(new Object[f.getAttributeCount()]);
        String s = f.toString();
    }

    static AttributeDescriptor newAtt(String name, Class c) {
        AttributeTypeBuilder ab = new AttributeTypeBuilder();
        ab.setBinding(c);
        return ab.buildDescriptor( name );
    }

    static AttributeDescriptor newAtt(String name, Class c, boolean nillable) {
        AttributeTypeBuilder ab = new AttributeTypeBuilder();
        ab.setNillable(nillable);
        ab.setBinding( c );
        return ab.buildDescriptor(name);
    }

    public void testModify() throws IllegalAttributeException {
        String newData = "new test string data";
        testFeature.setAttribute("testString", newData);
        assertEquals("match modified (string) attribute", testFeature.getAttribute("testString"), newData);

        GeometryFactory gf = new GeometryFactory();
        Point newGeom = gf.createPoint(new Coordinate(3, 4));
        testFeature.setAttribute("testGeometry", newGeom);
        assertEquals("match modified (geometry) attribute", testFeature.getAttribute("testGeometry"), newGeom);

        testFeature.setDefaultGeometry(newGeom);
        assertEquals("match modified (geometry) attribute", testFeature.getAttribute("testGeometry"), newGeom);

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

    public void testEquals() throws Exception {
        SimpleFeature f1 = SampleFeatureFixtures.createFeature();
        SimpleFeature f2 = SampleFeatureFixtures.createFeature();
        assertTrue(f1.equals(f1));
        assertTrue(f2.equals(f2));
        assertTrue(!f1.equals(f2));
        assertTrue(!f1.equals(null));
        
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName( "different" );
        tb.add( "name", String.class );
        SimpleFeatureType type = tb.buildFeatureType();
        
        assertTrue(!f1.equals(SimpleFeatureBuilder.build(type, new Object[1], null)));
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
//        assertTrue(((Geometry)testFeature.getDefaultGeometry()).getEnvelopeInternal().equals(testFeature.getBounds()));
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
