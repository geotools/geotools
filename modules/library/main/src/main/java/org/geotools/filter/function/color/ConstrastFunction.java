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
import java.util.Iterator;
import java.util.List;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.filter.FunctionImpl;
import org.geotools.filter.capability.FunctionNameImpl;

/**
 * Contrast lesscss.org color function. Returns the color with highest contrast with a given reference
 *
 * @author Andrea Aime - GeoSolutions
 */
public class ConstrastFunction extends FunctionImpl {

    public static FunctionName NAME = new FunctionNameImpl(
            "contrast",
            parameter("result", Color.class),
            parameter("reference", Color.class),
            parameter("color1", Color.class, 0, 1),
            parameter("color2", Color.class, 0, 1),
            parameter("threshold", Double.class, 0, 1));

    public ConstrastFunction() {
        this.functionName = NAME;
    }

    @Override
    public Object evaluate(Object object) {
        Color reference = (Color) getParameterValue(object, 0);
        Color color1 = (Color) getParameterValue(object, 1, Color.BLACK);
        Color color2 = (Color) getParameterValue(object, 2, Color.WHITE);
        double threshold = (Double) getParameterValue(object, 3, 0.43);

        double luma1 = luma(color1);
        double luma2 = luma(color2);
        Color light, dark;
        if (luma1 > luma2) {
            light = color1;
            dark = color2;
        } else {
            light = color2;
            dark = color1;
        }

        if (luma(reference) < threshold) {
            return light;
        } else {
            return dark;
        }
    }

    private double luma(Color color) {
        double r = color.getRed() / 255d;
        double g = color.getGreen() / 255d;
        double b = color.getBlue() / 255d;

        r = r <= 0.03928 ? r / 12.92 : Math.pow((r + 0.055) / 1.055, 2.4);
        g = g <= 0.03928 ? g / 12.92 : Math.pow((g + 0.055) / 1.055, 2.4);
        b = b <= 0.03928 ? b / 12.92 : Math.pow((b + 0.055) / 1.055, 2.4);

        return 0.2126 * r + 0.7152 * g + 0.0722 * b;
    }

    /** Creates a String representation of this Function with the function name and the arguments. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName());
        sb.append("(");
        List<org.geotools.api.filter.expression.Expression> params = getParameters();
        if (params != null) {
            org.geotools.api.filter.expression.Expression exp;
            for (Iterator<org.geotools.api.filter.expression.Expression> it = params.iterator(); it.hasNext(); ) {
                exp = it.next();
                sb.append("[");
                sb.append(exp);
                sb.append("]");
                if (it.hasNext()) {
                    sb.append(", ");
                }
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
