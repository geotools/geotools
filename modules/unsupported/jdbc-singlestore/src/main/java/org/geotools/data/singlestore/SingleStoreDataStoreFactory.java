/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.singlestore;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import org.geotools.api.data.Parameter;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.SQLDialect;

/**
 * DataStoreFactory for SingleStore database.
 *
 * @author David Winslow, The Open Planning Project
 * @author Nikolaos Pringouris <nprigour@gmail.com> added support for SingleStore versions 5.6 (and above)
 */
public class SingleStoreDataStoreFactory extends JDBCDataStoreFactory {
    /** parameter for database type */
    public static final Param DBTYPE = new Param(
            "dbtype", String.class, "Type", true, "singlestore", Collections.singletonMap(Parameter.LEVEL, "program"));
    /** Default port number for MYSQL */
    public static final Param PORT = new Param("port", Integer.class, "Port", true, 3306);
    /** Storage engine to use when creating tables */
    public static final Param ENHANCED_SPATIAL_SUPPORT =
            new Param("enhancedSpatialSupport", Boolean.class, "Enhanced Spatial Support", false, false);

    protected boolean enhancedSpatialSupport = (boolean) ENHANCED_SPATIAL_SUPPORT.sample;

    @Override
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        // return new SingleStoreDialectPrepared(dataStore);
        return new SingleStoreDialectBasic(dataStore, enhancedSpatialSupport);
    }

    @Override
    public String getDisplayName() {
        return "SingleStore";
    }

    @Override
    protected String getDriverClassName() {
        return "com.singlestore.jdbc.Driver";
    }

    @Override
    protected String getDatabaseID() {
        return (String) DBTYPE.sample;
    }

    @Override
    public String getDescription() {
        return "SingleStore Database";
    }

    @Override
    protected String getValidationQuery() {
        return "select version()";
    }

    @Override
    protected void setupParameters(Map<String, Object> parameters) {
        super.setupParameters(parameters);
        parameters.put(DBTYPE.key, DBTYPE);
        parameters.put(PORT.key, PORT);

        // TODO Why do we remove the schema key is this necessary
        parameters.remove(SCHEMA.key);
    }

    @Override
    protected JDBCDataStore createDataStoreInternal(JDBCDataStore dataStore, Map<String, ?> params) throws IOException {
        Boolean enhancedSpatialFlag = (Boolean) ENHANCED_SPATIAL_SUPPORT.lookUp(params);
        enhancedSpatialSupport = enhancedSpatialFlag != null && enhancedSpatialFlag;

        return dataStore;
    }
}
