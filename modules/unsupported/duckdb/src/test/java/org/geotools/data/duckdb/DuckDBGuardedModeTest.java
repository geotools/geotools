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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.data.SimpleFeatureStore;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCFeatureStore;
import org.geotools.jdbc.VirtualTable;
import org.junit.After;
import org.junit.Test;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;

public class DuckDBGuardedModeTest {

    private final List<Path> temporaryDirectories = new ArrayList<>();

    @After
    public void cleanup() throws IOException {
        for (Path directory : temporaryDirectories) {
            DuckDBTestUtils.deleteRecursively(directory);
        }
        temporaryDirectories.clear();
    }

    @Test
    public void testReadOnlyStoreRejectsRemoveSchemaAndFeatureWriters() throws Exception {
        Path database = createDatabasePath();
        createSeedTable(database);

        JDBCDataStore store = createStore(database, true);
        try {
            String typeName = "existing";
            assertFeatureSourceIsReadOnly(store, typeName);

            assertPolicyRejected(() -> store.removeSchema(typeName));
            assertReadOnlyFeatureWriters(typeName, () -> updateExistingFeature(store, typeName, "changed"));
            assertReadOnlyFeatureWriters(typeName, () -> appendFeature(store, typeName, 2, "blocked"));
        } finally {
            store.dispose();
        }
    }

    @Test
    public void testWritableStoreAllowsRemoveSchemaWhenReadOnlyPolicyIsDisabled() throws Exception {
        Path database = createDatabasePath();
        createSeedTable(database);
        JDBCDataStore store = createStore(database, false);

        try {
            String typeName = "existing";
            assertNotNull(store.getSchema(typeName));
            store.removeSchema(typeName);
            assertFalse(List.of(store.getTypeNames()).contains(typeName));
        } finally {
            store.dispose();
        }
    }

    @Test
    public void testWritableStoreAllowsFeatureWritersWhenReadOnlyPolicyIsDisabled() throws Exception {
        Path database = createDatabasePath();
        createSeedTable(database);
        JDBCDataStore store = createStore(database, false);

        try {
            String typeName = "existing";
            assertNotNull(store.getSchema(typeName));
            assertFeatureSourceIsWritable(store, typeName);

            appendFeature(store, typeName, 2, "created");
            updateExistingFeature(store, typeName, "updated");

            assertFeatureCount(store, typeName, 2);
            assertFeatureName(store, typeName, 1, "updated");
            assertFeatureName(store, typeName, 2, "created");
        } finally {
            store.dispose();
        }
    }

    @Test
    public void testVirtualTablesRemainDisabledInBothModes() throws Exception {
        assertVirtualTablesDisabled(true);
        assertVirtualTablesDisabled(false);
    }

    private void assertVirtualTablesDisabled(boolean readOnly) throws Exception {
        Path database = createDatabasePath();
        JDBCDataStore store = createStore(database, readOnly);
        try {
            try {
                store.createVirtualTable(new VirtualTable("vt", "select 1"));
                fail("Expected virtual tables to be disabled");
            } catch (UnsupportedOperationException e) {
                assertTrue(e.getMessage().contains("virtual tables are disabled"));
            }
        } finally {
            store.dispose();
        }
    }

    private void updateExistingFeature(JDBCDataStore store, String typeName, String name) throws Exception {
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                store.getFeatureWriter(typeName, Transaction.AUTO_COMMIT)) {
            assertTrue(writer.hasNext());
            SimpleFeature feature = writer.next();
            feature.setAttribute("name", name);
            writer.write();
        }
    }

    private void appendFeature(JDBCDataStore store, String typeName, int id, String name) throws Exception {
        SimpleFeatureSource source = store.getFeatureSource(typeName);
        if (source instanceof JDBCFeatureStore jdbcFeatureStore) {
            jdbcFeatureStore.setExposePrimaryKeyColumns(true);
        }
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                store.getFeatureWriterAppend(typeName, Transaction.AUTO_COMMIT)) {
            SimpleFeature feature = writer.next();
            feature.setAttribute("id", id);
            feature.setAttribute("name", name);
            feature.setAttribute("geom", point(1, 1));
            writer.write();
        }
    }

    private JDBCDataStore createStore(Path database, boolean readOnly) throws IOException {
        return DuckDBTestUtils.createStore(database, readOnly);
    }

    private void createSeedTable(Path database) throws Exception {
        JDBCDataStore store = createStore(database, false);
        try {
            DuckDBTestUtils.runSetupSql(
                    store,
                    "CREATE TABLE existing (id INTEGER PRIMARY KEY, geom GEOMETRY, name VARCHAR)",
                    "INSERT INTO existing VALUES (1, ST_GeomFromText('POINT (0 0)'), 'seed')");
        } finally {
            store.dispose();
        }
    }

    private Point point(double x, double y) throws Exception {
        Point point = (Point) new WKTReader().read(String.format("POINT (%s %s)", x, y));
        point.setSRID(4326);
        return point;
    }

    private Path createDatabasePath() throws IOException {
        Path directory = Files.createTempDirectory("gt-duckdb-guarded-");
        temporaryDirectories.add(directory);
        return directory.resolve("store.duckdb");
    }

    private void assertFeatureSourceIsReadOnly(JDBCDataStore store, String typeName) throws IOException {
        SimpleFeatureSource source = store.getFeatureSource(typeName);
        assertFalse("Expected a read-only feature source for " + typeName, source instanceof SimpleFeatureStore);
        Object readOnlyMarker = source.getSchema().getUserData().get(JDBCDataStore.JDBC_READ_ONLY);
        assertEquals("Expected JDBC_READ_ONLY marker for " + typeName, Boolean.TRUE, readOnlyMarker);
    }

    private void assertFeatureSourceIsWritable(JDBCDataStore store, String typeName) throws IOException {
        SimpleFeatureSource source = store.getFeatureSource(typeName);
        assertTrue("Expected a writable feature source for " + typeName, source instanceof SimpleFeatureStore);
        Object readOnlyMarker = source.getSchema().getUserData().get(JDBCDataStore.JDBC_READ_ONLY);
        org.junit.Assert.assertNotEquals(
                "Did not expect JDBC_READ_ONLY marker for " + typeName, Boolean.TRUE, readOnlyMarker);
    }

    private void assertFeatureCount(JDBCDataStore store, String tableName, int expectedCount)
            throws SQLException, IOException {
        try (Connection connection = store.getDataSource().getConnection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM \"" + tableName + "\"")) {
            if (!rs.next()) {
                fail("Expected a COUNT(*) row for table " + tableName);
            }
            assertEquals(expectedCount, rs.getInt(1));
        }
    }

    private void assertFeatureName(JDBCDataStore store, String tableName, int id, String expectedName)
            throws SQLException, IOException {
        try (Connection connection = store.getDataSource().getConnection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT name FROM \"" + tableName + "\" WHERE id = " + id)) {
            if (!rs.next()) {
                fail("Expected a row for table " + tableName + " and id " + id);
            }
            assertEquals(expectedName, rs.getString(1));
        }
    }

    private void assertPolicyRejected(ThrowingRunnable runnable) throws Exception {
        assertExpectedException(runnable, SQLException.class, "DuckDB execution policy");
    }

    private void assertReadOnlyFeatureWriters(String typeName, ThrowingRunnable runnable) throws Exception {
        assertExpectedException(runnable, IOException.class, typeName + " is read only");
    }

    private void assertExpectedException(
            ThrowingRunnable runnable, Class<? extends Throwable> expectedType, String expectedMessageFragment)
            throws Exception {
        try {
            runnable.run();
            fail("Expected " + expectedType.getSimpleName() + " containing '" + expectedMessageFragment + "'");
        } catch (Exception e) {
            Throwable match = findInCauseChain(e, expectedType, expectedMessageFragment);
            if (match == null) {
                fail("Expected "
                        + expectedType.getName()
                        + " containing '"
                        + expectedMessageFragment
                        + "', got cause chain: "
                        + describeCauseChain(e));
            }
        }
    }

    private Throwable findInCauseChain(
            Throwable throwable, Class<? extends Throwable> expectedType, String expectedMessageFragment) {
        Throwable current = throwable;
        while (current != null) {
            String message = current.getMessage();
            if (expectedType.isInstance(current) && message != null && message.contains(expectedMessageFragment)) {
                return current;
            }
            current = current.getCause();
        }
        return null;
    }

    private String describeCauseChain(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        Throwable current = throwable;
        while (current != null) {
            if (sb.length() > 0) {
                sb.append(" -> ");
            }
            sb.append(current.getClass().getSimpleName()).append(": ").append(current.getMessage());
            current = current.getCause();
        }
        return sb.toString();
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Exception;
    }
}
