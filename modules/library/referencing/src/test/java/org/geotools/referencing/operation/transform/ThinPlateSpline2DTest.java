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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;

public class ThinPlateSpline2DTest {

    @Test
    public void testExactInterpolation3Points() {

        CoordinateSequence seq = new CoordinateArraySequence(
                new Coordinate[] {new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(0, 1)});

        double[] v = {10, 20, 30};

        ThinPlateSpline2D tps = new ThinPlateSpline2D(seq, v);
        assertEquals(10.0, tps.interpolate(0, 0), 1e-6);
        assertEquals(20.0, tps.interpolate(1, 0), 1e-6);
        assertEquals(30.0, tps.interpolate(0, 1), 1e-6);
    }

    @Test
    public void testInterpolatedMiddleValue() {

        CoordinateSequence seq = new CoordinateArraySequence(
                new Coordinate[] {new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(0, 1)});
        double[] v = {10, 20, 30};
        ThinPlateSpline2D tps = new ThinPlateSpline2D(seq, v);
        double interp = tps.interpolate(0.5, 0.25);

        assertTrue("Value should be between control point values", interp > 10 && interp < 30);
    }

    @Test
    public void testAffineOnlyTransform() {

        double[] v = new double[4];

        CoordinateSequence seq = new CoordinateArraySequence(
                new Coordinate[] {new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(0, 1), new Coordinate(1, 1)
                });

        for (int i = 0; i < 4; i++) {
            v[i] = 2 + 3 * seq.getX(i) + 4 * seq.getY(i); // f(x, y) = 2 + 3x + 4y
        }

        ThinPlateSpline2D tps = new ThinPlateSpline2D(seq, v);

        assertEquals(2.0, tps.interpolate(0, 0), 1e-6);
        assertEquals(5.0, tps.interpolate(1, 0), 1e-6);
        assertEquals(6.0, tps.interpolate(0, 1), 1e-6);
        assertEquals(9.0, tps.interpolate(1, 1), 1e-6);
    }

    @Test
    public void testExtrapolation() {

        CoordinateSequence seq = new CoordinateArraySequence(
                new Coordinate[] {new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(1, 1), new Coordinate(0, 1)
                });
        double[] v = {10, 20, 30, 40};

        ThinPlateSpline2D tps = new ThinPlateSpline2D(seq, v);

        double outside = tps.interpolate(2, 2);
        assertTrue("Extrapolated value should be outside original range", outside > 40 || outside < 10);
    }

    @Test
    public void testRejectsDuplicatePoints() {

        double[] v = {10, 10};
        CoordinateSequence duplicateSeq =
                new CoordinateArraySequence(new Coordinate[] {new Coordinate(1, 1), new Coordinate(1, 1)});

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> new ThinPlateSpline2D(duplicateSeq, v));
        assertEquals("Duplicate control points are not allowed.", exception.getMessage());
    }

    @Test
    public void testAtLeastThreePointsRequired() {

        double[] v = {10, 10};
        CoordinateSequence onlyTwoPoints =
                new CoordinateArraySequence(new Coordinate[] {new Coordinate(0, 0), new Coordinate(1, 1)});

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> new ThinPlateSpline2D(onlyTwoPoints, v));
        assertEquals("At least 3 unique control points are required for TPS.", exception.getMessage());
    }
}
