/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.filter.text.cql2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.geotools.filter.text.commons.CompilerUtil;
import org.geotools.filter.text.commons.Language;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.temporal.Before;

/**
 * Test for Temporal Predicate
 *
 * <pre>
 * &lt;temporal predicate  &gt;::=
 *      &lt;attribute_name &gt; BEFORE &lt;date-time expression &gt;
 *  |   &lt;attribute_name &gt; BEFORE OR DURING  &lt;period &gt;
 *  |   &lt;attribute_name &gt; DURING  &lt;period &gt;
 *  |   &lt;attribute_name &gt; DURING OR AFTER  &lt;period &gt;
 *  |   &lt;attribute_name &gt; AFTER  &lt;date-time expression &gt;
 * &lt;date-time expression &gt; ::=  &lt;date-time &gt; |  &lt;period &gt;
 * &lt;period &gt; ::=
 *      &lt;date-time &gt; &quot;/&quot;  &lt;date-time &gt;
 *  |   &lt;date-time &gt; &quot;/&quot;  &lt;duration &gt;
 *  |   &lt;duration &gt; &quot;/&quot;  &lt;date-time &gt;
 * </pre>
 *
 * @since 2.5
 * @author Mauricio Pazos (Axios Engineering)
 */
public class CQLTemporalPredicateTest {

    protected final Language language;

    /** New instance of CQLTemporalPredicateTest */
    public CQLTemporalPredicateTest() {

        this(Language.CQL);
    }

    /** New instance of CQLTemporalPredicateTest */
    protected CQLTemporalPredicateTest(final Language language) {

        assert language != null : "language cannot be null value";

        this.language = language;
    }

    /** This test for BEFORE rule */
    @Test
    public void before() throws Exception {
        Filter resultFilter;
        Filter expected;

        // -------------------------------------------------------------
        // <attribute_name> BEFORE <date-time expression>
        // -------------------------------------------------------------
        // ATTR1 BEFORE 2006-12-31T01:30:00Z
        resultFilter = CompilerUtil.parseFilter(this.language, FilterCQLSample.FILTER_BEFORE_DATE);

        Assert.assertNotNull("not null expected", resultFilter);

        expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_BEFORE_DATE);
        Assert.assertEquals(expected, resultFilter);

        // ATTR1 BEFORE 2006-12-31T01:30:00.123Z
        resultFilter =
                CompilerUtil.parseFilter(this.language, FilterCQLSample.FILTER_BEFORE_DATE_MILLIS);

        Assert.assertNotNull("not null expected", resultFilter);

        expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_BEFORE_DATE_MILLIS);
        Assert.assertEquals(expected, resultFilter);

        // ATTR1 BEFORE 2006-11-31T01:30:00Z/2006-12-31T01:30:00Z
        resultFilter =
                CompilerUtil.parseFilter(
                        this.language, FilterCQLSample.FILTER_BEFORE_PERIOD_BETWEEN_DATES);

        Assert.assertNotNull("Filter expected", resultFilter);

        expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_BEFORE_PERIOD_BETWEEN_DATES);

        Assert.assertEquals("less than first date of period ", expected, resultFilter);

        // ATTR1 BEFORE 2006-11-31T01:30:00.123Z/2006-12-31T01:30:00.456Z
        resultFilter =
                CompilerUtil.parseFilter(
                        this.language, FilterCQLSample.FILTER_BEFORE_PERIOD_BETWEEN_DATES_MILLIS);

        Assert.assertNotNull("Filter expected", resultFilter);

        expected =
                FilterCQLSample.getSample(
                        FilterCQLSample.FILTER_BEFORE_PERIOD_BETWEEN_DATES_MILLIS);

        Assert.assertEquals("less than first date of period ", expected, resultFilter);

        // ATTR1 BEFORE 2006-11-31T01:30:00Z/P30D
        resultFilter =
                CompilerUtil.parseFilter(
                        this.language, FilterCQLSample.FILTER_BEFORE_PERIOD_DATE_AND_DAYS);

        Assert.assertNotNull("Filter expected", resultFilter);

        expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_BEFORE_PERIOD_DATE_AND_DAYS);

        Assert.assertEquals("less than first date of period ", expected, resultFilter);

        // "ATTR1 BEFORE 2006-11-31T01:30:00Z/P1Y"
        resultFilter =
                CompilerUtil.parseFilter(
                        this.language, FilterCQLSample.FILTER_BEFORE_PERIOD_DATE_AND_YEARS);

        Assert.assertNotNull("Filter expected", resultFilter);

        expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_BEFORE_PERIOD_DATE_AND_YEARS);

        Assert.assertEquals("less than first date of period ", expected, resultFilter);

        // "ATTR1 BEFORE 2006-11-31T01:30:00Z/P12M"
        resultFilter =
                CompilerUtil.parseFilter(
                        this.language, FilterCQLSample.FILTER_BEFORE_PERIOD_DATE_AND_MONTHS);

        Assert.assertNotNull("Filter expected", resultFilter);

        expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_BEFORE_PERIOD_DATE_AND_MONTHS);

        Assert.assertEquals("less than first date of period ", expected, resultFilter);

        // ATTR1 BEFORE P10Y10M10DT5H5M5S/2006-11-30T01:30:00Z
        resultFilter =
                CompilerUtil.parseFilter(
                        this.language, FilterCQLSample.FILTER_BEFORE_PERIOD_YMD_HMS_DATE);

        Assert.assertNotNull("Filter expected", resultFilter);

        expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_BEFORE_PERIOD_YMD_HMS_DATE);

        Assert.assertEquals("greater filter", expected, resultFilter);
    }

    /** It must produce a filter with an instance of Date object */
    @Test(expected = CQLException.class)
    public void lostTime() throws CQLException {

        CompilerUtil.parseFilter(this.language, "ZONE_VALID_FROM BEFORE 2008-09-09");

        Assert.fail("CQLException is expected. The \"date-time\" rule requires a time");
    }

    @Test(expected = CQLException.class)
    public void badTime() throws CQLException {

        CompilerUtil.parseFilter(this.language, "ZONE_VALID_FROM BEFORE 2008-09-09 17:00:00");

        Assert.fail(
                "CQLException is expected. The \"date-time\" rule require a time preceded by \"T\"");
    }

    /** It must produce a filter with an instance of Date object */
    @Test
    public void dateTime() throws Exception {

        final String cqlDateTime = "2008-09-09T17:00:00Z";
        Filter resultFilter =
                CompilerUtil.parseFilter(this.language, "ZONE_VALID_FROM BEFORE " + cqlDateTime);

        Before comparation = (Before) resultFilter;

        // date test
        Expression expr2 = comparation.getExpression2();
        Literal literalDate = (Literal) expr2;

        final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

        final DateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
        dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));

        Date expectedDate = dateFormatter.parse(cqlDateTime);
        Date actualDate = (Date) literalDate.getValue();

        Assert.assertEquals(expectedDate, actualDate);
    }

    /** Test local time */
    @Test
    public void dateTimeWithLocalTime() throws Exception {

        String localTime = "2008-09-09T17:00:00";

        Filter resultFilter =
                CompilerUtil.parseFilter(this.language, "ZONE_VALID_FROM BEFORE " + localTime);

        Before comparation = (Before) resultFilter;

        // date test
        Expression expr2 = comparation.getExpression2();
        Literal literalDate = (Literal) expr2;

        final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

        final DateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
        dateFormatter.setTimeZone(TimeZone.getDefault());

        Date expectedDate = dateFormatter.parse(localTime);
        Date actualDate = (Date) literalDate.getValue();

        Assert.assertEquals(expectedDate, actualDate);
    }

    /** Test time zone offset */
    @Test
    public void dateTimeWithOffset() throws Exception {

        {
            // test offset GMT+01:00
            final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssz");
            final String offset = "GMT+01:00";
            TimeZone tz = TimeZone.getTimeZone(offset);
            dateFormatter.setTimeZone(tz);

            Filter resultFilter =
                    CompilerUtil.parseFilter(
                            this.language, "ZONE_VALID_FROM BEFORE 2008-09-09T17:00:00+01:00");

            Before comparation = (Before) resultFilter;

            Expression expr2 = comparation.getExpression2();
            Literal literalDate = (Literal) expr2;
            Date actualDate = (Date) literalDate.getValue();

            Date expectedDate = dateFormatter.parse("2008-09-09 17:00:00" + offset);

            Assert.assertEquals(expectedDate, actualDate);
        }

        {
            // JD: there was a bug in this test case with the date format, litte "z" has to
            // specified as a named timezone, not an offset, so it was just being ignored
            // plus the offset was wrong
            // test offset GMT-01:00
            // final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssz");
            //    final String offset = "GMT+01:00";
            final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            final String offset = "GMT-01:00";
            TimeZone tz = TimeZone.getTimeZone(offset);
            dateFormatter.setTimeZone(tz);

            Filter resultFilter =
                    CompilerUtil.parseFilter(
                            this.language, "ZONE_VALID_FROM BEFORE 2008-09-09T17:00:00-01:00");

            Before comparation = (Before) resultFilter;

            Expression expr2 = comparation.getExpression2();
            Literal literalDate = (Literal) expr2;
            Date actualDate = (Date) literalDate.getValue();

            // Date expectedDate = dateFormatter.parse("2008-09-09 17:00:00 " +  offset);
            Date expectedDate = dateFormatter.parse("2008-09-09 17:00:00");

            Assert.assertEquals(expectedDate, actualDate);
        }
    }

    /**
     * before with compound attribute
     *
     * <p>sample: gmd:aa:bb.gmd:cc.gmd:dd BEFORE P10Y10M10DT5H5M5S/2006-11-30T01:30:00Z
     */
    @Test
    public void beforeCompundProperty() throws CQLException {

        // test compound attribute gmd:aa:bb.gmd:cc.gmd:dd
        final String prop = "gmd:aa:bb.gmd:cc.gmd:dd";
        final String propExpected = "gmd:aa:bb/gmd:cc/gmd:dd";
        String predicate = prop + " BEFORE P10Y10M10DT5H5M5S/2006-11-30T01:30:00Z ";
        Filter resultFilter = CompilerUtil.parseFilter(this.language, predicate);

        Assert.assertTrue(resultFilter instanceof Before);

        Before lessFilter = (Before) resultFilter;
        Expression property = lessFilter.getExpression1();

        Assert.assertEquals(propExpected, property.toString());
    }

    /**
     * Test temporal predicate. This tests <b>BEFORE or DURING</b> rule[*]
     *
     * <p>
     *
     * <pre>
     *  &lt;temporal predicate  &gt;::=
     *          &lt;attribute_name &gt; BEFORE  &lt;date-time expression &gt;
     *      |   &lt;b&gt; &lt;attribute_name &gt; BEFORE OR DURING  &lt;period &gt;[*]&lt;/b&gt;
     *      |   &lt;attribute_name &gt; DURING  &lt;period &gt;
     *      |   &lt;attribute_name &gt; DURING OR AFTER  &lt;period &gt;
     *      |   &lt;attribute_name &gt; AFTER  &lt;date-time expression &gt;
     *  &lt;date-time expression &gt; ::=  &lt;date-time &gt; |  &lt;period &gt;
     *  &lt;period &gt; ::=
     *          &lt;date-time &gt; &quot;/&quot;  &lt;date-time &gt;[*]
     *      |   &lt;date-time &gt; &quot;/&quot;  &lt;duration &gt;[*]
     *      |   &lt;duration &gt; &quot;/&quot;  &lt;date-time &gt;[*]
     * </pre>
     */
    @Test
    public void beforeOrDuring() throws Exception {
        Filter resultFilter;
        Filter expected;
        // -------------------------------------------------------------
        // <attribute_name> BEFORE OR DURING <period>
        // -------------------------------------------------------------
        // ATTR1 BEFORE OR DURING 2006-11-31T01:30:00Z/2006-12-31T01:30:00Z
        resultFilter =
                CompilerUtil.parseFilter(
                        this.language,
                        FilterCQLSample.FILTER_BEFORE_OR_DURING_PERIOD_BETWEEN_DATES);

        Assert.assertNotNull("Filter expected", resultFilter);

        expected =
                FilterCQLSample.getSample(
                        FilterCQLSample.FILTER_BEFORE_OR_DURING_PERIOD_BETWEEN_DATES);

        Assert.assertEquals("less than or equal the last date of period ", expected, resultFilter);

        // ATTR1 BEFORE OR DURING P10Y10M10DT5H5M5S/2006-11-30T01:30:00Z
        resultFilter =
                CompilerUtil.parseFilter(
                        this.language, FilterCQLSample.FILTER_BEFORE_OR_DURING_PERIOD_YMD_HMS_DATE);

        Assert.assertNotNull("Filter expected", resultFilter);

        expected =
                FilterCQLSample.getSample(
                        FilterCQLSample.FILTER_BEFORE_OR_DURING_PERIOD_YMD_HMS_DATE);

        Assert.assertEquals(" filter", expected, resultFilter);

        // ATTR1 BEFORE OR DURING 2006-11-30T01:30:00Z/P10Y10M10DT5H5M5S
        resultFilter =
                CompilerUtil.parseFilter(
                        this.language, FilterCQLSample.FILTER_BEFORE_OR_DURING_PERIOD_DATE_YMD_HMS);

        Assert.assertNotNull("Filter expected", resultFilter);

        expected =
                FilterCQLSample.getSample(
                        FilterCQLSample.FILTER_BEFORE_OR_DURING_PERIOD_DATE_YMD_HMS);

        Assert.assertEquals(" filter", expected, resultFilter);
    }

    /**
     * Test temporal predicate. This tests <b>DURING OR AFTER</b> rule[*]
     *
     * <p>
     *
     * <pre>
     *  &lt;temporal predicate  &gt;::=
     *          &lt;attribute_name &gt; BEFORE  &lt;date-time expression &gt;
     *      |   &lt;b&gt; &lt;attribute_name &gt; BEFORE OR DURING  &lt;period &gt;&lt;/b&gt;
     *      |   &lt;attribute_name &gt; DURING  &lt;period &gt;
     *      |   &lt;attribute_name &gt; DURING OR AFTER  &lt;period &gt;[*]
     *      |   &lt;attribute_name &gt; AFTER  &lt;date-time expression &gt;
     *  &lt;date-time expression &gt; ::=  &lt;date-time &gt; |  &lt;period &gt;
     *  &lt;period &gt; ::=
     *          &lt;date-time &gt; &quot;/&quot;  &lt;date-time &gt;[*]
     *      |   &lt;date-time &gt; &quot;/&quot;  &lt;duration &gt;[*]
     *      |   &lt;duration &gt; &quot;/&quot;  &lt;date-time &gt;[*]
     * </pre>
     */
    @Test
    public void duringOrAfter() throws Exception {
        Filter resultFilter;
        Filter expected;
        // -------------------------------------------------------------
        // <attribute_name> BEFORE OR DURING <period>
        // -------------------------------------------------------------
        // ATTR1 DURING OF AFTER 2006-11-31T01:30:00Z/2006-12-31T01:30:00Z
        resultFilter =
                CompilerUtil.parseFilter(
                        this.language, FilterCQLSample.FILTER_DURING_OR_AFTER_PERIOD_BETWEEN_DATES);

        Assert.assertNotNull("Filter expected", resultFilter);

        expected =
                FilterCQLSample.getSample(
                        FilterCQLSample.FILTER_DURING_OR_AFTER_PERIOD_BETWEEN_DATES);

        Assert.assertEquals(
                "greater than or equal the first date of period ", expected, resultFilter);

        // ATTR1 DURING OR AFTER P10Y10M10DT5H5M5S/2006-11-30T01:30:00Z
        resultFilter =
                CompilerUtil.parseFilter(
                        this.language, FilterCQLSample.FILTER_DURING_OR_AFTER_PERIOD_YMD_HMS_DATE);

        Assert.assertNotNull("Filter expected", resultFilter);

        expected =
                FilterCQLSample.getSample(
                        FilterCQLSample.FILTER_DURING_OR_AFTER_PERIOD_YMD_HMS_DATE);

        Assert.assertEquals(
                "greater than or equal the first date (is calculated subtract period to last date) of period",
                expected,
                resultFilter);

        // ATTR1 DURING OR AFTER 2006-11-30T01:30:00Z/P10Y10M10DT5H5M5S
        resultFilter =
                CompilerUtil.parseFilter(
                        this.language, FilterCQLSample.FILTER_DURING_OR_AFTER_PERIOD_DATE_YMD_HMS);

        Assert.assertNotNull("Filter expected", resultFilter);

        expected =
                FilterCQLSample.getSample(
                        FilterCQLSample.FILTER_DURING_OR_AFTER_PERIOD_DATE_YMD_HMS);

        Assert.assertEquals("greater than or equal the first date", expected, resultFilter);
    }

    /**
     * Test temporal predicate. This tests <b>DURING</b> rule[*]
     *
     * <p>
     *
     * <pre>
     *  &lt;temporal predicate  &gt;::=
     *          &lt;attribute_name &gt; BEFORE  &lt;date-time expression &gt;
     *      |   &lt;b&gt; &lt;attribute_name &gt; BEFORE OR DURING  &lt;period &gt;&lt;/b&gt;
     *      |  &lt;attribute_name &gt; DURING  &lt;period &gt;[*]
     *      |  &lt;attribute_name &gt; DURING OR AFTER  &lt;period &gt;
     *      |  &lt;attribute_name &gt; AFTER  &lt;date-time expression &gt;
     *  &lt;date-time expression &gt; ::=  &lt;date-time &gt; |  &lt;period &gt;
     *  &lt;period &gt; ::=
     *          &lt;date-time &gt; &quot;/&quot;  &lt;date-time &gt;[*]
     *      |   &lt;date-time &gt; &quot;/&quot;  &lt;duration &gt;[*]
     *      |   &lt;duration &gt; &quot;/&quot;  &lt;date-time &gt;[*]
     * </pre>
     */
    @Test
    public void during() throws Exception {
        Filter resultFilter;
        Filter expected;

        // ATTR1 DURING 2006-11-30T01:30:00Z/2006-12-31T01:30:00Z
        resultFilter =
                CompilerUtil.parseFilter(
                        this.language, FilterCQLSample.FILTER_DURING_PERIOD_BETWEEN_DATES);

        Assert.assertNotNull("Filter expected", resultFilter);

        expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_DURING_PERIOD_BETWEEN_DATES);

        Assert.assertEquals("greater filter ", expected, resultFilter);

        // ATTR1 DURING 2006-11-30T01:30:00Z/P10Y10M10DT5H5M5S
        resultFilter =
                CompilerUtil.parseFilter(
                        this.language, FilterCQLSample.FILTER_DURING_PERIOD_DATE_YMD_HMS);

        Assert.assertNotNull("Filter expected", resultFilter);

        expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_DURING_PERIOD_DATE_YMD_HMS);

        Assert.assertEquals("greater filter", expected, resultFilter);

        // ATTR1 DURING P10Y10M10DT5H5M5S/2006-11-30T01:30:00Z
        resultFilter =
                CompilerUtil.parseFilter(
                        this.language, FilterCQLSample.FILTER_DURING_PERIOD_YMD_HMS_DATE);

        Assert.assertNotNull("Filter expected", resultFilter);

        expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_DURING_PERIOD_YMD_HMS_DATE);

        Assert.assertEquals("greater filter", expected, resultFilter);
    }

    /**
     * Test temporal predicate. This tests <B>AFTER</B> or during rule[*]
     *
     * <p>
     *
     * <pre>
     * &lt;temporal predicate  &gt;::=
     *          &lt;attribute_name &gt; BEFORE  &lt;date-time expression &gt;
     *      |   &lt;attribute_name &gt; BEFORE OR DURING  &lt;period &gt;
     *      |   &lt;attribute_name &gt; DURING  &lt;period &gt;
     *      |   &lt;attribute_name &gt; DURING OR AFTER  &lt;period &gt;
     *      |   &lt;B&gt;  &lt;attribute_name &gt; AFTER  &lt;date-time expression &gt;[*]&lt;/B&gt;
     *  &lt;date-time expression &gt; ::=  &lt;date-time &gt; |  &lt;period &gt;
     *  &lt;period &gt; ::=
     *          &lt;date-time &gt; &quot;/&quot;  &lt;date-time &gt;[*]
     *      |   &lt;date-time &gt; &quot;/&quot;  &lt;duration &gt;  [*]
     *      |  &lt;duration &gt; &quot;/&quot;  &lt;date-time &gt;  [*]
     * </pre>
     */
    @Test
    public void after() throws Exception {
        Filter resultFilter;
        Filter expected;

        // -------------------------------------------------------------
        // <attribute_name> AFTER <date-time expression>
        // -------------------------------------------------------------
        //
        resultFilter = CompilerUtil.parseFilter(this.language, FilterCQLSample.FILTER_AFTER_DATE);

        Assert.assertNotNull("Filter expected", resultFilter);

        expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_AFTER_DATE);

        Assert.assertEquals("greater filter ", expected, resultFilter);

        // -------------------------------------------------------------
        // <attribute_name> AFTER <period>
        // -------------------------------------------------------------
        // ATTR1 BEFORE 2006-11-31T01:30:00Z/2006-12-31T01:30:00Z
        resultFilter =
                CompilerUtil.parseFilter(
                        this.language, FilterCQLSample.FILTER_AFTER_PERIOD_BETWEEN_DATES);

        Assert.assertNotNull("Filter expected", resultFilter);

        expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_AFTER_PERIOD_BETWEEN_DATES);

        Assert.assertEquals("greater filter ", expected, resultFilter);

        // ATTR1 AFTER 2006-11-30T01:30:00Z/P10D
        resultFilter =
                CompilerUtil.parseFilter(
                        this.language, FilterCQLSample.FILTER_AFTER_PERIOD_DATE_DAYS);

        Assert.assertNotNull("Filter expected", resultFilter);

        expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_AFTER_PERIOD_DATE_DAYS);

        Assert.assertEquals("greater filter", expected, resultFilter);

        // ATTR1 AFTER 2006-11-30T01:30:00Z/P10M
        resultFilter =
                CompilerUtil.parseFilter(
                        this.language, FilterCQLSample.FILTER_AFTER_PERIOD_DATE_MONTH);

        Assert.assertNotNull("Filter expected", resultFilter);

        expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_AFTER_PERIOD_DATE_MONTH);

        Assert.assertEquals("greater filter", expected, resultFilter);

        // ATTR1 AFTER 2006-11-30T01:30:00Z/P10Y
        resultFilter =
                CompilerUtil.parseFilter(
                        this.language, FilterCQLSample.FILTER_AFTER_PERIOD_DATE_YEARS);

        Assert.assertNotNull("Filter expected", resultFilter);

        expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_AFTER_PERIOD_DATE_YEARS);

        Assert.assertEquals("greater filter", expected, resultFilter);

        // ATTR1 AFTER 2006-11-30T01:30:00Z/P10Y10M
        resultFilter =
                CompilerUtil.parseFilter(
                        this.language, FilterCQLSample.FILTER_AFTER_PERIOD_DATE_YEARS_MONTH);

        Assert.assertNotNull("Filter expected", resultFilter);

        expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_AFTER_PERIOD_DATE_YEARS_MONTH);

        Assert.assertEquals("greater filter", expected, resultFilter);

        // ATTR1 AFTER 2006-11-30T01:30:00Z/T5H
        resultFilter =
                CompilerUtil.parseFilter(
                        this.language, FilterCQLSample.FILTER_AFTER_PERIOD_DATE_HOURS);

        Assert.assertNotNull("Filter expected", resultFilter);

        expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_AFTER_PERIOD_DATE_HOURS);

        Assert.assertEquals("greater filter", expected, resultFilter);

        // ATTR1 AFTER 2006-11-30T01:30:00Z/T5M
        resultFilter =
                CompilerUtil.parseFilter(
                        this.language, FilterCQLSample.FILTER_AFTER_PERIOD_DATE_MINUTES);

        Assert.assertNotNull("FilSter expected", resultFilter);

        expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_AFTER_PERIOD_DATE_MINUTES);

        Assert.assertEquals("greater filter", expected, resultFilter);

        // ATTR1 AFTER 2006-11-30T01:30:00Z/T5S
        resultFilter =
                CompilerUtil.parseFilter(
                        this.language, FilterCQLSample.FILTER_AFTER_PERIOD_DATE_SECONDS);

        Assert.assertNotNull("Filter expected", resultFilter);

        expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_AFTER_PERIOD_DATE_SECONDS);

        Assert.assertEquals("greater filter", expected, resultFilter);

        // ATTR1 AFTER 2006-11-30T01:30:00Z/P10Y10M10DT5H5M5S
        resultFilter =
                CompilerUtil.parseFilter(
                        this.language, FilterCQLSample.FILTER_AFTER_PERIOD_DATE_YMD_HMS);

        Assert.assertNotNull("Filter expected", resultFilter);

        expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_AFTER_PERIOD_DATE_YMD_HMS);

        Assert.assertEquals("greater filter", expected, resultFilter);
    }

    /**
     * Test for issue
     *
     * <p>http://jira.codehaus.org/browse/GEOT-2157?page=com.atlassian.jira.plugin.system.issuetabpanels:comment-tabpanel&focusedCommentId=154661#action_154661
     *
     * <p>Note: this test is ignored to avoid to add cycles in the geotools general build.
     */
    @Ignore
    public void issueCOT2157() throws Exception {

        Runnable cqlRunner =
                new Runnable() {
                    public void run() {
                        final String predicate =
                                "( ZONE_VALID_FROM BEFORE 2008-09-15T00:00:00Z AND ( ZONE_VALID_TO IS NULL OR ZONE_VALID_TO AFTER 2008-09-15T00:00:00Z))";
                        try {
                            Filter filter = CompilerUtil.parseFilter(language, predicate);
                            Assert.assertNotNull(filter);
                        } catch (CQLException e) {
                            java.util.logging.Logger.getGlobal()
                                    .log(java.util.logging.Level.INFO, "", e);
                        }
                    }
                };

        Thread[] threadList = new Thread[1000];
        for (int i = 0; i < threadList.length; i++) {
            threadList[i] = new Thread(cqlRunner);
        }

        for (int i = 0; i < threadList.length; i++) {
            threadList[i].start();
        }
    }

    @Test
    public void equal() throws Exception {
        // -------------------------------------------------------------
        // <attribute_name> TEQUALS <date-time expression>
        // -------------------------------------------------------------
        // ATTR1 = 2006-12-31T01:30:00Z
        Filter resultFilter =
                CompilerUtil.parseFilter(this.language, FilterCQLSample.FILTER_EQUAL_DATETIME);
        Assert.assertNotNull("not null expected", resultFilter);

        Filter expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_EQUAL_DATETIME);
        Assert.assertEquals(expected, resultFilter);
    }
}
