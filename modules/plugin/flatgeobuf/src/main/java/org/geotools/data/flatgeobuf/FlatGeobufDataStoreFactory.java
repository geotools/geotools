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
package org.geotools.data.flatgeobuf;

import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.util.KVP;

public class FlatGeobufDataStoreFactory implements FileDataStoreFactorySpi {

    public static final Param URL_PARAM =
            new Param(
                    "url",
                    URL.class,
                    "url to a FlatGeobuf file",
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

    <T> T lookup(Param param, Map<String, ?> params, Class<T> target) throws IOException {
        T result = target.cast(param.lookUp(params));
        if (result == null) {
            result = target.cast(param.getDefaultValue());
        }
        return result;
    }

    @Override
    public DataStore createDataStore(Map<String, ?> map) throws IOException {
        URL url = lookup(URL_PARAM, map, URL.class);
        URI namespace = lookup(NAMESPACE_PARAM, map, URI.class);
        FlatGeobufDataStore store = new FlatGeobufDataStore(url);
        if (namespace != null) {
            store.setNamespaceURI(namespace.toString());
        }
        return store;
    }

    @Override
    public FileDataStore createDataStore(URL url) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put(URL_PARAM.key, url);
        return (FileDataStore) createDataStore(params);
    }

    @Override
    public String getTypeName(URL url) throws IOException {
        DataStore ds = createDataStore(url);
        String[] names = ds.getTypeNames(); // should be exactly one
        ds.dispose();
        return ((names == null || names.length == 0) ? null : names[0]);
    }

    @Override
    public DataStore createNewDataStore(Map<String, ?> map) throws IOException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public String getDisplayName() {
        return "FlatGeobuf";
    }

    @Override
    public String getDescription() {
        return "A DataStore for reading FlatGeobuf files";
    }

    @Override
    public Param[] getParametersInfo() {
        return new Param[] {URL_PARAM, NAMESPACE_PARAM};
    }

    @Override
    public boolean canProcess(Map<String, ?> map) {
        try {
            URL url = (URL) URL_PARAM.lookUp(map);
            if (url != null)
                return new File(url.toURI()).toPath().toString().toLowerCase().endsWith(".fgb");
        } catch (IOException | URISyntaxException e) {
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
        return Collections.emptyMap();
    }

    @Override
    public String[] getFileExtensions() {
        return new String[] {".fgb"};
    }

    @Override
    public boolean canProcess(URL url) {
        return url != null && url.getFile().toLowerCase().endsWith("fgb");
    }
}
