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

public class MBMath extends MBExpression {

    public MBMath(JSONArray json) {
        super(json);
    }

    /**
     * For two inputs, returns the result of subtracting the second input from the first. For a
     * single input, returns the result of subtracting it from 0. Example: ["-", number, number]:
     * number ["-", number]: number
     */
    public Expression mathSubtract() {
        Expression e1 = parse.string(json, 1);
        Expression e2 = parse.string(json, 2);
        return ff.subtract(e1, e2);
    }

    /** Returns the product of the inputs. Example: ["*", number, number, ...]: number */
    public Expression mathMultiply() {
        Expression first = parse.string(json, 1);
        // Identiy function for multiplication, in case we only have 1 expression to multiply
        Expression multiplyFunction = ff.multiply(first, ff.literal(1));
        for (int i = 2; i < json.size(); ++i) {
            Expression next = parse.string(json, i);
            multiplyFunction = ff.multiply(multiplyFunction, next);
        }
        return multiplyFunction;
    }

    /**
     * Returns the result of floating point division of the first input by the second. Example:
     * ["/", number, number]: number
     */
    public Expression mathDivide() {
        Expression e1 = parse.string(json, 1);
        Expression e2 = parse.string(json, 2);
        return ff.divide(e1, e2);
    }

    /**
     * Returns the remainder after integer division of the first input by the second. Example: ["%",
     * number, number]: number
     */
    public Expression mathRemainder() {
        Expression e1 = parse.string(json, 1);
        Expression e2 = parse.string(json, 2);
        return ff.function("remainder", e1, e2);
    }

    /**
     * Returns the result of raising the first input to the power specified by the second. Example:
     * ["^", number, number]: number
     */
    public Expression mathExponent() {
        Expression e1 = parse.string(json, 1);
        Expression e2 = parse.string(json, 2);
        return ff.function("pow", e1, e2);
    }

    /** Returns the sum of the inputs. Example: ["+", number, number...]: number */
    public Expression mathAdd() {
        Expression first = parse.string(json, 1);
        // Identiy function for addition, in case we only have 1 expression to add
        Expression sumFunction = ff.add(first, ff.literal(0));
        for (int i = 2; i < json.size(); ++i) {
            Expression next = parse.string(json, i);
            sumFunction = ff.add(sumFunction, next);
        }
        return sumFunction;
    }

    /** Returns the arccosine of the input. Example: ["acos", number]: number */
    public Expression mathAcos() {
        Expression e1 = parse.string(json, 1);
        return ff.function("acos", e1);
    }

    /** Returns the arcsine of the input. Example: ["asin", number]: number */
    public Expression mathAsin() {
        Expression e1 = parse.string(json, 1);
        return ff.function("asin", e1);
    }

    /** Returns the arctangent of the input. Example: ["atan", number]: number */
    public Expression mathAtan() {
        Expression e1 = parse.string(json, 1);
        return ff.function("atan", e1);
    }

    /** Returns the cosine of the input. Example: ["cos", number]: number */
    public Expression mathCos() {
        Expression e1 = parse.string(json, 1);
        return ff.function("cos", e1);
    }

    /** Returns the mathematical constant e. Example: ["e"]: number */
    public Expression mathE() {
        return ff.literal(Math.E);
    }

    /** Returns the natural logarithm of the input. Example: ["ln", number]: number */
    public Expression mathLn() {
        Expression e1 = parse.string(json, 1);
        return ff.function("log", e1);
    }

    /** Returns mathematical constant ln(2). Example: ["ln2"]: number */
    public Expression mathLn2() {
        return ff.literal(Math.log(2));
    }

    /** Returns the base-ten logarithm of the input. Example: ["log10", number]: number */
    public Expression mathLog10() {
        Expression e1 = parse.string(json, 1);
        return ff.divide(ff.function("log", e1), ff.function("log", ff.literal(10)));
    }

    /** Returns the base-two logarithm of the input. Example: ["log2", number]: number */
    public Expression mathLog2() {
        Expression e1 = parse.string(json, 1);
        return ff.divide(ff.function("log", e1), ff.function("log", ff.literal(2)));
    }

    /** Returns the maximum value of the inputs. Example: ["max", number, number, ...]: number */
    public Expression mathMax() {
        Expression first = parse.string(json, 1);
        // Identiy function for max, in case we only have 1 expression to add
        Expression maxFunction = ff.function("max", first, first);
        for (int i = 2; i < json.size(); ++i) {
            Expression next = parse.string(json, i);
            maxFunction = ff.function("max", maxFunction, next);
        }
        return maxFunction;
    }

    /** Returns the minimum value of the inputs. Example: ["min", number, number, ...]: number */
    public Expression mathMin() {
        Expression first = parse.string(json, 1);
        // Identiy function for min, in case we only have 1 expression to add
        Expression minFunction = ff.function("min", first, first);
        for (int i = 2; i < json.size(); ++i) {
            Expression next = parse.string(json, i);
            minFunction = ff.function("min", minFunction, next);
        }
        return minFunction;
    }

    /** Returns the mathematical constant pi. Example: ["pi"]: number */
    public Expression mathPi() {
        return ff.function("pi");
    }

    /** Returns the sine of the input. Example: ["sin", number]: number */
    public Expression mathSin() {
        Expression e1 = parse.string(json, 1);
        return ff.function("sin", e1);
    }

    /** Returns the square root of the input. Example: ["sqrt", number]: number */
    public Expression mathSqrt() {
        Expression e1 = parse.string(json, 1);
        return ff.function("sqrt", e1);
    }

    /** Returns the tangent of the input. Example: ["tan", number]: number */
    public Expression mathTan() {
        Expression e1 = parse.string(json, 1);
        return ff.function("tan", e1);
    }

    @Override
    public Expression getExpression() throws MBFormatException {
        switch (name) {
            case "-":
                return mathSubtract();
            case "*":
                return mathMultiply();
            case "/":
                return mathDivide();
            case "%":
                return mathRemainder();
            case "^":
                return mathExponent();
            case "+":
                return mathAdd();
            case "acos":
                return mathAcos();
            case "asin":
                return mathAsin();
            case "atan":
                return mathAtan();
            case "cos":
                return mathCos();
            case "e":
                return mathE();
            case "ln":
                return mathLn();
            case "ln2":
                return mathLn2();
            case "log10":
                return mathLog10();
            case "log2":
                return mathLog2();
            case "max":
                return mathMax();
            case "min":
                return mathMin();
            case "pi":
                return mathPi();
            case "sin":
                return mathSin();
            case "sqrt":
                return mathSqrt();
            case "tan":
                return mathTan();
            default:
                throw new MBFormatException(name + " is an unsupported math expression");
        }
    }
}
