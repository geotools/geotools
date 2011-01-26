package org.geotools.data.directory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;

import org.geotools.data.directory.DirectoryTypeCache;
import org.junit.Test;


public class DataStoreCacheTest extends DirectoryTestSupport {
    
    private static final String DESTDIR = "shapes";
    // we need a long delay for builds under UNIX, the timestap is coarse
    // (on windows it worked with 100ms)
    private static final int DELAY = 1000;

    @Test
    public void testInitialization() throws Exception {
        copyShapefiles("shapes/archsites.shp");
        File f = copyShapefiles("shapes/bugsites.shp");
        tempDir = f.getParentFile();
        
        DirectoryTypeCache cache = new DirectoryTypeCache(tempDir, getFileStoreFactory());
        System.out.println(cache.getTypeNames());
        assertEquals(2, cache.getTypeNames().size());
        assertTrue(cache.getTypeNames().contains("archsites"));
        assertTrue(cache.getTypeNames().contains("bugsites"));
        cache.dispose();
    }
    
    @Test
    public void testAddNewDataStore() throws Exception {
        File f = copyShapefiles("shapes/bugsites.shp");
        tempDir = f.getParentFile();
        DirectoryTypeCache cache = new DirectoryTypeCache(tempDir, getFileStoreFactory());
        
        assertEquals(1, cache.getTypeNames().size());
        assertTrue(cache.getTypeNames().contains("bugsites"));
        
        // give the os some time, the directory last modification
        // time has a os specific time resolution
        Thread.sleep(DELAY);
        copyShapefiles("shapes/archsites.shp");
        assertEquals(2, cache.getTypeNames().size());
        assertTrue(cache.getTypeNames().contains("bugsites"));
        assertTrue(cache.getTypeNames().contains("archsites"));
        cache.dispose();
    }
    
    @Test
    public void testRemoveDataStore() throws Exception {
        File f = copyShapefiles("shapes/bugsites.shp");
        copyShapefiles("shapes/archsites.shp");
        tempDir = f.getParentFile();
        DirectoryTypeCache cache = new DirectoryTypeCache(tempDir, getFileStoreFactory());
        
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
        cache.dispose();
    }
    
    @Test
    public void testRemoveType() throws Exception {
        File f = copyShapefiles("shapes/bugsites.shp");
        copyShapefiles("shapes/archsites.shp");
        tempDir = f.getParentFile();
        DirectoryTypeCache cache = new DirectoryTypeCache(tempDir, getFileStoreFactory());
        assertEquals(2, cache.getTypeNames().size());
        assertTrue(cache.getTypeNames().contains("archsites"));
        assertTrue(cache.getTypeNames().contains("bugsites"));
        
        // give the os some time, the directory last modification
        // time has a os specific time resolution
        Thread.sleep(DELAY);
        assertTrue(new File(tempDir, "archsites.shp").delete());
        assertEquals(1, cache.getTypeNames().size());
        assertTrue(cache.getTypeNames().contains("bugsites"));
        cache.dispose();
    }
    
    @Test
    public void testAddType() throws Exception {
        File f = copyShapefiles("shapes/bugsites.shp");
        tempDir = f.getParentFile();
        DirectoryTypeCache cache = new DirectoryTypeCache(tempDir, getFileStoreFactory());
        assertEquals(1, cache.getTypeNames().size());
        assertTrue(cache.getTypeNames().contains("bugsites"));
        
        // give the os some time, the directory last modification
        // time has a os specific time resolution
        Thread.sleep(DELAY);
        copyShapefiles("shapes/archsites.shp");
        assertEquals(2, cache.getTypeNames().size());
        assertTrue(cache.getTypeNames().contains("bugsites"));
        assertTrue(cache.getTypeNames().contains("archsites"));
        cache.dispose();
    }

}

