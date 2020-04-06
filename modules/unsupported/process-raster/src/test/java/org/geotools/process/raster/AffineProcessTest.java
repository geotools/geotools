/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.media.jai.InterpolationBicubic;
import javax.media.jai.InterpolationBilinear;
import javax.media.jai.InterpolationNearest;
import org.geotools.TestData;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.coverage.grid.GridCoverageReader;

public class AffineProcessTest {

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

    /** No Data Values used for the Affine transformation */
    private static double[] nodata;

    /** Scale X parameter */
    private static double m00;

    /** Shear X parameter */
    private static double m01;

    /** Shear Y parameter */
    private static double m10;

    /** Scale Y parameter */
    private static double m11;

    /** Translate X parameter */
    private static double m02;

    /** Translate Y parameter */
    private static double m12;

    @BeforeClass
    public static void setup() throws FileNotFoundException, IOException {
        // Selection of the File to use
        File tiff = TestData.file(AffineProcessTest.class, "sample.tif");
        // Reading of the file with the GeoTiff reader
        AbstractGridFormat format = GridFormatFinder.findFormat(tiff);
        // Get a reader for the selected format
        GridCoverageReader reader = format.getReader(tiff);
        // Read the input Coverage
        coverage = (GridCoverage2D) reader.read(null);
        // Reader disposal
        reader.dispose();

        // Definition of the interpolation type
        nearest = new InterpolationNearest();
        bilinear = new InterpolationBilinear();
        bicubic = new InterpolationBicubic(8);

        // Definition of the background values
        nodata = new double[] {0};

        // Definition of the transformation. (The final image should be doubled and translated)
        m00 = 2d;
        m01 = 0d;
        m10 = 0d;
        m11 = 2d;
        m02 = 10d;
        m12 = 20d;
    }

    @Test
    public void testNoInterp() {
        // Selection of the Affine process
        AffineProcess process = new AffineProcess();

        // Execution of the operation
        GridCoverage2D result =
                process.execute(coverage, m00, m11, m01, m10, m02, m12, nodata, null);

        // Check if the final image is correct
        ensureCorrectTransformation(result, m00, m11, m02, m12);
    }

    @Test
    public void testNoBackground() {
        // Selection of the Affine process
        AffineProcess process = new AffineProcess();

        // Execution of the operation
        GridCoverage2D result =
                process.execute(coverage, m00, m11, m01, m10, m02, m12, null, nearest);

        // Check if the final image is correct
        ensureCorrectTransformation(result, m00, m11, m02, m12);
    }

    @Test
    public void testIdentity() {
        // Selection of the Affine process
        AffineProcess process = new AffineProcess();

        // Execution of the operation
        GridCoverage2D result =
                process.execute(coverage, null, null, null, null, null, null, nodata, nearest);

        // Check if the final image is correct
        ensureCorrectTransformation(result, 1, 1, 0, 0);
    }

    @Test
    public void testNearestInterp() {
        // Selection of the Affine process
        AffineProcess process = new AffineProcess();
        // Definition of the Transformation object to use (The final image should be doubled and
        // translated)

        // Execution of the operation
        GridCoverage2D result =
                process.execute(coverage, m00, m11, m01, m10, m02, m12, nodata, nearest);

        // Check if the final image is correct
        ensureCorrectTransformation(result, m00, m11, m02, m12);
    }

    @Test
    public void testBilinearInterp() {
        // Selection of the Affine process
        AffineProcess process = new AffineProcess();
        // Definition of the Transformation object to use (The final image should be doubled and
        // translated)

        // Execution of the operation
        GridCoverage2D result =
                process.execute(coverage, m00, m11, m01, m10, m02, m12, nodata, bilinear);

        // Check if the final image is correct
        ensureCorrectTransformation(result, m00, m11, m02, m12);
    }

    @Test
    public void testBicubicInterp() {
        // Selection of the Affine process
        AffineProcess process = new AffineProcess();
        // Definition of the Transformation object to use (The final image should be doubled and
        // translated)

        // Execution of the operation
        GridCoverage2D result =
                process.execute(coverage, m00, m11, m01, m10, m02, m12, nodata, bicubic);

        // Check if the final image is correct
        ensureCorrectTransformation(result, m00, m11, m02, m12);
    }

    /** Check if the Coverage is correctly transformed. */
    private void ensureCorrectTransformation(
            GridCoverage2D result, double m00, double m11, double m02, double m12) {
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
        double actualMinX = srcMinX + m02;
        double actualMinY = srcMinY + m12;
        double actualH = srcHeight * m00;
        double actualW = srcWidth * m11;
        // Ensure the results are correct
        assertEquals(actualMinX, dstMinX, TOLERANCE);
        assertEquals(actualMinY, dstMinY, TOLERANCE);
        assertEquals(dstHeight, actualH, TOLERANCE);
        assertEquals(dstWidth, actualW, TOLERANCE);
    }
}
