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
import java.util.HashSet;
import java.util.Set;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.Or;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.PropertyIsLike;
import org.geotools.api.filter.PropertyIsNull;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.api.filter.spatial.Equals;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.geotools.filter.function.EnvFunction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;

/**
 * Unit test for filters. Note that this unit test does not encompass all of filter package, just the filters
 * themselves. There is a seperate unit test for expressions.
 *
 * @author Andrea Aime, SATA
 */
public class FilterAttributeExtractorTest {
    boolean set = false;
    FilterAttributeExtractor fae;
    org.geotools.api.filter.FilterFactory fac;

    /**
     * Sets up a schema and a test feature.
     *
     * @throws SchemaException If there is a problem setting up the schema.
     */
    @Before
    public void setUp() throws SchemaException {
        if (set) {
            return;
        }

        set = true;

        fae = new FilterAttributeExtractor();

        fac = CommonFactoryFinder.getFilterFactory(null);
    }

    @Test
    public void testPropertyNameSet() throws IllegalFilterException {
        Filter filter = fac.equals(fac.property("testString"), fac.literal("test string data"));
        Expression expression1 = fac.property("code");
        Expression expression2 = fac.function("length", fac.property("identification"));

        FilterAttributeExtractor extract = new FilterAttributeExtractor(null);

        Set<String> names = new HashSet<>();
        // used to collect names from expression1, expression2, and filter

        expression1.accept(extract, names);
        expression2.accept(extract, names);
        filter.accept(extract, names);

        String[] array = extract.getAttributeNames();
        Set<String> attributes = extract.getAttributeNameSet();
        Set<PropertyName> properties = extract.getPropertyNameSet();

        Assert.assertEquals(3, array.length);
        Assert.assertEquals(3, attributes.size());
        Assert.assertEquals(3, properties.size());
    }
    /**
     * Sets up a schema and a test feature.
     *
     * @throws IllegalFilterException If the constructed filter is not valid.
     */
    @Test
    public void testCompare() throws IllegalFilterException {
        PropertyIsEqualTo filter = fac.equals(fac.property("testString"), fac.literal("test string data"));
        assertAttributeName(filter, "testString");
    }

    private void assertAttributeName(org.geotools.api.filter.Filter filter, String... names) {
        fae.clear();
        filter.accept(fae, null);

        Set<String> attNames = fae.getAttributeNameSet();

        Assert.assertNotNull(attNames);
        Assert.assertEquals(attNames.size(), names.length);

        for (String name : names) {
            Assert.assertTrue(attNames.contains(name));
        }

        // make sure the property name set is aligned
        Set<PropertyName> propNames = fae.getPropertyNameSet();
        Assert.assertNotNull(propNames);
        Assert.assertEquals(attNames.size(), propNames.size());

        for (PropertyName pn : propNames) {
            Assert.assertTrue(attNames.contains(pn.getPropertyName()));
        }
    }

    /**
     * Tests the like operator.
     *
     * @throws IllegalFilterException If the constructed filter is not valid.
     */
    @Test
    public void testLike() throws IllegalFilterException {
        PropertyIsLike filter = fac.like(fac.property("testString"), "abc");
        assertAttributeName(filter, "testString");
    }

    /**
     * Test the null operator.
     *
     * @throws IllegalFilterException If the constructed filter is not valid.
     */
    @Test
    public void testNull() throws IllegalFilterException {
        PropertyIsNull filter = fac.isNull(fac.property("foo"));
        assertAttributeName(filter, "foo");
    }

    /**
     * Test the between operator.
     *
     * @throws IllegalFilterException If the constructed filter is not valid.
     */
    @Test
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

        assertAttributeName(fac.between(pint, plong, pfloat), "testInteger", "testLong", "testFloat");
    }

    /**
     * Test the geometry operators.
     *
     * @throws IllegalFilterException If the constructed filter is not valid.
     */
    @Test
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

    @Test
    public void testDistanceGeometry() throws Exception {
        Coordinate[] coords2 = new Coordinate[5];
        coords2[0] = new Coordinate(10, 10);
        coords2[1] = new Coordinate(15, 10);
        coords2[2] = new Coordinate(15, 15);
        coords2[3] = new Coordinate(10, 15);
        coords2[4] = new Coordinate(10, 10);

        GeometryFactory gf = new GeometryFactory(new PrecisionModel());
        Literal right = fac.literal(gf.createPolygon(gf.createLinearRing(coords2), null));
        DWithin filter = fac.dwithin(fac.property("testGeometry"), right, 10, "m");

        assertAttributeName(filter, "testGeometry");
    }

    @Test
    public void testFid() {
        Id filter = fac.id(Collections.singleton(fac.featureId("fakeId")));
        assertAttributeName(filter, new String[0]);
    }

    /**
     * Test the logic operators.
     *
     * @throws IllegalFilterException If the constructed filter is not valid.
     */
    @Test
    public void testLogic() throws IllegalFilterException {

        PropertyName testAttribute = fac.property("testString");

        // Set up true sub filter
        PropertyIsEqualTo filterTrue = fac.equals(testAttribute, fac.literal("test string data"));

        // Set up false sub filter
        PropertyIsEqualTo filterFalse = fac.equals(testAttribute, fac.literal("incorrect test string data"));

        // Test OR for false negatives
        Or filter = fac.or(Arrays.asList(filterFalse, filterTrue));

        assertAttributeName(filter, "testString");
    }

    @Test
    public void testDynamicProperty() throws Exception {
        Function func = fac.function("property", fac.function("env", fac.literal("pname")));
        PropertyIsEqualTo filter = fac.equals(func, fac.literal("test"));
        try {
            EnvFunction.setLocalValue("pname", "name");
            assertAttributeName(filter, "name");
        } finally {
            EnvFunction.clearLocalValues();
        }
    }
}
