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
package org.geotools.ows.wmts.model;

/**
 * Range subset for a layer in a TileMatrixSet.
 *
 * <p>To inform the client about the valid range of the TileCol and Tile Row indices a layer
 * definition can optionally use the tileMatrixSetLimits section that specifies a minimum and a
 * maximum that are limits of these indices for each TileMatrix.
 *
 * @author ian
 * @author Emanuele Tajariol (etj at geo-solutions dot it)
 */
public class TileMatrixLimits {

    String tileMatix;

    long minrow, maxrow, mincol, maxcol;

    /** @return the tileMatix */
    public String getTileMatix() {
        return tileMatix;
    }

    /** @param tileMatix the tileMatix to set */
    public void setTileMatix(String tileMatix) {
        this.tileMatix = tileMatix;
    }

    /** @return the minrow */
    public long getMinrow() {
        return minrow;
    }

    /** @param minrow the minrow to set */
    public void setMinRow(long minrow) {
        this.minrow = minrow;
    }

    /** @return the maxrow */
    public long getMaxrow() {
        return maxrow;
    }

    /** @param maxrow the maxrow to set */
    public void setMaxRow(long maxrow) {
        this.maxrow = maxrow;
    }

    /** @return the mincol */
    public long getMincol() {
        return mincol;
    }

    /** @param mincol the mincol to set */
    public void setMinCol(long mincol) {
        this.mincol = mincol;
    }

    /** @return the maxcol */
    public long getMaxcol() {
        return maxcol;
    }

    /** @param maxcol the maxcol to set */
    public void setMaxCol(long maxcol) {
        this.maxcol = maxcol;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return tileMatix + " c:" + mincol + "-" + maxcol + " r:" + minrow + "-" + maxrow;
    }
}
