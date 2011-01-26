package org.geotools.data.directory;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.data.directory.DirectoryDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Polygon;

/**
 * This one checks proper wrapping and some api methods.
 * For a test of proper listing and updates see the DataStoreCache test
 * @author Andrea Aime - OpenGeo
 *
 *
 * @source $URL$
 */
public class DirectoryDataStoreTest extends DirectoryTestSupport {
    
    private static final String DESTDIR = "shapes";
    
    @Test
    public void testTypeNames() throws Exception {
        copyShapefiles("shapes/archsites.shp");
        File f = copyShapefiles("shapes/bugsites.shp");
        tempDir = f.getParentFile();
        
        DataStore store = new DirectoryDataStore(tempDir, getFileStoreFactory());
        List<String> typeNames = Arrays.asList(store.getTypeNames());
        assertEquals(2, typeNames.size());
        assertTrue(typeNames.contains("archsites"));
        assertTrue(typeNames.contains("bugsites"));
        store.dispose();
    }
    
    @Test
    public void testSchema() throws Exception {
        File file = copyShapefiles("shapes/archsites.shp");
        tempDir = file.getParentFile();
        
        DataStore dds = new DirectoryDataStore(tempDir, getFileStoreFactory());
       
        assertEquals(1, dds.getTypeNames().length);
        assertEquals("archsites", dds.getTypeNames()[0]);
        dds.dispose();
    }
    
    @Test
    @Ignore
    // this test is skipped, it checks something that formally is what we should expect,
    // but for the moment this breaks big time the shapefile renderer optimizations
    // until we get rid of the latter let's be a little lax on this one...
    public void testFeatureSource() throws Exception {
        File file = copyShapefiles("shapes/archsites.shp");
        tempDir = file.getParentFile();
        
        DataStore dds = new DirectoryDataStore(tempDir, getFileStoreFactory());
        FeatureSource fs = dds.getFeatureSource("archsites");
        assertNotNull(fs);
        assertSame(dds, fs.getDataStore());
        dds.dispose();
    }

    @Test
    public void testCreateSchema() throws Exception {
        File dir = File.createTempFile("foo", "shp", new File("target"));
        dir.delete();
        dir.mkdir();
        
        DataStore ds = new DirectoryDataStore(dir, getFileStoreFactory());
        assertEquals(0, ds.getTypeNames().length);
        
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("foo");
        tb.add("geom", Polygon.class);
        tb.add("bar", Integer.class);
        ds.createSchema(tb.buildFeatureType());
        
        SimpleFeatureType ft = ds.getSchema("foo");
        assertNotNull(ft);
        
        //clean up
        ds.dispose();
        for (File f : dir.listFiles()) {
            f.delete();
        }
        dir.delete();
    }
    
    @Test
    public void testFactoryWithoutType() throws Exception {
        copyShapefiles("shapes/archsites.shp");
        File f = copyShapefiles("shapes/bugsites.shp");
        tempDir = f.getParentFile();
        
        Map params = new HashMap();
        params.put(ShapefileDataStoreFactory.URLP.key, DataUtilities.fileToURL(tempDir));
        DataStore store = DataStoreFinder.getDataStore(params);
        
        assertNotNull(store);
        DirectoryDataStore dds = (DirectoryDataStore) store;
        List<String> typeNames = Arrays.asList(dds.getTypeNames());
        assertEquals(2, typeNames.size());
        assertTrue(typeNames.contains("archsites"));
        assertTrue(typeNames.contains("bugsites"));
        dds.dispose();
    }
    
    @Test
    public void testFactoryWithType() throws Exception {
        copyShapefiles("shapes/archsites.shp");
        File f = copyShapefiles("shapes/bugsites.shp");
        tempDir = f.getParentFile();
        
        Map params = new HashMap();
        params.put(ShapefileDataStoreFactory.URLP.key, DataUtilities.fileToURL(tempDir));
        params.put(ShapefileDataStoreFactory.FILE_TYPE.key, "shapefile");
        DataStore store = DataStoreFinder.getDataStore(params);
        
        assertNotNull(store);
        DirectoryDataStore dds = (DirectoryDataStore) store;
        List<String> typeNames = Arrays.asList(dds.getTypeNames());
        assertEquals(2, typeNames.size());
        assertTrue(typeNames.contains("archsites"));
        assertTrue(typeNames.contains("bugsites"));
        dds.dispose();
    }
    
    @Test
    public void testFactoryWithWrongType() throws Exception {
        copyShapefiles("shapes/archsites.shp");
        File f = copyShapefiles("shapes/bugsites.shp");
        tempDir = f.getParentFile();
        
        Map params = new HashMap();
        params.put(ShapefileDataStoreFactory.URLP.key, DataUtilities.fileToURL(tempDir));
        params.put(ShapefileDataStoreFactory.FILE_TYPE.key, "abcdef...");
        DataStore store = DataStoreFinder.getDataStore(params);
        
        assertNull(store);
     }
}
