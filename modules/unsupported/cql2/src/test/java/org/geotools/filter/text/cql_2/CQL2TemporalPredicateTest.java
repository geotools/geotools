/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.filter.text.cql_2;

import static org.geotools.filter.text.cql2.FilterCQLSample.DATE_FORMATTER;
import static org.geotools.filter.text.cql2.FilterCQLSample.DATE_TIME_FORMATTER;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.geootols.filter.text.cql_2.CQL2;
import org.geotools.filter.text.commons.Language;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.cql2.FilterCQLSample;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.temporal.Before;

/**
 * Temporal predicates changes shape completely in CQL2, changes and TODOs:
 *
 * <ul>
 *   <li>Temporal operators now look like function invocations just like the spatial operators.
 *   <li>All temporal operators are prefixed by T_
 *   <li>All temporal operators work with both date and timestamp literals (before, just timestamp)
 *   <li>Temporal operators accept both date and datetime (before, just datetime)
 *   <li>Local times are not longer accepted (the grammar suggests to only accept UTC, but the
 *       parser still accepts times with offsets, might want to remove that ability...)
 *   <li>Time interval is quite different from the old PERIOD syntax, it looks like a function,
 *       takes a start/end, these two can be fixed, attributes, function, or the open ended "..".
 *       The ability to specify an anchor and a duration is gone.
 *   <li>BEFORE OR DURING and AFTER OR DURING are gone, there is no replacement
 *   <li>TODO: there is a number of new operators coming from filter encoding that are not parsed
 *       yet
 *   <li>TODO: currently parses periods only in their INSTANT(literal, literal) version of interval
 *   <li>TODO: support the open ended intervals, e.g., INSTANT('..', end)
 * </ul>
 */
public class CQL2TemporalPredicateTest {

    protected final Language language;

    /** New instance of CQLTemporalPredicateTest */
    public CQL2TemporalPredicateTest() {

        this(Language.CQL);
    }

    /** New instance of CQLTemporalPredicateTest */
    protected CQL2TemporalPredicateTest(final Language language) {

        assert language != null : "language cannot be null value";

        this.language = language;
    }

    /** This test for BEFORE rule (removed tests with durations, not supported by CQL2 */
    @Test
    public void before() throws Exception {
        Filter resultFilter = CQL2.toFilter("T_BEFORE(ATTR1, TIMESTAMP('2006-11-30T01:30:00Z'))");
        Assert.assertNotNull("not null expected", resultFilter);
        Filter expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_BEFORE_DATE);
        assertEquals(expected, resultFilter);

        resultFilter = CQL2.toFilter("T_BEFORE(ATTR1, TIMESTAMP('2006-11-30T01:30:00.123Z'))");
        Assert.assertNotNull("not null expected", resultFilter);
        expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_BEFORE_DATE_MILLIS);
        assertEquals(expected, resultFilter);

        resultFilter =
                CQL2.toFilter(
                        "T_BEFORE(ATTR1, INTERVAL(TIMESTAMP('"
                                + FilterCQLSample.FIRST_DATE
                                + "'), TIMESTAMP('"
                                + FilterCQLSample.LAST_DATE
                                + "')))");

        Assert.assertNotNull("Filter expected", resultFilter);

        expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_BEFORE_PERIOD_BETWEEN_DATES);

        Assert.assertEquals("less than first date of period ", expected, resultFilter);

        resultFilter =
                CQL2.toFilter(
                        "T_BEFORE(ATTR1, INTERVAL(TIMESTAMP('2006-11-30T01:30:00.123Z'), TIMESTAMP('2006-12-31T01:30:00.456Z')))");

        Assert.assertNotNull("Filter expected", resultFilter);

        expected =
                FilterCQLSample.getSample(
                        FilterCQLSample.FILTER_BEFORE_PERIOD_BETWEEN_DATES_MILLIS);

        Assert.assertEquals("less than first date of period ", expected, resultFilter);
    }

    /** In CQL2 any time instant expression is valid, not just timestamps but also dates */
    @Test
    public void beforeDate() throws CQLException, ParseException {

        Filter filter = CQL2.toFilter("T_BEFORE(ZONE_VALID_FROM, DATE('2008-09-09'))");
        assertThat(filter, instanceOf(Before.class));
        Before before = (Before) filter;
        Object ex2Value = before.getExpression2().evaluate(null);
        assertThat(ex2Value, instanceOf(Date.class));

        SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMATTER);
        Date dateTime = dateFormatter.parse("2008-09-09");

        assertEquals(dateTime, ex2Value);
    }

    @Test(expected = CQLException.class)
    public void badTime() throws CQLException {
        CQL.toFilter("T_BEFORE(ZONE_VALID_FROM, TIMESTAMP('2008-09-09 17:00:00'))");
    }

    /** It must produce a filter with an instance of Date object */
    @Test
    public void dateTime() throws Exception {

        final String cqlDateTime = "2008-09-09T17:00:00Z";
        Filter resultFilter =
                CQL2.toFilter("T_BEFORE(ZONE_VALID_FROM, TIMESTAMP('" + cqlDateTime + "'))");
        Before before = (Before) resultFilter;

        // date test
        Expression expr2 = before.getExpression2();
        Literal literalDate = (Literal) expr2;

        final DateFormat dateFormatter = new SimpleDateFormat(DATE_TIME_FORMATTER);
        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date expectedDate = dateFormatter.parse(cqlDateTime);
        Date actualDate = (Date) literalDate.getValue();

        Assert.assertEquals(expectedDate, actualDate);
    }

    /** Test local time not accepted */
    @Test(expected = CQLException.class)
    public void dateTimeWithLocalTime() throws Exception {
        CQL2.toFilter("T_BEFORE(ZONE_VALID_FROM, TIMESTAMP('2008-09-09T17:00:00'))");
    }

    /** Test time zone offset (should not be accepted in theory, only Z supported */
    @Test
    public void dateTimeWithOffset() throws Exception {

        // test offset GMT+01:00
        final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        final String offset = "GMT+01:00";
        TimeZone tz = TimeZone.getTimeZone(offset);
        dateFormatter.setTimeZone(tz);

        Filter resultFilter =
                CQL2.toFilter("T_BEFORE(ZONE_VALID_FROM, TIMESTAMP('2008-09-09T17:00:00+01:00'))");

        Before before = (Before) resultFilter;

        Expression expr2 = before.getExpression2();
        Literal literalDate = (Literal) expr2;
        Date actualDate = (Date) literalDate.getValue();

        Date expectedDate = dateFormatter.parse("2008-09-09T17:00:00" + offset);

        Assert.assertEquals(expectedDate, actualDate);
    }

    @Test
    public void during() throws Exception {
        Filter resultFilter =
                CQL2.toFilter(
                        "T_DURING(ATTR1, INTERVAL(TIMESTAMP('2006-11-30T01:30:00Z'), TIMESTAMP('2006-12-31T01:30:00Z')))");
        Assert.assertNotNull("Filter expected", resultFilter);

        Filter expected =
                FilterCQLSample.getSample(FilterCQLSample.FILTER_DURING_PERIOD_BETWEEN_DATES);

        Assert.assertEquals("greater filter ", expected, resultFilter);
    }

    @Test
    public void after() throws Exception {

        Filter resultFilter = CQL2.toFilter("T_AFTER(ATTR1, TIMESTAMP('2006-12-31T01:30:00Z'))");

        Assert.assertNotNull("Filter expected", resultFilter);

        Filter expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_AFTER_DATE);

        Assert.assertEquals("greater filter ", expected, resultFilter);

        resultFilter =
                CQL2.toFilter(
                        "T_AFTER(ATTR1, INTERVAL(TIMESTAMP('2006-11-30T01:30:00Z'), TIMESTAMP('2006-12-31T01:30:00Z')))");

        Assert.assertNotNull("Filter expected", resultFilter);

        expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_AFTER_PERIOD_BETWEEN_DATES);

        Assert.assertEquals("greater filter ", expected, resultFilter);
    }

    @Test
    public void equal() throws Exception {
        Filter resultFilter = CQL2.toFilter("T_EQUALS(ATTR1,  TIMESTAMP('2006-11-30T01:30:00Z'))");
        Assert.assertNotNull("not null expected", resultFilter);

        Filter expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_EQUAL_DATETIME);
        Assert.assertEquals(expected, resultFilter);
    }
}
