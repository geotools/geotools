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

/**
 * Take an object as an argument and returns a boolean value. The result is false when then input is
 * an empty string, 0, false, null, or NaN; otherwise it is true.
 *
 * <p>This function is a helper to accommodate MBType expressions. Expressions in this section are
 * provided for the purpose of testing for and converting between different data types like strings,
 * numbers, and boolean values.
 */
class ToBoolFunction extends FunctionExpressionImpl {

    public static FunctionName NAME = new FunctionNameImpl("toBool");

    ToBoolFunction() {
        super(NAME);
    }

    @Override
    public Object evaluate(Object feature) {
        Object arg0;

        try { // attempt to get value and perform conversion
            arg0 = getExpression(0).evaluate(feature);
        } catch (Exception e) { // probably a type error
            throw new IllegalArgumentException(
                    "Filter Function problem for function \"toBool\" argument #0 - expected type Object");
        }
        if (arg0 == null) {
            return Boolean.FALSE;
        }
        if (arg0 instanceof Double && ((Double) arg0).isNaN()) {
            return Boolean.FALSE;
        }
        if (Number.class.isAssignableFrom(arg0.getClass()) && ((Number) arg0).doubleValue() == 0d) {
            return Boolean.FALSE;
        }
        if (arg0 instanceof Boolean && (Boolean) arg0 == false) {
            return Boolean.FALSE;
        }
        if (arg0 instanceof String && ((String) arg0).isEmpty()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
