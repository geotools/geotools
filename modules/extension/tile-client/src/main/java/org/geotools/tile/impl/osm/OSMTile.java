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

import java.net.URL;
import org.geotools.tile.Tile;
import org.geotools.tile.TileIdentifier;
import org.geotools.tile.TileService;
import org.geotools.tile.impl.WebMercatorTileFactory;
import org.geotools.tile.impl.ZoomLevel;

/**
 * The tile implementation for the OpenStreetMap family
 *
 * @author Tobias Sauerwein
 * @author Ugo Taddei
 * @since 12
 */
public class OSMTile extends Tile {

    public static final int DEFAULT_TILE_SIZE = 256;

    private TileService service;

    public OSMTile(int x, int y, ZoomLevel zoomLevel, TileService service) {
        this(new OSMTileIdentifier(x, y, zoomLevel, service.getName()), service);
    }

    public OSMTile(TileIdentifier tileName, TileService service) {
        super(tileName, WebMercatorTileFactory.getExtentFromTileName(tileName), DEFAULT_TILE_SIZE);

        this.service = service;
    }

    @Override
    public URL getUrl() {
        String url = this.service.getBaseUrl() + getTileIdentifier().getCode() + ".png";
        try {
            return new URL(url);
        } catch (Exception e) {
            final String mesg = "Cannot create URL from " + url;
            throw new RuntimeException(mesg, e);
        }
    }
}
