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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import mil.nga.giat.data.elasticsearch.ElasticDataStore;
import mil.nga.giat.data.elasticsearch.ElasticDataStoreFactory;

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
    public void testDbcpFactory() throws IOException {
        assertTrue(new ElasticDataStoreFactory().isAvailable());
        scanForPlugins();

        Map<String,Serializable> map = new HashMap<>();
        map.put(ElasticDataStoreFactory.HOSTNAME.key, "localhost");
        map.put(ElasticDataStoreFactory.HOSTPORT.key, String.valueOf(port));
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
