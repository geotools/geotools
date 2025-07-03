/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageReadParam;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.GeneralBounds;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;

/**
 * Tests the {@link GridCoverage2D} implementation.
 *
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class GridCoverageTest extends GridCoverageTestBase {

    /** Tests a grid coverage filled with random values. */
    @Test
    public void testRandomCoverage() {
        final CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        final GridCoverage2D coverage = getRandomCoverage(crs);
        assertRasterEquals(coverage, coverage); // Actually a test of assertEqualRasters(...).
        assertSame(
                coverage.getRenderedImage(), coverage.getRenderableImage(0, 1).createDefaultRendering());
    }

    @Test
    public void testSubSampling() throws IllegalArgumentException, IOException, TransformException {

        GridCoverage2D coverage = EXAMPLES.get(2);

        MockAbstractGridCoverage2DReader reader = new MockAbstractGridCoverage2DReader();

        reader.setOetOriginalGridRange(new GridEnvelope2D(new Rectangle(700, 400)));
        reader.setHighestResolution(0.5, 0.5);
        reader.setCRS(coverage.getCoordinateReferenceSystem());
        ImageReadParam readP = new ImageReadParam();
        Rectangle requestedDim = new Rectangle(700, 400);

        // TEST WHEN NO SUBSAMPLING IS REQUIRED
        GeneralBounds requestedEnvelope = coverage.gridGeometry.envelope;
        // requestedEnvelope.setEnvelope(-517.2704081632651, -353.2270408163266, 697.7295918367349,
        // 350.3571428571428);
        reader.setReadParams("geotools_coverage", null, readP, requestedEnvelope, requestedDim);
        assertNotNull(readP);
        // 1 means no subsampling and no pixel skipping
        assertEquals(1, readP.getSourceXSubsampling());
        assertEquals(1, readP.getSourceYSubsampling());

        // MOCK ZOOMOUT TO FORCE SUBSAMPLING
        GeneralBounds requestedEnvelopeZoomOut = coverage.gridGeometry.envelope.clone();
        // select much larger geographical area for same screen size
        requestedEnvelopeZoomOut.setEnvelope(
                -517.2704081632651, -353.2270408163266, 697.7295918367349, 350.3571428571428);
        reader.setReadParams("geotools_coverage", null, readP, requestedEnvelopeZoomOut, requestedDim);
        assertNotNull(readP);
        // above 1 means do sub-sampling
        assertTrue(readP.getSourceXSubsampling() > 1);
        assertTrue(readP.getSourceYSubsampling() > 1);
    }
}
