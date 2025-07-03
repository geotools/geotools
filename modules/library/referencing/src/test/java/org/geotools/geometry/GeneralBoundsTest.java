/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.geotools.api.geometry.Bounds;
import org.geotools.api.geometry.Position;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;

/**
 * Tests the {@link GeneralBounds} class.
 *
 * @author Martin Desruisseaux
 */
public final class GeneralBoundsTest {

    @Test
    public void testConstruction() {
        GeneralBounds empty = new GeneralBounds(DefaultGeographicCRS.WGS84);
        assertTrue(empty.isEmpty());
        GeneralBounds empty2d = new GeneralBounds(DefaultGeographicCRS.WGS84);
        assertTrue(empty2d.isEmpty());

        GeneralBounds world = new GeneralBounds(new double[] {-180, -90}, new double[] {180, 90});
        world.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);

        GeneralBounds copyWorld = new GeneralBounds(world); // expected to work
        assertEquals(world, copyWorld);

        GeneralBounds copyEmpty = new GeneralBounds(empty); // expected to work
        assertEquals(empty, copyEmpty);

        GeneralBounds nil = new GeneralBounds(DefaultGeographicCRS.WGS84);
        nil.setToNull();

        assertTrue(nil.isNull());
        GeneralBounds copyNil = new GeneralBounds(nil); // expected to work

        assertTrue(copyNil.isNull());
        assertEquals(nil, copyNil);

        GeneralBounds nil2 = new GeneralBounds(nil);
        GeneralBounds copyNil2 = new GeneralBounds((Bounds) nil2); // expected to work

        assertTrue(copyNil2.isNull());

        // See http://jira.codehaus.org/browse/GEOT-3051
        GeneralBounds geot3045 = new GeneralBounds(new double[] {0, 0}, new double[] {-1, -1});
        assertTrue(geot3045.isEmpty());

        // See http://jira.codehaus.org/browse/GEOT-4261
        GeneralBounds geot4261 = new GeneralBounds(2);
        GeneralPosition dp = new GeneralPosition(new double[] {100.0, 200.0});
        assertTrue(geot4261.isEmpty());
        geot4261.add(dp);
        assertEquals(new GeneralBounds(dp, dp), geot4261);
    }

    /** Tests {@link GeneralBounds#equals} method. */
    @Test
    public void testEquals() {
        /*
         * Initialize an empty envelope. The new envelope is empty and null.
         */
        final GeneralBounds e1 = new GeneralBounds(4);
        assertTrue(e1.isEmpty());
        assertTrue(e1.isNull());
        assertEquals(e1.getLowerCorner(), e1.getUpperCorner());
        /*
         * Initialize with arbitrary coordinate values. Should not be empty anymore.
         */
        for (int i = e1.getDimension(); --i >= 0; ) {
            e1.setRange(i, i * 5 + 2, i * 6 + 5);
        }
        assertFalse(e1.isNull());
        assertFalse(e1.isEmpty());
        assertNotEquals(e1.getLowerCorner(), e1.getUpperCorner());
        /*
         * Creates a new envelope initialized with the same coordinate values. The two envelope
         * should be equals.
         */
        final GeneralBounds e2 = new GeneralBounds(e1);
        assertPositionEquals(e1.getLowerCorner(), e2.getLowerCorner());
        assertPositionEquals(e1.getUpperCorner(), e2.getUpperCorner());
        assertTrue(e1.contains(e2, true));
        assertFalse(e1.contains(e2, false));
        assertNotSame(e1, e2);
        assertEquals(e1, e2);
        assertTrue(e1.equals(e2, 1E-4, true));
        assertTrue(e1.equals(e2, 1E-4, false));
        assertEquals(e1.hashCode(), e2.hashCode());
        /*
         * Offset slightly one coordinate value. Should not be equals anymore, except when comparing
         * with a tolerance value.
         */
        e2.setRange(2, e2.getMinimum(2) + 3E-5, e2.getMaximum(2) - 3E-5);
        assertTrue(e1.contains(e2, true));
        assertFalse(e1.contains(e2, false));
        assertNotEquals(e1, e2);
        assertTrue(e1.equals(e2, 1E-4, true));
        assertTrue(e1.equals(e2, 1E-4, false));
        assertNotEquals(e1.hashCode(), e2.hashCode());
        /*
         * Apply a greater offset. Should not be equals, even when comparing with a tolerance value.
         */
        e2.setRange(1, e2.getMinimum(1) + 3, e2.getMaximum(1) - 3);
        assertTrue(e1.contains(e2, true));
        assertFalse(e1.contains(e2, false));
        assertNotEquals(e1, e2);
        assertFalse(e1.equals(e2, 1E-4, true));
        assertFalse(e1.equals(e2, 1E-4, false));
        assertNotEquals(e1.hashCode(), e2.hashCode());
    }

    /** Compares the specified corners. */
    private static void assertPositionEquals(final Position p1, final Position p2) {
        assertNotSame(p1, p2);
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void testMedian() {
        double delta = 0.00001;
        GeneralBounds bounds = new GeneralBounds(DefaultGeographicCRS.WGS84);
        bounds.setEnvelope(-10, -10, 10, 10);
        assertEquals(0.0, bounds.getMedian(0), delta);
        assertEquals(0.0, bounds.getMedian(1), delta);
    }
}
