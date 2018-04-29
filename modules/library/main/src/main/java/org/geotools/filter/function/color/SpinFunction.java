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
import org.geotools.filter.FunctionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;

/**
 * Mix lesscss.org color function. Takes two colors and mixes them toghether based on a weight (and
 * their eventual alpha)
 *
 * @author Andrea Aime - GeoSolutions
 */
public class SpinFunction extends FunctionImpl {

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "spin",
                    parameter("result", Color.class),
                    parameter("color", Color.class),
                    parameter("amount", Double.class));

    public SpinFunction() {
        this.functionName = NAME;
    }

    @Override
    public Object evaluate(Object object) {
        Color color = (Color) getParameterValue(object, 0);
        double amount = (Double) getParameterValue(object, 1);

        HSLColor hsl = new HSLColor(color);
        double hue = (hsl.getHue() + amount) % 360;
        if (hue < 0) {
            hue = hue + 360;
        }
        hsl.setHue(hue);

        return hsl.toRGB();
    }
}
