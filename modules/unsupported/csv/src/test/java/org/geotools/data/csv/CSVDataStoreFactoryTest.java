/* (c) 2014 - 2015 Open Source Geospatial Foundation - all rights reserved
 * (c) 2001 - 2013 OpenPlans
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geotools.data.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.geotools.TestData;
import org.geotools.data.FileDataStore;
import org.geotools.data.csv.parse.CSVAttributesOnlyStrategy;
import org.geotools.data.csv.parse.CSVLatLonStrategy;
import org.geotools.data.csv.parse.CSVSpecifiedWKTStrategy;
import org.geotools.data.csv.parse.CSVStrategy;
import org.geotools.util.URLs;
import org.junit.Before;
import org.junit.Test;

public class CSVDataStoreFactoryTest {

    private CSVDataStoreFactory csvDataStoreFactory;

    private File file;

    private URL locationsResource;

    @Before
    public void setUp() throws Exception {
        csvDataStoreFactory = new CSVDataStoreFactory();
        locationsResource = TestData.getResource(CSVDataStoreFactoryTest.class, "locations.csv");
        assert locationsResource != null : "Could not find locations.csv resource";
        file = URLs.urlToFile(locationsResource);
    }

    @Test
    public void testBasicGetters() throws MalformedURLException {
        assertEquals("CSV", csvDataStoreFactory.getDisplayName());
        assertEquals("Comma delimited text file", csvDataStoreFactory.getDescription());
        assertTrue(csvDataStoreFactory.canProcess(locationsResource));
        assertTrue(csvDataStoreFactory.getImplementationHints().isEmpty());
        assertArrayEquals(new String[] {".csv"}, csvDataStoreFactory.getFileExtensions());
        assertNotNull("Invalid Parameter Info", csvDataStoreFactory.getParametersInfo());
    }

    @Test
    public void testCreateDataStoreFileParams() throws Exception {
        Map<String, Serializable> fileParams = new HashMap<String, Serializable>(1);
        fileParams.put("file", file);
        FileDataStore dataStore = csvDataStoreFactory.createDataStore(fileParams);
        assertNotNull("Could not create datastore from file params", dataStore);
    }

    @Test
    public void testCreateDataStoreURLParams() throws Exception {
        Map<String, Serializable> urlParams = new HashMap<String, Serializable>(1);
        urlParams.put("url", locationsResource);
        FileDataStore dataStore = csvDataStoreFactory.createDataStore(urlParams);
        assertNotNull("Could not create datastore from url params", dataStore);
    }

    @Test
    public void testCreateDataStoreURL() throws MalformedURLException, IOException {
        FileDataStore dataStore = csvDataStoreFactory.createDataStore(locationsResource);
        assertNotNull("Failure creating data store", dataStore);
    }

    @Test
    public void testGetTypeName() throws IOException {
        FileDataStore dataStore = csvDataStoreFactory.createDataStore(locationsResource);
        String[] typeNames = dataStore.getTypeNames();
        assertEquals("Invalid number of type names", 1, typeNames.length);
        assertEquals("Invalid type name", "locations", typeNames[0]);
    }

    @Test
    public void testCanProcessFileParams() {
        Map<String, Serializable> fileParams = new HashMap<String, Serializable>(1);
        fileParams.put("file", file);
        assertTrue("Did not process file params", csvDataStoreFactory.canProcess(fileParams));
    }

    @Test
    public void testCanProcessURLParams() {
        Map<String, Serializable> urlParams = new HashMap<String, Serializable>(1);
        urlParams.put("url", locationsResource);
        assertTrue("Did not process url params", csvDataStoreFactory.canProcess(urlParams));
    }

    @Test
    public void testInvalidParamsCreation() throws Exception {
        Map<String, Serializable> params = Collections.emptyMap();
        try {
            csvDataStoreFactory.createDataStore(params);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
            return;
        } catch (Exception e) {
        }
        fail("Did not throw illegal argument exception for null file");
    }

    @Test
    public void testFileDoesNotExist() throws Exception {
        try {
            csvDataStoreFactory.createDataStoreFromFile(new File("/tmp/does-not-exist.csv"));
        } catch (IllegalArgumentException e) {
            assertTrue(true);
            return;
        } catch (Exception e) {
        }
        assertTrue("Did not throw illegal argument exception for non-existent file", false);
    }

    @Test
    public void testCSVStrategyDefault() throws Exception {
        CSVDataStore datastore = (CSVDataStore) csvDataStoreFactory.createDataStoreFromFile(file);
        CSVStrategy csvStrategy = datastore.getCSVStrategy();
        assertEquals(
                "Unexpected default csv strategy",
                CSVAttributesOnlyStrategy.class,
                csvStrategy.getClass());
    }

    @Test
    public void testCSVStrategyGuess() throws Exception {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("strategy", "guess");
        params.put("file", file);
        CSVDataStore datastore = (CSVDataStore) csvDataStoreFactory.createDataStore(params);
        CSVStrategy csvStrategy = datastore.getCSVStrategy();
        assertEquals("Unexpected strategy", CSVLatLonStrategy.class, csvStrategy.getClass());
    }

    @Test
    public void testCSVStrategySpecifiedBadParams() throws Exception {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("strategy", "specify");
        params.put("file", file);
        try {
            csvDataStoreFactory.createDataStore(params);
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("Expected illegal argument exception for missing latField and lngField");
    }

    @Test
    public void testCSVStrategySpecified() throws Exception {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("strategy", "specify");
        params.put("file", file);
        params.put("latField", "foo");
        params.put("lngField", "bar");
        CSVDataStore datastore = (CSVDataStore) csvDataStoreFactory.createDataStore(params);
        CSVStrategy csvStrategy = datastore.getCSVStrategy();
        assertEquals("Unexpected strategy", CSVLatLonStrategy.class, csvStrategy.getClass());
    }

    @Test
    public void testCSVStrategyWKTMissingWktField() throws IOException {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("strategy", "wkt");
        params.put("file", file);
        try {
            csvDataStoreFactory.createDataStore(params);
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("Expected illegal argument exception for missing wktField");
    }

    @Test
    public void testCSVStrategyWKT() throws IOException {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("strategy", "wkt");
        params.put("wktField", "whatever");
        params.put("file", file);
        CSVDataStore datastore = (CSVDataStore) csvDataStoreFactory.createDataStore(params);
        CSVStrategy csvStrategy = datastore.getCSVStrategy();
        assertEquals("Unexpected strategy", CSVSpecifiedWKTStrategy.class, csvStrategy.getClass());
    }

    @Test
    public void testNamespace() throws IOException {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        String namespaceURI = "http://www.geotools.org/csv";
        params.put("namespace", namespaceURI);
        params.put("wktField", "whatever");
        params.put("file", file);
        CSVDataStore datastore = (CSVDataStore) csvDataStoreFactory.createDataStore(params);
        assertEquals(namespaceURI, datastore.getNamespaceURI());
        assertEquals(namespaceURI, datastore.getTypeName().getNamespaceURI());
    }
}
