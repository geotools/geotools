/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;

/**
 * The function checks whether a candidate value is contained in a list object.
 *
 * <p>If the candidate value is found, the function returns <code>true</code>; otherwise, it returns <code>false</code>.
 *
 * @author Niels Charlier
 */
public class LinFunction extends FunctionExpressionImpl {

    public static FunctionName NAME = new FunctionNameImpl(
            "lin",
            parameter("result", Boolean.class, "Result", "whether the item is in the list"),
            parameter("item", Object.class, "item", "The item"),
            parameter("source", List.class, "Source", "The list"));

    public LinFunction() {
        super(NAME);
    }

    @Override
    public Object evaluate(Object feature) {
        Object item = getExpression(0).evaluate(feature, Object.class);

        List<?> source = getExpression(1).evaluate(feature, List.class);
        if (source == null) {
            return false;
        }

        return source.contains(item);
    }
}
