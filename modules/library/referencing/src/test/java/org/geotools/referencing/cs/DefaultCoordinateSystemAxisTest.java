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

import org.opengis.referencing.cs.AxisDirection;
import static org.geotools.referencing.cs.DefaultCoordinateSystemAxis.*;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the {@link DefaultCoordinateSystemAxis} class.
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class DefaultCoordinateSystemAxisTest {
    /**
     * For floating point number comparaisons.
     */
    private static final double EPS = 1E-10;

    /**
     * Tests the {@link DefaultCoordinateSystemAxis#nameMatches} method.
     */
    @Test
    public void testNameMatches() {
        assertTrue (LONGITUDE.nameMatches(GEODETIC_LONGITUDE.getName().getCode()));
        assertFalse(LONGITUDE.nameMatches(GEODETIC_LATITUDE .getName().getCode()));
        assertFalse(LONGITUDE.nameMatches(ALTITUDE          .getName().getCode()));
        assertFalse(X        .nameMatches(LONGITUDE         .getName().getCode()));
        assertFalse(X        .nameMatches(EASTING           .getName().getCode()));
        assertFalse(X        .nameMatches(NORTHING          .getName().getCode()));
    }

    /**
     * Tests the {@link DefaultCoordinateSystemAxis#getPredefined(String)} method.
     */
    @Test
    public void testPredefined() {
        assertNull(getPredefined("Dummy", null));

        // Tests some abbreviations shared by more than one axis.
        // We should get the axis with the ISO 19111 name.
        assertSame(GEODETIC_LATITUDE,  getPredefined("\u03C6", null));
        assertSame(GEODETIC_LONGITUDE, getPredefined("\u03BB", null));
        assertSame(ELLIPSOIDAL_HEIGHT, getPredefined("h",      null));

        // The following abbreviation are used by WKT parsing
        assertSame(GEOCENTRIC_X, getPredefined("X",   AxisDirection.OTHER));
        assertSame(GEOCENTRIC_Y, getPredefined("Y",   AxisDirection.EAST));
        assertSame(GEOCENTRIC_Z, getPredefined("Z",   AxisDirection.NORTH));
        assertSame(LONGITUDE,    getPredefined("Lon", AxisDirection.EAST));
        assertSame(LATITUDE,     getPredefined("Lat", AxisDirection.NORTH));
        assertSame(X,            getPredefined("X",   AxisDirection.EAST));
        assertSame(Y,            getPredefined("Y",   AxisDirection.NORTH));
        assertSame(Z,            getPredefined("Z",   AxisDirection.UP));

        // Tests from names
        assertSame(LATITUDE,           getPredefined("Latitude",           null));
        assertSame(LONGITUDE,          getPredefined("Longitude",          null));
        assertSame(GEODETIC_LATITUDE,  getPredefined("Geodetic latitude",  null));
        assertSame(GEODETIC_LONGITUDE, getPredefined("Geodetic longitude", null));
        assertSame(NORTHING,           getPredefined("Northing",           null));
        assertSame(NORTHING,           getPredefined("N",                  null));
        assertSame(EASTING,            getPredefined("Easting",            null));
        assertSame(EASTING,            getPredefined("E",                  null));
        assertSame(SOUTHING,           getPredefined("Southing",           null));
        assertSame(SOUTHING,           getPredefined("S",                  null));
        assertSame(WESTING,            getPredefined("Westing",            null));
        assertSame(WESTING,            getPredefined("W",                  null));
        assertSame(GEOCENTRIC_X,       getPredefined("X",                  null));
        assertSame(GEOCENTRIC_Y,       getPredefined("Y",                  null));
        assertSame(GEOCENTRIC_Z,       getPredefined("Z",                  null));
        assertSame(X,                  getPredefined("x",                  null));
        assertSame(Y,                  getPredefined("y",                  null));
        assertSame(Z,                  getPredefined("z",                  null));
    }

    /**
     * Tests the {@link DefaultCoordinateSystemAxis#getPredefined(CoordinateSystemAxis)} method.
     */
    @Test
    public void testPredefinedAxis() {
        // A few hard-coded tests for debugging convenience.
        assertSame(LATITUDE,          getPredefined(LATITUDE));
        assertSame(GEODETIC_LATITUDE, getPredefined(GEODETIC_LATITUDE));

        // Tests all constants.
        final DefaultCoordinateSystemAxis[] values = DefaultCoordinateSystemAxis.values();
        for (int i=0; i<values.length; i++) {
            final DefaultCoordinateSystemAxis axis = values[i];
            final String message = "values[" + i + ']';
            assertNotNull(message, axis);
            assertSame(message, axis, getPredefined(axis));
        }
    }

    /**
     * Makes sure that the compass directions in {@link AxisDirection} are okay.
     */
    @Test
    public void testCompass() {
        final AxisDirection[] compass = new AxisDirection[] {
            AxisDirection.NORTH,
            AxisDirection.NORTH_NORTH_EAST,
            AxisDirection.NORTH_EAST,
            AxisDirection.EAST_NORTH_EAST,
            AxisDirection.EAST,
            AxisDirection.EAST_SOUTH_EAST,
            AxisDirection.SOUTH_EAST,
            AxisDirection.SOUTH_SOUTH_EAST,
            AxisDirection.SOUTH,
            AxisDirection.SOUTH_SOUTH_WEST,
            AxisDirection.SOUTH_WEST,
            AxisDirection.WEST_SOUTH_WEST,
            AxisDirection.WEST,
            AxisDirection.WEST_NORTH_WEST,
            AxisDirection.NORTH_WEST,
            AxisDirection.NORTH_NORTH_WEST
        };
        assertEquals(compass.length, COMPASS_DIRECTION_COUNT);
        final int base = AxisDirection.NORTH.ordinal();
        final int h = compass.length / 2;
        for (int i=0; i<compass.length; i++) {
            final String index = "compass[" + i +']';
            final AxisDirection c = compass[i];
            double angle = i * (360.0/compass.length);
            if (angle > 180) {
                angle -= 360;
            }
            assertEquals(index, base + i, c.ordinal());
            assertEquals(index, base + i + (i<h ? h : -h), c.opposite().ordinal());
            assertEquals(index, 0, getAngle(c, c), EPS);
            assertEquals(index, 180, Math.abs(getAngle(c, c.opposite())), EPS);
            assertEquals(index, angle, getAngle(c, AxisDirection.NORTH), EPS);
        }
    }

    /**
     * Tests {@link DefaultCoordinateSystemAxis#getAngle}.
     */
    @Test
    public void testAngle() {
        assertEquals( 90.0, getAngle(AxisDirection.WEST,             AxisDirection.SOUTH),      EPS);
        assertEquals(-90.0, getAngle(AxisDirection.SOUTH,            AxisDirection.WEST),       EPS);
        assertEquals( 45.0, getAngle(AxisDirection.SOUTH,            AxisDirection.SOUTH_EAST), EPS);
        assertEquals(-22.5, getAngle(AxisDirection.NORTH_NORTH_WEST, AxisDirection.NORTH),      EPS);
    }

    /**
     * Tests {@link DefaultCoordinateSystemAxis#getAngle} using textual directions.
     */
    @Test
    public void testAngle2() {
        compareAngle( 90.0, "West", "South");
        compareAngle(-90.0, "South", "West");
        compareAngle( 45.0, "South", "South-East");
        compareAngle(-22.5, "North-North-West", "North");
        compareAngle(-22.5, "North_North_West", "North");
        compareAngle(-22.5, "North North West", "North");
        compareAngle( 90.0, "North along 90 deg East", "North along 0 deg");
        compareAngle( 90.0, "South along 180 deg", "South along 90 deg West");
    }

    /**
     * Compare the angle between the specified directions.
     */
    private static void compareAngle(final double expected, final String source, final String target) {
        final AxisDirection dir1 = getDirection(source);
        final AxisDirection dir2 = getDirection(target);
        assertNotNull(dir1);
        assertNotNull(dir2);
        assertEquals(expected, getAngle(dir1, dir2), EPS);
    }
}
