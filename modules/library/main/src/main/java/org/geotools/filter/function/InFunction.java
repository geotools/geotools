/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.filter.function;

import java.util.List;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.FunctionExpressionImpl;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;

/**
 * The function checks whether a candidate value is contained in an arbitrary long list of user
 * provided values.
 *
 * <p>If the candidate value is found, the function returns <code>true</code>; otherwise, it returns
 * <code>false</code>.
 *
 * @author Stefano Costa, GeoSolutions
 */
public class InFunction extends FunctionExpressionImpl {

    /**
     * Returns true if the expression is a function in the "in" family, that is, "in", "in2", "in3",
     * ...
     *
     * @param expression The expression to be tested
     */
    public static boolean isInFunction(Expression expression) {
        return expression instanceof InFunction
                || expression instanceof FilterFunction_in2
                || expression instanceof FilterFunction_in3
                || expression instanceof FilterFunction_in4
                || expression instanceof FilterFunction_in5
                || expression instanceof FilterFunction_in6
                || expression instanceof FilterFunction_in7
                || expression instanceof FilterFunction_in8
                || expression instanceof FilterFunction_in9
                || expression instanceof FilterFunction_in10;
    }

    /**
     * Returns filter capabilities for all the "in" functions
     *
     * @return a {@link FilterCapabilities} with all the functions in the "in" family
     */
    public static FilterCapabilities getInCapabilities() {
        FilterCapabilities caps = new FilterCapabilities();
        caps.addType(InFunction.class);
        caps.addType(FilterFunction_in2.class);
        caps.addType(FilterFunction_in3.class);
        caps.addType(FilterFunction_in4.class);
        caps.addType(FilterFunction_in5.class);
        caps.addType(FilterFunction_in6.class);
        caps.addType(FilterFunction_in7.class);
        caps.addType(FilterFunction_in8.class);
        caps.addType(FilterFunction_in9.class);
        caps.addType(FilterFunction_in10.class);

        return caps;
    }

    public static FunctionName NAME =
            functionName("in", "result:Boolean", "candidate:Object:1,1", "v:Object:1,");

    public InFunction() {
        super(NAME);
    }

    @Override
    public String getName() {
        return NAME.getName();
    }

    public int getArgCount() {
        return NAME.getArgumentCount();
    }

    public Object evaluate(Object feature) {
        Object candidate = getExpression(0).evaluate(feature);

        boolean result = false;
        List<Expression> valuesToTest =
                (List<Expression>) getParameters().subList(1, getParameters().size());
        for (Expression expression : valuesToTest) {
            Object value = expression.evaluate(feature);
            if (candidate == null) {
                result = StaticGeometry.isNull(value);
            } else {
                result = result || StaticGeometry.equalTo(candidate, value);
            }

            if (result) {
                break;
            }
        }

        return result;
    }
}
