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
package org.geotools.ows.wmts.client;

import org.geotools.ows.wmts.model.TileMatrix;
import org.geotools.tile.impl.ZoomLevel;

/**
 * Represents a TileMatrix in WMTS.
 *
 * @author ian
 * @author Emanuele Tajariol (etj at geo-solutions dot it)
 */
class WMTSZoomLevel extends ZoomLevel {

    private final WMTSTileService service;

    /** @param zoomLevel */
    public WMTSZoomLevel(int zoomLevel, WMTSTileService service) {
        // super(zoomLevel);
        this.service = service;
        setZoomLevel(zoomLevel);
        this.maxTilePerRowNumber = calculateMaxTilePerRowNumber(zoomLevel);
        this.maxTilePerColNumber = calculateMaxTilePerColNumber(zoomLevel);

        this.maxTileNumber = calculateMaxTileNumber();
    }

    @Override
    public int calculateMaxTilePerRowNumber(int zoomLevel) {
        TileMatrix matrix = service.getTileMatrix(zoomLevel);
        return matrix.getMatrixWidth();
    }

    @Override
    public int calculateMaxTilePerColNumber(int zoomLevel) {
        TileMatrix matrix = service.getTileMatrix(zoomLevel);
        return matrix.getMatrixHeight();
    }

    public String toString() {
        return "ZoomLevel [z:"
                + getZoomLevel()
                + " col: "
                + getMaxTilePerColNumber()
                + " rows:"
                + getMaxTilePerRowNumber()
                + "]";
    }
}
