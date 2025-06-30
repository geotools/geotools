/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.h2gis.geotools;

import static org.h2gis.geotools.H2GISDataStoreFactory.AUTO_SERVER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.feature.type.GeometryType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.Intersects;
import org.geotools.api.referencing.ReferenceIdentifier;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.PrimaryKey;
import org.geotools.jdbc.SQLDialect;
import org.geotools.jdbc.VirtualTable;
import org.geotools.referencing.CRS;
import org.h2gis.utilities.GeometryTableUtilities;
import org.h2gis.utilities.TableLocation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

/** @author Erwan Bocher */
class H2GISTest extends H2GISTestSetup {

    private static final String DB_NAME = "H2GISDBTest";

    public H2GISDataStoreFactory factory;
    private Map<String, String> params;
    public JDBCDataStore ds;
    public WKTReader wKTReader;
    private static Statement st;

    @BeforeEach
    void setUpStatement() throws Exception {
        factory = new H2GISDataStoreFactory();
        factory.setBaseDirectory(new File(getDataBasePath(DB_NAME)));
        params = new HashMap<>();
        params.put(JDBCDataStoreFactory.NAMESPACE.key, "http://www.geotools.org/h2gis");
        params.put(JDBCDataStoreFactory.DATABASE.key, "h2gis");
        params.put(JDBCDataStoreFactory.DBTYPE.key, "h2gis");
        params.put(JDBCDataStoreFactory.USER.key, "h2gis");
        params.put(JDBCDataStoreFactory.PASSWD.key, "h2gis");
        params.put(AUTO_SERVER.key, "true");
        ds = factory.createDataStore(params);
        wKTReader = new WKTReader();
        st = ds.getDataSource().getConnection().createStatement();
        st.execute("DROP SCHEMA IF EXISTS MYGEOTABLE");
    }

    /**
     * Generate a path for the database
     *
     * @param dbName Name of the database
     * @return Path to the database.
     */
    private String getDataBasePath(String dbName) {
        if (dbName.startsWith("file://")) {
            return new File(URI.create(dbName)).getAbsolutePath();
        } else {
            return new File("target/test-resources/" + dbName).getAbsolutePath();
        }
    }

    @AfterEach
    void tearDownStatement() throws Exception {
        st.close();
        ds.getDataSource().getConnection().close();
    }

    @Test
    @SuppressWarnings("PMD.CheckResultSet")
    void createSpatialTables() throws SQLException {
        st.execute("DROP SCHEMA IF EXISTS h2gis; COMMIT;");
        st.execute("CREATE SCHEMA h2gis;");
        st.execute("DROP TABLE IF EXISTS h2gis.geomtable; COMMIT;");

        String sql =
                "CREATE TABLE h2gis.geomtable (id int AUTO_INCREMENT(1) PRIMARY KEY, " + "the_geom GEOMETRY(POINT))";
        st.execute(sql);

        sql = "INSERT INTO h2gis.geomtable VALUES (" + "0,ST_GeomFromText('POINT(12 0)',4326));";
        st.execute(sql);

        try (ResultSet rs = st.executeQuery("select count(id) from h2gis.geomtable")) {
            assertTrue(rs.next());
            assertEquals(1, rs.getInt(1));
        }

        try (ResultSet rs = st.executeQuery("select * from h2gis.geomtable;")) {
            assertTrue(rs.next());
            assertEquals(0, rs.getInt(1));
            assertEquals("SRID=4326;POINT (12 0)", rs.getString(2));
        }
        st.execute("DROP TABLE h2gis.geomtable");
    }

    @Test
    void getFeatureSchema() throws SQLException, IOException {
        st.execute("drop table if exists FORESTS");
        st.execute("CREATE TABLE FORESTS ( FID INTEGER, NAME CHARACTER VARYING(64),"
                + " THE_GEOM GEOMETRY(MULTIPOLYGON));"
                + "INSERT INTO FORESTS VALUES(109, 'Green Forest', ST_MPolyFromText( 'MULTIPOLYGON(((28 26,28 0,84 0,"
                + "84 42,28 26), (52 18,66 23,73 9,48 6,52 18)),((59 18,67 18,67 13,59 13,59 18)))', 101));");

        SimpleFeatureSource fs = ds.getFeatureSource("FORESTS");
        SimpleFeatureType schema = fs.getSchema();
        Query query = new Query(schema.getTypeName(), Filter.INCLUDE);
        assertEquals(1, fs.getCount(query));
        assertEquals(3, schema.getAttributeCount());
        assertEquals("THE_GEOM", schema.getGeometryDescriptor().getLocalName());
        assertEquals("FID", schema.getDescriptor(0).getLocalName());
        assertEquals("NAME", schema.getDescriptor(1).getLocalName());
        assertEquals("THE_GEOM", schema.getDescriptor(2).getLocalName());
        st.execute("drop table FORESTS");
    }

    @Test
    @SuppressWarnings("PMD.CheckResultSet")
    void getFeatureSchemaLinkedTable() throws SQLException, IOException {
        st.execute("drop table if exists LANDCOVER_LINKED");
        st.execute("CALL FILE_TABLE('"
                + H2GISTest.class.getResource("landcover.shp").getPath()
                + "', 'LANDCOVER_LINKED');");
        SimpleFeatureSource fs = ds.getFeatureSource("LANDCOVER_LINKED");
        SimpleFeatureType schema = fs.getSchema();
        GeometryDescriptor geomDesc = schema.getGeometryDescriptor();
        assertEquals("THE_GEOM", geomDesc.getLocalName());
        assertNotNull(geomDesc.getCoordinateReferenceSystem());

        try (ResultSet rs = st.executeQuery("SELECT ST_EXTENT(THE_GEOM) FROM LANDCOVER_LINKED")) {
            assertTrue(rs.next());
            assertTrue(JTS.toEnvelope((Geometry) rs.getObject(1)).boundsEquals2D(fs.getBounds(), 0.01));
        }
        st.execute("drop table LANDCOVER_LINKED");
    }

    @Test
    void getBoundingBox() throws SQLException, IOException, ParseException {
        st.execute("drop table if exists FORESTS");
        st.execute("CREATE TABLE FORESTS ( FID INTEGER PRIMARY KEY, NAME CHARACTER VARYING(64),"
                + " THE_GEOM GEOMETRY(POLYGON));"
                + "INSERT INTO FORESTS VALUES(109, 'Green Forest', 'POLYGON((0 0,10 0,10 10, 0 10, 0 0))');");

        SimpleFeatureSource fs = ds.getFeatureSource("FORESTS");
        SimpleFeatureType schema = fs.getSchema();
        Query query = new Query(schema.getTypeName(), Filter.INCLUDE);
        ReferencedEnvelope bounds = fs.getBounds(query);
        if (bounds == null) {
            FeatureCollection<SimpleFeatureType, SimpleFeature> collection = fs.getFeatures(query);
            bounds = collection.getBounds();
        }
        assertTrue(JTS.toEnvelope(wKTReader.read("POLYGON((0 0,10 0,10 10, 0 10, 0 0))"))
                .boundsEquals2D(bounds, 0.01));
        st.execute("drop table FORESTS");
    }

    @Test
    void getFeatures() throws SQLException, IOException {
        st.execute("drop table if exists LANDCOVER");
        st.execute(
                "CREATE TABLE LANDCOVER ( FID INTEGER, NAME CHARACTER VARYING(64),"
                        + " THE_GEOM GEOMETRY(POLYGON));"
                        + "INSERT INTO LANDCOVER VALUES(1, 'Green Forest', 'POLYGON((110 330, 210 330, 210 240, 110 240, 110 330))');"
                        + "INSERT INTO LANDCOVER VALUES(2, 'Cereal', 'POLYGON((200 220, 310 220, 310 160, 200 160, 200 220))');"
                        + "INSERT INTO LANDCOVER VALUES(3, 'Building', 'POLYGON((90 130, 140 130, 140 110, 90 110, 90 130))');");

        SimpleFeatureSource fs = ds.getFeatureSource("LANDCOVER");
        SimpleFeatureCollection featureCollection = fs.getFeatures(Filter.INCLUDE);

        double sumArea = 0;
        int sumFID = 0;
        try (SimpleFeatureIterator iterator = featureCollection.features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                Geometry geom = (Geometry) feature.getDefaultGeometry();
                sumArea += geom.getArea();
                sumFID += (Integer) feature.getAttribute("FID");
            }
        }
        assertEquals(16600.0, sumArea, 0.1);
        assertEquals(6, sumFID, 0.1);
        st.execute("drop table LANDCOVER");
    }

    @Test
    void getFeaturesFilter() throws SQLException, IOException {
        st.execute("drop table if exists LANDCOVER");
        st.execute(
                "CREATE TABLE LANDCOVER ( FID INTEGER, NAME CHARACTER VARYING(64),"
                        + " THE_GEOM GEOMETRY(POLYGON));"
                        + "INSERT INTO LANDCOVER VALUES(1, 'Green Forest', 'POLYGON((110 330, 210 330, 210 240, 110 240, 110 330))');"
                        + "INSERT INTO LANDCOVER VALUES(2, 'Cereal', 'POLYGON((200 220, 310 220, 310 160, 200 160, 200 220))');"
                        + "INSERT INTO LANDCOVER VALUES(3, 'Building', 'POLYGON((90 130, 140 130, 140 110, 90 110, 90 130))');");

        SimpleFeatureSource fs = ds.getFeatureSource("LANDCOVER");
        SimpleFeatureCollection features = fs.getFeatures(Filter.INCLUDE);
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        Function sum = ff.function("Collection_Sum", ff.property("FID"));
        Object value = sum.evaluate(features);
        assertEquals(6L, value);
        st.execute("drop table LANDCOVER");
    }

    @Test
    void getFeaturesFilter2() throws SQLException, IOException, CQLException {
        st.execute("drop table if exists LANDCOVER");
        st.execute(
                "CREATE TABLE LANDCOVER ( FID INTEGER, NAME CHARACTER VARYING(64),"
                        + " THE_GEOM GEOMETRY(POLYGON));"
                        + "INSERT INTO LANDCOVER VALUES(1, 'Green Forest', 'POLYGON((110 330, 210 330, 210 240, 110 240, 110 330))');"
                        + "INSERT INTO LANDCOVER VALUES(2, 'Cereal', 'POLYGON((200 220, 310 220, 310 160, 200 160, 200 220))');"
                        + "INSERT INTO LANDCOVER VALUES(3, 'Building', 'POLYGON((90 130, 140 130, 140 110, 90 110, 90 130))');");

        SimpleFeatureSource fs = ds.getFeatureSource("LANDCOVER");
        Filter filter = CQL.toFilter("FID >2");
        SimpleFeatureCollection features = fs.getFeatures(filter);
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        Function sum = ff.function("Collection_Sum", ff.property("FID"));
        Object value = sum.evaluate(features);
        assertEquals(3L, value);
        st.execute("drop table LANDCOVER");
    }

    @Test
    void getFeaturesFilter3() throws SQLException, IOException, CQLException {
        st.execute("drop table if exists LANDCOVER");
        st.execute("CREATE TABLE LANDCOVER ( FID INTEGER, CODE INTEGER,"
                + " THE_GEOM GEOMETRY(POLYGON));"
                + "INSERT INTO LANDCOVER VALUES(1, -1, 'POLYGON((110 330, 210 330, 210 240, 110 240, 110 330))');"
                + "INSERT INTO LANDCOVER VALUES(2, 3, 'POLYGON((200 220, 310 220, 310 160, 200 160, 200 220))');"
                + "INSERT INTO LANDCOVER VALUES(3, -1, 'POLYGON((90 130, 140 130, 140 110, 90 110, 90 130))');");

        SimpleFeatureSource fs = ds.getFeatureSource("LANDCOVER");
        Filter filter = CQL.toFilter("FID < abs(CODE)");
        SimpleFeatureCollection features = fs.getFeatures(filter);
        assertEquals(1, features.size());
        st.execute("drop table LANDCOVER");
    }

    @Test
    void testBboxFilter() throws SQLException, IOException, ParseException {
        st.execute("drop table if exists LANDCOVER");
        st.execute("CREATE TABLE LANDCOVER ( FID INTEGER, NAME CHARACTER VARYING(64),"
                + " THE_GEOM GEOMETRY(POINT));"
                + "INSERT INTO LANDCOVER VALUES(1, 'Green Forest', 'POINT(5 5)');"
                + "INSERT INTO LANDCOVER VALUES(2, 'Cereal', 'POINT(200 220)');"
                + "INSERT INTO LANDCOVER VALUES(3, 'Building', 'POINT(90 130)');");
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        BBOX bbox = ff.bbox("THE_GEOM", 0, 0, 10, 10, "EPSG:4326");
        FeatureCollection<?, ?> fc = ds.getFeatureSource("LANDCOVER").getFeatures(bbox);
        assertEquals(1, fc.size());
        SimpleFeature[] features = fc.toArray(new SimpleFeature[fc.size()]);
        assertEquals(features[0].getDefaultGeometry(), wKTReader.read("POINT(5 5)"));
        st.execute("drop table LANDCOVER");
    }

    @Test
    void testIntersectsFilter() throws Exception {
        st.execute("drop table if exists LANDCOVER");
        st.execute("CREATE TABLE LANDCOVER ( FID INTEGER, NAME CHARACTER VARYING(64),"
                + " THE_GEOM GEOMETRY(POINT));"
                + "INSERT INTO LANDCOVER VALUES(1, 'Green Forest', 'POINT(5 5)');"
                + "INSERT INTO LANDCOVER VALUES(2, 'Cereal', 'POINT(200 220)');"
                + "INSERT INTO LANDCOVER VALUES(3, 'Building', 'POINT(90 130)');");
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Intersects is = ff.intersects(ff.property("THE_GEOM"), ff.literal(wKTReader.read("POINT(5 5)")));
        FeatureCollection<?, ?> fc = ds.getFeatureSource("LANDCOVER").getFeatures(is);
        assertEquals(1, fc.size());
        SimpleFeature[] features = fc.toArray(new SimpleFeature[fc.size()]);
        assertEquals(features[0].getDefaultGeometry(), wKTReader.read("POINT(5 5)"));
        st.execute("drop table LANDCOVER");
    }

    @Test
    void testNoCRS() throws Exception {
        st.execute("drop table if exists LANDCOVER");
        st.execute(
                "CREATE TABLE LANDCOVER ( FID INTEGER, NAME CHARACTER VARYING(64),"
                        + " THE_GEOM GEOMETRY(POLYGON));"
                        + "INSERT INTO LANDCOVER VALUES(1, 'Green Forest', 'POLYGON((110 330, 210 330, 210 240, 110 240, 110 330))');"
                        + "INSERT INTO LANDCOVER VALUES(2, 'Cereal', 'POLYGON((200 220, 310 220, 310 160, 200 160, 200 220))');"
                        + "INSERT INTO LANDCOVER VALUES(3, 'Building', 'POLYGON((90 130, 140 130, 140 110, 90 110, 90 130))');");

        SimpleFeatureSource fs = ds.getFeatureSource("LANDCOVER");
        SimpleFeatureCollection features = fs.getFeatures(Filter.INCLUDE);
        CoordinateReferenceSystem crs = features.getBounds().getCoordinateReferenceSystem();
        assertNull(crs);
        st.execute("drop table LANDCOVER");
    }

    @Test
    void testWithCRS() throws Exception {
        st.execute("drop table if exists FORESTS");
        st.execute("CREATE TABLE FORESTS ( FID INTEGER, NAME CHARACTER VARYING(64),"
                + " THE_GEOM GEOMETRY(MULTIPOLYGON, 4326));"
                + "INSERT INTO FORESTS VALUES(109, 'Green Forest', ST_MPolyFromText( 'MULTIPOLYGON(((28 26,28 0,84 0,"
                + "84 42,28 26), (52 18,66 23,73 9,48 6,52 18)),((59 18,67 18,67 13,59 13,59 18)))', 4326));");

        SimpleFeatureSource fs = ds.getFeatureSource("FORESTS");
        SimpleFeatureCollection features = fs.getFeatures(Filter.INCLUDE);
        CoordinateReferenceSystem crs = features.getBounds().getCoordinateReferenceSystem();
        assertNotNull(crs);
        assertEquals("EPSG:4326", CRS.lookupIdentifier(crs, true));
        st.execute("drop table FORESTS");
    }

    @Test
    void testVirtualTable() throws SQLException, IOException, ParseException {
        st.execute("drop table if exists LANDCOVER");
        st.execute(
                "CREATE TABLE LANDCOVER ( FID INTEGER, NAME CHARACTER VARYING(64),"
                        + " THE_GEOM GEOMETRY(POLYGON));"
                        + "INSERT INTO LANDCOVER VALUES(1, 'Green Forest', 'POLYGON((110 330, 210 330, 210 240, 110 240, 110 330))');"
                        + "INSERT INTO LANDCOVER VALUES(2, 'Cereal', 'POLYGON((200 220, 310 220, 310 160, 200 160, 200 220))');"
                        + "INSERT INTO LANDCOVER VALUES(3, 'Building', 'POLYGON((90 130, 140 130, 140 110, 90 110, 90 130))');");
        VirtualTable vTable = new VirtualTable("LANDCOVER_CEREAL", "SELECT * FROM PUBLIC.LANDCOVER WHERE FID=2");
        vTable.addGeometryMetadatata("THE_GEOM", Polygon.class, 4326);
        ds.createVirtualTable(vTable);
        SimpleFeatureType type = ds.getSchema("LANDCOVER_CEREAL");
        assertNotNull(type);
        assertNotNull(type.getGeometryDescriptor());
        FeatureSource<?, ?> fsView = ds.getFeatureSource("LANDCOVER_CEREAL");
        ReferencedEnvelope env = fsView.getBounds();
        assertNotNull(env);
        assertTrue(JTS.toEnvelope(wKTReader.read("POLYGON((200 220, 310 220, 310 160, 200 160, 200 220))"))
                .boundsEquals2D(env, 0.01));
        ds.dropVirtualTable("LANDCOVER_CEREAL");
        st.execute("drop table LANDCOVER");
    }

    @Test
    void testVirtualTableFromSQL() throws SQLException, IOException, ParseException {
        st.execute("drop table if exists LANDCOVER");
        st.execute(
                "CREATE TABLE LANDCOVER ( FID INTEGER, NAME CHARACTER VARYING(64),"
                        + " THE_GEOM GEOMETRY(POLYGON));"
                        + "INSERT INTO LANDCOVER VALUES(1, 'Green Forest', 'POLYGON((110 330, 210 330, 210 240, 110 240, 110 330))');"
                        + "INSERT INTO LANDCOVER VALUES(2, 'Cereal', 'POLYGON((200 220, 310 220, 310 160, 200 160, 200 220))');"
                        + "INSERT INTO LANDCOVER VALUES(3, 'Building', 'POLYGON((90 130, 140 130, 140 110, 90 110, 90 130))');");
        VirtualTable vTable = new VirtualTable("LANDCOVER_CEREAL", "SELECT * FROM PUBLIC.LANDCOVER WHERE FID=2");
        // TODO workaround due to H2 2.0 changes on geometry type
        vTable.addGeometryMetadatata("THE_GEOM", Polygon.class, -1);
        ds.createVirtualTable(vTable);
        SimpleFeatureType type = ds.getSchema("LANDCOVER_CEREAL");
        assertNotNull(type);
        assertNotNull(type.getGeometryDescriptor());
        FeatureSource<?, ?> fsView = ds.getFeatureSource("LANDCOVER_CEREAL");
        ReferencedEnvelope env = fsView.getBounds();
        assertNotNull(env);
        assertTrue(JTS.toEnvelope(wKTReader.read("POLYGON((200 220, 310 220, 310 160, 200 160, 200 220))"))
                .boundsEquals2D(env, 0.01));
        ds.dropVirtualTable("LANDCOVER_CEREAL");
        st.execute("drop table LANDCOVER");
    }

    @Test
    void testH2GISFileTable() throws SQLException, IOException {
        st.execute("drop table if exists LANDCOVER");
        st.execute("CALL FILE_TABLE('"
                + H2GISTest.class.getResource("landcover.shp").getPath()
                + "', 'LANDCOVER');");
        assertTrue(st.execute("SELECT * FROM LANDCOVER LIMIT 0;"));
        SimpleFeatureSource fs = ds.getFeatureSource("LANDCOVER");
        SimpleFeatureType schema = fs.getSchema();
        Query query = new Query(schema.getTypeName(), Filter.INCLUDE);
        assertEquals(3, fs.getCount(query));
        query = new Query(schema.getName().getLocalPart(), Filter.INCLUDE);
        ReferencedEnvelope bounds = fs.getBounds(query);
        assertNotNull(bounds);
        assertEquals(
                "ReferencedEnvelope[90.0 : 310.0, 110.0 : 330.0] DefaultGeographicCRS[EPSG:WGS 84] AXIS[\"Geodetic latitude\", NORTH] AXIS[\"Geodetic longitude\", EAST]",
                bounds.toString());
        st.execute("drop table LANDCOVER");
    }

    @Test
    void updateGeometry_Columns() throws SQLException, IOException, SchemaException {
        SQLDialect dialect = factory.createSQLDialect(ds);
        String schemaName = "PUBLIC";
        st.execute("drop table if exists LANDCOVER");
        st.execute(
                "CREATE TABLE LANDCOVER ( FID INTEGER, NAME CHARACTER VARYING(64),"
                        + " THE_GEOM GEOMETRY(POLYGON));"
                        + "INSERT INTO LANDCOVER VALUES(1, 'Green Forest', 'POLYGON((110 330, 210 330, 210 240, 110 240, 110 330))');"
                        + "INSERT INTO LANDCOVER VALUES(2, 'Cereal', 'POLYGON((200 220, 310 220, 310 160, 200 160, 200 220))');"
                        + "INSERT INTO LANDCOVER VALUES(3, 'Building', 'POLYGON((90 130, 140 130, 140 110, 90 110, 90 130))');");

        SimpleFeatureType newFS = DataUtilities.createType("LANDCOVER", "FID:Integer,NAME:String,THE_GEOM:Polygon");

        assertEquals(newFS.getGeometryDescriptor().getType().getBinding(), Polygon.class);
        dialect.postCreateTable(schemaName, newFS, ds.getDataSource().getConnection());
        assertEquals(
                0, GeometryTableUtilities.getSRID(ds.getDataSource().getConnection(), new TableLocation("LANDCOVER")));
    }

    @Test
    void testGeometryTypes() throws SQLException, IOException {
        String sql =
                "DROP TABLE IF EXISTS GEOMTYPES; CREATE TABLE GEOMTYPES(G GEOMETRY, G_S GEOMETRY(GEOMETRY, 1), P GEOMETRY(POINT), P_S GEOMETRY(POINT, 1),\n"
                        + "    PZ1 GEOMETRY(POINT Z), PZ2 GEOMETRY(POINTZ), PZ1_S GEOMETRY(POINT Z, 1), PZ2_S GEOMETRY(POINTZ, 1),\n"
                        + "    PM GEOMETRY(POINT M), PZM GEOMETRY(POINT ZM), PZM_S GEOMETRY(POINT ZM, -100),\n"
                        + "    LS GEOMETRY(LINESTRING), PG GEOMETRY(POLYGON),\n"
                        + "    MP GEOMETRY(MULTIPOINT), MLS GEOMETRY(MULTILINESTRING), MPG GEOMETRY(MULTIPOLYGON),\n"
                        + "    GC GEOMETRY(GEOMETRYCOLLECTION),PGZ GEOMETRY(POLYGONZ),PGM GEOMETRY(POLYGONM),PGZM GEOMETRY(POLYGONZM));\n"
                        + "INSERT INTO GEOMTYPES VALUES ('POINT EMPTY', 'SRID=1;POINT EMPTY', 'POINT EMPTY', 'SRID=1;POINT EMPTY',\n"
                        + "    'POINT Z EMPTY', 'POINT Z EMPTY', 'SRID=1;POINT Z EMPTY', 'SRID=1;POINTZ EMPTY',\n"
                        + "    'POINT M EMPTY', 'POINT ZM EMPTY', 'SRID=-100;POINT ZM EMPTY',\n"
                        + "    'LINESTRING EMPTY', 'POLYGON EMPTY',\n"
                        + "    'MULTIPOINT EMPTY', 'MULTILINESTRING EMPTY', 'MULTIPOLYGON EMPTY',\n"
                        + "    'GEOMETRYCOLLECTION EMPTY','POLYGON Z EMPTY','POLYGON M EMPTY','POLYGON ZM EMPTY');";
        st.execute(sql);
        SimpleFeatureSource fs = ds.getFeatureSource("GEOMTYPES");
        SimpleFeatureType schema = fs.getSchema();
        GeometryType geomType = (GeometryType) schema.getDescriptor("G").getType();
        assertTrue(geomType.getBinding().isAssignableFrom(Geometry.class));
        geomType = (GeometryType) schema.getDescriptor("G_S").getType();
        assertTrue(geomType.getBinding().isAssignableFrom(Geometry.class));
        geomType = (GeometryType) schema.getDescriptor("P").getType();
        assertTrue(geomType.getBinding().isAssignableFrom(Point.class));
        geomType = (GeometryType) schema.getDescriptor("P_S").getType();
        assertTrue(geomType.getBinding().isAssignableFrom(Point.class));
        geomType = (GeometryType) schema.getDescriptor("PZ1").getType();
        assertTrue(geomType.getBinding().isAssignableFrom(Point.class));
        geomType = (GeometryType) schema.getDescriptor("PZ2").getType();
        assertTrue(geomType.getBinding().isAssignableFrom(Point.class));
        geomType = (GeometryType) schema.getDescriptor("PZ1_S").getType();
        assertTrue(geomType.getBinding().isAssignableFrom(Point.class));
        geomType = (GeometryType) schema.getDescriptor("PZ2_S").getType();
        assertTrue(geomType.getBinding().isAssignableFrom(Point.class));
        geomType = (GeometryType) schema.getDescriptor("PM").getType();
        assertTrue(geomType.getBinding().isAssignableFrom(Point.class));
        geomType = (GeometryType) schema.getDescriptor("PZM").getType();
        assertTrue(geomType.getBinding().isAssignableFrom(Point.class));
        geomType = (GeometryType) schema.getDescriptor("PZM_S").getType();
        assertTrue(geomType.getBinding().isAssignableFrom(Point.class));
        geomType = (GeometryType) schema.getDescriptor("LS").getType();
        assertTrue(geomType.getBinding().isAssignableFrom(LineString.class));
        geomType = (GeometryType) schema.getDescriptor("LS").getType();
        assertTrue(geomType.getBinding().isAssignableFrom(LineString.class));
        geomType = (GeometryType) schema.getDescriptor("PG").getType();
        assertTrue(geomType.getBinding().isAssignableFrom(Polygon.class));
        geomType = (GeometryType) schema.getDescriptor("MLS").getType();
        assertTrue(geomType.getBinding().isAssignableFrom(MultiLineString.class));
        geomType = (GeometryType) schema.getDescriptor("MPG").getType();
        assertTrue(geomType.getBinding().isAssignableFrom(MultiPolygon.class));
        geomType = (GeometryType) schema.getDescriptor("MP").getType();
        assertTrue(geomType.getBinding().isAssignableFrom(MultiPoint.class));
        geomType = (GeometryType) schema.getDescriptor("MPG").getType();
        assertTrue(geomType.getBinding().isAssignableFrom(MultiPolygon.class));
        geomType = (GeometryType) schema.getDescriptor("PGZ").getType();
        assertTrue(geomType.getBinding().isAssignableFrom(Polygon.class));
        geomType = (GeometryType) schema.getDescriptor("PGM").getType();
        assertTrue(geomType.getBinding().isAssignableFrom(Polygon.class));
        geomType = (GeometryType) schema.getDescriptor("PGZM").getType();
        assertTrue(geomType.getBinding().isAssignableFrom(Polygon.class));
    }

    @Test
    void createSchema() throws SQLException, IOException, SchemaException {
        String tableName = "mygeotable";
        st.execute("DROP TABLE if exists \"" + tableName + "\"");
        SimpleFeatureType schema = DataUtilities.createType(tableName, "geom:Point:srid=4326,id:Integer,name:String");
        ds.createSchema(schema);
        SimpleFeatureType dbSchema = ds.getSchema(schema.getName());
        assertNotNull(dbSchema);
        assertNotNull(ds.getFeatureSource(tableName));
        assertEquals(Point.class, dbSchema.getGeometryDescriptor().getType().getBinding());
        tableName = "MYGEOTABLE";
        st.execute("DROP TABLE if exists \"" + tableName + "\"");
        ds.removeSchema(schema.getTypeName());
        schema = DataUtilities.createType(tableName, "geom:Point:srid=4326,id:Integer,name:String");
        st.execute("DROP TABLE if exists \"" + tableName + "\"");
        ds.createSchema(schema);
        dbSchema = ds.getSchema(schema.getName());
        assertNotNull(dbSchema);
        assertNotNull(ds.getFeatureSource(tableName));
        assertEquals(Point.class, dbSchema.getGeometryDescriptor().getType().getBinding());
        ReferenceIdentifier crsidentifier =
                dbSchema.getGeometryDescriptor().getCoordinateReferenceSystem().getIdentifiers().stream()
                        .findFirst()
                        .get();
        assertNotNull(crsidentifier);
        assertEquals("EPSG:4326", crsidentifier.toString());
        st.execute("DROP TABLE if exists \"" + tableName + "\"");
    }

    @Test
    void getGeometryTypeFromVirtualTable() throws SQLException, IOException {
        st.execute("drop table if exists LANDCOVER");
        st.execute(
                "CREATE TABLE LANDCOVER ( FID INTEGER, NAME CHARACTER VARYING(64),"
                        + " THE_GEOM GEOMETRY(POLYGON,4326));"
                        + "INSERT INTO LANDCOVER VALUES(1, 'Green Forest', 'SRID=4326;POLYGON((110 330, 210 330, 210 240, 110 240, 110 330))');");
        VirtualTable vTable = new VirtualTable("LANDCOVER_CEREAL", "SELECT * FROM PUBLIC.LANDCOVER");
        ds.createVirtualTable(vTable);
        FeatureSource<?, ?> fs = ds.getFeatureSource("LANDCOVER_CEREAL");
        GeometryDescriptor geomDes = fs.getSchema().getGeometryDescriptor();
        assertNotNull(geomDes);
        assertEquals(Polygon.class, geomDes.getType().getBinding());
        st.execute("drop table LANDCOVER");
    }

    @Test
    @Disabled
    // TODO : FIX detect PK on table engine
    void getPrimaryKeyFromLinkedFile() throws SQLException, IOException {
        st.execute("drop table if exists LANDCOVER_LINKED");
        st.execute("CALL FILE_TABLE('"
                + H2GISTest.class.getResource("landcover.shp").getPath()
                + "', 'LANDCOVER_LINKED');");
        SimpleFeatureType schema = ds.getFeatureSource("LANDCOVER_LINKED").getSchema();
        PrimaryKey pk = ds.getPrimaryKey(schema);
        assertEquals(1, pk.getColumns().size());
    }
}
