/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.wfs.internal.v2_0.storedquery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StoredQueryConfiguration implements Serializable {
    private String storedQueryId;

    private List<ParameterMapping> storedQueryParameterMappings = new ArrayList<ParameterMapping>();

    public void setStoredQueryId(String storedQueryId) {
        this.storedQueryId = storedQueryId;
    }

    public String getStoredQueryId() {
        return storedQueryId;
    }

    public List<ParameterMapping> getStoredQueryParameterMappings() {
        return storedQueryParameterMappings;
    }

    public void setStoredQueryParameterMappings(
            List<ParameterMapping> storedQueryParameterMappings) {
        this.storedQueryParameterMappings = storedQueryParameterMappings;
    }
}
