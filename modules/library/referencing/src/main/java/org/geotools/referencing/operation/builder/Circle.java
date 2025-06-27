/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.builder;

import org.geotools.api.geometry.Position;
import org.geotools.geometry.Position2D;

/**
 * Simple Circle focused on Delaunays triangulation.
 *
 * @since 2.4
 * @version $Id$
 * @author Jan Jezek
 */
class Circle {
    /** Coordinates of center. */
    private Position2D center;

    /** Value of radius */
    private double radius;

    /** Tolerance for the cotains method. */
    private double tolerance = 0.0001;

    /** Creates a circle with center [0,0] and radius = 0. */
    protected Circle() {
        this(new Position2D(0, 0), 0);
    }

    /**
     * Creates a circle using the specified center and radius.
     *
     * @param center of the circle.
     * @param radius of the circle.
     */
    protected Circle(Position center, double radius) {
        this.center = new Position2D(center);
        this.radius = radius;
    }

    /**
     * Sets the center.
     *
     * @param center coordinates
     */
    protected void setCenter(Position center) {
        this.center = new Position2D(center);
    }

    /**
     * Sets the radius.
     *
     * @param radius value
     */
    protected void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * Returns the coordinate of the center.
     *
     * @return center coordinates
     */
    protected Position getCenter() {
        return center;
    }

    /**
     * Returns the radius.
     *
     * @return radius value.
     */
    protected double getRadius() {
        return radius;
    }

    /**
     * Sets the tolerance for the contains method.
     *
     * @param tolerance value
     */
    protected void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    /**
     * Raturns the tolerance
     *
     * @return tolerance value
     */
    protected double getTolerance() {
        return tolerance;
    }

    /**
     * The contains test whether the coordinate p is within the circle. Triangle contains coordinate if the distance
     * between center and p is smaller then the radius that is reduced by tolerance. This is used for triangulation when
     * there are four points on one circle to avoid neverending loop.
     *
     * @param p - the point to be tested
     * @return True if the circle contais p, False if not.
     */
    protected boolean contains(Position p) {
        if (center.distance(new Position2D(p)) < this.radius - tolerance) {
            return true;
        } else {
            return false;
        }
    }
}
