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
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import org.geotools.jackson.datatype.geoparquet.GeoParquetModule;
import org.junit.ClassRule;
import org.junit.Test;
import tools.jackson.databind.ObjectMapper;

/** Tests for GeoParquetTestSupport to verify it correctly generates test data structure. */
public class GeoParquetTestSupportTest {

    /** The test support rule that will generate our test data */
    @ClassRule
    public static final GeoParquetTestSupport testData = new GeoParquetTestSupport();

    @Test
    public void testPointsFileGenerated() {
        // Get the points file from the test data
        File pointsFile = testData.getWorldgridFile("points.parquet");

        // Verify file exists
        assertTrue("Points file should exist", pointsFile.exists());
    }

    @Test
    public void testLinesFileGenerated() {
        // Get the lines file from the test data
        File linesFile = testData.getWorldgridFile("lines.parquet");

        // Verify file exists
        assertTrue("Lines file should exist", linesFile.exists());
    }

    @Test
    public void testPolygonsFileGenerated() {
        // Get the polygons file from the test data
        File polygonsFile = testData.getWorldgridFile("polygons.parquet");

        // Verify file exists
        assertTrue("Polygons file should exist", polygonsFile.exists());
    }

    @Test
    public void testPartitionedFilesGenerated() {
        // Verify partitioned directories exist
        File pointsPartition = testData.getWorldgridPartition("points", "point");
        File multipointsPartition = testData.getWorldgridPartition("points", "multipoint");
        File linesPartition = testData.getWorldgridPartition("lines", "line");
        File multilinesPartition = testData.getWorldgridPartition("lines", "multiline");
        File polygonsPartition = testData.getWorldgridPartition("polygons", "polygon");
        File multipolygonsPartition = testData.getWorldgridPartition("polygons", "multipolygon");

        assertTrue("Points partition directory should exist", pointsPartition.exists());
        assertTrue("Multipoints partition directory should exist", multipointsPartition.exists());
        assertTrue("Lines partition directory should exist", linesPartition.exists());
        assertTrue("Multilines partition directory should exist", multilinesPartition.exists());
        assertTrue("Polygons partition directory should exist", polygonsPartition.exists());
        assertTrue("Multipolygons partition directory should exist", multipolygonsPartition.exists());

        // Verify each partition contains parquet files
        assertTrue(
                "Points partition should contain files",
                pointsPartition.listFiles(f -> f.getName().endsWith(".parquet")).length > 0);
        assertTrue(
                "Multipoints partition should contain files",
                multipointsPartition.listFiles(f -> f.getName().endsWith(".parquet")).length > 0);
    }

    @Test
    public void testPartitionFileAccess() {
        // Get a file from a partition and verify it can be accessed
        File pointFile = testData.getPartitionFile("points", "point");

        assertNotNull("Should be able to get a partition file", pointFile);
        assertTrue("Partition file should exist", pointFile.exists());
    }

    @Test
    @SuppressWarnings({"unchecked", "PMD.CheckResultSet"})
    public void testGeoParquetMetadataStructure() throws IOException, SQLException {
        File pointsFile = testData.getWorldgridFile("points.parquet");

        try (Connection conn = DriverManager.getConnection("jdbc:duckdb:");
                Statement stmt = conn.createStatement()) {

            stmt.execute("install spatial");
            stmt.execute("load spatial");
            stmt.execute("install parquet");
            stmt.execute("load parquet");

            String sql = "SELECT decode(value) AS value FROM parquet_kv_metadata('%s') where key = 'geo'"
                    .formatted(pointsFile.getAbsolutePath());

            String metadataJson;
            try (ResultSet rs = stmt.executeQuery(sql)) {
                assertTrue("Parquet file should have 'geo' metadata", rs.next());
                metadataJson = rs.getString("value");
                assertNotNull("Geo metadata value should not be null", metadataJson);
            }

            ObjectMapper mapper = GeoParquetModule.createObjectMapper();
            Map<String, Object> metadata = mapper.readValue(metadataJson, Map.class);

            // Verify metadata structure
            assertTrue("Metadata should have 'version' field", metadata.containsKey("version"));
            assertTrue("Metadata should have 'primary_column' field", metadata.containsKey("primary_column"));
            assertTrue("Metadata should have 'columns' field", metadata.containsKey("columns"));

            Map<String, Object> columns = (Map<String, Object>) metadata.get("columns");
            Map<String, Object> geometryColumn = (Map<String, Object>) columns.get("geometry");

            assertTrue("Geometry column should have 'encoding' field", geometryColumn.containsKey("encoding"));
            assertTrue(
                    "Geometry column should have 'geometry_types' field", geometryColumn.containsKey("geometry_types"));
            assertTrue("Geometry column should have 'bbox' field", geometryColumn.containsKey("bbox"));

            // Note: CRS is currently not added by DuckDB 1.2.2, but we document its expected location
            // in tests for when the DuckDB version is updated
            if (geometryColumn.containsKey("crs")) {
                // If crs exists, verify it's structured correctly
                Map<String, Object> crs = (Map<String, Object>) geometryColumn.get("crs");
                assertTrue("CRS should have type field", crs.containsKey("type"));
                assertEquals("CRS type should be GeographicCRS", "GeographicCRS", crs.get("type"));
            }
        }
    }
}
