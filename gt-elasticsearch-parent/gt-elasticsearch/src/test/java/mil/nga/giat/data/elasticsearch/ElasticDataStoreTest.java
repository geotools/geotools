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
import org.geotools.data.store.ContentFeatureSource;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;

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
    
    @Test
    public void testLayerConfigClone() {
        ElasticLayerConfiguration layerConfig = new ElasticLayerConfiguration("d");
        layerConfig.setLayerName("ln");
        layerConfig.getAttributes().add(new ElasticAttribute("a1"));
        
        ElasticLayerConfiguration layerConfig2 = new ElasticLayerConfiguration(layerConfig);
        assertTrue(layerConfig.getDocType().equals(layerConfig2.getDocType()));
        assertTrue(layerConfig.getLayerName().equals(layerConfig2.getLayerName()));
        assertTrue(layerConfig.getAttributes().equals(layerConfig2.getAttributes()));
    }
    
    @Test
    public void testSchema() throws IOException {
        Map<String,Serializable> params = createConnectionParams();
        ElasticDataStoreFactory factory = new ElasticDataStoreFactory();
        ElasticDataStore dataStore = (ElasticDataStore) factory.createDataStore(params);
        ContentFeatureSource featureSource = dataStore.getFeatureSource("active");
        SimpleFeatureType schema = featureSource.getSchema();
        assertTrue(schema.getAttributeCount() > 0);
        assertNotNull(schema.getDescriptor("speed_is"));
    }
    
    @Test
    public void testSchemaWithValidCustomName() throws Exception {
        init();
        Map<String,Serializable> params = createConnectionParams();
        ElasticDataStoreFactory factory = new ElasticDataStoreFactory();
        ElasticDataStore dataStore = (ElasticDataStore) factory.createDataStore(params);
        ElasticLayerConfiguration config2 = new ElasticLayerConfiguration(config);
        config2.setLayerName("fake");
        dataStore.setLayerConfiguration(config2);
        ContentFeatureSource featureSource = dataStore.getFeatureSource("fake");
        SimpleFeatureType schema = featureSource.getSchema();
        assertTrue(schema.getAttributeCount() > 0);
        assertNotNull(schema.getDescriptor("speed_is"));
    }

}
