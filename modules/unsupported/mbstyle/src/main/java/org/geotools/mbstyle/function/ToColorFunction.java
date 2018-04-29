/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbstyle.function;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.mbstyle.parse.MBObjectParser;
import org.json.simple.JSONArray;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;

/**
 * Takes an object as an argument and returns the color value if possible. Evaluates string in the
 * formats of: "rgb(int, int, int)" "rgba(int, int, int, double)"
 *
 * <p>And in arrays of 3 and 4 numbers for rgb and rgba colors: [int, int, int] [int, int, int,
 * double]
 *
 * <p>The integer values should be between 0-255, for red, green, and blue color values. The double
 * value should be between 0-1, and is converted to a 0-255 alpha value.
 */
class ToColorFunction extends FunctionExpressionImpl {
    MBObjectParser parse = new MBObjectParser(ToColorFunction.class);

    public static FunctionName NAME = new FunctionNameImpl("toColor");

    ToColorFunction() {
        super(NAME);
    }

    /** @see org.geotools.filter.FunctionExpressionImpl#setParameters(java.util.List) */
    @Override
    public void setParameters(List<Expression> params) {
        // set the parameters
        this.params = new ArrayList<>(params);
    }

    @Override
    public Object evaluate(Object feature) {
        Color c;
        for (Integer i = 1; i <= this.params.size() - 1; i++) {
            Object evaluation = this.params.get(i).evaluate(feature);
            if (evaluation instanceof Color) {
                return evaluation;
            }
            if (evaluation instanceof JSONArray) {
                JSONArray je = (JSONArray) evaluation;
                if (je.size() == 3 || je.size() == 4) {
                    Long r = (Long) je.get(0);
                    Long g = (Long) je.get(1);
                    Long b = (Long) je.get(2);
                    if (je.size() == 3) {
                        try {
                            c = new Color(r.intValue(), g.intValue(), b.intValue());
                            return c;
                        } catch (Exception e) {
                        }
                    }
                    if (je.size() == 4) {
                        Double a = (Double) je.get(3);
                        Integer alpha = ((Long) Math.round(a * 255)).intValue();
                        try {
                            c = new Color(r.intValue(), g.intValue(), b.intValue(), alpha);
                            return c;
                        } catch (Exception e) {
                        }
                    }
                }
            }
            if (evaluation instanceof String) {
                try {
                    return parse.convertToColor(evaluation.toString());
                } catch (Exception e) {
                }
            }
        }
        throw new IllegalArgumentException(
                "No arguments provided to the \"toColor\" function can be converted to a Color value");
    }
}
