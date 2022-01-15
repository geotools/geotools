/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.mongodb.complex;

import static org.geotools.data.complex.AppSchemaDataAccess.matchProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.digester.Digester;
import org.geotools.data.DataAccess;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.complex.AppSchemaDataAccess;
import org.geotools.data.complex.AttributeMapping;
import org.geotools.data.complex.DataAccessMappingFeatureIterator;
import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.data.complex.NestedAttributeMapping;
import org.geotools.data.complex.config.AppSchemaDataAccessDTO;
import org.geotools.data.complex.config.SourceDataStore;
import org.geotools.data.complex.filter.XPath;
import org.geotools.data.complex.spi.CustomImplementationsFinder;
import org.geotools.data.complex.spi.CustomSourceDataStore;
import org.geotools.data.complex.util.XPathUtil;
import org.geotools.data.mongodb.MongoDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FilterAttributeExtractor;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Class that builds an App-Schema iterator ready to be used with MongoDB. Data coming from MongoDB
 * is assumed to be always normalized, when using MongoDB with App-Schema we cannot chain multiple
 * collections together.
 */
public final class MongoComplexDataSource implements CustomSourceDataStore {

    // filter factory use to create filters
    private FilterFactory2 filterFactory = CommonFactoryFinder.getFilterFactory2(null);

    @Override
    public DataAccess<? extends FeatureType, ? extends Feature> buildDataStore(
            SourceDataStore dataStoreConfig, AppSchemaDataAccessDTO appSchemaConfig) {
        // nothing to do
        return null;
    }

    @Override
    public void configXmlDigesterDataSources(Digester digester) {
        // nothing to do
    }

    @Override
    public void configXmlDigesterAttributesMappings(Digester digester) {
        // nothing to do
    }

    @Override
    public DataAccessMappingFeatureIterator buildIterator(
            AppSchemaDataAccess store,
            FeatureTypeMapping featureTypeMapping,
            Query query,
            Transaction transaction) {
        // let's se if this is a feature type coming from MongoDB
        if (!(featureTypeMapping.getSource() != null
                && featureTypeMapping.getSource().getDataStore() instanceof MongoDataStore)) {
            // not a MongoDB feature type mapping
            return null;
        }
        try {
            // we consider that data coming from MongoDB is always normalized
            return new DataAccessMappingFeatureIterator(
                    store, featureTypeMapping, query, false, true);
        } catch (Exception exception) {
            throw new RuntimeException(
                    "Error creating App-Schema iterator for MongoDB data store.", exception);
        }
    }

    @Override
    public List<PropertyName> getSurrogatePropertyNames(
            List<PropertyName> requested, FeatureTypeMapping featureTypeMapping) {
        // let's se if this is a feature type coming from MongoDB
        if (!(featureTypeMapping.getSource() != null
                && featureTypeMapping.getSource().getDataStore() instanceof MongoDataStore)) {
            // not a MongoDB feature type mapping
            return Collections.emptyList();
        }
        // let's iterate over the requested properties and try to get the matching JSON path
        List<PropertyName> properties = new ArrayList<>();
        for (PropertyName propertyName : requested) {
            // get the JSON path matching the current requested property
            properties.addAll(extractJsonPaths(featureTypeMapping, propertyName));
        }
        return properties;
    }

    /**
     * Returns all the JSON paths associated with the provided property name as property names, in
     * practice this method finds the source expression associated with the attribute mapping
     * matching the provided property name and extracts its JSON path converting it to a property
     * name. Since the source expression may use multiple attributes, multiple JSON paths may be
     * found.
     */
    private List<PropertyName> extractJsonPaths(
            FeatureTypeMapping featureTypeMapping, PropertyName propertyName) {
        // get the expression of the mapping attribute matching the provided property name
        Expression expression = findPropertyExpression(featureTypeMapping, propertyName);
        if (expression == null) {
            // no attribute matches the provided property, let's move on
            return Collections.emptyList();
        }
        // extracts all the properties from the attribute mapping expression
        FilterAttributeExtractor propertiesExtractor = new FilterAttributeExtractor();
        expression.accept(propertiesExtractor, null);
        // convert all properties \ attributes to a property name
        return propertiesExtractor.getAttributeNameSet().stream()
                .map(attributeName -> filterFactory.property(attributeName))
                .collect(Collectors.toList());
    }

    /**
     * Finds the mapping attribute matching the provided property name and gets its source
     * expression or id expression. This method will navigate the feature type mapping in all its
     * extend, including the nested mappings.
     */
    private Expression findPropertyExpression(
            FeatureTypeMapping featureTypeMapping, PropertyName propertyName) {
        // gets the property name steps, i.e. XML path parcels
        XPathUtil.StepList propertySteps = buildPropertySteps(featureTypeMapping, propertyName);
        // fidn the attribute mapping matching the provided proeprty name
        AttributeMapping attributeMapping =
                findAttributeMapping(featureTypeMapping, propertyName, propertySteps);
        if (attributeMapping == null) {
            // no attribute found we are done
            return null;
        }
        // let's check if we have a nested attribute
        if (attributeMapping instanceof MongoNestedMapping) {
            // delegate the expression finding to MongoDB nested expression factories
            return CustomImplementationsFinder.find(
                    featureTypeMapping, propertySteps, (NestedAttributeMapping) attributeMapping);
        }
        // let's see if we have a source expression
        Expression expression = attributeMapping.getSourceExpression();
        // if the source expression let's try the identifier expression
        return expression == null || expression == Expression.NIL
                ? attributeMapping.getIdentifierExpression()
                : expression;
    }

    /** Builds the property steps list, i.e. split the XML path into parcels. */
    private XPathUtil.StepList buildPropertySteps(
            FeatureTypeMapping featureTypeMapping, PropertyName propertyName) {
        // get the correct namespace context, first we try the property name namespaces
        NamespaceSupport nameSpaces = propertyName.getNamespaceContext();
        if (nameSpaces == null) {
            // let's fallback on the feature type namespaces
            nameSpaces = featureTypeMapping.getNamespaces();
        }
        // build the property XML path steps list
        return XPath.steps(
                featureTypeMapping.getTargetFeature(), propertyName.getPropertyName(), nameSpaces);
    }

    /**
     * Finds the attribute mapping from in the provided feature type that matches the property name
     * steps. This method sticks to the feature type attributes mappings and doesn't follow the
     * nested mappings.
     */
    private AttributeMapping findAttributeMapping(
            FeatureTypeMapping featureTypeMapping,
            PropertyName propertyName,
            XPathUtil.StepList propertySteps) {
        // let's check if any of the feature type attributes matches the property name
        for (AttributeMapping attributeMapping : featureTypeMapping.getAttributeMappings()) {
            // build the attribute mapping XML path steps list
            XPathUtil.StepList attributeSteps = attributeMapping.getTargetXPath();
            if (propertySteps == null
                    // the property didn't had a valid XML path
                    ? matchProperty(propertyName.getPropertyName(), attributeSteps)
                    // the property had a valid XML path
                    : matchProperty(propertySteps, attributeSteps)) {
                // we found our attribute, we are done
                return attributeMapping;
            }
        }
        // no attribute matching the provided property found
        return null;
    }
}
