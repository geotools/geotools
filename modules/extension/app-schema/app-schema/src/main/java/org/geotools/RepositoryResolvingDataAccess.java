/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools;

import java.io.IOException;
import java.util.List;
import org.geotools.api.data.DataAccess;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.Repository;
import org.geotools.api.data.ServiceInfo;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.Name;

/**
 * A DataAccess wrapper that does not keep a hard reference to the delegate. Instead, it resolves the delegate from a
 * Repository and store Name on demand.
 *
 * @param <T> feature type
 * @param <F> feature
 */
public final class RepositoryResolvingDataAccess<T extends FeatureType, F extends Feature> implements DataAccess<T, F> {

    private final Repository repository;
    private final Name storeName;

    public RepositoryResolvingDataAccess(Repository repository, Name storeName) {
        this.repository = repository;
        this.storeName = storeName;
    }

    public Repository getRepository() {
        return repository;
    }

    public Name getStoreName() {
        return storeName;
    }

    @Override
    public void createSchema(T featureType) throws IOException {
        delegate().createSchema(featureType);
    }

    @Override
    public ServiceInfo getInfo() {
        return delegate().getInfo();
    }

    @Override
    public List<Name> getNames() throws IOException {
        return delegate().getNames();
    }

    @Override
    public T getSchema(Name name) throws IOException {
        return delegate().getSchema(name);
    }

    @Override
    public FeatureSource<T, F> getFeatureSource(Name typeName) throws IOException {
        return delegate().getFeatureSource(typeName);
    }

    @Override
    public void updateSchema(Name typeName, T featureType) throws IOException {
        delegate().updateSchema(typeName, featureType);
    }

    @Override
    public void removeSchema(Name typeName) throws IOException {
        delegate().removeSchema(typeName);
    }

    @Override
    public void dispose() {
        // no op
    }

    @SuppressWarnings("unchecked")
    private DataAccess<T, F> delegate() {
        return (DataAccess) repository.access(storeName);
    }
}
