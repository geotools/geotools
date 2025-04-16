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
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Objects;
import org.geotools.api.data.DataAccessFactory;
import org.geotools.api.data.DataAccessFactory.Param;
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
 * <pre>
 * Map&lt;String, Object&gt; params = new HashMap&lt;&gt;();
 * params.put("dbtype", "geoparquet");
 * params.put("uri", "file:/path/to/data.parquet");
 *
 * DataStore store = DataStoreFinder.getDataStore(params);
 * </pre>
 */
public class GeoParquetDataStoreFactory extends AbstractDuckDBDataStoreFactory implements DataStoreFactorySpi {

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

    public static final Param NAMESPACE = AbstractDuckDBDataStoreFactory.NAMESPACE;

    public static final Param FETCHSIZE = AbstractDuckDBDataStoreFactory.FETCHSIZE;

    public static final Param SCREENMAP = AbstractDuckDBDataStoreFactory.SCREENMAP;

    public static final Param SIMPLIFY = AbstractDuckDBDataStoreFactory.SIMPLIFY;

    @Override
    protected String getDatabaseID() {
        return GEOPARQUET;
    }

    @Override
    public String getDisplayName() {
        return "GeoParquet";
    }

    @Override
    public String getDescription() {
        return "GeoParquet format data files (*.parquet)";
    }

    @Override
    protected void addDatabaseSpecificParameters(Map<String, Object> parameters) {
        // Add GeoParquet specific parameters
        parameters.put(DBTYPE.key, DBTYPE);
        parameters.put(URI_PARAM.key, URI_PARAM);
    }

    @Override
    protected GeoParquetDialect createSQLDialect(JDBCDataStore dataStore, Map<String, ?> params) {
        dataStore.dispose();
        try {
            String lookUp = (String) Objects.requireNonNull(URI_PARAM.lookUp(params));
            java.net.URI uri = new URI(lookUp);
            return new GeoParquetDialect(dataStore, uri);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /** Unused, we override {@link #createSQLDialect(JDBCDataStore, Map)} instead */
    @Override
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected JDBCDataStore setupDataStore(JDBCDataStore dataStore, Map<String, ?> params) throws IOException {
        GeoParquetDialect dialect = (GeoParquetDialect) dataStore.getSQLDialect();
        dataStore.setPrimaryKeyFinder(dialect.getPrimaryKeyFinder());
        dialect.registerParquetViews();
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
