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

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.measure.Unit;
import javax.measure.UnitConverter;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.ows.wmts.model.TileMatrix;
import org.geotools.ows.wmts.model.TileMatrixLimits;
import org.geotools.ows.wmts.model.TileMatrixSet;
import org.geotools.ows.wmts.model.TileMatrixSetLink;
import org.geotools.tile.Tile;
import org.geotools.tile.TileFactory;
import org.geotools.tile.TileService;
import org.geotools.tile.impl.ZoomLevel;
import org.geotools.util.logging.Logging;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystem;
import si.uom.NonSI;
import si.uom.SI;

/**
 * Implementation of TileFactory for WMTS
 *
 * @author ian
 * @author Emanuele Tajariol (etj at geo-solutions dot it)
 */
public class WMTSTileFactory extends TileFactory {

    private static final double PixelSizeMeters = 0.28e-3;

    private static final Logger LOGGER = Logging.getLogger(WMTSTileFactory.class);

    /**
     * Return a tile with the proper row and column indexes.
     *
     * <p>Please notice that the tile indexes are purely computed on the zoom level details, but the
     * MatrixLimits in a given layer may make the row/col invalid for that layer.
     */
    @Override
    public Tile findTileAtCoordinate(
            double lon, double lat, ZoomLevel zoomLevel, TileService service) {

        WMTSZoomLevel zl = (WMTSZoomLevel) zoomLevel;
        TileMatrix tileMatrix =
                ((WMTSTileService) service).getMatrixSet().getMatrices().get(zl.getZoomLevel());

        double pixelSpan = getPixelSpan(tileMatrix);

        double tileSpanY = (tileMatrix.getTileHeight() * pixelSpan);
        double tileSpanX = (tileMatrix.getTileWidth() * pixelSpan);
        double tileMatrixMinX;
        double tileMatrixMaxY;
        if (tileMatrix
                .getCrs()
                .getCoordinateSystem()
                .getAxis(0)
                .getDirection()
                .equals(AxisDirection.EAST)) {
            tileMatrixMinX = tileMatrix.getTopLeft().getX();
            tileMatrixMaxY = tileMatrix.getTopLeft().getY();
        } else {
            tileMatrixMaxY = tileMatrix.getTopLeft().getX();
            tileMatrixMinX = tileMatrix.getTopLeft().getY();
        }
        // to compensate for floating point computation inaccuracies
        double epsilon = 1e-6;
        long xTile = (int) Math.floor((lon - tileMatrixMinX) / tileSpanX + epsilon);
        long yTile = (int) Math.floor((tileMatrixMaxY - lat) / tileSpanY + epsilon);

        // sanitize
        xTile = Math.max(0, xTile);
        yTile = Math.max(0, yTile);

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(
                    "findTile: (lon,lat)=("
                            + lon
                            + ","
                            + lat
                            + ")  (col,row)="
                            + xTile
                            + ", "
                            + yTile
                            + " zoom:"
                            + zoomLevel.getZoomLevel());
        }

        return new WMTSTile((int) xTile, (int) yTile, zoomLevel, service);
    }

    /** Find the first valid Upper Left tile for the current layer. */
    public Tile findUpperLeftTile(
            double lon, double lat, WMTSZoomLevel zoomLevel, WMTSTileService service) {
        // get the tile in the tilematrix
        Tile matrixTile = findTileAtCoordinate(lon, lat, zoomLevel, service);
        return constrainToUpperLeftTile(matrixTile, zoomLevel, service);
    }

    public static TileMatrixLimits getLimits(TileMatrixSetLink tmsl, TileMatrixSet tms, int z) {

        List<TileMatrixLimits> limitsList = tmsl.getLimits();
        TileMatrixLimits limits;

        if (limitsList != null && z < limitsList.size()) {
            limits = limitsList.get(z);
        } else {
            // no limits defined in layer; let's take all the defined tiles
            TileMatrix tileMatrix = tms.getMatrices().get(z);

            limits = new TileMatrixLimits();
            limits.setMinCol(0L);
            limits.setMinRow(0L);
            limits.setMaxCol(tileMatrix.getMatrixWidth() - 1);
            limits.setMaxRow(tileMatrix.getMatrixHeight() - 1);
            limits.setTileMatix(tms.getIdentifier());
        }

        return limits;
    }

    /** If the tile is outside the limits, take a valid one which can be used to start a loop on. */
    public WMTSTile constrainToUpperLeftTile(
            Tile matrixTile, WMTSZoomLevel zl, WMTSTileService service) {

        TileMatrixLimits limits =
                getLimits(service.getMatrixSetLink(), service.getMatrixSet(), zl.getZoomLevel());

        long origxTile = matrixTile.getTileIdentifier().getX();
        long origyTile = matrixTile.getTileIdentifier().getY();
        long xTile = origxTile;
        long yTile = origyTile;

        if (xTile >= limits.getMaxcol()) xTile = limits.getMaxcol() - 1;
        if (yTile >= limits.getMaxrow()) yTile = limits.getMaxrow() - 1;

        if (xTile < limits.getMincol()) xTile = limits.getMincol();
        if (yTile < limits.getMinrow()) yTile = limits.getMinrow();

        if (origxTile != xTile || origyTile != yTile) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine(
                        "findUpperLeftTile: constraining tile within limits: ("
                                + origxTile
                                + ","
                                + origyTile
                                + ") -> ("
                                + xTile
                                + ","
                                + yTile
                                + ")");
            }
        }

        return new WMTSTile((int) xTile, (int) yTile, zl, service);
    }

    @Override
    public WMTSZoomLevel getZoomLevel(int zoomLevel, TileService service) {
        return new WMTSZoomLevel(zoomLevel, (WMTSTileService) service);
    }

    @Override
    public Tile findRightNeighbour(Tile tile, TileService service) {
        WMTSTileIdentifier id = (WMTSTileIdentifier) tile.getTileIdentifier().getRightNeighbour();
        return id == null ? null : new WMTSTile(id, service);
    }

    @Override
    public Tile findLowerNeighbour(Tile tile, TileService service) {
        WMTSTileIdentifier id = (WMTSTileIdentifier) tile.getTileIdentifier().getLowerNeighbour();
        return id == null ? null : new WMTSTile(id, service);
    }

    /** */
    public static ReferencedEnvelope getExtentFromTileName(
            WMTSTileIdentifier tileIdentifier, TileService service) {
        WMTSZoomLevel zl = new WMTSZoomLevel(tileIdentifier.getZ(), (WMTSTileService) service);
        TileMatrix tileMatrix =
                ((WMTSTileService) service).getMatrixSet().getMatrices().get(zl.getZoomLevel());

        CoordinateReferenceSystem crs = tileMatrix.getCrs();
        CoordinateSystem coordinateSystem = crs.getCoordinateSystem();

        double pixelSpan = getPixelSpan(tileMatrix);
        double tileSpanY = (tileMatrix.getTileHeight() * pixelSpan);
        double tileSpanX = (tileMatrix.getTileWidth() * pixelSpan);

        double tileMatrixMinX;
        double tileMatrixMaxY;
        boolean longFirst = coordinateSystem.getAxis(0).getDirection().equals(AxisDirection.EAST);
        if (longFirst) {
            tileMatrixMinX = tileMatrix.getTopLeft().getX();
            tileMatrixMaxY = tileMatrix.getTopLeft().getY();
        } else {
            tileMatrixMaxY = tileMatrix.getTopLeft().getX();
            tileMatrixMinX = tileMatrix.getTopLeft().getY();
        }
        ReferencedEnvelope ret = new ReferencedEnvelope(crs);
        double minX = tileIdentifier.getX() * tileSpanX + tileMatrixMinX;
        double maxY = tileMatrixMaxY - tileIdentifier.getY() * tileSpanY;
        double maxX = minX + tileSpanX;
        double minY = maxY - tileSpanY;
        if (longFirst) {
            ret.expandToInclude(minX, minY);
            ret.expandToInclude(maxX, maxY);
        } else {
            ret.expandToInclude(minY, minX);
            ret.expandToInclude(maxY, maxX);
        }

        return ret;
    }

    /** */
    private static double getPixelSpan(TileMatrix tileMatrix) {
        CoordinateSystem coordinateSystem = tileMatrix.getCrs().getCoordinateSystem();
        Unit unit = coordinateSystem.getAxis(0).getUnit();

        // now divide by meters per unit!
        double pixelSpan = tileMatrix.getDenominator() * PixelSizeMeters;
        if (unit.equals(NonSI.DEGREE_ANGLE)) {
            /*
             * use the length of a degree at the equator = 60 nautical miles!
             * unit = USCustomary.NAUTICAL_MILE; UnitConverter metersperunit =
             * unit.getConverterTo(SI.METRE); pixelSpan /=
             * metersperunit.convert(60.0);
             */

            // constant value from
            // https://msi.nga.mil/MSISiteContent/StaticFiles/Calculators/degree.html
            // apparently - 60.10764611706782 NaMiles
            pixelSpan /= 111319;
        } else {
            UnitConverter metersperunit = unit.getConverterTo(SI.METRE);
            pixelSpan /= metersperunit.convert(1);
        }
        return pixelSpan;
    }
}
