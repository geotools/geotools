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
 */
package org.geotools.mbtiles;

import java.util.Objects;

/**
 * Location of a tile in the MBTiles package. Mind, the axis order is the TMS one, row starts at the
 * bottom, south, and goes up towards north.
 */
class MBTilesTileLocation {
    // the ordinates
    protected long zoomLevel;
    protected long tileColumn;
    protected long tileRow;

    public MBTilesTileLocation(long zoomLevel, long tileColumn, long tileRow) {
        this.zoomLevel = zoomLevel;
        this.tileColumn = tileColumn;
        this.tileRow = tileRow;
    }

    public long getZoomLevel() {
        return zoomLevel;
    }

    public long getTileColumn() {
        return tileColumn;
    }

    public long getTileRow() {
        return tileRow;
    }

    public void setZoomLevel(long zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    public void setTileColumn(long tileColumn) {
        this.tileColumn = tileColumn;
    }

    public void setTileRow(long tileRow) {
        this.tileRow = tileRow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MBTilesTileLocation that = (MBTilesTileLocation) o;
        return zoomLevel == that.zoomLevel
                && tileColumn == that.tileColumn
                && tileRow == that.tileRow;
    }

    @Override
    public int hashCode() {
        return Objects.hash(zoomLevel, tileColumn, tileRow);
    }

    @Override
    public String toString() {
        return "MBTilesTileLocation{"
                + "zoomLevel="
                + zoomLevel
                + ", tileColumn="
                + tileColumn
                + ", tileRow="
                + tileRow
                + '}';
    }
}
