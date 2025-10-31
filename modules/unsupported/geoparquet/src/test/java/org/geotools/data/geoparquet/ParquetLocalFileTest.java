/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.geoparquet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Before;
import org.junit.Test;

/** Tests for simple Parquet DataStore (without geometry) with local files. */
public class ParquetLocalFileTest extends GeoParquetTestBase {

    /** Flag to detect Windows OS */
    private static final boolean IS_WINDOWS =
            System.getProperty("os.name").toLowerCase().contains("windows");

    private static final FilterFactory FF = CommonFactoryFinder.getFilterFactory();

    @Before
    public void setUp() throws URISyntaxException, IOException {
        // Skip test on Windows due to path handling differences
        assumeFalse("Test disabled on Windows platform", IS_WINDOWS);
        URL resource = getClass().getResource("/org/geotools/data/geoparquet/localsample.parquet");
        File file = new File(resource.toURI());
        dataStore = createLocalDataStore(file.toURI().toASCIIString());
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testFeatureType() throws IOException {
        // Create the datastore with the local file
        // Verify we can get the feature type name
        String[] typeNames = dataStore.getTypeNames();

        assertEquals("Should have one type name", 1, typeNames.length);
        // Get a feature source and verify it has features
        SimpleFeatureSource source = dataStore.getFeatureSource(typeNames[0]);
        verifyFeatureSource(source, 548);
        SimpleFeatureType schema = source.getSchema();
        Map<String, Class<?>> expected = Map.of(
                "h3index0", String.class,
                "h3indexstr", String.class,
                "uniqueid", String.class,
                "landcover", Double.class,
                "percentage", Double.class);
        for (Map.Entry e : expected.entrySet()) {
            AttributeDescriptor desc = schema.getDescriptor((String) e.getKey());
            assertNotNull("Missing attribute: " + e.getKey(), desc);
            assertEquals(
                    "Wrong binding for " + e.getKey(),
                    e.getValue(),
                    desc.getType().getBinding());
        }
        assertNull("Schema should not have a geometry attribute", schema.getGeometryDescriptor());
    }

    @Test
    public void testFiltering() throws Exception {
        String typeName = dataStore.getTypeNames()[0];
        SimpleFeatureSource fs = dataStore.getFeatureSource(typeName);

        Filter filter = FF.greater(FF.property("landcover"), FF.literal("20"));
        Query landcoverQuery = new Query(typeName, filter);
        SimpleFeatureCollection above20 = fs.getFeatures(landcoverQuery);

        int countAbove20 = 0;
        try (SimpleFeatureIterator it = above20.features()) {
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                Number n = (Number) f.getAttribute("landcover");
                assertTrue("Found landcover <= 20 for feature id " + f.getID(), n.doubleValue() > 20.0);
                countAbove20++;
            }
        }
        assertEquals(25, countAbove20);

        filter = FF.equals(FF.property("h3indexstr"), FF.literal("8a1e60d62d97fff"));
        SimpleFeatureCollection equalsH3 = fs.getFeatures(filter);

        int countEqualsH3 = 0;
        try (SimpleFeatureIterator it = equalsH3.features()) {
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                assertEquals(
                        "h3indexstr mismatch for feature id " + f.getID(),
                        "8a1e60d62d97fff",
                        String.valueOf(f.getAttribute("h3indexstr")));
                countEqualsH3++;
            }
        }
        assertEquals(1, countEqualsH3);
    }
}
