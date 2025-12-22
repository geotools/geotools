/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.pmtiles.store;

import static org.geotools.pmtiles.store.PMTilesDataStoreFactory.NAMESPACEP;
import static org.geotools.pmtiles.store.PMTilesDataStoreFactory.URIP;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Streams;
import io.tileverse.pmtiles.PMTilesTestData;
import io.tileverse.rangereader.azure.AzureBlobRangeReaderProvider;
import io.tileverse.rangereader.gcs.GoogleCloudStorageRangeReaderProvider;
import io.tileverse.rangereader.s3.S3RangeReaderProvider;
import io.tileverse.rangereader.spi.AbstractRangeReaderProvider;
import io.tileverse.rangereader.spi.RangeReaderParameter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.geotools.api.data.DataAccessFactory.Param;
import org.geotools.api.data.DataAccessFinder;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFinder;
import org.geotools.tileverse.rangereader.RangeReaderParams;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/** Test suite for {@link PMTilesDataStoreFactory} */
public class PMTilesDataStoreFactoryTest {

    @ClassRule
    public static TemporaryFolder tmp = new TemporaryFolder();

    private static Path andorra;

    @BeforeClass
    public static void extractFile() throws IOException {
        andorra = PMTilesTestData.andorra(tmp.getRoot().toPath());
    }

    @Test
    public void testGetDisplayName() {
        assertEquals("PMTiles", new PMTilesDataStoreFactory().getDisplayName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Protomaps (.pmtiles) with vector tiles", new PMTilesDataStoreFactory().getDescription());
    }

    @Test
    public void testIsAvailable() {
        assertTrue(new PMTilesDataStoreFactory().isAvailable());
    }

    @Test
    public void testGetParametersInfo() {
        Param[] parametersInfo = new PMTilesDataStoreFactory().getParametersInfo();

        assertThat(parametersInfo.length, greaterThan(2));

        assertEquals(NAMESPACEP, parametersInfo[0]);
        assertEquals(URIP, parametersInfo[1]);

        assertCachingParameters(parametersInfo);
    }

    @Test
    public void testGetParametersInfoS3() {
        Param[] parametersInfo = new PMTilesDataStoreFactory().getParametersInfo();

        List<RangeReaderParameter<?>> s3Params = new S3RangeReaderProvider().getParameters();
        assertTrue(s3Params.stream().anyMatch(p -> p.key().contains("io.tileverse.rangereader.s3.")));
        assertParams(s3Params, parametersInfo);

        System.setProperty(S3RangeReaderProvider.ENABLED_KEY, "false");
        try {
            parametersInfo = new PMTilesDataStoreFactory().getParametersInfo();
            List<RangeReaderParameter<?>> noS3Params = s3Params.stream()
                    .filter(p -> !p.key().contains("io.tileverse.rangereader.s3."))
                    .toList();
            assertParams(noS3Params, parametersInfo);
        } finally {
            System.clearProperty(S3RangeReaderProvider.ENABLED_KEY);
        }
    }

    @Test
    public void testGetParametersInfoAzureBlobStorage() {
        Param[] parametersInfo = new PMTilesDataStoreFactory().getParametersInfo();

        List<RangeReaderParameter<?>> azureParams = new AzureBlobRangeReaderProvider().getParameters();
        assertTrue(azureParams.stream().anyMatch(p -> p.key().contains("io.tileverse.rangereader.azure.")));

        assertParams(azureParams, parametersInfo);
        System.setProperty(AzureBlobRangeReaderProvider.ENABLED_KEY, "false");
        try {
            parametersInfo = new PMTilesDataStoreFactory().getParametersInfo();
            List<RangeReaderParameter<?>> noAzureParams = azureParams.stream()
                    .filter(p -> !p.key().contains("io.tileverse.rangereader.azure."))
                    .toList();
            assertParams(noAzureParams, parametersInfo);
        } finally {
            System.clearProperty(AzureBlobRangeReaderProvider.ENABLED_KEY);
        }
    }

    @Test
    public void testGetParametersInfoGoogleCloudStorage() {
        Param[] parametersInfo = new PMTilesDataStoreFactory().getParametersInfo();

        List<RangeReaderParameter<?>> gcsParams = new GoogleCloudStorageRangeReaderProvider().getParameters();
        assertTrue(gcsParams.stream().anyMatch(p -> p.key().contains("io.tileverse.rangereader.gcs.")));
        assertParams(gcsParams, parametersInfo);

        System.setProperty(GoogleCloudStorageRangeReaderProvider.ENABLED_KEY, "false");
        try {
            parametersInfo = new PMTilesDataStoreFactory().getParametersInfo();
            List<RangeReaderParameter<?>> noGCSParams = gcsParams.stream()
                    .filter(p -> !p.key().contains("io.tileverse.rangereader.gcs."))
                    .toList();
            assertParams(noGCSParams, parametersInfo);
        } finally {
            System.clearProperty(GoogleCloudStorageRangeReaderProvider.ENABLED_KEY);
        }
    }

    private void assertParams(List<RangeReaderParameter<?>> expected, Param[] actual) {
        expected = new ArrayList<>(expected);
        expected.remove(AbstractRangeReaderProvider.MEMORY_CACHE_BLOCK_ALIGNED);
        expected.remove(AbstractRangeReaderProvider.MEMORY_CACHE_BLOCK_SIZE);

        List<Param> dataStoreParams = new ArrayList<>(Arrays.asList(actual));
        dataStoreParams.remove(URIP);
        dataStoreParams.remove(NAMESPACEP);

        for (RangeReaderParameter<?> param : expected) {
            Param expectedParam = RangeReaderParams.dataStoreParam(param);
            assertTrue(dataStoreParams.contains(expectedParam));
        }
    }

    private void assertCachingParameters(Param[] parametersInfo) {
        Predicate<? super Param> filter = p -> p.key.startsWith("io.tileverse.rangereader.caching.");
        List<Param> cachingParams = Stream.of(parametersInfo).filter(filter).toList();
        List<Param> expected = List.of(RangeReaderParams.MEMORY_CACHE_ENABLED);
        assertEquals(expected, cachingParams);
    }

    @Test
    public void testCreateNewDataStore() {
        Map<String, ?> params = Map.of(URIP.key, "http://pmtiles.example.com/newfile.pmtiles");
        assertThrows(
                UnsupportedOperationException.class, () -> new PMTilesDataStoreFactory().createNewDataStore(params));
    }

    @Test
    public void testCanProcessFile() throws MalformedURLException {
        PMTilesDataStoreFactory factory = new PMTilesDataStoreFactory();

        assertFalse(factory.canProcess(Map.of()));
        assertFalse(factory.canProcess(Map.of(URIP.key, "not a valid URI")));
        assertFalse(factory.canProcess(Map.of(URIP.key + "1", andorra.toUri())));

        URI notfound = tmp.getRoot().toPath().resolve("notfound.pmtiles").toUri();
        assertTrue(factory.canProcess(Map.of(URIP.key, notfound)));
        assertTrue(factory.canProcess(Map.of(URIP.key, notfound, NAMESPACEP.key, "pmtiles.io")));

        assertTrue("should support URI", factory.canProcess(Map.of(URIP.key, andorra.toUri())));
        assertTrue(
                "should support String",
                factory.canProcess(Map.of(URIP.key, andorra.toAbsolutePath().toString())));
        assertTrue(
                "should support URL",
                factory.canProcess(Map.of(URIP.key, andorra.toUri().toURL())));
        assertTrue("should support Path", factory.canProcess(Map.of(URIP.key, andorra)));
        assertTrue("should support File", factory.canProcess(Map.of(URIP.key, andorra.toFile())));
    }

    @Test
    public void testCanProcessHttp() throws MalformedURLException {
        PMTilesDataStoreFactory factory = new PMTilesDataStoreFactory();

        String http = "http://127.0.0.1:8000/andorra.pmtiles";
        String https = "https://127.0.0.1:8000/andorra.pmtiles";

        assertTrue(factory.canProcess(Map.of(URIP.key, http)));
        assertTrue(factory.canProcess(Map.of(URIP.key, https)));

        assertTrue(factory.canProcess(Map.of(URIP.key, URI.create(http))));
        assertTrue(factory.canProcess(Map.of(URIP.key, URI.create(https))));

        assertTrue(factory.canProcess(Map.of(URIP.key, URI.create(http).toURL())));
        assertTrue(factory.canProcess(Map.of(URIP.key, URI.create(https).toURL())));

        assertTrue(factory.canProcess(Map.of(URIP.key, "s3://" + http)));
        assertTrue(factory.canProcess(Map.of(URIP.key, "s3://" + https)));
    }

    @Test
    public void testCreateDataStoreNotFound() {
        PMTilesDataStoreFactory factory = new PMTilesDataStoreFactory();
        URI notfound = tmp.getRoot().toPath().resolve("notfound.pmtiles").toUri();
        assertThrows(IOException.class, () -> factory.createDataStore(Map.of(URIP.key, notfound)));
    }

    @Test
    public void testCreateDataStore() throws IOException {
        PMTilesDataStoreFactory factory = new PMTilesDataStoreFactory();
        testCreateDataStore(factory, andorra);
        testCreateDataStore(factory, andorra.toString());
        testCreateDataStore(factory, andorra.toUri());
        testCreateDataStore(factory, andorra.toFile());
        testCreateDataStore(factory, andorra.toUri().toURL());
    }

    private void testCreateDataStore(PMTilesDataStoreFactory factory, Object uri) throws IOException {
        PMTilesDataStore store = factory.createDataStore(Map.of(URIP.key, uri));
        try {
            assertNotNull(store);
        } finally {
            if (store != null) store.dispose();
        }
    }

    @Test
    public void testDataStoreFactories() throws IOException {
        PMTilesDataStoreFactory factory = new PMTilesDataStoreFactory();

        PMTilesDataStore store = factory.createDataStore(Map.of(URIP.key, andorra));

        assertSame(factory, store.getDataStoreFactory());
        assertNotNull(store.getFeatureTypeFactory());
        assertNotNull(store.getFeatureFactory());
        assertNotNull(store.getFilterFactory());
        assertNotNull(store.getGeometryFactory());
    }

    @Test
    public void testDataStoreFactoryLookup() {
        assertTrue(Streams.stream(DataStoreFinder.getAvailableDataStores())
                .anyMatch(PMTilesDataStoreFactory.class::isInstance));
        assertTrue(Streams.stream(DataAccessFinder.getAvailableDataStores())
                .anyMatch(PMTilesDataStoreFactory.class::isInstance));
    }

    @Test
    public void testDataStoreLookup() throws IOException {
        DataStore store = DataStoreFinder.getDataStore(Map.of(URIP.key, andorra, NAMESPACEP.key, "pmtiles.io"));
        try {
            assertNotNull(store);
        } finally {
            if (store != null) store.dispose();
        }
    }
}
