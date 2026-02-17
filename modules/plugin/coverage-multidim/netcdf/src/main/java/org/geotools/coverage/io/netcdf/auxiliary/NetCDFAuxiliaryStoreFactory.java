/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.netcdf.auxiliary;

import java.awt.RenderingHints.Key;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFactorySpi;

/**
 * Creates a read-only vector store that exposes the NetCDF slices directly from the reader, without relying on an
 * external DB index.
 */
public class NetCDFAuxiliaryStoreFactory implements DataStoreFactorySpi {

    public static final String AUXILIARY_STORE_KEY = "org.geotools.coverage.io.netcdf.auxiliary.store";

    public static final Param FILE_PARAM =
            new Param("File", File.class, "NetCDF File Path", true, null, Collections.emptyMap());

    /** Kept only for backward compatibility with existing configs. Ignored. */
    public static final Param INDEX_PARAM =
            new Param("Index", String.class, "Deprecated - ignored", false, null, Collections.emptyMap());

    /** Kept only for backward compatibility with existing configs. Ignored. */
    public static final Param DS_PARAM =
            new Param("DataStore", String.class, "Deprecated - ignored", false, null, Collections.emptyMap());

    public static final Param NS_PARAM =
            new Param("namespace", String.class, "Namespace", false, null, Collections.emptyMap());

    @Override
    public String getDisplayName() {
        return "NetCDF Auxiliary Store";
    }

    @Override
    public String getDescription() {
        return "Published NetCDF Index Data";
    }

    @Override
    public Param[] getParametersInfo() {
        return new Param[] {FILE_PARAM, INDEX_PARAM, DS_PARAM, NS_PARAM};
    }

    @Override
    public boolean canProcess(Map<String, ?> params) {
        try {
            File file = (File) FILE_PARAM.lookUp(params);
            if (file == null || !file.isFile() || !file.canRead()) {
                return false;
            }

            // Keep this very lightweight for now.
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isAvailable() {
        return "true".equalsIgnoreCase(System.getProperty(AUXILIARY_STORE_KEY));
    }

    @Override
    public Map<Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }

    @Override
    public DataStore createDataStore(Map<String, ?> params) throws IOException {
        File file = (File) FILE_PARAM.lookUp(params);
        if (file == null) {
            throw new IOException("Missing required parameter: " + FILE_PARAM.key);
        }

        String namespace = (String) NS_PARAM.lookUp(params);
        return new NetCDFAuxiliaryDataStore(file, namespace);
    }

    @Override
    public DataStore createNewDataStore(Map<String, ?> params) throws IOException {
        throw new UnsupportedOperationException("NetCDF auxiliary store is read-only");
    }
}
