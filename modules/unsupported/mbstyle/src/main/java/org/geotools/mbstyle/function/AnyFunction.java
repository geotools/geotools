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
 * MapBox Expression function that returns {@link java.lang.Boolean#TRUE} if any expression
 * evaluates to {@link java.lang.Boolean#TRUE}, {@link java.lang.Boolean#FALSE} otherwise. This
 * function is implemented as a short- circuit in that it will return {@link java.lang.Boolean#TRUE}
 * for the first expression that evaluates to {@link java.lang.Boolean#TRUE}.
 *
 * <p>Format:
 *
 * <pre>
 *     ["any", &lt;condition expression&gt;, &lt;condition expression&gt;, ...]
 * </pre>
 *
 * <p>Examples:
 *
 * <p>
 *
 * <table border="1" cellpadding="3">
 *   <tr>
 *     <th align="center">Expression</th>
 *     <th align="center">Output</th>
 *   </tr>
 *   <tr>
 *     <td>["any", true, true, true]</td>
 *     <td align="center">true</td>
 *   </tr>
 *   <tr>
 *     <td>["any", true, true, false]</td>
 *     <td align="center">true</td>
 *   </tr>
 *   <tr>
 *     <td>["any", false, false, false, false]</td>
 *     <td align="center">false</td>
 *   </tr>
 * </table>
 */
class AnyFunction extends FunctionExpressionImpl {

    public static FunctionName NAME = new FunctionNameImpl("any");

    AnyFunction() {
        super(NAME);
    }

    /** @see org.geotools.filter.FunctionExpressionImpl#setParameters(java.util.List) */
    @Override
    public void setParameters(List<Expression> params) {
        // set the parameters
        this.params = new ArrayList<>(params);
    }

    /** @see org.geotools.filter.FunctionExpressionImpl#equals(java.lang.Object) */
    @Override
    public Object evaluate(Object feature) {
        // loop over the arguments and ensure at least one evaluates to true
        for (Expression expression : this.params) {
            Object evaluation = expression.evaluate(feature);
            if (Boolean.TRUE.equals(evaluation)) {
                return Boolean.TRUE;
            }
        }
        // couldn't find a TRUE
        return Boolean.FALSE;
    }
}
