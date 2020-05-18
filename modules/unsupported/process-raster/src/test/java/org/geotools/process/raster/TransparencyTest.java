/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.image.ImageWorker;
import org.geotools.test.TestData;
import org.junit.Test;

public class TransparencyTest {

    @Test
    public void testTransparencyOp() throws IOException {

        // The test image has a wide transparent area at the top left,
        // plus some small white stripes in the region where y coordinates
        // are > 50.
        // Let's check that the wide area survives to the operation
        // and only the isolated stripes are fileld
        File file = TestData.file(this, "transparency.tif");
        GeoTiffReader reader = null;
        try {

            reader = new GeoTiffReader(file);

            GridCoverage2D coverage = null;
            coverage = reader.read(null);
            RenderedImage ri = coverage.getRenderedImage();
            ImageWorker worker = new ImageWorker(ri);
            double[] mins = worker.retainLastBand().getMinimums();

            // Ensure we have some transparent pixels.
            // Min value on the alpha band is zero
            assertEquals(0, mins[0], 1E-6);

            // Ensure the top left area is fully transparent
            worker = worker.crop(0, 0, 39, 19).retainLastBand();
            mins = worker.getMinimums();
            double maxs[] = worker.getMaximums();
            assertEquals(0, mins[0], 1E-6);
            assertEquals(0, maxs[0], 1E-6);

            // Ensure the area with y > 50 has some transparent pixels
            worker = new ImageWorker(ri).crop(0, 50, 100, 50);
            mins = worker.getMinimums();
            maxs = worker.getMaximums();
            assertEquals(0.0, mins[0], 1E-6);
            assertEquals(255, maxs[0], 1E-6);

            TransparencyFillProcess process = new TransparencyFillProcess();
            coverage = process.execute(coverage, null, null);

            worker =
                    new ImageWorker(coverage.getRenderedImage())
                            .crop(0, 0, 39, 19)
                            .retainLastBand();
            mins = worker.getMinimums();
            maxs = worker.getMaximums();

            // Make sure the top left area is still transparent
            assertEquals(0, mins[0], 1E-6);
            assertEquals(0, maxs[0], 1E-6);

            worker =
                    new ImageWorker(coverage.getRenderedImage())
                            .crop(0, 50, 100, 50)
                            .retainLastBand();

            // Make sure the area with white stripes is now fully opaque
            mins = worker.getMinimums();
            maxs = worker.getMaximums();

            assertEquals(255, mins[0], 1E-6);
            assertEquals(255, maxs[0], 1E-6);
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Ignore exceptions on close
                }
            }
        }
    }

    @Test
    public void testTransparencyOpFloat() throws IOException {

        // The test image has a wide transparent area at the top left,
        // plus some small white stripes in the region where y coordinates
        // are > 50.
        // Let's check that the wide area survives to the operation
        // and only the isolated stripes are fileld
        File file = TestData.file(this, "transparencyFloat.tif");
        GeoTiffReader reader = null;
        try {

            reader = new GeoTiffReader(file);

            GridCoverage2D coverage = null;
            coverage = reader.read(null);
            RenderedImage ri = coverage.getRenderedImage();
            ImageWorker worker = new ImageWorker(ri);
            double noData = (double) worker.getNoData().getMin();
            // Ensure the top left area is fully transparent
            worker = worker.crop(0, 0, 39, 19).retainLastBand();
            // assertEquals(noData, worker.getMean()[0], 1E-6);
            Raster raster = ri.getData();
            for (int i = 0; i < 26; i++) {
                assertEquals(raster.getPixel(i, 77, new int[1])[0], noData, 1E-6);
                assertEquals(raster.getPixel(i, 76, new int[1])[0], 255, 1E-6);
                assertEquals(raster.getPixel(i, 78, new int[1])[0], 255, 1E-6);
            }

            TransparencyFillProcess process = new TransparencyFillProcess();
            coverage = process.execute(coverage, null, null);

            worker =
                    new ImageWorker(coverage.getRenderedImage())
                            .crop(0, 0, 39, 19)
                            .retainLastBand();

            // Make sure the top left area is still transparent
            assertEquals(noData, worker.getMean()[0], 1E-6);

            // check the stripe is now filled
            raster = coverage.getRenderedImage().getData();
            for (int i = 0; i < 26; i++) {
                assertEquals(raster.getPixel(i, 77, new int[1])[0], 255, 1E-6);
            }

        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Ignore exceptions on close
                }
            }
        }
    }

    @Test
    public void testTransparencyOpWidth() throws IOException {

        // The test image has a wide transparent area at the top left,
        // plus some small white stripes in the region where y coordinates
        // are > 50.
        // Let's check that the wide area survives to the operation
        // and only the isolated stripes are fileld
        File file = TestData.file(this, "transparencyWidth.tif");
        GeoTiffReader reader = null;
        try {

            reader = new GeoTiffReader(file);

            GridCoverage2D coverage = null;
            coverage = reader.read(null);
            RenderedImage ri = coverage.getRenderedImage();
            ImageWorker worker = new ImageWorker(ri);
            double noData = (double) worker.getNoData().getMin();
            // Ensure the top left area is fully transparent
            worker = worker.crop(0, 0, 39, 19).retainLastBand();

            Raster raster = ri.getData();
            // this stripes has two line to be filled
            for (int y = 58; y < 62; y++) {
                assertEquals(raster.getPixel(65, y, new int[1])[0], noData, 1E-6);
                assertEquals(raster.getPixel(66, y, new int[1])[0], noData, 1E-6);
                assertEquals(raster.getPixel(64, y, new int[1])[0], 255, 1E-6);
                assertEquals(raster.getPixel(67, y, new int[1])[0], 255, 1E-6);
            }

            TransparencyFillProcess process = new TransparencyFillProcess();

            // set a width of 2
            coverage = process.execute(coverage, 2, null);

            worker =
                    new ImageWorker(coverage.getRenderedImage())
                            .crop(0, 0, 39, 19)
                            .retainLastBand();

            // Make sure the top left area is still transparent
            assertEquals(noData, worker.getMean()[0], 1E-6);

            raster = coverage.getRenderedImage().getData();

            // check that both lines have been filled
            for (int y = 58; y < 62; y++) {
                assertEquals(raster.getPixel(65, y, new int[1])[0], 255, 1E-6);
                assertEquals(raster.getPixel(66, y, new int[1])[0], 255, 1E-6);
            }

        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Ignore exceptions on close
                }
            }
        }
    }
}
