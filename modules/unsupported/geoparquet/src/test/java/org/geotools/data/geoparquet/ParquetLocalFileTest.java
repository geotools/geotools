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
import java.util.HashMap;
import java.util.Map;
import org.geotools.api.data.DataStore;
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

    private DataStore dataStorePrimaryKey;

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

        resource = getClass().getResource("/org/geotools/data/geoparquet/local_no_id.parquet");
        file = new File(resource.toURI());
        Map<String, Object> params = new HashMap<>();
        params.put(GeoParquetDataStoreFactory.DBTYPE.key, "geoparquet");
        params.put(GeoParquetDataStoreFactory.URI_PARAM.key, file.toURI().toASCIIString());
        params.put(GeoParquetDataStoreFactory.PRIMARY_KEY_ID.key, "fire_id");
        dataStorePrimaryKey = new GeoParquetDataStoreFactory().createDataStore(params);
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
                "percentage", Double.class,
                "startdate", java.sql.Timestamp.class,
                "enddate", java.sql.Timestamp.class);
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
    public void testDatastoreWithCustomPrimaryKey() throws IOException {
        String[] typeNames = dataStorePrimaryKey.getTypeNames();
        assertEquals("Should have one type name", 1, typeNames.length);
        // Get a feature source and verify it has features
        SimpleFeatureSource source = dataStorePrimaryKey.getFeatureSource(typeNames[0]);
        verifyFeatureSource(source, 42);
        SimpleFeatureType schema = source.getSchema();
        Map<String, Class<?>> expected = new HashMap<>();
        expected.put("cell_observations", Long.class);
        expected.put("cell_first_timestamp", java.sql.Timestamp.class);
        expected.put("cell_last_timestamp", java.sql.Timestamp.class);
        expected.put("cell_sensor", String.class);
        expected.put("cell_scene", String.class);
        expected.put("cell_severity", Double.class);
        expected.put("cell_severity_class", Double.class);
        expected.put("cell_confidence", Double.class);
        expected.put("cell_lulc", Double.class);
        expected.put("cell_h3index0", String.class);
        expected.put("cell_h3indexstr", String.class);
        expected.put("cell_h3res", Long.class);
        expected.put("cell_dist_firestation", Long.class);
        expected.put("cell_dist_hospital", Long.class);
        expected.put("cell_dist_road", Long.class);
        expected.put("cell_dist_settlement", Long.class);
        expected.put("cell_dist_water", Long.class);
        expected.put("cell_country", Long.class);
        expected.put("cell_exposure_class", Long.class);
        expected.put("cell_mitigation_class", Long.class);
        expected.put("cell_area_ha", Double.class);
        expected.put("fire_startdate", java.sql.Timestamp.class);
        expected.put("fire_enddate", java.sql.Timestamp.class);
        expected.put("fire_duration", Long.class);
        expected.put("fire_confidence_mean", Double.class);
        expected.put("fire_severity_mean", Double.class);
        expected.put("fire_severity_class_mode", Double.class);
        expected.put("fire_mitigation_class_min", Long.class);
        expected.put("fire_exposure_class_max", Long.class);
        expected.put("fire_lulc_mode", Double.class);
        expected.put("fire_area_ha", Double.class);
        expected.put("fire_status", String.class);
        expected.put("fire_h3index0", String.class);
        expected.put("fire_sensor", String.class);
        expected.put("cell_h3index", Long.class);

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
