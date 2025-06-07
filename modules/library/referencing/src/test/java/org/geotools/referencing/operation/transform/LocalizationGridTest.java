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
package org.geotools.referencing.operation.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.referencing.operation.builder.LocalizationGrid;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the {@link LocalizationGrid} implementation.
 *
 * @version $Id$
 * @author Remi Eve
 * @author Martin Desruisseaux (IRD)
 */
public final class LocalizationGridTest {
    /** Random number generator for this test. */
    private static final Random random = new Random(78421369762L);

    /** The grid of localization to test. */
    private LocalizationGrid grid;

    /** The "real world" coordinates of the image. */
    private static final double[][] GRID_DATA = {
        {
            74.273440, -37.882812, /* First line. */
            72.695310, -38.375000,
            71.382810, -38.765625,
            70.250000, -39.085938,
            69.257810, -39.359375,
            68.375000, -39.585938
        },
        {
            74.273440, -37.875000, /* Second line. */
            72.695310, -38.367188,
            71.375000, -38.757812,
            70.250000, -39.078125,
            69.257810, -39.343750,
            68.367190, -39.578125
        },
        {
            74.265625, -37.867188, /* third line. */
            72.687500, -38.359375,
            71.375000, -38.750000,
            70.242190, -39.070312,
            69.250000, -39.335938,
            68.367190, -39.570312
        },
        {
            74.257810, -37.859375, /* fourth line. */
            72.687500, -38.351562,
            71.367190, -38.742188,
            70.234375, -39.062500,
            69.242190, -39.328125,
            68.359375, -39.562500
        },
        {
            74.257810, -37.851562, /* fifth line. */
            72.679690, -38.343750,
            71.359375, -38.734375,
            70.234375, -39.054688,
            69.242190, -39.320312,
            68.359375, -39.546875
        },
        {
            74.250000, -37.843750, /* sixth line. */
            72.671875, -38.335938,
            71.359375, -38.726562,
            70.226560, -39.039062,
            69.234375, -39.312500,
            68.351560, -39.539062
        }
    };

    /**
     * Maximal error between expected "real world" and computed "real world" coordinates when direct transformation is
     * used.
     */
    private static final double EPS = 1E-9;

    /**
     * Maximal error between expected "real world" and computed "real world" coordinates when the fitted affine
     * transformation is used.
     */
    private static final double FIT_TOLERANCE = 0.4;

    /** Set up common objects used for all tests. This implementation construct a default localization grid. */
    @Before
    public void setUp() {
        final int width = GRID_DATA[0].length / 2;
        final int height = GRID_DATA.length;
        grid = new LocalizationGrid(width, height);
        for (int j = 0; j < height; j++) {
            final double[] line = GRID_DATA[j];
            assertEquals("Grid is not square", width * 2L, line.length);
            int offset = 0;
            for (int i = 0; i < width; i++) {
                grid.setLocalizationPoint(i, j, line[offset++], line[offset++]);
            }
        }
    }

    /** Test some grid properties. */
    @Test
    public void testProperties() {
        assertTrue(grid.isMonotonic(false));
        assertFalse(grid.isMonotonic(true));
        assertFalse(grid.isNaN());
    }

    /**
     * Returns an array with grid or "real world" coordinates.
     *
     * @param real <code>true</code> to gets the "real world" coordinates, or <code>false</code> to gets the grid
     *     coordinates.
     */
    private double[] getGridCoordinates(final boolean real) {
        final int width = GRID_DATA[0].length / 2;
        final int height = GRID_DATA.length;
        final double[] array = new double[width * height * 2];
        int offset = 0;
        for (int j = 0; j < height; j++) {
            final double[] line = GRID_DATA[j];
            assertEquals("Grid is not square", width * 2L, line.length);
            for (int i = 0; i < width; i++) {
                array[offset++] = real ? line[i * 2 + 0] : i;
                array[offset++] = real ? line[i * 2 + 1] : j;
            }
        }
        assertEquals("Grid is not square", width * ((long) height) * 2L, offset);
        return array;
    }

    /**
     * Compare a transformed array with the expected grid or "real world" coordinates.
     *
     * @param array The array to compare.
     * @param real <code>true</code> to compare with the "real world" coordinates, or <code>false
     *     </code> to compare with the grid coordinates.
     * @param eps The maximal error accepted in comparaisons.
     */
    private void compare(final double[] array, final boolean real, final double eps) {
        final int width = GRID_DATA[0].length / 2;
        final int height = GRID_DATA.length;
        int offset = 0;
        for (int j = 0; j < height; j++) {
            final double[] line = GRID_DATA[j];
            assertEquals("Grid is not square", width * 2L, line.length);
            for (int i = 0; i < width; i++) {
                assertEquals(real ? line[i * 2 + 0] : i, array[offset++], eps);
                assertEquals(real ? line[i * 2 + 1] : j, array[offset++], eps);
            }
        }
        assertEquals("Grid is not square", width * ((long) height) * 2L, offset);
    }

    /** Test direct transformation from grid coordinates to "real world" coordinates using the localization grid. */
    @Test
    public void testDirectTransform() throws TransformException {
        final double[] array = getGridCoordinates(false);
        grid.getMathTransform().transform(array, 0, array, 0, array.length / 2);
        compare(array, true, EPS);
    }

    /**
     * Test affine tranformation for the whole grid by comparing the expected "real world" to the approximated
     * coordinates. Since the affine transform is fitted using least-squares method, the transformation is
     * approximative.
     */
    @Test
    public void testAffineTransform() {
        final double[] array = getGridCoordinates(false);
        grid.getAffineTransform().transform(array, 0, array, 0, array.length / 2);
        compare(array, true, FIT_TOLERANCE);
    }

    /**
     * Test inverse transformation from "real world" coordinates to grid coordinates coordinate using the localization
     * grid.
     */
    @Test
    public void testInverseTransform() throws TransformException {
        final double[] array = getGridCoordinates(true);
        grid.getMathTransform().inverse().transform(array, 0, array, 0, array.length / 2);
        compare(array, false, EPS);
    }

    /** Test some mathematical identities used if {@link LocalizationGrid#fitPlane}. */
    @Test
    public void testMathematicalIdentities() {
        int sum_x = 0;
        int sum_y = 0;
        int sum_xx = 0;
        int sum_yy = 0;
        int sum_xy = 0;

        final int width = random.nextInt(100) + 5;
        final int height = random.nextInt(100) + 5;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                sum_x += x;
                sum_y += y;
                sum_xx += x * x;
                sum_yy += y * y;
                sum_xy += x * y;
            }
        }
        final int n = width * height;
        assertEquals("sum_x", (n * (width - 1)) / 2, sum_x);
        assertEquals("sum_y", (n * (height - 1)) / 2, sum_y);
        assertEquals("sum_xy", (n * (width - 1) * (height - 1)) / 4, sum_xy);
        assertEquals("sum_xx", (n * (width - 0.5) * (width - 1)) / 3, sum_xx, 1E-6);
        assertEquals("sum_yy", (n * (height - 0.5) * (height - 1)) / 3, sum_yy, 1E-6);
    }
}
