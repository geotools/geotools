/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.function;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.ExpressionVisitor;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.process.Process;
import org.geotools.util.Converters;

/**
 * A helper function helping to bridge the {@link Process} world with the {@link Function} one.
 *
 * <p>In particular this one is used to build a named parameter value, allowing to bridge between the positional
 * parameters of {@link Function} and the named parameters of {@link Process}.
 *
 * <p>The value in the returned map will vary depending on how many parameters are given:
 *
 * <ul>
 *   <li>The first parameter is always the process argument name
 *   <li>If there is no second parameter, the evaluation context is used as the value (normally that would be a Feature,
 *       feature collection or grid coverage
 *   <li>If there is is more than one param left after the argument name a collection containing them will be built and
 *       returned
 * </ul>
 *
 * @author Andrea Aime - GeoSolutions
 */
class ParameterFunction implements Function {

    static final FunctionName NAME = new FunctionNameImpl(
            "parameter",
            parameter("parameterMap", Map.class),
            parameter("argumentName", String.class),
            parameter("values", Object.class, 0, Integer.MAX_VALUE));

    Literal fallbackValue;

    List<Expression> parameters;

    ParameterFunction(Literal fallbackValue, List<Expression> parameters) {
        this.fallbackValue = fallbackValue;
        this.parameters = parameters;
    }

    @Override
    public Literal getFallbackValue() {
        return fallbackValue;
    }

    @Override
    public String getName() {
        return NAME.getName();
    }

    @Override
    public FunctionName getFunctionName() {
        return NAME;
    }

    @Override
    public List<Expression> getParameters() {
        return parameters;
    }

    @Override
    public Object accept(ExpressionVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    @Override
    public Object evaluate(Object object) {
        if (parameters.isEmpty()) {
            throw new IllegalArgumentException(
                    "The parameter function requires at " + "least one parameter, the argument name");
        }

        // get the param name
        String name = parameters.get(0).evaluate(object, String.class);
        if (name == null) {
            throw new IllegalArgumentException(
                    "The first function parameter should be a string, " + "the name of a process argument");
        }

        // get the other values
        Object value;
        if (parameters.size() == 1) {
            // we return the evaluation context itself
            value = object;
        } else if (parameters.size() == 2) {
            // a single value
            value = parameters.get(1).evaluate(object);
        } else {
            // a collection
            List<Object> values = new ArrayList<>();
            for (int i = 1; i < parameters.size(); i++) {
                Object o = parameters.get(i).evaluate(object);
                values.add(o);
            }

            value = values;
        }

        return Collections.singletonMap(name, value);
    }

    @Override
    public <T> T evaluate(Object object, Class<T> context) {
        return Converters.convert(evaluate(object), context);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (fallbackValue == null ? 0 : fallbackValue.hashCode());
        result = prime * result + (parameters == null ? 0 : parameters.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ParameterFunction other = (ParameterFunction) obj;
        if (fallbackValue == null) {
            if (other.fallbackValue != null) return false;
        } else if (!fallbackValue.equals(other.fallbackValue)) return false;
        if (parameters == null) {
            if (other.parameters != null) return false;
        } else if (!parameters.equals(other.parameters)) return false;
        return true;
    }
}
