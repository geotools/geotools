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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.geotools.api.data.DataAccessFactory;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFactorySpi;
import org.geotools.api.data.DataStoreFinder;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.util.decorate.Wrapper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class DuckDBDataStoreFactoryTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void testFactoryFinder() {
        DataStoreFactorySpi found = null;
        Iterator<DataStoreFactorySpi> it = DataStoreFinder.getAvailableDataStores();
        while (it.hasNext()) {
            DataStoreFactorySpi candidate = it.next();
            if (candidate instanceof DuckDBDataStoreFactory) {
                found = candidate;
                break;
            }
        }

        assertNotNull(found);
        assertEquals("DuckDB", found.getDisplayName());
    }

    @Test
    public void testCanProcessInMemoryDuckDB() {
        DuckDBDataStoreFactory factory = new DuckDBDataStoreFactory();
        Map<String, Object> params = new HashMap<>();
        params.put("dbtype", "duckdb");
        params.put("memory", Boolean.TRUE);

        assertTrue(factory.canProcess(params));
    }

    @Test
    public void testCanProcessRejectsMissingStorageConfiguration() {
        DuckDBDataStoreFactory factory = new DuckDBDataStoreFactory();
        Map<String, Object> params = new HashMap<>();
        params.put("dbtype", "duckdb");
        params.put("memory", Boolean.FALSE);

        assertFalse(factory.canProcess(params));
    }

    @Test
    public void testCanProcessRejectsConflictingStorageConfiguration() {
        DuckDBDataStoreFactory factory = new DuckDBDataStoreFactory();
        Map<String, Object> params = new HashMap<>();
        params.put("dbtype", "duckdb");
        params.put("memory", Boolean.TRUE);
        params.put("database", "conflict.duckdb");

        assertFalse(factory.canProcess(params));
    }

    @Test
    public void testReadOnlyParameterIsExposedAndDefaultsToTrue() {
        DuckDBDataStoreFactory factory = new DuckDBDataStoreFactory();
        DataAccessFactory.Param[] params = factory.getParametersInfo();

        DataAccessFactory.Param readOnly = null;
        for (DataAccessFactory.Param param : params) {
            if (AbstractDuckDBDataStoreFactory.READ_ONLY.key.equals(param.key)) {
                readOnly = param;
                break;
            }
        }

        assertNotNull(readOnly);
        assertEquals(Boolean.TRUE, readOnly.sample);
    }

    @Test
    public void testCreateDataStoreWrapsJdbcStoreAndFeatureSourcesReportWrapper() throws Exception {
        File database = new File(temporaryFolder.newFolder("duckdb-factory"), "store.duckdb");
        createSeedTable(database);

        DuckDBDataStoreFactory factory = new DuckDBDataStoreFactory();
        Map<String, Object> params = new HashMap<>();
        params.put("dbtype", "duckdb");
        params.put("database", database.getAbsolutePath());
        params.put("read_only", Boolean.FALSE);

        DataStore store = null;
        try {
            store = factory.createDataStore(params);
            assertTrue(store instanceof DuckDBDataStore);
            assertFalse(store instanceof org.geotools.jdbc.JDBCDataStore);
            assertTrue(store instanceof Wrapper);
            assertTrue(((Wrapper) store).isWrapperFor(DuckDBDataStore.class));
            assertFalse(((Wrapper) store).isWrapperFor(org.geotools.jdbc.JDBCDataStore.class));
            assertSame(store, ((Wrapper) store).unwrap(DuckDBDataStore.class));

            SimpleFeatureSource featureSource = store.getFeatureSource("existing");
            assertSame(store, featureSource.getDataStore());

            try {
                ((Wrapper) store).unwrap(org.geotools.jdbc.JDBCDataStore.class);
                fail("Expected JDBCDataStore unwrap to be rejected");
            } catch (IllegalArgumentException e) {
                assertNotEquals(-1, e.getMessage().indexOf("does not wrap"));
            }
        } finally {
            if (store != null) {
                store.dispose();
            }
        }
    }

    @Test
    public void testCreateDataStoreRejectsMissingDatabaseWhenNotInMemory() {
        DuckDBDataStoreFactory factory = new DuckDBDataStoreFactory();
        Map<String, Object> params = new HashMap<>();
        params.put("dbtype", "duckdb");
        params.put("memory", Boolean.FALSE);

        try {
            factory.createDataStore(params);
            fail("Expected missing database configuration to be rejected");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("'database' is required"));
        }
    }

    @Test
    public void testCreateDataStoreRejectsConflictingMemoryAndDatabaseParams() {
        DuckDBDataStoreFactory factory = new DuckDBDataStoreFactory();
        Map<String, Object> params = new HashMap<>();
        params.put("dbtype", "duckdb");
        params.put("memory", Boolean.TRUE);
        params.put("database", "conflict.duckdb");

        try {
            factory.createDataStore(params);
            fail("Expected conflicting storage configuration to be rejected");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("mutually exclusive"));
        }
    }

    @Test
    public void testCreateDataStoreSupportsInMemoryMode() throws Exception {
        DuckDBDataStoreFactory factory = new DuckDBDataStoreFactory();
        Map<String, Object> params = new HashMap<>();
        params.put("dbtype", "duckdb");
        params.put("memory", Boolean.TRUE);
        params.put("read_only", Boolean.FALSE);

        DataStore store = null;
        try {
            store = factory.createDataStore(params);
            assertNotNull(store);
            assertTrue(store instanceof DuckDBDataStore);
            assertEquals(0, store.getTypeNames().length);
        } finally {
            if (store != null) {
                store.dispose();
            }
        }
    }

    @Test
    public void testPrimaryKeyIsDetectedButNotExposedByDefault() throws Exception {
        File database = new File(temporaryFolder.newFolder("duckdb-pk-default"), "store.duckdb");
        createSeedTable(database);

        DuckDBDataStoreFactory factory = new DuckDBDataStoreFactory();
        Map<String, Object> params = new HashMap<>();
        params.put("dbtype", "duckdb");
        params.put("database", database.getAbsolutePath());
        params.put("read_only", Boolean.FALSE);

        DataStore store = null;
        try {
            store = factory.createDataStore(params);
            SimpleFeatureSource featureSource = store.getFeatureSource("existing");
            assertEquals(2, featureSource.getSchema().getAttributeCount());
            assertNotNull(featureSource.getSchema().getDescriptor("geom"));
            assertNotNull(featureSource.getSchema().getDescriptor("name"));
            assertNull(featureSource.getSchema().getDescriptor("id"));
        } finally {
            if (store != null) {
                store.dispose();
            }
        }
    }

    private void createSeedTable(File database) throws Exception {
        org.geotools.jdbc.JDBCDataStore store = DuckDBTestUtils.createStore(database.toPath(), false);
        try {
            DuckDBTestUtils.runSetupSql(
                    store,
                    "CREATE TABLE existing (id INTEGER PRIMARY KEY, geom GEOMETRY, name VARCHAR)",
                    "INSERT INTO existing VALUES (1, ST_GeomFromText('POINT (0 0)'), 'seed')");
        } finally {
            store.dispose();
        }
    }
}
