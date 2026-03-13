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
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.geotools.jdbc.JDBCDataStore;

/** Shared test helpers for DuckDB module tests. */
public final class DuckDBTestUtils {

    private DuckDBTestUtils() {}

    public static void deleteRecursively(Path directory) throws IOException {
        if (directory == null || !Files.exists(directory)) {
            return;
        }

        try (Stream<Path> paths = Files.walk(directory)) {
            for (Path path : paths.sorted(Comparator.reverseOrder()).toList()) {
                Files.deleteIfExists(path);
            }
        }
    }

    public static JDBCDataStore createStore(Path database, boolean readOnly) throws IOException {
        DuckDBJDBCDataStoreFactory factory = new DuckDBJDBCDataStoreFactory();
        Map<String, Object> params = new HashMap<>();
        params.put("dbtype", "duckdb");
        params.put("database", database.toAbsolutePath().toString());
        params.put("read_only", readOnly);
        return factory.createDataStore(params);
    }

    public static void runSetupSql(JDBCDataStore store, String... statements) throws Exception {
        DuckdbDataSourceGuard policyGuard = new DuckdbDataSourceGuard(store);
        try {
            policyGuard.enable();
            try (Connection connection = store.getDataSource().getConnection();
                    Statement statement = connection.createStatement()) {
                for (String sql : statements) {
                    statement.execute(sql);
                }
            }
        } finally {
            policyGuard.restore();
        }
    }

    private static final class DuckdbDataSourceGuard {
        private final JDBCDataStore store;
        private org.geotools.data.duckdb.security.DuckDBExecutionPolicy previous;

        private DuckdbDataSourceGuard(JDBCDataStore store) {
            this.store = store;
        }

        @SuppressWarnings("PMD.CloseResource")
        private void enable() throws Exception {
            if (store.getDataSource() instanceof org.geotools.data.duckdb.datasource.DuckdbDataSource dataSource) {
                previous = dataSource.getExecutionPolicy();
                dataSource.setExecutionPolicy(
                        org.geotools.data.duckdb.security.DuckDBExecutionPolicies.jdbcTestSetup());
            }
        }

        @SuppressWarnings("PMD.CloseResource")
        private void restore() throws Exception {
            if (previous != null
                    && store.getDataSource()
                            instanceof org.geotools.data.duckdb.datasource.DuckdbDataSource dataSource) {
                dataSource.setExecutionPolicy(previous);
            }
        }
    }
}
