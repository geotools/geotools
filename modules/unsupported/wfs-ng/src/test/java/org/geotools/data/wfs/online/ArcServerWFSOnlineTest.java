/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.online;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.test.OnlineTestSupport;
import org.geotools.util.factory.GeoTools;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

/** @author ian */
public class ArcServerWFSOnlineTest extends OnlineTestSupport {

    @Test
    public void testArcMapWFSFilter_V1_get() throws IOException, NoSuchElementException {
        arcMapTest("1.0.0", true);
    }

    @Test
    public void testArcMapWFSFilter_V1_post() throws IOException, NoSuchElementException {
        arcMapTest("1.0.0", false);
    }

    @Test
    public void testArcMapWFSFilter_V1_1_get() throws IOException, NoSuchElementException {
        arcMapTest("1.1.0", true);
    }

    @Test
    public void testArcMapWFSFilter_V1_1_post() throws IOException, NoSuchElementException {
        arcMapTest("1.1.0", false);
    }

    @Test
    public void testArcMapWFSFilter_V2_get() throws IOException, NoSuchElementException {
        arcMapTest("2.0.0", true);
    }

    @Test
    public void testArcMapWFSFilter_V2_post() throws IOException, NoSuchElementException {
        arcMapTest("2.0.0", false);
    }

    /** */
    private void arcMapTest(String version, boolean get) throws IOException {

        String getCapabilities =
                "https://gis-erd-der.gnb.ca/server/services/OpenData/ARMS/MapServer/WFSServer?service=wfs&request=getcapabilities&version="
                        + version;
        Map<String, Object> connectionParameters = new HashMap<>();
        connectionParameters.put(WFSDataStoreFactory.URL.key, getCapabilities);
        connectionParameters.put(WFSDataStoreFactory.LENIENT.key, Boolean.TRUE);
        connectionParameters.put(WFSDataStoreFactory.WFS_STRATEGY.key, "arcgis");
        connectionParameters.put(WFSDataStoreFactory.TIMEOUT.key, 30);
        //
        if (get) {
            connectionParameters.put(WFSDataStoreFactory.PROTOCOL.key, Boolean.FALSE);
        }
        // Attempt to connect to the datastore.
        WFSDataStore data = (WFSDataStore) DataStoreFinder.getDataStore(connectionParameters);
        assertEquals(version, data.getInfo().getVersion());
        String[] typeNames = data.getTypeNames();
        String typeName = typeNames[0];
        SimpleFeatureSource source = data.getFeatureSource(typeName);

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints());
        Filter filter = ff.like(ff.property("NBAFR"), "YON%", "%", ".", "!");

        Query query = new Query();
        query.setTypeName(typeName);

        query.setFilter(filter);
        SimpleFeatureCollection features = source.getFeatures(query);
        int size = features.size();
        assertTrue("Wrong number of features", size > 10);
        // Iterator through all the features and print them out.
        int count = 0;
        try (SimpleFeatureIterator iterator = features.features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                assertThat((String) feature.getAttribute("NBAFR"), CoreMatchers.startsWith("YON"));
                count++;
            }
        }
        assertTrue(count > 10);

        // check max-features is working properly
        query.setMaxFeatures(10);
        features = source.getFeatures(query);
        size = features.size();
        assertEquals(10, size);
        // Iterator through all the features and print them out.
        count = 0;
        try (SimpleFeatureIterator iterator = features.features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                assertThat((String) feature.getAttribute("NBAFR"), CoreMatchers.startsWith("YON"));
                count++;
            }
        }
        assertEquals(10, count);

        // set up an un-encodable filter using property selection
        query.setPropertyNames(Arrays.asList("NBAFR"));
        Filter functionFilter =
                ff.greater(ff.function("abs", ff.property("OBJECTID")), ff.literal(50));
        query.setFilter(ff.and(filter, functionFilter));
        features = source.getFeatures(query);
        SimpleFeatureType schema = features.getSchema();
        assertEquals(1, schema.getAttributeCount());
        assertEquals("NBAFR", schema.getDescriptor(0).getLocalName());
        count = 0;
        try (SimpleFeatureIterator iterator = features.features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                assertThat((String) feature.getAttribute("NBAFR"), CoreMatchers.startsWith("YON"));
                count++;
            }
        }
        assertEquals(10, count);
    }

    @Override
    protected String getFixtureId() {
        return "arcgis-wfs";
    }

    @Override
    protected Properties createExampleFixture() {
        Properties template = new Properties();
        template.put(WFSDataStoreFactory.URL.key, "this is currently hardcoded in");
        template.put(WFSDataStoreFactory.LENIENT.key, "true");
        template.put(WFSDataStoreFactory.TIMEOUT.key, "5");

        return template;
    }
}
