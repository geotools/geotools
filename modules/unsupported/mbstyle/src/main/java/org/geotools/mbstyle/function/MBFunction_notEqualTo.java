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
import org.opengis.filter.capability.FunctionName;

/**
 * MapBox Expression function that returns {@link java.lang.Boolean#FALSE} if two expressions are equivalent,
 * {@link java.lang.Boolean#TRUE} otherwise. It is slightly different from the GeoTools "notEqualTo" function as it
 * treats NULLs as equal (instead of not equal), and it does not compare Object.toString() values, which would result in
 * false equivalences for things like Boolean.TRUE compared to the literal string "true".
 * <p>
 * Format:
 * </p>
 * <pre>
 *     ["mbNotEqualTo", &lt;expression&gt;, &lt;expression&gt;]
 * </pre>
 * <p>
 * Examples:
 * </p>
 * <p>
 * <table border="1" cellpadding="3">
 *   <tr>
 *     <th align="center">Expression</th>
 *     <th align="center">Output</th>
 *   </tr>
 *   <tr>
 *     <td>["mbNotEqualTo", "aString", "aString"]</td>
 *     <td align="center">false</td>
 *   </tr>
 *   <tr>
 *     <td>["mbNotEqualTo", null, null]</td>
 *     <td align="center">false</td>
 *   </tr>
 *   <tr>
 *     <td>["mbNotEqualTo", true, "true"]</td>
 *     <td align="center">true</td>
 *   </tr>
 *   <tr>
 *     <td>["mbNotEqualTo", 5, 5.0]</td>
 *     <td align="center">false</td>
 *   </tr>
 * </table>
 * </p>
 */
class MBFunction_notEqualTo extends FunctionExpressionImpl {

    public static FunctionName NAME = new FunctionNameImpl("mbNotEqualTo",
        parameter("mbNotEqualTo", Boolean.class),
        parameter("a", Object.class),
        parameter("b", Object.class));

    MBFunction_notEqualTo() {
        super(NAME);
    }

    /**
     * @see org.geotools.filter.FunctionExpressionImpl#equals(java.lang.Object)
     */
    @Override
    public Object evaluate(Object feature) {
        Object arg0;
        Object arg1;

        try { // attempt to get value and perform conversion
            arg0 = (Object) getExpression(0).evaluate(feature);
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function equalTo argument #0 - expected type Object");
        }

        try { // attempt to get value and perform conversion
            arg1 = (Object) getExpression(1).evaluate(feature);
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function equalTo argument #1 - expected type Object");
        }

        return !MBFunctionUtil.argsEqual(arg0, arg1);
    }
}
