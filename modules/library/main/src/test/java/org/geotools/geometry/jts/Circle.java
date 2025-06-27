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

import static java.lang.Math.atan2;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.locationtech.jts.geom.CoordinateSequence;

/**
 * Utility class for the test, represents a circle in the origin with a given radius
 *
 * @author Andrea Aime - GeoSolutions
 */
class Circle {
    double radius;

    double cx, cy;

    static final double EPS = 1e-6;

    public Circle(double radius, double cx, double cy) {
        this.radius = radius;
        this.cx = cx;
        this.cy = cy;
    }

    public Circle(double radius) {
        this(radius, 0, 0);
    }

    public double[] samplePoints(double... angles) {
        double[] result = new double[angles.length * 2];
        int i = 0;
        for (double angle : angles) {
            result[i++] = cx + radius * Math.cos(angle);
            result[i++] = cy + radius * Math.sin(angle);
        }

        return result;
    }

    public CircularArc getCircularArc(double startAngle, double midAngle, double endAngle) {
        double[] controlPoints = samplePoints(startAngle, midAngle, endAngle);
        return new CircularArc(controlPoints);
    }

    public void assertTolerance(CoordinateSequence cs, double tolerance) {
        double[] ordinates = new double[cs.size() * 2];
        for (int i = 0; i < cs.size(); i++) {
            ordinates[i * 2] = cs.getOrdinate(i, 0);
            ordinates[i * 2 + 1] = cs.getOrdinate(i, 1);
        }
        assertTolerance(ordinates, tolerance);
    }

    public void assertTolerance(double[] ordinates, double tolerance) {
        // force counter-clockwise order to simplify the testing logic
        if (clockwise(ordinates)) {
            for (int i = 0; i < ordinates.length / 2; i++) {
                double temp = ordinates[i];
                ordinates[i] = ordinates[ordinates.length - i - 1];
                ordinates[ordinates.length - i - 1] = temp;
            }
        }

        double prevAngle = 0;
        for (int i = 0; i < ordinates.length; i += 2) {
            // check the point is on the circle
            double x = ordinates[i];
            double y = ordinates[i + 1];
            double dx = x - cx;
            double dy = y - cy;
            double d = Math.sqrt(dx * dx + dy * dy);
            double distanceFromCircle = Math.abs(radius - d);
            if (distanceFromCircle > tolerance) {
                fail("Found a point "
                        + x
                        + ","
                        + y
                        + " that's not on the circle with distance "
                        + distanceFromCircle
                        + " from it");
            }
            assertEquals(radius, d, tolerance);
            double angle = atan2(dy, dx);

            if (i > 1) {
                double chordAngle = angle - prevAngle;
                if (chordAngle < 0) {
                    chordAngle += Math.PI * 2;
                } else if (chordAngle > Math.PI) {
                    chordAngle = Math.PI * 2 - chordAngle;
                }
                double halfChordLength = radius * Math.sin(chordAngle / 2);
                double apothem = Math.sqrt(radius * radius - halfChordLength * halfChordLength);
                double distance = radius - apothem;
                if (distance > tolerance) {
                    fail("Max tolerance is "
                            + tolerance
                            + " but found a chord that is at "
                            + distance
                            + " from the circle");
                }
            }
            prevAngle = angle;
        }
    }

    boolean clockwise(double[] ordinates) {
        double sa = atan2(ordinates[1] - cx, ordinates[0] - cy);
        double ma = atan2(ordinates[3] - cx, ordinates[2] - cy);
        double ea = atan2(ordinates[5] - cx, ordinates[3] - cy);
        return sa > ma && ma > ea || sa < ma && sa > ea || ma < ea && sa > ea;
    }
}
