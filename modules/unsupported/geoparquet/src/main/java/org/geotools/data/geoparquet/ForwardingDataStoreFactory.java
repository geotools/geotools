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
import java.util.Map;
import java.util.Objects;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFactorySpi;

/**
 * A decorator implementation of the DataStoreFactorySpi interface that forwards all method calls to a delegate
 * DataStoreFactorySpi instance.
 *
 * <p>This class follows the decorator pattern, allowing specialized DataStoreFactory implementations to extend or
 * modify the behavior of an existing factory without modifying the original class. It's particularly useful when
 * implementing adapter or wrapper factories that need to adjust the behavior of another factory.
 *
 * @param <D> The type of DataStoreFactorySpi being decorated
 */
public class ForwardingDataStoreFactory<D extends DataStoreFactorySpi> implements DataStoreFactorySpi {

    protected final D delegate;

    /**
     * Creates a new forwarding factory that delegates all calls to the provided factory.
     *
     * @param delegate The factory to forward calls to (must not be null)
     * @throws NullPointerException if delegate is null
     */
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
    public boolean canProcess(java.util.Map<String, ?> params) {
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
