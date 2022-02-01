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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

import org.geootols.filter.text.cql_2.CQL2;
import org.geotools.filter.text.cql2.CQLException;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;
import org.opengis.filter.Filter;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;

/**
 * Fully copied instead of inherited, as the base tests are too dirty (parse filters as an excuse to
 * parse an expression, do encoding tests as well).
 *
 * <p>Changes compared to (E)CQL:
 *
 * <ul>
 *   <li>Simple time literals are not allowed in CQL2
 *   <li>RELATE is gone
 *   <li>ENVELOPE axis order is different
 * </ul>
 */
public class CQL2LiteralTest {

    @Test
    public void lineString() throws Exception {
        String wkt = "LINESTRING (1 2, 3 4)";
        assertParseGeometry(wkt, LineString.class);
    }

    @Test
    public void point() throws Exception {
        String wkt = "POINT(1 2)";
        assertParseGeometry(wkt, Point.class);
    }

    @Test
    public void polygon() throws Exception {
        String wkt = "POLYGON((1 2, 15 2, 15 20, 15 21, 1 2))";
        assertParseGeometry(wkt, Polygon.class);
    }

    @Test
    public void polygonWithHole() throws Exception {
        String wkt =
                "POLYGON ((40 60, 420 60, 420 320, 40 320, 40 60), (200 140, 160 220, 260 200, 200 140))";
        assertParseGeometry(wkt, Polygon.class);
    }

    @Test
    public void multiPoint() throws Exception {
        String wkt = "MULTIPOINT( (1 2), (15 2), (15 20), (15 21), (1 2))";
        String expectedWkt = "MULTIPOINT(1 2, 15 2, 15 20, 15 21, 1 2)";
        assertParseGeometry(wkt, expectedWkt, MultiPoint.class);
    }

    @Test
    public void multiLineString() throws Exception {
        String wkt = "MULTILINESTRING((10 10, 20 20),(15 15,30 15))";
        assertParseGeometry(wkt, MultiLineString.class);
    }

    @Test
    public void geometryCollection() throws Exception {
        String wkt = "GEOMETRYCOLLECTION (POINT (10 10),POINT (30 30),LINESTRING (15 15, 20 20))";
        assertParseGeometry(wkt, GeometryCollection.class);
    }

    @Test
    public void multiPolygon() throws Exception {
        String wkt =
                "MULTIPOLYGON( ((10 10, 10 20, 20 20, 20 15, 10 10)),((60 60, 70 70, 80 60, 60 60 )) )";
        assertParseGeometry(wkt, MultiPolygon.class);
    }

    private void assertParseGeometry(String wkt, Class<? extends Geometry> type) throws Exception {
        assertParseGeometry(wkt, wkt, type);
        assertParseGeometry(wkt, wkt, type);
    }

    private void assertParseGeometry(String wkt, String expectedWkt, Class expectedGeometryClass)
            throws Exception {
        Expression expression = CQL2.toExpression(wkt);

        assertThat(expression, instanceOf(Literal.class));
        Literal literal = (Literal) expression;
        Object actualGeometry = literal.getValue();
        assertThat(actualGeometry, instanceOf(expectedGeometryClass));

        assertEqualsGeometries(expectedWkt, (Geometry) actualGeometry);
    }

    /** Asserts that the geometries are equals */
    protected void assertEqualsGeometries(
            final String strGeomExpected, final Geometry actualGeometry) throws Exception {

        WKTReader reader = new WKTReader();
        Geometry expectedGeometry = reader.read(strGeomExpected);

        if (expectedGeometry instanceof GeometryCollection) {
            Assert.assertTrue(expectedGeometry.equalsExact(actualGeometry));
        } else {
            Assert.assertTrue(expectedGeometry.equalsTopo(actualGeometry));
        }
    }

    // Axis order changed since CQL/ECQL, currently (west,south,east,north)
    @Test
    public void testEnvelope() throws Exception {
        Expression expression = CQL2.toExpression("ENVELOPE(10, 20, 30, 40)");
        assertThat(expression, instanceOf(Literal.class));
        Literal literal = (Literal) expression;
        Object actualGeometry = literal.getValue();
        assertThat(actualGeometry, instanceOf(Polygon.class));

        Polygon polygon = (Polygon) actualGeometry;
        assertEqualsGeometries("POLYGON ((10 20, 10 40, 30 40, 30 20, 10 20))", polygon);
    }

    @Test(expected = CQLException.class) // EWKT not supported
    public void testParseInvalidSRID() throws Exception {
        CQL2.toExpression("SRID=12345678987654321;POINT(1 2)");
    }

    /** Test error at geometry literal */
    @Test(expected = CQLException.class)
    public void geometryLiteralsError() throws CQLException {
        CQL2.toExpression("POLYGON(1 2, 10 15), (10 15, 1 2)))");
    }

    @Test
    public void characterStringLiteral() throws Exception {

        // space check
        final String strWithSpace = "ALL PRACTICES";
        Filter filterWithSpace = CQL2.toFilter("practice='" + strWithSpace + "'");
        Assert.assertNotNull(filterWithSpace);
        Assert.assertTrue(filterWithSpace instanceof PropertyIsEqualTo);

        PropertyIsEqualTo eqFilter = (PropertyIsEqualTo) filterWithSpace;
        Expression spacesLiteral = eqFilter.getExpression2();
        Assert.assertEquals(strWithSpace, spacesLiteral.toString());

        // empty string ''
        Filter emptyFilter = CQL2.toFilter("MAJOR_WATERSHED_SYSTEM = ''");

        Assert.assertNotNull(emptyFilter);
        Assert.assertTrue(emptyFilter instanceof PropertyIsEqualTo);

        eqFilter = (PropertyIsEqualTo) emptyFilter;
        Expression emptyLiteral = eqFilter.getExpression2();
        Assert.assertEquals("", emptyLiteral.toString());

        // character string without quote
        final String expectedWithout = "ab";

        Filter filterWithoutQuote =
                CQL2.toFilter("MAJOR_WATERSHED_SYSTEM = '" + expectedWithout + "'");

        Assert.assertNotNull(filterWithoutQuote);
        Assert.assertTrue(filterWithoutQuote instanceof PropertyIsEqualTo);

        eqFilter = (PropertyIsEqualTo) filterWithoutQuote;
        Expression actualWhithoutQuote = eqFilter.getExpression2();
        Assert.assertEquals(expectedWithout, actualWhithoutQuote.toString());

        // <quote symbol>
        final String expected = "cde'' fg";

        Filter filter = CQL2.toFilter("MAJOR_WATERSHED_SYSTEM = '" + expected + "'");

        Assert.assertNotNull(filter);
        Assert.assertTrue(filter instanceof PropertyIsEqualTo);

        eqFilter = (PropertyIsEqualTo) filter;
        Expression actual = eqFilter.getExpression2();
        Assert.assertEquals(expected.replaceAll("''", "'"), actual.toString());

        // special characters
        final String otherChars = "üä";

        filter = CQL2.toFilter("NAME = '" + otherChars + "'");

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
        Expression actual = CQL2.toExpression("'" + str + "'");
        Assert.assertEquals(str, actual.toString());
    }

    @Test
    public void doubleLiteral() throws Exception {

        final String expected = "4.20082008E4";

        Expression expr = CQL2.toExpression(expected);

        Literal doubleLiteral = (Literal) expr;
        Double actual = (Double) doubleLiteral.getValue();

        Assert.assertEquals(Double.parseDouble(expected), actual.doubleValue(), 8);
    }

    @Test
    public void longLiteral() throws Exception {

        {
            final String expected = "123456789012345";

            Expression expr = CQL2.toExpression(expected);

            Literal intLiteral = (Literal) expr;
            Long actual = (Long) intLiteral.getValue();

            Assert.assertEquals(Long.parseLong(expected), actual.longValue());
        }

        {
            final String maxLongValue = "9223372036854775807";

            Expression expr = CQL2.toExpression(maxLongValue);

            Literal intLiteral = (Literal) expr;
            Long actual = (Long) intLiteral.getValue();

            Assert.assertEquals(Long.parseLong(maxLongValue), actual.longValue());
        }
    }

    @Test
    public void dateLiteral() throws Exception {
        String expectedDate = "2009-08-01"; // mind, encoded as a string literal!
        Filter filter = CQL2.toFilter("END_DATE = '" + expectedDate + "'");

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
        // as a character literal, not as an instant literal
        final String expectedTime = "12:08:01";
        Filter filter = CQL2.toFilter("END_TIME = '" + expectedTime + "'");

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
        // encoded as a character literal!
        final String expectedDateTime = "2009-08-01T12:08:01";
        Filter filter = CQL2.toFilter("END_DATE = '" + expectedDateTime + "'");

        Assert.assertNotNull(filter);
        Assert.assertTrue(filter instanceof PropertyIsEqualTo);

        PropertyIsEqualTo eq = (PropertyIsEqualTo) filter;
        Expression exp = eq.getExpression2();
        Assert.assertTrue(exp instanceof Literal);
        Literal actualDateTime = (Literal) exp;

        Assert.assertEquals(expectedDateTime, actualDateTime.toString());
    }
}
