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

import java.awt.Color;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;

/**
 * Converts the input value to a string. If the input is null, the result is "null". If the input is a boolean, the
 * result is "true" or "false". If the input is a number, it is converted to a string as specified by the
 * "NumberToString" algorithm of the ECMAScript Language Specification. If the input is a color, it is converted to a
 * string of the form "rgba(r,g,b,a)", where r, g, and b are numerals ranging from 0 to 255, and a ranges from 0 to 1.
 * Otherwise, the input is converted to a string in the format specified by the JSON.stringify function of the
 * ECMAScript Language Specification.
 */
class ToStringFunction extends FunctionExpressionImpl {
    private static final String NULL = "null";
    private static final String TRUE = "true";
    private static final String FALSE = "false";

    public static final FunctionName NAME = new FunctionNameImpl("toString");

    ToStringFunction() {
        super(NAME);
    }

    @Override
    public Object evaluate(Object feature) {
        Object arg0;

        try { // attempt to get value and perform conversion
            arg0 = getExpression(0).evaluate(feature);
        } catch (Exception e) { // probably a type error
            throw new IllegalArgumentException(
                    "Filter Function problem for function \"toString\" argument #0 - expected type Object");
        }
        if (arg0 == null) {
            return NULL;
        }
        if (arg0 instanceof Boolean) {
            if (arg0 == Boolean.TRUE) {
                return TRUE;
            }
            if (arg0 == Boolean.FALSE) {
                return FALSE;
            }
        }
        if (arg0 instanceof Number) {
            return String.valueOf(arg0);
        }
        if (arg0 instanceof Color) {
            Color c = (Color) arg0;
            return "rgba(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + "," + c.getAlpha() / 255d + ")";
        }
        if (arg0 instanceof String) {
            return String.valueOf(arg0);
        } else {
            return arg0.toString();
        }
    }
}
