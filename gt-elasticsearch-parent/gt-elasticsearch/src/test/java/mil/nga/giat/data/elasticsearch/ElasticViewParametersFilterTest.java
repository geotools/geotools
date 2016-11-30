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

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.index.query.QueryBuilders;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.factory.Hints;
import org.junit.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsEqualTo;

public class ElasticViewParametersFilterTest extends ElasticTestSupport {

    @Test
    public void testSinglesQParameters() throws Exception {
        init("not-active");
        Map<String, String> vparams = new HashMap<String, String>();
        vparams.put("q", QueryBuilders.termQuery("security_ss", "WPA").toString());
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
        assertEquals(fsi.next().getID(), "not-active.12");
    }

    @Test
    public void testMultipleQParameters() throws Exception {
        init();
        Map<String, String> vparams = new HashMap<String, String>();
        vparams.put("q", QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("security_ss", "WPA"))
                .mustNot(QueryBuilders.termQuery("modem_b", true)).toString());
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
    public void testSinglesFQParameters() throws Exception {
        init("not-active");
        Map<String, String> vparams = new HashMap<String, String>();
        vparams.put("f", QueryBuilders.termQuery("security_ss", "WPA").toString());
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
        assertEquals(fsi.next().getID(), "not-active.12");
    }

    @Test
    public void testMultipleFQParameters() throws Exception {
        init();
        Map<String, String> vparams = new HashMap<String, String>();
        vparams.put("f", QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("security_ss", "WPA"))
                .must(QueryBuilders.termQuery("modem_b", true)).toString());
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
        assertEquals(fsi.next().getAttribute("modem_b"), true);
    }

    @Test
    public void testMixQandFQParameters() throws Exception {
        init();
        Map<String, String> vparams = new HashMap<String, String>();
        vparams.put("q", QueryBuilders.termQuery("security_ss", "WPA").toString());
        vparams.put("f", QueryBuilders.termQuery("modem_b", true).toString());
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
        assertEquals(fsi.next().getAttribute("modem_b"), true);
    }

}
