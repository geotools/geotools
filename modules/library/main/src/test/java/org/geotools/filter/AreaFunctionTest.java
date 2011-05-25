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
package org.geotools.filter;

import java.util.Arrays;
import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.expression.PropertyName;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;


/**
 * Unit test for expressions.  This is a complimentary test suite with the
 * filter test suite.
 *
 * @author James MacGill, CCG
 * @author Rob Hranac, TOPP
 *
 * @source $URL$
 */                                
public class AreaFunctionTest extends TestCase {

      /** Standard logging instance */
    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.filter");
    /** Feature on which to preform tests */
    private static SimpleFeature testFeature = null;

    /** Schema on which to preform tests */
    private static SimpleFeatureType testSchema = null;
    boolean setup = false;
    /** Test suite for this test case */
    TestSuite suite = null;


    /** 
     * Constructor with test name.
     */
    public AreaFunctionTest(String testName) {
        super(testName);
        //BasicConfigurator.configure();
    
        
    }        
    
    /** 
     * Main for test runner.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    /** 
     * Required suite builder.
     * @return A test suite for this unit test.
     */
    public static Test suite() {
       
        TestSuite suite = new TestSuite(AreaFunctionTest.class);
        return suite;
    }
    
    /** 
     * Sets up a schema and a test feature.
     * @throws SchemaException If there is a problem setting up the schema.
     * @throws IllegalFeatureException If problem setting up the feature.
     */
    protected void setUp() throws SchemaException, IllegalAttributeException {
        if (setup) {
            return;
        }
        prepareFeatures();

        setup = true;
    }

    //HACK - this is cut and pasted from filter module tests.  Should be 
    //in a test support module.
    protected void prepareFeatures()
        throws SchemaException, IllegalAttributeException {
        //_log.getLoggerRepository().setThreshold(Level.INFO);
        // Create the schema attributes
        LOGGER.finer("creating flat feature...");

        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.add("testGeometry", Polygon.class);
        LOGGER.finer("created geometry attribute");

        ftb.add("testBoolean", Boolean.class);
        LOGGER.finer("created boolean attribute");

        ftb.add("testCharacter", Character.class);
        ftb.add("testByte", Byte.class);
        ftb.add("testShort", Short.class);
        ftb.add("testInteger", Integer.class);
        ftb.add("testLong", Long.class);
        ftb.add("testFloat", Float.class);
        ftb.add("testDouble", Double.class);
        ftb.add("testString", String.class);
        ftb.setName("testSchema");
        testSchema = ftb.buildFeatureType();

        // Creates coordinates for the linestring
        Coordinate[] coords = new Coordinate[5];
        coords[0] = new Coordinate(0, 0);
        coords[1] = new Coordinate(10, 0);
        coords[2] = new Coordinate(10, 10);
        coords[3] = new Coordinate(0, 10);
        coords[4] = new Coordinate(0, 0);

        // Builds the test feature
        Object[] attributes = new Object[10];
        GeometryFactory gf = new GeometryFactory(new PrecisionModel());
        LinearRing ring = gf.createLinearRing(coords);
        attributes[0] = gf.createPolygon(ring,null);
        attributes[1] = new Boolean(true);
        attributes[2] = new Character('t');
        attributes[3] = new Byte("10");
        attributes[4] = new Short("101");
        attributes[5] = new Integer(1002);
        attributes[6] = new Long(10003);
        attributes[7] = new Float(10000.4);
        attributes[8] = new Double(100000.5);
        attributes[9] = "test string data";

        // Creates the feature itself
        testFeature = SimpleFeatureBuilder.build(testSchema, attributes, null);
        LOGGER.finer("...flat feature created");

        //_log.getLoggerRepository().setThreshold(Level.DEBUG);
    }

    static org.opengis.filter.FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);
    
     /** 
     * Tests the min function expression.
     */
    public void testAreaFunction()
        throws IllegalFilterException {
            
        PropertyName a = filterFactory.property("testGeometry");         
        
        AreaFunction area = new AreaFunction();
        area.setParameters(Arrays.asList(new org.opengis.filter.expression.Expression[] {a}));         
        assertEquals(100d,((Double)area.evaluate(testFeature)).doubleValue(),0);
    }
    
   
    
}
