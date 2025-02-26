/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.Query;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.data.complex.config.AppSchemaDataAccessConfigurator;
import org.geotools.data.complex.feature.type.Types;
import org.geotools.data.complex.filter.ComplexFilterSplitter;
import org.geotools.data.complex.filter.XPath;
import org.geotools.data.complex.spi.CustomSourceDataStore;
import org.geotools.data.complex.util.XPathUtil.StepList;
import org.geotools.data.joining.JoiningQuery;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.visitor.DefaultFilterVisitor;
import org.geotools.jdbc.JDBCFeatureSource;
import org.geotools.jdbc.JDBCFeatureStore;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * @author Russell Petty (GeoScience Victoria)
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 */
public class MappingFeatureIteratorFactory {
    protected static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(MappingFeatureIteratorFactory.class);

    /**
     * Temporary filter visitor to determine whether the filter concerns any attribute mapping that has isList enabled.
     * This is because Bureau of Meteorology requires a subset of the property value to be returned, instead of the full
     * value. This should be a temporary solution. This won't work with feature chaining at the moment.
     *
     * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
     */
    static class IsListFilterVisitor extends DefaultFilterVisitor {

        // Attribute mappings that have isList enabled
        private List<AttributeMapping> listMappings;

        // True if the filter has properties that are configured as isList
        private boolean isListFilter;

        private FeatureTypeMapping mappings;

        public IsListFilterVisitor(List<AttributeMapping> listMappings, FeatureTypeMapping mappings) {
            this.listMappings = listMappings;
            this.mappings = mappings;
            isListFilter = false;
        }

        @Override
        public Object visit(PropertyName expression, Object extraData) {
            AttributeDescriptor root = mappings.getTargetFeature();
            String attPath = expression.getPropertyName();
            NamespaceSupport namespaces = mappings.getNamespaces();
            StepList simplifiedSteps = XPath.steps(root, attPath, namespaces);
            StepList targetXpath;
            for (AttributeMapping mapping : listMappings) {
                targetXpath = mapping.getTargetXPath();
                if (targetXpath.equals(simplifiedSteps)) {
                    // TODO: support feature chaining too?
                    isListFilter = true;
                    return extraData;
                }
            }
            return extraData;
        }

        public boolean isListFilterExists() {
            return isListFilter;
        }
    }

    public static IMappingFeatureIterator getInstance(
            AppSchemaDataAccess store, FeatureTypeMapping mapping, Query query, Filter unrolledFilter)
            throws IOException {
        return MappingFeatureIteratorFactory.getInstance(store, mapping, query, unrolledFilter, null);
    }

    public static IMappingFeatureIterator getInstance(
            AppSchemaDataAccess store,
            FeatureTypeMapping mapping,
            Query query,
            Filter unrolledFilter,
            Transaction transaction)
            throws IOException {
        return MappingFeatureIteratorFactory.getInstance(store, mapping, query, unrolledFilter, transaction, true);
    }

    public static IMappingFeatureIterator getInstance(
            AppSchemaDataAccess store,
            FeatureTypeMapping mapping,
            Query query,
            Filter unrolledFilter,
            Transaction transaction,
            boolean indexEnable)
            throws IOException {

        // if this feature type mapping uses an external index and indexing is enable
        // then create a specific iterator
        if (indexEnable && mapping.getIndexSource() != null) {
            IndexedMappingFeatureIteratorFactory factory =
                    new IndexedMappingFeatureIteratorFactory(store, mapping, query, unrolledFilter, transaction);
            if (factory.getIndexModeProcessor().isIndexDrivenIteratorCase()) {
                return factory.buildInstance();
            }
        }

        if (mapping instanceof XmlFeatureTypeMapping) {
            return new XmlMappingFeatureIterator(store, mapping, query);
        }

        boolean isJoining = AppSchemaDataAccessConfigurator.isJoining();
        boolean removeQueryLimitIfDenormalised = false;

        FeatureSource mappedSource = mapping.getSource();

        if (isJoining && !(mappedSource instanceof JDBCFeatureSource || mappedSource instanceof JDBCFeatureStore)) {
            // check if joining is explicitly set for non database backends
            if (AppSchemaDataAccessConfigurator.isJoiningSet()) {
                throw new IllegalArgumentException("Joining queries are only supported on JDBC data stores");
            } else {
                // override default behaviour
                // this is not intended
                isJoining = false;
            }
        }

        if (isJoining) {
            if (!(query instanceof JoiningQuery)) {
                boolean hasIdColumn = !Expression.NIL.equals(mapping.getFeatureIdExpression())
                        && !(mapping.getFeatureIdExpression() instanceof Literal);
                query = new JoiningQuery(query);
                if (hasIdColumn) {
                    FilterAttributeExtractor extractor = new FilterAttributeExtractor();
                    mapping.getFeatureIdExpression().accept(extractor, null);
                    for (String pn : extractor.getAttributeNameSet()) {
                        ((JoiningQuery) query).addId(pn);
                    }
                }
                ((JoiningQuery) query).setRootMapping(mapping);
            }
        }
        IMappingFeatureIterator iterator = null;
        if (unrolledFilter != null) {
            // unrolledFilter is set in JoiningNestedAttributeMapping
            // so this is for nested features with joining
            query.setFilter(Filter.INCLUDE);
            Query unrolledQuery = store.unrollQuery(query, mapping);
            unrolledQuery.setFilter(unrolledFilter);
            if (query instanceof JoiningQuery && unrolledQuery instanceof JoiningQuery) {
                ((JoiningQuery) unrolledQuery).setRootMapping(((JoiningQuery) query).getRootMapping());
            }
            if (isSimpleType(mapping)) {
                iterator = new MappingAttributeIterator(store, mapping, query, unrolledQuery, transaction);
            } else {
                iterator =
                        new DataAccessMappingFeatureIterator(store, mapping, query, unrolledQuery, false, transaction);
            }
        } else {
            // HACK HACK HACK
            // experimental/temporary solution for isList subsetting by filtering
            List<AttributeMapping> listMappings = mapping.getIsListMappings();
            Filter isListFilter = null;
            if (!listMappings.isEmpty()) {
                IsListFilterVisitor listChecker = new IsListFilterVisitor(listMappings, mapping);
                Filter complexFilter = query.getFilter();
                complexFilter.accept(listChecker, null);
                if (listChecker.isListFilterExists()) {
                    isListFilter = AppSchemaDataAccess.unrollFilter(complexFilter, mapping);
                }
            }
            // END OF HACK
            if (isJoining || mappedSource instanceof JDBCFeatureSource || mappedSource instanceof JDBCFeatureStore) {
                // has database as data source, we can use the data source filter capabilities
                FilterCapabilities capabilities = getFilterCapabilities(mappedSource);
                ComplexFilterSplitter splitter = new ComplexFilterSplitter(capabilities, mapping);
                Filter filter = query.getFilter();
                filter.accept(splitter, null);
                Filter preFilter = splitter.getFilterPre();
                query.setFilter(preFilter);
                filter = splitter.getFilterPost();

                if (isJoining) {
                    ((JoiningQuery) query).setDenormalised(mapping.isDenormalised());
                }
                if (isJoining && isListFilter != null) {
                    // pass it on in JoiningQuery so it can be handled when the SQL is prepared
                    // in JoiningJDBCSource
                    ((JoiningQuery) query).setSubset(true);
                    // also reset isListFilter to null so it doesn't perform the filtering in
                    // DataAccessMappingFeatureIterator except when post filtering is involved
                    // i.e. feature chaining is involved
                    if (filter == null || filter.equals(Filter.INCLUDE)) {
                        isListFilter = null;
                    }
                }
                // need to flag if this is non joining and has pre filter because it needs
                // to find denormalised rows that match the id (but doesn't match pre filter)
                boolean isFiltered = !isJoining && preFilter != null && preFilter != Filter.INCLUDE;
                // HACK HACK HACK
                // experimental/temporary solution for isList subsetting by filtering
                // Because subsetting should be done before the feature is built.. so we're not
                // using PostFilteringMappingFeatureIterator
                boolean hasPostFilter = false;
                if (isListFilter == null) {
                    // END OF HACK
                    if (filter != null && filter != Filter.INCLUDE) {
                        hasPostFilter = true;
                    }
                }
                int offset = 0;
                int maxFeatures = 1000000;
                if (hasPostFilter) {
                    // can't apply offset to the SQL query if using post filters
                    // it has to be applied to the post filter
                    offset = query.getStartIndex() == null ? 0 : query.getStartIndex();
                    query.setStartIndex(null);
                    maxFeatures = query.getMaxFeatures();
                    removeQueryLimitIfDenormalised = true;
                }
                iterator = new DataAccessMappingFeatureIterator(
                        store, mapping, query, isFiltered, removeQueryLimitIfDenormalised, hasPostFilter, transaction);
                if (isListFilter != null) {
                    ((DataAccessMappingFeatureIterator) iterator).setListFilter(isListFilter);
                }
                if (hasPostFilter) {
                    iterator = new PostFilteringMappingFeatureIterator(iterator, filter, maxFeatures, offset);
                }
            } else if (mappedSource instanceof MappingFeatureSource) {
                // web service data access wrapper
                iterator = new DataAccessMappingFeatureIterator(store, mapping, query);
                if (isListFilter != null) {
                    ((DataAccessMappingFeatureIterator) iterator).setListFilter(isListFilter);
                }
            } else {
                // non database sources e.g. property data store
                Filter filter = query.getFilter();
                for (CustomSourceDataStore customSourceDataStore : CustomSourceDataStore.loadExtensions()) {
                    // extension point to allow custom data source to provide their own iterator
                    iterator = customSourceDataStore.buildIterator(store, mapping, query, transaction);
                }
                if (iterator == null) {
                    iterator = new DataAccessMappingFeatureIterator(
                            store, mapping, query, !Filter.INCLUDE.equals(filter), true);
                }
                // HACK HACK HACK
                // experimental/temporary solution for isList subsetting by filtering
                if (isListFilter != null) {
                    ((DataAccessMappingFeatureIterator) iterator).setListFilter(isListFilter);
                }
                // END OF HACK
            }
        }
        return iterator;
    }

    private static boolean isSimpleType(FeatureTypeMapping mapping) {
        return Types.isSimpleContentType(mapping.getTargetFeature().getType());
    }

    private static FilterCapabilities getFilterCapabilities(FeatureSource mappedSource)
            throws IllegalArgumentException {
        FilterCapabilities capabilities = null;
        if (mappedSource instanceof JDBCFeatureSource) {
            capabilities = ((JDBCFeatureSource) mappedSource).getDataStore().getFilterCapabilities();
        } else if (mappedSource instanceof JDBCFeatureStore) {
            capabilities = ((JDBCFeatureStore) mappedSource).getDataStore().getFilterCapabilities();
        } else {
            throw new IllegalArgumentException("Joining queries are only supported on JDBC data stores");
        }
        return capabilities;
    }
}
