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
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        Set<String> fields = new LinkedHashSet<String>();
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
        Map<String, Class<?>> map = new LinkedHashMap<String, Class<?>>();
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

    public static String extractFilesNameFromUrl(String url) {
        return url.substring(url.lastIndexOf('/') + 1, url.length());
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
}
