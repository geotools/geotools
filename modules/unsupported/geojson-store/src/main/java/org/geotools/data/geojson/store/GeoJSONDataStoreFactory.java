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
package org.geotools.data.geojson.store;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.RenderingHints.Key;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.geotools.data.DataStore;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.KVP;
import org.geotools.util.URLs;

public class GeoJSONDataStoreFactory implements FileDataStoreFactorySpi {

    private static final String[] EXTENSIONS = {"geojson", "json", "gjson"};
    private Boolean isAvailable;

    /** Parameter description of information required to connect */
    public static final Param FILE_PARAM =
            new Param(
                    "file", File.class, "GeoJSON file", false, null, new KVP(Param.EXT, "geojson"));

    public static final Param URL_PARAM =
            new Param("url", URL.class, "GeoJSON URL", false, null, new KVP(Param.EXT, "geojson"));
    public static final Param BOUNDING_BOX =
            new Param(
                    "bbox",
                    ReferencedEnvelope.class,
                    "A bounding box for the features to be written",
                    false);
    public static final Param WRITE_BOUNDS =
            new Param(
                    "bounds",
                    Boolean.class,
                    "Should a bounding box be written out if available",
                    false);
    public static final Param QUICK_SCHEMA =
            new Param(
                    "quick",
                    Boolean.class,
                    "Should the schema be described by the first element of the collection (Default true)",
                    false);

    public GeoJSONDataStoreFactory() {}

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
    public DataStore createDataStore(Map<String, ?> params) throws IOException {
        URL url = (URL) URL_PARAM.lookUp(params);
        File file = (File) FILE_PARAM.lookUp(params);
        if (url == null && file == null) {
            throw new IOException("No file or url parameter provided");
        }
        GeoJSONDataStore ret;
        if (file != null) {
            ret = new GeoJSONDataStore(file);
        } else {
            ret = new GeoJSONDataStore(url);
        }

        Boolean bounds = (Boolean) WRITE_BOUNDS.lookUp(params);
        if (bounds != null) {
            ret.setWriteBounds(bounds);
        }
        ReferencedEnvelope bbox = (ReferencedEnvelope) BOUNDING_BOX.lookUp(params);
        if (bbox != null) {
            ret.setBbox(bbox);
        }
        Boolean quick = (Boolean) QUICK_SCHEMA.lookUp(params);
        if (quick != null) {
            ret.setQuickSchema(quick);
        }
        return ret;
    }

    @Override
    public DataStore createNewDataStore(Map<String, ?> params) throws IOException {
        URL url = (URL) URL_PARAM.lookUp(params);
        File file = (File) FILE_PARAM.lookUp(params);
        if (url == null && file == null) {
            throw new IOException("No file or url parameter provided");
        }
        if (url != null && "file".equalsIgnoreCase(url.getProtocol())) {
            file = URLs.urlToFile(url);
        }
        GeoJSONDataStore ret;
        if (file != null) {
            if (!file.exists()) {
                boolean ok = file.createNewFile();
                if (!ok) {
                    throw new IOException("Unable to create file " + file.getAbsoluteFile());
                }
            }
            ret = new GeoJSONDataStore(file);
        } else {
            ret = new GeoJSONDataStore(url);
        }

        Boolean bounds = (Boolean) WRITE_BOUNDS.lookUp(params);
        if (bounds != null) {
            ret.setWriteBounds(bounds);
        }
        ReferencedEnvelope bbox = (ReferencedEnvelope) BOUNDING_BOX.lookUp(params);
        if (bbox != null) {
            ret.setBbox(bbox);
        }
        Boolean quick = (Boolean) QUICK_SCHEMA.lookUp(params);
        if (quick != null) {
            ret.setQuickSchema(quick);
        }
        return ret;
    }

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

    @Override
    public Param[] getParametersInfo() {
        return new Param[] {FILE_PARAM, URL_PARAM};
    }

    @Override
    public String[] getFileExtensions() {
        return EXTENSIONS;
    }

    @Override
    public boolean canProcess(Map<String, ?> params) {

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
            for (String ext : EXTENSIONS) {
                if (name.endsWith(ext)) {
                    return true;
                }
            }
        } catch (IOException e) {
            // don't care
        }
        return false;
    }

    @Override
    public boolean canProcess(URL url) {
        final String s = url.toString().toLowerCase();
        String extension = s.substring(s.lastIndexOf(".") + 1);
        Set<String> set = (Set<String>) Stream.of(EXTENSIONS).collect(Collectors.toSet());

        return set.contains(extension);
    }

    @Override
    public FileDataStore createDataStore(URL url) {
        return new GeoJSONDataStore(url);
    }

    public FileDataStore createDataStore(File f) {
        return new GeoJSONDataStore(f);
    }

    @Override
    public String getTypeName(URL url) {
        return null;
    }
}
