/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import javax.media.jai.Interpolation;
import javax.media.jai.PlanarImage;

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
 * @source $URL$
 * @version $Id$
 * @author Simone Giannecchini (GeoSolutions)
 *
 * @since 12.0
 */
public class AffineTest extends GridProcessingTestBase {
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
    public void testAffine() {
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
        affine(originallyIndexedCoverage      .view(PACKED), interp);
        affine(indexedCoverage                .view(PACKED), interp);
        affine(indexedCoverageWithTransparency.view(PACKED), interp);

        ///////////////////////////////////////////////////////////////////////
        //
        // Nearest neighbor interpolation and geophysics view.
        //
        ///////////////////////////////////////////////////////////////////////
        affine(originallyIndexedCoverage      .view(GEOPHYSICS), interp);
        affine(indexedCoverage                .view(GEOPHYSICS), interp);
        affine(indexedCoverageWithTransparency.view(GEOPHYSICS), interp);

        ///////////////////////////////////////////////////////////////////////
        //
        // Bilinear interpolation and non-geo view
        //
        ///////////////////////////////////////////////////////////////////////
        interp = Interpolation.getInstance(Interpolation.INTERP_BILINEAR);
        affine(indexedCoverage                .view(PACKED), interp);
        affine(indexedCoverageWithTransparency.view(PACKED), interp);

        ///////////////////////////////////////////////////////////////////////
        //
        // Bilinear interpolation and geo view
        //
        ///////////////////////////////////////////////////////////////////////
        affine(indexedCoverage                .view(GEOPHYSICS), interp);
        affine(indexedCoverageWithTransparency.view(GEOPHYSICS), interp);

        ///////////////////////////////////////////////////////////////////////
        //
        // Bilinear interpolation and non-geo view for a float coverage
        //
        ///////////////////////////////////////////////////////////////////////
        // on this one the subsample average should NOT go back to the
        // geophysiscs view before being applied

        ///////////////////////////////////////////////////////////////////////
        //
        // Nearest neighbor  interpolation and non-geo view for a float coverage
        //
        ///////////////////////////////////////////////////////////////////////
        // on this one the subsample average should NOT go back to the
        // geophysiscs view before being applied
        interp = Interpolation.getInstance(Interpolation.INTERP_NEAREST);
        affine(floatCoverage.view(PACKED), interp);

        // Play with a rotated coverage
        affine(rotate(floatCoverage.view(GEOPHYSICS), Math.PI/4), null);
    }

    /**
     * Applies an affine operation on the photographic view of the given coverage.
     *
     * @param coverage The coverage to transfor.
     * @param interp The interpolation to use.
     */
    private void affine(final GridCoverage2D coverage, final Interpolation interp) {
        // Caching initial properties.
        final RenderedImage originalImage = coverage.getRenderedImage();
        final int w = originalImage.getWidth();
        final int h = originalImage.getHeight();

        // Getting parameters for doing a scale.
        final ParameterValueGroup param = processor.getOperation("Affine").getParameters();
        param.parameter("Source").setValue(coverage);
        param.parameter("transform").setValue(new AffineTransform(0.5,0.0,0.0,0.5,0.0,0.0));
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
            assertNotNull(PlanarImage.wrapRenderedImage(coverage.getRenderedImage()).getTiles());
            assertNotNull(PlanarImage.wrapRenderedImage(scaledImage).getTiles());
        }

        // Doing another scale using the default processor.
        scaled = (GridCoverage2D) Operations.DEFAULT.affine(scaled, AffineTransform.getScaleInstance(3, 3), interp,null);
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
