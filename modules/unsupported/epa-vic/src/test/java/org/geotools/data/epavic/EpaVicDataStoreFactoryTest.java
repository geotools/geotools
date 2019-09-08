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
package org.geotools.data.epavic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.DataStore;
import org.geotools.util.logging.Logging;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/** @source $URL$ */
public class EpaVicDataStoreFactoryTest {

    private static final Logger LOGGER = Logging.getLogger("org.geotools.data.arcgisrest");

    private static final String URL = "http://sciwebsvc.epa.vic.gov.au/aqapi";

    public static String NAMESPACE = "http://aurin.org.au";

    private EpaVicDataStoreFactory dsf;

    private Map<String, Serializable> params;

    @Before
    public void setUp() throws Exception {
        dsf = new EpaVicDataStoreFactory();
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
     * @param path File name to load
     * @return JSON content of the file
     * @throws URISyntaxException
     * @throws FileNotFoundException
     */
    static String readFile(String path, Charset encoding) throws IOException, URISyntaxException {
        byte[] encoded =
                Files.readAllBytes(
                        Paths.get(EpaVicDataStoreFactoryTest.class.getResource(path).toURI()));
        return new String(encoded, encoding);
    }

    /**
     * Helper method to read a JSON file into an InputString
     *
     * @param fileName File name to load
     * @return JSON content of the file
     * @throws FileNotFoundException
     */
    public static InputStream readJSONAsStream(String fileName) throws FileNotFoundException {
        return new FileInputStream(
                new File(EpaVicDataStoreFactoryTest.class.getResource(fileName).getFile()));
    }

    /** Helper method to create a default test data store on ArcGIS Server */
    public static DataStore createDefaultOpenDataTestDataStore() throws IOException {

        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put(EpaVicDataStoreFactory.NAMESPACE_PARAM.key, NAMESPACE);
        params.put(EpaVicDataStoreFactory.URL_PARAM.key, URL);
        return (new EpaVicDataStoreFactory()).createDataStore(params);
    }

    /**
     * Helper method to create a data store
     *
     * @param namespace
     * @param url
     * @param user
     * @param password
     * @return
     * @throws IOException
     */
    public DataStore createDataStore(
            final String namespace,
            final String url,
            boolean flag,
            final String user,
            final String password)
            throws IOException {

        params.put(EpaVicDataStoreFactory.NAMESPACE_PARAM.key, namespace);
        params.put(EpaVicDataStoreFactory.URL_PARAM.key, url);
        return (new EpaVicDataStoreFactory()).createDataStore(params);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateNewDataStore() throws UnsupportedOperationException {
        (new EpaVicDataStoreFactory()).createNewDataStore(params);
    }

    @Test
    public void testCanProcess() {
        // Nothing set
        assertFalse(dsf.canProcess(params));

        // Namespace set
        params.put(EpaVicDataStoreFactory.NAMESPACE_PARAM.key, NAMESPACE);
        assertFalse(dsf.canProcess(params));

        // URL set wrongly
        params.put(EpaVicDataStoreFactory.URL_PARAM.key, "ftp://example.com");
        assertTrue(dsf.canProcess(params));

        // URL set correctly
        params.put(EpaVicDataStoreFactory.URL_PARAM.key, URL);
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

    public static EpaVicDatastore createDefaultEPAServerTestDataStore()
            throws MalformedURLException, IOException {
        return new EpaVicDatastore(NAMESPACE, URL);
    }

    public static String readJSONAsString(String filePath) throws IOException, URISyntaxException {
        return readFile(filePath, Charset.forName("UTF-8"));
    }
}
