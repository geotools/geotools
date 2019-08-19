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

import org.geotools.data.ows.OperationType;

/**
 * Available WMTS Operations are listed in a Request element.
 *
 * <p>(Based on existing work by rgould for WMS service)
 *
 * @author ian
 * @author Emanuele Tajariol (etj at geo-solutions dot it)
 */
public class WMTSRequest {
    private OperationType getCapabilities;

    private OperationType getTile;

    public OperationType getGetCapabilities() {
        return getCapabilities;
    }

    public void setGetCapabilities(OperationType getCapabilities) {
        this.getCapabilities = getCapabilities;
    }

    public OperationType getGetTile() {
        return getTile;
    }

    public void setGetTile(OperationType getTile) {
        this.getTile = getTile;
    }
}
