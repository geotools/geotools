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

import java.io.StringWriter;
import org.geotools.data.jdbc.FilterToSQLException;
import org.geotools.data.jdbc.SQLFilterTestSupport;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.visitor.PostPreProcessFilterSplittingVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope3D;
import org.geotools.referencing.CRS;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.spatial.BBOX3D;
import org.opengis.filter.spatial.Intersects;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

public class PostgisFilterToSQLTest extends SQLFilterTestSupport {

    public PostgisFilterToSQLTest(String name) {
        super(name);
    }

    private static FilterFactory2 ff;

    private static GeometryFactory gf = new GeometryFactory();

    private PostGISDialect dialect;

    PostgisFilterToSQL filterToSql;

    StringWriter writer;

    @Before
    public void setUp() throws IllegalAttributeException, SchemaException {
        ff = CommonFactoryFinder.getFilterFactory2();
        dialect = new PostGISDialect(null);
        filterToSql = new PostgisFilterToSQL(dialect);
        filterToSql.setFunctionEncodingEnabled(true);
        writer = new StringWriter();
        filterToSql.setWriter(writer);

        prepareFeatures();
    }

    /**
     * Test for GEOS-5167. Checks that geometries are wrapped with ST_Envelope when used with
     * overlapping operator, when the encodeBBOXFilterAsEnvelope is true.
     */
    @Test
    public void testEncodeBBOXFilterAsEnvelopeEnabled() throws FilterToSQLException {
        filterToSql.setEncodeBBOXFilterAsEnvelope(true);
        filterToSql.setFeatureType(testSchema);

        Intersects filter =
                ff.intersects(
                        ff.property("testGeometry"),
                        ff.literal(
                                gf.createPolygon(
                                        gf.createLinearRing(
                                                new Coordinate[] {
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
     * Test for GEOS-5167. Checks that geometries are NOT wrapped with ST_Envelope when used with
     * overlapping operator, when the encodeBBOXFilterAsEnvelope is false.
     */
    @Test
    public void testEncodeBBOXFilterAsEnvelopeDisabled() throws FilterToSQLException {
        filterToSql.setEncodeBBOXFilterAsEnvelope(false);
        filterToSql.setFeatureType(testSchema);

        Intersects filter =
                ff.intersects(
                        ff.property("testGeometry"),
                        ff.literal(
                                gf.createPolygon(
                                        gf.createLinearRing(
                                                new Coordinate[] {
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
            throws FilterToSQLException, MismatchedDimensionException, NoSuchAuthorityCodeException,
                    FactoryException {
        filterToSql.setFeatureType(testSchema);
        BBOX3D bbox3d =
                ff.bbox("", new ReferencedEnvelope3D(2, 3, 1, 2, 0, 1, CRS.decode("EPSG:7415")));
        filterToSql.encode(bbox3d);
        String sql = writer.toString().toLowerCase();
        assertEquals(
                "where testgeometry &&& st_makeline(st_makepoint(2.0,1.0,0.0), st_makepoint(3.0,2.0,1.0))",
                sql);
    }

    @Test
    public void testBBOX3DCapabilities() throws Exception {
        BBOX3D bbox3d =
                ff.bbox("", new ReferencedEnvelope3D(2, 3, 1, 2, 0, 1, CRS.decode("EPSG:7415")));
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
                ff.equals(
                        ff.function("inArray", ff.literal(5), ff.property("testArray")),
                        ff.literal(true));

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
                ff.equals(
                        ff.function("inArray", ff.literal("5"), ff.property("testArray")),
                        ff.literal(true));

        filterToSql.encode(expr);
        String sql = writer.toString().toLowerCase();
        assertEquals("where 5=any(testarray)", sql);
    }

    @Test
    public void testEncodeInArrayWithCast() throws Exception {
        filterToSql.setFeatureType(testSchema);
        PropertyIsEqualTo expr =
                ff.equals(
                        ff.function("inArray", ff.literal(5), ff.property("testArray")),
                        ff.literal(true));

        filterToSql.encode(expr);
        String sql = writer.toString().toLowerCase();
        assertEquals("where 5::text=any(testarray)", sql);
    }

    @Test
    public void testFunctionLike() throws Exception {
        filterToSql.setFeatureType(testSchema);
        PropertyIsLike like =
                ff.like(
                        ff.function("strToLowerCase", ff.property("testString")),
                        "a_literal",
                        "%",
                        "-",
                        "\\",
                        true);

        filterToSql.encode(like);
        String sql = writer.toString().toLowerCase().trim();
        assertEquals("where lower(teststring) like 'a_literal'", sql);
    }
}
