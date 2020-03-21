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

import static org.junit.Assert.assertEquals;

import it.geosolutions.jaiext.JAIExt;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.media.jai.operator.ExtremaDescriptor;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.image.ImageWorker;
import org.junit.Before;
import org.junit.Test;
import org.opengis.parameter.ParameterValueGroup;

/**
 * Tests some binary operations like add and multiply.
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 * @since 2.7
 */
public class BinaryOpTest extends GridProcessingTestBase {

    private static final double DELTA = 1E-6d;
    /** The processor to be used for all tests. */
    private CoverageProcessor processor;

    /** Set up common objects used for all tests. */
    @Before
    public void setUp() {
        processor = CoverageProcessor.getInstance(null);
    }

    /** Tests the "Add" operation. */
    @Test
    public void testAdd() throws IOException {
        final GridCoverage2D shortCoverage = EXAMPLES.get(5);
        final GridCoverage2D floatCoverage = EXAMPLES.get(4);
        final GridCoverage2D result = doOp("Add", shortCoverage, floatCoverage);
        final RenderedImage image = result.getRenderedImage();
        ImageWorker w = new ImageWorker(image);
        double[] min = (double[]) w.getMinimums();
        double[] max = (double[]) w.getMaximums();
        assertEquals(min[0], 1.0, DELTA);
        assertEquals(max[0], 66401.0, DELTA);
    }

    /** Tests the "Multiply" operation. */
    @Test
    public void testMultiply() throws IOException {
        final GridCoverage2D shortCoverage = EXAMPLES.get(5);
        final GridCoverage2D floatCoverage = EXAMPLES.get(4);
        final GridCoverage2D result = doOp("Multiply", shortCoverage, floatCoverage);
        final RenderedImage image = result.getRenderedImage();
        final RenderedImage extrema = ExtremaDescriptor.create(image, null, 1, 1, false, 1, null);
        double[][] minMax = (double[][]) extrema.getProperty("Extrema");
        assertEquals(minMax[0][0], 0.0, DELTA);
        assertEquals(minMax[1][0], 6.5272192E7, DELTA);
    }

    /**
     * Applies the specified operation to the given coverages.
     *
     * @param coverage0 The coverage to scale.
     * @param interp The interpolation to use.
     */
    private GridCoverage2D doOp(
            final String operationName,
            final GridCoverage2D coverage0,
            final GridCoverage2D coverage1)
            throws IOException {

        // Getting parameters for doing a scale.
        final ParameterValueGroup param = processor.getOperation(operationName).getParameters();
        if (JAIExt.isJAIExtOperation(JAIExt.getOperationName(operationName))) {
            List<GridCoverage2D> sources = new ArrayList<>();
            sources.add(coverage0);
            sources.add(coverage1);
            param.parameter("Sources").setValue(sources);
        } else {
            param.parameter("Source0").setValue(coverage0);
            param.parameter("Source1").setValue(coverage1);
        }

        // Doing a first scale.
        GridCoverage2D result = (GridCoverage2D) processor.doOperation(param);
        assertEnvelopeEquals(coverage0, result);
        return result;
    }
}
