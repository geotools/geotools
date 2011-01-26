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

import org.geotools.geometry.DirectPosition2D;
import org.opengis.geometry.DirectPosition;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Simple polygons like three - sided (triangle) or four - sided
 * (qadrilateral), that are used for triangulation.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Jan Jezek
 */
class Polygon {
    /** Vertices of this polygon. */
    private DirectPosition[] vertices;

    /**
     * Creates a polygon using specified vertices.
     *
     * @param coordinates of vertices
     */
    Polygon(DirectPosition[] coordinates) {
        this.vertices = coordinates;
    }

    /**
     * Sets the vertices of this polygon.
     *
     * @param coordinates
     */
    public void setCoordinates(DirectPosition[] coordinates) {
        this.vertices = coordinates;
    }

    /**
     * Returns vertices of this polygon.
     *
     * @return vertices of this polygon.
     */
    public DirectPosition[] getPoints() {
        return vertices;
    }

    /**
     * Returns the LINESTRING representation in WKT.
     *
     * @return WKT format.
     */
    public String toString() {
        String wkt = "";

        for (int i = 0; i < vertices.length; i++) {
            wkt = wkt + vertices[i].getCoordinates()[0] + " "
                + vertices[i].getCoordinates()[1];

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
     *
     * @return generated GeneralPath.
     */
    protected GeneralPath generateGeneralPath(DirectPosition[] points) {
        GeneralPath ring = new GeneralPath();

        // Set the initiakl coordinates of the general Path
        ring.moveTo((float) points[0].getCoordinates()[0],
            (float) points[0].getCoordinates()[1]);

        // Create the star. Note: this does not draw the star.
        for (int i = 1; i < points.length; i++) {
            ring.lineTo((float) points[i].getCoordinates()[0],
                (float) points[i].getCoordinates()[1]);
        }

        return ring;
    }

    /**
     * Test whether the coordinate is inside or is vertex of ploygon.
     *
     * @param dp Point to be tested whether is inside of polygon.
     *
     * @return True if the point is inside (or is the vertex of polygon, false
     *         if not.
     */
    protected boolean containsOrIsVertex(DirectPosition dp) {
        if (generateGeneralPath(vertices).contains((Point2D) dp)
                || (hasVertex(dp))) {
            return true;
        }

        return false;
    }

    /**
     * Returns whether v is one of the vertices of this polygon.
     *
     * @param p the candidate point
     *
     * @return whether v is equal to one of the vertices of this Triangle
     */
    public boolean hasVertex(DirectPosition p) {
        for (int i = 0; i < vertices.length; i++) {
            if (p == vertices[i]) {
                return true;
            }
        }

        return false;
    }

    /**
     * Enlarge the polygon using homothetic transformation method.
     *
     * @param scale of enlargement (when scale = 1 then polygon stays
     *        unchanged)
     */
    protected void enlarge(double scale) {
        double sumX = 0;
        double sumY = 0;

        for (int i = 0; i < vertices.length; i++) {
            sumX = sumX + vertices[i].getCoordinates()[0];
            sumY = sumY + vertices[i].getCoordinates()[1];
        }

        // The center of polygon is calculated.
        sumX = sumX / vertices.length;
        sumY = sumY / vertices.length;

        // The homothetic transformation is made.
        for (int i = 0; i < vertices.length; i++) {
            vertices[i].getCoordinates()[0] = (scale * (vertices[i]
                .getCoordinates()[0] - sumX)) + sumX;
            vertices[i].getCoordinates()[1] = (scale * (vertices[i]
                .getCoordinates()[1] - sumY)) + sumY;
        }
    }

    /**
     * Returns reduced coordinates of vertices so the first vertex has
     * [0,0] coordinats.
     *
     * @return The List of reduced vertives
     */
    protected List <DirectPosition> reduce() {
        //Coordinate[] redCoords = new Coordinate[coordinates.length];
        ArrayList <DirectPosition> redCoords = new ArrayList<DirectPosition>();

        for (int i = 0; i < vertices.length; i++) {
            redCoords.add(new DirectPosition2D(
                    vertices[i].getCoordinateReferenceSystem(),
                    vertices[i].getCoordinates()[0]
                    - vertices[0].getCoordinates()[0],
                    vertices[i].getCoordinates()[1]
                    - vertices[0].getCoordinates()[1]));
        }

        return redCoords;
    }

    /**
     * Returns whether this Polygon contains all of the the given
     * coordinates.
     *
     * @param coordinate of coordinates
     *
     * @return whether this Polygon contains the all of the given coordinates
     */
    protected boolean containsAll(List <DirectPosition> coordinate) {
        for (Iterator <DirectPosition> i = coordinate.iterator(); i.hasNext();) {
            if (!this.containsOrIsVertex((DirectPosition) i.next())) {
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
