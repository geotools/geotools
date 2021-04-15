/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.flatgeobuf;

import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.store.ContentDataStore;
import org.geotools.util.KVP;

public class FlatGeobufDataStoreFactory implements DataStoreFactorySpi {

    public static final Param URL_PARAM =
            new Param(
                    "url",
                    URL.class,
                    "The FlatGeobuf file or directory",
                    true,
                    null,
                    new KVP(Param.EXT, "fgb"));

    public static final Param NAMESPACE_PARAM =
            new Param(
                    "namespace",
                    URI.class,
                    "uri to a the namespace",
                    false,
                    null, // not required
                    new KVP(Param.LEVEL, "advanced"));

    public FlatGeobufDataStoreFactory() {}

    @Override
    public DataStore createDataStore(Map<String, ?> map) throws IOException {
        URL url = (URL) URL_PARAM.lookUp(map);
        URI namespace = (URI) NAMESPACE_PARAM.lookUp(map);
        File file = FlatGeobufDataStore.getFile(url);

        ContentDataStore store;
        if (file != null && file.isDirectory()) {
            store = new FlatGeobufDirectoryDataStore(file);
        } else {
            store = new FlatGeobufDataStore(url);
        }
        if (namespace != null) {
            store.setNamespaceURI(namespace.toString());
        }
        return store;
    }

    @Override
    public DataStore createNewDataStore(Map<String, ?> map) throws IOException {
        return createDataStore(map);
    }

    @Override
    public String getDisplayName() {
        return "FlatGeobuf";
    }

    @Override
    public String getDescription() {
        return "A DataStore for reading and writing FlatGeobuf files";
    }

    @Override
    public Param[] getParametersInfo() {
        return new Param[] {URL_PARAM, NAMESPACE_PARAM};
    }

    @Override
    public boolean canProcess(Map<String, ?> map) {
        try {
            URL url = (URL) URL_PARAM.lookUp(map);
            File file = FlatGeobufDataStore.getFile(url);
            if (file != null) {
                return file.isDirectory() || file.getPath().toLowerCase().endsWith(".fgb");
            } else {
                return url.toString().endsWith(".fgb");
            }
        } catch (IOException e) {
            // ignore as we are expected to return true or false
        }
        return false;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public Map<RenderingHints.Key, ?> getImplementationHints() {
        return null;
    }
}
