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
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.api.filter.expression.Expression;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.mbstyle.parse.MBFormatException;
import org.json.simple.JSONArray;

/**
 * MapBox Expression function that will return the output expression evaluation result of the output expression that is
 * associated with the first label that matches the Input expression evaluation value. Each provided label can be a
 * single expression, or an array of expressions. If no label matches or contains the Input expression evaluation value,
 * the default expression value is returned, if present. If no label matches or contains the Input expression evaluation
 * value, and no default expression is provided, null is returned.
 *
 * <p>Format:
 *
 * <pre>
 *     ["match", &lt;Input expression&gt;,
 *                 &lt;Label expression&gt;, &lt;Output expression&gt;,
 *                 [&lt;Label expression&gt;, &lt;Label expression&gt;, ...], &lt;Output expression&gt;,
 *                 &lt;Default expression&gt;]
 * </pre>
 *
 * <p>Examples:
 *
 * <p>
 *
 * <table border="1" cellpadding="3" summary="MatchFunction examples">
 *   <tr>
 *     <th align="center">Expression</th>
 *     <th align="center">Output</th>
 *   </tr>
 *   <tr>
 *     <td>["match", "anInput", "label1", "output1", "anInput", "output2", "defaultOutput"]</td>
 *     <td align="center">"output2"</td>
 *   </tr>
 *   <tr>
 *     <td>["match", "anInput", "label1", "output1", "label2", "output2", ["literal", ["anInput", "anotherInput"]], "output3", "defaultOutput"]</td>
 *     <td align="center">"output3"</td>
 *   </tr>
 *   <tr>
 *     <td>["match", "anInput", "label1", "output1", "label2", "output2", "defaultOutput"]</td>
 *     <td align="center">"defaultOutput"</td>
 *   </tr>
 *   <tr>
 *     <td>["match", "anInput", "label1", "output1", "label2", "output2", "label2", "output3"]</td>
 *     <td align="center">null</td>
 *   </tr>
 * </table>
 */
class MatchFunction extends FunctionExpressionImpl {

    public static final FunctionName NAME = new FunctionNameImpl("match");

    MatchFunction() {
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
        // get the first expression. It's the InputType to match.
        final Object inputType = params.get(0).evaluate(feature);
        // if it's not a Number or String, it's not formatted correctly
        if (inputType == null
                || !Number.class.isAssignableFrom(inputType.getClass())
                        && !String.class.isAssignableFrom(inputType.getClass())) {
            throw new MBFormatException(String.format(
                    "MBDecision \"match\" requires a number or string expression for input type, found %s",
                    inputType != null ? inputType.getClass().getName() : null));
        }
        int labelIndex = 1;
        int outputIndex = 2;
        while (outputIndex < params.size()) {
            // evaluate the label
            Object label = params.get(labelIndex).evaluate(feature);
            if (label != null) {
                // if the label is a Number or String, compare it to the InputType
                if (Number.class.isAssignableFrom(label.getClass())
                        || String.class.isAssignableFrom(label.getClass())
                                && MBFunctionUtil.argsEqual(label, inputType)) {
                    // found a matching label
                    return params.get(outputIndex).evaluate(feature);
                } else if (JSONArray.class.isAssignableFrom(label.getClass())) {
                    // potential label is an array. Loop through the values tosee if we have a match
                    final JSONArray jsonArray = (JSONArray) label;
                    if (jsonArray.contains(inputType)) {
                        // found a match
                        return params.get(outputIndex).evaluate(feature);
                    }
                }
            }
            // no match yet, increment indecies
            labelIndex += 2;
            outputIndex += 2;
        }
        // no label match, return default, if provided
        if (labelIndex < params.size()) {
            // we have a default
            return params.get(labelIndex).evaluate(feature);
        }
        // no label match and no default
        return null;
    }
}
