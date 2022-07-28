/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.stac.store;

import static org.geotools.stac.store.STACDataStoreFactory.DBTYPE;
import static org.geotools.stac.store.STACDataStoreFactory.LANDING_PAGE;
import static org.geotools.stac.store.STACDataStoreFactory.NAMESPACE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.stac.STACOfflineTest;
import org.hamcrest.Matchers;
import org.junit.Test;

public class STACDataStoreFactoryTest extends STACOfflineTest {

    private static final String NS_URI = "http://www.geotools.org";

    @Test
    public void testConnect() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put(DBTYPE.key, DBTYPE.sample);
        params.put(LANDING_PAGE.key, LANDING_PAGE_URL);
        params.put(NAMESPACE.key, NS_URI);
        DataStore store = DataStoreFinder.getDataStore(params);
        assertThat(store, Matchers.instanceOf(STACDataStore.class));
        STACDataStore stac = (STACDataStore) store;
        assertEquals(NS_URI, stac.getNamespaceURI());
    }
}
