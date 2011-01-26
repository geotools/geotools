package org.geotools.data.directory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;

import org.junit.Test;


public class DataStoreCacheTest extends TestSupport {
    
    private static final String DESTDIR = "shapes";
    // we need a long delay for builds under UNIX, the timestap is coarse
    // (on windows it worked with 100ms)
    private static final int DELAY = 1000;

    @Test
    public void testInitialization() throws Exception {
        copyFile("Bridges.properties", DESTDIR);
        copyFile("Buildings.properties", DESTDIR);
        copyShapefiles("shapes/archsites.shp");
        File f = copyShapefiles("shapes/bugsites.shp");
        tempDir = f.getParentFile();
        
        DirectoryTypeCache cache = new DirectoryTypeCache(tempDir, NSURI);
        assertEquals(4, cache.getTypeNames().size());
        assertTrue(cache.getTypeNames().contains("archsites"));
        assertTrue(cache.getTypeNames().contains("bugsites"));
        assertTrue(cache.getTypeNames().contains("Bridges"));
        assertTrue(cache.getTypeNames().contains("Buildings"));
    }
    
    @Test
    public void testAddNewDataStore() throws Exception {
        File f = copyShapefiles("shapes/bugsites.shp");
        tempDir = f.getParentFile();
        DirectoryTypeCache cache = new DirectoryTypeCache(tempDir, NSURI);
        
        assertEquals(1, cache.getTypeNames().size());
        assertTrue(cache.getTypeNames().contains("bugsites"));
        
        // give the os some time, the directory last modification
        // time has a os specific time resolution
        Thread.sleep(DELAY);
        copyShapefiles("shapes/archsites.shp");
        assertEquals(2, cache.getTypeNames().size());
        assertTrue(cache.getTypeNames().contains("bugsites"));
        assertTrue(cache.getTypeNames().contains("archsites"));
    }
    
    @Test
    public void testRemoveDataStore() throws Exception {
        File f = copyShapefiles("shapes/bugsites.shp");
        copyShapefiles("shapes/archsites.shp");
        tempDir = f.getParentFile();
        DirectoryTypeCache cache = new DirectoryTypeCache(tempDir, NSURI);
        
        assertEquals(2, cache.getTypeNames().size());
        assertTrue(cache.getTypeNames().contains("bugsites"));
        assertTrue(cache.getTypeNames().contains("archsites"));
        
        // give the os some time, the directory last modification
        // time has a os specific time resolution
        Thread.sleep(DELAY);
        assertTrue(new File(tempDir, "archsites.shp").delete());
        assertTrue(new File(tempDir, "archsites.dbf").delete());
        assertTrue(new File(tempDir, "archsites.shx").delete());
        System.out.println(Arrays.asList(tempDir.listFiles()));
        assertEquals(1, cache.getTypeNames().size());
        assertTrue(cache.getTypeNames().contains("bugsites"));
    }
    
    @Test
    public void testRemoveType() throws Exception {
        copyFile("Bridges.properties", DESTDIR);
        File f = copyFile("Buildings.properties", DESTDIR);
        tempDir = f.getParentFile();
        DirectoryTypeCache cache = new DirectoryTypeCache(tempDir, NSURI);
        assertEquals(2, cache.getTypeNames().size());
        assertTrue(cache.getTypeNames().contains("Bridges"));
        assertTrue(cache.getTypeNames().contains("Buildings"));
        
        // give the os some time, the directory last modification
        // time has a os specific time resolution
        Thread.sleep(DELAY);
        assertTrue(new File(tempDir, "Buildings.properties").delete());
        assertEquals(1, cache.getTypeNames().size());
        assertTrue(cache.getTypeNames().contains("Bridges"));
    }
    
    @Test
    public void testAddType() throws Exception {
        File f = copyFile("Buildings.properties", DESTDIR);
        tempDir = f.getParentFile();
        DirectoryTypeCache cache = new DirectoryTypeCache(tempDir, NSURI);
        assertEquals(1, cache.getTypeNames().size());
        assertTrue(cache.getTypeNames().contains("Buildings"));
        
        // give the os some time, the directory last modification
        // time has a os specific time resolution
        Thread.sleep(DELAY);
        copyFile("Bridges.properties", DESTDIR);
        assertEquals(2, cache.getTypeNames().size());
        assertTrue(cache.getTypeNames().contains("Bridges"));
        assertTrue(cache.getTypeNames().contains("Buildings"));
    }

}

