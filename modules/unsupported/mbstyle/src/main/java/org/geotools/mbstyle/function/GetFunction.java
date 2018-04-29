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
import org.json.simple.JSONObject;
import org.opengis.filter.capability.FunctionName;

/** Returns the value of a given object key in a JSONObject. */
public class GetFunction extends FunctionExpressionImpl {
    public static FunctionName NAME =
            new FunctionNameImpl(
                    "get",
                    parameter("value", String.class),
                    parameter("object", JSONObject.class),
                    parameter("fallback", Object.class));

    public GetFunction() {
        super(NAME);
    }

    @Override
    public Object evaluate(Object feature) {
        String arg0;
        JSONObject arg1;

        try { // attempt to get value and perform conversion
            arg0 = getExpression(0).evaluate(feature, String.class);

        } catch (Exception e) { // probably a type error
            throw new IllegalArgumentException(
                    "Filter Function problem for function \"get\" argument #0 - expected type String");
        }
        try { // attempt to get value and perform conversion
            arg1 = getExpression(1).evaluate(feature, JSONObject.class);

        } catch (Exception e) { // probably a type error
            throw new IllegalArgumentException(
                    "Filter Function problem for function \"get\" argument #1 - expected type JSONObject");
        }
        return arg1.get(arg0);
    }
}
