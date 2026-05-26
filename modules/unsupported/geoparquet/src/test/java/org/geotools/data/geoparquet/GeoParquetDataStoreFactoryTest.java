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
package org.geotools.data.geoparquet;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.geotools.api.data.DataAccessFactory;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFactorySpi;
import org.geotools.api.data.DataStoreFinder;
import org.geotools.api.data.Parameter;
import org.geotools.data.duckdb.ForwardingDataStore;
import org.geotools.jdbc.JDBCDataStore;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

/** Tests for GeoParquetDataStoreFactory. */
public class GeoParquetDataStoreFactoryTest {

    @ClassRule
    public static final GeoParquetTestSupport support = new GeoParquetTestSupport();

    private GeoParquetDataStoreFactory factory;
    private Map<String, Object> worldGridDirParams;

    @Before
    public void setUp() {

        factory = new GeoParquetDataStoreFactory();
        worldGridDirParams = new HashMap<>();

        File directory = support.getWorldgridDir();
        // Basic params
        worldGridDirParams.put(GeoParquetDataStoreFactory.DBTYPE.key, "geoparquet");
        worldGridDirParams.put(
                GeoParquetDataStoreFactory.URI_PARAM.key, directory.toURI().toASCIIString());
    }

    @Test
    public void testGetDisplayName() {
        assertEquals("GeoParquet", factory.getDisplayName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("GeoParquet format data files (*.parquet)", factory.getDescription());
    }

    @Test
    public void testCanProcess() {
        assertTrue(factory.canProcess(worldGridDirParams));

        // Wrong database type
        Map<String, Object> wrongParams = new HashMap<>(worldGridDirParams);
        wrongParams.put(GeoParquetDataStoreFactory.DBTYPE.key, "postgis");
        assertFalse(factory.canProcess(wrongParams));
    }

    @Test
    public void testCreateDataStore() throws IOException {
        // We're not checking if the file exists in the factory
        DataStore ds = factory.createDataStore(worldGridDirParams);

        assertNotNull(ds);
        assertTrue(ds instanceof GeoparquetDataStore);

        assertNotNull(((GeoparquetDataStore) ds).getSQLDialect());
    }

    @Test
    public void testWithURL() {
        // Test with URL parameter
        Map<String, Object> urlParams = new HashMap<>(worldGridDirParams);
        urlParams.put(GeoParquetDataStoreFactory.URI_PARAM.key, "http://example.com/test.parquet");

        assertTrue(factory.canProcess(urlParams));

        // Both file and URL should not work
        Map<String, Object> bothParams = new HashMap<>(urlParams);
        bothParams.put(GeoParquetDataStoreFactory.URI_PARAM.key, "test_file.parquet");

        // This should still pass canProcess but may fail on createDataStore
        assertTrue(factory.canProcess(bothParams));
    }

    @Test
    public void testMissingRequired() throws IOException {
        // Missing file and URL
        Map<String, Object> missingParams = new HashMap<>(worldGridDirParams);
        missingParams.remove(GeoParquetDataStoreFactory.URI_PARAM.key);

        assertFalse(factory.canProcess(missingParams));

        IOException expected = assertThrows(IOException.class, () -> factory.createDataStore(missingParams));
        assertThat(expected.getMessage(), containsString("Parameter uri is required"));
    }

    @Test
    public void testFactoryFinder() {
        // Test that our factory can be found through DataStoreFinder
        DataStoreFactorySpi found = null;
        // Convert iterator to iterable
        Iterator<DataStoreFactorySpi> it = DataStoreFinder.getAvailableDataStores();
        while (it.hasNext()) {
            DataStoreFactorySpi f = it.next();
            if ("GeoParquet".equals(f.getDisplayName())) {
                found = f;
                break;
            }
        }
        assertNotNull("Should find the GeoParquet factory", found);
        assertEquals(GeoParquetDataStoreFactory.class, found.getClass());
    }

    @Test
    public void testS3CredentialChainParametersAreExposedForUi() {
        DataAccessFactory.Param[] params = factory.getParametersInfo();

        DataAccessFactory.Param endpoint = null;
        DataAccessFactory.Param urlStyle = null;
        for (DataAccessFactory.Param param : params) {
            if (GeoParquetDataStoreFactory.ENDPOINT.key.equals(param.key)) {
                endpoint = param;
            } else if (GeoParquetDataStoreFactory.URL_STYLE.key.equals(param.key)) {
                urlStyle = param;
            }
        }

        assertNotNull(endpoint);
        assertNotNull(urlStyle);
        assertFalse(endpoint.required);
        assertFalse(urlStyle.required);
        assertEquals("user", endpoint.metadata.get(Parameter.LEVEL));
        assertEquals("user", urlStyle.metadata.get(Parameter.LEVEL));
    }

    @Test
    public void testCreateDataStoreAppliesDuckdbResourceLimits() throws Exception {
        Map<String, Object> params = new HashMap<>(worldGridDirParams);
        params.put(GeoParquetDataStoreFactory.MEMORY_LIMIT.key, "1GB");
        params.put(GeoParquetDataStoreFactory.THREADS.key, 2);

        GeoparquetDataStore store = null;
        try {
            store = factory.createDataStore(params);

            assertDuckDbSetting(store, "memory_limit", "953.6 MiB");
            assertDuckDbSetting(store, "threads", "2");
        } finally {
            if (store != null) {
                store.dispose();
            }
        }
    }

    private void assertDuckDbSetting(GeoparquetDataStore store, String settingName, String expectedValue)
            throws Exception {
        JDBCDataStore delegate = getDelegate(store);
        try (Connection connection = delegate.getDataSource().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT current_setting('" + settingName + "')")) {
            if (!resultSet.next()) {
                fail("Expected DuckDB setting " + settingName + " to be returned");
            }
            String actualValue = resultSet.getString(1);
            assertNotNull(actualValue);
            assertEquals(expectedValue, actualValue.trim());
        }
    }

    private JDBCDataStore getDelegate(GeoparquetDataStore store) throws Exception {
        Field delegate = ForwardingDataStore.class.getDeclaredField("delegate");
        delegate.setAccessible(true);
        return (JDBCDataStore) delegate.get(store);
    }
}
