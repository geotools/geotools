/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.processing;

import java.awt.image.RenderedImage;
import javax.media.jai.Interpolation;

import org.opengis.parameter.ParameterValueGroup;

import org.geotools.factory.Hints;
import org.geotools.coverage.grid.Viewer;
import org.geotools.coverage.grid.GridCoverage2D;
import static org.geotools.coverage.grid.ViewType.*;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the scale operation.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Simone Giannecchini (GeoSolutions)
 * @author Martin Desruisseaux (Geomatys)
 *
 * @since 2.3
 */
public class ScaleTest extends GridProcessingTestBase {
    /**
     * The processor to be used for all tests.
     */
    private CoverageProcessor processor;

    /**
     * Set up common objects used for all tests.
     */
    @Before
    public void setUp() {
        Hints hints = new Hints(Hints.COVERAGE_PROCESSING_VIEW, PHOTOGRAPHIC);
        processor = CoverageProcessor.getInstance(hints);
    }

    /**
     * Tests the "Scale" operation.
     *
     * @todo Disabled for now because seems to be trapped in a never ending loop.
     */
    @Test
    @Ignore
    public void testScale() {
        final GridCoverage2D originallyIndexedCoverage       = EXAMPLES.get(0);
        final GridCoverage2D indexedCoverage                 = EXAMPLES.get(2);
        final GridCoverage2D indexedCoverageWithTransparency = EXAMPLES.get(3);
        final GridCoverage2D floatCoverage                   = EXAMPLES.get(4);

        ///////////////////////////////////////////////////////////////////////
        //
        // Nearest neighbor interpolation and non-geophysics view.
        //
        ///////////////////////////////////////////////////////////////////////
        Interpolation interp = Interpolation.getInstance(Interpolation.INTERP_NEAREST);
        scale(originallyIndexedCoverage      .view(PACKED), interp);
        scale(indexedCoverage                .view(PACKED), interp);
        scale(indexedCoverageWithTransparency.view(PACKED), interp);

        ///////////////////////////////////////////////////////////////////////
        //
        // Nearest neighbor interpolation and geophysics view.
        //
        ///////////////////////////////////////////////////////////////////////
        scale(originallyIndexedCoverage      .view(GEOPHYSICS), interp);
        scale(indexedCoverage                .view(GEOPHYSICS), interp);
        scale(indexedCoverageWithTransparency.view(GEOPHYSICS), interp);

        ///////////////////////////////////////////////////////////////////////
        //
        // Bilinear interpolation and non-geo view
        //
        ///////////////////////////////////////////////////////////////////////
        interp = Interpolation.getInstance(Interpolation.INTERP_BILINEAR);
        scale(originallyIndexedCoverage      .view(PACKED), interp);
        scale(indexedCoverage                .view(PACKED), interp);
        scale(indexedCoverageWithTransparency.view(PACKED), interp);

        ///////////////////////////////////////////////////////////////////////
        //
        // Bilinear interpolation and geo view
        //
        ///////////////////////////////////////////////////////////////////////
        scale(originallyIndexedCoverage      .view(GEOPHYSICS), interp);
        scale(indexedCoverage                .view(GEOPHYSICS), interp);
        scale(indexedCoverageWithTransparency.view(GEOPHYSICS), interp);

        ///////////////////////////////////////////////////////////////////////
        //
        // Bilinear interpolation and non-geo view for a float coverage
        //
        ///////////////////////////////////////////////////////////////////////
        // on this one the subsample average should NOT go back to the
        // geophysiscs view before being applied
        scale(floatCoverage.view(PACKED), interp);

        ///////////////////////////////////////////////////////////////////////
        //
        // Nearest neighbor  interpolation and non-geo view for a float coverage
        //
        ///////////////////////////////////////////////////////////////////////
        // on this one the subsample average should NOT go back to the
        // geophysiscs view before being applied
        interp = Interpolation.getInstance(Interpolation.INTERP_NEAREST);
        scale(floatCoverage.view(PACKED), interp);

        // Play with a rotated coverage
        scale(rotate(floatCoverage.view(GEOPHYSICS), Math.PI/4), null);
    }

    /**
     * Applies a scale on the photographic view of the given coverage.
     *
     * @param coverage The coverage to scale.
     * @param interp The interpolation to use.
     */
    private void scale(final GridCoverage2D coverage, final Interpolation interp) {
        // Caching initial properties.
        final RenderedImage originalImage = coverage.getRenderedImage();
        final int w = originalImage.getWidth();
        final int h = originalImage.getHeight();

        // Getting parameters for doing a scale.
        final ParameterValueGroup param = processor.getOperation("Scale").getParameters();
        param.parameter("Source").setValue(coverage);
        param.parameter("xScale").setValue(Float.valueOf(0.5f));
        param.parameter("yScale").setValue(Float.valueOf(0.5f));
        param.parameter("xTrans").setValue(Float.valueOf(0.0f));
        param.parameter("yTrans").setValue(Float.valueOf(0.0f));
        param.parameter("Interpolation").setValue(interp);

        // Doing a first scale.
        GridCoverage2D scaled = (GridCoverage2D) processor.doOperation(param);
        assertEnvelopeEquals(coverage, scaled);
        RenderedImage scaledImage = scaled.getRenderedImage();
        assertEquals(w / 2.0, scaledImage.getWidth(),  EPS);
        assertEquals(h / 2.0, scaledImage.getHeight(), EPS);
        if (SHOW) {
            Viewer.show(coverage);
            Viewer.show(scaled);
        } else {
            // Force computation
            assertNotNull(coverage.getRenderedImage().getData());
            assertNotNull(scaledImage.getData());
        }

        // Doing another scale using the default processor.
        scaled = (GridCoverage2D) Operations.DEFAULT.scale(scaled, 3, 3, 0, 0, interp);
        scaledImage = scaled.getRenderedImage();
        assertEnvelopeEquals(coverage, scaled);
        assertEquals(w * 1.5, scaledImage.getWidth(),  EPS);
        assertEquals(h * 1.5, scaledImage.getHeight(), EPS);
        if (SHOW) {
            Viewer.show(scaled);
        } else {
            // Force computation
            assertNotNull(scaledImage.getData());
        }
    }
}
