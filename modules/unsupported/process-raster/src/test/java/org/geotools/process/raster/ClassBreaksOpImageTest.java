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
package org.geotools.process.raster;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.*;
import java.awt.image.RenderedImage;
import java.util.Arrays;
import javax.media.jai.ParameterBlockJAI;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.image.ImageWorker;
import org.geotools.process.classify.ClassificationMethod;
import org.geotools.process.raster.classify.Classification;
import org.geotools.processing.jai.ClassBreaksDescriptor;
import org.geotools.processing.jai.ClassBreaksRIF;
import org.junit.Test;

public class ClassBreaksOpImageTest {

    static final double EPS = 1e-3;

    @Test
    public void getMissingProperty() {
        GridCoverage2D coverage = CoverageClassStatsTest.createCoverage();
        RenderedImage image = coverage.getRenderedImage();

        ParameterBlockJAI pb = new ParameterBlockJAI(new ClassBreaksDescriptor());
        pb.addSource(image);
        pb.setParameter("method", ClassificationMethod.QUANTILE);
        pb.setParameter("numClasses", 5);
        // raw creation like in CoverageClassStats, otherwise the issue gets masked by JAI wrappers
        RenderedImage op = new ClassBreaksRIF().create(pb, null);

        // used to NPE here
        Object roi = op.getProperty("ROI");
        assertEquals(Image.UndefinedProperty, roi);
    }

    @Test
    public void testQuantileBreaks() throws Exception {
        GridCoverage2D coverage = CoverageClassStatsTest.createCoverage();
        RenderedImage image = coverage.getRenderedImage();

        ParameterBlockJAI pb = new ParameterBlockJAI(new ClassBreaksDescriptor());
        pb.addSource(image);
        pb.setParameter("method", ClassificationMethod.QUANTILE);
        pb.setParameter("numClasses", 4);
        // raw creation like in CoverageClassStats, otherwise the issue gets masked by JAI wrappers
        RenderedImage op = new ClassBreaksRIF().create(pb, null);
        Classification classification =
                (Classification) op.getProperty(ClassBreaksDescriptor.CLASSIFICATION_PROPERTY);
        assertNotNull(classification);
        Number[] breaks = classification.getBreaks()[0];

        // 4 classes, 5 breaks
        // 1, 1, 2,
        // 3, 3, 8, 8, 9,
        // 11, 14, 16, 24,
        // 26, 26, 45, 53
        assertEquals(5, breaks.length);
        assertEquals(1, breaks[0].doubleValue(), EPS);
        assertEquals(3, breaks[1].doubleValue(), EPS);
        assertEquals(11, breaks[2].doubleValue(), EPS);
        assertEquals(26, breaks[3].doubleValue(), EPS);
        assertEquals(53, breaks[4].doubleValue(), EPS);
    }

    @Test
    public void testQuantileBreaksHistogram() throws Exception {
        GridCoverage2D coverage = CoverageClassStatsTest.createCoverage();
        RenderedImage image = coverage.getRenderedImage();

        ParameterBlockJAI pb = new ParameterBlockJAI(new ClassBreaksDescriptor());
        pb.addSource(image);
        pb.setParameter("method", ClassificationMethod.QUANTILE);
        pb.setParameter("numClasses", 4);
        pb.setParameter("extrema", getExtrema(image));
        pb.setParameter("histogram", true);
        pb.setParameter("histogramBins", 100);
        // raw creation like in CoverageClassStats, otherwise the issue gets masked by JAI wrappers
        RenderedImage op = new ClassBreaksRIF().create(pb, null);
        Classification classification =
                (Classification) op.getProperty(ClassBreaksDescriptor.CLASSIFICATION_PROPERTY);
        assertNotNull(classification);
        Number[] breaks = classification.getBreaks()[0];
        System.out.println(Arrays.asList(breaks));

        // 4 classes, 5 breaks (not the same as the exact count, slightly different approach,
        // but still correct)
        // 1, 1, 2, 3, 3,
        // 8, 8, 9,
        // 11, 14, 16, 24,
        // 26, 26, 45, 53
        assertEquals(5, breaks.length);
        assertEquals(1, breaks[0].doubleValue(), EPS);
        assertEquals(8, breaks[1].doubleValue(), EPS);
        assertEquals(11, breaks[2].doubleValue(), EPS);
        assertEquals(26, breaks[3].doubleValue(), EPS);
        assertEquals(53, breaks[4].doubleValue(), EPS);
    }

    @Test
    public void testNaturalBreaks() throws Exception {
        GridCoverage2D coverage = CoverageClassStatsTest.createCoverage();
        RenderedImage image = coverage.getRenderedImage();

        ParameterBlockJAI pb = new ParameterBlockJAI(new ClassBreaksDescriptor());
        pb.addSource(image);
        pb.setParameter("method", ClassificationMethod.NATURAL_BREAKS);
        pb.setParameter("numClasses", 4);
        // raw creation like in CoverageClassStats, otherwise the issue gets masked by JAI wrappers
        RenderedImage op = new ClassBreaksRIF().create(pb, null);
        Classification classification =
                (Classification) op.getProperty(ClassBreaksDescriptor.CLASSIFICATION_PROPERTY);
        assertNotNull(classification);
        Number[] breaks = classification.getBreaks()[0];

        // 4 classes, 5 breaks
        assertEquals(5, breaks.length);
        assertEquals(1, breaks[0].doubleValue(), EPS);
        assertEquals(3, breaks[1].doubleValue(), EPS);
        assertEquals(16, breaks[2].doubleValue(), EPS);
        assertEquals(26, breaks[3].doubleValue(), EPS);
        assertEquals(53, breaks[4].doubleValue(), EPS);
    }

    @Test
    public void testNaturalBreaksHistogram() throws Exception {
        GridCoverage2D coverage = CoverageClassStatsTest.createCoverage();
        RenderedImage image = coverage.getRenderedImage();

        ParameterBlockJAI pb = new ParameterBlockJAI(new ClassBreaksDescriptor());
        pb.addSource(image);
        pb.setParameter("method", ClassificationMethod.NATURAL_BREAKS);
        pb.setParameter("numClasses", 4);
        pb.setParameter("extrema", getExtrema(image));
        pb.setParameter("histogram", true);
        pb.setParameter("histogramBins", 100);
        // raw creation like in CoverageClassStats, otherwise the issue gets masked by JAI wrappers
        RenderedImage op = new ClassBreaksRIF().create(pb, null);
        Classification classification =
                (Classification) op.getProperty(ClassBreaksDescriptor.CLASSIFICATION_PROPERTY);
        assertNotNull(classification);
        Number[] breaks = classification.getBreaks()[0];

        // 4 classes, 5 breaks
        assertEquals(5, breaks.length);
        System.out.println(Arrays.toString(breaks));
        assertEquals(1, breaks[0].doubleValue(), EPS);
        assertEquals(3, breaks[1].doubleValue(), EPS);
        assertEquals(16, breaks[2].doubleValue(), EPS);
        assertEquals(26, breaks[3].doubleValue(), EPS);
        assertEquals(53, breaks[4].doubleValue(), EPS);
    }

    private Double[][] getExtrema(RenderedImage image) {
        ImageWorker iw = new ImageWorker(image);
        Double[][] extrema = new Double[2][1];
        extrema[0] = new Double[] {iw.getMinimums()[0]};
        extrema[1] = new Double[] {iw.getMaximums()[0]};
        return extrema;
    }
}
