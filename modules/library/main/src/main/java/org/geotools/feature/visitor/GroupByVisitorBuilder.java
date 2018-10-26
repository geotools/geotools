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
package org.geotools.feature.visitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.geotools.data.util.NullProgressListener;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.util.ProgressListener;

/**
 * Helper class to help building a valid group by visitor.
 *
 * <p>A valid group by visitor requires an aggregate attribute, an aggregate visitor and at least
 * one group by attribute.
 */
public final class GroupByVisitorBuilder {

    private Expression aggregateAttribute;
    private Aggregate aggregateVisitor;
    private List<Expression> groupByAttributes = new ArrayList<>();
    private ProgressListener progressListener;

    public GroupByVisitorBuilder withAggregateAttribute(
            int attributeTypeIndex, SimpleFeatureType type) {
        aggregateAttribute = toExpression(attributeTypeIndex, type);
        return this;
    }

    public GroupByVisitorBuilder withAggregateAttribute(
            String attributeName, SimpleFeatureType type) {
        aggregateAttribute = toExpression(attributeName, type);
        return this;
    }

    public GroupByVisitorBuilder withAggregateAttribute(Expression aggregateAttribute) {
        this.aggregateAttribute = aggregateAttribute;
        return this;
    }

    public GroupByVisitorBuilder withAggregateVisitor(Aggregate aggregateVisitor) {
        this.aggregateVisitor = aggregateVisitor;
        return this;
    }

    public GroupByVisitorBuilder withAggregateVisitor(String aggregateVisitorName) {
        this.aggregateVisitor = Aggregate.valueOfIgnoreCase(aggregateVisitorName);
        return this;
    }

    public GroupByVisitorBuilder withGroupByAttribute(
            int attributeTypeIndex, SimpleFeatureType type) {
        groupByAttributes.add(toExpression(attributeTypeIndex, type));
        return this;
    }

    public GroupByVisitorBuilder withGroupByAttribute(
            String attributeName, SimpleFeatureType type) {
        groupByAttributes.add(toExpression(attributeName, type));
        return this;
    }

    public GroupByVisitorBuilder withGroupByAttributes(
            Collection<String> attributesNames, SimpleFeatureType type) {
        for (String attributeName : attributesNames) {
            withGroupByAttribute(attributeName, type);
        }
        return this;
    }

    public GroupByVisitorBuilder withGroupByAttribute(Expression groupByAttribute) {
        groupByAttributes.add(groupByAttribute);
        return this;
    }

    public GroupByVisitorBuilder withProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
        return this;
    }

    private Expression toExpression(int attributeTypeIndex, SimpleFeatureType type) {
        FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);
        AttributeDescriptor attribute = type.getDescriptor(attributeTypeIndex);
        if (attribute == null) {
            throw new IllegalArgumentException(
                    "Attribute index '" + attributeTypeIndex + "' is not valid.");
        }
        return filterFactory.property(attribute.getLocalName());
    }

    private Expression toExpression(String attributeName, SimpleFeatureType type) {
        FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);
        AttributeDescriptor attribute = type.getDescriptor(attributeName);
        if (attribute == null) {
            throw new IllegalArgumentException("Attribute '" + attributeName + "' is not valid.");
        }
        return filterFactory.property(attribute.getLocalName());
    }

    /**
     * Create a group by visitor checking that all mandatory values are present.
     *
     * @return a new group by visitor
     */
    public GroupByVisitor build() {
        if (aggregateAttribute == null) {
            throw new IllegalArgumentException("An aggregate attribute is required.");
        }
        if (aggregateVisitor == null) {
            throw new IllegalArgumentException("An aggregate visitor is required.");
        }
        if (groupByAttributes == null || groupByAttributes.isEmpty()) {
            throw new IllegalArgumentException("At least one group by attribute is required.");
        }
        if (progressListener == null) {
            progressListener = new NullProgressListener();
        }
        return new GroupByVisitor(
                aggregateVisitor, aggregateAttribute, groupByAttributes, progressListener);
    }
}
