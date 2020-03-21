/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.joining;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.opengis.wfs20.ResolveValueType;
import org.geotools.appschema.jdbc.JoiningJDBCFeatureSource;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.complex.AbstractMappingFeatureIterator;
import org.geotools.data.complex.AppSchemaDataAccessRegistry;
import org.geotools.data.complex.AttributeMapping;
import org.geotools.data.complex.DataAccessMappingFeatureIterator;
import org.geotools.data.complex.DataAccessRegistry;
import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.data.complex.MappingFeatureCollection;
import org.geotools.data.complex.NestedAttributeMapping;
import org.geotools.data.complex.util.XPathUtil.StepList;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.util.factory.Hints;
import org.opengis.feature.Feature;
import org.opengis.feature.type.Name;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Nested attribute mapping used for joining system
 *
 * @author Niels Charlier (Curtin University of Technology)
 */
public class JoiningNestedAttributeMapping extends NestedAttributeMapping {

    /**
     * Instance that holds temporary data for going through the features, for each 'caller' (any
     * object going through the features) there is one.
     */
    protected static class Instance {

        public static class Skip {

            List<Object> idValues;

            public Skip(List<Object> idValues) {
                this.idValues = idValues;
            }
        }

        public Map<Name, DataAccessMappingFeatureIterator> featureIterators =
                new HashMap<Name, DataAccessMappingFeatureIterator>();

        public Map<Name, Expression> nestedSourceExpressions = new HashMap<Name, Expression>();

        public List<Skip> skipped = new ArrayList<Skip>();

        public Query baseTableQuery;

        public FeatureTypeMapping mapping;
    }

    /** The instances. */
    protected Map<Object, Instance> instances = new HashMap<Object, Instance>();

    /** Constructor */
    public JoiningNestedAttributeMapping(
            Expression idExpression,
            Expression parentExpression,
            StepList targetXPath,
            boolean isMultiValued,
            Map<Name, Expression> clientProperties,
            Expression sourceElement,
            StepList sourcePath,
            NamespaceSupport namespaces)
            throws IOException {
        super(
                idExpression,
                parentExpression,
                targetXPath,
                isMultiValued,
                clientProperties,
                sourceElement,
                sourcePath,
                namespaces);
    }

    public List<Feature> getInputFeatures(Object foreignKeyValue, FeatureTypeMapping fMapping) {
        throw new UnsupportedOperationException(
                "Internal error: Not Allowed to run this method for Joining Nested Attribute Mapping!");
    }

    /** Initialise a new iterator (for polymorphism, there could be multiple per instance) */
    public DataAccessMappingFeatureIterator initSourceFeatures(
            Instance instance,
            Name featureTypeName,
            CoordinateReferenceSystem reprojection,
            List<PropertyName> selectedProperties,
            boolean includeMandatory,
            int resolveDepth,
            Integer resolveTimeOut,
            Transaction transaction)
            throws IOException {
        JoiningQuery query = new JoiningQuery();
        query.setCoordinateSystemReproject(reprojection);

        query.setRootMapping(((JoiningQuery) instance.baseTableQuery).getRootMapping());
        FeatureTypeMapping fMapping = AppSchemaDataAccessRegistry.getMappingByName(featureTypeName);

        AttributeMapping mapping = fMapping.getAttributeMapping(this.nestedTargetXPath);
        if (mapping == null) {
            throw new IllegalArgumentException(
                    "Mapping is missing for: '" + this.nestedTargetXPath + "'!");
        }
        Expression nestedSourceExpression = mapping.getSourceExpression();

        List<JoiningQuery.QueryJoin> joins = new ArrayList<JoiningQuery.QueryJoin>();
        if (instance.baseTableQuery instanceof JoiningQuery) {
            if (((JoiningQuery) instance.baseTableQuery).getQueryJoins() != null) {
                joins.addAll(((JoiningQuery) instance.baseTableQuery).getQueryJoins());
            }
        }

        JoiningQuery.QueryJoin join = new JoiningQuery.QueryJoin();
        join.setForeignKeyName(sourceExpression);
        join.setJoiningKeyName(nestedSourceExpression);
        join.setJoiningTypeName(instance.baseTableQuery.getTypeName());
        join.setDenormalised(fMapping.isDenormalised());
        join.setSortBy(instance.baseTableQuery.getSortBy()); // incorporate order
        // pass on paging from the parent table to the same query within this join
        join.setMaxFeatures(instance.baseTableQuery.getMaxFeatures());
        join.setRootMapping(((JoiningQuery) instance.baseTableQuery).getRootMapping());
        join.setStartIndex(instance.baseTableQuery.getStartIndex());
        FilterAttributeExtractor extractor = new FilterAttributeExtractor();
        instance.mapping.getFeatureIdExpression().accept(extractor, null);
        for (String pn : extractor.getAttributeNameSet()) {
            join.addId(pn);
        }
        joins.add(0, join);
        query.setQueryJoins(joins);

        if (selectedProperties != null && !selectedProperties.isEmpty()) {
            selectedProperties = new ArrayList<PropertyName>(selectedProperties);
            selectedProperties.add(filterFac.property(this.nestedTargetXPath.toString()));
        }

        final Hints hints = new Hints();
        hints.put(Query.INCLUDE_MANDATORY_PROPS, includeMandatory);

        if (resolveDepth > 0) {
            hints.put(Hints.RESOLVE, ResolveValueType.ALL);
            hints.put(Hints.ASSOCIATION_TRAVERSAL_DEPTH, resolveDepth);
            hints.put(Hints.RESOLVE_TIMEOUT, resolveTimeOut);
        } else {
            hints.put(Hints.RESOLVE, ResolveValueType.NONE);
        }

        query.setHints(hints);

        query.setProperties(selectedProperties);

        FeatureSource fSource = DataAccessRegistry.getFeatureSource((Name) featureTypeName);

        if (fSource == null) {
            throw new IOException("Internal error: Source could not be found");
        }

        FeatureCollection collection = fSource.getFeatures(query);

        if (!(collection instanceof MappingFeatureCollection)) {
            throw new IOException(
                    "Internal error: Mapping feature Collection expected but found " + collection);
        }

        MappingFeatureCollection mfc = ((MappingFeatureCollection) collection);
        // copy unrolled filter
        mfc.setUnrolledFilter(instance.baseTableQuery.getFilter());

        // propagate transaction to nested feature iterators
        @SuppressWarnings("PMD.CloseResource") // wrapped and returned
        FeatureIterator featureIterator = mfc.features(transaction);

        if (!(featureIterator instanceof DataAccessMappingFeatureIterator)) {
            throw new IOException(
                    "Internal error: Data Access Mapping feature Iterator expected but found "
                            + featureIterator);
        }

        DataAccessMappingFeatureIterator daFeatureIterator =
                (DataAccessMappingFeatureIterator) featureIterator;

        List<Expression> foreignIds = new ArrayList<Expression>();
        for (int i = 0; i < query.getQueryJoins().size(); i++) {
            for (int j = 0; j < query.getQueryJoins().get(i).getIds().size(); j++) {
                foreignIds.add(
                        filterFac.property(
                                JoiningJDBCFeatureSource.FOREIGN_ID + "_" + i + "_" + j));
            }
        }

        daFeatureIterator.setForeignIds(foreignIds);

        instance.featureIterators.put(featureTypeName, daFeatureIterator);
        instance.nestedSourceExpressions.put(featureTypeName, nestedSourceExpression);

        for (Instance.Skip toSkip : instance.skipped) {
            while (daFeatureIterator.hasNext()
                    && daFeatureIterator.checkForeignIdValues(toSkip.idValues)) {
                daFeatureIterator.skip();
            }
        }

        return daFeatureIterator;
    }

    /**
     * Open an instance (cursor) for a specific caller. An instance holds a cursor and any
     * additional information to move through the features.
     */
    public void open(Object caller, Query baseTableQuery, FeatureTypeMapping mapping)
            throws IOException {
        if (instances.get(caller) != null) {
            throw new IllegalArgumentException(
                    "Trying to open Joining Nested Attribute Mapping that is already open!");
        } else {
            Instance instance = new Instance();
            instance.baseTableQuery = baseTableQuery;
            instance.mapping = mapping;

            instances.put(caller, instance);
        }
    }

    /** Close the instance of this caller. */
    @SuppressWarnings("PMD.CloseResource") // iterators are getting closed right here
    public void close(Object caller) {
        Instance instance = instances.get(caller);
        if (instance != null) {

            for (FeatureIterator featureIterator : instance.featureIterators.values()) {
                try {
                    featureIterator.close();
                } catch (Exception e) {
                    // move on and close all
                }
            }
            instance.featureIterators.clear();
            instances.remove(caller);
        } else {
            throw new IllegalArgumentException(
                    "Trying to close Joining Nested Attribute Mapping hasn't been opened!");
        }
    }

    /**
     * Get matching input features that are stored in this mapping using a supplied link value.
     *
     * @return The matching input feature
     */
    @Override
    public List<Feature> getInputFeatures(
            Object caller,
            Object foreignKeyValue,
            List<Object> idValues,
            Object feature,
            CoordinateReferenceSystem reprojection,
            List<PropertyName> selectedProperties,
            boolean includeMandatory)
            throws IOException {

        if (isSameSource()) {
            // if linkField is null, this method shouldn't be called because the mapping
            // should use the same table, and handles it differently
            throw new UnsupportedOperationException(
                    "Link field is missing from feature chaining mapping!");
        }

        @SuppressWarnings("PMD.CloseResource") // not managed here
        Transaction transaction = null;
        if (caller instanceof AbstractMappingFeatureIterator) {
            transaction = ((AbstractMappingFeatureIterator) caller).getTransaction();
        }

        Instance instance = instances.get(caller);
        if (instance == null) {
            throw new IllegalArgumentException(
                    "Trying to read Joining Nested Attribute Mapping that is not open.");
        }

        Object featureTypeName = getNestedFeatureType(feature);
        if (featureTypeName == null || !(featureTypeName instanceof Name)) {
            throw new IllegalArgumentException(
                    "Internal error: Feature type name expected but found " + featureTypeName);
        }
        @SuppressWarnings("PMD.CloseResource") // not managed here (field, closed later)
        DataAccessMappingFeatureIterator featureIterator =
                instance.featureIterators.get((Name) featureTypeName);
        if (featureIterator == null) {
            featureIterator =
                    initSourceFeatures(
                            instance,
                            (Name) featureTypeName,
                            reprojection,
                            selectedProperties,
                            includeMandatory,
                            0,
                            null,
                            transaction);
        }
        Expression nestedSourceExpression =
                instance.nestedSourceExpressions.get((Name) featureTypeName);
        if (nestedSourceExpression == null) {
            throw new IllegalArgumentException(
                    "Internal error: nested source expression expected but found "
                            + featureTypeName);
        }

        ArrayList<Feature> matchingFeatures = new ArrayList<Feature>();

        if (featureIterator != null) {
            while (featureIterator.hasNext()
                    && featureIterator
                            .peekNextValue(nestedSourceExpression)
                            .toString()
                            .equals(foreignKeyValue.toString())
                    && featureIterator.checkForeignIdValues(idValues)) {
                matchingFeatures.addAll(featureIterator.skip());
            }
        }

        // skip all others
        for (Name name : instance.featureIterators.keySet()) {
            @SuppressWarnings("PMD.CloseResource") // not managed here, field, closed later
            DataAccessMappingFeatureIterator fIt = instance.featureIterators.get(name);
            if (fIt != featureIterator) {
                skipFeatures(
                        fIt, instance.nestedSourceExpressions.get(name), foreignKeyValue, idValues);
            }
        }
        instance.skipped.add(new Instance.Skip(idValues));

        return matchingFeatures;
    }

    /**
     * Get the maching built features that are stored in this mapping using a supplied link value
     *
     * @param reprojection Reprojected CRS or null
     * @param selectedProperties list of properties to get
     * @return The matching simple features
     */
    @Override
    public List<Feature> getFeatures(
            Object caller,
            Object foreignKeyValue,
            List<Object> idValues,
            CoordinateReferenceSystem reprojection,
            Object feature,
            List<PropertyName> selectedProperties,
            boolean includeMandatory,
            int resolveDepth,
            Integer resolveTimeOut)
            throws IOException {

        if (isSameSource()) {
            // if linkField is null, this method shouldn't be called because the mapping
            // should use the same table, and handles it differently
            throw new UnsupportedOperationException(
                    "Link field is missing from feature chaining mapping!");
        }

        @SuppressWarnings("PMD.CloseResource") // not managed here
        Transaction transaction = null;
        if (caller instanceof AbstractMappingFeatureIterator) {
            transaction = ((AbstractMappingFeatureIterator) caller).getTransaction();
        }

        Instance instance = instances.get(caller);
        if (instance == null) {
            throw new IllegalArgumentException(
                    "Trying to read Joining Nested Attribute Mapping that is not open.");
        }

        Object featureTypeName = getNestedFeatureType(feature);
        if (featureTypeName == null || !(featureTypeName instanceof Name)) {
            throw new IllegalArgumentException("Something is wrong!!");
        }
        @SuppressWarnings("PMD.CloseResource") // not managed here, closed later
        DataAccessMappingFeatureIterator featureIterator =
                instance.featureIterators.get((Name) featureTypeName);
        if (featureIterator == null) {
            featureIterator =
                    initSourceFeatures(
                            instance,
                            (Name) featureTypeName,
                            reprojection,
                            selectedProperties,
                            includeMandatory,
                            resolveDepth,
                            resolveTimeOut,
                            transaction);
        }
        Expression nestedSourceExpression =
                instance.nestedSourceExpressions.get((Name) featureTypeName);
        if (nestedSourceExpression == null) {
            throw new IllegalArgumentException(
                    "Internal error: nested source expression expected but found "
                            + featureTypeName);
        }

        ArrayList<Feature> matchingFeatures = new ArrayList<Feature>();

        if (featureIterator != null) {
            while (featureIterator.hasNext()
                    && featureIterator.checkForeignIdValues(idValues)
                    && featureIterator
                            .peekNextValue(nestedSourceExpression)
                            .toString()
                            .equals(foreignKeyValue.toString())) {
                matchingFeatures.add(featureIterator.next());
            }
        }

        // skip all others
        for (Name name : instance.featureIterators.keySet()) {
            @SuppressWarnings("PMD.CloseResource") // not managed here, field, closed later
            DataAccessMappingFeatureIterator fIt = instance.featureIterators.get(name);
            if (fIt != featureIterator) {
                skipFeatures(
                        fIt, instance.nestedSourceExpressions.get(name), foreignKeyValue, idValues);
            }
        }
        instance.skipped.add(new Instance.Skip(idValues));

        return matchingFeatures;
    }

    protected void skipFeatures(
            DataAccessMappingFeatureIterator featureIterator,
            Expression nestedSourceExpression,
            Object foreignKeyValue,
            List<Object> idValues)
            throws IOException {
        while (featureIterator.hasNext()
                && featureIterator
                        .peekNextValue(nestedSourceExpression)
                        .toString()
                        .equals(foreignKeyValue.toString())
                && featureIterator.checkForeignIdValues(idValues)) {
            featureIterator.skip();
        }
    }

    /**
     * If we have decided not to build the parent feature, we need to skip all rows that were
     * returned to build it
     */
    public void skip(Object caller, Object foreignKeyValue, List<Object> idValues)
            throws IOException {
        Instance instance = instances.get(caller);
        if (instance == null) {
            throw new IllegalArgumentException(
                    "Trying to read Joining Nested Attribute Mapping that is not open.");
        }

        // skip all
        for (Name name : instance.featureIterators.keySet()) {
            @SuppressWarnings("PMD.CloseResource") // not managed here, closed later
            DataAccessMappingFeatureIterator fIt = instance.featureIterators.get(name);
            Expression nestedSourceExpression = instance.nestedSourceExpressions.get(name);
            skipFeatures(fIt, nestedSourceExpression, foreignKeyValue, idValues);
        }

        instance.skipped.add(new Instance.Skip(idValues));
    }

    /**
     * For testing purposes only: exposes the nested feature iterators to calling code.
     *
     * @param caller the object going through the features
     * @return a map of nested feature iterators, keyed by nested feature type name
     */
    public Map<Name, DataAccessMappingFeatureIterator> getNestedFeatureIterators(Object caller) {
        if (instances.containsKey(caller)) {
            return instances.get(caller).featureIterators;
        } else {
            return Collections.emptyMap();
        }
    }
}
