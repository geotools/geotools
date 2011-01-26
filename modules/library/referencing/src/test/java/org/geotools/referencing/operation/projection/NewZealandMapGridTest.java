/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.projection;

import org.opengis.referencing.operation.TransformException;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the {@link NewZealandMapGrid} implementation.
 *
 * @source $URL$
 * @version $Id$
 * @author Justin Deoliveira
 */
public final class NewZealandMapGridTest {
    /**
     * Sets of geographic coordinates to project.
     */
    private static final double[] GEOGRAPHIC = {
        172.739194,  -34.444066,
        172.723106,  -40.512409,
        169.172062,  -46.651295
    };

    /**
     * Set of projected coordinates.
     */
    private static final double[] PROJECTED = {
        2487100.638,  6751049.719,
        2486533.395,  6077263.661,
        2216746.425,  5388508.765
    };

    /**
     * Computes the forward transform and compares against the expected result.
     */
    @Test
    public void testTransform() throws TransformException {
        final double[] dst = new double[6];
        new NewZealandMapGrid().transform(GEOGRAPHIC, 0, dst, 0, 3);
        for (int i=0; i<PROJECTED.length; i++) {
            assertEquals(PROJECTED[i], dst[i], 0.1);   // 10 cm precision
        }
    }

    /**
     * Computes the inverse transform and compares against the expected result.
     */
    @Test
    public void testInverseTransform() throws TransformException {
        final double[] dst = new double[6];
        new NewZealandMapGrid().inverse().transform(PROJECTED, 0, dst, 0, 3);
        for (int i=0; i<GEOGRAPHIC.length; i++) {
            assertEquals(GEOGRAPHIC[i], dst[i], 0.0001); // About 10 m precision
        }
    }

    /**
     * Tests WKT formatting.
     */
    @Test
    public void testWKT() {
        final String wkt = new NewZealandMapGrid().toWKT();
        assertTrue(wkt.indexOf("central_meridian") >= 0);
    }
}
