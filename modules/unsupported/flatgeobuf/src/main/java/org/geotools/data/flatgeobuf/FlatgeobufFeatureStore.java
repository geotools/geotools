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
package org.geotools.data.flatgeobuf;

import java.io.IOException;
import org.geotools.data.*;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureStore;
import org.geotools.data.store.ContentState;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

public class FlatgeobufFeatureStore extends ContentFeatureStore {

    public FlatgeobufFeatureStore(ContentEntry entry, Query query) {
        super(entry, query);
    }

    FlatgeobufFeatureSource delegate =
            new FlatgeobufFeatureSource(entry, query) {
                @Override
                public void setTransaction(Transaction transaction) {
                    super.setTransaction(transaction);
                    FlatgeobufFeatureStore.this.setTransaction(transaction);
                }
            };

    @Override
    public void setTransaction(Transaction transaction) {
        super.setTransaction(transaction);
        if (delegate.getTransaction() != transaction) {
            delegate.setTransaction(transaction);
        }
    }

    @Override
    protected FlatgeobufFeatureWriter getWriterInternal(Query query, int flags) throws IOException {
        return new FlatgeobufFeatureWriter(getState(), query);
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {
        return delegate.getReaderInternal(query);
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
    protected SimpleFeatureType buildFeatureType() throws IOException {
        return delegate.buildFeatureType();
    }

    @Override
    protected boolean handleVisitor(Query query, FeatureVisitor visitor) throws IOException {
        return delegate.handleVisitor(query, visitor);
    }

    @Override
    public FlatgeobufDataStore getDataStore() {
        return delegate.getDataStore();
    }

    @Override
    public ContentEntry getEntry() {
        return delegate.getEntry();
    }

    public Transaction getTransaction() {
        return delegate.getTransaction();
    }

    public ContentState getState() {
        return delegate.getState();
    }

    public ResourceInfo getInfo() {
        return delegate.getInfo();
    }

    public Name getName() {
        return delegate.getName();
    }

    public QueryCapabilities getQueryCapabilities() {
        return delegate.getQueryCapabilities();
    }
}
