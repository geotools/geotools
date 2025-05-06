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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.SimpleFeatureSource;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.rules.TemporaryFolder;

/** Base class for GeoParquet tests that handles common setup and teardown. */
public abstract class GeoParquetTestBase {

    /** The resource directory containing test data */
    protected static final String TEST_DATA_DIR =
            "/Users/groldan/git/geotools/modules/unsupported/geoparquet"; // "/test-data/overturemaps";

    /** Temporary directory for test file extraction */
    @ClassRule
    public static TemporaryFolder tempFolder = new TemporaryFolder();

    /** DataStore under test */
    protected DataStore dataStore;

    @After
    public void tearDown() {
        if (dataStore != null) {
            dataStore.dispose();
            dataStore = null;
        }
    }

    /**
     * Creates a DataStore for a local GeoParquet file or directory.
     *
     * @param path Path to file or directory
     * @return The created DataStore
     * @throws IOException If there's an error creating the DataStore
     */
    protected DataStore createLocalDataStore(String path) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put(GeoParquetDataStoreFactory.DBTYPE.key, "geoparquet");
        params.put(GeoParquetDataStoreFactory.URI_PARAM.key, path);

        return new GeoParquetDataStoreFactory().createDataStore(params);
    }

    /**
     * Creates a DataStore for a remote GeoParquet file.
     *
     * @param url URL to the remote GeoParquet file
     * @return The created DataStore
     * @throws IOException If there's an error creating the DataStore
     * @throws URISyntaxException if there's an error creating an {@link URI} from {@code url}
     */
    protected DataStore createRemoteDataStore(String url) throws IOException, URISyntaxException {
        Map<String, Object> params = new HashMap<>();
        params.put(GeoParquetDataStoreFactory.DBTYPE.key, "geoparquet");
        params.put(GeoParquetDataStoreFactory.URI_PARAM.key, url);
        // params.put(GeoParquetDataStoreFactory.IN_MEMORY.key, Boolean.TRUE);

        return new GeoParquetDataStoreFactory().createDataStore(params);
    }

    /**
     * Verifies that a feature source contains data.
     *
     * @param source The feature source to verify
     * @param expectedCount Expected number of features, or -1 to just verify non-empty
     * @throws IOException If there's an error accessing the features
     */
    protected void verifyFeatureSource(SimpleFeatureSource source, int expectedCount) throws IOException {
        assertNotNull("Feature source should not be null", source);

        // Get the count of features
        int count = source.getFeatures().size();

        if (expectedCount >= 0) {
            assertEquals("Feature count should match expected", expectedCount, count);
        } else {
            assertTrue("Feature source should have features", count > 0);
        }
    }

    /**
     * Gets the path to a test resource file.
     *
     * @param filename The name of the file in the test-data directory
     * @return File object pointing to the resource
     */
    protected File getTestResourceFile(String filename) {
        String resourcePath = TEST_DATA_DIR + "/" + filename;
        return new File(resourcePath); // new File(getClass().getResource(resourcePath).getFile());
    }

    /**
     * Extracts test resources to the temporary directory.
     *
     * @param filename The name of the file to extract
     * @return Path to the extracted file
     * @throws IOException If extraction fails
     */
    protected File extractTestResource(String filename) throws IOException {
        File source = getTestResourceFile(filename);
        File root = tempFolder.getRoot();
        File destination = new File(root, filename);
        FileUtils.copyFile(source, destination);
        return destination;
    }
}
