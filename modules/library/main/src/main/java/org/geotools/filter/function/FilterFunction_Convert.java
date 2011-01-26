package org.geotools.filter.function;

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.filter.FunctionExpression;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.util.Converters;

public class FilterFunction_Convert extends FunctionExpressionImpl implements
        FunctionExpression {

    public FilterFunction_Convert() {
        super("convert");
    }

    public int getArgCount() {
        return 2;
    }

    public Object evaluate(Object feature) {
        try {
            Object arg = getExpression(0).evaluate(feature); 
            Class target = getExpression(1).evaluate(feature, Class.class);
            return Converters.convert(arg, target);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Filter Function problem for function convert argument #1 - expected type Class");
        }
    }
}
