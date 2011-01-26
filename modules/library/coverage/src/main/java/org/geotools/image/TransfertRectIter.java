/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image;

// J2SE dependencies
import java.awt.image.RasterFormatException;

// JAI dependencies
import javax.media.jai.iterator.RectIter;
import javax.media.jai.iterator.WritableRectIter;


/**
 * A {@linkplain WritableRectIter writable iterator} that read pixel values from an image, and
 * write pixel values to a different image. All {@code get} methods read values from the
 * <cite>source</cite> image specified at {@linkplain #create creation time}. All {@code set}
 * methods write values to the <cite>destination</cite> image specified at {@linkplain #create
 * creation time}, which may or may not be the same than the <cite>source</cite> image. This is
 * different than the usual {@link WritableRectIter} contract, which read and write values in the
 * same image. This {@code TransfertRectIter} is convenient for the implementation of some image
 * operations.
 *
 * @since 2.3
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class TransfertRectIter implements WritableRectIter {
    /**
     * The string for error message.
     *
     * @todo Localize.
     */
    private static final String ERROR = "Size mismatch";

    /**
     * The source.
     */
    private final RectIter src;

    /**
     * The destination.
     */
    private final WritableRectIter dst;

    /**
     * Constructs a {@code TransfertRectIter} object.
     */
    private TransfertRectIter(final RectIter src, final WritableRectIter dst) {
        this.src = src;
        this.dst = dst;
    }

    /**
     * Creates a {@link WritableRectIter} for the specified source and destination iterator.
     * The two iterators must iterate over a rectangle of the same size, otherwise a
     * {@link RasterFormatException} may be thrown during the iteration.
     *
     * @param  src The source iterator.
     * @param  dst The destination iterator.
     * @return An iterator that read sample from {@code src} and write sample
     *         to {@code dst}. If {@code src == dst}, then the destination
     *         iterator itself is returned.
     */
    public static WritableRectIter create(final RectIter src, final WritableRectIter dst) {
        if (src == dst) {
            return dst;
        }
        return new TransfertRectIter(src, dst);
    }

    /**
     * Sets the iterator to the first line of its bounding rectangle.
     */
    public void startLines() {
        src.startLines();
        dst.startLines();
    }

    /**
     * Sets the iterator to the leftmost pixel of its bounding rectangle.
     */
    public void startPixels() {
        src.startPixels();
        dst.startPixels();
    }

    /**
     * Sets the iterator to the first band of the image.
     */
    public void startBands() {
        src.startBands();
        dst.startBands();
    }

    /**
     * Jumps downward num lines from the current position.
     */
    public void jumpLines(int num) {
        src.jumpLines(num);
        dst.jumpLines(num);
    }

    /**
     * Jumps rightward num pixels from the current position.
     */
    public void jumpPixels(int num) {
        src.jumpPixels(num);
        dst.jumpPixels(num);
    }

    /**
     * Sets the iterator to the next line of the image.
     */
    public void nextLine() {
        src.nextLine();
        dst.nextLine();
    }

    /**
     * Sets the iterator to the next pixel in image (that is, move rightward).
     */
    public void nextPixel() {
        src.nextPixel();
        dst.nextPixel();
    }

    /**
     * Sets the iterator to the next band in the image.
     */
    public void nextBand() {
        src.nextBand();
        dst.nextBand();
    }

    /**
     * Sets the iterator to the next line in the image,
     * and returns {@code true} if the bottom row of the bounding rectangle has been passed.
     */
    public boolean nextLineDone() {
        boolean check = src.nextLineDone();
        if (check == dst.nextLineDone()) {
            return check;
        }
        throw new RasterFormatException(ERROR);
    }

    /**
     * Sets the iterator to the next pixel in the image (that is, move rightward).
     */
    public boolean nextPixelDone() {
        boolean check = src.nextPixelDone();
        if (check == dst.nextPixelDone()) {
            return check;
        }
        throw new RasterFormatException(ERROR);
    }

    /**
     * Sets the iterator to the next band in the image,
     * and returns {@code true} if the max band has been exceeded.
     */
    public boolean nextBandDone() {
        boolean check = src.nextBandDone();
        if (check == dst.nextBandDone()) {
            return check;
        }
        throw new RasterFormatException(ERROR);
    }


    /**
     * Returns {@code true} if the bottom row of the bounding rectangle has been passed.
     */
    public boolean finishedLines() {
        boolean check = src.finishedLines();
        if (check == dst.finishedLines()) {
            return check;
        }
        throw new RasterFormatException(ERROR);
    }

    /**
     * Returns {@code true} if the right edge of the bounding rectangle has been passed.
     */
    public boolean finishedPixels() {
        boolean check = src.finishedPixels();
        if (check == dst.finishedPixels()) {
            return check;
        }
        throw new RasterFormatException(ERROR);
    }

    /**
     * Returns {@code true} if the max band in the image has been exceeded.
     */
    public boolean finishedBands() {
        boolean check = src.finishedBands();
        if (check == dst.finishedBands()) {
            return check;
        }
        throw new RasterFormatException(ERROR);
    }

    /**
     * Returns the samples of the current pixel from the image in an array of int.
     */
    public int[] getPixel(int[] array) {
        return src.getPixel(array);
    }

    /**
     * Returns the samples of the current pixel from the image in an array of float.
     */
    public float[] getPixel(float[] array) {
        return src.getPixel(array);
    }

   /**
    * Returns the samples of the current pixel from the image in an array of double.
    */
    public double[] getPixel(double[] array) {
        return src.getPixel(array);
    }

    /**
     * Returns the current sample as an integer.
     */
    public int getSample() {
        return src.getSample();
    }

    /**
     * Returns the specified sample of the current pixel as an integer.
     */
    public int getSample(int b) {
        return src.getSample(b);
    }

    /**
     * Returns the current sample as a float.
     */
    public float getSampleFloat() {
        return src.getSampleFloat();
    }

    /**
     * Returns the specified sample of the current pixel as a float.
     */
    public float getSampleFloat(int b) {
        return src.getSampleFloat(b);
    }

    /**
     * Returns the current sample as a double.
     */
    public double getSampleDouble() {
        return src.getSampleDouble();
    }

    /**
     * Returns the specified sample of the current pixel as a double.
     */
    public double getSampleDouble(int b) {
        return src.getSampleDouble(b);
    }

    /**
     * Sets all samples of the current pixel to a set of int values.
     */
    public void setPixel(int[] array) {
        dst.setPixel(array);
    }

    /**
     * Sets all samples of the current pixel to a set of float values.
     */
    public void setPixel(float[] array) {
        dst.setPixel(array);
    }

    /**
     * Sets all samples of the current pixel to a set of double values.
     */
    public void setPixel(double[] array) {
        dst.setPixel(array);
    }

    /**
     * Sets the current sample to an integral value.
     */
    public void setSample(int s) {
        dst.setSample(s);
    }

    /**
     * Sets the current sample to a float value.
     */
    public void setSample(float s) {
        dst.setSample(s);
    }

    /**
     * Sets the current sample to a double value.
     */
    public void setSample(double s) {
        dst.setSample(s);
    }

    /**
     * Sets the specified sample of the current pixel to an integral value.
     */
    public void setSample(int b, int s) {
        dst.setSample(b, s);
    }

    /**
     * Sets the specified sample of the current pixel to a float value.
     */
    public void setSample(int b, float s) {
        dst.setSample(b, s);
    }

    /**
     * Sets the specified sample of the current pixel to a double value.
     */
    public void setSample(int b, double s) {
        dst.setSample(b, s);
    }
}
