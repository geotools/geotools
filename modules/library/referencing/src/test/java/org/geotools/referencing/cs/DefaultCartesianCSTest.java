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
package org.geotools.referencing.cs;

import javax.measure.unit.SI;

import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystem;
import org.geotools.referencing.CRS;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the {@link CartesianCS} class.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class DefaultCartesianCSTest {
    /**
     * Tests the creation of a cartesian CS with legal and illegal axis.
     */
    @Test
    public void testAxis() {
        DefaultCartesianCS cs;
        try {
            cs = new DefaultCartesianCS("Test",
                    DefaultCoordinateSystemAxis.LONGITUDE,
                    DefaultCoordinateSystemAxis.LATITUDE);
            fail("Angular units should not be accepted.");
        } catch (IllegalArgumentException e) {
            // Expected exception: illegal angular units.
        }

        // Legal CS (the most usual one).
        cs = new DefaultCartesianCS("Test",
                DefaultCoordinateSystemAxis.EASTING,
                DefaultCoordinateSystemAxis.NORTHING);

        try {
            cs = new DefaultCartesianCS("Test",
                    DefaultCoordinateSystemAxis.SOUTHING,
                    DefaultCoordinateSystemAxis.NORTHING);
            fail("Colinear units should not be accepted.");
        } catch (IllegalArgumentException e) {
            // Expected exception: colinear axis.
        }

        // Legal CS rotated 45°
        cs = create(AxisDirection.NORTH_EAST, AxisDirection.SOUTH_EAST);

        try {
            cs = create(AxisDirection.NORTH_EAST, AxisDirection.EAST);
            fail("Non-perpendicular axis should not be accepted.");
        } catch (IllegalArgumentException e) {
            // Expected exception: non-perpendicular axis.
        }

        // Legal CS, but no perpendicularity check.
        cs = create(AxisDirection.NORTH_EAST, AxisDirection.UP);

        // Inconsistent axis direction.
        try {
            cs = new DefaultCartesianCS("Test",
                    DefaultCoordinateSystemAxis.EASTING,
                    new DefaultCoordinateSystemAxis("Northing", AxisDirection.SOUTH, SI.METER));
        } catch (IllegalArgumentException e) {
            // Expected exception: inconsistent direction.
        }
    }

    /**
     * Tests {@link AbstractCS#standard} with cartesian CS, especially
     * the ones that leads to the creation of right-handed CS.
     */
    @Test
    public void testStandard() {
        // ----------- Axis to test ------ Expected axis --
        assertOrdered("East", "North",    "East", "North");
        assertOrdered("North", "East",    "East", "North");
        assertOrdered("South", "East",    "East", "North");
        assertOrdered("South", "West",    "East", "North");

        assertOrdered("East",                       "North");
        assertOrdered("South-East",                 "North-East");
        assertOrdered("North along  90 deg East",   "North along   0 deg");
        assertOrdered("North along  90 deg East",   "North along   0 deg");
        assertOrdered("North along  75 deg West",   "North along 165 deg West");
        assertOrdered("South along  90 deg West",   "South along   0 deg");
        assertOrdered("South along 180 deg",        "South along  90 deg West");
        assertOrdered("North along 130 deg West",   "North along 140 deg East");
    }

    /**
     * Creates an axis for testing purpose for the specified direction.
     */
    private static DefaultCoordinateSystemAxis create(final AxisDirection direction) {
        if (direction.equals(AxisDirection.NORTH)) {
            return DefaultCoordinateSystemAxis.NORTHING;
        }
        if (direction.equals(AxisDirection.EAST)) {
            return DefaultCoordinateSystemAxis.EASTING;
        }
        if (direction.equals(AxisDirection.SOUTH)) {
            return DefaultCoordinateSystemAxis.SOUTHING;
        }
        if (direction.equals(AxisDirection.WEST)) {
            return DefaultCoordinateSystemAxis.WESTING;
        }
        return new DefaultCoordinateSystemAxis("Test", direction, SI.METER);
    }

    /**
     * Creates a coordinate system with the specified axis directions.
     */
    private static DefaultCartesianCS create(final AxisDirection x, final AxisDirection y) {
        return new DefaultCartesianCS("Test", create(x), create(y));
    }

    /**
     * Creates a coordinate system with the specified axis directions.
     */
    private static DefaultCartesianCS create(final String x, final String y) {
        return create(DefaultCoordinateSystemAxis.getDirection(x),
                      DefaultCoordinateSystemAxis.getDirection(y));
    }

    /**
     * Tests ordering with a CS created from the specified axis.
     */
    private static void assertOrdered(final String expectedX, final String expectedY) {
        assertOrdered(expectedY, expectedX, expectedX, expectedY);
        assertOrdered(expectedX, expectedY, expectedX, expectedY);
    }

    /**
     * Creates a cartesian CS using the provided test axis, invoke {@link AbstractCS#standard}
     * with it and compare with the expected axis.
     */
    private static void assertOrdered(final String testX,     final String testY,
                                      final String expectedX, final String expectedY)
    {
        final CoordinateSystem cs = AbstractCS.standard(create(testX, testY));
        assertTrue(CRS.equalsIgnoreMetadata(create(expectedX, expectedY), cs));
    }
}
