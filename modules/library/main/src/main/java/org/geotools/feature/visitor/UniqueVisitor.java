/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.IllegalFilterException;
import org.geotools.util.Converters;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * Generates a list of unique values from a collection
 *
 * @author Cory Horner, Refractions
 * @since 2.2.M2
 */
public class UniqueVisitor implements FeatureCalc, FeatureAttributeVisitor, LimitingVisitor {
    private Expression expr;
    Set<Object> set = new HashSet<>();
    Set<Object> skipped = new HashSet<>();
    int startIndex = 0;
    int maxFeatures = Integer.MAX_VALUE;
    int currentItem = 0;
    boolean preserveOrder = false;

    public UniqueVisitor(String attributeTypeName) {
        FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);
        expr = factory.property(attributeTypeName);
    }

    public UniqueVisitor(int attributeTypeIndex, SimpleFeatureType type)
            throws IllegalFilterException {
        FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);
        expr = factory.property(type.getDescriptor(attributeTypeIndex).getLocalName());
    }

    public UniqueVisitor(String attrName, SimpleFeatureType type) throws IllegalFilterException {
        FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);
        expr = factory.property(type.getDescriptor(attrName).getLocalName());
    }

    public UniqueVisitor(Expression expr) {
        this.expr = expr;
    }

    public void init(SimpleFeatureCollection collection) {
        // do nothing
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public void setMaxFeatures(int maxFeatures) {
        this.maxFeatures = maxFeatures;
    }

    public void setPreserveOrder(boolean preserveOrder) {
        this.preserveOrder = preserveOrder;
        set = createNewSet(Collections.emptyList());
    }

    @Override
    public int getStartIndex() {
        return startIndex;
    }

    @Override
    public int getMaxFeatures() {
        return maxFeatures;
    }

    @Override
    public List<Expression> getExpressions() {
        return Arrays.asList(expr);
    }

    @Override
    public Optional<List<Class>> getResultType(List<Class> inputTypes) {
        return CalcUtil.reflectInputTypes(1, inputTypes);
    }

    public void visit(SimpleFeature feature) {
        visit((Feature) feature);
    }

    @Override
    public void visit(Feature feature) {
        // we ignore null attributes
        Object value = expr.evaluate(feature);
        if (value != null) {
            if (!set.contains(value) && !skipped.contains(value)) {
                if (currentItem >= startIndex && currentItem < (startIndex + maxFeatures)) {
                    set.add(value);
                } else {
                    skipped.add(value);
                }
                currentItem++;
            }
        }
    }

    public Expression getExpression() {
        return expr;
    }

    public Set getUnique() {
        /** Return a list of unique values from the collection */
        return set;
    }

    public void setValue(Object newSet) {

        if (newSet instanceof Collection) { // convert to set
            @SuppressWarnings("unchecked")
            Collection<Object> cast = (Collection<Object>) newSet;
            this.set = createNewSet(cast);
        } else {
            @SuppressWarnings("unchecked")
            List<Object> collection = Converters.convert(newSet, List.class);
            if (collection != null) {
                this.set = createNewSet(collection);
            } else {
                this.set = createNewSet(Collections.singleton(newSet));
            }
        }
    }

    private Set<Object> createNewSet(Collection<Object> collection) {
        return UniqueResult.createNewSet(collection, preserveOrder);
    }

    public void reset() {
        /** Reset the unique and current minimum for the features in the collection */
        this.set = createNewSet(Collections.emptyList());
        this.skipped = new HashSet<>();

        currentItem = 0;
    }

    @Override
    public CalcResult getResult() {
        if (set.isEmpty()) {
            return CalcResult.NULL_RESULT;
        }
        return new UniqueResult(set, this.preserveOrder);
    }

    public static class UniqueResult extends AbstractCalcResult {
        private Set<Object> unique;
        private boolean preserveOrder = false;

        public UniqueResult(Set<Object> newSet) {
            unique = newSet;
        }

        public UniqueResult(Set<Object> newSet, boolean preserveOrder) {
            unique = newSet;
            this.preserveOrder = preserveOrder;
        }

        public static <T> Set<T> createNewSet(Collection<T> collection, boolean preserveOrder) {
            if (preserveOrder) {
                return new LinkedHashSet<>(collection);
            } else {
                return new HashSet<>(collection);
            }
        }

        @Override
        public Object getValue() {
            return createNewSet(unique, preserveOrder);
        }

        @Override
        public boolean isCompatible(CalcResult targetResults) {
            // list each calculation result which can merge with this type of result
            if (targetResults instanceof UniqueResult || targetResults == CalcResult.NULL_RESULT)
                return true;
            return false;
        }

        @Override
        public CalcResult merge(CalcResult resultsToAdd) {
            if (!isCompatible(resultsToAdd)) {
                throw new IllegalArgumentException("Parameter is not a compatible type");
            }

            if (resultsToAdd == CalcResult.NULL_RESULT) {
                return this;
            }

            if (resultsToAdd instanceof UniqueResult) {
                // add one set to the other (to create one big unique list)
                Set<Object> newSet = createNewSet(unique, preserveOrder);
                @SuppressWarnings("unchecked")
                Set<Object> other = (Set<Object>) resultsToAdd.getValue();
                newSet.addAll(other);
                return new UniqueResult(newSet, preserveOrder);
            } else {
                throw new IllegalArgumentException(
                        "The CalcResults claim to be compatible, but the appropriate merge method has not been implemented.");
            }
        }
    }

    @Override
    public boolean hasLimits() {
        return startIndex > 0 || maxFeatures < Integer.MAX_VALUE;
    }

    /** True if the unique visitor must be order preserving */
    public boolean isPreserveOrder() {
        return preserveOrder;
    }
}
