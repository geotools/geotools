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

import org.opengis.geometry.DirectPosition;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Generates TIN with respect to delaunay criterion. It
 * means that there are no alien vertices in  the circumcircle of each
 * triangle. The algorithm that is used is also known as incremental insertion.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Jan Jezek
 */
class TriangulationFactory {
    /** The list of TINTriangles in the TIN. */
    private List <TINTriangle> triangles;

    /**
     * Constructs the TriangulationFactory.
     *
     *    @param quad of location to be triangulated.
     *    @param pt Array of points for triangulation.
     *    @throws TriangulationException when the vertices are outside of the specified quad.
     */
    protected TriangulationFactory(Quadrilateral quad, DirectPosition[] pt)
        throws TriangulationException {
        List <DirectPosition> vertices = new ArrayList<DirectPosition>();

        for (int i = 0; i < pt.length; i++) {
            vertices.add(pt[i]);
        }

        if (quad.containsAll(vertices) == false) {
            throw new TriangulationException("Point is outside triangles");
        }

        this.triangles = quad.getTriangles();

        for (Iterator<DirectPosition> i = vertices.iterator(); i.hasNext();) {
            DirectPosition vertex = (DirectPosition) i.next();
            insertPoint(vertex);
        }
    }

    /**
     * Set a List of points for triangulation
     *
     * @return TIN as list of triangles.
     */
    public List <TINTriangle> getTriangulation() {
        return triangles;
    }

    /**
     * This is the test loop, that starts to tests of triangles until
     * the result of insertation of triangle evokes changes in TIN.
     *
     * @param ChangedTriangles List of changed triangles
     *
     * @throws TriangulationException TriangulationException
     */
    protected void recursiveDelaunayTest(List ChangedTriangles)
        throws TriangulationException {
        int i = ChangedTriangles.size();

        while (i != 0) {
            triangles.removeAll(ChangedTriangles);
            ChangedTriangles = insertTriangles(ChangedTriangles);
            i = ChangedTriangles.size();
        }
    }

    /**
     * Decides whether to insert triangle directly or go throught
     * delaunay test.
     *
     * @param trian if triangles to be inserted
     *
     * @return List of changed triangles
     *
     * @throws TriangulationException TriangulationException
     */
    protected List insertTriangles(List trian) throws TriangulationException {
        List ChangedTriangles = new ArrayList();

        for (Iterator i = trian.iterator(); i.hasNext();) {
            TINTriangle trig = (TINTriangle) i.next();

            if (trig.getAdjacentTriangles().size() <= 2) {
                // this triangles were generate by splitting one of a boundary triangle
                triangles.add(trig);
            } else {
                ChangedTriangles.addAll(delaunayCircleTest(trig));
            }
        }

        return ChangedTriangles;
    }

    /**
     * Tests whether there is a alien vertex in the circumcircle of
     * triangle. When there is, the diagonal of quad made by these triangles
     * changes.
     *
     * @param triangle to be tested
     *
     * @return List of changed triangles
     *
     * @throws TriangulationException DOCUMENT ME!
     */
    private List delaunayCircleTest(TINTriangle triangle)
        throws TriangulationException {
        List changedTriangles = new ArrayList();

        Iterator j = triangle.getAdjacentTriangles().iterator();
        int ct = changedTriangles.size();

        while (j.hasNext() && (changedTriangles.size() == ct)) {
            TINTriangle adjacent = (TINTriangle) j.next();

            List NewTriangles = new ArrayList();

            // The delaunay test
            if (triangle.getCircumCicle().contains(adjacent.p1)
                    || triangle.getCircumCicle().contains(adjacent.p0)
                    || triangle.getCircumCicle().contains(adjacent.p2)) {
                triangles.remove(triangle);
                triangles.remove(adjacent);

                NewTriangles.addAll(alternativeTriangles(triangle, adjacent));

                triangles.addAll(NewTriangles);
                changedTriangles = NewTriangles;
            } else if (!triangles.contains(triangle)) {
                triangles.add(triangle);
            }
        }

        return changedTriangles;
    }

    /**
     * Accommodate new vertex into the existing triangles.
     *
     * @param newVertex new vertex
     *
     * @throws TriangulationException when {@code newVertex} is outside triangles
     */
    public void insertPoint(DirectPosition newVertex)
        throws TriangulationException {
        TINTriangle triangleContainingNewVertex = triangleContains(newVertex);

        if (triangleContainingNewVertex == null) {
            throw new TriangulationException("Point is outside triangles");
        }

        triangles.remove(triangleContainingNewVertex);
        recursiveDelaunayTest(triangleContainingNewVertex.subTriangles(
                newVertex));
    }

    /**
     * Changes the diagonal of the quad generated from two
     * adjacent triangles.
     *
     * @param ABC triangle sharing an edge with BCD
     * @param BCD triangle sharing an edge with ABC
     *
     * @return triangles ABD and ADC, or null if ABCD is not convex
     *
     * @throws TriangulationException if {@code ABC} and {@code BCD} are not adjacent
     */
    private List alternativeTriangles(TINTriangle ABC, TINTriangle BCD)
        throws TriangulationException {
        ArrayList ABCvertices = new ArrayList();
        ArrayList BCDvertices = new ArrayList();

        ABCvertices.add(ABC.p0);
        ABCvertices.add(ABC.p1);
        ABCvertices.add(ABC.p2);
        BCDvertices.add(BCD.p0);
        BCDvertices.add(BCD.p1);
        BCDvertices.add(BCD.p2);

        ArrayList sharedVertices = new ArrayList();
        ArrayList unsharedVertices = new ArrayList();

        // Finds shared and unshared vertices
        for (Iterator i = ABCvertices.iterator(); i.hasNext();) {
            DirectPosition vertex = (DirectPosition) i.next();

            if (!BCDvertices.contains(vertex)) {
                unsharedVertices.add(vertex);
            } else if (BCDvertices.contains(vertex)) {
                sharedVertices.add(vertex);
                BCDvertices.remove(vertex);
            } else {
                throw new TriangulationException("should never reach here");
            }
        }

        unsharedVertices.addAll(BCDvertices);

        if (sharedVertices.size() < 2) {
            throw new TriangulationException(
                "Unable to make alternative triangles");
        }

        // remove Adjacent from original triangles
        ABC.removeAdjacent(BCD);
        BCD.removeAdjacent(ABC);

        // new triangles are generated
        TINTriangle trigA = new TINTriangle((DirectPosition) sharedVertices.get(
                    0), (DirectPosition) unsharedVertices.get(0),
                (DirectPosition) unsharedVertices.get(1));
        TINTriangle trigB = new TINTriangle((DirectPosition) unsharedVertices
                .get(0), (DirectPosition) unsharedVertices.get(1),
                (DirectPosition) sharedVertices.get(1));

        // Adjacent triangles are added
        trigA.addAdjacentTriangle(trigB);
        trigB.addAdjacentTriangle(trigA);
        trigA.tryToAddAdjacent(BCD.getAdjacentTriangles());
        trigA.tryToAddAdjacent(ABC.getAdjacentTriangles());
        trigB.tryToAddAdjacent(BCD.getAdjacentTriangles());
        trigB.tryToAddAdjacent(ABC.getAdjacentTriangles());

        List list = new ArrayList();
        list.add(trigA);
        list.add(trigB);

        // Adjacent triangles of adjacent triangles are modified.
        for (Iterator i = ABC.getAdjacentTriangles().iterator(); i.hasNext();) {
            TINTriangle trig = (TINTriangle) i.next();
            trig.removeAdjacent(ABC);
            trig.tryToAddAdjacent(list);
        }

        for (Iterator i = BCD.getAdjacentTriangles().iterator(); i.hasNext();) {
            TINTriangle trig = (TINTriangle) i.next();
            trig.removeAdjacent(BCD);
            trig.tryToAddAdjacent(list);
        }

        return list;
    }

    /**
     * Returns the TINTriangle that contains the p Coordinate.
     *
     * @param p The Coordinate to be tested
     *
     * @return the triangle containing p, or null if there is no triangle that
     *         contains p
     */
    private TINTriangle triangleContains(DirectPosition p) {
        for (Iterator i = triangles.iterator(); i.hasNext();) {
            TINTriangle triangle = (TINTriangle) i.next();

            if (triangle.containsOrIsVertex(p)) {
                return triangle;
            }
        }

        return null;
    }
}
