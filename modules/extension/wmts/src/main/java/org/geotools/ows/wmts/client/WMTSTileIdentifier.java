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

import org.geotools.tile.TileIdentifier;
import org.geotools.tile.impl.ZoomLevel;

/**
 * A TileIdentifier locates a tile in the grid space of a given tile server by giving its column,
 * row and zoom level.
 *
 * @author ian
 * @author Emanuele Tajariol (etj at geo-solutions dot it)
 */
class WMTSTileIdentifier extends TileIdentifier {

    /**
     * create an identifier based on /layername/{TileMatrixSet}/{TileMatrix}/{TileCol}/{TileRow}.png
     */
    public WMTSTileIdentifier(int x, int y, ZoomLevel zoomLevel, String serviceName) {
        super(x, y, zoomLevel, serviceName);
    }

    @Override
    public String getId() {
        final String separator = "_";
        StringBuilder sb = createGenericCodeBuilder(separator);
        sb.insert(0, separator).insert(0, getServiceName());
        return sb.toString();
    }

    @Override
    public String getCode() {
        final String separator = "/";
        return createGenericCodeBuilder(separator).toString();
    }

    private StringBuilder createGenericCodeBuilder(final String separator) {
        StringBuilder sb = new StringBuilder(50);
        sb.append(getZ()).append(separator).append(getX()).append(separator).append(getY());

        return sb;
    }

    @Override
    public TileIdentifier getRightNeighbour() {
        int newX = getX() + 1;
        if (newX >= getZoomLevel().getMaxTilePerRowNumber()) return null;
        else return new WMTSTileIdentifier(newX, getY(), getZoomLevel(), getServiceName());
    }

    @Override
    public TileIdentifier getLowerNeighbour() {
        int newY = getY() + 1;
        if (newY >= ((WMTSZoomLevel) getZoomLevel()).getMaxTilePerColNumber()) return null;
        else return new WMTSTileIdentifier(getX(), newY, getZoomLevel(), getServiceName());
    }
}
