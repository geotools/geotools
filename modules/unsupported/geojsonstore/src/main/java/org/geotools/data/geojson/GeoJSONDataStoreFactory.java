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
package org.geotools.data.geojson;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.RenderingHints.Key;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.util.KVP;
import org.geotools.util.URLs;

public class GeoJSONDataStoreFactory implements FileDataStoreFactorySpi {

    private static final String[] EXTENSIONS = new String[] {"geojson", "json", "gjson"};

    public GeoJSONDataStoreFactory() {
        // TODO Auto-generated constructor stub
    }

    /** No implementation hints required at this time */
    @Override
    public Map<Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }

    @Override
    public String getDisplayName() {
        return "GeoJSON";
    }

    @Override
    public String getDescription() {
        return "GeoJSON file or URL";
    }

    @Override
    public DataStore createDataStore(Map<String, Serializable> params) throws IOException {
        URL url = (URL) URL_PARAM.lookUp(params);
        File file = (File) FILE_PARAM.lookUp(params);
        if (url == null && file == null) {
            throw new IOException("No file or url parameter provided");
        }
        if (file != null) {
            return new GeoJSONDataStore(file);
        } else {
            return new GeoJSONDataStore(url);
        }
    }

    @Override
    public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException {
        URL url = (URL) URL_PARAM.lookUp(params);
        File file = (File) FILE_PARAM.lookUp(params);
        if (url == null && file == null) {
            throw new IOException("No file or url parameter provided");
        }
        if (url != null && "file".equalsIgnoreCase(url.getProtocol())) {
            file = URLs.urlToFile(url);
        }
        if (file != null) {
            if (!file.exists()) {
                file.createNewFile();
            }
            return new GeoJSONDataStore(file);
        } else {
            return new GeoJSONDataStore(url);
        }
    }

    Boolean isAvailable;

    @Override
    public synchronized boolean isAvailable() {
        if (isAvailable == null) {
            try {
                @SuppressWarnings("PMD.UnusedLocalVariable")
                Class geoJSONReaderType =
                        Class.forName("com.fasterxml.jackson.databind.ObjectMapper");
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JtsModule());
                isAvailable = true;
            } catch (ClassNotFoundException e) {
                isAvailable = false;
            }
        }
        return isAvailable;
    }

    /** Parameter description of information required to connect */
    public static final Param FILE_PARAM =
            new Param(
                    "file", File.class, "GeoJSON file", false, null, new KVP(Param.EXT, "geojson"));

    public static final Param URL_PARAM =
            new Param("url", URL.class, "GeoJSON URL", false, null, new KVP(Param.EXT, "geojson"));

    @Override
    public Param[] getParametersInfo() {
        return new Param[] {FILE_PARAM, URL_PARAM};
    }

    @Override
    public String[] getFileExtensions() {

        return EXTENSIONS;
    }

    @Override
    public boolean canProcess(Map<String, Serializable> params) {

        try {
            URL url = (URL) URL_PARAM.lookUp(params);

            File file = (File) FILE_PARAM.lookUp(params);
            if (file == null && url == null) {
                return false;
            }
            String name;
            if (file != null) {
                name = file.getPath().toLowerCase();

            } else {
                name = url.getPath().toLowerCase();
            }
            if (file != null) {
                for (String ext : EXTENSIONS) {
                    if (name.endsWith(ext)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            // don't care
        }
        return false;
    }

    @Override
    public boolean canProcess(URL url) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public FileDataStore createDataStore(URL url) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTypeName(URL url) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }
}
