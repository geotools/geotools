/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling.css.util;

import java.util.HashSet;
import java.util.Set;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.expression.VolatileFunction;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.filter.function.EnvFunction;
import org.geotools.filter.function.FilterFunction_property;
import org.geotools.filter.visitor.DefaultFilterVisitor;

/**
 * A subclass of {@link FilterAttributeExtractor} that computes a specificity score for the filter.
 * Besides counting the attributes being used, it applies special logic to the dynamic property
 * function, the env function, and volatile functions too
 */
public class FilterSpecificityExtractor extends DefaultFilterVisitor {

    Set<Expression> properties = new HashSet<>();

    @Override
    public Object visit(PropertyName expression, Object data) {
        properties.add(expression);
        return data;
    }

    @Override
    public Object visit(org.geotools.api.filter.expression.Function expression, Object data) {
        super.visit(expression, data);
        if (expression instanceof VolatileFunction) {
            // the volatile function is assumed to be the same as a property, since it returns
            // different values when we evaluate it
            properties.add(expression);
        }
        if (expression instanceof FilterFunction_property || expression instanceof EnvFunction) {
            // the contents of these is assumed to be one variable too
            properties.add(expression.getParameters().get(0));
        }
        return super.visit(expression, data);
    }
    ;

    public int getSpecificityScore() {
        return properties.size();
    }
}
