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

import java.io.Serializable;
import java.util.Properties;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.Filter;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;

/** Configuration for a vector mosaic. */
public class VectorMosaicGranule implements Serializable {

    public static final String CONNECTION_PARAMETERS_DELEGATE_FIELD_DEFAULT = "params";
    public static final String GRANULE_TYPE_NAME = "type";

    public static final String GRANULE_FILTER = "filter";
    public static final String GRANULE_ID_FIELD = "id";

    public static final String[] GRANULE_CONFIG_FIELDS = {
        CONNECTION_PARAMETERS_DELEGATE_FIELD_DEFAULT, GRANULE_FILTER, GRANULE_TYPE_NAME
    };

    /** The feature type name */
    String name;

    String granuleTypeName;
    String params;
    Properties connProperties;

    String storeName;

    Filter filter;

    public static VectorMosaicGranule fromDelegateFeature(SimpleFeature delegateFeature) {
        VectorMosaicGranule config = new VectorMosaicGranule();
        config.params = (String) delegateFeature.getAttribute(CONNECTION_PARAMETERS_DELEGATE_FIELD_DEFAULT);
        if (delegateFeature.getAttribute(GRANULE_TYPE_NAME) != null) {
            config.granuleTypeName = (String) delegateFeature.getAttribute(GRANULE_TYPE_NAME);
        }
        String filterAttribute = (String) delegateFeature.getAttribute(GRANULE_FILTER);
        if (filterAttribute != null && !filterAttribute.isEmpty()) {
            try {
                config.filter = ECQL.toFilter(filterAttribute);
            } catch (CQLException e) {
                throw new IllegalArgumentException("Failed to parse filter: " + filterAttribute);
            }
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

    public Properties getConnProperties() {
        return connProperties;
    }

    public void setConnProperties(Properties connProperties) {
        this.connProperties = connProperties;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
