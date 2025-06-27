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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.http.HTTPClient;
import org.geotools.http.HTTPResponse;
import org.geotools.image.io.ImageIOExt;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.tile.impl.ScaleZoomLevelMatcher;
import org.geotools.tile.impl.ZoomLevel;
import org.geotools.util.ObjectCache;
import org.geotools.util.ObjectCaches;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Envelope;

/**
 * A TileService represent the class of objects that serve map tiles.
 *
 * <p>TileServices must at least have a name and a base URL.
 *
 * @author to.srwn
 * @author Ugo Taddei
 * @since 12
 */
public abstract class TileService implements ImageLoader {

    protected static final Logger LOGGER = Logging.getLogger(TileService.class);

    protected static int cacheSize = 50;

    /**
     * This WeakHashMap acts as a memory cache.
     *
     * <p>Because we are using SoftReference, we won't run out of Memory, the GC will free space.
     */
    private final ObjectCache<String, Tile> tiles = ObjectCaches.create("soft", cacheSize);

    private final String baseURL;
    private final String name;
    private final HTTPClient client;

    /**
     * Creates a TileService
     *
     * <p>Client isn't set so you should override loadImageTileImage.
     *
     * @param name the name. Cannot be null.
     */
    protected TileService(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        this.name = name;
        this.baseURL = null;
        this.client = null;
    }

    /**
     * Create a new TileService with a name and a base URL.
     *
     * <p>Client isn't set so you should override loadImageTileImage.
     *
     * @param name the name. Cannot be null.
     * @param baseURL the base URL. This is a string representing the common part of the URL for all this service's
     *     tiles. Cannot be null. Note that this constructor doesn't ensure that the URL is well-formed.
     */
    protected TileService(String name, String baseURL) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        this.name = name;
        if (baseURL == null || baseURL.isEmpty()) {
            throw new IllegalArgumentException("Base URL cannot be null");
        }
        this.baseURL = baseURL;
        this.client = null;
    }

    /**
     * Create a new TileService with a name and a base URL
     *
     * @param name the name. Cannot be null.
     * @param baseURL the base URL. This is a string representing the common part of the URL for all this service's
     *     tiles. Cannot be null. Note that this constructor doesn't ensure that the URL is well-formed.
     * @param client HTTPClient instance to use for a tile request.
     */
    protected TileService(String name, String baseURL, HTTPClient client) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        this.name = name;

        if (baseURL == null || baseURL.isEmpty()) {
            throw new IllegalArgumentException("Base URL cannot be null");
        }
        this.baseURL = baseURL;

        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null");
        }
        this.client = client;
    }

    public String getName() {
        return name;
    }

    public int getTileWidth() {
        return 256;
    }

    public int getTileHeight() {
        return 256;
    }

    /** Returns the prefix of an tile-url, e.g.: http://tile.openstreetmap.org/ */
    public String getBaseUrl() {
        return this.baseURL;
    }

    /** The CRS that is used when the extent is cut in tiles. */
    public CoordinateReferenceSystem getTileCrs() {
        return DefaultGeographicCRS.WGS84;
    }

    /**
     * Translates the map scale into a zoom-level for the map services.
     *
     * <p>The scale-factor (0-100) decides whether the tiles will be scaled down (100) or scaled up (0).
     *
     * @param scaleFactor Scale-factor (0-100)
     * @return Zoom-level
     */
    public int getZoomLevelFromMapScale(ScaleZoomLevelMatcher zoomLevelMatcher, double scaleFactor) {
        // fallback scale-list
        double[] scaleList = getScaleList();
        assert scaleList != null && scaleList.length > 0;

        // during the calculations this list caches already calculated scales
        double[] tempScaleList = new double[scaleList.length];
        Arrays.fill(tempScaleList, Double.NaN);

        int zoomLevel = zoomLevelMatcher.getZoomLevelFromScale(this, tempScaleList);

        // Now apply the scale-factor
        if (zoomLevel == 0) {
            return zoomLevel;
        } else {
            int upperScaleIndex = zoomLevel - 1;
            int lowerScaleIndex = zoomLevel;

            double deltaScale = tempScaleList[upperScaleIndex] - tempScaleList[lowerScaleIndex];
            double rangeScale = scaleFactor / 100d * deltaScale;
            double limitScale = tempScaleList[lowerScaleIndex] + rangeScale;

            if (zoomLevelMatcher.getScale() > limitScale) {
                return upperScaleIndex;
            } else {
                return lowerScaleIndex;
            }
        }
    }

    /**
     * Returns the zoom-level that should be used to fetch the tiles.
     *
     * @param useRecommended always use the calculated zoom-level, do not use the one the user selected
     */
    public int getZoomLevelToUse(ScaleZoomLevelMatcher zoomLevelMatcher, double scaleFactor, boolean useRecommended) {
        if (useRecommended) {
            return getZoomLevelFromMapScale(zoomLevelMatcher, scaleFactor);
        }

        boolean selectionAutomatic = true;
        int zoomLevel = -1;

        // check if the zoom-level is valid
        if (!selectionAutomatic && zoomLevel >= getMinZoomLevel() && zoomLevel <= getMaxZoomLevel()) {
            // the zoom-level from the properties is valid, so let's take it

            return zoomLevel;
        } else {

            // No valid property values or automatic selection of the zoom-level
            return getZoomLevelFromMapScale(zoomLevelMatcher, scaleFactor);
        }
    }

    /** Returns the lowest zoom-level number from the scaleList. */
    public int getMinZoomLevel() {
        double[] scaleList = getScaleList();
        int minZoomLevel = 0;

        while (Double.isNaN(scaleList[minZoomLevel]) && minZoomLevel < scaleList.length) {
            minZoomLevel++;
        }

        return minZoomLevel;
    }

    /** Returns the highest zoom-level number from the scaleList. */
    public int getMaxZoomLevel() {
        double[] scaleList = getScaleList();
        int maxZoomLevel = scaleList.length - 1;

        while (Double.isNaN(scaleList[maxZoomLevel]) && maxZoomLevel >= 0) {
            maxZoomLevel--;
        }

        return maxZoomLevel;
    }

    public Set<Tile> findTilesInExtent(
            ReferencedEnvelope _mapExtent, double scaleFactor, boolean recommendedZoomLevel, int maxNumberOfTiles) {

        ReferencedEnvelope mapExtent = createSafeEnvelopeInWGS84(_mapExtent);

        ReferencedEnvelope extent = normalizeExtent(mapExtent);

        // only continue, if we have tiles that cover the requested extent
        if (!extent.intersects((Envelope) getBounds())) {
            return Collections.emptySet();
        }

        TileFactory tileFactory = getTileFactory();

        // TODO CRS
        ScaleZoomLevelMatcher zoomLevelMatcher = null;
        try {

            zoomLevelMatcher = new ScaleZoomLevelMatcher(
                    getTileCrs(),
                    getProjectedTileCrs(),
                    CRS.findMathTransform(getTileCrs(), getProjectedTileCrs()),
                    CRS.findMathTransform(getProjectedTileCrs(), getTileCrs()),
                    mapExtent,
                    mapExtent,
                    scaleFactor);

        } catch (FactoryException e) {
            throw new RuntimeException(e);
        }

        // TODO understand the minus 1 below
        int zoomLevelA = getZoomLevelToUse(zoomLevelMatcher, scaleFactor, recommendedZoomLevel) - 1;
        if (zoomLevelA <= 0) zoomLevelA = 0; // this is related to the -1 above!
        ZoomLevel zoomLevel = tileFactory.getZoomLevel(zoomLevelA, this);

        long maxNumberOfTilesForZoomLevel = zoomLevel.getMaxTileNumber();

        Set<Tile> tileList = new HashSet<>(100);

        // Let's get the first tile which covers the upper-left corner
        TileIdentifier identifier = identifyTileAtCoordinate(extent.getMinX(), extent.getMaxY(), zoomLevel);
        Tile firstTile = obtainTile(identifier);

        tileList.add(firstTile);

        Tile firstTileOfRow = firstTile;
        Tile movingTile = firstTile;

        // Loop column
        do {
            // Loop row
            do {

                // get the next tile right of this one
                Tile rightNeighbour = tileFactory.findRightNeighbour(movingTile, this);

                // Check if the new tile is still part of the extent and
                // that we don't have the first tile again
                if (rightNeighbour != null
                        && extent.intersects((Envelope) rightNeighbour.getExtent())
                        && !firstTileOfRow.equals(rightNeighbour)) {

                    tileList.add(rightNeighbour);

                    movingTile = rightNeighbour;
                } else {

                    break;
                }
                if (tileList.size() > maxNumberOfTiles) {
                    LOGGER.warning("Reached tile limit of " + maxNumberOfTiles + ". Returning an empty collection.");
                    return Collections.emptySet();
                }
            } while (tileList.size() < maxNumberOfTilesForZoomLevel);

            // get the next tile under the first one of the row
            Tile lowerNeighbour = tileFactory.findLowerNeighbour(firstTileOfRow, this);

            // Check if the new tile is still part of the extent
            if (lowerNeighbour != null
                    && extent.intersects((Envelope) lowerNeighbour.getExtent())
                    && !firstTile.equals(lowerNeighbour)) {

                tileList.add(lowerNeighbour);

                firstTileOfRow = movingTile = lowerNeighbour;
            } else {
                break;
            }
        } while (tileList.size() < maxNumberOfTilesForZoomLevel);

        return tileList;
    }

    /** Returns tile identifier for the tile at the given coordinate */
    public abstract TileIdentifier identifyTileAtCoordinate(double lon, double lat, ZoomLevel zoomLevel);

    /** Fetches the image from url given by tile. */
    @Override
    public BufferedImage loadImageTileImage(Tile tile) throws IOException {
        final HTTPResponse response = getHttpClient().get(tile.getUrl());
        try {
            return ImageIOExt.readBufferedImage(response.getResponseStream());
        } finally {
            response.dispose();
        }
    }

    /** Check cache for given identifier. Call TileFactory to create new if not present. */
    public Tile obtainTile(TileIdentifier identifier) {
        String id = identifier.getId();
        boolean isInCache = !(tiles.peek(id) == null || tiles.get(id) == null);

        if (isInCache) {
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.fine("Tile already in cache: " + id);
            }
            return tiles.get(id);
        } else {
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.fine("Tile created new: " + id);
            }
            final Tile newTile = getTileFactory().create(identifier, this);
            tiles.put(id, newTile);
            return newTile;
        }
    }

    /**
     * Returns a list that represents a mapping between zoom-levels and map scale.
     *
     * <p>Array index: zoom-level Value at index: map scale High zoom-level -> more detailed map Low zoom-level -> less
     * detailed map
     *
     * @return mapping between zoom-levels and map scale
     */
    public abstract double[] getScaleList();

    /** Returns the bounds for the complete TileService */
    public abstract ReferencedEnvelope getBounds();

    /** The projection the tiles are drawn in. */
    public abstract CoordinateReferenceSystem getProjectedTileCrs();

    /** Returns the TileFactory which is used to call the method getTileFromCoordinate(). */
    public abstract TileFactory getTileFactory();

    public static final ReferencedEnvelope createSafeEnvelopeInWGS84(ReferencedEnvelope _mapExtent) {

        try {

            return _mapExtent.transform(DefaultGeographicCRS.WGS84, true);

        } catch (TransformException | FactoryException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Normalize extents.
     *
     * <p>The extent from the viewport may look like this: MaxY: 110° (=-70°) MinY: -110° MaxX: 180° MinX: -180°
     *
     * <p>But cutExtentIntoTiles(..) requires an extent that looks like this: MaxY: 85° (or 90°) MinY: -85° (or -90°)
     * MaxX: 180° MinX: -180°
     */
    private ReferencedEnvelope normalizeExtent(ReferencedEnvelope envelope) {
        ReferencedEnvelope bounds = getBounds();

        if (envelope.getMaxY() > bounds.getMaxY()
                || envelope.getMinY() < bounds.getMinY()
                || envelope.getMaxX() > bounds.getMaxX()
                || envelope.getMinX() < bounds.getMinX()) {

            double maxY = envelope.getMaxY() > bounds.getMaxY() ? bounds.getMaxY() : envelope.getMaxY();
            double minY = envelope.getMinY() < bounds.getMinY() ? bounds.getMinY() : envelope.getMinY();
            double maxX = envelope.getMaxX() > bounds.getMaxX() ? bounds.getMaxX() : envelope.getMaxX();
            double minX = envelope.getMinX() < bounds.getMinX() ? bounds.getMinX() : envelope.getMinX();

            ReferencedEnvelope newEnvelope =
                    new ReferencedEnvelope(minX, maxX, minY, maxY, envelope.getCoordinateReferenceSystem());

            return newEnvelope;
        }

        return envelope;
    }

    @Override
    public String toString() {
        return getName();
    }

    /**
     * Returns the http client to use for fetching images.
     *
     * @throws IllegalStateException If the service is constructed without a client.
     */
    public final HTTPClient getHttpClient() {
        if (this.client == null) {
            throw new IllegalStateException("This service isn't set up with a http client.");
        }
        return this.client;
    }
}
