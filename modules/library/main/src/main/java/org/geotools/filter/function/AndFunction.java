/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import org.geotools.api.filter.capability.FunctionName;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;

/**
 * This function is used to evaluate a And operator with a left and right operands
 *
 * @author Erwan Bocher, CNRS, 2020
 */
public class AndFunction extends FunctionExpressionImpl {

    public static FunctionName NAME = new FunctionNameImpl(
            "And", Boolean.class, parameter("left", Boolean.class), parameter("right", Boolean.class));

    /** Creates a new instance of AndFunction */
    public AndFunction() {
        super(NAME);
    }

    @Override
    public Object evaluate(Object feature) {
        boolean left;
        boolean right;

        try { // attempt to get the left value and perform conversion
            left = getExpression(0).evaluate(feature, Boolean.class);
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function And argument #0 - expected type boolean");
        }

        try { // attempt to get the right value and perform conversion
            right = getExpression(1).evaluate(feature, Boolean.class);
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function And argument #1 - expected type boolean");
        }

        return left && right;
    }
}
