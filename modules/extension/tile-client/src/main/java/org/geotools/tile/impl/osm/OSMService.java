/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015-2017, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.tile.TileFactory;
import org.geotools.tile.TileIdentifier;
import org.geotools.tile.impl.WebMercatorTileService;
import org.geotools.tile.impl.ZoomLevel;

/**
 * The tile service for the OpenStreetMap family.
 *
 * @author Tobias Sauerwein
 * @author Ugo Taddei
 * @since 12
 */
public class OSMService extends WebMercatorTileService {

    private static final TileFactory tileFactory = new OSMTileFactory();

    private static double[] scaleList = {
        Double.NaN,
        Double.NaN,
        147914381,
        73957190,
        36978595,
        18489297,
        9244648,
        4622324,
        2311162,
        1155581,
        577790,
        288895,
        144447,
        72223,
        36111,
        18055,
        9027,
        4513,
        2256,
        1128,
        564,
        282,
        141,
        70
    };

    public OSMService(String name, String baseUrl) {
        super(name, baseUrl);
    }

    @Override
    public double[] getScaleList() {
        return scaleList;
    }

    @Override
    public TileFactory getTileFactory() {
        return tileFactory;
    }

    @Override
    public TileIdentifier identifyTileAtCoordinate(double lon, double lat, ZoomLevel zoomLevel) {
        lat = TileFactory.normalizeDegreeValue(lat, 90);
        lon = TileFactory.normalizeDegreeValue(lon, 180);

        /**
         * Because the latitude is only valid in 85.0511 °N to 85.0511 °S
         * (http://wiki.openstreetmap.org/wiki/Tilenames#X_and_Y), we have to correct if necessary.
         */
        lat =
                OSMTileFactory.moveInRange(
                        lat,
                        WebMercatorTileService.MIN_LATITUDE,
                        WebMercatorTileService.MAX_LATITUDE);

        int zoomPower = 1 << zoomLevel.getZoomLevel();
        int xTile = (int) Math.floor((lon + 180) / 360 * zoomPower);
        double latr = lat * Math.PI / 180;
        double yd = (1 - Math.log(Math.tan(latr) + 1 / Math.cos(latr)) / Math.PI) / 2 * zoomPower;
        int yTile = (int) Math.floor(yd);
        if (yTile < 0) yTile = 0;
        return new OSMTileIdentifier(xTile, yTile, zoomLevel, getName());
    }
}
