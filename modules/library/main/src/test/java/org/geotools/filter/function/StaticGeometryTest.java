/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StaticGeometryTest {

    @Test
    public void testGreaterThan() {
        // First test as ints
        Object arg1 = 3;
        Object arg2 = 2;

        assertTrue(StaticGeometry.greaterThan(arg1, arg2));
        assertFalse(StaticGeometry.greaterThan(arg2, arg1));

        // And as doubles
        arg1 = 3.;
        arg2 = 2.98;

        assertTrue(StaticGeometry.greaterThan(arg1, arg2));
        assertFalse(StaticGeometry.greaterThan(arg2, arg1));

        // And actual string (NB this didn't work at all in previous implementation of
        // StaticGeometry.  Test is included here for clarity of intent.
        arg1 = "B";
        arg2 = "A";
        assertTrue(StaticGeometry.greaterThan(arg1, arg2));
        assertFalse(StaticGeometry.greaterThan(arg2, arg1));

        // And actual string, case varying
        arg1 = "a";
        arg2 = "B";
        assertTrue(StaticGeometry.greaterThan(arg1, arg2));
        assertFalse(StaticGeometry.greaterThan(arg2, arg1));

        // Now test as string numbers
        arg1 = "3";
        arg2 = "2";

        assertTrue(StaticGeometry.greaterThan(arg1, arg2));
        assertFalse(StaticGeometry.greaterThan(arg2, arg1));
        arg1 = "3.00";
        arg2 = "2.98";

        assertTrue(StaticGeometry.greaterThan(arg1, arg2));
        assertFalse(StaticGeometry.greaterThan(arg2, arg1));
    }

    @Test
    public void testLessThan() {
        // First test as ints
        Object arg1 = 3;
        Object arg2 = 2;

        assertFalse(StaticGeometry.lessThan(arg1, arg2));
        assertTrue(StaticGeometry.lessThan(arg2, arg1));

        // And as doubles
        arg1 = 3.;
        arg2 = 2.98;

        assertFalse(StaticGeometry.lessThan(arg1, arg2));
        assertTrue(StaticGeometry.lessThan(arg2, arg1));

        // Now test as strings
        arg1 = "3";
        arg2 = "2";

        assertFalse(StaticGeometry.lessThan(arg1, arg2));
        assertTrue(StaticGeometry.lessThan(arg2, arg1));
        arg1 = "3.00";
        arg2 = "2.98";

        assertFalse(StaticGeometry.lessThan(arg1, arg2));
        assertTrue(StaticGeometry.lessThan(arg2, arg1));
    }

    @Test
    public void testLessEqualThan() {
        // First test as ints
        Object arg1 = 3;
        Object arg2 = 2;

        assertFalse(StaticGeometry.lessEqualThan(arg1, arg2));
        assertTrue(StaticGeometry.lessEqualThan(arg2, arg1));
        assertTrue(StaticGeometry.lessEqualThan(arg2, arg2));

        // And as doubles
        arg1 = 3.;
        arg2 = 2.98;

        assertFalse(StaticGeometry.lessEqualThan(arg1, arg2));
        assertTrue(StaticGeometry.lessEqualThan(arg2, arg1));
        assertTrue(StaticGeometry.lessEqualThan(arg2, arg2));

        // Now test as strings
        arg1 = "3";
        arg2 = "2";

        assertFalse(StaticGeometry.lessEqualThan(arg1, arg2));
        assertTrue(StaticGeometry.lessEqualThan(arg2, arg1));
        assertTrue(StaticGeometry.lessEqualThan(arg2, arg2));
        arg1 = "3.00";
        arg2 = "2.98";

        assertFalse(StaticGeometry.lessEqualThan(arg1, arg2));
        assertTrue(StaticGeometry.lessEqualThan(arg2, arg1));
        assertTrue(StaticGeometry.lessEqualThan(arg2, arg2));
    }

    @Test
    public void testGreaterEqualThan() {
        // First test as ints
        Object arg1 = 3;
        Object arg2 = 2;

        assertTrue(StaticGeometry.greaterEqualThan(arg1, arg2));
        assertFalse(StaticGeometry.greaterEqualThan(arg2, arg1));
        assertTrue(StaticGeometry.greaterEqualThan(arg2, arg2));

        // And as doubles
        arg1 = 3.;
        arg2 = 2.98;

        assertTrue(StaticGeometry.greaterEqualThan(arg1, arg2));
        assertFalse(StaticGeometry.greaterEqualThan(arg2, arg1));
        assertTrue(StaticGeometry.greaterEqualThan(arg2, arg2));

        // Now test as strings
        arg1 = "3";
        arg2 = "2";

        assertTrue(StaticGeometry.greaterEqualThan(arg1, arg2));
        assertFalse(StaticGeometry.greaterEqualThan(arg2, arg1));
        assertTrue(StaticGeometry.greaterEqualThan(arg2, arg2));
        arg1 = "3.00";
        arg2 = "2.98";

        assertTrue(StaticGeometry.greaterEqualThan(arg1, arg2));
        assertFalse(StaticGeometry.greaterEqualThan(arg2, arg1));
        assertTrue(StaticGeometry.greaterEqualThan(arg2, arg2));
    }
}
