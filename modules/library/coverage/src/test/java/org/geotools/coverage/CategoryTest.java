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
package org.geotools.coverage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;
import org.junit.Test;
import org.opengis.referencing.operation.MathTransform1D;
import org.opengis.referencing.operation.TransformException;

/**
 * Tests the {@link Category} implementation.
 *
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class CategoryTest {
    /** Random number generator for this test. */
    private static final Random random = new Random(9119969932919929834L);

    /** Checks if a {@link Comparable} is a number identical to the supplied integer value. */
    private static void assertValueEquals(String message, Comparable<?> number, int expected) {
        assertTrue("Integer.class", number instanceof Integer);
        assertEquals(message, expected, ((Number) number).intValue());
    }

    /** Checks if a {@link Comparable} is a number identical to the supplied float value. */
    private static void assertValueEquals(
            String message, Comparable<?> number, double expected, double EPS) {
        assertTrue("Double.class", number instanceof Double);
        final double actual = ((Number) number).doubleValue();
        if (Double.isNaN(expected)) {
            assertEquals(message, toHexString(expected), toHexString(actual));
        } else {
            assertEquals(message, expected, actual, EPS);
        }
    }

    /** Returns the specified value as an hexadecimal string. Usefull for comparing NaN values. */
    private static String toHexString(final double value) {
        return Integer.toHexString(Float.floatToRawIntBits((float) value));
    }

    /**
     * Make sure that linear category produce the expected result. This test check also if the
     * default {@link MathTransform1D} for a linear relation is right.
     *
     * @throws TransformException If an error occured while transforming a value.
     */
    @Test
    public void testLinearCategory() throws TransformException {
        for (int pass = 0; pass < 100; pass++) {
            final int lower = random.nextInt(64);
            final int upper = random.nextInt(128) + lower + 1;
            final Category category = new Category("Auto", null, lower, upper);

            assertValueEquals("lower", category.getRange().getMinValue().intValue(), lower);
            assertValueEquals("upper", category.getRange().getMaxValue().intValue(), upper);
        }
    }
}
