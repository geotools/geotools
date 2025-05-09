/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.appschema.filter.expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.ExpressionVisitor;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.data.complex.util.ComplexFeatureConstants;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.util.factory.Hints;

/**
 * This function redirects an attribute to be encoded as xlink:href, instead of being encoded as a full attribute. This
 * is useful in polymorphism, where static client property cannot be used when the encoding is conditional. This
 * function expects:
 *
 * <ol>
 *   <li>Expression: REFERENCE_VALUE (could be another function or literal)
 * </ol>
 *
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 */
public class ToXlinkHrefFunction implements Function {

    public static final FunctionName NAME = new FunctionNameImpl("toXlinkHref", "REFERENCE_VALUE");

    private final List<Expression> parameters;

    private final Literal fallback;

    public ToXlinkHrefFunction() {
        this(new ArrayList<>(), null);
    }

    public ToXlinkHrefFunction(List<Expression> parameters, Literal fallback) {
        this.parameters = parameters;
        this.fallback = fallback;
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
        return Collections.unmodifiableList(parameters);
    }

    @Override
    public Literal getFallbackValue() {
        return fallback;
    }

    @Override
    public Object accept(ExpressionVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    @Override
    public Object evaluate(Object object) {
        return evaluate(object, Hints.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T evaluate(Object object, Class<T> context) {
        return (T)
                new Hints(ComplexFeatureConstants.STRING_KEY, parameters.get(0).evaluate(object, String.class));
    }
}
