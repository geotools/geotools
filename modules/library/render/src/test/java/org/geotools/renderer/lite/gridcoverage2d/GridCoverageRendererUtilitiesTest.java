/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite.gridcoverage2d;

import static org.geotools.referencing.crs.DefaultEngineeringCRS.GENERIC_2D;
import static org.junit.Assert.assertEquals;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import javax.media.jai.RasterFactory;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.util.ComponentColorModelJAI;
import org.geotools.util.factory.Hints;
import org.junit.Test;

public class GridCoverageRendererUtilitiesTest {

    /** Creates a simple 500x500 {@link RenderedImage} for testing purposes. */
    public static RenderedImage getSynthetic() {
        final int width = 500;
        final int height = 500;
        final WritableRaster raster =
                RasterFactory.createBandedRaster(DataBuffer.TYPE_DOUBLE, width, height, 1, null);
        final Random random = new Random();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                raster.setSample(x, y, 0, (x + y));
            }
        }
        final ColorModel cm =
                new ComponentColorModelJAI(
                        ColorSpace.getInstance(ColorSpace.CS_GRAY),
                        false,
                        false,
                        Transparency.OPAQUE,
                        DataBuffer.TYPE_DOUBLE);
        final BufferedImage image = new BufferedImage(cm, raster, false, null);
        return image;
    }

    @Test
    public void testAlignFirstCoverage() throws Exception {
        GridCoverageFactory factory = new GridCoverageFactory();
        GridCoverage2D c1 =
                factory.create(
                        "cov1",
                        getSynthetic(),
                        new ReferencedEnvelope(-123.123, 123.123, -123.123, 123.123, GENERIC_2D));
        GridCoverage2D c2 =
                factory.create(
                        "cov2", getSynthetic(), new ReferencedEnvelope(0, 50, 0, 50, GENERIC_2D));

        GridCoverage2D mosaic =
                GridCoverageRendererUtilities.mosaic(
                        Arrays.asList(c1, c2),
                        Collections.emptyList(),
                        new GeneralEnvelope(new ReferencedEnvelope(10, 80, 10, 80, GENERIC_2D)),
                        new Hints(),
                        null);
        assertEquals(
                c1.getGridGeometry().getGridToCRS2D(), mosaic.getGridGeometry().getGridToCRS2D());
    }
}
