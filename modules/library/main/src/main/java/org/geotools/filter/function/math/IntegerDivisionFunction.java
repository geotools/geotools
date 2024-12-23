/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.filter.function.math;

import org.geotools.api.filter.capability.FunctionName;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;

/** Implements an integer division */
public class IntegerDivisionFunction extends FunctionExpressionImpl {
    static FunctionName NAME = new FunctionNameImpl(
            "div",
            Integer.class,
            FunctionNameImpl.parameter("dividend", Integer.class),
            FunctionNameImpl.parameter("divisor", Integer.class));

    public IntegerDivisionFunction() {
        super(NAME);
    }

    @Override
    public Object evaluate(Object object) {
        Integer dividend = getExpression(0).evaluate(object, Integer.class);
        Integer divisor = getExpression(1).evaluate(object, Integer.class);

        if (dividend == null || divisor == null) {
            return null;
        }

        return dividend / divisor;
    }
}
