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

import org.geotools.tile.Tile;
import org.geotools.tile.TileIdentifier;
import org.geotools.tile.TileService;
import org.geotools.tile.impl.WebMercatorTileFactory;
import org.geotools.tile.impl.ZoomLevel;

/**
 * The tile factory for Bing.
 *
 * @author Ugo Taddei
 * @since 12
 */
class BingTileFactory extends WebMercatorTileFactory {

    @Override
    public Tile create(TileIdentifier identifier, TileService service) {
        return new BingTile(identifier, service);
    }

    @Override
    public Tile findTileAtCoordinate(
            double lon, double lat, ZoomLevel zoomLevel, TileService service) {

        return create(service.identifyTileAtCoordinate(lon, lat, zoomLevel), service);
    }

    @Override
    public Tile findRightNeighbour(Tile tile, TileService service) {
        return create(tile.getTileIdentifier().getRightNeighbour(), service);
    }

    @Override
    public Tile findLowerNeighbour(Tile tile, TileService service) {
        return create(tile.getTileIdentifier().getLowerNeighbour(), service);
    }
}
