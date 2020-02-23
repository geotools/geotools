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
import org.json.simple.JSONObject;
import org.opengis.filter.expression.Expression;

/**
 * The expressions in this section are provided for the purpose of testing for and converting
 * between different data types like strings, numbers, and boolean values.
 *
 * <p>Often, such tests and conversions are unnecessary, but they may be necessary in some
 * expressions where the type of a certain sub-expression is ambiguous. They can also be useful in
 * cases where your feature data has inconsistent types; for example, you could use to-number to
 * make sure that values like ""1.5"" (instead of 1.5) are treated as numeric
 */
public class MBTypes extends MBExpression {
    public MBTypes(JSONArray json) {
        super(json);
    }

    /**
     * Asserts that the input is an array (optionally with a specific item type and length). If,
     * when the input expression is evaluated, it is not of the asserted type, then this assertion
     * will cause the whole expression to be aborted. Example: ["array", value]: array ["array",
     * type: "string" | "number" | "boolean", value]: array<type> ["array", type: "string" |
     * "number" | "boolean", N: number (literal), value ]: array<type, N>
     */
    public Expression typesArray() {
        return ff.function("mbType", exprList());
    }

    /**
     * Asserts that the input value is a boolean. If multiple values are provided, each one is
     * evaluated in order until a boolean is obtained. If none of the inputs are booleans, the
     * expression is an error. Example: ["boolean", value]: boolean ["boolean", value, fallback:
     * value, fallback: value, ...]: boolean
     */
    public Expression typesBoolean() {
        return ff.function("mbType", exprList());
    }

    /**
     * Provides a literal array or object value. Example: ["literal", [...] (JSON array literal)]:
     * array<T, N> ["literal", {...} (JSON object literal)]: Object
     */
    private Expression typesLiteral() {
        if (json.size() == 2) {
            if (json.get(1) instanceof JSONObject) {
                JSONObject object = (JSONObject) json.get(1);
                return ff.literal(object);
            } else if (json.get(1) instanceof JSONArray) {
                JSONArray arr = (JSONArray) json.get(1);
                return ff.literal(arr);
            } else {
                throw new MBFormatException(
                        "The \"literal\" expression requires a JSONObject or JSONArray but was "
                                + json.get(1).getClass());
            }
        }
        throw new MBFormatException("The \"literal\" expression requires exactly 1 argument");
    }

    /**
     * Asserts that the input value is a number. If multiple values are provided, each one is
     * evaluated in order until a number is obtained. If none of the inputs are numbers, the
     * expression is an error. Example: ["number", value]: number ["number", value, fallback: value,
     * fallback: value, ...]: number
     */
    public Expression typesNumber() {
        return ff.function("mbType", exprList());
    }

    /**
     * Asserts that the input value is an object. If multiple values are provided, each one is
     * evaluated in order until an object is obtained. If none of the inputs are objects, the
     * expression is an error. Example: ["object", value]: object ["object", value, fallback: value,
     * fallback: value, ...]: object
     */
    public Expression typesObject() {
        return ff.function("mbType", exprList());
    }

    /**
     * Asserts that the input value is a string. If multiple values are provided, each one is
     * evaluated in order until a string is obtained. If none of the inputs are strings, the
     * expression is an error. Example: ["string", value]: string ["string", value, fallback: value,
     * fallback: value, ...]: string
     */
    public Expression typesString() {
        return ff.function("mbType", exprList());
    }

    /**
     * Converts the input value to a boolean. The result is false when then input is an empty
     * string, 0, false, null, or NaN; otherwise it is true. Example: ["to-boolean", value]: boolean
     */
    public Expression typesToBoolean() {
        return ff.function("toBool", parse.string(json, 1));
    }

    /**
     * Converts the input value to a color. If multiple values are provided, each one is evaluated
     * in order until the first successful conversion is obtained. If none of the inputs can be
     * converted, the expression is an error. Example: ["to-color", value, fallback: value,
     * fallback: value, ...]: color
     */
    public Expression typesToColor() {
        return ff.function("toColor", exprList());
    }

    /**
     * Converts the input value to a number, if possible. If the input is null or false, the result
     * is 0. If the input is true, the result is 1. If the input is a string, it is converted to a
     * number as specified by the "ToNumber Applied to the String Type" algorithm of the ECMAScript
     * Language Specification. If multiple values are provided, each one is evaluated in order until
     * the first successful conversion is obtained. If none of the inputs can be converted, the
     * expression is an error. Example: ["to-number", value, fallback: value, fallback: value, ...]:
     * number
     */
    public Expression typesToNumber() {
        return ff.function("toNumber", exprList());
    }

    /**
     * Converts the input value to a string. If the input is null, the result is "null". If the
     * input is a boolean, the result is "true" or "false". If the input is a number, it is
     * converted to a string as specified by the "NumberToString" algorithm of the ECMAScript
     * Language Specification. If the input is a color, it is converted to a string of the form
     * "rgba(r,g,b,a)", where r, g, and b are numerals ranging from 0 to 255, and a ranges from 0 to
     * 1. Otherwise, the input is converted to a string in the format specified by the
     * JSON.stringify function of the ECMAScript Language Specification. Example: ["to-string",
     * value]: string
     */
    public Expression typesToString() {
        return ff.function("toString", parse.string(json, 1));
    }

    /**
     * Returns a string describing the type of the given value. Example: ["typeof", value]: string
     */
    public Expression typesTypeOf() {
        Expression value = parse.string(json, 1);
        return ff.function("mbTypeOf", value);
    }

    private Expression[] exprList() {
        // Build an array of Expression arguments for functions that accept multiple parameters.
        Expression[] args = new Expression[json.size()];
        for (Integer i = 0; i <= json.size() - 1; i++) {
            Expression obj = parse.string(json, i);
            args[i] = obj;
        }
        return args;
    }

    @Override
    public Expression getExpression() throws MBFormatException {
        switch (name) {
            case "array":
                return typesArray();
            case "boolean":
                return typesBoolean();
            case "literal":
                return typesLiteral();
            case "number":
                return typesNumber();
            case "object":
                return typesObject();
            case "string":
                return typesString();
            case "to-boolean":
                return typesToBoolean();
            case "to-color":
                return typesToColor();
            case "to-number":
                return typesToNumber();
            case "to-string":
                return typesToString();
            case "typeof":
                return typesTypeOf();
        }
        throw new MBFormatException(name + " is an unsupported types expression");
    }
}
