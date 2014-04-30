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
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.geotools.filter.expression.AddImpl;
import org.opengis.filter.And;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Not;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.Touches;


/**
 * Unit test for SQLUnpacker.
 *
 * @author Chris Holmes, TOPP
 *
 *
 * @source $URL$
 */
public class SQLUnpackerTest extends SQLFilterTestSupport {
    /** Standard logging instance */

    //private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.defaultcore");

    /** Filters on which to perform tests */
    private PropertyIsBetween btwnFilter;
    private PropertyIsLessThan compFilter;
    private Touches geomFilter;
    private PropertyIsLike likeFilter;
    private PropertyIsNull nullFilter;
    private PropertyName attrExp1;
    private PropertyName attrExp2;
    private Literal litExp1;
    private Literal litExp2;
    private Add mathExp1;

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
            FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
            
            attrExp1 = ff.property("testInteger");
            attrExp2 = ff.property( "testGeometry");
            litExp1 = ff.literal(new Integer(65));
            litExp2 =  ff.literal(new Integer(35));
            
            mathExp1 = ff.add(litExp1,litExp2);

            btwnFilter = ff.between(attrExp1, litExp1, mathExp1);

            compFilter = ff.less(attrExp1,litExp2);

            
            geomFilter = ff.touches(attrExp2,litExp2);

            likeFilter = ff.like(attrExp1,pattern, wcMulti, wcSingle, escape);

            nullFilter = ff.isNull(attrExp2);
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
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        
        
        And andFilter = ff.and( btwnFilter, compFilter);
        unpacker.unPackAND(andFilter);

        //both supported (null, andFilter)
        assertNull(unpacker.getUnSupported());
        assertEquals(andFilter, unpacker.getSupported());

        andFilter = ff.and( likeFilter,compFilter);
        unpacker.unPackAND(andFilter);

        //Comp supported, Like not: (likeFilter, compFilter)
        assertEquals(likeFilter, unpacker.getUnSupported());
        assertEquals(compFilter, unpacker.getSupported());

        andFilter = ff.and( likeFilter,geomFilter);
        unpacker.unPackAND(andFilter);

        //both unsupported (andFilter, null)
        assertEquals(andFilter, unpacker.getUnSupported());
        assertNull(unpacker.getSupported());
    }

    public void testNot() throws IllegalFilterException {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        
        Not notFilter = ff.not( nullFilter );
        unpacker.unPackAND(notFilter);

        //nullFilters supported (null, nullFilter)
        assertNull(unpacker.getUnSupported());
        assertEquals(notFilter, unpacker.getSupported());

        notFilter = ff.not( geomFilter );
        unpacker.unPackAND(notFilter);

        //geomFilters not supported (geomFilter, null);
        assertEquals(notFilter, unpacker.getUnSupported());
        assertNull(unpacker.getSupported());
    }

    public void testOr() throws IllegalFilterException {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        
        org.opengis.filter.Filter orFilter = ff.or( btwnFilter,compFilter);
        unpacker.unPackAND(orFilter);

        //both supported (null, orFilter)
        assertNull(unpacker.getUnSupported());
        assertEquals(orFilter, unpacker.getSupported());

        orFilter = ff.or(likeFilter,compFilter);
        unpacker.unPackAND(orFilter);

        //both unsupported: (orFilter, null)  
        assertEquals(orFilter, unpacker.getUnSupported());
        assertNull(unpacker.getSupported());

        orFilter = ff.and(likeFilter,geomFilter);
        unpacker.unPackAND(orFilter);

        //both unsupported (orFilter, null)
        assertEquals(orFilter, unpacker.getUnSupported());
        assertNull(unpacker.getSupported());
    }

    //put in more unPackOR's
    public void testComplex() throws IllegalFilterException {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        
        Or orFilter = ff.or( likeFilter, btwnFilter);
        And andFilter = ff.and( orFilter, compFilter);
        unpacker.unPackAND(andFilter);

        //compFilter supported, none of orFilter: (orFilter, compFilter)
        assertEquals(orFilter, unpacker.getUnSupported());
        assertEquals(compFilter, unpacker.getSupported());

        Or bigOrFilter = ff.or(ff.or(andFilter,compFilter),nullFilter);
        Or biggerOrFilter = ff.or( bigOrFilter,nullFilter);

        //Top level or with one unsupported (like from orFilter)
        //makes all unsupported: (bigOrFilter, null)
        unpacker.unPackAND(biggerOrFilter);
        assertEquals(biggerOrFilter, unpacker.getUnSupported());
        assertNull(unpacker.getSupported());

        And bigAndFilter = ff.and(ff.and(geomFilter,compFilter),orFilter);

        //comp supported, orFilter not, geomFilter not:
        //(orFilter AND geomFilter, compFilter)
        unpacker.unPackAND(bigAndFilter);
        assertEquals(ff.and(orFilter,geomFilter), unpacker.getUnSupported());
        assertEquals(compFilter, unpacker.getSupported());
        unpacker.unPackOR(bigAndFilter);
        assertEquals(bigAndFilter, unpacker.getUnSupported());
        assertNull(unpacker.getSupported());

        And hugeAndFilter = ff.and(ff.and(bigAndFilter,andFilter),bigOrFilter);
        unpacker.unPackAND(hugeAndFilter);

        // two comps should be supported; geom, or and bigOr unsupported
        // (compFilter AND compFilter, geomFilter AND orFilter AND orFilter AND bigOrFilter)
        assertEquals( ff.and(compFilter,compFilter), unpacker.getSupported() );
        
        List<org.opengis.filter.Filter>combine = Arrays.asList( new org.opengis.filter.Filter[]{
                geomFilter, orFilter, orFilter,bigOrFilter});
        
        And expected = ff.and(combine);
        org.opengis.filter.Filter unsupported = unpacker.getUnSupported();
        
        assertEquals( expected.toString(), unsupported.toString());
        assertEquals( expected, unsupported );
    }
}
