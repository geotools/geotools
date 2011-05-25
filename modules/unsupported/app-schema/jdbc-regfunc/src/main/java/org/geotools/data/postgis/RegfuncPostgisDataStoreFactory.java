/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

/**
 * Factory for creating {@link RegfuncPostgisDataStore} instances, which support registered
 * functions.
 * 
 * <p>
 * 
 * This factory differs from {@link PostgisDataStoreFactory} only in having params with key "dbtype"
 * and value "postgis-regfunc", and returning instances of {@link RegfuncPostgisDataStore}.
 * 
 * <p>
 * 
 * TODO: this class will be obsolete in the future, when enough of its peers functionality is pulled
 * up into core code.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 * @version $Id$
 *
 * @source $URL$
 * @since 2.4
 */
public class RegfuncPostgisDataStoreFactory extends PostgisDataStoreFactory {

    public static final Param DBTYPE = new Param("dbtype", String.class,
            "must be 'postgis-regfunc'", true, "postgis-regfunc");

    /**
     * Return true if the parameters can be used to create a {@link RegfuncPostgisDataStore}.
     * 
     * @param params
     *                database connection parameters, a Map with String keys and
     *                String/Integer/Boolean values.
     * 
     * @see org.geotools.data.postgis.PostgisDataStoreFactory#canProcess(java.util.Map)
     */
    public boolean canProcess(Map params) {
        if (params != null && params.get(DBTYPE.key) != null
                && ((String) params.get(DBTYPE.key)).equalsIgnoreCase((String) DBTYPE.sample)) {
            return super.canProcess(rewriteParamsForSuper(params));
        } else {
            return false;
        }
    }

    /**
     * This method duplicates the implementation in {@link PostgisDataStoreFactory}, except that it
     * ensures a {@link RegfuncPostgisDataStore} is created, enabling support for registered
     * functions.
     * 
     * @see org.geotools.data.postgis.PostgisDataStoreFactory#createDataStoreInternal(javax.sql.DataSource,
     *      java.lang.String, java.lang.String)
     */
    // @Override
    protected PostgisDataStore createDataStoreInternal(DataSource dataSource, String namespace,
            String schema) throws IOException {
        if (schema == null && namespace == null) {
            return new RegfuncPostgisDataStore(dataSource);
        } else if (schema == null && namespace != null) {
            return new RegfuncPostgisDataStore(dataSource, namespace);
        } else {
            return new RegfuncPostgisDataStore(dataSource, schema, namespace);
        }
    }

    /**
     * Rewrite the parameters for the parent class, by replacing the dbtype with the one it expects.
     * 
     * @param params
     *                database connection parameters, a Map with String keys and
     *                String/Integer/Boolean values.
     * @return new params Map with dbtype postgis, suitable for passing to
     *         {@link PostgisDataStoreFactory}.
     */
    private Map rewriteParamsForSuper(Map params) {
        Map rewrittenParams = new HashMap();
        Set entries = params.entrySet();
        Iterator entryIterator = entries.iterator();
        while (entryIterator.hasNext()) {
            Map.Entry entry = (Map.Entry) entryIterator.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (((String) key).equalsIgnoreCase(DBTYPE.key)
                    && ((String) value).equalsIgnoreCase((String) DBTYPE.sample)) {
                rewrittenParams.put(key, PostgisDataStoreFactory.DBTYPE.sample);
            } else {
                rewrittenParams.put(key, value);
            }
        }
        return rewrittenParams;
    }

    /**
     * Describe parameters.
     * 
     * @see org.geotools.data.DataStoreFactorySpi#getParametersInfo()
     */
    // @Override
    public Param[] getParametersInfo() {
        return new Param[] { DBTYPE, HOST, PORT, SCHEMA, DATABASE, USER, PASSWD, MAXCONN, MINCONN,
                VALIDATECONN, WKBENABLED, LOOSEBBOX, ESTIMATEDEXTENT, NAMESPACE };
    }

}
