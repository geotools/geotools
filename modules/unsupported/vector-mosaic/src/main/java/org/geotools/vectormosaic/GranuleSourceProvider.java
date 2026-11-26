/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.Query;
import org.geotools.api.data.QueryCapabilities;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.sort.SortBy;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.util.logging.Logging;

/** Walks the index rows, opens the granule of each one, and builds the query and the sort its store gets. */
class GranuleSourceProvider implements Closeable {

    static final Logger LOGGER = Logging.getLogger(GranuleSourceProvider.class);

    private final VectorMosaicFeatureSource source;

    private final Query query;
    private final SimpleFeatureIterator delegateIterator;

    DataStore granuleDataStore;

    String params;

    protected SimpleFeature delegateFeature = null;

    /** The index row before delegateFeature */
    private SimpleFeature previousDelegateFeature;

    private Query granuleQuery;

    /** The index row after delegateFeature, read ahead to compare their index values */
    private SimpleFeature nextDelegateFeature;

    /** The index sort attributes of a group sort, empty otherwise */
    private final List<String> groupAttributes;

    /** The sort pushed to a granule alone in its group. Empty when the granules keep the store order. */
    private final SortBy[] granuleSort;

    /** True if the current granule is alone in its group and its store sorted it on granuleSort */
    private boolean granuleSorted;

    /** Builds a provider for the index rows of delegateCollection, for a query without a group sort */
    GranuleSourceProvider(VectorMosaicFeatureSource source, SimpleFeatureCollection delegateCollection, Query query) {
        this(source, delegateCollection, query, List.of());
    }

    /**
     * Builds a provider for the index rows of delegateCollection.
     *
     * @param groupKeys the index sort keys whose values define the groups of a group sort. Empty when the query has no
     *     group sort, or when the whole mosaic is one group.
     */
    GranuleSourceProvider(
            VectorMosaicFeatureSource source,
            SimpleFeatureCollection delegateCollection,
            Query query,
            List<SortBy> groupKeys) {
        this.source = source;
        this.query = query;
        this.groupAttributes = groupKeys.stream()
                .map(sb -> sb.getPropertyName().getPropertyName())
                .toList();
        this.granuleSort = getGranuleSort(groupKeys.size());
        this.delegateIterator = delegateCollection.features();
    }

    /**
     * Returns the keys after the index sort that are granule attributes. An index attribute has the same value in the
     * whole granule, so the granule store does not need it. Returns an empty array when a key is the natural order.
     */
    private SortBy[] getGranuleSort(int groupKeys) {
        SortBy[] sortBy = query.getSortBy();
        if (sortBy == null) {
            return new SortBy[0];
        }
        List<SortBy> remaining = Arrays.asList(sortBy).subList(groupKeys, sortBy.length);
        if (remaining.stream().anyMatch(sb -> sb.getPropertyName() == null)) {
            return new SortBy[0];
        }
        Set<String> indexAttributes = source.getIndexAttributeNames();
        return remaining.stream()
                .filter(sb -> !indexAttributes.contains(sb.getPropertyName().getPropertyName()))
                .toArray(SortBy[]::new);
    }

    private Filter getGranuleFilter(VectorMosaicGranule granule) {
        Filter filter = source.getSplitFilter(query, granuleDataStore, granule.getGranuleTypeName(), false);
        Filter configuredFilter = granule.getFilter();
        if (configuredFilter != null && configuredFilter != Filter.INCLUDE) {
            FilterFactory ff = source.getDataStore().getFilterFactory();
            return ff.and(filter, configuredFilter);
        }
        return filter;
    }

    public SimpleFeatureSource getNextGranuleSource() throws IOException {
        while (peekDelegateFeature() != null) {
            previousDelegateFeature = delegateFeature;
            delegateFeature = nextDelegateFeature;
            nextDelegateFeature = null;
            VectorMosaicGranule granule = VectorMosaicGranule.fromDelegateFeature(delegateFeature);
            // if tracking granule statistics, this is how many times granules are accessed
            if (source.finder.granuleTracker != null) {
                source.finder.granuleTracker.incrementAccessCount();
            }
            if (params != null && params.equals(granule.getParams())) {
                // same connection, no need to reinitialize
                source.populateGranuleTypeName(granule, granuleDataStore);
            } else {
                // different connection, need to reinitialize
                if (granuleDataStore != null) {
                    granuleDataStore.dispose();
                }
                granuleDataStore = source.initGranule(granule, false);
                params = granule.getParams();
            }

            Filter granuleFilter = getGranuleFilter(granule);
            this.granuleQuery = new Query(granule.getGranuleTypeName(), granuleFilter);
            if (query.getPropertyNames() != Query.ALL_NAMES) {
                String[] filteredArray = VectorMosaicFeatureSource.getOnlyTypeMatchingAttributes(
                        granuleDataStore, granule.getGranuleTypeName(), query, false);
                granuleQuery.setPropertyNames(filteredArray);
            }
            return granuleDataStore.getFeatureSource(granule.getGranuleTypeName());
        }

        return null;
    }

    private SimpleFeature peekDelegateFeature() {
        if (nextDelegateFeature == null && delegateIterator.hasNext()) {
            nextDelegateFeature = delegateIterator.next();
        }
        return nextDelegateFeature;
    }

    /** Returns true if the current granule is alone in its group and its store sorted it */
    boolean isGranuleSorted() {
        return granuleSorted;
    }

    /** Returns true if the index rows before and after the current one have other index values */
    private boolean isAloneInGroup() {
        List<Object> values = getGroupValues(delegateFeature);
        SimpleFeature next = peekDelegateFeature();
        return (previousDelegateFeature == null || !values.equals(getGroupValues(previousDelegateFeature)))
                && (next == null || !values.equals(getGroupValues(next)));
    }

    /** Returns the values of the group sort index attributes. Works on index rows and on mosaic features. */
    List<Object> getGroupValues(SimpleFeature feature) {
        // an index attribute can be null. Stream.toList accepts null values, List.of does not.
        return groupAttributes.stream().map(feature::getAttribute).toList();
    }

    /**
     * Returns the features of the granule, sorted as far as the mosaic needs.
     *
     * <p>A group sort sends the granule attribute keys to a granule alone in its group, when its store can sort on
     * them. {@link GroupSortingReader} sorts the other groups.
     *
     * <p>A lone NATURAL_ORDER goes to a granule store that sorts it natively. Any other store returns its own order:
     * asked for the natural order, it would sort a copy on the feature identifiers, and a JDBC table without a primary
     * key throws.
     */
    public SimpleFeatureCollection getGranuleFeatures(SimpleFeatureSource granuleSource) throws IOException {
        if (granuleSort.length > 0) {
            // GroupSortingReader sorts a group of granules again, so a sort of each granule would be wasted
            QueryCapabilities capabilities = granuleSource.getQueryCapabilities();
            granuleSorted = isAloneInGroup() && capabilities.supportsSorting(granuleSort);
            return granuleSorted
                    ? getSortedFeatures(granuleSource, granuleSort)
                    : granuleSource.getFeatures(granuleQuery);
        }
        SortBy[] sortBy = query.getSortBy();
        if (VectorMosaicFeatureSource.isNaturalOrder(sortBy)
                && VectorMosaicFeatureSource.isNativeSort(granuleSource, sortBy)) {
            return getSortedFeatures(granuleSource, sortBy);
        }
        return granuleSource.getFeatures(granuleQuery);
    }

    private SimpleFeatureCollection getSortedFeatures(SimpleFeatureSource granuleSource, SortBy... sortBy)
            throws IOException {
        Query sortedQuery = new Query(granuleQuery);
        sortedQuery.setSortBy(sortBy);
        return granuleSource.getFeatures(sortedQuery);
    }

    public Query getQuery() {
        return query;
    }

    public Query getGranuleQuery() {
        return granuleQuery;
    }

    public SimpleFeature getDelegateFeature() {
        return delegateFeature;
    }

    @Override
    public void close() throws IOException {
        try {
            if (delegateIterator != null) {
                delegateIterator.close();
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to close delegate iterator", e);
        }
        if (granuleDataStore != null) {
            granuleDataStore.dispose();
        }
    }
}
