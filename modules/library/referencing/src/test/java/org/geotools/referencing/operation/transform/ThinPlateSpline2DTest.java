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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ThinPlateSpline2DTest {

    @Test
    public void testExactInterpolation3Points() {
        double[] x = {0, 1, 0};
        double[] y = {0, 0, 1};
        double[] v = {10, 20, 30};

        ThinPlateSpline2D tps = new ThinPlateSpline2D(x, y, v);

        assertEquals(10.0, tps.interpolate(0, 0), 1e-6);
        assertEquals(20.0, tps.interpolate(1, 0), 1e-6);
        assertEquals(30.0, tps.interpolate(0, 1), 1e-6);
    }

    @Test
    public void testInterpolatedMiddleValue() {
        double[] x = {0, 1, 0};
        double[] y = {0, 0, 1};
        double[] v = {10, 20, 30};

        ThinPlateSpline2D tps = new ThinPlateSpline2D(x, y, v);
        double interp = tps.interpolate(0.5, 0.25);

        assertTrue("Value should be between control point values", interp > 10 && interp < 30);
    }

    @Test
    public void testAffineOnlyTransform() {
        double[] x = {0, 1, 0, 1};
        double[] y = {0, 0, 1, 1};
        double[] v = new double[4];

        for (int i = 0; i < 4; i++) {
            v[i] = 2 + 3 * x[i] + 4 * y[i]; // f(x, y) = 2 + 3x + 4y
        }

        ThinPlateSpline2D tps = new ThinPlateSpline2D(x, y, v);

        assertEquals(2.0, tps.interpolate(0, 0), 1e-6);
        assertEquals(5.0, tps.interpolate(1, 0), 1e-6);
        assertEquals(6.0, tps.interpolate(0, 1), 1e-6);
        assertEquals(9.0, tps.interpolate(1, 1), 1e-6);
    }

    @Test
    public void testExtrapolation() {
        double[] x = {0, 1, 1, 0};
        double[] y = {0, 0, 1, 1};
        double[] v = {10, 20, 30, 40};

        ThinPlateSpline2D tps = new ThinPlateSpline2D(x, y, v);

        double outside = tps.interpolate(2, 2);
        assertTrue("Extrapolated value should be outside original range", outside > 40 || outside < 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRejectsDuplicatePoints() {
        double[] x = {1, 1, 2};
        double[] y = {1, 1, 2};
        double[] v = {10, 10, 30};

        new ThinPlateSpline2D(x, y, v);
    }
}
