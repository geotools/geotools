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

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import java.awt.*;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;

/** Generate a rgb color from integer values between 0-255. */
public class ToRgb extends FunctionExpressionImpl {

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "torgb",
                    parameter("r", Integer.class),
                    parameter("g", Integer.class),
                    parameter("b", Integer.class),
                    parameter("fallback", Color.class));

    public ToRgb() {
        super(NAME);
    }

    public Object evaluate(Object feature) {
        Integer arg0;
        Integer arg1;
        Integer arg2;

        try { // attempt to get value and perform conversion
            Number red = getExpression(0).evaluate(feature, Integer.class);
            arg0 = red.intValue();
            Number green = getExpression(1).evaluate(feature, Integer.class);
            arg1 = green.intValue();
            Number blue = getExpression(2).evaluate(feature, Integer.class);
            arg2 = blue.intValue();
        } catch (Exception e) {
            // probably a type error
            throw new IllegalArgumentException(
                    "Function problem for function torgb - expected type integer");
        }
        return new Color(arg0, arg1, arg2);
    }
}
