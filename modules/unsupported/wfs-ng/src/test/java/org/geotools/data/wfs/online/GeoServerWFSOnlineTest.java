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
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.test.OnlineTestSupport;
import org.geotools.util.factory.GeoTools;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsEqualTo;

/** @author ian */
public class GeoServerWFSOnlineTest extends OnlineTestSupport {

    /** All states with LAND_KM between 100,000 and 150,000 */
    TreeSet<String> expected =
            new TreeSet<>(
                    Arrays.asList(
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
                            "states.23",
                            "states.24",
                            "states.30",
                            "states.36",
                            "states.39",
                            "states.40",
                            "states.48"));

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

    /** */
    private void geoServerTest(String version, boolean get) throws IOException {
        Properties fixture = getFixture();

        String getCapabilities =
                fixture.getProperty(
                                WFSDataStoreFactory.URL.key, "http://localhost:8080/geoserver/wfs?")
                        + "REQUEST=GetCapabilities&SERVICE=WFS&VERSION="
                        + version;

        Map<String, Object> connectionParameters = new HashMap<>();
        connectionParameters.put(WFSDataStoreFactory.URL.key, getCapabilities);
        connectionParameters.put(
                WFSDataStoreFactory.LENIENT.key,
                Boolean.valueOf(fixture.getProperty(WFSDataStoreFactory.LENIENT.key, "true")));
        connectionParameters.put(
                WFSDataStoreFactory.TIMEOUT.key,
                Integer.valueOf(fixture.getProperty(WFSDataStoreFactory.TIMEOUT.key, "30")));

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

        // simple filter
        query.setFilter(filter);
        SimpleFeatureCollection features = source.getFeatures(query);
        assertEquals(expected.size(), features.size());
        assertEquals(expected, collectIds(features));

        // adding paging
        query.setMaxFeatures(10);
        features = source.getFeatures(query);
        assertEquals(10, features.size());
        assertTrue(expected.containsAll(collectIds(features)));

        // and now a non-encodable filter too, to check how it interacts with paging
        // (only 2 features match the filter)
        PropertyIsEqualTo functionFilter =
                ff.equal(
                        ff.function(
                                "strSubstring",
                                ff.property("STATE_NAME"),
                                ff.literal(0),
                                ff.literal(1)),
                        ff.literal("A"),
                        true);
        query.setFilter(ff.and(filter, functionFilter));
        features = source.getFeatures(query);
        assertEquals(2, features.size());
        TreeSet<String> expectedFF = new TreeSet<>(Arrays.asList("states.17", "states.21"));
        assertEquals(expectedFF, collectIds(features));

        // finally, make sure that property selection works with a non-encodable filter
        // (the attribute chosen is not part of either filters)
        query.setPropertyNames(new String[] {"STATE_ABBR"});
        features = source.getFeatures(query);
        SimpleFeatureType schema = features.getSchema();
        assertEquals(1, schema.getAttributeCount());
        assertEquals("STATE_ABBR", schema.getDescriptor(0).getLocalName());
        Set<String> names =
                DataUtilities.list(features).stream()
                        .map(f -> (String) f.getAttribute("STATE_ABBR"))
                        .collect(Collectors.toSet());
        assertEquals(new TreeSet<>(Arrays.asList("AR", "AL")), names);
    }

    private static Set<String> collectIds(SimpleFeatureCollection features) {
        return DataUtilities.list(features).stream()
                .map(f -> f.getID())
                .collect(Collectors.toSet());
    }

    @Override
    protected String getFixtureId() {
        return "geoserver-wfs";
    }

    /** Create placeholder fixture, the use of localhost is hardcoded anyways */
    @Override
    protected Properties createExampleFixture() {
        Properties template = new Properties();
        template.put(WFSDataStoreFactory.URL.key, "http://localhost:8080/geoserver/wfs?");
        template.put(WFSDataStoreFactory.LENIENT.key, "true");
        template.put(WFSDataStoreFactory.TIMEOUT.key, "5");

        return template;
    }
}
