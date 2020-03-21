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
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.ows.HTTPClient;
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
    private static final ObjectCache tileImages;

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

    private final WMTSServiceType type;

    private final WMTSTileService service;

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
        this.type = this.service.getType();
    }

    /** @return the type of WMTS KVP or REST */
    public WMTSServiceType getType() {
        return type;
    }

    @Override
    public URL getUrl() {
        String baseUrl = service.getTemplateURL();

        TileIdentifier tileIdentifier = getTileIdentifier();
        if (null == type) {
            throw new IllegalArgumentException("Unexpected WMTS Service type " + type);
        } else
            switch (type) {
                case KVP:
                    return getKVPurl(baseUrl, tileIdentifier);
                case REST:
                    return getRESTurl(baseUrl, tileIdentifier);
                default:
                    throw new IllegalArgumentException("Unexpected WMTS Service type " + type);
            }
    }

    private URL getRESTurl(String baseUrl, TileIdentifier tileIdentifier) throws RuntimeException {
        String tileMatrix = service.getTileMatrix(tileIdentifier.getZ()).getIdentifier();

        baseUrl = baseUrl.replace("{TileMatrixSet}", service.getTileMatrixSetName());
        baseUrl = baseUrl.replace("{TileMatrix}", "" + tileMatrix);
        baseUrl = baseUrl.replace("{TileCol}", "" + tileIdentifier.getX());
        baseUrl = baseUrl.replace("{TileRow}", "" + tileIdentifier.getY());

        baseUrl =
                replaceToken(
                        baseUrl,
                        "time",
                        service.getDimensions().get(WMTSTileService.DIMENSION_TIME));

        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine("Requesting tile " + tileIdentifier.getCode());

        try {
            return new URL(baseUrl);
        } catch (MalformedURLException e) {
            // I'm pretty sure this never happens!
            throw new RuntimeException(e);
        }
    }

    private String replaceToken(String base, String dimName, String dimValue) {
        String token = "{" + dimName + "}";
        int index = base.toLowerCase().indexOf(token.toLowerCase());
        if (index != -1) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.fine("Resolving dimension " + dimName + " --> " + dimValue);
            return base.substring(0, index)
                    + dimValue
                    + base.substring(index + dimName.length() + 2);
        } else {
            return base;
        }
    }

    private URL getKVPurl(String baseUrl, TileIdentifier tileIdentifier) throws RuntimeException {
        // in theory it should end with ? but there may be other params that the
        // server needs out of spec
        if (!baseUrl.contains("?")) {
            baseUrl += "?";
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("service", "WMTS");
        params.put("version", "1.0.0");
        params.put("request", "GetTile");
        params.put("layer", service.getLayerName());
        params.put("style", service.getStyleName());
        params.put("format", service.getFormat());
        params.put("tilematrixset", service.getTileMatrixSetName());
        params.put("TileMatrix", service.getTileMatrix(tileIdentifier.getZ()).getIdentifier());
        params.put("TileCol", tileIdentifier.getX());
        params.put("TileRow", tileIdentifier.getY());

        StringBuilder arguments = new StringBuilder();
        for (String key : params.keySet()) {
            Object val = params.get(key);
            try {
                if (val != null) {
                    arguments.append(key).append("=");
                    arguments.append(URLEncoder.encode(val.toString(), "UTF-8"));
                    arguments.append('&');
                }
            } catch (Exception e) {
                LOGGER.warning("Could not encode param '" + key + "' with value '" + val + "'");
            }
        }

        try {
            return new URL(baseUrl + arguments.toString());
        } catch (MalformedURLException e) {
            // I'm pretty sure this never happens!
            throw new RuntimeException(e);
        }
    }

    /** Load and cache locally the WMTS tiles */
    @Override
    public BufferedImage loadImageTileImage(Tile tile) throws IOException {
        LOGGER.log(Level.FINE, "Loading tile " + getId() + ": " + this.getUrl());

        String tileKey = tile.getUrl().toString();

        if (!(tileImages.peek(tileKey) == null || tileImages.get(tileKey) == null)) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, "Tile image already loaded for tile " + getId());
            return (BufferedImage) tileImages.get(tileKey);
        } else {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, "Tile image not yet loaded for tile " + getId());
            BufferedImage bi = doLoadImageTileImage(tile);
            tileImages.put(tileKey, bi);
            return bi;
        }
    }

    public BufferedImage doLoadImageTileImage(Tile tile) throws IOException {

        Map<String, String> headers =
                (Map<String, String>)
                        this.service.getExtrainfo().get(WMTSTileService.EXTRA_HEADERS);
        try (InputStream is = setupInputStream(getUrl(), headers)) {
            return ImageIOExt.readBufferedImage(is);
        }
    }

    private InputStream setupInputStream(URL url, Map<String, String> headers) throws IOException {
        HTTPClient client = this.service.getHttpClient();
        return client.get(url, headers).getResponseStream();
    }
}
