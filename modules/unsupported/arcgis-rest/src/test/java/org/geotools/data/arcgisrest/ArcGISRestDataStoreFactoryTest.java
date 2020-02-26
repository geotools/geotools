/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008 - 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.arcgisrest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.DataStore;
import org.geotools.util.logging.Logging;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArcGISRestDataStoreFactoryTest {

    private static final Logger LOGGER = Logging.getLogger(ArcGISRestDataStoreFactoryTest.class);

    public static String URL = "http://data.dhs.opendata.arcgis.com/data.json";
    public static String WSURL =
            "https://services.arcgis.com/B7qHofahIc9hrOqB/arcgis/rest/services/LGA_Profile_2014_(beta)/FeatureServer/0";
    public static String URL_ARCGISSERVER =
            "http://services.arcgis.com/rOo16HdIMeOBI4Mb/ArcGIS/rest/services/Zoning_Data/FeatureServer";
    public static String QUERYURL = WSURL + "/query";
    public static String NAMESPACE = "http://aurin.org.au";
    public static String USER = "testuser";
    public static String PASSWORD = "testpassword";

    private ArcGISRestDataStoreFactory dsf;
    private Map<String, Serializable> params;

    @Before
    public void setUp() throws Exception {
        dsf = new ArcGISRestDataStoreFactory();
        params = new HashMap<String, Serializable>();
    }

    @After
    public void tearDown() throws Exception {
        dsf = null;
        params = null;
    }

    /**
     * Helper method to read a JSON file into a String
     *
     * @param fileName File name to load
     * @return JSON content of the file
     */
    public static String readJSONAsString(String fileName) throws FileNotFoundException {
        Scanner input =
                new Scanner(
                        new File(
                                ArcGISRestDataStoreFactoryTest.class
                                        .getResource(fileName)
                                        .getFile()));
        StringBuilder jsonObj = new StringBuilder();
        while (input.hasNextLine()) {
            jsonObj.append(input.nextLine());
        }
        return jsonObj.toString();
    }

    /**
     * Helper method to read a JSON file into an InputString
     *
     * @param fileName File name to load
     * @return JSON content of the file
     */
    public static InputStream readJSONAsStream(String fileName) throws FileNotFoundException {
        return new FileInputStream(
                new File(ArcGISRestDataStoreFactoryTest.class.getResource(fileName).getFile()));
    }

    /** Helper method to create a default test data store with Open Data catlaog */
    public static DataStore createDefaultArcGISServerTestDataStore() throws IOException {

        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put(ArcGISRestDataStoreFactory.NAMESPACE_PARAM.key, NAMESPACE);
        params.put(ArcGISRestDataStoreFactory.URL_PARAM.key, URL_ARCGISSERVER);
        params.put(ArcGISRestDataStoreFactory.ISOPENDATA_PARAM.key, false);
        params.put(ArcGISRestDataStoreFactory.USER_PARAM.key, USER);
        params.put(ArcGISRestDataStoreFactory.PASSWORD_PARAM.key, PASSWORD);
        return (new ArcGISRestDataStoreFactory()).createDataStore(params);
    }

    /** Helper method to create a default test data store on ArcGIS Server */
    public static DataStore createDefaultOpenDataTestDataStore() throws IOException {

        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put(ArcGISRestDataStoreFactory.NAMESPACE_PARAM.key, NAMESPACE);
        params.put(ArcGISRestDataStoreFactory.URL_PARAM.key, URL);
        params.put(ArcGISRestDataStoreFactory.ISOPENDATA_PARAM.key, true);
        params.put(ArcGISRestDataStoreFactory.USER_PARAM.key, USER);
        params.put(ArcGISRestDataStoreFactory.PASSWORD_PARAM.key, PASSWORD);
        return (new ArcGISRestDataStoreFactory()).createDataStore(params);
    }

    /**
     * Helper method to create a data store
     *
     */
    public DataStore createDataStore(
            final String namespace,
            final String url,
            boolean flag,
            final String user,
            final String password)
            throws IOException {

        params.put(ArcGISRestDataStoreFactory.NAMESPACE_PARAM.key, namespace);
        params.put(ArcGISRestDataStoreFactory.URL_PARAM.key, url);
        params.put(ArcGISRestDataStoreFactory.ISOPENDATA_PARAM.key, flag);
        params.put(ArcGISRestDataStoreFactory.USER_PARAM.key, user);
        params.put(ArcGISRestDataStoreFactory.PASSWORD_PARAM.key, password);
        return (new ArcGISRestDataStoreFactory()).createDataStore(params);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateNewDataStore() throws UnsupportedOperationException {
        (new ArcGISRestDataStoreFactory()).createNewDataStore(params);
    }

    @Test
    public void testCanProcess() {
        // Nothing set
        assertFalse(dsf.canProcess(params));

        // Namespace set
        params.put(ArcGISRestDataStoreFactory.NAMESPACE_PARAM.key, NAMESPACE);
        assertFalse(dsf.canProcess(params));

        // URL set wrongly
        params.put(ArcGISRestDataStoreFactory.URL_PARAM.key, "ftp://example.com");
        assertTrue(dsf.canProcess(params));

        // URL set correctly
        params.put(ArcGISRestDataStoreFactory.URL_PARAM.key, URL);
        assertTrue(dsf.canProcess(params));

        // Open data flag set correrctly
        params.put(ArcGISRestDataStoreFactory.ISOPENDATA_PARAM.key, true);
        assertTrue(dsf.canProcess(params));

        // Username set
        params.put(ArcGISRestDataStoreFactory.USER_PARAM.key, USER);
        assertTrue(dsf.canProcess(params));

        // Password set
        params.put(ArcGISRestDataStoreFactory.PASSWORD_PARAM.key, PASSWORD);
        assertTrue(dsf.canProcess(params));
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateDataStoreMalformedNamespace() throws IOException {
        LOGGER.setLevel(Level.OFF);
        createDataStore("aaa", "bbb", true, "ccc", "ddd");
        LOGGER.setLevel(Level.FINEST);
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateDataStoreMalformedURL() throws IOException {
        LOGGER.setLevel(Level.OFF);
        createDataStore(NAMESPACE, "bbb", true, "ccc", "ddd");
        LOGGER.setLevel(Level.FINEST);
    }
}
