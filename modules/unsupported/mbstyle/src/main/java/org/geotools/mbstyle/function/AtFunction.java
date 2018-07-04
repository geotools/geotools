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

import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.json.simple.JSONArray;
import org.opengis.filter.capability.FunctionName;

/** Returns the value in a JSONArray at a given index. */
public class AtFunction extends FunctionExpressionImpl {
    public static FunctionName NAME =
            new FunctionNameImpl(
                    "at",
                    parameter("array", JSONArray.class),
                    parameter("index", Integer.class),
                    parameter("fallback", Object.class));

    public AtFunction() {
        super(NAME);
    }

    @Override
    public Object evaluate(Object feature) {
        JSONArray arg0;
        Integer arg1;

        try { // attempt to get value and perform conversion
            arg0 = getExpression(0).evaluate(feature, JSONArray.class);

        } catch (Exception e) { // probably a type error
            throw new IllegalArgumentException(
                    "Filter Function problem for function \"at\" argument #0 - expected type JSONArray");
        }
        try { // attempt to get value and perform conversion
            arg1 = getExpression(1).evaluate(feature, Integer.class);

        } catch (Exception e) { // probably a type error
            throw new IllegalArgumentException(
                    "Filter Function problem for function \"at\" argument #1 - expected type Integer");
        }
        return arg0.get(arg1);
    }
}
