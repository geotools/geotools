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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.geotools.data.DataStore;

public class SolrDataStoreTest extends SolrTestSupport {

    public void testGetNames() throws IOException {
        String url = fixture.getProperty(SolrDataStoreFactory.URL.key);
        String field = "status_s";

        Map params = new HashMap();
        params.put(SolrDataStoreFactory.URL.key, url);
        params.put(SolrDataStoreFactory.FIELD.key, field);
        params.put(SolrDataStoreFactory.NAMESPACE.key, SolrDataStoreFactory.NAMESPACE.sample);

        SolrDataStoreFactory factory = new SolrDataStoreFactory();
        DataStore dataStore = factory.createDataStore(params);
        String[] typeNames = dataStore.getTypeNames();
        assertTrue(new HashSet(Arrays.asList(typeNames)).contains(("active")));
    }

    @Override
    protected String getFixtureId() {
        return SolrDataStoreFactory.NAMESPACE.sample.toString();
    }
}
