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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.filter.IllegalFilterException;
import org.geotools.util.Converters;

/**
 * Generates a list of unique values from a collection
 *
 * @author Cory Horner, Refractions
 * @since 2.2.M2
 */
public class UniqueVisitor implements FeatureCalc, FeatureAttributeVisitor, LimitingVisitor {
    private List<Expression> expressions = new LinkedList<>();
    Set<Object> set = new HashSet<>();
    Set<Object> skipped = new HashSet<>();
    int startIndex = 0;
    int maxFeatures = Integer.MAX_VALUE;
    int currentItem = 0;
    boolean preserveOrder = false;
    static FilterFactory factory = CommonFactoryFinder.getFilterFactory();

    public UniqueVisitor(int attributeTypeIndex, SimpleFeatureType type) throws IllegalFilterException {
        this(type, attributeTypeIndex);
    }

    public UniqueVisitor(String attrName, SimpleFeatureType type) throws IllegalFilterException {
        this(type, attrName);
    }

    public UniqueVisitor(String... attributeTypeNames) {
        for (String atn : attributeTypeNames) {
            expressions.add(factory.property(atn));
        }
    }

    public UniqueVisitor(SimpleFeatureType type, Integer... indexes) throws IllegalFilterException {
        for (Integer i : indexes) {
            String attrName = type.getDescriptor(i).getLocalName();
            expressions.add(factory.property(attrName));
        }
    }

    public UniqueVisitor(SimpleFeatureType type, String... attributeNames) throws IllegalFilterException {
        for (String an : attributeNames) {
            String attrName = type.getDescriptor(an).getLocalName();
            expressions.add(factory.property(attrName));
        }
    }

    public UniqueVisitor(Expression... expressions) {
        for (Expression e : expressions) {
            this.expressions.add(e);
        }
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
        this.set = createNewSet(Collections.emptyList());
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
        return expressions;
    }

    @Override
    public Optional<List<Class>> getResultType(List<Class> inputTypes) {
        return CalcUtil.reflectInputTypes(inputTypes.size(), inputTypes);
    }

    public void visit(SimpleFeature feature) {
        visit((Feature) feature);
    }

    @Override
    public void visit(Feature feature) {
        if (!isMultiAttr()) {
            visitWithSingleAttribute(feature);
        } else {
            visitWithMultiAttributes(feature);
        }
    }

    private void visitWithSingleAttribute(Feature feature) {
        Expression expr = expressions.get(0);
        // we ignore null attributes
        Object value = expr.evaluate(feature);

        if (value != null) {
            if (!set.contains(value) && !skipped.contains(value)) {
                if (currentItem >= startIndex && currentItem < startIndex + maxFeatures) {
                    set.add(value);
                } else {
                    skipped.add(value);
                }
                currentItem++;
            }
        }
    }

    private void visitWithMultiAttributes(Feature feature) {
        List<Object> uniqueVal = new LinkedList<>();
        for (Expression expr : expressions) {
            Object value = expr.evaluate(feature);
            uniqueVal.add(value);
        }
        if (skipped == null) {
            skipped = new LinkedHashSet<>();
        }
        if (!set.contains(uniqueVal) && !skipped.contains(uniqueVal)) {
            if (currentItem >= startIndex && currentItem < startIndex + maxFeatures) {
                set.add(uniqueVal);
            } else {
                skipped.add(uniqueVal);
            }
            currentItem++;
        }
    }

    private boolean isMultiAttr() {
        return expressions.size() > 1;
    }

    public Expression getExpression() {
        return expressions.get(0);
    }

    public Set getUnique() {
        /* Return a list of unique values from the collection */
        return set;
    }

    public void setValue(Object value) {
        if (value instanceof Collection) { // convert to set
            @SuppressWarnings("unchecked")
            Collection<Object> cast = (Collection<Object>) value;
            this.set = createNewSet(cast);
        } else {
            @SuppressWarnings("unchecked")
            List<Object> collection = Converters.convert(value, List.class);
            if (collection != null) {
                this.set = createNewSet(collection);
            } else {
                this.set = createNewSet(Collections.singleton(value));
            }
        }
    }

    private Set<Object> createNewSet(Collection<Object> collection) {
        return UniqueResult.createNewSet(collection, preserveOrder);
    }

    public void reset() {
        /* Reset the unique and current minimum for the features in the collection */
        this.set = createNewSet(Collections.emptyList());
        this.skipped = new HashSet<>();

        currentItem = 0;
    }

    @Override
    public CalcResult getResult() {
        if (set.isEmpty()) return CalcResult.NULL_RESULT;
        return new UniqueResult(set, this.preserveOrder);
    }

    public List<String> getAttrNames() {
        List<String> attributes = new LinkedList<>();
        for (Expression e : expressions) {
            attributes.add(getPropertyName(e));
        }
        return attributes;
    }

    private String getPropertyName(Expression e) {
        String name = null;
        if (e instanceof PropertyName) {
            name = ((PropertyName) e).getPropertyName();
        } else {
            FilterAttributeExtractor extractor = new FilterAttributeExtractor();
            e.accept(extractor, null);
            String[] attrs = extractor.getAttributeNames();
            if (attrs != null && attrs.length > 0) name = extractor.getAttributeNames()[0];
        }
        return name;
    }

    public static class UniqueResult extends AbstractCalcResult {

        private Set<Object> unique;
        private boolean preserveOrder = false;

        public UniqueResult(Set<Object> newSet) {
            this(newSet, false);
        }

        public UniqueResult(Set<Object> newSet, boolean preserveOrder) {
            this.unique = newSet;
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
            if (unique == null) return Collections.emptySet();
            else return createNewSet(unique, preserveOrder);
        }

        @Override
        public boolean isCompatible(CalcResult targetResults) {
            // list each calculation result which can merge with this type of result
            if (targetResults instanceof UniqueResult || targetResults == CalcResult.NULL_RESULT) return true;
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
