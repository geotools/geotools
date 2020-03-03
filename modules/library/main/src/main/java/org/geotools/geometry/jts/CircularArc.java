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

import static java.lang.Math.*;

import java.util.Arrays;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;

/**
 * Represents an arc by three points, and provides methods to linearize it to a given max distance
 * from the actual circle
 *
 * @author Andrea Aime - GeoSolutions
 */
public class CircularArc {

    static final double EPS = 1.0e-12;

    public static final double COLLINEARS = Double.POSITIVE_INFINITY;

    private static final String BASE_SEGMENTS_QUADRANT_KEY =
            "org.getools.geometry.arc.baseSegmentsQuadrant";

    /** Minimum number of segments per quadrant */
    static int BASE_SEGMENTS_QUADRANT =
            Integer.valueOf(System.getProperty(BASE_SEGMENTS_QUADRANT_KEY, "12"));

    private static final String MAX_SEGMENTS_QUADRANT_KEY =
            "org.getools.geometry.arc.maxSegmentsQuadrant";

    /** Max number of segments per quadrant the system will use to satisfy the given tolerance */
    static int MAX_SEGMENTS_QUADRANT =
            Integer.valueOf(System.getProperty(MAX_SEGMENTS_QUADRANT_KEY, "10000"));

    /** Allows to programmatically set the number of segments per quadrant (default to 8) */
    public static void setBaseSegmentsQuadrant(int baseSegmentsQuadrant) {
        if (baseSegmentsQuadrant < 0) {
            throw new IllegalArgumentException("The base segments per quadrant must be at least 1");
        }
        BASE_SEGMENTS_QUADRANT = baseSegmentsQuadrant;
    }

    /**
     * Allows to programmatically set the maximum number of segments per quadrant (default to 10000)
     */
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
    };

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
                while (currentTolerance > tolerance
                        && segmentsPerQuadrant < MAX_SEGMENTS_QUADRANT) {
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

        boolean clockwise = (sa > ma && ma > ea) || (sa > ma && sa < ea) || (ma > ea && sa < ea);
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
            double temp, bc, cd, determinate;

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

                radius = sqrt((centerX - sx) * (centerX - sx) + (centerY - sy) * (centerY - sy));
            } else {
                temp = mx * mx + my * my;
                bc = (sx * sx + sy * sy - temp) / 2.0;
                cd = (temp - ex * ex - ey * ey) / 2.0;
                determinate = (sx - mx) * (my - ey) - (mx - ex) * (sy - my);

                /* Check collinearity */
                if (abs(determinate) < EPS) {
                    radius = COLLINEARS;
                    return;
                }
                determinate = 1.0 / determinate;
                centerX = (bc * (my - ey) - cd * (sy - my)) * determinate;
                centerY = ((sx - mx) * cd - (mx - ex) * bc) * determinate;

                radius = sqrt((centerX - sx) * (centerX - sx) + (centerY - sy) * (centerY - sy));
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

        boolean clockwise = (sa > ma && ma > ea) || (sa < ma && sa > ea) || (ma < ea && sa > ea);
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
