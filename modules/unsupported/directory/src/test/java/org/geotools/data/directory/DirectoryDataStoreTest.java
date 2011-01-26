package org.geotools.data.directory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
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
public class DirectoryDataStoreTest extends TestSupport {
    
    private static final String DESTDIR = "shapes";
    
    @Test
    public void testTypeNames() throws Exception {
        copyFile("Bridges.properties", DESTDIR);
        copyFile("Buildings.properties", DESTDIR);
        copyShapefiles("shapes/archsites.shp");
        File f = copyShapefiles("shapes/bugsites.shp");
        tempDir = f.getParentFile();
        
        DataStore store = new DirectoryDataStore(tempDir, NSURI);
        List<String> typeNames = Arrays.asList(store.getTypeNames());
        assertEquals(4, typeNames.size());
        assertTrue(typeNames.contains("archsites"));
        assertTrue(typeNames.contains("bugsites"));
        assertTrue(typeNames.contains("Bridges"));
        assertTrue(typeNames.contains("Buildings"));
    }
    
    @Test
    public void testSchema() throws Exception {
        File prop = copyFile("Buildings.properties", DESTDIR);
        tempDir = prop.getParentFile();
        
        DataStore pds = new PropertyDataStore(tempDir, NSURI.toString());
        DataStore dds = new DirectoryDataStore(tempDir, NSURI);
       
        assertEquals(1, dds.getTypeNames().length);
        assertEquals(pds.getSchema("Buildings"), dds.getSchema("Buildings"));
        pds.dispose();
        dds.dispose();
    }
    
    @Test
    public void testFeatureSource() throws Exception {
        File prop = copyFile("Buildings.properties", DESTDIR);
        tempDir = prop.getParentFile();
        
        DataStore dds = new DirectoryDataStore(tempDir, NSURI);
        FeatureSource fs = dds.getFeatureSource("Buildings");
        assertNotNull(fs);
        assertSame(dds, fs.getDataStore());
    }

    @Test
    public void testCreateSchema() throws Exception {
        File dir = File.createTempFile("foo", "shp", new File("target"));
        dir.delete();
        dir.mkdir();
        
        DataStore ds = new DirectoryDataStore(dir, NSURI);
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
    
}
