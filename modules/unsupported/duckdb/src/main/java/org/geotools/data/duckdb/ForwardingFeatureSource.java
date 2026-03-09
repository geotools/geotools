/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.duckdb;

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.geotools.api.data.DataAccess;
import org.geotools.api.data.FeatureListener;
import org.geotools.api.data.FeatureLock;
import org.geotools.api.data.Query;
import org.geotools.api.data.QueryCapabilities;
import org.geotools.api.data.ResourceInfo;
import org.geotools.api.data.SimpleFeatureLocking;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.data.SimpleFeatureStore;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;

/** Delegating feature source wrapper that can override the datastore reported by the delegate feature source. */
public class ForwardingFeatureSource implements SimpleFeatureSource {

    protected final SimpleFeatureSource delegate;
    private final DataAccess<SimpleFeatureType, SimpleFeature> dataStore;

    public ForwardingFeatureSource(SimpleFeatureSource delegate) {
        this(delegate, delegate.getDataStore());
    }

    public ForwardingFeatureSource(
            SimpleFeatureSource delegate, DataAccess<SimpleFeatureType, SimpleFeature> overridingStore) {
        this.delegate = delegate;
        this.dataStore = overridingStore;
    }

    static SimpleFeatureSource wrap(
            SimpleFeatureSource delegate, DataAccess<SimpleFeatureType, SimpleFeature> overridingStore) {
        if (delegate instanceof SimpleFeatureLocking locking) {
            return new ForwardingFeatureLocking(locking, overridingStore);
        }
        if (delegate instanceof SimpleFeatureStore store) {
            return new ForwardingFeatureStore(store, overridingStore);
        }
        return new ForwardingFeatureSource(delegate, overridingStore);
    }

    @Override
    public DataAccess<SimpleFeatureType, SimpleFeature> getDataStore() {
        return dataStore;
    }

    @Override
    public SimpleFeatureType getSchema() {
        return delegate.getSchema();
    }

    @Override
    public SimpleFeatureCollection getFeatures() throws IOException {
        return delegate.getFeatures();
    }

    @Override
    public SimpleFeatureCollection getFeatures(Filter filter) throws IOException {
        return delegate.getFeatures(filter);
    }

    @Override
    public SimpleFeatureCollection getFeatures(Query query) throws IOException {
        return delegate.getFeatures(query);
    }

    @Override
    public Name getName() {
        return delegate.getName();
    }

    @Override
    public ResourceInfo getInfo() {
        return delegate.getInfo();
    }

    @Override
    public QueryCapabilities getQueryCapabilities() {
        return delegate.getQueryCapabilities();
    }

    @Override
    public void addFeatureListener(FeatureListener listener) {
        delegate.addFeatureListener(listener);
    }

    @Override
    public void removeFeatureListener(FeatureListener listener) {
        delegate.removeFeatureListener(listener);
    }

    @Override
    public ReferencedEnvelope getBounds() throws IOException {
        return delegate.getBounds();
    }

    @Override
    public ReferencedEnvelope getBounds(Query query) throws IOException {
        return delegate.getBounds(query);
    }

    @Override
    public int getCount(Query query) throws IOException {
        return delegate.getCount(query);
    }

    @Override
    public Set<Key> getSupportedHints() {
        return delegate.getSupportedHints();
    }

    static class ForwardingFeatureStore extends ForwardingFeatureSource implements SimpleFeatureStore {

        private final SimpleFeatureStore store;

        ForwardingFeatureStore(
                SimpleFeatureStore delegate, DataAccess<SimpleFeatureType, SimpleFeature> overridingStore) {
            super(delegate, overridingStore);
            this.store = delegate;
        }

        @Override
        public List<FeatureId> addFeatures(
                org.geotools.feature.FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection)
                throws IOException {
            return store.addFeatures(featureCollection);
        }

        @Override
        public void removeFeatures(Filter filter) throws IOException {
            store.removeFeatures(filter);
        }

        @Override
        public void modifyFeatures(Name[] attributeNames, Object[] attributeValues, Filter filter) throws IOException {
            store.modifyFeatures(attributeNames, attributeValues, filter);
        }

        @Override
        public void modifyFeatures(Name attributeName, Object attributeValue, Filter filter) throws IOException {
            store.modifyFeatures(attributeName, attributeValue, filter);
        }

        @Override
        public void setFeatures(org.geotools.api.data.FeatureReader<SimpleFeatureType, SimpleFeature> reader)
                throws IOException {
            store.setFeatures(reader);
        }

        @Override
        public void setTransaction(Transaction transaction) {
            store.setTransaction(transaction);
        }

        @Override
        public Transaction getTransaction() {
            return store.getTransaction();
        }

        @Override
        public void modifyFeatures(String name, Object attributeValue, Filter filter) throws IOException {
            store.modifyFeatures(name, attributeValue, filter);
        }

        @Override
        public void modifyFeatures(String[] names, Object[] attributeValues, Filter filter) throws IOException {
            store.modifyFeatures(names, attributeValues, filter);
        }
    }

    static final class ForwardingFeatureLocking extends ForwardingFeatureStore implements SimpleFeatureLocking {

        private final SimpleFeatureLocking locking;

        ForwardingFeatureLocking(
                SimpleFeatureLocking delegate, DataAccess<SimpleFeatureType, SimpleFeature> overridingStore) {
            super(delegate, overridingStore);
            this.locking = delegate;
        }

        @Override
        public void setFeatureLock(FeatureLock lock) {
            locking.setFeatureLock(lock);
        }

        @Override
        public int lockFeatures(Query query) throws IOException {
            return locking.lockFeatures(query);
        }

        @Override
        public int lockFeatures(Filter filter) throws IOException {
            return locking.lockFeatures(filter);
        }

        @Override
        public int lockFeatures() throws IOException {
            return locking.lockFeatures();
        }

        @Override
        public void unLockFeatures() throws IOException {
            locking.unLockFeatures();
        }

        @Override
        public void unLockFeatures(Filter filter) throws IOException {
            locking.unLockFeatures(filter);
        }

        @Override
        public void unLockFeatures(Query query) throws IOException {
            locking.unLockFeatures(query);
        }
    }
}
