package org.geotools.geopkg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.geotools.TestData;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.data.simple.SimpleFeatureWriter;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.gce.image.WorldImageFormat;
import org.geotools.gce.image.WorldImageReader;
import org.geotools.geometry.jts.Geometries;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geopkg.Entry;
import org.geotools.geopkg.FeatureEntry;
import org.geotools.geopkg.Features;
import org.geotools.geopkg.GeoPackage;
import org.geotools.geopkg.RasterEntry;
import org.geotools.geopkg.Tile;
import org.geotools.geopkg.TileEntry;
import org.geotools.geopkg.TileMatrix;
import org.geotools.geopkg.TileReader;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.FilterFactory;

import com.vividsolutions.jts.geom.Geometry;

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

        //for debugging, copy the current geopackage file to a well known file
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
        assertApplicationId();
    }
    
    void assertApplicationId() throws Exception {
        Connection cx = geopkg.getDataSource().getConnection();
        Statement st = cx.createStatement();
        try {
            ResultSet rs = st.executeQuery("PRAGMA application_id;");
            assertEquals(rs.getInt(1), 0x47503130);
        }
        catch(Exception e) {
            fail(e.getMessage());
        }
        finally {
            st.close();
            cx.close();
        }
    }

    void assertTableExists(String table) throws Exception {
        Connection cx = geopkg.getDataSource().getConnection();
        Statement st = cx.createStatement();
        try {
            st.execute(String.format("SELECT count(*) FROM %s;", table));
        }
        catch(Exception e) {
            fail(e.getMessage());
        }
        finally {
            st.close();
            cx.close();
        }
    }

    @Test
    public void testCreateFeatureEntry() throws Exception {
        ShapefileDataStore shp = new ShapefileDataStore(setUpShapefile());

        FeatureEntry entry = new FeatureEntry();
        geopkg.add(entry, shp.getFeatureSource(), null);

        assertTableExists("bugsites");

        //check metadata contents
        assertFeatureEntry(entry);
        
        SimpleFeatureReader re = Features.simple(shp.getFeatureReader());
        SimpleFeatureReader ra = geopkg.reader(entry, null, null);

        while(re.hasNext()) {
            assertTrue(ra.hasNext());
            assertSimilar(re.next(), ra.next());
        }

        re.close();
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
            while(re.hasNext()) {
                SimpleFeature f = re.next();
                ResultSet rs = st.executeQuery((String.format(
                        "SELECT ST_MinX(the_geom), ST_MinY(the_geom), ST_MaxX(the_geom), ST_MaxY(the_geom), ST_IsEmpty(the_geom) FROM bugsites WHERE ID=" + f.getProperty("ID").getValue())));
                assertEquals(rs.getDouble(1), ((Geometry) f.getDefaultGeometry()).getEnvelopeInternal().getMinX(), 0.0001 );
                assertEquals(rs.getDouble(2), ((Geometry) f.getDefaultGeometry()).getEnvelopeInternal().getMinY(), 0.0001 );
                assertEquals(rs.getDouble(3), ((Geometry) f.getDefaultGeometry()).getEnvelopeInternal().getMaxX(), 0.0001 );
                assertEquals(rs.getDouble(4), ((Geometry) f.getDefaultGeometry()).getEnvelopeInternal().getMaxY(), 0.0001 );
                assertEquals(rs.getDouble(5)==1, ((Geometry) f.getDefaultGeometry()).isEmpty() );
                rs.close();
            }
        }
        catch(Exception e) {
            fail(e.getMessage());
        }
        finally {
            st.close();
            cx.close();
            re.close();
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
            while(re.hasNext()) {
                SimpleFeature f = re.next();
                ResultSet rs = st.executeQuery((String.format(
                        "SELECT ST_MinX(the_geom), ST_MinY(the_geom), ST_MaxX(the_geom), ST_MaxY(the_geom), ST_IsEmpty(the_geom) FROM bugsites WHERE ID=" + f.getProperty("ID").getValue())));
                assertEquals(rs.getDouble(1), ((Geometry) f.getDefaultGeometry()).getEnvelopeInternal().getMinX(), 0.0001 );
                assertEquals(rs.getDouble(2), ((Geometry) f.getDefaultGeometry()).getEnvelopeInternal().getMinY(), 0.0001 );
                assertEquals(rs.getDouble(3), ((Geometry) f.getDefaultGeometry()).getEnvelopeInternal().getMaxX(), 0.0001 );
                assertEquals(rs.getDouble(4), ((Geometry) f.getDefaultGeometry()).getEnvelopeInternal().getMaxY(), 0.0001 );
                assertEquals(rs.getDouble(5)==1, ((Geometry) f.getDefaultGeometry()).isEmpty() );
                rs.close();
            }
        }
        catch(Exception e) {
            fail(e.getMessage());
        }
        finally {
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
        
        //write some features before and some after
        SimpleFeatureIterator it = coll.features();
        
        //some features
        Transaction tx = new DefaultTransaction();
        SimpleFeatureWriter w = geopkg.writer(entry, true, null, tx);
        for (int i=0; i<3; i++) {
            SimpleFeature f = it.next(); 
            SimpleFeature g = w.next();
            for (PropertyDescriptor pd : coll.getSchema().getDescriptors()) {
                String name = pd.getName().getLocalPart();
                g.setAttribute(name, f.getAttribute(name));
            }
                                         
            w.write();
        }
        tx.commit();
        tx.close();
        w.close();
        
        //create spatial index
        geopkg.createSpatialIndex(entry);
                

        //the rest of features
        tx = new DefaultTransaction();
        w = geopkg.writer(entry, true, null, tx);        
        while(it.hasNext()) {
            SimpleFeature f = it.next(); 
            SimpleFeature g = w.next();
            for (PropertyDescriptor pd : coll.getSchema().getDescriptors()) {
                String name = pd.getName().getLocalPart();
                g.setAttribute(name, f.getAttribute(name));
            }
                                         
            w.write();
        }
        tx.commit();
        tx.close();
        w.close();
        
        it.close();
        
        //test if the index was properly created
              
        Connection cx = geopkg.getDataSource().getConnection();
        Statement st = cx.createStatement();
        try {            
            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM rtree_bugsites_the_geom");
            rs.next();
            
            assertEquals(rs.getInt(1), coll.size());
        }
        catch(Exception e) {
            fail(e.getMessage());
        }
        finally {
            st.close();
            cx.close();
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
        SimpleFeatureReader sfr = geopkg.reader(entry, ff.id(ids), null);
        
        assertTrue(sfr.hasNext());
        assertEquals("bugsites.1", sfr.next().getID().toString());
        assertFalse(sfr.hasNext());       
    }

    @Test
    public void testCreateRasterEntry() throws Exception {
        GeoTiffFormat format = new GeoTiffFormat();
        GeoTiffReader reader = format.getReader(setUpGeoTiff());
        GridCoverage2D cov = reader.read(null);

        RasterEntry entry = new RasterEntry();
        entry.setTableName("world");

        geopkg.add(entry, cov, format);

        assertTableExists("world");
        assertRasterEntry(entry);

        GridCoverageReader r = geopkg.reader(entry, format);
        GridCoverage2D c = (GridCoverage2D) r.read(null);
        assertNotNull(c);
    }

    @Test @Ignore
    public void testCreateRasterEntryPNG() throws Exception {
        //TODO: re-enable this test once we can pass in teh bounds to a world image
        WorldImageFormat format = new WorldImageFormat();
        WorldImageReader reader = format.getReader(setUpPNG());
        GridCoverage2D cov = reader.read(null);

        RasterEntry entry = new RasterEntry();
        entry.setTableName("Pk50095");

        geopkg.add(entry, cov, format);
        assertTableExists("Pk50095");
        assertRasterEntry(entry);

        GridCoverageReader r = geopkg.reader(entry, format);
        GridCoverage2D c = (GridCoverage2D) r.read(null);
        assertNotNull(c);
    }

    @Test
    public void testCreateTileEntry() throws Exception {
        TileEntry e = new TileEntry();
        e.setTableName("foo");
        e.setBounds(new ReferencedEnvelope(-180,180,-90,90,DefaultGeographicCRS.WGS84));
        e.getTileMatricies().add(new TileMatrix(0, 1, 1, 256, 256, 0.1, 0.1));
        e.getTileMatricies().add(new TileMatrix(1, 2, 2, 256, 256, 0.1, 0.1));

        geopkg.create(e);
        assertTileEntry(e);

        List<Tile> tiles = new ArrayList();
        tiles.add(new Tile(0,0,0,new byte[]{0}));
        tiles.add(new Tile(1,0,0,new byte[]{1}));
        tiles.add(new Tile(1,0,1,new byte[]{2}));
        tiles.add(new Tile(1,1,0,new byte[]{3}));
        tiles.add(new Tile(1,1,1,new byte[]{4}));

        for (Tile t : tiles) {
            geopkg.add(e, t);
        }

        TileReader r = geopkg.reader(e, null, null, null, null, null, null);
        assertTiles(tiles, r);
    }

    @Test
    public void testListEntries() throws Exception {
        testCreateFeatureEntry();
        testCreateRasterEntry();
        testCreateTileEntry();

        List<FeatureEntry> lf = geopkg.features();
        assertEquals(1, lf.size());
        assertEquals("bugsites", lf.get(0).getTableName());

        List<RasterEntry> lr = geopkg.rasters();
        assertEquals(1, lr.size());
        assertEquals("world", lr.get(0).getTableName());

        List<TileEntry> lt = geopkg.tiles();
        assertEquals(1, lt.size());
        
        TileEntry te = lt.get(0);
        assertEquals("foo", te.getTableName());
        assertEquals(2, te.getTileMatricies().size());
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
        }
        finally {
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
            assertEquals(entry.getGeometryType(), Geometries.getForName(rs.getString("geometry_type_name")));
            assertEquals(entry.getSrid().intValue(), rs.getInt("srs_id"));
            assertEquals(entry.isZ(), rs.getBoolean("z"));
            assertEquals(entry.isM(), rs.getBoolean("m"));

            rs.close();
            ps.close();
        }
        finally {
            cx.close();
        }
    }

    void assertRasterEntry(RasterEntry entry) throws Exception {
        assertContentEntry(entry);
        
        Connection cx = geopkg.getDataSource().getConnection();
        try {
            PreparedStatement ps = 
                cx.prepareStatement("SELECT * FROM gpkg_data_columns WHERE table_name = ?");
            ps.setString(1, entry.getTableName());

            ResultSet rs = ps.executeQuery();
            assertTrue(rs.next());

            assertEquals(entry.getRasterColumn(), rs.getString("column_name"));
            assertEquals(entry.getTableName(), rs.getString("table_name"));
            assertEquals(entry.getName(), rs.getString("name"));
            assertEquals(entry.getTitle(), rs.getString("title"));

            rs.close();
            ps.close();
        }
        finally {
            cx.close();
        }
    }

    void assertTileEntry(TileEntry entry) throws Exception {
        assertContentEntry(entry);
        
        Connection cx = geopkg.getDataSource().getConnection();
        try {
            PreparedStatement ps = cx.prepareStatement(
                "SELECT count(*) from gpkg_tile_matrix WHERE table_name = ?");
            ps.setString(1, entry.getTableName());
            ResultSet rs = ps.executeQuery();

            rs.next();
            assertEquals(rs.getInt(1), entry.getTileMatricies().size());
            
            rs.close();
            ps.close();
            
            ps = cx.prepareStatement(
                    "SELECT * from gpkg_tile_matrix_set WHERE table_name = ?");
            ps.setString(1, entry.getTableName());
            rs = ps.executeQuery();

            rs.next();
            assertEquals(rs.getInt(2), entry.getSrid().intValue());
            assertEquals(rs.getDouble(3), entry.getBounds().getMinX(), 0.01);
            assertEquals(rs.getDouble(4), entry.getBounds().getMinY(), 0.01);
            assertEquals(rs.getDouble(5), entry.getBounds().getMaxX(), 0.01);
            assertEquals(rs.getDouble(6), entry.getBounds().getMaxY(), 0.01);
            
            assertFalse(rs.next());

            rs.close();
            ps.close();
            
            //index
            ps = cx.prepareStatement(
                    "SELECT * from sqlite_master WHERE type='index' and name = ?");
            ps.setString(1, entry.getTableName() + "_zyx_idx");
            rs = ps.executeQuery();

            rs.close();
            ps.close();
        }
        finally {
            cx.close();
        }
    }

    void assertSimilar(SimpleFeature expected, SimpleFeature actual) {
        assertNotNull(actual);

        assertTrue(((Geometry)expected.getDefaultGeometry()).equals(
            ((Geometry)actual.getDefaultGeometry())));
        for (AttributeDescriptor d : expected.getType().getAttributeDescriptors()) {
            Object e = expected.getAttribute(d.getLocalName());
            Object a = actual.getAttribute(d.getLocalName());

            if (e instanceof Number) {
                assertEquals(((Number) e).intValue(), ((Number)a).intValue());
            }
            else {
                assertEquals(e, a);
            }
        }
    }

    URL setUpShapefile() throws IOException {
        File d = File.createTempFile("bugsites", "shp", new File("target"));
        d.delete();
        d.mkdirs();

        String[] exts = new String[]{"shp", "shx", "dbf", "prj"};
        for (String ext : exts) {
            FileUtils.copyURLToFile(TestData.url("shapes/bugsites." + ext), 
                new File(d, "bugsites." + ext));
        }
        
        return DataUtilities.fileToURL(new File(d, "bugsites.shp"));
    }

    URL setUpGeoTiff() throws IOException {
        File d = File.createTempFile("world", "tiff", new File("target"));
        d.delete();
        d.mkdirs();

        FileUtils.copyURLToFile(TestData.url("geotiff/world.tiff"), new File(d, "world.tiff"));
        return DataUtilities.fileToURL(new File(d, "world.tiff")); 
    }

    URL setUpPNG() throws IOException {
        File d = File.createTempFile("Pk50095", "png", new File("target"));
        d.delete();
        d.mkdirs();

        FileUtils.copyURLToFile(TestData.url(this, "Pk50095.png"), new File(d, "Pk50095.png"));
        FileUtils.copyURLToFile(TestData.url(this, "Pk50095.pgw"), new File(d, "Pk50095.pgw"));
        return DataUtilities.fileToURL(new File(d, "Pk50095.png")); 
    }
}
