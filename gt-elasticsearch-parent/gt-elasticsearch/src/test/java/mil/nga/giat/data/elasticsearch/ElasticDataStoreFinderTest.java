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

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.factory.FactoryCreator;
import org.geotools.factory.FactoryRegistry;
import org.junit.Test;

public class ElasticDataStoreFinderTest extends ElasticTestSupport {

    protected static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(ElasticDataStoreFinderTest.class);

    private DataStore source;

    @Test
    public void testFactoryDefaults() throws IOException {
        Map<String,Serializable> params = createConnectionParams();
        ElasticDataStoreFactory factory = new ElasticDataStoreFactory();
        dataStore = (ElasticDataStore) factory.createDataStore(params);

        ElasticDataStoreFactory fac = new ElasticDataStoreFactory();
        assertTrue(fac.getDisplayName().equals(ElasticDataStoreFactory.DISPLAY_NAME));
        assertTrue(fac.getDescription().equals(ElasticDataStoreFactory.DESCRIPTION));
        assertTrue(fac.getParametersInfo().equals(ElasticDataStoreFactory.PARAMS));
        assertTrue(fac.getImplementationHints()==null);
        assertTrue(fac.createNewDataStore(null)==null);
    }

    @Test
    public void testFactoryWithTransportClient() throws IOException {
        assertTrue(new ElasticDataStoreFactory().isAvailable());
        scanForPlugins();

        Map<String,Serializable> map = new HashMap<>();
        map.put(ElasticDataStoreFactory.HOSTNAME.key, "localhost");
        map.put(ElasticDataStoreFactory.HOSTPORT.key, String.valueOf(9300));
        map.put(ElasticDataStoreFactory.INDEX_NAME.key, "sample");

        Iterator<DataStoreFactorySpi> ps = getAvailableDataSources();
        ElasticDataStoreFactory fac;
        while (ps.hasNext()) {
            fac = (ElasticDataStoreFactory) ps.next();

            try {
                if (fac.canProcess(map)) {
                    source = fac.createDataStore(map);
                }
            } catch (Throwable t) {
                LOGGER.log(Level.WARNING, "Could not acquire " + fac.getDescription() + ":" + t, t);
            }
        }

        assertNotNull(source);
        assertTrue(source instanceof ElasticDataStore);
    }

    @Test
    public void testFactoryWithSearchIndices() throws IOException {
        assertTrue(new ElasticDataStoreFactory().isAvailable());
        scanForPlugins();

        Map<String,Serializable> map = new HashMap<>();
        map.put(ElasticDataStoreFactory.HOSTNAME.key, "localhost");
        map.put(ElasticDataStoreFactory.HOSTPORT.key, String.valueOf(9300));
        map.put(ElasticDataStoreFactory.INDEX_NAME.key, "sample");
        map.put(ElasticDataStoreFactory.SEARCH_INDICES.key, "sample1,sample2");

        Iterator<DataStoreFactorySpi> ps = getAvailableDataSources();
        ElasticDataStoreFactory fac;
        while (ps.hasNext()) {
            fac = (ElasticDataStoreFactory) ps.next();

            try {
                if (fac.canProcess(map)) {
                    source = fac.createDataStore(map);
                }
            } catch (Throwable t) {
                LOGGER.log(Level.WARNING, "Could not acquire " + fac.getDescription() + ":" + t, t);
            }
        }

        assertNotNull(source);
        assertTrue(source instanceof ElasticDataStore);
        assertTrue(((ElasticDataStore) source).getSearchIndices().equals("sample1,sample2"));
    }

    @Test
    public void testFactoryWithMissingRequired() throws IOException {
        assertTrue(new ElasticDataStoreFactory().isAvailable());
        scanForPlugins();

        Map<String,Serializable> map = new HashMap<>();
        map.put(ElasticDataStoreFactory.HOSTNAME.key, "localhost");
        map.put(ElasticDataStoreFactory.HOSTPORT.key, String.valueOf(9300));
        map.put(ElasticDataStoreFactory.SEARCH_INDICES.key, "sample1,sample2");

        Iterator<DataStoreFactorySpi> ps = getAvailableDataSources();
        ElasticDataStoreFactory fac;
        while (ps.hasNext()) {
            fac = (ElasticDataStoreFactory) ps.next();
            assertTrue(!fac.canProcess(map));
        }

        assertNull(source);
    }

    private FactoryRegistry getServiceRegistry() {
        FactoryRegistry registry = new FactoryCreator(
                Arrays.asList(new Class<?>[] { DataStoreFactorySpi.class }));
        return registry;
    }

    private void scanForPlugins() {
        getServiceRegistry().scanForPlugins();
    }

    public Iterator<DataStoreFactorySpi> getAvailableDataSources() {
        Set<DataStoreFactorySpi> availableDS = new HashSet<>();
        Iterator<DataStoreFactorySpi> it = getServiceRegistry().getServiceProviders(DataStoreFactorySpi.class, null,
                null);
        ElasticDataStoreFactory dsFactory;
        while (it.hasNext()) {
            Object ds = it.next();
            if (ds instanceof ElasticDataStoreFactory) {
                dsFactory = (ElasticDataStoreFactory) ds;
                if (dsFactory.isAvailable()) {
                    availableDS.add(dsFactory);
                }
            }
        }
        return availableDS.iterator();
    }

}
