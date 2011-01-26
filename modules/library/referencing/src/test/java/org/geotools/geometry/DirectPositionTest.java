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
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.geotools.referencing.crs.DefaultGeographicCRS;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the {@link GeneralDirectPosition} and {@link DirectPosition2D} classes.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class DirectPositionTest {
    /**
     * Tests {@link GeneralDirectPosition#equals} method between different implementations. The
     * purpose of this test is also to run the assertion in the direct position implementations.
     */
    @Test
    public void testEquals() {
        assertTrue(GeneralDirectPosition.class.desiredAssertionStatus());
        assertTrue(DirectPosition2D.class.desiredAssertionStatus());

        CoordinateReferenceSystem WGS84 = DefaultGeographicCRS.WGS84;
        DirectPosition p1 = new DirectPosition2D(WGS84, 48.543261561072285, -123.47009555832284);
        GeneralDirectPosition p2 = new GeneralDirectPosition(48.543261561072285, -123.47009555832284);
        assertFalse(p1.equals(p2));
        assertFalse(p2.equals(p1));

        p2.setCoordinateReferenceSystem(WGS84);
        assertTrue(p1.equals(p2));
        assertTrue(p2.equals(p1));
    }
}
