/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.vector;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests the {@link BilinearInterpolator}
 *
 * @author Martin Davis, OpenGeo
 */
public class BilinearInterpolatorTest {

    @Test
    public void testSquareWithTriangleData() {
        float NO_DATA = -99.0f;

        float[][] input = new float[2][2];
        input[0][0] = 0;
        input[0][1] = NO_DATA;
        input[1][0] = 10;
        input[1][1] = 20;

        BilinearInterpolator interp = new BilinearInterpolator(input, NO_DATA);
        float[][] output = interp.interpolate(10, 10, true);
        printGrid(output);

        // assertTrue(isMonotonicTriangle(output, 0, 0, 10, 10, true, NO_DATA));
        assertTrue(isMonotonic(output, NO_DATA));
    }

    private boolean isMonotonic(float[][] grid, float noDataValue) {

        // check monotonicity in X direction
        for (int j = 0; j < grid[0].length; j++) {
            float slice[] = sliceX(grid, j);
            if (!isMonotonicSequence(slice, noDataValue)) return false;
        }
        // check monotonicity in Y direction
        for (int x = 0; x < grid[0].length; x++) {
            float slice[] = sliceY(grid, x);
            if (!isMonotonicSequence(slice, noDataValue)) return false;
        }
        return true;
    }

    /** Extracts a slice of a grid along the X dimension (a row) */
    private float[] sliceX(float[][] grid, int y) {
        float[] slice = new float[grid.length];
        for (int i = 0; i < slice.length; i++) {
            slice[i] = grid[i][y];
        }
        return slice;
    }

    /** Extracts a slice of a grid along the Y dimension (a column) */
    private float[] sliceY(float[][] grid, int x) {
        float[] slice = new float[grid[0].length];
        for (int i = 0; i < slice.length; i++) {
            slice[i] = grid[x][i];
        }
        return slice;
    }

    /**
     * Checks if a sequence of values is monotonic, ignoring values at the end of the sequence which
     * are NO_DATA.
     */
    private boolean isMonotonicSequence(float[] seq, final float noDataValue) {
        int istart = 0;
        for (int i = 0; i < seq.length; i++) {
            if (seq[i] != noDataValue) istart = i;
        }
        int iend = 0;
        for (int i = seq.length - 1; i >= 0; i--) {
            if (seq[i] != noDataValue) iend = i;
        }
        float globalSlope = Math.signum(seq[iend] - seq[istart]);

        // verify slope is identical throughout sequence
        for (int i = istart; i < iend; i++) {
            float localSlope = Math.signum(seq[i + 1] - seq[i]);
            if (localSlope != globalSlope) return false;
        }
        return true;
    }

    private void printGrid(float[][] grid) {
        for (int j = grid[0].length - 1; j >= 0; j--) {
            for (int i = 0; i < grid.length; i++) {
                // System.out.print(grid[i][j] + " ");
            }
            // System.out.println();
        }
    }
}
