/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ogr;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * 
 *
 * @source $URL$
 */
public class OGRDataStoreFactoryTest extends TestCaseSupport {

    public OGRDataStoreFactoryTest(String name) throws IOException {
        super(name);
    }

    public void testLookup() throws Exception {
        Map<String, Serializable> map = new HashMap<String, Serializable>();
        map.put(OGRDataStoreFactory.OGR_NAME.key, getAbsolutePath(STATE_POP));
        DataStore ds = null;
        try {
            ds = DataStoreFinder.getDataStore(map);
            assertNotNull(ds);
            assertTrue(ds instanceof OGRDataStore);
        } finally {
            disposeQuietly(ds);
        }
    }

    private void disposeQuietly(DataStore ds) {
        if (ds != null) {
            ds.dispose();
        }
    }

    public void testNamespace() throws Exception {
        OGRDataStoreFactory factory = new OGRDataStoreFactory();
        Map<String, Serializable> map = new HashMap<String, Serializable>();
        URI namespace = new URI("http://jesse.com");
        map.put(OGRDataStoreFactory.NAMESPACEP.key, namespace);
        map.put(OGRDataStoreFactory.OGR_NAME.key, getAbsolutePath(STATE_POP));
        DataStore store = null;
        try {
            store = factory.createDataStore(map);
            SimpleFeatureType schema = store.getSchema(
                    STATE_POP.substring(STATE_POP.lastIndexOf('/') + 1,
                            STATE_POP.lastIndexOf('.')));
            assertEquals(namespace.toString(), schema.getName().getNamespaceURI());
        } finally {
            disposeQuietly(store);
        }
    }
    
    public void testNames() throws Exception {
        Set<String> drivers = OGRDataStoreFactory.getAvailableDrivers();
        assertTrue(drivers.size() > 0);
        assertTrue(drivers.contains("ESRI Shapefile"));
    }

}
