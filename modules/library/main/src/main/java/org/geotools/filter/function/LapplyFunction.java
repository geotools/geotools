/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

import java.util.ArrayList;
import java.util.List;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;

/**
 * Apply an expression to each item of a list, resulting in a new list. Provide a list and an
 * expression as input, returns a new list as output. Use the "." symbol inside the expression to
 * refer to each item of the list.
 *
 * @author Niels Charlier
 */
public class LapplyFunction extends FunctionExpressionImpl {

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "lapply",
                    parameter("result", List.class, "Result", "The new list"),
                    parameter("source", List.class, "Source", "The original list"),
                    parameter(
                            "expression",
                            Object.class,
                            "Expression",
                            "Expression to apply to each item in the list."));

    public LapplyFunction() {
        super(NAME);
    }

    @Override
    public Object evaluate(Object feature) {
        List<?> source = getExpression(0).evaluate(feature, List.class);
        if (source == null) {
            return null;
        }

        List<Object> result = new ArrayList<Object>();
        for (Object item : source) {
            result.add(getExpression(1).evaluate(item));
        }

        return result;
    }
}
