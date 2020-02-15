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

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.tile.Tile;
import org.geotools.tile.TileIdentifier;
import org.geotools.tile.TileService;
import org.geotools.tile.impl.WebMercatorTileFactory;
import org.geotools.tile.impl.ZoomLevel;
import org.geotools.util.logging.Logging;

/**
 * The BingTile, for the Bing Maps family
 *
 * @author Tobias Sauerwein
 * @author Ugo Taddei
 * @since 12
 */
public class BingTile extends Tile {

    public static final int DEFAULT_TILE_SIZE = 256;

    private static final Logger LOGGER = Logging.getLogger(BingTile.class);

    private TileService service;

    /** Creates a new BingTile. */
    public BingTile(int x, int y, ZoomLevel zoomLevel, TileService service) {
        this(new BingTileIdentifier(x, y, zoomLevel, service.getName()), service);
    }

    public BingTile(TileIdentifier tileName, TileService service) {

        super(tileName, WebMercatorTileFactory.getExtentFromTileName(tileName), DEFAULT_TILE_SIZE);

        // this.tileIdentifier = tileName;
        this.service = service;
    }

    public URL getUrl() {
        String url = this.service.getBaseUrl().replace("${code}", getTileIdentifier().getCode());
        try {
            return new URL(url);
        } catch (Exception e) {
            final String mesg = "Cannot create URL from " + url;
            LOGGER.log(Level.SEVERE, mesg, e);
            throw new RuntimeException(mesg, e);
        }
    }
}
