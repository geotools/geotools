/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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

import java.util.HashSet;
import java.util.Set;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;

/**
 * A 2D Thin Plate Spline (TPS) interpolator.
 *
 * <p>TPS is a smooth spline-based interpolation for warping, image registration, and surface fitting. This
 * implementation solves for the spline weights using radial basis functions and an affine component to interpolate
 * scalar values at arbitrary 2D locations.
 *
 * <p>Each instance fits a surface from control points (x, y) to scalar values {@code v}. To model 2D transformations
 * (e.g., (x, y) → (x', y')), construct two instances: one for x' and one for y'.
 *
 * <p>Reference:
 *
 * <ul>
 *   <li><a href="https://en.wikipedia.org/wiki/Thin_plate_spline">Thin plate spline (Wikipedia)</a>
 * </ul>
 */
public class ThinPlateSpline2D {

    private final CoordinateSequence sourcePoints;
    private final double[] weights;
    private final int n;

    /**
     * Constructs a Thin Plate Spline (TPS) interpolator using the given control points and target values.
     *
     * <p>This class fits a smooth interpolating surface through the control points, minimizing the bending energy of
     * the surface. The resulting function can be evaluated at arbitrary (x, y) locations to obtain interpolated values.
     *
     * @param sourcePoints The source control points in 2D space. Must contain at least 3 unique points.
     * @param values The target values at each control point, of the same length as {@code sourcePoints}.
     * @throws IllegalArgumentException if the number of source points does not match the number of values, or if there
     *     are duplicate source coordinates.
     */
    public ThinPlateSpline2D(CoordinateSequence sourcePoints, double[] values) {
        if (sourcePoints.size() != values.length) {
            throw new IllegalArgumentException("Source points and target values must have the same length.");
        }
        // duplicates can calculate very inaccurate weights
        Set<Coordinate> uniquePoints = new HashSet<>();
        for (int i = 0; i < sourcePoints.size(); i++) {
            Coordinate p = sourcePoints.getCoordinate(i);
            if (!uniquePoints.add(p)) {
                throw new IllegalArgumentException("Duplicate control points are not allowed.");
            }
        }

        if (uniquePoints.size() < 3) {
            throw new IllegalArgumentException("At least 3 unique control points are required for TPS.");
        }

        this.n = sourcePoints.size();
        this.sourcePoints = sourcePoints;
        this.weights = solveSystem(sourcePoints, values);
    }

    private double U(double r2) {
        if (r2 == 0.0) return 0.0;
        return r2 * Math.log(Math.sqrt(r2));
    }

    private double r2(double x1, double y1, double x2, double y2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return dx * dx + dy * dy;
    }

    private double[] solveSystem(CoordinateSequence sourcePoints, double[] v) {
        int N = sourcePoints.size();
        int M = N + 3;

        double[][] A = new double[M][M];
        double[] b = new double[M];

        // Fill K (radial basis kernel)
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                A[i][j] = U(r2(sourcePoints.getX(i), sourcePoints.getY(i), sourcePoints.getX(j), sourcePoints.getY(j)));
            }
        }

        // Fill P (affine part)
        for (int i = 0; i < N; i++) {
            A[i][N] = 1.0;
            A[i][N + 1] = sourcePoints.getX(i);
            A[i][N + 2] = sourcePoints.getY(i);

            A[N][i] = 1.0;
            A[N + 1][i] = sourcePoints.getX(i);
            A[N + 2][i] = sourcePoints.getY(i);
        }

        // Fill b (target values)
        System.arraycopy(v, 0, b, 0, N);

        // Solve A * w = b
        return gaussianElimination(A, b);
    }

    private double[] gaussianElimination(double[][] A, double[] b) {
        int N = A.length;
        double[][] aug = new double[N][N + 1];

        // Build augmented matrix
        for (int i = 0; i < N; i++) {
            System.arraycopy(A[i], 0, aug[i], 0, N);
            aug[i][N] = b[i];
        }

        // Forward elimination
        for (int i = 0; i < N; i++) {
            // Pivot
            int maxRow = i;
            for (int k = i + 1; k < N; k++) {
                if (Math.abs(aug[k][i]) > Math.abs(aug[maxRow][i])) {
                    maxRow = k;
                }
            }
            double[] tmp = aug[i];
            aug[i] = aug[maxRow];
            aug[maxRow] = tmp;

            // Eliminate
            for (int k = i + 1; k < N; k++) {
                double f = aug[k][i] / aug[i][i];
                for (int j = i; j <= N; j++) {
                    aug[k][j] -= f * aug[i][j];
                }
            }
        }

        // Backward substitution
        double[] x = new double[N];
        for (int i = N - 1; i >= 0; i--) {
            x[i] = aug[i][N] / aug[i][i];
            for (int k = i - 1; k >= 0; k--) {
                aug[k][N] -= aug[k][i] * x[i];
            }
        }

        return x;
    }

    /**
     * Interpolates a value at the given 2D location using the thin plate spline surface.
     *
     * <p>The returned value is computed based on the previously fitted surface that passes through all control points.
     * The function is smooth and differentiable, with minimum bending energy.
     *
     * @param xVal The x-coordinate of the input point.
     * @param yVal The y-coordinate of the input point.
     * @return The interpolated value at the given location.
     */
    public double interpolate(double xVal, double yVal) {
        double sum = 0.0;
        for (int i = 0; i < n; i++) {
            sum += weights[i] * U(r2(xVal, yVal, sourcePoints.getX(i), sourcePoints.getY(i)));
        }
        // Affine terms
        sum += weights[n]; // constant
        sum += weights[n + 1] * xVal;
        sum += weights[n + 2] * yVal;
        return sum;
    }
}
