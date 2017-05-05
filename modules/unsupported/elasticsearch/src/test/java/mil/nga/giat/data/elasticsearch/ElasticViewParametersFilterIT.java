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

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.factory.Hints;
import org.junit.Test;

import static org.junit.Assert.*;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsEqualTo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;

public class ElasticViewParametersFilterIT extends ElasticTestSupport {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testNativeTermQuery() throws Exception {
        init("not-active");
        Map<String, String> vparams = new HashMap<String, String>();
        Map<String,Object> query = ImmutableMap.of("term", ImmutableMap.of("security_ss", "WPA"));
        vparams.put("q", mapper.writeValueAsString(query));
        Hints hints = new Hints(Hints.VIRTUAL_TABLE_PARAMETERS, vparams);
        Query q = new Query(featureSource.getSchema().getTypeName());
        q.setHints(hints);
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter = ff.equals(ff.property("speed_is"), ff.literal("300"));
        q.setFilter(filter);
        ContentFeatureCollection features = featureSource.getFeatures(q);
        assertEquals(1, features.size());
        SimpleFeatureIterator fsi = features.features();
        assertTrue(fsi.hasNext());
        assertEquals(fsi.next().getID(), "active.12");
    }

    @Test
    public void testEncodedNativeTermQuery() throws Exception {
        init("not-active");
        Map<String, String> vparams = new HashMap<String, String>();
        Map<String,Object> query = ImmutableMap.of("term", ImmutableMap.of("security_ss", "WPA"));
        vparams.put("q", URLEncoder.encode(mapper.writeValueAsString(query), "UTF-8"));
        Hints hints = new Hints(Hints.VIRTUAL_TABLE_PARAMETERS, vparams);
        Query q = new Query(featureSource.getSchema().getTypeName());
        q.setHints(hints);
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter = ff.equals(ff.property("speed_is"), ff.literal("300"));
        q.setFilter(filter);
        ContentFeatureCollection features = featureSource.getFeatures(q);
        assertEquals(1, features.size());
        SimpleFeatureIterator fsi = features.features();
        assertTrue(fsi.hasNext());
        assertEquals(fsi.next().getID(), "active.12");
    }

    @Test
    public void testNativeBooleanQuery() throws Exception {
        init();
        Map<String, String> vparams = new HashMap<String, String>();
        Map<String,Object> query = ImmutableMap.of("bool", ImmutableMap.of("must", 
                ImmutableMap.of("term", ImmutableMap.of("security_ss", "WPA")),
                "must_not", ImmutableMap.of("term", ImmutableMap.of("modem_b", true))));
        vparams.put("q", mapper.writeValueAsString(query));
        Hints hints = new Hints(Hints.VIRTUAL_TABLE_PARAMETERS, vparams);
        Query q = new Query(featureSource.getSchema().getTypeName());
        q.setHints(hints);
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter = ff.equals(ff.property("speed_is"), ff.literal("300"));
        q.setFilter(filter);
        ContentFeatureCollection features = featureSource.getFeatures(q);
        assertEquals(2, features.size());
        SimpleFeatureIterator fsi = features.features();
        assertTrue(fsi.hasNext());
        assertEquals(fsi.next().getAttribute("modem_b"), false);
        assertTrue(fsi.hasNext());
        assertEquals(fsi.next().getAttribute("modem_b"), false);
    }

    @Test
    public void testNativeAggregation() throws Exception {
        init();
        Map<String, String> vparams = new HashMap<String, String>();
        Map<String,Object> query = ImmutableMap.of("agg", ImmutableMap.of("geohash_grid", 
                ImmutableMap.of("field", "geo", "precision", 3)));
        vparams.put("a", mapper.writeValueAsString(query));
        Hints hints = new Hints(Hints.VIRTUAL_TABLE_PARAMETERS, vparams);
        Query q = new Query(featureSource.getSchema().getTypeName());
        q.setHints(hints);
        ContentFeatureCollection features = featureSource.getFeatures(q);
        assertFalse(features.isEmpty());
        SimpleFeatureIterator fsi = features.features();
        assertTrue(fsi.hasNext());
        assertNotNull(fsi.next().getAttribute("_aggregation"));
    }

}
