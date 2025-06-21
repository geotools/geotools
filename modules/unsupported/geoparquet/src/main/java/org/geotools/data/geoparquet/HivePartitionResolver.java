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

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.net.URI;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 * Utility class for working with Hive-partitioned data files, particularly from object stores like S3.
 *
 * <p>This class provides functionality to discover and organize files according to their Hive partition structure,
 * which follows the pattern of {@code key1=value1/key2=value2/...} in file paths.
 *
 * <p><b>Examples:</b>
 *
 * <p>Given files with paths like:
 *
 * <pre>
 * s3://bucket/data/year=2023/month=01/day=01/file1.parquet
 * s3://bucket/data/year=2023/month=01/day=02/file2.parquet
 * s3://bucket/data/year=2023/month=02/day=01/file3.parquet
 * </pre>
 *
 * <p>The function would return a Map with entries:
 *
 * <pre>
 * "year=2023/month=01/day=01" → [s3://bucket/data/year=2023/month=01/day=01/file1.parquet]
 * "year=2023/month=01/day=02" → [s3://bucket/data/year=2023/month=01/day=02/file2.parquet]
 * "year=2023/month=02/day=01" → [s3://bucket/data/year=2023/month=02/day=01/file3.parquet]
 * </pre>
 *
 * <p>For the Overture Maps example:
 *
 * <pre>
 * s3://overturemaps-us-west-2/release/2025-03-19.0/theme=addresses/type=address/part-00000-xyz.parquet
 * s3://overturemaps-us-west-2/release/2025-03-19.0/theme=addresses/type=address/part-00001-xyz.parquet
 * s3://overturemaps-us-west-2/release/2025-03-19.0/theme=transportation/type=segment/part-00043-abc.parquet
 * </pre>
 *
 * <p>The function would return:
 *
 * <pre>
 * "theme=addresses/type=address" → [
 *     "s3://overturemaps-us-west-2/release/2025-03-19.0/theme=addresses/type=address/part-00000-xyz.parquet",
 *     "s3://overturemaps-us-west-2/release/2025-03-19.0/theme=addresses/type=address/part-00001-xyz.parquet"
 * ]
 * "theme=transportation/type=segment" → [
 *     "s3://overturemaps-us-west-2/release/2025-03-19.0/theme=transportation/type=segment/part-00043-abc.parquet"
 * ]
 * </pre>
 */
class HivePartitionResolver {

    // Pattern for characters NOT allowed in NCNames.
    // Allowed: Letters (a-zA-Z), Digits (0-9), Underscore (_), Hyphen (-), Period (.)
    // This pattern matches anything ELSE.
    private static final Pattern INVALID_NC_CHARS_PATTERN = Pattern.compile("[^a-zA-Z0-9_.-]");

    // Pattern to find sequences of one or more underscores or hyphens (to collapse later)
    // We treat hyphen and underscore similarly as separators here.
    private static final Pattern MULTIPLE_SEPARATORS_PATTERN = Pattern.compile("[_-]+");

    private HivePartitionResolver() {}

    /**
     * Converts an input string into a valid XML NCName (Non-Colonized Name), suitable for use as a WFS FeatureType name
     * or similar identifiers.
     *
     * <p>Handles potential Hive partitioning strings (like "theme=lines/type=line") by extracting values and joining
     * them with underscores. Replaces invalid characters with underscores and ensures the name starts with a letter or
     * an underscore. Collapses multiple consecutive underscores/hyphens.
     *
     * <p>NCName rules:
     *
     * <ul>
     *   <li>1. Must start with a letter or underscore (_).
     *   <li>2. Subsequent characters can be letters, digits, underscore (_), hyphen (-), or period (.).
     *   <li>3. Cannot contain colons (:), spaces, or other special characters.
     * </ul>
     *
     * @param input The raw input string (e.g., "countries", "my layer", "theme=lines/type=line").
     * @return A valid NCName string. Returns "_" if the input is null, empty, or results in an empty string after
     *     sanitization.
     */
    static String toNCName(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "_"; // Return a default valid NCName for null/empty input
        }

        String workingString = input.trim();
        // This handles spaces, special characters, etc.
        workingString = INVALID_NC_CHARS_PATTERN.matcher(workingString).replaceAll("_");
        // Replace sequences of multiple underscores or hyphens with a single underscore
        workingString = MULTIPLE_SEPARATORS_PATTERN.matcher(workingString).replaceAll("_");

        if (workingString.isEmpty() || workingString.equals("_")) {
            return "_";
        }

        char firstChar = workingString.charAt(0);
        boolean startsWithLetter = Character.isLetter(firstChar);

        if (!startsWithLetter && firstChar != '_') {
            // If it doesn't start with a letter or underscore, prepend underscore
            workingString = "_" + workingString;
        }

        if (workingString.endsWith("_") && workingString.length() > 1) {
            workingString = workingString.substring(0, workingString.length() - 1);
        }

        return workingString;
    }

    /**
     * Extracts Hive partition information from file paths and groups files by their partition structure, with control
     * over partition depth.
     *
     * <p>This method uses DuckDB's {@code glob()} function to find files matching the provided URI pattern, then groups
     * them by their Hive partition structure up to the specified maximum depth.
     *
     * <p>Performance differences:
     *
     * <ul>
     *   <li>{@code glob()} only performs listing operations on the filesystem or object store, which is a lightweight
     *       metadata operation
     *   <li>{@code parquet_file_metadata()} must open each Parquet file to read its headers, requiring individual GET
     *       requests for each file, which can be extremely slow for large datasets
     *   <li>{@code parquet_metadata()} similarly needs to access each file's metadata, making it inefficient for
     *       initial discovery of partition structure
     * </ul>
     *
     * <p>For S3 or similar object stores, the difference can be orders of magnitude: listing might take seconds while
     * accessing metadata could take minutes or even time out for large datasets.
     *
     * <p>The maxDepth parameter allows for control over the partition hierarchy:
     *
     * <ul>
     *   <li>maxDepth=null or maxDepth >= partition count: Uses all partition levels
     *   <li>maxDepth=0: Groups all files together under the base path
     *   <li>maxDepth=1: Groups files by first partition level only (e.g., year=2023)
     *   <li>maxDepth=2: Groups files by first and second partition levels (e.g., year=2023/month=01)
     * </ul>
     *
     * @param connection A JDBC connection to DuckDB
     * @param uri A file path, directory, or glob pattern (e.g., "s3://bucket/path/**" or "file:/data/*")
     * @param maxDepth Maximum partition depth to consider, or null to use all partition levels
     * @return A Map where keys are Hive partition paths and values are Lists of file URIs belonging to that partition
     * @throws SQLException If a database error occurs
     * @see #buildPartitionMap(String, List, Integer)
     */
    public static Map<String, List<String>> getHivePartitionedFiles(Connection connection, URI uri, Integer maxDepth)
            throws SQLException {

        // uri can be a no-schema file (or directory) path
        String targetUri = sanitizeURI(uri);

        // First, get the list of files
        List<String> files = listFiles(connection, targetUri);

        // Then, process the files to build the partition map
        return buildPartitionMap(targetUri, files, maxDepth);
    }

    private static String sanitizeURI(URI uri) {
        String finalPath;
        Optional<File> file = toFile(uri);
        if (file.isPresent()) {
            File fileOrDir = file.orElseThrow();
            finalPath = fileOrDir.getAbsolutePath();
            if (fileOrDir.isDirectory()) {
                finalPath += "/*";
            }
        } else {
            finalPath = uri.toASCIIString();
        }
        return finalPath;
    }

    private static Optional<File> toFile(URI uri) {
        if (StringUtils.isEmpty(uri.getScheme())) {
            uri = URI.create("file:" + uri);
        }
        if ("file".equals(uri.getScheme())) {
            File file = new File(uri).getAbsoluteFile();
            return Optional.of(file);
        }
        return Optional.empty();
    }

    /**
     * Lists all files matching the given URI pattern using DuckDB's glob function.
     *
     * @param connection A JDBC connection to DuckDB
     * @param uri A file path, directory, or glob pattern
     * @return A list of file URIs matching the pattern
     * @throws SQLException If a database error occurs
     */
    public static List<String> listFiles(Connection connection, String uri) throws SQLException {
        List<String> files = new ArrayList<>();

        try (Statement stmt = connection.createStatement()) {
            // Execute the query without adding quotes to the URI
            String query = String.format("SELECT file FROM glob('%s')", uri);
            try (ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    files.add(rs.getString("file"));
                }
            }
        }

        return files;
    }

    /**
     * Builds a map of partition paths to file URIs based on Hive partition structure.
     *
     * <p>This method groups files by their Hive partition path structure, which follows the pattern of
     * {@code key1=value1/key2=value2/...} in file paths. The {@code maxDepth} parameter controls how many levels of
     * partitioning to consider.
     *
     * <p>When no partition information is found, uses the file path as the key, preserving a 1:1 relationship between
     * files and keys.
     *
     * <p>Examples of behavior with different maxDepth values:
     *
     * <ul>
     *   <li>maxDepth=null or maxDepth >= number of partitions: Uses all partition levels
     *   <li>maxDepth=0: Groups all files together under the base path
     *   <li>maxDepth=1: Groups files by the first partition level only
     *   <li>maxDepth=2: Groups files by the first two partition levels, and so on
     * </ul>
     *
     * @param uri The original URI potentially with glob patterns (e.g., "s3://bucket/path/**" or "file:/data/*")
     * @param files List of file URIs to process
     * @param maxDepth Maximum number of partition levels to consider, or null to use all levels
     * @return A Map where keys are partition URIs and values are Lists of file URIs belonging to that partition
     */
    static Map<String, List<String>> buildPartitionMap(String uri, List<String> files, Integer maxDepth) {
        Map<String, List<String>> partitionToFiles = new HashMap<>();

        for (String filePath : files) {
            final String fullPartitionParts = extractPartitionPath(filePath);
            final String partitionUri;
            if (fullPartitionParts.isEmpty()) {
                // For non-partitioned files, preserve a 1:1 relationship between partition and file URI
                partitionUri = filePath;
            } else {
                partitionUri = buildPartitionUri(filePath, fullPartitionParts, maxDepth);
            }
            partitionToFiles
                    .computeIfAbsent(partitionUri, k -> new ArrayList<>())
                    .add(filePath);
        }

        return partitionToFiles;
    }

    /**
     * Constructs a partition URI based on the file path, partition structure, and max depth.
     *
     * <p>This method creates a URI that represents a partition at the specified depth level. The resulting URI
     * includes:
     *
     * <ul>
     *   <li>The base path (everything before the partition part)
     *   <li>The partition path up to the specified depth
     *   <li>An appropriate glob pattern ("*" for full depth partitions, "**\/*" for truncated partitions)
     * </ul>
     *
     * <p>For example, given:
     *
     * <ul>
     *   <li>filePath = "s3://bucket/data/year=2023/month=01/day=01/file.parquet"
     *   <li>fullPartitionParts = "year=2023/month=01/day=01"
     * </ul>
     *
     * <p>With different maxDepth values:
     *
     * <ul>
     *   <li>maxDepth=null or 3: "s3://bucket/data/year=2023/month=01/day=01/*"
     *   <li>maxDepth=2: "s3://bucket/data/year=2023/month=01/**\/*"
     *   <li>maxDepth=1: "s3://bucket/data/year=2023/**\/*"
     *   <li>maxDepth=0: "s3://bucket/data/**\/*"
     * </ul>
     *
     * @param filePath The complete file URI
     * @param fullPartitionParts The full partition path extracted from the file URI
     * @param maxDepth Maximum number of partition levels to include, or null to include all
     * @return A partition URI string representing the partition at the specified depth
     */
    private static String buildPartitionUri(String filePath, final String fullPartitionParts, Integer maxDepth) {
        final String partitionUri;
        String basePath = filePath.substring(0, filePath.indexOf(fullPartitionParts));
        if (basePath.endsWith("/")) {
            basePath = basePath.substring(0, basePath.length() - 1);
        }

        int partitionCount = partitionCount(filePath);
        int partitionLimit = maxDepth == null ? partitionCount : Math.min(partitionCount, maxDepth);

        if (partitionLimit == 0) {
            partitionUri = String.format("%s/**/*", basePath);
        } else {
            String limitedPartitionParts = extractPartitionPath(filePath, partitionLimit);
            String glob = partitionCount > partitionLimit ? "**/*" : "*";
            // If partition information found, use it as the key
            partitionUri = String.format("%s/%s/%s", basePath, limitedPartitionParts, glob);
        }
        return partitionUri;
    }

    public static String buildPartitionName(String partitionUri) {
        requireNonNull(partitionUri);
        if (partitionUri.trim().isEmpty()) {
            throw new IllegalArgumentException("empty partition uri");
        }

        String partition = extractPartitionPath(partitionUri);
        if (!partition.isEmpty()) {
            return toNCName(partition);
        }

        List<String> parts = Arrays.stream(partitionUri.split("/"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        // stop at the first glob, or the last element if there're no globs
        String name = "_";
        for (String part : parts) {
            boolean glob = part.contains("*");
            if (glob) {
                break;
            }
            name = part;
        }
        if (name.endsWith(".parquet")) {
            name = name.substring(0, name.lastIndexOf(".parquet"));
        }
        return toNCName(name);
    }

    private static String extractPartitionPath(String fileUri) {
        return extractPartitionPath(fileUri, null);
    }

    /**
     * Extracts the Hive partition path from a file URI.
     *
     * @param fileUri The URI of a file (e.g., 's3://bucket/path/theme=addresses/type=address/file.parquet')
     * @param maxDepth Maximum number of partition levels to include, or null to include all
     * @return The partition path portion (e.g., "theme=addresses/type=address")
     * @throws IllegalArgumentException if maxDepth is negative
     */
    static String extractPartitionPath(String fileUri, Integer maxDepth) {
        if (maxDepth != null && maxDepth < 0) {
            throw new IllegalArgumentException("maxDepth is negative: " + maxDepth);
        }

        // Split the URI into parts
        String[] parts = fileUri.split("/");
        List<String> partitionParts = new ArrayList<>();

        // Collect all partition parts (containing "=")
        for (String part : parts) {
            if (part.contains("=")) {
                partitionParts.add(part);
            }
        }

        // If maxDepth is null or 0, return all partition parts
        int depth =
                maxDepth == null || maxDepth == 0 ? partitionParts.size() : Math.min(maxDepth, partitionParts.size());

        // Join the specified number of partition parts
        return String.join("/", partitionParts.subList(0, depth));
    }

    /**
     * Counts the number of Hive partition components in a file URI.
     *
     * <p>This method counts path segments containing "=" which indicates a key-value partition component in the Hive
     * partitioning scheme. For example, in the path "s3://bucket/data/year=2023/month=01/day=01/file.parquet", there
     * are 3 partition components (year=2023, month=01, day=01).
     *
     * @param fileUri The file URI to analyze
     * @return The number of partition components found in the URI
     */
    public static int partitionCount(String fileUri) {
        return (int) Arrays.stream(fileUri.split("/"))
                .filter(part -> part.contains("="))
                .count();
    }
}
