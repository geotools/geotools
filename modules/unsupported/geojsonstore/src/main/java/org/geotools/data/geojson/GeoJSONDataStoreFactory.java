package org.geotools.data.geojson;
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.util.KVP;

public class GeoJSONDataStoreFactory implements DataStoreFactorySpi {

    public static final String[] EXTS = {"geojson", "json"};
    public static final Param URLP =
            new Param(
                    "url",
                    URL.class,
                    "url to a .json file",
                    true,
                    null,
                    new KVP(Param.EXT, "json", Param.EXT, "geojson"));

    /** Confirm DataStore availability, null if unknown */
    Boolean isAvailable = null;

    public GeoJSONDataStoreFactory() {
        super();
    }

    @Override
    public String getDisplayName() {
        return "GeoJSON";
    }

    @Override
    public String getDescription() {
        return "GeoJSON files and URLsi containing feature collections.";
    }

    @Override
    public Param[] getParametersInfo() {
        return new Param[] {URLP};
    }

    @Override
    public boolean canProcess(Map<String, Serializable> params) {
        URL url;
        try {
            url = (URL) URLP.lookUp(params);
            if (url != null) {
                for (String ext : EXTS) {
                    if (FilenameUtils.getExtension(url.toExternalForm()).equalsIgnoreCase(ext)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            // in fact we don't really care as we are merely testing
        }
        return false;
    }

    @Override
    /**
     * Test to see if this DataStore is available, for example if it has all the appropriate
     * libraries to construct an instance.
     *
     * <p>This method is used for interactive applications, so as to not advertise support for
     * formats that will not function.
     *
     * @return <tt>true</tt> if and only if this factory is available to create DataStores.
     */
    public synchronized boolean isAvailable() {
        if (isAvailable == null) {
            try {
                Class geoJSONReaderType = Class.forName("org.geotools.geojson.feature.FeatureJSON");
                isAvailable = true;
            } catch (ClassNotFoundException e) {
                isAvailable = false;
            }
        }
        return isAvailable;
    }

    @Override
    public Map<Key, ?> getImplementationHints() {
        // none required
        return Collections.emptyMap();
    }

    @Override
    public DataStore createDataStore(Map<String, Serializable> params) throws IOException {
        URL url = (URL) URLP.lookUp(params);
        return new GeoJSONDataStore(url);
    }

    @Override
    public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException {
        URL url = (URL) URLP.lookUp(params);
        // We can only really write to local files
        String scheme = url.getProtocol();
        String host = url.getHost();
        if ("file".equalsIgnoreCase(scheme) && (host == null || host.isEmpty())) {
            return new GeoJSONDataStore(url);
        } else {
            throw new IllegalArgumentException("unable to write to remote URLs");
        }
    }
}
