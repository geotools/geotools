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
package org.geotools.gce.imagemosaic.catalog;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;
import org.geotools.coverage.grid.io.footprint.MultiLevelROI;
import org.geotools.coverage.grid.io.footprint.MultiLevelROIProvider;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.util.factory.Hints;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.BoundingBox;

/**
 * Applies read/write locks around all operations to protect the underlying store, which might not
 * be able to handle this scenario correctly
 */
class LockingGranuleCatalog extends GranuleCatalog {

    GranuleCatalog delegate;
    ReadWriteLock rwLock = new ReentrantReadWriteLock(true);

    /** */
    public LockingGranuleCatalog(GranuleCatalog delegate, Hints hints) {
        super(hints);
        this.delegate = delegate;
    }

    interface IOCallable<V> {
        V call() throws IOException;
    }

    interface IORunnable {
        void run() throws IOException;
    }

    private void guard(Runnable operation, Lock lock) {
        try {
            lock.lock();
            operation.run();
        } finally {
            lock.unlock();
        }
    }

    private <V> V guard(Supplier<V> supplier, Lock lock) {
        try {
            lock.lock();
            return supplier.get();
        } finally {
            lock.unlock();
        }
    }

    private <V> V guardIO(IOCallable<V> operation, Lock lock) throws IOException {
        try {
            lock.lock();
            return operation.call();
        } finally {
            lock.unlock();
        }
    }

    private void guardIO(IORunnable operation, Lock lock) throws IOException {
        try {
            lock.lock();
            operation.run();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void addGranule(String typeName, SimpleFeature granule, Transaction transaction)
            throws IOException {
        guardIO(() -> delegate.addGranule(typeName, granule, transaction), rwLock.writeLock());
    }

    @Override
    public void addGranules(
            String typeName, Collection<SimpleFeature> granules, Transaction transaction)
            throws IOException {
        guardIO(() -> delegate.addGranules(typeName, granules, transaction), rwLock.writeLock());
    }

    @Override
    public void computeAggregateFunction(Query q, FeatureCalc function) throws IOException {
        guardIO(() -> delegate.computeAggregateFunction(q, function), rwLock.readLock());
    }

    @Override
    public void createType(String namespace, String typeName, String typeSpec)
            throws IOException, SchemaException {
        Lock lock = rwLock.writeLock();
        try {
            lock.lock();
            delegate.createType(namespace, typeName, typeSpec);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void createType(SimpleFeatureType featureType) throws IOException {
        guardIO(() -> delegate.createType(featureType), rwLock.writeLock());
    }

    @Override
    public void createType(String identification, String typeSpec)
            throws SchemaException, IOException {
        Lock lock = rwLock.writeLock();
        try {
            lock.lock();
            delegate.createType(identification, typeSpec);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void dispose() {
        Lock lock = rwLock.writeLock();
        try {
            lock.lock();
            delegate.dispose();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public BoundingBox getBounds(String typeName) {
        Lock lock = rwLock.readLock();
        try {
            lock.lock();
            return delegate.getBounds(typeName);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public SimpleFeatureCollection getGranules(Query q) throws IOException {
        return guardIO(() -> delegate.getGranules(q), rwLock.readLock());
    }

    @Override
    public int getGranulesCount(Query q) throws IOException {
        return guardIO(() -> delegate.getGranulesCount(q), rwLock.readLock());
    }

    @Override
    public void getGranuleDescriptors(Query q, GranuleCatalogVisitor visitor) throws IOException {
        guardIO(() -> delegate.getGranuleDescriptors(q, visitor), rwLock.readLock());
    }

    @Override
    public QueryCapabilities getQueryCapabilities(String typeName) {
        return guard(() -> delegate.getQueryCapabilities(typeName), rwLock.readLock());
    }

    @Override
    public SimpleFeatureType getType(String typeName) throws IOException {
        return guardIO(() -> delegate.getType(typeName), rwLock.readLock());
    }

    @Override
    public void removeType(String typeName) throws IOException {
        guardIO(() -> delegate.removeType(typeName), rwLock.writeLock());
    }

    @Override
    public int removeGranules(Query query) {
        return guard(() -> delegate.removeGranules(query), rwLock.writeLock());
    }

    @Override
    public String[] getTypeNames() {
        Lock lock = rwLock.readLock();
        try {
            lock.lock();
            return delegate.getTypeNames();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Query mergeHints(Query q) {
        return guard(() -> delegate.mergeHints(q), rwLock.readLock());
    }

    @Override
    public void setMultiScaleROIProvider(MultiLevelROIProvider footprintProvider) {
        guard(() -> delegate.setMultiScaleROIProvider(footprintProvider), rwLock.writeLock());
    }

    @Override
    public MultiLevelROI getGranuleFootprint(SimpleFeature sf) {
        return guard(() -> delegate.getGranuleFootprint(sf), rwLock.readLock());
    }

    @Override
    public List<File> getFootprintFiles(SimpleFeature sf) throws IOException {
        return guardIO(() -> delegate.getFootprintFiles(sf), rwLock.readLock());
    }

    @Override
    public void drop() throws IOException {
        guardIO(() -> delegate.drop(), rwLock.writeLock());
    }
}
