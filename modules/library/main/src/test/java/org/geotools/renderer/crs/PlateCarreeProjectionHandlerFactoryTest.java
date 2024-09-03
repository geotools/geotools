/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.crs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;

@SuppressWarnings("PMD.SimplifiableTestAssertion")
public class PlateCarreeProjectionHandlerFactoryTest {

    private static final ReferencedEnvelope WORLD = new ReferencedEnvelope(-180, 180, -90, 90, null);

    @Test
    public void testWorldValidArea_NoWrap() throws Exception {
        // Rendering CRS as geographic WGS84 -> Plate Carrée rendering
        CoordinateReferenceSystem plateCarree = CRS.decode("EPSG:4326", true);

        ReferencedEnvelope re = new ReferencedEnvelope(WORLD, plateCarree);

        // wrap = false means: do not cross the dateline in queries
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(re, plateCarree, false);
        assertNotNull(handler);

        // With no wrapping, expect a single world query envelope
        List<ReferencedEnvelope> envs = handler.getQueryEnvelopes();
        assertEquals(1, envs.size());
        assertTrue(JTS.equals(new Envelope(-180, 180, -90, 90), envs.get(0), 1e-6));
    }

    @Test
    public void testDatelineCrossing_WithWrap() throws Exception {
        CoordinateReferenceSystem plateCarree = CRS.decode("EPSG:4326", true);

        // An envelope that crosses the antimeridian: [170, 190] == [170, 180] U [-180, -170]
        ReferencedEnvelope crossing = new ReferencedEnvelope(170, 190, -10, 10, plateCarree);

        // Ask for wrapping (maxWraps = 1 is enough here)
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(crossing, plateCarree, true);
        assertNotNull(handler);

        List<ReferencedEnvelope> envelopes = handler.getQueryEnvelopes();

        // We expect two query envelopes when wrapping is on
        assertEquals(2, envelopes.size());

        // The two parts should be near [-180,-170]x[-10,10] and [170,180]x[-10,10]
        ReferencedEnvelope e0 = envelopes.get(0);
        ReferencedEnvelope e1 = envelopes.get(1);

        // Sort so assertions don’t depend on order
        ReferencedEnvelope west = e0.getMinimum(0) < e1.getMinimum(0) ? e0 : e1;
        ReferencedEnvelope east = west == e0 ? e1 : e0;

        assertTrue(JTS.equals(new Envelope(-180, -170, -10, 10), west, 1e-6));
        assertTrue(JTS.equals(new Envelope(170, 180, -10, 10), east, 1e-6));
    }
}
