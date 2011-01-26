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
package org.geotools.data.geometryless.filter;

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
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;


/**
 * Unit test for SQLEncoderLocationsXY.  This is a complementary  test suite with
 * the filter test suite.
 *
 * @author Chris Holmes, TOPP
 * @source $URL$
 */
public class SQLEncoderLocationsXYTest extends TestCase {
    /** Standard logging instance */
    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.filter");

 //  private FilterFactory filterFactory = FilterFactoryFinder.createFilterFactory();
   private FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);
   
 
    /** Schema on which to preform tests */
    protected static SimpleFeatureType testSchema = null;

    /** Schema on which to preform tests */
    protected static SimpleFeature testFeature = null;

    /** Test suite for this test case */
    TestSuite suite = null;

    /** folder where test data is stored.. */
    String dataFolder = "";
    protected boolean setup = false;

    public SQLEncoderLocationsXYTest(String testName) {
        super(testName);
        System.out.println("running SQLEncoderTests");
        ;
        dataFolder = System.getProperty("dataFolder");

        if (dataFolder == null) {
            //then we are being run by maven
            dataFolder = System.getProperty("basedir");
            dataFolder += "/tests/unit/testData";
        }
    }

    protected void setUp() throws SchemaException, IllegalAttributeException {
        if (setup) {
            return;
        } else {
            prepareFeatures();
        }

        setup = true;
    }

    protected void prepareFeatures()
        throws SchemaException, IllegalAttributeException {
        //_log.getLoggerRepository().setThreshold(Level.INFO);
        // Create the schema attributes
        System.out.println("creating flat feature...");

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName( "testSchema" );
        
        tb.add("testGeometry", LineString.class);
        tb.add("testBoolean", Boolean.class);
        tb.add("testCharacter", Character.class);
        tb.add("testByte", Byte.class);
        tb.add("testShort", Short.class);
        tb.add("testInteger", Integer.class);
        tb.add("testLong", Long.class);
        tb.add("testFloat", Float.class);
        tb.add("testDouble", Double.class);
        tb.add("testString", String.class);

        // Builds the schema
        testSchema = tb.buildFeatureType();

        GeometryFactory geomFac = new GeometryFactory();

        // Creates coordinates for the linestring
        Coordinate[] coords = new Coordinate[3];
        coords[0] = new Coordinate(1, 2);
        coords[1] = new Coordinate(3, 4);
        coords[2] = new Coordinate(5, 6);

        // Builds the test feature
        Object[] attributes = new Object[10];
        attributes[0] = geomFac.createLineString(coords);
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
        System.out.println("...flat feature created");

        //_log.getLoggerRepository().setThreshold(Level.DEBUG);
    }

    /**
     * Main for test runner.
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * Required suite builder.
     *
     * @return A test suite for this unit test.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(SQLEncoderLocationsXYTest.class);

        return suite;
    }

    public void test1() throws Exception {
 //       GeometryFilter gf = filterFactory.createGeometryFilter(AbstractFilter.GEOMETRY_BBOX);

        Filter gf = filterFactory.bbox("testGeometry",0,
                300, 0, 300, "EPSG:2356");
        
//         GeometryFilterImpl gf = new GeometryFilterImpl(AbstractFilter.GEOMETRY_BBOX);
 /*       LiteralExpressionImpl right = new BBoxExpressionImpl(new Envelope(0,
                    300, 0, 300));
        gf.addRightGeometry(right);

        AttributeExpressionImpl left = new AttributeExpressionImpl(testSchema,
                "testGeometry");
        gf.addLeftGeometry(left);
*/
        SQLEncoderLocationsXY encoder = new SQLEncoderLocationsXY("long","lat");
        encoder.setSRID(2356);

        String out = encoder.encodeToString( gf);
        LOGGER.info("Resulting SQL filter is \n" + out);

        //assertEquals("WHERE \"testGeometry\" && GeometryFromText('POLYGON"
        //    + " ((0 0, 0 300, 300 300, 300 0, 0 0))'" + ", 2356)", out);
    }

    public void test2() throws Exception {
//        GeometryFilterImpl gf = new GeometryFilterImpl(AbstractFilter.GEOMETRY_BBOX);
        Filter gf = filterFactory.bbox("testGeometry",0,
                300, 0, 300, "EPSG:2356");
        
/*    	GeometryFilter gf = filterFactory.createGeometryFilter(AbstractFilter.GEOMETRY_BBOX);
        LiteralExpressionImpl left = new BBoxExpressionImpl(new Envelope(10,
                    300, 10, 300));
        gf.addLeftGeometry(left);
*/
        
        SQLEncoderLocationsXY encoder = new SQLEncoderLocationsXY("long","lat");
        encoder.setDefaultGeometry("testGeometry");

        String out = encoder.encodeToString( gf);
        System.out.println("Resulting SQL filter is \n" + out);

        //assertEquals(out,
        //    "WHERE GeometryFromText("
        //    + "'POLYGON ((10 10, 10 300, 300 300, 300 10, 10 10))'"
        //    + ", 2346) && \"testGeometry\"");
    }


    public void testException() throws Exception {
//        GeometryFilterImpl gf = new GeometryFilterImpl(AbstractFilter.GEOMETRY_BEYOND);

        Filter gf = filterFactory.bbox("testGeometry",0,
                300, 0, 300, "EPSG:2356");
        
/*    	GeometryFilter gf = filterFactory.createGeometryFilter(AbstractFilter.GEOMETRY_BEYOND);

        LiteralExpressionImpl right = new BBoxExpressionImpl(new Envelope(10,
                    10, 300, 300));
        gf.addRightGeometry(right);

        AttributeExpressionImpl left = new AttributeExpressionImpl(testSchema,
                "testGeometry");
        gf.addLeftGeometry(left);
*/
        try {
            SQLEncoderLocationsXY encoder = new SQLEncoderLocationsXY("long","lat");
            String out = encoder.encodeToString( gf);
            System.out.println("out is " + out);
        } catch (Exception e) {
            System.out.println(e.getMessage());

            //  assertEquals("Filter type not supported", e.getMessage());
        }
    }
}
