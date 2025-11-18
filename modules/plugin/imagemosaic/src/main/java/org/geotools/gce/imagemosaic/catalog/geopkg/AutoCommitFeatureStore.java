/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.gce.imagemosaic.catalog.geopkg;

import java.awt.RenderingHints;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.geotools.api.data.DataAccess;
import org.geotools.api.data.FeatureListener;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.Query;
import org.geotools.api.data.QueryCapabilities;
import org.geotools.api.data.ResourceInfo;
import org.geotools.api.data.SimpleFeatureStore;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * A SimpleFeatureStore wrapper that ignores any attempt to set a transaction, making the store always operate in
 * auto-commit mode.
 */
class AutoCommitFeatureStore implements SimpleFeatureStore {
    SimpleFeatureStore delegate;

    public AutoCommitFeatureStore(SimpleFeatureStore delegate) {
        this.delegate = delegate;
    }

    @Override
    public void modifyFeatures(String name, Object attributeValue, Filter filter) throws IOException {
        delegate.modifyFeatures(name, attributeValue, filter);
    }

    @Override
    public void modifyFeatures(String[] names, Object[] attributeValues, Filter filter) throws IOException {
        delegate.modifyFeatures(names, attributeValues, filter);
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
    public List<FeatureId> addFeatures(FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection)
            throws IOException {
        return delegate.addFeatures(featureCollection);
    }

    @Override
    public void removeFeatures(Filter filter) throws IOException {
        delegate.removeFeatures(filter);
    }

    @Override
    public void modifyFeatures(Name[] attributeNames, Object[] attributeValues, Filter filter) throws IOException {
        delegate.modifyFeatures(attributeNames, attributeValues, filter);
    }

    @Override
    public void modifyFeatures(Name attributeName, Object attributeValue, Filter filter) throws IOException {
        delegate.modifyFeatures(attributeName, attributeValue, filter);
    }

    @Override
    public void setFeatures(FeatureReader<SimpleFeatureType, SimpleFeature> reader) throws IOException {
        delegate.setFeatures(reader);
    }

    @Override
    public void setTransaction(Transaction transaction) {
        // do not delegate the transaction, this store is always auto commit
        // delegate.setTransaction(transaction);
    }

    @Override
    public Transaction getTransaction() {
        return delegate.getTransaction();
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
    public DataAccess<SimpleFeatureType, SimpleFeature> getDataStore() {
        return delegate.getDataStore();
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
    public SimpleFeatureType getSchema() {
        return delegate.getSchema();
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
    public Set<RenderingHints.Key> getSupportedHints() {
        return delegate.getSupportedHints();
    }
}
