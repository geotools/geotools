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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.geotools.data.duckdb.DuckDBTestUtils;
import org.geotools.data.duckdb.security.DuckDBExecutionPolicies;
import org.geotools.data.duckdb.security.DuckDBExecutionPolicy;
import org.junit.After;
import org.junit.Test;

public class DuckdbDataSourceSecurityTest {

    private final List<Path> temporaryDirectories = new ArrayList<>();

    @After
    public void cleanup() throws IOException {
        for (Path directory : temporaryDirectories) {
            DuckDBTestUtils.deleteRecursively(directory);
        }
        temporaryDirectories.clear();
    }

    @Test
    public void testInitializationRejectsUserManagedBootstrapSql() throws Exception {
        assertInitRejected(DuckDBExecutionPolicies.jdbcStorePublic());
        assertInitRejected(DuckDBExecutionPolicies.jdbcStoreWritable());
    }

    @Test
    public void testPublicDataSourceRejectsWritesViewsAndSqlEscapePaths() throws Exception {
        try (DuckdbDataSource dataSource = createDataSource(List.of(), DuckDBExecutionPolicies.jdbcStorePublic());
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()) {

            assertRejected(() -> statement.execute("create table blocked(id integer)"));
            assertRejected(() -> statement.execute("create or replace view blocked as select 1"));
            assertRejected(() -> statement.execute("with cte as (select 1) select * from cte"));
            assertRejected(() -> statement.execute("with x as (insert into t values (1)) select * from x"));
            assertRejected(() -> statement.execute("-- comment\nselect 1"));
            assertRejected(() -> connection.nativeSQL("select 1; select 2"));
        }
    }

    @Test
    public void testWritableDataSourceAllowsTablesButRejectsViewsAndSecrets() throws Exception {
        try (DuckdbDataSource dataSource = createDataSource(List.of(), DuckDBExecutionPolicies.jdbcStoreWritable());
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()) {

            statement.execute("create table allowed(id integer)");
            statement.execute("drop table allowed");

            assertRejected(() -> statement.execute("create or replace view blocked as select 1"));
            assertRejected(() -> statement.execute(
                    "create or replace secret blocked (type s3, key_id 'a', secret 'b', region 'us-east-1')"));
        }
    }

    @Test
    public void testTrustedInitSqlIsAppliedToDuplicatedConnections() throws Exception {
        List<String> initSql = List.of("install spatial", "load spatial");
        try (DuckdbDataSource dataSource = createDataSource(initSql, DuckDBExecutionPolicies.jdbcStorePublic())) {
            assertSpatialFunctionAvailable(dataSource.getConnection());
            assertSpatialFunctionAvailable(dataSource.getConnection());
        }
    }

    private void assertInitRejected(DuckDBExecutionPolicy policy) throws Exception {
        try (DuckdbDataSource dataSource = createDataSource(List.of("load spatial", "select 1"), policy)) {
            try {
                dataSource.getConnection();
                fail("Expected init SQL to be rejected for policy " + policy.getName());
            } catch (SQLException e) {
                assertTrue(e.getMessage().contains("rejected"));
            }
        }
    }

    private DuckdbDataSource createDataSource(List<String> initSql, DuckDBExecutionPolicy policy) throws IOException {
        Path database = createDatabasePath();
        DuckdbDataSource dataSource = new DuckdbDataSource(initSql, policy);
        dataSource.setUrl("jdbc:duckdb:" + database.toAbsolutePath());
        dataSource.setMinIdle(0);
        dataSource.setMaxActive(1);
        return dataSource;
    }

    private Path createDatabasePath() throws IOException {
        Path directory = Files.createTempDirectory("gt-duckdb-datasource-");
        temporaryDirectories.add(directory);
        return directory.resolve("store.duckdb");
    }

    private void assertRejected(ThrowingRunnable runnable) throws Exception {
        try {
            runnable.run();
            fail("Expected operation to be rejected");
        } catch (SQLException e) {
            assertTrue(e.getMessage().contains("rejected"));
        }
    }

    private void assertSpatialFunctionAvailable(Connection connection) throws Exception {
        try (connection;
                Statement statement = connection.createStatement()) {
            assertTrue(statement.execute("select ST_AsText(ST_Point(0, 0))"));
        }
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Exception;
    }
}
