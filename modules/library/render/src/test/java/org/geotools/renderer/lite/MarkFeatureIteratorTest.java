/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.renderer.lite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import org.geotools.data.Query;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.util.DefaultProgressListener;
import org.geotools.renderer.lite.MarkFeatureIterator.DiskMarkFeatureIterator;
import org.geotools.renderer.lite.MarkFeatureIterator.MemoryMarkFeatureIterator;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;

public class MarkFeatureIteratorTest {

    SimpleFeatureSource zroads;

    @Before
    public void setUp() throws Exception {
        File property = new File(TestData.getResource(this, "zorder/zroads.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        zroads = ds.getFeatureSource("zroads");
    }

    @Test
    public void testResetOnDisk() throws Exception {
        testReset(0);
    }

    @Test
    public void testResetInMemory() throws Exception {
        testReset(1000);
    }

    @Test
    public void testCanceled() throws Exception {
        DefaultProgressListener listener = new DefaultProgressListener();
        listener.setCanceled(true);
        MarkFeatureIterator iterator =
                MarkFeatureIterator.create(zroads.getFeatures(), 1000, listener);
        assertNull(iterator);
    }

    private void testReset(int limit) throws IOException {
        DefaultProgressListener listener = new DefaultProgressListener();
        try (MarkFeatureIterator iterator =
                MarkFeatureIterator.create(zroads.getFeatures(), limit, listener)) {
            if (limit >= zroads.getCount(Query.ALL)) {
                assertTrue(iterator instanceof MemoryMarkFeatureIterator);
            } else {
                assertTrue(iterator instanceof DiskMarkFeatureIterator);
            }

            iterator.mark();
            assertThreeFeatures(iterator);
            iterator.reset();
            assertThreeFeatures(iterator);
        }
    }

    private void assertThreeFeatures(MarkFeatureIterator iterator) {
        assertTrue(iterator.hasNext());
        assertEquals("Line.1", iterator.next().getIdentifier().getID());
        assertTrue(iterator.hasNext());
        assertEquals("Line.2", iterator.next().getIdentifier().getID());
        assertTrue(iterator.hasNext());
        assertEquals("Line.3", iterator.next().getIdentifier().getID());
        assertFalse(iterator.hasNext());
    }
}
