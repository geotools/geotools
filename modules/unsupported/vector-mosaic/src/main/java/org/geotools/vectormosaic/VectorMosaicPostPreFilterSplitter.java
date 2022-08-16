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
package org.geotools.vectormosaic;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.filter.visitor.PostPreProcessFilterSplittingVisitor;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.Filter;
import org.opengis.filter.Id;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNil;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.temporal.BinaryTemporalOperator;

public class VectorMosaicPostPreFilterSplitter extends PostPreProcessFilterSplittingVisitor {
    private Set<String> attributeNames = new HashSet<>();

    /**
     * Constructor
     *
     * @param featureType The feature type to check attributes against
     */
    public VectorMosaicPostPreFilterSplitter(SimpleFeatureType featureType) {
        super(null, null, null);
        List<AttributeDescriptor> indexAttributeDescriptors = featureType.getAttributeDescriptors();
        indexAttributeDescriptors.stream()
                .map(AttributeDescriptor::getLocalName)
                .forEach(this.attributeNames::add);
    }

    /**
     * Validates that all attributes in the filter are in the featuretype
     *
     * @param filter the filter to validate
     * @return true if the filter attributes match featuretype is valid, false otherwise
     */
    private boolean containsAll(Filter filter) {
        FilterAttributeExtractor extractor = new FilterAttributeExtractor();
        filter.accept(extractor, null);
        return this.attributeNames.containsAll(extractor.getAttributeNameSet());
    }

    @Override
    protected void visitBinaryComparisonOperator(BinaryComparisonOperator filter) {
        if (!containsAll(filter)) {
            postStack.push(filter);
            return;
        }
        super.visitBinaryComparisonOperator(filter);
    }

    @Override
    protected void visitBinarySpatialOperator(BinarySpatialOperator filter) {
        if (!containsAll(filter)) {
            postStack.push(filter);
            return;
        }
        super.visitBinarySpatialOperator(filter);
    }

    @Override
    protected Object visit(BinaryTemporalOperator filter, Object data) {
        if (!containsAll(filter)) {
            postStack.push(filter);
            return data;
        }
        return super.visit(filter, data);
    }

    @Override
    public Object visit(BBOX filter, Object extraData) {
        if (!containsAll(filter)) {
            postStack.push(filter);
            return extraData;
        }
        return super.visit(filter, extraData);
    }

    @Override
    public Object visit(PropertyIsLike likeFilter, Object extraData) {
        if (!containsAll(likeFilter)) {
            postStack.push(likeFilter);
            return extraData;
        }
        return super.visit(likeFilter, extraData);
    }

    @Override
    public Object visit(PropertyIsBetween betweenFilter, Object extraData) {
        if (!containsAll(betweenFilter)) {
            postStack.push(betweenFilter);
            return extraData;
        }
        return super.visit(betweenFilter, extraData);
    }

    @Override
    public Object visit(ExcludeFilter excludeFilter, Object extraData) {
        if (!containsAll(excludeFilter)) {
            postStack.push(excludeFilter);
            return extraData;
        }
        return super.visit(excludeFilter, extraData);
    }

    @Override
    public Object visit(PropertyIsNil filter, Object extraData) {
        if (!containsAll(filter)) {
            postStack.push(filter);
            return extraData;
        }
        return super.visit(filter, extraData);
    }

    @Override
    public Object visit(PropertyIsNull filter, Object extraData) {
        if (!containsAll(filter)) {
            postStack.push(filter);
            return extraData;
        }
        return super.visit(filter, extraData);
    }

    @Override
    public Object visit(Id filter, Object extraData) {
        if (!containsAll(filter)) {
            postStack.push(filter);
            return extraData;
        }
        return super.visit(filter, extraData);
    }

    @Override
    protected boolean supports(Object value) {
        return true;
    }
}
