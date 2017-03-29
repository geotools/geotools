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
import java.util.Map;
import java.util.NoSuchElementException;

import org.geotools.data.DataSourceException;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.store.ContentState;
import org.geotools.factory.Hints;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.identity.FeatureId;

/**
 * Update contents of MemoryDataStore. 
 */
public class MemoryFeatureWriter implements FeatureWriter<SimpleFeatureType, SimpleFeature>{
    ContentState state;
    SimpleFeatureType featureType;
    Map<String,SimpleFeature> contents;
    Iterator<SimpleFeature> iterator;
    
    SimpleFeature live = null;
    
    SimpleFeature current = null; // current Feature returned to user        
    
    public MemoryFeatureWriter(ContentState state, Query query) throws IOException {
        this.state = state;
        featureType = state.getFeatureType();
        String typeName = featureType.getTypeName();
        MemoryDataStore store = (MemoryDataStore) state.getEntry().getDataStore();
        contents = store.features(typeName);
        iterator = contents.values().iterator();
    }
    
    public SimpleFeatureType getFeatureType() {
        return featureType;
    }
    
    public SimpleFeature next() throws IOException, NoSuchElementException {
        if (hasNext()) {
            // existing content
            live = iterator.next();

            try {
                current = SimpleFeatureBuilder.copy(live);
            } catch (IllegalAttributeException e) {
                throw new DataSourceException("Unable to edit " + live.getID() + " of "
                    + featureType.getTypeName());
            }
        } else {
            // new content
            live = null;

            try {
                current = SimpleFeatureBuilder.template(featureType, null);
            } catch (IllegalAttributeException e) {
                throw new DataSourceException("Unable to add additional Features of "
                    + featureType.getTypeName());
            }
        }
        
        return current;
    }

    
    public void remove() throws IOException {
        if (contents == null) {
            throw new IOException("FeatureWriter has been closed");
        }

        if (current == null) {
            throw new IOException("No feature available to remove");
        }

        if (live != null) {
            // remove existing content
            iterator.remove();
            live = null;
            current = null;
        } else {
            // cancel add new content
            current = null;
        }
    }
    
    public void write() throws IOException {
        if (contents == null) {
            throw new IOException("FeatureWriter has been closed");
        }
    
        if (current == null) {
            throw new IOException("No feature available to write");
        }
        //[GEOT-5683] preserve FeatureIDd during add feature
        if (Boolean.TRUE.equals(current.getUserData().get(Hints.USE_PROVIDED_FID))) {
            if (current.getUserData().containsKey(Hints.PROVIDED_FID)) {
                String fid = (String) current.getUserData().get(Hints.PROVIDED_FID);
                FeatureId id = new FeatureIdImpl(fid);
                current = new SimpleFeatureImpl(current.getAttributes(), current.getFeatureType(), id);
            }
        }

        if (live != null) {
            if (live.equals(current)) {
                // no modifications made to current
                //
                live = null;
                current = null;
            } else {
                // accept modifications
                //
                try {
                    live.setAttributes(current.getAttributes());
                } catch (Exception e) {
                    throw new DataSourceException("Unable to accept modifications to "
                        + live.getID() + " on " + featureType.getTypeName());
                }
    
                ReferencedEnvelope bounds = new ReferencedEnvelope();
                bounds.expandToInclude(new ReferencedEnvelope(live.getBounds()));
                bounds.expandToInclude(new ReferencedEnvelope(current.getBounds()));
                live = null;
                current = null;
            }
        } else {
            // add new content
            contents.put(current.getID(), current);
            current = null;
        }
    }
    
    public boolean hasNext() throws IOException {
        if (contents == null) {
            throw new IOException("FeatureWriter has been closed");
        }
        
        return (iterator != null) && iterator.hasNext();
    }
    
    public void close(){
        if (iterator != null) {
            iterator = null;
        }
        
        if (featureType != null) {
            featureType = null;
        }
        
        contents = null;
        current = null;
        live = null;
    }
}
