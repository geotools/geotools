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
package org.geotools.data.property;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import org.geotools.data.AbstractFeatureLocking;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureEvent;
import org.geotools.data.FeatureListener;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.Transaction;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

public class PropertyFeatureSource extends AbstractFeatureLocking {


    String typeName;
    SimpleFeatureType featureType;
    PropertyDataStore store;
    
    long cacheTimestamp = 0;
    ReferencedEnvelope cacheBounds = null;
    int cacheCount = -1;
    
    PropertyFeatureSource( PropertyDataStore propertyDataStore, String typeName ) throws IOException{
        this.store = propertyDataStore;
        this.typeName = typeName;
        this.featureType = store.getSchema( typeName );
        store.listenerManager.addFeatureListener( this, new FeatureListener(){
            public void changed(FeatureEvent featureEvent) {
                if( cacheBounds != null ){
                    if( featureEvent.getEventType() == FeatureEvent.FEATURES_ADDED ){
                        cacheBounds.expandToInclude( featureEvent.getBounds() );
                    }
                    else {
                        cacheBounds = null;                                            
                    }                
                }
                cacheCount = -1;
            }
        }); 
        this.queryCapabilities = new QueryCapabilities() {
            @Override
            public boolean isUseProvidedFIDSupported() {
                return true;
            }
        };
    }
    
    public DataStore getDataStore() {
        return store;
    }

    public void addFeatureListener(FeatureListener listener) {
        store.listenerManager.addFeatureListener(this, listener);
    }

    public void removeFeatureListener(
        FeatureListener listener) {
        store.listenerManager.removeFeatureListener(this, listener);
    }

    public SimpleFeatureType getSchema() {
        return featureType;
    }
    
    public int getCount(Query query) {
        if( Filter.INCLUDE == query.getFilter() && getTransaction() == Transaction.AUTO_COMMIT ){
            File file = new File( store.directory, typeName+".properties" );            
            if( cacheCount != -1 && file.lastModified() == cacheTimestamp){
                return cacheCount;
            }
            cacheCount = countFile( file );
            cacheTimestamp = file.lastModified();
            return cacheCount;
        }
        return -1;
    }
    private int countFile(File file){
        LineNumberReader reader = null;
        try {
            reader = new LineNumberReader( new FileReader( file ) );
            while( reader.readLine() != null);                    
            return reader.getLineNumber() -1;   
        }
        catch( IOException e){
            return -1;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // we tried
                }
            }
        }
    }
    public ReferencedEnvelope getBounds() {
        File file = new File( store.directory, typeName+".properties" );                
        if( cacheBounds != null && file.lastModified() == cacheTimestamp ){            
            // we have the cache
            return cacheBounds;
        }
        try {
            // calculate and store in cache                    
            cacheBounds = getFeatures().getBounds();
            cacheTimestamp = file.lastModified();            
            return cacheBounds;
        } catch (IOException e) {            
        }
        // bounds are unavailable!
        return null;
    }

}
