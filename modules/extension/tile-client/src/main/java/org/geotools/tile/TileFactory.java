/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015-2017, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2010, Refractions Research Inc.
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
package org.geotools.tile;

import org.geotools.tile.impl.ZoomLevel;

/**
 * A TileFactory is responsible for finding and/or creating tiles for a given TileService and area.
 * Here we make no distinction whereas a requested tile is created or retrieved form an internal
 * cache.
 *
 * <p>When creating/finding a tile, the factory must know the zoom level for which the tile is
 * required. Also, a geographic position or, alternatively a reference tile, must be passed.
 *
 * @author Tobias Sauerwein
 * @author Ugo Taddei
 * @since 12
 */
public abstract class TileFactory {

    /**
     * Finds the tile for a service at the given position and zoom level.
     *
     * @param lon the longitude
     * @param lat the latitude
     * @param zoomLevel the zoom level
     * @param service the service
     * @return a tile
     */
    public abstract Tile findTileAtCoordinate(
            double lon, double lat, ZoomLevel zoomLevel, TileService service);

    /**
     * Gets the ZoomLevel (object) for a given zoom level integer.
     *
     * @param zoomLevel the zoom level
     * @param service the service
     * @return a zoom level
     */
    public abstract ZoomLevel getZoomLevel(int zoomLevel, TileService service);

    /**
     * Finds the tile for a service at the given position and zoom level, which is immediately to
     * the right of the passed tile.
     *
     * @param tile the reference tile
     */
    public abstract Tile findRightNeighbour(Tile tile, TileService service);

    /**
     * Finds the tile for a service at the given position and zoom level, which is immediately below
     * the the passed tile.
     *
     * @param tile the reference tile
     */
    public abstract Tile findLowerNeighbour(Tile tile, TileService service);

    /**
     * Some clients, e.g. uDig, may produce numbers like -210° for the longitude, but we need a
     * number in the range -180 to 180, so instead of -210 we want 150.
     *
     * @param value the number to normalize (e.g. -210)
     * @param maxValue the maximum value (e.g. 180 -> the range is: -180..180)
     * @return a number between (-maxvalue) and maxvalue
     */
    public static double normalizeDegreeValue(double value, int maxValue) {
        int range = 2 * maxValue;

        if (value > 0) {

            value = (value + maxValue - 1) % range;

            if (value < 0) {
                value += range;
            }

            return (value - maxValue + 1);
        } else {
            value = (value + maxValue) % range;

            if (value < 0) {
                value += range;
            }

            return (value - maxValue);
        }
    }
}
