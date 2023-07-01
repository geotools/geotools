/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.vectormosaic;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.data.DataStore;

/** Configuration for a vector mosaic. */
public class VectorMosaicGranule implements Serializable, Closeable {

    public static final String CONNECTION_PARAMETERS_DELEGATE_FIELD_DEFAULT = "params";
    public static final String GRANULE_TYPE_NAME = "type";
    public static final String GRANULE_ID_FIELD = "id";

    /** The feature type name */
    String name;

    String granuleTypeName;
    String params;
    DataStore dataStore;
    Properties connProperties;

    public static VectorMosaicGranule fromDelegateFeature(SimpleFeature delegateFeature) {
        VectorMosaicGranule config = new VectorMosaicGranule();
        config.params =
                (String) delegateFeature.getAttribute(CONNECTION_PARAMETERS_DELEGATE_FIELD_DEFAULT);
        if (delegateFeature.getAttribute(GRANULE_TYPE_NAME) != null) {
            config.granuleTypeName = (String) delegateFeature.getAttribute(GRANULE_TYPE_NAME);
        }
        return config;
    }

    public String getGranuleTypeName() {
        return granuleTypeName;
    }

    public void setGranuleTypeName(String granuleTypeName) {
        this.granuleTypeName = granuleTypeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public DataStore getDataStore() {
        return dataStore;
    }

    public void setDataStore(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public Properties getConnProperties() {
        return connProperties;
    }

    public void setConnProperties(Properties connProperties) {
        this.connProperties = connProperties;
    }

    @Override
    public void close() throws IOException {
        if (dataStore != null) {
            dataStore.dispose();
        }
    }
}
