/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io;

import static org.junit.Assert.*;

import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class ReadResolutionCalculatorTest {

    private static final double NATIVE_RES = 0.02;

    @Test
    public void testReadResolutionCalculator() throws Exception {
        // &BBOX=-10000000,10000000,0,20000000&WIDTH=256&HEIGHT=256
        final CoordinateReferenceSystem requestCRS = CRS.decode("EPSG:3857", true);
        final CoordinateReferenceSystem nativeCRS = CRS.decode("EPSG:4326", true);
        final ReferencedEnvelope requestBounds = new ReferencedEnvelope(-10000000, -5000000, 10000000, 15000000, requestCRS);
        GridGeometry2D gg = new GridGeometry2D(new GridEnvelope2D(0, 0, 256, 256),
                requestBounds);
        ReadResolutionCalculator calculator = new ReadResolutionCalculator(gg, nativeCRS,
                new double[] { NATIVE_RES, NATIVE_RES });
        ReferencedEnvelope readBounds = requestBounds.transform(nativeCRS, true);
        calculator.setAccurateResolution(true);
        double[] requestedResolution = calculator.computeRequestedResolution(readBounds);
        // due to high stretch this far up north, should be using almost the native resolution
        assertEquals(0.0331, requestedResolution[0], 1e-4);
        assertEquals(0.0331, requestedResolution[1], 1e-4);
    }
}
