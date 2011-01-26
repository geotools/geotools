/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.io;

import java.awt.Insets;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.IIOImage;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import javax.imageio.ImageWriteParam;
import javax.media.jai.iterator.RectIter;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests {@link GeographicImageWriter}, especially the {@link RectIter} creation.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class GeographicImageWriterTest {
    /**
     * Tests the {@link RectIter}.
     *
     * @throws IOException If an I/O operation was required and failed.
     */
    @Test
    public void testRectIter() throws IOException {
        final Insets margin = new Insets(5, 2, 2, 1); // top, left, bottom, right
        runRectIter(10, 20, margin);
        runRectIter(11, 21, margin);
        runRectIter(10, 21, margin);
        runRectIter(11, 20, margin);
    }

    /**
     * Creates a dummy image and tests immediately the iteration.
     */
    private static IIOImage runRectIter(final int width, final int height, final Insets m) {
        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_GRAY);
        final WritableRaster raster = image.getRaster();
        int value = 0;
        for (int y=image.getHeight(); --y>=0;) {
            for (int x=image.getWidth(); --x>=0;) {
                raster.setSample(x, y, 0, value++);
            }
        }
        final int count = value;
        final IIOImage iiom = new IIOImage(image, null, null);
        assertEquals(count, GeographicImageWriter.computeSize(iiom, null).getNumSampleValues());
        /*
         * Using JAI iterator on the whole image.
         */
        RectIter iterator = GeographicImageWriter.createRectIter(iiom, null);
        assertFalse(iterator instanceof SubsampledRectIter);
        checkSampleSuite(iterator, --value, -1, 0);
        /*
         * Using JAI iterator with subregion and subsampling.
         */
        final ImageWriteParam param = new ImageWriteParam(null);
        final Rectangle bounds = new Rectangle(m.left, m.top, width-(m.left+m.right), height-(m.top+m.bottom));
        param.setSourceRegion(bounds);
        value -= (m.top*width + m.left);
        for (int sourceYSubsampling=1; sourceYSubsampling<=6; sourceYSubsampling++) {
            for (int sourceXSubsampling=1; sourceXSubsampling<=6; sourceXSubsampling++) {
                param.setSourceSubsampling(sourceXSubsampling, sourceYSubsampling, 0, 0);
                iterator = GeographicImageWriter.createRectIter(iiom, param);

                // Number of points on which to iterate in a row.
                int n = (bounds.width - 1) / sourceXSubsampling + 1;

                // The range on which the iteration will take place.
                n *= sourceXSubsampling;

                // Spaces skipped on the left and right sides.
                n = width - n;

                // Adds the range occuped by the skipped lines.
                n += width * (sourceYSubsampling - 1);
                checkSampleSuite(iterator, value, -sourceXSubsampling, -n);
            }
        }
        return iiom;
    }

    /**
     * Tests the values returned by the iterator.
     */
    private static void checkSampleSuite(final RectIter iterator, int value, final int dx, final int dy) {
        if (!iterator.finishedBands()) do {
            if (!iterator.finishedLines()) do {
                if (!iterator.finishedPixels()) do {
                    assertEquals(value, iterator.getSample());
                    value += dx;
                } while (!iterator.nextPixelDone());
                iterator.startPixels();
                value += dy;
            } while (!iterator.nextLineDone());
            iterator.startLines();
        } while (!iterator.nextBandDone());
    }
}
