/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2025, Open Source Geospatial Foundation (OSGeo)
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

import static org.geotools.api.data.DataStoreFinder.getDataStore;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.util.logging.Logging;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;

/** Tests for GeoParquetDataStoreFactory. */
public class GeoParquetDataStoreTest {

    private static final Logger LOGGER = Logging.getLogger(GeoParquetDataStoreTest.class);

    /** Flag to detect Windows OS */
    private static final boolean IS_WINDOWS =
            System.getProperty("os.name").toLowerCase().contains("windows");

    @ClassRule
    public static final GeoParquetTestSupport support = new GeoParquetTestSupport();

    private GeoParquetDataStoreFactory factory;
    private Map<String, Object> worldGridDirParams;
    private Map<String, Object> worldgridPartitionedParams;

    private DataStore store;

    @Before
    public void setUp() {
        // Skip test on Windows due to path handling differences
        assumeFalse("Test disabled on Windows platform", IS_WINDOWS);

        factory = new GeoParquetDataStoreFactory();
        worldGridDirParams = new HashMap<>();

        File directory = support.getWorldgridDir();

        // Basic params
        worldGridDirParams.put(GeoParquetDataStoreFactory.DBTYPE.key, "geoparquet");
        worldGridDirParams.put(
                GeoParquetDataStoreFactory.URI_PARAM.key, directory.toURI().toASCIIString());

        worldgridPartitionedParams = new HashMap<>();
        File worldgridPartitionedDir = support.getWorldgridPartitionedDir();
        String globbingUrl = String.format("%s/**/*", worldgridPartitionedDir.getAbsolutePath());

        worldgridPartitionedParams.put(GeoParquetDataStoreFactory.DBTYPE.key, "geoparquet");
        worldgridPartitionedParams.put(GeoParquetDataStoreFactory.URI_PARAM.key, globbingUrl);
    }

    @After
    public void tearDown() {
        if (store != null) {
            store.dispose();
        }
    }

    @Test
    public void testGetTypeNames() throws IOException {
        store = getDataStore(worldGridDirParams);
        String[] typeNames = store.getTypeNames();
        assertNotNull(typeNames);
        assertEquals(Set.of("lines", "points", "polygons"), Set.copyOf(Arrays.asList(typeNames)));
    }

    @Test
    public void testTwoStoresSameParamsDispose() throws IOException {
        store = getDataStore(worldGridDirParams);
        DataStore store2 = getDataStore(worldGridDirParams);
        try {
            Set<String> expected = Set.of("lines", "points", "polygons");

            String[] typeNames1 = store.getTypeNames();
            assertEquals(expected, Set.copyOf(Arrays.asList(typeNames1)));
            store.dispose();

            String[] typeNames2 = store2.getTypeNames();
            assertEquals(expected, Set.copyOf(Arrays.asList(typeNames2)));

        } finally {
            store2.dispose();
        }
    }

    @Test
    public void testGetTypeNamesHivePartition() throws IOException {
        store = getDataStore(worldgridPartitionedParams);
        String[] typeNames = store.getTypeNames();
        assertNotNull(typeNames);

        Set<String> actual = Set.copyOf(Arrays.asList(typeNames));
        assertEquals(
                Set.of(
                        "theme_lines_type_line",
                        "theme_lines_type_multiline",
                        "theme_points_type_point",
                        "theme_points_type_multipoint",
                        "theme_polygons_type_polygon",
                        "theme_polygons_type_multipolygon"),
                actual);
    }

    @Test
    public void testGetTypeNamesHivePartitionMaxDepth() throws IOException {
        Set<String> all = Set.of(
                "theme_lines_type_line",
                "theme_lines_type_multiline",
                "theme_points_type_point",
                "theme_points_type_multipoint",
                "theme_polygons_type_polygon",
                "theme_polygons_type_multipolygon");

        worldgridPartitionedParams.put(GeoParquetDataStoreFactory.MAX_HIVE_DEPTH.key, 4);
        store = getDataStore(worldgridPartitionedParams);
        assertEquals(all, Set.copyOf(Arrays.asList(store.getTypeNames())));
        store.dispose();

        worldgridPartitionedParams.put(GeoParquetDataStoreFactory.MAX_HIVE_DEPTH.key, 3);
        store = getDataStore(worldgridPartitionedParams);
        assertEquals(all, Set.copyOf(Arrays.asList(store.getTypeNames())));
        store.dispose();

        worldgridPartitionedParams.put(GeoParquetDataStoreFactory.MAX_HIVE_DEPTH.key, 2);
        store = getDataStore(worldgridPartitionedParams);
        assertEquals(all, Set.copyOf(Arrays.asList(store.getTypeNames())));
        store.dispose();

        worldgridPartitionedParams.put(GeoParquetDataStoreFactory.MAX_HIVE_DEPTH.key, 1);
        store = getDataStore(worldgridPartitionedParams);
        Set<String> actual = Set.copyOf(Arrays.asList(store.getTypeNames()));
        assertEquals(Set.of("theme_lines", "theme_points", "theme_polygons"), actual);
        store.dispose();

        worldgridPartitionedParams.put(GeoParquetDataStoreFactory.MAX_HIVE_DEPTH.key, 0);
        store = getDataStore(worldgridPartitionedParams);
        actual = Set.copyOf(Arrays.asList(store.getTypeNames()));
        assertEquals(Set.of("worldgrid_partitioned"), actual);
        store.dispose();

        worldgridPartitionedParams.put(GeoParquetDataStoreFactory.MAX_HIVE_DEPTH.key, -1);
        IOException expected = assertThrows(IOException.class, () -> getDataStore(worldgridPartitionedParams));
        assertThat(expected.getMessage(), containsString("max_hive_depth is negative: -1"));
    }

    /**
     * Tests that multiple threads can access the same GeoParquet datastore concurrently and see the same tables. This
     * is crucial for connection pooling scenarios where multiple connections might be used simultaneously.
     */
    @Test
    public void testMultiThreadedTypeNames() throws Exception {
        LOGGER.info("Starting multi-threaded GeoParquet DataStore test");

        // Create a DataStore that all threads will share
        store = factory.createDataStore(worldGridDirParams);

        // Verify that the data store can see the expected tables before starting
        // threads
        String[] initialTypeNames = store.getTypeNames();
        LOGGER.info("Initial type names: " + Arrays.toString(initialTypeNames));

        // The expected type names from the worldgrid directory
        final Set<String> expectedTypeNames = new HashSet<>(Arrays.asList("points", "lines", "polygons"));

        // Verify that the initial state is correct
        assertEquals(expectedTypeNames, new HashSet<>(Arrays.asList(initialTypeNames)));

        // Number of threads to run concurrently
        final int threadCount = 10;
        // Number of iterations per thread (each will get type names)
        final int iterationsPerThread = 10;

        LOGGER.info("Running test with " + threadCount + " threads, " + iterationsPerThread + " iterations per thread");

        // Track errors
        final AtomicBoolean errors = new AtomicBoolean(false);
        final AtomicInteger errorCount = new AtomicInteger(0);
        final List<String> errorMessages = new ArrayList<>();
        final CountDownLatch latch = new CountDownLatch(threadCount);

        // Track successful iterations
        final AtomicInteger successCount = new AtomicInteger(0);

        // Create a thread pool
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        // Submit tasks - each will get type names from the datastore
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < iterationsPerThread; j++) {
                        // Get type names
                        String[] typeNames = store.getTypeNames();

                        // Verify we got the expected type names
                        Set<String> actualTypeNames = new HashSet<>(Arrays.asList(typeNames));
                        if (!expectedTypeNames.equals(actualTypeNames)) {
                            String errorMsg = String.format(
                                    "Thread %d, iteration %d: Expected type names %s but got %s",
                                    threadId, j, expectedTypeNames, actualTypeNames);
                            LOGGER.warning(errorMsg);
                            synchronized (errorMessages) {
                                errorMessages.add(errorMsg);
                            }
                            errors.set(true);
                            errorCount.incrementAndGet();
                        } else {
                            // Successfully got expected type names
                            successCount.incrementAndGet();
                            if (LOGGER.isLoggable(Level.FINE)) {
                                LOGGER.fine(String.format(
                                        "Thread %d, iteration %d: Successfully got type names %s",
                                        threadId, j, actualTypeNames));
                            }
                        }
                    }
                } catch (Exception e) {
                    String errorMsg = String.format("Thread %d failed with exception: %s", threadId, e.getMessage());
                    LOGGER.log(Level.SEVERE, errorMsg, e);
                    synchronized (errorMessages) {
                        errorMessages.add(errorMsg);
                    }
                    errors.set(true);
                    errorCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        // Wait for all threads to finish
        LOGGER.info("Waiting for threads to complete...");
        boolean completed = latch.await(30, TimeUnit.SECONDS);
        executor.shutdown();

        // Check if all threads completed
        assertTrue("Timed out waiting for threads to complete", completed);

        // Log success stats
        int expectedTotal = threadCount * iterationsPerThread;
        int actualSuccess = successCount.get();
        LOGGER.info(String.format(
                "Test completed: %d/%d successful iterations (%.2f%%)",
                actualSuccess, expectedTotal, 100.0 * actualSuccess / expectedTotal));

        // Check for any errors
        if (errors.get()) {
            StringBuilder errorReport = new StringBuilder();
            errorReport.append("Encountered ").append(errorCount.get()).append(" errors across threads:\n");

            for (String msg : errorMessages) {
                errorReport.append(" - ").append(msg).append("\n");
            }

            Assert.fail(errorReport.toString());
        }

        // Verify the data store still has the correct type names after all threads
        // finish
        String[] finalTypeNames = store.getTypeNames();
        LOGGER.info("Final type names: " + Arrays.toString(finalTypeNames));
        assertEquals(expectedTypeNames, new HashSet<>(Arrays.asList(finalTypeNames)));
    }

    @Test
    public void testGetSchema() throws IOException {
        store = getDataStore(worldgridPartitionedParams);

        testGetSchema(store, "theme_lines_type_line", Geometry.class);
        testGetSchema(store, "theme_lines_type_multiline", Geometry.class);

        Set.of(
                "theme_lines_type_line",
                "theme_lines_type_multiline",
                "theme_points_type_point",
                "theme_points_type_multipoint",
                "theme_polygons_type_polygon",
                "theme_polygons_type_multipolygon");
    }

    /**
     * Tests the CRS handling in GeoParquet metadata.
     *
     * <p>This test verifies that:
     *
     * <ol>
     *   <li>GeoParquet metadata correctly contains CRS information from the 'geo' field
     *   <li>The CRS is properly parsed from the PROJJSON representation
     *   <li>The CRS is correctly converted to a GeoTools CoordinateReferenceSystem object
     *   <li>The GeoParquetDialect correctly extracts SRID information from the CRS
     *   <li>The implementation properly handles column-specific CRS information
     *   <li>The SRID lookup correctly handles WGS84 (EPSG:4326) defined in test data
     * </ol>
     *
     * <p>The test uses the strongly-typed CRS model that follows the PROJJSON v0.7 schema as defined by the OGC
     * GeoParquet specification.
     */
    @Test
    public void testGetCrsFromMetadata() throws IOException, SQLException {
        store = getDataStore(worldGridDirParams);

        // Get the dialect to test CRS handling
        GeoParquetDialect dialect = (GeoParquetDialect) ((JDBCDataStore) store).getSQLDialect();
        SimpleFeatureType schema = store.getSchema("points");

        // Get metadata and verify it has valid CRS information
        GeoparquetDatasetMetadata metadata = dialect.getGeoparquetMetadata(schema);
        assertNotNull(metadata);
        assertNotNull(metadata.getCrs());

        // Test getting SRID from metadata
        try (Connection cx = ((JDBCDataStore) store).getConnection(Transaction.AUTO_COMMIT)) {
            Integer srid = dialect.getGeometrySRIDInternal(metadata, "invalid_column", cx);
            assertNull(srid);
            String geomColumn = metadata.getPrimaryColumnName().orElseThrow();
            srid = dialect.getGeometrySRIDInternal(metadata, geomColumn, cx);
            // Should be 4326 (WGS84) as defined in the test data
            assertEquals(Integer.valueOf(4326), srid);
        }
    }

    /**
     * Helper method to test the schema for a GeoParquet feature type, including CRS validation.
     *
     * <p>This method tests:
     *
     * <ol>
     *   <li>That the feature type schema can be loaded correctly
     *   <li>That the GeoParquet metadata contains valid CRS information
     *   <li>That the bounds have a proper CoordinateReferenceSystem
     *   <li>That the geometry descriptor has the expected geometry type
     *   <li>That the geometry descriptor has a valid CRS attached
     * </ol>
     *
     * <p>The CRS validation ensures that:
     *
     * <ol>
     *   <li>CRS information is correctly extracted from the GeoParquet 'geo' metadata
     *   <li>The PROJJSON CRS representation is properly converted to GeoTools CRS
     *   <li>The CRS is properly attached to both the bounds and geometry descriptor
     * </ol>
     *
     * @param store The GeoParquet DataStore
     * @param typeName The feature type name to test
     * @param geomType The expected geometry class
     * @return The validated SimpleFeatureType
     * @throws IOException If there's an error accessing the schema
     */
    private SimpleFeatureType testGetSchema(DataStore store, String typeName, Class<? extends Geometry> geomType)
            throws IOException {

        GeoParquetDialect dialect = (GeoParquetDialect) ((JDBCDataStore) store).getSQLDialect();
        SimpleFeatureType schema = store.getSchema(typeName);

        GeoparquetDatasetMetadata aggregattedGeo = dialect.getGeoparquetMetadata(schema);
        assertNotNull(aggregattedGeo);
        ReferencedEnvelope bounds = aggregattedGeo.getBounds();

        // Verify we have a valid CRS
        assertNotNull(bounds.getCoordinateReferenceSystem());

        GeometryDescriptor geometryDescriptor = schema.getGeometryDescriptor();
        assertEquals(geomType, geometryDescriptor.getType().getBinding());

        // Verify the geometry descriptor has a CRS
        assertNotNull(geometryDescriptor.getCoordinateReferenceSystem());

        return schema;
    }
}
