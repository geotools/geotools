/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015-2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.tile.impl.bing;

import org.geotools.tile.TileIdentifier;
import org.geotools.tile.impl.ZoomLevel;

/**
 * The TileIdentifier implementation for the BingMaps family. This identifier follows the grid logic of similar
 * implementations. The different characteristic of a BingTileIdentifier is the use of quadkey to locate a tile in the
 * grid space. Please refer to <a href="https://msdn.microsoft.com/en-us/library/bb259689.aspx>Bing Maps Tile
 * System</a>.
 *
 * @author Ugo Taddei
 * @since 12
 */
public class BingTileIdentifier extends TileIdentifier {

    /** Creates a new BingTileIdentifier. */
    public BingTileIdentifier(int x, int y, ZoomLevel zoomLevel, String serviceName) {
        super(x, y, zoomLevel, serviceName);
    }

    @Override
    public BingTileIdentifier getRightNeighbour() {

        return new BingTileIdentifier(
                TileIdentifier.arithmeticMod(getX() + 1, getZoomLevel().getMaxTilePerRowNumber()),
                getY(),
                getZoomLevel(),
                getServiceName());
    }

    @Override
    public BingTileIdentifier getLowerNeighbour() {

        return new BingTileIdentifier(
                getX(),
                TileIdentifier.arithmeticMod(getY() + 1, getZoomLevel().getMaxTilePerRowNumber()),
                getZoomLevel(),
                getServiceName());
    }

    @Override
    public String getId() {
        return getServiceName() + "_" + getCode();
    }

    @Override
    public String getCode() {
        return BingTileUtil.tileXYToQuadKey(this.getX(), this.getY(), this.getZ());
    }
}
