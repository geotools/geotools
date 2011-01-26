package org.geotools.data.directory;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.shapefile.ShapefileDirectoryFactory;
import org.junit.Before;
import org.junit.Test;

public class DirectoryDataStoreFactoryTest extends DirectoryTestSupport {
    private static final String DESTDIR = "dir with spaces";

    @Before
    public void setup() throws Exception {
        File f = copyShapefiles("shapes/bugsites.shp", DESTDIR);
        copyShapefiles("shapes/archsites.shp", DESTDIR);
        tempDir = f.getParentFile();
    }
    
    @Test
    public void testSpaces() throws Exception {
        Map params = new HashMap();
        params.put(ShapefileDataStoreFactory.URLP.key, tempDir.toURI().toURL());
        params.put(ShapefileDataStoreFactory.NAMESPACEP.key, "http://www.geotools.org");
        ShapefileDirectoryFactory factory = new ShapefileDirectoryFactory();
        assertTrue(factory.canProcess(params));
        DataStore store = factory.createDataStore(params);
        assertNotNull(store);
        assertEquals(2, store.getNames().size());
        store.dispose();
    }
    
    @Test
    public void testSpacesPlainToURL() throws Exception {
        Map params = new HashMap();
        params.put(ShapefileDataStoreFactory.URLP.key, tempDir.toURI().toURL());
        params.put(ShapefileDataStoreFactory.NAMESPACEP.key, "http://www.geotools.org");
        ShapefileDirectoryFactory factory = new ShapefileDirectoryFactory();
        assertTrue(factory.canProcess(params));
        DataStore store = factory.createDataStore(params);
		assertNotNull(store);
		store.dispose();
    }
}
