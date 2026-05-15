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

import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.geotools.jdbc.CompositePrimaryKeyFinder;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.SQLDialect;

class DuckDBJDBCDataStoreFactory extends AbstractDuckDBDataStoreFactory {

    /** Parameter for memory only database */
    static final JDBCDataStoreFactory.Param IN_MEMORY = new ParamBuilder("memory")
            .type(Boolean.class)
            .title("Use in-memory DuckDB database")
            .description("Use in-memory DuckDB database. Required if 'database' is not provided.")
            .required(false)
            .defaultValue(false)
            .build();

    /** Parameter for DuckDB database file */
    static final JDBCDataStoreFactory.Param DB_PATH = new ParamBuilder("database")
            .type(String.class)
            .description("Path to DuckDB database file. Required if it's not an in-memory database.")
            .required(false)
            .build();

    /** base location to store temporary files */
    protected File baseDirectory = null;

    public void setBaseDirectory(File baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

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
    public boolean canProcess(Map<String, ?> params) {
        if (!super.canProcess(params)) {
            return false;
        }
        try {
            return validateStorageConfiguration(params) == null;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    protected String getDatabaseID() {
        return "duckdb";
    }

    @Override
    protected String getJDBCUrl(Map<String, ?> params) throws IOException {
        String validationError = validateStorageConfiguration(params);
        if (validationError != null) {
            throw new IOException(validationError);
        }

        String database = (String) DB_PATH.lookUp(params);
        database = database == null ? null : database.trim();
        boolean inMemory = Boolean.TRUE.equals(IN_MEMORY.lookUp(params));

        if (inMemory) {
            return "jdbc:duckdb:";
        } else if (baseDirectory == null) {
            return "jdbc:duckdb:" + database;
        } else {
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
        JDBCDataStore guarded = DuckDBDataStores.guard(dataStore);
        guarded.setPrimaryKeyFinder(
                new CompositePrimaryKeyFinder(guarded.getPrimaryKeyFinder(), new DuckDBPrimaryKeyFinder()));
        return guarded;
    }

    /**
     * Validates the storage configuration parameters to ensure that they are consistent and meet the requirements for
     * creating a DuckDB datastore. Specifically, it checks that if 'memory' is true, then 'database' must not be
     * provided, and if 'memory' is false, then 'database' must be provided.
     *
     * @param params the parameters to validate
     * @return an error message if the configuration is invalid, or null if it is valid
     * @throws IOException if an I/O error occurs during validation
     */
    private String validateStorageConfiguration(Map<String, ?> params) throws IOException {
        String database = (String) DB_PATH.lookUp(params);
        database = database == null ? null : database.trim();
        boolean hasDatabase = database != null && !database.isEmpty();
        boolean inMemory = Boolean.TRUE.equals(IN_MEMORY.lookUp(params));

        if (inMemory && hasDatabase) {
            return "DuckDB datastore parameters 'memory' and 'database' are mutually exclusive";
        }
        if (!inMemory && !hasDatabase) {
            return "DuckDB datastore parameter 'database' is required unless 'memory' is true";
        }
        return null;
    }
}
