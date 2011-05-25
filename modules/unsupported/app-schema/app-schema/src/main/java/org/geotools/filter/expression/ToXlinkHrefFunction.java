/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.geotools.data.complex.ComplexFeatureConstants;
import org.geotools.factory.Hints;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

/**
 * This function redirects an attribute to be encoded as xlink:href, instead of being encoded as a
 * full attribute. This is useful in polymorphism, where static client property cannot be used when
 * the encoding is conditional. This function expects:
 * <ol>
 * <li>Expression: REFERENCE_VALUE (could be another function or literal)
 * </ol>
 * 
 * @author Rini Angreani, CSIRO Earth Science and Resource Engineering
 * 
 *
 * @source $URL$
 */
public class ToXlinkHrefFunction implements Function {

    /**
     * Make the instance of FunctionName available in a consistent spot.
     */
    public static final FunctionName NAME = new Name();

    private final List<Expression> parameters;

    private final Literal fallback;

    /**
     * Describe how this function works. (should be available via FactoryFinder lookup...)
     */
    public static class Name implements FunctionName {

        public int getArgumentCount() {
            return 1;
        }

        public List<String> getArgumentNames() {
            return Arrays.asList(new String[] { getName(), "REFERENCE_VALUE" });
        }

        public String getName() {
            return "toXlinkHref";
        }
    };

    public ToXlinkHrefFunction() {
        this(new ArrayList<Expression>(), null);
    }

    public ToXlinkHrefFunction(List<Expression> parameters, Literal fallback) {
        this.parameters = parameters;
        this.fallback = fallback;
    }

    public String getName() {
        return "toXlinkHref";
    }

    public List<Expression> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    public Literal getFallbackValue() {
        return fallback;
    }

    public Object accept(ExpressionVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    public Object evaluate(Object object) {
        return evaluate(object, Hints.class);
    }

    @SuppressWarnings("unchecked")
    public <T> T evaluate(Object object, Class<T> context) {
        return (T) new Hints(ComplexFeatureConstants.STRING_KEY, parameters.get(0).evaluate(object,
                String.class));
    }

}
