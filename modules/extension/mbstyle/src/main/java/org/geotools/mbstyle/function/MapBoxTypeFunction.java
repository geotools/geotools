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
package org.geotools.mbstyle.function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.api.filter.expression.Expression;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.json.simple.JSONObject;

/**
 * Takes one or more arguments and returns the the first argument of type specified by the first string argument, throws
 * an exception if no arguments are of type specified.
 */
class MapBoxTypeFunction extends FunctionExpressionImpl {
    Class<?> type;

    public static final FunctionName NAME = new FunctionNameImpl("mbType");

    MapBoxTypeFunction() {
        super(NAME);
    }

    /** @see org.geotools.filter.FunctionExpressionImpl#setParameters(java.util.List) */
    @Override
    public void setParameters(List<Expression> params) {
        // set the parameters
        this.params = new ArrayList<>(params);
    }

    @Override
    public Object evaluate(Object feature) {
        // loop over the arguments and ensure at least one evaluates to a JSONObject
        String arg = this.params.get(0).evaluate(feature, String.class);
        type = type(arg);
        for (int i = 1; i <= this.params.size() - 1; i++) {
            Object evaluation = this.params.get(i).evaluate(feature);
            if (type.isAssignableFrom(evaluation.getClass())) {
                return evaluation;
            }
        }
        // couldn't find a JSONObject value
        throw new IllegalArgumentException("Function \"mbType\" failed with no arguments of type JSONObject");
    }

    public Class<?> type(String string) {
        switch (string) {
            case "array":
                return Collection.class;
            case "boolean":
                return Boolean.class;
            case "number":
                return Number.class;
            case "object":
                return JSONObject.class;
            case "string":
                return String.class;
        }
        throw new IllegalArgumentException("Requires argument of array, boolean, number, object or string");
    }
}
