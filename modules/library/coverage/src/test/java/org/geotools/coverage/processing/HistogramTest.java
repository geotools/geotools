/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2012, Open Source Geospatial Foundation (OSGeo)
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

import javax.media.jai.Histogram;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.ViewType;
import org.junit.Before;
import org.junit.Test;
import org.opengis.parameter.ParameterValueGroup;

public class HistogramTest extends GridProcessingTestBase {
    /**
     * The grid coverage to test.
     */
    private GridCoverage2D coverage;
    
    /**
     * Set up common objects used for all tests.
     */
    @Before
    public void setUp() {
        coverage = EXAMPLES.get(0);
    }

    @Test
    public void testHistogram() throws Exception {
        GridCoverage2D source = coverage.view(ViewType.NATIVE);
        CoverageProcessor processor = CoverageProcessor.getInstance();

        ParameterValueGroup param = processor.getOperation("Histogram").getParameters();
        param.parameter("Source").setValue(source);

        GridCoverage2D processed = (GridCoverage2D) processor.doOperation(param);

        Histogram histo = (Histogram) processed.getProperty("histogram");
        assertNotNull(histo);

        assertEquals(256, histo.getNumBins()[0]);
    }

    @Test
    public void testHistogramWithNumBins() throws Exception {
        GridCoverage2D source = coverage.view(ViewType.NATIVE);
        CoverageProcessor processor = CoverageProcessor.getInstance();

        ParameterValueGroup param = processor.getOperation("Histogram").getParameters();
        param.parameter("Source").setValue(source);
        param.parameter("numBins").setValue(new int[]{10});
        
        GridCoverage2D processed = (GridCoverage2D) processor.doOperation(param);

        Histogram histo = (Histogram) processed.getProperty("histogram");
        assertNotNull(histo);

        assertEquals(10, histo.getNumBins()[0]);
    }

    @Test
    public void testHistogramWithHighLow() throws Exception {
        GridCoverage2D source = coverage.view(ViewType.NATIVE);
        CoverageProcessor processor = CoverageProcessor.getInstance();

        ParameterValueGroup param = processor.getOperation("Histogram").getParameters();
        param.parameter("Source").setValue(source);
        param.parameter("numBins").setValue(new int[]{10});
        param.parameter("lowValue").setValue(new double[]{100});
        param.parameter("highValue").setValue(new double[]{200});
        GridCoverage2D processed = (GridCoverage2D) processor.doOperation(param);

        Histogram histo = (Histogram) processed.getProperty("histogram");
        assertNotNull(histo);

        assertEquals(10, histo.getNumBins()[0]);
        assertEquals(100d, histo.getBinLowValue(0, 0), 0d);
        assertEquals(200d, histo.getBinLowValue(0, 10), 0d);
    }
}
