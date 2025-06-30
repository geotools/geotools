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
package org.geotools.tile.impl;

/**
 * The WebMercatorTileService is an abstract class that holds some of the tile service logic for Mercator-based tile
 * services.
 *
 * @author to.srwn
 * @author Ugo Taddei
 * @since 12
 */
public class WebMercatorZoomLevel extends ZoomLevel {

    public WebMercatorZoomLevel(int zoomLevel) {
        super(zoomLevel);
    }

    /**
     * The maximum tile-number: For example at zoom-level 2, the tilenames are in the following range: 2/0/0 - 2/3/3
     * (zoom-level/x/y): zoom-level/2^(zoom-level)-1/2^(zoom-level)-1)
     */
    @Override
    public int calculateMaxTilePerColNumber(int zoomLevel) {
        return 1 << zoomLevel; // 2 ^ (zoomLevel)
    }

    @Override
    public int calculateMaxTilePerRowNumber(int zoomLevel) {
        return calculateMaxTilePerColNumber(zoomLevel);
    }
}
