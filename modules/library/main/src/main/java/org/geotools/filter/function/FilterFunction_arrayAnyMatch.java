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

import java.util.Arrays;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;

public class FilterFunction_arrayAnyMatch extends FunctionExpressionImpl {
    public static FunctionName NAME =
            new FunctionNameImpl(
                    "arrayAnyMatch",
                    parameter(
                            "arrayAnyMatch",
                            Boolean.class,
                            "Any",
                            "Returns true if any array value matches candidate."),
                    parameter("array", Object[].class, "Array", "array of values"),
                    parameter("candidate", Object.class, "Candidate", "value to match with array"));

    public FilterFunction_arrayAnyMatch() {
        super(NAME);
    }

    public Object evaluate(Object feature) {
        Object[] array;
        Object candidate;

        try { // attempt to get array and perform conversion
            array = (Object[]) getExpression(0).evaluate(feature);
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function between argument #0 - expected type Object[]");
        }

        try { // attempt to get candidate and perform conversion
            candidate = (Object) getExpression(1).evaluate(feature);
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function between argument #1 - expected type Object");
        }

        return Boolean.valueOf(Arrays.asList(array).contains(candidate));
    }
}
