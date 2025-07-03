/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;
import org.junit.rules.ExternalResource;
import org.junit.rules.TemporaryFolder;

/**
 * JUnit rule that provides access to GeoParquet test files.
 *
 * <p>This rule creates a temporary directory and provides access to GeoParquet test files with various geometry types
 * for testing.
 *
 * <p>Usage:
 *
 * <pre><code>
 * public class MyTest {
 *     @Rule
 *     public GeoParquetTestSupport testData = new GeoParquetTestSupport();
 *
 *     @Test
 *     public void testSomething() {
 *         File worldgridDir = testData.getWorldgridDir();
 *         File pointsFile = testData.getWorldgridFile("points.parquet");
 *         // Test with the GeoParquet files
 *     }
 * }
 * </code>
 * </pre>
 *
 * <p>This support class will create the following directories and files in the {@link #getTemporaryFolder() temp
 * folder}:
 *
 * <pre>{@code
 * .
 * ├── worldgrid
 * │   ├── lines.parquet
 * │   ├── points.parquet
 * │   └── polygons.parquet
 * └── worldgrid_partitioned
 *     ├── theme=lines
 *     │   ├── type=line
 *     │   │   └── lines-0.parquet
 *     │   └── type=multiline
 *     │       └── lines-0.parquet
 *     ├── theme=points
 *     │   ├── type=multipoint
 *     │   │   └── points-0.parquet
 *     │   └── type=point
 *     │       └── points-0.parquet
 *     └── theme=polygons
 *         ├── type=multipolygon
 *         │   └── polygons-0.parquet
 *         └── type=polygon
 *             └── polygons-0.parquet
 * }</pre>
 *
 * {@literal worldgrid} contains single files, while {@literal worldgrid_partitioned} uses <a
 * href="https://duckdb.org/docs/stable/data/partitioning/hive_partitioning.html">Hive Partioning</a> on the
 * {@literal theme} and {@literal type} columns.
 *
 * <h2>Test Files:</h2>
 *
 * <h3>points.parquet</h3>
 *
 * <ul>
 *   <li>65,341 individual points covering the world grid (-180 to 180, -90 to 90)
 *   <li>4 multipoints (one per quadrant)
 *   <li>Attributes include: id, geometry, bbox, names (multilingual), confidence, phones, addresses
 * </ul>
 *
 * <pre>{@code
 * ┌─────────────┬───────────────────────────────────────────────────────────────────────────────────────────────┐
 * │ column_name │                                          column_type                                          │
 * ├─────────────┼───────────────────────────────────────────────────────────────────────────────────────────────┤
 * │ id          │ VARCHAR                                                                                       │
 * │ geometry    │ GEOMETRY                                                                                      │
 * │ bbox        │ STRUCT(xmin FLOAT, xmax FLOAT, ymin FLOAT, ymax FLOAT)                                        │
 * │ names       │ STRUCT("primary" VARCHAR, common MAP(VARCHAR, VARCHAR), rules STRUCT(variant VARCHAR))        │
 * │ confidence  │ DOUBLE                                                                                        │
 * │ phones      │ VARCHAR[]                                                                                     │
 * │ addresses   │ STRUCT(freeform VARCHAR, locality VARCHAR, postcode VARCHAR, region VARCHAR, country VARCHAR) │
 * │ theme       │ VARCHAR                                                                                       │
 * │ type        │ VARCHAR                                                                                       │
 * └─────────────┴───────────────────────────────────────────────────────────────────────────────────────────────┘
 * ┌─────────┬────────────┬──────────────┐
 * │  theme  │    type    │    count     │
 * ├─────────┼────────────┼──────────────┤
 * │ points  │ multipoint │            4 │
 * │ points  │ point      │        65341 │
 * └─────────┴────────────┴──────────────┘
 * }</pre>
 *
 * <h3>lines.parquet</h3>
 *
 * <ul>
 *   <li>1,084 lines (horizontal and vertical lines along lat/lon grid)
 *   <li>4 multilines (one per quadrant)
 *   <li>Attributes include: id, geometry, bbox, names (multilingual)
 * </ul>
 *
 * <pre>{@code
 * ┌─────────────┬────────────────────────────────────────────────────────────────────────────────────────┐
 * │ column_name │                                      column_type                                       │
 * ├─────────────┼────────────────────────────────────────────────────────────────────────────────────────┤
 * │ id          │ VARCHAR                                                                                │
 * │ geometry    │ GEOMETRY                                                                               │
 * │ bbox        │ STRUCT(xmin FLOAT, xmax FLOAT, ymin FLOAT, ymax FLOAT)                                 │
 * │ names       │ STRUCT("primary" VARCHAR, common MAP(VARCHAR, VARCHAR), rules STRUCT(variant VARCHAR)) │
 * │ theme       │ VARCHAR                                                                                │
 * │ type        │ VARCHAR                                                                                │
 * └─────────────┴────────────────────────────────────────────────────────────────────────────────────────┘
 * ┌─────────┬───────────┬──────────────┐
 * │  theme  │    type   │    count     │
 * ├─────────┼───────────┼──────────────┤
 * │ lines   │ line      │         1084 │
 * │ lines   │ multiline │            4 │
 * └─────────┴───────────┴──────────────┘
 *
 * }</pre>
 *
 * <h3>polygons.parquet</h3>
 *
 * <pre>{@code
 * ┌─────────────┬────────────────────────────────────────────────────────────────────────────────────────┐
 * │ column_name │                                      column_type                                       │
 * ├─────────────┼────────────────────────────────────────────────────────────────────────────────────────┤
 * │ id          │ VARCHAR                                                                                │
 * │ geometry    │ GEOMETRY                                                                               │
 * │ bbox        │ STRUCT(xmin FLOAT, xmax FLOAT, ymin FLOAT, ymax FLOAT)                                 │
 * │ names       │ STRUCT("primary" VARCHAR, common MAP(VARCHAR, VARCHAR), rules STRUCT(variant VARCHAR)) │
 * │ theme       │ VARCHAR                                                                                │
 * │ type        │ VARCHAR                                                                                │
 * └─────────────┴────────────────────────────────────────────────────────────────────────────────────────┘
 * ┌──────────┬──────────────┬────────────┐
 * │  theme   │     type     │   count    │
 * ├──────────┼──────────────┼────────────┤
 * │ polygons │ polygon      │       2485 │
 * │ polygons │ multipolygon │          4 │
 * └──────────┴──────────────┴────────────┘
 * }</pre>
 */
public class GeoParquetTestSupport extends ExternalResource {
    private static final Logger LOGGER = Logging.getLogger(GeoParquetTestSupport.class);

    /** The temporary folder rule where test files will be created */
    private final TemporaryFolder tempFolder = new TemporaryFolder();

    /** Flag indicating if the files have been generated */
    private boolean filesGenerated = false;

    /** Path to the worldgrid partitioned directory */
    private File worldgridPartitioned;

    /** Path to the worldgrid directory with non-partitioned files */
    private File worldgridDir;

    /** Creates a new GeoParquetTestSupport. */
    public GeoParquetTestSupport() {
        // Default constructor
    }

    @Override
    protected void before() throws Throwable {
        // Create temporary directory
        tempFolder.create();

        // Create worldgrid directories
        worldgridDir = tempFolder.newFolder("worldgrid");
        worldgridPartitioned = tempFolder.newFolder("worldgrid_partitioned");

        // Generate test files
        generateTestFiles();
    }

    @Override
    protected void after() {
        tempFolder.delete();
    }

    /**
     * Gets the root temporary directory.
     *
     * @return the root temporary directory
     */
    public File getTemporaryFolder() {
        return tempFolder.getRoot();
    }

    /**
     * Gets the worldgrid directory with non-partitioned files.
     *
     * @return the worldgrid directory
     */
    public File getWorldgridDir() {
        return worldgridDir;
    }

    /**
     * Gets the worldgrid_partitioned directory with Hive partitioning.
     *
     * @return the worldgrid_partitioned directory
     */
    public File getWorldgridPartitionedDir() {
        return worldgridPartitioned;
    }

    /**
     * Gets a specific file from the worldgrid directory.
     *
     * @param filename the name of the file
     * @return the file
     */
    public File getWorldgridFile(String filename) {
        return new File(worldgridDir, filename);
    }

    /**
     * Gets a specific type partition directory from the worldgrid_partitioned directory.
     *
     * @param theme the theme (points, lines, polygons)
     * @param type the type (point, multipoint, line, multiline, polygon, multipolygon)
     * @return the partition directory
     */
    public File getWorldgridPartition(String theme, String type) {
        return new File(worldgridPartitioned, "theme=" + theme + "/type=" + type);
    }

    /**
     * Gets a file from a specific partition.
     *
     * @param theme the theme (points, lines, polygons)
     * @param type the type (point, multipoint, line, multiline, polygon, multipolygon)
     * @return the first file in the partition
     */
    public File getPartitionFile(String theme, String type) {
        File partitionDir = getWorldgridPartition(theme, type);
        File[] files = partitionDir.listFiles(f -> f.getName().endsWith(".parquet"));
        if (files != null && files.length > 0) {
            return files[0];
        }
        return null;
    }

    /**
     * Generates the test files for GeoParquet testing.
     *
     * @throws IOException
     * @throws SQLException
     */
    private void generateTestFiles() throws IOException, SQLException {
        if (filesGenerated) {
            return;
        }

        LOGGER.info("Generating GeoParquet test files using DuckDB JDBC...");

        // First make sure output directories exist
        Files.createDirectories(worldgridDir.toPath());
        Files.createDirectories(worldgridPartitioned.toPath());

        // Read the SQL script that will generate the test data
        String sqlScript = loadScript();

        // Set the output paths directly in the SQL to our test directories
        String modifiedScript = sqlScript
                .replace("test-data/worldgrid", worldgridDir.getAbsolutePath().replace("\\", "/"))
                .replace(
                        "test-data/worldgrid_partitioned",
                        worldgridPartitioned.getAbsolutePath().replace("\\", "/"))
                // remove `.shell mkdir` calls
                .replace(".shell", "-- .shell");

        // Execute the SQL script to generate real GeoParquet files
        executeScriptWithDuckDB(modifiedScript);

        // Verify the files were created
        verifyParquetFilesExist();

        filesGenerated = true;
        LOGGER.info("Successfully generated GeoParquet test files in " + worldgridDir.getAbsolutePath());
    }

    private String loadScript() throws IOException {
        String sqlScript;
        try (InputStream scriptStream = getClass().getResourceAsStream("setup_test_data.sql")) {
            if (scriptStream == null) {
                throw new IOException("Failed to load setup_test_data.sql script");
            }
            sqlScript = new String(scriptStream.readAllBytes(), StandardCharsets.UTF_8);
        }
        return sqlScript;
    }

    /**
     * Executes a SQL script using DuckDB JDBC driver to generate real GeoParquet files.
     *
     * @param script the SQL script to execute
     * @throws SQLException if execution fails
     */
    private void executeScriptWithDuckDB(String script) throws SQLException {
        LOGGER.fine("Starting DuckDB script execution to generate GeoParquet test files");
        try (Connection conn = DriverManager.getConnection("jdbc:duckdb:");
                Statement stmt = conn.createStatement()) {
            stmt.execute(script);
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage() + " script: \n" + script);
            throw e;
        }
    }

    /**
     * Verifies that all required Parquet files exist.
     *
     * @throws IOException if any required file is missing
     */
    private void verifyParquetFilesExist() throws IOException {
        LOGGER.fine("Verifying GeoParquet files were successfully created...");

        // Check non-partitioned files
        File pointsFile = new File(worldgridDir, "points.parquet");
        File linesFile = new File(worldgridDir, "lines.parquet");
        File polygonsFile = new File(worldgridDir, "polygons.parquet");

        checkExists(pointsFile);
        checkExists(linesFile);
        checkExists(polygonsFile);

        checkExists(getWorldgridPartition("points", "point", "points-0.parquet"));
        checkExists(getWorldgridPartition("points", "multipoint", "points-0.parquet"));

        checkExists(getWorldgridPartition("lines", "line", "lines-0.parquet"));
        checkExists(getWorldgridPartition("lines", "multiline", "lines-0.parquet"));

        checkExists(getWorldgridPartition("polygons", "polygon", "polygons-0.parquet"));
        checkExists(getWorldgridPartition("polygons", "multipolygon", "polygons-0.parquet"));
    }

    private File getWorldgridPartition(String theme, String type, String file) {
        return new File(getWorldgridPartition(theme, type), file);
    }

    private void checkExists(File geoparquetFile) throws IOException {
        if (!geoparquetFile.exists()) {
            throw new IOException("Required file missing: " + geoparquetFile);
        } else {
            LOGGER.fine("Found main file: " + geoparquetFile.getName() + " (" + geoparquetFile.length() + " bytes)");
        }
    }
}
