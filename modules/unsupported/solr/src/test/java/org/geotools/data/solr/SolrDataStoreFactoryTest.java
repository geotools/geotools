/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import static org.geotools.data.solr.SolrDataStoreFactory.FIELD;
import static org.geotools.data.solr.SolrDataStoreFactory.LAYER_MAPPER;
import static org.geotools.data.solr.SolrDataStoreFactory.URL;

import java.util.Map;
import junit.framework.TestCase;
import org.geotools.data.solr.SolrLayerMapper.Type;
import org.geotools.util.KVP;

public class SolrDataStoreFactoryTest extends TestCase {

    SolrDataStoreFactory dataStoreFactory;

    @Override
    protected void setUp() throws Exception {
        dataStoreFactory = new SolrDataStoreFactory();
    }

    public void testDefaultMapper() throws Exception {
        SolrDataStore dataStore =
                (SolrDataStore)
                        dataStoreFactory.createDataStore(
                                (Map)
                                        new KVP(
                                                URL.key,
                                                "http://localhost:8080/solr/geotools",
                                                FIELD.key,
                                                "foo"));
        assertTrue(dataStore.getLayerMapper() instanceof FieldLayerMapper);
    }

    public void testSingleLayerMapper() throws Exception {
        SolrDataStore dataStore =
                (SolrDataStore)
                        dataStoreFactory.createDataStore(
                                (Map)
                                        new KVP(
                                                URL.key,
                                                "http://localhost:8080/solr/geotools",
                                                LAYER_MAPPER.key,
                                                Type.SINGLE.name()));
        assertTrue(dataStore.getLayerMapper() instanceof SingleLayerMapper);
    }
}
