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

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;

public class MongoDataStoreFactory implements DataStoreFactorySpi {

    public static final Param NAMESPACE =
            new Param("namespace", String.class, "Namespace prefix", false);
    public static final Param DATASTORE_URI =
            new Param(
                    "data_store",
                    String.class,
                    "MongoDB URI",
                    true,
                    "mongodb://localhost/<database name>");
    public static final Param SCHEMASTORE_URI =
            new Param(
                    "schema_store",
                    String.class,
                    "Schema Store URI",
                    true,
                    "file://<absolute path>");
    public static final Param MAX_OBJECTS_FOR_SCHEMA =
            new Param(
                    "max_objs_schema",
                    Integer.class,
                    "Max objects for schema generation",
                    false,
                    1);
    public static final Param OBJECTS_IDS_FOR_SCHEMA =
            new Param(
                    "objs_id_schema",
                    String.class,
                    "Objects IDs for schema generation (comma separated)",
                    false,
                    null);

    @Override
    public String getDisplayName() {
        return "MongoDB";
    }

    @Override
    public String getDescription() {
        return "MongoDB database";
    }

    @Override
    public Param[] getParametersInfo() {
        return new Param[] {
            NAMESPACE,
            DATASTORE_URI,
            SCHEMASTORE_URI,
            MAX_OBJECTS_FOR_SCHEMA,
            OBJECTS_IDS_FOR_SCHEMA,
        };
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public MongoDataStore createDataStore(Map<String, Serializable> params) throws IOException {
        // retrieve schema generation parameters
        final List<String> ids = getIds(params);
        final Integer maxObjects = (Integer) MAX_OBJECTS_FOR_SCHEMA.lookUp(params);
        final MongoSchemaInitParams schemaParams =
                MongoSchemaInitParams.builder()
                        .ids(ids.toArray(new String[] {}))
                        .maxObjects(maxObjects != null ? maxObjects : 1)
                        .build();
        // instance datastore
        MongoDataStore dataStore =
                new MongoDataStore(
                        (String) DATASTORE_URI.lookUp(params),
                        (String) SCHEMASTORE_URI.lookUp(params),
                        true,
                        schemaParams);
        String uri = (String) NAMESPACE.lookUp(params);
        if (uri != null) {
            dataStore.setNamespaceURI(uri);
        }
        return dataStore;
    }

    private List<String> getIds(Map<String, Serializable> params) throws IOException {
        List<String> ids = new ArrayList<>();
        Object ofs = OBJECTS_IDS_FOR_SCHEMA.lookUp(params);
        // if null, there are not ids to parse
        if (ofs == null) return ids;
        // type checking
        if (!(ofs instanceof String)) {
            throw new IllegalArgumentException("Object Ids parameter should be String type.");
        }
        String idsStr = (String) ofs;
        String[] parts = idsStr.split(",");
        for (String epart : parts) {
            String id = epart.trim();
            if (StringUtils.isNotEmpty(id)) ids.add(id);
        }
        return ids;
    }

    @Override
    public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException {
        throw new UnsupportedOperationException();
    }
}
