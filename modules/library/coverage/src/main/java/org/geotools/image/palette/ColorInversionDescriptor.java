/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.palette;

// J2SE dependencies

import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import javax.media.jai.OperationDescriptorImpl;
import javax.media.jai.registry.RenderedRegistryMode;

public class ColorInversionDescriptor extends OperationDescriptorImpl {
    /** */
    private static final long serialVersionUID = -8859576263333814317L;

    /** Builds the internet safe paletteInverter */
    private static IndexColorModel buildDefaultPalette() {
        int[] cmap = new int[256];

        // Create the standard 6x6x6 color cube (all elements do cycle
        // between 00, 33, 66, 99, CC and FF, the decimal difference is 51)
        // The color is made of alpha, red, green and blue, in this order, from
        // the most significant bit onwards.
        int i = 0;
        int opaqueAlpha = 255 << 24;

        for (int r = 0; r < 256; r += 51) {
            for (int g = 0; g < 256; g += 51) {
                for (int b = 0; b < 256; b += 51) {
                    cmap[i] = opaqueAlpha | r << 16 | g << 8 | b;
                    i++;
                }
            }
        }

        // The gray scale. Make sure we end up with gray == 255
        int grayIncr = 256 / (255 - i);
        int gray = 255 - (255 - i - 1) * grayIncr;

        for (; i < 255; i++) {
            cmap[i] = opaqueAlpha | gray << 16 | gray << 8 | gray;
            gray += grayIncr;
        }

        // setup the transparent color (alpha == 0)
        cmap[255] = 255 << 16 | 255 << 8 | 255;

        // create the color model
        return new IndexColorModel(8, 256, cmap, 0, true, 255, DataBuffer.TYPE_BYTE);
    }

    /** The operation name, which is {@value}. */
    public static final String OPERATION_NAME = "org.geotools.ColorInversion";

    /** Constructs the descriptor. */
    public ColorInversionDescriptor() {
        super(
                new String[][] {
                    {"GlobalName", OPERATION_NAME},
                    {"LocalName", OPERATION_NAME},
                    {"Vendor", "it.geosolutions"},
                    {"Description", "Produce a paletted imge from an RGB or RGBA image using a provided palette."},
                    {"DocURL", "http://www.geo-solutions.it/"}, // TODO:
                    // provides more
                    // accurate URL
                    {"Version", "1.0"},
                    {"arg0Desc", "Indexed color model."},
                    {"arg1Desc", "Number of colors after the reduction."},
                    {"arg2Desc", "Threshold for thresholding alpha"}
                },
                new String[] {RenderedRegistryMode.MODE_NAME},
                0, // Supported
                // modes
                new String[] {"IndexColorModel", "quantizationColors", "alphaThreshold"}, // Parameter
                // names
                new Class[] {
                    IndexColorModel.class, Integer.class, Integer.class,
                }, // Parameter
                // classes
                new Object[] {
                    buildDefaultPalette(),
                    Integer.valueOf(InverseColorMapRasterOp.DEFAULT_QUANTIZATION_COLORS),
                    Integer.valueOf(1),
                }, // Default
                // values
                null // Valid parameter values
                );
    }

    /**
     * Returns {@code true} if this operation supports the specified mode, and is capable of handling the given input
     * source(s) for the specified mode.
     *
     * @param modeName The mode name (usually "Rendered").
     * @param param The parameter block for the operation to performs.
     * @param message A buffer for formatting an error message if any.
     */
    @Override
    protected boolean validateSources(final String modeName, final ParameterBlock param, final StringBuffer message) {
        if (super.validateSources(modeName, param, message)) {
            for (int i = param.getNumSources(); --i >= 0; ) {
                final Object source = param.getSource(i);
                if (!(source instanceof RenderedImage)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Returns {@code true} if the parameters are valids. This implementation check that the number of bands in the
     * source src1 is equals to the number of bands of source src2.
     *
     * @param modeName The mode name (usually "Rendered").
     * @param param The parameter block for the operation to performs.
     * @param message A buffer for formatting an error message if any.
     */
    @Override
    protected boolean validateParameters(
            final String modeName, final ParameterBlock param, final StringBuffer message) {
        if (!super.validateParameters(modeName, param, message)) {
            return false;
        }
        if (!(param.getObjectParameter(0) instanceof IndexColorModel)) return false;
        final int numColors = ((Integer) param.getObjectParameter(1)).intValue();
        final int alphaThreashold = ((Integer) param.getObjectParameter(2)).intValue();
        if (alphaThreashold < 0 || alphaThreashold > 255) return false;
        if (numColors <= 0 || numColors > 256) return false;
        return true;
    }
}
