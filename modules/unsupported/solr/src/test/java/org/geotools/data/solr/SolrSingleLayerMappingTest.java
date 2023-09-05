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

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.locationtech.jts.geom.Geometry;

public class SolrSingleLayerMappingTest extends SolrTestSupport {

    @Override
    protected void setUpInternal() throws Exception {
        super.setUpInternal();
    }

    @Override
    protected Map<String, Object> createConnectionParams(String url, Properties fixture) {
        Map<String, Object> params = new HashMap<>();
        params.put(SolrDataStoreFactory.URL.key, url);
        params.put(SolrDataStoreFactory.LAYER_MAPPER.key, "SINGLE");
        params.put(SolrDataStoreFactory.NAMESPACE.key, SolrDataStoreFactory.NAMESPACE.sample);

        return params;
    }

    public void testTypeNames() throws Exception {
        String[] names = dataStore.getTypeNames();
        assertEquals(1, names.length);
        assertEquals(names[0], coreName(dataStore));
    }

    public void testSchema() throws Exception {
        init(coreName(dataStore));
        SimpleFeatureType schema = dataStore.getSchema(coreName(dataStore));
        assertNotNull(schema);

        assertNotNull(schema.getGeometryDescriptor());
        assertTrue(schema.getDescriptor("geo") instanceof GeometryDescriptor);
        assertTrue(schema.getDescriptor("geo2") instanceof GeometryDescriptor);
        assertTrue(schema.getDescriptor("geo3") instanceof GeometryDescriptor);
    }

    public void testFeatureSource() throws Exception {
        init(coreName(dataStore));
        SimpleFeatureSource featureSource = dataStore.getFeatureSource(coreName(dataStore));
        assertEquals(13, featureSource.getCount(Query.ALL));

        SimpleFeatureCollection features = featureSource.getFeatures(Query.ALL);
        try (SimpleFeatureIterator it = features.features()) {
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                assertTrue(f.getAttribute("geo") instanceof Geometry);
                assertTrue(f.getAttribute("geo2") instanceof Geometry);
                assertTrue(f.getAttribute("geo3") instanceof Geometry);
            }
        }
    }

    String coreName(SolrDataStore dataStore) {
        String[] split = dataStore.getSolrServer().getBaseURL().split("/");
        return split[split.length - 1];
    }
}
