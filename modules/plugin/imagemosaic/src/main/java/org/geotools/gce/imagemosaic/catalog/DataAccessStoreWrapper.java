/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.geotools.api.data.DataAccess;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.LockingManager;
import org.geotools.api.data.Query;
import org.geotools.api.data.ServiceInfo;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.data.SimpleFeatureStore;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.data.DataUtilities;
import org.geotools.data.collection.DelegateFeatureReader;
import org.geotools.util.SoftValueHashMap;

/**
 * A wrapper trying to expose a DataAccess providing, among others, simple feature instances.
 *
 * @author Andrea Aime - GeoSolutions
 */
@SuppressWarnings("unchecked") // don't know how to make it work without raw types...
class DataAccessStoreWrapper implements DataStore {

    DataAccess delegate;

    SoftValueHashMap<String, Name> NAME_CACHE = new SoftValueHashMap<>();

    public DataAccessStoreWrapper(DataAccess<FeatureType, Feature> delegate) {
        this.delegate = delegate;
    }

    @Override
    public ServiceInfo getInfo() {
        return delegate.getInfo();
    }

    @Override
    public void createSchema(SimpleFeatureType featureType) throws IOException {
        delegate.createSchema(featureType);
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
    public List getNames() throws IOException {
        return delegate.getNames();
    }

    @Override
    public SimpleFeatureType getSchema(Name name) throws IOException {
        FeatureType schema = delegate.getSchema(name);
        if (schema instanceof SimpleFeatureType) {
            return (SimpleFeatureType) schema;
        } else {
            return null;
        }
    }

    @Override
    public SimpleFeatureSource getFeatureSource(Name typeName) throws IOException {
        return DataUtilities.simple(delegate.getFeatureSource(typeName));
    }

    @Override
    public void dispose() {
        delegate.dispose();
    }

    @Override
    public void updateSchema(String typeName, SimpleFeatureType featureType) throws IOException {
        Name name = getNameFromLocal(typeName);
        delegate.updateSchema(name, featureType);
    }

    @Override
    public void removeSchema(String typeName) throws IOException {
        Name name = getNameFromLocal(typeName);
        delegate.removeSchema(name);
    }

    @Override
    public String[] getTypeNames() throws IOException {
        List<Name> names = delegate.getNames();
        String[] typeNames =
                names.stream()
                        .map(name -> name.getLocalPart())
                        .distinct()
                        .sorted()
                        .toArray(String[]::new);
        return typeNames;
    }

    @Override
    public SimpleFeatureType getSchema(String typeName) throws IOException {
        Name name = getNameFromLocal(typeName);
        FeatureType schema = delegate.getSchema(name);
        if (schema instanceof SimpleFeatureType) {
            return (SimpleFeatureType) schema;
        } else {
            return null;
        }
    }

    @Override
    public SimpleFeatureSource getFeatureSource(String typeName) throws IOException {
        // simple case, no lookups needed
        if (delegate instanceof DataStore) {
            return ((DataStore) delegate).getFeatureSource(typeName);
        }
        Name name = getNameFromLocal(typeName);
        return DataUtilities.simple(delegate.getFeatureSource(name));
    }

    /** Returns a qualified name from an unqualified type name. Ensures a single value is found. */
    private Name getNameFromLocal(String typeName) throws IOException {
        Name result = NAME_CACHE.get(typeName);
        if (result == null) {
            Stream<Name> stream = delegate.getNames().stream();
            Set<Name> names =
                    stream.filter(name -> typeName.equals(name.getLocalPart()))
                            .collect(Collectors.toSet());
            if (names.isEmpty()) {
                throw new IOException("Could not find a type name '" + typeName + "'");
            } else if (names.size() > 1) {
                throw new IOException("Found multiple matches for '" + typeName + "': " + names);
            } else {
                result = names.iterator().next();
                NAME_CACHE.put(typeName, result);
            }
        }

        return result;
    }

    @Override
    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(
            Query query, Transaction transaction) throws IOException {
        SimpleFeatureSource fs = getFeatureSource(query.getTypeName());
        if (fs == null) {
            throw new IOException(
                    "Could not find feature type mentioned in query: '"
                            + query.getTypeName()
                            + "'");
        }
        if (fs instanceof SimpleFeatureStore) {
            ((SimpleFeatureStore) fs).setTransaction(transaction);
        }
        return new DelegateFeatureReader<>(fs.getSchema(), fs.getFeatures().features());
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
            String typeName, Filter filter, Transaction transaction) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
            String typeName, Transaction transaction) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(
            String typeName, Transaction transaction) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public LockingManager getLockingManager() {
        return null;
    }

    public boolean wraps(DataAccess access) {
        return delegate == access;
    }
}
