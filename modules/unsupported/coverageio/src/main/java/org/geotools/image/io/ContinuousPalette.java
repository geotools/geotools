/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Transparency;
import java.awt.image.DataBuffer;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import javax.media.jai.ComponentSampleModelJAI;
import javax.imageio.ImageTypeSpecifier;
import java.io.IOException;

import org.geotools.resources.image.ComponentColorModelJAI;


/**
 * A factory for building {@linkplain ColorModel color model} suitable for
 * {@link DataBuffer#TYPE_FLOAT}.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
final class ContinuousPalette extends Palette {
    /**
     * Tells if we should use {@link ComponentSampleModelJAI} instead of the more standard
     * {@link java.awt.image.ComponentSampleModel}. There is two problems with models provided
     * with J2SE 1.4:
     * <p>
     * <ul>
     *   <li>As of J2SE 1.4.0, {@link ImageTypeSpecifier#createBanded} doesn't accept
     *       {@link DataBuffer#TYPE_FLOAT} and {@link DataBuffer#TYPE_DOUBLE} argument.
     *
     *       UPDATE: it seems to be fixed with Java 5.</li>
     *
     *   <li>As of JAI 1.1, operators don't accept Java2D's {@link java.awt.image.DataBufferFloat}
     *       and {@link java.awt.image.DataBufferDouble}. They require JAI's DataBuffer instead.
     *
     *       We have not yet checked if the problem still presents with JAI 1.1.3.</li>
     * </ul>
     */
    static final boolean USE_JAI_MODEL = ComponentColorModelJAI.ENABLED;

    /**
     * The minimal value, inclusive.
     */
    protected final float minimum;

    /**
     * The maximal value, inclusive.
     */
    protected final float maximum;

    /**
     * The data type, as a {@link DataBuffer#TYPE_FLOAT} or {@link DataBuffer#TYPE_DOUBLE}
     * constant.
     */
    private final int dataType;

    /**
     * Creates a palette with the specified name.
     *
     * @param factory     The originating factory.
     * @param name        The palette name.
     * @param minimum     The minimal sample value expected.
     * @param maximum     The maximal sample value expected.
     * @param dataType    The data type as a {@link DataBuffer#TYPE_FLOAT}
     *                    or {@link DataBuffer#TYPE_DOUBLE} constant.
     * @param numBands    The number of bands (usually 1).
     * @param visibleBand The band to use for color computations (usually 0).
     */
    protected ContinuousPalette(final PaletteFactory factory, final String name, final float minimum,
            final float maximum, final int dataType, final int numBands, final int visibleBand)
    {
        super(factory, name, numBands, visibleBand);
        this.minimum  = minimum;
        this.maximum  = maximum;
        this.dataType = dataType;
    }

    /**
     * Returns the scale from <cite>normalized values</cite> (values in the range [0..1])
     * to values in the range of this palette.
     */
    @Override
    double getScale() {
        return maximum - minimum;
    }

    /**
     * Returns the offset from <cite>normalized values</cite> (values in the range [0..1])
     * to values in the range of this palette.
     */
    @Override
    double getOffset() {
        return minimum;
    }

    /**
     * Creates a grayscale image type for this palette.
     * The image type is suitable for floating point values.
     *
     * @return  A default color space scaled to fit data.
     * @throws  IOException If an I/O operation was needed and failed.
     */
    public synchronized ImageTypeSpecifier getImageTypeSpecifier() throws IOException {
        ImageTypeSpecifier its = queryCache();
        if (its != null) {
            return its;
        }
        final ColorSpace colorSpace;
        if (minimum < maximum && !Float.isInfinite(minimum) && !Float.isInfinite(maximum)) {
            colorSpace = new ScaledColorSpace(numBands, visibleBand, minimum, maximum);
        } else {
            colorSpace = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        }
        final int[] bankIndices = new int[numBands];
        final int[] bandOffsets = new int[numBands];
        for (int i=numBands; --i>=0;) {
            bankIndices[i] = i;
        }
        if (USE_JAI_MODEL) {
            final ColorModel cm = new ComponentColorModelJAI(
                    colorSpace, null, false, false, Transparency.OPAQUE, dataType);
            its = new ImageTypeSpecifier(cm, new ComponentSampleModelJAI(
                    dataType, 1, 1, 1, 1, bankIndices, bandOffsets));
        } else {
            its = ImageTypeSpecifier.createBanded(
                    colorSpace, bankIndices, bandOffsets, dataType, false, false);
        }
        cache(its);
        return its;
    }

    /**
     * Returns a hash value for this palette.
     */
    @Override
    public int hashCode() {
        return 37 * (37 * (37 * super.hashCode() + Float.floatToIntBits(minimum)) +
                Float.floatToIntBits(maximum)) + dataType;
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
            final ContinuousPalette that = (ContinuousPalette) object;
            return Float.floatToIntBits(this.minimum) == Float.floatToIntBits(that.minimum) &&
                   Float.floatToIntBits(this.maximum) == Float.floatToIntBits(that.maximum) &&
                   this.dataType == that.dataType;
        }
        return false;
    }

    /**
     * Returns a string representation of this palette. Used for debugging purpose only.
     */
    @Override
    public String toString() {
        return name + " [" + minimum + " ... " + maximum + ']';
    }
}
