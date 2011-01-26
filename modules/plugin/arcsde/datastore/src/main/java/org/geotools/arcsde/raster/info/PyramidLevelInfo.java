/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.arcsde.raster.info;

import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.opengis.coverage.grid.GridEnvelope;

/**
 * Represents one level in an ArcSDE pyramid. Holds information about a given pyramid level, like
 * resolution, x/y offsets, number of tiles high/wide, total pixel size and total envelope covered
 * by this level.
 * 
 * @author sfarber
 * 
 */
final class PyramidLevelInfo {

    private int pyramidLevel, xTiles, yTiles;

    private final GeneralEnvelope spatialExtent;

    private GridEnvelope2D gridEnvelope;

    PyramidLevelInfo(final int level, final int numTilesWide, final int numTilesHigh,
            final GridEnvelope gridEnvelope, final GeneralEnvelope spatialExtent) {
        this.pyramidLevel = level;
        this.spatialExtent = spatialExtent;
        this.gridEnvelope = new GridEnvelope2D(gridEnvelope.getLow(0), gridEnvelope.getLow(1),
                gridEnvelope.getSpan(0), gridEnvelope.getSpan(1));
        this.xTiles = numTilesWide;
        this.yTiles = numTilesHigh;
    }

    /**
     * @return Which level in the pyramid this object represents
     */
    public int getLevel() {
        return pyramidLevel;
    }

    /**
     * @return The X and Y resolution in units/pixel for pixels at this level
     */
    public double getXRes() {
        return spatialExtent.getSpan(0) / gridEnvelope.getSpan(0);
    }

    /**
     * @return The X and Y resolution in units/pixel for pixels at this level
     */
    public double getYRes() {
        return spatialExtent.getSpan(1) / gridEnvelope.getSpan(1);
    }

    /**
     * @return The total number of tiles covering the width of this level
     */
    public int getNumTilesWide() {
        return xTiles;
    }

    /**
     * @return The total number of tiles covering the height of this level
     */
    public int getNumTilesHigh() {
        return yTiles;
    }

    /**
     * The envelope covering the image grid range inside fully tiled image at this pyramid level
     * 
     * @return The geographical area covered by the {@link #getGridEnvelope() grid range} of the
     *         raster at this pyramid level
     */
    public GeneralEnvelope getSpatialExtent() {
        return new GeneralEnvelope(spatialExtent);
    }

    /**
     * The rectangle covering the actual raster data inside the tiled space
     * 
     * @return
     */
    public GridEnvelope getGridEnvelope() {
        return new GridEnvelope2D(this.gridEnvelope);
    }

    @Override
    public String toString() {
        return "[level: " + pyramidLevel + " size: " + gridEnvelope.getSpan(0) + "x"
                + gridEnvelope.getSpan(1) + " Grid: " + gridEnvelope + "  xRes: " + getXRes()
                + "  yRes: " + getYRes() + "  xOffset: " + gridEnvelope.getLow(0) + "  yOffset: "
                + gridEnvelope.getLow(1) + "  extent: " + spatialExtent.getMinimum(0) + ","
                + spatialExtent.getMinimum(1) + "," + spatialExtent.getMaximum(0) + ","
                + spatialExtent.getMaximum(1) + "  tilesWide: " + xTiles + "  tilesHigh: " + yTiles
                + "]";
    }
}