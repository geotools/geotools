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
package org.geotools.data.wmts.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ian
 *
 */
public class TileMatrixSetLink {
    String identifier = "";
    List<TileMatrixLimits> limits = new ArrayList<>();
    /**
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }
    /**
     * @param identifier the identifier to set
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    /**
     * @return the limits
     */
    public List<TileMatrixLimits> getLimits() {
        return limits;
    }
    /**
     * @param limits the limits to set
     */
    public void setLimits(List<TileMatrixLimits> limits) {
        this.limits = limits;
    }
    
    public void addLimit(TileMatrixLimits limit) {
        limits.add(limit);
    }
}
