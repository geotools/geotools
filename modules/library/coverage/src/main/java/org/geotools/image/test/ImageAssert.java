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
import java.util.logging.Level;
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
 */
public class ImageAssert {

    /**
     * Makes the test interactive, showing a Swing dialog with before/after and a choice to
     * overwrite the expected image
     */
    static final boolean INTERACTIVE = Boolean.getBoolean("org.geotools.image.test.interactive");

    /** Forces the image comparison tests to be skipped */
    static final boolean SKIP = Boolean.getBoolean("org.geotools.image.test.skip");

    static final Logger LOGGER = Logging.getLogger(Logger.class);

    /**
     * Checks the image in the reference file and the actual image are equals from a human
     * perception p.o.v
     */
    public static void assertEquals(File expectedFile, RenderedImage actualImage, int threshold)
            throws IOException {
        if (SKIP) {
            return;
        }
        assertImagesResemble(expectedFile, actualImage, Mode.IgnoreAntialiasing, threshold, true);
    }

    /** Checks the expected image and the actual image are equals from a human perception p.o.v */
    public static void assertEquals(
            RenderedImage expectedImage, RenderedImage actualImage, int threshold) {
        ImageComparator comparator =
                new ImageComparator(Mode.IgnoreAntialiasing, expectedImage, actualImage);
        if (comparator.getMismatchCount() > threshold) {
            if (INTERACTIVE) {
                CompareImageDialog.show(expectedImage, actualImage, false);
            }
            throw new AssertionError(
                    "Images are visibly different, found "
                            + comparator.getMismatchCount()
                            + " different pixels, against a threshold of "
                            + threshold
                            + "\nYou can add -Dorg.geotools.image.test.interactive=true to show a dialog comparing them (requires GUI support)");
        }
    }

    /** Checks the expected image and the actual image are equals from a human perception p.o.v */
    public static void assertEquals(
            File expectedImage, RenderedImage actualImage, int threshold, Mode mode)
            throws IOException {
        assertImagesResemble(expectedImage, actualImage, mode, threshold, true);
    }

    private static void assertImagesResemble(
            File expectedFile,
            RenderedImage actualImage,
            Mode mode,
            int threshold,
            boolean actualReferenceFile)
            throws IOException {
        // do we have the reference image at all?
        if (!expectedFile.exists()) {

            // see what the user thinks of the image
            boolean useAsReference =
                    actualReferenceFile
                            && INTERACTIVE
                            && ReferenceImageDialog.show(realignImage(actualImage));
            if (useAsReference) {
                try {
                    File parent = expectedFile.getParentFile();
                    if (!parent.exists() && !parent.mkdirs()) {
                        throw new AssertionError(
                                "Could not create directory that will contain :"
                                        + expectedFile.getParent());
                    }
                    new ImageWorker(actualImage)
                            .writePNG(expectedFile, "FILTERED", 0.9f, false, false);
                } catch (IOException e) {
                    throw (Error)
                            new AssertionError("Failed to write the image to disk").initCause(e);
                }
            } else {
                throw new AssertionError(
                        "Reference image is missing: "
                                + expectedFile
                                + ", add -Dorg.geotools.image.test.interactive=true to show a dialog comparing them (requires GUI support)");
            }
        } else {
            RenderedImage expectedImage = ImageIO.read(expectedFile);
            ImageComparator comparator = new ImageComparator(mode, expectedImage, actualImage);
            if (comparator.getMismatchCount() > threshold) {
                // check with the user
                boolean overwrite = false;
                if (INTERACTIVE) {
                    overwrite =
                            CompareImageDialog.show(
                                    realignImage(expectedImage),
                                    realignImage(actualImage),
                                    actualReferenceFile);
                } else {
                    String message =
                            "Images are different, add -Dorg.geotools.image.test.interactive=true to show a dialog comparing them (requires GUI support)";
                    try {
                        File actualFile = writeActualImage(expectedFile, actualImage);
                        message += "\n Actual image saved at " + actualFile.getCanonicalPath();
                    } catch (IOException e) {
                        LOGGER.log(Level.WARNING, "Failure while saving actual image on file", e);
                    }

                    LOGGER.info(message);
                }

                if (overwrite) {
                    ImageIO.write(actualImage, "PNG", expectedFile);
                } else {
                    throw new AssertionError(
                            "Images are visibly different, found "
                                    + comparator.getMismatchCount()
                                    + " different pixels, against a threshold of "
                                    + threshold
                                    + "\nYou can add -Dorg.geotools.image.test.interactive=true to show a dialog comparing them (requires GUI support)");
                }
            } else {
                LOGGER.fine(
                        "Images are not visibly different, found "
                                + comparator.getMismatchCount()
                                + " different pixels, against a threshold of "
                                + threshold);
            }
        }
    }

    private static File writeActualImage(File expectedFile, RenderedImage actualImage)
            throws IOException {
        File failureDir = new File("./target/image-compare-fails");

        if (!failureDir.exists()) {
            failureDir.mkdir();
        }
        File file = new File(failureDir, expectedFile.getName());
        ImageIO.write(actualImage, "PNG", file);

        return file;
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
     * Makes sure the image starts at 0,0, all images coming from files do but the ones coming from
     * a JAI chain might not
     */
    static final RenderedImage realignImage(RenderedImage image) {
        if (image.getMinX() > 0 || image.getMinY() > 0) {
            return new BufferedImage(
                    image.getColorModel(),
                    ((WritableRaster) image.getData()).createWritableTranslatedChild(0, 0),
                    image.getColorModel().isAlphaPremultiplied(),
                    null);
        } else {
            return image;
        }
    }
}
