/*
 * GeoTools - The Open Source Java GIS Toolkit http://geotools.org
 * (C) 2004-2011, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 */

package org.geotools.appschema.filter.expression;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.util.factory.Hints;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

/**
 * {@link Function} to format a time given as a {@link Date} using a {@link SimpleDateFormat}
 * pattern in a time zone supported by {@link TimeZone}.
 *
 * <p>Parameters:
 *
 * <ol>
 *   <li>pattern: formatting pattern supported by {@link SimpleDateFormat}, for example
 *       "yyyy-MM-dd".
 *   <li>date: the {@link Date} for the time to be formatted or its string representation, for
 *       example "1948-01-01T00:00:00Z". A {@link RuntimeException} with be thrown if the date is
 *       malformed (and not null).
 *   <li>timezone: the name of a time zone supported by {@link TimeZone}, for example "UTC" or
 *       "Canada/Mountain". Note that unrecognised timezones will silently be converted to UTC.
 * </ol>
 *
 * <p>This function returns null if any parameter is null.
 *
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 */
public class FormatDateTimezoneFunction implements Function {

    public static final FunctionName NAME =
            new FunctionNameImpl("FormatDateTimezone", "pattern", "date", "timezone");

    private final Literal fallback;

    private final List<Expression> parameters;

    public FormatDateTimezoneFunction() {
        this(new ArrayList<Expression>(), null);
    }

    public FormatDateTimezoneFunction(List<Expression> parameters, Literal fallback) {
        this.parameters = parameters;
        this.fallback = fallback;
    }

    public Object accept(ExpressionVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    public Object evaluate(Object object) {
        return evaluate(object, Hints.class);
    }

    @SuppressWarnings("unchecked")
    public <T> T evaluate(Object object, Class<T> context) {
        if (parameters.size() != 3) {
            throw new RuntimeException(
                    getName() + ": wrong number of parameters (" + parameters.size() + " not 3)");
        }
        // return null if any parameter is null
        for (Expression p : parameters) {
            if (p.evaluate(object) == null) {
                return null;
            }
        }
        String pattern = parameters.get(0).evaluate(object, String.class);
        Date date = parameters.get(1).evaluate(object, Date.class);
        if (date == null) {
            // malformed date that was not null
            throw new RuntimeException(
                    getName()
                            + ": could not parse date: "
                            + (String) parameters.get(1).evaluate(object, String.class));
        }
        // if timezone is not understood it is silently set to UTC
        TimeZone timezone = TimeZone.getTimeZone(parameters.get(2).evaluate(object, String.class));
        // broken patterns corrupt output but do not cause exceptions
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setTimeZone(timezone);
        return (T) dateFormat.format(date);
    }

    public Literal getFallbackValue() {
        return fallback;
    }

    public FunctionName getFunctionName() {
        return NAME;
    }

    public String getName() {
        return NAME.getName();
    }

    public List<Expression> getParameters() {
        return Collections.unmodifiableList(parameters);
    }
}
