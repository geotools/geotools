/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.geojson.store;

import java.io.IOException;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.Query;
import org.geotools.api.data.QueryCapabilities;
import org.geotools.api.data.ResourceInfo;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureStore;
import org.geotools.data.store.ContentState;
import org.geotools.geometry.jts.ReferencedEnvelope;

public class GeoJSONFeatureStore extends ContentFeatureStore {
    private GeoJSONFeatureSource delegate;
    private boolean writeBounds = false;

    public GeoJSONFeatureStore(ContentEntry entry, Query query) {
        super(entry, query);
        delegate =
                new GeoJSONFeatureSource(entry, query) {
                    @Override
                    public void setTransaction(Transaction transaction) {
                        super.setTransaction(transaction);
                        GeoJSONFeatureStore.this.setTransaction(transaction);
                    }
                };
        delegate.setQuick(((GeoJSONDataStore) entry.getDataStore()).isQuick());
    }

    @Override
    protected FeatureWriter<SimpleFeatureType, SimpleFeature> getWriterInternal(
            Query query, int flags) throws IOException {
        GeoJSONFeatureWriter writer = new GeoJSONFeatureWriter(entry, query);
        writer.setWriteBounds(writeBounds);
        return writer;
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        return delegate.getBoundsInternal(query);
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {

        return delegate.getCountInternal(query);
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {
        return delegate.getReaderInternal(query);
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        return delegate.buildFeatureType();
    }

    /** @see GeoJSONFeatureSource#getDataStore() */
    @Override
    public GeoJSONDataStore getDataStore() {
        return delegate.getDataStore();
    }

    /** @see org.geotools.data.store.ContentFeatureSource#getEntry() */
    @Override
    public ContentEntry getEntry() {
        return delegate.getEntry();
    }

    /** @see org.geotools.data.store.ContentFeatureSource#getTransaction() */
    @Override
    public Transaction getTransaction() {
        return delegate.getTransaction();
    }

    @Override
    public void setTransaction(Transaction transaction) {
        super.setTransaction(transaction);
        if (delegate.getTransaction() != transaction) {
            delegate.setTransaction(transaction);
        }
    }
    /** @see org.geotools.data.store.ContentFeatureSource#getState() */
    @Override
    public ContentState getState() {
        return delegate.getState();
    }

    /** @see org.geotools.data.store.ContentFeatureSource#getInfo() */
    @Override
    public ResourceInfo getInfo() {
        return delegate.getInfo();
    }

    /** @see org.geotools.data.store.ContentFeatureSource#getName() */
    @Override
    public Name getName() {
        return delegate.getName();
    }

    /** @see org.geotools.data.store.ContentFeatureSource#getQueryCapabilities() */
    @Override
    public QueryCapabilities getQueryCapabilities() {
        return delegate.getQueryCapabilities();
    }

    /** @param writeBounds */
    public void setWriteBounds(boolean writeBounds) {
        this.writeBounds = writeBounds;
    }
}
