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

import java.awt.image.RenderedImage;

import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.opengis.coverage.grid.GridEnvelope;

/**
 * Captures information about a query for a single raster in a raster dataset.
 * 
 * @author Gabriel Roldan
 * @version $Id$
 * @since 2.5.6
 * @see RasterUtils#findMatchingRasters
 * @see RasterUtils#fitRequestToRaster
 */
public final class RasterQueryInfo {

    private GeneralEnvelope requestedEnvelope;

    private GridEnvelope requestedDim;

    private int pyramidLevel;

    /**
     * The two-dimensional range of tile indices whose envelope intersect the requested extent. Will
     * have negative width and height if none of the tiles do.
     */
    private GridEnvelope matchingTiles;

    private GeneralEnvelope resultEnvelope;

    private GridEnvelope resultDimension;

    private Long rasterId;

    private GridEnvelope mosaicLocation;

    private RenderedImage resultImage;

    private GridEnvelope tiledImageGridRange;

    private double[] resolution;

    private int rasterIndex;

    /**
     * The full tile range for the matching pyramid level
     */
    private GridEnvelope levelTileRange;

    private GridEnvelope resultGridRange;

    public RasterQueryInfo() {
        setResultDimensionInsideTiledImage(new GridEnvelope2D(0, 0, 0, 0));
        setMatchingTiles(new GridEnvelope2D(0, 0, 0, 0));
        setResultEnvelope(null);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("[Raster query info:");
        s.append("\n\tRaster ID            : ").append(getRasterId());
        s.append("\n\tPyramid level        : ").append(getPyramidLevel());
        s.append("\n\tResolution           : ").append(
                getResolution()[0] + "," + getResolution()[1]);
        s.append("\n\tRequested envelope   : ").append(getRequestedEnvelope());
        s.append("\n\tRequested dimension  : ").append(getRequestedDim());
        GridEnvelope mt = getMatchingTiles();
//        GridEnvelope ltr = getLevelTileRange();
//        String matching = "x=" + mt.getLow(0) + "-" + mt.getHigh(0) + ", y=" + mt.getLow(1) + "-"
//                + mt.getHigh(1);
//        String level = "x=" + ltr.getLow(0) + "-" + ltr.getHigh(0) + ", y=" + ltr.getLow(1) + "-"
//                + ltr.getHigh(1);
        s.append("\n\tMatching tiles       : ").append(mt).append(" out of ").append("level");
        s.append("\n\tTiled image size     : ").append(getTiledImageGridRange());
        s.append("\n\tResult dimension     : ").append(getResultDimensionInsideTiledImage());
        s.append("\n\tMosaiced dimension   : ").append(getMosaicLocation());
        s.append("\n\tResult envelope      : ").append(getResultEnvelope());
        s.append("\n]");
        return s.toString();
    }

    /**
     * @return the rasterId (as in SeRaster.getId()) for the raster in the raster dataset this query
     *         works upon
     */
    public Long getRasterId() {
        return rasterId;
    }

    public GeneralEnvelope getRequestedEnvelope() {
        return requestedEnvelope;
    }

    public GridEnvelope getRequestedDim() {
        return requestedDim;
    }

    public int getPyramidLevel() {
        return pyramidLevel;
    }

    public GridEnvelope getMatchingTiles() {
        return matchingTiles;
    }

    public GeneralEnvelope getResultEnvelope() {
        return resultEnvelope;
    }

    @Deprecated
    public GridEnvelope getResultDimensionInsideTiledImage() {
        return resultDimension;
    }

    void setRasterId(Long rasterId) {
        this.rasterId = rasterId;
    }

    void setPyramidLevel(int pyramidLevel) {
        this.pyramidLevel = pyramidLevel;
    }

    void setRequestedEnvelope(GeneralEnvelope requestedEnvelope) {
        this.requestedEnvelope = requestedEnvelope;
    }

    void setRequestedDim(GridEnvelope requestedDim) {
        this.requestedDim = requestedDim;
    }

    void setResultEnvelope(GeneralEnvelope resultEnvelope) {
        this.resultEnvelope = resultEnvelope;
    }

    void setMatchingTiles(GridEnvelope matchingTiles) {
        this.matchingTiles = matchingTiles;
    }

    void setResultDimensionInsideTiledImage(GridEnvelope resultDimensionInsideTiledImage) {
        this.resultDimension = resultDimensionInsideTiledImage;
    }

    void setMosaicLocation(GridEnvelope targetRasterGridRange) {
        this.mosaicLocation = targetRasterGridRange;
    }

    public GridEnvelope getMosaicLocation() {
        return mosaicLocation;
    }

    public void setResultImage(RenderedImage rasterImage) {
        this.resultImage = rasterImage;
        // if (rasterImage.getWidth() != tiledImageSize.width
        // || rasterImage.getHeight() != tiledImageSize.height) {
        // LOGGER.warning("Result image and expected dimensions don't match: image="
        // + resultImage.getWidth() + "x" + resultImage.getHeight() + ", expected="
        // + tiledImageSize.width + "x" + tiledImageSize.height);
        // }
    }

    public RenderedImage getResultImage() {
        return resultImage;
    }

    void setTiledImageGridRange(GridEnvelope tiledImageGridRange) {
        this.tiledImageGridRange = tiledImageGridRange;
    }

    public GridEnvelope getTiledImageGridRange() {
        return tiledImageGridRange;
    }

    void setResolution(double[] resolution) {
        this.resolution = resolution;
    }

    public double[] getResolution() {
        return resolution == null ? new double[] { -1, -1 } : resolution;
    }

    void setRasterIndex(int rasterN) {
        this.rasterIndex = rasterN;
    }

    public int getRasterIndex() {
        return rasterIndex;
    }

    @Deprecated
    void setLevelTileRange(GridEnvelope levelTileRange2) {
        this.levelTileRange = levelTileRange2;
    }

    @Deprecated
    public GridEnvelope getLevelTileRange() {
        return levelTileRange;
    }

    void setResultGridRange(GridEnvelope resultGridRange) {
        this.resultGridRange = resultGridRange;     
    }
    
    public GridEnvelope getResultGridRange(){
        return resultGridRange;
    }
}