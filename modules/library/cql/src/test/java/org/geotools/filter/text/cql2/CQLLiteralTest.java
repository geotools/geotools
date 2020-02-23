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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.geotools.filter.function.FilterFunction_relatePattern;
import org.geotools.filter.text.commons.CompilerUtil;
import org.geotools.filter.text.commons.Language;
import org.geotools.referencing.CRS;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.io.WKTReader;
import org.opengis.filter.Filter;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Literal parser test
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 */
public class CQLLiteralTest {
    protected final Language language;

    public CQLLiteralTest() {

        this(Language.CQL);
    }

    protected CQLLiteralTest(final Language language) {

        assert language != null : "language cannot be null value";

        this.language = language;
    }

    /**
     * Tests Geometry Literals
     *
     * <p>
     *
     * <pre>
     *  &lt;geometry literal &gt; :=
     *          &lt;Point Tagged Text &gt;
     *      |   &lt;LineString Tagged Text &gt;
     *      |   &lt;Polygon Tagged Text &gt;
     *      |   &lt;MultiPoint Tagged Text &gt;
     *      |   &lt;MultiLineString Tagged Text &gt;
     *      |   &lt;MultiPolygon Tagged Text &gt;
     *      |   &lt;GeometryCollection Tagged Text &gt;
     *      |   &lt;Envelope Tagged Text &gt;
     * </pre>
     */
    @Test
    public void geometryLiterals() throws Exception {
        BinarySpatialOperator result;
        Literal geom;

        // Point":" <time-second> "Z"
        result =
                (BinarySpatialOperator)
                        CompilerUtil.parseFilter(this.language, "WITHIN(ATTR1, POINT(1 2))");

        geom = (Literal) result.getExpression2();

        Assert.assertNotNull(geom.getValue());
        Assert.assertTrue(geom.getValue() instanceof org.locationtech.jts.geom.Point);

        // LineString
        result =
                (BinarySpatialOperator)
                        CompilerUtil.parseFilter(
                                this.language, "WITHIN(ATTR1, LINESTRING(1 2, 10 15))");

        geom = (Literal) result.getExpression2();

        Assert.assertNotNull(geom.getValue());
        Assert.assertTrue(geom.getValue() instanceof org.locationtech.jts.geom.LineString);

        // Polygon
        result =
                (BinarySpatialOperator)
                        CompilerUtil.parseFilter(
                                this.language,
                                "WITHIN(ATTR1, POLYGON((1 2, 15 2, 15 20, 15 21, 1 2)))");

        geom = (Literal) result.getExpression2();

        Assert.assertNotNull(geom.getValue());
        Assert.assertTrue(geom.getValue() instanceof org.locationtech.jts.geom.Polygon);

        // MultiPoint
        result =
                (BinarySpatialOperator)
                        CompilerUtil.parseFilter(
                                this.language,
                                "WITHIN(ATTR1, MULTIPOINT( (1 2), (15 2), (15 20), (15 21), (1 2) ))");

        geom = (Literal) result.getExpression2();

        Assert.assertNotNull(geom.getValue());
        Assert.assertTrue(geom.getValue() instanceof org.locationtech.jts.geom.MultiPoint);

        // MultiLineString
        result =
                (BinarySpatialOperator)
                        CompilerUtil.parseFilter(
                                this.language,
                                "WITHIN(ATTR1, MULTILINESTRING((10 10, 20 20),(15 15,30 15)) )");

        geom = (Literal) result.getExpression2();

        Assert.assertNotNull(geom.getValue());
        Assert.assertTrue(geom.getValue() instanceof org.locationtech.jts.geom.MultiLineString);

        // MultiPolygon
        result =
                (BinarySpatialOperator)
                        CompilerUtil.parseFilter(
                                this.language,
                                "WITHIN(ATTR1, MULTIPOLYGON( ((10 10, 10 20, 20 20, 20 15, 10 10)),((60 60, 70 70, 80 60, 60 60 )) ) )");

        geom = (Literal) result.getExpression2();

        Assert.assertNotNull(geom.getValue());
        Assert.assertTrue(geom.getValue() instanceof org.locationtech.jts.geom.MultiPolygon);

        // ENVELOPE
        result =
                (BinarySpatialOperator)
                        CompilerUtil.parseFilter(
                                this.language, "WITHIN(ATTR1, ENVELOPE( 10, 20, 30, 40) )");

        geom = (Literal) result.getExpression2();

        Assert.assertNotNull(geom.getValue());
        Assert.assertTrue(geom.getValue() instanceof org.locationtech.jts.geom.Polygon);
    }

    /** Test error at geometry literal */
    @Test(expected = CQLException.class)
    public void geometryLiteralsError() throws CQLException {

        final String filterError = "WITHIN(ATTR1, POLYGON(1 2, 10 15), (10 15, 1 2)))";
        CompilerUtil.parseFilter(this.language, filterError);
    }

    /**
     *
     *
     * <pre>
     * &lt;character string literal&gt; ::= &lt;quote&gt; [ {&lt;character representation&lt;} ]  &lt;quote&gt;
     * &lt;character representation&gt; ::=
     *                  &lt;nonquote character&gt;
     *          |       &lt;quote symbol&gt;
     * &lt;quote symbol&gt; ::=  &lt;quote&gt; &lt;quote&gt;
     * </pre>
     */
    @Test
    public void characterStringLiteral() throws Exception {

        PropertyIsEqualTo eqFilter;

        // space check
        final String strWithSpace = "ALL PRACTICES";
        Filter filterWithSpace =
                CompilerUtil.parseFilter(language, "practice='" + strWithSpace + "'");
        Assert.assertNotNull(filterWithSpace);
        Assert.assertTrue(filterWithSpace instanceof PropertyIsEqualTo);

        eqFilter = (PropertyIsEqualTo) filterWithSpace;
        Expression spacesLiteral = eqFilter.getExpression2();
        Assert.assertEquals(strWithSpace, spacesLiteral.toString());

        // empty string ''
        Filter emptyFilter = CompilerUtil.parseFilter(language, "MAJOR_WATERSHED_SYSTEM = ''");

        Assert.assertNotNull(emptyFilter);
        Assert.assertTrue(emptyFilter instanceof PropertyIsEqualTo);

        eqFilter = (PropertyIsEqualTo) emptyFilter;
        Expression emptyLiteral = eqFilter.getExpression2();
        Assert.assertEquals("", emptyLiteral.toString());

        // character string without quote
        final String expectedWithout = "ab";

        Filter filterWithoutQuote =
                CompilerUtil.parseFilter(
                        language, "MAJOR_WATERSHED_SYSTEM = '" + expectedWithout + "'");

        Assert.assertNotNull(filterWithoutQuote);
        Assert.assertTrue(filterWithoutQuote instanceof PropertyIsEqualTo);

        eqFilter = (PropertyIsEqualTo) filterWithoutQuote;
        Expression actualWhithoutQuote = eqFilter.getExpression2();
        Assert.assertEquals(expectedWithout, actualWhithoutQuote.toString());

        // <quote symbol>
        final String expected = "cde'' fg";

        Filter filter = CQL.toFilter("MAJOR_WATERSHED_SYSTEM = '" + expected + "'");

        Assert.assertNotNull(filter);
        Assert.assertTrue(filter instanceof PropertyIsEqualTo);

        eqFilter = (PropertyIsEqualTo) filter;
        Expression actual = eqFilter.getExpression2();
        Assert.assertEquals(expected.replaceAll("''", "'"), actual.toString());

        // special characters
        final String otherChars = "üä";

        filter =
                (PropertyIsEqualTo)
                        CompilerUtil.parseFilter(language, "NAME = '" + otherChars + "'");

        Assert.assertNotNull(filter);
        Assert.assertTrue(filter instanceof PropertyIsEqualTo);

        eqFilter = (PropertyIsEqualTo) filter;
        actual = eqFilter.getExpression2();
        Assert.assertEquals(otherChars, actual.toString());
    }

    @Test
    public void russianCharacterStringLiteral() throws Exception {

        testCharacterString("ДОБРИЧ");
        testCharacterString("название");
        testCharacterString("фамилия");
        testCharacterString("среды");
    }

    /** Japan charset */
    @Test
    public void japanCharacterStringLiteral() throws Exception {

        testCharacterString("名");
        testCharacterString("姓");
        testCharacterString("環境");
    }

    private void testCharacterString(final String str) throws Exception {

        Filter filter =
                (PropertyIsEqualTo) CompilerUtil.parseFilter(language, "NAME = '" + str + "'");

        Assert.assertNotNull(filter);
        Assert.assertTrue(filter instanceof PropertyIsEqualTo);

        PropertyIsEqualTo eqFilter = (PropertyIsEqualTo) filter;
        Expression actual = eqFilter.getExpression2();
        Assert.assertEquals(str, actual.toString());
    }

    @Test
    public void doubleLiteral() throws Exception {

        final String expected = "4.20082008E4";

        Expression expr = CompilerUtil.parseExpression(language, expected);

        Literal doubleLiteral = (Literal) expr;
        Double actual = (Double) doubleLiteral.getValue();

        Assert.assertEquals(Double.parseDouble(expected), actual.doubleValue(), 8);
    }

    @Test
    public void longLiteral() throws Exception {

        {
            final String expected = "123456789012345";

            Expression expr = CompilerUtil.parseExpression(language, expected);

            Literal intLiteral = (Literal) expr;
            Long actual = (Long) intLiteral.getValue();

            Assert.assertEquals(Long.parseLong(expected), actual.longValue());
        }

        {
            final String maxLongValue = "9223372036854775807";

            Expression expr = CompilerUtil.parseExpression(language, maxLongValue);

            Literal intLiteral = (Literal) expr;
            Long actual = (Long) intLiteral.getValue();

            Assert.assertEquals(Long.parseLong(maxLongValue), actual.longValue());
        }
    }

    /**
     * Tests that the ambiguous syntax between Integer and relate pattern is solved by the parser.
     */
    @Test
    public void clashLongLiteralandDE9IM() throws Exception {

        {
            final String expected = "201000002";

            Expression expr = CompilerUtil.parseExpression(language, expected);

            Literal intLiteral = (Literal) expr;
            Long actual = (Long) intLiteral.getValue();

            Assert.assertEquals(Long.parseLong(expected), actual.longValue());
        }
        {
            PropertyIsEqualTo resultFilter;

            resultFilter =
                    (PropertyIsEqualTo)
                            CompilerUtil.parseFilter(
                                    language,
                                    "RELATE(the_geom, LINESTRING (-134.921387 58.687767, -135.303391 59.092838), 201000002)");

            Expression relateFunction = resultFilter.getExpression1();
            Assert.assertTrue(relateFunction instanceof FilterFunction_relatePattern);

            Literal trueLiteral = (Literal) resultFilter.getExpression2();
            Assert.assertTrue(trueLiteral.getValue() instanceof Boolean);
        }
    }

    /**
     * Test the pattern that represent the intersection matrix that is required by the Relate
     * predicate.
     */
    @Test
    public void relatePatterns() throws CQLException {

        testRelatePatten("T******F*");

        testRelatePatten("T012**FF*");

        testRelatePatten("100000001");

        testRelatePatten("200000000");
    }

    private void testRelatePatten(final String pattern) throws CQLException {

        PropertyIsEqualTo resultFilter;

        resultFilter =
                (PropertyIsEqualTo)
                        CompilerUtil.parseFilter(
                                language,
                                "RELATE(the_geom, LINESTRING (-134.921387 58.687767, -135.303391 59.092838), "
                                        + pattern
                                        + ")");

        Expression relateFunction = resultFilter.getExpression1();
        Assert.assertTrue(relateFunction instanceof FilterFunction_relatePattern);

        Literal trueLiteral = (Literal) resultFilter.getExpression2();
        Assert.assertTrue(trueLiteral.getValue() instanceof Boolean);
    }

    @Test
    public void dateLiteral() throws Exception {

        // Date value
        final String expectedDate = "2009-08-01";

        Filter filter = CompilerUtil.parseFilter(language, "END_DATE = '" + expectedDate + "'");

        Assert.assertNotNull(filter);
        Assert.assertTrue(filter instanceof PropertyIsEqualTo);

        PropertyIsEqualTo eq = (PropertyIsEqualTo) filter;
        Expression exp = eq.getExpression2();
        Assert.assertTrue(exp instanceof Literal);
        Literal actualDate = (Literal) exp;

        Assert.assertEquals(expectedDate, actualDate.toString());
    }

    @Test
    public void timeLiteral() throws Exception {

        final String expectedTime = "12:08:01";

        Filter filter = CompilerUtil.parseFilter(language, "END_TIME = '" + expectedTime + "'");

        Assert.assertNotNull(filter);
        Assert.assertTrue(filter instanceof PropertyIsEqualTo);

        PropertyIsEqualTo eq = (PropertyIsEqualTo) filter;
        Expression exp = eq.getExpression2();
        Assert.assertTrue(exp instanceof Literal);
        Literal actualTime = (Literal) exp;

        Assert.assertEquals(expectedTime, actualTime.toString());
    }

    @Test
    public void timeStampLiteral() throws Exception {

        final String expectedDateTime = "2009-08-01 12:08:01";

        Filter filter = CompilerUtil.parseFilter(language, "END_DATE = '" + expectedDateTime + "'");

        Assert.assertNotNull(filter);
        Assert.assertTrue(filter instanceof PropertyIsEqualTo);

        PropertyIsEqualTo eq = (PropertyIsEqualTo) filter;
        Expression exp = eq.getExpression2();
        Assert.assertTrue(exp instanceof Literal);
        Literal actualDateTime = (Literal) exp;

        Assert.assertEquals(expectedDateTime, actualDateTime.toString());
    }

    /** Asserts that the geometries are equals */
    protected void assertEqualsReferencedGeometries(
            final String strGeomExpected, final Geometry actualGeometry, final int expectedSrid)
            throws Exception {
        CoordinateReferenceSystem expectedCRS = CRS.decode("EPSG:" + expectedSrid);
        assertThat(actualGeometry.getUserData(), equalTo(expectedCRS));
        assertEqualsGeometries(strGeomExpected, actualGeometry);
    }

    /** Asserts that the geometries are equals */
    protected void assertEqualsGeometries(
            final String strGeomExpected, final Geometry actualGeometry) throws Exception {

        WKTReader reader = new WKTReader();
        Geometry expectedGeometry;
        expectedGeometry = reader.read(strGeomExpected);

        if (expectedGeometry instanceof GeometryCollection) {
            Assert.assertTrue(expectedGeometry.equalsExact(actualGeometry));
        } else {
            Assert.assertTrue(expectedGeometry.equalsTopo(actualGeometry));
        }
    }
}
