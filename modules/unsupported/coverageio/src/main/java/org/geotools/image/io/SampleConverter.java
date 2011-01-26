/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.resources.XMath;
import org.geotools.resources.Classes;


/**
 * Converts samples from the values stored in the image file to the values stored in the
 * {@linkplain java.awt.image.Raster raster}. Some typical conversions are:
 * <p>
 * <ul>
 *   <li>Replace "<cite>nodata</cite>" values (typically a fixed value like 9999 or
 *       {@value Short#MAX_VALUE}) by {@link Float#NaN NaN} if the target type is
 *       {@code float} or {@code double}, or 0 if the target type is an integer.</li>
 *   <li>Replace <em>signed</em> integers by <em>unsigned</em> integers, by applying
 *       an offset to the values.</li>
 * </ul>
 * <p>
 * Note that pad values are replaced by 0 in the integer case, not by an arbitrary number,
 * because 0 is the result of {@code (int) NaN} cast. While not mandatory, this property
 * make some mathematics faster during conversions between <cite>geophysics</cite> and
 * <cite>display</cite> views in the coverage module.
 * <p>
 * There is no scaling because this class is not for <cite>samples to geophysics values</cite>
 * conversions (except the replacement of pad values by {@link Double#NaN NaN}). This class is
 * about the minimal changes needed in order to comply to the contraints of a target
 * {@linkplain java.awt.image.ColorModel color model}, e.g. for working around negative numbers.
 *
 * Sample converters work on {@code int}, {@code float} or {@code double} primitive types,
 * which match the primitive types expected by the {@link java.awt.image.Raster} API.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public abstract class SampleConverter {
    /**
     * A sample converter that do not performs any conversion.
     */
    public static final SampleConverter IDENTITY = new Identity();

    /**
     * Constructs a sample converter.
     */
    protected SampleConverter() {
    }

    /**
     * Creates a sample converter that replaces a single pad value by {@link Double#NaN NaN}
     * for floating point numbers, or {@code 0} for integers.
     *
     * @param padValue The pad values to replace by {@link Double#NaN NaN} or {@code 0}.
     */
    public static SampleConverter createPadValueMask(final double padValue) {
        return Double.isNaN(padValue) ? IDENTITY : new PadValueMask(padValue);
    }

    /**
     * Creates a sample converter that replaces an arbitrary amount of pad values by
     * {@link Double#NaN NaN} for floating point numbers, or {@code 0} for integers.
     *
     * @param padValue The pad values to replace by {@link Double#NaN NaN} or {@code 0},
     *                 or {@code null} if none.
     */
    public static SampleConverter createPadValuesMask(final double[] padValues) {
        if (padValues != null) {
            switch (padValues.length) {
                default: return new PadValuesMask(padValues);
                case 1:  return createPadValueMask(padValues[0]);
                case 0:  break;
            }
        }
        return IDENTITY;
    }

    /**
     * Creates a sample converter that replaces a pad value by {@link Double#NaN NaN} or
     * {@code 0}, and applies an offset on all other values. This is typically used in
     * order to shift a range of arbitrary (including negative) integer values to a range
     * of strictly positive values. The later is more manageable by
     * {@linkplain java.awt.image.IndexColorModel index color model}.
     *
     * @param An offset to add to the values to be read, before to store them in the raster. This
     *        is used primarily for transforming <em>signed</em> short into <em>unsigned</em> short.
     * @param padValue The pad value to replace. This the value before the offset is applied.
     */
    public static SampleConverter createOffset(final double offset, final double padValue) {
        return (offset == 0) ? createPadValueMask(padValue) : new Offset(offset, padValue);
    }

    /**
     * Creates a sample converter that replaces an arbitrary amount of pad values by
     * {@link Double#NaN NaN} or {@code 0}, and applies an offset on all other values.
     *
     * @param An offset to add to the values to be read, before to store them in the raster.
     * @param padValue The pad values to replace. They the values before the offset is applied.
     */
    public static SampleConverter createOffset(final double offset, final double[] padValues) {
        if (offset == 0) {
            return createPadValuesMask(padValues);
        }
        if (padValues != null) {
            switch (padValues.length) {
                default: return new MaskAndOffset(offset, padValues);
                case 1:  return createOffset(offset, padValues[0]);
                case 0:  break;
            }
        }
        return createOffset(offset, Double.NaN);
    }

    /**
     * Converts a double-precision value before to store it in the raster.
     * Subclasses should override this method if some fixed values need to
     * be converted into {@link Double#NaN} value.
     *
     * @param value The value read from the image file.
     * @return The value to store in the {@linkplain java.awt.image.Raster raster}.
     */
    public abstract double convert(double value);

    /**
     * Converts a float-precision value before to store it in the raster.
     * Subclasses should override this method if some fixed values need to
     * be converted into {@link Float#NaN} value.
     *
     * @param value The value read from the image file.
     * @return The value to store in the {@linkplain java.awt.image.Raster raster}.
     */
    public abstract float convert(float value);

    /**
     * Converts a float-precision value before to store it in the raster.
     *
     * @param value The value read from the image file.
     * @return The value to store in the {@linkplain java.awt.image.Raster raster}.
     */
    public abstract int convert(int value);

    /**
     * If this converter applies an offset, returns the offset. Otherwise returns 0.
     */
    public double getOffset() {
        return 0;
    }

    /**
     * Returns a string representation of this sample converter.
     * This is mostly for debugging purpose and may change in any future version.
     */
    @Override
    public String toString() {
        return Classes.getShortClassName(this) + "[offset=" + getOffset() + ']';
    }

    /**
     * A sample converter that do not performs any conversion.
     */
    private static final class Identity extends SampleConverter {
        public double convert(double value) {
            return value;
        }

        public float convert(float value) {
            return value;
        }

        public int convert(int value) {
            return value;
        }
    }

    /**
     * A sample converter that replaces a single pad value by {@link Double#NaN NaN}
     * for floating point numbers, or {@code 0} for integers.
     */
    private static class PadValueMask extends SampleConverter {
        final double doubleValue;
        final float  floatValue;
        final int    integerValue;

        PadValueMask(final double padValue) {
            doubleValue  =         padValue;
            floatValue   = (float) padValue;
            final int p  = (int)   padValue;
            integerValue = (p == padValue) ? p : 0;
        }

        public double convert(final double value) {
            return (value == doubleValue) ? Double.NaN : value;
        }

        public float convert(final float value) {
            return (value == floatValue) ? Float.NaN : value;
        }

        public int convert(final int value) {
            return (value == integerValue) ? 0 : value;
        }
    }

    /**
     * A sample converter that replaces a single pad value by 0,
     * and applies an offset on all other values.
     */
    private static final class Offset extends PadValueMask {
        private final double doubleOffset;
        private final float  floatOffset;
        private final int    integerOffset;

        Offset(final double offset, final double padValue) {
            super(padValue);
            doubleOffset  = offset;
            floatOffset   = (float) offset;
            integerOffset = (int) Math.round(offset);
        }

        @Override
        public double convert(final double value) {
            return (value == doubleValue) ? Double.NaN : value + doubleOffset;
        }

        @Override
        public float convert(final float value) {
            return (value == floatValue) ? Float.NaN : value + floatOffset;
        }

        @Override
        public int convert(final int value) {
            return (value == integerValue) ? 0 : value + integerOffset;
        }

        @Override
        public double getOffset() {
            return doubleOffset;
        }
    }

    /**
     * A sample converter that replaces an arbitrary amount of pad values by
     * {@link Double#NaN NaN} for floating point numbers, or {@code 0} for integers.
     */
    private static class PadValuesMask extends SampleConverter {
        private final double[] doubleValues;
        private final float [] floatValues;
        private final float [] NaNs;

        PadValuesMask(final double[] padValues) {
            doubleValues  = new double[padValues.length];
            floatValues   = new float [padValues.length];
            NaNs          = new float [padValues.length];
            for (int i=0; i<padValues.length; i++) {
                floatValues[i] = (float) (doubleValues[i] = padValues[i]);
                NaNs[i] = XMath.toNaN(i);
            }
        }

        public double convert(final double value) {
            for (int i=0; i<doubleValues.length; i++) {
                if (value == doubleValues[i]) {
                    return NaNs[i];
                }
            }
            return value;
        }

        public float convert(final float value) {
            for (int i=0; i<floatValues.length; i++) {
                if (value == floatValues[i]) {
                    return NaNs[i];
                }
            }
            return value;
        }

        // Do not override: we really need the arithmetic on NaN values.
        public final int convert(final int value) {
            return (int) convert((double) value);
        }
    }

    /**
     * A sample converter that replaces many pad values by 0,
     * and applies an offset on all other values.
     */
    private static final class MaskAndOffset extends PadValuesMask {
        private final double doubleOffset;
        private final float  floatOffset;

        MaskAndOffset(final double offset, final double[] padValues) {
            super(padValues);
            doubleOffset =         offset;
            floatOffset  = (float) offset;
        }

        @Override
        public double convert(final double value) {
            return super.convert(value) + doubleOffset;
        }

        @Override
        public float convert(final float value) {
            return super.convert(value) + floatOffset;
        }

        @Override
        public double getOffset() {
            return doubleOffset;
        }
    }
}
