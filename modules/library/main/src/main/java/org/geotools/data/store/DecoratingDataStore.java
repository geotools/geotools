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
package org.geotools.data.store;

import java.io.IOException;
import java.util.List;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.LockingManager;
import org.geotools.data.Query;
import org.geotools.data.ServiceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.util.decorate.AbstractDecorator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

/**
 * Delegates every method to the wrapped feature source. Subclasses will override selected methods
 * to perform their "decoration" job
 *
 * @author Andrea Aime
 */
public abstract class DecoratingDataStore extends AbstractDecorator<DataStore>
        implements DataStore {

    public DecoratingDataStore(DataStore delegate) {
        super(delegate);
    }

    public void createSchema(SimpleFeatureType featureType) throws IOException {
        delegate.createSchema(featureType);
    }

    public void dispose() {
        delegate.dispose();
    }

    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(
            Query query, Transaction transaction) throws IOException {
        return delegate.getFeatureReader(query, transaction);
    }

    public SimpleFeatureSource getFeatureSource(Name typeName) throws IOException {
        return delegate.getFeatureSource(typeName);
    }

    public SimpleFeatureSource getFeatureSource(String typeName) throws IOException {
        return delegate.getFeatureSource(typeName);
    }

    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
            String typeName, Filter filter, Transaction transaction) throws IOException {
        return delegate.getFeatureWriter(typeName, filter, transaction);
    }

    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
            String typeName, Transaction transaction) throws IOException {
        return delegate.getFeatureWriter(typeName, transaction);
    }

    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(
            String typeName, Transaction transaction) throws IOException {
        return delegate.getFeatureWriterAppend(typeName, transaction);
    }

    public ServiceInfo getInfo() {
        return delegate.getInfo();
    }

    public LockingManager getLockingManager() {
        return delegate.getLockingManager();
    }

    public List<Name> getNames() throws IOException {
        return delegate.getNames();
    }

    public SimpleFeatureType getSchema(Name name) throws IOException {
        return delegate.getSchema(name);
    }

    public SimpleFeatureType getSchema(String typeName) throws IOException {
        return delegate.getSchema(typeName);
    }

    public String[] getTypeNames() throws IOException {
        return delegate.getTypeNames();
    }

    public void updateSchema(Name typeName, SimpleFeatureType featureType) throws IOException {
        delegate.updateSchema(typeName, featureType);
    }

    public void updateSchema(String typeName, SimpleFeatureType featureType) throws IOException {
        delegate.updateSchema(typeName, featureType);
    }

    public void removeSchema(Name typeName) throws IOException {
        delegate.removeSchema(typeName);
    }

    public void removeSchema(String typeName) throws IOException {
        delegate.removeSchema(typeName);
    }
}
