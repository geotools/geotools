package org.geotools.geopkg;

public class TileMatrix {

    Integer zoomLevel;
    Integer matrixWidth, matrixHeight;
    Integer tileWidth, tileHeight;
    Double xPixelSize;
    Double yPixelSize;

    public TileMatrix() {
    }

    public TileMatrix(Integer zoomLevel, Integer matrixWidth, Integer matrixHeight, 
        Integer tileWidth, Integer tileHeight, Double xPixelSize, Double yPixelSize) {
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
}
