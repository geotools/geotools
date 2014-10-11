package org.geotools.data.store;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

public class ContentDataStoreTest extends AbstractContentTest {
    
    @Test
    public void testRepeatedTypeListCreation() throws IOException {
        // setup a store in which we can count how many times we call createTypeNames()
        final AtomicInteger creationCounter = new AtomicInteger(0);
        MockContentDataStore store = new MockContentDataStore() {
            protected java.util.List<org.opengis.feature.type.Name> createTypeNames()
                    throws java.io.IOException {
                creationCounter.incrementAndGet();
                return super.createTypeNames();
            };
        };
        
        store.getFeatureSource(TYPENAME.getLocalPart());
        assertEquals(1, creationCounter.get());

        // we used to keep no calling createTypeNames
        store.getFeatureSource(TYPENAME.getLocalPart());
        assertEquals(1, creationCounter.get());
    }

}
