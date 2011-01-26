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
package org.geotools.image.io;

import java.awt.color.ColorSpace;
import org.geotools.resources.Classes;


/**
 * Color space for images storing pixels as real numbers. The color model can have an
 * arbitrary number of bands, but in current implementation only one band is used.
 *
 * @author Martin Desruisseaux (IRD)
 * @source $URL$
 * @version $Id$
 */
final class ScaledColorSpace extends ColorSpace {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = 438226855772441165L;

    /**
     * Minimal normalized RGB value.
     */
    private static final float MIN_VALUE = 0f;

    /**
     * Maximal normalized RGB value.
     */
    private static final float MAX_VALUE = 1f;

    /**
     * The scaling factor for pixel values.
     */
    private final float scale;

    /**
     * The offset to apply after the {@linkplain #scale} on pixel values.
     */
    private final float offset;

    /**
     * Index of the band to display.
     */
    private final int visibleBand;

    /**
     * Creates a color model.
     *
     * @param numComponents The number of components.
     * @param visibleBand The band to use for computing colors.
     * @param minimum The minimal sample value expected.
     * @param maximum The maximal sample value expected.
     */
    public ScaledColorSpace(final int numComponents, final int visibleBand,
                            final float minimum, final float maximum)
    {
        super(TYPE_GRAY, numComponents);
        this.visibleBand = visibleBand;
        scale  = (maximum - minimum) / (MAX_VALUE - MIN_VALUE);
        offset = minimum - MIN_VALUE*scale;
    }

    /**
     * Returns a RGB color for a gray scale value.
     */
    public float[] toRGB(final float[] values) {
        float value = (values[visibleBand] - offset) / scale;
        if (Float.isNaN(value)) {
            value = MIN_VALUE;
        }
        return new float[] {value, value, value};
    }

    /**
     * Returns a real value for the specified RGB color.
     * The RGB color is assumed to be a gray scale value.
     */
    public float[] fromRGB(final float[] RGB) {
        final float[] values = new float[getNumComponents()];
        values[visibleBand] = (RGB[0] + RGB[1] + RGB[2]) / 3 * scale + offset;
        return values;
    }

    /**
     * Convert a color to the CIEXYZ color space.
     */
    public float[] toCIEXYZ(final float[] values) {
        float value = (values[visibleBand] - offset) / scale;
        if (Float.isNaN(value)) {
            value = MIN_VALUE;
        }
        return new float[] {
            value * 0.9642f,
            value * 1.0000f,
            value * 0.8249f
        };
    }

    /**
     * Convert a color from the CIEXYZ color space.
     */
    public float[] fromCIEXYZ(final float[] RGB) {
        final float[] values = new float[getNumComponents()];
        values[visibleBand] = (RGB[0] / 0.9642f + RGB[1] + RGB[2] / 0.8249f) / 3 * scale + offset;
        return values;
    }

    /**
     * Returns the minimum value for the specified RGB component.
     */
    @Override
    public float getMinValue(final int component) {
        return MIN_VALUE * scale + offset;
    }

    /**
     * Returns the maximum value for the specified RGB component.
     */
    @Override
    public float getMaxValue(final int component) {
        return MAX_VALUE * scale + offset;
    }

    /**
     * Returns a string representation of this color model.
     */
    @Override
    public String toString() {
        return Classes.getShortClassName(this) +
                '[' + getMinValue(visibleBand) + ", " + getMaxValue(visibleBand) + ']';
    }
}
