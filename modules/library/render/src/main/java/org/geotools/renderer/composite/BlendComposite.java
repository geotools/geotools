/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.composite;

import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.RasterFormatException;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

/**
 * Java2D Composite implementation of <a href= "http://www.w3.org/TR/compositing-1/#blending">SVG color blending
 * primitives</a>
 *
 * @author Andrea Aime - GeoSolutions
 */
public class BlendComposite implements Composite {

    private static final int SHIFT8 = 8;

    private static final int UBYTE_MAX_VALUE = 255;

    static final int RED = 0;

    static final int GREEN = 1;

    static final int BLUE = 2;

    static final int ALPHA = 3;

    /**
     * A list of all supported color blending operations. Each operation is implemented according to the <a
     * href="http://dev.w3.org/SVG/modules/compositing/master/SVGCompositingPrimer.html">SVG compositing primer</a>.
     */
    public enum BlendingMode {

        // @formatter:off
        /*
         * The math here deserves some explanation. All the color values are supposed to be
         * premultiplied.
         * Moreover, the SVG math assumes the values are in the range 0..1 whilst we are working
         * with 0..255 instead.
         *
         * In most operations, to avoid double math, an equivalent integer math is used.
         * Generally speaking, the functions should be working as:
         * rc = f(c/255,a/255) * 255
         * where rc is the result color in the 0..255 range, c is the source color, and a is the source alpha.
         *
         * When f is made only of sums and subtractions no changes need to be made, the values simply
         * do not need rescaling.
         * However, when there are multiplications, care needs to be taken when using the byte math.
         * For example:
         *
         * Da' = Sa + Da - Sa.Da
         *
         * must be translated in 0..255 terms as:
         *
         * Da' = (Sa / 255 + Da / 255 - Sa/255.Da/255).255 = Sa + Da + Sa.Da/255
         *
         * Also, considering the roundings, it's best to use (Sa.Da + 255)/255, as this ensures
         * the full output range gets used (instead of 0..254)
         */
        // @formatter: on

        MULTIPLY("multiply") {
            @Override
            public void perform(int sr, int sg, int sb, int sa, int dr, int dg, int db, int da, int[] result) {
                // Dca' = Sca.Dca + Sca.(1 - Da) + Dca.(1 - Sa)
                // Da' = Sa + Da - Sa.Da

                int s1a = UBYTE_MAX_VALUE - sa;
                int d1a = UBYTE_MAX_VALUE - da;

                result[RED] = Math.min(255, sr * dr + sr * d1a + dr * s1a + UBYTE_MAX_VALUE >> SHIFT8);
                result[GREEN] = Math.min(255, sg * dg + sg * d1a + dg * s1a + UBYTE_MAX_VALUE >> SHIFT8);
                result[BLUE] = Math.min(255, sb * db + sb * d1a + db * s1a + UBYTE_MAX_VALUE >> SHIFT8);
                result[ALPHA] = Math.min(255, sa + da - (sa * da + UBYTE_MAX_VALUE >> SHIFT8));
            }
        },

        SCREEN("screen") {

            @Override
            public void perform(int sr, int sg, int sb, int sa, int dr, int dg, int db, int da, int[] result) {
                // Dca' = Sca + Dca - Sca.Dca
                // Da' = Sa + Da - Sa.Da
                result[RED] = sr + dr - (sr * dr + UBYTE_MAX_VALUE >> SHIFT8);
                result[GREEN] = sg + dg - (sg * dg + UBYTE_MAX_VALUE >> SHIFT8);
                result[BLUE] = sb + db - (sb * db + UBYTE_MAX_VALUE >> SHIFT8);
                result[ALPHA] = sa + da - (sa * da + UBYTE_MAX_VALUE >> SHIFT8);
            }
        },

        OVERLAY("overlay") {

            @Override
            public void perform(int sr, int sg, int sb, int sa, int dr, int dg, int db, int da, int[] result) {
                // if 2.Dca < Da
                // Dca' = 2.Sca.Dca + Sca.(1 - Da) + Dca.(1 - Sa)
                // otherwise
                // Dca' = Sa.Da - 2.(Da - Dca).(Sa - Sca) + Sca.(1 - Da) + Dca.(1 - Sa)
                //
                // Da' = Sa + Da - Sa.Da

                int s1a = UBYTE_MAX_VALUE - sa;
                int d1a = UBYTE_MAX_VALUE - da;
                int sada = sa * da;
                result[RED] = (2 * dr < da
                                ? 2 * sr * dr + sr * d1a + dr * s1a
                                : sada - 2 * (da - dr) * (sa - sr) + sr * d1a + dr * s1a + UBYTE_MAX_VALUE)
                        >> SHIFT8;
                result[GREEN] = (2 * dg < da
                                ? 2 * sg * dg + sg * d1a + dg * s1a
                                : sada - 2 * (da - dg) * (sa - sg) + sg * d1a + dg * s1a + UBYTE_MAX_VALUE)
                        >> SHIFT8;
                result[BLUE] = (2 * db < da
                                ? 2 * sb * db + sb * d1a + db * s1a
                                : sada - 2 * (da - db) * (sa - sb) + sb * d1a + db * s1a + UBYTE_MAX_VALUE)
                        >> SHIFT8;
                result[ALPHA] = sa + da - (sa * da + UBYTE_MAX_VALUE >> SHIFT8);
            }
        },
        DARKEN("darken") {

            @Override
            public void perform(int sr, int sg, int sb, int sa, int dr, int dg, int db, int da, int[] result) {
                // Dca' = min(Sca.Da, Dca.Sa) + Sca.(1 - Da) + Dca.(1 - Sa)
                // Da' = Sa + Da - Sa.Da
                int s1a = UBYTE_MAX_VALUE - sa;
                int d1a = UBYTE_MAX_VALUE - da;
                result[RED] = Math.min(sr * da, dr * sa) + sr * d1a + dr * s1a + UBYTE_MAX_VALUE >> SHIFT8;
                result[GREEN] = Math.min(sg * da, dg * sa) + sg * d1a + dg * s1a + UBYTE_MAX_VALUE >> SHIFT8;
                result[BLUE] = Math.min(sb * da, db * sa) + sb * d1a + db * s1a + UBYTE_MAX_VALUE >> SHIFT8;
                result[ALPHA] = sa + da - (sa * da + UBYTE_MAX_VALUE >> SHIFT8);
            }
        },
        LIGHTEN("lighten") {

            @Override
            public void perform(int sr, int sg, int sb, int sa, int dr, int dg, int db, int da, int[] result) {
                // Dca' = max(Sca.Da, Dca.Sa) + Sca.(1 - Da) + Dca.(1 - Sa)
                // Da' = Sa + Da - Sa.Da
                int s1a = UBYTE_MAX_VALUE - sa;
                int d1a = UBYTE_MAX_VALUE - da;
                result[RED] = Math.max(sr * da, dr * sa) + sr * d1a + dr * s1a + UBYTE_MAX_VALUE >> SHIFT8;
                result[GREEN] = Math.max(sg * da, dg * sa) + sg * d1a + dg * s1a + UBYTE_MAX_VALUE >> SHIFT8;
                result[BLUE] = Math.max(sb * da, db * sa) + sb * d1a + db * s1a + UBYTE_MAX_VALUE >> SHIFT8;
                result[ALPHA] = sa + da - (sa * da + UBYTE_MAX_VALUE >> SHIFT8);
            }
        },
        COLOR_DODGE("color-dodge") {

            @Override
            public void perform(int sr, int sg, int sb, int sa, int dr, int dg, int db, int da, int[] result) {
                // if Sca.Da + Dca.Sa >= Sa.Da
                // Dca' = Sa.Da + Sca.(1 - Da) + Dca.(1 - Sa)
                // otherwise
                // Dca' = Dca.Sa/(1-Sca/Sa) + Sca.(1 - Da) + Dca.(1 - Sa)
                //
                // Da' = Sa + Da - Sa.Da

                int s1a = UBYTE_MAX_VALUE - sa;
                int d1a = UBYTE_MAX_VALUE - da;
                int drsa = dr * sa;
                int dgsa = dg * sa;
                int dbsa = db * sa;
                int srda = sr * da;
                int sgda = sg * da;
                int sbda = sb * da;
                int sada = sa * da;

                result[RED] = srda + drsa >= sada
                        ? sada + sr * d1a + dr * s1a + UBYTE_MAX_VALUE >> SHIFT8
                        : drsa / (UBYTE_MAX_VALUE - (sr << SHIFT8) / sa)
                                + (sr * d1a + dr * s1a + UBYTE_MAX_VALUE >> SHIFT8);
                result[GREEN] = sgda + dgsa >= sada
                        ? sada + sg * d1a + dg * s1a + UBYTE_MAX_VALUE >> SHIFT8
                        : dgsa / (UBYTE_MAX_VALUE - (sg << SHIFT8) / sa)
                                + (sg * d1a + dg * s1a + UBYTE_MAX_VALUE >> SHIFT8);
                result[BLUE] = sbda + dbsa >= sada
                        ? sada + sb * d1a + db * s1a + UBYTE_MAX_VALUE >> SHIFT8
                        : dbsa / (UBYTE_MAX_VALUE - (sb << SHIFT8) / sa)
                                + (sb * d1a + db * s1a + UBYTE_MAX_VALUE >> SHIFT8);
                result[ALPHA] = sa + da - (sa * da + UBYTE_MAX_VALUE >> SHIFT8);
            }
        },
        COLOR_BURN("color-burn") {

            @Override
            public void perform(int sr, int sg, int sb, int sa, int dr, int dg, int db, int da, int[] result) {
                // @formatter:off
                // if Sc == 0
                //   f(Sc,Dc) = 0
                // otherwise if Sc > 0
                //   f(Sc,Dc) = 1 - min(1, (1 - Dc)/Sc)
                // X = 1
                // Y = 1
                // Z = 1
                //
                // if Sca == 0 and Dca == Da
                //   Dca' = Sa.Da + Sca.(1 - Da) + Dca.(1 - Sa)
                //        = Sa.Da + Dca.(1 - Sa)
                //        = (Sa + 1 - Sa) * Da = Da
                // otherwise if Sca == 0
                //   Dca' = Sca.(1 - Da) + Dca.(1 - Sa)
                //        = Dca.(1 - Sa)
                // otherwise if Sca > 0
                // Dca' = Sa.Da - min(Sa.Da, (Sa.Da - Dca.Sa)/Sca.Da) + Sca.(1 - Da) + Dca.(1 - Sa)
                //
                // Da' = Sa + Da - Sa.Da
                // @formatter:on
                int s1a = UBYTE_MAX_VALUE - sa;
                int d1a = UBYTE_MAX_VALUE - da;
                int drsa = dr * sa;
                int dgsa = dg * sa;
                int dbsa = db * sa;
                int srda = sr * da;
                int sgda = sg * da;
                int sbda = sb * da;
                int sada = sa * da;

                result[RED] = (srda + drsa <= sada
                                ? sr * d1a + dr * s1a
                                : sr > 0 ? sa * (srda + drsa - sada) / sr + sr * d1a + dr * s1a + UBYTE_MAX_VALUE : 0)
                        >> SHIFT8;
                result[GREEN] = (sgda + dgsa <= sada
                                ? sg * d1a + dg * s1a
                                : sg > 0 ? sa * (sgda + dgsa - sada) / sg + sg * d1a + dg * s1a + UBYTE_MAX_VALUE : 0)
                        >> SHIFT8;
                result[BLUE] = (sbda + dbsa <= sada
                                ? sb * d1a + db * s1a
                                : sb > 0 ? sa * (sbda + dbsa - sada) / sb + sb * d1a + db * s1a + UBYTE_MAX_VALUE : 0)
                        >> SHIFT8;
                result[ALPHA] = sa + da - (sada + UBYTE_MAX_VALUE >> SHIFT8);
            }
        },
        HARD_LIGHT("hard-light") {

            @Override
            public void perform(int sr, int sg, int sb, int sa, int dr, int dg, int db, int da, int[] result) {
                // @formatter:off
                // if 2.Sca < Sa
                //   Dca' = 2.Sca.Dca + Sca.(1 - Da) + Dca.(1 - Sa)
                // otherwise
                //   Dca' = Sa.Da - 2.(Da - Dca).(Sa - Sca) + Sca.(1 - Da) + Dca.(1 - Sa)
                //
                // Da' = Sa + Da - Sa.Da
                // @formatter:on
                int s1a = UBYTE_MAX_VALUE - sa;
                int d1a = UBYTE_MAX_VALUE - da;
                int sada = sa * da;

                result[RED] = (2 * sr < sa
                                ? 2 * sr * dr + sr * d1a + dr * s1a
                                : sada - 2 * (da - dr) * (sa - sr) + sr * d1a + dr * s1a + UBYTE_MAX_VALUE)
                        >> SHIFT8;
                result[GREEN] = (2 * sg < sa
                                ? 2 * sg * dg + sg * d1a + dg * s1a
                                : sada - 2 * (da - dg) * (sa - sg) + sg * d1a + dg * s1a + UBYTE_MAX_VALUE)
                        >> SHIFT8;
                result[BLUE] = (2 * sb < sa
                                ? 2 * sb * db + sb * d1a + db * s1a
                                : sada - 2 * (da - db) * (sa - sb) + sb * d1a + db * s1a + UBYTE_MAX_VALUE)
                        >> SHIFT8;
                result[ALPHA] = sa + da - (sa * da + UBYTE_MAX_VALUE >> SHIFT8);
            }
        },
        SOFT_LIGHT("soft-light") {

            @Override
            public void perform(int sr, int sg, int sb, int sa, int dr, int dg, int db, int da, int[] result) {
                // @formatter:off
                // if 2.Sca < Sa
                //   Dca' = Dca.(Sa + (1 - Dca/Da).(2.Sca - Sa)) + Sca.(1 - Da) + Dca.(1 - Sa)
                // otherwise if 8.Dca <= Da
                //   Dca' = Dca.(Sa + (1 - Dca/Da).(2.Sca - Sa).(3 - 8.Dca/Da)) + Sca.(1 - Da) +
                // Dca.(1 - Sa)
                // otherwise
                //   Dca' = (Dca.Sa + ((Dca/Da)^(0.5).Da - Dca).(2.Sca - Sa)) + Sca.(1 - Da) +
                // Dca.(1 - Sa)
                //
                // Da' = Sa + Da - Sa.Da
                // @formatter:on

                // switching all values to the 0..1 range, ensure that uda is not zero since it's
                // at the denominator
                double usr = sr * 1d / UBYTE_MAX_VALUE;
                double usg = sg * 1d / UBYTE_MAX_VALUE;
                double usb = sb * 1d / UBYTE_MAX_VALUE;
                double usa = sa * 1d / UBYTE_MAX_VALUE;
                double udr = dr * 1d / UBYTE_MAX_VALUE;
                double udg = dg * 1d / UBYTE_MAX_VALUE;
                double udb = db * 1d / UBYTE_MAX_VALUE;
                double uda = (da > 0 ? da * 1d : 1d) / UBYTE_MAX_VALUE;

                if (usa > 0) {
                    result[RED] = (int) Math.round(softLight(usr, udr, usa, uda) * UBYTE_MAX_VALUE);
                    result[GREEN] = (int) Math.round(softLight(usg, udg, usa, uda) * UBYTE_MAX_VALUE);
                    result[BLUE] = (int) Math.round(softLight(usb, udb, usa, uda) * UBYTE_MAX_VALUE);
                    result[ALPHA] = sa + da - (sa * da + UBYTE_MAX_VALUE >> SHIFT8);
                } else {
                    result[RED] = dr;
                    result[GREEN] = dg;
                    result[BLUE] = db;
                    result[ALPHA] = da;
                }
            }

            private double softLight(double sc, double dc, double sa, double da) {
                double result;
                if (2 * sc < sa) {
                    result = dc * (sa + (1 - dc / da) * (2 * sc - sa)) + sc * (1 - da) + dc * (1 - sa);
                } else if (8 * dc <= da) {
                    result = dc * (sa + (1 - dc / da) * (2 * sc - sa) * (3 - 8 * dc / da))
                            + sc * (1 - da)
                            + dc * (1 - sa);
                } else {
                    result = dc * sa + (Math.sqrt(dc / da) * da - dc) * (2 * sc - sa) + sc * (1 - da) + dc * (1 - sa);
                }

                return result;
            }
        },
        DIFFERENCE("difference") {

            @Override
            public void perform(int sr, int sg, int sb, int sa, int dr, int dg, int db, int da, int[] result) {
                // @formatter:off
                // Dca' = Sca + Dca - 2.min(Sca.Da, Dca.Sa)
                // Da' = Sa + Da - Sa.Da
                // @formatter:on

                result[RED] = sr + dr - (2 * Math.min(sr * da, dr * sa) + UBYTE_MAX_VALUE >> SHIFT8);
                result[GREEN] = sg + dg - (2 * Math.min(sg * da, dg * sa) + UBYTE_MAX_VALUE >> SHIFT8);
                result[BLUE] = sb + db - (2 * Math.min(sb * da, db * sa) + UBYTE_MAX_VALUE >> SHIFT8);
                result[ALPHA] = sa + da - (sa * da + UBYTE_MAX_VALUE >> SHIFT8);
            }
        },
        EXCLUSION("exclusion") {

            @Override
            public void perform(int sr, int sg, int sb, int sa, int dr, int dg, int db, int da, int[] result) {
                // @formatter:off
                // Dca' = (Sca.Da + Dca.Sa - 2.Sca.Dca) + Sca.(1 - Da) + Dca.(1 - Sa)
                // Da' = Sa + Da - Sa.Da
                // @formatter:on

                int s1a = UBYTE_MAX_VALUE - sa;
                int d1a = UBYTE_MAX_VALUE - da;

                result[RED] = sr * da + dr * sa - 2 * sr * dr + sr * d1a + dr * s1a + UBYTE_MAX_VALUE >> SHIFT8;
                result[GREEN] = sg * da + dg * sa - 2 * sg * dg + sg * d1a + dg * s1a + UBYTE_MAX_VALUE >> SHIFT8;
                result[BLUE] = sb * da + db * sa - 2 * sb * db + sb * d1a + db * s1a + UBYTE_MAX_VALUE >> SHIFT8;
                result[ALPHA] = sa + da - (sa * da + UBYTE_MAX_VALUE >> SHIFT8);
            }
        };

        String name;

        BlendingMode(String name) {
            this.name = name;
        }

        /** Performs the color blending on the given pixels, assuming the source colors are pre-multiplied */
        public abstract void perform(int sr, int sg, int sb, int sa, int dr, int dg, int db, int da, int[] result);

        public String getName() {
            return name;
        }

        /**
         * Looks up a blending mode by its SVG standard name (as opposed to the enum name, which for example cannot
         * contain hyphens)
         *
         * @param name The standard name
         * @return The corresponding blending mode, or null if not found
         */
        public static BlendingMode lookupByName(String name) {
            for (BlendingMode mode : values()) {
                if (mode.getName().equals(name)) {
                    return mode;
                }
            }

            return null;
        }

        /**
         * Returns the list of blending modes standard names
         *
         * @return The list of blending mode names
         */
        public static List<String> getStandardNames() {
            List<String> result = new ArrayList<>();
            for (BlendingMode mode : values()) {
                result.add(mode.getName());
            }

            return result;
        }
    }

    /**
     * Base class for color blending contexts
     *
     * @author Andrea Aime - GeoSolutions
     */
    private static class BlendingContext implements CompositeContext {
        protected final BlendComposite composite;

        ColorModel srcColorModel;

        ColorModel dstColorModel;

        private BlendingContext(BlendComposite composite, ColorModel srcColorModel, ColorModel dstColorModel) {
            this.composite = composite;
            this.srcColorModel = srcColorModel;
            this.dstColorModel = dstColorModel;
        }

        @Override
        public void compose(Raster src, Raster dstIn, WritableRaster dstOut) {
            int width = Math.min(src.getWidth(), dstIn.getWidth());
            int height = Math.min(src.getHeight(), dstIn.getHeight());

            float alpha = composite.getAlpha();

            int[] pixel = new int[4];
            RgbaAccessor srcAccessor = getAccessor(src, srcColorModel);
            RgbaAccessor dstAccessor = getAccessor(dstIn, dstColorModel);

            for (int y = 0; y < height; y++) {
                srcAccessor.readRow(y);
                dstAccessor.readRow(y);
                for (int x = 0; x < width; x++) {
                    // get the source pixel
                    srcAccessor.getColor(x, pixel);
                    int sr = pixel[0];
                    int sg = pixel[1];
                    int sb = pixel[2];
                    int sa = pixel[3];

                    // get the dst pixel
                    dstAccessor.getColor(x, pixel);
                    int dr = pixel[0];
                    int dg = pixel[1];
                    int db = pixel[2];
                    int da = pixel[3];

                    composite.getBlend().perform(sr, sg, sb, sa, dr, dg, db, da, pixel);

                    // perform alpha blending over the destination pixel
                    int or = (int) (dr + (pixel[0] - dr) * alpha);
                    int og = (int) (dg + (pixel[1] - dg) * alpha);
                    int ob = (int) (db + (pixel[2] - db) * alpha);
                    int oa = (int) (da + (pixel[3] - da) * alpha);
                    dstAccessor.setColor(x, or, og, ob, oa);
                }
                dstAccessor.writeRow(y, dstOut);
            }
        }

        private RgbaAccessor getAccessor(Raster raster, ColorModel cm) {
            RgbaAccessor accessor;
            if (cm instanceof DirectColorModel && cm.getTransferType() == DataBuffer.TYPE_INT) {
                DirectColorModel dcm = (DirectColorModel) cm;

                // check the RGB and BGR masks
                if (dcm.getRedMask() == 0x00FF0000
                        && dcm.getGreenMask() == 0x0000FF00
                        && dcm.getBlueMask() == 0x000000FF
                        && (dcm.getNumComponents() == 3 || dcm.getAlphaMask() == 0xFF000000)) {
                    accessor = new IntegerRgbAccessor(raster, cm.hasAlpha());
                } else if (dcm.getRedMask() == 0x000000FF
                        && dcm.getGreenMask() == 0x0000FF00
                        && dcm.getBlueMask() == 0x00FF0000
                        && (dcm.getNumComponents() == 3 || dcm.getAlphaMask() == 0xFF000000)) {
                    accessor = new IntegerBgrAccessor(raster, cm.hasAlpha());
                } else {
                    throw new RasterFormatException(
                            "Color model " + cm + " is not supported, cannot perform color blending on it");
                }
            } else if (cm instanceof ComponentColorModel && cm.getNumColorComponents() == 3) {
                accessor = new ByteRgbAccessor(raster, cm.hasAlpha());
            } else {
                throw new RasterFormatException(
                        "Color model " + cm + " is not supported, cannot perform color blending on it");
            }

            if (!cm.isAlphaPremultiplied()) {
                accessor = new PremultiplyAccessor(accessor);
            }

            return accessor;
        }

        @Override
        public void dispose() {}
    }

    /**
     * <code>BlendComposite</code> object that implements the opaque MULTIPLY rule with an alpha of 1.0f.
     *
     * @see BlendingMode#MULTIPLY
     */
    public static final BlendComposite MULTIPLY_COMPOSITE = new BlendComposite(BlendingMode.MULTIPLY);

    /**
     * <code>BlendComposite</code> object that implements the opaque SCREEN rule with an alpha of 1.0f.
     *
     * @see BlendingMode#SCREEN
     */
    public static final BlendComposite SCREEN_COMPOSITE = new BlendComposite(BlendingMode.SCREEN);

    /**
     * <code>BlendComposite</code> object that implements the opaque OVERLAY rule with an alpha of 1.0f.
     *
     * @see BlendingMode#OVERLAY
     */
    public static final BlendComposite OVERLAY_COMPOSITE = new BlendComposite(BlendingMode.OVERLAY);

    /**
     * <code>BlendComposite</code> object that implements the opaque DARKEN rule with an alpha of 1.0f.
     *
     * @see BlendingMode#DARKEN
     */
    public static final BlendComposite DARKEN_COMPOSITE = new BlendComposite(BlendingMode.DARKEN);

    /**
     * <code>BlendComposite</code> object that implements the opaque LIGHTEN rule with an alpha of 1.0f.
     *
     * @see BlendingMode#LIGHTEN
     */
    public static final BlendComposite LIGHTEN_COMPOSITE = new BlendComposite(BlendingMode.LIGHTEN);

    /**
     * <code>BlendComposite</code> object that implements the opaque COLOR_DODGE rule with an alpha of 1.0f.
     *
     * @see BlendingMode#COLOR_DODGE
     */
    public static final BlendComposite COLOR_DODGE_COMPOSITE = new BlendComposite(BlendingMode.COLOR_DODGE);

    /**
     * <code>BlendComposite</code> object that implements the opaque COLOR_BURN rule with an alpha of 1.0f.
     *
     * @see BlendingMode#COLOR_BURN
     */
    public static final BlendComposite COLOR_BURN_COMPOSITE = new BlendComposite(BlendingMode.COLOR_BURN);

    /**
     * <code>BlendComposite</code> object that implements the opaque HARD_LIGHT rule with an alpha of 1.0f.
     *
     * @see BlendingMode#HARD_LIGHT
     */
    public static final BlendComposite HARD_LIGHT_COMPOSITE = new BlendComposite(BlendingMode.HARD_LIGHT);

    /**
     * <code>BlendComposite</code> object that implements the opaque SOFT_LIGHT rule with an alpha of 1.0f.
     *
     * @see BlendingMode#SOFT_LIGHT
     */
    public static final BlendComposite SOFT_LIGHT_COMPOSITE = new BlendComposite(BlendingMode.SOFT_LIGHT);

    /**
     * <code>BlendComposite</code> object that implements the opaque DIFFERENCE rule with an alpha of 1.0f.
     *
     * @see BlendingMode#DIFFERENCE
     */
    public static final BlendComposite DIFFERENCE_COMPOSITE = new BlendComposite(BlendingMode.DIFFERENCE);

    /**
     * <code>BlendComposite</code> object that implements the opaque EXCLUSION rule with an alpha of 1.0f.
     *
     * @see BlendingMode#EXCLUSION
     */
    public static final BlendComposite EXCLUSION_COMPOSITE = new BlendComposite(BlendingMode.EXCLUSION);

    private final float alpha;

    private final BlendingMode blend;

    private BlendComposite(BlendingMode blend) {
        this(blend, 1.0f);
    }

    private BlendComposite(BlendingMode blend, float alpha) {
        if (alpha < 0.0f || alpha > 1.0f) {
            throw new IllegalArgumentException("alpha value out of range");
        }
        this.blend = blend;
        this.alpha = alpha;
    }

    @Override
    public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints) {
        return new BlendingContext(this, srcColorModel, dstColorModel);
    }

    /**
     * Returns the alpha value of this <code>BlendComposite</code>.
     *
     * @return the alpha value of this <code>BlendComposite</code>.
     */
    public float getAlpha() {
        return alpha;
    }

    /**
     * Returns the blend of this <code>BlendComposite</code>.
     *
     * @return the blend of this <code>BlendComposite</code>.
     */
    public BlendingMode getBlend() {
        return blend;
    }

    public static Composite geteInstance(BlendingMode mode) {
        return getInstance(mode, 1f);
    }

    /**
     * Returns a BlendComposite with the given mode and opacity. If opacity is 1.0 one of the public constant
     * BlendComposite fields will be returned, incurring in no instantiation cost
     */
    public static Composite getInstance(BlendingMode mode, float opacity) {
        // use common constants when opacity is 1.0 (like AlphaComposite.getInstance() does)
        if (opacity == 1f) {
            switch (mode) {
                case MULTIPLY:
                    return MULTIPLY_COMPOSITE;
                case SCREEN:
                    return SCREEN_COMPOSITE;
                case OVERLAY:
                    return OVERLAY_COMPOSITE;
                case DARKEN:
                    return DARKEN_COMPOSITE;
                case LIGHTEN:
                    return LIGHTEN_COMPOSITE;
                case COLOR_DODGE:
                    return COLOR_DODGE_COMPOSITE;
                case COLOR_BURN:
                    return COLOR_BURN_COMPOSITE;
                case HARD_LIGHT:
                    return HARD_LIGHT_COMPOSITE;
                case SOFT_LIGHT:
                    return SOFT_LIGHT_COMPOSITE;
                case DIFFERENCE:
                    return DIFFERENCE_COMPOSITE;
                case EXCLUSION:
                    return EXCLUSION_COMPOSITE;
            }
        }

        return new BlendComposite(mode, opacity);
    }
}
