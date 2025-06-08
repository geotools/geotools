/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.jts;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import java.util.Arrays;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.decomposition.lu.LUDecompositionAlt_DDRM;
import org.ejml.dense.row.linsol.lu.LinearSolverLu_DDRM;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;

/**
 * Represents an arc by three points, and provides methods to linearize it to a given max distance from the actual
 * circle
 *
 * @author Andrea Aime - GeoSolutions
 */
public class CircularArc {

    static final double EPS = 1.0e-12;

    static final double MAXCOND = 2.0e4;

    public static final double COLLINEARS = Double.POSITIVE_INFINITY;

    private static final String BASE_SEGMENTS_QUADRANT_KEY = "org.getools.geometry.arc.baseSegmentsQuadrant";

    /** Minimum number of segments per quadrant */
    static int BASE_SEGMENTS_QUADRANT = Integer.valueOf(System.getProperty(BASE_SEGMENTS_QUADRANT_KEY, "12"));

    private static final String MAX_SEGMENTS_QUADRANT_KEY = "org.getools.geometry.arc.maxSegmentsQuadrant";

    /** Max number of segments per quadrant the system will use to satisfy the given tolerance */
    static int MAX_SEGMENTS_QUADRANT = Integer.valueOf(System.getProperty(MAX_SEGMENTS_QUADRANT_KEY, "10000"));

    /** Allows to programmatically set the number of segments per quadrant (default to 8) */
    public static void setBaseSegmentsQuadrant(int baseSegmentsQuadrant) {
        if (baseSegmentsQuadrant < 0) {
            throw new IllegalArgumentException("The base segments per quadrant must be at least 1");
        }
        BASE_SEGMENTS_QUADRANT = baseSegmentsQuadrant;
    }

    /** Allows to programmatically set the maximum number of segments per quadrant (default to 10000) */
    public static void setMaxSegmentsQuadrant(int baseSegmentsQuadrant) {
        if (baseSegmentsQuadrant < 0) {
            throw new IllegalArgumentException("The max segments per quadrant must be at least 1");
        }
        MAX_SEGMENTS_QUADRANT = baseSegmentsQuadrant;
    }

    static final double HALF_PI = PI / 2.0;

    static final double TAU = PI * 2.0;

    double[] controlPoints;

    double radius = Double.NaN;

    double centerX;

    double centerY;

    public CircularArc(double[] controlPoints) {
        if (controlPoints == null || controlPoints.length != 6) {
            throw new IllegalArgumentException(
                    "Invalid control point array, it must be made of 6 ordinates for a total of 3 control points, start, mid and end");
        }
        this.controlPoints = controlPoints;
    }

    public CircularArc(double sx, double sy, double mx, double my, double ex, double ey) {
        this(new double[] {sx, sy, mx, my, ex, ey});
    }

    public int getDimension() {
        return 2;
    }

    public double[] getControlPoints() {
        return controlPoints;
    }

    public double getRadius() {
        initializeCenterRadius();
        return radius;
    }

    public Coordinate getCenter() {
        initializeCenterRadius();
        if (radius == COLLINEARS) {
            return null;
        } else {
            return new Coordinate(centerX, centerY);
        }
    }

    public double[] linearize(double tolerance) {
        initializeCenterRadius();
        // the collinear case is simple, we just return the control points (and do the same for same
        // points case)
        if (radius == COLLINEARS || radius == 0) {
            return controlPoints;
        }

        return linearize(tolerance, new GrowableOrdinateArray()).getData();
    }

    GrowableOrdinateArray linearize(double tolerance, GrowableOrdinateArray array) {
        initializeCenterRadius();
        if (tolerance < 0) {
            throw new IllegalArgumentException(
                    "The tolerance must be a positive number (or zero, to make the system use the "
                            + "max number of segments per quadrant configured in "
                            + "org.getools.geometry.arc.maxSegmentsQuadrant, default is 10000)");
        }

        // ok, we need to find out which number of segments per quadrant
        // will get us below the threshold
        int segmentsPerQuadrant;
        if (tolerance == 0) {
            segmentsPerQuadrant = MAX_SEGMENTS_QUADRANT;
        } else if (tolerance == Double.MAX_VALUE) {
            segmentsPerQuadrant = BASE_SEGMENTS_QUADRANT;
        } else {
            segmentsPerQuadrant = BASE_SEGMENTS_QUADRANT;
            double currentTolerance = computeChordCircleDistance(segmentsPerQuadrant);
            if (currentTolerance < tolerance) {
                while (currentTolerance < tolerance && segmentsPerQuadrant > 1) {
                    // going down
                    segmentsPerQuadrant /= 2;
                    currentTolerance = computeChordCircleDistance(segmentsPerQuadrant);
                }
                if (currentTolerance > tolerance) {
                    segmentsPerQuadrant *= 2;
                }
            } else {
                while (currentTolerance > tolerance && segmentsPerQuadrant < MAX_SEGMENTS_QUADRANT) {
                    // going up
                    segmentsPerQuadrant *= 2;
                    currentTolerance = computeChordCircleDistance(segmentsPerQuadrant);
                }
            }
        }

        // now we linearize, using the following approach
        // - create regular segments, our base angle is always 0, to make sure concentric
        // arcs won't touch when linearized
        // - make sure the control points are included in the result
        double sx = controlPoints[0];
        double sy = controlPoints[1];
        double mx = controlPoints[2];
        double my = controlPoints[3];
        double ex = controlPoints[4];
        double ey = controlPoints[5];

        // our reference angles
        double sa = atan2(sy - centerY, sx - centerX);
        double ma = atan2(my - centerY, mx - centerX);
        double ea = atan2(ey - centerY, ex - centerX);
        double step = HALF_PI / segmentsPerQuadrant;

        // check if clockwise

        boolean clockwise = sa > ma && ma > ea || sa > ma && sa < ea || ma > ea && sa < ea;
        if (clockwise) {
            // we need to walk all arcs the same way, or we incur in the risk of having
            // two close but concentric arcs to touch each other
            double tx = sx;
            sx = ex;
            ex = tx;
            double ty = sy;
            sy = ey;
            ey = ty;
            double ta = sa;
            sa = ea;
            ea = ta;
        }

        // normalize angle so that we can treat steps like a linear progression
        if (ma < sa) {
            ma += TAU;
            ea += TAU;
        } else if (ea < sa) {
            ea += TAU;
        }

        // the starting point
        double angle = (Math.floor(sa / step) + 1) * step;
        // very short arc case, or high tolerance, we only use the control points
        if (angle > ea) {
            array.addAll(controlPoints);
            return array;
        }

        // guessing number of points
        int points = 2 + (int) Math.ceil((ea - angle) / step);
        // this test might fail due to numeric reasons, in that case we might be
        // a couple of indexes short or long (depending on the way it fails)
        if (!isWhole((ma - angle) / step)) {
            points++;
        }

        int start = array.size();
        array.ensureLength(start + points * 2);
        // add the start point
        array.add(sx, sy);

        // case where the "mid" point is actually very close to the start point
        if (angle > ma) {
            array.add(mx, my);
            if (equals(angle, ma)) {
                angle += step;
            }
        }
        // move on and add the other points
        final double end = ea - EPS;
        while (angle < end) {
            double x = centerX + radius * cos(angle);
            double y = centerY + radius * sin(angle);
            array.add(x, y);
            double next = angle + step;
            if (angle < ma && next > ma && !equals(angle, ma) && !equals(next, ma)) {
                array.add(mx, my);
            }
            angle = next;
        }
        array.add(ex, ey);
        if (clockwise) {
            array.reverseOrdinates(start, array.size() - 1);
        }
        return array;
    }

    private boolean isWhole(double d) {
        long num = (long) d;
        double fractional = d - num;
        return fractional < EPS;
    }

    private double computeChordCircleDistance(int segmentsPerQuadrant) {
        double halfChordLength = radius * Math.sin(HALF_PI / segmentsPerQuadrant);
        double apothem = Math.sqrt(radius * radius - halfChordLength * halfChordLength);
        return radius - apothem;
    }

    private void initializeCenterRadius() {
        if (Double.isNaN(radius)) {
            final double sx = controlPoints[0];
            final double sy = controlPoints[1];
            final double mx = controlPoints[2];
            final double my = controlPoints[3];
            final double ex = controlPoints[4];
            final double ey = controlPoints[5];

            /* Closed circle */
            if (equals(sx, ex) && equals(sy, ey)) {
                centerX = sx + (mx - sx) / 2.0;
                centerY = sy + (my - sy) / 2.0;
            } else {
                // Let <a, b> be the dot product of the two-dimensional vectors a and b. Given three
                // distinct points p1, p2, p3 located on a circle, we can find the center c of the
                // circle by solving the following over-determined system of equations:
                //   <p1-p2, c> = 0.5*<p1-p2, p1+p2>    (1)
                //   <p1-p3, c> = 0.5*<p1-p3, p1+p3>    (2)
                //   <p2-p3, c> = 0.5*<p2-p3, p2+p3>    (3)
                // The equations express that the vector from the center to the midpoint of a
                // triangle side must be perpendicular to the triangle side.
                //
                // We select the two equations that result in the 2x2 matrix with the best
                // condition. The matrix condition number k of a 2x2 matrix
                //   | a b |
                //   | c d |
                // is
                //   k = (1 + sqrt(1 - R^2))/R
                // with
                //   R = 2|det(A)|/tr(A^T*A) = 2|ad - bc|/(a^2 + b^2 + c^2 + d^2)
                // where
                //   0 <= R <= 1
                // Hence a larger R results in a better (smaller) condition number.
                //
                // Independent of the two equations selected, 2|det(A)| is always
                //   2|det(A)| = 2|(x1(y2-y3) - x2(y1-y3) + x3(y1-y2))|
                //
                // Hence we have the best-conditioned system if we select the two equations so that
                // a^2 + b^2 + c^2 + d^2 is as small as possible.

                // Compute the matrix row candidates
                final double dx12 = sx - mx;
                final double dy12 = sy - my;
                final double dx13 = sx - ex;
                final double dy13 = sy - ey;
                final double dx23 = mx - ex;
                final double dy23 = my - ey;

                // Compute the square sums of the row candidates
                final double sqs1 = dx12 * dx12 + dy12 * dy12;
                final double sqs2 = dx13 * dx13 + dy13 * dy13;
                final double sqs3 = dx23 * dx23 + dy23 * dy23;

                DMatrixRMaj A;
                DMatrixRMaj b;
                double sqs; // a^2 + b^2 + c^2 + d^2
                if (sqs1 <= sqs3 && sqs2 <= sqs3) {
                    // Take equations (1) and (2)
                    A = new DMatrixRMaj(2, 2, true, dx12, dy12, dx13, dy13);
                    b = new DMatrixRMaj(
                            2,
                            1,
                            true,
                            0.5 * (dx12 * (sx + mx) + dy12 * (sy + my)),
                            0.5 * (dx13 * (sx + ex) + dy13 * (sy + ey)));
                    sqs = sqs1 + sqs2;
                } else if (sqs1 <= sqs2 && sqs3 <= sqs2) {
                    // Take equations (1) and (3)
                    A = new DMatrixRMaj(2, 2, true, dx12, dy12, dx23, dy23);
                    b = new DMatrixRMaj(
                            2,
                            1,
                            true,
                            0.5 * (dx12 * (sx + mx) + dy12 * (sy + my)),
                            0.5 * (dx23 * (mx + ex) + dy23 * (my + ey)));
                    sqs = sqs1 + sqs3;
                } else {
                    // Take equations (2) and (3)
                    A = new DMatrixRMaj(2, 2, true, dx13, dy13, dx23, dy23);
                    b = new DMatrixRMaj(
                            2,
                            1,
                            true,
                            0.5 * (dx13 * (sx + ex) + dy13 * (sy + ey)),
                            0.5 * (dx23 * (mx + ex) + dy23 * (my + ey)));
                    sqs = sqs2 + sqs3;
                }

                LUDecompositionAlt_DDRM lu = new LUDecompositionAlt_DDRM();
                LinearSolverLu_DDRM solver = new LinearSolverLu_DDRM(lu);
                if (!solver.setA(A)) {
                    radius = COLLINEARS;
                    return;
                }

                // If the control points are nearly collinear, a slight move of a control point will
                // cause a large move of the center. Looking at the equation system we are about to
                // solve
                //   Ax = b
                // we can observe this behavior if the condition number is high. A high condition
                // number means that small changes in b lead to large changes in x. We know that
                //   R = 2|det(A)|/tr(A^T*A) = 2|ad - bc|/(a^2 + b^2 + c^2 + d^2)
                // and we already have a^2 + b^2 + c^2 + d^2 in sqs, we just need the determinant to
                // compute R.
                final double R = 2.0 * abs(lu.computeDeterminant().getReal()) / sqs;

                // From R we can now compute the condition number k. When generating random
                // collinear points with double precision, the points are basically never perfectly
                // collinear due to the limited precision of double precision numbers. In such
                // cases, the matrix condition number usually is > 2*10^4.
                final double k = (1.0 + sqrt(1.0 - R * R)) / R;
                if (k > MAXCOND) {
                    radius = COLLINEARS;
                    return;
                }

                DMatrixRMaj x = new DMatrixRMaj(2, 1);
                solver.solve(b, x);

                centerX = x.get(0);
                centerY = x.get(1);
            }

            // As the center has to be snapped on the floating point grid, the distance of the
            // center to the control points can be different. We use the median of the three
            // distances as radius.
            final double rsx = centerX - sx;
            final double rsy = centerY - sy;
            final double rs = sqrt(rsx * rsx + rsy * rsy);

            final double rmx = centerX - mx;
            final double rmy = centerY - my;
            final double rm = sqrt(rmx * rmx + rmy * rmy);

            final double rex = centerX - ex;
            final double rey = centerY - ey;
            final double re = sqrt(rex * rex + rey * rey);

            if (rs <= rm) {
                if (rm <= re) {
                    radius = rm; // rs <= rm <= re
                } else if (rs <= re) {
                    radius = re; // rs <= re <  rm
                } else {
                    radius = rs; // re <  rs <= rm
                }
            } else {
                if (rs <= re) {
                    radius = rs; // rm <  rs <= re
                } else if (rm <= re) {
                    radius = re; // rm <= re <  rs
                } else {
                    radius = rm; // re <  rm <  rs
                }
            }
        }
    }

    @Override
    public String toString() {
        return "CircularArc[" + Arrays.toString(controlPoints) + "]";
    }

    /** Clears the caches (useful if the control points have been changed) */
    void reset() {
        radius = Double.NaN;
    }

    /** Checks if the two doubles provided are at a distance less than EPS */
    static boolean equals(double a, double b) {
        return Math.abs(a - b) < EPS;
    }

    public Envelope getEnvelope() {
        Envelope result = new Envelope();
        expandEnvelope(result);
        return result;
    }

    /** Expands the given envelope */
    void expandEnvelope(Envelope envelope) {
        initializeCenterRadius();

        // get the points
        double sx = controlPoints[0];
        double sy = controlPoints[1];
        double mx = controlPoints[2];
        double my = controlPoints[3];
        double ex = controlPoints[4];
        double ey = controlPoints[5];

        // add start and end
        envelope.expandToInclude(sx, sy);
        envelope.expandToInclude(ex, ey);

        // if it's not curved, we can exit now
        if (radius == COLLINEARS) {
            return;
        }

        // compute the reference angles
        double sa = atan2(sy - centerY, sx - centerX);
        double ma = atan2(my - centerY, mx - centerX);
        double ea = atan2(ey - centerY, ex - centerX);

        boolean clockwise = sa > ma && ma > ea || sa < ma && sa > ea || ma < ea && sa > ea;
        if (clockwise) {
            // we want to go counter-clock wise to simplify the rest of the algorithm
            double tx = sx;
            sx = ex;
            ex = tx;
            double ty = sy;
            sy = ey;
            ey = ty;
            double ta = sa;
            sa = ea;
            ea = ta;
        }

        // normalize angle so that we can treat steps like a linear progression
        if (ma <= sa) {
            ma += TAU;
            ea += TAU;
        } else if (ea <= sa) {
            ea += TAU;
        }

        // scan the circle and add the points at 90° corners
        double angle = (Math.floor(sa / HALF_PI) + 1) * HALF_PI;
        while (angle < ea) {
            double x = centerX + radius * cos(angle);
            double y = centerY + radius * sin(angle);
            envelope.expandToInclude(x, y);
            angle += HALF_PI;
        }
    }
}
