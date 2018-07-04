/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015-2017, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2010, Refractions Research Inc.
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
package org.geotools.tile.impl;

/**
 * ZoomLevel is a class in dire need of documentation.
 *
 * @author Tobias Sauerwein
 * @since 12
 */
public abstract class ZoomLevel {

    private int zoomLevel;

    protected int maxTilePerRowNumber;

    protected int maxTilePerColNumber;

    protected long maxTileNumber;

    public ZoomLevel() {}

    public ZoomLevel(int zoomLevel) {
        setZoomLevel(zoomLevel);
    }

    protected void setZoomLevel(int zoomLevel) {
        if (zoomLevel < 0) {
            throw new IllegalArgumentException("Zoom level must be >= 0.");
        }
        this.zoomLevel = zoomLevel;

        this.maxTilePerRowNumber = calculateMaxTilePerRowNumber(zoomLevel);
        this.maxTilePerColNumber = calculateMaxTilePerColNumber(zoomLevel);

        this.maxTileNumber = calculateMaxTileNumber();
    }

    public abstract int calculateMaxTilePerRowNumber(int zoomLevel);

    public abstract int calculateMaxTilePerColNumber(int zoomLevel);

    public long calculateMaxTileNumber() {
        return ((long) (maxTilePerColNumber)) * ((long) (maxTilePerRowNumber));
    }

    public int getZoomLevel() {
        return zoomLevel;
    }

    public int getMaxTilePerRowNumber() {
        return maxTilePerRowNumber;
    }

    public int getMaxTilePerColNumber() {
        return maxTilePerColNumber;
    }

    public long getMaxTileNumber() {
        return maxTileNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ZoomLevel)) {
            return false;
        }

        ZoomLevel other = (ZoomLevel) obj;

        return zoomLevel == other.zoomLevel;
    }
}
