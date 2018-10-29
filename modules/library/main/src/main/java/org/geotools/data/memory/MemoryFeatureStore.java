/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.memory;

import java.io.IOException;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.Transaction;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureStore;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class MemoryFeatureStore extends ContentFeatureStore {

    public MemoryFeatureStore(ContentEntry entry, Query query) {
        super(entry, query);
    }

    @Override
    public MemoryState getState() {
        return (MemoryState) super.getState();
    }

    @Override
    protected FeatureWriter<SimpleFeatureType, SimpleFeature> getWriterInternal(
            Query query, int flags) throws IOException {
        // TODO Auto-generated method stub
        return new MemoryFeatureWriter(getState(), query);
    }

    /**
     * Delegate used for FeatureSource methods (We do this because Java cannot inherit from both
     * ContentFeatureStore and CSVFeatureSource at the same time
     */
    MemoryFeatureSource delegate =
            new MemoryFeatureSource(entry, query) {
                @Override
                public void setTransaction(Transaction transaction) {
                    super.setTransaction(transaction);
                    MemoryFeatureStore.this.setTransaction(
                            transaction); // Keep these two implementations on the same transaction
                }
            };

    @Override
    public void setTransaction(Transaction transaction) {
        super.setTransaction(transaction);
        if (delegate.getTransaction() != transaction) {
            delegate.setTransaction(transaction);
        }
    }

    //
    // Internal Delegate Methods
    // Implement FeatureSource methods using CSVFeatureSource implementation
    //
    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        return delegate.buildFeatureType();
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
    protected boolean handleVisitor(Query query, FeatureVisitor visitor) throws IOException {
        return delegate.handleVisitor(query, visitor);
    }

    @Override
    protected QueryCapabilities buildQueryCapabilities() {
        return new QueryCapabilities() {
            @Override
            public boolean isUseProvidedFIDSupported() {
                return true;
            }
        };
    }
}
