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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.geotools.api.geometry.Position;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.geometry.Position2D;

/**
 * A triangle, with special methods for use with RubberSheetTransform.
 *
 * @since 2.4
 * @version $Id$
 * @author Jan Jezek
 */
class TINTriangle extends Polygon {
    /** The first vertex. */
    public Position p0;

    /** The second vertex. */
    public Position p1;

    /** The third vertex. */
    public Position p2;

    private final List<TINTriangle> adjacentTriangles = new ArrayList<>();

    /**
     * Creates a Triangle.
     *
     * @param p0 one vertex
     * @param p1 another vertex
     * @param p2 another vertex
     */
    protected TINTriangle(Position p0, Position p1, Position p2) {
        super(p0, p1, p2, p0);
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
    }

    /**
     * Returns the CircumCicle of the this TINTriangle.
     *
     * @return Returns the CircumCicle of the this TINTriangle
     */
    protected Circle getCircumCicle() {
        // DirectPosition center = new DirectPosition2D();
        List<Position> reducedVertices = reduce();

        CoordinateReferenceSystem crs = reducedVertices.get(1).getCoordinateReferenceSystem();

        double x1 = reducedVertices.get(1).getCoordinate()[0];
        double y1 = reducedVertices.get(1).getCoordinate()[1];

        double x2 = reducedVertices.get(2).getCoordinate()[0];
        double y2 = reducedVertices.get(2).getCoordinate()[1];

        // Calculation of Circumcicle center
        double t = (0.5 * (((x1 * x1) + (y1 * y1)) - (x1 * x2) - (y1 * y2))) / ((y1 * x2) - (x1 * y2));

        // t = Math.abs(t);
        Position2D center = new Position2D(
                crs, (x2 / 2) - (t * y2) + p0.getCoordinate()[0], (y2 / 2) + (t * x2) + p0.getCoordinate()[1]);

        return new Circle(center.getDirectPosition(), center.distance(new Position2D(p0)));
    }

    /**
     * Returns the three triangles that are created by splitting this TINTriangle at a newVertex.
     *
     * @param newVertex the split point (must be inside triangle).
     * @return three Triangles created by splitting this TINTriangle at a newVertex.
     */
    public List<TINTriangle> subTriangles(Position newVertex) {
        ArrayList<TINTriangle> triangles = new ArrayList<>();
        TINTriangle trigA = new TINTriangle(p0, p1, newVertex);
        TINTriangle trigB = new TINTriangle(p1, p2, newVertex);
        TINTriangle trigC = new TINTriangle(p2, p0, newVertex);

        // sub triangles are adjacent to each other.
        try {
            trigA.addAdjacentTriangle(trigB);
            trigA.addAdjacentTriangle(trigC);

            trigB.addAdjacentTriangle(trigA);
            trigB.addAdjacentTriangle(trigC);

            trigC.addAdjacentTriangle(trigA);
            trigC.addAdjacentTriangle(trigB);
        } catch (TriangulationException e) {
            // should never reach here so we can ignore.
        }

        // last adjacent triangle of each sub triangle is one of adjacent triangle of this triangle.
        trigA.tryToAddAdjacent(this.getAdjacentTriangles());
        trigB.tryToAddAdjacent(this.getAdjacentTriangles());
        trigC.tryToAddAdjacent(this.getAdjacentTriangles());

        triangles.add(trigA);
        triangles.add(trigB);
        triangles.add(trigC);

        Iterator<TINTriangle> i = this.getAdjacentTriangles().iterator();

        while (i.hasNext()) {
            TINTriangle trig = i.next();
            trig.removeAdjacent(this);
        }

        return triangles;
    }

    /**
     * Tries to add {@code adjacents} triangles. Before adding they are checked whether they are really adjacent or not
     * and whether they are not already known.
     *
     * @param adjacents triangles to be added
     * @return number of successfully added triangles
     */
    protected int tryToAddAdjacent(List<TINTriangle> adjacents) {
        Iterator<TINTriangle> i = adjacents.iterator();
        int count = 0;

        while (i.hasNext()) {
            try {
                TINTriangle candidate = i.next();

                if (candidate.isAdjacent(this) && !this.adjacentTriangles.contains(candidate)) {
                    this.addAdjacentTriangle(candidate);
                }

                if (candidate.isAdjacent(this) && !candidate.adjacentTriangles.contains(this)) {
                    // this.addAdjacentTriangle(candidate);
                    candidate.addAdjacentTriangle(this);
                    count++;
                }
            } catch (TriangulationException e) {
                // should never reach here so we can ignore
            }
        }

        return count;
    }

    /**
     * Adds adjacent triangles.
     *
     * @param adjacent triangles to be added
     * @throws TriangulationException if triangle is not adjacent
     * @return true if the triangle is adjacent
     */
    protected boolean addAdjacentTriangle(TINTriangle adjacent) throws TriangulationException {
        if (isAdjacent(adjacent)) {
            adjacentTriangles.add(adjacent);

            return true;
        }

        return false;
    }

    /**
     * Checks whether triangle is adjacent.
     *
     * @param adjacent triangle to be tested
     * @return true if triangle is adjacent
     * @throws TriangulationException if
     */
    private boolean isAdjacent(TINTriangle adjacent) throws TriangulationException {
        int identicalVertices = 0;

        if (adjacent.hasVertex(p0)) {
            identicalVertices++;
        }

        if (adjacent.hasVertex(p1)) {
            identicalVertices++;
        }

        if (adjacent.hasVertex(p2)) {
            identicalVertices++;
        }

        if (identicalVertices == 3) {
            throw new TriangulationException("Same triangle");
        }

        if (identicalVertices == 2) {
            return true;
        }

        return false;
    }

    /**
     * Returns adjacent triangles
     *
     * @return adjacent triangles
     */
    public List<TINTriangle> getAdjacentTriangles() {
        return adjacentTriangles;
    }

    /** Removes adjacent triangles */
    protected void removeAdjacent(TINTriangle remAdjacent) {
        adjacentTriangles.remove(remAdjacent);
    }
}
