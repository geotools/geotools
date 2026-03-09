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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import org.duckdb.DuckDBDriver;
import org.geotools.data.duckdb.DuckDBTestUtils;
import org.geotools.data.duckdb.security.DuckDBExecutionPolicies;
import org.junit.After;
import org.junit.Test;

public class DuckdbConnectionFactoryTest {

    private final List<Path> temporaryDirectories = new ArrayList<>();

    @After
    public void cleanup() throws IOException {
        for (Path directory : temporaryDirectories) {
            DuckDBTestUtils.deleteRecursively(directory);
        }
        temporaryDirectories.clear();
    }

    @Test
    public void testCreateConnectionIsNotSynchronized() throws Exception {
        Method method = DuckdbConnectionFactory.class.getDeclaredMethod("createConnection");
        assertFalse(Modifier.isSynchronized(method.getModifiers()));
    }

    @Test
    public void testConcurrentConnectionCreationUsesSinglePhysicalConnection() throws Exception {
        CountingDriver driver = new CountingDriver(new DuckDBDriver());
        Path database = createDatabasePath();
        DuckdbConnectionFactory factory = new DuckdbConnectionFactory(
                driver,
                "jdbc:duckdb:" + database.toAbsolutePath(),
                new Properties(),
                List.of(),
                DuckDBExecutionPolicies.jdbcStorePublic());

        ExecutorService executor = Executors.newFixedThreadPool(8);
        try {
            CyclicBarrier barrier = new CyclicBarrier(8);
            List<Callable<Void>> tasks = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                tasks.add(() -> {
                    barrier.await(5, TimeUnit.SECONDS);
                    try (Connection connection = factory.createConnection()) {
                        assertNotNull(connection);
                    }
                    return null;
                });
            }

            List<Future<Void>> futures = executor.invokeAll(tasks);
            for (Future<Void> future : futures) {
                future.get(5, TimeUnit.SECONDS);
            }
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            } finally {
                factory.close();
            }
        }

        assertEquals(1, driver.getConnectCount());
    }

    private Path createDatabasePath() throws IOException {
        Path directory = Files.createTempDirectory("gt-duckdb-factory-");
        temporaryDirectories.add(directory);
        return directory.resolve("store.duckdb");
    }

    private static final class CountingDriver implements Driver {

        private final Driver delegate;
        private final AtomicInteger connectCount = new AtomicInteger();

        private CountingDriver(Driver delegate) {
            this.delegate = delegate;
        }

        @Override
        public Connection connect(String url, Properties info) throws SQLException {
            Connection connection = delegate.connect(url, info);
            if (connection != null) {
                connectCount.incrementAndGet();
            }
            return connection;
        }

        @Override
        public boolean acceptsURL(String url) throws SQLException {
            return delegate.acceptsURL(url);
        }

        @Override
        public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
            return delegate.getPropertyInfo(url, info);
        }

        @Override
        public int getMajorVersion() {
            return delegate.getMajorVersion();
        }

        @Override
        public int getMinorVersion() {
            return delegate.getMinorVersion();
        }

        @Override
        public boolean jdbcCompliant() {
            return delegate.jdbcCompliant();
        }

        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return delegate.getParentLogger();
        }

        int getConnectCount() {
            return connectCount.get();
        }
    }
}
