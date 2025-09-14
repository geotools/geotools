/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.List;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.FeatureStore;
import org.geotools.api.data.SimpleFeatureStore;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.NameImpl;

/** Bridges between {@link FeatureStore<SimpleFeatureType, SimpleFeature>} and {@link SimpleFeatureStore} */
class SimpleFeatureStoreBridge extends SimpleFeatureSourceBridge implements SimpleFeatureStore {

    public SimpleFeatureStoreBridge(FeatureStore<SimpleFeatureType, SimpleFeature> delegate) {
        super(delegate);
    }

    private FeatureStore<SimpleFeatureType, SimpleFeature> delegate() {
        return (FeatureStore<SimpleFeatureType, SimpleFeature>) delegate;
    }

    @Override
    public List<FeatureId> addFeatures(FeatureCollection<SimpleFeatureType, SimpleFeature> collection)
            throws IOException {
        return delegate().addFeatures(collection);
    }

    @Override
    public Transaction getTransaction() {
        return delegate().getTransaction();
    }

    @Override
    public void modifyFeatures(Name[] names, Object[] values, Filter filter) throws IOException {
        delegate().modifyFeatures(names, values, filter);
    }

    @Override
    public void modifyFeatures(Name name, Object value, Filter filter) throws IOException {
        delegate().modifyFeatures(name, value, filter);
    }

    @Override
    public void modifyFeatures(String name, Object attributeValue, Filter filter) throws IOException {
        if (delegate instanceof SimpleFeatureStore store) {
            store.modifyFeatures(name, attributeValue, filter);
        } else {
            modifyFeatures(
                    new Name[] {
                        new NameImpl(name),
                    },
                    new Object[] {
                        attributeValue,
                    },
                    filter);
        }
    }

    @Override
    public void modifyFeatures(String[] names, Object[] values, Filter filter) throws IOException {
        if (delegate instanceof SimpleFeatureStore store) {
            store.modifyFeatures(names, values, filter);
        } else {
            Name[] attributeNames = new Name[names.length];
            for (int i = 0; i < names.length; i++) {
                attributeNames[i] = new NameImpl(names[i]);
            }
            modifyFeatures(attributeNames, values, filter);
        }
    }

    @Override
    public void removeFeatures(Filter filter) throws IOException {
        delegate().removeFeatures(filter);
    }

    @Override
    public void setFeatures(FeatureReader<SimpleFeatureType, SimpleFeature> reader) throws IOException {
        delegate().setFeatures(reader);
    }

    @Override
    public void setTransaction(Transaction transaction) {
        delegate().setTransaction(transaction);
    }
}
