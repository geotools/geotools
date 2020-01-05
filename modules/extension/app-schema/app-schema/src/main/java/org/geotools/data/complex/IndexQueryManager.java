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
package org.geotools.data.complex;

import java.util.List;
import java.util.stream.Collectors;
import org.geotools.appschema.util.IndexQueryUtils;
import org.geotools.data.Query;
import org.geotools.data.complex.filter.IndexCombinedFilterTransformerVisitor;
import org.geotools.data.complex.filter.IndexedFilterDetectorVisitor;
import org.geotools.data.complex.filter.SchemaIndexedFilterDetectorVisitor;
import org.geotools.data.util.FeatureStreams;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.filter.Filters;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Or;
import org.opengis.filter.identity.FeatureId;

/**
 * Manages unrolled Query indexes and partial indexes
 *
 * @author Fernando Miño, Geosolutions
 */
public class IndexQueryManager {

    protected final FeatureTypeMapping mapping;
    protected final Query query;
    protected FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

    public IndexQueryManager(FeatureTypeMapping mapping, Query query) {
        super();
        this.mapping = mapping;
        this.query = query;
    }

    public boolean isIndexDrivenIteratorCase() {
        if (getIndexMode().equals(QueryIndexCoverage.NONE)) return false;
        return true;
    }

    /**
     * Returns the IndexMode usage mode to use for current Query
     *
     * <p>QueryIndexMode.ALL: if all filter and sort attributes are indexed QueryIndexMode.PARTIAL:
     * if at least one filter attribute is indexed, all sort attributes indexed QueryIndexMode.NONE:
     * If no one of previous conditions are accomplished
     *
     * @return QueryIndexMode
     */
    public QueryIndexCoverage getIndexMode() {
        if (query.equals(Query.ALL)
                || query.equals(Query.FIDS)
                || query.getFilter().equals(Filter.INCLUDE)) return QueryIndexCoverage.NONE;
        // Filter match:
        List<String> filterAttributes = IndexQueryUtils.getAttributesOnFilter(query.getFilter());
        int filterMatchCount =
                (int) filterAttributes.stream().filter(attr -> hasIndex(attr)).count();
        // Sort match:
        List<String> sortAttributes = IndexQueryUtils.getAttributesOnSort(query);
        int sortMatchCount = (int) sortAttributes.stream().filter(attr -> hasIndex(attr)).count();
        // Query mode rules:
        if (filterAttributes.size() == filterMatchCount && sortAttributes.size() == sortMatchCount)
            return QueryIndexCoverage.ALL;
        if (filterMatchCount > 0 && sortAttributes.size() == sortMatchCount)
            return QueryIndexCoverage.PARTIAL;
        return QueryIndexCoverage.NONE;
    }

    protected boolean hasIndex(String propertyName) {
        // return mapping.getIndexAttributeNameUnrolled(propertyName) != null;
        return mapping.getIndexAttributeName(propertyName) != null;
    }

    public static enum QueryIndexCoverage {
        ALL,
        NONE,
        PARTIAL
    }

    /**
     * Partial Indexed Query management/transform/utils
     *
     * @author Fernando Miño, Geosolutions
     */
    public static class PartialIndexQueryManager extends IndexQueryManager {

        // indexed detected:
        private BinaryLogicOperator indexedParentLogicOperator;
        private List<Filter> indexedFilters;
        // builded indexed Query:
        private Query indexQuery;

        public PartialIndexQueryManager(FeatureTypeMapping mapping, Query query) {
            super(mapping, query);
            initDetection();
        }

        protected void initDetection() {
            IndexedFilterDetectorVisitor visitor = new SchemaIndexedFilterDetectorVisitor(mapping);
            query.getFilter().accept(visitor, null);
            indexedParentLogicOperator = visitor.getParentLogicOperator();
            indexedFilters = visitor.getIndexedFilters();
            buildIndexQuery();
        }

        private void buildIndexQuery() {
            Query idsQuery = new Query(query);
            idsQuery.setFilter(buildIndexFilter());
            idsQuery.setProperties(Query.NO_PROPERTIES);
            indexQuery = idsQuery;
        }

        private Filter buildIndexFilter() {
            List<Filter> dupFilters = duplicateFilters(indexedFilters);
            Filter indexFilter = createLogicOperator(indexedParentLogicOperator, dupFilters);
            return indexFilter;
        }

        private BinaryLogicOperator createLogicOperator(
                BinaryLogicOperator originalOperator, List<Filter> filters) {
            if (originalOperator instanceof Or) {
                return ff.or(filters);
            } else {
                return ff.and(filters);
            }
        }

        private List<Filter> duplicateFilters(List<Filter> filterList) {
            Filters filtersUtil = new Filters();
            return filterList.stream()
                    .map(f -> filtersUtil.duplicate(f))
                    .collect(Collectors.toList());
        }

        public Query getIndexQuery() {
            return indexQuery;
        }

        public Query buildCombinedQuery(
                FeatureCollection<? extends FeatureType, ? extends Feature> featureCollection) {
            List<String> ids =
                    FeatureStreams.toFeatureStream(featureCollection)
                            .map(Feature::getIdentifier)
                            .map(FeatureId::getID)
                            .collect(Collectors.toList());
            Filter filter1 = buildCombinedFilter(ids);
            Query query1 = new Query(query);
            query1.setFilter(filter1);
            return query1;
        }

        public Query buildCombinedQuery(List<String> ids) {
            Filter filter1 = buildCombinedFilter(ids);
            Query query1 = new Query(query);
            query1.setFilter(filter1);
            return query1;
        }

        private Filter buildCombinedFilter(List<String> ids) {
            // Ids in filter:
            Filter idsIn = IndexQueryUtils.buildIdInExpression(ids, mapping);
            // build new and/or operator
            IndexCombinedFilterTransformerVisitor visitor =
                    new IndexCombinedFilterTransformerVisitor(
                            indexedParentLogicOperator, indexedFilters, idsIn);
            Filter resultFilter = (Filter) query.getFilter().accept(visitor, ff);
            return resultFilter;
        }
    }
}
