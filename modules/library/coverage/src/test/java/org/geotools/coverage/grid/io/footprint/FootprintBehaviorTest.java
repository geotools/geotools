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
package org.geotools.coverage.grid.io.footprint;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.media.jai.ROI;
import org.geotools.image.ImageWorker;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class FootprintBehaviorTest {

    @Test
    public void testGetValuesAsString() {
        String[] values = FootprintBehavior.valuesAsStrings();
        assertNotNull(values);
        Set<String> testSet = new HashSet<>(Arrays.asList(values));
        Set<String> expectedSet =
                Arrays.stream(FootprintBehavior.values())
                        .map(v -> v.name())
                        .collect(Collectors.toSet());
        assertEquals(expectedSet, testSet);
    }

    @Test
    public void testPostProcessNone() {
        BufferedImage bi = new BufferedImage(10, 10, BufferedImage.TYPE_BYTE_GRAY);
        RenderedImage postProcessed = FootprintBehavior.None.postProcessBlankResponse(bi, null);
        assertSame(bi, postProcessed);
    }

    @Test
    public void testPostProcessCut() {
        BufferedImage bi = new BufferedImage(10, 10, BufferedImage.TYPE_BYTE_GRAY);
        RenderedImage postProcessed = FootprintBehavior.Cut.postProcessBlankResponse(bi, null);
        assertNotSame(bi, postProcessed);
        checkEmptyROI(postProcessed);
    }

    public void checkEmptyROI(RenderedImage postProcessed) {
        Object roiCandiate = postProcessed.getProperty("roi");
        assertThat(roiCandiate, CoreMatchers.instanceOf(ROI.class));
        ROI roi = (ROI) roiCandiate;
        // roi is as big as the image, but all empty
        assertEquals(new Rectangle(0, 0, 10, 10), roi.getBounds());
        assertArrayEquals(new double[] {0}, new ImageWorker(roi.getAsImage()).getMaximums(), 0d);
    }

    @Test
    public void testPostProcessTransparent() {
        BufferedImage bi = new BufferedImage(10, 10, BufferedImage.TYPE_BYTE_GRAY);
        RenderedImage postProcessed =
                FootprintBehavior.Transparent.postProcessBlankResponse(bi, null);
        assertNotSame(bi, postProcessed);
        checkEmptyROI(postProcessed);
        // has also been expanded and alpha channel added
        assertEquals(2, postProcessed.getColorModel().getNumComponents());
        // but alpha is all null
        assertArrayEquals(new double[] {0, 0}, new ImageWorker(postProcessed).getMaximums(), 0d);
    }
}
