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

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.geotools.api.data.Transaction;
import org.geotools.jdbc.JDBCDataStore;

/**
 * Manages the creation and lifecycle of DuckDB views for GeoParquet data sources.
 *
 * <p>This class is responsible for:
 *
 * <ul>
 *   <li>Creating DuckDB SQL views for GeoParquet data partitions
 *   <li>Managing the mapping between partition URIs and view names
 *   <li>Handling Hive-partitioned datasets (datasets organized in directories following key=value patterns)
 *   <li>Dropping and recreating views when the target dataset changes
 * </ul>
 *
 * <p>The view manager works with {@link HivePartitionResolver} to discover partition structure and creates
 * corresponding DuckDB views. Each partition becomes a separate view in DuckDB, allowing the GeoParquetDataStore to
 * expose each partition as a distinct feature type.
 */
class GeoParquetViewManager {

    private final JDBCDataStore dataStore;

    private GeoParquetConfig config;

    private Map<String, List<String>> partitionUrlToFiles = Map.of();
    private Map<String, String> viewNamesToPartitionUrls = Map.of();

    /**
     * Creates a new GeoParquetViewManager.
     *
     * @param dataStore The JDBC datastore that this manager will work with
     */
    public GeoParquetViewManager(JDBCDataStore dataStore) {
        this.dataStore = dataStore;
    }

    /**
     * Gets the current configuration.
     *
     * @return The current GeoParquetConfig
     */
    public GeoParquetConfig getConfig() {
        return config;
    }

    /**
     * Initializes the view manager with the provided configuration.
     *
     * <p>This method:
     *
     * <ol>
     *   <li>Discovers partitions in the target URI
     *   <li>Creates SQL view names for each partition
     *   <li>Drops obsolete views and creates new ones
     *   <li>Updates internal mappings between views and partitions
     * </ol>
     *
     * @param config The configuration containing the target URI and partitioning parameters
     * @throws IOException If there's an error accessing or processing the data source
     */
    public void initialize(GeoParquetConfig config) throws IOException {

        final URI targetUri = config.getTargetUri();
        final Integer maxHiveDepth = config.getMaxHiveDepth();

        Map<String, List<String>> newPartitionedFiles = findPartitions(targetUri, maxHiveDepth);
        Map<String, String> newViewNamesToPartitions = new HashMap<>();

        Map<String, String> createViewStatements = new HashMap<>();
        for (Map.Entry<String, List<String>> partition : newPartitionedFiles.entrySet()) {
            String partitionUrl = partition.getKey();
            String partitionViewName = HivePartitionResolver.buildPartitionName(partitionUrl);
            newViewNamesToPartitions.put(partitionViewName, partitionUrl);

            String viewSql = createViewSql(partitionViewName, partitionUrl);
            createViewStatements.put(partitionViewName, viewSql);
        }

        dropOldViewsAndCreateNewOnes(createViewStatements);

        this.config = config;
        this.partitionUrlToFiles = newPartitionedFiles;
        this.viewNamesToPartitionUrls = newViewNamesToPartitions;
    }

    /**
     * Returns all view names created by this manager.
     *
     * @return A set of all view names
     */
    public Set<String> getViewNames() {
        return Set.copyOf(viewNamesToPartitionUrls.keySet());
    }

    /**
     * Returns all partition URIs managed by this view manager.
     *
     * @return A set of all partition URIs
     */
    public Set<String> getPartitionUris() {
        return Set.copyOf(partitionUrlToFiles.keySet());
    }

    /**
     * Gets the URI corresponding to a view name.
     *
     * @param viewName The name of the view
     * @return The target URI for the specified view
     * @throws NullPointerException if the view name is null or not found
     */
    public String getVieUri(String viewName) {
        return requireNonNull(
                viewNamesToPartitionUrls.get(requireNonNull(viewName, "viewName")),
                () -> String.format("No target URL exists for view %s", viewName));
    }

    /**
     * Discovers partitions in the target URI.
     *
     * @param targetUri The URI pointing to the GeoParquet file or directory
     * @param maxHiveDepth Maximum depth of Hive partitioning to consider
     * @return A map of partition URIs to their contained files
     * @throws IOException If there's an error accessing the data source
     */
    private Map<String, List<String>> findPartitions(final URI targetUri, final Integer maxHiveDepth)
            throws IOException {
        Map<String, List<String>> partitionedFiles;
        try (Connection c = getConnection()) {
            partitionedFiles = HivePartitionResolver.getHivePartitionedFiles(c, targetUri, maxHiveDepth);
        } catch (SQLException e) {
            throw new IOException(e);
        }
        return partitionedFiles;
    }

    /**
     * Drops obsolete views and creates new ones in a single transaction.
     *
     * <p>This method:
     *
     * <ol>
     *   <li>Identifies views that no longer correspond to partitions
     *   <li>Drops those views
     *   <li>Creates or replaces views for all current partitions
     * </ol>
     *
     * @param createViewStatements Map of view names to their SQL CREATE statements
     * @throws IOException If there's an error executing the SQL statements
     */
    private void dropOldViewsAndCreateNewOnes(Map<String, String> createViewStatements) throws IOException {

        Set<String> newViewNames = createViewStatements.keySet();
        Set<String> viewsToDrop = this.viewNamesToPartitionUrls.keySet().stream()
                .filter(view -> !newViewNames.contains(view))
                .collect(Collectors.toSet());

        try (Connection c = getConnection();
                Statement st = c.createStatement()) {
            c.setAutoCommit(false);
            try {
                for (String view : viewsToDrop) {
                    String sql = String.format("DROP VIEW IF EXISTS \"%s\"", view);
                    st.addBatch(sql);
                }
                st.executeBatch();
                st.clearBatch();

                for (String viewSql : createViewStatements.values()) {
                    st.addBatch(viewSql);
                }

                st.executeBatch();
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /**
     * Gets a database connection from the data store.
     *
     * @return A JDBC Connection
     * @throws IOException If there's an error getting the connection
     */
    public Connection getConnection() throws IOException {
        return dataStore.getConnection(Transaction.AUTO_COMMIT);
    }

    /**
     * Creates the SQL statement for a view.
     *
     * <p>The created view uses DuckDB's {@code union_by_name = true} option which allows files with different schemas
     * to be combined, as long as columns with the same name have compatible types. See <a
     * href="https://duckdb.org/docs/stable/data/multiple_files/combining_schemas.html">Combining Schemas</a> in the
     * DuckDB documentation.
     *
     * @param viewName The name for the view
     * @param partitionUrl The URI pattern for the GeoParquet files to include
     * @return The SQL statement to create the view
     */
    private String createViewSql(String viewName, String partitionUrl) {
        return String.format(
                "CREATE OR REPLACE VIEW \"%s\" AS SELECT * FROM read_parquet('%s', union_by_name = true)",
                viewName, partitionUrl);
    }
}
