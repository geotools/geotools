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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.http.HTTPResponse;
import org.geotools.image.io.ImageIOExt;
import org.geotools.ows.wmts.model.WMTSServiceType;
import org.geotools.tile.Tile;
import org.geotools.tile.TileIdentifier;
import org.geotools.tile.TileService;
import org.geotools.tile.impl.ZoomLevel;
import org.geotools.util.ObjectCache;
import org.geotools.util.ObjectCaches;
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

    /**
     * Cache for tiles.
     *
     * <p>Many WMTS tiles may be reloaded over and over, especially in a tiled getMap request.
     *
     * <p>You can set the cache size using the property WMTS_TILE_CACHE_SIZE_PROPERTY_NAME.
     */
    private static final ObjectCache<String, BufferedImage> tileImages;

    static {
        int cacheSize = 150;

        String size = System.getProperty(WMTS_TILE_CACHE_SIZE_PROPERTY_NAME);
        if (size != null) {
            try {
                cacheSize = Integer.parseUnsignedInt(size);
            } catch (NumberFormatException ex) {
                LOGGER.info(
                        "Bad " + WMTS_TILE_CACHE_SIZE_PROPERTY_NAME + " property '" + size + "'");
            }
        }
        tileImages = ObjectCaches.create("soft", cacheSize);
    }

    public WMTSTile(int x, int y, ZoomLevel zoomLevel, TileService service) {
        this(new WMTSTileIdentifier(x, y, zoomLevel, service.getName()), service);
    }

    /** */
    public WMTSTile(WMTSTileIdentifier tileIdentifier, TileService service) {
        super(
                tileIdentifier,
                WMTSTileFactory.getExtentFromTileName(tileIdentifier, service),
                ((WMTSTileService) service)
                        .getTileMatrix(tileIdentifier.getZoomLevel().getZoomLevel())
                        .getTileWidth());

        this.service = (WMTSTileService) service;
    }

    /** @return the type of WMTS KVP or REST */
    @Deprecated
    public WMTSServiceType getType() {
        return getService().getType();
    }

    private WMTSTileService getService() {
        return (WMTSTileService) service;
    }

    @Override
    public URL getUrl() {

        TileIdentifier tileIdentifier = getTileIdentifier();
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Requesting tile " + tileIdentifier.getCode());
        }

        String tileMatrix = getService().getTileMatrix(tileIdentifier.getZ()).getIdentifier();
        int tileCol = tileIdentifier.getX();
        int tileRow = tileIdentifier.getY();

        String baseUrl = getService().getURLBuilder().createURL(tileMatrix, tileCol, tileRow);
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("TileURLBuilder gives url:" + baseUrl);
        }

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Final url is :" + baseUrl);
        }
        try {
            return new URL(baseUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(
                    "WMTS Tile URL was wrong baseUrl:" + getService().getBaseUrl(), e);
        }
    }

    /** Load and cache locally the WMTS tiles */
    @Override
    public BufferedImage loadImageTileImage(Tile tile) throws IOException {
        final URL url = getUrl();
        LOGGER.log(Level.FINE, "Loading tile " + getId() + ": " + url);

        String tileKey = url.toString();

        if (!(tileImages.peek(tileKey) == null || tileImages.get(tileKey) == null)) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, "Tile image already loaded for tile " + getId());
            return tileImages.get(tileKey);
        } else {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, "Tile image not yet loaded for tile " + getId());
            BufferedImage bi = doLoadImageTileImage(tile);
            tileImages.put(tileKey, bi);
            return bi;
        }
    }

    @SuppressWarnings("unchecked")
    public BufferedImage doLoadImageTileImage(Tile tile) throws IOException {
        final WMTSTileService service = getService();
        Map<String, String> headers =
                (Map<String, String>) service.getExtrainfo().get(WMTSTileService.EXTRA_HEADERS);
        HTTPResponse http = service.getHttpClient().get(getUrl(), headers);
        try {
            return ImageIOExt.readBufferedImage(http.getResponseStream());
        } finally {
            http.dispose();
        }
    }
}
