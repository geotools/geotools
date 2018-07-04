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

import java.util.ArrayList;
import java.util.List;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;

/**
 * Converts the input value to a number, if possible. If the input is null or false, the result is
 * 0. If the input is true, the result is 1. If the input is a string, it is converted to a number
 * as specified by the "ToNumber Applied to the String Type" algorithm of the ECMAScript Language
 * Specification. If multiple values are provided, each one is evaluated in order until the first
 * successful conversion is obtained. If none of the inputs can be converted, the expression is an
 * error.
 */
class ToNumberFunction extends FunctionExpressionImpl {

    public static FunctionName NAME = new FunctionNameImpl("toNumber");

    ToNumberFunction() {
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
        for (Integer i = 1; i <= this.params.size() - 1; i++) {
            Object evaluation = this.params.get(i).evaluate(feature);
            if (evaluation == null) {
                return 0L;
            }
            if (evaluation instanceof Boolean) {
                if (evaluation == Boolean.FALSE) {
                    return 0L;
                }
                if (evaluation == Boolean.TRUE) {
                    return 1L;
                }
            }
            if (evaluation instanceof String) {
                try {
                    return Double.valueOf((String) evaluation);
                } catch (Exception e) {
                }
            }
            if (Number.class.isAssignableFrom(evaluation.getClass())) {
                return evaluation;
            }
        }
        throw new IllegalArgumentException(
                "No arguments provided can be converted to a Number value");
    }
}
