/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

import com.mongodb.util.JSON;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.geotools.data.ows.HTTPClient;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

/**
 * @author ImranR
 *     <p>This classes provides implementation of MongoSchemaStore for working with Zip Archives
 */
public class MongoSchemaZipFileStore implements MongoSchemaStore {

    static final String SUFFIX_zip = ".zip";

    final File schemaStoreFile;

    public MongoSchemaZipFileStore(String zipFile) throws URISyntaxException {
        this(new URI(zipFile));
    }

    public MongoSchemaZipFileStore(URI zipFile) {
        schemaStoreFile = new File(zipFile);
    }

    public MongoSchemaZipFileStore(String storeName, URL url, HTTPClient httpClient)
            throws IOException {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        try (InputStream in = httpClient.get(url).getResponseStream()) {

            Logger.getGlobal()
                    .info(
                            "MongoDBStore:"
                                    + storeName
                                    + ":Downloading Schema File from :"
                                    + url.toExternalForm());
            // create file in temp with name of store
            String filesName = MongoUtil.extractFilesNameFromUrl(url.toExternalForm());
            schemaStoreFile = new File(tempDir, filesName);

            Files.copy(in, schemaStoreFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Logger.getGlobal()
                    .info(
                            "MongoDBStore:"
                                    + storeName
                                    + ":Downloaded File Stored at :"
                                    + schemaStoreFile.getAbsolutePath());
        }
    }

    @Override
    public void storeSchema(SimpleFeatureType schema) throws IOException {

        if (schema == null) {
            return;
        }
        // create json schema from schema
        File tempSchemaFile =
                Files.createTempFile(schema.getTypeName(), MongoSchemaFileStore.SUFFIX_json)
                        .toFile();

        // store schema in a temp file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempSchemaFile))) {
            writer.write(JSON.serialize(FeatureTypeDBObject.convert(schema)));
        }
        final String newZippedFileName = schema.getTypeName() + MongoSchemaFileStore.SUFFIX_json;

        Map<String, String> zip_properties = new HashMap<>();
        zip_properties.put("create", "false");

        URI zip_disk = URI.create("jar:" + schemaStoreFile.toURI());
        // temp file in zip file
        try (FileSystem zipfs = FileSystems.newFileSystem(zip_disk, zip_properties)) {
            Path pathInZipfile = zipfs.getPath(newZippedFileName);
            // Add file to archive
            Files.copy(tempSchemaFile.toPath(), pathInZipfile);
        } finally {
            tempSchemaFile.delete();
        }
    }

    @Override
    public SimpleFeatureType retrieveSchema(Name name) throws IOException {
        if (name == null) {
            return null;
        }
        if (!typeNames().contains(name.getLocalPart())) return null;

        ZipEntry zippedJsonFile = getSchemaFile(name.getLocalPart());

        try (ZipFile schemaStoreZipFile = new ZipFile(schemaStoreFile)) {
            InputStream is = schemaStoreZipFile.getInputStream(zippedJsonFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            // use default implementation
            return MongoSchemaStore.super.getSimpleFeatureType(reader, name);
        }
    }

    @Override
    public void deleteSchema(Name name) throws IOException {
        // not creating new zip
        Map<String, String> zip_properties = new HashMap<>();
        zip_properties.put("create", "false");

        // get the schema file to delete
        ZipEntry zipEntry = getSchemaFile(name.getLocalPart());
        if (zipEntry == null)
            throw new IOException(
                    "Unable to find " + name.getLocalPart() + " in " + schemaStoreFile.toURI());

        URI zip_disk = URI.create("jar:" + schemaStoreFile.toURI());
        // Path path = Paths.get(schemaStoreFile.toURI());
        try (FileSystem zipfs = FileSystems.newFileSystem(zip_disk, zip_properties)) {
            // Get the Path inside ZIP File to delete the ZIP Entry
            Path pathInZipfile = zipfs.getPath(zipEntry.getName());
            Logger.getGlobal()
                    .fine(
                            "About to delete an entry "
                                    + pathInZipfile.toUri()
                                    + " from "
                                    + zip_disk);
            // Execute Delete
            Files.delete(pathInZipfile);
            Logger.getGlobal().info("Deleted entry " + pathInZipfile.toUri() + " from " + zip_disk);
        }
    }

    @Override
    public List<String> typeNames() {
        List<String> typeNames = new ArrayList<String>();
        try (ZipFile schemaStoreZipFile = new ZipFile(schemaStoreFile)) {

            Enumeration<? extends ZipEntry> entries = schemaStoreZipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (!entry.isDirectory()) {
                    if (entry.getName().endsWith(MongoSchemaFileStore.SUFFIX_json)) {
                        String name = entry.getName();
                        name = name.substring(0, name.indexOf(MongoSchemaFileStore.SUFFIX_json));
                        typeNames.add(name);
                    }
                }
            }
        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
        }

        return typeNames;
    }

    private ZipEntry getSchemaFile(String name) {
        if (!typeNames().contains(name)) return null;
        try (ZipFile schemaStoreZipFile = new ZipFile(schemaStoreFile)) {
            return schemaStoreZipFile.getEntry(name + MongoSchemaFileStore.SUFFIX_json);
        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub

    }
}
