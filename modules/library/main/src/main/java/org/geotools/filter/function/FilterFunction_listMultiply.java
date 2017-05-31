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
package org.geotools.filter.function;

import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import static org.geotools.filter.capability.FunctionNameImpl.parameter;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;

/**
 * Filter function to multiply a text list of numbers with a given factor.
 *
 * "1 2 3" * 2 = "2 4 6"
 *
 * @author Tobias Warneke
 */
public class FilterFunction_listMultiply extends FunctionExpressionImpl {

    public static FunctionName NAME = new FunctionNameImpl("listMultiply", String.class,
            parameter("factor", Number.class),
            parameter("list", String.class));

    public FilterFunction_listMultiply() {
        super(NAME);
    }

    @Override
    public Object evaluate(Object feature) {
        Number arg0;
        String arg1;

        try { // attempt to get value and perform conversion
            Object o = getExpression(0).evaluate(feature);
            if (o instanceof String) 
                arg0 = Double.valueOf((String)o);
            else
                arg0 = (Number) o;
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function listMultiply argument #0 - expected type Double");
        }

        try {
            // attempt to get value and perform conversion
            final Expression exprArg1 = getExpression(1);
            if (exprArg1 == null) {
                return null;
            }

            arg1 = (String) exprArg1.evaluate(feature);
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function listMultiply argument #1 - expected type String");
        }

        if (arg1 == null || arg1.length()==0) {
            return null;
        }

        String[] values = arg1.split(" ");
        StringBuilder b = new StringBuilder();

        for (int i = 0; i < values.length; i++) {
            if (b.length() != 0) {
                b.append(" ");
            }
            b.append(Double.valueOf(values[i]) * arg0.doubleValue());
        }

        return b.toString();
    }
}