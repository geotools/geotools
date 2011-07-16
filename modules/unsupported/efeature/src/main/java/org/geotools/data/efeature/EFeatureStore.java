/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.efeature;

import java.io.IOException;

import org.geotools.data.Query;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureStore;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * @author kengu - 10. juli 2011
 *
 */
@SuppressWarnings("unchecked")
public class EFeatureStore extends ContentFeatureStore {

    /**
     * Cached {@link EFeatureInfo} instance
     */
    protected final EFeatureInfo eStructure;
    
    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------

    /**
     * @param entry
     * @param query
     */
    public EFeatureStore(ContentEntry entry, Query query) {
        //
        // Forward to ContentFeatureSource
        //
        super(entry, query);
        //
        // Get structure instance
        //
        this.eStructure = getDataStore().eStructure().eGetFeatureInfo(entry.getTypeName());
        //
        // Cache schema
        // 
        this.schema = eStructure.getFeatureType();
    }
    
    // ----------------------------------------------------- 
    //  Overridden methods
    // -----------------------------------------------------

    /**
     * The {@link EFeatureDataStore} that this {@link EFeatureSource} originated from.
     */
    @Override
    public EFeatureDataStore getDataStore() {
        return (EFeatureDataStore)entry.getDataStore();
    }
    
    // ----------------------------------------------------- 
    //  ContentFeatureStore implementation
    // -----------------------------------------------------

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        return eStructure.featureType;
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {        
        return EFeatureSource.getBounds(getDataStore(), getSchema(), getReader(query));
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        return EFeatureSource.getCount(getDataStore(), getReader(query));
    }

    @Override
    protected EFeatureReader getReaderInternal(Query query) throws IOException {
        return new EFeatureReader(getDataStore(), query);
    }

    @Override
    protected EFeatureWriter getWriterInternal(Query query, int flags) throws IOException {
        return new EFeatureWriter(getDataStore(), query, getTransaction(), 0);
    }

}
