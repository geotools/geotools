/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.geometry.MismatchedReferenceSystemException;
import org.geotools.api.geometry.Position;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.geometry.GeneralBounds;
import org.geotools.referencing.CRS;

/**
 * Builds a RubberSheet transformation from a set of control points, defined as a List of
 * {@linkplain org.geotools.referencing.operation.builder.MappedPosition MappedPosition} objects, and a quadrilateral
 * delimiting the outer area of interest, defined as a List of four {@linkplain Position DirectPosition} objects.
 *
 * <p>An explanation of the RubberSheet transformation algorithm can be seen <a href
 * ="http://planner.t.u-tokyo.ac.jp/member/fuse/rubber_sheeting.pdf">here</a>.
 *
 * @since 2.4
 * @version $Id$
 * @author Jan Jezek
 * @author Adrian Custer
 */
public class RubberSheetBuilder extends MathTransformBuilder {

    /* Map of the original and destination triangles. */
    private HashMap<TINTriangle, Object> trianglesMap;

    /* Map of a original triangles and associated AffineTransformation.*/
    private Map<TINTriangle, Object> trianglesToKeysMap;

    /**
     * Creates the Builder from a List of control points and a List of four DirectPositions defining the vertexes of the
     * area for interpolation.
     *
     * @param vectors A List of {@linkplain org.geotools.referencing.operation.builder.MappedPosition MappedPosition}
     * @param vertices A List with four points defining the quadrilateral in the region of interest.
     */
    public RubberSheetBuilder(List<MappedPosition> vectors, List<Position> vertices)
            throws IllegalArgumentException, MismatchedDimensionException, MismatchedReferenceSystemException,
                    TriangulationException {

        // Validates the vectors parameter while setting it
        super.setMappedPositions(vectors);

        // Validate the vertices parameter
        if (vertices.size() != 4) {
            throw new IllegalArgumentException("The region of interest must have four vertices.");
        }

        // Get the DirectPositions (In Java 1.4 we fail hard on this cast.)
        Position[] ddpp = new Position[4];
        for (int i = 0; i < vertices.size(); i++) {
            ddpp[i] = vertices.get(i);
        }

        // Check they have a common crs;
        CoordinateReferenceSystem crs;
        try {
            crs = getSourceCRS();
        } catch (FactoryException e) {
            // Can't fetch the CRS. Use the one from the first region of interest point instead.
            crs = ddpp[0].getCoordinateReferenceSystem();
        }
        if (!(CRS.equalsIgnoreMetadata(crs, ddpp[0].getCoordinateReferenceSystem())
                || CRS.equalsIgnoreMetadata(crs, ddpp[1].getCoordinateReferenceSystem())
                || CRS.equalsIgnoreMetadata(crs, ddpp[2].getCoordinateReferenceSystem())
                || CRS.equalsIgnoreMetadata(crs, ddpp[3].getCoordinateReferenceSystem()))) {
            throw new MismatchedReferenceSystemException("Region of interest defined by mismatched DirectPositions.");
        }

        // Check the vectors are inside the vertices.
        //  This is a quick check by envelope, can be more rigorous when we move
        //  to n dimensional operations.
        Position[] dpa = this.getSourcePoints();
        GeneralBounds srcextnt = new GeneralBounds(2);
        for (Position directPosition : dpa) {
            srcextnt.add(directPosition);
        }
        GeneralBounds vtxextnt = new GeneralBounds(2);
        vtxextnt.add(ddpp[0]);
        vtxextnt.add(ddpp[1]);
        vtxextnt.add(ddpp[2]);
        vtxextnt.add(ddpp[3]);
        if (!vtxextnt.contains(srcextnt, true))
            throw new IllegalArgumentException("The region of interest must contain the control points");

        Quadrilateral quad = new Quadrilateral(ddpp[0], ddpp[1], ddpp[2], ddpp[3]);

        MapTriangulationFactory trianglemap = new MapTriangulationFactory(quad, vectors);

        this.trianglesMap = trianglemap.getTriangleMap();
        this.trianglesToKeysMap = mapTrianglesToKey();
    }

    /**
     * Returns the minimum number of points required by this builder.
     *
     * @return 1
     */
    @Override
    public int getMinimumPointCount() {
        return 1;
    }

    /**
     * Returns the map of source and destination triangles.
     *
     * @return The Map of source and destination triangles.
     */
    public Map<TINTriangle, Object> getMapTriangulation() {
        return trianglesMap;
    }

    /**
     * Returns MathTransform transformation setup as RubberSheet.
     *
     * @return calculated MathTransform
     * @throws FactoryException when the size of source and destination point is not the same.
     */
    @Override
    protected MathTransform computeMathTransform() throws FactoryException {
        return new RubberSheetTransform(trianglesToKeysMap);
    }

    /**
     * Calculates affine transformation parameters from the pair of triangles.
     *
     * @return The HashMap where the keys are the original triangles and values are AffineTransformation Objects.
     */
    private Map<TINTriangle, Object> mapTrianglesToKey() {
        AffineTransformBuilder calculator;

        @SuppressWarnings("unchecked")
        HashMap<TINTriangle, Object> trianglesToKeysMap = (HashMap<TINTriangle, Object>) trianglesMap.clone();

        Iterator<Map.Entry<TINTriangle, Object>> it =
                trianglesToKeysMap.entrySet().iterator();

        while (it.hasNext()) {

            Map.Entry<TINTriangle, Object> a = it.next();
            List<MappedPosition> pts = new ArrayList<>();

            for (int i = 1; i <= 3; i++) {
                pts.add(new MappedPosition(a.getKey().getPoints()[i], ((TINTriangle) a.getValue()).getPoints()[i]));
            }

            try {
                calculator = new AffineTransformBuilder(pts);
                a.setValue(calculator.getMathTransform());
            } catch (Exception e) {
                // should never reach here because AffineTransformBuilder(pts)
                // should not throw any Exception.
                java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            }
        }

        return trianglesToKeysMap;
    }
}
