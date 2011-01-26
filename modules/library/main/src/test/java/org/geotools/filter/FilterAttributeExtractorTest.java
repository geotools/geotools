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
import java.util.Collections;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.opengis.filter.Id;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Equals;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;


/**
 * Unit test for filters.  Note that this unit test does not encompass all of filter package, just
 * the filters themselves.  There is a seperate unit test for expressions.
 *
 * @author Andrea Aime, SATA
 * @source $URL$
 */
public class FilterAttributeExtractorTest extends TestCase {
    boolean set = false;
    FilterAttributeExtractor fae;
    org.opengis.filter.FilterFactory2 fac;

    /** Test suite for this test case */
    TestSuite suite = null;

    /**
     * Constructor with test name.
     *
     * @param testName DOCUMENT ME!
     */
    public FilterAttributeExtractorTest(String testName) {
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
        TestSuite suite = new TestSuite(FilterAttributeExtractorTest.class);

        return suite;
    }

    /**
     * Sets up a schema and a test feature.
     *
     * @throws SchemaException If there is a problem setting up the schema.
     */
    protected void setUp() throws SchemaException {
        if (set) {
            return;
        }

        set = true;

        fae = new FilterAttributeExtractor();

        fac = CommonFactoryFinder.getFilterFactory2(null);
    }

    /**
     * Sets up a schema and a test feature.
     *
     * @throws IllegalFilterException If the constructed filter is not valid.
     */
    public void testCompare() throws IllegalFilterException {
        PropertyIsEqualTo filter = fac.equals(fac.property("testString"), fac.literal("test string data"));
        assertAttributeName(filter, "testString");
    }

    private void assertAttributeName(org.opengis.filter.Filter filter, String name) {
        assertAttributeName(filter, new String[] { name });
    }

    private void assertAttributeName(org.opengis.filter.Filter filter, String[] names) {
        fae.clear();
        filter.accept(fae, null);

        Set attNames = fae.getAttributeNameSet();

        assertNotNull(attNames);
        assertEquals(attNames.size(), names.length);

        for (int i = 0; i < names.length; i++) {
            assertTrue(attNames.contains(names[i]));
        }
    }

    /**
     * Tests the like operator.
     *
     * @throws IllegalFilterException If the constructed filter is not valid.
     */
    public void testLike() throws IllegalFilterException {
        PropertyIsLike filter = fac.like(fac.property("testString"), "abc");
        assertAttributeName(filter, "testString");
    }

    /**
     * Test the null operator.
     *
     * @throws IllegalFilterException If the constructed filter is not valid.
     */
    public void testNull() throws IllegalFilterException {
        PropertyIsNull filter = fac.isNull(fac.property("foo"));
        assertAttributeName( filter, new String[]{"foo"} );        
    }

    /**
     * Test the between operator.
     *
     * @throws IllegalFilterException If the constructed filter is not valid.
     */
    public void testBetween() throws IllegalFilterException {
        // Set up the integer
        Literal lower = fac.literal(1001);
        Literal upper = fac.literal(1003);
        PropertyName pint = fac.property("testInteger");
        PropertyName plong = fac.property("testLong");
        PropertyName pfloat = fac.property("testFloat");

        assertAttributeName(fac.between(lower, lower, upper), new String[0]);
        assertAttributeName(fac.between(pint, lower, upper), "testInteger");
        assertAttributeName(fac.between(pint, pint, pint), "testInteger");

        assertAttributeName(fac.between(pint, plong, pfloat), 
                new String[] { "testInteger", "testLong", "testFloat" });
    }

    /**
     * Test the geometry operators.
     *
     * @throws IllegalFilterException If the constructed filter is not valid.
     */
    public void testGeometry() throws IllegalFilterException {
        Coordinate[] coords = new Coordinate[3];
        coords[0] = new Coordinate(1, 2);
        coords[1] = new Coordinate(3, 4);
        coords[2] = new Coordinate(5, 6);

        // Test Equals
        PropertyName att = fac.property("testGeometry");
        GeometryFactory gf = new GeometryFactory(new PrecisionModel());
        Literal geom = fac.literal(gf.createLineString(coords));
        
        Equals filter = fac.equal(att, geom);
        assertAttributeName(filter, "testGeometry");

        filter = fac.equal(att, att);
        assertAttributeName(filter, "testGeometry");

        filter = fac.equal(geom, att);
        assertAttributeName(filter, "testGeometry");
    }

    public void testDistanceGeometry() throws Exception {
        Coordinate[] coords2 = new Coordinate[5];
        coords2[0] = new Coordinate(10, 10);
        coords2[1] = new Coordinate(15, 10);
        coords2[2] = new Coordinate(15, 15);
        coords2[3] = new Coordinate(10, 15);
        coords2[4] = new Coordinate(10, 10);

        GeometryFactory gf = new GeometryFactory(new PrecisionModel());
        Literal right = fac.literal(gf.createPolygon(
                    gf.createLinearRing(coords2), null));
        DWithin filter = fac.dwithin(fac.property("testGeometry"), right, 10, "m");

        assertAttributeName(filter, "testGeometry");
    }

    public void testFid() {
        Id filter = fac.id(Collections.singleton(fac.featureId("fakeId")));
        assertAttributeName(filter, new String[0]);
    }

    /**
     * Test the logic operators.
     *
     * @throws IllegalFilterException If the constructed filter is not valid.
     */
    public void testLogic() throws IllegalFilterException {
        
        PropertyName testAttribute = fac.property("testString");

        // Set up true sub filter
        PropertyIsEqualTo filterTrue = fac.equals(testAttribute, fac.literal("test string data"));

        // Set up false sub filter
        PropertyIsEqualTo filterFalse = fac.equals(testAttribute, fac.literal("incorrect test string data"));

        // Test OR for false negatives
        Or filter = fac.or(Arrays.asList((org.opengis.filter.Filter) filterFalse, 
                (org.opengis.filter.Filter) filterTrue));

        assertAttributeName(filter, "testString");
    }
}
