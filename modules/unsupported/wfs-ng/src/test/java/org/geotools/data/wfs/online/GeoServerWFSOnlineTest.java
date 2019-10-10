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
public class GeoServerWFSOnlineTest extends OnlineTestSupport {

    TreeSet<String> expected = new TreeSet<>();

    @Before
    public void setup() {
        String[] expectedA = {
            "states.1",
            "states.7",
            "states.9",
            "states.13",
            "states.14",
            "states.17",
            "states.18",
            "states.19",
            "states.21",
            "states.22",
        };
        expected.addAll(Arrays.asList(expectedA));
    }

    @Test
    public void testGeoServerWFSFilter_V1_get() throws IOException, NoSuchElementException {
        geoServerTest("1.0.0", true);
    }

    @Test
    public void testGeoServerWFSFilter_V1_post() throws IOException, NoSuchElementException {
        geoServerTest("1.0.0", false);
    }

    @Test
    public void testGeoServerWFSFilter_V1_1_get() throws IOException, NoSuchElementException {
        geoServerTest("1.1.0", true);
    }

    @Test
    public void testGeoServerWFSFilter_V1_1_post() throws IOException, NoSuchElementException {
        geoServerTest("1.1.0", false);
    }

    @Test
    public void testGeoServerWFSFilter_V2_get() throws IOException, NoSuchElementException {
        geoServerTest("2.0.0", true);
    }

    @Test
    public void testGeoServerWFSFilter_V2_post() throws IOException, NoSuchElementException {
        geoServerTest("2.0.0", false);
    }
    /**
     * @param expected
     * @param version
     * @throws IOException
     */
    private void geoServerTest(String version, boolean get) throws IOException {

        String getCapabilities =
                "http://localhost:8080/geoserver/wfs?request=GetCapabilities&service=WFS&version="
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

        String typeName = "topp:states";
        SimpleFeatureSource source = data.getFeatureSource(typeName);

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints());
        Filter filter =
                ff.between(ff.property("LAND_KM"), ff.literal(100_000), ff.literal(150_000));

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
        return "geoserver-wfs";
    }
}
