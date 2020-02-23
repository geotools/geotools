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
package org.geotools.tile.impl;

import java.util.Arrays;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.renderer.lite.RendererUtilities;
import org.geotools.tile.Tile;
import org.geotools.tile.TileFactory;
import org.geotools.tile.TileService;
import org.locationtech.jts.geom.Coordinate;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * This class is responsible for finding the right zoom-level for a given map extent.
 *
 * @author to.srwn
 * @since 12
 */
public class ScaleZoomLevelMatcher {
    /** the CRS of the map (MapCrs) */
    private CoordinateReferenceSystem crsMap;

    /** the CRS used for the tile cutting (TileCrs) */
    private CoordinateReferenceSystem crsTiles;

    /** Transformation: MapCrs -> TileCrs (mostly WGS_84) */
    private MathTransform transformMapToTileCrs;

    /** Transformation: TileCrs (mostly WGS_84) -> MapCrs (needed for the blank tiles) */
    private MathTransform transformTileCrsToMap;

    /** the extent that should be drawn in TileCrs */
    private ReferencedEnvelope mapExtentTileCrs;

    /** the current map-scale */
    private double scale;

    private static int DPI;

    static {
        try {
            DPI = 96; // TODO ?????Toolkit.getDefaultToolkit().getd
            // Display.getDefault().getDPI().x;
        } catch (Exception exc) {
            DPI = 96;
        }
    }

    public ScaleZoomLevelMatcher(
            CoordinateReferenceSystem crsMap,
            CoordinateReferenceSystem crsTiles,
            MathTransform transformMapToTileCrs,
            MathTransform transformTileCrsToMap,
            ReferencedEnvelope mapExtentTileCrs,
            ReferencedEnvelope mapExtentMapCrs,
            double scale) {

        this.crsMap = crsMap;
        this.crsTiles = crsTiles;
        this.transformMapToTileCrs = transformMapToTileCrs;
        this.transformTileCrsToMap = transformTileCrsToMap;
        this.mapExtentTileCrs = mapExtentTileCrs;
        this.scale = scale;
    }

    public static ScaleZoomLevelMatcher createMatcher(
            ReferencedEnvelope requestExtent, CoordinateReferenceSystem crsTiles, double scale)
            throws FactoryException, TransformException {

        CoordinateReferenceSystem crsMap = requestExtent.getCoordinateReferenceSystem();

        // Transformation: MapCrs -> TileCrs
        MathTransform transformMapToTile = CRS.findMathTransform(crsMap, crsTiles);

        // Transformation: TileCrs -> MapCrs (needed for the blank tiles)
        MathTransform transformTileToMap = CRS.findMathTransform(crsTiles, crsMap);

        // Get the mapExtent in the tiles CRS
        ReferencedEnvelope mapExtentTileCrs =
                getProjectedEnvelope(requestExtent, crsTiles, transformMapToTile);

        return new ScaleZoomLevelMatcher(
                crsMap,
                crsTiles,
                transformMapToTile,
                transformTileToMap,
                mapExtentTileCrs,
                requestExtent,
                scale);
    }

    /** Re-Projects the given envelope to destinationCRS using transformation. */
    public static ReferencedEnvelope getProjectedEnvelope(
            ReferencedEnvelope envelope,
            CoordinateReferenceSystem destinationCRS,
            MathTransform transformation)
            throws TransformException, FactoryException {

        CoordinateReferenceSystem sourceCRS = envelope.getCoordinateReferenceSystem();

        if (sourceCRS.equals(destinationCRS)) {
            // no need to reproject
            return envelope;

        } else {
            // Reproject envelope: first try JTS.transform, if that fails use
            // ReferencedEnvelope.transform
            try {

                return new ReferencedEnvelope(
                        JTS.transform(envelope, transformation), destinationCRS);
            } catch (Exception exc) {

                return envelope.transform(destinationCRS, false);
            }
        }
    }

    public CoordinateReferenceSystem getCrsMap() {
        return crsMap;
    }

    public CoordinateReferenceSystem getCrsTiles() {
        return crsTiles;
    }

    public ReferencedEnvelope getMapExtentTileCrs() {
        return mapExtentTileCrs;
    }

    public double getScale() {
        return scale;
    }

    /** Finds out the best fitting zoom-level for a given map-scale. */
    public int getZoomLevelFromScale(TileService service, double[] tempScaleList) {
        double[] scaleList = service.getScaleList();

        // Start with the most detailed zoom-level and search the best-fitting
        // one
        int zoomLevel = scaleList.length - 1;
        getOptimumScaleFromZoomLevel(zoomLevel, service, tempScaleList);

        for (int i = scaleList.length - 2; i >= 0; i--) {
            if (Double.isNaN(scaleList[i])) {
                break;
            } else if (getScale() < getOptimumScaleFromZoomLevel(i, service, tempScaleList)) {
                break;
            }

            zoomLevel = i;
            if (getScale() > getOptimumScaleFromZoomLevel(i + 1, service, tempScaleList)) {
                zoomLevel = i;
            }
        }

        return zoomLevel;
    }

    /**
     * Calculates the "best" scale for a given zoom-level by calculating the scale for a tile in the
     * center of the map-extent and by taking the mapCrs in account. "Best" scale is the scale where
     * a 256x256 tile has also this size when displayed in uDig.
     */
    public double getOptimumScaleFromZoomLevel(
            int zoomLevel, TileService service, double[] tempScaleList) {
        // check if we have calculated this already
        if (!Double.isNaN(tempScaleList[zoomLevel])) {
            return tempScaleList[zoomLevel];
        }

        try {
            ReferencedEnvelope centerTileBounds = getBoundsOfCenterTileInMapCrs(zoomLevel, service);

            double _scale =
                    RendererUtilities.calculateScale(
                            centerTileBounds, service.getTileWidth(), service.getTileHeight(), DPI);

            // cache the scale
            tempScaleList[zoomLevel] = _scale;

            return _scale;
        } catch (Exception exc) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", exc);
        }

        // in case of error, return fallback zoom-level
        return service.getScaleList()[zoomLevel];
    }

    public double getOptimumScaleFromZoomLevel(int zoomLevel, TileService wmtSource) {
        double[] tempScaleList = new double[wmtSource.getScaleList().length];
        Arrays.fill(tempScaleList, Double.NaN);

        return getOptimumScaleFromZoomLevel(zoomLevel, wmtSource, tempScaleList);
    }

    /**
     * Returns the bounds of the tile which covers the center of the map extent in the CRS of the
     * map.
     */
    private ReferencedEnvelope getBoundsOfCenterTileInMapCrs(int zoomLevel, TileService wmtSource)
            throws Exception {
        Tile centerTile = getCenterTile(zoomLevel, wmtSource);
        ReferencedEnvelope boundsInTileCrs = centerTile.getExtent();
        ReferencedEnvelope boundsInMapCrs = projectTileToMapCrs(boundsInTileCrs);

        return boundsInMapCrs;
    }

    /** Returns the tile which covers the center of the map extent. */
    private Tile getCenterTile(int zoomLevel, TileService wmtSource) {
        TileFactory tileFactory = wmtSource.getTileFactory();
        ZoomLevel zoomLevelInstance = tileFactory.getZoomLevel(zoomLevel, wmtSource);

        // get the coordinates of the map centre (in TileCrs)
        Coordinate centerPoint = mapExtentTileCrs.centre();

        return tileFactory.findTileAtCoordinate(
                centerPoint.x, centerPoint.y, zoomLevelInstance, wmtSource);
    }

    public ReferencedEnvelope projectTileToMapCrs(ReferencedEnvelope boundsInTileCrs)
            throws Exception {
        // assert(boundsInTileCrs.getCoordinateReferenceSystem().equals(crsTiles));
        return getProjectedEnvelope(boundsInTileCrs, crsMap, transformTileCrsToMap);
    }

    public ReferencedEnvelope projectMapToTileCrs(ReferencedEnvelope boundsInMapCrs)
            throws Exception {
        return getProjectedEnvelope(boundsInMapCrs, crsTiles, transformMapToTileCrs);
    }
}
