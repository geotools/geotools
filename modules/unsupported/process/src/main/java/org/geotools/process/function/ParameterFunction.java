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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.process.Process;
import org.geotools.util.Converters;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

/**
 * A helper function helping to bridge the {@link Process} world with the {@link Function} one.
 * <p>
 * In particular this one is used to build a named parameter value, allowing to bridge between the
 * positional parameters of {@link Function} and the named parameters of {@link Process}.
 * <p>
 * The value in the returned map will vary depending on how many parameters are given:
 * <ul>
 * <li>The first parameter is always the process argument name</li>
 * <li>If there is no second parameter, the evaluation context is used as the value (normally that
 * would be a Feature, feature collection or grid coverage</li>
 * <li>If there is is more than one param left after the argument name a collection containing them
 * will be built and returned</li>
 * </ul>
 * 
 * @author Andrea Aime - GeoSolutions
 */
class ParameterFunction implements Function {
    
    static final String NAME = "parameter"; 

    Literal fallbackValue;

    List<Expression> parameters;

    ParameterFunction(Literal fallbackValue, List<Expression> parameters) {
        this.fallbackValue = fallbackValue;
        this.parameters = parameters;
    }

    public Literal getFallbackValue() {
        return fallbackValue;
    }

    public String getName() {
        return NAME;
    }
    public FunctionName getFunctionName() {
        return new FunctionNameImpl(NAME, parameters.size() );
    }
    public List<Expression> getParameters() {
        return parameters;
    }

    public Object accept(ExpressionVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    public Object evaluate(Object object) {
        if (parameters.size() < 1) {
            throw new IllegalArgumentException("The parameter function requires at "
                    + "least one parameter, the argument name");
        }

        // get the param name
        String name = parameters.get(0).evaluate(object, String.class);
        if (name == null) {
            throw new IllegalArgumentException("The first function parameter should be a string, "
                    + "the name of a process argument");
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
            List<Object> values = new ArrayList<Object>();
            for (int i = 1; i < parameters.size(); i++) {
                Object o = parameters.get(i).evaluate(object);
                values.add(o);
            }

            value = values;
        }

        return Collections.singletonMap(name, value);
    }

    public <T> T evaluate(Object object, Class<T> context) {
        return Converters.convert(evaluate(object), context);
    }

}
