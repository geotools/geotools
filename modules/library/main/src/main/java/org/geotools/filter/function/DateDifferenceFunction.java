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

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import java.util.Date;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;

/** The function computes the difference between two date objects, in milliseconds (as a-b) */
public class DateDifferenceFunction extends FunctionExpressionImpl {

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "dateDifference",
                    parameter("result", Long.class),
                    parameter("a", Date.class),
                    parameter("b", Date.class));

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

    public Object evaluate(Object feature) {
        Date a = getExpression(0).evaluate(feature, Date.class);
        Date b = getExpression(1).evaluate(feature, Date.class);

        if (a == null || b == null) {
            return null;
        }

        return a.getTime() - b.getTime();
    }
}
