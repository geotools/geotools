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
package org.geotools.data.jdbc;

import java.util.Collections;
import java.util.Map;


/**
 * Configuration object for JDBCDataStore.
 *
 * @author Sean Geoghegan, Defence Science and Technology Organisation
 * @author $Author: aaime $
 *
 * @source $URL$
 * @version $Id$ Last Modified: $Date: 2004/04/18 09:19:43 $
 * 
 * @deprecated scheduled for removal in 2.7, use classes in org.geotools.jdbc
 */
public class JDBCDataStoreConfig {
    public static final String FID_GEN_INSERT_NULL = "INSERT_NULL";
    public static final String FID_GEN_MANUAL_INC = "MANUAL_INC";
    public static final String DEFAULT_FID_GEN_KEY = "DEFAULT_GEN";
    public static final String DEFAULT_FID_GEN = FID_GEN_INSERT_NULL;
    
    /**
     * Namespace of schema describing this JDBCDatastore.
     * <p>
     * Assume this is in the GML sense?
     * </p> 
     */
    private final String namespace;
    
    /** Name of database schema */ 
    private final String databaseSchemaName;
    
    protected final long typeHandlerCacheTimout;
    
    /**
     * Construct <code>JDBCDataStoreConfig</code>.
     *
     */
    public JDBCDataStoreConfig() {
        this(null, null, Collections.EMPTY_MAP, Collections.EMPTY_MAP);
    }

    /**
     * Construct <code>JDBCDataStoreConfig</code>.
     *
     * @param namespace
     * @param databaseSchemaName
     * @param fidColumnOverrideMap
     * @param fidGenerationMap
     */
    public JDBCDataStoreConfig(String namespace, String databaseSchemaName,
        Map fidColumnOverrideMap, Map fidGenerationMap) {
        this(namespace, databaseSchemaName, Long.MAX_VALUE);
    }

    /**
     * Construct <code>JDBCDataStoreConfig</code>.
     *
     * @param namespace
     * @param databaseSchemaName
     * @param typeHandlerCacheTimeout
     */
    public JDBCDataStoreConfig(String namespace, String databaseSchemaName,
         long typeHandlerCacheTimeout) {
        this.namespace = namespace;
	if (databaseSchemaName == null || databaseSchemaName.equals("")) {
	    this.databaseSchemaName = null;
	} else {
	    this.databaseSchemaName = databaseSchemaName;
        }
	this.typeHandlerCacheTimout = typeHandlerCacheTimeout;
    }

    /**
     * TODO summary sentence for createWithNameSpaceAndSchemaName ...
     * 
     * @param namespace
     * @param schemaName
     */
    public static JDBCDataStoreConfig createWithNameSpaceAndSchemaName(
        String namespace, String schemaName) {
        return new JDBCDataStoreConfig(namespace, schemaName,
            Collections.EMPTY_MAP, Collections.EMPTY_MAP);
    }

    public static JDBCDataStoreConfig createWithSchemaNameAndFIDGenMap(
        String schemaName, Map fidGenerationMap) {
        return new JDBCDataStoreConfig(null, schemaName, Collections.EMPTY_MAP,
            fidGenerationMap);
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the databaseSchemaName.
     */
    public String getDatabaseSchemaName() {
        return databaseSchemaName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the namespace.
     */
    public String getNamespace() {
        return namespace;
    }
    /**
     */
    public long getTypeHandlerTimeout() {
        return typeHandlerCacheTimout;
    }

}
