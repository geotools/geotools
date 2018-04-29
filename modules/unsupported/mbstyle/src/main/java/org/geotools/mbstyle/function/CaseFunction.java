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
 * MapBox Expression function that will return the expression evaluation result that immediately
 * follows the first condition expression that evaluates to {@link java.lang.Boolean#TRUE}. If no
 * condition expression evaluates to {@link java.lang.Boolean#TRUE}, this function will return the
 * expression evaluation of the default expression, if present. If no condition expression evaluates
 * to {@link java.lang.Boolean#TRUE}, and either there is no default expression, or the default
 * expression evaluates to null, then a null value is returned.
 *
 * <p>Format:
 *
 * <pre>
 *     ["case", &lt;condition expression&gt;, &lt;output expression&gt;, &lt;condition expression&gt;, &lt;output expression&gt;, ..., &lt;default expression&gt;]
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
 *     <td>["case", false, "output1", true, "output2", "default"]</td>
 *     <td align="center">"output2"</td>
 *   </tr>
 *   <tr>
 *     <td>["case", false, "output1", false, "output2", "default"]</td>
 *     <td align="center">"default"</td>
 *   </tr>
 *   <tr>
 *     <td>["case", false, "output1", false, "output2"]</td>
 *     <td align="center">null</td>
 *   </tr>
 * </table>
 */
class CaseFunction extends FunctionExpressionImpl {

    public static FunctionName NAME = new FunctionNameImpl("case");

    CaseFunction() {
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
        int conditionExprssionIndex = 0;
        int outputExpressionIndex = 1;
        while (outputExpressionIndex < this.params.size()) {
            // get the condition expression and see if it evaluates to TRUE
            Object condition = params.get(conditionExprssionIndex).evaluate(feature);
            if (Boolean.TRUE.equals(condition)) {
                // condition passes, return output evaluation.
                return params.get(outputExpressionIndex).evaluate(feature);
            }
            // condition didn't evaluate to TRUE, try the next one.
            conditionExprssionIndex += 2;
            outputExpressionIndex += 2;
        }
        // if we are here, we have't found a condition that evaluated to TRUE. Return the default,
        // if one exists.
        if (conditionExprssionIndex < params.size()) {
            return params.get(conditionExprssionIndex).evaluate(feature);
        }
        // no conditions evaluated to TRUE and no default... problem
        return null;
    }
}
