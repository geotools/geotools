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

package org.geotools.filter.function;

import static java.util.stream.Collectors.joining;
import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;

/**
 * The function computes the difference between two date objects (as a-b), in the specified time
 * unit (default to milliseconds). Supported time units are "s", "m", "h", "d" (for seconds,
 * minutes, hours, days respectively).
 */
public class DateDifferenceFunction extends FunctionExpressionImpl {

    static final Set<String> SUPPORTED_TIME_UNITS =
            new HashSet<>(Arrays.asList("s", "m", "h", "d"));
    static final String SUPPORTED_TIME_UNITS_STRING =
            SUPPORTED_TIME_UNITS.stream().collect(joining(","));

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "dateDifference",
                    parameter("result", Long.class),
                    parameter("a", Date.class),
                    parameter("b", Date.class),
                    parameter("timeUnits", String.class, 0, 1));

    public DateDifferenceFunction() {
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
    public Object evaluate(Object feature) {
        Date a = getExpression(0).evaluate(feature, Date.class);
        Date b = getExpression(1).evaluate(feature, Date.class);
        if (a == null || b == null) {
            return null;
        }
        TimeUnit timeUnit = getTimeUnit();
        return timeUnit.convert(a.getTime() - b.getTime(), TimeUnit.MILLISECONDS);
    }

    private TimeUnit getTimeUnit() {
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        if (params.size() == 3) {
            try {
                timeUnit = getExpression(2).evaluate(null, TimeUnit.class);
                if (timeUnit == null) {
                    throw new IllegalArgumentException(
                            "The specified timeUnit should be one of "
                                    + SUPPORTED_TIME_UNITS_STRING);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException(
                        "Unable to parse the specified timeUnit which should be one of "
                                + SUPPORTED_TIME_UNITS_STRING,
                        e);
            }
        }
        return timeUnit;
    }
}
