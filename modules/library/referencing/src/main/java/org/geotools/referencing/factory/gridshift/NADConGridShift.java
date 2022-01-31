/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.gridshift;

import java.io.Serializable;
import java.util.Objects;
import org.geotools.referencing.operation.builder.LocalizationGrid;

/**
 * A NADCON localization grid
 *
 * @author Andrea Aime - GeoSolutions
 * @author Rueben Schulz
 */
public class NADConGridShift extends LocalizationGrid implements Serializable {

    /** The minimum longitude value covered by this grid (decimal degrees) */
    private double minX;

    /** The minimum latitude value covered by this grid (decimal degrees) */
    private double minY;

    /** The maximum longitude value covered by this grid (decimal degrees) */
    private double maxX;

    /** The maximum latitude value covered by this grid (decimal degrees) */
    private double maxY;

    /** The difference between longitude grid points (decimal degrees) */
    private double dx;

    /** The difference between latitude grid points (decimal degrees) */
    private double dy;

    public NADConGridShift(
            double xmin,
            double ymin,
            double xmax,
            double ymax,
            double dx,
            double dy,
            int width,
            int height) {
        super(width, height);
        this.minX = xmin;
        this.maxX = xmax;
        this.minY = ymin;
        this.maxY = ymax;
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minX, minY, maxX, maxY, dx, dy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NADConGridShift that = (NADConGridShift) o;
        return Double.compare(that.minX, minX) == 0
                && Double.compare(that.minY, minY) == 0
                && Double.compare(that.maxX, maxX) == 0
                && Double.compare(that.maxY, maxY) == 0
                && Double.compare(that.dx, dx) == 0
                && Double.compare(that.dy, dy) == 0;
    }

    /** The minimum longitude value covered by this grid (decimal degrees) */
    public double getMinX() {
        return minX;
    }

    /** The minimum latitude value covered by this grid (decimal degrees) */
    public double getMinY() {
        return minY;
    }

    /** The maximum longitude value covered by this grid (decimal degrees) */
    public double getMaxX() {
        return maxX;
    }

    /** The maximum latitude value covered by this grid (decimal degrees) */
    public double getMaxY() {
        return maxY;
    }

    /** The difference between longitude grid points (decimal degrees) */
    public double getDx() {
        return dx;
    }

    /** The difference between latitude grid points (decimal degrees) */
    public double getDy() {
        return dy;
    }
}
