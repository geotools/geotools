/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2008, Refractions Research Inc.
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
package org.geotools.tile.impl.osm;

import org.geotools.tile.Tile;
import org.geotools.tile.TileFactory;
import org.geotools.tile.TileService;
import org.geotools.tile.impl.WebMercatorTileFactory;
import org.geotools.tile.impl.ZoomLevel;

/**
 * The tile factory implementation for the OpenStreetMap family
 * 
 * @author Tobias Sauerwein
 * @author Ugo Taddei
 * @since 12
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/tile-client
 *         /src/main/java/org/geotools/tile/impl/osm/OSMTileFactory.java $
 */
public class OSMTileFactory extends WebMercatorTileFactory {

    public Tile findTileAtCoordinate(double lon, double lat,
            ZoomLevel zoomLevel, TileService service) {
        lat = TileFactory.normalizeDegreeValue(lat, 90);
        lon = TileFactory.normalizeDegreeValue(lon, 180);

        /**
         * Because the latitude is only valid in 85.0511 °N to 85.0511 °S
         * (http://wiki.openstreetmap.org/wiki/Tilenames#X_and_Y), we have to
         * correct if necessary.
         */
        lat = OSMTileFactory.moveInRange(lat, -85.0511, 85.0511);

        int xTile = (int) Math.floor((lon + 180) / 360
                * (1 << zoomLevel.getZoomLevel()));
        int yTile = (int) Math.floor((1 - Math.log(Math
                .tan(lat * Math.PI / 180) + 1 / Math.cos(lat * Math.PI / 180))
                / Math.PI)
                / 2 * (1 << zoomLevel.getZoomLevel()));

        return new OSMTile(xTile, yTile, zoomLevel, service);
    }

    /**
     * This method ensures that value is between min and max. If value < min,
     * min is returned. If value > max, max is returned. Otherwise value.
     *
     * @param value
     * @param min
     * @param max
     * @return
     */
    private static double moveInRange(double value, double min, double max) {
        if (value < min) {
            value = min;
        } else if (value > max) {
            value = max;
        }

        return value;
    }

    public Tile findRightNeighbour(Tile tile, TileService service) {
        return new OSMTile(tile.getTileIdentifier().getRightNeighbour(),
                service);
    }

    @Override
    public Tile findLowerNeighbour(Tile tile, TileService service) {
        return new OSMTile(tile.getTileIdentifier().getLowerNeighbour(),
                service);
    }

}
