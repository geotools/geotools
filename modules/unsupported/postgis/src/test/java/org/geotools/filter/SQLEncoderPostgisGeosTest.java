/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.geotools.data.jdbc.fidmapper.BasicFIDMapper;
import org.geotools.data.jdbc.fidmapper.TypedFIDMapper;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

/**
 *  DJB added this because there were NO testcases for SQLEncoderGeos!!!
 *    I dont really know what I'm doing for this, but I'm just copying the SQLEncoderPostgis tests and
 *    adding one more.
 * 
 * TODO: roll into SQLEncoderPostgisTest (SQLEncoderPostgisGeos is deprecated)
 *
 *
 * @source $URL$
 */
public class SQLEncoderPostgisGeosTest extends TestCase {
	   /** Standard logging instance */
    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.filter");
    /** Schema on which to preform tests */
    protected static SimpleFeatureType testSchema = null;

    /** Schema on which to preform tests */
    protected static SimpleFeature testFeature = null;

    /** Test suite for this test case */
    TestSuite suite = null;

    /** folder where test data is stored.. */
    String dataFolder = "";
    protected boolean setup = false;
    
    
    public SQLEncoderPostgisGeosTest(String testName) {
        super(testName);
        LOGGER.finer("running SQLEncoderTests - SQLEncoderPostgisGeosTest");
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
    LOGGER.finer("creating flat feature...");

    SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
	ftb.add("testGeometry", LineString.class);
	ftb.add("testBoolean", Boolean.class);
	ftb.add("testCharacter", Character.class);
	ftb.add("testByte", Byte.class);
	ftb.add("testShort", Short.class);
	ftb.add("testInteger", Integer.class);
	ftb.add("testLong", Long.class);
	ftb.add("testFloat", Float.class);
	ftb.add("testDouble", Double.class);
	ftb.add("testString", String.class);
	ftb.add("testZeroDouble", Double.class);
	ftb.setName("testSchema");
    testSchema = ftb.buildFeatureType();

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
    LOGGER.finer("...flat feature created");

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
    TestSuite suite = new TestSuite(SQLEncoderPostgisGeosTest.class);

    return suite;
}

public void test1() throws Exception {
	FilterFactory factory = FilterFactoryFinder.createFilterFactory();
    GeometryFilter gf = factory.createGeometryFilter(AbstractFilter.GEOMETRY_BBOX);
    LiteralExpressionImpl right = new BBoxExpressionImpl(new Envelope(0,
                300, 0, 300));
    gf.addRightGeometry(right);

    AttributeExpressionImpl left = new AttributeExpressionImpl(testSchema,
            "testGeometry");
    gf.addLeftGeometry(left);

    SQLEncoderPostgisGeos encoder = new SQLEncoderPostgisGeos();
    encoder.setLooseBbox(true);
    encoder.setSRID(2356);

    String out = encoder.encode(gf);
    LOGGER.fine("Resulting SQL filter is \n" + out);
    assertEquals("WHERE \"testGeometry\" && GeometryFromText('POLYGON"
        + " ((0 0, 0 300, 300 300, 300 0, 0 0))'" + ", 2356)", out);
}

public void test2() throws Exception {
	FilterFactory factory = FilterFactoryFinder.createFilterFactory();
    GeometryFilter gf = factory.createGeometryFilter(AbstractFilter.GEOMETRY_BBOX);
    LiteralExpressionImpl left = new BBoxExpressionImpl(new Envelope(10,
                300, 10, 300));
    gf.addLeftGeometry(left);

    SQLEncoderPostgisGeos encoder = new SQLEncoderPostgisGeos(2346);
    encoder.setDefaultGeometry("testGeometry");

    String out = encoder.encode(gf);
    LOGGER.fine("Resulting SQL filter is \n" + out);
    assertEquals(out,
        "WHERE GeometryFromText('POLYGON ((10 10, 10 300, 300 300, 300 10, 10 10))', 2346) && \"testGeometry\" AND intersects(GeometryFromText('POLYGON ((10 10, 10 300, 300 300, 300 10, 10 10))', 2346), \"testGeometry\")");
}

public void testFid() throws Exception {
    FilterFactory filterFac = FilterFactoryFinder.createFilterFactory();

    FidFilter fidFilter = filterFac.createFidFilter("road.345");
    SQLEncoderPostgisGeos encoder = new SQLEncoderPostgisGeos();
    encoder.setFIDMapper(new TypedFIDMapper(
            new BasicFIDMapper("gid", 255, true), "road"));

    String out = encoder.encode((AbstractFilterImpl) fidFilter);
    LOGGER.fine("Resulting SQL filter is \n" + out);
    assertEquals(out, "WHERE (\"gid\" = '345')");
}

public void test3() throws Exception {
    FilterFactory filterFac = FilterFactoryFinder.createFilterFactory();
    CompareFilter compFilter = filterFac.createCompareFilter(AbstractFilter.COMPARE_EQUALS);
    compFilter.addLeftValue(filterFac.createAttributeExpression(
            testSchema, "testInteger"));
    compFilter.addRightValue(filterFac.createLiteralExpression(
            new Double(5)));

    SQLEncoderPostgisGeos encoder = new SQLEncoderPostgisGeos(2346);
    String out = encoder.encode(compFilter);
    LOGGER.fine("Resulting SQL filter is \n" + out);
    assertEquals(out, "WHERE \"testInteger\" = 5.0");
}

//DJB: to test disjoint's behavior
public void test4() throws Exception{
	FilterFactory factory = FilterFactoryFinder.createFilterFactory();
    GeometryFilter gf = factory.createGeometryFilter(AbstractFilter.GEOMETRY_DISJOINT);
    LiteralExpressionImpl left = new BBoxExpressionImpl(new Envelope(10,
                300, 10, 300));
    gf.addLeftGeometry(left);

    SQLEncoderPostgisGeos encoder = new SQLEncoderPostgisGeos(2346);
    encoder.setDefaultGeometry("testGeometry");

    String out = encoder.encode(gf);
    
    LOGGER.fine("Resulting SQL filter is \n" + out);
    assertEquals(out,
        "WHERE NOT (intersects(GeometryFromText('POLYGON ((10 10, 10 300, 300 300, 300 10, 10 10))', 2346), \"testGeometry\"))");
}

public void testException() throws Exception {
	FilterFactory factory = FilterFactoryFinder.createFilterFactory();
    GeometryFilter gf = factory.createGeometryFilter(AbstractFilter.GEOMETRY_BEYOND);
    LiteralExpressionImpl right = new BBoxExpressionImpl(new Envelope(10,
                10, 300, 300));
    gf.addRightGeometry(right);

    AttributeExpressionImpl left = new AttributeExpressionImpl(testSchema,
            "testGeometry");
    gf.addLeftGeometry(left);

    try {
        SQLEncoderPostgisGeos encoder = new SQLEncoderPostgisGeos(2346);
        String out = encoder.encode(gf);
        LOGGER.fine("out is " + out);
        fail("This filter type should not be supported at the moment?");
    } catch (SQLEncoderException e) {
        LOGGER.fine(e.getMessage());
        // good, this is expected
        assertEquals("Filter type not supported", e.getMessage());
    }
}

}
