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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.util.logging.Logging;

/**
 * Fed with a delegate feature collection, transforms each in a granule sources, or into a granule
 * collection with the expected filter.
 */
class GranuleSourceProvider implements Closeable {

    static final Logger LOGGER = Logging.getLogger(GranuleSourceProvider.class);

    private final VectorMosaicFeatureSource source;

    private final Query query;
    private final SimpleFeatureIterator delegateIterator;

    DataStore granuleDataStore;

    String params;

    protected SimpleFeature delegateFeature = null;
    private Query granuleQuery;

    GranuleSourceProvider(
            VectorMosaicFeatureSource source,
            SimpleFeatureCollection delegateCollection,
            Query query) {
        this.source = source;
        this.query = query;
        this.delegateIterator = delegateCollection.features();
    }

    private Filter getGranuleFilter(VectorMosaicGranule granule) {
        Filter filter =
                source.getSplitFilter(query, granuleDataStore, granule.getGranuleTypeName(), false);
        Filter configuredFilter = granule.getFilter();
        if (configuredFilter != null && configuredFilter != Filter.INCLUDE) {
            FilterFactory ff = source.getDataStore().getFilterFactory();
            return ff.and(filter, configuredFilter);
        }
        return filter;
    }

    public SimpleFeatureSource getNextGranuleSource() throws IOException {
        while (delegateIterator.hasNext()) {
            delegateFeature = delegateIterator.next();
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
                String[] filteredArray =
                        VectorMosaicFeatureSource.getOnlyTypeMatchingAttributes(
                                granuleDataStore, granule.getGranuleTypeName(), query, false);
                granuleQuery.setPropertyNames(filteredArray);
            }

            return granuleDataStore.getFeatureSource(granule.getGranuleTypeName());
        }

        return null;
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
