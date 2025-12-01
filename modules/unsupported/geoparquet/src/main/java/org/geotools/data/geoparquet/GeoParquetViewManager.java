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
import static java.util.logging.Level.CONFIG;
import static java.util.logging.Level.INFO;

import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.util.logging.Logging;

/**
 * Manages the creation and lifecycle of DuckDB views for GeoParquet data sources.
 *
 * <p>This class is responsible for:
 *
 * <ul>
 *   <li>Creating DuckDB SQL views for GeoParquet data partitions
 *   <li>Managing the mapping between partition URIs and view names
 *   <li>Handling Hive-partitioned datasets (datasets organized in directories following key=value patterns)
 *   <li>Maintaining the lifecycle of views when the target dataset changes
 *   <li>Implementing lazy view registration for better performance
 * </ul>
 *
 * <p>The view manager implements a lazy initialization pattern where:
 *
 * <ol>
 *   <li>During initialization, it only discovers available partitions and creates mappings
 *   <li>The actual SQL views are only created when a feature type is first accessed
 *   <li>Thread-safe locking ensures views are created exactly once even with concurrent access
 * </ol>
 *
 * <p>This lazy approach significantly improves performance for large datasets or remote data, as it avoids unnecessary
 * view registrations during DataStore initialization.
 *
 * <p>The view manager works with {@link HivePartitionResolver} to discover partition structure. Each partition becomes
 * a separate view in DuckDB, allowing the GeoParquetDataStore to expose each partition as a distinct feature type.
 */
class GeoParquetViewManager {

    private static final Logger LOGGER = Logging.getLogger(GeoParquetViewManager.class);

    private final JDBCDataStore dataStore;

    private GeoParquetConfig config;

    private Map<String, Partition> partitionsByViewName = Map.of();

    /**
     * Represents a GeoParquet data partition with thread-safe view registration tracking.
     *
     * <p>This class encapsulates:
     *
     * <ul>
     *   <li>The URI pattern for the partition (may include Hive partition information)
     *   <li>The view name derived from the partition URI
     *   <li>The list of actual GeoParquet files included in this partition
     *   <li>A thread-safe registration status using atomic operations
     *   <li>A reentrant lock to ensure thread safety during view creation
     *   <li>Logic for safely creating SQL views when needed (lazy initialization)
     * </ul>
     *
     * <p>This design ensures that even with concurrent access, each partition's view is created exactly once, and
     * waiting threads will see the created view. The view registration is handled internally by the partition object,
     * which encapsulates all the thread-safety and view creation logic.
     */
    private class Partition {
        private final Lock lock = new ReentrantLock();
        private final AtomicBoolean registered = new AtomicBoolean();

        private final String uri;
        private final String viewName;
        private final List<String> files;
        private SimpleFeatureType viewType;

        /** Creates a new partition entry. */
        public Partition(String uri, String viewName, List<String> files) {
            this.uri = requireNonNull(uri);
            this.viewName = requireNonNull(viewName);
            this.files = requireNonNull(files);
        }

        /**
         * Returns the SQL view name for this partition.
         *
         * <p>This name is used as both the database view name and the feature type name in the GeoParquet DataStore.
         */
        public String getViewName() {
            return viewName;
        }

        /**
         * Returns the URI pattern for this partition.
         *
         * @return The URI pattern string
         */
        public String getURI() {
            return uri;
        }

        /**
         * Returns the list of files in this partition.
         *
         * @return List of file paths or URI patterns
         */
        public List<String> getFiles() {
            return files;
        }

        /**
         * Gets the enhanced feature type for this partition with caching.
         *
         * <p>This method implements a thread-safe caching mechanism for feature types:
         *
         * <ol>
         *   <li>First checks if a cached version already exists
         *   <li>If not, acquires a lock to prevent concurrent rebuilding
         *   <li>Double-checks if another thread created the type while waiting for the lock
         *   <li>If still needed, applies the builder function to create the enhanced type
         *   <li>Caches the result for future use by all threads
         * </ol>
         *
         * <p>This implementation follows the double-checked locking pattern to provide both thread safety and
         * performance. The lock is only acquired when necessary, and the expensive builder function is called exactly
         * once per partition.
         *
         * @param original The original feature type from JDBC metadata
         * @param builder Function to enhance the feature type (typically narrowing geometry types)
         * @return The enhanced feature type, either from cache or freshly built
         */
        public SimpleFeatureType getFeatureType(SimpleFeatureType original, UnaryOperator<SimpleFeatureType> builder) {
            SimpleFeatureType finalType = this.viewType;
            if (finalType == null) {
                lock.lock();
                try {
                    if (this.viewType == null) {
                        finalType = builder.apply(original);
                        this.viewType = finalType;
                    } else {
                        finalType = this.viewType;
                    }
                } finally {
                    lock.unlock();
                }
            }
            return finalType;
        }

        /**
         * Ensures the SQL view for this partition is registered in the database.
         *
         * <p>This method implements the lazy initialization pattern and thread-safety:
         *
         * <ol>
         *   <li>It acquires a lock to ensure thread-safety
         *   <li>It checks if the view is already registered using atomic compare-and-set
         *   <li>If not yet registered, it creates the view
         *   <li>Finally, it releases the lock
         * </ol>
         *
         * <p>This ensures that even with concurrent calls, the view is created exactly once, and all threads will see a
         * consistent view state without requiring synchronization at the manager level.
         *
         * @throws IOException If there's an error creating the view
         */
        public void ensureRegistered() throws IOException {
            lock.lock();
            try {
                if (registered.compareAndSet(false, true)) {
                    String partitionUrl = getURI();
                    createView(viewName, partitionUrl);
                }
            } finally {
                lock.unlock();
            }
        }

        /**
         * Creates a SQL view in the DuckDB database.
         *
         * <p>This method is called after a lock has been acquired and registration status confirmed, ensuring it's only
         * called once per view. It:
         *
         * <ol>
         *   <li>Logs the view creation at INFO level
         *   <li>Builds the SQL statement for creating the view
         *   <li>Executes the SQL using a new connection
         *   <li>Logs completion at INFO level
         * </ol>
         *
         * @param viewName The name of the view to create
         * @param partitionUrl The URI pattern for the partition
         * @throws IOException If there is an error executing the SQL statement
         */
        private void createView(String viewName, final String partitionUrl) throws IOException {
            LOGGER.log(INFO, () -> "Creating view %s for URI %s".formatted(viewName, partitionUrl));

            final String viewSql = createViewSql(viewName, partitionUrl);
            try (Connection c = GeoParquetViewManager.this.getConnection();
                    Statement st = c.createStatement()) {
                st.execute(viewSql);
            } catch (SQLException e) {
                throw new IOException(e);
            }

            LOGGER.log(INFO, () -> "Created view %s for URI %s".formatted(viewName, partitionUrl));
        }

        /**
         * Creates the SQL statement for a view.
         *
         * <p>The created view uses DuckDB's {@code union_by_name = true} option which allows files with different
         * schemas to be combined, as long as columns with the same name have compatible types. See <a href=
         * "https://duckdb.org/docs/stable/data/multiple_files/combining_schemas.html">Combining Schemas</a> in the
         * DuckDB documentation.
         *
         * @param viewName The name for the view
         * @param partitionUrl The URI pattern for the GeoParquet files to include
         * @return The SQL statement to create the view
         */
        private String createViewSql(String viewName, String partitionUrl) {
            return "CREATE OR REPLACE VIEW \"%s\" AS SELECT * FROM read_parquet('%s', union_by_name = true)"
                    .formatted(viewName, partitionUrl);
        }
    }

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
     *   <li>Creates Partition objects for each partition with their view names
     *   <li>Drops any existing views from previous configurations
     *   <li>Updates internal mappings between view names and partitions
     * </ol>
     *
     * <p>Following the lazy initialization pattern, this method only discovers available partitions and prepares the
     * mapping structures. It does not actually create SQL views in the database - those are created on-demand when
     * {@link #createViewIfNotExists(String)} is called for a specific view name.
     *
     * @param config The configuration containing the target URI and partitioning parameters
     * @throws IOException If there's an error accessing or processing the data source
     */
    public void initialize(GeoParquetConfig config) throws IOException {

        final URI targetUri = config.getTargetUri();
        final Integer maxHiveDepth = config.getMaxHiveDepth();

        LOGGER.config("Resolving files for geoparquet uri " + targetUri);

        // Create S3 secret if credential chain is enabled
        if (config.isUseAwsCredentialChain()) {
            createAwsCredentialChainSecret(config.getAwsRegion(), config.getAwsProfile());
            LOGGER.log(CONFIG, "Created AWS credential chain secret for S3 access to " + targetUri);
        }

        Map<String, Partition> partitions = loadPartitions(targetUri, maxHiveDepth);

        LOGGER.log(CONFIG, () -> "Found %,d partitions with %,d total files"
                .formatted(
                        partitions.keySet().size(),
                        partitions.values().stream()
                                .map(Partition::getFiles)
                                .flatMap(List::stream)
                                .count()));

        dropViews();
        this.config = config;
        this.partitionsByViewName = partitions;
    }

    /**
     * Creates a DuckDB secret for AWS credential chain authentication.
     *
     * <p>This enables automatic credential discovery using AWS SDK mechanisms: environment variables, config files, IAM
     * roles, etc.
     *
     * <p>If a region is specified, it will override the automatically fetched region. If a profile is specified,
     * credentials will be loaded from that profile in ~/.aws/credentials and ~/.aws/config files.
     *
     * @param region AWS region to use (may be null for automatic detection)
     * @param profile AWS profile name to load credentials from (may be null for default)
     * @throws IOException If secret creation fails
     */
    private void createAwsCredentialChainSecret(String region, String profile) throws IOException {
        String createSecretSql = buildCreateSecretSql(region, profile);

        try (Connection c = getConnection();
                Statement st = c.createStatement()) {
            st.execute(createSecretSql);
            LOGGER.log(CONFIG, "Created AWS credential chain secret for S3 access");
        } catch (SQLException e) {
            throw new IOException("Failed to create AWS credential chain secret: " + e.getMessage(), e);
        }
    }

    /**
     * Builds a DuckDB CREATE SECRET SQL statement for AWS credential chain authentication.
     *
     * <p>This method constructs the SQL with optional region and profile parameters. The generated SQL follows the
     * pattern:
     *
     * <pre>
     * CREATE OR REPLACE SECRET geoparquet_s3_secret (
     *     TYPE s3,
     *     PROVIDER credential_chain[, CHAIN 'config'][, PROFILE 'profile_name'][, REGION 'region_name']
     * )
     * </pre>
     *
     * <p>The CHAIN 'config' clause is automatically included when a profile is specified, as it is required by DuckDB
     * to load credentials from AWS configuration files.
     *
     * @param region AWS region to use (may be null or empty for automatic detection)
     * @param profile AWS profile name to load credentials from (may be null or empty for default)
     * @return A complete CREATE SECRET SQL statement
     */
    static String buildCreateSecretSql(String region, String profile) {
        // Build optional clauses for the CREATE SECRET statement
        String chainClause = "";
        String profileClause = "";
        String regionClause = "";

        if (StringUtils.isNotBlank(profile)) {
            // CHAIN 'config' is required when using PROFILE parameter to specify a non-default profile.
            // Without PROFILE, the default credential chain uses the default profile from config files.
            // With PROFILE, we explicitly specify which profile to use from ~/.aws/credentials and ~/.aws/config.
            chainClause = ", CHAIN 'config'";
            profileClause = ", PROFILE '%s'".formatted(profile.replace("'", "''"));
        }

        if (StringUtils.isNotBlank(region)) {
            // REGION allows overriding the region from the credential chain
            regionClause = ", REGION '%s'".formatted(region.replace("'", "''"));
        }

        return """
                CREATE OR REPLACE SECRET geoparquet_s3_secret (
                    TYPE s3,
                    PROVIDER credential_chain%s%s%s
                )
                """
                .formatted(chainClause, profileClause, regionClause);
    }

    /**
     * Loads available partitions from the target URI.
     *
     * <p>This method is part of the initialization process and:
     *
     * <ol>
     *   <li>Finds all partitions based on the URI and max hive depth settings
     *   <li>Creates Partition objects for each partition with generated view names
     *   <li>Builds a map of view names to Partition objects
     * </ol>
     *
     * <p>This method doesn't actually create the SQL views - following the lazy initialization pattern, views are only
     * created when requested via {@link #createViewIfNotExists(String)}. Each Partition object handles its own view
     * registration in a thread-safe manner when needed.
     *
     * @param targetUri The target URI to find partitions for
     * @param maxHiveDepth The maximum Hive partition depth to use
     * @return A map of view names to Partition objects
     * @throws IOException If there is an error finding partitions
     */
    private Map<String, Partition> loadPartitions(final URI targetUri, final Integer maxHiveDepth) throws IOException {

        Map<String, List<String>> partitionFilesByUrl = findPartitions(targetUri, maxHiveDepth);

        return partitionFilesByUrl.entrySet().stream()
                .map(e -> newPartition(e.getKey(), e.getValue()))
                .collect(Collectors.toMap(Partition::getViewName, Function.identity()));
    }

    /**
     * Creates a new Partition object with a view name derived from the URI.
     *
     * <p>This helper method:
     *
     * <ol>
     *   <li>Generates a view name based on the URI using the HivePartitionResolver
     *   <li>Creates a new Partition object with the URI, view name, and files
     * </ol>
     *
     * @param uri The URI pattern for the partition
     * @param files The list of GeoParquet files in the partition
     * @return A new Partition object
     */
    private Partition newPartition(String uri, List<String> files) {
        String viewName = HivePartitionResolver.buildPartitionName(uri);
        return new Partition(uri, viewName, files);
    }

    /**
     * Drops all registered views from the database.
     *
     * <p>This synchronized method ensures that view cleanup happens atomically:
     *
     * <ol>
     *   <li>Gets all view names from the current mapping
     *   <li>Creates DROP VIEW statements for each view
     *   <li>Executes them as a batch operation for efficiency
     * </ol>
     *
     * <p>This method is called during re-initialization when the configuration changes, ensuring any stale views are
     * removed.
     *
     * @throws IOException If there is an error dropping the views
     */
    private synchronized void dropViews() throws IOException {
        try (Connection c = getConnection();
                Statement st = c.createStatement()) {
            for (Partition p : partitionsByViewName.values()) {
                String view = p.getViewName();
                String sql = "DROP VIEW IF EXISTS \"%s\"".formatted(view);
                st.addBatch(sql);
                LOGGER.log(CONFIG, () -> "Dropping view %s for URI %s".formatted(view, p.getURI()));
            }
            st.executeBatch();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /**
     * Creates a DuckDB view for a partition if it doesn't already exist.
     *
     * <p>This method implements the lazy initialization pattern, where views are only created when they are needed. It
     * follows these steps:
     *
     * <ol>
     *   <li>Looks up the Partition object for the given view name
     *   <li>Delegates the view registration to the Partition object
     * </ol>
     *
     * <p>The actual view registration is handled by the {@link Partition#ensureRegistered()} method, which handles all
     * the thread-safety and atomic registration concerns. This design follows good object-oriented principles by
     * encapsulating the registration logic within the Partition object itself.
     *
     * <p>This implementation is thread-safe, ensuring that even with concurrent access:
     *
     * <ul>
     *   <li>Each view is created exactly once
     *   <li>All threads will see a consistent view state
     *   <li>No deadlocks occur during view creation
     * </ul>
     *
     * @param viewName The name of the view to create
     * @throws IOException If there is an error creating the view
     * @throws NullPointerException If the viewName is not valid or not registered
     */
    public void createViewIfNotExists(String viewName) throws IOException {
        final Partition partition = requireNonNull(partitionsByViewName.get(viewName));
        partition.ensureRegistered();
    }

    /**
     * Gets the enhanced feature type for a view, with caching for performance.
     *
     * <p>This method retrieves the feature type for a given view name, applying the provided builder function to
     * enhance it (typically by narrowing geometry types based on GeoParquet metadata). Results are cached at the
     * partition level to avoid repeatedly rebuilding the same feature type.
     *
     * <p>Thread-safety is maintained through a lock in the Partition class, ensuring that:
     *
     * <ol>
     *   <li>The builder function is applied only once per feature type
     *   <li>All threads see the same, consistently-built feature type once created
     *   <li>No race conditions occur even with concurrent access to the same feature type
     * </ol>
     *
     * @param original The original feature type as inferred by JDBCDataStore from the database metadata
     * @param builder A function to enhance the original feature type (typically by narrowing geometry types)
     * @return The enhanced feature type, either freshly built or retrieved from cache
     */
    public SimpleFeatureType getViewFeatureType(SimpleFeatureType original, UnaryOperator<SimpleFeatureType> builder) {
        final Partition partition = requireNonNull(partitionsByViewName.get(original.getTypeName()));
        return partition.getFeatureType(original, builder);
    }

    /**
     * Returns all view names created by this manager.
     *
     * <p>This method returns the names of all available views, sorted alphabetically. These view names can be used as
     * feature type names by the GeoParquet DataStore, and they correspond to partitions in the dataset.
     *
     * <p>Note that this method returns the names of all available views, whether or not they have been actually created
     * in the database yet. The actual SQL view creation happens lazily via {@link #createViewIfNotExists(String)}.
     *
     * @return A sorted list of all view names
     */
    public List<String> getViewNames() {
        return partitionsByViewName.keySet().stream().sorted().collect(Collectors.toList());
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
                partitionsByViewName.get(requireNonNull(viewName, "viewName")).getURI(),
                () -> "No target URL exists for view %s".formatted(viewName));
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
     * Gets a database connection from the data store.
     *
     * @return A JDBC Connection
     * @throws IOException If there's an error getting the connection
     */
    public Connection getConnection() throws IOException {
        return dataStore.getConnection(Transaction.AUTO_COMMIT);
    }
}
