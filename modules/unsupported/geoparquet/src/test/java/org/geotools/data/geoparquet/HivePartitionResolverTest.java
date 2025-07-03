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

import static org.geotools.data.geoparquet.HivePartitionResolver.buildPartitionMap;
import static org.geotools.data.geoparquet.HivePartitionResolver.buildPartitionName;
import static org.geotools.data.geoparquet.HivePartitionResolver.extractPartitionPath;
import static org.geotools.data.geoparquet.HivePartitionResolver.partitionCount;
import static org.geotools.data.geoparquet.HivePartitionResolver.toNCName;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

/**
 * Comprehensive test suite for the HivePartitionResolver class, focusing on the buildPartitionMap() function with
 * various URI types and structures.
 */
public class HivePartitionResolverTest {

    @Test
    public void testBuildPartitionName() {
        // Hive partition paths
        testPartitionName(
                "s3://overturemaps-us-west-2/release/2025-02-19.0/theme=addresses/type=address/*",
                "theme_addresses_type_address");

        testPartitionName(
                "s3://overturemaps-us-west-2/release/2025-02-19.0/theme=addresses/type=address/*.parquet",
                "theme_addresses_type_address");

        testPartitionName("s3://overturemaps-us-west-2/release/2025-02-19.0/theme=addresses/**/*", "theme_addresses");

        testPartitionName("s3://overturemaps-us-west-2/release/2025-02-19.0/**/*", "_2025_02_19.0");

        testPartitionName("s3://overturemaps-us-west-2/release/**/*", "release");

        // Simple valid names
        testPartitionName("lines.parquet", "lines");
        testPartitionName("/data/parquet/countries.parquet", "countries");
        testPartitionName("/data/parquet/*.parquet", "parquet");

        // With query parameters
        testPartitionName(
                "s3://some-private-bucket/theme=addresses/type=address/*?s3_region=us-west-2",
                "theme_addresses_type_address");
    }

    private void testPartitionName(String partitionUri, String expectedName) {
        String actual = buildPartitionName(partitionUri);
        assertEquals(expectedName, actual);
    }

    @Test
    public void testToNCName() {

        // Hive partition paths
        testToNCName("theme=lines/type=line", "theme_lines_type_line");
        testToNCName("theme=lines/type=multiline", "theme_lines_type_multiline");
        testToNCName("theme=points/type=point", "theme_points_type_point");
        testToNCName("theme=points/type=multipoint", "theme_points_type_multipoint");
        testToNCName("theme=polygons/type=polygon", "theme_polygons_type_polygon");
        testToNCName("theme=polygons/type=multipolygon", "theme_polygons_type_multipolygon");

        // Simple valid names
        testToNCName("lines", "lines");
        testToNCName("countries", "countries");

        // Names needing transformation
        testToNCName("my layer", "my_layer"); // Contains space
        testToNCName("1layer", "_1layer"); // Starts with digit
        testToNCName("-layer", "_layer"); // Starts with hyphen
        testToNCName(".layer", "_.layer"); // Starts with period
        testToNCName("layer:name", "layer_name"); // Contains colon
        testToNCName("weird//path=]=value", "weird_path_value"); // Multiple issues, empty parts

        // Single key-value pairs
        testToNCName("theme=admin", "theme_admin");
        testToNCName("admin", "admin");

        // Valid characters
        testToNCName("name_with-hyphen.ok", "name_with_hyphen.ok"); // Valid chars, hyphen replaced

        // Edge cases
        testToNCName("  leading and trailing spaces  ", "leading_and_trailing_spaces"); // Trim needed
        testToNCName("key=", "key"); // Key with empty value
        testToNCName("=value", "_value"); // Empty key
        testToNCName("key=value/", "key_value"); // Trailing slash
        testToNCName("/key=value", "_key_value"); // Leading slash
        testToNCName("a=b=c/d=e", "a_b_c_d_e"); // Multiple equals signs
        testToNCName(null, "_"); // Null
        testToNCName("", "_"); // Empty string
        testToNCName("   ", "_"); // Whitespace only
        testToNCName("_=", "_"); // Only invalid chars
        testToNCName(":", "_"); // Only invalid char
    }

    private void testToNCName(String input, String expected) {
        String actual = toNCName(input);

        assertEquals(
                String.format("Input '%s' should convert to '%s' but got '%s'", input, expected, actual),
                expected,
                actual);
    }

    @Test
    public void testExtractPartitionPath() {
        String fileUri = "s3://mybucket/data/year=2023/month=01/day=01/file1.parquet";

        assertEquals("year=2023/month=01/day=01", extractPartitionPath(fileUri, null));
        assertEquals("year=2023/month=01/day=01", extractPartitionPath(fileUri, 4));
        assertEquals("year=2023/month=01/day=01", extractPartitionPath(fileUri, 3));

        assertEquals("year=2023/month=01", extractPartitionPath(fileUri, 2));
        assertEquals("year=2023", extractPartitionPath(fileUri, 1));

        assertEquals("year=2023/month=01/day=01", extractPartitionPath(fileUri, 0));

        IllegalArgumentException e =
                assertThrows(IllegalArgumentException.class, () -> extractPartitionPath(fileUri, -1));

        assertThat(e.getMessage(), CoreMatchers.containsString("maxDepth is negative"));
    }

    @Test
    public void tesBuildPartitionMapSingleLocalParquetFile() {
        List<String> files = List.of("/data/myfile.parquet");

        final Map<String, List<String>> expected = Map.of("/data/myfile.parquet", List.of("/data/myfile.parquet"));

        Map<String, List<String>> result;
        result = buildPartitionMap("/data/myfile.parquet", files, null);
        assertEquals(expected, result);

        result = buildPartitionMap("/data/*.parquet", files, null);
        assertEquals(expected, result);

        result = buildPartitionMap("/data/*", files, null);
        assertEquals(expected, result);

        result = buildPartitionMap("/**/*", files, null);
        assertEquals(expected, result);
    }

    @Test
    public void tesBuildPartitionMapLocalDirectoryNonPartitioned() {
        List<String> files =
                List.of("/data/parquet/file1.parquet", "/data/parquet/file2.parquet", "/data/parquet/file3.parquet");

        Map<String, List<String>> expected = Map.of(
                "/data/parquet/file1.parquet",
                List.of("/data/parquet/file1.parquet"),
                "/data/parquet/file2.parquet",
                List.of("/data/parquet/file2.parquet"),
                "/data/parquet/file3.parquet",
                List.of("/data/parquet/file3.parquet"));

        Map<String, List<String>> result;

        result = buildPartitionMap("/data/parquet/**/*", files, null);
        assertEquals(expected, result);

        result = buildPartitionMap("/data/parquet/*", files, null);
        assertEquals(expected, result);

        result = buildPartitionMap("/data/parquet", files, null);
        assertEquals(expected, result);

        result = buildPartitionMap("/data/**/*", files, null);
        assertEquals(expected, result);

        result = buildPartitionMap("/**/*", files, null);
        assertEquals(expected, result);
    }

    @Test
    public void tesBuildPartitionMapNestedLocalDirectoryNonPartitioned() {
        List<String> files = List.of(
                "/data/dir1/file1.parquet",
                "/data/dir1/file2.parquet",
                "/data/dir1/file3.parquet",
                "/data/dir2/subdir/file1.parquet",
                "/data/dir2/subdir/file2.parquet",
                "/data/dir2/subdir/file3.parquet");

        Map<String, List<String>> expected = Map.of(
                "/data/dir1/file1.parquet",
                List.of("/data/dir1/file1.parquet"),
                "/data/dir1/file2.parquet",
                List.of("/data/dir1/file2.parquet"),
                "/data/dir1/file3.parquet",
                List.of("/data/dir1/file3.parquet"),
                "/data/dir2/subdir/file1.parquet",
                List.of("/data/dir2/subdir/file1.parquet"),
                "/data/dir2/subdir/file2.parquet",
                List.of("/data/dir2/subdir/file2.parquet"),
                "/data/dir2/subdir/file3.parquet",
                List.of("/data/dir2/subdir/file3.parquet"));

        Map<String, List<String>> result;

        result = buildPartitionMap("/data/**/*", files, null);
        assertEquals(expected, result);

        result = buildPartitionMap("/**/*", files, null);
        assertEquals(expected, result);
    }

    @Test
    public void tesBuildPartitionMapSingleS3ParquetFile() {
        List<String> files = List.of("s3://mybucket/data/myfile.parquet");

        Map<String, List<String>> expected =
                Map.of("s3://mybucket/data/myfile.parquet", List.of("s3://mybucket/data/myfile.parquet"));

        Map<String, List<String>> result;

        result = buildPartitionMap("s3://mybucket/data/myfile.parquet", files, null);
        assertEquals(expected, result);

        result = buildPartitionMap("s3://mybucket/data/*", files, null);
        assertEquals(expected, result);

        result = buildPartitionMap("s3://mybucket/**/*", files, null);
        assertEquals(expected, result);
    }

    @Test
    public void tesBuildPartitionMapSingleS3ParquetFileWithQueryParameters() {
        List<String> files = List.of("s3://mybucket/data/myfile.parquet?s3_region=us-west-2");

        Map<String, List<String>> expected = Map.of(
                "s3://mybucket/data/myfile.parquet?s3_region=us-west-2",
                List.of("s3://mybucket/data/myfile.parquet?s3_region=us-west-2"));

        Map<String, List<String>> result;

        result = buildPartitionMap("s3://mybucket/data/myfile.parquet?s3_region=us-west-2", files, null);
        assertEquals(expected, result);

        result = buildPartitionMap("s3://mybucket/data/*?s3_region=us-west-2", files, null);
        assertEquals(expected, result);

        result = buildPartitionMap("s3://mybucket/**/*?s3_region=us-west-2", files, null);
        assertEquals(expected, result);
    }

    @Test
    public void tesBuildPartitionMapS3DirectoryNonPartitioned() {
        List<String> files = List.of(
                "s3://mybucket/data/file1.parquet",
                "s3://mybucket/data/file2.parquet",
                "s3://mybucket/data/subdir/file3.parquet");

        final Map<String, List<String>> expected = Map.of(
                "s3://mybucket/data/file1.parquet",
                List.of("s3://mybucket/data/file1.parquet"),
                "s3://mybucket/data/file2.parquet",
                List.of("s3://mybucket/data/file2.parquet"),
                "s3://mybucket/data/subdir/file3.parquet",
                List.of("s3://mybucket/data/subdir/file3.parquet"));

        Map<String, List<String>> result;
        result = buildPartitionMap("s3://mybucket/data/**/*", files, null);
        assertEquals(expected, result);

        result = buildPartitionMap("s3://mybucket/**/*", files, null);
        assertEquals(expected, result);
    }

    @Test
    public void tesBuildPartitionMaptHivePartitionedLocalFiles() {
        final List<String> files = List.of(
                "/data/year=2023/month=01/day=01/file1.parquet",
                "/data/year=2023/month=01/day=02/file2.parquet",
                // Two files in same partition
                "/data/year=2023/month=02/day=01/file3.parquet",
                "/data/year=2023/month=02/day=01/file4.parquet");

        final Map<String, List<String>> expected = Map.of(
                "/data/year=2023/month=01/day=01/*",
                List.of("/data/year=2023/month=01/day=01/file1.parquet"),
                "/data/year=2023/month=01/day=02/*",
                List.of("/data/year=2023/month=01/day=02/file2.parquet"),
                "/data/year=2023/month=02/day=01/*",
                List.of(
                        "/data/year=2023/month=02/day=01/file3.parquet",
                        "/data/year=2023/month=02/day=01/file4.parquet"));

        Map<String, List<String>> result;

        result = buildPartitionMap("/data/**/*", files, null);
        assertEquals(expected, result);

        result = buildPartitionMap("/**/*", files, null);
        assertEquals(expected, result);
    }

    @Test
    public void tesBuildPartitionMapHivePartitionedS3Files() {
        final List<String> files = List.of(
                "s3://mybucket/data/year=2023/month=01/day=01/file1.parquet",
                "s3://mybucket/data/year=2023/month=01/day=02/file2.parquet",
                // Two files in same partition
                "s3://mybucket/data/year=2023/month=02/day=01/file3.parquet",
                "s3://mybucket/data/year=2023/month=02/day=01/file4.parquet");

        Map<String, List<String>> expected = Map.of(
                "s3://mybucket/data/year=2023/month=01/day=01/*",
                List.of("s3://mybucket/data/year=2023/month=01/day=01/file1.parquet"),
                "s3://mybucket/data/year=2023/month=01/day=02/*",
                List.of("s3://mybucket/data/year=2023/month=01/day=02/file2.parquet"),
                "s3://mybucket/data/year=2023/month=02/day=01/*",
                List.of(
                        "s3://mybucket/data/year=2023/month=02/day=01/file3.parquet",
                        "s3://mybucket/data/year=2023/month=02/day=01/file4.parquet"));

        Map<String, List<String>> result = buildPartitionMap("s3://mybucket/data/**/*", files, null);

        assertEquals(expected, result);

        result = buildPartitionMap("s3://mybucket/**/*", files, null);
        assertEquals(expected, result);

        assertEquals(3, partitionCount(files.get(0)));
    }

    @Test
    public void tesBuildPartitionMapHivePartitionedMaxDepth() {
        final List<String> files = List.of(
                "s3://mybucket/data/year=2023/month=01/day=01/file1.parquet",
                "s3://mybucket/data/year=2023/month=01/day=02/file2.parquet",
                // Two files in same partition
                "s3://mybucket/data/year=2023/month=02/day=01/file3.parquet",
                "s3://mybucket/data/year=2023/month=02/day=01/file4.parquet");

        Map<String, List<String>> expected = Map.of(
                "s3://mybucket/data/year=2023/month=01/day=01/*",
                List.of("s3://mybucket/data/year=2023/month=01/day=01/file1.parquet"),
                "s3://mybucket/data/year=2023/month=01/day=02/*",
                List.of("s3://mybucket/data/year=2023/month=01/day=02/file2.parquet"),
                "s3://mybucket/data/year=2023/month=02/day=01/*",
                List.of(
                        "s3://mybucket/data/year=2023/month=02/day=01/file3.parquet",
                        "s3://mybucket/data/year=2023/month=02/day=01/file4.parquet"));

        Integer maxDepth = null;
        Map<String, List<String>> result;
        result = buildPartitionMap("s3://mybucket/data/**/*", files, maxDepth);
        assertEquals(expected, result);

        assertEquals(3, partitionCount(files.get(0)));

        // test maxDepth > partitionCount()
        maxDepth = 4;
        result = buildPartitionMap("s3://mybucket/data/**/*", files, maxDepth);
        assertEquals(expected, result);

        // test maxDepth == partitionCount()
        maxDepth = 3;
        result = buildPartitionMap("s3://mybucket/**/*", files, maxDepth);
        assertEquals(expected, result);

        // test maxDepth = 2
        maxDepth = 2;
        expected = Map.of(
                // year=2023/month=01
                "s3://mybucket/data/year=2023/month=01/**/*",
                List.of(
                        "s3://mybucket/data/year=2023/month=01/day=01/file1.parquet",
                        "s3://mybucket/data/year=2023/month=01/day=02/file2.parquet"),
                // year=2023/month=02
                "s3://mybucket/data/year=2023/month=02/**/*",
                List.of(
                        "s3://mybucket/data/year=2023/month=02/day=01/file3.parquet",
                        "s3://mybucket/data/year=2023/month=02/day=01/file4.parquet"));

        result = buildPartitionMap("s3://mybucket/**/*", files, maxDepth);
        assertEquals(expected, result);

        // test maxDepth = 1
        maxDepth = 1;
        expected = Map.of(
                // year=2023
                "s3://mybucket/data/year=2023/**/*",
                List.of(
                        "s3://mybucket/data/year=2023/month=01/day=01/file1.parquet",
                        "s3://mybucket/data/year=2023/month=01/day=02/file2.parquet",
                        "s3://mybucket/data/year=2023/month=02/day=01/file3.parquet",
                        "s3://mybucket/data/year=2023/month=02/day=01/file4.parquet"));

        result = buildPartitionMap("s3://mybucket/**/*", files, maxDepth);
        assertEquals(expected, result);

        // test maxDepth = 0
        maxDepth = 0;
        expected = Map.of(
                "s3://mybucket/data/**/*",
                List.of(
                        "s3://mybucket/data/year=2023/month=01/day=01/file1.parquet",
                        "s3://mybucket/data/year=2023/month=01/day=02/file2.parquet",
                        "s3://mybucket/data/year=2023/month=02/day=01/file3.parquet",
                        "s3://mybucket/data/year=2023/month=02/day=01/file4.parquet"));

        result = buildPartitionMap("s3://mybucket/**/*", files, maxDepth);
        assertEquals(expected, result);
    }

    @Test
    public void tesBuildPartitionMapOvertureMapsExample() {
        List<String> files2025_02_19 = List.of(
                "s3://overturemaps-us-west-2/release/2025-02-19.0/theme=addresses/type=address/part-00000-6f6b9970-5d26-40f7-b396-7627e7258cd8-c000.zstd.parquet",
                "s3://overturemaps-us-west-2/release/2025-02-19.0/theme=addresses/type=address/part-00001-6f6b9970-5d26-40f7-b396-7627e7258cd8-c000.zstd.parquet",
                "s3://overturemaps-us-west-2/release/2025-02-19.0/theme=transportation/type=segment/part-00043-7b8eabdf-2b61-4445-be96-37ce732727d9-c000.zstd.parquet",
                "s3://overturemaps-us-west-2/release/2025-02-19.0/theme=transportation/type=segment/part-00044-7b8eabdf-2b61-4445-be96-37ce732727d9-c000.zstd.parquet");

        List<String> files2025_03_19 = List.of(
                "s3://overturemaps-us-west-2/release/2025-03-19.0/theme=addresses/type=address/part-00000-6f6b9970-5d26-40f7-b396-7627e7258cd8-c000.zstd.parquet",
                "s3://overturemaps-us-west-2/release/2025-03-19.0/theme=addresses/type=address/part-00001-6f6b9970-5d26-40f7-b396-7627e7258cd8-c000.zstd.parquet",
                "s3://overturemaps-us-west-2/release/2025-03-19.0/theme=transportation/type=segment/part-00043-7b8eabdf-2b61-4445-be96-37ce732727d9-c000.zstd.parquet",
                "s3://overturemaps-us-west-2/release/2025-03-19.0/theme=transportation/type=segment/part-00044-7b8eabdf-2b61-4445-be96-37ce732727d9-c000.zstd.parquet");

        List<String> allFiles = Stream.concat(files2025_02_19.stream(), files2025_03_19.stream())
                .collect(Collectors.toList());

        Map<String, List<String>> r2025_02_19 = Map.of(
                // theme=addresses/type=address
                "s3://overturemaps-us-west-2/release/2025-02-19.0/theme=addresses/type=address/*",
                List.of(
                        "s3://overturemaps-us-west-2/release/2025-02-19.0/theme=addresses/type=address/part-00000-6f6b9970-5d26-40f7-b396-7627e7258cd8-c000.zstd.parquet",
                        "s3://overturemaps-us-west-2/release/2025-02-19.0/theme=addresses/type=address/part-00001-6f6b9970-5d26-40f7-b396-7627e7258cd8-c000.zstd.parquet"),
                // theme=transportation/type=segment
                "s3://overturemaps-us-west-2/release/2025-02-19.0/theme=transportation/type=segment/*",
                List.of(
                        "s3://overturemaps-us-west-2/release/2025-02-19.0/theme=transportation/type=segment/part-00043-7b8eabdf-2b61-4445-be96-37ce732727d9-c000.zstd.parquet",
                        "s3://overturemaps-us-west-2/release/2025-02-19.0/theme=transportation/type=segment/part-00044-7b8eabdf-2b61-4445-be96-37ce732727d9-c000.zstd.parquet"));

        Map<String, List<String>> r2025_03_19 = Map.of(
                // theme=addresses/type=address
                "s3://overturemaps-us-west-2/release/2025-03-19.0/theme=addresses/type=address/*",
                List.of(
                        "s3://overturemaps-us-west-2/release/2025-03-19.0/theme=addresses/type=address/part-00000-6f6b9970-5d26-40f7-b396-7627e7258cd8-c000.zstd.parquet",
                        "s3://overturemaps-us-west-2/release/2025-03-19.0/theme=addresses/type=address/part-00001-6f6b9970-5d26-40f7-b396-7627e7258cd8-c000.zstd.parquet"),
                // theme=transportation/type=segment
                "s3://overturemaps-us-west-2/release/2025-03-19.0/theme=transportation/type=segment/*",
                List.of(
                        "s3://overturemaps-us-west-2/release/2025-03-19.0/theme=transportation/type=segment/part-00043-7b8eabdf-2b61-4445-be96-37ce732727d9-c000.zstd.parquet",
                        "s3://overturemaps-us-west-2/release/2025-03-19.0/theme=transportation/type=segment/part-00044-7b8eabdf-2b61-4445-be96-37ce732727d9-c000.zstd.parquet"));

        Map<String, List<String>> all = new HashMap<>(r2025_02_19);
        all.putAll(r2025_03_19);

        Map<String, List<String>> result;

        result = buildPartitionMap("s3://overturemaps-us-west-2/release/2025-02-19.0/**/*", files2025_02_19, null);
        assertEquals(r2025_02_19, result);

        result = buildPartitionMap("s3://overturemaps-us-west-2/release/2025-03-19.0/**/*", files2025_03_19, null);
        assertEquals(r2025_03_19, result);

        result = buildPartitionMap("s3://overturemaps-us-west-2/release/**/*", allFiles, null);
        assertEquals(all, result);
    }

    @Test
    public void tesBuildPartitionMapSpecialPartitionValues() {
        List<String> files = List.of(
                "s3://mybucket/data/country=United_States/city=New_York/file1.parquet",
                "s3://mybucket/data/country=Canada/city=Toronto-ON/file2.parquet",
                "s3://mybucket/data/category=electronics-computers/brand=Dell_Inc/model=XPS-15/file3.parquet");

        Map<String, List<String>> result = buildPartitionMap("s3://mybucket/data/**/*", files, null);

        final Map<String, List<String>> expected = Map.of(
                "s3://mybucket/data/country=United_States/city=New_York/*",
                List.of("s3://mybucket/data/country=United_States/city=New_York/file1.parquet"),
                "s3://mybucket/data/country=Canada/city=Toronto-ON/*",
                List.of("s3://mybucket/data/country=Canada/city=Toronto-ON/file2.parquet"),
                "s3://mybucket/data/category=electronics-computers/brand=Dell_Inc/model=XPS-15/*",
                List.of("s3://mybucket/data/category=electronics-computers/brand=Dell_Inc/model=XPS-15/file3.parquet"));

        assertEquals(expected, result);

        result = buildPartitionMap("s3://mybucket/**/*", files, null);
        assertEquals(expected, result);
    }

    @Test
    public void testEmptyFileList() {
        Map<String, List<String>> result = buildPartitionMap("s3://mybucket/*", List.of(), null);
        assertTrue(result.isEmpty());
    }
}
