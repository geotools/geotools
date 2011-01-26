/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Color;
import java.awt.image.*;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.IIOException;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.image.ColorUtilities;


/**
 * A set of RGB colors created by a {@linkplain PaletteFactory palette factory} from a name.
 * A palette can creates an {@linkplain IndexColorModel index color model} or an {@linkplain
 * ImageTypeSpecifier image type specifier} from the RGB colors. The color model is retained
 * by the palette as a {@linkplain WeakReference weak reference} (<strong>not</strong> as a
 * {@linkplain java.lang.ref.SoftReference soft reference}) because it may consume up to 256
 * kb. The purpose of the weak reference is to share existing instances in order to reduce
 * memory usage; the purpose is not to provide caching.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Antoine Hnawia
 * @author Martin Desruisseaux (IRD)
 */
final class IndexedPalette extends Palette {
    /**
     * The maximal allowed value, corresponding to the maximum value for unsigned 16 bits integer.
     * DO NOT EDIT: this value <strong>MUST</strong> be {@code 0xFFFF}.
     */
    private static final int MAX_UNSIGNED = 0xFFFF;

    /**
     * Index of the first valid element (inclusive) in the {@linkplain IndexColorModel
     * index color model} to be created. Pixels in the range 0 inclusive to {@code lower}
     * exclusive will be reserved for "no data" values.
     * <p>
     * Strictly speaking, this index should be non-negative because {@link IndexColorModel}
     * do not supports negative index. However this {@code Palette} implementation accepts
     * negative values provided that {@link #upper} is not greater than {@value Short#MAX_VALUE}.
     * If this condition holds, then {@code Palette} will transpose negative values as positive
     * values in the range {@code 0x80000} to {@code 0xFFFF} inclusive. Be aware that such
     * approach consume the maximal amount of memory, i.e. 256 kilobytes for each color model.
     */
    protected final int lower;

    /**
     * Index of the last valid element (exclusive) in the {@linkplain IndexColorModel
     * index color model} to be created. Pixels in the range {@code upper} inclusive
     * to {@link #size} exclusive will be reserved for "no data" values. This value
     * is always greater than {@link #lower} (note that it may be negative).
     */
    protected final int upper;

    /**
     * The size of the {@linkplain IndexColorModel index color model} to be created.
     * This is the value to be returned by {@link IndexColorModel#getMapSize}. This
     * value is always positive.
     */
    protected final int size;

    /**
     * Creates a palette with the specified name and size. The RGB colors will be distributed
     * in the range {@code lower} inclusive to {@code upper} exclusive. Remaining pixel values
     * (if any) will be left to a black or transparent color by default.
     *
     * @param factory The originating factory.
     * @param name    The palette name.
     * @param lower   Index of the first valid element (inclusive) in the
     *                {@linkplain IndexColorModel index color model} to be created.
     * @param upper   Index of the last valid element (exclusive) in the
     *                {@linkplain IndexColorModel index color model} to be created.
     * @param size    The size of the {@linkplain IndexColorModel index color model} to be created.
     *                This is the value to be returned by {@link IndexColorModel#getMapSize}.
     * @param numBands    The number of bands (usually 1).
     * @param visibleBand The band to use for color computations (usually 0).
     */
    protected IndexedPalette(final PaletteFactory factory, final String name, final int lower,
                   final int upper, int size, final int numBands, final int visibleBand)
    {
        super(factory, name, numBands, visibleBand);
        final int minAllowed, maxAllowed; // inclusives
        if (lower < 0) {
            minAllowed = Short.MIN_VALUE;
            maxAllowed = Short.MAX_VALUE;
            size       = (size <= 0x100) ? 0x100 : (MAX_UNSIGNED + 1);
            // 'size' must be FF or FFFF in order to rool negative values.
        } else {
            minAllowed = 0;
            maxAllowed = MAX_UNSIGNED;
        }
        ensureInsideBounds(lower, minAllowed, maxAllowed);
        ensureInsideBounds(upper, minAllowed, maxAllowed + 1);
        ensureInsideBounds(size,  upper,      MAX_UNSIGNED + 1);
        if (lower >= upper) {
            throw new IllegalArgumentException(factory.getErrorResources().getString(
                    ErrorKeys.BAD_RANGE_$2, lower, upper));
        }
        this.lower = lower;
        this.upper = upper;
        this.size  = size;
    }

    /**
     * Returns the scale from <cite>normalized values</cite> (values in the range [0..1])
     * to values in the range of this palette.
     */
    @Override
    double getScale() {
        return upper - lower;
    }

    /**
     * Returns the offset from <cite>normalized values</cite> (values in the range [0..1])
     * to values in the range of this palette.
     */
    @Override
    double getOffset() {
        return lower;
    }

    /**
     * Creates and returns ARGB values for the {@linkplain IndexColorModel index color model} to be
     * created. This method is invoked automatically the first time the color model is required, or
     * when it need to be rebuilt.
     *
     * @throws  FileNotFoundException If the RGB values need to be read from a file and this file
     *                                (typically inferred from {@link #name}) is not found.
     * @throws  IOException           If an other find of I/O error occured.
     * @throws  IIOException          If an other kind of error prevent this method to complete.
     */
    private int[] createARGB() throws IOException {
        final Color[] colors = factory.getColors(name);
        if (colors == null) {
            throw new FileNotFoundException(factory.getErrorResources().getString(
                    ErrorKeys.FILE_DOES_NOT_EXIST_$1, name));
        }
        final int[] ARGB = new int[size];
        if (lower >= 0) {
            ColorUtilities.expand(colors, ARGB, lower, upper);
        } else {
            ColorUtilities.expand(colors, ARGB, 0, upper - lower);
            final int negativeStart = size + lower;
            final int negativeCount = -lower;
            final int[] negatives = new int[negativeCount];
            System.arraycopy(ARGB, 0, negatives, 0, negativeCount);
            System.arraycopy(ARGB, negativeCount, ARGB, 0, negativeStart);
            System.arraycopy(negatives, 0, ARGB, negativeStart, negativeCount);
        }
        return ARGB;
    }

    /**
     * Returns the image type specifier for this palette. This method tries to reuse existing
     * color model if possible, since it may consume a significant amount of memory.
     *
     * @throws  FileNotFoundException If the RGB values need to be read from a file and this file
     *          (typically inferred from {@linkplain #name name}) is not found.
     * @throws  IOException  If an other find of I/O error occured.
     * @throws  IIOException If an other kind of error prevent this method to complete.
     */
    public synchronized ImageTypeSpecifier getImageTypeSpecifier() throws IOException {
        /*
         * First checks the weak references.
         */
        ImageTypeSpecifier its = queryCache();
        if (its != null) {
            return its;
        }
        /*
         * Nothing reacheable. Rebuild the specifier.
         */
        final int[] ARGB = createARGB();
        final int bits = ColorUtilities.getBitCount(ARGB.length);
        final int type = (bits <= 8) ? DataBuffer.TYPE_BYTE : DataBuffer.TYPE_USHORT;
        final boolean packed = (bits==1 || bits==2 || bits==4);
        final boolean dense  = (packed || bits==8 || bits==16);
        if (dense && (1 << bits) == ARGB.length && numBands == 1) {
            final byte[] A = new byte[ARGB.length];
            final byte[] R = new byte[ARGB.length];
            final byte[] G = new byte[ARGB.length];
            final byte[] B = new byte[ARGB.length];
            for (int i=0; i<ARGB.length; i++) {
                int code = ARGB[i];
                B[i] = (byte) ((code       ) & 0xFF);
                G[i] = (byte) ((code >>>= 8) & 0xFF);
                R[i] = (byte) ((code >>>= 8) & 0xFF);
                A[i] = (byte) ((code >>>= 8) & 0xFF);
            }
            its = ImageTypeSpecifier.createIndexed(R,G,B,A, bits, type);
        } else {
            /*
             * The "ImageTypeSpecifier.createIndexed(...)" method is too strict. The IndexColorModel
             * constructor is more flexible. This block mimic the "ImageTypeSpecifier.createIndexed"
             * work without the constraints imposed by "createIndexed". Being more flexible consume
             * less memory for the color palette, since we don't force it to be 64 kb in the USHORT
             * data type case.
             */
            final IndexColorModel colors = ColorUtilities.getIndexColorModel(ARGB, numBands, visibleBand);
            final SampleModel samples;
            if (packed) {
                samples = new MultiPixelPackedSampleModel(type, 1, 1, bits);
            } else {
                samples = new PixelInterleavedSampleModel(type, 1, 1, 1, 1, new int[1]);
            }
            its = new ImageTypeSpecifier(colors, samples);
        }
        cache(its);
        return its;
    }

    /**
     * Returns a hash value for this palette.
     */
    @Override
    public int hashCode() {
        return super.hashCode() + 37*(lower + 37*(upper + 37*size));
    }

    /**
     * Compares this palette with the specified object for equality.
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (super.equals(object)) {
            final IndexedPalette that = (IndexedPalette) object;
            return this.lower == that.lower &&
                   this.upper == that.upper &&
                   this.size  == that.size;
        }
        return false;
    }

    /**
     * Returns a string representation of this palette. Used for debugging purpose only.
     */
    @Override
    public String toString() {
        return name + " [" + lower + "..." + (upper-1) + "] size=" + size;
    }
}
