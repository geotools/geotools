/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.catalog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.collection.DecoratingSimpleFeatureCollection;
import org.geotools.feature.collection.DecoratingSimpleFeatureIterator;
import org.geotools.util.SoftValueHashMap;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class QueryCacheGranuleCatalog extends DelegatingGranuleCatalog {

    static final Logger LOGGER = Logging.getLogger(QueryCacheGranuleCatalog.class);

    class CachingFeatureIterator extends DecoratingSimpleFeatureIterator {

        private final Query query;
        private final SimpleFeatureType schema;
        List<SimpleFeature> features = new ArrayList<>();
        protected boolean completed;

        /**
         * Wrap the provided iterator up as a FeatureIterator.
         *
         * @param iterator Iterator to be used as a delegate.
         */
        public CachingFeatureIterator(
                Query query, SimpleFeatureType schema, SimpleFeatureIterator iterator) {
            super(iterator);
            this.query = query;
            this.schema = schema;
        }

        @Override
        public boolean hasNext() {
            boolean hasNext = super.hasNext();
            if (!hasNext) completed = true;
            return hasNext;
        }

        @Override
        public SimpleFeature next() throws NoSuchElementException {
            SimpleFeature next = super.next();
            if (features.size() < maxCachedFeatures) features.add(next);
            return next;
        }

        @Override
        public void close() {
            if (features.size() < maxCachedFeatures) {
                queryCache.put(getCacheKey(query), new ExpiringFeatureCollection(schema, features));
            }
            super.close();
        }
    }

    private static class ExpiringFeatureCollection extends ListFeatureCollection {
        private final long created;

        public ExpiringFeatureCollection(SimpleFeatureType schema, List<SimpleFeature> features) {
            super(schema, features);
            this.created = System.currentTimeMillis();
        }

        public boolean isExpired(int maxAge) {
            return System.currentTimeMillis() - maxAge > created;
        }
    }

    private class CachingFeatureCollection extends DecoratingSimpleFeatureCollection {

        private final Query query;

        public CachingFeatureCollection(Query query, SimpleFeatureCollection delegate) {
            super(delegate);
            this.query = query;
        }

        @Override
        public SimpleFeatureIterator features() {
            return new CachingFeatureIterator(query, getSchema(), delegate.features());
        }
    }

    private final int maxCachedFeatures;

    private final int maxAge;

    private final SoftValueHashMap<Query, ExpiringFeatureCollection> queryCache =
            new SoftValueHashMap<>();

    public QueryCacheGranuleCatalog(
            AbstractGTDataStoreGranuleCatalog adaptee, int maxCachedFeatures, int maxAge) {
        super(adaptee);
        this.maxCachedFeatures = maxCachedFeatures;
        this.maxAge = maxAge;
    }

    @Override
    public SimpleFeatureCollection getGranules(Query q) throws IOException {
        return this.getGranules(q, Transaction.AUTO_COMMIT);
    }

    @Override
    public SimpleFeatureCollection getGranules(Query q, Transaction t) throws IOException {
        if (maxAge > 0 && t == Transaction.AUTO_COMMIT) {
            ExpiringFeatureCollection cached = queryCache.get(getCacheKey(q));
            if (cached != null) {
                if (!cached.isExpired(maxAge)) {
                    LOGGER.log(Level.FINE, () -> "Cache hit on query: " + q);
                    return cached;
                } else {
                    LOGGER.log(
                            Level.FINE,
                            () -> "Cache found, but expired, refreshing, on query: " + q);
                }
            } else {
                LOGGER.log(Level.FINE, "Cache miss on query: " + q);
            }
            return new CachingFeatureCollection(q, adaptee.getGranules(q, t));
        }
        return adaptee.getGranules(q);
    }

    private Query getCacheKey(Query q) {
        Query key = new Query(q);
        // clean up hints that do not affect which features are loaded
        key.getHints().remove(CatalogConfigurationBeans.COVERAGE_NAME);
        return key;
    }

    @Override
    public void addGranules(
            String typeName, Collection<SimpleFeature> granules, Transaction transaction)
            throws IOException {
        queryCache.clear();
        super.addGranules(typeName, granules, transaction);
    }

    @Override
    public void removeType(String typeName) throws IOException {
        queryCache.clear();
        super.removeType(typeName);
    }

    @Override
    public void addGranule(String typeName, SimpleFeature granule, Transaction transaction)
            throws IOException {
        queryCache.clear();
        super.addGranule(typeName, granule, transaction);
    }

    @Override
    public int removeGranules(Query query) {
        queryCache.clear();
        return super.removeGranules(query);
    }

    @Override
    public int removeGranules(Query query, Transaction transaction) {
        queryCache.clear();
        return super.removeGranules(query, transaction);
    }
}
