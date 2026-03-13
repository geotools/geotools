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
import org.geotools.util.decorate.Wrapper;

/**
 * Decorator implementation of {@link DataStore} that forwards all method calls to a delegate datastore.
 *
 * <p>The wrapper preserves the intent of shielding JDBC internals from general use while still allowing explicit
 * unwrapping for advanced internal use cases.
 *
 * @param <S> the type of datastore being decorated
 */
public class ForwardingDataStore<S extends DataStore> implements DataStore, Wrapper {

    protected final S delegate;

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
        return ForwardingFeatureSource.wrap(delegate.getFeatureSource(typeName), this);
    }

    @Override
    public SimpleFeatureSource getFeatureSource(Name typeName) throws IOException {
        return ForwardingFeatureSource.wrap(delegate.getFeatureSource(typeName), this);
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

    @Override
    public boolean isWrapperFor(Class<?> aClass) {
        return aClass != null && aClass.isInstance(this);
    }

    @Override
    public <T> T unwrap(Class<T> aClass) throws IllegalArgumentException {
        if (isWrapperFor(aClass)) {
            return aClass.cast(this);
        }
        throw new IllegalArgumentException(getClass().getName() + " does not wrap " + aClass.getName());
    }
}
