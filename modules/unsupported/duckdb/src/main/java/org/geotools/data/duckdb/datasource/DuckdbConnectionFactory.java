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
package org.geotools.data.duckdb.datasource;

import static java.util.Objects.requireNonNull;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import org.apache.commons.dbcp.DriverConnectionFactory;
import org.duckdb.DuckDBConnection;

/**
 * A specialized connection factory for DuckDB that leverages connection duplication.
 *
 * <p>This class extends Apache DBCP's DriverConnectionFactory to create and maintain DuckDB connections. It uses a
 * singleton pattern where a single "sentinel" connection is created and then duplicated for each connection request,
 * using DuckDB's native {@code duplicate()} method.
 *
 * <p>The factory initializes the database only once when the sentinel connection is created, executing a provided list
 * of SQL statements (typically to load extensions like 'spatial' and 'parquet').
 *
 * <p>This approach ensures all connections share the same underlying database and extensions only need to be loaded
 * once.
 */
class DuckdbConnectionFactory extends DriverConnectionFactory {

    private volatile DuckDBConnection sentinel;
    private final List<String> databaseInitSqls;

    /**
     * Constructs a new DuckdbConnectionFactory.
     *
     * @param driver The JDBC driver to use
     * @param connectUri The connection URI
     * @param props The connection properties
     * @param databaseInitSqls SQL statements to execute when initializing the database
     */
    public DuckdbConnectionFactory(Driver driver, String connectUri, Properties props, List<String> databaseInitSqls) {
        super(driver, connectUri, props);
        this.databaseInitSqls = requireNonNull(databaseInitSqls, "databaseInitSqls");
    }

    /**
     * Creates a new DuckDB connection by duplicating the sentinel connection.
     *
     * <p>This method implements connection reuse through DuckDB's native {@code duplicate()} feature. On the first
     * call, it creates a "sentinel" connection and initializes the database by executing the initialization SQL
     * statements. Subsequent calls simply duplicate this sentinel connection.
     *
     * @return A new connection to the database
     * @throws SQLException if a database access error occurs
     */
    @Override
    // `current` is an alias to the shared sentinel connection owned by this factory and closed in close().
    @SuppressWarnings("PMD.CloseResource")
    public Connection createConnection() throws SQLException {
        DuckDBConnection current = initializeSentinelIfNeeded();

        DuckDBConnection duplicate = null;
        try {
            duplicate = current.duplicate();
            initDatabase(duplicate, false);
            return duplicate;
        } catch (SQLException e) {
            if (duplicate != null) {
                try {
                    duplicate.close();
                } catch (SQLException suppressed) {
                    e.addSuppressed(suppressed);
                }
            }
            throw e;
        }
    }

    private DuckDBConnection initializeSentinelIfNeeded() throws SQLException {
        DuckDBConnection existing = sentinel;
        if (existing != null) {
            return existing;
        }
        synchronized (this) {
            DuckDBConnection initialized = sentinel;
            if (initialized == null) {
                initialized = createAndInitializeSentinel();
                sentinel = initialized;
            }
            return initialized;
        }
    }

    private DuckDBConnection createAndInitializeSentinel() throws SQLException {
        DuckDBConnection created = (DuckDBConnection) super.createConnection();
        try {
            initDatabase(created, true);
            return created;
        } catch (SQLException e) {
            try {
                created.close();
            } catch (SQLException suppressed) {
                e.addSuppressed(suppressed);
            }
            throw e;
        }
    }

    /**
     * Closes this connection factory and releases all resources.
     *
     * <p>This method safely closes the sentinel connection to free database resources.
     *
     * @throws SQLException if a database access error occurs
     */
    public synchronized void close() throws SQLException {
        if (sentinel != null) {
            try {
                sentinel.close();
            } finally {
                sentinel = null;
            }
        }
    }

    /**
     * Initializes the database by executing the provided SQL statements.
     *
     * <p>This method is called only once when the sentinel connection is created. It typically loads DuckDB extensions
     * like 'spatial' and 'parquet' that are required for GeoParquet functionality.
     *
     * @param conn The connection to execute the initialization SQL on
     * @throws SQLException if a database access error occurs
     */
    private void initDatabase(DuckDBConnection conn, boolean allowInstall) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            for (String sql : databaseInitSqls) {
                if (!allowInstall && isInstallStatement(sql)) {
                    continue;
                }
                stmt.execute(sql);
            }
        }
    }

    private boolean isInstallStatement(String sql) {
        return sql != null && sql.trim().regionMatches(true, 0, "install", 0, "install".length());
    }
}
