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

package org.geotools.mbstyle.expression;

import org.geotools.mbstyle.parse.MBFormatException;
import org.json.simple.JSONArray;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

/** A class to transform mapbox color expressions into geotools expressions. */
public class MBColor extends MBExpression {

    public MBColor(JSONArray json) {
        super(json);
    }

    /**
     * Creates a color value from red, green, and blue components, which must range between 0 and
     * 255, and an alpha component of 1. If any component is out of range, the expression is an
     * error. Example: ["rgb", number, number, number]: color
     */
    public Expression colorRGB() {
        if (json.size() == 4) {
            Expression er = parse.string(json, 1);
            er = transformLiteral(er);
            Function roundr = ff.function("round_2", er);

            Expression eg = parse.string(json, 2);
            eg = transformLiteral(eg);
            Function roundg = ff.function("round_2", eg);

            Expression eb = parse.string(json, 3);
            eb = transformLiteral(eb);
            Function roundb = ff.function("round_2", eb);

            Function color = ff.function("torgb", roundr, roundg, roundb);
            return color;
        } else {
            throw new MBFormatException("Expression \"rgb\" requires exactly 3 arguments");
        }
    }

    /**
     * Creates a color value from red, green, blue components, which must range between 0 and 255,
     * and an alpha component which must range between 0 and 1. If any component is out of range,
     * the expression is an error. Example: ["rgba", number, number, number, number]: color
     */
    // Currently unsupported
    public Expression colorRGBA() {
        throw new UnsupportedOperationException("RGBA colors are currently unsupported");
    }

    /**
     * Returns a four-element array containing the input color's red, green, blue, and alpha
     * components, in that order. Example: ["to-rgba", color]: array<number, 4>
     */
    // Currently unsupported
    public Expression colorToRGBA() {
        throw new UnsupportedOperationException("RGBA colors are currently unsupported.");
    }

    @Override
    public Expression getExpression() throws MBFormatException {
        switch (name) {
            case "rgb":
                return colorRGB();
            case "rgba":
                return colorRGBA();
            case "to-rgba":
                return colorToRGBA();
            default:
                throw new MBFormatException(name + " is an unsupported color expression");
        }
    }
}
