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
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.FilterFactory2;

/**
 * Unit test for FilterCapabilities.
 *
 * @author Chris Holmes, TOPP
 * @source $URL$
 */
public class FilterCapabilitiesTest extends TestCase {
    /** Standard logging instance */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger("org.geotools.defaultcore");

    /** Feature on which to preform tests */
    private org.opengis.filter.Filter gFilter;

    private org.opengis.filter.Filter compFilter;
    private org.opengis.filter.Filter logFilter;
    private FilterCapabilities capabilities;
    private FilterFactory2 fact = CommonFactoryFinder.getFilterFactory2();

    /** Test suite for this test case */
    TestSuite suite = null;

    /** Constructor with test name. */
    String dataFolder = "";

    boolean setup = false;

    public FilterCapabilitiesTest(String testName) {
        super(testName);
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
        // _log.getLoggerRepository().setThreshold(Level.DEBUG);
        TestSuite suite = new TestSuite(FilterCapabilitiesTest.class);

        return suite;
    }

    /** Sets up a schema and a test feature. */
    protected void setUp() {
        LOGGER.finer("Setting up FilterCapabilitiesTest");

        if (setup) {
            return;
        }

        setup = true;
        capabilities = new FilterCapabilities();

        try {
            gFilter = fact.within(fact.property("geom"), fact.literal(null));
            compFilter = fact.less(fact.property("size"), fact.literal(3));
        } catch (IllegalFilterException ife) {
            LOGGER.fine("Bad filter " + ife);
        }

        capabilities.addType(AbstractFilter.LOGIC_OR);
        capabilities.addType(AbstractFilter.LOGIC_AND);
        capabilities.addType(AbstractFilter.LOGIC_NOT);
        capabilities.addType(FilterType.COMPARE_EQUALS);
        capabilities.addType(FilterType.COMPARE_LESS_THAN);
        capabilities.addType(AbstractFilter.BETWEEN);
    }

    public void testAdd() {
        capabilities.addType(FilterType.COMPARE_GREATER_THAN);
        capabilities.addType(FilterType.COMPARE_LESS_THAN_EQUAL);
        capabilities.addType(AbstractFilter.NULL);
        assertTrue(capabilities.supports(AbstractFilter.NULL));
    }

    public void testShortSupports() {
        assertTrue(capabilities.supports(AbstractFilter.LOGIC_AND));
        assertTrue(!(capabilities.supports(AbstractFilter.LIKE)));
    }

    public void testFilterSupports() {
        assertTrue(capabilities.supports(compFilter));
        assertTrue(!(capabilities.supports(gFilter)));
    }

    public void testFullySupports() {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        try {
            logFilter = ff.and(gFilter, compFilter);
            assertTrue(capabilities.fullySupports(compFilter));
            assertTrue(!(capabilities.fullySupports(gFilter)));
            assertTrue(!(capabilities.fullySupports(logFilter)));
            logFilter =
                    ff.and(
                            compFilter,
                            ff.between(ff.property("sample"), ff.literal(1), ff.literal(2)));
            assertTrue(capabilities.fullySupports(logFilter));
            logFilter =
                    ff.or(
                            logFilter,
                            ff.between(ff.property("sample"), ff.literal(1), ff.literal(2)));
            assertTrue(capabilities.fullySupports(logFilter));
            logFilter = ff.and(logFilter, gFilter);
            assertTrue(!(capabilities.fullySupports(logFilter)));
        } catch (IllegalFilterException e) {
            LOGGER.fine("Bad filter " + e);
        }
    }
}
