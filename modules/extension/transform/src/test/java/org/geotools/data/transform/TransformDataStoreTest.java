package org.geotools.data.transform;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;

import org.geotools.data.DataStore;
import org.geotools.data.ServiceInfo;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.transform.SingleFeatureSourceDataStore;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.type.Name;

public class TransformDataStoreTest extends AbstractTransformTest {

    private DataStore ds;
    private SimpleFeatureSource transformed;

    @Before
    public void before() throws Exception {
        transformed = transformWithSelection();
        ds = (DataStore) transformed.getDataStore();
    }

    @Test
    public void testStoreSourceRelationship() throws Exception {
        assertTrue(ds instanceof SingleFeatureSourceDataStore);
        assertSame(transformed, ds.getFeatureSource(transformed.getSchema().getTypeName()));
    }
    
    @Test
    public void testTypeNames() throws Exception {
        String[] typeNames = ds.getTypeNames();
        assertEquals(1, typeNames.length);
        assertEquals("states_mini", typeNames[0]);
    }
    
    @Test
    public void testGetSchema() throws Exception {
        assertEquals(transformed.getSchema(), ds.getSchema("states_mini"));
        try {
            ds.getSchema("ImNotThere");
            fail("Should have thrown an exception");
        } catch(IOException e) {
            // fine
        }
    }
    
    @Test
    public void testGetFeatureSource() throws Exception {
        assertSame(transformed, ds.getFeatureSource("states_mini"));
        try {
            ds.getFeatureSource("ImNotThere");
            fail("Should have thrown an exception");
        } catch(IOException e) {
            // fine
        }
    }
    
    @Test
    public void testInfo() throws Exception {
        ServiceInfo info = ds.getInfo();
        Name name = transformed.getSchema().getName();
        assertEquals("Features from " + name,  info.getDescription());
        assertEquals(name.toString(),  info.getTitle());
        assertEquals(new URI(name.getNamespaceURI()),  info.getSchema());
    }
    
    @Test
    public void testLockingManager() throws Exception {
        // there is no way to extract a locking manager from a feature source
        assertNull(ds.getLockingManager());
    }
    
}
