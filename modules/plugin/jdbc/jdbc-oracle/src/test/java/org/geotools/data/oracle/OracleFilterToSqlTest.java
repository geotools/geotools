/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.oracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.Contains;
import org.geotools.api.filter.spatial.Crosses;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.api.filter.spatial.Intersects;
import org.geotools.api.filter.spatial.Overlaps;
import org.geotools.data.jdbc.FilterToSQLException;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.jdbc.JDBCDataStore;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

public class OracleFilterToSqlTest {

    OracleFilterToSQL encoder;

    FilterFactory ff;

    GeometryFactory gf;

    @Before
    public void setUp() throws Exception {
        encoder = new OracleFilterToSQL(null);
        ff = CommonFactoryFinder.getFilterFactory(null);
        gf = new GeometryFactory();
    }

    @Test
    public void testIncludeEncoding() throws Exception {
        // nothing to filter, no WHERE clause
        assertEquals("WHERE 1 = 1", encoder.encodeToString(Filter.INCLUDE));
    }

    @Test
    public void testExcludeEncoding() throws Exception {
        assertEquals("WHERE 0 = 1", encoder.encodeToString(Filter.EXCLUDE));
    }

    @Test
    public void testBboxFilter() throws Exception {
        BBOX bbox = ff.bbox("GEOM", -180, -90, 180, 90, "EPSG:4326");
        String encoded = encoder.encodeToString(bbox);
        assertEquals(
                "WHERE SDO_RELATE(\"GEOM\", ?, 'mask=anyinteract querytype=WINDOW') = 'TRUE' ",
                encoded);
    }

    @Test
    public void testLooseBboxFilter() throws Exception {
        BBOX bbox = ff.bbox("GEOM", -180, -90, 180, 90, "EPSG:4326");
        encoder.setLooseBBOXEnabled(true);
        String encoded = encoder.encodeToString(bbox);
        assertEquals(
                "WHERE SDO_FILTER(\"GEOM\", ?, 'mask=anyinteract querytype=WINDOW') = 'TRUE' ",
                encoded);
    }

    @Test
    public void testContainsFilter() throws Exception {
        Contains contains =
                ff.contains(
                        ff.property("SHAPE"),
                        ff.literal(gf.createPoint(new Coordinate(10.0, -10.0))));
        String encoded = encoder.encodeToString(contains);
        assertEquals(
                "WHERE SDO_RELATE(\"SHAPE\", ?, 'mask=contains querytype=WINDOW') = 'TRUE' ",
                encoded);
    }

    @Test
    public void testCrossesFilter() throws Exception {
        Crosses crosses =
                ff.crosses(
                        ff.property("GEOM"),
                        ff.literal(
                                gf.createLineString(
                                        new Coordinate[] {
                                            new Coordinate(-10.0d, -10.0d), new Coordinate(10d, 10d)
                                        })));
        String encoded = encoder.encodeToString(crosses);
        assertEquals(
                "WHERE SDO_RELATE(\"GEOM\", ?, 'mask=overlapbdydisjoint querytype=WINDOW') = 'TRUE' ",
                encoded);
    }

    @Test
    public void testIntersectsFilter() throws Exception {
        Intersects intersects =
                ff.intersects(
                        ff.property("GEOM"),
                        ff.literal(
                                gf.createLineString(
                                        new Coordinate[] {
                                            new Coordinate(-10.0d, -10.0d), new Coordinate(10d, 10d)
                                        })));
        String encoded = encoder.encodeToString(intersects);
        assertEquals(
                "WHERE SDO_RELATE(\"GEOM\", ?, 'mask=anyinteract querytype=WINDOW') = 'TRUE' ",
                encoded);
    }

    @Test
    public void testOverlapsFilter() throws Exception {
        Overlaps overlaps =
                ff.overlaps(
                        ff.property("GEOM"),
                        ff.literal(
                                gf.createLineString(
                                        new Coordinate[] {
                                            new Coordinate(-10.0d, -10.0d), new Coordinate(10d, 10d)
                                        })));
        String encoded = encoder.encodeToString(overlaps);
        assertEquals(
                "WHERE SDO_RELATE(\"GEOM\", ?, 'mask=overlapbdyintersect querytype=WINDOW') = 'TRUE' ",
                encoded);
    }

    @Test
    public void testDWithinFilterWithUnit() throws Exception {
        Coordinate coordinate = new Coordinate();
        DWithin dwithin =
                ff.dwithin(
                        ff.property("GEOM"),
                        ff.literal(gf.createPoint(coordinate)),
                        10.0,
                        "kilometers");
        String encoded = encoder.encodeToString(dwithin);
        assertEquals(
                "WHERE SDO_WITHIN_DISTANCE(\"GEOM\",?,'distance=10.0 unit=km') = 'TRUE' ", encoded);
    }

    @Test
    public void testDWithinFilterWithUnitEscaping() throws Exception {
        Coordinate coordinate = new Coordinate();
        DWithin dwithin =
                ff.dwithin(
                        ff.property("GEOM"), ff.literal(gf.createPoint(coordinate)), 10.0, "'FOO");
        String encoded = encoder.encodeToString(dwithin);
        assertEquals(
                "WHERE SDO_WITHIN_DISTANCE(\"GEOM\",?,'distance=10.0 unit=''FOO') = 'TRUE' ",
                encoded);
    }

    @Test
    public void testDWithinFilterWithoutUnit() throws Exception {
        Coordinate coordinate = new Coordinate();
        DWithin dwithin =
                ff.dwithin(ff.property("GEOM"), ff.literal(gf.createPoint(coordinate)), 10.0, null);
        String encoded = encoder.encodeToString(dwithin);
        assertEquals("WHERE SDO_WITHIN_DISTANCE(\"GEOM\",?,'distance=10.0') = 'TRUE' ", encoded);
    }

    @Test
    public void testJsonArrayContainsString() throws Exception {
        Function function =
                ff.function(
                        "jsonArrayContains",
                        ff.property("operations"),
                        ff.literal("/operations"),
                        ff.literal("OP1"));
        Filter filter = ff.equals(function, ff.literal(true));
        String encoded = encoder.encodeToString(filter);
        assertEquals("WHERE json_exists(operations, '$.operations?(@ == \"OP1\")')", encoded);
    }

    @Test
    public void testJsonArrayContainsNumber() throws Exception {
        Function function =
                ff.function(
                        "jsonArrayContains",
                        ff.property("operations"),
                        ff.literal("/operations"),
                        ff.literal(1));
        Filter filter = ff.equals(function, ff.literal(true));
        String encoded = encoder.encodeToString(filter);
        assertEquals("WHERE json_exists(operations, '$.operations?(@ == \"1\")')", encoded);
    }

    @Test
    public void testJsonArrayContainsNestedObject() throws Exception {
        Function function =
                ff.function(
                        "jsonArrayContains",
                        ff.property("operations"),
                        ff.literal("/operations/parameters"),
                        ff.literal(1));
        Filter filter = ff.equals(function, ff.literal(true));
        String encoded = encoder.encodeToString(filter);
        assertEquals(
                "WHERE json_exists(operations, '$.operations.parameters?(@ == \"1\")')", encoded);
    }

    @Test
    public void testFunctionJsonArrayContainsEscapingPointer() throws Exception {
        Function function =
                ff.function(
                        "jsonArrayContains",
                        ff.property("operations"),
                        ff.literal("/'FOO"),
                        ff.literal(1));
        Filter filter = ff.equals(function, ff.literal(true));
        String encoded = encoder.encodeToString(filter);
        assertEquals("WHERE json_exists(operations, '$.''FOO?(@ == \"1\")')", encoded);
    }

    @Test
    public void testFunctionJsonArrayContainsEscapingExpected() throws Exception {
        Function function =
                ff.function(
                        "jsonArrayContains",
                        ff.property("operations"),
                        ff.literal("/operations/parameters"),
                        ff.literal("'FOO"));
        Filter filter = ff.equals(function, ff.literal(true));
        String encoded = encoder.encodeToString(filter);
        assertEquals(
                "WHERE json_exists(operations, '$.operations.parameters?(@ == \"''FOO\")')",
                encoded);
    }

    @Test
    public void testFunctionJsonPointer() throws Exception {
        Function function =
                ff.function(
                        "jsonPointer",
                        ff.property("operations"),
                        ff.literal("/operations/parameters"));
        String encoded = encoder.encodeToString(function);
        assertEquals("JSON_VALUE(operations, '$.operations.parameters')", encoded);
    }

    @Test
    public void testFunctionJsonPointerWithArray() throws Exception {
        Function function =
                ff.function(
                        "jsonPointer",
                        ff.property("operations"),
                        ff.literal("/operations/parameters/0"));
        String encoded = encoder.encodeToString(function);
        assertEquals("JSON_VALUE(operations, '$.operations.parameters[0]')", encoded);
    }

    // THIS ONE WON'T PASS RIGHT NOW, BUT WE NEED TO PUT A TEST LIKE THIS
    // SOMEHWERE
    // THAT IS, SOMETHING CHECKING THAT TYPED FIDS GET CONVERTED INTO THE PROPER
    // WHERE CLAUSE
    // public void testFIDEncoding() throws Exception {
    // encoder = new SQLEncoderOracle("FID",new HashMap());
    //
    // Filter filter = filterFactory.createFidFilter("FID.1");
    // String value = encoder.encode(filter);
    // assertEquals("WHERE FID = '1'",value);
    //
    // FidFilter fidFilter = filterFactory.createFidFilter();
    // fidFilter.addFid("FID.1");
    // fidFilter.addFid("FID.3");
    // value = encoder.encode(fidFilter);
    // // depending on the iterator order it may be swapped
    // assertTrue("WHERE FID = '3' OR FID = '1'".equals(value) ||
    // "WHERE FID = '1' OR FID = '3'".equals(value));
    // }

    @Test
    public void testLiteralGeomPoint() throws IOException, FilterToSQLException {
        encoder.setPrepareEnabled(false);
        encoder.setFeatureType(
                buildSimpleFeatureType(
                        "testPolyLiteral",
                        Arrays.asList("testGeometry"),
                        Arrays.asList(Polygon.class)));
        Intersects filter =
                ff.intersects(
                        ff.property("testGeometry"),
                        ff.literal(gf.createPoint(new Coordinate(2, 2))));
        String result = encoder.encodeToString(filter);
        assertEquals(
                "WHERE SDO_RELATE(\"testGeometry\", MDSYS.SDO_GEOMETRY(2001,4326,MDSYS.SDO_POINT_TYPE(2.0,2.0,NULL),NULL,NULL), 'mask=anyinteract querytype=WINDOW') = 'TRUE' ",
                result);
    }

    @Test
    public void testLiteralGeomLine() throws IOException, FilterToSQLException {
        encoder.setPrepareEnabled(false);
        encoder.setFeatureType(
                buildSimpleFeatureType(
                        "testLineLiteral",
                        Arrays.asList("testGeometry"),
                        Arrays.asList(Polygon.class)));
        Intersects filter =
                ff.intersects(
                        ff.property("testGeometry"),
                        ff.literal(
                                gf.createLineString(
                                        new Coordinate[] {
                                            new Coordinate(-10.0d, -10.0d),
                                            new Coordinate(-5.0d, -5.0d),
                                            new Coordinate(5.0d, 5.0d),
                                            new Coordinate(10d, 10d)
                                        })));
        String result = encoder.encodeToString(filter);
        assertEquals(
                "WHERE SDO_RELATE(\"testGeometry\", MDSYS.SDO_GEOMETRY(2002,4326,NULL,MDSYS.SDO_ELEM_INFO_ARRAY(1,2,1),MDSYS.SDO_ORDINATE_ARRAY(-10.0,-10.0,-5.0,-5.0,5.0,5.0,10.0,10.0)), 'mask=anyinteract querytype=WINDOW') = 'TRUE' ",
                result);
    }

    @Test
    public void testLiteralGeomPolygon() throws IOException, FilterToSQLException {
        encoder.setPrepareEnabled(false);
        encoder.setFeatureType(
                buildSimpleFeatureType(
                        "testPolyLiteral",
                        Arrays.asList("testGeometry"),
                        Arrays.asList(Polygon.class)));
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
        String result = encoder.encodeToString(filter);
        assertEquals(
                "WHERE SDO_RELATE(\"testGeometry\", MDSYS.SDO_GEOMETRY(2003,4326,NULL,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,3),MDSYS.SDO_ORDINATE_ARRAY(0.0,0.0,2.0,2.0)), 'mask=anyinteract querytype=WINDOW') = 'TRUE' ",
                result);
    }

    private SimpleFeatureType buildSimpleFeatureType(
            String name, List<String> fields, List<Class<?>> bindings) {
        assumeTrue(fields.size() == bindings.size());
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        for (int i = 0; i < fields.size(); i++) {
            builder.add(fields.get(i), bindings.get(i));
        }
        builder.setName(name);
        SimpleFeatureType ft = builder.buildFeatureType();
        ft.getGeometryDescriptor().getUserData().put(JDBCDataStore.JDBC_NATIVE_SRID, 4326);
        return ft;
    }
}
