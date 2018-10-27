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
package org.geotools.data.solr.complex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.digester.Digester;
import org.geotools.appschema.filter.FilterFactoryImplReportInvalidProperty;
import org.geotools.data.DataAccess;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.complex.AppSchemaDataAccess;
import org.geotools.data.complex.DataAccessMappingFeatureIterator;
import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.data.complex.config.AppSchemaDataAccessConfigurator;
import org.geotools.data.complex.config.AppSchemaDataAccessDTO;
import org.geotools.data.complex.config.AttributeMapping;
import org.geotools.data.complex.config.SourceDataStore;
import org.geotools.data.complex.config.TypeMapping;
import org.geotools.data.complex.config.XMLConfigDigester;
import org.geotools.data.complex.spi.CustomSourceDataStore;
import org.geotools.data.solr.IndexesConfig;
import org.geotools.data.solr.SingleLayerMapper;
import org.geotools.data.solr.SolrDataStore;
import org.geotools.data.solr.SolrFeatureSource;
import org.geotools.filter.expression.AbstractExpressionVisitor;
import org.geotools.util.Converters;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

/**
 * This class covers \ uses the available App-Schema extension points to make Apache Solr usable in
 * the mappings.
 */
public final class ComplexDataStoreFactory implements CustomSourceDataStore {

    private final FilterFactory filterFactory = new FilterFactoryImplReportInvalidProperty();

    @Override
    public void configXmlDigesterDataSources(Digester digester) {
        XMLConfigDigester.setCommonSourceDataStoreRules(
                ComplexDataStoreConfigWithContext.class, "SolrDataStore", digester);
        String dataStores = "AppSchemaDataAccess/sourceDataStores/";
        // set a rule for passing the URL
        digester.addCallMethod(dataStores + "SolrDataStore/url", "setUrl", 1);
        digester.addCallParam(dataStores + "SolrDataStore/url", 0);
        // set rules for parsing geometries
        digester.addSetProperties(dataStores + "SolrDataStore/index", "name", "currentIndex");
        digester.addCallMethod(dataStores + "SolrDataStore/index/geometry", "addGeometry", 4);
        digester.addCallParam(dataStores + "SolrDataStore/index/geometry/name", 0);
        digester.addCallParam(dataStores + "SolrDataStore/index/geometry/srid", 1);
        digester.addCallParam(dataStores + "SolrDataStore/index/geometry/type", 2);
        digester.addCallParam(dataStores + "SolrDataStore/index/geometry", 3, "default");
    }

    @Override
    public void configXmlDigesterAttributesMappings(Digester digester) {
        String rootPath =
                "AppSchemaDataAccess/typeMappings/FeatureTypeMapping/attributeMappings/AttributeMapping";
        String multipleValuePath = rootPath + "/solrMultipleValue";
        digester.addObjectCreate(
                multipleValuePath, XMLConfigDigester.CONFIG_NS_URI, SolrMultipleValue.class);
        digester.addCallMethod(multipleValuePath, "setExpression", 1);
        digester.addCallParam(multipleValuePath, 0);
        digester.addSetNext(multipleValuePath, "setMultipleValue");
    }

    @Override
    public DataAccessMappingFeatureIterator buildIterator(
            AppSchemaDataAccess store,
            FeatureTypeMapping featureTypeMapping,
            Query query,
            Transaction transaction) {
        if (!(featureTypeMapping.getSource() instanceof SolrFeatureSource)) {
            // not an Apache Solr feature type mapping
            return null;
        }
        // add the sort by expressions to the query
        query.setSortBy(sortByFeatureTypeIds(featureTypeMapping));
        try {
            // build the iterator using the adapted query
            return new DataAccessMappingFeatureIterator(
                    store, featureTypeMapping, query, false, true);
        } catch (Exception exception) {
            throw new RuntimeException(
                    String.format(
                            "Error creating iterator for feature type mapping '%s'.",
                            featureTypeMapping.getMappingName()),
                    exception);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataAccess<? extends FeatureType, ? extends Feature> buildDataStore(
            SourceDataStore dataStoreConfig, AppSchemaDataAccessDTO appSchemaConfig) {
        if (!(dataStoreConfig instanceof ComplexDataStoreConfig)) {
            // not an Apache Solr feature type mapping
            return null;
        }
        ComplexDataStoreConfig complexDataStoreConfig = (ComplexDataStoreConfig) dataStoreConfig;
        IndexesConfig indexesConfig = complexDataStoreConfig.getIndexesConfig();
        // specify the index attributes we are interested in based on the App-Schema mappings file
        for (TypeMapping mapping : (Set<TypeMapping>) appSchemaConfig.getTypeMappings()) {
            // get all the attributes names used in the feature type mapping
            Set<String> attributes = extractAttributesNames(mapping);
            indexesConfig.addAttributes(mapping.getSourceTypeName(), attributes);
            if (isDenormalizedIndexMode(mapping, dataStoreConfig)) {
                // set as denormalizedIndexMode
                indexesConfig
                        .getIndexConfig(getTypeName(mapping, dataStoreConfig))
                        .setDenormalizedIndexMode(true);
            }
        }
        // build the Apache Solr store
        return new SolrDataStore(
                complexDataStoreConfig.getUrl(), new SingleLayerMapper(), indexesConfig);
    }

    /**
     * Auxiliary class for the XML configuration parsing of the complex data store. It basically
     * allow us to store to keep track of the index name whose configuration is being parsed.
     */
    public static final class ComplexDataStoreConfigWithContext extends ComplexDataStoreConfig {

        // current index name whose configuration is being parsed
        private String currentIndex;

        /**
         * Set the current index name whose configuration is being parsed.
         *
         * @param currentIndex the index name
         */
        public void setCurrentIndex(String currentIndex) {
            this.currentIndex = currentIndex;
        }

        /**
         * Adds the specification of a geometry attribute of the index to the current index
         * configuration.
         *
         * @param attributeName the index attribute name
         * @param srid the SIRD of the geometry, e.g. EPSG:4326
         * @param type the type of the geometry, e.g. POINT
         * @param isDefault TRUE fi this attribute contains the default geometry of the feature
         *     type, otherwise FALSE
         */
        public void addGeometry(String attributeName, String srid, String type, String isDefault) {
            super.addGeometry(currentIndex, attributeName, srid, type, isDefault);
        }
    }

    /**
     * Helper method that retrieves all the attributes names used in a feature type mapping in an
     * App-Schema file.
     *
     * @param typeMapping the feature type mapping
     * @return set of the attributes names found
     */
    @SuppressWarnings("unchecked")
    private Set<String> extractAttributesNames(TypeMapping typeMapping) {
        Set<String> attributes = new HashSet<>();
        List<AttributeMapping> attributesMappings = typeMapping.getAttributeMappings();
        for (AttributeMapping attributeMapping : attributesMappings) {
            // normal attributes
            Expression expression = parseExpression(attributeMapping.getSourceExpression());
            attributes.addAll(extractAttributesNames(expression));
            // identifiers attributes
            expression = parseExpression(attributeMapping.getIdentifierExpression());
            attributes.addAll(extractAttributesNames(expression));
            // client properties
            for (Object value : attributeMapping.getClientProperties().values()) {
                attributes.addAll(extractAttributesNames(parseExpression(value)));
            }
            // solr multiple values expressions
            if (attributeMapping.getMultipleValue() instanceof SolrMultipleValue) {
                SolrMultipleValue multipleValue =
                        (SolrMultipleValue) attributeMapping.getMultipleValue();
                attributes.addAll(extractAttributesNames(multipleValue.getExpression()));
            }
        }
        return attributes;
    }

    /**
     * Helper method that retrieves all the attributes names used in the source expression of an
     * attribute mapping in an App-Schema file.
     *
     * @param expression the attribute source expression
     * @return set of the attributes names found
     */
    private Set<String> extractAttributesNames(Expression expression) {
        AttributesExtractor visitor = new AttributesExtractor();
        expression.accept(visitor, null);
        return visitor.getAttributes();
    }

    /**
     * Expression visitor that retrieves all the attributes names used in the source expression of
     * an attribute mapping in an App-Schema file.
     */
    private static class AttributesExtractor extends AbstractExpressionVisitor {

        // the attributes found by visiting the expression, we use a set to avoid duplicates
        private final Set<String> attributes = new HashSet<>();

        @Override
        @SuppressWarnings("unchecked")
        public Object visit(PropertyName expression, Object data) {
            attributes.add(expression.getPropertyName());
            return expression;
        }

        /**
         * Return the set of attributes names found by visiting the expression, as per its
         * definition the set doesn't contain duplicates.
         *
         * @return set of the attributes names found
         */
        public Set<String> getAttributes() {
            return attributes;
        }
    }

    /**
     * Helper method that builds sort by expression for all the identifier attributes of the
     * provided feature type.
     *
     * @param featureTypeMapping the feature type from where to extract the identifier attributes
     * @return an arrays contain ascending sort by expression on the featrue type identifier
     *     attributes
     */
    private SortBy[] sortByFeatureTypeIds(FeatureTypeMapping featureTypeMapping) {
        ArrayList<SortBy> sortByExpressions = new ArrayList<>();
        for (org.geotools.data.complex.AttributeMapping mapping :
                featureTypeMapping.getAttributeMappings()) {
            if (mapping.getIdentifierExpression() != null
                    && !mapping.getIdentifierExpression().equals(Expression.NIL)) {
                // we have an identifier expression
                String attributeXpath = mapping.getTargetXPath().toString();
                SortBy sortByExpression = filterFactory.sort(attributeXpath, SortOrder.ASCENDING);
                sortByExpressions.add(sortByExpression);
            }
        }
        // add the sort by expression to the query
        return sortByExpressions.toArray(new SortBy[sortByExpressions.size()]);
    }

    /**
     * Helper that returns the provided value as is if its already an expression, otherwise converts
     * it to a string and parses it.
     *
     * @param value value to handle
     * @return an expression obtained from the provided value
     */
    private Expression parseExpression(Object value) {
        if (value instanceof Expression) {
            return (Expression) value;
        }
        return parseExpression(Converters.convert(value, String.class));
    }

    /**
     * Helper method that tries to build an expression from the provided string.
     *
     * @param expression text representation fo an expression
     * @return the built expression
     */
    private Expression parseExpression(String expression) {
        try {
            return AppSchemaDataAccessConfigurator.parseOgcCqlExpression(expression, filterFactory);
        } catch (Exception exception) {
            throw new RuntimeException(
                    String.format("Error parsing expression '%s'.", expression), exception);
        }
    }

    //    private Set<String> parseIndexModeAttributes(TypeMapping mapping) {
    //        Set<String> attributes = new HashSet<>();
    //        // basic feature attributes:
    //        ((List<AttributeMapping>) mapping.getAttributeMappings())
    //                .stream()
    //                .filter(
    //                        attributeMapping ->
    //                                StringUtils.isNotEmpty(attributeMapping.getIndexField()))
    //                .forEach(
    //                        attributeMapping -> {
    //                            attributes.add(attributeMapping.getIndexField());
    //                        });
    //        // chained features attributes:
    //
    //        return attributes;
    //    }

    private Set<String> parseSourceModeAttributes(TypeMapping mapping) {
        Set<String> attributes = new HashSet<>();
        ((List<AttributeMapping>) mapping.getAttributeMappings())
                .forEach(
                        attributeMapping -> {
                            // mapped attributes
                            Expression expression =
                                    parseExpression(attributeMapping.getSourceExpression());
                            attributes.addAll(extractAttributesNames(expression));
                            if (attributeMapping.getMultipleValue() instanceof SolrMultipleValue) {
                                expression =
                                        ((SolrMultipleValue) attributeMapping.getMultipleValue())
                                                .getExpression();
                                attributes.addAll(extractAttributesNames(expression));
                            }
                            // mapped identifiers
                            expression =
                                    parseExpression(attributeMapping.getIdentifierExpression());
                            attributes.addAll(extractAttributesNames(expression));
                            // mapped client properties
                            attributes.addAll(
                                    (Set<String>)
                                            attributeMapping
                                                    .getClientProperties()
                                                    .values()
                                                    .stream()
                                                    .flatMap(
                                                            value ->
                                                                    extractAttributesNames(
                                                                                    parseExpression(
                                                                                            value))
                                                                            .stream())
                                                    .collect(Collectors.toSet()));
                        });
        return attributes;
    }

    private Set<String> parseAttributeNames(TypeMapping mapping, SourceDataStore dataStoreConfig) {
        // if mapping index points to dataStore: is index use case
        //        if (dataStoreConfig.getId().equals(mapping.getIndexDataStore())) {
        //            return parseIndexModeAttributes(mapping);
        //        }
        return parseSourceModeAttributes(mapping);
    }

    private String getTypeName(TypeMapping mapping, SourceDataStore dataStoreConfig) {
        if (dataStoreConfig.getId().equals(mapping.getIndexDataStore())) {
            return mapping.getIndexTypeName();
        }
        return mapping.getSourceTypeName();
    }

    private boolean isDenormalizedIndexMode(TypeMapping mapping, SourceDataStore dataStoreConfig) {
        return dataStoreConfig.getId().equals(mapping.getIndexDataStore());
    }
}
