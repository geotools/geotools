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

/** A rectangle using long values */
class RectangleLong {

    long minX;
    long maxX;
    long minY;
    long maxY;

    public RectangleLong(long minX, long maxX, long minY, long maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public long getMinX() {
        return minX;
    }

    public void setMinX(long minX) {
        this.minX = minX;
    }

    public long getMaxX() {
        return maxX;
    }

    public void setMaxX(long maxX) {
        this.maxX = maxX;
    }

    public long getMinY() {
        return minY;
    }

    public void setMinY(long minY) {
        this.minY = minY;
    }

    public long getMaxY() {
        return maxY;
    }

    public void setMaxY(long maxY) {
        this.maxY = maxY;
    }

    @Override
    public String toString() {
        return "RectangleLong{"
                + "minX="
                + minX
                + ", maxX="
                + maxX
                + ", minY="
                + minY
                + ", maxY="
                + maxY
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RectangleLong that = (RectangleLong) o;
        return minX == that.minX && minY == that.minY && maxX == that.maxX && maxY == that.maxY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minX, minY, maxX, maxY);
    }
}
