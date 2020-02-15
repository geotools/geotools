/* (c) 2014 Open Source Geospatial Foundation - all rights reserved
 * (c) 2001 - 2013 OpenPlans
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geotools.data.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.csv.parse.CSVLatLonStrategy;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.CRS.AxisOrder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.geotools.util.URLs;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

public class CSVDataStoreTest {

    private CSVDataStore csvDataStore;

    @Before
    public void setUp() throws Exception {
        URL resource = TestData.getResource(CSVDataStoreTest.class, "locations.csv");
        assertNotNull("Failure finding locations csv file", resource);
        File file = URLs.urlToFile(resource);
        CSVFileState csvFileState = new CSVFileState(file);
        CSVLatLonStrategy csvStrategy = new CSVLatLonStrategy(csvFileState);
        csvDataStore = new CSVDataStore(csvFileState, csvStrategy);
    }

    @Test
    public void testGetTypeName() {
        Name typeName = csvDataStore.getTypeName();
        assertEquals("Invalid type name", "locations", typeName.getLocalPart());
    }

    private List<Coordinate> makeExpectedCoordinates(double... points) {
        List<Coordinate> result = new ArrayList<>(points.length);
        double x = -1;
        for (double d : points) {
            if (x == -1) {
                x = d;
            } else {
                Coordinate coordinate;
                if (CRS.getAxisOrder(CSVLatLonStrategy._CRS).equals(AxisOrder.NORTH_EAST)) {
                    coordinate = new Coordinate(x, d);
                } else {
                    coordinate = new Coordinate(d, x);
                }

                x = -1;
                result.add(coordinate);
            }
        }
        return result;
    }

    @Test
    public void testFeatureReader() throws IOException {
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                csvDataStore.getFeatureReader(); ) {

            assertNotNull(reader);
            int count = 0;
            while (reader.hasNext()) {
                SimpleFeature f = reader.next();
                assertNotNull(f);
                count++;
            }
            assertEquals(9, count);
        }
    }

    @Test
    public void testFeatureSource() throws IOException {
        SimpleFeatureSource source = csvDataStore.getFeatureSource();
        assertNotNull(source);
        SimpleFeatureCollection features = source.getFeatures();
        assertNotNull(features);
        assertEquals(9, features.size());
        ReferencedEnvelope env = source.getBounds();
        assertNotNull(env);
        ReferencedEnvelope expected =
                new ReferencedEnvelope(
                        -123.365556, 151.211111, -33.925278, 48.428611, DefaultGeographicCRS.WGS84);
        assertTrue(env.boundsEquals2D(expected, 0.0001));
    }

    @Test
    public void testBlankLines() throws IOException {
        URL resource = TestData.getResource(CSVDataStoreTest.class, "locations.csv");
        File tmp = File.createTempFile("example", "");
        boolean exists = tmp.exists();
        if (exists) {
            tmp.delete();
        }
        boolean created = tmp.mkdirs();
        assertTrue(created);
        File blankLinefile = new File(tmp, "locations.csv");
        Files.copy(
                resource.openStream(), blankLinefile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        try (FileWriter writer = new FileWriter(blankLinefile, true)) {
            writer.append("\n");
        }
        CSVFileState csvFileState = new CSVFileState(blankLinefile);
        CSVLatLonStrategy csvStrategy = new CSVLatLonStrategy(csvFileState);
        CSVDataStore dataStore = new CSVDataStore(csvFileState, csvStrategy);
        SimpleFeatureSource source = dataStore.getFeatureSource();
        assertNotNull(source);
        SimpleFeatureCollection features = source.getFeatures();
        assertNotNull(features);
        assertEquals(9, features.size());
    }

    @Test
    public void testReadFeatures() throws IOException {
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                csvDataStore.getFeatureReader()) {
            List<Coordinate> geometries = new ArrayList<>();
            List<String> cities = new ArrayList<>();
            List<String> numbers = new ArrayList<>();

            while (reader.hasNext()) {
                SimpleFeature feature = reader.next();
                Point geometry = (Point) feature.getDefaultGeometry();
                geometries.add(geometry.getCoordinate());
                cities.add(feature.getAttribute("CITY").toString());
                numbers.add(feature.getAttribute("NUMBER").toString());
            }

            List<Coordinate> expectedCoordinates =
                    makeExpectedCoordinates(
                            46.066667,
                            11.116667,
                            44.9441,
                            -93.0852,
                            13.752222,
                            100.493889,
                            45.420833,
                            -75.69,
                            44.9801,
                            -93.251867,
                            46.519833,
                            6.6335,
                            48.428611,
                            -123.365556,
                            -33.925278,
                            18.423889,
                            -33.859972,
                            151.211111);
            assertEquals("Unexpected coordinates", expectedCoordinates, geometries);

            List<String> expectedCities =
                    Arrays.asList(
                            "Trento, St Paul, Bangkok, Ottawa, Minneapolis, Lausanne, Victoria, Cape Town, Sydney"
                                    .split(", "));
            assertEquals("Unexecpted cities", expectedCities, cities);

            List<String> expectedNumbers =
                    Arrays.asList("140, 125, 150, 200, 350, 560, 721, 550, 436".split(", "));
            assertEquals("Unexpected numbers", expectedNumbers, numbers);
        }
    }

    /** Test query with a start index */
    @Test
    public void testOffset() throws FileNotFoundException, IOException {
        Query query = new Query(Query.ALL);
        query.setStartIndex(3);
        SimpleFeatureSource rows = csvDataStore.getFeatureSource();
        SimpleFeatureCollection matches = rows.getFeatures(query);
        List<String> offsetCities =
                Arrays.asList(
                        "Ottawa", "Minneapolis", "Lausanne", "Victoria", "Cape Town", "Sydney");
        int count = 0;
        try (SimpleFeatureIterator iter = matches.features()) {
            while (iter.hasNext()) {
                SimpleFeature f = iter.next();
                assertTrue(offsetCities.contains(f.getAttribute("CITY")));
                count++;
            }
        }
        assertEquals(6, count);
        assertEquals(6, matches.size());
        assertEquals(6, rows.getCount(query));
    }

    /** Test query with maxFeatures */
    @Test
    public void testLimit() throws FileNotFoundException, IOException {
        Query query = new Query(Query.ALL);
        query.setMaxFeatures(3);
        SimpleFeatureSource rows = csvDataStore.getFeatureSource();
        SimpleFeatureCollection matches = rows.getFeatures(query);
        List<String> limitCities = Arrays.asList("Trento", "St Paul", "Bangkok");
        int count = 0;
        try (SimpleFeatureIterator iter = matches.features()) {
            while (iter.hasNext()) {
                SimpleFeature f = iter.next();
                String city = (String) f.getAttribute("CITY");

                assertTrue("Didn't find " + city, limitCities.contains(city));
                count++;
            }
        }
        assertEquals(3, count);
        assertEquals(3, matches.size());
        assertEquals(3, rows.getCount(query));
    }

    /** Test query with maxFeatures and startIndex */
    @Test
    public void testLimitOffset() throws FileNotFoundException, IOException {
        Query query = new Query(Query.ALL);
        query.setMaxFeatures(3);
        query.setStartIndex(3);
        SimpleFeatureSource rows = csvDataStore.getFeatureSource();
        SimpleFeatureCollection matches = rows.getFeatures(query);
        List<String> limitCities = Arrays.asList("Ottawa", "Minneapolis", "Lausanne");
        int count = 0;
        try (SimpleFeatureIterator iter = matches.features()) {
            while (iter.hasNext()) {
                SimpleFeature f = iter.next();
                assertTrue(limitCities.contains(f.getAttribute("CITY")));
                count++;
            }
        }
        assertEquals(3, count);
        assertEquals(3, matches.size());
        assertEquals(3, rows.getCount(query));
    }
}
