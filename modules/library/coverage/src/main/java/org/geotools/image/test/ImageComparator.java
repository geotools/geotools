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
package org.geotools.image.test;

import java.awt.image.RenderedImage;
import javax.media.jai.PlanarImage;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;
import org.geotools.image.ImageWorker;

/**
 * Utility to compare two images and verify if the are equal to the human eye, or not. See the {@link Mode} enumeration
 * for comparison modes. The image comparison logic has been ported to Java from Resemble.js,
 * https://github.com/Huddle/Resemble.js
 *
 * @author Andrea Aime - GeoSolutions
 */
public class ImageComparator {

    public enum Mode {
        /**
         * Checks if the images are equal taking into account the full color and all pixels. Some light difference
         * between the two images is still being tolerated
         */
        IgnoreNothing,
        /**
         * Same as above, but if a pixel is found to be anti-aliased, only brightness will be compared, instead of the
         * full color component
         */
        IgnoreAntialiasing,
        /** Ignores the colors and compares only the brightness */
        IgnoreColors
    }

    final class Pixel {
        int r;

        int g;

        int b;

        int a;

        private int brightness;

        private double hue;

        public Pixel() {}

        public void init(int[] px) {
            if (bands < 2) {
                r = g = b = px[0];
                a = 255;
            } else {
                r = px[0];
                g = px[1];
                b = px[2];
                if (bands == 4) {
                    a = px[3];
                } else {
                    a = 255;
                }
            }
            brightness = Integer.MIN_VALUE;
            hue = Double.NaN;
        }

        int getBrightness() {
            if (brightness == Integer.MIN_VALUE) {
                brightness = (int) Math.round(0.3 * r + 0.59 * g + 0.11 * b);
            }
            return brightness;
        }

        double getHue() {
            if (Double.isNaN(hue)) {
                double r = this.r / 255d;
                double g = this.g / 255d;
                double b = this.b / 255d;
                double max = Math.max(r, Math.max(g, b));
                double min = Math.min(r, Math.max(g, b));

                if (max == min) {
                    hue = 0; // achromatic
                } else {
                    double d = max - min;
                    if (max == r) {
                        hue = (g - b) / d + (g < b ? 6 : 0);
                    } else if (max == g) {
                        hue = (b - r) / d + 2;
                    } else {
                        hue = (r - g) / d + 4;
                    }
                    hue /= 6;
                }
            }
            return hue;
        }

        public boolean isRGBSame(Pixel other) {
            if (a != other.a) {
                return false;
            }
            if (b != other.b) return false;
            if (g != other.g) return false;
            if (r != other.r) return false;
            return true;
        }

        public boolean isSimilar(Pixel other) {
            return isColorSimilar(r, other.r, RED)
                    && //
                    isColorSimilar(g, other.g, GREEN)
                    && //
                    isColorSimilar(b, other.b, BLUE)
                    && //
                    isColorSimilar(a, other.a, ALPHA);
        }

        public boolean isConstrasting(Pixel other) {
            return Math.abs(getBrightness() - other.getBrightness()) > tolerance[MAX_BRIGHTNESS];
        }

        private boolean isColorSimilar(int a, int b, int color) {
            final int diff = Math.abs(a - b);
            return diff == 0 || diff < tolerance[color];
        }

        public boolean isBrightnessSimilar(Pixel other) {
            return isColorSimilar(a, other.a, ALPHA)
                    && isColorSimilar(getBrightness(), other.getBrightness(), MIN_BRIGHTNESS);
        }

        public boolean hasDifferentHue(Pixel cursor) {
            return Math.abs(getHue() - cursor.getHue()) > 0.3;
        }

        @Override
        public String toString() {
            return "Pixel [r=" + r + ", g=" + g + ", b=" + b + ", a=" + a + "]";
        }
    }

    static final int RED = 0;

    static final int GREEN = 1;

    static final int BLUE = 2;

    static final int ALPHA = 3;

    static final int MIN_BRIGHTNESS = 4;

    static final int MAX_BRIGHTNESS = 5;

    int[] tolerance = new int[MAX_BRIGHTNESS + 1];

    Mode mode;

    long mismatchCount = 0;

    double mismatchPercent;

    int bands;

    public ImageComparator(Mode mode, RenderedImage image1, RenderedImage image2) {

        int height = image1.getHeight();
        int width = image1.getWidth();
        if (width != image2.getWidth() || height != image2.getHeight()) {
            mismatchCount = Integer.MAX_VALUE;
            mismatchPercent = 1d;
            return;
        }

        // switch to rbg/rgba/gray/gray-alpha
        image1 = normalizeImage(image1);
        image2 = normalizeImage(image2);

        this.bands = image1.getSampleModel().getNumBands();
        final boolean hasAlpha = image1.getColorModel().hasAlpha();
        if (bands > 4 || bands == 2 && !hasAlpha || bands == 3 && hasAlpha) {
            throw new IllegalArgumentException("Images have the wrong type, this code only supports gray, gray/alpha, "
                    + "RGB, RGBA images, or images that can be transformed in those models");
        }

        this.mode = mode;
        switch (mode) {
            case IgnoreNothing:
                tolerance[RED] = 16;
                tolerance[GREEN] = 16;
                tolerance[BLUE] = 16;
                tolerance[ALPHA] = 16;
                tolerance[MIN_BRIGHTNESS] = 16;
                tolerance[MAX_BRIGHTNESS] = 240;
                break;
            case IgnoreAntialiasing:
                tolerance[RED] = 32;
                tolerance[GREEN] = 32;
                tolerance[BLUE] = 32;
                tolerance[ALPHA] = 128;
                tolerance[MIN_BRIGHTNESS] = 64;
                tolerance[MAX_BRIGHTNESS] = 98;
                break;
            case IgnoreColors:
                tolerance[ALPHA] = 16;
                tolerance[MIN_BRIGHTNESS] = 16;
                tolerance[MAX_BRIGHTNESS] = 240;
                break;
        }

        computeDifference(image1, image2);
        mismatchPercent = mismatchCount * 1d / (width * image2.getHeight());
    }

    /** Forces the image to start in the origin and have a rgb/rbga/gray/gray+alpha structure */
    private RenderedImage normalizeImage(RenderedImage image1) {
        image1 = new ImageWorker(image1)
                .forceColorSpaceRGB()
                .forceComponentColorModel()
                .getRenderedImage();
        if (image1.getMinX() != 0 || image1.getMinY() != 0) {
            image1 = PlanarImage.wrapRenderedImage(image1).getAsBufferedImage();
        }
        return image1;
    }

    public double getMismatchPercent() {
        return mismatchPercent;
    }

    public long getMismatchCount() {
        return mismatchCount;
    }

    void computeDifference(RenderedImage image1, RenderedImage image2) {
        int[] components = new int[bands];
        Pixel px1 = new Pixel();
        Pixel px2 = new Pixel();

        final int width = image1.getWidth();
        final int height = image1.getHeight();
        RandomIter it1 = RandomIterFactory.create(image1, null);
        RandomIter it2 = RandomIterFactory.create(image2, null);
        Pixel cursor = new Pixel();
        try {
            for (int r = 0; r < height; r++) {
                for (int c = 0; c < width; c++) {
                    it1.getPixel(c, r, components);
                    px1.init(components);
                    it2.getPixel(c, r, components);
                    px2.init(components);

                    if (mode == Mode.IgnoreColors) {
                        if (!px1.isBrightnessSimilar(px2)) {
                            mismatchCount++;
                        }
                    } else if (!px1.isSimilar(px2)) {
                        if (mode == Mode.IgnoreAntialiasing) {
                            if (isAntialised(px1, it1, r, c, width, height, components, cursor)
                                    || isAntialised(px2, it2, r, c, width, height, components, cursor)) {
                                if (!px1.isBrightnessSimilar(px2)) {
                                    mismatchCount++;
                                }
                            } else {
                                mismatchCount++;
                            }
                        } else {
                            mismatchCount++;
                        }
                    }
                }
            }
        } finally {
            it1.done();
            it2.done();
        }
    }

    private boolean isAntialised(
            Pixel source, RandomIter it, int row, int col, int width, int height, int[] pixel, Pixel cursor) {
        final int DISTANCE = 1;

        int highContrastSibling = 0;
        int siblingWithDifferentHue = 0;
        int equivalentSibling = 0;

        final int rowMin = Math.max(row - DISTANCE, 0);
        final int rowMax = Math.min(row + DISTANCE, width);
        final int colMin = Math.max(col - DISTANCE, 0);
        final int colMax = Math.min(col + DISTANCE, height);
        for (int c = colMin; c < colMax; c++) {
            for (int r = rowMin; r < rowMax; r++) {
                if (c == col && r == row) {
                    // ignore source pixel
                    continue;
                } else {
                    it.getPixel(c, r, pixel);
                    cursor.init(pixel);

                    if (source.isRGBSame(cursor)) {
                        equivalentSibling++;
                    } else if (source.isConstrasting(cursor)) {
                        highContrastSibling++;
                    }

                    if (source.hasDifferentHue(cursor)) {
                        siblingWithDifferentHue++;
                    }

                    if (siblingWithDifferentHue > 1 || highContrastSibling > 1) {
                        return true;
                    }
                }
            }
        }

        if (equivalentSibling < 2) {
            return true;
        }

        return false;
    }
}
