/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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

import java.util.LinkedHashMap;
import java.util.Map;

import org.geotools.data.store.ContentEntry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Entry used to store features (of a single FeatureType).
 * <p>
 * <p>
 * Please be sure to synchronize on entry before access:
 * <pre><code> synchronize ( entry ){
 *     entry.memory.put( feature.getID(), feature );
 * }</code></pre>
 * 
 * @author Jody Garnett (Boundless)
 */
public class MemoryEntry extends ContentEntry {
    
    /**
     * Schema of managed content.
     */
    final SimpleFeatureType schema;

    /**
     * Memory storage for features (addressed by fid).
     * <p>
     * Please be sure to synchronize on entry before access:
     * <pre><code> synchronize ( entry ){
     *     entry.memory.put( feature.getID(), feature );
     * }</code></pre>
     */
    Map<String, SimpleFeature> memory;

    /**
     * Entry to store content of the provided SimpleFeatureType.
     * 
     * @param store
     * @param schema
     */
    MemoryEntry( MemoryDataStore store, SimpleFeatureType schema){
        super( store, schema.getName() );
        this.schema = schema;
        memory = new LinkedHashMap<String, SimpleFeature>();
    }

    protected MemoryState createContentState(ContentEntry entry) {
        return new MemoryState( (MemoryEntry) entry );
    }
    
    public String toString() {
        return "MemoryEntry '" + getTypeName()+"': "+memory.size() + " features";
    }
}
