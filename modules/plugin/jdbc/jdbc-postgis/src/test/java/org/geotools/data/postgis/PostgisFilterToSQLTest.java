/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.PropertyIsLike;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.BBOX3D;
import org.geotools.api.filter.spatial.Disjoint;
import org.geotools.api.filter.spatial.Intersects;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.data.DataUtilities;
import org.geotools.data.jdbc.FilterToSQLException;
import org.geotools.data.jdbc.SQLFilterTestSupport;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.visitor.PostPreProcessFilterSplittingVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope3D;
import org.geotools.referencing.CRS;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

public class PostgisFilterToSQLTest extends SQLFilterTestSupport {

    private SimpleFeatureType curveSchema;

    private static FilterFactory ff;

    private static GeometryFactory gf = new GeometryFactory();

    private PostGISDialect dialect;

    PostgisFilterToSQL filterToSql;

    StringWriter writer;

    @Override
    @Before
    public void setUp() throws IllegalAttributeException, SchemaException {
        ff = CommonFactoryFinder.getFilterFactory();
        dialect = new PostGISDialect(null);
        filterToSql = new PostgisFilterToSQL(dialect);
        filterToSql.setFunctionEncodingEnabled(true);
        writer = new StringWriter();
        filterToSql.setWriter(writer);

        prepareFeatures();

        curveSchema = DataUtilities.createType(
                "curveIntersection", "name:String,testGeometry:org.geotools.geometry.jts.MultiSurface");
    }

    /**
     * Test for GEOS-5167. Checks that geometries are wrapped with ST_Envelope when used with overlapping operator, when
     * the encodeBBOXFilterAsEnvelope is true.
     */
    @Test
    public void testEncodeBBOXFilterAsEnvelopeEnabled() throws FilterToSQLException {
        filterToSql.setEncodeBBOXFilterAsEnvelope(true);
        filterToSql.setFeatureType(testSchema);

        Intersects filter = ff.intersects(
                ff.property("testGeometry"), ff.literal(gf.createPolygon(gf.createLinearRing(new Coordinate[] {
                    new Coordinate(0, 0),
                    new Coordinate(0, 2),
                    new Coordinate(2, 2),
                    new Coordinate(2, 0),
                    new Coordinate(0, 0)
                }))));
        filterToSql.encode(filter);
        assertTrue(writer.toString().toLowerCase().contains("st_envelope"));
    }

    /**
     * Test for GEOS-5167. Checks that geometries are NOT wrapped with ST_Envelope when used with overlapping operator,
     * when the encodeBBOXFilterAsEnvelope is false.
     */
    @Test
    public void testEncodeBBOXFilterAsEnvelopeDisabled() throws FilterToSQLException {
        filterToSql.setEncodeBBOXFilterAsEnvelope(false);
        filterToSql.setFeatureType(testSchema);

        Intersects filter = ff.intersects(
                ff.property("testGeometry"), ff.literal(gf.createPolygon(gf.createLinearRing(new Coordinate[] {
                    new Coordinate(0, 0),
                    new Coordinate(0, 2),
                    new Coordinate(2, 2),
                    new Coordinate(2, 0),
                    new Coordinate(0, 0)
                }))));
        filterToSql.encode(filter);
        assertFalse(writer.toString().toLowerCase().contains("st_envelope"));
    }

    @Test
    public void testEncodeBBOX3D()
            throws FilterToSQLException, MismatchedDimensionException, NoSuchAuthorityCodeException, FactoryException {
        filterToSql.setFeatureType(testSchema);
        BBOX3D bbox3d = ff.bbox("", new ReferencedEnvelope3D(2, 3, 1, 2, 0, 1, CRS.decode("EPSG:7415")));
        filterToSql.encode(bbox3d);
        String sql = writer.toString().toLowerCase();
        assertEquals("where testgeometry &&& st_makeline(st_makepoint(2.0,1.0,0.0), st_makepoint(3.0,2.0,1.0))", sql);
    }

    @Test
    public void testBBOX3DCapabilities() throws Exception {
        BBOX3D bbox3d = ff.bbox("", new ReferencedEnvelope3D(2, 3, 1, 2, 0, 1, CRS.decode("EPSG:7415")));
        FilterCapabilities caps = filterToSql.getCapabilities();
        PostPreProcessFilterSplittingVisitor splitter =
                new PostPreProcessFilterSplittingVisitor(caps, testSchema, null);
        bbox3d.accept(splitter, null);

        Filter[] split = new Filter[2];
        split[0] = splitter.getFilterPre();
        split[1] = splitter.getFilterPost();

        assertEquals(bbox3d, split[0]);
        assertEquals(Filter.INCLUDE, split[1]);
    }

    @Test
    public void testEncodeInArrayCapabilities() throws Exception {
        filterToSql.setFeatureType(testSchema);
        PropertyIsEqualTo expr =
                ff.equals(ff.function("inArray", ff.literal(5), ff.property("testArray")), ff.literal(true));

        FilterCapabilities caps = filterToSql.getCapabilities();
        PostPreProcessFilterSplittingVisitor splitter =
                new PostPreProcessFilterSplittingVisitor(caps, testSchema, null);
        expr.accept(splitter, null);

        Filter[] split = new Filter[2];
        split[0] = splitter.getFilterPre();
        split[1] = splitter.getFilterPost();

        assertEquals(expr, split[0]);
        assertEquals(Filter.INCLUDE, split[1]);
    }

    @Test
    public void testEncodeInArray() throws Exception {
        filterToSql.setFeatureType(testSchema);
        PropertyIsEqualTo expr =
                ff.equals(ff.function("inArray", ff.literal("5"), ff.property("testArray")), ff.literal(true));

        filterToSql.encode(expr);
        String sql = writer.toString().toLowerCase();
        assertEquals("where 5=any(testarray)", sql);
    }

    @Test
    public void testEncodeInArrayWithCast() throws Exception {
        filterToSql.setFeatureType(testSchema);
        PropertyIsEqualTo expr =
                ff.equals(ff.function("inArray", ff.literal(5), ff.property("testArray")), ff.literal(true));

        filterToSql.encode(expr);
        String sql = writer.toString().toLowerCase();
        assertEquals("where 5::text=any(testarray)", sql);
    }

    @Test
    public void testEncodeEqualToArraysAny() throws Exception {
        filterToSql.setFeatureType(testSchema);
        PropertyIsEqualTo expr = ff.equals(
                ff.function(
                        "equalTo",
                        ff.property("testArray"),
                        ff.literal(new String[] {"1", "2", "3"}),
                        ff.literal("ANY")),
                ff.literal(true));

        filterToSql.encode(expr);
        String sql = writer.toString().toLowerCase();
        assertEquals("where testarray && array['1', '2', '3']", sql);
    }

    @Test
    public void testEncodeEqualToArraysAll() throws Exception {
        filterToSql.setFeatureType(testSchema);
        PropertyIsEqualTo expr = ff.equals(
                ff.function(
                        "equalTo",
                        ff.property("testArray"),
                        ff.literal(new String[] {"1", "2", "3"}),
                        ff.literal("ALL")),
                ff.literal(true));

        filterToSql.encode(expr);
        String sql = writer.toString().toLowerCase();
        assertEquals("where testarray = array['1', '2', '3']", sql);
    }

    @Test
    public void testFunctionStrEndsWithEscaping() throws Exception {
        filterToSql.setFeatureType(testSchema);
        Filter filter =
                ff.equals(ff.literal(true), ff.function("strEndsWith", ff.property("testString"), ff.literal("'FOO")));
        filterToSql.encode(filter);
        String sql = writer.toString();
        assertEquals("WHERE true = (testString LIKE ('%' || '''FOO'))", sql);
    }

    @Test
    public void testFunctionStrStartsWithEscaping() throws Exception {
        filterToSql.setFeatureType(testSchema);
        Filter filter = ff.equals(
                ff.literal(true), ff.function("strStartsWith", ff.property("testString"), ff.literal("'FOO")));
        filterToSql.encode(filter);
        String sql = writer.toString();
        assertEquals("WHERE true = (testString LIKE ('''FOO' || '%'))", sql);
    }

    @Test
    public void testFunctionLike() throws Exception {
        filterToSql.setFeatureType(testSchema);
        PropertyIsLike like =
                ff.like(ff.function("strToLowerCase", ff.property("testString")), "a_literal", "%", "-", "\\", true);

        filterToSql.encode(like);
        String sql = writer.toString().toLowerCase().trim();
        assertEquals("where lower(teststring) like 'a_literal'", sql);
    }

    @Test
    public void testFunctionJsonPointer() throws Exception {
        filterToSql.setFeatureType(testSchema);
        Function pointer = ff.function("jsonPointer", ff.property("testJSON"), ff.literal("/arr/0"));

        filterToSql.encode(pointer);
        String sql = writer.toString().toLowerCase().trim();
        assertEquals("testjson ::json  -> 'arr' ->> 0", sql);
    }

    @Test
    public void testFunctionJsonPointerWithNumericObjectKey() throws Exception {
        filterToSql.setFeatureType(testSchema);
        Function pointer = ff.function("jsonPointer", ff.property("testjson"), ff.literal("/'1407'"));
        Filter startsWith = ff.equals(pointer, ff.literal("value"));
        filterToSql.encode(startsWith);
        String sql = writer.toString().toLowerCase().trim();
        assertEquals("where testjson ::json  ->> '1407' = 'value'", sql);
    }

    @Test
    public void testFunctionJsonPointerWithApostropheKey() throws Exception {
        filterToSql.setFeatureType(testSchema);
        Function pointer = ff.function("jsonPointer", ff.property("testjson"), ff.literal("/'"));
        Filter startsWith = ff.equals(pointer, ff.literal("escaped_apostrophe"));
        filterToSql.encode(startsWith);
        String sql = writer.toString().toLowerCase().trim();
        assertEquals("where testjson ::json  ->> '''' = 'escaped_apostrophe'", sql);
    }

    @Test
    public void testFunctionJsonPointerWithUnescapedNumericKey() throws Exception {
        filterToSql.setFeatureType(testSchema);
        Function pointer = ff.function("jsonPointer", ff.property("testjson"), ff.literal("/'2202"));
        Filter startsWith = ff.equals(pointer, ff.literal("escaped_literal"));
        filterToSql.encode(startsWith);
        String sql = writer.toString().toLowerCase().trim();
        assertEquals("where testjson ::json  ->> '''2202' = 'escaped_literal'", sql);
    }

    @Test
    public void testFunctionJsonPointerWithComplexPath() throws Exception {
        filterToSql.setFeatureType(testSchema);
        Function pointer =
                ff.function("jsonPointer", ff.property("testjson"), ff.literal("/numeric_key/'0714'/array_index/1407"));
        Filter startsWith = ff.equals(pointer, ff.literal("complex_pointer"));
        filterToSql.encode(startsWith);
        String sql = writer.toString().toLowerCase().trim();
        assertEquals(
                "where testjson ::json  -> 'numeric_key' -> '0714' -> 'array_index' ->> 1407 = 'complex_pointer'", sql);
    }

    @Test
    public void testBinaryComparisonWithJsonPointer() throws Exception {
        filterToSql.setFeatureType(testSchema);
        Function pointer = ff.function("jsonPointer", ff.property("testJSON"), ff.literal("/arr/0"));
        Expression literal = ff.literal(3);
        Filter less = ff.less(pointer, literal);
        filterToSql.encode(less);
        String sql = writer.toString().toLowerCase().trim();
        assertEquals("where (testjson ::json  -> 'arr' ->> 0)::integer < 3", sql);
    }

    @Test
    public void testLikeWithJsonPointer() throws Exception {
        // test that encoding not fails with NPE for LIKE
        // when is specified an expression as parameter with Object as return type
        filterToSql.setFeatureType(testSchema);
        Function pointer = ff.function("jsonPointer", ff.property("testJSON"), ff.literal("/arr/0"));
        Filter like = ff.like(pointer, "a_literal", "%", "-", "\\", true);
        filterToSql.encode(like);
        String sql = writer.toString().toLowerCase().trim();
        assertEquals("where testjson ::json  -> 'arr' ->> 0 like 'a_literal'", sql);
    }

    @Test
    public void testFunctionJsonArrayContains() throws Exception {
        filterToSql.setFeatureType(testSchema);
        Function pointer = ff.function(
                "jsonArrayContains", ff.property("OPERATIONS"), ff.literal("/operations"), ff.literal("OP1"));
        filterToSql.encode(pointer);
        String sql = writer.toString().trim();
        assertEquals("OPERATIONS::jsonb @> '{ \"operations\": [\"OP1\"] }'::jsonb", sql);
    }

    @Test
    public void testFunctionJsonArrayContainsNumber() throws Exception {
        filterToSql.setFeatureType(testSchema);
        Function pointer =
                ff.function("jsonArrayContains", ff.property("OPERATIONS"), ff.literal("/operations"), ff.literal(1));
        filterToSql.encode(pointer);
        String sql = writer.toString().trim();
        assertEquals("OPERATIONS::jsonb @> '{ \"operations\": [1] }'::jsonb", sql);
    }

    @Test
    public void testNestedObjectJsonArrayContains() throws Exception {
        filterToSql.setFeatureType(testSchema);
        Function pointer = ff.function(
                "jsonArrayContains", ff.property("OPERATIONS"), ff.literal("/operations/parameters"), ff.literal("P1"));
        filterToSql.encode(pointer);
        String sql = writer.toString().trim();
        assertEquals("OPERATIONS::jsonb @> '{ \"operations\": { \"parameters\": [\"P1\"] } }'::jsonb", sql);
    }

    @Test
    public void testFunctionJsonArrayContainsEscapingPointer() throws Exception {
        filterToSql.setFeatureType(testSchema);
        Function pointer =
                ff.function("jsonArrayContains", ff.property("OPERATIONS"), ff.literal("/\"'FOO"), ff.literal("OP1"));
        filterToSql.encode(pointer);
        String sql = writer.toString().trim();
        assertEquals("OPERATIONS::jsonb @> '{ \"\\\"''FOO\": [\"OP1\"] }'::jsonb", sql);
    }

    @Test
    public void testFunctionJsonArrayContainsEscapingExpected() throws Exception {
        filterToSql.setFeatureType(testSchema);
        Function pointer = ff.function(
                "jsonArrayContains", ff.property("OPERATIONS"), ff.literal("/operations"), ff.literal("\"'FOO"));
        filterToSql.encode(pointer);
        String sql = writer.toString().trim();
        assertEquals("OPERATIONS::jsonb @> '{ \"operations\": [\"\\\"''FOO\"] }'::jsonb", sql);
    }

    @Test
    public void testFlatIntersectionImplementation() throws Exception {
        Intersects intersects =
                ff.intersects(ff.property("testGeometry"), ff.literal(gf.createPoint(new Coordinate(0, 0))));

        // encode plain
        filterToSql.setFeatureType(testSchema);
        filterToSql.encode(intersects);
        assertEquals(
                "WHERE testGeometry && ST_GeomFromText('POINT (0 0)', ST_SRID(testGeometry)) AND ST_Intersects(testGeometry, ST_GeomFromText('POINT (0 0)', ST_SRID(testGeometry)))",
                writer.toString());
    }

    @Test
    public void testCurveIntersectionImplementation() throws Exception {
        Intersects intersects =
                ff.intersects(ff.property("testGeometry"), ff.literal(gf.createPoint(new Coordinate(0, 0))));

        // encode plain
        filterToSql.setFeatureType(curveSchema);
        filterToSql.encode(intersects);
        assertEquals(
                "WHERE testGeometry && ST_GeomFromText('POINT (0 0)', ST_SRID(testGeometry)) AND ST_Distance(testGeometry, ST_GeomFromText('POINT (0 0)', ST_SRID(testGeometry))) = 0",
                writer.toString());
    }

    @Test
    public void testFlatDisjointImplementation() throws Exception {
        Disjoint disjoint = ff.disjoint(ff.property("testGeometry"), ff.literal(gf.createPoint(new Coordinate(0, 0))));

        // encode plain
        filterToSql.setFeatureType(testSchema);
        filterToSql.encode(disjoint);
        assertEquals(
                "WHERE NOT (ST_Intersects(testGeometry, ST_GeomFromText('POINT (0 0)', ST_SRID(testGeometry))))",
                writer.toString());
    }

    @Test
    public void testCurveDisjointImplementation() throws Exception {
        Disjoint disjoint = ff.disjoint(ff.property("testGeometry"), ff.literal(gf.createPoint(new Coordinate(0, 0))));

        // encode plain
        filterToSql.setFeatureType(curveSchema);
        filterToSql.encode(disjoint);
        assertEquals(
                "WHERE ST_Distance(testGeometry, ST_GeomFromText('POINT (0 0)', ST_SRID(testGeometry))) > 0",
                writer.toString());
    }

    @Test
    public void testFlatBBOXImplementation() throws Exception {
        BBOX bbox = ff.bbox(ff.property("testGeometry"), 0, 0, 1, 1, "EPSG:4326");

        // encode plain
        filterToSql.setFeatureType(testSchema);
        filterToSql.encode(bbox);
        assertEquals(
                "WHERE testGeometry && ST_GeomFromText('POLYGON ((0 0, 0 1, 1 1, 1 0, 0 0))', ST_SRID(testGeometry)) AND ST_Intersects(testGeometry, ST_GeomFromText('POLYGON ((0 0, 0 1, 1 1, 1 0, 0 0))', ST_SRID(testGeometry)))",
                writer.toString());
    }

    @Test
    public void testCurveBBOXImplementation() throws Exception {
        BBOX bbox = ff.bbox(ff.property("testGeometry"), 0, 0, 1, 1, "EPSG:4326");

        // encode plain
        filterToSql.setFeatureType(curveSchema);
        filterToSql.encode(bbox);
        assertEquals(
                "WHERE testGeometry && ST_GeomFromText('POLYGON ((0 0, 0 1, 1 1, 1 0, 0 0))', ST_SRID(testGeometry)) AND ST_Distance(testGeometry, ST_GeomFromText('POLYGON ((0 0, 0 1, 1 1, 1 0, 0 0))', ST_SRID(testGeometry))) = 0",
                writer.toString());
    }

    @Test
    public void testNumberIsCastToText() throws Exception {
        filterToSql.setFeatureType(testSchema);

        testAttribute("testByte", "*12", "WHERE testByte::text LIKE '*12'");

        testAttribute("testShort", "*123", "WHERE testShort::text LIKE '*123'");

        testAttribute("testInteger", "123*", "WHERE testInteger::text LIKE '123*'");

        testAttribute("testLong", "*123*", "WHERE testLong::text LIKE '*123*'");

        testAttribute("testFloat", "1234*", "WHERE testFloat::text LIKE '1234*'");

        testAttribute("testDouble", "1234", "WHERE testDouble::text LIKE '1234'");
    }

    private void testAttribute(String attributeName, String pattern, String expectedExpression)
            throws FilterToSQLException {
        Filter filter = ff.like(ff.property(attributeName), pattern, "%", "-", "\\", true);
        filterToSql.encode(filter);

        Assert.assertEquals(expectedExpression, writer.toString());
        // Clear the buffer to reuse it
        writer.getBuffer().setLength(0);
    }
}
