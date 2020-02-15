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
package org.geotools.data.mysql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import org.geotools.data.Parameter;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.SQLDialect;

/**
 * DataStoreFactory for MySQL database.
 *
 * @author David Winslow, The Open Planning Project
 * @author Nikolaos Pringouris <nprigour@gmail.com> added support for MySQL versions 5.6 (and above)
 */
public class MySQLDataStoreFactory extends JDBCDataStoreFactory {
    /** parameter for database type */
    public static final Param DBTYPE =
            new Param(
                    "dbtype",
                    String.class,
                    "Type",
                    true,
                    "mysql",
                    Collections.singletonMap(Parameter.LEVEL, "program"));
    /** Default port number for MYSQL */
    public static final Param PORT = new Param("port", Integer.class, "Port", true, 3306);
    /** Storage engine to use when creating tables */
    public static final Param STORAGE_ENGINE =
            new Param("storage engine", String.class, "Storage Engine", false, "MyISAM");

    /**
     * EnhanceSpatialSupport is available from MYSQL version 5.6 and onward. This includes some
     * differentiation of the spatial function naming which generally follow the naming convention
     * ST_xxxx. Moreover spatial operations are performed with precise object shape and not with
     * minumum bounding rectangles
     */
    public static final Param ENHANCED_SPATIAL_SUPPORT =
            new Param(
                    "enhancedSpatialSupport",
                    Boolean.class,
                    "Enhanced Spatial Support",
                    false,
                    false);

    protected boolean enhancedSpatialSupport = (boolean) ENHANCED_SPATIAL_SUPPORT.sample;

    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        // return new MySQLDialectPrepared(dataStore);
        return new MySQLDialectBasic(dataStore, enhancedSpatialSupport);
    }

    public String getDisplayName() {
        return "MySQL";
    }

    protected String getDriverClassName() {
        return "com.mysql.jdbc.Driver";
    }

    protected String getDatabaseID() {
        return (String) DBTYPE.sample;
    }

    public String getDescription() {
        return "MySQL Database";
    }

    @Override
    protected String getValidationQuery() {
        return "select version()";
    }

    @Override
    protected void setupParameters(Map parameters) {
        super.setupParameters(parameters);
        parameters.put(DBTYPE.key, DBTYPE);
        parameters.put(PORT.key, PORT);
        parameters.put(STORAGE_ENGINE.key, STORAGE_ENGINE);

        parameters.remove(SCHEMA.key);
    }

    @Override
    protected JDBCDataStore createDataStoreInternal(JDBCDataStore dataStore, Map params)
            throws IOException {
        String storageEngine = (String) STORAGE_ENGINE.lookUp(params);
        if (storageEngine == null) {
            storageEngine = (String) STORAGE_ENGINE.sample;
        }

        Boolean enhancedSpatialFlag = (Boolean) ENHANCED_SPATIAL_SUPPORT.lookUp(params);
        if (enhancedSpatialFlag == null) {
            // enhanced spatial support should be enabled if MySQL
            // version is at least 5.6.
            enhancedSpatialSupport = isMySqlVersion56(dataStore);
        } else if (enhancedSpatialFlag && !isMySqlVersion56(dataStore)) {
            dataStore
                    .getLogger()
                    .info("MySQL version does not support enhancedSpatialSupport. Disabling it");
            enhancedSpatialSupport = false;
        }

        SQLDialect dialect = dataStore.getSQLDialect();
        if (dialect instanceof MySQLDialectBasic) {
            ((MySQLDialectBasic) dialect).setStorageEngine(storageEngine);
            ((MySQLDialectBasic) dialect).setUsePreciseSpatialOps(enhancedSpatialSupport);
        } else {
            ((MySQLDialectPrepared) dialect).setStorageEngine(storageEngine);
            ((MySQLDialectPrepared) dialect).setUsePreciseSpatialOps(enhancedSpatialSupport);
        }

        return dataStore;
    }

    /** check if the version of MySQL is at least 5.6 (or above). */
    protected static boolean isMySqlVersion56(JDBCDataStore dataStore) {
        boolean isMySQLVersion56OrAbove = false;
        Connection con = null;
        try {
            con = dataStore.getDataSource().getConnection();
            int major = con.getMetaData().getDatabaseMajorVersion();
            int minor = con.getMetaData().getDatabaseMinorVersion();
            if (major > 5 || (major == 5 && minor > 6)) {
                isMySQLVersion56OrAbove = true;
            } else {
                isMySQLVersion56OrAbove = false;
            }
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                // do nothing
            }
        }
        return isMySQLVersion56OrAbove;
    }
}
