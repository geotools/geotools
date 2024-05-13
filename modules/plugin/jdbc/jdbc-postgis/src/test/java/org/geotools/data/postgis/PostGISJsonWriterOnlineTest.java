/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import org.geotools.api.data.Query;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.Filter;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.data.store.ContentFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.junit.Test;

/**
 * Tests SQL INSERT and UPDATE for PostGIS JSON and JSONB typed columns using regular statements
 * (=not prepared).
 *
 * @author awaterme
 */
public class PostGISJsonWriterOnlineTest extends JDBCTestSupport {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new PostGISJsonTestSetup();
    }

    @Test
    public void testInsertUpdateReload() throws Exception {
        String typeName = tname("jsontest");
        ContentFeatureSource source = dataStore.getFeatureSource(typeName);
        ContentFeatureStore store = (ContentFeatureStore) source;

        Query query = new Query(typeName);
        SimpleFeatureCollection collection = store.getFeatures(query);
        String id = null;
        Filter filter = null;

        try (SimpleFeatureIterator iter = collection.features()) {
            SimpleFeature next = iter.next();
            SimpleFeatureBuilder builder = new SimpleFeatureBuilder(next.getFeatureType());
            builder.init(next);
            id = "" + (collection.size() + 1);
            SimpleFeature newFeature = builder.buildFeature(id);
            newFeature.setAttribute(aname("name"), "insert");
            newFeature.setAttribute(aname("jsonColumn"), "{\"json\": \"insert\"}");
            newFeature.setAttribute(aname("jsonbColumn"), "{\"jsonb\": \"insert\"}");
            store.addFeatures(Collections.singletonList(newFeature));

            filter = dataStore.getFilterFactory().id(newFeature.getIdentifier());
            String[] names = {aname("name"), aname("jsonColumn"), aname("jsonbColumn")};
            String[] values = {"update", "{\"json\": \"update\"}", "{\"jsonb\": \"update\"}"};
            store.modifyFeatures(names, values, filter);
        }
        assertEquals(1, source.getFeatures(filter).size());
        try (SimpleFeatureIterator iter = source.getFeatures(filter).features()) {
            SimpleFeature next = iter.next();
            assertEquals("update", next.getAttribute(aname("name")));
            String json = next.getAttribute(aname("jsonbColumn")).toString();
            assertTrue("json should contain 'update': " + json, json.contains("update"));
        }
    }
}
