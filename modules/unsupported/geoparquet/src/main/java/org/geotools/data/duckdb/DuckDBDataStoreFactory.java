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
package org.geotools.data.duckdb;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.geotools.api.data.DataStoreFactorySpi;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.SQLDialect;

public class DuckDBDataStoreFactory extends AbstractDuckDBDataStoreFactory implements DataStoreFactorySpi {

    /** Parameter for memory only database */
    public static final JDBCDataStoreFactory.Param IN_MEMORY = new ParamBuilder("memory")
            .type(Boolean.class)
            .title("Use in-memory DuckDB database")
            .description("Use in-memory DuckDB database. Required if 'database' is not provided.")
            .required(false)
            .defaultValue(false)
            .build();

    /** Parameter for DuckDB database file */
    public static final JDBCDataStoreFactory.Param DB_PATH = new ParamBuilder("database")
            .type(String.class)
            .description("Path to DuckDB database file. Required if it's not an in-memory database.")
            .required(false)
            .build();

    /** base location to store temporary files */
    protected File baseDirectory = null;

    /**
     * Sets the base location for any temporary files.
     *
     * @param baseDirectory A directory.
     */
    public void setBaseDirectory(File baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    /** The base location for any temporary files. */
    public File getBaseDirectory() {
        return baseDirectory;
    }

    @Override
    public String getDisplayName() {
        return "DuckDB";
    }

    @Override
    public String getDescription() {
        return "DuckDB";
    }

    @Override
    protected void addDatabaseSpecificParameters(Map<String, Object> parameters) {
        parameters.put(IN_MEMORY.key, IN_MEMORY);
        parameters.put(DB_PATH.key, DB_PATH);
    }

    @Override
    protected String getDatabaseID() {
        return "duckdb"; // Used for JDBC URL construction
    }

    @Override
    protected String getJDBCUrl(Map<String, ?> params) throws IOException {
        String database = (String) DB_PATH.lookUp(params);

        if (Boolean.TRUE.equals(IN_MEMORY.lookUp(params)) || database == null) {
            return "jdbc:duckdb:";
        } else if (baseDirectory == null) {
            // use current working directory
            return "jdbc:duckdb:" + database;
        } else {
            // use directory specified if the path is relative
            String location;
            if (!new File(database).isAbsolute()) {
                location = new File(baseDirectory, database).getAbsolutePath();
            } else {
                location = database;
            }

            return "jdbc:duckdb:" + location;
        }
    }

    @Override
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        return new DuckDBDialect(dataStore);
    }

    @Override
    protected JDBCDataStore setupDataStore(JDBCDataStore dataStore, Map<String, ?> params) throws IOException {
        return dataStore;
    }
}
