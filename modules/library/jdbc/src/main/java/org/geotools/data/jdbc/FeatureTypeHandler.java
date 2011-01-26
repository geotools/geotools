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
package org.geotools.data.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geotools.data.DataSourceException;
import org.geotools.data.Transaction;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.data.jdbc.fidmapper.FIDMapperFactory;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;


/**
 * This class acts as a manager for FIDMappers and FeatureTypeInfo on behalf of a JDBCDataStore.<br>
 * At the same time, it acts as a FeatureTypeInfo cache, with a user selectable timeout. Set the
 * timeout to 0 if you want each request to be in-line with the actual database state, to
 * Long.MAX_VALUE in order to make it work basically in-memory, or to an intermediate value to
 * make requests cached allowing at the same time to keep it in-line with a  changing database.
 *
 * @author wolf
 * @source $URL$
 * 
 * @deprecated scheduled for removal in 2.7, use classes in org.geotools.jdbc
 */
public class FeatureTypeHandler {
    private FIDMapperFactory fmFactory;
    protected Map featureTypeMap = new HashMap();
    protected Map featureTypeTimeoutMap = new HashMap();
    JDBC1DataStore dataStore;
    Map typeMappers = new HashMap();
    long cacheTimeOut;
    long lastTypeNameRequestTime;

    /**
     * Creates a new feature type handler
     *
     * @param store the parent data store
     * @param fmFactory the FIDMapper factory
     * @param cacheTimeOut timeout used to purge possibly stale data from the caches
     */
    public FeatureTypeHandler(JDBC1DataStore store, FIDMapperFactory fmFactory, long cacheTimeOut) {
        this.dataStore = store;
        this.cacheTimeOut = cacheTimeOut;
        this.fmFactory = fmFactory;
    }

    /**
     * Returns a list of FeatureType names contained in the parent JDBCDataStore
     *
     *
     * @throws IOException
     */
    public String[] getTypeNames() throws IOException {
        long lastTime = lastTypeNameRequestTime;
        long now = System.currentTimeMillis();
        lastTypeNameRequestTime = now;

        if ((lastTime < (now - cacheTimeOut)) || (featureTypeMap.size() == 0)) {
            // type names cache timeout, better sync with the actual
            // list of type names while we're at it    
            String[] newTypeNames = loadTypeNamesFromDatabase();
            
            HashMap newFeatureTypeMap = new HashMap();

            // get the already parsed feature type info from the old map and put them
            // into the new one
            for (int i = 0; i < newTypeNames.length; i++) {
                String typeName = newTypeNames[i];
                FeatureTypeInfo info = (FeatureTypeInfo) featureTypeMap.get(typeName);
                newFeatureTypeMap.put(typeName, info);
            }

            featureTypeMap = newFeatureTypeMap;

            return newTypeNames;
        } else {
            // cache has not expired, thus we can freely return it
            Set keys = featureTypeMap.keySet();

            return (String[]) keys.toArray(new String[keys.size()]);
        }
    }

    /**
     * Clears the map between FeatureType name and FIDMappers
     */
    public void resetFIDMappers() {
        typeMappers.clear();
    }

    /**
     * Really loads the list of FeatureType names from the database
     *
     *
     * @throws IOException
     * @throws DataSourceException DOCUMENT ME!
     */
    private String[] loadTypeNamesFromDatabase() throws IOException {
        final int TABLE_NAME_COL = 3;
        Connection conn = null;
        List list = new ArrayList();

        try {
            conn = dataStore.getConnection(Transaction.AUTO_COMMIT);

            DatabaseMetaData meta = conn.getMetaData();
            String[] tableType = { "TABLE" , "VIEW"};
            ResultSet tables = meta.getTables(null, dataStore.config.getDatabaseSchemaName(), "%",
                    tableType);

            while (tables.next()) {
                String tableName = tables.getString(TABLE_NAME_COL);

                if (dataStore.allowTable(tableName)) {
                    list.add(tableName);
                }
            }

            return (String[]) list.toArray(new String[list.size()]);
        } catch (SQLException sqlException) {
            JDBCUtils.close(conn, Transaction.AUTO_COMMIT, sqlException);
            conn = null;

            String message = "Error querying database for list of tables:"
                + sqlException.getMessage();
            throw new DataSourceException(message, sqlException);
        } finally {
            JDBCUtils.close(conn, Transaction.AUTO_COMMIT, null);
        }
    }

    /**
     * Will reverse engineer and return the schema from the database.<br>
     * Performance warning: this request will always hit the database for unknown types
     *
     * @param typeName 
     * 
     *
     * @throws IOException 
     */
    public SimpleFeatureType getSchema(String typeName) throws IOException {
        FeatureTypeInfo info = getFeatureTypeInfo(typeName);

        return info.getSchema();
    }

    /**
     * Retreives the FeatureTypeInfo object for a SimpleFeatureType.
     * 
     * <p>
     * This allows subclasses to get access to the information about a feature type, this includes
     * the schema and the fidColumnName.
     * </p>
     *
     * @param featureTypeName The name of the feature type to get the info for.
     *
     * @return The FeatureTypeInfo object for the named feature type or null if the feature type
     *         does not exist.
     *
     * @throws IOException If an error occurs creating the FeatureTypeInfo.
     */
    public FeatureTypeInfo getFeatureTypeInfo(String featureTypeName)
        throws IOException {
        long now = System.currentTimeMillis();
        Long ftInfoTime = (Long) featureTypeTimeoutMap.get(featureTypeName);

        FeatureTypeInfo info = (FeatureTypeInfo) featureTypeMap.get(featureTypeName);

        // If the feature type info is not there or is outdated, load it from the
        // datastore and put it in the cache
        if ((ftInfoTime == null) || ((now - ftInfoTime.longValue()) > cacheTimeOut)) {
            // make sure table exists
            FIDMapper mapper = getFIDMapper(featureTypeName);
            SimpleFeatureType schema = dataStore.buildSchema(featureTypeName, mapper);
            info = new FeatureTypeInfo(featureTypeName, schema, mapper);

            // get srdid for each geometry
            for (AttributeDescriptor ad : schema.getAttributeDescriptors()) {
                if (ad instanceof GeometryDescriptor ) {
                    int srid = dataStore.determineSRID(featureTypeName, ad.getLocalName());
                    info.putSRID(ad.getLocalName(), srid);
                }
            }

            // put the infos in the feature type info cache, but only if the caching
            // is allowed, no point in continously overwriting it otherwise
            if (cacheTimeOut > 0) {
                featureTypeMap.put(featureTypeName, info);
                featureTypeTimeoutMap.put(featureTypeName, new Long(now));
            }
        }

        return info;
    }

    /**
     * @see org.geotools.data.jdbc.FeatureTypeHandler#getFIDMapper(java.lang.String)
     */
    public FIDMapper getFIDMapper(String typeName) throws IOException {
        FIDMapper mapper = (FIDMapper) typeMappers.get(typeName);

        if (mapper != null)
            return mapper;
        
        mapper = dataStore.buildFIDMapper(typeName, fmFactory);
        if(mapper == null)
            throw new RuntimeException("Could not build a FIDMapper for type " + typeName);
        typeMappers.put(typeName, mapper);
        return mapper;
    }

    /**
     * @see org.geotools.data.jdbc.FeatureTypeHandler#setFIDMapper(java.lang.String)
     */
    public void setFIDMapper(String typeName, FIDMapper mapper) {
        typeMappers.put(typeName, mapper);
    }

    /**
     * Forces the type handler to throw away all cached information and parse again the database on
     * type requests
     */
    public void forceRefresh() {
        lastTypeNameRequestTime = Long.MIN_VALUE;
        featureTypeMap.clear();
        featureTypeTimeoutMap.clear();
    }

    /**
     * Returns the FIDMapperFactory used by this FeatureTypeHandler
     *
     */
    public FIDMapperFactory getFIDMapperFactory() {
        return fmFactory;
    }

    /**
     * Sets the FIDMapperFactory used by this FeatureTypeHandler. It can't be null.
     *
     * @param factory
     */
    public void setFIDMapperFactory(FIDMapperFactory factory) {
        if(factory == null)
            throw new IllegalArgumentException("FIDMapper factory cannot be null");
            
        fmFactory = factory;
    }
}
