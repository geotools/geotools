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
package org.geotools.data.duckdb.security;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import org.duckdb.DuckDBConnection;
import org.junit.Test;

public class DuckDBSecureObjectsTest {

    @Test
    public void testPoliciesRejectWrappedConnectionSqlEscapePaths() throws Exception {
        assertWrappedConnectionRestrictions(DuckDBExecutionPolicies.jdbcStorePublic());
        assertWrappedConnectionRestrictions(DuckDBExecutionPolicies.jdbcStoreWritable());
    }

    @Test
    public void testWrappedConnectionRejectsPrepareCallAndVendorUnwrap() throws Exception {
        assertPrepareCallAndUnwrapAreBlocked(DuckDBExecutionPolicies.jdbcStorePublic());
        assertPrepareCallAndUnwrapAreBlocked(DuckDBExecutionPolicies.jdbcStoreWritable());
    }

    private void assertWrappedConnectionRestrictions(DuckDBExecutionPolicy policy) throws Exception {
        try (Connection raw = DriverManager.getConnection("jdbc:duckdb:");
                Connection wrapped = DuckDBSecureObjects.wrapConnection(raw, policy);
                Statement statement = wrapped.createStatement()) {

            assertRejected(
                    () -> wrapped.nativeSQL("select 1; select 2"),
                    "Expected multi-statement SQL to be rejected for policy " + policy.getName());
            assertRejected(
                    () -> statement.execute("-- comment\nselect 1"),
                    "Expected commented SQL to be rejected for policy " + policy.getName());
            assertRejected(
                    () -> statement.execute("load spatial"),
                    "Expected runtime LOAD to be rejected for policy " + policy.getName());
        }
    }

    private void assertPrepareCallAndUnwrapAreBlocked(DuckDBExecutionPolicy policy) throws Exception {
        try (Connection raw = DriverManager.getConnection("jdbc:duckdb:");
                Connection wrapped = DuckDBSecureObjects.wrapConnection(raw, policy)) {

            try {
                wrapped.prepareCall("select 1");
                fail("Expected prepareCall to be blocked");
            } catch (SQLFeatureNotSupportedException e) {
                assertTrue(e.getMessage().contains("disabled"));
            }

            try {
                wrapped.unwrap(DuckDBConnection.class);
                fail("Expected vendor unwrap to be blocked");
            } catch (SQLException e) {
                assertTrue(e.getMessage().contains("disabled"));
            }
        }
    }

    private void assertRejected(ThrowingRunnable runnable, String message) throws Exception {
        try {
            runnable.run();
            fail(message);
        } catch (SQLException e) {
            assertTrue(e.getMessage().contains("rejected"));
        }
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Exception;
    }
}
