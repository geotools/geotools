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
package org.geotools.data.property.ng;

import java.io.IOException;

import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureStore;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Implementation used for writeable property files.
 * Supports limited caching of number of features and bounds.
 *
 *
 * @source $URL$
 */
public class PropertyFeatureStore extends ContentFeatureStore {

    String typeName;
    SimpleFeatureType featureType;
    PropertyDataStore store;
    PropertyFeatureSource delegate;
    
    PropertyFeatureStore( ContentEntry entry, Query query ) throws IOException{
        super( entry, query );
        delegate = new PropertyFeatureSource(entry,  query );
        this.store = (PropertyDataStore) entry.getDataStore();
        this.typeName = entry.getTypeName();
    }
    
    protected QueryCapabilities buildQueryCapabilities() {
        return new QueryCapabilities(){
            public boolean isUseProvidedFIDSupported() {
                return true;
            }
        };
    }

    
    public PropertyDataStore getDataStore() {
        return (PropertyDataStore) super.getDataStore();
    }

    @Override
    protected FeatureWriter<SimpleFeatureType, SimpleFeature> getWriterInternal(Query query,
            int flags) throws IOException {
        return new PropertyFeatureWriter(this,getState(), query, (flags | WRITER_ADD) == WRITER_ADD);
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        return delegate.getBoundsInternal(query);
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        return delegate.getCountInternal(query);
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {
        return delegate.getReaderInternal(query);
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        return delegate.buildFeatureType();
    }

}
