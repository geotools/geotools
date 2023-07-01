/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.stac.store;

import java.util.Date;
import java.util.Optional;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;

/**
 * Several parts of GeoTools build simple comparisons for temporal filters, CQL2 uses dedicated
 * operators instead, this visitor switches simple comparisons to temporal ones when a temporal
 * property is used.
 */
public class TemporalFilterVisitor extends DuplicatingFilterVisitor {

    SimpleFeatureType schema;

    public TemporalFilterVisitor(SimpleFeatureType schema) {
        this.schema = schema;
    }

    @Override
    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        if (isTimeProperty(filter.getExpression1()) || isTimeProperty(filter.getExpression2())) {
            return ff.tequals(filter.getExpression1(), filter.getExpression2());
        }

        return super.visit(filter, extraData);
    }

    /**
     * Checks if a property is a time property, users can override if they have more sophisticated
     * logic than matching a single property name
     *
     * @param expression
     * @return
     */
    protected boolean isTimeProperty(Expression expression) {
        if (!(expression instanceof PropertyName)) return false;
        String name = ((PropertyName) expression).getPropertyName();
        return Optional.ofNullable(name)
                .map(n -> schema.getDescriptor(name))
                .map(d -> d.getType().getBinding())
                .filter(c -> Date.class.isAssignableFrom(c))
                .isPresent();
    }
}
