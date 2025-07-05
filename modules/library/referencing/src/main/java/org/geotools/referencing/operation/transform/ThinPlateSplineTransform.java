/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.transform;

import java.util.List;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.geometry.Position;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.Matrix;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.Position2D;
import org.geotools.referencing.operation.builder.MappedPosition;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.locationtech.jts.geom.Coordinate;

/**
 * A 2D transformation using Thin Plate Splines (TPS) for smoothly interpolating between a set of control points. This
 * transform is useful for georeferencing scanned maps or warping images based on Ground Control Points (GCPs).
 *
 * <p>This implementation uses separate TPS interpolators for the X and Y dimensions.
 */
public class ThinPlateSplineTransform implements MathTransform {

    private final List<Coordinate> sourcePoints;
    private final List<Coordinate> targetPoints;
    private final ThinPlateSpline2D tpsX;
    private final ThinPlateSpline2D tpsY;

    /**
     * Constructs a Thin Plate Spline transform from lists of source and target coordinates.
     *
     * @param sourcePoints list of source coordinates (e.g., image coordinates)
     * @param targetPoints list of target coordinates (e.g., map coordinates)
     * @throws IllegalArgumentException if the two lists are not of equal length
     */
    public ThinPlateSplineTransform(List<Coordinate> sourcePoints, List<Coordinate> targetPoints) {
        if (sourcePoints.size() != targetPoints.size()) {
            throw new IllegalArgumentException("Source and target point lists must be the same size.");
        }
        this.sourcePoints = sourcePoints;
        this.targetPoints = targetPoints;

        double[] xSource = new double[sourcePoints.size()];
        double[] ySource = new double[sourcePoints.size()];
        double[] xTarget = new double[targetPoints.size()];
        double[] yTarget = new double[targetPoints.size()];

        for (int i = 0; i < sourcePoints.size(); i++) {
            xSource[i] = sourcePoints.get(i).x;
            ySource[i] = sourcePoints.get(i).y;
            xTarget[i] = targetPoints.get(i).x;
            yTarget[i] = targetPoints.get(i).y;
        }

        this.tpsX = new ThinPlateSpline2D(xSource, ySource, xTarget);
        this.tpsY = new ThinPlateSpline2D(xSource, ySource, yTarget);
    }

    /**
     * Constructs a Thin Plate Spline transform from a list of {@link MappedPosition} instances.
     *
     * @param positions list of mappings from source to target positions
     * @throws IllegalArgumentException if the list is null, empty, or mismatched
     */
    public ThinPlateSplineTransform(List<MappedPosition> positions) {
        if (positions == null || positions.isEmpty()) {
            throw new IllegalArgumentException("Positions list must not be null or empty.");
        }
        List<Coordinate> src = new java.util.ArrayList<>(positions.size());
        List<Coordinate> dst = new java.util.ArrayList<>(positions.size());
        for (MappedPosition mp : positions) {
            src.add(new Coordinate(mp.getSource().getOrdinate(0), mp.getSource().getOrdinate(1)));
            dst.add(new Coordinate(mp.getTarget().getOrdinate(0), mp.getTarget().getOrdinate(1)));
        }

        this.sourcePoints = src;
        this.targetPoints = dst;

        double[] xSource = new double[src.size()];
        double[] ySource = new double[src.size()];
        double[] xTarget = new double[dst.size()];
        double[] yTarget = new double[dst.size()];

        for (int i = 0; i < src.size(); i++) {
            xSource[i] = src.get(i).x;
            ySource[i] = src.get(i).y;
            xTarget[i] = dst.get(i).x;
            yTarget[i] = dst.get(i).y;
        }

        this.tpsX = new ThinPlateSpline2D(xSource, ySource, xTarget);
        this.tpsY = new ThinPlateSpline2D(xSource, ySource, yTarget);
    }

    /** {@inheritDoc} */
    @Override
    public int getSourceDimensions() {
        return 2;
    }

    /** {@inheritDoc} */
    @Override
    public int getTargetDimensions() {
        return 2;
    }

    /**
     * Transforms a 2D position using the thin plate spline transformation.
     *
     * @param ptSrc the source position
     * @param ptDst the destination position to store the result (or null to create a new one)
     * @return the transformed position
     * @throws MismatchedDimensionException if dimensions are invalid
     * @throws TransformException if the transformation fails
     */
    @Override
    public Position transform(Position ptSrc, Position ptDst) throws MismatchedDimensionException, TransformException {
        if (ptDst == null) {
            ptDst = new Position2D();
        }
        double x = ptSrc.getOrdinate(0);
        double y = ptSrc.getOrdinate(1);
        ptDst.setOrdinate(0, tpsX.interpolate(x, y));
        ptDst.setOrdinate(1, tpsY.interpolate(x, y));
        return ptDst;
    }

    /** {@inheritDoc} */
    @Override
    public void transform(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts)
            throws TransformException {
        for (int i = 0; i < numPts; i++) {
            double x = srcPts[srcOff + 2 * i];
            double y = srcPts[srcOff + 2 * i + 1];
            dstPts[dstOff + 2 * i] = tpsX.interpolate(x, y);
            dstPts[dstOff + 2 * i + 1] = tpsY.interpolate(x, y);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void transform(float[] srcPts, int srcOff, float[] dstPts, int dstOff, int numPts)
            throws TransformException {
        for (int i = 0; i < numPts; i++) {
            double x = srcPts[srcOff + 2 * i];
            double y = srcPts[srcOff + 2 * i + 1];
            dstPts[dstOff + 2 * i] = (float) tpsX.interpolate(x, y);
            dstPts[dstOff + 2 * i + 1] = (float) tpsY.interpolate(x, y);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void transform(float[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts)
            throws TransformException {
        for (int i = 0; i < numPts; i++) {
            double x = srcPts[srcOff + 2 * i];
            double y = srcPts[srcOff + 2 * i + 1];
            dstPts[dstOff + 2 * i] = tpsX.interpolate(x, y);
            dstPts[dstOff + 2 * i + 1] = tpsY.interpolate(x, y);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void transform(double[] srcPts, int srcOff, float[] dstPts, int dstOff, int numPts)
            throws TransformException {
        for (int i = 0; i < numPts; i++) {
            double x = srcPts[srcOff + 2 * i];
            double y = srcPts[srcOff + 2 * i + 1];
            dstPts[dstOff + 2 * i] = (float) tpsX.interpolate(x, y);
            dstPts[dstOff + 2 * i + 1] = (float) tpsY.interpolate(x, y);
        }
    }

    /**
     * Estimates the Jacobian matrix (first-order partial derivatives) of the transformation at a given point using
     * central differences.
     *
     * @param point the position to evaluate the derivative at
     * @return a 2×2 matrix representing the derivative
     * @throws MismatchedDimensionException if dimensions are invalid
     * @throws TransformException if the transformation fails
     */
    @Override
    public Matrix derivative(Position point) throws MismatchedDimensionException, TransformException {
        double x = point.getOrdinate(0);
        double y = point.getOrdinate(1);
        double eps = 1e-6;

        double dx1 = (tpsX.interpolate(x + eps, y) - tpsX.interpolate(x - eps, y)) / (2 * eps);
        double dy1 = (tpsX.interpolate(x, y + eps) - tpsX.interpolate(x, y - eps)) / (2 * eps);
        double dx2 = (tpsY.interpolate(x + eps, y) - tpsY.interpolate(x - eps, y)) / (2 * eps);
        double dy2 = (tpsY.interpolate(x, y + eps) - tpsY.interpolate(x, y - eps)) / (2 * eps);

        return new GeneralMatrix(new double[][] {
            {dx1, dy1},
            {dx2, dy2}
        });
    }

    /**
     * Returns the inverse of this transformation by swapping the source and target points.
     *
     * @return the inverse transform
     * @throws UnsupportedOperationException if inversion is not supported
     */
    @Override
    public MathTransform inverse() throws UnsupportedOperationException {
        return new ThinPlateSplineTransform(targetPoints, sourcePoints);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isIdentity() {
        return false;
    }

    /**
     * Returns the Well-Known Text (WKT) representation of the transform.
     *
     * @return the WKT string
     */
    @Override
    public String toWKT() throws UnsupportedOperationException {
        StringBuilder wkt = new StringBuilder();
        wkt.append("PARAM_MT[\"ThinPlateSpline\",\n");

        wkt.append("  PARAMETER[\"num_points\", ").append(sourcePoints.size()).append("],\n");

        for (int i = 0; i < sourcePoints.size(); i++) {
            Coordinate src = sourcePoints.get(i);
            Coordinate dst = targetPoints.get(i);
            wkt.append("  PARAMETER[\"source_")
                    .append(i)
                    .append("\", [")
                    .append(src.x)
                    .append(", ")
                    .append(src.y)
                    .append("]],\n");
            wkt.append("  PARAMETER[\"target_")
                    .append(i)
                    .append("\", [")
                    .append(dst.x)
                    .append(", ")
                    .append(dst.y)
                    .append("]]");
            if (i < sourcePoints.size() - 1) {
                wkt.append(",");
            }
            wkt.append("\n");
        }

        wkt.append("]");
        return wkt.toString();
    }

    /**
     * Returns a human-readable description of this transform.
     *
     * @return string description
     */
    @Override
    public String toString() {
        return "ThinPlateSplineTransform";
    }
}
