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

import java.util.Arrays;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FunctionImpl;
import org.geotools.mbstyle.parse.MBFormatException;
import org.geotools.mbstyle.parse.MBObjectParser;
import org.geotools.mbstyle.transform.MBStyleTransformer;
import org.json.simple.JSONArray;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;

/**
 * The value for any layout property, paint property, or filter may be specified as an expression.
 * An expression defines a formula for computing the value of the property using the operators
 * described below: -Mathematical operators for performing arithmetic and other operations on
 * numeric values -Logical operators for manipulating boolean values and making conditional
 * decisions -String operators for manipulating strings -Data operators, providing access to the
 * properties of source features -Camera operators, providing access to the parameters defining the
 * current map view
 *
 * <p>Expressions are represented as JSON arrays. The first element of an expression array is a
 * string naming the expression operator, e.g. "*"or "case". Subsequent elements (if any) are the
 * arguments to the expression. Each argument is either a literal value (a string, number, boolean,
 * or null), or another expression array.
 */
public abstract class MBExpression extends FunctionImpl {

    protected final JSONArray json;
    protected final String name;
    protected final FilterFactory2 ff;
    protected final MBObjectParser parse;
    protected final MBStyleTransformer transformer;

    public MBExpression(JSONArray json) {
        this.json = json;
        this.name = (String) json.get(0);
        this.ff = CommonFactoryFinder.getFilterFactory2();
        parse = new MBObjectParser(MBExpression.class);
        transformer = new MBStyleTransformer(parse);
    }

    public String getName() {
        return name;
    }

    /* A list of color expression names */
    public static List colors = Arrays.asList("rgb", "rgba", "to-rgba");

    /* A list of decision expression names */
    public static List decisions =
            Arrays.asList(
                    "!",
                    "!=",
                    "<",
                    "<=",
                    "==",
                    ">",
                    ">=",
                    "all",
                    "any",
                    "case",
                    "coalesce",
                    "match");

    /* A list of feature data expression names */
    public static List featureData = Arrays.asList("geometry-type", "id", "properties");

    /* A list of heatmap expression names */
    public static List heatMap = Arrays.asList("heatmap-density");

    /* A list of lookup expression names */
    public static List lookUp = Arrays.asList("at", "length", "has", "get");

    /* A list of math expression names */
    public static List math =
            Arrays.asList(
                    "-", "*", "/", "%", "^", "+", "acos", "asin", "atan", "cos", "e", "ln", "ln2",
                    "log10", "log2", "max", "min", "pi", "sin", "sqrt", "tan");

    /* A list of ramps expression names */
    public static List ramps = Arrays.asList("interpolate", "step");

    /* A list of string expression names */
    public static List string = Arrays.asList("concat", "downcase", "upcase");

    /* A list of types expression names */
    public static List types =
            Arrays.asList(
                    "array",
                    "boolean",
                    "literal",
                    "number",
                    "object",
                    "string",
                    "to-boolean",
                    "to-color",
                    "to-number",
                    "to-string",
                    "typeof");

    /* A list of variable bindings expression names */
    public static List variableBindings = Arrays.asList("let", "var");

    /* A list of zoom expression names */
    public static List zoom = Arrays.asList("zoom");

    public static MBExpression create(JSONArray json) {
        String name;
        if (json.get(0) instanceof String) {
            name = (String) json.get(0);

            if (colors.contains(name)) {
                return new MBColor(json);
            } else if (decisions.contains(name)) {
                return new MBDecision(json);
            } else if (featureData.contains(name)) {
                return new MBFeatureData(json);
            } else if (heatMap.contains(name)) {
                return new MBHeatmap(json);
            } else if (lookUp.contains(name)) {
                return new MBLookup(json);
            } else if (math.contains(name)) {
                return new MBMath(json);
            } else if (ramps.contains(name)) {
                return new MBRampsScalesCurves(json);
            } else if (string.contains(name)) {
                return new MBString(json);
            } else if (types.contains(name)) {
                return new MBTypes(json);
            } else if (variableBindings.contains(name)) {
                return new MBVariableBinding(json);
            } else if (zoom.contains(name)) {
                return new MBZoom(json);
            } else {
                throw new MBFormatException("Expression \"" + name + "\" invalid.");
            }
        }
        throw new MBFormatException("Requires a string name of the expression at position 0");
    }

    public static boolean canCreate(final String name) {
        return colors.contains(name)
                || decisions.contains(name)
                || featureData.contains(name)
                || heatMap.contains(name)
                || lookUp.contains(name)
                || math.contains(name)
                || ramps.contains(name)
                || string.contains(name)
                || types.contains(name)
                || variableBindings.contains(name)
                || zoom.contains(name);
    }

    /** Determines which expression to use. */
    public abstract Expression getExpression();

    /**
     * A function to evaluate a given parameter as an expression and use the MBStyleTransformer to
     * transform Mapbox tokens into CQL expressions.
     */
    public Expression transformLiteral(Expression ex) {
        String text = ex.evaluate(null, String.class);
        if (text.trim().isEmpty()) {
            ex = ff.literal(" ");
        } else {
            ex = transformer.cqlExpressionFromTokens(text);
        }
        return ex;
    }

    /** Creates an MBExpression and calls the associated function. */
    public static Expression transformExpression(JSONArray json) {
        return create(json).getExpression();
    }

    protected void throwUnexpectedArgumentCount(String expression, int argCount)
            throws MBFormatException {
        throw new MBFormatException(
                String.format(
                        "Expression \"%s\" should have exactly %d argument(s)",
                        expression, argCount));
    }

    protected void throwInsufficientArgumentCount(String expression, int argCount)
            throws MBFormatException {
        throw new MBFormatException(
                String.format(
                        "Expression \"%s\" should have at least %d argument(s)",
                        expression, argCount));
    }
}
