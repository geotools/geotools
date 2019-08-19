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

import java.util.HashMap;
import java.util.Map;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.util.factory.Hints;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsEqualTo;

public class SolrViewParametersTest extends SolrTestSupport {

    public void testSinglesQParameters() throws Exception {
        init("not-active");
        Map<String, String> vparams = new HashMap<String, String>();
        vparams.put("q", "security_ss:WPA");
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

    public void testMultipleQParameters() throws Exception {
        init();
        Map<String, String> vparams = new HashMap<String, String>();
        vparams.put("q", "security_ss:WPA -modem_b:true");
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

    public void testSinglesFQParameters() throws Exception {
        init("not-active");
        Map<String, String> vparams = new HashMap<String, String>();
        vparams.put("fq", "security_ss:WPA");
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

    public void testMultipleFQParameters() throws Exception {
        init();
        Map<String, String> vparams = new HashMap<String, String>();
        vparams.put("fq", "security_ss:WPA -modem_b:true");
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

    public void testMixQandFQParameters() throws Exception {
        init();
        Map<String, String> vparams = new HashMap<String, String>();
        vparams.put("q", "security_ss:WPA");
        vparams.put("fq", "-modem_b:true");
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
}
