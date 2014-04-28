/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.test;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.geotools.image.ImageWorker;
import org.geotools.image.test.ImageComparator.Mode;
import org.geotools.util.logging.Logging;

/**
 * Compares two images using perceptual criterias: the assertions will fail if the images would look
 * different to a human being.
 * 
 * @author Andrea Aime - GeoSolutions
 *
 *
 * @source $URL$
 */
public class ImageAssert {

    static final boolean INTERACTIVE = Boolean.getBoolean("org.geotools.image.test.interactive");
    
    static final Logger LOGGER = Logging.getLogger(Logger.class);

    /**
     * Checks the image in the reference file and the actual image are equals from a human
     * perception p.o.v
     * 
     * @param expectedFile
     * @param actualImage
     * @param threshold
     * @throws IOException
     */
    public static void assertEquals(File expectedFile, RenderedImage actualImage, int threshold)
            throws IOException {
        assertImagesResemble(expectedFile, actualImage, Mode.IgnoreAntialiasing, threshold, true);
    }

    /**
     * Checks the expected image and the actual image are equals from a human perception p.o.v
     * 
     * @param expectedFile
     * @param actualImage
     * @param threshold
     */
    public static void assertEquals(RenderedImage expectedImage, RenderedImage actualImage,
            int threshold) {
        ImageComparator comparator = new ImageComparator(Mode.IgnoreAntialiasing, expectedImage, actualImage);
        if (comparator.getMismatchCount() > threshold) {
            throw new AssertionError("Images are visibly different, found "
                    + comparator.getMismatchCount() + " different pixels, against a threshold of "
                    + threshold);
        }
    }

    /**
     * Checks the expected image and the actual image are equals from a human perception p.o.v
     * 
     * @param expectedFile
     * @param actualImage
     * @param threshold
     * @throws IOException
     */
    public static void assertEquals(File expectedImage, RenderedImage actualImage, int threshold,
            Mode mode) throws IOException {
        assertImagesResemble(expectedImage, actualImage, mode, threshold, true);
    }

    private static void assertImagesResemble(File expectedFile, RenderedImage actualImage,
            Mode mode, int threshold, boolean actualReferenceFile) throws IOException {
        // do we have the reference image at all?
        if (!expectedFile.exists()) {

            // see what the user thinks of the image
            boolean useAsReference = actualReferenceFile && INTERACTIVE
                    && ReferenceImageDialog.show(realignImage(actualImage));
            if (useAsReference) {
                try {
                    new ImageWorker(actualImage).writePNG(expectedFile, "FILTERED", 0.9f, false,
                            false);
                } catch (IOException e) {
                    throw (Error) new AssertionError("Failed to write the image to disk")
                            .initCause(e);
                }
            } else {
                throw new AssertionError("Reference image is missing: " + expectedFile);
            }
        } else {
            RenderedImage expectedImage = ImageIO.read(expectedFile);
            ImageComparator comparator = new ImageComparator(mode, expectedImage, actualImage);
            if (comparator.getMismatchCount() > threshold) {
                // check with the user
                boolean overwrite = false;
                if (INTERACTIVE) {
                    overwrite = CompareImageDialog.show(realignImage(expectedImage),
                            realignImage(actualImage), actualReferenceFile);
                }

                if (overwrite) {
                    ImageIO.write(actualImage, "PNG", expectedFile);
                } else {
                    throw new AssertionError("Images are visibly different, found "
                            + comparator.getMismatchCount()
                            + " different pixels, against a threshold of " + threshold);
                }
            } else {
                LOGGER.info("Images are not visibly different, found "
                        + comparator.getMismatchCount()
                        + " different pixels, against a threshold of " + threshold);
            }
        }
    }

    static String getFormat(File expectedImage) {
        final String loName = expectedImage.getName().toLowerCase();
        if (loName.endsWith(".png")) {
            return "PNG";
        } else if (loName.endsWith(".tif") || loName.endsWith(".tiff")) {
            return "TIFF";
        } else {
            throw new IllegalArgumentException("Expected image file should be a png or a tiff");
        }
    }
    
    /**
     * Makes sure the image starts at 0,0, all images coming from files do but the ones
     * coming from a JAI chain might not
     * @param image
     * @return
     */
    static final RenderedImage realignImage(RenderedImage image) {
        if (image.getMinX() > 0 || image.getMinY() > 0) {
            return new BufferedImage(image.getColorModel(),
                    ((WritableRaster) image.getData()).createWritableTranslatedChild(0, 0), image
                            .getColorModel().isAlphaPremultiplied(), null);
        } else {
            return image;
        }
    }

}
