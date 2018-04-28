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
 * MapBox Expression function that will return the expression evaluation result of the first
 * expression that evaluates to a non-null value. If no expression evaluates to a non-null value,
 * this function will return null.
 *
 * <p>Format:
 *
 * <pre>
 *     ["coalesce", &lt;expression&gt;, &lt;expression&gt;, &lt;expression&gt;, ...]
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
 *     <td>["coalesce", "aString", false, null]</td>
 *     <td align="center">"aString"</td>
 *   </tr>
 *   <tr>
 *     <td>["coalesce", null, null, null, "default"]</td>
 *     <td align="center">"default"</td>
 *   </tr>
 *   <tr>
 *     <td>["coalesce", null, true, null]</td>
 *     <td align="center">true</td>
 *   </tr>
 *   <tr>
 *     <td>["coalesce", null, null, null]</td>
 *     <td align="center">null</td>
 *   </tr>
 * </table>
 */
class CoalesceFunction extends FunctionExpressionImpl {

    public static FunctionName NAME = new FunctionNameImpl("coalesce");

    CoalesceFunction() {
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
        for (Expression expression : params) {
            Object evaluate = expression.evaluate(feature);
            if (evaluate != null) {
                return evaluate;
            }
        }
        // no non-null expression evaluations
        return null;
    }
}
