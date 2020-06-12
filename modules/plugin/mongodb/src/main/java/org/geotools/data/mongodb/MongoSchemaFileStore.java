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

import com.mongodb.BasicDBObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

/** @author tkunicki@boundlessgeo.com */
public class MongoSchemaFileStore implements MongoSchemaStore {

    static final String SUFFIX_json = ".json";
    // covers http(s) also
    static final String PRE_FIX_HTTP = "http";

    static final String SUFFIX_ZIP = ".zip";

    protected File schemaStoreFile;

    public MongoSchemaFileStore(String uri) throws IOException, URISyntaxException {
        this(new URI(uri));
    }

    public MongoSchemaFileStore(URI uri) throws IOException {
        this(new File(uri));
    }

    public MongoSchemaFileStore(File file) throws IOException {
        schemaStoreFile = file;
        validateDirectory(schemaStoreFile);
    }

    @Override
    public void storeSchema(SimpleFeatureType schema) throws IOException {
        if (schema == null) {
            return;
        }
        File schemaFile = schemaFile(schema.getTypeName());
        BufferedWriter writer = new BufferedWriter(new FileWriter(schemaFile));
        try {
            BasicDBObject dbObject = FeatureTypeDBObject.convert(schema);
            writer.write(dbObject.toJson());
        } finally {
            writer.close();
        }
    }

    @Override
    public SimpleFeatureType retrieveSchema(Name name) throws IOException {
        if (name == null) {
            return null;
        }
        File schemaFile = schemaFile(name);
        if (!schemaFile.canRead()) {
            return null;
        }
        @SuppressWarnings("PMD.CloseResource") // closed in getSimpleFeatureType
        BufferedReader reader = new BufferedReader(new FileReader(schemaFile));
        return MongoUtil.getSimpleFeatureType(reader, name);
    }

    @Override
    public void deleteSchema(Name name) throws IOException {
        if (name == null) {
            return;
        }
        schemaFile(name).delete();
    }

    @Override
    public List<String> typeNames() {
        List<String> typeNames = new ArrayList<String>();
        File[] files = schemaStoreFile.listFiles(new SchemaFilter());
        if (files != null) {
            for (File schemaFile : files) {
                typeNames.add(typeName(schemaFile));
            }
        }
        return typeNames;
    }

    static String typeName(File schemaFile) {
        String typeName = schemaFile.getName();
        return typeName.substring(0, typeName.length() - SUFFIX_json.length());
    }

    File schemaFile(Name name) {
        return schemaFile(name.getLocalPart());
    }

    File schemaFile(String typeName) {
        return new File(schemaStoreFile, typeName + SUFFIX_json);
    }

    @Override
    public void close() {}

    private static class SchemaFilter implements FileFilter {
        @Override
        public boolean accept(File file) {
            return file.isFile() && file.getName().endsWith(SUFFIX_json);
        }
    }

    static void validateDirectory(File file) throws IOException {
        if (!file.exists() && !file.mkdirs()) {
            throw new IOException(
                    "Schema store directory does not exist and could not be created: "
                            + file.getAbsolutePath());
        }
        if (file.isDirectory()) {
            // File.canWrite() doesn't report as intended for directories on
            // certain platforms with certain permissions scenarios.  Will
            // instead we verify we can create a file then delete it.
            if (!File.createTempFile("test", ".tmp", file).delete()) {
                throw new IOException(
                        "Unable to write to schema store directory: " + file.getAbsolutePath());
            }
        } else {
            throw new IOException(
                    "Specified schema store directory exists but is not a directory: "
                            + file.getAbsolutePath());
        }
    }
}
