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
package org.geotools.data.memory;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.geotools.data.DataSourceException;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.SchemaNotFoundException;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

public class MemoryFeatureSource extends ContentFeatureSource {
    String typeName;
    SimpleFeatureType featureType;
    //MemoryDataStore store;
    
    public MemoryFeatureSource(ContentEntry entry, Query query) {
        super(entry, query);
        this.typeName = entry.getTypeName();
        this.featureType = getDataStore().schema.get(typeName);
        
    }
    
    /**
     * Access parent MemoryDataStore.
     */
    public MemoryDataStore getDataStore() {
        return (MemoryDataStore) super.getDataStore();
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        if (query.getFilter() == Filter.INCLUDE) { //filtering not implemented
            ReferencedEnvelope bounds = ReferencedEnvelope.create( 
                    getSchema().getCoordinateReferenceSystem() ); 
            FeatureReader<SimpleFeatureType, SimpleFeature> featureReader = getReaderInternal(query);
            try {
                while (featureReader.hasNext()) {
                    SimpleFeature feature = featureReader.next();
                    bounds.include(feature.getBounds());
                }
            } finally {
                featureReader.close();
            }
            return bounds;
        }
        return null; // feature by feature scan required to count records
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        if (query.getFilter() == Filter.INCLUDE) {
            return getDataStore().features(typeName).size();
        }
        //feature by feature count required
        return -1;
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(
            Query query) throws IOException {
        return new MemoryFeatureReader(getState(), query);
    }

    @Override
    protected SimpleFeatureType buildFeatureType() {
        return featureType;
    }
    
    @Override
    protected boolean handleVisitor(Query query, FeatureVisitor visitor) throws IOException {
        return super.handleVisitor(query, visitor);
    }

}
