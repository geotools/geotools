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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.csv.parse.CSVLatLonStrategy;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
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
        List<Coordinate> result = new ArrayList<Coordinate>(points.length);
        double x = -1;
        for (double d : points) {
            if (x == -1) {
                x = d;
            } else {
                Coordinate coordinate = new Coordinate(x, d);
                x = -1;
                result.add(coordinate);
            }
        }
        return result;
    }

    @Test
    public void testReadFeatures() throws IOException {
        FeatureReader<SimpleFeatureType, SimpleFeature> reader = csvDataStore.getFeatureReader();
        List<Coordinate> geometries = new ArrayList<Coordinate>();
        List<String> cities = new ArrayList<String>();
        List<String> numbers = new ArrayList<String>();

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

    /**
     * Test query with a start index
     *
     * @throws IOException
     * @throws FileNotFoundException
     */
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
        SimpleFeatureIterator iter = matches.features();
        while (iter.hasNext()) {
            SimpleFeature f = iter.next();
            assertTrue(offsetCities.contains(f.getAttribute("CITY")));
            count++;
        }
        iter.close();
        assertEquals(6, count);
        assertEquals(6, matches.size());
        assertEquals(6, rows.getCount(query));
    }

    /**
     * Test query with maxFeatures
     *
     * @throws IOException
     * @throws FileNotFoundException
     */
    @Test
    public void testLimit() throws FileNotFoundException, IOException {
        Query query = new Query(Query.ALL);
        query.setMaxFeatures(3);
        SimpleFeatureSource rows = csvDataStore.getFeatureSource();
        SimpleFeatureCollection matches = rows.getFeatures(query);
        List<String> limitCities = Arrays.asList("Trento", "St Paul", "Bangkok");
        int count = 0;
        SimpleFeatureIterator iter = matches.features();
        while (iter.hasNext()) {
            SimpleFeature f = iter.next();
            assertTrue(limitCities.contains(f.getAttribute("CITY")));
            count++;
        }
        iter.close();
        assertEquals(3, count);
        assertEquals(3, matches.size());
        assertEquals(3, rows.getCount(query));
    }

    /**
     * Test query with maxFeatures and startIndex
     *
     * @throws IOException
     * @throws FileNotFoundException
     */
    @Test
    public void testLimitOffset() throws FileNotFoundException, IOException {
        Query query = new Query(Query.ALL);
        query.setMaxFeatures(3);
        query.setStartIndex(3);
        SimpleFeatureSource rows = csvDataStore.getFeatureSource();
        SimpleFeatureCollection matches = rows.getFeatures(query);
        List<String> limitCities = Arrays.asList("Ottawa", "Minneapolis", "Lausanne");
        int count = 0;
        SimpleFeatureIterator iter = matches.features();
        while (iter.hasNext()) {
            SimpleFeature f = iter.next();
            assertTrue(limitCities.contains(f.getAttribute("CITY")));
            count++;
        }
        iter.close();
        assertEquals(3, count);
        assertEquals(3, matches.size());
        assertEquals(3, rows.getCount(query));
    }
}
