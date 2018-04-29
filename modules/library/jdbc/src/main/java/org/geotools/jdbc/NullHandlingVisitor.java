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
package org.geotools.jdbc;

import java.util.ArrayList;
import java.util.List;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.And;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.Filter;
import org.opengis.filter.Not;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;

/**
 * Amends the differences between our in-memory two-valued logic and the database own three-valued
 * logic making sure we treat null values just like in memory (avoiding the third "unkonwn" status)
 *
 * @author Andrea Aime - GeoSolutions
 */
class NullHandlingVisitor extends DuplicatingFilterVisitor {

    private FeatureType schema;

    /**
     * When providing the schema, the attributes will have null checks added only if they are marked
     * as nillable
     */
    public NullHandlingVisitor(FeatureType schema) {
        this.schema = schema;
    }

    public NullHandlingVisitor() {}

    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        return guardAgainstNulls((BinaryComparisonOperator) super.visit(filter, extraData));
    }

    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
        return guardAgainstNulls((BinaryComparisonOperator) super.visit(filter, extraData));
    }

    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        return guardAgainstNulls((BinaryComparisonOperator) super.visit(filter, extraData));
    }

    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        return guardAgainstNulls((BinaryComparisonOperator) super.visit(filter, extraData));
    }

    public Object visit(PropertyIsLessThan filter, Object extraData) {
        return guardAgainstNulls((BinaryComparisonOperator) super.visit(filter, extraData));
    }

    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        return guardAgainstNulls((BinaryComparisonOperator) super.visit(filter, extraData));
    }

    private Object guardAgainstNulls(BinaryComparisonOperator clone) {
        Filter result = guardAgainstNulls(clone, clone.getExpression1());
        result = guardAgainstNulls(result, clone.getExpression2());
        return result;
    }

    @Override
    public Object visit(PropertyIsLike filter, Object extraData) {
        PropertyIsLike clone = (PropertyIsLike) super.visit(filter, extraData);
        return guardAgainstNulls(filter, clone.getExpression());
    }

    public Object visit(PropertyIsBetween filter, Object extraData) {
        PropertyIsBetween clone = (PropertyIsBetween) super.visit(filter, extraData);
        Filter f = guardAgainstNulls(clone, clone.getExpression());
        f = guardAgainstNulls(f, clone.getLowerBoundary());
        f = guardAgainstNulls(f, clone.getUpperBoundary());
        return f;
    }

    /**
     * Guards the filter against potential null values in the target property name (if it is a
     * property name, to start with)
     */
    private Filter guardAgainstNulls(Filter filter, Expression potentialPropertyName) {
        if (potentialPropertyName instanceof PropertyName) {
            PropertyName pn = (PropertyName) potentialPropertyName;
            String name = pn.getPropertyName();
            if (isNillable(name)) {
                Not notNull = ff.not(ff.isNull(ff.property(name)));
                if (filter instanceof And) {
                    And and = (And) filter;
                    List<Filter> children = new ArrayList<Filter>(and.getChildren());
                    children.add(notNull);
                    return ff.and(children);
                } else {
                    return ff.and(filter, notNull);
                }
            }
        }

        return filter;
    }

    /**
     * Returns if a property can contain null values, or not. If we don't have the schema
     * information, or we don't know the property, we are going to assume the property is nillable
     * to stay on the safe side
     */
    private boolean isNillable(String name) {
        if (schema == null) {
            return true;
        }
        PropertyDescriptor descriptor = schema.getDescriptor(name);
        return descriptor == null || descriptor.isNillable();
    }
}
