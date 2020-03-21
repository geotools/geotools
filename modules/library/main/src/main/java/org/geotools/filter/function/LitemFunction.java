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

import java.util.List;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;

/**
 * Select one item for a list. Allows expressions to be used as index (as opposed to the []
 * notation). The first item in the list is 0.
 *
 * @author Niels Charlier
 */
public class LitemFunction extends FunctionExpressionImpl {

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "litem",
                    parameter(
                            "result",
                            Object.class,
                            "Result",
                            "The item in the list on this position"),
                    parameter("source", List.class, "Source", "The list"),
                    parameter("index", Integer.class, "Index", "Zero-based position index"));

    public LitemFunction() {
        super(NAME);
    }

    @Override
    public Object evaluate(Object feature) {
        List<?> source = getExpression(0).evaluate(feature, List.class);
        if (source == null) {
            return null;
        }
        Integer index = getExpression(1).evaluate(feature, Integer.class);
        if (index == null) {
            throw new IllegalArgumentException("litem requires index");
        }

        return source.get(index);
    }
}
