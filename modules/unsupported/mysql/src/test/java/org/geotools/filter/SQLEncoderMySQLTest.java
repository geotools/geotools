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

import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.geotools.data.jdbc.FilterToSQLException;
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
 * Unit test for SQLEncoderPostgis. This is a complimentary test suite with the
 * filter test suite.
 * 
 * @author Chris Holmes, TOPP
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/unsupported/mysql/src/test/java/org/geotools/filter/SQLEncoderMySQLTest.java $
 */
public class SQLEncoderMySQLTest extends TestCase {
    /** Standard logging instance */
    protected static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.filter");

    /** Schema on which to preform tests */
    protected static SimpleFeatureType testSchema = null;

    /** Schema on which to preform tests */
    protected static SimpleFeature testFeature = null;

    /** Test suite for this test case */
    TestSuite suite = null;

    /** folder where test data is stored.. */
    String dataFolder = "";

    protected boolean setup = false;

    public SQLEncoderMySQLTest(String testName) {
        super(testName);
        LOGGER.finer("running SQLEncoderTests");
        ;
        dataFolder = System.getProperty("dataFolder");

        if (dataFolder == null) {
            // then we are being run by maven
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

    protected void prepareFeatures() throws SchemaException, IllegalAttributeException {
        // _log.getLoggerRepository().setThreshold(Level.INFO);
        // Create the schema attributes
        LOGGER.finer("creating flat feature...");
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.add("testGeometry", LineString.class);
        LOGGER.finer("created geometry attribute");

        builder.add("testBoolean", Boolean.class);

        builder.add("testCharacter", Character.class);
        builder.add("testByte", Byte.class);
        builder.add("testShort", Short.class);
        builder.add("testInteger", Integer.class);
        builder.add("testLong", Long.class);
        builder.add("testFloat", Float.class);
        builder.add("testDouble", Double.class);
        builder.add("testString", String.class);

        builder.setName("testSchema");
        // Builds the schema
        testSchema = builder.buildFeatureType();

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
        testFeature = SimpleFeatureBuilder.build(testSchema, attributes, "fid.5");
        LOGGER.finer("...flat feature created");

        // _log.getLoggerRepository().setThreshold(Level.DEBUG);
    }

    /**
     * Main for test runner.
     * 
     * @param args
     *            DOCUMENT ME!
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
        TestSuite suite = new TestSuite(SQLEncoderMySQLTest.class);

        return suite;
    }

    public void testBbox() throws Exception {
        FilterFactory factory = FilterFactoryFinder.createFilterFactory();
        GeometryFilter gf = factory.createGeometryFilter(AbstractFilter.GEOMETRY_BBOX);
        LiteralExpressionImpl right = new BBoxExpressionImpl(new Envelope(0, 300, 0, 300));
        gf.addRightGeometry(right);

        AttributeExpressionImpl left = new AttributeExpressionImpl(testSchema, "testGeometry");
        gf.addLeftGeometry(left);

        SQLEncoderMySQL encoder = new SQLEncoderMySQL();
        encoder.setSRID(2356);

        String out = encoder.encodeToString(gf);
        LOGGER.fine("Resulting SQL filter is \n" + out);

        assertEquals(
                "WHERE MBRIntersects(testGeometry, GeometryFromText(\'POLYGON ((0 0, 0 300, 300 300, 300 0, 0 0))\', 2356))",
                out);
    }

    public void testBboxDefaultGeometry() throws Exception {
        FilterFactory factory = FilterFactoryFinder.createFilterFactory();
        GeometryFilter gf = factory.createGeometryFilter(AbstractFilter.GEOMETRY_BBOX);
        LiteralExpressionImpl left = new BBoxExpressionImpl(new Envelope(10, 300, 10, 300));
        gf.addRightGeometry(left);

        SQLEncoderMySQL encoder = new SQLEncoderMySQL(2346);
        encoder.setDefaultGeometry("testGeometry");

        String out = encoder.encodeToString(gf);
        LOGGER.fine("Resulting SQL filter is \n" + out);

        assertEquals("WHERE MBRIntersects(testGeometry, "
                + "GeometryFromText(\'POLYGON ((10 10, 10 300, 300 300, 300 10, 10 10))\', 2346))", out);
    }

    public void testFid() throws Exception {
        FilterFactory filterFac = FilterFactoryFinder.createFilterFactory();

        FidFilter fidFilter = filterFac.createFidFilter("road.345");
        SQLEncoderMySQL encoder = new SQLEncoderMySQL();
        encoder.setFIDMapper(new TypedFIDMapper(new BasicFIDMapper("gid", 255, true), "road"));

        String out = encoder.encodeToString((AbstractFilterImpl) fidFilter);
        LOGGER.fine("Resulting SQL filter is \n" + out);
        LOGGER.fine(out + "|" + "WHERE (gid = '345')");

         assertEquals(out, "WHERE (gid = '345')");
    }

    public void testCompareToDouble() throws Exception {
        FilterFactory filterFac = FilterFactoryFinder.createFilterFactory();
        CompareFilter compFilter = filterFac.createCompareFilter(AbstractFilter.COMPARE_EQUALS);
        compFilter.addLeftValue(filterFac.createAttributeExpression(testSchema, "testInteger"));
        compFilter.addRightValue(filterFac.createLiteralExpression(new Double(5)));

        SQLEncoderMySQL encoder = new SQLEncoderMySQL(2346);
        encoder.setFeatureType(testSchema);
        String out = encoder.encodeToString(compFilter);
        LOGGER.fine("Resulting SQL filter is \n" + out);

        assertEquals("WHERE testInteger = 5.0", out);
    }

    public void testException() throws Exception {
        FilterFactory filterFac = FilterFactoryFinder.createFilterFactory();
        GeometryFilter gf = filterFac.createGeometryFilter(AbstractFilter.GEOMETRY_BEYOND);
        LiteralExpressionImpl right = new BBoxExpressionImpl(new Envelope(10, 10, 300, 300));
        gf.addRightGeometry(right);

        AttributeExpressionImpl left = new AttributeExpressionImpl(testSchema, "testGeometry");
        gf.addLeftGeometry(left);

        try {
            SQLEncoderMySQL encoder = new SQLEncoderMySQL(2346);
            String out = encoder.encodeToString(gf);
            fail("We do not support beyond filters");
        } catch (FilterToSQLException e) {
            LOGGER.fine(e.getMessage());

            // assertEquals("Filter type not supported", e.getMessage());
        }
    }
}
