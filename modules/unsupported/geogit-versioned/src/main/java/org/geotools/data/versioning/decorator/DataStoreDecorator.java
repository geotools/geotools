/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.versioning.decorator;

import java.io.IOException;

import org.geogit.repository.Repository;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.LockingManager;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.versioning.VersioningDataStore;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

public class DataStoreDecorator extends
        DataAccessDecorator<SimpleFeatureType, SimpleFeature> implements
        VersioningDataStore {

    public DataStoreDecorator(DataStore unversioned, Repository versioningRepo) {
        super(unversioned, versioningRepo);
    }

    @Override
    public SimpleFeatureSource getFeatureSource(Name typeName)
            throws IOException {
        return (SimpleFeatureSource) VersioningAdapterFactory.create(
                unversioned.getFeatureSource(typeName), repository);
    }

    @Override
    public void updateSchema(String typeName, SimpleFeatureType featureType)
            throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] getTypeNames() throws IOException {
        return ((DataStore) unversioned).getTypeNames();
    }

    @Override
    public SimpleFeatureType getSchema(String typeName) throws IOException {
        return ((DataStore) unversioned).getSchema(typeName);
    }

    @Override
    public SimpleFeatureSource getFeatureSource(String typeName)
            throws IOException {
        return (SimpleFeatureSource) VersioningAdapterFactory.create(
                ((DataStore) unversioned).getFeatureSource(typeName),
                repository);
    }

    @Override
    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(
            Query query, Transaction transaction) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
            String typeName, Filter filter, Transaction transaction)
            throws IOException {
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

}
