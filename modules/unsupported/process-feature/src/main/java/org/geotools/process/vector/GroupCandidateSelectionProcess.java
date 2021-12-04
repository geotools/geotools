/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;
import org.geotools.coverage.processing.Operations;
import org.geotools.data.Query;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.collection.DecoratingFeatureCollection;
import org.geotools.feature.collection.DecoratingFeatureIterator;
import org.geotools.feature.collection.PushBackFeatureIterator;
import org.geotools.feature.type.Types;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.SortByImpl;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.Errors;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.util.factory.GeoTools;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.xml.sax.helpers.NamespaceSupport;

@DescribeProcess(
        title = "Group candidate selection",
        description =
                "Given a collection of features for each group defined only the feature having the MIN or MAX value for the chosen attribute will be included in the final output")
public class GroupCandidateSelectionProcess implements VectorProcess {

    protected FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());

    public FeatureCollection execute(
            @DescribeParameter(name = "data", description = "Input feature collection")
                    FeatureCollection<? extends FeatureType, ? extends Feature> features,
            @DescribeParameter(
                            name = "aggregation",
                            description =
                                    "The aggregate operation to be computed, it can be MAX or MIN",
                            min = 1)
                    String aggregation,
            @DescribeParameter(
                            name = "operationAttribute",
                            description =
                                    "The feature's attribute to be used to compute the aggregation",
                            min = 1)
                    String operationAttribute,
            @DescribeParameter(
                            name = "groupingAttributes",
                            description =
                                    "The feature's attributes defining groups for which perform the filtering based on the aggregation operation and the operation attribute."
                                            + "Consistent results are guaranteed only if the vector process is fed with features already sorted  by these attributes",
                            min = 1)
                    List<String> groupingAttributes) {
        try {
            if (features == null) {
                throw new ProcessException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1, "features"));
            }
            if (operationAttribute == null) {
                throw new ProcessException(
                        Errors.format(ErrorKeys.NULL_ARGUMENT_$1, "operationAttribute"));
            }
            if (groupingAttributes == null || groupingAttributes.isEmpty()) {
                throw new ProcessException(
                        Errors.format(ErrorKeys.NULL_ARGUMENT_$1, "groupingAttributes"));
            }
            if (aggregation == null) {
                throw new ProcessException(
                        Errors.format(ErrorKeys.NULL_ARGUMENT_$1, "aggregation"));
            }
            Operations op = Operations.valueOf(aggregation);
            FeatureType schema = features.getSchema();
            NamespaceSupport ns = declareNamespaces(schema);
            List<PropertyName> groupingPn =
                    groupingAttributes.stream()
                            .map(
                                    g ->
                                            validatePropertyName(
                                                    new AttributeExpressionImpl(g, ns), schema))
                            .collect(Collectors.toList());
            PropertyName opValue =
                    validatePropertyName(ff.property(operationAttribute, ns), schema);
            return new GroupCandidateSelectionFeatureCollection<>(
                    features, groupingPn, opValue, op);
        } catch (IllegalArgumentException e) {
            throw new ProcessException(
                    Errors.format(ErrorKeys.BAD_PARAMETER_$2, "aggregation", aggregation));
        }
    }

    public Query invertQuery(
            @DescribeParameter(
                            name = "operationAttribute",
                            description =
                                    "The feature's attribute to be used to compute the aggregation",
                            min = 1)
                    String operationAttribute,
            @DescribeParameter(
                            name = "groupingAttributes",
                            description =
                                    "The feature's attributes defining groups for which perform the filtering based on the aggregation operation and the operation attribute."
                                            + "Consistent results are guaranteed only if the vector process is fed with features already sorted  by these attributes",
                            min = 1)
                    List<String> groupingAttributes,
            Query targetQuery,
            GridGeometry gridGeometry) {

        // optimize the query adding the needed property name to it

        List<PropertyName> properties = targetQuery.getProperties();
        SortBy[] sorts = targetQuery.getSortBy();

        Query q = targetQuery != null ? new Query(targetQuery) : new Query();
        // add the sortBy if not present
        SortBy[] sortBy = buildNewSortBy(sorts, groupingAttributes);
        q.setSortBy(sortBy);

        // produces new PropertyName to add to the query
        List<PropertyName> propertiesToAdd =
                Stream.of(sortBy).map(s -> s.getPropertyName()).collect(Collectors.toList());
        PropertyName operationAttributeProp = ff.property(operationAttribute);
        propertiesToAdd.add(operationAttributeProp);
        // eventually merge with existing ones
        List<PropertyName> pns = getNewProperties(propertiesToAdd, properties);
        q.setProperties(pns);
        return q;
    }

    private SortBy[] buildNewSortBy(SortBy[] sorts, List<String> groupingAttributes) {
        // eventually creates new SortBy and add them to the array
        List<SortBy> newSorts = new ArrayList<>(groupingAttributes.size());
        List<PropertyName> properties =
                groupingAttributes.stream().map(s -> ff.property(s)).collect(Collectors.toList());
        for (PropertyName pn : properties) {
            if (!sortByAlreadyExists(sorts, pn))
                newSorts.add(new SortByImpl(pn, SortOrder.ASCENDING));
        }
        if (!newSorts.isEmpty()) {
            if (sorts == null) return newSorts.toArray(new SortBy[newSorts.size()]);
            else return ArrayUtils.addAll(sorts, newSorts.toArray(new SortBy[newSorts.size()]));
        }
        return sorts;
    }

    private List<PropertyName> getNewProperties(
            List<PropertyName> toAdd, List<PropertyName> originalProperties) {
        Set<PropertyName> properties = new HashSet<>();
        if (originalProperties != null) {
            properties.addAll(originalProperties);
        }
        if (toAdd != null) {
            properties.addAll(toAdd);
        }
        return new ArrayList<>(properties);
    }

    private boolean sortByAlreadyExists(SortBy[] sorts, PropertyName pn) {
        if (sorts == null) return false;
        for (SortBy s : sorts) {
            // just checking the property name. In the context of the rendering
            // transformation the sortBy order doesn't matter
            if (s.getPropertyName().equals(pn)) {
                return true;
            }
        }
        return false;
    }

    private PropertyName validatePropertyName(PropertyName pn, FeatureType schema) {
        // checks propertyName against the schema
        if (pn.evaluate(schema) == null)
            throw new ProcessException(
                    "Unable to resolve " + pn.getPropertyName() + " against the FeatureType");
        return pn;
    }

    private NamespaceSupport declareNamespaces(FeatureType type) {
        // retrieves Namespaces for complex features
        NamespaceSupport namespaceSupport = null;
        Map namespaces = (Map) type.getUserData().get(Types.DECLARED_NAMESPACES_MAP);
        if (namespaces != null) {
            namespaceSupport = new NamespaceSupport();
            for (Object o : namespaces.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                String prefix = (String) entry.getKey();
                String namespace = (String) entry.getValue();
                namespaceSupport.declarePrefix(prefix, namespace);
            }
        }
        return namespaceSupport;
    }

    /**
     * A FeatureCollection wrapper to filter out features according to the aggregation parameter and
     * the groups defined by the groupingAttributes
     */
    static class GroupCandidateSelectionFeatureCollection<T extends FeatureType, F extends Feature>
            extends DecoratingFeatureCollection<T, F> {

        List<PropertyName> groupingAttributes;

        PropertyName operationAttribute;

        Operations aggregation;

        public GroupCandidateSelectionFeatureCollection(
                FeatureCollection<T, F> delegate,
                List<PropertyName> groupingAttributes,
                PropertyName operationAttribute,
                Operations aggregation) {
            super(delegate);
            this.groupingAttributes = groupingAttributes;
            this.operationAttribute = operationAttribute;
            this.aggregation = aggregation;
        }

        @Override
        public FeatureIterator<F> features() {
            return new GroupCandidateSelectionIterator<>(
                    new PushBackFeatureIterator<>(delegate.features()),
                    groupingAttributes,
                    operationAttribute,
                    aggregation);
        }
    }

    /**
     * A FeatureIterator wrapper to filter out features according to the aggregation parameter and
     * the groups defined by the groupingAttributes
     */
    static class GroupCandidateSelectionIterator<F extends Feature>
            extends DecoratingFeatureIterator<F> {

        private List<PropertyName> groupByAttributes;

        private PropertyName operationAttribute;

        private Operations aggregation;

        private F next;

        /**
         * Wrap the provided FeatureIterator.
         *
         * @param iterator Iterator to be used as a delegate.
         */
        public GroupCandidateSelectionIterator(
                PushBackFeatureIterator<F> iterator,
                List<PropertyName> groupByAttributes,
                PropertyName operationValue,
                Operations aggregation) {
            super(iterator);
            this.groupByAttributes = groupByAttributes;
            this.operationAttribute = operationValue;
            this.aggregation = aggregation;
        }

        @Override
        public boolean hasNext() {
            List<Object> groupingValues = new ArrayList<>(groupByAttributes.size());
            F bestFeature = null;
            while (super.hasNext()) {
                F f = super.next();
                if (bestFeature == null) {
                    // no features in the list this is the first of the group
                    // takes the values to check the following features if belong to the same
                    // group
                    groupingValues = getGroupingValues(groupingValues, f);
                    bestFeature = f;
                } else {
                    // is the feature in the group?
                    if (featureComparison(groupingValues, f)) {
                        // if operationValue is null skip
                        bestFeature = updateBestFeature(f, bestFeature);
                    } else {
                        @SuppressWarnings({"unchecked", "PMD.CloseResource"})
                        PushBackFeatureIterator<F> pb = (PushBackFeatureIterator) delegate;
                        pb.pushBack();
                        break;
                    }
                }
            }
            next = bestFeature;
            return next != null;
        }

        private boolean featureComparison(List<Object> groupingValues, Feature f) {
            List<Object> toCompareValues = new ArrayList<>(groupingValues.size());
            for (PropertyName p : groupByAttributes) {
                Object val = p.evaluate(f);
                if (val != null) toCompareValues.add(p.evaluate(f));
            }
            if (toCompareValues.isEmpty()) toCompareValues = null;
            if (groupingValues == null && toCompareValues == null) return true;
            else if (groupingValues != null
                    && toCompareValues != null
                    && groupingValues.equals(toCompareValues)) return true;
            else return false;
        }

        private List<Object> getGroupingValues(List<Object> groupingValues, F f) {
            for (PropertyName p : groupByAttributes) {
                Object result = p.evaluate(f);
                groupingValues.add(result);
            }
            if (groupingValues.isEmpty()) return null;
            else return groupingValues;
        }

        private F updateBestFeature(F best, F f) {
            Comparable bestValue = getComparableFromEvaluation(best);
            Comparable value = getComparableFromEvaluation(f);
            if (value == null) return best;
            else if (bestValue == null) return f;
            if (aggregation.equals(Operations.MAX)) {
                return findBestMax(best, f, bestValue, value);
            } else {
                return findBestMin(best, f, bestValue, value);
            }
        }

        @SuppressWarnings("unchecked")
        private F findBestMax(F best, F f, Comparable bestValue, Comparable value) {
            if (bestValue.compareTo(value) < 0) return f;
            return best;
        }

        @SuppressWarnings("unchecked")
        private F findBestMin(F best, F f, Comparable bestValue, Comparable value) {
            if (bestValue.compareTo(value) > 0) return f;
            return best;
        }

        private Comparable getComparableFromEvaluation(Feature f) {
            // In case of complex features we got the property instead of the value
            Object o = operationAttribute.evaluate(f);
            if (o instanceof Property) o = ((Property) o).getValue();
            return (Comparable) o;
        }

        @Override
        public F next() throws NoSuchElementException {
            if (next == null && !this.hasNext()) {
                throw new NoSuchElementException();
            }
            F f = next;
            next = null;
            return f;
        }

        @Override
        public void close() {
            delegate.close();
            delegate = null;
            next = null;
        }
    }

    enum Operations {
        MAX("MAX"),
        MIN("MIN");

        private String operation;

        Operations(String operation) {
            this.operation = operation;
        }

        public String getOperation() {
            return operation;
        }
    }
}
