/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023-2025, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import org.junit.ClassRule;
import org.junit.Test;

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
}
