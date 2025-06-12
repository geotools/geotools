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

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import org.geotools.api.geometry.Position;
import org.geotools.geometry.Position2D;

/**
 * Simple polygons like three - sided (triangle) or four - sided (qadrilateral), that are used for triangulation.
 *
 * @since 2.4
 * @version $Id$
 * @author Jan Jezek
 */
class Polygon implements Cloneable {
    /** Vertices of this polygon. */
    private Position[] vertices;

    /**
     * Creates a polygon using specified vertices.
     *
     * @param coordinates of vertices
     */
    Polygon(Position... coordinates) {
        this.vertices = coordinates;
    }

    /** Sets the vertices of this polygon. */
    public void setCoordinates(Position... coordinates) {
        this.vertices = coordinates;
    }

    /**
     * Returns vertices of this polygon.
     *
     * @return vertices of this polygon.
     */
    public Position[] getPoints() {
        return vertices;
    }

    /**
     * Returns the LINESTRING representation in WKT.
     *
     * @return WKT format.
     */
    @Override
    public String toString() {
        String wkt = "";

        for (int i = 0; i < vertices.length; i++) {
            wkt = wkt + vertices[i].getCoordinate()[0] + " " + vertices[i].getCoordinate()[1];

            if (i != (vertices.length - 1)) {
                wkt = wkt + ", ";
            }
        }

        return "LINESTRING (" + wkt + ")";
    }

    /**
     * Generates GeneralPath from the array of points.
     *
     * @param points vertices of polygon.
     * @return generated GeneralPath.
     */
    protected GeneralPath generateGeneralPath(Position... points) {
        GeneralPath ring = new GeneralPath();

        // Set the initiakl coordinates of the general Path
        ring.moveTo((float) points[0].getCoordinate()[0], (float) points[0].getCoordinate()[1]);

        // Create the star. Note: this does not draw the star.
        for (int i = 1; i < points.length; i++) {
            ring.lineTo((float) points[i].getCoordinate()[0], (float) points[i].getCoordinate()[1]);
        }

        return ring;
    }

    /**
     * Test whether the coordinate is inside or is vertex of ploygon.
     *
     * @param dp Point to be tested whether is inside of polygon.
     * @return True if the point is inside (or is the vertex of polygon, false if not.
     */
    protected boolean containsOrIsVertex(Position dp) {
        if (generateGeneralPath(vertices).contains((Point2D) dp) || hasVertex(dp)) {
            return true;
        }

        return false;
    }

    /**
     * Returns whether v is one of the vertices of this polygon.
     *
     * @param p the candidate point
     * @return whether v is equal to one of the vertices of this Triangle
     */
    public boolean hasVertex(Position p) {
        for (Position vertex : vertices) {
            if (p == vertex) {
                return true;
            }
        }

        return false;
    }

    /**
     * Enlarge the polygon using homothetic transformation method.
     *
     * @param scale of enlargement (when scale = 1 then polygon stays unchanged)
     */
    protected void enlarge(double scale) {
        double sumX = 0;
        double sumY = 0;

        for (Position directPosition : vertices) {
            sumX = sumX + directPosition.getCoordinate()[0];
            sumY = sumY + directPosition.getCoordinate()[1];
        }

        // The center of polygon is calculated.
        sumX = sumX / vertices.length;
        sumY = sumY / vertices.length;

        // The homothetic transformation is made.
        for (Position vertex : vertices) {
            vertex.getCoordinate()[0] = (scale * (vertex.getCoordinate()[0] - sumX)) + sumX;
            vertex.getCoordinate()[1] = (scale * (vertex.getCoordinate()[1] - sumY)) + sumY;
        }
    }

    /**
     * Returns reduced coordinates of vertices so the first vertex has [0,0] coordinats.
     *
     * @return The List of reduced vertives
     */
    protected List<Position> reduce() {
        // Coordinate[] redCoords = new Coordinate[coordinates.length];
        ArrayList<Position> redCoords = new ArrayList<>();

        for (Position vertex : vertices) {
            redCoords.add(new Position2D(
                    vertex.getCoordinateReferenceSystem(),
                    vertex.getCoordinate()[0] - vertices[0].getCoordinate()[0],
                    vertex.getCoordinate()[1] - vertices[0].getCoordinate()[1]));
        }

        return redCoords;
    }

    /**
     * Returns whether this Polygon contains all of the the given coordinates.
     *
     * @param coordinate of coordinates
     * @return whether this Polygon contains the all of the given coordinates
     */
    protected boolean containsAll(List<Position> coordinate) {
        for (Position directPosition : coordinate) {
            if (!this.containsOrIsVertex(directPosition)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns a copy of this.
     *
     * @return copy of this object.
     */
    @Override
    public Polygon clone() {
        return new Polygon(vertices);
    }
}
