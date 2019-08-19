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
package org.geotools.data.solr;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import junit.framework.TestCase;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.util.factory.FactoryCreator;
import org.geotools.util.factory.FactoryRegistry;

public class SolrDataStoreFinderTest extends TestCase {

    protected static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(SolrDataStoreFinderTest.class);

    private DataStore source;

    public void testDbcpFactory() throws IOException {
        assertTrue(new SolrDataStoreFactory().isAvailable());
        scanForPlugins();

        Map map = new HashMap();
        map.put(SolrDataStoreFactory.URL.key, new URL("http://localhost:8080/solr"));
        map.put(SolrDataStoreFactory.FIELD.key, "layer_type");
        map.put(SolrDataStoreFactory.NAMESPACE.key, "namesapce");

        Iterator<DataStoreFactorySpi> ps = getAvailableDataSources().iterator();
        while (ps.hasNext()) {
            DataStoreFactorySpi fac = ps.next();

            try {
                if (fac.canProcess(map)) {
                    source = fac.createDataStore(map);
                }
            } catch (Throwable t) {
                LOGGER.log(Level.WARNING, "Could not acquire " + fac.getDescription() + ":" + t, t);
            }
        }

        assertNotNull(source);
        assertTrue(source instanceof SolrDataStore);
    }

    private FactoryRegistry getServiceRegistry() {
        FactoryRegistry registry =
                new FactoryCreator(Arrays.asList(new Class<?>[] {DataStoreFactorySpi.class}));
        return registry;
    }

    private void scanForPlugins() {
        getServiceRegistry().scanForPlugins();
    }

    public Stream<DataStoreFactorySpi> getAvailableDataSources() {
        return getServiceRegistry()
                .getFactories(DataStoreFactorySpi.class, null, null)
                .filter(ds -> ds instanceof SolrDataStoreFactory)
                .filter(DataStoreFactorySpi::isAvailable);
    }
}
