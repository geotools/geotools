/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.transform;

import java.util.Map;

import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;

/**
 * Visits a filter and expands the transforming expressions into it
 * 
 * @author Andrea Aime - GeoSolutions 
 */
class TransformFilterVisitor extends DuplicatingFilterVisitor {

    private Map<String, Expression> expressions;

    public TransformFilterVisitor(Map<String, Expression> expressions) {
        this.expressions = expressions;
    }

    @Override
    public Object visit(PropertyName expression, Object extraData) {
        String name = expression.getPropertyName();
        Expression ex = expressions.get(name);
        if (ex == null) {
            return super.visit(expression, extraData);
        } else {
            // inject the actual expression into the filter
            return ex;
        }
    }
}
