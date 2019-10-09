/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2015, Boundless
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
package org.geotools.data.mongodb;

import static org.geotools.data.mongodb.MongoSchemaFileStore.SUFFIX_json;
import static org.geotools.data.mongodb.MongoSchemaFileStore.typeName;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.io.IOUtils;
import org.geotools.data.mongodb.data.SchemaStoreDirectory;
import org.geotools.data.mongodb.data.SchemaStoreDirectoryProvider;
import org.geotools.feature.NameImpl;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;

/** @author tkunicki@boundlessgeo.com */
public class MongoSchemaFileStoreTest extends MongoSchemaStoreTest<MongoSchemaFileStore> {

    Map<MongoSchemaFileStore, File> directories = new HashMap<MongoSchemaFileStore, File>();

    static SchemaStoreDirectoryProvider direcotryProvider;

    @Override
    MongoSchemaFileStore createUniqueStore() throws IOException {
        File directory = createUniqueTempDirectory();
        MongoSchemaFileStore store = new MongoSchemaFileStore(directory);
        directories.put(store, directory);
        return store;
    }

    @Override
    void destroyUniqueStore(MongoSchemaFileStore store) {
        if (store != null) {
            File directory = directories.get(store);
            if (directory != null) {
                resursiveDelete(directory);
            }
        }
    }

    static File createUniqueTempDirectory() {
        return new File(
                new File(System.getProperty("java.io.tmpdir")), UUID.randomUUID().toString());
    }

    private static void resursiveDelete(File f) {
        File[] l = f.listFiles();
        if (l != null) {
            for (File c : l) {
                resursiveDelete(c);
            }
        }
        f.delete();
    }

    @Test
    public void test_Constructor_String() throws URISyntaxException, IOException {
        File test = createUniqueTempDirectory();
        try {
            assertThat(test.exists(), is(equalTo(false)));
            MongoSchemaStore mss = new MongoSchemaFileStore(test.toURI().toString());
            assertThat(test.exists(), is(equalTo(true)));
            assertThat(test.isDirectory(), is(equalTo(true)));
        } finally {
            test.delete();
        }
    }

    @Test
    public void test_Constructor_URI() throws IOException {
        File test = createUniqueTempDirectory();
        try {
            assertThat(test.exists(), is(equalTo(false)));
            MongoSchemaStore mss = new MongoSchemaFileStore(test.toURI());
            assertThat(test.exists(), is(equalTo(true)));
            assertThat(test.isDirectory(), is(equalTo(true)));
        } finally {
            test.delete();
        }
    }

    @Test
    public void test_Constructor_URL() throws Exception {
        final String mockFileURL = "https://mock.url.com/stations.json";

        MockHTTPClient mockHttpClient = new MockHTTPClient();
        byte[] responseBytes =
                IOUtils.toByteArray(getClass().getResourceAsStream("stations-schema.json"));
        MockHttpResponse mockResponse = new MockHttpResponse(responseBytes, "text/json");
        mockHttpClient.expectGet(mockFileURL, mockResponse);
        SchemaStoreDirectory directory = SchemaStoreDirectoryProvider.getLowestPriority();

        File downloadedFile =
                MongoUtil.downloadSchemaFile(
                        "mockshema", new URL(mockFileURL), mockHttpClient, directory);

        MongoSchemaStore mss = new MongoSchemaFileStore(downloadedFile.getParentFile().toURI());
        assertEquals(1, mss.typeNames().size());
        SimpleFeatureType type = mss.retrieveSchema(new NameImpl("stations"));
        assertNotNull(type);
        mss.deleteSchema(new NameImpl("stations"));
        // cleanup
        Files.walk(new File(directory.getDirectory(), "mockshema").toPath())
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    public void test_Constructor_ZIP_URL() throws Exception {
        final String mockFileURL = "https://mock.url.com/schemas.zip";

        MockHTTPClient mockHttpClient = new MockHTTPClient();
        byte[] responseBytes = IOUtils.toByteArray(getClass().getResourceAsStream("schemas.zip"));
        MockHttpResponse mockResponse = new MockHttpResponse(responseBytes, "application/zip");
        mockHttpClient.expectGet(mockFileURL, mockResponse);
        SchemaStoreDirectory directory = SchemaStoreDirectoryProvider.getLowestPriority();
        File downloadedFile =
                MongoUtil.downloadSchemaFile(
                        "mockshema", new URL(mockFileURL), mockHttpClient, directory);
        File extractedLocation =
                MongoUtil.extractZipFile(downloadedFile.getParentFile(), downloadedFile);

        MongoSchemaStore mss = new MongoSchemaFileStore(extractedLocation.toURI());
        // retreive test
        assertEquals(2, mss.typeNames().size());
        SimpleFeatureType type = mss.retrieveSchema(new NameImpl("stations"));
        assertNotNull(type);
        SimpleFeatureType type2 = mss.retrieveSchema(new NameImpl("stations2"));
        assertNotNull(type2);

        // delete test
        mss.deleteSchema(new NameImpl("stations2"));
        assertEquals(1, mss.typeNames().size());

        // store test
        mss.storeSchema(type2);
        assertEquals(2, mss.typeNames().size());
        // cleanup
        Files.walk(new File(directory.getDirectory(), "mockshema").toPath())
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    public void test_Constructor_ZIP_DIR_URL() throws Exception {
        final String mockFileURL = "https://mock.url.com/schemasdir.zip";

        MockHTTPClient mockHttpClient = new MockHTTPClient();
        byte[] responseBytes =
                IOUtils.toByteArray(getClass().getResourceAsStream("schemas_dir.zip"));
        MockHttpResponse mockResponse = new MockHttpResponse(responseBytes, "application/zip");
        mockHttpClient.expectGet(mockFileURL, mockResponse);
        SchemaStoreDirectory directory = SchemaStoreDirectoryProvider.getLowestPriority();
        File downloadedFile =
                MongoUtil.downloadSchemaFile(
                        "mockshema", new URL(mockFileURL), mockHttpClient, directory);
        File extractedLocation =
                MongoUtil.extractZipFile(downloadedFile.getParentFile(), downloadedFile);

        MongoSchemaStore mss = new MongoSchemaFileStore(extractedLocation.toURI());
        // retreive test
        assertEquals(2, mss.typeNames().size());
        SimpleFeatureType type = mss.retrieveSchema(new NameImpl("stations"));
        assertNotNull(type);
        SimpleFeatureType type2 = mss.retrieveSchema(new NameImpl("stations2"));
        assertNotNull(type2);

        // delete test
        mss.deleteSchema(new NameImpl("stations2"));
        assertEquals(1, mss.typeNames().size());

        // store test
        mss.storeSchema(type2);
        assertEquals(2, mss.typeNames().size());

        // cleanup
        Files.walk(new File(directory.getDirectory(), "mockshema").toPath())
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    public void test_Constructor_File() throws IOException {
        File test = createUniqueTempDirectory();
        try {
            assertThat(test.exists(), is(equalTo(false)));
            MongoSchemaStore mss = new MongoSchemaFileStore(test);
            assertThat(test.exists(), is(equalTo(true)));
            assertThat(test.isDirectory(), is(equalTo(true)));
        } finally {
            test.delete();
        }
    }

    @Test
    public void testTypeName() {
        assertThat(typeName(new File("testMe.json")), is(equalTo("testMe")));
        assertThat(typeName(new File("c:/testMe.json")), is(equalTo("testMe")));
        assertThat(
                typeName(
                        new File("/opt/tomcat/webapps/data/mongodb-schemas/teststore/testMe.json")),
                is(equalTo("testMe")));
    }

    @Test
    public void testSchemaFile_Name() throws IOException {
        MongoSchemaFileStore store = createUniqueStore();
        File directory = directories.get(store);
        File expected = new File(directory, "test" + SUFFIX_json);
        // method takes name but ignores URI and separator
        assertThat(store.schemaFile(new NameImpl("test")), is(equalTo(expected)));
        assertThat(store.schemaFile(new NameImpl("uri", "test")), is(equalTo(expected)));
        assertThat(store.schemaFile(new NameImpl("uri", "-", "test")), is(equalTo(expected)));
    }

    @Test
    public void testSchemaFile_String() throws IOException {
        MongoSchemaFileStore store = createUniqueStore();
        File directory = directories.get(store);
        File expected = new File(directory, "test" + SUFFIX_json);
        // method takes name but ignores URI and separator
        assertThat(store.schemaFile("test"), is(equalTo(expected)));
    }

    @Test
    public void test_validateDirectory_Exists() throws IOException {
        File tempDirectory = new File(System.getProperty("java.io.tmpdir"));
        MongoSchemaFileStore.validateDirectory(tempDirectory);
    }

    @Test
    public void test_validateDirectory_DoesNotExistAndNeedsCreation() throws IOException {
        File needsToBeCreated = createUniqueTempDirectory();
        try {
            assertThat(needsToBeCreated.exists(), is(equalTo(false)));
            MongoSchemaFileStore.validateDirectory(needsToBeCreated);
            assertThat(needsToBeCreated.isDirectory(), is(equalTo(true)));
            assertThat(needsToBeCreated.exists(), is(equalTo(true)));
        } finally {
            needsToBeCreated.delete();
        }
    }

    @Ignore // not platform independent
    @Test(expected = IOException.class)
    public void test_validateDirectory_CanNotWrite() throws IOException {
        File root = new File("/");
        MongoSchemaFileStore.validateDirectory(root);
    }

    @Ignore // not platform independent
    @Test(expected = IOException.class)
    public void test_validateDirectory_CanNotCreate() throws IOException {
        File canNotCreate = File.createTempFile(getClass().getSimpleName(), "test", new File("/"));
        try {
            MongoSchemaFileStore.validateDirectory(canNotCreate);
        } finally {
            canNotCreate.delete();
        }
    }

    @Test(expected = IOException.class)
    public void test_validateDirectory_ExistsAndIsNotADirectory() throws IOException {
        File fileThatExists = File.createTempFile(getClass().getSimpleName(), "test");
        try {
            MongoSchemaFileStore.validateDirectory(fileThatExists);
        } finally {
            fileThatExists.delete();
        }
    }
}
