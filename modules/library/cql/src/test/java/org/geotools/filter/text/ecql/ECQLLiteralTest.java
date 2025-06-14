/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.spatial.Beyond;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.api.filter.spatial.Intersects;
import org.geotools.api.referencing.FactoryException;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.commons.CompilerUtil;
import org.geotools.filter.text.commons.ExpressionToText;
import org.geotools.filter.text.commons.Language;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.cql2.CQLLiteralTest;
import org.geotools.referencing.CRS;
import org.geotools.util.factory.Hints;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * Literal Test Cases
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 */
public class ECQLLiteralTest extends CQLLiteralTest {

    public static final int WGS84 = 4326;
    private FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    public ECQLLiteralTest() {
        super(Language.ECQL);
    }

    /** Test for LineString Expression Sample: LINESTRING( 1 2, 3 4) */
    @Test
    public void lineString() throws Exception {
        String wkt = "LINESTRING (1 2, 3 4)";
        assertParseReferencedAndUnreferenced(wkt, LineString.class);
    }

    /** Sample: POINT(1 2) */
    @Test
    public void point() throws Exception {
        String wkt = "POINT(1 2)";
        assertParseReferencedAndUnreferenced(wkt, Point.class);
    }

    /** Sample: POLYGON((1 2, 15 2, 15 20, 15 21, 1 2)) */
    @Test
    public void polygon() throws Exception {
        String wkt = "POLYGON((1 2, 15 2, 15 20, 15 21, 1 2))";
        assertParseReferencedAndUnreferenced(wkt, Polygon.class);
    }

    /** Sample: POLYGON ((40 60, 420 60, 420 320, 40 320, 40 60), (200 140, 160 220, 260 200, 200 140)) */
    @Test
    public void polygonWithHole() throws Exception {
        String wkt = "POLYGON ((40 60, 420 60, 420 320, 40 320, 40 60), (200 140, 160 220, 260 200, 200 140))";
        assertParseReferencedAndUnreferenced(wkt, Polygon.class);
    }

    /** Sample: MULTIPOINT( (1 2), (15 2), (15 20), (15 21), (1 2) )) */
    @Test
    public void multiPoint() throws Exception {
        String wkt = "MULTIPOINT( (1 2), (15 2), (15 20), (15 21), (1 2))";
        String expectedWkt = "MULTIPOINT(1 2, 15 2, 15 20, 15 21, 1 2)";
        assertParseReferencedAndUnreferenced(wkt, expectedWkt, MultiPoint.class, null);
        assertParseReferencedAndUnreferenced(wkt, expectedWkt, MultiPoint.class, WGS84);
    }

    /** Sample: MULTILINESTRING((10 10, 20 20),(15 15,30 15)) */
    @Test
    public void multiLineString() throws Exception {
        String wkt = "MULTILINESTRING((10 10, 20 20),(15 15,30 15))";
        assertParseReferencedAndUnreferenced(wkt, MultiLineString.class);
    }

    /** sample: CROSS(ATTR1, GEOMETRYCOLLECTION (POINT (10 10),POINT (30 30),LINESTRING (15 15, 20 20)) ) */
    @Test
    public void geometryCollection() throws Exception {
        String wkt = "GEOMETRYCOLLECTION (POINT (10 10),POINT (30 30),LINESTRING (15 15, 20 20))";
        assertParseReferencedAndUnreferenced(wkt, GeometryCollection.class);
    }

    /** Sample: MULTIPOLYGON( ((10 10, 10 20, 20 20, 20 15, 10 10)),((60 60, 70 70, 80 60, 60 60 )) ) */
    @Test
    public void multiPolygon() throws Exception {
        String wkt = "MULTIPOLYGON( ((10 10, 10 20, 20 20, 20 15, 10 10)),((60 60, 70 70, 80 60, 60 60 )) )";
        assertParseReferencedAndUnreferenced(wkt, MultiPolygon.class);
    }

    private void assertParseReferencedAndUnreferenced(String wkt, Class<? extends Geometry> type) throws Exception {
        assertParseReferencedAndUnreferenced(wkt, type, null);
        assertParseReferencedAndUnreferenced(wkt, type, WGS84);
    }

    private void assertParseReferencedAndUnreferenced(String wkt, Class expectedGeometryClass, Integer srid)
            throws Exception {
        assertParseReferencedAndUnreferenced(wkt, wkt, expectedGeometryClass, srid);
    }

    private void assertParseReferencedAndUnreferenced(
            String wkt, String expectedWkt, Class expectedGeometryClass, Integer srid) throws Exception {
        String sridPrefix = srid != null ? "SRID=" + srid + ";" : "";
        Expression expression = CompilerUtil.parseExpression(language, sridPrefix + wkt);

        assertThat(expression, instanceOf(Literal.class));
        Literal literal = (Literal) expression;
        Object actualGeometry = literal.getValue();
        assertThat(actualGeometry, instanceOf(expectedGeometryClass));

        if (srid != null) {
            assertEqualsReferencedGeometries(expectedWkt, (Geometry) actualGeometry, WGS84);
        } else {
            assertEqualsGeometries(expectedWkt, (Geometry) actualGeometry);
        }
    }

    @Test()
    public void testParseInvalidSRID() throws Exception {
        String wkt = "SRID=12345678987654321;POINT(1 2)";
        try {
            CompilerUtil.parseExpression(language, wkt);
        } catch (CQLException e) {
            assertThat(
                    e.getMessage(), allOf(containsString("Failed to build CRS"), containsString("12345678987654321")));
        }
    }

    @Test
    public void testEncodeEWKTControlOnExpression() throws Exception {
        Literal literalGeometry = getWgs84PointLiteral();

        String cql1 = expressionToText(literalGeometry, false);
        assertEquals("POINT (1 2)", cql1);

        String cql2 = expressionToText(literalGeometry, true);
        assertEquals("SRID=4326;POINT (1 2)", cql2);
    }

    @Test
    public void testEncodeEWKTControlOnIntersects() throws Exception {
        Literal literalGeometry = getWgs84PointLiteral();
        Intersects intersects = ff.intersects(ff.property("the_geom"), literalGeometry);

        String cql1 = filterToText(intersects, false);
        assertEquals("INTERSECTS(the_geom, POINT (1 2))", cql1);

        String cql2 = filterToText(intersects, true);
        assertEquals("INTERSECTS(the_geom, SRID=4326;POINT (1 2))", cql2);
    }

    @Test
    public void testEncodeEWKTControlOnIntersectsWithHints() throws Exception {
        Literal literalGeometry = getWgs84PointLiteral();
        Intersects intersects = ff.intersects(ff.property("the_geom"), literalGeometry);

        // disable the hints
        Hints.putSystemDefault(Hints.ENCODE_EWKT, false);
        try {
            String cql1 = ECQL.toCQL(intersects);
            assertEquals("INTERSECTS(the_geom, POINT (1 2))", cql1);
        } finally {
            Hints.putSystemDefault(Hints.ENCODE_EWKT, true);
        }

        String cql2 = filterToText(intersects, true);
        assertEquals("INTERSECTS(the_geom, SRID=4326;POINT (1 2))", cql2);
    }

    @Test
    public void testEncodeEWKTControlOnDWithin() throws Exception {
        Literal literalGeometry = getWgs84PointLiteral();
        DWithin dwithin = ff.dwithin(ff.property("the_geom"), literalGeometry, 10, "m");

        String cql1 = filterToText(dwithin, false);
        assertEquals("DWITHIN(the_geom, POINT (1 2), 10.0, m)", cql1);

        String cql2 = filterToText(dwithin, true);
        assertEquals("DWITHIN(the_geom, SRID=4326;POINT (1 2), 10.0, m)", cql2);
    }

    @Test
    public void testEncodeEWKTControlOnBeyond() throws Exception {
        Literal literalGeometry = getWgs84PointLiteral();
        Beyond beyond = ff.beyond(ff.property("the_geom"), literalGeometry, 10, "m");

        String cql1 = filterToText(beyond, false);
        assertEquals("BEYOND(the_geom, POINT (1 2), 10.0, m)", cql1);

        String cql2 = filterToText(beyond, true);
        assertEquals("BEYOND(the_geom, SRID=4326;POINT (1 2), 10.0, m)", cql2);
    }

    private Literal getWgs84PointLiteral() throws FactoryException {
        Point p = new GeometryFactory().createPoint(new Coordinate(1, 2));
        p.setUserData(CRS.decode("EPSG:4326", true));
        return ff.literal(p);
    }

    private String expressionToText(Literal literalGeometry, boolean encodeEWKT) {
        ExpressionToText encoder = new ExpressionToText(encodeEWKT);
        StringBuilder sb = new StringBuilder();
        literalGeometry.accept(encoder, sb);
        return sb.toString();
    }

    private String filterToText(Filter filter, boolean encodeEWKT) {
        FilterToECQL encoder = new FilterToECQL(encodeEWKT);
        StringBuilder sb = new StringBuilder();
        filter.accept(encoder, sb);
        return sb.toString();
    }
}
