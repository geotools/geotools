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
package org.geotools.data.postgis;

import java.io.IOException;
import java.util.List;

import org.geotools.data.FeatureReader;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.identity.FeatureId;

/**
 * Really delegates everything to a wrapped feature store, but allows to
 * advertise a data store other than the original one
 * 
 * @author aaime
 * @since 2.4
 * 
 */
class WrappingPostgisFeatureStore extends WrappingPostgisFeatureSource
        implements SimpleFeatureStore {

    private SimpleFeatureStore wrappedStore;

    public WrappingPostgisFeatureStore(SimpleFeatureStore wrapped,
            VersionedPostgisDataStore store) {
        super(wrapped, store);
        this.wrappedStore = wrapped;
    }

    public List<FeatureId> addFeatures(FeatureCollection<SimpleFeatureType,SimpleFeature> collection) throws IOException {
        return wrappedStore.addFeatures(collection);
    }

    public Transaction getTransaction() {
        return wrappedStore.getTransaction();
    }

    public void modifyFeatures(Name attributeName, Object attributeValue, Filter filter)
            throws IOException {
        wrappedStore.modifyFeatures(attributeName, attributeValue, filter);        
    }
    public void modifyFeatures(Name[] attributeNames, Object[] attributeValues, Filter filter)
            throws IOException {
        wrappedStore.modifyFeatures(attributeNames, attributeValues, filter);        
    }
    
    public void modifyFeatures(String name, Object attributeValue, Filter filter)
            throws IOException {
        wrappedStore.modifyFeatures(name, attributeValue, filter);
    }

    public void modifyFeatures(String[] names, Object[] attributeValues, Filter filter)
            throws IOException {
        wrappedStore.modifyFeatures(names, attributeValues, filter);
    }

    public void modifyFeatures(AttributeDescriptor type, Object value, Filter filter)
            throws IOException {
        wrappedStore.modifyFeatures(type, value, filter);
    }

    public void modifyFeatures(AttributeDescriptor[] type, Object[] value,
            Filter filter) throws IOException {
        wrappedStore.modifyFeatures(type, value, filter);
    }

    public void removeFeatures(Filter filter) throws IOException {
        wrappedStore.removeFeatures(filter);
    }

    public void setFeatures(FeatureReader <SimpleFeatureType, SimpleFeature> reader) throws IOException {
        wrappedStore.setFeatures(reader);
    }

    public void setTransaction(Transaction transaction) {
        wrappedStore.setTransaction(transaction);
    }

}
