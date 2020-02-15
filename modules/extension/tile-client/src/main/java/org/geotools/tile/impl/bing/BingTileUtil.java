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

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.tile.impl.WebMercatorTileService;
import org.locationtech.jts.geom.Envelope;

/**
 * BingTileUtil contains code ported from <a
 * href="https://msdn.microsoft.com/en-us/library/bb259689.aspx>Bing Maps</a> offering a collection
 * of utilities methods concerning Bing Maps and its tile model.
 *
 * <p>Disclaimer: code contained here has been ported and, thus, re-written. The comments, on the
 * other hand, have been mostly taken verbatim with the sole purpose of precision and keeping the
 * original intent left clear. The author does not claim copyrights or authorship for comments.
 * Original comments are <q>quoted</q>.
 *
 * @author Ugo Taddei
 * @since 12
 */
public final class BingTileUtil {

    private BingTileUtil() {
        // utility class
    }

    /**
     * <q>Converts a point from latitude/longitude WGS-84 coordinates (in degrees) into pixel XY
     * coordinates at a specified level of detail.</q>
     *
     * <p>The Term "Level of detail" is a synonym for zoom level.
     *
     * @param longitude Longitude of the point, in degrees.
     * @param latitude Latitude of the point, in degrees.
     * @param zoomLevel The zoom level or "Level of detail", from 1 (lowest detail) to 23 (highest
     *     detail).
     */
    public static int[] lonLatToPixelXY(double longitude, double latitude, int zoomLevel) {
        double _latitude =
                clip(
                        latitude,
                        WebMercatorTileService.MIN_LATITUDE,
                        WebMercatorTileService.MAX_LATITUDE);
        double _longitude =
                clip(
                        longitude,
                        WebMercatorTileService.MIN_LONGITUDE,
                        WebMercatorTileService.MAX_LONGITUDE);

        double x = (_longitude + 180) / 360;
        double sinLatitude = Math.sin(_latitude * Math.PI / 180);
        double y = 0.5 - Math.log((1 + sinLatitude) / (1 - sinLatitude)) / (4 * Math.PI);

        int mapSize = mapSize(zoomLevel);
        int pixelX = (int) clip(x * mapSize + 0.5, 0, mapSize - 1);
        int pixelY = (int) clip(y * mapSize + 0.5, 0, mapSize - 1);

        return new int[] {pixelX, pixelY};
    }

    /**
     * <q>Converts a pixel from pixel XY coordinates at a specified level of detail [i.e. zoom
     * level] into latitude/longitude WGS-84 coordinates (in degrees).</q>
     *
     * <p>Note that the X and Y coordinates of a "virtual image" that contains all tiles of a given
     * level. They are not the coordinates of a given column or row.
     *
     * @param pixelX X coordinate of the point, in pixels.
     * @param pixelY Y coordinates of the point, in pixels.
     * @param zoomLevel zoom level or "Level of detail", from 1 (lowest detail) to 23 (highest
     *     detail)
     */
    public static double[] pixelXYToLonLat(int pixelX, int pixelY, int zoomLevel) {
        double mapSize = mapSize(zoomLevel);
        double x = (clip(pixelX, 0, mapSize - 1) / mapSize) - 0.5;
        double y = 0.5 - (clip(pixelY, 0, mapSize - 1) / mapSize);

        double latitude = 90 - 360 * Math.atan(Math.exp(-y * 2 * Math.PI)) / Math.PI;
        double longitude = 360 * x;

        return new double[] {longitude, latitude};
    }

    /**
     * <q>Determines the map width and height (in pixels) at a specified level of detail.</q>
     *
     * @param zoomLevel Zoom level or "Level of detail", from 1 (lowest detail) to 23 (highest
     *     detail)
     * @return the map size
     */
    public static int mapSize(int zoomLevel) {
        return BingTile.DEFAULT_TILE_SIZE << zoomLevel;
    }

    /**
     * <q>Converts pixel XY coordinates into tile XY coordinates of the tile containing the
     * specified pixel.</q>
     *
     * @param pixelX Pixel X coordinate.
     * @param pixelY Pixel Y coordinate.
     */
    public static int[] pixelXYToTileXY(int pixelX, int pixelY) {
        int tileX = pixelX / BingTile.DEFAULT_TILE_SIZE;
        int tileY = pixelY / BingTile.DEFAULT_TILE_SIZE;

        return new int[] {tileX, tileY};
    }

    /**
     * <q>Converts tile XY coordinates into a QuadKey at a specified level of detail.</q>
     *
     * @param tileX Tile X coordinate.
     * @param tileY Tile Y coordinate.
     * @param zoomLevel Zoom level or "Level of detail", from 1 (lowest detail) to 23 (highest
     *     detail)
     * @return A string containing the QuadKey.
     */
    public static String tileXYToQuadKey(int tileX, int tileY, int zoomLevel) {
        StringBuilder quadKey = new StringBuilder();
        for (int i = zoomLevel; i > 0; i--) {
            char digit = '0';
            int mask = 1 << (i - 1);
            if ((tileX & mask) != 0) {
                digit++;
            }
            if ((tileY & mask) != 0) {
                digit++;
                digit++;
            }
            quadKey.append(digit);
        }

        return quadKey.toString();
    }

    /**
     * <q>Clips a number to the specified minimum and maximum values.</q>
     *
     * @param n The number to clip.
     * @param minValue Minimum allowable value.
     * @param maxValue Maximum allowable value.
     * @return The clipped value.
     */
    private static double clip(double n, double minValue, double maxValue) {
        return Math.min(Math.max(n, minValue), maxValue);
    }

    /**
     * Finds the quadkey of a tile for a given pair of coordinates and at a given zoom level.
     *
     * @param lon The longitude
     * @param lat The latitude
     * @param zoomLevel The zoom level
     * @return A string denoting the quadkey of the tile.
     */
    public static String lonLatToQuadKey(double lon, double lat, int zoomLevel) {

        int[] pixelXY = BingTileUtil.lonLatToPixelXY(lon, lat, zoomLevel);
        int[] tileXY = BingTileUtil.pixelXYToTileXY(pixelXY[0], pixelXY[1]);

        return BingTileUtil.tileXYToQuadKey(tileXY[0], tileXY[1], zoomLevel);
    }

    /**
     * Calculates the extent of a tile, given the coordinates and a zoom level
     *
     * @param lon The longitude
     * @param lat The latitude
     * @return A string denoting the quadkey of the tile.
     */
    public static ReferencedEnvelope getTileBoundingBox(double lon, double lat, int zoomLevel) {

        int[] imageXY = lonLatToPixelXY(lon, lat, zoomLevel);

        int numberOfTilesX = imageXY[0] / BingTile.DEFAULT_TILE_SIZE;
        int numberOfTilesY = imageXY[1] / BingTile.DEFAULT_TILE_SIZE;

        int tileTopLeftPixelX = numberOfTilesX * BingTile.DEFAULT_TILE_SIZE;
        int tileTopLeftPixelY = numberOfTilesY * BingTile.DEFAULT_TILE_SIZE;

        double[] topLeftCoords = pixelXYToLonLat(tileTopLeftPixelX, tileTopLeftPixelY, zoomLevel);
        double[] bottomRightCoords =
                pixelXYToLonLat(
                        tileTopLeftPixelX + BingTile.DEFAULT_TILE_SIZE,
                        tileTopLeftPixelY + BingTile.DEFAULT_TILE_SIZE,
                        zoomLevel);

        Envelope envelope =
                new Envelope(
                        topLeftCoords[0],
                        bottomRightCoords[0],
                        topLeftCoords[1],
                        bottomRightCoords[1]);

        return new ReferencedEnvelope(envelope, DefaultGeographicCRS.WGS84);
    }
}
