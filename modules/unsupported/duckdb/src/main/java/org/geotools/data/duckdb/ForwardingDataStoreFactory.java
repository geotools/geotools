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
import java.util.Map;
import java.util.Objects;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFactorySpi;

/**
 * Decorator implementation of {@link DataStoreFactorySpi} that forwards all method calls to a delegate factory.
 *
 * @param <D> the type of datastore factory being decorated
 */
public class ForwardingDataStoreFactory<D extends DataStoreFactorySpi> implements DataStoreFactorySpi {

    protected final D delegate;

    public ForwardingDataStoreFactory(D delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    @Override
    public String getDisplayName() {
        return delegate.getDisplayName();
    }

    @Override
    public String getDescription() {
        return delegate.getDescription();
    }

    @Override
    public Param[] getParametersInfo() {
        return delegate.getParametersInfo();
    }

    @Override
    public boolean isAvailable() {
        return delegate.isAvailable();
    }

    @Override
    public boolean canProcess(Map<String, ?> params) {
        return delegate.canProcess(params);
    }

    @Override
    public DataStore createDataStore(Map<String, ?> params) throws IOException {
        return delegate.createDataStore(params);
    }

    @Override
    public DataStore createNewDataStore(Map<String, ?> params) throws IOException {
        return delegate.createNewDataStore(params);
    }
}
