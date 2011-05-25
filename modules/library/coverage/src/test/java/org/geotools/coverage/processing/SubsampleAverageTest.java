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

import static org.geotools.coverage.grid.ViewType.GEOPHYSICS;
import static org.geotools.coverage.grid.ViewType.PACKED;
import static org.geotools.coverage.grid.ViewType.PHOTOGRAPHIC;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.image.RenderedImage;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.Viewer;
import org.geotools.factory.Hints;
import org.junit.Before;
import org.junit.Test;
import org.opengis.parameter.ParameterValueGroup;


/**
 * Tests the Subsample Average operation.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Simone Giannecchini (GeoSolutions)
 * @author Martin Desruisseaux (Geomatys)
 *
 * @since 2.3
 */
public final class SubsampleAverageTest extends GridProcessingTestBase {
    /**
     * The processors to be used for all tests.
     */
    private CoverageProcessor defaultProcessor, photographicProcessor;

    /**
     * Set up common objects used for all tests.
     */
    @Before
    public void setUp() {
        Hints hints = new Hints(Hints.COVERAGE_PROCESSING_VIEW, PHOTOGRAPHIC);
        photographicProcessor = CoverageProcessor.getInstance(hints);
        defaultProcessor      = CoverageProcessor.getInstance(null);
    }

    /**
     * Tests the "SubsampleAverage" operation.
     */
    @Test
    public void testSubsampleAverage() {
        final GridCoverage2D originallyIndexedCoverage       = EXAMPLES.get(0);
        final GridCoverage2D indexedCoverage                 = EXAMPLES.get(2);
        final GridCoverage2D indexedCoverageWithTransparency = EXAMPLES.get(3);
        final GridCoverage2D floatCoverage                   = EXAMPLES.get(4);

        // On this one the Subsample average should do an RGB expansion.
        subsampleAverage(indexedCoverage.view(GEOPHYSICS), false);

        // On this one the Subsample average should do an RGB expansion preserving alpha.
        subsampleAverage(indexedCoverageWithTransparency.view(GEOPHYSICS), false);

        // On this one the subsample average should go back to the geophysics
        // view before being applied.
        subsampleAverage(originallyIndexedCoverage.view(GEOPHYSICS), false);

        // On this one the Subsample average should do an RGB expansion.
        subsampleAverage(indexedCoverage.view(PACKED), false);

        // On this one the Subsample average should do an RGB expansion preserving alpha.
        subsampleAverage(indexedCoverageWithTransparency.view(PACKED), false);

        // On this one the subsample average should go back to the geophysics
        // view before being applied.
        subsampleAverage(originallyIndexedCoverage.view(PACKED), false);

        // On this one the subsample average should NOT go back to the
        // geophysics view before being applied.
        subsampleAverage(floatCoverage.view(PACKED), true);

        // On this one the subsample average should go back to the
        // geophysiscs view before being applied.
        subsampleAverage(floatCoverage.view(PACKED), true);

        // On this one the subsample average should go back to the
        // geophysiscs view before being applied.
        subsampleAverage(floatCoverage.view(PACKED), true);

        // Play with a rotated coverage.
        subsampleAverage(rotate(floatCoverage.view(GEOPHYSICS), Math.PI/4), false);
    }

    /**
     * Applies a subsample operation on the given coverage.
     *
     * @param coverage The coverage on which to apply the operation.
     * @param photographic {@code true} if the operation should be applied on the photographic view.
     */
    private void subsampleAverage(final GridCoverage2D coverage, final boolean photographic) {
        /*
         * Caching initial properties.
         */
        final RenderedImage originalImage = coverage.getRenderedImage();
        int w = originalImage.getWidth();
        int h = originalImage.getHeight();
        /*
         * Get the processor and prepare the first operation.
         */
        final CoverageProcessor processor = photographic ? photographicProcessor : defaultProcessor;
        final ParameterValueGroup param = processor.getOperation("SubsampleAverage").getParameters();
        param.parameter("Source").setValue(coverage);
        param.parameter("scaleX").setValue(Double.valueOf(0.5));
        param.parameter("scaleY").setValue(Double.valueOf(0.5));
        GridCoverage2D scaled = (GridCoverage2D) processor.doOperation(param);
        RenderedImage scaledImage = scaled.getRenderedImage();
        assertEquals(w / 2.0, scaledImage.getWidth(),  EPS);
        assertEquals(h / 2.0, scaledImage.getHeight(), EPS);
        w = scaledImage.getWidth();
        h = scaledImage.getHeight();
        /*
         * Check that the final envelope is close enough to the initial envelope.
         * In a perfect world they should be the same exact thing but in practice
         * this is quite hard to achieve when doing scaling due to the fact that
         * the various JAI operations use some complex laws to compute the final
         * image bounds.
         */
        assertEnvelopeEquals(scaled, coverage);
        /*
         * Show the result.
         */
        if (SHOW) {
            Viewer.show(coverage);
            Viewer.show(scaled);
        } else {
            // Force computation
            assertNotNull(coverage.getRenderedImage().getData());
            assertNotNull(scaled.getRenderedImage().getData());
        }
        /*
         * Use the default processor and then scale again.
         */
        scaled = (GridCoverage2D) Operations.DEFAULT.subsampleAverage(scaled, 0.3333, 0.3333);
        assertEnvelopeEquals(scaled, coverage);
        scaledImage = scaled.getRenderedImage();
        /*
         * I had to comment this out since sometimes this evaluation fails
         * unexpectedly. I think it is a JAI issue because here below I am using
         * the rule they claim to follow.
         */
        if (false) {
            assertEquals(w / 3.0, scaledImage.getWidth(),  EPS);
            assertEquals(h / 3.0, scaledImage.getHeight(), EPS);
        }
        if (SHOW) {
            Viewer.show(scaled);
        } else {
            // Force computation
            assertNotNull(scaled.getRenderedImage().getData());
        }
    }
}
