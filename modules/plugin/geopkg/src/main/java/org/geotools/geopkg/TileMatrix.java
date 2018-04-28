/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg;

/**
 * A TileMatrix inside a Geopackage. Corresponds to the gpkg_tile_matrix table.
 *
 * @author Justin Deoliveira
 * @author Niels Charlier
 */
public class TileMatrix {

    Integer zoomLevel;
    Integer matrixWidth, matrixHeight;
    Integer tileWidth, tileHeight;
    Double xPixelSize;
    Double yPixelSize;
    boolean tiles;

    public TileMatrix() {}

    public TileMatrix(
            Integer zoomLevel,
            Integer matrixWidth,
            Integer matrixHeight,
            Integer tileWidth,
            Integer tileHeight,
            Double xPixelSize,
            Double yPixelSize) {
        super();
        this.zoomLevel = zoomLevel;
        this.matrixWidth = matrixWidth;
        this.matrixHeight = matrixHeight;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.xPixelSize = xPixelSize;
        this.yPixelSize = yPixelSize;
    }

    public Integer getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(Integer zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    public Integer getMatrixWidth() {
        return matrixWidth;
    }

    public void setMatrixWidth(Integer matrixWidth) {
        this.matrixWidth = matrixWidth;
    }

    public Integer getMatrixHeight() {
        return matrixHeight;
    }

    public void setMatrixHeight(Integer matrixHeight) {
        this.matrixHeight = matrixHeight;
    }

    public Integer getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(Integer tileWidth) {
        this.tileWidth = tileWidth;
    }

    public Integer getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(Integer tileHeight) {
        this.tileHeight = tileHeight;
    }

    public Double getXPixelSize() {
        return xPixelSize;
    }

    public void setXPixelSize(Double xPixelSize) {
        this.xPixelSize = xPixelSize;
    }

    public Double getYPixelSize() {
        return yPixelSize;
    }

    public void setYPixelSize(Double yPixelSize) {
        this.yPixelSize = yPixelSize;
    }

    public boolean hasTiles() {
        return tiles;
    }

    public void setTiles(boolean tiles) {
        this.tiles = tiles;
    }

    @Override
    public String toString() {
        return "TileMatrix [zoomLevel="
                + zoomLevel
                + ", matrixWidth="
                + matrixWidth
                + ", matrixHeight="
                + matrixHeight
                + ", tileWidth="
                + tileWidth
                + ", tileHeight="
                + tileHeight
                + ", xPixelSize="
                + xPixelSize
                + ", yPixelSize="
                + yPixelSize
                + ", tiles="
                + tiles
                + "]";
    }
}
