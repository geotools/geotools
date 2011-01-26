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

import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;

import org.geotools.referencing.crs.DefaultGeocentricCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Tests the {@link GeneralEnvelope} class.
 * 
 * @source $URL:
 *         http://svn.osgeo.org/geotools/branches/2.6.x/modules/library/referencing/src/test/java
 *         /org/geotools/geometry/GeneralEnvelopeTest.java $
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class GeneralEnvelopeTest {

    @Test
    public void testConstruction() {
        GeneralEnvelope empty = new GeneralEnvelope(DefaultGeographicCRS.WGS84);
        assertTrue(empty.isEmpty());
        Envelope2D empty2d = new Envelope2D();
        assertTrue(empty2d.isEmpty());

        GeneralEnvelope world = new GeneralEnvelope(new double[] { -180, -90 }, new double[] { 180,
                90 });
        world.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);

        GeneralEnvelope copyWorld = new GeneralEnvelope(world); // expected to work
        assertEquals(world, copyWorld);

        GeneralEnvelope copyEmpty = new GeneralEnvelope(empty); // expected to work
        assertEquals(empty, copyEmpty);

        GeneralEnvelope nil = new GeneralEnvelope(DefaultGeographicCRS.WGS84);
        nil.setToNull();

        assertTrue(nil.isNull());
        GeneralEnvelope copyNil = new GeneralEnvelope(nil); // expected to work

        assertTrue(copyNil.isNull());
        assertEquals(nil, copyNil);

        Envelope2D nil2 = new Envelope2D(nil);
        GeneralEnvelope copyNil2 = new GeneralEnvelope((Envelope) nil2); // expected to work

        assertTrue(copyNil2.isNull());
        
        // See http://jira.codehaus.org/browse/GEOT-3051
        GeneralEnvelope geot3045 = new GeneralEnvelope(new double[]{0,0}, new double[]{-1,-1}); 
        assertTrue( geot3045.isEmpty() );

    }

    /**
     * Tests {@link GeneralEnvelope#equals} method.
     */
    @Test
    public void testEquals() {
        /*
         * Initialize an empty envelope. The new envelope is empty but not null because initialized
         * to 0, not NaN.
         */
        final GeneralEnvelope e1 = new GeneralEnvelope(4);
        assertTrue(e1.isEmpty());
        assertFalse(e1.isNull());
        assertEquals(e1.getLowerCorner(), e1.getUpperCorner());
        /*
         * Initialize with arbitrary coordinate values. Should not be empty anymore.
         */
        for (int i = e1.getDimension(); --i >= 0;) {
            e1.setRange(i, i * 5 + 2, i * 6 + 5);
        }
        assertFalse(e1.isNull());
        assertFalse(e1.isEmpty());
        assertFalse(e1.getLowerCorner().equals(e1.getUpperCorner()));
        /*
         * Creates a new envelope initialized with the same coordinate values. The two envelope
         * should be equals.
         */
        final GeneralEnvelope e2 = new GeneralEnvelope(e1);
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
        assertFalse(e1.equals(e2));
        assertTrue(e1.equals(e2, 1E-4, true));
        assertTrue(e1.equals(e2, 1E-4, false));
        assertFalse(e1.hashCode() == e2.hashCode());
        /*
         * Apply a greater offset. Should not be equals, even when comparing with a tolerance value.
         */
        e2.setRange(1, e2.getMinimum(1) + 3, e2.getMaximum(1) - 3);
        assertTrue(e1.contains(e2, true));
        assertFalse(e1.contains(e2, false));
        assertFalse(e1.equals(e2));
        assertFalse(e1.equals(e2, 1E-4, true));
        assertFalse(e1.equals(e2, 1E-4, false));
        assertFalse(e1.hashCode() == e2.hashCode());
    }

    /**
     * Compares the specified corners.
     */
    private static void assertPositionEquals(final DirectPosition p1, final DirectPosition p2) {
        assertNotSame(p1, p2);
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }
}
