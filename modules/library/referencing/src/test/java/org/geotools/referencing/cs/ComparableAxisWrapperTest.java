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

import java.util.Arrays;
import javax.measure.unit.SI;

import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystemAxis;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the {@link ComparableAxisWrapper} class.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class ComparableAxisWrapperTest {
    /**
     * Tests sorting of axis.
     */
    @Test
    public void testSortAxis() {
        assertOrdered(new CoordinateSystemAxis[] {
            DefaultCoordinateSystemAxis.LONGITUDE,
            DefaultCoordinateSystemAxis.LATITUDE,
            DefaultCoordinateSystemAxis.ELLIPSOIDAL_HEIGHT
        }, new CoordinateSystemAxis[] {
            DefaultCoordinateSystemAxis.LONGITUDE,
            DefaultCoordinateSystemAxis.LATITUDE,
            DefaultCoordinateSystemAxis.ELLIPSOIDAL_HEIGHT
        });
        assertOrdered(new CoordinateSystemAxis[] {
            DefaultCoordinateSystemAxis.LATITUDE,
            DefaultCoordinateSystemAxis.ELLIPSOIDAL_HEIGHT,
            DefaultCoordinateSystemAxis.LONGITUDE
        }, new CoordinateSystemAxis[] {
            DefaultCoordinateSystemAxis.LONGITUDE,
            DefaultCoordinateSystemAxis.LATITUDE,
            DefaultCoordinateSystemAxis.ELLIPSOIDAL_HEIGHT
        });
    }

    /**
     * Tests sorting of directions.
     */
    @Test
    public void testSortDirections() {
        // A plausible CS.
        assertOrdered(new AxisDirection[] {
            AxisDirection.NORTH,
            AxisDirection.UP,
            AxisDirection.EAST
        }, new AxisDirection[] {
            AxisDirection.EAST,    // Right handed-rule
            AxisDirection.NORTH,   // Right handed-rule
            AxisDirection.UP
        });

        // A very dummy CS just for testing. The order of
        // any non-compass direction should be unchanged.
        assertOrdered(new AxisDirection[] {
            AxisDirection.GEOCENTRIC_Y,
            AxisDirection.NORTH_NORTH_WEST,
            AxisDirection.GEOCENTRIC_X,
            AxisDirection.NORTH_EAST,
            AxisDirection.PAST
        }, new AxisDirection[] {
            AxisDirection.NORTH_EAST,        // Right handed-rule
            AxisDirection.NORTH_NORTH_WEST,  // Right handed-rule
            AxisDirection.GEOCENTRIC_Y,
            AxisDirection.GEOCENTRIC_X,
            AxisDirection.PAST
        });

        // An other plausible CS.
        assertOrdered(new AxisDirection[] {
            AxisDirection.SOUTH,
            AxisDirection.DOWN,
            AxisDirection.WEST
        }, new AxisDirection[] {
            AxisDirection.WEST,   // Right handed-rule
            AxisDirection.SOUTH,  // Right handed-rule
            AxisDirection.DOWN
        });

        // An other plausible CS.
        assertOrdered(new AxisDirection[] {
            AxisDirection.SOUTH,
            AxisDirection.DOWN,
            AxisDirection.EAST
        }, new AxisDirection[] {
            AxisDirection.SOUTH,  // Right handed-rule
            AxisDirection.EAST,   // Right handed-rule
            AxisDirection.DOWN
        });
    }

    /**
     * Sorts the specified axis and compares against the expected result.
     */
    private static void assertOrdered(final CoordinateSystemAxis[] toTest,
                                      final CoordinateSystemAxis[] expected)
    {
        final boolean same = Arrays.equals(toTest, expected);
        assertEquals(!same, ComparableAxisWrapper.sort(toTest));
        assertTrue(Arrays.equals(toTest, expected));
    }

    /**
     * Sorts the specified directions and compares against the expected result.
     */
    private static void assertOrdered(final AxisDirection[] toTest,
                                      final AxisDirection[] expected)
    {
        assertOrdered(toAxis(toTest), toAxis(expected));
    }

    /**
     * Creates axis from the specified directions.
     */
    private static CoordinateSystemAxis[] toAxis(final AxisDirection[] directions) {
        final CoordinateSystemAxis[] axis = new CoordinateSystemAxis[directions.length];
        for (int i=0; i<directions.length; i++) {
            axis[i] = new DefaultCoordinateSystemAxis("Test", directions[i], SI.METER);
        }
        return axis;
    }
}
