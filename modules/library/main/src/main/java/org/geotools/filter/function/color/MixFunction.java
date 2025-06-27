/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function.color;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import java.awt.Color;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.filter.FunctionImpl;
import org.geotools.filter.capability.FunctionNameImpl;

/**
 * Mix lesscss.org color function. Takes two colors and mixes them together based on a weight (and their eventual alpha)
 *
 * @author Andrea Aime - GeoSolutions
 */
public class MixFunction extends FunctionImpl {

    public static FunctionName NAME = new FunctionNameImpl(
            "mix",
            parameter("result", Color.class),
            parameter("color1", Color.class),
            parameter("color2", Color.class),
            parameter("weight", Double.class));

    public MixFunction() {
        this.functionName = NAME;
    }

    @Override
    public Object evaluate(Object object) {
        Color color1 = (Color) getParameterValue(object, 0);
        Color color2 = (Color) getParameterValue(object, 1);
        double weight = (Double) getParameterValue(object, 2);

        return mix(color1, color2, weight);
    }

    protected Color mix(Color color1, Color color2, double weight) {
        // adjust weight based on alpha
        double da = color1.getAlpha() - color2.getAlpha();
        double w = weight * 2 - 1;
        double w1 = ((w * da == -1 ? w : (w + da) / (1 + w * da)) + 1) / 2.0;
        double w2 = 1 - w1;

        // mix colors
        int red = (int) Math.round(color1.getRed() * w1 + color2.getRed() * w2);
        int green = (int) Math.round(color1.getGreen() * w1 + color2.getGreen() * w2);
        int blue = (int) Math.round(color1.getBlue() * w1 + color2.getBlue() * w2);
        int alpha = 255;

        if (color1.getAlpha() < 255 || color2.getAlpha() < 255) {
            alpha = (int) Math.round(color1.getAlpha() * weight + color2.getAlpha() * (1 - weight));
        }
        return new Color(red, green, blue, alpha);
    }
}
