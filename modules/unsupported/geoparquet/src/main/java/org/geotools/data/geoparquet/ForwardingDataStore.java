/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.geoparquet;

import java.io.IOException;
import java.util.List;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.LockingManager;
import org.geotools.api.data.Query;
import org.geotools.api.data.ServiceInfo;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;

/**
 * A decorator implementation of the DataStore interface that forwards all method calls to a delegate DataStore
 * instance.
 *
 * <p>This class is used as a base for creating specialized DataStore implementations that need to modify or extend the
 * behavior of an existing DataStore without modifying the original class.
 *
 * @param <S> The type of DataStore being decorated
 */
public class ForwardingDataStore<S extends DataStore> implements DataStore {

    protected final S delegate;

    /**
     * Creates a new forwarding datastore that delegates all calls to the provided datastore.
     *
     * @param delegate The datastore to forward calls to
     */
    public ForwardingDataStore(S delegate) {
        this.delegate = delegate;
    }

    @Override
    public ServiceInfo getInfo() {
        return delegate.getInfo();
    }

    @Override
    public String[] getTypeNames() throws IOException {
        return delegate.getTypeNames();
    }

    @Override
    public List<Name> getNames() throws IOException {
        return delegate.getNames();
    }

    @Override
    public SimpleFeatureType getSchema(Name name) throws IOException {
        return delegate.getSchema(name);
    }

    @Override
    public SimpleFeatureType getSchema(String typeName) throws IOException {
        return delegate.getSchema(typeName);
    }

    @Override
    public SimpleFeatureSource getFeatureSource(String typeName) throws IOException {
        return delegate.getFeatureSource(typeName);
    }

    @Override
    public SimpleFeatureSource getFeatureSource(Name typeName) throws IOException {
        return delegate.getFeatureSource(typeName);
    }

    @Override
    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(Query query, Transaction transaction)
            throws IOException {
        return delegate.getFeatureReader(query, transaction);
    }

    @Override
    public void dispose() {
        delegate.dispose();
    }

    @Override
    public void createSchema(SimpleFeatureType featureType) throws IOException {
        delegate.createSchema(featureType);
    }

    @Override
    public void updateSchema(String typeName, SimpleFeatureType featureType) throws IOException {
        delegate.updateSchema(typeName, featureType);
    }

    @Override
    public void updateSchema(Name typeName, SimpleFeatureType featureType) throws IOException {
        delegate.updateSchema(typeName, featureType);
    }

    @Override
    public void removeSchema(Name typeName) throws IOException {
        delegate.removeSchema(typeName);
    }

    @Override
    public void removeSchema(String typeName) throws IOException {
        delegate.removeSchema(typeName);
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
            String typeName, Filter filter, Transaction transaction) throws IOException {
        return delegate.getFeatureWriter(typeName, filter, transaction);
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName, Transaction transaction)
            throws IOException {
        return delegate.getFeatureWriter(typeName, transaction);
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(
            String typeName, Transaction transaction) throws IOException {
        return delegate.getFeatureWriterAppend(typeName, transaction);
    }

    @Override
    public LockingManager getLockingManager() {
        return delegate.getLockingManager();
    }
}
