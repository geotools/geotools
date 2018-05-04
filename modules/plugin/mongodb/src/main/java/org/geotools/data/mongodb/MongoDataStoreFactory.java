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
import java.util.Map;
import org.geotools.data.AbstractDataStoreFactory;
import org.geotools.data.DataStore;

public class MongoDataStoreFactory extends AbstractDataStoreFactory {

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
        return new Param[] {NAMESPACE, DATASTORE_URI, SCHEMASTORE_URI};
    }

    @Override
    public MongoDataStore createDataStore(Map<String, Serializable> params) throws IOException {
        MongoDataStore dataStore =
                new MongoDataStore(
                        (String) DATASTORE_URI.lookUp(params),
                        (String) SCHEMASTORE_URI.lookUp(params));
        String uri = (String) NAMESPACE.lookUp(params);
        if (uri != null) {
            dataStore.setNamespaceURI(uri);
        }
        return dataStore;
    }

    @Override
    public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException {
        throw new UnsupportedOperationException();
    }
}
