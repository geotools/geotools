package org.geotools.data.directory;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStore;
import org.junit.Before;
import org.junit.Test;

public class DirectoryDataStoreFactoryTest extends TestSupport {
    private static final String DESTDIR = "dir with spaces";

    @Before
    public void setup() throws Exception {
        copyFile("Bridges.properties", DESTDIR);
        File f = copyFile("Buildings.properties", DESTDIR);
        tempDir = f.getParentFile();
    }
    
    @Test
    public void testSpaces() throws Exception {
        Map params = new HashMap();
        params.put(DirectoryDataStoreFactory.URLP.key, tempDir.toURI().toURL());
        params.put(DirectoryDataStoreFactory.NAMESPACE.key, "http://www.geotools.org");
        DirectoryDataStoreFactory factory = new DirectoryDataStoreFactory();
        assertTrue(factory.canProcess(params));
        DataStore store = factory.createDataStore(params);
        assertNotNull(store);
        assertEquals(2, store.getNames().size());
    }
    
    @Test
    public void testSpacesPlainToURL() throws Exception {
        Map params = new HashMap();
        params.put(DirectoryDataStoreFactory.URLP.key, tempDir.toURI().toURL());
        params.put(DirectoryDataStoreFactory.NAMESPACE.key, "http://www.geotools.org");
        DirectoryDataStoreFactory factory = new DirectoryDataStoreFactory();
        assertTrue(factory.canProcess(params));
        assertNotNull(factory.createDataStore(params));
    }
}
