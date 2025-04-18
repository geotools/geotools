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
package org.geotools.data.duckdb.datasource;

import static java.util.Objects.requireNonNull;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;
import java.util.logging.Logger;
import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.data.jdbc.datasource.ManageableDataSource;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.util.logging.Logging;

/**
 * A specialized JDBC DataSource implementation for DuckDB connections.
 *
 * <p>This class extends Apache DBCP's BasicDataSource to provide connection pooling specifically for DuckDB. It uses a
 * custom {@link DuckdbConnectionFactory} to create connections that leverage DuckDB's connection duplication feature.
 *
 * <p>The class accepts a list of SQL statements that are executed once to initialize the database (typically to load
 * extensions like 'spatial' and 'parquet').
 *
 * @implNote implementing {@link ManageableDataSource} is necessary for {@link JDBCDataStore#dispose()} to call
 *     {@link #close()}
 */
public class DuckdbDataSource extends BasicDataSource implements ManageableDataSource {

    private DuckdbConnectionFactory duckdbConnectionFactory;
    private List<String> databaseInitSqls;

    /**
     * Constructs a new DuckdbDataSource.
     *
     * @param databaseInitSqls A list of SQL statements to execute once when initializing the database (typically used
     *     to load DuckDB extensions like 'spatial' and 'parquet')
     * @throws NullPointerException if databaseInitSqls is null
     */
    public DuckdbDataSource(List<String> databaseInitSqls) {
        this.databaseInitSqls = requireNonNull(databaseInitSqls);
        super.setDriverClassName("org.duckdb.DuckDBDriver");
    }

    /**
     * Creates a DuckDB-specific connection factory.
     *
     * <p>This method overrides the parent class implementation to create a {@link DuckdbConnectionFactory} instead of
     * the default one. It configures connection properties and passes the database initialization SQL statements to the
     * factory.
     *
     * @return The created DuckdbConnectionFactory
     * @throws SQLException if there is an error creating the connection factory
     */
    @Override
    protected DuckdbConnectionFactory createConnectionFactory() throws SQLException {

        Driver driver = DriverManager.getDriver(requireNonNull(getUrl(), "url is null"));
        String validationQuery = getValidationQuery();
        if (validationQuery == null) {
            setTestOnBorrow(false);
            setTestOnReturn(false);
            setTestWhileIdle(false);
        }
        String user = getUsername();
        String pwd = getPassword();
        if (user != null) {
            addConnectionProperty("user", user);
        }
        if (pwd != null) {
            addConnectionProperty("password", pwd);
        }
        duckdbConnectionFactory = new DuckdbConnectionFactory(driver, url, connectionProperties, databaseInitSqls);
        return duckdbConnectionFactory;
    }

    /**
     * Closes this data source, releasing all associated resources.
     *
     * <p>This method ensures proper cleanup by first closing the underlying DuckdbConnectionFactory (which closes the
     * sentinel connection) and then calling the parent class close method.
     *
     * @throws SQLException if there is an error closing the data source
     */
    @Override
    public synchronized void close() throws SQLException {
        if (duckdbConnectionFactory != null) {
            duckdbConnectionFactory.close();
        }
        super.close();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logging.getLogger(getClass()).getParent();
    }
}
