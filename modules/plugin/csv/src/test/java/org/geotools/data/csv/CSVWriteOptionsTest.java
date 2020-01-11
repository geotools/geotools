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
package org.geotools.data.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.TestData;
import org.geotools.data.DataStore;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.csv.parse.CSVIterator;
import org.geotools.data.csv.parse.CSVLatLonStrategy;
import org.geotools.data.csv.parse.CSVTestStrategySupport;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;

/** @author ian */
public class CSVWriteOptionsTest {
    File tmp;
    File statesfile;
    private FileDataStore store;
    CSVDataStoreFactory factory = new CSVDataStoreFactory();

    @Before
    public void setup() throws IOException {
        URL states = TestData.url("shapes/statepop.shp");
        store = FileDataStoreFinder.getDataStore(states);
        assertNotNull("couldn't create input store", store);
    }

    @Before
    public void createTemporaryLocations() throws IOException {
        tmp = File.createTempFile("example", "");
        boolean exists = tmp.exists();
        if (exists) {
            tmp.delete();
        }
        boolean created = tmp.mkdirs();
        if (!created) {
            System.exit(1);
        }
        statesfile = new File(tmp, "locations.csv");

        URL resource = TestData.getResource(CSVWriteTest.class, "locations.csv");
        Files.copy(resource.openStream(), statesfile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    private String getFileContents(File modified) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Files.copy(modified.toPath(), baos);
        String contents = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        return contents;
    }

    @After
    public void removeTemporaryLocations() throws IOException {
        File list[] = tmp.listFiles();
        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                list[i].delete();
            }
        }
        tmp.delete();
    }

    // Make sure any temp files were cleaned up.
    public boolean cleanedup() {
        File list[] = tmp.listFiles((dir, name) -> name.endsWith(".csv"));
        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                if (list[i].getName().equalsIgnoreCase("locations.csv")) {
                    continue;
                }
                return false;
            }
        }
        return true;
    }

    @Test
    public void testWithOutQuotes() throws IOException {

        File file2 = File.createTempFile("CSVTest", ".csv");
        Map<String, Serializable> params2 = new HashMap<>();
        params2.put("file", file2);

        params2.put(CSVDataStoreFactory.STRATEGYP.key, CSVDataStoreFactory.WKT_STRATEGY);
        params2.put(CSVDataStoreFactory.WKTP.key, "the_geom_wkt");

        String contents = createOutputFile(file2, params2);
        try (BufferedReader lineReader =
                new BufferedReader(new CharArrayReader(contents.toCharArray()))) {
            String line = lineReader.readLine(); // header
            assertNotNull(line);
            assertTrue("Geom is not included", line.toLowerCase().contains("the_geom_wkt"));
            line = lineReader.readLine();
            assertNotNull(line);
            assertTrue("Multipolygon is not quoted", line.toLowerCase().contains("\"multipolygon"));
        }
        file2.delete();
    }

    @Test
    public void testWithQuotes() throws IOException {
        File file2 = File.createTempFile("CSVTest", ".csv");
        Map<String, Serializable> params2 = new HashMap<>();
        params2.put("file", file2);

        params2.put(CSVDataStoreFactory.STRATEGYP.key, CSVDataStoreFactory.WKT_STRATEGY);
        params2.put(CSVDataStoreFactory.WKTP.key, "the_geom_wkt");
        params2.put(CSVDataStoreFactory.QUOTEALL.key, true);
        String contents = createOutputFile(file2, params2);
        try (BufferedReader lineReader =
                new BufferedReader(new CharArrayReader(contents.toCharArray()))) {
            String line = lineReader.readLine(); // header
            assertNotNull(line);

            assertTrue("Geom is not included", line.toLowerCase().contains("\"the_geom_wkt\""));
            line = lineReader.readLine();
            assertNotNull(line);
            assertTrue("Multipolygon is not quoted", line.toLowerCase().contains("\"multipolygon"));
            assertTrue("Missing quotes", line.contains("\"0.0\""));
            file2.delete();
        }
    }

    @Test
    public void testWithSingleQuotes() throws IOException {
        File file2 = File.createTempFile("CSVTest", ".csv");
        Map<String, Serializable> params2 = new HashMap<>();
        params2.put("file", file2);

        params2.put(CSVDataStoreFactory.STRATEGYP.key, CSVDataStoreFactory.WKT_STRATEGY);
        params2.put(CSVDataStoreFactory.WKTP.key, "the_geom_wkt");
        params2.put(CSVDataStoreFactory.QUOTEALL.key, true);
        params2.put(CSVDataStoreFactory.QUOTECHAR.key, '\'');
        String contents = createOutputFile(file2, params2);
        try (BufferedReader lineReader =
                new BufferedReader(new CharArrayReader(contents.toCharArray()))) {
            String line = lineReader.readLine(); // header
            assertNotNull(line);

            assertTrue("Geom is not included", line.toLowerCase().contains("the_geom_wkt"));
            line = lineReader.readLine();
            assertNotNull(line);
            assertTrue("Multipolygon is not quoted", line.toLowerCase().contains("'multipolygon"));
            assertTrue("Missing quotes", line.contains("'0.0'"));
            file2.delete();
        }
    }

    @Test
    public void testWithtabs() throws IOException {
        File file2 = File.createTempFile("CSVTest", ".csv");
        Map<String, Serializable> params2 = new HashMap<>();
        params2.put("file", file2);

        params2.put(CSVDataStoreFactory.STRATEGYP.key, CSVDataStoreFactory.WKT_STRATEGY);
        params2.put(CSVDataStoreFactory.WKTP.key, "the_geom_wkt");
        params2.put(CSVDataStoreFactory.SEPERATORCHAR.key, '\t');
        String contents = createOutputFile(file2, params2);
        try (BufferedReader lineReader =
                new BufferedReader(new CharArrayReader(contents.toCharArray()))) {
            String line = lineReader.readLine(); // header
            assertNotNull(line);

            assertTrue("Geom is not included", line.toLowerCase().contains("the_geom_wkt"));
            line = lineReader.readLine();
            assertNotNull(line);
            assertTrue("Wrong seperator", line.contains("\t"));
            assertTrue("Should not need quotes", line.toLowerCase().startsWith("multipolygon"));
            file2.delete();
        }
    }

    @Test
    public void testLineSeperator() throws IOException {
        File file2 = File.createTempFile("CSVTest", ".csv");
        Map<String, Serializable> params2 = new HashMap<>();
        params2.put("file", file2);

        params2.put(CSVDataStoreFactory.STRATEGYP.key, CSVDataStoreFactory.WKT_STRATEGY);
        params2.put(CSVDataStoreFactory.WKTP.key, "the_geom_wkt");
        params2.put(CSVDataStoreFactory.LINESEPSTRING.key, "EOL;\n");
        String contents = createOutputFile(file2, params2);
        try (BufferedReader lineReader =
                new BufferedReader(new CharArrayReader(contents.toCharArray()))) {
            String line = lineReader.readLine(); // header
            assertNotNull(line);

            assertTrue("Geom is not included", line.toLowerCase().contains("the_geom_wkt"));
            line = lineReader.readLine();
            assertNotNull(line);
            assertTrue("Wrong Line seperator", line.contains("EOL;"));

            file2.delete();
        }
    }

    @Test
    public void testReadEscapeChars() throws IOException {
        String input =
                CSVTestStrategySupport.buildInputString(
                        "doubleval,\"int'val\",lat,stringval,lon",
                        "3.8,7,73.28,\"f'oo\",-14.39",
                        "9.12,-38,0,bar,29",
                        "-37,0,49,baz,0");
        CSVFileState fileState = new CSVFileState(input, "typename");

        CSVLatLonStrategy strategy = new CSVLatLonStrategy(fileState);
        try (CSVIterator iterator = strategy.iterator()) {

            SimpleFeatureType featureType = strategy.getFeatureType();
            assertEquals("invalid attribute count", 4, featureType.getAttributeCount());

            GeometryDescriptor geometryDescriptor = featureType.getGeometryDescriptor();
            String localName = geometryDescriptor.getLocalName();
            assertEquals("Invalid geometry name", "location", localName);
            // iterate through values and verify
            Object[][] expValues =
                    new Object[][] {
                        new Object[] {3.8, 7, "f'oo", 73.28, -14.39},
                        new Object[] {9.12, -38, "bar", 0, 29},
                        new Object[] {-37.0, 0, "baz", 49, 0}
                    };
            Object[] expTypes = new Object[] {Double.class, Integer.class, String.class};
            List<SimpleFeature> features = new ArrayList<>(3);
            while (iterator.hasNext()) {
                features.add(iterator.next());
            }

            assertEquals("Invalid number of features", 3, features.size());

            String[] attrNames = new String[] {"doubleval", "int'val", "stringval"};
            int i = 0;
            for (SimpleFeature feature : features) {
                Object[] expVals = expValues[i];
                for (int j = 0; j < 3; j++) {
                    String attr = attrNames[j];
                    Object value = feature.getAttribute(attr);
                    Class<?> type = value.getClass();
                    assertEquals("Invalid attribute type", expTypes[j], type);
                    assertEquals("Invalid value", expVals[j], value);
                }
                i++;
            }
        }
    }

    @Test
    public void testWriteEscapeChars() throws IOException {
        File file2 = File.createTempFile("CSVTest", ".csv");
        Map<String, Serializable> params2 = new HashMap<>();
        params2.put("file", file2);

        params2.put(CSVDataStoreFactory.STRATEGYP.key, CSVDataStoreFactory.WKT_STRATEGY);
        params2.put(CSVDataStoreFactory.WKTP.key, "the_geom_wkt");
        params2.put(CSVDataStoreFactory.QUOTECHAR.key, '\'');
        params2.put(CSVDataStoreFactory.ESCAPECHAR.key, '#');
        String input =
                CSVTestStrategySupport.buildInputString(
                        "doubleval,\"int'val\",lat,stringval,lon",
                        "3.8,7,73.28,\"f'oo\",-14.39",
                        "9.12,-38,0,bar,29",
                        "-37,0,49,baz,0");
        CSVFileState fileState = new CSVFileState(input, "typename");
        fileState.setQuotechar('"');
        fileState.setEscapechar('#');
        CSVLatLonStrategy strategy = new CSVLatLonStrategy(fileState);
        CSVDataStore escapeStore = new CSVDataStore(fileState, strategy);
        String contents = createOutputFile(file2, params2, escapeStore);
        try (BufferedReader lineReader =
                new BufferedReader(new CharArrayReader(contents.toCharArray()))) {
            String line = lineReader.readLine(); // header
            assertNotNull(line);

            assertTrue("Header is not escaped", line.toLowerCase().contains("int#'val"));

            line = lineReader.readLine();
            assertNotNull(line);

            assertTrue("Value is not escaped", line.contains("'f#'oo'"));

            file2.delete();
        }
    }

    @Test
    public void testWriteEscapeChars2() throws IOException {
        File file2 = File.createTempFile("CSVTest", ".csv");
        Map<String, Serializable> params2 = new HashMap<>();
        params2.put("file", file2);

        params2.put(CSVDataStoreFactory.STRATEGYP.key, CSVDataStoreFactory.WKT_STRATEGY);
        params2.put(CSVDataStoreFactory.WKTP.key, "the_geom_wkt");
        params2.put(CSVDataStoreFactory.QUOTECHAR.key, '\'');
        params2.put(CSVDataStoreFactory.ESCAPECHAR.key, '!');
        String input =
                CSVTestStrategySupport.buildInputString(
                        "doubleval,\"int'val\",lat,stringval,lon",
                        "3.8,7,73.28,\"f'oo\",-14.39",
                        "9.12,-38,0,bar,29",
                        "-37,0,49,baz,0");
        CSVFileState fileState = new CSVFileState(input, "typename");

        CSVLatLonStrategy strategy = new CSVLatLonStrategy(fileState);
        CSVDataStore escapeStore = new CSVDataStore(fileState, strategy);
        String contents = createOutputFile(file2, params2, escapeStore);
        try (BufferedReader lineReader =
                new BufferedReader(new CharArrayReader(contents.toCharArray()))) {
            String line = lineReader.readLine(); // header
            assertNotNull(line);

            assertTrue("Header is not escaped", line.toLowerCase().contains("'int!'val'"));
            line = lineReader.readLine();
            assertNotNull(line);

            assertTrue("Value is not escaped", line.contains("'f!'oo'"));

            file2.delete();
        }
    }

    private String createOutputFile(File file2, Map<String, Serializable> params2)
            throws IOException {
        return createOutputFile(file2, params2, store);
    }

    private String createOutputFile(
            File file2, Map<String, Serializable> params2, FileDataStore input) throws IOException {
        DataStore datastore = factory.createNewDataStore(params2);

        SimpleFeatureType featureType = input.getSchema();

        datastore.createSchema(featureType);
        String typeName = datastore.getTypeNames()[0];
        SimpleFeatureSource source = datastore.getFeatureSource(typeName);
        if (source instanceof SimpleFeatureStore) {
            SimpleFeatureStore outStore = (SimpleFeatureStore) source;
            outStore.addFeatures(input.getFeatureSource().getFeatures());
        } else {
            fail("Can't write to CSVDatastore");
        }

        datastore.dispose();
        datastore = factory.createNewDataStore(params2);
        source = datastore.getFeatureSource(typeName);
        SimpleFeatureCollection features = source.getFeatures();
        assertEquals(
                "wrong number of records",
                input.getFeatureSource().getFeatures().size(),
                features.size());
        String contents = getFileContents(file2);
        return contents;
    }
}
