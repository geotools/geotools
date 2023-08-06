/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.elasticsearch;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import org.junit.Test;

/** Tests for {@link ElasticDataStoreFactory}. */
public class ElasticDataStoreFactoryIT extends ElasticTestSupport {

    @Test(expected = IOException.class)
    public void testExceedsResponseBufferLimit() throws Exception {
        Map<String, Serializable> params = createConnectionParams();
        params.put(ElasticDataStoreFactory.RESPONSE_BUFFER_LIMIT.key, 1);
        ElasticDataStoreFactory factory = new ElasticDataStoreFactory();
        ElasticDataStore dataStore = (ElasticDataStore) factory.createDataStore(params);
        try (ElasticClient elasticClient = dataStore.getClient()) {
            createIndices(elasticClient, "test1");
        } finally {
            dataStore.dispose();
        }
    }

    @Test
    public void testLessThanResponseBufferLimit() throws Exception {
        Map<String, Serializable> params = createConnectionParams();
        params.put(ElasticDataStoreFactory.RESPONSE_BUFFER_LIMIT.key, 1000);
        ElasticDataStoreFactory factory = new ElasticDataStoreFactory();
        ElasticDataStore dataStore = (ElasticDataStore) factory.createDataStore(params);
        try (ElasticClient elasticClient = dataStore.getClient()) {
            createIndices(elasticClient, "test2");
            assertTrue(elasticClient.getTypes(indexName).size() > 0);
        } finally {
            dataStore.dispose();
        }
    }
}
