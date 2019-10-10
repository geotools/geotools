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

import java.util.HashMap;
import java.util.Map;

public class MBTilesGrid {

    // the ordinates
    protected final long ZoomLevel;
    protected final long TileColumn;
    protected final long TileRow;

    // the data
    protected byte[] grid = null;
    protected Map<String, String> gridData = new HashMap<String, String>();

    public MBTilesGrid(long zoomLevel, long tileColumn, long tileRow) {
        ZoomLevel = zoomLevel;
        TileColumn = tileColumn;
        TileRow = tileRow;
    }

    public byte[] getGrid() {
        return grid;
    }

    public void setGrid(byte[] grid) {
        this.grid = grid;
    }

    public String getGridDataKey(String key) {
        return gridData.get(key);
    }

    public void setGridDataKey(String key, String value) {
        gridData.put(key, value);
    }

    public Map<String, String> getGridData() {
        return gridData;
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
