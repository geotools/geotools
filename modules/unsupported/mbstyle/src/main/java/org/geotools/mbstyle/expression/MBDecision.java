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
import org.json.simple.JSONArray;
import org.opengis.filter.expression.Expression;

/**
 * The expressions in this section can be used to add conditional logic to your styles. For example,
 * the 'case' expression provides basic "if/then/else" logic, and 'match' allows you to map specific
 * values of an input expression to different output expressions.
 */
public class MBDecision extends MBExpression {

    // static String operators
    private static final String NOT = "!";
    private static final String NOT_EQUALS = "!=";
    private static final String LESS_THAN = "<";
    private static final String LESS_THAN_EQUALS = "<=";
    private static final String EQUALS = "==";
    private static final String GREATER_THAN = ">";
    private static final String GREATER_THAN_EQUALS = ">=";
    private static final String ALL = "all";
    private static final String ANY = "any";
    private static final String CASE = "case";
    private static final String COALESCE = "coalesce";
    private static final String MATCH = "match";

    public MBDecision(JSONArray json) {
        super(json);
    }

    /**
     * Logical negation. Returns true if the input is false, and false if the input is true.
     * Example: ["!", boolean]: boolean
     */
    private Expression decisionNot() {
        // validate the arg list
        if (json.size() != 2) {
            throwUnexpectedArgumentCount(NOT, 1);
        }
        // second argument better be a Boolean, or another expression that results in a Boolean
        Expression boolArg = parse.string(json, 1);
        // return the opposite of the arg
        return ff.function("not", boolArg);
    }

    /**
     * Returns true if the input values are not equal, false otherwise. The inputs must be numbers,
     * strings, or booleans, and both of the same type. Examples:["!=", number, number]: boolean
     * ["!=", string, string]: boolean ["!=", boolean, boolean]: boolean ["!=", null, null]: boolean
     */
    private Expression decisionNotEqual() {
        // validate the arg list
        if (json.size() != 3) {
            throwUnexpectedArgumentCount(NOT_EQUALS, 2);
        }
        // get the comparables
        Expression comparable1 = parse.string(json, 1);
        Expression comparable2 = parse.string(json, 2);
        return ff.function("mbNotEqualTo", comparable1, comparable2);
    }

    /**
     * Returns true if the first input is strictly less than the second, false otherwise. The inputs
     * must be numbers or strings, and both of the same type. Examples: ["<", number, number]:
     * boolean ["<", string, string]: boolean
     */
    private Expression decisionLessThan() {
        // validate the arg list
        if (json.size() != 3) {
            throwUnexpectedArgumentCount(LESS_THAN, 2);
        }
        Expression firstArgument = parse.string(json, 1);
        Expression secondArgument = parse.string(json, 2);
        return ff.function("lessThan", firstArgument, secondArgument);
    }

    /**
     * Returns true if the first input is less than or equal to the second, false otherwise. The
     * inputs must be numbers or strings, and both of the same type. Examples: ["<=", number,
     * number]: boolean ["<=", string, string]: boolean
     */
    private Expression decisionLessEqualThan() {
        // validate the arg list
        if (json.size() != 3) {
            throwUnexpectedArgumentCount(LESS_THAN_EQUALS, 2);
        }
        Expression firstArgument = parse.string(json, 1);
        Expression secondArgument = parse.string(json, 2);
        return ff.function("lessEqualThan", firstArgument, secondArgument);
    }

    /**
     * Returns true if the input values are equal, false otherwise. The inputs must be numbers,
     * strings, or booleans, and both of the same type. Examples: ["==", number, number]: boolean
     * ["==", string, string]: boolean ["==", boolean, boolean]: boolean ["==", null, null]: boolean
     */
    private Expression decisionEqualTo() {
        // validate the arg list
        if (json.size() != 3) {
            throwUnexpectedArgumentCount(EQUALS, 2);
        }
        // get the comparables
        Expression comparable1 = parse.string(json, 1);
        Expression comparable2 = parse.string(json, 2);
        return ff.function("mbEqualTo", comparable1, comparable2);
    }

    /**
     * Returns true if the first input is strictly greater than the second, false otherwise. The
     * inputs must be numbers or strings, and both of the same type. Example: [">", number, number]:
     * boolean [">", string, string]: boolean
     */
    private Expression decisionGreaterThan() {
        // validate the arg list
        if (json.size() != 3) {
            throwUnexpectedArgumentCount(GREATER_THAN, 2);
        }
        Expression firstArgument = parse.string(json, 1);
        Expression secondArgument = parse.string(json, 2);
        return ff.function("greaterThan", firstArgument, secondArgument);
    }

    /**
     * Returns true if the first input is greater than or equal to the second, false otherwise. The
     * inputs must be numbers or strings, and both of the same type. Example: [">=", number,
     * number]: boolean [">=", string, string]: boolean
     */
    private Expression decisionGreaterEqualThan() {
        // validate the arg list
        if (json.size() != 3) {
            throwUnexpectedArgumentCount(GREATER_THAN_EQUALS, 2);
        }
        Expression firstArgument = parse.string(json, 1);
        Expression secondArgument = parse.string(json, 2);
        return ff.function("greaterEqualThan", firstArgument, secondArgument);
    }

    /**
     * Returns true if all the inputs are true, false otherwise. The inputs are evaluated in order,
     * and evaluation is short-circuiting: once an input expression evaluates to false, the result
     * is false and no further input expressions are evaluated. Example: [""all"", boolean,
     * boolean]: boolean [""all"", boolean, boolean, ...]: boolean
     */
    private Expression decisionAll() {
        // validate the arg list
        if (json.size() < 2) {
            throwInsufficientArgumentCount(ALL, 1);
        }
        Expression[] expressions = new Expression[json.size() - 1];
        for (int i = 1; i < json.size(); ++i) {
            Expression expression = parse.string(json, i);
            expressions[i - 1] = expression;
        }
        return ff.function("all", expressions);
    }

    /**
     * Returns true if any of the inputs are true, false otherwise. The inputs are evaluated in
     * order, and evaluation is short-circuiting: once an input expression evaluates to true, the
     * result is true and no further input expressions are evaluated. Example: [""any"", boolean,
     * boolean]: boolean [""any"", boolean, boolean, ...]: boolean
     */
    private Expression decisionAny() {
        // validate the arg list
        if (json.size() < 2) {
            throwInsufficientArgumentCount(ALL, 1);
        }
        Expression[] expressions = new Expression[json.size() - 1];
        for (int i = 1; i < json.size(); ++i) {
            Expression expression = parse.string(json, i);
            expressions[i - 1] = expression;
        }
        return ff.function("any", expressions);
    }

    /**
     * Selects the first output whose corresponding test condition evaluates to true. Example:
     * [""case"", condition: boolean, output: OutputType, ...condition: boolean, output: OutputType,
     * ...default: OutputType]: OutputType
     */
    private Expression decisionCase() {
        // validate the arg list
        if (json.size() < 3) {
            throwInsufficientArgumentCount(ALL, 2);
        }
        Expression[] expressions = new Expression[json.size() - 1];
        for (int i = 1; i < json.size(); ++i) {
            Expression expression = parse.string(json, i);
            expressions[i - 1] = expression;
        }
        return ff.function("case", expressions);
    }

    /**
     * Evaluates each expression in turn until the first non-null value is obtained, and returns
     * that value. Example: ["coalesce", OutputType, OutputType, ...]: OutputType
     */
    private Expression decisionCoalesce() {
        // validate the arg list
        if (json.size() < 2) {
            throwInsufficientArgumentCount(COALESCE, 1);
        }
        Expression[] expressions = new Expression[json.size() - 1];
        for (int i = 1; i < json.size(); ++i) {
            Expression expression = parse.string(json, i);
            expressions[i - 1] = expression;
        }
        return ff.function("coalesce", expressions);
    }

    /**
     * Selects the output whose label value matches the input value, or the fallback value if no
     * match is found. The input can be any string or number expression (e.g. ["get",
     * "building_type"]). Each label can either be a single literal value or an array of values.
     * Example: ["match", input: InputType (number or string), label_1: InputType | [InputType,
     * InputType, ...], output_1: OutputType, label_n: InputType | [InputType, InputType, ...],
     * output_n: OutputType, ..., default: OutputType]: OutputType
     */
    private Expression decisionMatch() {
        // validate the arg list
        if (json.size() < 4) {
            throwInsufficientArgumentCount(COALESCE, 3);
        }
        Expression[] expressions = new Expression[json.size() - 1];
        for (int i = 1; i < json.size(); ++i) {
            Expression expression = parse.string(json, i);
            expressions[i - 1] = expression;
        }
        return ff.function("match", expressions);
    }

    @Override
    public Expression getExpression() throws MBFormatException {
        switch (name) {
            case NOT:
                return decisionNot();
            case NOT_EQUALS:
                return decisionNotEqual();
            case LESS_THAN:
                return decisionLessThan();
            case LESS_THAN_EQUALS:
                return decisionLessEqualThan();
            case EQUALS:
                return decisionEqualTo();
            case GREATER_THAN:
                return decisionGreaterThan();
            case GREATER_THAN_EQUALS:
                return decisionGreaterEqualThan();
            case ALL:
                return decisionAll();
            case ANY:
                return decisionAny();
            case CASE:
                return decisionCase();
            case COALESCE:
                return decisionCoalesce();
            case MATCH:
                return decisionMatch();
            default:
                throw new MBFormatException(name + " is an unsupported decision expression");
        }
    }
}
