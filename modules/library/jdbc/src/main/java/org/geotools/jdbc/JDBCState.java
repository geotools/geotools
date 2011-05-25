/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentState;


/**
 * State for jdbc datastore providing additional cached values such as primary
 * key and database connection.
 *
 * @author Jody Garnett, Refractions Research Inc.
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public final class JDBCState extends ContentState {
    /**
     * cached primary key
     */
    private PrimaryKey primaryKey;
    
    /**
     * flag indicating wether columns which are part of the primary key 
     * are exposed.
     */
    private boolean exposePrimaryKeyColumns;
    
    /**
     * Creates the state from an existing one.
     */
    public JDBCState(JDBCState state) {
        super(state);

        //copy the primary key
        primaryKey = state.getPrimaryKey();
        exposePrimaryKeyColumns = state.isExposePrimaryKeyColumns();
    }

    /**
     * Creates a new state object.
     */
    public JDBCState(ContentEntry entry) {
        super(entry);
    }

    /**
     * The cached primary key.
     */
    public PrimaryKey getPrimaryKey() {
        return primaryKey;
    }

    /**
     * Sets the cached primary key.
     * @param primaryKey
     */
    public void setPrimaryKey(PrimaryKey primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * Returns the flag indicating if columns which compose the primary key are exposed 
     * via the feature type.
     */
    public boolean isExposePrimaryKeyColumns() {
        return exposePrimaryKeyColumns;
    }
    
    /**
     * Sets the flag indicating if columns which compose the primary key are exposed 
     * via the feature type.
     */
    public void setExposePrimaryKeyColumns(boolean exposePrimaryKeyColumns) {
        if ( exposePrimaryKeyColumns != this.exposePrimaryKeyColumns ) {
            //need to clear the feature type cache, as it will need to be rebuilt
            featureType = null;
        }
        this.exposePrimaryKeyColumns = exposePrimaryKeyColumns;
    }
    
    /**
     * Flushes all cached state.
     */
    public void flush() {
        primaryKey = null;
        super.flush();
    }

    /**
     * Copies the state.
     */
    public ContentState copy() {
        return new JDBCState(this);
    }

}
