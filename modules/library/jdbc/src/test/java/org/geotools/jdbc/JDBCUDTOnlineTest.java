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

import java.util.HashMap;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public abstract class JDBCUDTOnlineTest extends JDBCTestSupport {

    @Override
    protected abstract JDBCUDTTestSetup createTestSetup();

    public void testSchema() throws Exception {
        SimpleFeatureType type = dataStore.getSchema(tname("udt"));
        assertNotNull(type);
        assertNotNull(type.getDescriptor(aname("ut")));

        assertEquals(String.class, type.getDescriptor(aname("ut")).getType().getBinding());
    }

    public void testRead() throws Exception {
        SimpleFeatureType type = dataStore.getSchema(tname("udt"));

        SimpleFeatureCollection features = dataStore.getFeatureSource(tname("udt")).getFeatures();
        try (SimpleFeatureIterator fi = features.features()) {
            assertTrue(fi.hasNext());
            assertEquals("12ab", fi.next().getAttribute(aname("ut")));
            assertFalse(fi.hasNext());
        }
    }

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
    protected HashMap createDataStoreFactoryParams() throws Exception {
        HashMap params = super.createDataStoreFactoryParams();
        // Set the batch insert size in order to be sure the failures happens while write is called.
        params.put(JDBCDataStoreFactory.BATCH_INSERT_SIZE.key, 1);
        return params;
    }
}
