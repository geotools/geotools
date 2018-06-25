/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.*;

import it.geosolutions.jaiext.JAIExt;
import it.geosolutions.jaiext.shadedrelief.ShadedReliefAlgorithm;
import java.awt.image.RenderedImage;
import javax.media.jai.PlanarImage;
import org.geotools.coverage.grid.GridCoverage2D;
import org.junit.*;
import org.opengis.coverage.processing.OperationNotFoundException;
import org.opengis.parameter.InvalidParameterValueException;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;

/**
 * Tests the ShadedRelief operation.
 *
 * @author Emanuele Tajariol (GeoSolutions)
 */
public class ShadedReliefTest extends GridProcessingTestBase {

    /** The processor to be used for all tests. */
    private CoverageProcessor processor;

    GridCoverage2D indexedCoverage;

    public ShadedReliefTest() {
        JAIExt.initJAIEXT();
    }

    /** Set up common objects used for all tests. */
    @Before
    public void setUp() {
        processor = CoverageProcessor.getInstance(null);
        indexedCoverage = EXAMPLES.get(2);
    }

    /** Tests the "ZEVENBERGEN_THORNE" algo */
    @Test
    public void testZT() {

        ParameterValueGroup params = createDefaultParams(indexedCoverage);
        params.parameter("algorithm").setValue(ShadedReliefAlgorithm.ZEVENBERGEN_THORNE);

        applyRelief(indexedCoverage, params);
    }

    /** Tests the "COMBINED" algo */
    @Test
    public void testCombined() {
        ParameterValueGroup params = createDefaultParams(indexedCoverage);
        params.parameter("algorithm").setValue(ShadedReliefAlgorithm.COMBINED);

        applyRelief(indexedCoverage, params);
    }

    /**
     * Applies a scale on the photographic view of the given coverage.
     *
     * @param coverage The coverage to scale.
     * @param interp The interpolation to use.
     */
    private void applyRelief(final GridCoverage2D coverage, final ParameterValueGroup params) {

        assertNotNull(coverage);

        // Caching initial properties.
        final RenderedImage originalImage = coverage.getRenderedImage();
        final int w = originalImage.getWidth();
        final int h = originalImage.getHeight();

        // Doing a first scale.
        GridCoverage2D shaded = (GridCoverage2D) processor.doOperation(params);
        assertEnvelopeEquals(coverage, shaded);
        RenderedImage shadedRI = shaded.getRenderedImage();

        assertEquals(w, shadedRI.getWidth());
        assertEquals(h, shadedRI.getHeight());

        // Force computation
        assertNotNull(PlanarImage.wrapRenderedImage(coverage.getRenderedImage()).getTiles());
        assertNotNull(PlanarImage.wrapRenderedImage(shadedRI).getTiles());
    }

    private ParameterValueGroup createDefaultParams(final GridCoverage2D coverage)
            throws InvalidParameterValueException, OperationNotFoundException,
                    ParameterNotFoundException {
        // Getting parameters for doing a scale.
        final ParameterValueGroup params = processor.getOperation("ShadedRelief").getParameters();
        params.parameter("Source").setValue(coverage);
        params.parameter("roi").setValue(null);
        params.parameter("srcNoData").setValue(null);
        params.parameter("dstNoData").setValue(Double.valueOf(0.0f));
        params.parameter("resX").setValue(0.5d);
        params.parameter("resY").setValue(0.5d);
        params.parameter("zetaFactor").setValue(1d);
        params.parameter("scale").setValue(10000d);
        params.parameter("altitude").setValue(100d);
        params.parameter("azimuth").setValue(60d);
        params.parameter("algorithm").setValue(ShadedReliefAlgorithm.ZEVENBERGEN_THORNE);
        return params;
    }
}
