/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.appschema.jdbc;

import org.geotools.appschema.filter.NestedAttributeExpression;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;

/**
 * Renames the specified attribute to a new target name, preserving the namespace context.
 *
 * @author Stefano Costa, GeoSolutions
 */
public class NamespaceAwareAttributeRenameVisitor extends DuplicatingFilterVisitor {

    String sourceProperty;

    String targetProperty;

    public NamespaceAwareAttributeRenameVisitor(String sourceProperty, String targetProperty) {
        super();
        this.sourceProperty = sourceProperty;
        this.targetProperty = targetProperty;
    }

    /**
     * Creates a copy of the input {@link NestedAttributeExpression} with renamed attributes.
     *
     * @param expression the expression to visit
     * @param extraData if an instance of {@link FilterFactory2} is passed, it is used to build the
     *     returned expression
     * @return a new {@link NestedAttributeExpression} expression with renamed attributes
     */
    public Expression visit(NestedAttributeExpression expression, Object extraData) {
        if (expression == null) return null;
        if (expression.getPropertyName().equals(sourceProperty)) {
            return getFactory(extraData).property(targetProperty, expression.getNamespaceContext());
        }
        return expression;
    }

    /**
     * Creates a copy of the input {@link PropertyName} expression with renamed attributes.
     *
     * @param expression the expression to visit
     * @param extraData if an instance of {@link FilterFactory2} is passed, it is used to build the
     *     returned expression
     * @return a new {@link PropertyName} expression with renamed attributes
     */
    @Override
    public Object visit(PropertyName expression, Object extraData) {
        if (expression.getPropertyName().equals(sourceProperty)) {
            return getFactory(extraData).property(targetProperty, expression.getNamespaceContext());
        }
        return getFactory(extraData)
                .property(expression.getPropertyName(), expression.getNamespaceContext());
    }
}
