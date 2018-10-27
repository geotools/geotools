/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

/**
 * Available via {@link DataUtilities#dataStore(SimpleFeatureSource)} methods.
 *
 * <p>This implementation is a wrapper around a provided {@link SimpleFeatureSource}.
 *
 * @author Jody Garnett (Boundless)
 */
final class DataStoreAdaptor implements DataStore {
    private Name name;

    private SimpleFeatureSource source;

    private SimpleFeatureType schema;

    private String typeName;

    public DataStoreAdaptor(SimpleFeatureSource source) {
        this.source = source;
        this.schema = source.getSchema();
        this.name = schema.getName();
        this.typeName = name.getLocalPart();
    }

    @Override
    public void updateSchema(Name name, SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException("DataStoreAdaptor does not support modification");
    }

    @Override
    public void removeSchema(Name name) throws IOException {
        throw new UnsupportedOperationException("DataStoreAdaptor does not support modification");
    }

    @Override
    public SimpleFeatureType getSchema(Name name) throws IOException {
        if (this.name.equals(name)) {
            return schema;
        }
        throw new IOException(
                "Not found: " + name + " DataStoreAdaptor datastore contains " + this.name);
    }

    @Override
    public List<Name> getNames() throws IOException {
        return Collections.singletonList(this.name);
    }

    @Override
    public ServiceInfo getInfo() {
        DefaultServiceInfo info = new DefaultServiceInfo();
        info.setDescription("DataStoreAdaptor " + this.name);
        info.setTitle(this.name.getLocalPart());
        return info;
    }

    private void ensureNotDisposed() throws IOException {
        if (this.source == null) {
            throw new IOException("DataStoreAdaptor is not available as it has been disposed");
        }
    }

    @Override
    public void dispose() {
        this.source = null;
        this.schema = null;
        this.name = null;
    }

    @Override
    public void createSchema(SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException("DataStoreAdaptor does not support modification");
    }

    @Override
    public void updateSchema(String typeName, SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException("DataStoreAdaptor does not support modification");
    }

    @Override
    public void removeSchema(String typeName) throws IOException {
        throw new UnsupportedOperationException("DataStoreAdaptor does not support modification");
    }

    @Override
    public String[] getTypeNames() throws IOException {
        ensureNotDisposed();
        return new String[] {typeName};
    }

    @Override
    public SimpleFeatureType getSchema(String typeName) throws IOException {
        ensureNotDisposed();
        if (this.typeName.equals(typeName)) {
            return schema;
        }
        throw new IOException(
                "Not found: " + typeName + " DataStoreAdaptor datastore contains " + this.typeName);
    }

    @Override
    public LockingManager getLockingManager() {
        return new LockingManager() {

            @Override
            public void unLockFeatureID(
                    String typeName,
                    String authID,
                    Transaction transaction,
                    FeatureLock featureLock)
                    throws IOException {}

            @Override
            public boolean release(String authID, Transaction transaction) throws IOException {
                return false;
            }

            @Override
            public boolean refresh(String authID, Transaction transaction) throws IOException {
                return false;
            }

            @Override
            public void lockFeatureID(
                    String typeName,
                    String authID,
                    Transaction transaction,
                    FeatureLock featureLock)
                    throws IOException {}

            @Override
            public boolean exists(String authID) {
                return false;
            }
        };
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(
            String typeName, Transaction transaction) throws IOException {
        throw new UnsupportedOperationException("DataStoreAdaptor does not support modification");
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
            String typeName, Transaction transaction) throws IOException {
        throw new UnsupportedOperationException("DataStoreAdaptor does not support modification");
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
            String typeName, Filter filter, Transaction transaction) throws IOException {
        throw new UnsupportedOperationException("DataStoreAdaptor does not support modification");
    }

    @Override
    public SimpleFeatureSource getFeatureSource(Name name) throws IOException {
        ensureNotDisposed();
        if (this.name.equals(name)) {
            return source;
        }
        throw new IOException("Not found: " + name + "DataStoreAdaptor contains " + this.typeName);
    }

    @Override
    public SimpleFeatureSource getFeatureSource(String typeName) throws IOException {
        ensureNotDisposed();
        if (this.typeName.equals(typeName)) {
            return source;
        }
        throw new IOException(
                "Not found: " + typeName + " DataStoreAdaptor contains " + this.typeName);
    }

    @Override
    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(
            Query query, Transaction transaction) throws IOException {
        ensureNotDisposed();
        if (this.typeName.equals(query.getTypeName())) {
            return DataUtilities.reader(source.getFeatures());
        }
        throw new IOException(
                "Not found: "
                        + query.getTypeName()
                        + " DataStoreAdaptor contains "
                        + this.typeName);
    }
}
