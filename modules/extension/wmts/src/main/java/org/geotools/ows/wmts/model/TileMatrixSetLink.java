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
package org.geotools.ows.wmts.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Tells which TileMatrixSet a layer is using.
 *
 * <p>Optionally defines a row/column subrange (TileMatrixLimits) for the layer in such
 * TileMatrixSet.
 *
 * @author ian
 * @author Emanuele Tajariol (etj at geo-solutions dot it)
 */
public class TileMatrixSetLink {

    String identifier = "";

    List<TileMatrixLimits> limits = new ArrayList<>();

    /** @return the identifier */
    public String getIdentifier() {
        return identifier;
    }

    /** @param identifier the identifier to set */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /** @return the limits */
    public List<TileMatrixLimits> getLimits() {
        return limits;
    }

    /** @param limits the limits to set */
    public void setLimits(List<TileMatrixLimits> limits) {
        this.limits = limits;
    }

    public void addLimit(TileMatrixLimits limit) {
        limits.add(limit);
    }
}
