/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.directory;

import java.io.IOException;
import java.util.List;
import org.geotools.api.data.FeatureListener;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.SimpleFeatureStore;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.feature.FeatureCollection;

public class DirectoryFeatureStore extends DirectoryFeatureSource implements SimpleFeatureStore {

    SimpleFeatureStore fstore;

    public DirectoryFeatureStore(SimpleFeatureStore store) {
        super(store);
        this.fstore = store;
    }

    @Override
    public Transaction getTransaction() {
        return fstore.getTransaction();
    }

    @Override
    public void modifyFeatures(Name attributeName, Object attributeValue, Filter filter)
            throws IOException {
        fstore.modifyFeatures(attributeName, attributeValue, filter);
    }

    @Override
    public void modifyFeatures(Name[] name, Object[] value, Filter filter) throws IOException {
        fstore.modifyFeatures(name, value, filter);
    }

    @Override
    public void modifyFeatures(String name, Object value, Filter filter) throws IOException {
        fstore.modifyFeatures(name, value, filter);
    }

    @Override
    public void modifyFeatures(String[] names, Object[] values, Filter filter) throws IOException {
        fstore.modifyFeatures(names, values, filter);
    }

    @Override
    public void removeFeatureListener(FeatureListener listener) {
        fstore.removeFeatureListener(listener);
    }

    @Override
    public void removeFeatures(Filter filter) throws IOException {
        fstore.removeFeatures(filter);
    }

    @Override
    public void setFeatures(FeatureReader<SimpleFeatureType, SimpleFeature> reader)
            throws IOException {
        fstore.setFeatures(reader);
    }

    @Override
    public void setTransaction(Transaction transaction) {
        fstore.setTransaction(transaction);
    }

    @Override
    public List<FeatureId> addFeatures(
            FeatureCollection<SimpleFeatureType, SimpleFeature> collection) throws IOException {
        return fstore.addFeatures(collection);
    }

    @Override
    public SimpleFeatureStore unwrap() {
        return fstore;
    }
}
