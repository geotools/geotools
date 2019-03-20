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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.And;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.Filter;
import org.opengis.filter.Not;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

/**
 * Amends the differences between our in-memory two-valued logic and the database own three-valued
 * logic making sure we treat null values just like in memory (avoiding the third "unknown" status)
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

    @Override
    public Object visit(Or filter, Object extraData) {
        if (filter.getChildren().size() < 2) {
            return super.visit(filter, extraData);
        }
        // this list contains a name -> List<filter> entry for any simple property name comparison,
        // and a filter -> visit(Filter) for any other case
        LinkedHashMap<Object, Object> grouped = new LinkedHashMap();
        int maxListSize = 0;
        for (Filter child : filter.getChildren()) {
            // not equal comparisons are simplified another way
            if (child instanceof PropertyIsNotEqualTo
                    || !(child instanceof BinaryComparisonOperator)) {
                grouped.put(child, child.accept(this, null));
            } else {
                String name = getComparisonPropertyName((BinaryComparisonOperator) child);
                if (name == null) {
                    // not a case we can simplify, visit and accumulate
                    grouped.put(child, child.accept(this, null));
                } else {
                    List<Filter> filters = (List<Filter>) grouped.get(name);
                    if (filters == null) {
                        filters = new ArrayList<>();
                        grouped.put(name, filters);
                    }
                    // just add the child, no need to clone it
                    filters.add(child);
                    maxListSize = Math.max(filters.size(), maxListSize);
                }
            }
        }

        // nothing to group, the classic translation can be used instead
        if (maxListSize < 2) {
            return super.visit(filter, extraData);
        }

        // some comparisons can be grouped, great
        List<Filter> children = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : grouped.entrySet()) {
            if (entry.getKey() instanceof String) {
                Not notNull = ff.not(ff.isNull(ff.property((String) entry.getKey())));
                List<Filter> filters = (List<Filter>) entry.getValue();
                if (filters.size() == 1) {
                    children.add(ff.and(filters.get(0), notNull));
                } else {
                    children.add(ff.and(ff.or(filters), notNull));
                }
            } else {
                children.add((Filter) entry.getValue());
            }
        }
        if (children.size() == 1) {
            return children.get(0);
        }
        return ff.or(children);
    }

    /**
     * Given a binary comparison, returns the property name in case the comparison is between a
     * property and a literal
     */
    private String getComparisonPropertyName(BinaryComparisonOperator filter) {
        if (filter.getExpression1() instanceof PropertyName
                && filter.getExpression2() instanceof Literal) {
            return ((PropertyName) filter.getExpression1()).getPropertyName();
        } else if (filter.getExpression2() instanceof PropertyName
                && filter.getExpression1() instanceof Literal) {
            return ((PropertyName) filter.getExpression2()).getPropertyName();
        }
        return null;
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
                    List<Filter> children = new ArrayList<>(and.getChildren());
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
