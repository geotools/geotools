/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package mil.nga.giat.data.elasticsearch;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import mil.nga.giat.data.elasticsearch.ElasticDataStoreFactory;

import org.geotools.data.DataStore;
import org.junit.Test;

public class ElasticDataStoreTest extends ElasticTestSupport {

    @Test
    public void testGetNames() throws IOException {
        Map<String,Serializable> params = createConnectionParams();

        ElasticDataStoreFactory factory = new ElasticDataStoreFactory();
        DataStore dataStore = factory.createDataStore(params);
        String[] typeNames = dataStore.getTypeNames();
        assertTrue(new HashSet<String>(Arrays.asList(typeNames)).contains(("active")));
    }

    @Test
    public void testDefaultSearchIndices() throws IOException {
        Map<String,Serializable> params = createConnectionParams();

        ElasticDataStoreFactory factory = new ElasticDataStoreFactory();
        DataStore dataStore = factory.createDataStore(params);
        String indexName = ((ElasticDataStore) dataStore).getIndexName();
        String searchIndices = ((ElasticDataStore) dataStore).getSearchIndices();
        assertTrue(searchIndices.equals(indexName));
    }

}
