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

import java.net.URL;
import java.util.logging.Logger;
import org.geotools.tile.Tile;
import org.geotools.tile.TileIdentifier;
import org.geotools.tile.TileService;
import org.geotools.tile.impl.ZoomLevel;
import org.geotools.util.logging.Logging;

/**
 * Handle information about a WMTS tile
 *
 * @author ian
 * @author Emanuele Tajariol (etj at geo-solutions dot it)
 */
class WMTSTile extends Tile {

    protected static final Logger LOGGER = Logging.getLogger(WMTSTile.class);

    public static final String WMTS_TILE_CACHE_SIZE_PROPERTY_NAME = "wmts.tile.cache.size";

    private URL url = null;

    public WMTSTile(int x, int y, ZoomLevel zoomLevel, TileService service) {
        this(new WMTSTileIdentifier(x, y, zoomLevel, service.getName()), service);
    }

    public WMTSTile(TileIdentifier tileIdentifier, TileService service) {
        this((WMTSTileIdentifier) tileIdentifier, service);
    }

    public WMTSTile(WMTSTileIdentifier tileIdentifier, TileService service) {
        super(
                tileIdentifier,
                WMTSTileFactory.getExtentFromTileName(tileIdentifier, service),
                ((WMTSTileService) service)
                        .getTileMatrix(tileIdentifier.getZoomLevel().getZoomLevel())
                        .getTileWidth(),
                service);
    }

    private WMTSTileService getService() {
        return (WMTSTileService) service;
    }

    /** The url should be unique for this tile. Keeps the url as an instance variable. */
    @Override
    public URL getUrl() {
        if (url == null) {
            url = getService().createURL(this);
        }
        return url;
    }
}
