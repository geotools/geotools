/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.And;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.Or;
import org.geotools.api.filter.PropertyIsBetween;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.PropertyIsGreaterThan;
import org.geotools.api.filter.PropertyIsGreaterThanOrEqualTo;
import org.geotools.api.filter.PropertyIsLessThan;
import org.geotools.api.filter.PropertyIsLessThanOrEqualTo;
import org.geotools.api.filter.PropertyIsLike;
import org.geotools.api.filter.PropertyIsNull;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.ExpressionVisitor;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.Beyond;
import org.geotools.api.filter.spatial.Contains;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.api.filter.spatial.Disjoint;
import org.geotools.api.filter.spatial.Equals;
import org.geotools.api.filter.spatial.Intersects;
import org.geotools.api.filter.spatial.Within;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.filter.expression.PropertyAccessor;
import org.geotools.filter.expression.PropertyAccessorFactory;
import org.geotools.filter.function.InFunction;
import org.geotools.geometry.jts.JTS;
import org.geotools.util.factory.Hints;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;

/**
 * Unit test for filters. Note that this unit test does not encompass all of filter package, just the filters
 * themselves. There is a seperate unit test for expressions.
 *
 * @author James MacGill, CCG
 * @author Rob Hranac, TOPP
 */
public class FilterTest {
    /** The logger for the filter module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(FilterTest.class);

    /** SimpleFeature on which to preform tests */
    private static SimpleFeature testFeature = null;

    /** Schema on which to preform tests */
    private static SimpleFeatureType testSchema = null;

    boolean set = false;

    org.geotools.api.filter.FilterFactory fac = CommonFactoryFinder.getFilterFactory(null);

    private Calendar calDateTime;
    private Calendar calTime;
    private Calendar calDate;

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

        fac = CommonFactoryFinder.getFilterFactory(null);

        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setName("testFeatureType");
        ftb.setCRS(null);
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
        ftb.add("testString2", String.class);
        ftb.add("date", java.sql.Date.class);
        ftb.add("time", java.sql.Time.class);
        ftb.add("datetime1", java.util.Date.class);
        ftb.add("datetime2", java.sql.Timestamp.class);
        ftb.add("nullInt", Integer.class);
        ftb.add("unicodeString", String.class);
        testSchema = ftb.buildFeatureType();

        // LOGGER.finer("added string to feature type");
        // Creates coordinates for the linestring
        Coordinate[] coords = new Coordinate[3];
        coords[0] = new Coordinate(1, 2);
        coords[1] = new Coordinate(3, 4);
        coords[2] = new Coordinate(5, 6);

        // Builds the test feature
        Object[] attributes = new Object[17];
        GeometryFactory gf = new GeometryFactory(new PrecisionModel());
        attributes[0] = gf.createLineString(coords);
        attributes[1] = Boolean.TRUE;
        attributes[2] = Character.valueOf('t');
        attributes[3] = Byte.valueOf("10");
        attributes[4] = Short.valueOf("101");
        attributes[5] = Integer.valueOf(1002);
        attributes[6] = Long.valueOf(10003);
        attributes[7] = Float.valueOf(10000.4f);
        attributes[8] = Double.valueOf(100000.5);
        attributes[9] = "test string data";
        attributes[10] = "cow $10";

        // setup date ones
        calDate = Calendar.getInstance();
        calDate.clear();
        calDate.set(2007, 7, 15);
        calTime = Calendar.getInstance();
        calTime.clear();
        calTime.set(Calendar.HOUR_OF_DAY, 12);
        calDateTime = Calendar.getInstance();
        calDateTime.clear();
        calDateTime.set(2007, 7, 15, 12, 00, 00);
        attributes[11] = new java.sql.Date(calDate.getTimeInMillis());
        attributes[12] = new java.sql.Time(calTime.getTimeInMillis());
        attributes[13] = calDateTime.getTime();
        attributes[14] = new java.sql.Timestamp(calDateTime.getTimeInMillis());

        // Unicode string
        attributes[16] = "Barañáin";

        // Creates the feature itself
        // FlatFeatureFactory factory = new FlatFeatureFactory(testSchema);
        testFeature = SimpleFeatureBuilder.build(testSchema, attributes, null);
        // LOGGER.finer("...flat feature created");
    }

    @Test
    public void testLikeToSQL() {
        Assert.assertEquals("BroadWay%", LikeFilterImpl.convertToSQL92('!', '*', '.', true, "BroadWay*", true));
        Assert.assertEquals("broad#ay", LikeFilterImpl.convertToSQL92('!', '*', '.', true, "broad#ay", true));
        Assert.assertEquals("broadway", LikeFilterImpl.convertToSQL92('!', '*', '.', true, "broadway", true));

        Assert.assertEquals("broad_ay", LikeFilterImpl.convertToSQL92('!', '*', '.', true, "broad.ay", true));
        Assert.assertEquals("broad.ay", LikeFilterImpl.convertToSQL92('!', '*', '.', true, "broad!.ay", true));

        Assert.assertEquals("broa''dway", LikeFilterImpl.convertToSQL92('!', '*', '.', true, "broa'dway", true));
        Assert.assertEquals(
                "broa''''dway", LikeFilterImpl.convertToSQL92('!', '*', '.', true, "broa" + "''dway", true));
        Assert.assertEquals("broa'dway", LikeFilterImpl.convertToSQL92('!', '*', '.', true, "broa'dway", false));
        Assert.assertEquals("broa''dway", LikeFilterImpl.convertToSQL92('!', '*', '.', true, "broa" + "''dway", false));

        Assert.assertEquals("broadway_", LikeFilterImpl.convertToSQL92('!', '*', '.', true, "broadway.", true));
        Assert.assertEquals("broadway", LikeFilterImpl.convertToSQL92('!', '*', '.', true, "broadway!", true));
        Assert.assertEquals("broadway!", LikeFilterImpl.convertToSQL92('!', '*', '.', true, "broadway!!", true));
    }

    /**
     * Sets up a schema and a test feature.
     *
     * @throws IllegalFilterException If the constructed filter is not valid.
     */
    @Test
    public void testCompare() throws IllegalFilterException {
        // Test all integer permutations
        PropertyName testAttribute = new AttributeExpressionImpl(testSchema, "testInteger");
        compareNumberRunner(testAttribute, PropertyIsEqualTo.class, false, true, false);
        compareNumberRunner(testAttribute, PropertyIsGreaterThan.class, true, false, false);
        compareNumberRunner(testAttribute, PropertyIsLessThan.class, false, false, true);
        compareNumberRunner(testAttribute, PropertyIsGreaterThanOrEqualTo.class, true, true, false);
        compareNumberRunner(testAttribute, PropertyIsLessThanOrEqualTo.class, false, true, true);

        // Test all permutations of integers as strings
        compareStringToIntegerRunner(testAttribute, PropertyIsEqualTo.class, false, true, false);
        compareStringToIntegerRunner(testAttribute, PropertyIsGreaterThan.class, true, false, false);
        compareStringToIntegerRunner(testAttribute, PropertyIsLessThan.class, false, false, true);
        compareStringToIntegerRunner(testAttribute, PropertyIsGreaterThanOrEqualTo.class, true, true, false);
        compareStringToIntegerRunner(testAttribute, PropertyIsLessThanOrEqualTo.class, false, true, true);

        // Test all permutations of integers as doubles
        compareIntegerToDoubleRunner(testAttribute, PropertyIsEqualTo.class, false, true, false);
        compareIntegerToDoubleRunner(testAttribute, PropertyIsGreaterThan.class, true, false, false);
        compareIntegerToDoubleRunner(testAttribute, PropertyIsLessThan.class, false, false, true);
        compareIntegerToDoubleRunner(testAttribute, PropertyIsGreaterThanOrEqualTo.class, true, true, false);
        compareIntegerToDoubleRunner(testAttribute, PropertyIsLessThanOrEqualTo.class, false, true, true);

        // test all date permutations, with string/date conversion included
        testAttribute = new AttributeExpressionImpl(testSchema, "date");
        compareSqlDateRunner(testAttribute, PropertyIsEqualTo.class, false, true, false);
        compareSqlDateRunner(testAttribute, PropertyIsGreaterThan.class, true, false, false);
        compareSqlDateRunner(testAttribute, PropertyIsLessThan.class, false, false, true);
        compareSqlDateRunner(testAttribute, PropertyIsGreaterThanOrEqualTo.class, true, true, false);
        compareSqlDateRunner(testAttribute, PropertyIsLessThanOrEqualTo.class, false, true, true);

        // test all date permutations, with string/date conversion included
        testAttribute = new AttributeExpressionImpl(testSchema, "time");
        compareSqlTimeRunner(testAttribute, PropertyIsEqualTo.class, false, true, false);
        compareSqlTimeRunner(testAttribute, PropertyIsGreaterThan.class, true, false, false);
        compareSqlTimeRunner(testAttribute, PropertyIsLessThan.class, false, false, true);
        compareSqlTimeRunner(testAttribute, PropertyIsGreaterThanOrEqualTo.class, true, true, false);
        compareSqlTimeRunner(testAttribute, PropertyIsLessThanOrEqualTo.class, false, true, true);

        // Set up the string test.
        testAttribute = new AttributeExpressionImpl(testSchema, "testString");

        // Test for false positive.
        Literal testLiteral = new LiteralExpressionImpl("test string data");
        org.geotools.api.filter.Filter filter = compare(PropertyIsEqualTo.class, testAttribute, testLiteral);

        // LOGGER.finer( filter.toString());
        // LOGGER.finer( "contains feature: " + filter.contains(testFeature));
        Assert.assertTrue(filter.evaluate(testFeature));

        // Test for false negative.
        testLiteral = new LiteralExpressionImpl("incorrect test string data");
        filter = compare(PropertyIsEqualTo.class, testAttribute, testLiteral);

        Assert.assertFalse(filter.evaluate(testFeature));

        // Test for false positive.
        testLiteral = new LiteralExpressionImpl("zebra");
        filter = compare(PropertyIsLessThan.class, testAttribute, testLiteral);
        Assert.assertTrue(filter.evaluate(testFeature));

        testLiteral = new LiteralExpressionImpl("blorg");
        filter = compare(PropertyIsLessThan.class, testAttribute, testLiteral);
        Assert.assertFalse(filter.evaluate(testFeature));
    }

    /**
     * Helper class for the integer compare operators.
     *
     * @throws IllegalFilterException If the constructed filter is not valid.
     */
    public void compareNumberRunner(
            PropertyName testAttribute, Class filterType, boolean test1, boolean test2, boolean test3)
            throws IllegalFilterException {
        Literal testLiteral = new LiteralExpressionImpl(Integer.valueOf(1001));
        org.geotools.api.filter.Filter filter = compare(filterType, testAttribute, testLiteral);

        // LOGGER.finer( filter.toString());
        // LOGGER.finer( "contains feature: " + filter.contains(testFeature));
        Assert.assertEquals(filter.evaluate(testFeature), test1);

        testLiteral = new LiteralExpressionImpl(Integer.valueOf(1002));
        filter = compare(filterType, testAttribute, testLiteral);

        // LOGGER.finer( filter.toString());
        // LOGGER.finer( "contains feature: " + filter.contains(testFeature));
        Assert.assertEquals(filter.evaluate(testFeature), test2);

        testLiteral = new LiteralExpressionImpl(Integer.valueOf(1003));
        filter = compare(filterType, testAttribute, testLiteral);

        // LOGGER.finer( filter.toString());
        // LOGGER.finer( "contains feature: " + filter.contains(testFeature));
        Assert.assertEquals(filter.evaluate(testFeature), test3);
    }

    public void compareStringToIntegerRunner(
            PropertyName testAttribute, Class filterType, boolean test1, boolean test2, boolean test3)
            throws IllegalFilterException {
        Literal testLiteral = new LiteralExpressionImpl("1001.0");
        org.geotools.api.filter.Filter filter = compare(filterType, testAttribute, testLiteral);

        // LOGGER.finer( filter.toString());
        // LOGGER.finer( "contains feature: " + filter.contains(testFeature));
        Assert.assertEquals(filter.evaluate(testFeature), test1);

        testLiteral = new LiteralExpressionImpl("1002.0");
        filter = compare(filterType, testAttribute, testLiteral);

        // LOGGER.finer( filter.toString());
        // LOGGER.finer( "contains feature: " + filter.contains(testFeature));
        Assert.assertEquals(filter.evaluate(testFeature), test2);

        testLiteral = new LiteralExpressionImpl("1003.0");
        filter = compare(filterType, testAttribute, testLiteral);

        // LOGGER.finer( filter.toString());
        // LOGGER.finer( "contains feature: " + filter.contains(testFeature));
        Assert.assertEquals(filter.evaluate(testFeature), test3);
    }

    public void compareIntegerToDoubleRunner(
            PropertyName testAttribute, Class filterType, boolean test1, boolean test2, boolean test3)
            throws IllegalFilterException {
        Literal testLiteral = new LiteralExpressionImpl(Double.valueOf(1001.0));
        org.geotools.api.filter.Filter filter = compare(filterType, testAttribute, testLiteral);

        // LOGGER.finer( filter.toString());
        // LOGGER.finer( "contains feature: " + filter.contains(testFeature));
        Assert.assertEquals(filter.evaluate(testFeature), test1);

        testLiteral = new LiteralExpressionImpl(Double.valueOf(1002.0));
        filter = compare(filterType, testAttribute, testLiteral);

        // LOGGER.finer( filter.toString());
        // LOGGER.finer( "contains feature: " + filter.contains(testFeature));
        Assert.assertEquals(filter.evaluate(testFeature), test2);

        testLiteral = new LiteralExpressionImpl(Double.valueOf(1003.0));
        filter = compare(filterType, testAttribute, testLiteral);

        // LOGGER.finer( filter.toString());
        // LOGGER.finer( "contains feature: " + filter.contains(testFeature));
        Assert.assertEquals(filter.evaluate(testFeature), test3);
    }

    /**
     * Helper class for the integer compare operators.
     *
     * @throws IllegalFilterException If the constructed filter is not valid.
     */
    public void compareSqlDateRunner(
            PropertyName testAttribute, Class filterType, boolean test1, boolean test2, boolean test3)
            throws IllegalFilterException {
        Calendar calLocal = Calendar.getInstance();
        calLocal.setTime(calDate.getTime());
        calLocal.set(Calendar.DAY_OF_MONTH, calDateTime.get(Calendar.DAY_OF_MONTH) - 1);
        Literal testLiteral = new LiteralExpressionImpl(new java.sql.Date(calLocal.getTimeInMillis()).toString());
        org.geotools.api.filter.Filter filter = compare(filterType, testAttribute, testLiteral);

        // LOGGER.finer( filter.toString());
        // LOGGER.finer( "contains feature: " + filter.contains(testFeature));
        Assert.assertEquals(test1, filter.evaluate(testFeature));

        testLiteral = new LiteralExpressionImpl(new java.sql.Date(calDate.getTimeInMillis()).toString());
        filter = compare(filterType, testAttribute, testLiteral);

        // LOGGER.finer( filter.toString());
        // LOGGER.finer( "contains feature: " + filter.contains(testFeature));
        Assert.assertEquals(test2, filter.evaluate(testFeature));

        calLocal.set(Calendar.DAY_OF_MONTH, calDateTime.get(Calendar.DAY_OF_MONTH) + 1);
        testLiteral = new LiteralExpressionImpl(new java.sql.Date(calLocal.getTimeInMillis()).toString());
        filter = compare(filterType, testAttribute, testLiteral);

        // LOGGER.finer( filter.toString());
        // LOGGER.finer( "contains feature: " + filter.contains(testFeature));
        Assert.assertEquals(filter.evaluate(testFeature), test3);
    }

    /** Builds a filter that compares a and b: <code>a compare b</code> */
    org.geotools.api.filter.Filter compare(
            Class filterType,
            org.geotools.api.filter.expression.Expression a,
            org.geotools.api.filter.expression.Expression b) {
        if (filterType == PropertyIsLessThan.class) {
            return fac.less(a, b);
        } else if (filterType == PropertyIsLessThanOrEqualTo.class) {
            return fac.lessOrEqual(a, b);
        }
        if (filterType == PropertyIsEqualTo.class) {
            return fac.equals(a, b);
        } else if (filterType == PropertyIsGreaterThanOrEqualTo.class) {
            return fac.greaterOrEqual(a, b);
        } else if (filterType == PropertyIsGreaterThan.class) {
            return fac.greater(a, b);
        } else {
            throw new IllegalArgumentException("Uknown compare filter type " + filterType);
        }
    }

    /**
     * Helper class for the integer compare operators.
     *
     * @throws IllegalFilterException If the constructed filter is not valid.
     */
    public void compareSqlTimeRunner(
            PropertyName testAttribute, Class filterType, boolean test1, boolean test2, boolean test3)
            throws IllegalFilterException {
        Calendar calLocal = Calendar.getInstance();
        calLocal.setTime(calTime.getTime());
        calLocal.set(Calendar.HOUR_OF_DAY, calTime.get(Calendar.HOUR_OF_DAY) - 1);
        Literal testLiteral = new LiteralExpressionImpl(new java.sql.Time(calLocal.getTimeInMillis()).toString());
        org.geotools.api.filter.Filter filter = compare(filterType, testAttribute, testLiteral);

        // LOGGER.finer( filter.toString());
        // LOGGER.finer( "contains feature: " + filter.contains(testFeature));
        Assert.assertEquals(filter.evaluate(testFeature), test1);

        testLiteral = new LiteralExpressionImpl(new java.sql.Time(calTime.getTimeInMillis()).toString());
        filter = compare(filterType, testAttribute, testLiteral);

        // LOGGER.finer( filter.toString());
        // LOGGER.finer( "contains feature: " + filter.contains(testFeature));
        Assert.assertEquals(filter.evaluate(testFeature), test2);

        calLocal.set(Calendar.HOUR_OF_DAY, calTime.get(Calendar.HOUR_OF_DAY) + 1);
        testLiteral = new LiteralExpressionImpl(new java.sql.Time(calLocal.getTimeInMillis()).toString());
        filter = compare(filterType, testAttribute, testLiteral);

        // LOGGER.finer( filter.toString());
        // LOGGER.finer( "contains feature: " + filter.contains(testFeature));
        Assert.assertEquals(filter.evaluate(testFeature), test3);
    }

    /**
     * Tests the like operator.
     *
     * @throws IllegalFilterException If the constructed filter is not valid.
     */
    @Test
    public void testLike() throws IllegalFilterException {

        Pattern compPattern = java.util.regex.Pattern.compile("test.*");
        Matcher matcher = compPattern.matcher("test string");

        Assert.assertTrue(matcher.matches());

        // Set up string
        PropertyName testAttribute = new AttributeExpressionImpl(testSchema, "testString");

        PropertyIsLike filter = fac.like(testAttribute, "test*", "*", ".", "!");
        Assert.assertTrue(filter.evaluate(testFeature));

        // Test for false positive.
        filter = fac.like(testAttribute, "cows*", "*", ".", "!");
        Assert.assertFalse(filter.evaluate(testFeature));

        // Test we don't match if single character is missing
        filter = fac.like(testAttribute, "test*a.", "*", ".", "!");
        Assert.assertFalse(filter.evaluate(testFeature));

        // Test we do match if the single char is there
        filter = fac.like(testAttribute, "test*dat.", "*", ".", "!");
        Assert.assertTrue(filter.evaluate(testFeature));
    }

    /**
     * Tests the like operator with unicode chars.
     *
     * @throws IllegalFilterException If the constructed filter is not valid.
     */
    @Test
    public void testUnicodeLike() throws IllegalFilterException {

        // Set up string
        PropertyName testAttribute = new AttributeExpressionImpl(testSchema, "unicodeString");

        PropertyIsLike filter = fac.like(testAttribute, "Barañá*", "*", ".", "!", false);
        Assert.assertTrue(filter.evaluate(testFeature));

        filter = fac.like(testAttribute, "Barañá*", "*", ".", "!", true);
        Assert.assertTrue(filter.evaluate(testFeature));

        filter = fac.like(testAttribute, "barañá*", "*", ".", "!", false);
        Assert.assertTrue(filter.evaluate(testFeature));

        filter = fac.like(testAttribute, "barañá*", "*", ".", "!", true);
        Assert.assertFalse(filter.evaluate(testFeature));

        filter = fac.like(testAttribute, "BARAÑÁ*", "*", ".", "!", false);
        Assert.assertTrue(filter.evaluate(testFeature));

        filter = fac.like(testAttribute, "BARAÑÁ*", "*", ".", "!", true);
        Assert.assertFalse(filter.evaluate(testFeature));
    }

    /**
     * Test the null operator.
     *
     * @throws IllegalFilterException If the constructed filter is not valid.
     */
    @Test
    public void testNull() throws IllegalFilterException {
        // Test for false positive.
        PropertyName testAttribute = new AttributeExpressionImpl(testSchema, "testString");

        PropertyIsNull filter = fac.isNull(org.geotools.api.filter.expression.Expression.NIL);
        Assert.assertTrue(filter.evaluate(testFeature));

        filter = fac.isNull(testAttribute);
        Assert.assertFalse(filter.evaluate(testFeature));
    }

    /**
     * A filter is composed of a logic AND bettween a non null check and a comparison filter, for an
     * AttributeExpression. If the AttributeExpression evaluates to null, the short-circuit comparison in the
     * LogicFilter should return without throwing a NullPointerException. If short-circuit evaluation would not be done
     * in LogicFilter, then a NullPointerException would be thrown.
     *
     * @throws IllegalFilterException If the constructed filter is not valid.
     */
    @Test
    public void testCompareShortCircuit() throws IllegalFilterException {
        // Test all integer permutations
        PropertyName testAttribute = new AttributeExpressionImpl(testSchema, "testInteger");

        PropertyIsNull nullFilter = fac.isNull(testAttribute);

        org.geotools.api.filter.Filter notNullFilter = fac.not(nullFilter);

        PropertyIsEqualTo compareFilter = fac.equals(testAttribute, fac.literal(10));

        testFeature.setAttribute("testInteger", null);
        Assert.assertFalse(compareFilter.evaluate(testFeature));

        Assert.assertTrue(nullFilter.evaluate(testFeature));
        Assert.assertFalse(notNullFilter.evaluate(testFeature));

        // test AND
        org.geotools.api.filter.Filter finalFilter = fac.and(notNullFilter, compareFilter);
        try {
            Assert.assertFalse(finalFilter.evaluate(testFeature));
        } catch (NullPointerException e) {
            Assert.fail("Short-circuit evaluation was not performed by LogicFilter: " + e.getMessage());
        }

        // test OR
        finalFilter = fac.or(nullFilter, compareFilter);
        try {
            Assert.assertTrue(finalFilter.evaluate(testFeature));
        } catch (NullPointerException e) {
            Assert.fail("Short-circuit evaluation was not performed by LogicFilter: " + e.getMessage());
        }
    }

    /**
     * Test the between operator.
     *
     * @throws IllegalFilterException If the constructed filter is not valid.
     */
    @Test
    public void testBetween() throws IllegalFilterException {
        // Set up the integer
        Literal testLiteralLower = fac.literal(1001);
        PropertyName testAttribute = fac.property("testInteger");
        Literal testLiteralUpper = fac.literal(1003);

        // String tests
        PropertyIsBetween filter = fac.between(testAttribute, testLiteralLower, testLiteralUpper);
        Assert.assertTrue(filter.evaluate(testFeature));

        // Test for false positive.
        testLiteralLower = fac.literal(1);
        testLiteralUpper = fac.literal(1000);
        filter = fac.between(testAttribute, testLiteralLower, testLiteralUpper);

        // LOGGER.finer( filter.toString());
        // LOGGER.finer( "contains feature: " + filter.evaluate(testFeature));
        Assert.assertFalse(filter.evaluate(testFeature));
    }

    @Test
    public void testBetweenStrings() throws IllegalFilterException {
        // Set up the integer
        Literal testLiteralLower = new LiteralExpressionImpl("blorg");
        PropertyName testAttribute = new AttributeExpressionImpl(testSchema, "testString");
        Literal testLiteralUpper = new LiteralExpressionImpl("tron");

        // String tests
        PropertyIsBetween filter = fac.between(testAttribute, testLiteralLower, testLiteralUpper);
        Assert.assertTrue(filter.evaluate(testFeature));

        // Test for false positive.
        testLiteralLower = new LiteralExpressionImpl("zebra");
        testLiteralUpper = new LiteralExpressionImpl("zikes");
        filter = fac.between(testAttribute, testLiteralLower, testLiteralUpper);
        Assert.assertFalse(filter.evaluate(testFeature));
    }

    @Test
    public void testGeometryEquals() throws Exception {
        Coordinate[] coords = new Coordinate[3];
        coords[0] = new Coordinate(1, 2);
        coords[1] = new Coordinate(3, 4);
        coords[2] = new Coordinate(5, 6);

        // Test Equals
        PropertyName left = new AttributeExpressionImpl(testSchema, "testGeometry");
        GeometryFactory gf = new GeometryFactory(new PrecisionModel());
        LineString geom = gf.createLineString(coords);
        Literal right = new LiteralExpressionImpl(geom);
        Equals filter = fac.equal(left, right);

        LOGGER.finer(filter.toString());
        LOGGER.finer("contains feature: " + filter.evaluate(testFeature));
        Assert.assertTrue(filter.evaluate(testFeature));

        Function function = new GeometryFunction(geom);
        filter = fac.equal(left, function);
        LOGGER.finer(filter.toString());
        LOGGER.finer("contains feature: " + filter.evaluate(testFeature));
        Assert.assertTrue(filter.evaluate(testFeature));

        coords[0] = new Coordinate(0, 0);
        right = new LiteralExpressionImpl(geom);
        filter = fac.equal(left, right);

        LOGGER.finer(filter.toString());
        LOGGER.finer("contains feature: " + filter.evaluate(testFeature));
        Assert.assertFalse(filter.evaluate(testFeature));

        filter = fac.equal(left, new LiteralExpressionImpl(null));

        LOGGER.finer(filter.toString());
        LOGGER.finer("contains feature: " + filter.evaluate(testFeature));
        Assert.assertFalse(filter.evaluate(testFeature));
    }

    @Test
    public void testContains() throws Exception {
        Coordinate[] coords = {
            new Coordinate(0, 0), new Coordinate(6, 0), new Coordinate(6, 7), new Coordinate(0, 7), new Coordinate(0, 0)
        };

        // Test Equals
        GeometryFactory gf = new GeometryFactory(new PrecisionModel());
        Polygon geom = gf.createPolygon(gf.createLinearRing(coords), new LinearRing[0]);
        Literal expr1 = new LiteralExpressionImpl(geom);
        PropertyName expr2 = new AttributeExpressionImpl(testSchema, "testGeometry");

        Contains filter = fac.contains(expr1, expr2);

        LOGGER.finer(filter.toString());
        LOGGER.finer("contains feature: " + filter.evaluate(testFeature));
        Assert.assertTrue(filter.evaluate(testFeature));

        Function function = new GeometryFunction(geom);
        filter = fac.contains(expr1, function);
        LOGGER.finer(filter.toString());
        LOGGER.finer("contains feature: " + filter.evaluate(testFeature));
        Assert.assertTrue(filter.evaluate(testFeature));

        filter = fac.contains(expr2, expr1);

        LOGGER.finer(filter.toString());
        LOGGER.finer("contains feature: " + filter.evaluate(testFeature));
        Assert.assertFalse(filter.evaluate(testFeature));

        coords = new Coordinate[] {
            new Coordinate(2, 2), new Coordinate(6, 0), new Coordinate(6, 7), new Coordinate(0, 7), new Coordinate(2, 2)
        };
        geom = gf.createPolygon(gf.createLinearRing(coords), new LinearRing[0]);
        expr1 = new LiteralExpressionImpl(geom);
        filter = fac.contains(expr1, expr2);

        LOGGER.finer(filter.toString());
        LOGGER.finer("contains feature: " + filter.evaluate(testFeature));
        Assert.assertFalse(filter.evaluate(testFeature));

        filter = fac.contains(new LiteralExpressionImpl(null), expr2);

        LOGGER.finer(filter.toString());
        LOGGER.finer("contains feature: " + filter.evaluate(testFeature));
        Assert.assertFalse(filter.evaluate(testFeature));
    }

    @Test
    public void testWithin() throws Exception {
        Coordinate[] coords = {
            new Coordinate(0, 0), new Coordinate(6, 0), new Coordinate(6, 7), new Coordinate(0, 7), new Coordinate(0, 0)
        };

        // Test Equals
        GeometryFactory gf = new GeometryFactory(new PrecisionModel());
        Polygon geom = gf.createPolygon(gf.createLinearRing(coords), new LinearRing[0]);
        Literal expr2 = new LiteralExpressionImpl(geom);
        PropertyName expr1 = new AttributeExpressionImpl(testSchema, "testGeometry");

        Within filter = fac.within(expr1, expr2);

        LOGGER.finer(filter.toString());
        LOGGER.finer("contains feature: " + filter.evaluate(testFeature));
        Assert.assertTrue(filter.evaluate(testFeature));

        Function function = new GeometryFunction(geom);
        filter = fac.within(expr1, function);
        LOGGER.finer(filter.toString());
        LOGGER.finer("contains feature: " + filter.evaluate(testFeature));
        Assert.assertTrue(filter.evaluate(testFeature));

        filter = fac.within(expr2, expr1);

        LOGGER.finer(filter.toString());
        LOGGER.finer("contains feature: " + filter.evaluate(testFeature));
        Assert.assertFalse(filter.evaluate(testFeature));

        coords = new Coordinate[] {
            new Coordinate(2, 2), new Coordinate(6, 0), new Coordinate(6, 7), new Coordinate(0, 7), new Coordinate(2, 2)
        };
        expr2 = new LiteralExpressionImpl(gf.createPolygon(gf.createLinearRing(coords), new LinearRing[0]));
        filter = fac.within(expr2, expr1);

        LOGGER.finer(filter.toString());
        LOGGER.finer("contains feature: " + filter.evaluate(testFeature));
        Assert.assertFalse(filter.evaluate(testFeature));

        expr2 = new LiteralExpressionImpl(null);
        filter = fac.within(expr2, expr1);

        LOGGER.finer(filter.toString());
        LOGGER.finer("contains feature: " + filter.evaluate(testFeature));
        Assert.assertFalse(filter.evaluate(testFeature));
    }

    @Test
    public void testDisjoint() throws Exception {
        Coordinate[] coords = new Coordinate[3];
        coords[0] = new Coordinate(0, 0);
        coords[1] = new Coordinate(3, 0);
        coords[2] = new Coordinate(6, 0);

        GeometryFactory gf = new GeometryFactory(new PrecisionModel());

        // Test Disjoint
        AttributeExpressionImpl expr1 = new AttributeExpressionImpl(testSchema, "testGeometry");
        LineString geom = gf.createLineString(coords);
        LiteralExpressionImpl expr2 = new LiteralExpressionImpl(geom);
        Disjoint disjoint = fac.disjoint(expr1, expr2);

        LOGGER.finer(disjoint.toString());
        LOGGER.finer("contains feature: " + disjoint.evaluate(testFeature));
        Assert.assertTrue(disjoint.evaluate(testFeature));

        Function function = new GeometryFunction(geom);
        disjoint = fac.disjoint(expr1, function);
        LOGGER.finer(disjoint.toString());
        LOGGER.finer("contains feature: " + disjoint.evaluate(testFeature));
        Assert.assertTrue(disjoint.evaluate(testFeature));

        disjoint = fac.disjoint(expr2, expr1);

        LOGGER.finer(disjoint.toString());
        LOGGER.finer("contains feature: " + disjoint.evaluate(testFeature));
        Assert.assertTrue(disjoint.evaluate(testFeature));

        coords[0] = new Coordinate(1, 2);
        coords[1] = new Coordinate(3, 0);
        coords[2] = new Coordinate(6, 0);
        geom = gf.createLineString(coords);
        expr2 = new LiteralExpressionImpl(geom);
        disjoint = fac.disjoint(expr1, expr2);

        LOGGER.finer(disjoint.toString());
        LOGGER.finer("contains feature: " + disjoint.evaluate(testFeature));
        Assert.assertFalse(disjoint.evaluate(testFeature));

        expr2 = new LiteralExpressionImpl(null);
        disjoint = fac.disjoint(expr1, expr2);

        LOGGER.finer(disjoint.toString());
        LOGGER.finer("contains feature: " + disjoint.evaluate(testFeature));
        Assert.assertFalse(disjoint.evaluate(testFeature));
    }

    @Test
    public void testIntersects() throws Exception {
        Coordinate[] coords = new Coordinate[3];
        coords[0] = new Coordinate(1, 5);
        coords[1] = new Coordinate(3, 4);
        coords[2] = new Coordinate(5, 1);

        GeometryFactory gf = new GeometryFactory(new PrecisionModel());

        // Test Disjoint
        AttributeExpressionImpl expr1 = new AttributeExpressionImpl(testSchema, "testGeometry");
        LineString geom = gf.createLineString(coords);
        LiteralExpressionImpl expr2 = new LiteralExpressionImpl(geom);
        Intersects intersects = fac.intersects(expr1, expr2);

        LOGGER.finer(intersects.toString());
        LOGGER.finer("contains feature: " + intersects.evaluate(testFeature));
        Assert.assertTrue(intersects.evaluate(testFeature));

        intersects = fac.intersects(expr2, expr1);

        LOGGER.finer(intersects.toString());
        LOGGER.finer("contains feature: " + intersects.evaluate(testFeature));
        Assert.assertTrue(intersects.evaluate(testFeature));

        Function function = new GeometryFunction(geom);
        intersects = fac.intersects(expr1, function);
        LOGGER.finer(intersects.toString());
        LOGGER.finer("contains feature: " + intersects.evaluate(testFeature));
        Assert.assertTrue(intersects.evaluate(testFeature));

        LOGGER.finer(intersects.toString());
        LOGGER.finer("contains feature: " + intersects.evaluate(testFeature));
        Assert.assertTrue(intersects.evaluate(testFeature));

        coords[0] = new Coordinate(0, 0);
        coords[1] = new Coordinate(3, 0);
        coords[2] = new Coordinate(6, 0);
        expr2 = new LiteralExpressionImpl(gf.createLineString(coords));
        intersects = fac.intersects(expr1, expr2);

        LOGGER.finer(intersects.toString());
        LOGGER.finer("contains feature: " + intersects.evaluate(testFeature));
        Assert.assertFalse(intersects.evaluate(testFeature));

        expr2 = new LiteralExpressionImpl(null);
        intersects = fac.intersects(expr1, expr2);

        LOGGER.finer(intersects.toString());
        LOGGER.finer("contains feature: " + intersects.evaluate(testFeature));
        Assert.assertFalse(intersects.evaluate(testFeature));
    }

    /**
     * Test the geometry operators.
     *
     * @throws IllegalFilterException If the constructed filter is not valid.
     */
    @Test
    public void testBBOX() throws IllegalFilterException {

        // Test BBOX
        AttributeExpressionImpl left = new AttributeExpressionImpl(testSchema, "testGeometry");
        BBOX bbox = fac.bbox(left, 0, 0, 10, 10, null);

        LOGGER.finer(bbox.toString());
        LOGGER.finer("contains feature: " + bbox.evaluate(testFeature));
        Assert.assertTrue(bbox.evaluate(testFeature));

        bbox = fac.bbox(left, 0, 0, 1, 1, null);

        LOGGER.finer(bbox.toString());
        LOGGER.finer("contains feature: " + bbox.evaluate(testFeature));
        Assert.assertFalse(bbox.evaluate(testFeature));

        bbox = fac.bbox(left, 0, 0, 10, 10, "EPSG:4326");

        LOGGER.finer(bbox.toString());
        LOGGER.finer("contains feature: " + bbox.evaluate(testFeature));
        Assert.assertTrue(bbox.evaluate(testFeature));

        bbox = fac.bbox(left, 0, 0, 10, 10, "");

        LOGGER.finer(bbox.toString());
        LOGGER.finer("contains feature: " + bbox.evaluate(testFeature));
        Assert.assertTrue(bbox.evaluate(testFeature));
    }

    @Test
    public void testDWithin() throws Exception {
        // Test DWithin
        PropertyName left = new AttributeExpressionImpl(testSchema, "testGeometry");

        Coordinate[] coords2 = new Coordinate[5];
        coords2[0] = new Coordinate(10, 10);
        coords2[1] = new Coordinate(15, 10);
        coords2[2] = new Coordinate(15, 15);
        coords2[3] = new Coordinate(10, 15);
        coords2[4] = new Coordinate(10, 10);
        GeometryFactory gf = new GeometryFactory(new PrecisionModel());
        Literal right = new LiteralExpressionImpl(gf.createPolygon(gf.createLinearRing(coords2), null));

        DWithin filter = fac.dwithin(left, right, 20, "m");
        LOGGER.finer(filter.toString());
        LOGGER.finer("contains feature: " + filter.evaluate(testFeature));
        Assert.assertTrue(filter.evaluate(testFeature));

        filter = fac.dwithin(left, right, 2, "m");
        LOGGER.finer(filter.toString());
        LOGGER.finer("contains feature: " + filter.evaluate(testFeature));
        Assert.assertFalse(filter.evaluate(testFeature));

        right = new LiteralExpressionImpl(null);
        filter = fac.dwithin(left, right, 2, "m");
        LOGGER.finer(filter.toString());
        LOGGER.finer("contains feature: " + filter.evaluate(testFeature));
        Assert.assertFalse(filter.evaluate(testFeature));
    }

    @Test
    public void testBeyond() throws Exception {
        PropertyName left = new AttributeExpressionImpl(testSchema, "testGeometry");

        Coordinate[] coords2 = new Coordinate[5];
        coords2[0] = new Coordinate(10, 10);
        coords2[1] = new Coordinate(15, 10);
        coords2[2] = new Coordinate(15, 15);
        coords2[3] = new Coordinate(10, 15);
        coords2[4] = new Coordinate(10, 10);
        GeometryFactory gf = new GeometryFactory(new PrecisionModel());
        Literal right = new LiteralExpressionImpl(gf.createPolygon(gf.createLinearRing(coords2), null));

        Beyond filter = fac.beyond(left, right, 20, "m");
        LOGGER.finer(filter.toString());
        LOGGER.finer("contains feature: " + filter.evaluate(testFeature));
        Assert.assertFalse(filter.evaluate(testFeature));

        filter = fac.beyond(left, right, 2, "m");
        LOGGER.finer("contains feature: " + filter.evaluate(testFeature));
        Assert.assertTrue(filter.evaluate(testFeature));

        coords2[0] = new Coordinate(20, 20);
        coords2[1] = new Coordinate(21, 20);
        coords2[2] = new Coordinate(21, 21);
        coords2[3] = new Coordinate(20, 21);
        coords2[4] = new Coordinate(20, 20);
        right = fac.literal(gf.createPolygon(gf.createLinearRing(coords2), null));
        filter = fac.beyond(left, right, 2, "m");
        LOGGER.finer(filter.toString());
        LOGGER.finer("contains feature: " + filter.evaluate(testFeature));
        Assert.assertTrue(filter.evaluate(testFeature));

        right = new LiteralExpressionImpl(null);
        filter = fac.beyond(left, right, 2, "m");
        LOGGER.finer(filter.toString());
        LOGGER.finer("contains feature: " + filter.evaluate(testFeature));
        Assert.assertFalse(filter.evaluate(testFeature));
    }

    @Test
    public void testFid() {
        Id ff = fac.id(new HashSet<>());
        Assert.assertFalse(ff.evaluate(testFeature));
        ff = fac.id(Collections.singleton(fac.featureId(testFeature.getID())));
        Assert.assertTrue(ff.evaluate(testFeature));
        Assert.assertFalse(ff.evaluate(null));
        Assert.assertFalse(ff.evaluate(new Object()));
    }

    /**
     * Test the logic operators.
     *
     * @throws IllegalFilterException If the constructed filter is not valid.
     */
    @Test
    public void testOrFilter() throws IllegalFilterException {
        // Set up true sub filter
        PropertyName testAttribute = new AttributeExpressionImpl(testSchema, "testString");
        Literal testLiteral = new LiteralExpressionImpl("test string data");

        PropertyIsEqualTo filterTrue = fac.equals(testAttribute, testLiteral);

        // Set up false sub filter
        testLiteral = new LiteralExpressionImpl("incorrect test string data");
        PropertyIsEqualTo filterFalse = fac.equals(testAttribute, testLiteral);

        // Test OR for false negatives
        Or filter = fac.or(filterFalse, filterTrue);

        LOGGER.finer(filter.toString());
        LOGGER.finer("contains feature: " + filter.evaluate(testFeature));
        Assert.assertTrue(filter.evaluate(testFeature));

        // Test OR for false negatives
        filter = fac.or(filterTrue, filterTrue);

        LOGGER.finer(filter.toString());
        LOGGER.finer("contains feature: " + filter.evaluate(testFeature));
        Assert.assertTrue(filter.evaluate(testFeature));

        // Test OR for false positives
        filter = fac.or(filterFalse, filterFalse);
        Assert.assertFalse(filter.evaluate(testFeature));

        LOGGER.finer(filter.toString());
        LOGGER.finer("contains feature: " + filter.evaluate(testFeature));
        Assert.assertFalse(filter.evaluate(testFeature));
    }

    /**
     * Test the logic operators.
     *
     * @throws IllegalFilterException If the constructed filter is not valid.
     */
    @Test
    public void testAndFilter() throws IllegalFilterException {
        // Set up true sub filter
        PropertyName testAttribute = new AttributeExpressionImpl(testSchema, "testString");
        Literal testLiteral = new LiteralExpressionImpl("test string data");

        PropertyIsEqualTo filterTrue = fac.equals(testAttribute, testLiteral);

        // Set up false sub filter
        testLiteral = new LiteralExpressionImpl("incorrect test string data");
        PropertyIsEqualTo filterFalse = fac.equals(testAttribute, testLiteral);

        // Test AND for false positives
        And filter = fac.and(filterFalse, filterTrue);

        LOGGER.finer(filter.toString());
        LOGGER.finer("contains feature: " + filter.evaluate(testFeature));
        Assert.assertFalse(filter.evaluate(testFeature));

        // Test AND for false positives
        filter = fac.and(filterTrue, filterFalse);

        LOGGER.finer(filter.toString());
        LOGGER.finer("contains feature: " + filter.evaluate(testFeature));
        Assert.assertFalse(filter.evaluate(testFeature));

        // Test AND for false positives
        filter = fac.and(filterTrue, filterTrue);
        LOGGER.finer(filter.toString());
        LOGGER.finer("contains feature: " + filter.evaluate(testFeature));
        Assert.assertTrue(filter.evaluate(testFeature));

        // finally test nothing shortcut
        Assert.assertFalse(fac.not(filter).evaluate(testFeature));
    }

    @Test
    public void testLiteralExpression() {
        LiteralExpressionImpl literal = new LiteralExpressionImpl(1.0D);
        Assert.assertEquals(ExpressionType.LITERAL_DOUBLE, Filters.getExpressionType(literal));
        Assert.assertEquals(Double.valueOf(1.0D), literal.evaluate(null));

        GeometryFactory gf = new GeometryFactory();
        literal = new LiteralExpressionImpl(gf.createPoint(new Coordinate(0, 0)));
        Assert.assertEquals(ExpressionType.LITERAL_GEOMETRY, Filters.getExpressionType(literal));
        Geometry value = (Geometry) literal.evaluate(null);
        Assert.assertTrue(gf.createPoint(new Coordinate(0, 0)).equalsExact(value));

        literal = new LiteralExpressionImpl(1);
        Assert.assertEquals(ExpressionType.LITERAL_INTEGER, Filters.getExpressionType(literal));
        Assert.assertEquals(Integer.valueOf(1), literal.evaluate(null));

        literal = new LiteralExpressionImpl(1L);
        Assert.assertEquals(ExpressionType.LITERAL_LONG, Filters.getExpressionType(literal));
        Assert.assertEquals(Long.valueOf(1), literal.evaluate(null));

        literal = new LiteralExpressionImpl("string value");
        Assert.assertEquals(ExpressionType.LITERAL_STRING, Filters.getExpressionType(literal));
        Assert.assertEquals("string value", literal.evaluate(null));

        literal = new LiteralExpressionImpl(new Date(0));
        Assert.assertEquals(ExpressionType.LITERAL_UNDECLARED, Filters.getExpressionType(literal));
        Assert.assertEquals(new Date(0), literal.evaluate(null));

        literal = new LiteralExpressionImpl(null);
        Assert.assertEquals(ExpressionType.LITERAL_UNDECLARED, Filters.getExpressionType(literal));
        Assert.assertNull(literal.evaluate(null));
    }

    /**
     * Test that Filter works over Object as expected, provided there exists a {@link PropertyAccessor} for the given
     * kind of object.
     */
    @Test
    public void testEvaluateNonFeatureObject() {
        MockDataObject object = new MockDataObject();
        object.intVal = 5;
        object.stringVal = "cinco";

        org.geotools.api.filter.Filter f = fac.greater(fac.property("intVal"), fac.literal(3));

        Assert.assertTrue(f.evaluate(object));

        org.geotools.api.filter.Filter f2 = fac.and(f, fac.equals(fac.property("stringVal"), fac.literal("cinco")));

        Assert.assertTrue(f2.evaluate(object));

        org.geotools.api.filter.Filter f3 = fac.and(f, fac.equals(fac.property("stringVal"), fac.literal("seis")));

        Assert.assertFalse(f3.evaluate(object));

        org.geotools.api.filter.Filter f4 =
                fac.not(fac.and(f, fac.equals(fac.property("stringVal"), fac.literal("cinco"))));

        Assert.assertFalse(f4.evaluate(object));
    }

    /**
     * A simple data object to be used on testing Filter.evaluate(Object) through {@link MockPropertyAccessorFactory}
     *
     * @author Gabriel Roldan, Axios Engineering
     */
    public static class MockDataObject {
        public int intVal;

        public String stringVal;

        public MockDataObject() {
            this(0, null);
        }

        public MockDataObject(int intVal, String stringVal) {
            this.intVal = intVal;
            this.stringVal = stringVal;
        }
    }

    /**
     * A {@link PropertyAccessorFactory} intended to be used on testing that the Filter implementation works over Object
     * as expected, and not only over SimpleFeature
     *
     * @author Gabriel Roldan, Axios Engineering
     */
    public static class MockPropertyAccessorFactory implements PropertyAccessorFactory {

        @Override
        public PropertyAccessor createPropertyAccessor(Class type, String xpath, Class target, Hints hints) {
            if (!MockDataObject.class.equals(type)) {
                return null;
            }
            return new PropertyAccessor() {
                @Override
                public boolean canHandle(Object object, String xpath, Class target) {
                    return object instanceof MockDataObject;
                }

                @Override
                public <T> T get(Object object, String xpath, Class<T> target) throws IllegalArgumentException {
                    if (object == null) return null;

                    try {
                        Field field = MockDataObject.class.getField(xpath);
                        Object value = field.get(object);
                        @SuppressWarnings("unchecked")
                        T cast = (T) value;
                        return cast;
                    } catch (Exception e) {
                        throw (IllegalArgumentException)
                                new IllegalArgumentException("Illegal property name: " + xpath).initCause(e);
                    }
                }

                @Override
                public void set(Object object, String xpath, Object value, Class target)
                        throws IllegalArgumentException {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }

    private static final class GeometryFunction implements Function {
        final Geometry ls;

        public GeometryFunction(Geometry geom) throws Exception {
            ls = geom;
        }

        @Override
        public String getName() {
            return "function";
        }

        @Override
        public FunctionName getFunctionName() {
            return new FunctionNameImpl(getName(), 0);
        }

        @Override
        public List<org.geotools.api.filter.expression.Expression> getParameters() {
            return Collections.emptyList();
        }

        @Override
        public Object accept(ExpressionVisitor visitor, Object extraData) {
            return visitor.visit(this, extraData);
        }

        @Override
        public Object evaluate(Object object) {
            return ls;
        }

        @Override
        public <T> T evaluate(Object object, Class<T> context) {
            return context.cast(ls);
        }

        @Override
        public Literal getFallbackValue() {
            return null;
        }
    }

    @Test
    public void testSafeConversions() {
        Literal d = fac.literal(1.1);
        Literal i = fac.literal(1);

        Filter f1 = fac.greater(d, i);
        Assert.assertTrue(f1.evaluate(null));

        Filter f2 = fac.less(i, d);
        Assert.assertTrue(f2.evaluate(null));
    }

    @Test
    public void testFilterEquality() {
        Filter f1 = fac.less(fac.property("ATR"), fac.literal("32"));
        Filter f2 = fac.notEqual(fac.property("ATR2"), fac.literal("1"));

        Assert.assertEquals(f1, f1);
        Assert.assertNotEquals(f1, f2);
        Assert.assertNotEquals(f2, f1);

        Filter f4 = fac.notEqual(fac.property("BBB"), fac.literal("2"));
        Assert.assertNotEquals(f2, f4);
        Assert.assertNotEquals(f4, f2);

        Filter f3 = fac.less(fac.property("ATR"), fac.literal("40"));
        Assert.assertNotEquals(f1, f3);
        Assert.assertNotEquals(f3, f1);

        Expression l32 = fac.literal("32");
        Expression l40 = fac.literal("40");
        Assert.assertNotEquals(l32, l40);
        Assert.assertNotEquals(l40, l32);
    }

    @Test
    public void testNullBetween() {
        Filter f = fac.between(fac.property("nullInt"), fac.literal(10), fac.literal(20));
        Assert.assertFalse(f.evaluate(testFeature));
    }

    @Test
    public void testBoundedBy() {
        Geometry box = JTS.toGeometry(new Envelope(0, 10, 0, 10));
        Intersects intersects = fac.intersects(fac.function("boundedBy"), fac.literal(box));

        Assert.assertTrue(intersects.evaluate(testFeature));
    }

    @Test
    public void testInConditionWithDateClassesGEOS11591() {
        InFunction inFunctionTimestamp = new InFunction();
        inFunctionTimestamp.setParameters(List.of(fac.property("datetime2"), fac.literal("2007-08-15 12:00:00")));
        Assert.assertTrue(fac.equals(inFunctionTimestamp, fac.literal(true)).evaluate(testFeature));

        InFunction inFunctionUtilDate = new InFunction();
        inFunctionUtilDate.setParameters(List.of(fac.property("datetime1"), fac.literal("2007-08-15 12:00:00PM")));
        Assert.assertTrue(fac.equals(inFunctionUtilDate, fac.literal(true)).evaluate(testFeature));

        InFunction inFunctionSqlDate = new InFunction();
        inFunctionSqlDate.setParameters(List.of(fac.property("date"), fac.literal("2007-08-15")));
        Assert.assertTrue(fac.equals(inFunctionSqlDate, fac.literal(true)).evaluate(testFeature));

        InFunction inFunctionSqlTime = new InFunction();
        inFunctionSqlTime.setParameters(List.of(fac.property("time"), fac.literal("12:00:00")));
        Assert.assertTrue(fac.equals(inFunctionSqlTime, fac.literal(true)).evaluate(testFeature));
    }
}
