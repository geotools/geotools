/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.resources.image;

import java.awt.color.ColorSpace;
import java.awt.image.ComponentColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.SampleModel;

import javax.media.jai.ComponentSampleModelJAI;
import javax.media.jai.FloatDoubleColorModel;
import javax.media.jai.iterator.RectIter;


/**
 * A {@link ComponentColorModel} modified for interoperability with Java Advanced Imaging.
 * JAI 1.1 was designed for use with J2SE 1.3 and is not aware of new features in J2SE 1.4.
 * This leads to the following problems:
 *
 * <ul>
 *   <li>{@link ComponentColorModel} supports {@code float} and {@code double}
 *       datatypes since J2SE 1.4 only. The workaround for J2SE 1.3 is to use the
 *       {@link FloatDoubleColorModel} provided with JAI 1.1.</li>
 *   <li>{@link FloatDoubleColorModel} ignores the new API in {@link ColorSpace}, especially
 *       the {@code getMinValue} and {@code getMaxValue} methods. Consequently,
 *       rendering of any image using our custom {@code ScaledColorSpace} is wrong.</li>
 *   <li>{@link ComponentColorModel} uses {@link java.awt.image.DataBufferFloat} and {@link
 *       java.awt.image.DataBufferDouble}, which are unknown to JAI 1.1. Consequently, trying
 *       to use {@link RectIter} with one of those will throw {@link ClassCastException}.</li>
 * </ul>
 *
 * The work around is to use J2SE's {@link ComponentColorModel} (which work with our custom
 * {@link ColorSpace}) and override its {@code createCompatibleSampleModel} in order to
 * returns {@link ComponentSampleModelJAI} instead of {@link ComponentSampleModel} when
 * {@code float} or {@code double} datatype is requested.
 *
 * @todo Remove this patch when JAI will recognize J2SE 1.4 classes.
 *
 * @since 2.0
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class ComponentColorModelJAI extends ComponentColorModel {
    /**
     * Whatever usage of this class should be enabled or not.
     */
    public static final boolean ENABLED = false;

    /**
     * Construct a new color model.
     */
    public ComponentColorModelJAI(final ColorSpace colorSpace,
                                  final int[] bits,
                                  final boolean hasAlpha,
                                  final boolean isAlphaPremultiplied,
                                  final int transparency,
                                  final int transferType)
    {
        super(colorSpace, bits, hasAlpha, isAlphaPremultiplied, transparency, transferType);
    }

    /**
     * Construct a new color model.
     */
    public ComponentColorModelJAI(final ColorSpace colorSpace,
                                  final boolean hasAlpha,
                                  final boolean isAlphaPremultiplied,
                                  final int transparency,
                                  final int transferType)
    {
        super(colorSpace, hasAlpha, isAlphaPremultiplied, transparency, transferType);
    }

    /**
     * Returns a compatible sample model. This implementation is nearly identical
     * to default J2SE's implementation, except that it construct a JAI color model
     * instead of a J2SE one.
     */
    public SampleModel createCompatibleSampleModel(final int w, final int h) {
        switch (transferType) {
            default: {
                return super.createCompatibleSampleModel(w, h);
            }
            case DataBuffer.TYPE_FLOAT:   // fall through
            case DataBuffer.TYPE_DOUBLE: {
                final int numComponents = getNumComponents();
                final int[] bandOffsets = new int[numComponents];
                for (int i=0; i<numComponents; i++) {
                    bandOffsets[i] = i;
                }
                return new ComponentSampleModelJAI(transferType, w, h, numComponents,
                                                   w*numComponents, bandOffsets);
            }
        }
    }

    /**
     * Returns the {@code String} representation of the contents of
     * this {@code ColorModel}object.
     *
     * @return a {@code String} representing the contents of this
     * {@code ColorModel} object.
     */
    public String toString() {
       return new String("ComponentColorModelJAI: #pixelBits = "+pixel_bits
                         + " numComponents = "+ super.getNumComponents()
                         + " color space = "+ super.getColorSpace()
                         + " transparency = "+ super.getTransparency()
                         + " has alpha = "+ super.hasAlpha()
                         + " isAlphaPre = "+ super.isAlphaPremultiplied());
    }
}
