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

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import org.geotools.data.mongodb.data.SchemaStoreDirectory;
import org.geotools.http.HTTPClient;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

/** @author tkunicki@boundlessgeo.com */
public class MongoUtil {

    public static Object getDBOValue(DBObject dbo, String path) {
        return getDBOValue(dbo, Arrays.asList(path.split("\\.")).iterator());
    }

    public static Object getDBOValue(DBObject dbo, Iterator<String> path) {
        return getDBOValueInternal(path, dbo);
    }

    private static Object getDBOValueInternal(Iterator<String> path, Object current) {
        if (path.hasNext()) {
            if (current instanceof DBObject) {
                String key = path.next();
                // If we are in an array, key must be an int
                if (current instanceof BasicDBList) {
                    try {
                        Integer.parseInt(key);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
                Object value = ((DBObject) current).get(key);
                return getDBOValueInternal(path, value);
            }
            return null;
        } else {
            return current;
        }
    }

    public static void setDBOValue(DBObject dbo, String path, Object value) {
        setDBOValue(dbo, Arrays.asList(path.split("\\.")).iterator(), value);
    }

    public static void setDBOValue(DBObject dbo, Iterator<String> path, Object value) {
        setDBOValueInternal(dbo, path, value);
    }

    private static void setDBOValueInternal(
            DBObject currentDBO, Iterator<String> path, Object value) {
        String key = path.next();
        if (path.hasNext()) {
            Object next = currentDBO.get(key);
            DBObject nextDBO;
            if (next instanceof DBObject) {
                nextDBO = (DBObject) next;
            } else {
                currentDBO.put(key, nextDBO = new BasicDBObject());
            }
            setDBOValueInternal(nextDBO, path, value);
        } else {
            currentDBO.put(key, value);
        }
    }

    public static Set<String> findIndexedGeometries(DBCollection dbc) {
        return findIndexedFields(dbc, "2dsphere");
    }

    public static Set<String> findIndexedFields(DBCollection dbc) {
        return findIndexedFields(dbc, null);
    }

    public static Set<String> findIndexedFields(DBCollection dbc, String type) {
        Set<String> fields = new LinkedHashSet<>();
        List<DBObject> indices = dbc.getIndexInfo();
        for (DBObject index : indices) {
            Object key = index.get("key");
            if (key instanceof DBObject) {
                for (Map.Entry<?, ?> entry : ((Map<?, ?>) ((DBObject) key).toMap()).entrySet()) {
                    if (type == null || type.equals(entry.getValue())) {
                        fields.add(entry.getKey().toString());
                    }
                }
            }
        }
        fields.remove("_id");
        return fields;
    }

    public static Map<String, Class<?>> findMappableFields(DBCollection dbc) {
        return findMappableFields(dbc.findOne());
    }

    public static Map<String, Class<?>> findMappableFields(DBObject dbo) {
        if (dbo == null) {
            return Collections.emptyMap();
        }
        Map<String, Class<?>> map = doFindMappableFields(dbo);
        map.remove("_id");
        return map;
    }

    private static Map<String, Class<?>> doFindMappableFields(DBObject dbo) {
        if (dbo == null) {
            return Collections.emptyMap();
        }
        Map<String, Class<?>> map = new LinkedHashMap<>();
        for (Map.Entry<?, ?> e : ((Map<?, ?>) dbo.toMap()).entrySet()) {
            Object k = e.getKey();
            if (k instanceof String) {
                String field = (String) k;
                Object v = e.getValue();
                if (v instanceof DBObject) {
                    // No list support
                    if (!(v instanceof BasicDBList)) {
                        for (Map.Entry<String, Class<?>> childEntry :
                                doFindMappableFields((DBObject) v).entrySet()) {
                            map.put(field + "." + childEntry.getKey(), childEntry.getValue());
                        }
                    }
                } else if (!(v instanceof List)) {
                    // this is here as documentation/placeholder.  no array/list support yet.
                    Class<?> binding = mapBSONObjectToJavaType(v);
                    if (binding != null) {
                        map.put(field, binding);
                    }
                }
            }
        }
        return map;
    }

    public static Class<?> mapBSONObjectToJavaType(Object o) {
        if (o instanceof String
                || o instanceof Double
                || o instanceof Long
                || o instanceof Integer
                || o instanceof Boolean
                || o instanceof Date) {
            return o.getClass();
        }
        return null;
    }

    public static String extractFilesNameFromUrl(String url) throws MalformedURLException {
        final URL urlObject = new URL(url);
        String path = urlObject.getPath();
        int lastSeparatorIndex = path.lastIndexOf("/");
        return path.substring(lastSeparatorIndex > -1 ? lastSeparatorIndex + 1 : 0, path.length());
    }

    public static boolean isZipFile(File file) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            long n = raf.readInt();
            raf.close();
            return n == 0x504B0304;
        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
            return false;
        }
    }

    public static boolean isZipFile(String uri) throws IOException, URISyntaxException {
        File file = new File(new URI(uri));
        return isZipFile(file);
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

    static SimpleFeatureType getSimpleFeatureType(BufferedReader reader, Name name)
            throws IOException {
        try {
            String lineSeparator = System.getProperty("line.separator");
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
                jsonBuilder.append(lineSeparator);
            }
            BasicDBObject o = BasicDBObject.parse(jsonBuilder.toString());
            return FeatureTypeDBObject.convert(o, name);
        } finally {
            reader.close();
        }
    }

    static File downloadSchemaFile(
            String storeName,
            URL url,
            HTTPClient httpClient,
            SchemaStoreDirectory downloadDirectory)
            throws IOException {
        File downloadDir = new File(downloadDirectory.getDirectory(), storeName);
        MongoUtil.validateDirectory(downloadDir);
        httpClient.setTryGzip(true);
        try (InputStream in = httpClient.get(url).getResponseStream()) {
            Logger.getGlobal()
                    .info(
                            "MongoDBStore:"
                                    + storeName
                                    + ":Downloading Schema File from :"
                                    + url.toExternalForm());

            // create file in temp with name of store
            String filesName = MongoUtil.extractFilesNameFromUrl(url.toExternalForm());
            File schemaStoreFile = new File(downloadDir, filesName);

            Files.copy(in, schemaStoreFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Logger.getGlobal()
                    .info(
                            "MongoDBStore:"
                                    + storeName
                                    + ":Downloaded File Stored at :"
                                    + schemaStoreFile.getAbsolutePath());
            return schemaStoreFile;
        }
    }

    public static File extractZipFile(File destDir, File zipFile) throws ZipException, IOException {

        int BUFFER = 2048;
        String extractFolderName = zipFile.getName();
        int idx = extractFolderName.lastIndexOf(MongoSchemaFileStore.SUFFIX_ZIP);
        extractFolderName = extractFolderName.substring(0, idx);
        File extractLocation = new File(destDir, extractFolderName);
        extractLocation.mkdir();
        try (ZipFile zip = new ZipFile(zipFile)) {
            Enumeration zipFileEntries = zip.entries();
            // Process each entry
            while (zipFileEntries.hasMoreElements()) {
                // grab a zip file entry
                ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
                String currentEntry = entry.getName();
                File destFile = new File(extractLocation, currentEntry);
                // destFile = new File(newPath, destFile.getName());
                File destinationParent = destFile.getParentFile();

                // create the parent directory structure if needed
                destinationParent.mkdirs();

                if (!entry.isDirectory()) {
                    try (BufferedInputStream is =
                            new BufferedInputStream(zip.getInputStream(entry))) {
                        int currentByte;
                        // establish buffer for writing file
                        byte data[] = new byte[BUFFER];

                        // write the current file to disk
                        FileOutputStream fos = new FileOutputStream(destFile);
                        try (BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER)) {
                            // read and write until last byte is encountered
                            while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                                dest.write(data, 0, currentByte);
                            }
                        }
                    }
                }
            }
        }
        Files.deleteIfExists(zipFile.toPath());

        SchemaFolderFinder jsonFolderFinder = new SchemaFolderFinder();
        Files.walkFileTree(extractLocation.toPath(), jsonFolderFinder);
        return jsonFolderFinder.getLastDirectory();
    }

    public static class SchemaFolderFinder implements FileVisitor<Path> {

        private File lastDirectory = null;

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (file.toString().endsWith(MongoSchemaFileStore.SUFFIX_json.toLowerCase())) {
                Path parent = file.getParent();
                if (parent != null) {
                    lastDirectory = parent.toFile();
                    return FileVisitResult.TERMINATE;
                }
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        public File getLastDirectory() {
            return lastDirectory;
        }
    }
}
