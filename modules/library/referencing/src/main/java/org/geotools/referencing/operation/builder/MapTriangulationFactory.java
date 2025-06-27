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

import java.util.HashMap;
import java.util.List;
import org.geotools.api.geometry.Position;
import org.geotools.geometry.Position2D;

/**
 * Implements methods for triangulation for {@linkplain org.geotools.referencing.operation.builder.RubberSheetBuilder
 * RubberSheeting} transformation.
 *
 * @since 2.4
 * @version $Id$
 * @author Jan Jezek
 */
class MapTriangulationFactory {
    private final Quadrilateral quad;
    private final List<MappedPosition> vectors;

    /**
     * @param quad defines the area for transformation.
     * @param vectors represents pairs of identical points.
     * @throws TriangulationException thrown when the source points are outside the quad.
     */
    public MapTriangulationFactory(Quadrilateral quad, List<MappedPosition> vectors) throws TriangulationException {
        this.quad = quad;
        this.vectors = vectors;
    }

    /**
     * Generates map of source and destination triangles.
     *
     * @return Map of a source and destination triangles.
     * @throws TriangulationException thrown when the source points are outside the quad.
     */
    @SuppressWarnings("NonApiType") // public method, can't return Map without risking breaking client code
    public HashMap<TINTriangle, Object> getTriangleMap() throws TriangulationException {
        Quadrilateral mQuad = mappedQuad(quad, vectors);

        ExtendedPosition[] vertices = new ExtendedPosition[vectors.size()];

        // converts MappedPosition to ExtendedPosition
        for (int i = 0; i < vectors.size(); i++) {
            vertices[i] = new ExtendedPosition(
                    vectors.get(i).getSource(), vectors.get(i).getTarget());
        }

        TriangulationFactory triangulator = new TriangulationFactory(mQuad, vertices);
        List<TINTriangle> taggedSourceTriangles = triangulator.getTriangulation();
        final HashMap<TINTriangle, Object> triangleMap = new HashMap<>();

        for (Object taggedSourceTriangle : taggedSourceTriangles) {
            final TINTriangle sourceTriangle = (TINTriangle) taggedSourceTriangle;
            triangleMap.put(
                    sourceTriangle,
                    new TINTriangle(
                            ((ExtendedPosition) sourceTriangle.p0).getMappedposition(),
                            ((ExtendedPosition) sourceTriangle.p1).getMappedposition(),
                            ((ExtendedPosition) sourceTriangle.p2).getMappedposition()));
        }

        return triangleMap;
    }

    /**
     * Generates mapped quad from destination quad and source quad. The vertices of destination quad are calculated from
     * source quad and difference of nearest pair of identical points.
     *
     * @param sourceQuad the quad that defines the area for triangulating.
     * @param vectors of identical points (MappedCoordinates).
     * @return destination quad
     */
    private Quadrilateral mappedQuad(Quadrilateral sourceQuad, List<MappedPosition> vectors) {
        if (vectors.isEmpty()) {
            return (Quadrilateral) sourceQuad.clone();
        }

        // super.setMappedPositions(vectors);
        MappedPosition[] mappedVertices = new MappedPosition[4];

        for (int i = 0; i < mappedVertices.length; i++) {
            mappedVertices[i] = generateCoordFromNearestOne(sourceQuad.getPoints()[i], vectors);
        }

        return new Quadrilateral(
                new ExtendedPosition(mappedVertices[0].getSource(), mappedVertices[0].getTarget()),
                new ExtendedPosition(mappedVertices[1].getSource(), mappedVertices[1].getTarget()),
                new ExtendedPosition(mappedVertices[2].getSource(), mappedVertices[2].getTarget()),
                new ExtendedPosition(mappedVertices[3].getSource(), mappedVertices[3].getTarget()));
    }

    /**
     * Calculate the destination position for the quad vertex as source position using the difference between nearest
     * source and destination point pair.
     *
     * @param x the original coordinate.
     * @param vertices List of the MappedPosition.
     * @return MappedPosition from the original and new coordinate, so the difference between them is the same as for
     *     the nearest one MappedPosition. It is used for calculating destination quad.
     */
    protected MappedPosition generateCoordFromNearestOne(Position x, List<MappedPosition> vertices) {
        MappedPosition nearestOne = nearestMappedCoordinate(x, vertices);

        double dstX = x.getCoordinate()[0]
                + (nearestOne.getTarget().getCoordinate()[0]
                        - nearestOne.getSource().getCoordinate()[0]);
        double dstY = x.getCoordinate()[1]
                + (nearestOne.getTarget().getCoordinate()[1]
                        - nearestOne.getSource().getCoordinate()[1]);
        Position dst = new Position2D(nearestOne.getTarget().getCoordinateReferenceSystem(), dstX, dstY);

        return new MappedPosition(x, dst);
    }

    /**
     * Returns the nearest MappedPosition to specified point P.
     *
     * @param dp P point.
     * @param vertices the List of MappedCoordinates.
     * @return the MappedPosition to the x Coordinate.
     */
    protected MappedPosition nearestMappedCoordinate(Position dp, List<MappedPosition> vertices) {
        Position2D x = new Position2D(dp);

        // Assert.isTrue(vectors.size() > 0);
        MappedPosition nearestOne = vertices.get(0);

        for (MappedPosition candidate : vertices) {
            if (((Position2D) candidate.getSource()).distance(x.toPoint2D())
                    < ((Position2D) nearestOne.getSource()).distance(x.toPoint2D())) {
                nearestOne = candidate;
            }
        }

        return nearestOne;
    }
}
