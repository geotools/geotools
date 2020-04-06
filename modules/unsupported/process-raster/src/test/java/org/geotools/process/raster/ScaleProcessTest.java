/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014-2015, Open Source Geospatial Foundation (OSGeo)
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
import static org.junit.Assert.assertNull;

import it.geosolutions.jaiext.iterators.RandomIterFactory;
import it.geosolutions.jaiext.range.NoDataContainer;
import it.geosolutions.jaiext.range.Range;
import it.geosolutions.jaiext.range.RangeFactory;
import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.media.jai.InterpolationBicubic;
import javax.media.jai.InterpolationBilinear;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.ROI;
import javax.media.jai.ROIShape;
import javax.media.jai.iterator.RandomIter;
import org.geotools.TestData;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.image.ImageWorker;
import org.geotools.util.factory.GeoTools;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.coverage.grid.GridCoverageReader;

public class ScaleProcessTest {

    /** Tolerance value used for double comparison */
    private static final double TOLERANCE = 0.01;

    /** Coverage to elaborate */
    private static GridCoverage2D coverage;

    /** Nearest Interpolation */
    private static InterpolationNearest nearest;

    /** Bilinear Interpolation */
    private static InterpolationBilinear bilinear;

    /** Bicubic Interpolation */
    private static InterpolationBicubic bicubic;

    /** Scale X parameter */
    private static double scaleX;

    /** Scale Y parameter */
    private static double scaleY;

    private static GridCoverage2D coverageNoData;

    private static GridCoverage2D coverageROI;

    private static GridCoverage2D coverageNoDataROI;

    private static ROI roi;

    @BeforeClass
    public static void setup() throws FileNotFoundException, IOException {
        // Selection of the File to use
        File tiff = TestData.file(ScaleProcessTest.class, "sample.tif");
        // Reading of the file with the GeoTiff reader
        AbstractGridFormat format = GridFormatFinder.findFormat(tiff);
        // Get a reader for the selected format
        GridCoverageReader reader = format.getReader(tiff);
        // Read the input Coverage
        coverage = (GridCoverage2D) reader.read(null);
        // Coverage properties
        Map properties = coverage.getProperties();
        if (properties == null) {
            properties = new HashMap<>();
        }
        GridCoverageFactory gcf = new GridCoverageFactory(GeoTools.getDefaultHints());

        // Same coverage with NoData Property
        Map properties1 = new HashMap(properties);
        CoverageUtilities.setNoDataProperty(
                properties1, RangeFactory.create((short) -1, (short) 1));
        coverageNoData =
                gcf.create(
                        "nodata",
                        coverage.getRenderedImage(),
                        coverage.getEnvelope(),
                        coverage.getSampleDimensions(),
                        null,
                        properties1);
        // Same Coverage with ROI Property
        Map properties2 = new HashMap(properties);
        roi = new ROIShape(new Rectangle(8, 8, 2, 2));
        CoverageUtilities.setROIProperty(properties2, roi);
        coverageROI =
                gcf.create(
                        "roi",
                        coverage.getRenderedImage(),
                        coverage.getEnvelope(),
                        coverage.getSampleDimensions(),
                        null,
                        properties2);

        // Same Coverage with ROI and NoData Property
        Map properties3 = new HashMap(properties);
        CoverageUtilities.setNoDataProperty(
                properties3, RangeFactory.create((short) -1, (short) -1));
        CoverageUtilities.setROIProperty(properties3, roi);
        coverageNoDataROI =
                gcf.create(
                        "roiNoData",
                        coverage.getRenderedImage(),
                        coverage.getEnvelope(),
                        coverage.getSampleDimensions(),
                        null,
                        properties3);

        // Reader disposal
        reader.dispose();

        // Definition of the interpolation type
        nearest = new InterpolationNearest();
        bilinear = new InterpolationBilinear();
        bicubic = new InterpolationBicubic(8);

        // Definition of the transformation. (The final image should be doubled and translated)
        scaleX = 2d;
        scaleY = 2d;
    }

    @Test
    public void testNoInterp() throws IOException {
        // Selection of the Scale process
        ScaleCoverage process = new ScaleCoverage();

        // Execution of the operation
        GridCoverage2D result = process.execute(coverage, scaleX, scaleY, 0, 0, null);

        // Check if the final image is correct
        ensureCorrectTransformation(result, scaleX, scaleY);
    }

    @Test
    public void testNoBackground() throws IOException {
        // Selection of the Scale process
        ScaleCoverage process = new ScaleCoverage();

        // Execution of the operation
        GridCoverage2D result = process.execute(coverage, scaleX, scaleY, 0, 0, nearest);

        // Check if the final image is correct
        ensureCorrectTransformation(result, scaleX, scaleY);
    }

    @Test
    public void testIdentity() throws IOException {
        // Selection of the Scale process
        ScaleCoverage process = new ScaleCoverage();

        // Execution of the operation
        GridCoverage2D result = process.execute(coverage, 1, 1, 0, 0, nearest);

        // Check if the final image is correct
        ensureCorrectTransformation(result, 1, 1);
    }

    @Test
    public void testNearestInterp() throws IOException {
        // Selection of the Scale process
        ScaleCoverage process = new ScaleCoverage();
        // Definition of the Transformation object to use (The final image should be doubled and
        // translated)

        // Execution of the operation
        GridCoverage2D result = process.execute(coverage, scaleX, scaleY, 0, 0, nearest);

        // Check if the final image is correct
        ensureCorrectTransformation(result, scaleX, scaleY);
    }

    @Test
    public void testBilinearInterp() throws IOException {
        // Selection of the Scale process
        ScaleCoverage process = new ScaleCoverage();
        // Definition of the Transformation object to use (The final image should be doubled and
        // translated)

        // Execution of the operation
        GridCoverage2D result = process.execute(coverage, scaleX, scaleY, 0, 0, bilinear);

        // Check if the final image is correct
        ensureCorrectTransformation(result, scaleX, scaleY);
    }

    @Test
    public void testBicubicInterp() throws IOException {
        // Selection of the Affine process
        ScaleCoverage process = new ScaleCoverage();
        // Definition of the Transformation object to use (The final image should be doubled and
        // translated)

        // Execution of the operation
        GridCoverage2D result = process.execute(coverage, scaleX, scaleY, 0, 0, bicubic);

        // Check if the final image is correct
        ensureCorrectTransformation(result, scaleX, scaleY);
    }

    @Test
    public void testNoData() throws IOException {
        // Selection of the Affine process
        ScaleCoverage process = new ScaleCoverage();
        // Definition of the Transformation object to use (The final image should be doubled and
        // translated)

        // Execution of the operation
        GridCoverage2D result = process.execute(coverageNoData, scaleX, scaleY, 0, 0, bicubic);

        // Check if the final image is correct
        ensureCorrectTransformation(result, scaleX, scaleY);

        // Check if the NoData property is present
        NoDataContainer container = CoverageUtilities.getNoDataProperty(result);
        assertNotNull(container);
        Range nod = container.getAsRange();

        int minNodata = nod.getMin().intValue();

        assertEquals(nod.getMax().intValue(), 1);
        assertEquals(minNodata, -1);

        // Check if all the values are equal to nodata
        ImageWorker w = new ImageWorker(result.getRenderedImage());
        w.setNoData(null);
        int max = (int) w.getMaximums()[0];
        int min = (int) w.getMinimums()[0];

        assertEquals(max, minNodata);
        assertEquals(min, minNodata);
    }

    @Test
    public void testROI() throws IOException {
        // Selection of the Affine process
        ScaleCoverage process = new ScaleCoverage();
        // Definition of the Transformation object to use (The final image should be doubled and
        // translated)

        // Execution of the operation
        GridCoverage2D result = process.execute(coverageROI, scaleX, scaleY, 0, 0, nearest);

        // Check if the final image is correct
        ensureCorrectTransformation(result, scaleX, scaleY);

        // Check if the NoData property is present
        NoDataContainer container = CoverageUtilities.getNoDataProperty(result);
        assertNull(container);

        // Check if the ROI property is present
        ROI roiNew = CoverageUtilities.getROIProperty(result);
        assertNotNull(roiNew);
        // Check if ROI has been scaled
        Rectangle bounds = roiNew.getBounds();
        Rectangle inBounds = roi.getBounds();

        assertEquals(bounds.x, inBounds.x * scaleX, 0.0001);
        assertEquals(bounds.y, inBounds.y * scaleY, 0.0001);
        assertEquals(bounds.width, inBounds.width * scaleX, 0.0001);
        assertEquals(bounds.height, inBounds.height * scaleY, 0.0001);

        RenderedImage img = result.getRenderedImage();
        RandomIter it = RandomIterFactory.create(img, null, true, true);

        int minX = img.getMinX();
        int minY = img.getMinY();
        int maxX = img.getWidth() + minX;
        int maxY = img.getHeight() + minY;

        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                int sample = it.getSample(x, y, 0);
                if (!roiNew.contains(x, y)) {
                    assertEquals(sample, 0);
                }
            }
        }
    }

    @Test
    public void testROINoData() throws IOException {
        // Selection of the Affine process
        ScaleCoverage process = new ScaleCoverage();
        // Definition of the Transformation object to use (The final image should be doubled and
        // translated)

        // Execution of the operation
        GridCoverage2D result = process.execute(coverageNoDataROI, scaleX, scaleY, 0, 0, nearest);

        // Check if the final image is correct
        ensureCorrectTransformation(result, scaleX, scaleY);

        // Check if the NoData property is present
        NoDataContainer container = CoverageUtilities.getNoDataProperty(result);
        assertNotNull(container);

        // Check if the ROI property is present
        ROI roiNew = CoverageUtilities.getROIProperty(result);
        assertNotNull(roiNew);
        // Check if ROI has been scaled
        Rectangle bounds = roiNew.getBounds();
        Rectangle inBounds = roi.getBounds();

        assertEquals(bounds.x, inBounds.x * scaleX, 0.0001);
        assertEquals(bounds.y, inBounds.y * scaleY, 0.0001);
        assertEquals(bounds.width, inBounds.width * scaleX, 0.0001);
        assertEquals(bounds.height, inBounds.height * scaleY, 0.0001);

        RenderedImage img = result.getRenderedImage();
        RandomIter it = RandomIterFactory.create(img, null, true, true);

        int minX = img.getMinX();
        int minY = img.getMinY();
        int maxX = img.getWidth() + minX;
        int maxY = img.getHeight() + minY;

        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                int sample = it.getSample(x, y, 0);
                if (!roiNew.contains(x, y)) {
                    assertEquals(sample, -1);
                }
            }
        }
    }

    /** Check if the Coverage is correctly transformed. */
    private void ensureCorrectTransformation(GridCoverage2D result, double m00, double m11) {
        // Selection of the RenderedImages associated to the coverages
        RenderedImage inputImage = coverage.getRenderedImage();
        RenderedImage outputImage = result.getRenderedImage();
        // Definition of the source image parameters
        double srcMinX = inputImage.getMinX();
        double srcMinY = inputImage.getMinY();
        double srcHeight = inputImage.getHeight();
        double srcWidth = inputImage.getWidth();
        // Definition of the destination image parameters
        double dstMinX = outputImage.getMinX();
        double dstMinY = outputImage.getMinY();
        double dstHeight = outputImage.getHeight();
        double dstWidth = outputImage.getWidth();
        // Calculation of the parameters
        double actualH = srcHeight * m00;
        double actualW = srcWidth * m11;
        // Ensure the results are correct
        assertEquals(srcMinX, dstMinX, TOLERANCE);
        assertEquals(srcMinY, dstMinY, TOLERANCE);
        assertEquals(dstHeight, actualH, TOLERANCE);
        assertEquals(dstWidth, actualW, TOLERANCE);
    }
}
