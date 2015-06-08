package org.geotools.data.store;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import org.geotools.feature.NameImpl;
import org.junit.Test;
import org.opengis.feature.type.Name;

public class ContentDataStoreTest extends AbstractContentTest {
    
    protected static final Name TYPENAME2 = new NameImpl("http://www.geotools.org", "Mock2");

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

        // we used to keep on calling createTypeNames
        store.getFeatureSource(TYPENAME.getLocalPart());
        assertEquals(1, creationCounter.get());
    }

    @Test
    public void testCallCreateTypeNamesOnce() throws IOException {
        // setup a store in which we can count how many times we call createTypeNames()
        final AtomicInteger creationCounter = new AtomicInteger(0);
        MockContentDataStore store = new MockContentDataStore() {
            protected java.util.List<org.opengis.feature.type.Name> createTypeNames()
                    throws java.io.IOException {
                creationCounter.incrementAndGet();
                return Arrays.asList(TYPENAME, TYPENAME2);
            };
        };

        store.getFeatureSource(TYPENAME.getLocalPart());
        assertEquals(1, creationCounter.get());

        // we used to keep on calling createTypeNames
        store.getFeatureSource(TYPENAME2.getLocalPart());
        assertEquals(1, creationCounter.get());
    }

}
