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

import java.io.IOException;
import java.util.Map;
import org.geotools.api.data.DataAccessFactory;
import org.geotools.api.data.DataStoreFactorySpi;
import org.geotools.data.duckdb.AbstractDuckDBDataStoreFactory;
import org.geotools.data.duckdb.ParamBuilder;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.SQLDialect;

/**
 * DataStoreFactory for GeoParquet files, powered by DuckDB.
 *
 * <p>This factory creates DataStore instances that can read and query GeoParquet format files, both local and remote.
 * GeoParquet is an open format for geospatial data that builds on the Apache Parquet columnar storage format, providing
 * efficient access to large geospatial datasets.
 *
 * <p>The implementation uses DuckDB and its extensions (spatial, parquet, httpfs) to handle the heavy lifting of
 * reading and querying Parquet files. This provides excellent performance and compatibility with various storage
 * backends including local files, HTTP/HTTPS, and S3.
 *
 * <p>Usage example:
 *
 * <pre>{@code
 * Map<String, Object> params = new HashMap<>();
 * params.put("dbtype", "geoparquet");
 * params.put("uri", "file:/path/to/data.parquet");
 *
 * DataStore store = DataStoreFinder.getDataStore(params);
 * }</pre>
 */
class GeoParquetDataStoreFactoryDelegate extends AbstractDuckDBDataStoreFactory implements DataStoreFactorySpi {

    static final String GEOPARQUET = "geoparquet";

    /**
     * Parameter for database type.
     *
     * <p>Must be "geoparquet" for this DataStore.
     */
    public static final DataAccessFactory.Param DBTYPE = new ParamBuilder("dbtype")
            .type(String.class)
            .title("DataStore type identifier")
            .required(true)
            .defaultValue(GEOPARQUET)
            .programLevel()
            .build();
    /**
     * Parameter for GeoParquet URI.
     *
     * <p>Must be a valid URI pointing to a GeoParquet file or directory. Supported schemes:
     *
     * <ul>
     *   <li>file:// - for local files and directories
     *   <li>https:// - for remote files
     *   <li>s3:// - for files in S3 storage (requires credentials)
     * </ul>
     *
     * <p>For S3 URIs, additional parameters can be included in the query string:
     *
     * <pre>
     * s3://bucket/path/to/file.parquet?region=us-west-2&access_key=ACCESS_KEY&secret_key=SECRET_KEY
     * </pre>
     */
    public static final DataAccessFactory.Param URI_PARAM = new ParamBuilder("uri")
            .type(String.class)
            .description("URI to GeoParquet local or remote file")
            .required(true)
            .userLevel()
            .build();

    /**
     * Parameter for controlling Hive partition depth in GeoParquet datasets.
     *
     * <p>This parameter determines how many levels of key=value directories are used for partitioning:
     *
     * <ul>
     *   <li>null (default): Use all partition levels found
     *   <li>0: No partitioning, treat all files as a single dataset
     *   <li>1+: Use this many levels of partitioning
     * </ul>
     *
     * <p>For example, with a dataset structure like: {@code s3://bucket/data/year=2023/month=01/day=01/file.parquet}
     *
     * <ul>
     *   <li>max_hive_depth=1: Only "year=2023" is used for partitioning
     *   <li>max_hive_depth=2: "year=2023/month=01" is used
     *   <li>max_hive_depth=3 or null: "year=2023/month=01/day=01" is used
     * </ul>
     */
    public static final DataAccessFactory.Param MAX_HIVE_DEPTH = new ParamBuilder("max_hive_depth")
            .type(Integer.class)
            .title("Max Hive partition depth")
            .description("Max number Hive partitions to use when resolving feature types")
            .required(false)
            .defaultValue(null)
            .advancedLevel()
            .build();

    /**
     * Parameter for enabling AWS credential chain for S3 access.
     *
     * <p>When enabled, uses AWS SDK credential chain to automatically discover credentials from:
     *
     * <ul>
     *   <li>Environment variables (AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY)
     *   <li>AWS config files (~/.aws/credentials, ~/.aws/config)
     *   <li>IAM instance profiles (EC2)
     *   <li>ECS task roles
     *   <li>AWS SSO/federation
     * </ul>
     *
     * <p>When disabled (default), S3 credentials must be provided in URI query parameters.
     *
     * <p>Example with credential chain enabled:
     *
     * <pre>
     * use_aws_credential_chain=true
     * uri=s3://bucket/path/file.parquet
     * </pre>
     *
     * <p>Example without credential chain (legacy):
     *
     * <pre>
     * uri=s3://bucket/path/file.parquet?s3_access_key_id=xxx&s3_secret_access_key=yyy
     * </pre>
     */
    public static final DataAccessFactory.Param USE_AWS_CREDENTIAL_CHAIN = new ParamBuilder("use_aws_credential_chain")
            .type(Boolean.class)
            .title("Use AWS Credential Chain")
            .description("Use AWS SDK credential chain for S3 authentication instead of URI parameters")
            .required(false)
            .defaultValue(false)
            .userLevel()
            .build();

    /**
     * Parameter for specifying AWS region for S3 access.
     *
     * <p>This parameter allows overriding the AWS region when using credential chain authentication. If not specified,
     * the region will be determined automatically from AWS SDK configuration.
     *
     * <p>Example:
     *
     * <pre>
     * use_aws_credential_chain=true
     * aws_region=eu-west-1
     * uri=s3://bucket/path/file.parquet
     * </pre>
     *
     * <p>This corresponds to the REGION parameter in DuckDB's CREATE SECRET statement:
     *
     * <pre>
     * CREATE OR REPLACE SECRET secret (
     *     TYPE s3,
     *     PROVIDER credential_chain,
     *     REGION 'eu-west-1'
     * );
     * </pre>
     */
    public static final DataAccessFactory.Param AWS_REGION = new ParamBuilder("aws_region")
            .type(String.class)
            .title("AWS Region")
            .description("AWS region for S3 access (e.g., 'us-east-1', 'eu-west-1')")
            .required(false)
            .userLevel()
            .build();

    /**
     * Parameter for specifying AWS profile for S3 access.
     *
     * <p>This parameter allows loading credentials from a specific AWS profile in ~/.aws/credentials and ~/.aws/config
     * files when using credential chain authentication.
     *
     * <p>Example:
     *
     * <pre>
     * use_aws_credential_chain=true
     * aws_profile=my_profile
     * uri=s3://bucket/path/file.parquet
     * </pre>
     *
     * <p>This corresponds to the PROFILE parameter in DuckDB's CREATE SECRET statement:
     *
     * <pre>
     * CREATE OR REPLACE SECRET secret (
     *     TYPE s3,
     *     PROVIDER credential_chain,
     *     CHAIN config,
     *     PROFILE 'my_profile'
     * );
     * </pre>
     */
    public static final DataAccessFactory.Param AWS_PROFILE = new ParamBuilder("aws_profile")
            .type(String.class)
            .title("AWS Profile")
            .description("AWS profile name to load credentials from ~/.aws/credentials")
            .required(false)
            .userLevel()
            .build();

    /** Parameter for specifying the namespace URI for the feature type. */
    public static final Param NAMESPACE = AbstractDuckDBDataStoreFactory.NAMESPACE;

    /** Parameter for controlling the number of features to fetch at once. */
    public static final Param FETCHSIZE = AbstractDuckDBDataStoreFactory.FETCHSIZE;

    /** Parameter for enabling/disabling screen map optimization for rendering. */
    public static final Param SCREENMAP = AbstractDuckDBDataStoreFactory.SCREENMAP;

    /** Parameter for enabling/disabling geometry simplification when rendering. */
    public static final Param SIMPLIFY = AbstractDuckDBDataStoreFactory.SIMPLIFY;

    /**
     * Returns the database ID for this factory.
     *
     * @return The database ID ("geoparquet")
     */
    @Override
    protected String getDatabaseID() {
        return GEOPARQUET;
    }

    /**
     * Returns the human-readable display name for this datastore.
     *
     * @return The display name ("GeoParquet")
     */
    @Override
    public String getDisplayName() {
        return "GeoParquet";
    }

    /**
     * Returns a human-readable description of this datastore.
     *
     * @return The description of the datastore
     */
    @Override
    public String getDescription() {
        return "GeoParquet format data files (*.parquet)";
    }

    /**
     * Adds GeoParquet-specific parameters to the parameter map.
     *
     * @param parameters The parameter map to add to
     */
    @Override
    protected void addDatabaseSpecificParameters(Map<String, Object> parameters) {
        // Add GeoParquet specific parameters
        parameters.put(DBTYPE.key, DBTYPE);
        parameters.put(URI_PARAM.key, URI_PARAM);
        parameters.put(MAX_HIVE_DEPTH.key, MAX_HIVE_DEPTH);
        parameters.put(USE_AWS_CREDENTIAL_CHAIN.key, USE_AWS_CREDENTIAL_CHAIN);
        parameters.put(AWS_REGION.key, AWS_REGION);
        parameters.put(AWS_PROFILE.key, AWS_PROFILE);
    }

    /**
     * Creates a GeoParquetDialect for handling GeoParquet-specific SQL operations.
     *
     * @param dataStore The datastore to create a dialect for
     * @param params The connection parameters
     * @return A new GeoParquetDialect instance
     */
    @Override
    protected GeoParquetDialect createSQLDialect(JDBCDataStore dataStore, Map<String, ?> params) {
        return new GeoParquetDialect(dataStore);
    }

    /**
     * Unused method since we override {@link #createSQLDialect(JDBCDataStore, Map)} instead.
     *
     * @param dataStore The datastore to create a dialect for
     * @return Never returns, always throws UnsupportedOperationException
     * @throws UnsupportedOperationException Always
     */
    @Override
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        throw new UnsupportedOperationException();
    }

    /**
     * Sets up the datastore with GeoParquet-specific configuration.
     *
     * <p>This method:
     *
     * <ol>
     *   <li>Sets up the primary key finder for feature ID handling
     *   <li>Creates a configuration object from the parameters
     *   <li>Registers the GeoParquet views based on the configuration
     * </ol>
     *
     * @param dataStore The datastore to set up
     * @param params The connection parameters
     * @return The configured datastore
     * @throws IOException If there's an error setting up the datastore
     */
    @Override
    protected JDBCDataStore setupDataStore(JDBCDataStore dataStore, Map<String, ?> params) throws IOException {
        GeoParquetDialect dialect = (GeoParquetDialect) dataStore.getSQLDialect();
        dataStore.setPrimaryKeyFinder(dialect.getPrimaryKeyFinder());

        GeoParquetConfig config = GeoParquetConfig.valueOf(params);
        dialect.initialize(config);
        return dataStore;
    }

    /**
     * Creates a JDBC URL for a persistent DuckDB database tied to the GeoParquet dataset configuration.
     *
     * <p>This implementation uses {@link GeoParquetDatabaseUtils} to create a temporary on-disk database that's shared
     * among all connections to the same GeoParquet dataset configuration, ensuring that:
     *
     * <ul>
     *   <li>SQL views created for GeoParquet files persist across connections
     *   <li>Metadata caching is properly shared between connections
     *   <li>When {@link GeoParquetDialect#initializeConnection(Connection)} is called for each connection from the
     *       pool, the views don't need to be re-registered
     *   <li>Temporary database files are properly managed and cleaned up when the JVM exits
     * </ul>
     *
     * @param params The parameter map containing the connection parameters
     * @return A JDBC URL pointing to a persistent DuckDB database for this dataset configuration
     * @throws IOException If there's an error creating the temporary directory
     */
    @Override
    protected String getJDBCUrl(Map<String, ?> params) throws IOException {
        return GeoParquetDatabaseUtils.getJDBCUrl(params, getParametersInfo());
    }
}
