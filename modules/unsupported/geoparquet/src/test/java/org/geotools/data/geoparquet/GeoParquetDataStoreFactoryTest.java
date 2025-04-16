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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import org.geotools.api.data.DataStoreFactorySpi;
import org.geotools.api.data.DataStoreFinder;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.util.logging.Logging;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

/** Tests for GeoParquetDataStoreFactory. */
public class GeoParquetDataStoreFactoryTest {

    private static final Logger LOGGER = Logging.getLogger(GeoParquetDataStoreFactoryTest.class);

    @ClassRule
    public static final GeoParquetTestSupport support = new GeoParquetTestSupport();

    private GeoParquetDataStoreFactory factory;
    private Map<String, Object> params;

    @Before
    public void setUp() {

        factory = new GeoParquetDataStoreFactory();
        params = new HashMap<>();

        File directory = support.getWorldgridDir();
        // Basic params
        params.put(GeoParquetDataStoreFactory.DBTYPE.key, "geoparquet");
        params.put(GeoParquetDataStoreFactory.URI_PARAM.key, directory.toURI().toASCIIString());
    }

    @Test
    public void testGetDisplayName() {
        assertEquals("GeoParquet", factory.getDisplayName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("GeoParquet format data files (*.parquet)", factory.getDescription());
    }

    @Test
    public void testCanProcess() {
        assertTrue(factory.canProcess(params));

        // Wrong database type
        Map<String, Object> wrongParams = new HashMap<>(params);
        wrongParams.put(GeoParquetDataStoreFactory.DBTYPE.key, "postgis");
        assertFalse(factory.canProcess(wrongParams));
    }

    @Test
    public void testCreateDataStore() throws IOException {
        // We're not checking if the file exists in the factory
        DataStore ds = factory.createDataStore(params);

        assertNotNull(ds);
        assertTrue(ds instanceof JDBCDataStore);

        // Check the dialect is correctly set
        JDBCDataStore jdbcDS = (JDBCDataStore) ds;
        assertTrue(jdbcDS.getSQLDialect() instanceof GeoParquetDialect);
    }

    @Test
    public void testWithURL() {
        // Test with URL parameter
        Map<String, Object> urlParams = new HashMap<>(params);
        urlParams.put(GeoParquetDataStoreFactory.URI_PARAM.key, "http://example.com/test.parquet");

        assertTrue(factory.canProcess(urlParams));

        // Both file and URL should not work
        Map<String, Object> bothParams = new HashMap<>(urlParams);
        bothParams.put(GeoParquetDataStoreFactory.URI_PARAM.key, "test_file.parquet");

        // This should still pass canProcess but may fail on createDataStore
        assertTrue(factory.canProcess(bothParams));
    }

    @Test
    public void testMissingRequired() throws IOException {
        // Missing file and URL
        Map<String, Object> missingParams = new HashMap<>(params);
        missingParams.remove(GeoParquetDataStoreFactory.URI_PARAM.key);

        assertFalse(factory.canProcess(missingParams));

        try {
            factory.createDataStore(missingParams);
            Assert.fail("Should have thrown IOException for missing file/url parameter");
        } catch (UncheckedIOException e) {
            // Expected exception
        }
    }

    @Test
    public void testFactoryFinder() {
        // Test that our factory can be found through DataStoreFinder
        DataStoreFactorySpi found = null;
        // Convert iterator to iterable
        Iterator<DataStoreFactorySpi> it = DataStoreFinder.getAvailableDataStores();
        while (it.hasNext()) {
            DataStoreFactorySpi f = it.next();
            if ("GeoParquet".equals(f.getDisplayName())) {
                found = f;
                break;
            }
        }
        assertNotNull("Should find the GeoParquet factory", found);
        assertEquals(GeoParquetDataStoreFactory.class, found.getClass());
    }

    /**
     * Tests that multiple threads can access the same GeoParquet datastore concurrently and see the same tables. This
     * is crucial for connection pooling scenarios where multiple connections might be used simultaneously.
     *
     * <p>This test verifies we can safely remove GeoParquetDialect.initializeConnection(Connection) relying on the
     * persistent database file to maintain registered views.
     */
    @Test
    public void testMultiThreadedTypeNames() throws Exception {
        LOGGER.info("Starting multi-threaded GeoParquet DataStore test");

        // Create a DataStore that all threads will share
        DataStore ds = factory.createDataStore(params);
        assertNotNull(ds);

        // Verify that the data store can see the expected tables before starting threads
        String[] initialTypeNames = ds.getTypeNames();
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
                        String[] typeNames = ds.getTypeNames();

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

        // Verify the data store still has the correct type names after all threads finish
        String[] finalTypeNames = ds.getTypeNames();
        LOGGER.info("Final type names: " + Arrays.toString(finalTypeNames));
        assertEquals(expectedTypeNames, new HashSet<>(Arrays.asList(finalTypeNames)));

        // Clean up
        ds.dispose();
        LOGGER.info("Multi-threaded test completed successfully");
    }
}
