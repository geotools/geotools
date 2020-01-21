/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

package com.esri.sde.sdk.client;

import java.awt.image.DataBufferByte;

public class SeRasterAttr {

    public SeRasterAttr(boolean b) {}

    public int getPixelType() throws SeException {
        return -1;
    }

    public int getTileHeight() {
        return -1;
    }

    public int getTileWidth() {
        return -1;
    }

    public SeRasterBand[] getBands() throws SeException {
        return null;
    }

    public int getMaxLevel() {
        return -1;
    }

    public boolean skipLevelOne() {
        return false;
    }

    public SeExtent getExtentByLevel(int i) throws SeException {
        return null;
    }

    public int getImageWidthByLevel(int i) {
        return -1;
    }

    public int getImageHeightByLevel(int i) {
        return -1;
    }

    public SDEPoint getImageOffsetByLevel(int i) {
        return null;
    }

    public int getTilesPerRowByLevel(int i) {
        return -1;
    }

    public int getTilesPerColByLevel(int i) {
        return -1;
    }

    public int getNumBands() {
        return -1;
    }

    public SeRasterBand getBandInfo(int i) throws SeException {
        return null;
    }

    public SeObjectId getRasterColumnId() {
        return null;
    }

    public SeExtent getExtent() throws SeException {
        return null;
    }

    public SeRaster getRasterInfo() throws SeException, CloneNotSupportedException {
        return null;
    }

    public void setImageSize(int h, int w, int d) {}

    public void setTileSize(int w, int h) {}

    public void setPixelType(int p) {}

    public void setCompressionType(int c) {}

    public void setMaskMode(boolean b) {}

    public void setImportMode(boolean b) {}

    public void setExtent(SeExtent ext) {}

    public void setRasterProducer(SeRasterProducer p) {}

    public void setPyramidInfo(int maxLevel, boolean skipLevelOne, int interpolation)
            throws SeException {}

    public void setColorMap(int colorMapType, DataBufferByte dataBuffer) throws SeException {}

    public int getImageWidth() throws SeException {
        return 0;
    }

    public int getImageHeight() throws SeException {
        return 0;
    }

    public int getCompressionType() {
        return 0;
    }

    public SDEPoint getTileOrigin() {
        return null;
    }

    public int getInterpolation() {
        return 0;
    }

    public int getBandWidth() {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getBandHeight() {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getInterleave() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setInterleave(boolean b, int se_raster_interleave_bsq) {
        // TODO Auto-generated method stub

    }

    public SeObjectId getRasterId() throws SeException {
        // TODO Auto-generated method stub
        return null;
    }

    public SDEPoint getExtentOffsetByLevel(int level) throws SeException {
        // TODO Auto-generated method stub
        return null;
    }
}
