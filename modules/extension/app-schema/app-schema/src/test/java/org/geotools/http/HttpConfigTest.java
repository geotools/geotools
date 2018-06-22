/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.http;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.geotools.data.DataAccess;
import org.geotools.data.DataAccessFinder;
import org.geotools.data.complex.AppSchemaDataAccess;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.xml.resolver.SchemaCache;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.type.Name;

/**
 * This class contains tests that validate the mappings files obtained through http and the
 * correspondent includes are correctly handled.
 */
public final class HttpConfigTest {

    private static final HttpStaticServer HTTP_SERVER = new HttpStaticServer();

    private static final File APP_SCHEMA_CACHE_DIR =
            new File(FileUtils.getTempDirectory(), "app-schema-cache");

    @BeforeClass
    public static void setup() {
        // setup schemas cache
        System.setProperty(SchemaCache.PROVIDED_CACHE_LOCATION_KEY, APP_SCHEMA_CACHE_DIR.getPath());
        // start HTTP static server and load the necessary resources
        HTTP_SERVER.start();
        HTTP_SERVER.putResource(
                "measurements_http_includes.xml", "/test-data/http/measurements_http_includes.xml");
        HTTP_SERVER.putResource(
                "measurements_relative_includes.xml",
                "/test-data/http/measurements_relative_includes.xml");
        HTTP_SERVER.putResource(
                "stations_http_includes.xml", "/test-data/http/stations_http_includes.xml");
        HTTP_SERVER.putResource(
                "stations_relative_includes.xml", "/test-data/http/stations_relative_includes.xml");
        HTTP_SERVER.putResource("stations.xsd", "/test-data/http/stations.xsd");
    }

    @AfterClass
    public static void teardown() {
        try {
            HTTP_SERVER.stop();
        } finally {
            // remove the test schema cache
            FileUtils.deleteQuietly(APP_SCHEMA_CACHE_DIR);
        }
    }

    @Test
    public void testHttpIncludes() throws Exception {
        // included types are specified with a full HTTP URL
        testHttpMapping("stations_http_includes.xml");
    }

    @Test
    public void testRelativeIncludes() throws Exception {
        // included types are specified relatively
        testHttpMapping("stations_relative_includes.xml");
    }

    /**
     * Helper method that tests that mappings files obtained through an hTTP request are correctly
     * handled, as well the included types and schemas.
     */
    private void testHttpMapping(String mappingFileName) throws Exception {
        // instantiate a store for the mapping file
        AppSchemaDataAccess store = buildAppSchemaDataStore(mappingFileName);
        // check that a stations type is available
        assertThat(store.getTypeNames().length, is(1));
        Name name = new NameImpl("http://www.stations.org/1.0", "Station");
        assertThat(store.getTypeNames()[0], is(name));
        // read the complex features
        FeatureIterator iterator = store.getFeatureSource(name).getFeatures().features();
        List<Feature> features = new ArrayList<>();
        while (iterator.hasNext()) {
            features.add(iterator.next());
        }
        // three stations should be available
        assertThat(features.size(), is(3));
        for (Feature feature : features) {
            // check that each station has at least a measurement
            Name measurements = new NameImpl("http://www.stations.org/1.0", "measurements");
            Property property = feature.getProperty(measurements);
            assertThat(property.getValue(), instanceOf(List.class));
            List values = (List) property.getValue();
            assertThat(values.size() > 0, is(true));
        }
    }

    /**
     * Static method that cna be sued to start the HTTP static server in the background, this only
     * exists for debug proposes.
     */
    public static void runHttpStaticServer() {
        // start the HTTP static server in the background
        setup();
        HTTP_SERVER.join();
    }

    /**
     * Helper method that instantiates a new App-Schema data store using the provided mapping file
     * name.
     */
    private static AppSchemaDataAccess buildAppSchemaDataStore(String mappingsName) {
        // create the parameters map
        Map<String, Serializable> parameters = new HashMap<>();
        parameters.put("dbtype", "app-schema");
        parameters.put("url", HTTP_SERVER.buildUrl(mappingsName));
        try {
            // instantiate the App-Schema data store
            DataAccess dataAccess = DataAccessFinder.getDataStore(parameters);
            return (AppSchemaDataAccess) dataAccess;
        } catch (Exception exception) {
            throw new RuntimeException(
                    String.format(
                            "Error build App-Schema data store for mappings '%s'.", mappingsName),
                    exception);
        }
    }
}
