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

import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;

/** Takes an object as an argument and returns the class as a string value. */
class MapBoxTypeOfFunction extends FunctionExpressionImpl {

    public static FunctionName NAME = new FunctionNameImpl("mbTypeOf");

    MapBoxTypeOfFunction() {
        super(NAME);
    }

    @Override
    public Object evaluate(Object feature) {
        Object arg0;

        try { // attempt to get value and perform conversion
            arg0 = getExpression(0).evaluate(feature);
        } catch (Exception e) { // probably a type error
            throw new IllegalArgumentException(
                    "Filter Function problem for function \"mbTypeOf\" argument #0 - expected type Object");
        }
        return arg0.getClass().toString();
    }
}
