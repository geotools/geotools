/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.geometry;

import org.geotools.geometry.jts.GeometryBuilder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;

/**
 * Based on Vladimir Agafonkin's Algorithm https://www.mapbox.com/blog/polygon-center/
 *
 * <p>Implementation of quadtree cells for "Pole of inaccessibility".
 *
 * @author Ian Turton
 * @author Casper Børgesen
 */
public class Cell implements Comparable<Cell> {
    private static final GeometryBuilder GB = new GeometryBuilder();

    private static final double SQRT2 = 1.4142135623730951;

    private double x;

    private double y;

    private double h;

    private double d;

    private double max;

    Cell(double x, double y, double h, MultiPolygon polygon) {

        this.setX(x); // cell center x
        this.setY(y); // cell center y
        this.setH(h); // half the cell size
        Point p = GB.point(x, y);

        // distance from cell center to polygon
        this.setD(pointToPolygonDist(p, polygon));

        // max distance to polygon within a cell
        this.setMax(this.getD() + this.getH() * SQRT2);
    }

    @Override
    public int compareTo(Cell o) {

        return (int) (o.getMax() - getMax());
    }

    public Point getPoint() {
        return GB.point(x, y);
    }

    // signed distance from point to polygon outline (negative if point is
    // outside)
    private double pointToPolygonDist(Point point, MultiPolygon polygon) {

        boolean inside = false;
        double minDistSq = Double.POSITIVE_INFINITY;

        for (int k = 0; k < polygon.getNumGeometries(); k++) {
            Coordinate[] ring = polygon.getGeometryN(k).getCoordinates();
            for (int i = 0, len = ring.length, j = len - 1; i < len; j = i++) {
                Coordinate a = ring[i];
                Coordinate b = ring[j];
                if ((a.y > y != b.y > y) && (x < (b.x - a.x) * (y - a.y) / (b.y - a.y) + a.x))
                    inside = !inside;
                minDistSq = Math.min(minDistSq, getSegDistSq(x, y, a, b));
            }
        }

        // Points outside has a negative distance and thus will be weighted down later.
        return (inside ? 1 : -1) * Math.sqrt(minDistSq);
    }
    // get squared distance from a point to a segment
    private double getSegDistSq(double px, double py, Coordinate a, Coordinate b) {

        double x = a.x;
        double y = a.y;
        double dx = b.x - x;
        double dy = b.y - y;

        if (dx != 0.0 || dy != 0.0) {

            double t = ((px - x) * dx + (py - y) * dy) / (dx * dx + dy * dy);

            if (t > 1) {
                x = b.x;
                y = b.y;

            } else if (t > 0) {
                x += dx * t;
                y += dy * t;
            }
        }

        dx = px - x;
        dy = py - y;

        return dx * dx + dy * dy;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
