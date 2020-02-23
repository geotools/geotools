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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeSet;
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
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

/** @author ian */
public class ArcServerWFSOnlineTest extends OnlineTestSupport {

    TreeSet<String> expected = new TreeSet<>();

    @Before
    public void setup() {
        String[] expectedA = {
            "USGS_TNM_Structures.10",
            "USGS_TNM_Structures.16",
            "USGS_TNM_Structures.101",
            "USGS_TNM_Structures.147",
            "USGS_TNM_Structures.182",
            "USGS_TNM_Structures.205",
            "USGS_TNM_Structures.212",
            "USGS_TNM_Structures.220",
            "USGS_TNM_Structures.289",
            "USGS_TNM_Structures.366"
        };
        expected.addAll(Arrays.asList(expectedA));
    }

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
                "http://cartowfs.nationalmap.gov/arcgis/services/structures/MapServer/WFSServer?request=GetCapabilities&service=WFS&version="
                        + version;
        Map<String, Object> connectionParameters = new HashMap<String, Object>();
        connectionParameters.put(WFSDataStoreFactory.URL.key, getCapabilities);
        connectionParameters.put(WFSDataStoreFactory.LENIENT.key, Boolean.TRUE);
        // connectionParameters.put(WFSDataStoreFactory.WFS_STRATEGY.key, "arcgis");
        connectionParameters.put(WFSDataStoreFactory.TIMEOUT.key, 30);
        //
        if (get) {
            connectionParameters.put(WFSDataStoreFactory.PROTOCOL.key, Boolean.FALSE);
        }
        // Attempt to connect to the datastore.
        WFSDataStore data = (WFSDataStore) DataStoreFinder.getDataStore(connectionParameters);
        assertEquals(version, data.getInfo().getVersion());
        String typeNames[] = data.getTypeNames();
        String typeName = typeNames[0];
        SimpleFeatureSource source = data.getFeatureSource(typeName);

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints());
        Filter filter = ff.equals(ff.property("STATE"), ff.literal("MO"));

        Query query = new Query();
        query.setTypeName(typeName);

        query.setFilter(filter);
        SimpleFeatureCollection features = source.getFeatures(query);
        int size = features.size();

        assertTrue(size > 10);
        // Iterator through all the features and print them out.
        int count = 0;
        try (SimpleFeatureIterator iterator = features.features()) {
            while (iterator.hasNext() && count < 10) {
                SimpleFeature feature = iterator.next();
                assertTrue(expected.contains(feature.getID()));
                count++;
            }
        }

        query.setMaxFeatures(10);
        features = source.getFeatures(query);
        size = features.size();
        assertEquals(10, size);
        // Iterator through all the features and print them out.
        try (SimpleFeatureIterator iterator = features.features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                assertTrue(expected.contains(feature.getID()));
            }
        }
    }

    @Override
    protected String getFixtureId() {
        return "arcgis-wfs";
    }
}
