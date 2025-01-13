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

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.api.filter.expression.Expression;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.util.Converters;

/**
 * The function checks whether a candidate value is contained in an arbitrary long list of user provided values.
 *
 * <p>If the candidate value is found, the function returns <code>true</code>; otherwise, it returns <code>false</code>.
 *
 * @author Stefano Costa, GeoSolutions
 */
public class InFunction extends FunctionExpressionImpl {

    static final Boolean FAST_IN_FUNCTION =
            Boolean.valueOf(System.getProperty("geotools.function.fastInFunction", "true"));

    private boolean staticSet = true;

    private Set<?> lookup;
    private Class<?> lastContext;

    /**
     * Returns true if the expression is a function in the "in" family, that is, "in", "in2", "in3", ...
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

    public static FunctionName NAME = functionName("in", "result:Boolean", "candidate:Object:1,1", "v:Object:1,");

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

    @Override
    public void setParameters(List<Expression> parameters) {
        super.setParameters(parameters);

        // see if the table is full of attribute independent expressions
        if (!FAST_IN_FUNCTION) return;
        synchronized (this) {
            staticSet = true;
            FilterAttributeExtractor extractor = new FilterAttributeExtractor();
            for (int i = 1; i < parameters.size(); i++) {
                Expression expression = parameters.get(i);
                if (expression != null) {
                    extractor.clear();
                    expression.accept(extractor, null);
                    if (!extractor.isConstantExpression()) {
                        staticSet = false;
                        break;
                    }
                }
            }
        }
    }

    @Override
    public Object evaluate(Object feature) {
        Object candidate = getExpression(0).evaluate(feature);
        Class<?> context = getContextFromCandidate(candidate);

        // lazy initialization of the lookup set
        if (FAST_IN_FUNCTION) {
            if (staticSet && context != null && (lookup == null || !lastContext.equals(context))) {
                synchronized (this) {
                    if (lookup == null) {
                        lastContext = context;
                        lookup = buildLookup(context);
                    }
                }
            }
            // if we have a lookup and the situation allows for optimization, go for the fast path
            if (context != null && lookup != null) {
                // numbers are converted to doubles, dates to java.util.Date
                Object converted = Converters.convert(candidate, context);
                if (converted != null) {
                    return lookup.contains(converted);
                }
            }
        }

        boolean result = false;
        List<Expression> valuesToTest =
                getParameters().subList(1, getParameters().size());
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

    private Class<?> getContextFromCandidate(Object candidate) {
        if (candidate == null) return Object.class;
        Class<?> target = candidate.getClass();
        if (Number.class.isAssignableFrom(target)) {
            return Double.class; // StaticGeometry.equalTo will convert to double
        } else if (Boolean.class.isAssignableFrom(target)) {
            return Boolean.class;
        } else if (java.sql.Timestamp.class.isAssignableFrom(target)) {
            return java.sql.Timestamp.class;
        } else if (java.sql.Date.class.isAssignableFrom(target)) {
            return java.sql.Date.class;
        } else if (java.sql.Time.class.isAssignableFrom(target)) {
            return java.sql.Time.class;
        } else if (Date.class.isAssignableFrom(target)) {
            return Date.class;
        } else if (String.class.isAssignableFrom(target)) {
            return String.class;
        }
        // chance of improper emulation of StaticGeometry.equalTo is too high, bail out
        return null;
    }

    private <T> Set<?> buildLookup(Class<T> context) {
        return params.subList(1, params.size()).stream()
                .map(e -> e.evaluate(null, context))
                .collect(Collectors.toSet());
    }
}
