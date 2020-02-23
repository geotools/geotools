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

import java.util.ArrayList;
import java.util.List;
import org.geotools.mbstyle.parse.MBFormatException;
import org.json.simple.JSONArray;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

/**
 * This class is an extension of the MBExpression containing the functions to support MapBox Style
 * String expressions. String operators "concat", "upcase", and "downcase" are used to manipulate
 * strings within a style.
 */
public class MBString extends MBExpression {

    public MBString(JSONArray json) {
        super(json);
    }

    /**
     * Returns a string consisting of the concatenation of the inputs. Example: ["concat", string,
     * string, ...]: string
     */
    public Expression stringConcat() throws MBFormatException {
        List<Expression> list = new ArrayList<>();
        for (int i = 1; i < json.size(); i++) {
            Expression arg = parse.string(json, i);
            if (arg instanceof Literal) {
                arg = transformLiteral(arg);
                list.add(arg);
            } else {
                list.add(arg);
            }
        }
        Expression[] args = new Expression[list.size()];
        args = list.toArray(args);
        Function function = ff.function("Concatenate", args);
        return function;
    }

    /**
     * Returns the input string converted to lowercase. Follows the Unicode Default Case Conversion
     * algorithm and the locale-insensitive case mappings in the Unicode Character Database.
     * Example: ["downcase", string]: string
     */
    public Expression stringDowncase() {
        if (parse.string(json, 1) != null) {
            Expression arg = parse.string(json, 1);
            if (arg instanceof Literal) {
                arg = transformLiteral(arg);
            }
            Expression function = ff.function("strToLowerCase", arg);
            return function;
        }
        throw new MBFormatException("Unable to downcase the value from " + parse.get(json, 1));
    }

    /**
     * Returns the input string converted to uppercase. Follows the Unicode Default Case Conversion
     * algorithm and the locale-insensitive case mappings in the Unicode Character Database.
     * ["upcase", string]: string
     */
    public Expression stringUpcase() {
        if (parse.string(json, 1) != null) {
            Expression arg = parse.string(json, 1);
            if (arg instanceof Literal) {
                arg = transformLiteral(arg);
            }
            Expression function = ff.function("strToUpperCase", arg);
            return function;
        }
        throw new MBFormatException("Unable to upcase the value from " + parse.get(json, 1));
    }

    /** Will return a function base on the mbexpression string name; */
    @Override
    public Expression getExpression() {
        switch (name) {
            case "concat":
                return stringConcat();
            case "downcase":
                return stringDowncase();
            case "upcase":
                return stringUpcase();
            default:
                throw new MBFormatException(name + " is an unsupported string function");
        }
    }
}
