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
import org.geotools.data.DataAccess;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.LockingManager;
import org.geotools.data.Query;
import org.geotools.data.ServiceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.collection.DelegateFeatureReader;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.util.SoftValueHashMap;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

/**
 * A wrapper trying to expose a DataAccess providing, among others, simple feature instances.
 *
 * @author Andrea Aime - GeoSolutions
 */
class DataAccessStoreWrapper implements DataStore {

    DataAccess delegate;

    SoftValueHashMap<String, Name> NAME_CACHE = new SoftValueHashMap<>();

    public DataAccessStoreWrapper(DataAccess<FeatureType, Feature> delegate) {
        this.delegate = delegate;
    }

    public ServiceInfo getInfo() {
        return delegate.getInfo();
    }

    public void createSchema(SimpleFeatureType featureType) throws IOException {
        delegate.createSchema(featureType);
    }

    public void updateSchema(Name typeName, SimpleFeatureType featureType) throws IOException {
        delegate.updateSchema(typeName, featureType);
    }

    public void removeSchema(Name typeName) throws IOException {
        delegate.removeSchema(typeName);
    }

    public List getNames() throws IOException {
        return delegate.getNames();
    }

    public SimpleFeatureType getSchema(Name name) throws IOException {
        FeatureType schema = delegate.getSchema(name);
        if (schema instanceof SimpleFeatureType) {
            return (SimpleFeatureType) schema;
        } else {
            return null;
        }
    }

    public SimpleFeatureSource getFeatureSource(Name typeName) throws IOException {
        return DataUtilities.simple(delegate.getFeatureSource(typeName));
    }

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
        return new DelegateFeatureReader<SimpleFeatureType, SimpleFeature>(
                fs.getSchema(), fs.getFeatures().features());
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
