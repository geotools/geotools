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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import javax.measure.MetricPrefix;
import javax.measure.Unit;
import javax.measure.quantity.Length;
import org.geotools.api.referencing.cs.AxisDirection;
import org.geotools.api.referencing.cs.CoordinateSystem;
import org.geotools.api.referencing.cs.CoordinateSystemAxis;
import org.geotools.api.referencing.operation.Matrix;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.junit.Test;
import si.uom.SI;

/**
 * Tests the {@link AbstractCS} class.
 *
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class AbstractCSTest {
    /** Tests the swapping of axis. */
    @Test
    public void testAxisSwapping() {
        CoordinateSystem cs1 = new DefaultEllipsoidalCS(
                "cs1", DefaultCoordinateSystemAxis.GEODETIC_LONGITUDE, DefaultCoordinateSystemAxis.GEODETIC_LATITUDE);
        CoordinateSystem cs2 = new DefaultEllipsoidalCS(
                "cs2", DefaultCoordinateSystemAxis.GEODETIC_LATITUDE, DefaultCoordinateSystemAxis.GEODETIC_LONGITUDE);
        assertTrue(AbstractCS.swapAndScaleAxis(cs1, cs1).isIdentity());
        assertTrue(AbstractCS.swapAndScaleAxis(cs2, cs2).isIdentity());
        compareMatrix(cs1, cs2, new double[] {
            0, 1, 0,
            1, 0, 0,
            0, 0, 1
        });

        cs1 = new DefaultEllipsoidalCS(
                "cs1",
                DefaultCoordinateSystemAxis.GEODETIC_LONGITUDE,
                DefaultCoordinateSystemAxis.GEODETIC_LATITUDE,
                DefaultCoordinateSystemAxis.ELLIPSOIDAL_HEIGHT);
        cs2 = new DefaultEllipsoidalCS(
                "cs2",
                DefaultCoordinateSystemAxis.GEODETIC_LATITUDE,
                DefaultCoordinateSystemAxis.GEODETIC_LONGITUDE,
                DefaultCoordinateSystemAxis.ELLIPSOIDAL_HEIGHT);
        compareMatrix(cs1, cs2, new double[] {
            0, 1, 0, 0,
            1, 0, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1
        });

        cs1 = new DefaultCartesianCS(
                "cs1",
                DefaultCoordinateSystemAxis.ELLIPSOIDAL_HEIGHT,
                DefaultCoordinateSystemAxis.EASTING,
                DefaultCoordinateSystemAxis.NORTHING);
        cs2 = new DefaultCartesianCS(
                "cs2",
                DefaultCoordinateSystemAxis.SOUTHING,
                DefaultCoordinateSystemAxis.EASTING,
                DefaultCoordinateSystemAxis.ELLIPSOIDAL_HEIGHT);
        compareMatrix(cs1, cs2, new double[] {
            0, 0, -1, 0,
            0, 1, 0, 0,
            1, 0, 0, 0,
            0, 0, 0, 1
        });
    }

    /** Compares the matrix computes by {@link AbstractCS#swapAndScaleAxis} with the specified one. */
    private static void compareMatrix(final CoordinateSystem cs1, final CoordinateSystem cs2, final double[] expected) {
        final Matrix matrix = AbstractCS.swapAndScaleAxis(cs1, cs2);
        final int numRow = matrix.getNumRow();
        final int numCol = matrix.getNumCol();
        assertEquals(expected.length, numRow * (long) numCol);
        final Matrix em = new GeneralMatrix(numRow, numCol, expected);
        assertEquals(em, matrix);
    }

    /** Tests {@link AbstractCS#axisUsingUnit}. */
    @Test
    public void testAxisUsingUnit() {
        assertNull("Should detect that no axis change is needed", DefaultCartesianCS.PROJECTED.axisUsingUnit(SI.METRE));

        final Unit<Length> KILOMETER = MetricPrefix.KILO(SI.METRE);
        final CoordinateSystemAxis[] axis = DefaultCartesianCS.PROJECTED.axisUsingUnit(KILOMETER);
        assertNotNull(axis);
        assertEquals("Expected two-dimensional", 2, axis.length);
        assertEquals(KILOMETER, axis[0].getUnit());
        assertEquals(KILOMETER, axis[1].getUnit());
        assertEquals(AxisDirection.EAST, axis[0].getDirection());
        assertEquals(AxisDirection.NORTH, axis[1].getDirection());
        assertEquals("Easting", axis[0].getName().getCode());
        assertEquals("Northing", axis[1].getName().getCode());
    }

    /** Tests {@link AbstractCS#standard}. */
    @Test
    public void testStandards() {
        CoordinateSystem cs = DefaultCartesianCS.GRID;
        assertSame(cs, AbstractCS.standard(cs));
        cs = DefaultCartesianCS.GEOCENTRIC;
        assertSame(cs, AbstractCS.standard(cs));
        cs = DefaultCartesianCS.GENERIC_2D;
        assertSame(cs, AbstractCS.standard(cs));
        cs = DefaultCartesianCS.GENERIC_3D;
        assertSame(cs, AbstractCS.standard(cs));
        cs = DefaultCartesianCS.PROJECTED;
        assertSame(cs, AbstractCS.standard(cs));
        cs = DefaultEllipsoidalCS.GEODETIC_2D;
        assertSame(cs, AbstractCS.standard(cs));
        cs = DefaultEllipsoidalCS.GEODETIC_3D;
        assertSame(cs, AbstractCS.standard(cs));
        cs = DefaultSphericalCS.GEOCENTRIC;
        assertSame(cs, AbstractCS.standard(cs));
        cs = DefaultTimeCS.DAYS;
        assertSame(cs, AbstractCS.standard(cs));
        cs = DefaultVerticalCS.ELLIPSOIDAL_HEIGHT;
        assertSame(cs, AbstractCS.standard(cs));
        cs = DefaultVerticalCS.GRAVITY_RELATED_HEIGHT;
        assertSame(
                "\"Standard\" vertical axis should be forced to ellipsoidal height.",
                DefaultVerticalCS.ELLIPSOIDAL_HEIGHT,
                AbstractCS.standard(cs));
    }
}
