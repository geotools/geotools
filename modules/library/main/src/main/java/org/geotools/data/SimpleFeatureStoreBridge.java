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

import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.NameImpl;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.identity.FeatureId;

/**
 * Bridges between {@link FeatureStore<SimpleFeatureType, SimpleFeature>} and {@link SimpleFeatureStore}
 */
class SimpleFeatureStoreBridge extends SimpleFeatureSourceBridge implements
        SimpleFeatureStore {
    
    public SimpleFeatureStoreBridge(FeatureStore<SimpleFeatureType, SimpleFeature> delegate) {
        super(delegate);
    }

    private FeatureStore<SimpleFeatureType, SimpleFeature> delegate(){
        return (FeatureStore<SimpleFeatureType, SimpleFeature>) delegate;
    }
    
    public List<FeatureId> addFeatures(
            FeatureCollection<SimpleFeatureType, SimpleFeature> collection) throws IOException {
        return delegate().addFeatures(collection);
    }

    public Transaction getTransaction() {
        return delegate().getTransaction();
    }

    public void modifyFeatures(AttributeDescriptor[] type, Object[] value, Filter filter)
            throws IOException {
        delegate().modifyFeatures(type, value,
                filter);
    }

    public void modifyFeatures(AttributeDescriptor type, Object value, Filter filter)
            throws IOException {
        delegate().modifyFeatures(type, value,
                filter);
    }

    public void modifyFeatures(Name[] names, Object[] values, Filter filter)
            throws IOException {
        delegate().modifyFeatures(names, values,
                filter);
    }

    public void modifyFeatures(Name name, Object value, Filter filter)
            throws IOException {
        delegate().modifyFeatures(name, value,
                filter);
    }
    public void modifyFeatures(String name, Object attributeValue, Filter filter)
            throws IOException {
        if( delegate instanceof SimpleFeatureStore){
            ((SimpleFeatureStore)delegate).modifyFeatures(name, attributeValue, filter);
        }
        else {
            modifyFeatures(new Name[] { new NameImpl(name), }, new Object[] { attributeValue, }, filter);
        }
    }

    public void modifyFeatures(String[] names, Object[] values, Filter filter) throws IOException {
        if( delegate instanceof SimpleFeatureStore){
            ((SimpleFeatureStore)delegate).modifyFeatures(names, values, filter);
        }
        else {
            Name attributeNames[] = new Name[names.length];
            for (int i = 0; i < names.length; i++) {
                attributeNames[i] = new NameImpl(names[i]);
            }
            modifyFeatures(attributeNames, values, filter);
        }
    }
    
    public void removeFeatures(Filter filter) throws IOException {
        delegate().removeFeatures(filter);
    }

    public void setFeatures(FeatureReader<SimpleFeatureType, SimpleFeature> reader)
            throws IOException {
        delegate().setFeatures(reader);
    }

    public void setTransaction(Transaction transaction) {
        delegate().setTransaction(transaction);
    }

}
