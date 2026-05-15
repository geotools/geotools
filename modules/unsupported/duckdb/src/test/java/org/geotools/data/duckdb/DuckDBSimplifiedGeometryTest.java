/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.duckdb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.util.factory.Hints;
import org.junit.Assume;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

/** Non-online simplification regression tests adapted from PostGIS simplified geometry coverage. */
public class DuckDBSimplifiedGeometryTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder(new File("target"));

    @Test
    public void testPointSimplificationKeepsCoordinate() throws Exception {
        JDBCDataStore store = createSeededStore();
        try {
            Geometry simplified = getSimplifiedGeometry(store, "simplify_point", 10d);
            assertTrue(simplified instanceof Point);
            assertEquals(-120d, ((Point) simplified).getX(), 0d);
            assertEquals(40d, ((Point) simplified).getY(), 0d);
        } finally {
            store.dispose();
        }
    }

    @Test
    public void testLineSimplificationRemovesMiddleVertex() throws Exception {
        JDBCDataStore store = createSeededStore();
        try {
            Geometry simplified = getSimplifiedGeometry(store, "simplify_line", 20d);
            assertTrue(simplified instanceof LineString);
            LineString line = (LineString) simplified;
            assertEquals(2, line.getNumPoints());
            assertEquals(-120d, line.getCoordinateN(0).x, 0d);
            assertEquals(40d, line.getCoordinateN(0).y, 0d);
            assertEquals(-140d, line.getCoordinateN(1).x, 0d);
            assertEquals(60d, line.getCoordinateN(1).y, 0d);
        } finally {
            store.dispose();
        }
    }

    @Test
    public void testPolygonSimplificationReducesToSingleRingWithoutHoles() throws Exception {
        JDBCDataStore store = createSeededStore();
        try {
            Geometry simplified = getSimplifiedGeometry(store, "simplify_polygon", 20d);
            assertTrue(simplified instanceof Polygon);
            Polygon polygon = (Polygon) simplified;
            assertEquals(0, polygon.getNumInteriorRing());
            assertEquals(4, polygon.getExteriorRing().getNumPoints());
        } finally {
            store.dispose();
        }
    }

    @Test
    public void testGeometryCollectionSimplificationReducesLineMember() throws Exception {
        JDBCDataStore store = createSeededStore();
        try {
            Geometry simplified = getSimplifiedGeometry(store, "simplify_collection", 20d);
            assertTrue(simplified instanceof GeometryCollection);
            GeometryCollection collection = (GeometryCollection) simplified;
            assertEquals(2, collection.getNumGeometries());
            assertTrue(collection.getGeometryN(0) instanceof Point);
            assertTrue(collection.getGeometryN(1) instanceof LineString);
            LineString simplifiedLine = (LineString) collection.getGeometryN(1);
            assertEquals(2, simplifiedLine.getNumPoints());
        } finally {
            store.dispose();
        }
    }

    private JDBCDataStore createSeededStore() throws Exception {
        JDBCDataStore store =
                DuckDBTestUtils.createStore(tmp.newFolder().toPath().resolve("simplify.duckdb"), false);
        DuckDBTestUtils.runSetupSql(
                store,
                "CREATE TABLE simplify_point (id INTEGER PRIMARY KEY, geom GEOMETRY)",
                "INSERT INTO simplify_point VALUES (1, " + geometryLiteral("POINT(-120.0 40.0)") + ")",
                "CREATE TABLE simplify_line (id INTEGER PRIMARY KEY, geom GEOMETRY)",
                "INSERT INTO simplify_line VALUES (1, "
                        + geometryLiteral("LINESTRING(-120.0 40.0, -130.0 50.0, -140.0 60.0)")
                        + ")",
                "CREATE TABLE simplify_polygon (id INTEGER PRIMARY KEY, geom GEOMETRY)",
                "INSERT INTO simplify_polygon VALUES (1, "
                        + geometryLiteral("POLYGON((-120.0 40.0,-130.0 40.0,-130.0 50.0,-130.0 40.0,-120.0 40.0))")
                        + ")",
                "CREATE TABLE simplify_collection (id INTEGER PRIMARY KEY, geom GEOMETRY)",
                "INSERT INTO simplify_collection VALUES (1, "
                        + geometryLiteral(
                                "GEOMETRYCOLLECTION(POINT(-120.0 40.0),LINESTRING(-120.0 40.0,-130.0 50.0,-140.0 50.0))")
                        + ")");
        return store;
    }

    private Geometry getSimplifiedGeometry(JDBCDataStore store, String tableName, double distance) throws Exception {
        SimpleFeatureSource featureSource = store.getFeatureSource(tableName);
        Assume.assumeTrue(featureSource.getSupportedHints().contains(Hints.GEOMETRY_SIMPLIFICATION));

        Query query = new Query(tableName);
        query.setHints(new Hints(Hints.GEOMETRY_SIMPLIFICATION, distance));
        try (SimpleFeatureIterator it = featureSource.getFeatures(query).features()) {
            assertTrue(it.hasNext());
            return (Geometry) it.next().getDefaultGeometry();
        }
    }

    private String geometryLiteral(String wkt) throws IOException, ParseException {
        Geometry geometry = new WKTReader().read(wkt);
        StringBuffer sql = new StringBuffer("ST_GeomFromHEXEWKB('");
        HexWKBEncoder.encode(geometry, sql);
        sql.append("')");
        return sql.toString();
    }
}
