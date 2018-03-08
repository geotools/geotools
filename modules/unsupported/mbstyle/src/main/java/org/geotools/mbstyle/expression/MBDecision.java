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

package org.geotools.mbstyle.expression;

import org.geotools.mbstyle.parse.MBFormatException;
import org.geotools.mbstyle.parse.MBObjectParser;
import org.json.simple.JSONArray;
import org.opengis.filter.expression.Expression;

/**
 * The expressions in this section can be used to add conditional logic to your styles. For example, the 'case'
 * expression provides basic "if/then/else" logic, and 'match' allows you to map specific values of an input
 * expression to different output expressions.
 */
public class MBDecision extends MBExpression {

    public MBDecision(JSONArray json) {
        super(json);
    }

    /**
     * Logical negation. Returns true if the input is false, and false if the input is true.
     * Example: ["!", boolean]: boolean
     * @return
     */
    public Expression decisionNot(){
        return null;
    }


    /**
     * Returns true if the input values are not equal, false otherwise.
     * The inputs must be numbers, strings, or booleans, and both of the same type.
     * Examples:["!=", number, number]: boolean
     *          ["!=", string, string]: boolean
     *          ["!=", boolean, boolean]: boolean
     *          ["!=", null, null]: boolean
     * @return
     */
    public Expression decisionNotEqual(){
        return null;
    }

    /**
     * Returns true if the first input is strictly less than the second, false otherwise.
     * The inputs must be numbers or strings, and both of the same type.
     * Examples: ["<", number, number]: boolean
     *           ["<", string, string]: boolean
     * @return
     */
    public Expression decisionLessThan(){
        return null;
    }

    /**
     * Returns true if the first input is less than or equal to the second, false otherwise.
     * The inputs must be numbers or strings, and both of the same type.
     * Examples: ["<=", number, number]: boolean
     *           ["<=", string, string]: boolean
     * @return
     */
    public Expression decisionLessEqualThan(){
        return null;
    }

    /**
     * Returns true if the input values are equal, false otherwise.
     * The inputs must be numbers, strings, or booleans, and both of the same type.
     * Examples: ["==", number, number]: boolean
     *           ["==", string, string]: boolean
     *           ["==", boolean, boolean]: boolean
     *           ["==", null, null]: boolean
     * @return
     */
    public Expression decisionEqualTo(){
        return null;
    }

    /**
     * Returns true if the first input is strictly greater than the second, false otherwise.
     * The inputs must be numbers or strings, and both of the same type.
     * Example: [">", number, number]: boolean
     *          [">", string, string]: boolean
     * @return
     */
    public Expression decisionGreaterThan(){
        return null;
    }

    /**
     * Returns true if the first input is greater than or equal to the second, false otherwise.
     * The inputs must be numbers or strings, and both of the same type.
     * Example: [">=", number, number]: boolean
     *          [">=", string, string]: boolean
     * @return
     */
    public Expression decisionGreaterEqualThan(){
        return null;
    }

    /**
     * Returns true if all the inputs are true, false otherwise. The inputs are evaluated in order, and evaluation is
     * short-circuiting: once an input expression evaluates to false, the result is false and no further input
     * expressions are evaluated.
     * Example: [""all"", boolean, boolean]: boolean
     *          [""all"", boolean, boolean, ...]: boolean
     * @return
     */
    public Expression decisionAll(){
        return null;
    }

    /**
     * Returns true if any of the inputs are true, false otherwise. The inputs are evaluated in order,
     * and evaluation is short-circuiting: once an input expression evaluates to true, the result is true and no
     * further input expressions are evaluated.
     * Example: [""any"", boolean, boolean]: boolean
     *          [""any"", boolean, boolean, ...]: boolean
     * @return
     */
    public Expression decisionAny(){
        return null;
    }

    /**
     * Selects the first output whose corresponding test condition evaluates to true.
     * Example: [""case"", condition: boolean, output: OutputType,
     *              ...condition: boolean, output: OutputType,
     *              ...default: OutputType]: OutputType
     * @return
     */
    public Expression decisionCase(){
        return null;
    }

    /**
     * Evaluates each expression in turn until the first non-null value is obtained, and returns that value.
     * Example: ["coalesce", OutputType, OutputType, ...]: OutputType
     * @return
     */
    public Expression decisionCoalesce(){
        return null;
    }

    /**
     * Selects the output whose label value matches the input value, or the fallback value if no match is found.
     * The input can be any string or number expression (e.g. ["get", "building_type"]).
     * Each label can either be a single literal value or an array of values.
     * Example: ["match",
     *              input: InputType (number or string),
     *              label_1: InputType | [InputType, InputType, ...], output_1: OutputType,
     *              label_n: InputType | [InputType, InputType, ...], output_n: OutputType,
     *               ..., default: OutputType]: OutputType
     * @return
     */
    public Expression decisionMatch(){
        return null;
    }

    public Expression getExpression()throws MBFormatException {
            switch (name) {
                case "!":
                    return decisionNot();
                case "!=":
                    return decisionNotEqual();
                case "<":
                    return decisionLessThan();
                case "<=":
                    return decisionLessEqualThan();
                case "==":
                    return decisionEqualTo();
                case ">":
                    return decisionGreaterThan();
                case ">=":
                    return decisionGreaterEqualThan();
                case "all":
                    return decisionAll();
                case "case":
                    return decisionCase();
                case "coalesce":
                    return decisionCoalesce();
                case "match":
                    return decisionMatch();
                default:
                    throw new MBFormatException(name + " is an unsupported decision expression");
            }
    }
}
