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
package org.geotools.image.util;

import java.awt.color.ColorSpace;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.SampleModel;
import org.eclipse.imagen.ComponentSampleModelImageN;

/**
 * This class is a leftover of JAI-JDK 1.4 compatibility bridge. It is deprecated and you're not supposed to use it
 * anymore. It is only kept around for backward compatibility. Won't be removed anytime soon, in order to support long
 * term serialization of ImageLayout descriptions.
 */
@Deprecated
public class ComponentColorModelJAI extends ComponentColorModel {
    /** Whatever usage of this class should be enabled or not. */
    public static final boolean ENABLED = false;

    /** Construct a new color model. */
    public ComponentColorModelJAI(
            final ColorSpace colorSpace,
            final int[] bits,
            final boolean hasAlpha,
            final boolean isAlphaPremultiplied,
            final int transparency,
            final int transferType) {
        super(colorSpace, bits, hasAlpha, isAlphaPremultiplied, transparency, transferType);
    }

    /** Construct a new color model. */
    public ComponentColorModelJAI(
            final ColorSpace colorSpace,
            final boolean hasAlpha,
            final boolean isAlphaPremultiplied,
            final int transparency,
            final int transferType) {
        super(colorSpace, hasAlpha, isAlphaPremultiplied, transparency, transferType);
    }

    /**
     * Returns a compatible sample model. This implementation is nearly identical to default J2SE's implementation,
     * except that it construct a ImageN color model instead of a J2SE one.
     */
    @Override
    public SampleModel createCompatibleSampleModel(final int w, final int h) {
        switch (transferType) {
            default: {
                return super.createCompatibleSampleModel(w, h);
            }
            case DataBuffer.TYPE_FLOAT: // fall through
            case DataBuffer.TYPE_DOUBLE: {
                final int numComponents = getNumComponents();
                final int[] bandOffsets = new int[numComponents];
                for (int i = 0; i < numComponents; i++) {
                    bandOffsets[i] = i;
                }
                return new ComponentSampleModelImageN(
                        transferType, w, h, numComponents, w * numComponents, bandOffsets);
            }
        }
    }

    /**
     * Returns the {@code String} representation of the contents of this {@code ColorModel}object.
     *
     * @return a {@code String} representing the contents of this {@code ColorModel} object.
     */
    @Override
    public String toString() {
        return "ComponentColorModelImageN: #pixelBits = "
                + pixel_bits
                + " numComponents = "
                + super.getNumComponents()
                + " color space = "
                + super.getColorSpace()
                + " transparency = "
                + super.getTransparency()
                + " has alpha = "
                + super.hasAlpha()
                + " isAlphaPre = "
                + super.isAlphaPremultiplied();
    }
}
