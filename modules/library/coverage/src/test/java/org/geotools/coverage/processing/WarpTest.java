/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012-2015, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;

import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.WarpAffine;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.Viewer;
import org.geotools.coverage.processing.operation.Warp;
import org.geotools.factory.Hints;
import it.geosolutions.jaiext.utilities.ImageLayout2;
import org.junit.Before;
import org.junit.Test;
import org.opengis.parameter.ParameterValueGroup;


/**
 * Tests the {@link Warp} operation.
 *
 * @source $URL$
 * @version $Id$
 * @author Simone Giannecchini (GeoSolutions)
 *
 * @since 9.0
 */
public class WarpTest extends GridProcessingTestBase {
    /**
     * The processor to be used for all tests.
     */
    private CoverageProcessor processor;

    /**
     * Set up common objects used for all tests.
     */
    @Before
    public void setUp() {
        processor = CoverageProcessor.getInstance(null);
    }

    /**
     * Tests the "Scale" operation.
     *
     * @todo Disabled for now because seems to be trapped in a never ending loop.
     */
    @Test
    public void testWarp() {
        final GridCoverage2D originallyIndexedCoverage       = EXAMPLES.get(0);
        final GridCoverage2D indexedCoverage                 = EXAMPLES.get(2);
        final GridCoverage2D indexedCoverageWithTransparency = EXAMPLES.get(3);
        final GridCoverage2D floatCoverage                   = EXAMPLES.get(4);

        ///////////////////////////////////////////////////////////////////////
        //
        // Nearest neighbor interpolation
        //
        ///////////////////////////////////////////////////////////////////////
        Interpolation interp = Interpolation.getInstance(Interpolation.INTERP_NEAREST);
        warp(originallyIndexedCoverage      , interp);
        warp(indexedCoverage                , interp);
        warp(indexedCoverageWithTransparency, interp);


        ///////////////////////////////////////////////////////////////////////
        //
        // Bilinear interpolation 
        //
        ///////////////////////////////////////////////////////////////////////
        interp = Interpolation.getInstance(Interpolation.INTERP_BILINEAR);
        warp(originallyIndexedCoverage      , interp);
        warp(indexedCoverage                , interp);

        ///////////////////////////////////////////////////////////////////////
        //
        // Bilinear interpolation  for a float coverage
        //
        ///////////////////////////////////////////////////////////////////////
        warp(floatCoverage, interp);

        ///////////////////////////////////////////////////////////////////////
        //
        // Nearest neighbor  interpolation  for a float coverage
        //
        ///////////////////////////////////////////////////////////////////////
        interp = Interpolation.getInstance(Interpolation.INTERP_NEAREST);
        warp(floatCoverage, interp);

        // Play with a rotated coverage
        warp(rotate(floatCoverage, Math.PI/4), null);
    }

    /**
     * Applies a scale on the photographic view of the given coverage.
     *
     * @param coverage The coverage to scale.
     * @param interp The interpolation to use.
     */
    private void warp(final GridCoverage2D coverage, final Interpolation interp) {
        // Caching initial properties.
        final RenderedImage originalImage = coverage.getRenderedImage();
        final int w = originalImage.getWidth();
        final int h = originalImage.getHeight();

        // Getting parameters for doing a scale.
        final ParameterValueGroup param = processor.getOperation("Warp").getParameters();
        param.parameter("Source").setValue(coverage);
        param.parameter("warp").setValue(new WarpAffine(AffineTransform.getScaleInstance(2, 2)));
        param.parameter("Interpolation").setValue(interp);

        // Doing a first scale.
        final ImageLayout2 layout= new ImageLayout2(0,0,(int)(w / 2.0),(int)(h / 2.0));
        GridCoverage2D scaled = (GridCoverage2D) processor.doOperation(param, new Hints(JAI.KEY_IMAGE_LAYOUT,layout));
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

//        // Doing another scale using the default processor.
//        scaled = (GridCoverage2D) Operations.DEFAULT.scale(scaled, 3, 3, 0, 0, interp);
//        scaledImage = scaled.getRenderedImage();
//        assertEnvelopeEquals(coverage, scaled);
//        assertEquals(w * 1.5, scaledImage.getWidth(),  EPS);
//        assertEquals(h * 1.5, scaledImage.getHeight(), EPS);
//        if (SHOW) {
//            Viewer.show(scaled);
//        } else {
//            // Force computation
//            assertNotNull(scaledImage.getData());
//        }
    }
}
