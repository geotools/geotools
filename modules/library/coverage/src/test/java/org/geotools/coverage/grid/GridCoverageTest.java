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

import static org.junit.Assert.*;

import java.awt.Rectangle;
import java.io.IOException;
import java.net.InetAddress;
import javax.imageio.ImageReadParam;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.*;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

/**
 * Tests the {@link GridCoverage2D} implementation.
 *
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class GridCoverageTest extends GridCoverageTestBase {

    /** Used to avoid errors if building on a system where hostname is not defined */
    private boolean hostnameDefined;

    @Before
    public void setup() {
        try {
            InetAddress.getLocalHost();
            hostnameDefined = true;
        } catch (Exception ex) {
            hostnameDefined = false;
        }
    }

    /** Tests a grid coverage filled with random values. */
    @Test
    public void testRandomCoverage() {
        final CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        final GridCoverage2D coverage = getRandomCoverage(crs);
        assertRasterEquals(coverage, coverage); // Actually a test of assertEqualRasters(...).
        assertSame(
                coverage.getRenderedImage(),
                coverage.getRenderableImage(0, 1).createDefaultRendering());
    }

    /**
     * Tests the serialization of a grid coverage.
     *
     * @throws IOException if an I/O operation was needed and failed.
     * @throws ClassNotFoundException Should never happen.
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        if (hostnameDefined) {
            GridCoverage2D coverage = EXAMPLES.get(0);
            GridCoverage2D serial = serialize(coverage);
            assertNotSame(coverage, serial);
            assertEquals(GridCoverage2D.class, serial.getClass());
            assertRasterEquals(coverage, serial);
        }
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
        GeneralEnvelope requestedEnvelope = coverage.gridGeometry.envelope;
        // requestedEnvelope.setEnvelope(-517.2704081632651, -353.2270408163266, 697.7295918367349,
        // 350.3571428571428);
        reader.setReadParams("geotools_coverage", null, readP, requestedEnvelope, requestedDim);
        assertNotNull(readP);
        // 1 means no subsampling and no pixel skipping
        assertTrue(readP.getSourceXSubsampling() == 1);
        assertTrue(readP.getSourceYSubsampling() == 1);

        // MOCK ZOOMOUT TO FORCE SUBSAMPLING
        GeneralEnvelope requestedEnvelopeZoomOut = coverage.gridGeometry.envelope.clone();
        // select much larger geographical area for same screen size
        requestedEnvelopeZoomOut.setEnvelope(
                -517.2704081632651, -353.2270408163266, 697.7295918367349, 350.3571428571428);
        reader.setReadParams(
                "geotools_coverage", null, readP, requestedEnvelopeZoomOut, requestedDim);
        assertNotNull(readP);
        // above 1 means do sub-sampling
        assertTrue(readP.getSourceXSubsampling() > 1);
        assertTrue(readP.getSourceYSubsampling() > 1);
    }
}
