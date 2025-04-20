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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFactorySpi;
import org.geotools.api.data.DataStoreFinder;
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
}
