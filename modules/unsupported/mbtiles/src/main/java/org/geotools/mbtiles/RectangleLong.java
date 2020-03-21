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

    @FunctionalInterface
    public static interface PositionConsumer {
        void accept(long x, long y);
    }

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

    public RectangleLong() {
        setToNull();
    }

    public void setToNull() {
        this.minX = 0;
        this.maxX = -1;
        this.minY = 0;
        this.maxY = -1;
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

    /** Expands envelopes to include the specified location */
    public void expandToInclude(MBTilesTileLocation loc) {
        if (isNull()) {
            minX = maxX = loc.getTileColumn();
            minY = maxY = loc.getTileRow();
        } else {
            long x = loc.getTileColumn();
            long y = loc.getTileRow();
            if (x < minX) {
                minX = x;
            } else if (x > maxX) {
                maxX = x;
            }
            if (y < minY) {
                minY = y;
            } else if (y > maxY) {
                maxY = y;
            }
        }
    }

    public void expandToInclude(RectangleLong other) {
        if (isNull()) {
            init(other);
        } else {
            if (other.minX < minX) {
                minX = other.minX;
            }
            if (other.maxX > maxX) {
                maxX = other.maxX;
            }
            if (other.minY < minY) {
                minY = other.minY;
            }
            if (other.maxY > maxY) {
                maxY = other.maxY;
            }
        }
    }

    protected void init(RectangleLong other) {
        this.minX = other.minX;
        this.maxX = other.maxX;
        this.minY = other.minY;
        this.maxY = other.maxY;
    }

    /** Computes the intersection between two rectangles */
    public RectangleLong intersection(RectangleLong other) {
        if (!this.isNull() && !other.isNull() && this.intersects(other)) {
            long intMinX = this.minX > other.minX ? this.minX : other.minX;
            long intMinY = this.minY > other.minY ? this.minY : other.minY;
            long intMaxX = this.maxX < other.maxX ? this.maxX : other.maxX;
            long intMaxY = this.maxY < other.maxY ? this.maxY : other.maxY;
            return new RectangleLong(intMinX, intMaxX, intMinY, intMaxY);
        } else {
            return new RectangleLong();
        }
    }

    /** Returns true if the two rectangles intersect, false otherwise */
    public boolean intersects(RectangleLong other) {
        if (!this.isNull() && !other.isNull()) {
            return other.minX <= this.maxX
                    && other.maxX >= this.minX
                    && other.minY <= this.maxY
                    && other.maxY >= this.minY;
        } else {
            return false;
        }
    }

    public boolean isNull() {
        return this.maxX < this.minX || this.maxY < this.minY;
    }

    public void forEach(PositionConsumer consumer) {
        for (long y = minY; y <= maxY; y++) {
            for (long x = minX; x <= maxX; x++) {
                consumer.accept(x, y);
            }
        }
    }
}
