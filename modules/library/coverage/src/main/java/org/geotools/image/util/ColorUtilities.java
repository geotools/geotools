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
package org.geotools.image.util;

import java.awt.Color;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.text.MessageFormat;
import java.util.Arrays;
import org.geotools.metadata.i18n.ErrorKeys;

/**
 * A set of static methods for handling of colors informations. Some of those methods are useful, but not really
 * rigorous. This is why they do not appear in any "official" package, but instead in this private one.
 *
 * <p><strong>Do not rely on this API!</strong>
 *
 * <p>It may change in incompatible way in any future version.
 *
 * @since 2.0
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Simone Giannecchini
 */
public final class ColorUtilities {
    /** Small number for rounding errors. */
    private static final double EPS = 1E-6;

    /** Do not allow creation of instances of this class. */
    private ColorUtilities() {}

    /**
     * Creates an sRGB color with the specified red, green, blue, and alpha values in the range (0 - 255).
     *
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @param a the alpha component
     * @throws IllegalArgumentException if {@coder}, {@code g}, {@code b} or {@code a} are outside of the range 0 to
     *     255, inclusive.
     */
    public static int getIntFromColor(int r, int g, int b, int a) {
        return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF) << 0;
    }

    /**
     * Returns a subarray of the specified color array. The {@code lower} and {@code upper} index will be clamb into the
     * {@code palette} range. If they are completly out of range, or if they would result in an empty array, then
     * {@code null} is returned.
     *
     * <p>This method is used by {@link org.geotools.cv.SampleDimension} as an heuristic approach for distributing
     * palette colors into a list of categories.
     *
     * @param palette The color array (may be {@code null}).
     * @param lower The lower index, inclusive.
     * @param upper The upper index, inclusive.
     * @return The subarray (may be {@code palette} if the original array already fit), or {@code null} if the
     *     {@code lower} and {@code upper} index are out of {@code palette} bounds.
     */
    public static Color[] subarray(final Color[] palette, int lower, int upper) {
        if (palette != null) {
            lower = Math.max(lower, 0);
            upper = Math.min(upper, palette.length);
            if (lower >= upper) {
                return null;
            }
            if (lower != 0 || upper != palette.length) {
                final Color[] sub = new Color[upper - lower];
                System.arraycopy(palette, lower, sub, 0, sub.length);
                return sub;
            }
        }
        return palette;
    }

    /**
     * Copies {@code colors} into array {@code ARGB} from index {@code lower} inclusive to index {@code upper}
     * exclusive. If {@code upper-lower} is not equals to the length of {@code colors} array, then colors will be
     * interpolated.
     *
     * <p><b>Note:</b> Profiling shows that this method is a "hot spot". It needs to be fast, which is why the
     * implementation is not as straight-forward as it could.
     *
     * @param colors Colors to copy into the {@code ARGB} array.
     * @param ARGB Array of integer to write ARGB values to.
     * @param lower Index (inclusive) of the first element of {@code ARGB} to change.
     * @param upper Index (exclusive) of the last element of {@code ARGB} to change.
     */
    @SuppressWarnings("fallthrough")
    public static void expand(final Color[] colors, final int[] ARGB, final int lower, final int upper) {
        /*
         * Trivial cases.
         */
        switch (colors.length) {
            case 1:
                Arrays.fill(ARGB, lower, upper, colors[0].getRGB()); // fall through
            case 0:
                return; // Note: getRGB() is really getARGB()
        }
        switch (upper - lower) {
            case 1:
                ARGB[lower] = colors[0].getRGB(); // fall through
            case 0:
                return; // Note: getRGB() is really getARGB()
        }
        /*
         * Prepares the coefficients for the iteration.
         * The non-final ones will be updated inside the loop.
         */
        final double scale = (colors.length - 1) / (double) (upper - 1 - lower);
        final int maxBase = colors.length - 2;
        double index = 0;
        int base = 0;
        for (int i = lower; ; ) {
            final int C0 = colors[base + 0].getRGB();
            final int C1 = colors[base + 1].getRGB();
            final int A0 = C0 >>> 24 & 0xFF, A1 = (C1 >>> 24 & 0xFF) - A0;
            final int R0 = C0 >>> 16 & 0xFF, R1 = (C1 >>> 16 & 0xFF) - R0;
            final int G0 = C0 >>> 8 & 0xFF, G1 = (C1 >>> 8 & 0xFF) - G0;
            final int B0 = C0 & 0xFF, B1 = (C1 & 0xFF) - B0;
            final int oldBase = base;
            do {
                final double delta = index - base;
                ARGB[i] = roundByte(A0 + delta * A1) << 24
                        | roundByte(R0 + delta * R1) << 16
                        | roundByte(G0 + delta * G1) << 8
                        | roundByte(B0 + delta * B1);
                if (++i == upper) {
                    return;
                }
                index = (i - lower) * scale;
                base = Math.min(maxBase, (int) (index + EPS)); // Really want rounding toward 0.
            } while (base == oldBase);
        }
    }

    /** Rounds a float value and clamp the result between 0 and 255 inclusive. */
    public static int roundByte(final double value) {
        return (int) Math.min(Math.max(Math.round(value), 0), 255);
    }

    /**
     * Returns an index color model for specified ARGB codes. If the specified array has not transparent color (i.e. all
     * alpha values are 255), then the returned color model will be opaque. Otherwise, if the specified array has one
     * and only one color with alpha value of 0, the returned color model will have only this transparent color.
     * Otherwise, the returned color model will be translucent.
     *
     * @param ARGB An array of ARGB values.
     * @return An index color model for the specified array.
     */
    public static IndexColorModel getIndexColorModel(final int[] ARGB) {
        return getIndexColorModel(ARGB, 1, 0);
    }

    /**
     * Returns a tolerant index color model for the specified ARGB code. This color model accept image with the
     * specified number of bands.
     *
     * @param ARGB An array of ARGB values.
     * @param numBands The number of bands.
     * @param visibleBand The band to display.
     * @return An index color model for the specified array.
     * @todo Considerer caching previously created instances using weak references. Index color model may be big (up to
     *     256 kb), so it may be worth to cache big instances. NOTE: IndexColorModel inherits a equals(Object)
     *     implementation from ColorModel, but do not override it, so the definition is incomplete.
     */
    public static IndexColorModel getIndexColorModel(final int[] ARGB, final int numBands, final int visibleBand) {
        boolean hasAlpha = false;
        int transparent = -1;
        final int length = ARGB.length;
        for (int i = 0; i < length; i++) {
            final int alpha = ARGB[i] & 0xFF000000;
            if (alpha != 0xFF000000) {
                if (alpha == 0x00000000 && transparent < 0) {
                    transparent = i;
                    continue;
                }
                hasAlpha = true;
                break;
            }
        }
        final int bits = getBitCount(length);
        final int type = getTransferType(length);
        if (numBands == 1) {
            return new IndexColorModel(bits, length, ARGB, 0, hasAlpha, transparent, type);
        } else {
            return new MultiBandsIndexColorModel(
                    bits, length, ARGB, 0, hasAlpha, transparent, type, numBands, visibleBand);
        }
    }

    /**
     * Returns a bit count for an {@link IndexColorModel} mapping {@code mapSize} colors. It is guaranteed that the
     * following relation is hold:
     *
     * <p><center>
     *
     * <pre>(1 << getBitCount(mapSize)) >= mapSize</pre>
     *
     * </center>
     */
    public static int getBitCount(final int mapSize) {
        int max = mapSize - 1;
        if (max <= 1) {
            return 1;
        }
        int count = 0;
        do {
            count++;
            max >>= 1;
        } while (max != 0);
        assert 1 << count >= mapSize : mapSize;
        assert 1 << count - 1 < mapSize : mapSize;
        return count;
    }

    /**
     * Returns a suggered type for an {@link IndexColorModel} of {@code mapSize} colors. This method returns
     * {@link DataBuffer#TYPE_BYTE} or {@link DataBuffer#TYPE_USHORT}.
     */
    public static int getTransferType(final int mapSize) {
        return mapSize <= 256 ? DataBuffer.TYPE_BYTE : DataBuffer.TYPE_USHORT;
    }

    /**
     * Transforms a color from XYZ color space to LAB. The color are transformed in place. This method returns
     * {@code color} for convenience. Reference: http://www.brucelindbloom.com/index.html?ColorDifferenceCalc.html
     */
    public static float[] XYZtoLAB(final float[] color) {
        color[0] /= 0.9642; // Other refeference: 0.95047;
        color[1] /= 1.0000; //                    1.00000;
        color[2] /= 0.8249; //                    1.08883;
        for (int i = 0; i < 3; i++) {
            final float c = color[i];
            color[i] = (float) (c > 216 / 24389f ? Math.pow(c, 1.0 / 3) : (24389 / 27.0 * c + 16) / 116);
        }
        final float L = 116 * color[1] - 16;
        final float a = 500 * (color[0] - color[1]);
        final float b = 200 * (color[1] - color[2]);
        assert !Float.isNaN(L) && !Float.isNaN(a) && !Float.isNaN(b);
        color[0] = L;
        color[1] = a;
        color[2] = b;
        return color;
    }

    /**
     * Computes the distance E (CIE 1994) between two colors in LAB color space. Reference:
     * http://www.brucelindbloom.com/index.html?ColorDifferenceCalc.html
     */
    public static float colorDistance(final float[] lab1, final float[] lab2) {
        //        if (false) {
        //            // Compute distance using CIE94 formula.
        //            // NOTE: this formula sometime fails because of negative
        //            //       value in the first Math.sqrt(...) expression.
        //            final double dL = (double)lab1[0] - lab2[0];
        //            final double da = (double)lab1[1] - lab2[1];
        //            final double db = (double)lab1[2] - lab2[2];
        //            final double C1 = Math.hypot(lab1[1], lab1[2]);
        //            final double C2 = Math.hypot(lab2[1], lab2[2]);
        //            final double dC = C1 - C2;
        //            final double dH = Math.sqrt(da*da + db*db - dC*dC);
        //            final double sL = dL / 2;
        //            final double sC = dC / (1 + 0.048*C1);
        //            final double sH = dH / (1 + 0.014*C1);
        //            return (float)Math.sqrt(sL*sL + sC*sC + sH*sH);
        //        } else {
        // Compute distance using delta E formula.
        double sum = 0;
        for (int i = Math.min(lab1.length, lab2.length); --i >= 0; ) {
            final double delta = lab1[i] - lab2[i];
            sum += delta * delta;
        }
        return (float) Math.sqrt(sum);
        //        }
    }

    /**
     * Returns the most transparent pixel in the specified color model. If many colors has the same alpha value, than
     * the darkest one is returned. This method never returns a negative value (0 is returned if the color model has no
     * colors).
     *
     * @param colors The color model in which to look for a transparent color.
     * @return The index of a transparent color, or 0.
     */
    public static int getTransparentPixel(final IndexColorModel colors) {
        int index = colors.getTransparentPixel();
        if (index < 0) {
            index = 0;
            int alpha = Integer.MAX_VALUE;
            float delta = Float.POSITIVE_INFINITY;
            final ColorSpace space = colors.getColorSpace();
            final float[] RGB = new float[3];
            final float[] BLACK = XYZtoLAB(space.toCIEXYZ(RGB)); // Black in Lab color space.
            assert BLACK != RGB;
            for (int i = colors.getMapSize(); --i >= 0; ) {
                final int a = colors.getAlpha(i);
                if (a <= alpha) {
                    RGB[0] = colors.getRed(i) / 255f;
                    RGB[1] = colors.getGreen(i) / 255f;
                    RGB[2] = colors.getBlue(i) / 255f;
                    final float d = colorDistance(XYZtoLAB(space.toCIEXYZ(RGB)), BLACK);
                    assert d >= 0 : i; // Check mostly for NaN value
                    if (a < alpha || d < delta) {
                        alpha = a;
                        delta = d;
                        index = i;
                    }
                }
            }
        }
        return index;
    }

    /**
     * Returns the index of the specified color, excluding the specified one. If the color is not explicitly found, a
     * close color is returned. This method never returns a negative value (0 is returned if the color model has no
     * colors).
     *
     * @param colors The color model in which to look for a color index.
     * @param color The color to search for.
     * @param exclude An index to exclude from the search (usually the background or the
     *     {@linkplain #getTransparentPixel transparent} pixel), or -1 if none.
     * @return The index of the color, or 0.
     */
    public static int getColorIndex(final IndexColorModel colors, final Color color, final int exclude) {
        final ColorSpace space = colors.getColorSpace();
        final float[] RGB = {color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f};
        final float[] REF = XYZtoLAB(space.toCIEXYZ(RGB));
        float delta = Float.POSITIVE_INFINITY;
        int index = 0;
        assert REF != RGB;
        for (int i = colors.getMapSize(); --i >= 0; ) {
            if (i != exclude) {
                RGB[0] = colors.getRed(i) / 255f;
                RGB[1] = colors.getGreen(i) / 255f;
                RGB[2] = colors.getBlue(i) / 255f;
                final float d = colorDistance(XYZtoLAB(space.toCIEXYZ(RGB)), REF);
                assert d >= 0 : i; // Check mostly for NaN value
                if (d <= delta) {
                    delta = d;
                    index = i;
                }
            }
        }
        return index;
    }

    /**
     * Tries to guess the number of bands from the specified color model. The recommanded approach is to invoke
     * {@link java.awt.image.SampleModel#getNumBands}. This method should be used only as a fallback when the sample
     * model is not available. This method uses some heuristic rules for guessing the number of bands, so the return
     * value may not be exact in all cases.
     */
    public static int getNumBands(final ColorModel model) {
        if (model instanceof IndexColorModel) {
            if (model instanceof MultiBandsIndexColorModel) {
                return ((MultiBandsIndexColorModel) model).numBands;
            }
            return 1;
        }
        return model.getNumComponents();
    }

    /**
     * Tells us if a specific {@link IndexColorModel} contains only gray color or not, ignoring alpha information.
     *
     * @param icm {@link IndexColorModel} to be inspected.
     * @param ignoreTransparents {@code true} if the RGB values of fully transparent pixels (the ones with an
     *     {@linkplain IndexColorModel#getAlpha(int) alpha} value of 0) should not be taken in account during the check
     *     for gray color.
     * @return {@code true} if the palette is grayscale, {@code false} otherwise.
     */
    public static boolean isGrayPalette(final IndexColorModel icm, boolean ignoreTransparents) {
        if (!icm.hasAlpha()) {
            // We will not check transparent pixels if there is none in the color model.
            ignoreTransparents = false;
        }
        final int mapSize = icm.getMapSize();
        for (int i = 0; i < mapSize; i++) {
            if (ignoreTransparents) {
                // If this entry is transparent and we were asked
                // to check transparents pixels, let's leave.
                if (icm.getAlpha(i) == 0) {
                    continue;
                }
            }
            // Get the color for this pixel only if it is requested.
            // If gray, all components are the same.
            final int green = icm.getGreen(i);
            if (green != icm.getRed(i) || green != icm.getBlue(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Provide the minimum allowe value for a certain data type.
     *
     * @param dataType the data type to suggest a maximum value for.
     * @return the data type maximum value for.
     */
    public static double getMinimum(int dataType) {
        switch (dataType) {
            case DataBuffer.TYPE_BYTE:
            case DataBuffer.TYPE_USHORT:
                return 0;
            case DataBuffer.TYPE_SHORT:
                return Short.MIN_VALUE;
            case DataBuffer.TYPE_INT:
                return Integer.MIN_VALUE;
            case DataBuffer.TYPE_DOUBLE:
                return Long.MIN_VALUE;
            case DataBuffer.TYPE_FLOAT:
                return -Float.MAX_VALUE;
            default:
                throw new IllegalArgumentException(
                        MessageFormat.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "DataType unknown:", dataType));
        }
    }

    /**
     * Returns a suitable threshold depending on the {@link DataBuffer} type.
     *
     * <p>Remember that the threshold works with >=.
     *
     * @param dataType to create a low threshold for.
     * @return a minimum threshold value suitable for this data type.
     */
    public static double getThreshold(int dataType) {
        switch (dataType) {
            case DataBuffer.TYPE_BYTE:
            case DataBuffer.TYPE_USHORT:
                // this may cause problems and truncations when the native mosaic
                // operations is enabled
                return 0.0;
            case DataBuffer.TYPE_INT:
                return Integer.MIN_VALUE;
            case DataBuffer.TYPE_SHORT:
                return Short.MIN_VALUE;
            case DataBuffer.TYPE_DOUBLE:
                return -Double.MAX_VALUE;
            case DataBuffer.TYPE_FLOAT:
                return -Float.MAX_VALUE;
        }
        return 0;
    }

    /**
     * Looks for the specified color in the color model
     *
     * @param bgColor The color to be searched
     * @param icm The color model to be searched into
     * @return The index of the color in the color model, or -1 if not found
     */
    public static int findColorIndex(Color bgColor, IndexColorModel icm) {
        if (bgColor == null)
            throw new NullPointerException(MessageFormat.format(ErrorKeys.NULL_ARGUMENT_$1, "bgColor"));
        if (icm == null) throw new NullPointerException(MessageFormat.format(ErrorKeys.NULL_ARGUMENT_$1, "icm"));

        final int r = bgColor.getRed();
        final int g = bgColor.getGreen();
        final int b = bgColor.getBlue();
        final int a = bgColor.getAlpha();
        final int size = icm.getMapSize();

        for (int i = 0; i < size; i++) {
            if (r == icm.getRed(i)
                    && g == icm.getGreen(i)
                    && b == icm.getBlue(i)
                    && (a == icm.getAlpha(i) || !icm.hasAlpha())) return i;
        }
        return -1;
    }

    /**
     * Applies the specified background color to an index color model, building a new index color model (or returning
     * the same index color mode if it has no transparency at all)
     */
    public static IndexColorModel applyBackgroundColor(IndexColorModel icm, Color bg) {
        int trasparentIdx = icm.getTransparentPixel();
        if (icm.getTransparency() == Transparency.OPAQUE && trasparentIdx == -1) {
            // no transparency at all
            return icm;
        }

        // grab the components
        int size = icm.getMapSize();
        byte[] reds = new byte[size];
        byte[] greens = new byte[size];
        byte[] blues = new byte[size];
        icm.getReds(reds);
        icm.getGreens(greens);
        icm.getBlues(blues);

        // single transparent pixel? replace it
        if (icm.getTransparency() == Transparency.OPAQUE && trasparentIdx != -1) {
            reds[trasparentIdx] = (byte) bg.getRed();
            greens[trasparentIdx] = (byte) bg.getGreen();
            blues[trasparentIdx] = (byte) bg.getBlue();
            return new IndexColorModel(icm.getPixelSize(), size, reds, greens, blues);
        } else {
            // grab the alpha and do the merge
            byte[] alphas = new byte[size];
            icm.getAlphas(alphas);

            int r = bg.getRed() & 0xFF;
            int g = bg.getGreen() & 0xFF;
            int b = bg.getBlue() & 0xFF;
            int a = bg.getAlpha() & 0xFF;
            for (int i = 0; i < size; i++) {
                int t1 = alphas[i] & 0xFF; // a1
                float t2 = (a & 0xFF) * (1.0f - t1 / 255f); // a2 * (1 - a1 / maxValue)
                float t3 = t1 + t2; // output alpha
                float t4, t5;
                if (t3 == 0.0F) {
                    t4 = 0.0F;
                    t5 = 0.0F;
                } else {
                    t4 = t1 / t3;
                    t5 = 1.0f - t4;
                }

                int ri = reds[i] & 0xFF;
                reds[i] = (byte) (ri * t4 + r * t5);
                int gi = greens[i] & 0xFF;
                greens[i] = (byte) (gi * t4 + g * t5);
                int bi = blues[i] & 0xFF;
                blues[i] = (byte) (bi * t4 + b * t5);
                alphas[i] = (byte) t3;
            }

            // if the bg color had transparency we still have a translucent image, otherwise
            // an opaque one
            if (a < 255) {
                return new IndexColorModel(icm.getPixelSize(), size, reds, greens, blues, alphas);
            } else {
                return new IndexColorModel(icm.getPixelSize(), size, reds, greens, blues);
            }
        }
    }

    /**
     * Provide the maximum allowe value for a certain data type.
     *
     * @param dataType the data type to suggest a maximum value for.
     * @return the data type maximum value for.
     */
    public static double getMaximum(int dataType) {
        switch (dataType) {
            case DataBuffer.TYPE_BYTE:
                return 255;
            case DataBuffer.TYPE_SHORT:
                return Short.MAX_VALUE;
            case DataBuffer.TYPE_USHORT:
                return 65535;
            case DataBuffer.TYPE_INT:
                return Integer.MAX_VALUE;
            case DataBuffer.TYPE_DOUBLE:
                return Long.MAX_VALUE;
            case DataBuffer.TYPE_FLOAT:
                return Float.MAX_VALUE;

            default:
                throw new IllegalArgumentException(
                        MessageFormat.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "DataType unknown:", dataType));
        }
    }

    public static final ComponentColorModel GRAY_CM = new ComponentColorModel(
            ColorSpace.getInstance(ColorSpace.CS_GRAY), false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);

    public static final ComponentColorModel GRAY_ALPHA_CM = new ComponentColorModel(
            ColorSpace.getInstance(ColorSpace.CS_GRAY), true, false, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);

    public static final ComponentColorModel RGB_CM = new ComponentColorModel(
            ColorSpace.getInstance(ColorSpace.CS_sRGB), false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);

    public static final ComponentColorModel RGB_ALPHA_CM = new ComponentColorModel(
            ColorSpace.getInstance(ColorSpace.CS_sRGB), true, false, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);
}
