/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javax.media.jai.ROI;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.image.jai.Registry;
import org.geotools.test.TestData;
import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;
import org.opengis.coverage.grid.GridCoverageReader;

public class CropCoverageTest {

    private static GridCoverage2D gray;
    private static GridCoverage2D current;

    @BeforeClass
    public static void readCoverages() throws Exception {
        // read a simple 1 band gray image, no ROI, no NODATA
        // Disable medialib
        System.setProperty("com.sun.media.jai.disableMediaLib", "true");
        // Disable bandmerge and mosaic native operation
        Registry.setNativeAccelerationAllowed("BandMerge", false);
        Registry.setNativeAccelerationAllowed("Mosaic", false);
        // First file selection
        gray = readCoverage(TestData.file(CropCoverageTest.class, "sample.tif"));
        current = readCoverage(TestData.file(CropCoverageTest.class, "current.tif"));
    }

    private static GridCoverage2D readCoverage(File input) throws IOException {
        AbstractGridFormat format = GridFormatFinder.findFormat(input);
        // Get a reader for the selected format
        GridCoverageReader reader = format.getReader(input);
        try {
            // Read the simple gray image
            return (GridCoverage2D) reader.read(null);
            // Reader disposal
        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testROI() throws Exception {
        CropCoverage cc = new CropCoverage();
        // a small triangle
        Geometry roi =
                new WKTReader().read("POLYGON((8.91 47.10, 8.92 47.10, 8.92 47.11, 8.91 47.10))");
        GridCoverage2D result = cc.execute(gray, roi, null);
        RenderedImage ri = result.getRenderedImage();
        assertThat(ri.getProperty("ROI"), instanceOf(ROI.class));
    }

    @Test
    public void testNoData() throws Exception {
        CropCoverage cc = new CropCoverage();
        // a small triangle
        Geometry roi = new WKTReader().read("POLYGON((0 0, 16 0, 16 16, 0 0))");
        GridCoverage2D result = cc.execute(current, roi, null);
        RenderedImage ri = result.getRenderedImage();
        // there is NODATA, no need for a ROI too
        assertEquals(Image.UndefinedProperty, ri.getProperty("ROI"));
    }
}
