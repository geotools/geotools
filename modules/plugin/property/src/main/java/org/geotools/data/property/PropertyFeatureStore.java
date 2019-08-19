/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.property;

import java.io.IOException;
import java.util.Set;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.ResourceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureStore;
import org.geotools.data.store.ContentState;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.factory.Hints;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

/**
 * Implementation used for writeable property files. Supports limited caching of number of features
 * and bounds.
 *
 * @author Jody Garnett
 * @author Torben Barsballe (Boundless)
 */
public class PropertyFeatureStore extends ContentFeatureStore {

    String typeName;
    SimpleFeatureType featureType;
    PropertyDataStore store;

    PropertyFeatureStore(ContentEntry entry, Query query) throws IOException {
        super(entry, query);
        this.store = (PropertyDataStore) entry.getDataStore();
        this.typeName = entry.getTypeName();
    }

    @Override
    protected void addHints(Set<Hints.Key> hints) {
        // mark the features as detached, that is, the user can directly alter them
        // without altering the state of the DataStore
        hints.add(Hints.FEATURE_DETACHED);
    }

    /** We handle events internally */
    protected boolean canEvent() {
        return false;
    }

    protected QueryCapabilities buildQueryCapabilities() {
        return new QueryCapabilities() {
            public boolean isUseProvidedFIDSupported() {
                return true;
            }
        };
    }

    @Override
    protected FeatureWriter<SimpleFeatureType, SimpleFeature> getWriterInternal(
            Query query, int flags) throws IOException {
        return new PropertyFeatureWriter(
                this, getState(), query, (flags | WRITER_ADD) == WRITER_ADD);
    }

    /**
     * Delegate used for FeatureSource methods (We do this because Java cannot inherit from both
     * ContentFeatureStore and CSVFeatureSource at the same time
     */
    PropertyFeatureSource delegate =
            new PropertyFeatureSource(entry, query) {
                @Override
                public void setTransaction(Transaction transaction) {
                    super.setTransaction(transaction);
                    PropertyFeatureStore.this.setTransaction(
                            transaction); // Keep these two implementations on the same transaction
                }
            };

    //
    // Internal Delegate Methods
    // Implement FeatureSource methods using CSVFeatureSource implementation
    //
    @Override
    public void setTransaction(Transaction transaction) {
        super.setTransaction(transaction);
        if (delegate.getTransaction() != transaction) {
            delegate.setTransaction(transaction);
        }
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

    @Override
    protected boolean handleVisitor(Query query, FeatureVisitor visitor) throws IOException {
        return delegate.handleVisitor(query, visitor);
    }
    //
    // Public Delegate Methods
    // Implement FeatureSource methods using CSVFeatureSource implementation
    //
    @Override
    public PropertyDataStore getDataStore() {
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
