/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.text.ecql;

import static java.util.Calendar.FEBRUARY;
import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.geotools.filter.AndImpl;
import org.geotools.filter.NotImpl;
import org.geotools.filter.text.commons.CompilerUtil;
import org.geotools.filter.text.commons.Language;
import org.geotools.filter.text.cql2.CQLComparisonPredicateTest;
import org.geotools.filter.text.cql2.CQLException;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.Filter;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

/**
 * ECQL Comparison Predicate Test Case.
 *
 * <p>The implementation must parse comparison predicate using the following grammar rule:
 *
 * <pre>
 * &lt comparison predicate &gt ::= &lt expression &gt &lt comp op &gt &lt expression &gt
 * </pre>
 *
 * <p>This test case extends the from CQL test in order to assure that this extension (ECQL)
 * contains the base language (CQL).
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 */
public class ECQLComparisonPredicateTest extends CQLComparisonPredicateTest {

    public ECQLComparisonPredicateTest() {
        // sets the language used to execute this test case
        super(Language.ECQL);
    }

    /** Equals predicate sample */
    @Test
    public void deprecatedPredicate() throws Exception {

        ECQL.toFilter("POP_RANK eq 6");
        ECQL.toFilter("POP_RANK neq 6");
        ECQL.toFilter("POP_RANK lte 6");
        ECQL.toFilter("! (POP_RANK = 6)");

        assertTrue(ECQL.toFilter("POP_RANK eq 6") instanceof PropertyIsEqualTo);
        assertTrue(ECQL.toFilter("POP_RANK == 6") instanceof PropertyIsEqualTo);

        assertTrue(ECQL.toFilter("POP_RANK lte 6") instanceof PropertyIsLessThanOrEqualTo);
        assertTrue(ECQL.toFilter("POP_RANK gte 6") instanceof PropertyIsGreaterThanOrEqualTo);
        assertTrue(ECQL.toFilter("POP_RANK lt 6") instanceof PropertyIsLessThan);
        assertTrue(ECQL.toFilter("POP_RANK gt 6") instanceof PropertyIsGreaterThan);

        assertTrue(ECQL.toFilter("! (POP_RANK == 6)") instanceof NotImpl);
        assertTrue(ECQL.toFilter("POP_RANK neq 6") instanceof NotImpl);

        assertTrue(ECQL.toFilter("A > 2 && B < 1") instanceof AndImpl);
    }
    /**
     * Test: Expression on the Left hand of comparison predicate
     *
     * <pre>
     * Sample: (1+3) > aProperty
     *         (1+3) > (4-5)
     * </pre>
     */
    @Test
    public void expressionComparisonProperty() throws CQLException {

        // (1+3) > aProperty
        testComparison(FilterECQLSample.EXPRESION_GREATER_PROPERTY);

        // (1+3) > (4-5)
        testComparison(FilterECQLSample.ADD_EXPRESION_GREATER_SUBTRACT_EXPRESION);

        // (x+3) > (y-5)
        testComparison(FilterECQLSample.EXPRESSIONS_WITH_PROPERTIES);
    }

    /** Negative value test */
    @Test
    public void negativeNumber() throws CQLException {

        // minus integer
        // aProperty > -1
        testComparison(FilterECQLSample.PROPERTY_GREATER_MINUS_INGEGER);

        // -1 > aProperty
        testComparison(FilterECQLSample.MINUS_INTEGER_GREATER_PROPERTY);

        // minus float
        // aProperty > -1.05
        testComparison(FilterECQLSample.PROPERTY_GREATER_MINUS_FLOAT);

        // -1.05 > aProperty
        testComparison(FilterECQLSample.MINUS_FLOAT_GREATER_PROPERTY);

        // -1.05 + 4.6 > aProperty
        testComparison(FilterECQLSample.MINUS_EXPR_GREATER_PROPERTY);

        //  aProperty > -1.05 + 4.6
        testComparison(FilterECQLSample.PROPERTY_GREATER_MINUS_EXPR);

        // -1.05 + (-4.6* -10) > aProperty
        testComparison(FilterECQLSample.PROPERTY_GREATER_NESTED_EXPR);

        // 10--1.05 > aProperty
        testComparison(FilterECQLSample.MINUS_MINUS_EXPR_GRATER_PROPERTY);
    }

    /**
     * Test: function on the Left hand of comparison predicate
     *
     * <pre>
     * Samples:
     *          abs(10) < aProperty
     *          area( the_geom ) < 30000
     *          area( the_geom ) < (1+3)
     *          area( the_geom ) < abs(10)
     *
     * </pre>
     */
    @Test
    public void functionsInComparison() throws CQLException {

        // abs(10) < aProperty
        testComparison(FilterECQLSample.ABS_FUNCTION_LESS_PROPERTY);

        // area( the_geom ) < 30000
        testComparison(FilterECQLSample.AREA_FUNCTION_LESS_NUMBER);

        // area( the_geom ) < (1+3)
        testComparison(FilterECQLSample.FUNCTION_LESS_SIMPLE_ADD_EXPR);

        // area( the_geom ) < abs(10)
        testComparison(FilterECQLSample.FUNC_AREA_LESS_FUNC_ABS);
    }

    @Test
    public void dateLiteral() throws Exception {
        Filter f = CompilerUtil.parseFilter(this.language, "X = 2012-02-01");
        testPropertyIsEqualDate(f, date(2012, FEBRUARY, 1, 0, 0, 0, 0, TimeZone.getDefault()));
    }

    @Test
    public void dateLiteralTimeZoneUTC() throws Exception {
        Filter f = CompilerUtil.parseFilter(this.language, "X = 2012-02-01Z");
        testPropertyIsEqualDate(
                f, date(2012, FEBRUARY, 1, 0, 0, 0, 0, TimeZone.getTimeZone("GMT")));
    }

    @Test
    public void dateLiteralTimeZonePlusMinus() throws Exception {
        Filter f = CompilerUtil.parseFilter(this.language, "X = 2012-02-01-0800");
        testPropertyIsEqualDate(
                f, date(2012, FEBRUARY, 1, 0, 0, 0, 0, TimeZone.getTimeZone("GMT-8:00")));

        f = CompilerUtil.parseFilter(this.language, "X = 2012-02-01+08:00");
        testPropertyIsEqualDate(
                f, date(2012, FEBRUARY, 1, 0, 0, 0, 0, TimeZone.getTimeZone("GMT+8:00")));
    }

    @Test
    public void dateTimeLiteral() throws Exception {
        Filter f = CompilerUtil.parseFilter(this.language, "X = 2012-02-01T12:10:13");
        testPropertyIsEqualDate(f, date(2012, FEBRUARY, 1, 12, 10, 13, 0, TimeZone.getDefault()));
    }

    @Test
    public void dateTimeLiteralMilliseconds() throws Exception {
        Filter f = CompilerUtil.parseFilter(this.language, "X = 2012-02-01T12:10:13.123");
        testPropertyIsEqualDate(f, date(2012, FEBRUARY, 1, 12, 10, 13, 123, TimeZone.getDefault()));
    }

    @Test
    public void dateTimeLiteralTimeZoneUTC() throws Exception {
        Filter f = CompilerUtil.parseFilter(this.language, "X = 2012-02-01T12:10:13.123Z");
        testPropertyIsEqualDate(
                f, date(2012, FEBRUARY, 1, 12, 10, 13, 123, TimeZone.getTimeZone("GMT")));
    }

    @Test
    public void dateTimeLiteralTimeZonePlusMinus() throws Exception {
        Filter f = CompilerUtil.parseFilter(this.language, "X = 2012-02-01T12:10:13.123-0800");
        testPropertyIsEqualDate(
                f, date(2012, FEBRUARY, 1, 12, 10, 13, 123, TimeZone.getTimeZone("GMT-8:00")));

        f = CompilerUtil.parseFilter(this.language, "X = 2012-02-01T12:10:13+08:00");
        testPropertyIsEqualDate(
                f, date(2012, FEBRUARY, 1, 12, 10, 13, 0, TimeZone.getTimeZone("GMT+8:00")));
    }

    /** Checks that both positive and negative numbers are parsed to numbers, not strings */
    @Test
    public void testPositiveNegativeConsistent() throws Exception {
        BinaryComparisonOperator f =
                (BinaryComparisonOperator) CompilerUtil.parseFilter(this.language, "foo > -1");
        assertEquals(Long.valueOf(-1), f.getExpression2().evaluate(null));
        f = (BinaryComparisonOperator) CompilerUtil.parseFilter(this.language, "foo > 1");
        assertEquals(Long.valueOf(1), f.getExpression2().evaluate(null));

        f = (BinaryComparisonOperator) CompilerUtil.parseFilter(this.language, "-1 > foo");
        assertEquals(Long.valueOf(-1), f.getExpression1().evaluate(null));
        f = (BinaryComparisonOperator) CompilerUtil.parseFilter(this.language, "1 > foo");
        assertEquals(Long.valueOf(1), f.getExpression1().evaluate(null));

        PropertyIsBetween between =
                (PropertyIsBetween) CompilerUtil.parseFilter(this.language, "foo between -1 and 1");
        assertEquals(Long.valueOf(-1), between.getLowerBoundary().evaluate(null));
        assertEquals(Long.valueOf(1), between.getUpperBoundary().evaluate(null));

        between =
                (PropertyIsBetween)
                        CompilerUtil.parseFilter(this.language, "-1 between foo and bar");
        assertEquals(Long.valueOf(-1), between.getExpression().evaluate(null));
    }

    private Date date(
            int year,
            int month,
            int dayOfMonth,
            int hourOfDay,
            int minute,
            int second,
            int milliscond,
            TimeZone tz) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        c.set(Calendar.MILLISECOND, milliscond);
        c.setTimeZone(tz);
        return c.getTime();
    }

    private void testPropertyIsEqualDate(Filter f, Date expected) {
        Assert.assertTrue(f instanceof PropertyIsEqualTo);

        PropertyIsEqualTo eq = (PropertyIsEqualTo) f;
        Assert.assertTrue(eq.getExpression1() instanceof PropertyName);
        Assert.assertTrue(eq.getExpression2() instanceof Literal);

        Object o = eq.getExpression2().evaluate(null);
        Assert.assertTrue(o instanceof Date);

        Date d = (Date) o;
        Assert.assertEquals(expected, d);
    }

    /**
     * Asserts that the filter returned is the specified by the predicate
     *
     * @param testPredicate predicate to test
     */
    private void testComparison(final String testPredicate) throws CQLException {

        Filter expected = FilterECQLSample.getSample(testPredicate);

        Filter actual = CompilerUtil.parseFilter(this.language, testPredicate);

        Assert.assertNotNull("expects filter not null", actual);

        Assert.assertEquals("compare filter error", expected, actual);
    }
}
