/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.util.Converters;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

/**
 * StringTemplate function, applies a regular expression with capturing groups, and them uses the
 * groups to build a new string based on the provided template.
 * <p>
 * This function expects four expressions in input, the first three of which are mandatory.
 * <ol>
 * <li>The string to be matched
 * <li>A regular expression, possibly using capturing groups
 * <li>A template using ${0} for the fully matched string, ${n} for the n-th matching group
 * <li>An optional value returned if the regular expresion does not match the input string
 * </ol>
 *
 * Here are some examples using CQL as the expression language:
 * <ol>
 * <li>
 * <code>stringTemplate('2002-03-01T13:00:00Z', '\d{4}-\d{2}-\d{2}T(\d{2}):\d{2}:\d{2}', '${1}')
 * will result in <code>13</code> (the only matching group returns the hour of the timestamp)
 * <li>
 * <code>stringTemplate('abcd', '\d{4}-\d{2}-\d{2}T(\d{2}):\d{2}:\d{2}', '${1}')</code> will return
 * <code>null</code> (no match)
 * <li>
 * <code>stringTemplate('abcd', '\d{4}-\d{2}-\d{2}T(\d{2}):\d{2}:\d{2}', '${1}', 'default')</code>
 * will return <code>default</code> (no match, but there is a default value </old>
 *
 */
public class StringTemplateFunction implements Function {

    private final List<Expression> parameters;

    private Pattern staticPattern;
    private final Literal fallback;
    volatile Object[] convertedValues;

    /** Make the instance of FunctionName available in a consistent spot. */
    public static final FunctionName NAME =
            new FunctionNameImpl("stringTemplate", "input", "pattern", "template", "defaultValue");

    public StringTemplateFunction() {
        this.parameters = new ArrayList<Expression>();
        this.fallback = null;
    }

    public StringTemplateFunction(List<Expression> parameters, Literal fallback) {
        this.parameters = parameters;
        this.fallback = fallback;

        // check for valid structure
        if (parameters.size() < 3) {
            throw new IllegalArgumentException(
                    "We need at least 3 input values, the input string, the regular expression, and the template");
        } else if (parameters.size() > 4) {
            throw new IllegalArgumentException(
                    "We need at least 3 or 4 input values, "
                            + parameters.size()
                            + " were given instead");
        }
    }

    public String getName() {
        return NAME.getName();
    }

    public FunctionName getFunctionName() {
        return NAME;
    }

    public List<Expression> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    public Object accept(ExpressionVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    public Object evaluate(Object object) {
        return evaluate(object, Object.class);
    }

    public <T> T evaluate(Object object, Class<T> context) {

        // get the default value
        String defaultValue;
        if (parameters.size() == 4) {
            defaultValue = parameters.get(3).evaluate(object, String.class);
        } else {
            defaultValue = null;
        }

        // the input string
        String input = parameters.get(0).evaluate(object, String.class);
        Pattern pattern = getPattern(object);
        String template = parameters.get(2).evaluate(object, String.class);
        String result = defaultValue;
        if (input != null && template != null && pattern != null) {
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                result = applyTemplate(template, matcher);
            }
        }

        if (context != null) {
            return Converters.convert(result, context);
        } else {
            return (T) result;
        }
    }

    private String applyTemplate(String template, Matcher matcher) {
        String result = template.replace("${0}", matcher.group());
        for (int i = 0; i < matcher.groupCount(); i++) {
            int groupIdx = i + 1;
            result = result.replace("${" + groupIdx + "}", matcher.group(groupIdx));
        }

        return result;
    }

    private Pattern getPattern(Object object) {
        if (staticPattern != null) {
            return staticPattern;
        }

        // see if we have a static pattern
        Expression pe = parameters.get(1);
        if (pe instanceof Literal) {
            String ps = pe.evaluate(null, String.class);
            if (ps == null) {
                staticPattern = null;
            } else {
                staticPattern = Pattern.compile(ps);
            }

            return staticPattern;
        } else {
            // dynamic evaluation
            String ps = pe.evaluate(object, String.class);
            if (ps == null) {
                return null;
            } else {
                return Pattern.compile(ps);
            }
        }
    }

    public Literal getFallbackValue() {
        return fallback;
    }

    /**
     * Creates a String representation of this Function with the function name and the arguments.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName());
        sb.append("(");
        List<org.opengis.filter.expression.Expression> params = getParameters();
        if (params != null) {
            org.opengis.filter.expression.Expression exp;
            for (Iterator<org.opengis.filter.expression.Expression> it = params.iterator();
                    it.hasNext(); ) {
                exp = it.next();
                sb.append("[");
                sb.append(exp);
                sb.append("]");
                if (it.hasNext()) {
                    sb.append(", ");
                }
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
