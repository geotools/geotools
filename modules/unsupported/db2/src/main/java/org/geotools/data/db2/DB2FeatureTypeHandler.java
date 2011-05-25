/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    (C) Copyright IBM Corporation, 2005. All rights reserved.
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
 *
 */
package org.geotools.data.db2;

import java.io.IOException;
import java.util.logging.Logger;

import org.geotools.data.jdbc.FeatureTypeHandler;
import org.geotools.data.jdbc.JDBCDataStore;
import org.geotools.data.jdbc.fidmapper.FIDMapperFactory;

/**
 * Override methods from FeatureTypeHandler for DB2-specific handling.
 *
 * @author David Adler - IBM Corporation
 *
 * @source $URL$
 */
public class DB2FeatureTypeHandler extends FeatureTypeHandler {
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.data.db2");

    // It is really nasty that we make a second copy of the DataStore in the subclass because
    // it is private in FeatureTypeHandler and not visible to subclasses.	
    DB2DataStore db2Store;

    /**
     * Creates a new feature type handler
     *
     * @param store the parent data store
     * @param fmFactory the FIDMapper factory
     * @param cacheTimeOut timeout used to purge possibly stale data from the
     *        caches
     */
    public DB2FeatureTypeHandler(JDBCDataStore store,
        FIDMapperFactory fmFactory, long cacheTimeOut) {
        super(store, fmFactory, cacheTimeOut);
        this.db2Store = (DB2DataStore) store;
    }

    /**
     * Gets the list of type names from the DB2SpatialCatalog which should have
     * been loaded with all geometry columns (includes type names) when the
     * DB2DataStore was created.
     * 
     * <p>
     * Note: there seems to be some ambiguity as one could have the same type
     * name (table name) in the database with different table schemas.
     * </p>
     * 
     * <p>
     * It isn't clear how this relates to DataStore.getTypeNames
     * </p>
     *
     * @return an array of type (table) names.
     *
     * @throws IOException
     */
    private String[] loadTypeNamesFromDatabase() throws IOException {
        return this.db2Store.getTypeNames();
    }
}
