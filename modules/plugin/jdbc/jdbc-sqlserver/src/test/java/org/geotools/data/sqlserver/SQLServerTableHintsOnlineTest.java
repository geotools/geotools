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
package org.geotools.data.sqlserver;

import java.io.IOException;
import org.geotools.data.Query;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.impl.PackedCoordinateSequenceFactory;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Contains;

/**
 * Same as {@link SQLServerSpatialFiltersOnlineTest}, but forcing the sql hints for spatial filters
 *
 * @source $URL$
 */
public class SQLServerTableHintsOnlineTest extends SQLServerSpatialFiltersOnlineTest {

    private String originalSchema;

    @Override
    protected void connect() throws Exception {
        super.connect();

        SQLServerDialect dialect = (SQLServerDialect) dataStore.getSQLDialect();
        dialect.setForceSpatialIndexes(true);
        dialect.setTableHints(null);
    }

    @Override
    protected void tearDownInternal() throws Exception {
        dataStore.setDatabaseSchema(originalSchema);
        super.tearDownInternal();
    }

    public void testDecorateWithIndex() throws IOException {
        SQLServerDialect dialect = (SQLServerDialect) dataStore.getSQLDialect();
        StringBuffer sql = decorateSpatialQuery(dialect);

        assertTrue(sql.toString().contains("FROM \"road\" WITH(INDEX(\"_road_geometry_index\"))"));
    }

    public void testDecorateWithIndexAndNamespace() throws IOException {
        SQLServerDialect dialect = (SQLServerDialect) dataStore.getSQLDialect();
        StringBuffer sql1 =
                new StringBuffer(
                        "SELECT \"fid\",\"id\",\"geom\".STAsBinary() as \"geom\",\"name\" "
                                + "FROM \"schema\".\"road\" "
                                + "WHERE  \"geom\".Filter(geometry::STGeomFromText('POLYGON ((2 -1, 2 5, 4 5, 4 -1, 2 -1))', 4326)) = 1 "
                                + "AND geometry::STGeomFromText('POLYGON ((2 -1, 2 5, 4 5, 4 -1, 2 -1))', 4326).STContains(\"geom\") = 1");

        // the filter for the Query
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        LinearRing shell =
                gf.createLinearRing(sf.create(new double[] {2, -1, 2, 5, 4, 5, 4, -1, 2, -1}, 2));
        Polygon polygon = gf.createPolygon(shell, null);
        Contains cs = ff.contains(ff.literal(polygon), ff.property(aname("geom")));

        SimpleFeatureType roadSchema = dataStore.getSchema("road");
        originalSchema = dataStore.getDatabaseSchema();
        dataStore.setDatabaseSchema("schema");
        dialect.handleSelectHints(sql1, roadSchema, new Query("road", cs));
        StringBuffer sql = sql1;

        assertTrue(
                sql.toString()
                        .contains(
                                "FROM \"schema\".\"road\" WITH(INDEX(\"_road_geometry_index\"))"));
    }

    public void testDecorateWithIndexAndTableHints() throws IOException {
        SQLServerDialect dialect = (SQLServerDialect) dataStore.getSQLDialect();
        dialect.setTableHints("NOLOCK");
        StringBuffer sql = decorateSpatialQuery(dialect);

        assertTrue(
                sql.toString()
                        .contains("FROM \"road\" WITH(INDEX(\"_road_geometry_index\"), NOLOCK)"));
    }

    private StringBuffer decorateSpatialQuery(SQLServerDialect dialect) throws IOException {
        StringBuffer sql =
                new StringBuffer(
                        "SELECT \"fid\",\"id\",\"geom\".STAsBinary() as \"geom\",\"name\" "
                                + "FROM \"road\" "
                                + "WHERE  \"geom\".Filter(geometry::STGeomFromText('POLYGON ((2 -1, 2 5, 4 5, 4 -1, 2 -1))', 4326)) = 1 "
                                + "AND geometry::STGeomFromText('POLYGON ((2 -1, 2 5, 4 5, 4 -1, 2 -1))', 4326).STContains(\"geom\") = 1");

        // the filter for the Query
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        LinearRing shell =
                gf.createLinearRing(sf.create(new double[] {2, -1, 2, 5, 4, 5, 4, -1, 2, -1}, 2));
        Polygon polygon = gf.createPolygon(shell, null);
        Contains cs = ff.contains(ff.literal(polygon), ff.property(aname("geom")));

        SimpleFeatureType roadSchema = dataStore.getSchema("road");
        dialect.handleSelectHints(sql, roadSchema, new Query("road", cs));
        return sql;
    }

    public void testNonSpatialNoTableHints() throws IOException {
        SQLServerDialect dialect = (SQLServerDialect) dataStore.getSQLDialect();
        StringBuffer sql =
                new StringBuffer(
                        "SELECT \"fid\",\"id\",\"geom\".STAsBinary() as \"geom\",\"name\" "
                                + "FROM \"road\" "
                                + "WHERE \"name\" = 'XXX')");

        // the filter for the Query
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        Filter filter = ff.equal(ff.property("name"), ff.literal("XXX"), true);

        SimpleFeatureType roadSchema = dataStore.getSchema("road");
        dialect.handleSelectHints(sql, roadSchema, new Query("road", filter));

        assertFalse(sql.toString().contains("WITH"));
    }

    public void testNonSpatialWithTableHints() throws IOException {
        SQLServerDialect dialect = (SQLServerDialect) dataStore.getSQLDialect();
        dialect.setTableHints("NOLOCK");
        StringBuffer sql =
                new StringBuffer(
                        "SELECT \"fid\",\"id\",\"geom\".STAsBinary() as \"geom\",\"name\" "
                                + "FROM \"road\" "
                                + "WHERE \"name\" = 'XXX')");

        // the filter for the Query
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        Filter filter = ff.equal(ff.property("name"), ff.literal("XXX"), true);

        SimpleFeatureType roadSchema = dataStore.getSchema("road");
        dialect.handleSelectHints(sql, roadSchema, new Query("road", filter));

        assertTrue(sql.toString().contains("WITH(NOLOCK)"));
    }

    public void testNonSpatialWithTableHintsAndSchema() throws IOException {
        SQLServerDialect dialect = (SQLServerDialect) dataStore.getSQLDialect();
        dialect.setTableHints("NOLOCK");
        StringBuffer sql =
                new StringBuffer(
                        "SELECT \"fid\",\"id\",\"geom\".STAsBinary() as \"geom\",\"name\" "
                                + "FROM \"schema\".\"road\" "
                                + "WHERE \"name\" = 'XXX')");

        // the filter for the Query
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        Filter filter = ff.equal(ff.property("name"), ff.literal("XXX"), true);

        SimpleFeatureType roadSchema = dataStore.getSchema("road");
        originalSchema = dataStore.getDatabaseSchema();
        dataStore.setDatabaseSchema("schema");
        dialect.handleSelectHints(sql, roadSchema, new Query("road", filter));

        assertTrue(sql.toString().contains("WITH(NOLOCK)"));
    }

    public void testEnvelopeBboxFilter() throws Exception {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        // should match only "r2"
        BBOX bbox = ff.bbox(aname("geom"), 2, 3, 4, 5, "EPSG:4326");
        ReferencedEnvelope bounds =
                dataStore.getFeatureSource(tname("road")).getBounds(new Query(null, bbox));
        assertEquals(3, bounds.getMinX(), 1e-3d);
        assertEquals(3, bounds.getMaxX(), 1e-3d);
        assertEquals(0, bounds.getMinY(), 1e-3d);
        assertEquals(4, bounds.getMaxY(), 1e-3d);
    }

    public void testCountBboxFilter() throws Exception {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        // should match only "r2"
        BBOX bbox = ff.bbox(aname("geom"), 2, 3, 4, 5, "EPSG:4326");
        int count = dataStore.getFeatureSource(tname("road")).getCount(new Query(null, bbox));
        assertEquals(1, count);
    }
}
