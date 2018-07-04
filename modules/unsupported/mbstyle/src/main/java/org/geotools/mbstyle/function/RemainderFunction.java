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
 * MapBox Expression function that computes the remainder operation on two {@link
 * java.lang.Integer}s.
 *
 * <p>Format:
 *
 * <pre>
 *     ["&#37;", &lt;expression&gt;, &lt;expression&gt;]
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
 *     <td>["&#37;", 10, 4]</td>
 *     <td align="center">2</td>
 *   </tr>
 *   <tr>
 *     <td>["&#37;", -1, -4]</td>
 *     <td align="center">-1</td>
 *   </tr>
 * </table>
 */
class RemainderFunction extends FunctionExpressionImpl {

    public static FunctionName NAME = new FunctionNameImpl("remainder");

    RemainderFunction() {
        super(NAME);
    }

    /** @see org.geotools.filter.FunctionExpressionImpl#equals(java.lang.Object) */
    @Override
    public Object evaluate(Object feature) {
        Integer dividend;
        Integer divisor;
        try {
            dividend = getExpression(0).evaluate(feature, Integer.class);
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "Filter Function problem for function remainder argument #0 - expected type Integer",
                    ex);
        }
        try {
            divisor = getExpression(1).evaluate(feature, Integer.class);
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "Filter Function problem for function remainder argument #1 - expected type Integer",
                    ex);
        }
        // can't divide by 0
        if (divisor.compareTo(0) == 0) {
            throw new IllegalArgumentException(
                    "Filter Function problem for function remainder argument #1 - expected non-zero Integer");
        }
        // get the remainder
        return Integer.valueOf(dividend.intValue() % divisor.intValue());
    }
}
