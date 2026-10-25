/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.duckdb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.duckdb.DuckDBDriver;
import org.geotools.api.data.DataStoreFactorySpi;
import org.geotools.data.duckdb.datasource.DuckdbDataSource;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.SQLDialect;

/**
 * Abstract base class for DuckDB-powered datastores. Handles common DuckDB setup, configuration and initialization.
 *
 * <p>This factory provides the foundation for creating DataStore implementations that use DuckDB as their underlying
 * database engine. It manages:
 *
 * <ul>
 *   <li>Common parameter definition and handling
 *   <li>DuckDB JDBC connection setup and configuration
 *   <li>Extension management
 *   <li>DataStore configuration (screenmap, resource limits)
 * </ul>
 *
 * <p>Subclasses like GeoParquetDataStoreFactory extend this to provide format-specific implementation details while
 * inheriting the common DuckDB handling code. This separation allows reuse of the DuckDB infrastructure across multiple
 * datastore implementations.
 *
 * <p>DuckDB is an embedded analytical database that excels at reading and processing analytical data formats like
 * Parquet, and includes excellent built-in support for spatial operations.
 */
public abstract class AbstractDuckDBDataStoreFactory extends JDBCDataStoreFactory implements DataStoreFactorySpi {

    public static final Param NAMESPACE = JDBCDataStoreFactory.NAMESPACE;

    public static final Param FETCHSIZE = JDBCDataStoreFactory.FETCHSIZE;

    public static final Param READ_ONLY = new ParamBuilder("read_only")
            .type(Boolean.class)
            .title("Read only")
            .description(
                    "Open file-backed DuckDB databases in read-only mode at both the GeoTools API layer and the DuckDB engine level")
            .required(false)
            .defaultValue(true)
            .userLevel()
            .build();

    public static final Param MEMORY_LIMIT = new ParamBuilder("memory_limit")
            .type(String.class)
            .title("Memory limit")
            .description("DuckDB memory limit for the store, for example '1GB'")
            .required(false)
            .advancedLevel()
            .build();

    public static final Param THREADS = new ParamBuilder("threads")
            .type(Integer.class)
            .title("Threads")
            .description("DuckDB maximum number of execution threads for the store")
            .required(false)
            .advancedLevel()
            .build();

    public static final Param SCREENMAP = new ParamBuilder("screenmap")
            .type(Boolean.class)
            .title("Support rendering screenmap")
            .description(
                    "Enables the rendering aid used to avoid painting tiny features over and over in the same pixel")
            .required(false)
            .defaultValue(true)
            .advancedLevel()
            .build();

    /** Allows subclasses to add their specific parameters */
    protected abstract void addDatabaseSpecificParameters(Map<String, Object> parameters);

    @Override
    protected abstract String getJDBCUrl(Map<String, ?> params) throws IOException;

    /** Allows subclasses to perform additional setup for their specific datastores */
    protected abstract JDBCDataStore setupDataStore(JDBCDataStore dataStore, Map<String, ?> params) throws IOException;

    @Override
    protected void setupParameters(Map<String, Object> parameters) {
        // Most parameters that don't apply to our datastore, just don't call
        // super.setupParameters() to avoid confusing
        // users

        // Keep namespace parameter
        parameters.put(NAMESPACE.key, NAMESPACE);

        // Add datastore-specific parameters (implemented by subclasses)
        addDatabaseSpecificParameters(parameters);

        parameters.put(READ_ONLY.key, READ_ONLY);
        parameters.put(MEMORY_LIMIT.key, MEMORY_LIMIT);
        parameters.put(THREADS.key, THREADS);
        parameters.put(SCREENMAP.key, SCREENMAP);
        // add these ones back, order matters for the webui
        parameters.put(FETCHSIZE.key, FETCHSIZE);
    }

    @Override
    protected final String getDriverClassName() {
        return "org.duckdb.DuckDBDriver";
    }

    /**
     * Called by {@link #createDataStore(Map)} to set up the JDCB {@link DataSource}
     *
     * @param params the connection parameters
     * @param dialect the {@link DuckDBDialect} from {@link #createSQLDialect(JDBCDataStore, Map)}
     */
    @Override
    protected BasicDataSource createDataSource(Map<String, ?> params, SQLDialect dialect) throws IOException {

        DuckDBDialect duckDBDialect = (DuckDBDialect) dialect;
        List<String> databaseInitSqls = getDatabaseInitSql(params, duckDBDialect);
        Boolean readOnly = (Boolean) READ_ONLY.lookUp(params);
        String jdbcUrl = getJDBCUrl(params);
        boolean inMemory = "jdbc:duckdb:".equals(jdbcUrl);

        DuckdbDataSource dataSource = new DuckdbDataSource(databaseInitSqls);
        dataSource.setUrl(jdbcUrl);
        dataSource.setDriverClassName(getDriverClassName());
        // configure the driver for streaming
        dataSource.addConnectionProperty(DuckDBDriver.JDBC_STREAM_RESULTS, "true");
        if (!inMemory && (readOnly == null || readOnly)) {
            dataSource.addConnectionProperty(DuckDBDriver.DUCKDB_READONLY_PROPERTY, "true");
        }
        // keep at least one open connection to not lose memory databases
        dataSource.setMinIdle(1);
        dataSource.setMaxActive(2 * Runtime.getRuntime().availableProcessors());

        return dataSource;
    }

    private List<String> getDatabaseInitSql(Map<String, ?> params, DuckDBDialect duckDBDialect) throws IOException {
        List<String> databaseInitSqls = new ArrayList<>(duckDBDialect.getDatabaseInitSql());

        String memoryLimit = normalizeStringParam(MEMORY_LIMIT.lookUp(params));
        if (memoryLimit != null) {
            databaseInitSqls.add("SET memory_limit = '" + escapeSqlLiteral(memoryLimit) + "'");
        }

        Integer threads = (Integer) THREADS.lookUp(params);
        if (threads != null) {
            if (threads.intValue() <= 0) {
                throw new IOException("DuckDB datastore parameter 'threads' must be greater than zero");
            }
            databaseInitSqls.add("SET threads = " + threads);
        }

        return databaseInitSqls;
    }

    private String normalizeStringParam(Object value) throws IOException {
        if (value == null) {
            return null;
        }
        String stringValue = value.toString().trim();
        if (stringValue.isEmpty()) {
            throw new IOException("DuckDB datastore parameter 'memory_limit' must not be empty");
        }
        return stringValue;
    }

    private String escapeSqlLiteral(String value) {
        return value.replace("'", "''");
    }

    @Override
    protected JDBCDataStore createDataStoreInternal(JDBCDataStore dataStore, Map<String, ?> params) throws IOException {
        DuckDBDialect dialect = (DuckDBDialect) dataStore.getSQLDialect();

        Boolean screenmap = (Boolean) SCREENMAP.lookUp(params);
        dialect.setScreenMapEnabled(screenmap == null || screenmap);

        // Allow subclasses to perform additional setup
        return setupDataStore(dataStore, params);
    }

    @Override
    protected String getValidationQuery() {
        return "SELECT 1";
    }
}
