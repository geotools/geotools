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

public class MBMath extends MBExpression {
    public MBMath(JSONArray json) {
        super(json);
    }

    /**
     * For two inputs, returns the result of subtracting the second input from the first. For a single input,
     * returns the result of subtracting it from 0.
     * Example:
     *   ["-", number, number]: number
     *   ["-", number]: number
     * @return
     */
    public Expression mathSubtract(){
        return null;
    }

    /**
     * Returns the product of the inputs.
     * Example: ["*", number, number, ...]: number
     * @return
     */
    public Expression mathMultiply(){
        return null;
    }

    /**
     * Returns the result of floating point division of the first input by the second.
     * Example: ["/", number, number]: number
     * @return
     */
    public Expression mathDivide(){
        return null;
    }

    /**
     * Returns the remainder after integer division of the first input by the second.
     * Example: ["%", number, number]: number
     * @return
     */
    public Expression mathRemainder(){
        return null;
    }

    /**
     * Returns the result of raising the first input to the power specified by the second.
     * Example: ["^", number, number]: number
     * @return
     */
    public Expression mathExponent(){
        return null;
    }

    /**
     * Returns the sum of the inputs.
     * Example: ["+", number, number]: number
     * @return
     */
    public Expression mathAdd(){
        return null;
    }

    /**
     * Returns the arccosine of the input.
     * Example: ["acos", number]: number
     * @return
     */
    public Expression mathAcos(){
        return null;
    }

    /**
     * Returns the arcsine of the input.
     * Example: ["asin", number]: number
     * @return
     */
    public Expression mathAsin(){
        return null;
    }

    /**
     * Returns the arctangent of the input.
     * Example: ["atan", number]: number
     * @return
     */
    public Expression mathAtan(){
        return null;
    }

    /**
     * Returns the cosine of the input.
     * Example: ["cos", number]: number
     * @return
     */
    public Expression mathCos(){
        return null;
    }

    /**
     * Returns the mathematical constant e.
     * Example: ["e"]: number
     * @return
     */
    public Expression mathE(){
        return null;
    }

    /**
     * Returns the natural logarithm of the input.
     * Example: ["ln", number]: number
     * @return
     */
    public Expression mathLn(){
        return null;
    }

    /**
     * Returns mathematical constant ln(2).
     * Example: ["ln2"]: number
     * @return
     */
    public Expression mathLn2(){
        return null;
    }

    /**
     * Returns the base-ten logarithm of the input.
     * Example: ["log10", number]: number
     * @return
     */
    public Expression mathLog10(){
        return null;
    }

    /**
     * Returns the base-two logarithm of the input.
     * Example: ["log2", number]: number
     * @return
     */
    public Expression mathLog2(){
        return null;
    }

    /**
     * Returns the maximum value of the inputs.
     * Example: ["max", number, number, ...]: number
     * @return
     */
    public Expression mathMax(){
        return null;
    }

    /**
     * Returns the minimum value of the inputs.
     * Example: ["min", number, number, ...]: number
     * @return
     */
    public Expression mathMin(){
        return null;
    }

    /**
     * Returns the mathematical constant pi.
     * Example: ["pi"]: number
     * @return
     */
    public Expression mathPi(){
        return null;
    }

    /**
     * Returns the sine of the input.
     * Example: ["sin", number]: number
     * @return
     */
    public Expression mathSin(){ return null; }

    /**
     * Returns the square root of the input.
     * Example: ["sqrt", number]: number
     * @return
     */
    public Expression mathSqrt(){ return null; }

    /**
     * Returns the tangent of the input.
     * Example: ["tan", number]: number
     * @return
     */
    public Expression mathTan(){ return null; }

    @Override
    public Expression getExpression()throws MBFormatException {
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
