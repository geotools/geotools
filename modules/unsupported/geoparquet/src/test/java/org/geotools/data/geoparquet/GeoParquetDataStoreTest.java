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

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;

import com.google.common.collect.Sets;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Struct;
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
import java.util.stream.Collectors;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFinder;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

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

    private GeoparquetDataStore store;

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
        String globbingUrl = "%s/**/*".formatted(worldgridPartitionedDir.getAbsolutePath());

        worldgridPartitionedParams.put(GeoParquetDataStoreFactory.DBTYPE.key, "geoparquet");
        worldgridPartitionedParams.put(GeoParquetDataStoreFactory.URI_PARAM.key, globbingUrl);
    }

    @After
    public void tearDown() {
        if (store != null) {
            store.dispose();
        }
    }

    static GeoparquetDataStore getDataStore(Map<String, ?> params) throws IOException {
        return (GeoparquetDataStore) DataStoreFinder.getDataStore(params);
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
                            String errorMsg = "Thread %d, iteration %d: Expected type names %s but got %s"
                                    .formatted(threadId, j, expectedTypeNames, actualTypeNames);
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
                                LOGGER.fine("Thread %d, iteration %d: Successfully got type names %s"
                                        .formatted(threadId, j, actualTypeNames));
                            }
                        }
                        for (String typeName : actualTypeNames) {
                            SimpleFeatureType schema = store.getSchema(typeName);
                            assertNotNull(schema);
                            assertEquals(typeName, schema.getTypeName());
                        }
                    }
                } catch (Exception e) {
                    String errorMsg = "Thread %d failed with exception: %s".formatted(threadId, e.getMessage());
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
        LOGGER.info("Test completed: %d/%d successful iterations (%.2f%%)"
                .formatted(actualSuccess, expectedTotal, 100.0 * actualSuccess / expectedTotal));

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
    public void testGeometryTypes() throws Exception {
        store = getDataStore(worldgridPartitionedParams);

        testGeometryAttribute(store, "theme_lines_type_line", LineString.class);
        testGeometryAttribute(store, "theme_lines_type_multiline", MultiLineString.class);
        testGeometryAttribute(store, "theme_points_type_point", Point.class);
        testGeometryAttribute(store, "theme_points_type_multipoint", MultiPoint.class);
        testGeometryAttribute(store, "theme_polygons_type_polygon", Polygon.class);
        testGeometryAttribute(store, "theme_polygons_type_multipolygon", MultiPolygon.class);
    }

    @Test
    public void testGeometryTypesHivePartitioned() throws Exception {
        worldgridPartitionedParams.put(GeoParquetDataStoreFactory.MAX_HIVE_DEPTH.key, 1);
        store = getDataStore(worldgridPartitionedParams);

        testGeometryAttribute(store, "theme_lines", MultiLineString.class);
        testGeometryAttribute(store, "theme_points", MultiPoint.class);
        testGeometryAttribute(store, "theme_polygons", MultiPolygon.class);

        worldgridPartitionedParams.put(GeoParquetDataStoreFactory.MAX_HIVE_DEPTH.key, 0);
        store = getDataStore(worldgridPartitionedParams);

        testGeometryAttribute(store, "worldgrid_partitioned", Geometry.class);
    }

    /**
     * Test that when limiting the number of hive-partitions, {@literal read_parquet(<uri>, union_by_name=true)} is used
     * and hence the resulting schema contains the union of all attributes
     */
    @Test
    public void testGetSchemaHivePartitionUnionByName() throws IOException {
        store = getDataStore(worldgridPartitionedParams);
        assertEquals(
                Set.of(
                        "theme_lines_type_line",
                        "theme_lines_type_multiline",
                        "theme_points_type_point",
                        "theme_points_type_multipoint",
                        "theme_polygons_type_polygon",
                        "theme_polygons_type_multipolygon"),
                Set.copyOf(Arrays.asList(store.getTypeNames())));

        Set<String> lines = getAttributeNames(store, "theme_lines_type_line");
        Set<String> multilines = getAttributeNames(store, "theme_lines_type_multiline");
        Set<String> points = getAttributeNames(store, "theme_points_type_point");
        Set<String> multipoints = getAttributeNames(store, "theme_points_type_multipoint");
        Set<String> polygons = getAttributeNames(store, "theme_polygons_type_polygon");
        Set<String> multipolygons = getAttributeNames(store, "theme_polygons_type_multipolygon");

        assertNotEquals(lines, points);
        assertNotEquals(points, polygons);

        // now get 2 level less of hive partitioning, since types from the same theme
        // have the exact same attributes due
        // to how the test data is built
        store.dispose();
        worldgridPartitionedParams.put(GeoParquetDataStoreFactory.MAX_HIVE_DEPTH.key, 0);
        store = getDataStore(worldgridPartitionedParams);

        assertEquals(Set.of("worldgrid_partitioned"), Set.copyOf(Arrays.asList(store.getTypeNames())));

        Set<String> unionedAttributes = getAttributeNames(store, "worldgrid_partitioned");

        Set<String> expected = new HashSet<>();
        expected.addAll(Sets.union(lines, multilines));
        expected.addAll(Sets.union(points, multipoints));
        expected.addAll(Sets.union(polygons, multipolygons));
        assertThat(unionedAttributes, equalTo(expected));
    }

    private Set<String> getAttributeNames(GeoparquetDataStore store, String typeName) throws IOException {
        return store.getSchema(typeName).getAttributeDescriptors().stream()
                .map(AttributeDescriptor::getLocalName)
                .collect(Collectors.toSet());
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
        GeoParquetDialect dialect = store.getSQLDialect();
        SimpleFeatureType schema = store.getSchema("points");

        // Get metadata and verify it has valid CRS information
        GeoparquetDatasetMetadata metadata = dialect.getGeoparquetMetadata(schema.getTypeName());
        assertNotNull(metadata);
        assertNotNull(metadata.getCrs());

        // Test getting SRID from metadata
        Integer srid = dialect.getGeometrySRIDInternal(metadata, "invalid_column");
        assertNull(srid);
        String geomColumn = metadata.getPrimaryColumnName().orElseThrow();
        srid = dialect.getGeometrySRIDInternal(metadata, geomColumn);
        // Should be 4326 (WGS84) as defined in the test data
        assertEquals(Integer.valueOf(4326), srid);
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
     * @throws IOException If there's an error accessing the schema
     * @throws FactoryException
     * @throws NoSuchAuthorityCodeException
     */
    private void testGeometryAttribute(GeoparquetDataStore store, String typeName, Class<? extends Geometry> geomType)
            throws Exception {

        final CoordinateReferenceSystem crs = CRS.decode("EPSG:4326", true);

        GeoParquetDialect dialect = store.getSQLDialect();
        SimpleFeatureType schema = store.getSchema(typeName);

        GeoparquetDatasetMetadata aggregattedGeo = dialect.getGeoparquetMetadata(schema.getTypeName());
        assertNotNull(aggregattedGeo);
        ReferencedEnvelope bounds = aggregattedGeo.getBounds();

        // Verify we have a valid CRS
        assertNotNull(bounds.getCoordinateReferenceSystem());
        assertThat(bounds.getCoordinateReferenceSystem(), equalTo(crs));

        GeometryDescriptor geometryDescriptor = schema.getGeometryDescriptor();
        assertEquals(geomType, geometryDescriptor.getType().getBinding());

        // Verify the geometry descriptor has a CRS
        assertNotNull(geometryDescriptor.getCoordinateReferenceSystem());
        assertThat(geometryDescriptor.getCoordinateReferenceSystem(), equalTo(crs));

        SimpleFeatureSource featureSource = store.getFeatureSource(typeName);
        assertEquals(schema, featureSource.getSchema());

        try (SimpleFeatureIterator it = featureSource.getFeatures().features()) {
            assertTrue(it.hasNext());
            while (it.hasNext()) {
                SimpleFeature feature = it.next();
                assertEquals(schema, feature.getFeatureType());
                Geometry value = (Geometry) feature.getAttribute(geometryDescriptor.getLocalName());
                assertThat(value, instanceOf(geomType));
            }
        }
    }

    @Test
    public void testStruct() throws IOException, SQLException {
        store = getDataStore(worldgridPartitionedParams);

        SimpleFeatureSource featureSource = store.getFeatureSource("theme_lines_type_line");
        SimpleFeatureCollection fc = featureSource.getFeatures();
        SimpleFeature feature;
        try (SimpleFeatureIterator it = fc.features()) {
            feature = it.next();
        }

        Object bbox = feature.getAttribute("bbox");
        assertThat(bbox, instanceOf(java.sql.Struct.class));

        java.sql.Struct struct = (Struct) bbox;
        Object[] attributes = struct.getAttributes();
        assertNotNull(attributes);
        assertEquals(4, attributes.length);

        String sqlTypeName = struct.getSQLTypeName();
        assertEquals("STRUCT(xmin FLOAT, xmax FLOAT, ymin FLOAT, ymax FLOAT)", sqlTypeName);
    }

    @Test
    public void testGetBounds() throws Exception {
        store = getDataStore(worldgridPartitionedParams);
        testWorldBounds("theme_points_type_point");
        testWorldBounds("theme_lines_type_line");
    }

    private void testWorldBounds(String typeName) throws NoSuchAuthorityCodeException, FactoryException, IOException {
        final ReferencedEnvelope world = new ReferencedEnvelope(-180, 180, -90, 90, CRS.decode("EPSG:4326", true));

        SimpleFeatureSource featureSource = store.getFeatureSource(typeName);
        ReferencedEnvelope bounds;

        bounds = featureSource.getBounds();
        assertThat(bounds, equalTo(world));

        bounds = featureSource.getBounds(new Query());
        assertThat(bounds, equalTo(world));

        bounds = featureSource.getBounds(new Query());
        assertThat(bounds, equalTo(world));

        SimpleFeatureCollection fc = featureSource.getFeatures();
        bounds = fc.getBounds();
        assertThat(bounds, equalTo(world));

        Query query = new Query();
        query.setMaxFeatures(1);
        bounds = featureSource.getBounds(query);
        assertThat(bounds, not(equalTo(world)));
    }

    /**
     * Fallback method when there's no geo metadata but there's a {@literal STRUCT(xmin FLOAT, xmax FLOAT, ymin FLOAT,
     * ymax FLOAT)} {@code bbox} column
     *
     * @see GeoParquetDialect#computeBoundsFromBboxColumn(SimpleFeatureType, java.sql.Connection)
     */
    @Test
    public void testGeoParquetDialectComputeBoundsFromBboxColumn() throws Exception {
        store = getDataStore(worldgridPartitionedParams);
        GeoParquetDialect dialect = store.getSQLDialect();
        final ReferencedEnvelope world = new ReferencedEnvelope(-180, 180, -90, 90, CRS.decode("EPSG:4326", true));

        try (Connection cx = dialect.getConnection()) {
            SimpleFeatureType schema = store.getSchema("theme_points_type_point");
            ReferencedEnvelope bounds = dialect.computeBoundsFromBboxColumn(schema, cx);
            assertThat(bounds, equalTo(world));
        }
    }
}
