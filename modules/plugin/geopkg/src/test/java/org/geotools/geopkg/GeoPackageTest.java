/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.media.jai.PlanarImage;
import org.apache.commons.io.FileUtils;
import org.geotools.TestData;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.memory.MemoryFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.data.simple.SimpleFeatureWriter;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.Geometries;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geopkg.mosaic.GeoPackageFormat;
import org.geotools.geopkg.mosaic.GeoPackageReader;
import org.geotools.image.test.ImageAssert;
import org.geotools.jdbc.util.SqlUtil;
import org.geotools.parameter.Parameter;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.FilterFactory;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.FactoryException;

public class GeoPackageTest {

    GeoPackage geopkg;

    @BeforeClass
    public static void setUpOnce() {
        Hints.putSystemDefault(Hints.COMPARISON_TOLERANCE, 1e-9);
    }

    @Before
    public void setUp() throws Exception {
        geopkg = new GeoPackage(File.createTempFile("geopkg", "db", new File("target")));
        geopkg.init();
    }

    @After
    public void tearDown() throws Exception {
        geopkg.close();

        // for debugging, copy the current geopackage file to a well known file
        File f = new File("target", "geopkg.db");
        if (f.exists()) {
            f.delete();
        }

        FileUtils.copyFile(geopkg.getFile(), f);
    }

    @Test
    public void testInit() throws Exception {
        assertTableExists("gpkg_contents");
        assertTableExists("gpkg_geometry_columns");
        assertTableExists("gpkg_spatial_ref_sys");
        assertDefaultSpatialReferencesExist();
        assertApplicationId();
    }

    void assertDefaultSpatialReferencesExist() throws Exception {
        Connection cx = geopkg.getDataSource().getConnection();
        Statement st = cx.createStatement();
        try {
            ResultSet rs =
                    st.executeQuery("SELECT srs_id FROM gpkg_spatial_ref_sys WHERE srs_id = -1");
            assertEquals(rs.getInt(1), -1);
            rs = st.executeQuery("SELECT srs_id FROM gpkg_spatial_ref_sys WHERE srs_id = 0");
            assertEquals(rs.getInt(1), 0);
            rs = st.executeQuery("SELECT srs_id FROM gpkg_spatial_ref_sys WHERE srs_id = 4326");
            assertEquals(rs.getInt(1), 4326);
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            st.close();
            cx.close();
        }
    }

    void assertApplicationId() throws Exception {
        Connection cx = geopkg.getDataSource().getConnection();
        Statement st = cx.createStatement();
        try {
            ResultSet rs = st.executeQuery("PRAGMA application_id;");
            assertEquals(rs.getInt(1), 0x47503130);
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            st.close();
            cx.close();
        }
    }

    void assertTableExists(String table) throws Exception {
        Connection cx = geopkg.getDataSource().getConnection();
        Statement st = cx.createStatement();
        try {
            st.execute(String.format("SELECT count(*) FROM %s;", table));
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            st.close();
            cx.close();
        }
    }

    void assertLastChangedDateString(Calendar startTime, Calendar endTime) throws Exception {
        final TimeZone tz = TimeZone.getTimeZone("GMT");
        // get the date now for comparison
        final Calendar c = Calendar.getInstance(tz);
        // this is what should be used for the date string format in the DB
        final String dateFomratString = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        final SimpleDateFormat sdf = new SimpleDateFormat(dateFomratString);
        sdf.setTimeZone(tz);

        try (Connection cx = geopkg.getDataSource().getConnection();
                Statement st = cx.createStatement();
                ResultSet rs =
                        st.executeQuery(
                                String.format(
                                        "SELECT last_change FROM %s;",
                                        GeoPackage.GEOPACKAGE_CONTENTS))) {

            if (rs.next()) {
                final String dateString = rs.getString(1);
                // parse the date with the expected format string
                Date parsedDate = sdf.parse(dateString);
                // assert the parsed time is between the start and end time
                c.setTime(parsedDate);
                assertTrue(
                        "Start time should be less than or equal to last_change time",
                        startTime.compareTo(c) <= 0);
                assertTrue(
                        "End time should be greater than or equal to last_change time",
                        endTime.compareTo(c) >= 0);
            }
        }
    }

    boolean doesEntryExists(String table, Entry entry) throws Exception {
        boolean exists = false;
        Connection cx = geopkg.getDataSource().getConnection();
        try {
            String sql = String.format("SELECT * FROM %s WHERE table_name = ?", table);
            SqlUtil.PreparedStatementBuilder psb =
                    SqlUtil.prepare(cx, sql).set(entry.getTableName());
            PreparedStatement ps = psb.log(Level.FINE).statement();
            try {
                ResultSet rs = ps.executeQuery();
                try {
                    while (rs.next()) {
                        exists = true;
                    }
                } finally {
                    rs.close();
                }
            } finally {
                ps.close();
            }
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            cx.close();
        }
        return exists;
    }

    @Test
    public void testSRS() throws Exception {
        Entry entry = new Entry();
        entry.setTableName("points");
        entry.setDataType(Entry.DataType.Feature);
        entry.setIdentifier("points");
        entry.setBounds(new ReferencedEnvelope(-180, 180, -90, 90, CRS.decode("EPSG:2000", true)));
        entry.setSrid(2000);

        geopkg.addGeoPackageContentsEntry(entry);

        Connection cx = geopkg.getDataSource().getConnection();
        try {
            String sql =
                    String.format(
                            "SELECT srs_name FROM %s WHERE srs_id = ?", GeoPackage.SPATIAL_REF_SYS);
            SqlUtil.PreparedStatementBuilder psb = SqlUtil.prepare(cx, sql).set(2000);
            PreparedStatement ps = psb.log(Level.FINE).statement();
            try {
                ResultSet rs = ps.executeQuery();
                try {
                    assertTrue(rs.next());
                    assertEquals("epsg:2000", rs.getString(1));
                } finally {
                    rs.close();
                }
            } finally {
                ps.close();
            }
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            cx.close();
        }
    }

    @Test
    public void testDeleteGeoPackageContentsEntry() throws Exception {
        Entry entry = new Entry();
        entry.setTableName("points");
        entry.setDataType(Entry.DataType.Feature);
        entry.setIdentifier("points");
        entry.setBounds(new ReferencedEnvelope(-180, 180, -90, 90, CRS.decode("EPSG:4326", true)));
        entry.setSrid(4326);

        geopkg.addGeoPackageContentsEntry(entry);
        assertTrue(doesEntryExists(GeoPackage.GEOPACKAGE_CONTENTS, entry));
        geopkg.deleteGeoPackageContentsEntry(entry);
        assertFalse(doesEntryExists(GeoPackage.GEOPACKAGE_CONTENTS, entry));
    }

    @Test
    public void testDeleteGeometryColumnsEntry() throws Exception {
        FeatureEntry entry = new FeatureEntry();
        entry.setTableName("points");
        entry.setDataType(Entry.DataType.Feature);
        entry.setIdentifier("points");
        entry.setBounds(new ReferencedEnvelope(-180, 180, -90, 90, CRS.decode("EPSG:4326", true)));
        entry.setSrid(4326);
        entry.setGeometryColumn("geom");
        entry.setGeometryType(Geometries.POINT);

        geopkg.addGeometryColumnsEntry(entry);
        assertTrue(doesEntryExists(GeoPackage.GEOMETRY_COLUMNS, entry));
        geopkg.deleteGeometryColumnsEntry(entry);
        assertFalse(doesEntryExists(GeoPackage.GEOMETRY_COLUMNS, entry));
    }

    @Test
    public void testCreateFeatureEntry() throws Exception {
        ShapefileDataStore shp = new ShapefileDataStore(setUpShapefile());

        FeatureEntry entry = new FeatureEntry();
        geopkg.add(entry, shp.getFeatureSource(), null);

        assertTableExists("bugsites");

        // check metadata contents
        assertFeatureEntry(entry);

        SimpleFeatureReader re = Features.simple(shp.getFeatureReader());
        SimpleFeatureReader ra = geopkg.reader(entry, null, null);

        while (re.hasNext()) {
            assertTrue(ra.hasNext());
            assertSimilar(re.next(), ra.next());
        }

        re.close();
        ra.close();
    }

    @Test
    public void test3DGeometry() throws Exception {
        // create feature with 3d geometry
        Point geom =
                new GeometryFactory(new PrecisionModel(), 4326)
                        .createPoint(new Coordinate(5, 3, 8));
        SimpleFeatureTypeBuilder tBuilder = new SimpleFeatureTypeBuilder();
        tBuilder.setName("mytype");
        tBuilder.add("name", String.class);
        tBuilder.add("geom", Geometry.class, 4326);
        SimpleFeatureType type = tBuilder.buildFeatureType();
        SimpleFeatureBuilder fBuilder = new SimpleFeatureBuilder(type);
        MemoryFeatureCollection featCollection = new MemoryFeatureCollection(type);
        fBuilder.add("testfeature");
        fBuilder.add(geom);
        featCollection.add(fBuilder.buildFeature("fid-0001"));

        FeatureEntry entry = new FeatureEntry();
        // important, store in database that there is a z
        entry.setZ(true);
        geopkg.add(entry, featCollection);

        assertTableExists("mytype");

        // check metadata contents
        assertFeatureEntry(entry);

        // read feature and verify dimension
        SimpleFeatureReader ra = geopkg.reader(entry, null, null);
        assertTrue(ra.hasNext());

        SimpleFeature f = ra.next();
        Point readGeom = (Point) f.getAttribute("geom");

        assertEquals(3, readGeom.getCoordinateSequence().getDimension());
        assertEquals(geom.getCoordinate().z, readGeom.getCoordinate().z, 0.0001);

        ra.close();
    }

    @Test
    public void testFunctions() throws Exception {
        ShapefileDataStore shp = new ShapefileDataStore(setUpShapefile());
        SimpleFeatureReader re = Features.simple(shp.getFeatureReader());

        FeatureEntry entry = new FeatureEntry();
        geopkg.add(entry, shp.getFeatureSource(), null);

        Connection cx = geopkg.getDataSource().getConnection();
        Statement st = cx.createStatement();
        try {
            while (re.hasNext()) {
                SimpleFeature f = re.next();
                ResultSet rs =
                        st.executeQuery(
                                (String.format(
                                        "SELECT ST_MinX(the_geom), ST_MinY(the_geom), ST_MaxX(the_geom), ST_MaxY(the_geom), ST_IsEmpty(the_geom) FROM bugsites WHERE ID="
                                                + f.getProperty("ID").getValue())));
                assertEquals(
                        rs.getDouble(1),
                        ((Geometry) f.getDefaultGeometry()).getEnvelopeInternal().getMinX(),
                        0.0001);
                assertEquals(
                        rs.getDouble(2),
                        ((Geometry) f.getDefaultGeometry()).getEnvelopeInternal().getMinY(),
                        0.0001);
                assertEquals(
                        rs.getDouble(3),
                        ((Geometry) f.getDefaultGeometry()).getEnvelopeInternal().getMaxX(),
                        0.0001);
                assertEquals(
                        rs.getDouble(4),
                        ((Geometry) f.getDefaultGeometry()).getEnvelopeInternal().getMaxY(),
                        0.0001);
                assertEquals(rs.getDouble(5) == 1, ((Geometry) f.getDefaultGeometry()).isEmpty());
                rs.close();
            }
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            st.close();
            cx.close();
            re.close();
        }
    }

    @Test
    public void testBooleanWrite() throws Exception {
        ShapefileDataStore shp = new ShapefileDataStore(setUpBoolShapefile());
        SimpleFeatureCollection coll = shp.getFeatureSource().getFeatures();

        FeatureEntry entry = new FeatureEntry();
        entry.setBounds(coll.getBounds());
        try {
            geopkg.add(entry, coll);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFunctionsNoEnvelope() throws Exception {
        ShapefileDataStore shp = new ShapefileDataStore(setUpShapefile());
        SimpleFeatureReader re = Features.simple(shp.getFeatureReader());

        FeatureEntry entry = new FeatureEntry();

        geopkg.getWriterConfiguration().setWriteEnvelope(false);
        geopkg.add(entry, shp.getFeatureSource(), null);

        Connection cx = geopkg.getDataSource().getConnection();
        Statement st = cx.createStatement();
        try {
            while (re.hasNext()) {
                SimpleFeature f = re.next();
                ResultSet rs =
                        st.executeQuery(
                                (String.format(
                                        "SELECT ST_MinX(the_geom), ST_MinY(the_geom), ST_MaxX(the_geom), ST_MaxY(the_geom), ST_IsEmpty(the_geom) FROM bugsites WHERE ID="
                                                + f.getProperty("ID").getValue())));
                assertEquals(
                        rs.getDouble(1),
                        ((Geometry) f.getDefaultGeometry()).getEnvelopeInternal().getMinX(),
                        0.0001);
                assertEquals(
                        rs.getDouble(2),
                        ((Geometry) f.getDefaultGeometry()).getEnvelopeInternal().getMinY(),
                        0.0001);
                assertEquals(
                        rs.getDouble(3),
                        ((Geometry) f.getDefaultGeometry()).getEnvelopeInternal().getMaxX(),
                        0.0001);
                assertEquals(
                        rs.getDouble(4),
                        ((Geometry) f.getDefaultGeometry()).getEnvelopeInternal().getMaxY(),
                        0.0001);
                assertEquals(rs.getDouble(5) == 1, ((Geometry) f.getDefaultGeometry()).isEmpty());
                rs.close();
            }
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            st.close();
            cx.close();
            re.close();
        }
    }

    @Test
    public void testSpatialIndexWriting() throws Exception {
        ShapefileDataStore shp = new ShapefileDataStore(setUpShapefile());
        SimpleFeatureCollection coll = shp.getFeatureSource().getFeatures();

        FeatureEntry entry = new FeatureEntry();
        entry.setBounds(coll.getBounds());
        geopkg.create(entry, shp.getSchema());

        // write some features before and some after
        try (SimpleFeatureIterator it = coll.features()) {

            // some features
            try (Transaction tx = new DefaultTransaction();
                    SimpleFeatureWriter w = geopkg.writer(entry, true, null, tx)) {
                for (int i = 0; i < 3; i++) {
                    SimpleFeature f = it.next();
                    SimpleFeature g = w.next();
                    for (PropertyDescriptor pd : coll.getSchema().getDescriptors()) {
                        String name = pd.getName().getLocalPart();
                        g.setAttribute(name, f.getAttribute(name));
                    }

                    w.write();
                }
                tx.commit();
            }

            // create spatial index
            geopkg.createSpatialIndex(entry);

            // the rest of features
            try (Transaction tx = new DefaultTransaction();
                    SimpleFeatureWriter w = geopkg.writer(entry, true, null, tx)) {
                while (it.hasNext()) {
                    SimpleFeature f = it.next();
                    SimpleFeature g = w.next();
                    for (PropertyDescriptor pd : coll.getSchema().getDescriptors()) {
                        String name = pd.getName().getLocalPart();
                        g.setAttribute(name, f.getAttribute(name));
                    }

                    w.write();
                }
                tx.commit();
            }
        }

        // test if the index was properly created
        try (Connection cx = geopkg.getDataSource().getConnection();
                Statement st = cx.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM rtree_bugsites_the_geom");
            rs.next();

            assertEquals(rs.getInt(1), coll.size());
        }
    }

    @Test
    public void testSpatialIndexReading() throws Exception {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        ShapefileDataStore shp = new ShapefileDataStore(setUpShapefile());

        FeatureEntry entry = new FeatureEntry();
        geopkg.add(entry, shp.getFeatureSource(), null);

        assertFalse(geopkg.hasSpatialIndex(entry));

        geopkg.createSpatialIndex(entry);

        assertTrue(geopkg.hasSpatialIndex(entry));

        Set ids = geopkg.searchSpatialIndex(entry, 590230.0, 4915038.0, 590234.0, 4915040.0);
        try (SimpleFeatureReader sfr = geopkg.reader(entry, ff.id(ids), null)) {
            assertTrue(sfr.hasNext());
            assertEquals("bugsites.1", sfr.next().getID().toString());
            assertFalse(sfr.hasNext());
        }
    }

    @Test
    public void testCreateTileEntry() throws Exception {
        TileEntry e = new TileEntry();
        e.setTableName("foo");
        e.setBounds(new ReferencedEnvelope(-180, 180, -90, 90, DefaultGeographicCRS.WGS84));
        e.getTileMatricies().add(new TileMatrix(0, 1, 1, 256, 256, 0.1, 0.1));
        e.getTileMatricies().add(new TileMatrix(1, 2, 2, 256, 256, 0.1, 0.1));

        geopkg.create(e);
        assertTileEntry(e);

        List<Tile> tiles = new ArrayList();
        tiles.add(new Tile(0, 0, 0, new byte[] {0}));
        tiles.add(new Tile(1, 0, 0, new byte[] {1}));
        tiles.add(new Tile(1, 0, 1, new byte[] {2}));
        tiles.add(new Tile(1, 1, 0, new byte[] {3}));
        tiles.add(new Tile(1, 1, 1, new byte[] {4}));

        for (Tile t : tiles) {
            geopkg.add(e, t);
        }

        try (TileReader r = geopkg.reader(e, null, null, null, null, null, null)) {
            assertTiles(tiles, r);
        }
    }

    @Test
    public void testIndependentTileMatrix() throws Exception {
        TileEntry e = new TileEntry();
        e.setTableName("foo");
        e.setBounds(new ReferencedEnvelope(-10, 10, -10, 10, DefaultGeographicCRS.WGS84));
        e.setTileMatrixSetBounds(
                new ReferencedEnvelope(-180, 180, -90, 90, DefaultGeographicCRS.WGS84));
        e.getTileMatricies().add(new TileMatrix(0, 1, 1, 256, 256, 0.1, 0.1));
        e.getTileMatricies().add(new TileMatrix(1, 2, 2, 256, 256, 0.1, 0.1));

        geopkg.create(e);

        assertContentEntry(e);

        try (Connection cx = geopkg.getDataSource().getConnection();
                PreparedStatement ps =
                        cx.prepareStatement(
                                "SELECT * from gpkg_tile_matrix_set WHERE table_name = ?")) {
            ps.setString(1, e.getTableName());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                assertEquals(4326, rs.getInt(2));
                assertEquals(-180, rs.getDouble(3), 0.01);
                assertEquals(-90, rs.getDouble(4), 0.01);
                assertEquals(180, rs.getDouble(5), 0.01);
                assertEquals(90, rs.getDouble(6), 0.01);

                assertFalse(rs.next());
            }
        }
    }

    @Test
    public void testListEntries() throws Exception {
        // grab the start and end time to ensure the last_change time range
        final TimeZone tz = TimeZone.getTimeZone("GMT");
        final Calendar startTime = Calendar.getInstance(tz);
        testCreateFeatureEntry();
        testCreateTileEntry();
        final Calendar endTime = Calendar.getInstance(tz);

        List<FeatureEntry> lf = geopkg.features();
        assertEquals(1, lf.size());
        assertEquals("bugsites", lf.get(0).getTableName());
        // make sure Date format String is fine
        assertLastChangedDateString(startTime, endTime);

        List<TileEntry> lt = geopkg.tiles();
        assertEquals(1, lt.size());

        TileEntry te = lt.get(0);
        assertEquals("foo", te.getTableName());
        assertEquals(2, te.getTileMatricies().size());
    }

    @Test
    /*
     * From the OGC GeoPackage Specification [1]:
     *
     * "The tile coordinate (0,0) always refers to the tile in the upper left corner of the tile matrix at any zoom
     * level, regardless of the actual availability of that tile"
     *
     * [1]: http://www.geopackage.org/spec/#tile_matrix
     */
    public void testTopLeftTile() throws IOException, FactoryException {
        File sourceFile =
                GeoPackageFormat.getFileFromSource(getClass().getResource("Blue_Marble.gpkg"));
        GeoPackage geopkg = new GeoPackage(sourceFile);
        List<TileEntry> tiles = geopkg.tiles();

        // Get 0,0,0 tile
        Tile topLeftTile = geopkg.reader(tiles.get(0), 0, 0, 0, 0, 0, 0).next();

        BufferedImage tileImg = ImageIO.read(new ByteArrayInputStream(topLeftTile.getData()));
        ImageAssert.assertEquals(
                URLs.urlToFile(getClass().getResource("bluemarble_0_0_0.jpeg")), tileImg, 250);

        // Render the GeoPackage at zoom level 0
        GeoPackageReader reader =
                new GeoPackageReader(getClass().getResource("Blue_Marble.gpkg"), null);

        GeneralParameterValue[] parameters = new GeneralParameterValue[1];
        GridGeometry2D gg =
                new GridGeometry2D(
                        new GridEnvelope2D(new Rectangle(1536, 768)),
                        new ReferencedEnvelope(-180, 180, -90, 90, CRS.decode("EPSG:4326", true)));
        parameters[0] = new Parameter<GridGeometry2D>(AbstractGridFormat.READ_GRIDGEOMETRY2D, gg);
        GridCoverage2D gc = reader.read("bluemarble_tif_tiles", parameters);
        BufferedImage img = ((PlanarImage) gc.getRenderedImage()).getAsBufferedImage();

        // ImageIO.write(img, "JPEG", new File("bluemarblerendered.jpeg"));

        // Get top left tile
        BufferedImage topLeftImg = new BufferedImage(256, 256, img.getType());
        Graphics2D graphics = topLeftImg.createGraphics();
        graphics.drawImage(
                img, 0, 0, 256, 256, // Destination coordinates
                0, 0, 256, 256, // Source coordinates
                null);

        // ImageIO.write(topLeftImg, "JPEG", new File("bluemarbletopleft.jpeg"));
        ImageAssert.assertEquals(
                URLs.urlToFile(getClass().getResource("bluemarble_0_0_0.jpeg")), topLeftImg, 250);
    }

    void assertTiles(List<Tile> tiles, TileReader r) throws IOException {
        for (Tile t : tiles) {
            assertTrue(r.hasNext());

            Tile a = r.next();
            assertEquals(t, a);
        }
        assertFalse(r.hasNext());
        r.close();
    }

    void assertContentEntry(Entry entry) throws Exception {
        Connection cx = geopkg.getDataSource().getConnection();
        try {
            PreparedStatement ps =
                    cx.prepareStatement("SELECT * FROM gpkg_contents WHERE table_name = ?");
            ps.setString(1, entry.getTableName());

            ResultSet rs = ps.executeQuery();
            assertTrue(rs.next());

            assertEquals(entry.getIdentifier(), rs.getString("identifier"));
            assertEquals(entry.getDescription(), rs.getString("description"));
            assertEquals(entry.getSrid().intValue(), rs.getInt("srs_id"));

            assertEquals(entry.getBounds().getMinX(), rs.getDouble("min_x"), 0.1);
            assertEquals(entry.getBounds().getMinY(), rs.getDouble("min_y"), 0.1);
            assertEquals(entry.getBounds().getMaxX(), rs.getDouble("max_x"), 0.1);
            assertEquals(entry.getBounds().getMaxY(), rs.getDouble("max_y"), 0.1);

            rs.close();
            ps.close();
        } finally {
            cx.close();
        }
    }

    void assertFeatureEntry(FeatureEntry entry) throws Exception {
        assertContentEntry(entry);

        Connection cx = geopkg.getDataSource().getConnection();
        try {
            PreparedStatement ps =
                    cx.prepareStatement("SELECT * FROM gpkg_geometry_columns WHERE table_name = ?");
            ps.setString(1, entry.getTableName());

            ResultSet rs = ps.executeQuery();
            assertTrue(rs.next());

            assertEquals(entry.getGeometryColumn(), rs.getString("column_name"));
            assertEquals(
                    entry.getGeometryType(),
                    Geometries.getForName(rs.getString("geometry_type_name")));
            assertEquals(entry.getSrid().intValue(), rs.getInt("srs_id"));
            assertEquals(entry.isZ(), rs.getBoolean("z"));
            assertEquals(entry.isM(), rs.getBoolean("m"));

            rs.close();
            ps.close();
        } finally {
            cx.close();
        }
    }

    void assertTileEntry(TileEntry entry) throws Exception {
        assertContentEntry(entry);

        Connection cx = geopkg.getDataSource().getConnection();
        try {
            PreparedStatement ps =
                    cx.prepareStatement(
                            "SELECT count(*) from gpkg_tile_matrix WHERE table_name = ?");
            ps.setString(1, entry.getTableName());
            ResultSet rs = ps.executeQuery();

            rs.next();
            assertEquals(rs.getInt(1), entry.getTileMatricies().size());

            rs.close();
            ps.close();

            ps = cx.prepareStatement("SELECT * from gpkg_tile_matrix_set WHERE table_name = ?");
            ps.setString(1, entry.getTableName());
            rs = ps.executeQuery();

            rs.next();
            assertEquals(rs.getInt(2), entry.getSrid().intValue());
            assertEquals(rs.getDouble(3), entry.getTileMatrixSetBounds().getMinX(), 0.01);
            assertEquals(rs.getDouble(4), entry.getTileMatrixSetBounds().getMinY(), 0.01);
            assertEquals(rs.getDouble(5), entry.getTileMatrixSetBounds().getMaxX(), 0.01);
            assertEquals(rs.getDouble(6), entry.getTileMatrixSetBounds().getMaxY(), 0.01);

            assertFalse(rs.next());

            rs.close();
            ps.close();

            // index
            ps = cx.prepareStatement("SELECT * from sqlite_master WHERE type='index' and name = ?");
            ps.setString(1, entry.getTableName() + "_zyx_idx");
            rs = ps.executeQuery();

            rs.close();
            ps.close();
        } finally {
            cx.close();
        }
    }

    void assertSimilar(SimpleFeature expected, SimpleFeature actual) {
        assertNotNull(actual);

        assertTrue(
                ((Geometry) expected.getDefaultGeometry())
                        .equals(((Geometry) actual.getDefaultGeometry())));
        for (AttributeDescriptor d : expected.getType().getAttributeDescriptors()) {
            Object e = expected.getAttribute(d.getLocalName());
            Object a = actual.getAttribute(d.getLocalName());

            if (e instanceof Number) {
                assertEquals(((Number) e).intValue(), ((Number) a).intValue());
            } else {
                assertEquals(e, a);
            }
        }
    }

    URL setUpShapefile() throws Exception {
        File d = File.createTempFile("bugsites", "shp", new File("target"));
        d.delete();
        d.mkdirs();

        String[] exts = new String[] {"shp", "shx", "dbf", "prj"};
        for (String ext : exts) {
            if ("prj".equals(ext)) {
                String wkt = CRS.decode("EPSG:26713", true).toWKT();
                FileUtils.writeStringToFile(new File(d, "bugsites.prj"), wkt);
            } else {
                FileUtils.copyURLToFile(
                        TestData.url("shapes/bugsites." + ext), new File(d, "bugsites." + ext));
            }
        }

        return URLs.fileToUrl(new File(d, "bugsites.shp"));
    }

    URL setUpBoolShapefile() throws Exception {
        File d = File.createTempFile("BooleanTest", "shp", new File("target"));
        d.delete();
        d.mkdirs();

        String[] exts = new String[] {"shp", "shx", "dbf", "prj"};
        for (String ext : exts) {
            if ("prj".equals(ext)) {
                String wkt = CRS.decode("EPSG:4326", true).toWKT();
                FileUtils.writeStringToFile(new File(d, "BooleanTest.prj"), wkt);
            } else {
                FileUtils.copyURLToFile(
                        TestData.url("shapes/BooleanTest." + ext),
                        new File(d, "BooleanTest." + ext));
            }
        }

        return URLs.fileToUrl(new File(d, "BooleanTest.shp"));
    }

    URL setUpGeoTiff() throws IOException {
        File d = File.createTempFile("world", "tiff", new File("target"));
        d.delete();
        d.mkdirs();

        FileUtils.copyURLToFile(TestData.url("geotiff/world.tiff"), new File(d, "world.tiff"));
        return URLs.fileToUrl(new File(d, "world.tiff"));
    }

    URL setUpPNG() throws IOException {
        File d = File.createTempFile("Pk50095", "png", new File("target"));
        d.delete();
        d.mkdirs();

        FileUtils.copyURLToFile(TestData.url(this, "Pk50095.png"), new File(d, "Pk50095.png"));
        FileUtils.copyURLToFile(TestData.url(this, "Pk50095.pgw"), new File(d, "Pk50095.pgw"));
        return URLs.fileToUrl(new File(d, "Pk50095.png"));
    }
}
