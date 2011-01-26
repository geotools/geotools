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
package org.geotools.coverage;

import java.util.Arrays;
import java.util.Random;

import org.opengis.referencing.operation.TransformException;
import org.geotools.resources.XArray;
import org.geotools.util.Range;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the {@link CategoryList} implementation.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class CategoryListTest {
    /**
     * Set to {@code true} in order to print diagnostic messages.
     */
    private static final boolean VERBOSE = false;

    /**
     * Small value for comparaisons.
     */
    private static final double EPS = 1E-9;

    /**
     * Random number generator for this test.
     */
    private static final Random random = new Random(1471753385855374101L);

    /**
     * Returns the specified value as an hexadecimal string. Usefull
     * for comparing NaN values.
     */
    private static String toHexString(final double value) {
        return Integer.toHexString(Float.floatToRawIntBits((float)value));
    }

    /**
     * Tests the {@link CategoryList#binarySearch} method.
     */
    @Test
    public void testBinarySearch() {
        for (int pass=0; pass<50; pass++) {
            final double[] array = new double[64];
            for (int i=0; i<array.length; i++) {
                array[i] = (random.nextInt(100) - 50) / 10;
            }
            Arrays.sort(array);
            for (int i=0; i<300; i++) {
                final double searchFor = (random.nextInt(150) - 75) / 10;
                assertEquals("binarySearch", Arrays.binarySearch(array, searchFor),
                                       CategoryList.binarySearch(array, searchFor));
            }
            /*
             * Previous test didn't tested NaN values (which is the main difference
             * between binarySearch method in Arrays and CategoryList). Now test it.
             */
            final Category[] categories = new Category[array.length];
            for (int i=0; i<categories.length; i++) {
                categories[i] = new Category(String.valueOf(i), null, random.nextInt(100)).inverse;
            }
            Arrays.sort(categories, new CategoryList(new Category[0], null));
            assertTrue("isSorted", CategoryList.isSorted(categories));
            for (int i=0; i<categories.length; i++) {
                array[i] = categories[i].inverse.minimum;
            }
            for (int i=0; i<categories.length; i++) {
                final double expected = categories[i].inverse.minimum;
                final int    foundAt  = CategoryList.binarySearch(array, expected);
                final double actual   = categories[foundAt].inverse.minimum;
                assertEquals("binarySearch", toHexString(expected), toHexString(actual));
            }
        }
    }

    /**
     * Tests the {@link CategoryList} constructor.
     */
    @Test
    public void testArgumentChecks() {
        Category[] categories;
        categories = new Category[] {
            new Category("No data",     null, 0),
            new Category("Land",        null, 10),
            new Category("Clouds",      null, 2),
            new Category("Land again",  null, 10) // Range overlaps.
        };
        try {
            new CategoryList(categories, null);
            fail("Argument check");
        } catch (IllegalArgumentException exception) {
            if (VERBOSE) {
                System.out.println(exception.getLocalizedMessage());
                // This is the expected exception.
            }
        }
        for (int i=0; i<categories.length; i++) {
            final Category cat = categories[i];
            assertSame(cat, cat.geophysics(true).geophysics(false));
            categories[i] = cat.geophysics(true);
        }
        try {
            new CategoryList(categories, null);
            fail("Argument check");
        } catch (IllegalArgumentException exception) {
            if (VERBOSE) {
                System.out.println(exception.getLocalizedMessage());
                // This is the expected exception.
            }
        }
        // Removes the wrong category. Now, construction should succed.
        categories = XArray.resize(categories, categories.length-1);
        new CategoryList(categories, null);
    }

    /**
     * Tests the {@link CategoryList#getCategory} method and a
     * limited set of {@link CategoryList#transform} calls.
     *
     * @throws TransformException If an error occured while transforming a value.
     */
    @Test
    public void testGetCategory() throws TransformException {
        final Category[] categories = new Category[] {
            /*[0]*/ new Category("No data",     null, 0),
            /*[1]*/ new Category("Land",        null, 7),
            /*[2]*/ new Category("Clouds",      null, 3),
            /*[3]*/ new Category("Temperature", null, 10, 100, 0.1, 5),
            /*[4]*/ new Category("Foo",         null, 100, 120, -1, 3)
        };
        CategoryList list;
        boolean searchNearest = false;
        do {
            list = new CategoryList(categories, null, searchNearest, null);
            assertTrue("containsAll", list.containsAll(Arrays.asList(categories)));
            assertSame(list.geophysics(true), list.inverse());
            assertSame(list.geophysics(true).geophysics(false), list);
            assertSame(list.geophysics(false), list);

            final Range range = list.getRange();
            assertEquals("min", 0,   ((Number)range.getMinValue()).doubleValue(), 0);
            assertEquals("max", 120, ((Number)range.getMaxValue()).doubleValue(), 0);
            assertTrue  ("min included", range.isMinIncluded() == true);
            assertTrue  ("max included", range.isMaxIncluded() == false);
            /*
             * Checks category search.
             */
            assertSame(  "0", list.getCategory(  0), categories[0]);
            assertSame(  "7", list.getCategory(  7), categories[1]);
            assertSame(  "3", list.getCategory(  3), categories[2]);
            assertSame(" 10", list.getCategory( 10), categories[3]);
            assertSame(" 50", list.getCategory( 50), categories[3]);
            assertSame("100", list.getCategory(100), categories[4]);
            assertSame("110", list.getCategory(110), categories[4]);
            if (searchNearest) {
                assertSame( "-1", list.getCategory( -1), categories[0]); // Nearest sample is 0.
                assertSame(  "2", list.getCategory(  2), categories[2]); // Nearest sample is 3.
                assertSame(  "4", list.getCategory(  4), categories[2]); // Nearest sample is 3.
                assertSame(  "9", list.getCategory(  9), categories[3]); // Nearest sample is 10.
                assertSame("120", list.getCategory(120), categories[4]); // Nearest sample is 119
                assertSame("200", list.getCategory(200), categories[4]); // Nearest sample is 119
            } else {
                assertNull( "-1", list.getCategory( -1));
                assertNull(  "2", list.getCategory(  2));
                assertNull(  "4", list.getCategory(  4));
                assertNull(  "9", list.getCategory(  9));
                assertNull("120", list.getCategory(120));
                assertNull("200", list.getCategory(200));
            }
            /*
             * Checks transformations.
             */
            assertTrue  (  "0", Double.isNaN(list.transform(0)));
            assertTrue  (  "7", Double.isNaN(list.transform(7)));
            assertTrue  (  "3", Double.isNaN(list.transform(3)));
            assertEquals( "10",    6, list.transform( 10), EPS);
            assertEquals( "50",   10, list.transform( 50), EPS);
            assertEquals("100",  -97, list.transform(100), EPS);
            assertEquals("110", -107, list.transform(110), EPS);
            try {
                assertEquals("9", searchNearest ? 6 : 5.9, list.transform(9), EPS);
                if (!searchNearest) {
                    fail();
                }
            } catch (TransformException exception) {
                if (searchNearest) {
                    throw exception;
                }
            }
        } while ((searchNearest = !searchNearest) == true);
        /*
         * Test transformation using methods working on arrays.
         * We assume that the 'transform(double)' version can
         * be used as a reference.
         */
        final double[] input   = new double[512];
        final double[] output0 = new double[input.length];
        final double[] output1 = new double[input.length];
        for (int i=0; i<input.length; i++) {
            input  [i] = random.nextInt(130)-5;
            output0[i] = list.transform(input[i]);
        }
        list.transform(input, 0, output1, 0, input.length);
        compare(output0, output1, EPS);
        /*
         * Test the transform using overlapping array.
         */
        System.arraycopy(input, 0, output1, 3, input.length-3);
        list.transform (output1, 3, output1, 0, input.length-3);
        System.arraycopy(output0, input.length-3, output1, input.length-3, 3);
        compare(output0, output1, EPS);
        // Implementation will do the following transform in reverse direction.
        System.arraycopy(input, 3, output1, 0, input.length-3);
        list.transform (output1, 0, output1, 3, input.length-3);
        System.arraycopy(output0, 0, output1, 0, 3);
        compare(output0, output1, EPS);
        // Test inverse transform
        list.inverse().transform(output0, 0, output0, 0, output0.length);
        for (int i=0; i<output0.length; i++) {
            final double expected = input[i];
            if (expected >= 10 && expected < 120) {
                // Values outside this range have been clamped.
                // They would usually not be equal.
                assertEquals("inverse", expected, output0[i], EPS);
            }
        }
    }

    /**
     * Compares two arrays. Special comparaison is performed for NaN values.
     */
    static void compare(final double[] output0, final double[] output1, final double eps) {
        assertEquals("length", output0.length, output1.length);
        for (int i=0; i<output0.length; i++) {
            final double expected = output0[i];
            final double actual   = output1[i];
            final String name = "transform[" + i + ']';
            if (Double.isNaN(expected)) {
                final String hex1 = Integer.toHexString(Float.floatToRawIntBits((float) expected));
                final String hex2 = Integer.toHexString(Float.floatToRawIntBits((float)   actual));
                assertEquals(name, hex1, hex2);
                continue;
            }
            assertEquals(name, expected, actual, eps);
        }
    }
}
