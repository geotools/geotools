/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.grid;

import org.geotools.geometry.jts.ReferencedEnvelope;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the Grids class.
 *
 * @author mbedward
 * @since 2.7
 *
 * @source $URL$
 * @version $Id$
 */
public class EnvelopesTest {

    private static final double TOL = 1.0e-8d;

    @Test
    public void expand() {
        ReferencedEnvelope env = new ReferencedEnvelope(2.6, 9.4, 2.4, 9.6, null);
        ReferencedEnvelope result = Envelopes.expandToInclude(env, 1.0);
        ReferencedEnvelope expected = new ReferencedEnvelope(2.0, 10.0, 2.0, 10.0, null);
        assertTrue(expected.boundsEquals2D(result, TOL));
    }

    @Test
    public void expandNegativeX() {
        ReferencedEnvelope env = new ReferencedEnvelope(-2.6, -9.4, 2.4, 9.6, null);
        ReferencedEnvelope result = Envelopes.expandToInclude(env, 1.0);
        ReferencedEnvelope expected = new ReferencedEnvelope(-2.0, -10.0, 2.0, 10.0, null);
        assertTrue(expected.boundsEquals2D(result, TOL));
    }

    @Test
    public void expandNegativeY() {
        ReferencedEnvelope env = new ReferencedEnvelope(2.6, 9.4, -2.4, -9.6, null);
        ReferencedEnvelope result = Envelopes.expandToInclude(env, 1.0);
        ReferencedEnvelope expected = new ReferencedEnvelope(2.0, 10.0, -2.0, -10.0, null);
        assertTrue(expected.boundsEquals2D(result, TOL));
    }

}
