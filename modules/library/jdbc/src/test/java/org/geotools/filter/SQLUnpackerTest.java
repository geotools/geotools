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

import junit.framework.Test;
import junit.framework.TestSuite;

import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.geotools.filter.expression.AddImpl;


/**
 * Unit test for SQLUnpacker.
 *
 * @author Chris Holmes, TOPP
 * @source $URL$
 */
public class SQLUnpackerTest extends SQLFilterTestSupport {
    /** Standard logging instance */

    //private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.defaultcore");

    /** Filters on which to perform tests */
    private BetweenFilterImpl btwnFilter;
    private CompareFilter compFilter;
    private GeometryFilter geomFilter;
    private LikeFilterImpl likeFilter;
    private NullFilterImpl nullFilter;
    private AttributeExpressionImpl attrExp1;
    private AttributeExpressionImpl attrExp2;
    private LiteralExpressionImpl litExp1;
    private LiteralExpressionImpl litExp2;
    private MathExpressionImpl mathExp1;

    /** strings for Like filter */
    private String pattern = "te_st!";
    private String wcMulti = "!";
    private String wcSingle = "_";
    private String escape = "#";

    /** capabilities for unpacker */
    private FilterCapabilities capabilities;

    //    private SQLEncoder encoder;
    private SQLUnpacker unpacker;

    /** Test suite for this test case */
    TestSuite suite = null;

    /**
     * Constructor with test name.
     *
     * @param testName DOCUMENT ME!
     */
    public SQLUnpackerTest(String testName) {
        super(testName);
        LOGGER.finer("running SQLUnpackerTests");
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
        //_log.getLoggerRepository().setThreshold(Level.DEBUG);
        TestSuite suite = new TestSuite(SQLUnpackerTest.class);

        return suite;
    }

    /**
     * Sets up a schema and a test feature.
     *
     * @throws SchemaException If there is a problem setting up the schema.
     * @throws IllegalAttributeException If problem setting up the feature.
     */
    protected void setUp() throws SchemaException, IllegalAttributeException {
        //if(setup) return;
        super.setUp();
        setup = true;

        //_log.getLoggerRepository().setThreshold(Level.INFO);
        //Set capabilities for the SQLUnpacker
        capabilities = new FilterCapabilities();
        capabilities.addType(AbstractFilter.LOGIC_OR);
        capabilities.addType(AbstractFilter.LOGIC_AND);
        capabilities.addType(AbstractFilter.LOGIC_NOT);
        capabilities.addType(AbstractFilter.COMPARE_EQUALS);
        capabilities.addType(AbstractFilter.COMPARE_LESS_THAN);
        capabilities.addType(AbstractFilter.COMPARE_GREATER_THAN);
        capabilities.addType(AbstractFilter.COMPARE_LESS_THAN_EQUAL);
        capabilities.addType(AbstractFilter.COMPARE_GREATER_THAN_EQUAL);
        capabilities.addType(AbstractFilter.NULL);
        capabilities.addType(AbstractFilter.BETWEEN);
        unpacker = new SQLUnpacker(capabilities);

        try {
            attrExp1 = new AttributeExpressionImpl(testSchema, "testInteger");
            attrExp2 = new AttributeExpressionImpl(testSchema, "testGeometry");
            litExp1 = new LiteralExpressionImpl(new Integer(65));
            litExp2 = new LiteralExpressionImpl(new Integer(35));
            mathExp1 = new AddImpl(null,null);
            mathExp1.addLeftValue(litExp1);
            mathExp1.addRightValue(litExp2);

            btwnFilter = new BetweenFilterImpl();
            btwnFilter.addLeftValue(litExp1);
            btwnFilter.addMiddleValue(attrExp1);
            btwnFilter.addRightValue(mathExp1);

            FilterFactory factory = FilterFactoryFinder.createFilterFactory();
            compFilter = factory.createCompareFilter(AbstractFilter.COMPARE_LESS_THAN);
            compFilter.addLeftValue(attrExp1);
            compFilter.addRightValue(litExp2);

            
            geomFilter = factory.createGeometryFilter(AbstractFilter.GEOMETRY_TOUCHES);
            geomFilter.addLeftGeometry(attrExp2);
            geomFilter.addRightGeometry(litExp2);

            likeFilter = new LikeFilterImpl();
            likeFilter.setValue(attrExp1);
            likeFilter.setPattern(pattern, wcMulti, wcSingle, escape);

            nullFilter = new NullFilterImpl();
            nullFilter.nullCheckValue(attrExp2);
        } catch (IllegalFilterException e) {
            //should not happen.
            fail(e.getMessage());
        }

        //    testFeature = factory.create(attributes);
        LOGGER.finer("...set up complete");

        //_log.getLoggerRepository().setThreshold(Level.DEBUG);
    }

    public void testBasics() throws IllegalFilterException {
        unpacker.unPackAND(btwnFilter);

        //Unsupported should be null, supported btwnFilter
        assertNull(unpacker.getUnSupported());
        assertEquals(btwnFilter, unpacker.getSupported());

        unpacker.unPackAND(compFilter);

        //null, compFilter
        assertNull(unpacker.getUnSupported());
        assertEquals(compFilter, unpacker.getSupported());

        unpacker.unPackAND(geomFilter);

        //Unsupported should be geomFilter, supported null
        assertEquals(geomFilter, unpacker.getUnSupported());
        assertNull(unpacker.getSupported());

        unpacker.unPackAND(likeFilter);

        //likeFilter, null
        assertEquals(likeFilter, unpacker.getUnSupported());
        assertNull(unpacker.getSupported());

        unpacker.unPackAND(nullFilter);

        //null, nullFilter
        assertNull(unpacker.getUnSupported());
        assertEquals(nullFilter, unpacker.getSupported());
    }

    public void testBasicsOR() throws IllegalFilterException {
        unpacker.unPackOR(btwnFilter);

        //Unsupported should be null, supported btwnFilter
        assertNull(unpacker.getUnSupported());
        assertEquals(btwnFilter, unpacker.getSupported());

        unpacker.unPackOR(compFilter);

        //null, compFilter
        assertNull(unpacker.getUnSupported());
        assertEquals(compFilter, unpacker.getSupported());

        unpacker.unPackOR(geomFilter);

        //Unsupported should be geomFilter, supported null
        assertEquals(geomFilter, unpacker.getUnSupported());
        assertNull(unpacker.getSupported());

        unpacker.unPackOR(likeFilter);

        //likeFilter, null
        assertEquals(likeFilter, unpacker.getUnSupported());
        assertNull(unpacker.getSupported());

        unpacker.unPackOR(nullFilter);

        //null, nullFilter
        assertNull(unpacker.getUnSupported());
        assertEquals(nullFilter, unpacker.getSupported());
    }

    public void testAnd() throws IllegalFilterException {
        //I will use the notation (Unsupported, Supported) to indicate
        //the filters that should result, that we are testing against.
        Filter andFilter = btwnFilter.and(compFilter);
        unpacker.unPackAND(andFilter);

        //both supported (null, andFilter)
        assertNull(unpacker.getUnSupported());
        assertEquals(andFilter, unpacker.getSupported());

        andFilter = likeFilter.and(compFilter);
        unpacker.unPackAND(andFilter);

        //Comp supported, Like not: (likeFilter, compFilter)
        assertEquals(likeFilter, unpacker.getUnSupported());
        assertEquals(compFilter, unpacker.getSupported());

        andFilter = likeFilter.and(geomFilter);
        unpacker.unPackAND(andFilter);

        //both unsupported (andFilter, null)
        assertEquals(andFilter, unpacker.getUnSupported());
        assertNull(unpacker.getSupported());
    }

    public void testNot() throws IllegalFilterException {
        Filter notFilter = nullFilter.not();
        unpacker.unPackAND(notFilter);

        //nullFilters supported (null, nullFilter)
        assertNull(unpacker.getUnSupported());
        assertEquals(notFilter, unpacker.getSupported());

        notFilter = geomFilter.not();
        unpacker.unPackAND(notFilter);

        //geomFilters not supported (geomFilter, null);
        assertEquals(notFilter, unpacker.getUnSupported());
        assertNull(unpacker.getSupported());
    }

    public void testOr() throws IllegalFilterException {
        Filter orFilter = btwnFilter.or(compFilter);
        unpacker.unPackAND(orFilter);

        //both supported (null, orFilter)
        assertNull(unpacker.getUnSupported());
        assertEquals(orFilter, unpacker.getSupported());

        orFilter = likeFilter.or(compFilter);
        unpacker.unPackAND(orFilter);

        //both unsupported: (orFilter, null)  
        assertEquals(orFilter, unpacker.getUnSupported());
        assertNull(unpacker.getSupported());

        orFilter = likeFilter.and(geomFilter);
        unpacker.unPackAND(orFilter);

        //both unsupported (orFilter, null)
        assertEquals(orFilter, unpacker.getUnSupported());
        assertNull(unpacker.getSupported());
    }

    //put in more unPackOR's
    public void testComplex() throws IllegalFilterException {
        Filter orFilter = likeFilter.or(btwnFilter);
        Filter andFilter = orFilter.and(compFilter);
        unpacker.unPackAND(andFilter);

        //compFilter supported, none of orFilter: (orFilter, compFilter)
        assertEquals(orFilter, unpacker.getUnSupported());
        assertEquals(compFilter, unpacker.getSupported());

        Filter bigOrFilter = (andFilter.or(compFilter)).or(nullFilter);
        Filter biggerOrFilter = bigOrFilter.or(nullFilter);

        //Top level or with one unsupported (like from orFilter)
        //makes all unsupported: (bigOrFilter, null)
        unpacker.unPackAND(biggerOrFilter);
        assertEquals(biggerOrFilter, unpacker.getUnSupported());
        assertNull(unpacker.getSupported());

        Filter bigAndFilter = (geomFilter.and(compFilter)).and(orFilter);

        //comp supported, orFilter not, geomFilter not:
        //(orFilter AND geomFilter, compFilter)
        unpacker.unPackAND(bigAndFilter);
        assertEquals((orFilter.and(geomFilter)), unpacker.getUnSupported());
        assertEquals(compFilter, unpacker.getSupported());
        unpacker.unPackOR(bigAndFilter);
        assertEquals(bigAndFilter, unpacker.getUnSupported());
        assertNull(unpacker.getSupported());

        Filter hugeAndFilter = (bigAndFilter.and(andFilter)).and(bigOrFilter);
        unpacker.unPackAND(hugeAndFilter);

        // two comps should be supported; geom, or and bigOr unsupported
        // (compFilter AND compFilter, geomFilter AND orFilter AND orFilter AND bigOrFilter)
        assertEquals( (compFilter.and(compFilter)), unpacker.getSupported() );
        
        Filter expected = (((geomFilter.and(orFilter)).and(orFilter)).and(
                bigOrFilter));
        org.opengis.filter.Filter unsupported = unpacker.getUnSupported();

        assertEquals( expected, unsupported );
    }
}
