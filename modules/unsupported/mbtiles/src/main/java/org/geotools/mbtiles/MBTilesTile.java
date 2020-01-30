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

public class MBTilesTile extends MBTilesTileLocation {

    // the data
    protected byte[] data = null;

    public MBTilesTile(long zoomLevel, long tileColumn, long tileRow) {
        super(zoomLevel, tileColumn, tileRow);
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public MBTilesTileLocation toLocation() {
        return new MBTilesTileLocation(zoomLevel, tileColumn, tileRow);
    }
}
