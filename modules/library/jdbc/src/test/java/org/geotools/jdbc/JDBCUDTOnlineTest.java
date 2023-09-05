/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.Query;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.junit.Test;

public abstract class JDBCUDTOnlineTest extends JDBCTestSupport {

    @Override
    protected abstract JDBCUDTTestSetup createTestSetup();

    @Test
    public void testSchema() throws Exception {
        SimpleFeatureType type = dataStore.getSchema(tname("udt"));
        assertNotNull(type);
        assertNotNull(type.getDescriptor(aname("ut")));

        assertEquals(String.class, type.getDescriptor(aname("ut")).getType().getBinding());
    }

    @Test
    public void testRead() throws Exception {
        SimpleFeatureCollection features = dataStore.getFeatureSource(tname("udt")).getFeatures();
        try (SimpleFeatureIterator fi = features.features()) {
            assertTrue(fi.hasNext());
            assertEquals("12ab", fi.next().getAttribute(aname("ut")));
            assertFalse(fi.hasNext());
        }
    }

    @Test
    public void testWrite() throws Exception {
        int count = dataStore.getFeatureSource(tname("udt")).getCount(Query.ALL);

        try (FeatureWriter w =
                dataStore.getFeatureWriterAppend(tname("udt"), Transaction.AUTO_COMMIT)) {
            w.hasNext();

            SimpleFeature f = (SimpleFeature) w.next();
            f.setAttribute(aname("ut"), "abcd");
            try {
                w.write();
                fail("Write should have failed with UDT constraint failure");
            } catch (Exception e) {
            }
            f.setAttribute(aname("ut"), "34cd");
            w.write();
        }

        assertEquals(count + 1, dataStore.getFeatureSource(tname("udt")).getCount(Query.ALL));
    }

    @Override
    protected Map<String, Object> createDataStoreFactoryParams() throws Exception {
        Map<String, Object> params = super.createDataStoreFactoryParams();
        // Set the batch insert size in order to be sure the failures happens while write is called.
        params.put(JDBCDataStoreFactory.BATCH_INSERT_SIZE.key, 1);
        return params;
    }
}
