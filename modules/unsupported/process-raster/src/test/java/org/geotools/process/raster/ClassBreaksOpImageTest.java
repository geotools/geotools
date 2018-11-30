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

import java.awt.*;
import java.awt.image.RenderedImage;
import javax.media.jai.ParameterBlockJAI;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.process.classify.ClassificationMethod;
import org.geotools.processing.jai.ClassBreaksDescriptor;
import org.geotools.processing.jai.ClassBreaksRIF;
import org.junit.Test;

public class ClassBreaksOpImageTest {

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
}
