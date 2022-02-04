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

import static java.util.Calendar.FEBRUARY;
import static org.junit.Assert.fail;

import java.util.TimeZone;
import org.geootols.filter.text.cql_2.CQL2;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.cql2.FilterCQLSample;
import org.geotools.filter.text.ecql.ECQLComparisonPredicateTest;
import org.junit.Test;
import org.opengis.filter.Filter;

/**
 * Several things changed here:
 *
 * <ul>
 *   <li>Deprecated syntax no longer supported (eq/neq/lte/gte/gt/lt/!)
 *   <li>Brackets are used for arrays (currently unsupported) should not be allowed for expressions
 *   <li>Date literals use a function like expression in CQL2, rather than an un-quoted literal as
 *       in CQL and ECQL
 * </ul>
 */
public class CQL2ComparisonPredicateTest extends ECQLComparisonPredicateTest {

    /** Old CQL sytanx no longer supported */
    @Override
    public void deprecatedPredicate() {
        ensureFail("POP_RANK eq 6");
        ensureFail("POP_RANK neq 6");
        ensureFail("POP_RANK lte 6");
        ensureFail("! (POP_RANK = 6)");

        ensureFail("POP_RANK eq 6");
        ensureFail("POP_RANK == 6");

        ensureFail("POP_RANK lte 6");
        ensureFail("POP_RANK gte 6");
        ensureFail("POP_RANK lt 6");
        ensureFail("POP_RANK gt 6");

        ensureFail("! (POP_RANK == 6)");
        ensureFail("POP_RANK neq 6");

        ensureFail("A > 2 && B < 1");
    }

    /**
     * Brackets are used for array literals now, trying to use them as alternative to parenthesis
     * should fail.
     *
     * @throws Exception
     */
    @Override
    @Test
    public void bracketRoundtripFilter() {
        ensureFail(FilterCQLSample.FILTER_WITH_BRACKET_ROUNDTRIP_EXPR);
        ensureFail("[[[ X < 4 ] AND NOT [ Y < 4 ]] AND [ Z < 4 ]]");
        ensureFail("[X<4 AND Y<4 ] OR Z<4");
        ensureFail("[([ X < 4 ] AND NOT [ Y < 4 ]) AND [ Z < 4 ]]");
    }

    @Override
    @Test
    public void dateLiteral() throws Exception {
        Filter f = parseFilter("X = DATE('2012-02-01')");
        testPropertyIsEqualDate(f, date(2012, FEBRUARY, 1, 0, 0, 0, 0, TimeZone.getDefault()));
    }

    @Override
    @Test
    public void dateLiteralTimeZoneUTC() throws Exception {
        Filter f = parseFilter("X = DATE('2012-02-01Z')");
        testPropertyIsEqualDate(
                f, date(2012, FEBRUARY, 1, 0, 0, 0, 0, TimeZone.getTimeZone("GMT")));
    }

    @Override
    @Test
    public void dateLiteralTimeZonePlusMinus() throws Exception {
        Filter f = parseFilter("X = DATE('2012-02-01-0800')");
        testPropertyIsEqualDate(
                f, date(2012, FEBRUARY, 1, 0, 0, 0, 0, TimeZone.getTimeZone("GMT-8:00")));

        f = parseFilter("X = DATE('2012-02-01+08:00')");
        testPropertyIsEqualDate(
                f, date(2012, FEBRUARY, 1, 0, 0, 0, 0, TimeZone.getTimeZone("GMT+8:00")));
    }

    @Override
    @Test
    public void dateTimeLiteral() throws Exception {
        Filter f = parseFilter("X = TIMESTAMP('2012-02-01T12:10:13Z')");
        testPropertyIsEqualDate(
                f, date(2012, FEBRUARY, 1, 12, 10, 13, 0, TimeZone.getTimeZone("UTC")));
    }

    @Override
    @Test
    public void dateTimeLiteralMilliseconds() throws Exception {
        Filter f = parseFilter("X = TIMESTAMP('2012-02-01T12:10:13.123Z')");
        testPropertyIsEqualDate(
                f, date(2012, FEBRUARY, 1, 12, 10, 13, 123, TimeZone.getTimeZone("UTC")));
    }

    @Override
    @Test
    public void dateTimeLiteralTimeZoneUTC() throws Exception {
        Filter f = parseFilter("X = TIMESTAMP('2012-02-01T12:10:13.123Z')");
        testPropertyIsEqualDate(
                f, date(2012, FEBRUARY, 1, 12, 10, 13, 123, TimeZone.getTimeZone("GMT")));
    }

    @Override
    @Test
    public void dateTimeLiteralTimeZonePlusMinus() throws Exception {
        Filter f = parseFilter("X = TIMESTAMP('2012-02-01T12:10:13.123-0800')");
        testPropertyIsEqualDate(
                f, date(2012, FEBRUARY, 1, 12, 10, 13, 123, TimeZone.getTimeZone("GMT-8:00")));

        f = parseFilter("X = TIMESTAMP('2012-02-01T12:10:13+08:00')");
        testPropertyIsEqualDate(
                f, date(2012, FEBRUARY, 1, 12, 10, 13, 0, TimeZone.getTimeZone("GMT+8:00")));
    }

    private void ensureFail(String filter) {
        try {
            Filter parsed = parseFilter(filter);
            fail("Filter should not have been parsed: " + filter + " but got instead: " + parsed);
        } catch (CQLException e) {
            // ignore, it's fine
        }
    }

    @Override
    protected Filter parseFilter(String filter) throws CQLException {
        return CQL2.toFilter(filter);
    }
}
