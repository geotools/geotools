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

package org.geotools.mbtiles;

public class MBTilesTile {

    // the ordinates
    protected final long ZoomLevel;
    protected final long TileColumn;
    protected final long TileRow;

    // the data
    protected byte[] data = null;

    public MBTilesTile(long zoomLevel, long tileColumn, long tileRow) {
        ZoomLevel = zoomLevel;
        TileColumn = tileColumn;
        TileRow = tileRow;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public long getZoomLevel() {
        return ZoomLevel;
    }

    public long getTileColumn() {
        return TileColumn;
    }

    public long getTileRow() {
        return TileRow;
    }
}
