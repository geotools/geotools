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
import org.geotools.api.data.Parameter;
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
    public static final Param DBTYPE = new Param(
            "dbtype", String.class, "Type", true, "mysql", Collections.singletonMap(Parameter.LEVEL, "program"));
    /** Default port number for MYSQL */
    public static final Param PORT = new Param("port", Integer.class, "Port", true, 3306);
    /** Storage engine to use when creating tables */
    public static final Param STORAGE_ENGINE =
            new Param("storage engine", String.class, "Storage Engine", false, "MyISAM");

    /**
     * Enhanced Spatial Support is available from MySQL version 5.6 and onward. This includes some differentiation of
     * the spatial function naming which generally follow the naming convention ST_xxxx. Moreover spatial operations are
     * performed with precise object shape and not with minimum bounding rectangles. As of version 8 it is the only
     * option.
     */
    public static final Param ENHANCED_SPATIAL_SUPPORT =
            new Param("enhancedSpatialSupport", Boolean.class, "Enhanced Spatial Support", false, false);

    protected boolean enhancedSpatialSupport = (boolean) ENHANCED_SPATIAL_SUPPORT.sample;

    @Override
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        // return new MySQLDialectPrepared(dataStore);
        return new MySQLDialectBasic(dataStore, enhancedSpatialSupport);
    }

    @Override
    public String getDisplayName() {
        return "MySQL";
    }

    @Override
    protected String getDriverClassName() {
        return "com.mysql.cj.jdbc.Driver";
    }

    @Override
    protected String getDatabaseID() {
        return (String) DBTYPE.sample;
    }

    @Override
    public String getDescription() {
        return "MySQL Database";
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
        parameters.put(STORAGE_ENGINE.key, STORAGE_ENGINE);

        parameters.remove(SCHEMA.key);
    }

    @Override
    protected JDBCDataStore createDataStoreInternal(JDBCDataStore dataStore, Map<String, ?> params) throws IOException {
        String storageEngine = (String) STORAGE_ENGINE.lookUp(params);
        if (storageEngine == null) {
            storageEngine = (String) STORAGE_ENGINE.sample;
        }

        Boolean enhancedSpatialFlag = (Boolean) ENHANCED_SPATIAL_SUPPORT.lookUp(params);
        if (enhancedSpatialFlag == null) {
            // enhanced spatial support should be enabled if MySQL
            // version is at least 5.6.
            enhancedSpatialSupport = isMySqlVersion56OrAbove(dataStore);
        } else if (enhancedSpatialFlag && !isMySqlVersion56OrAbove(dataStore)) {
            dataStore.getLogger().info("MySQL version does not support enhancedSpatialSupport. Disabling it");
            enhancedSpatialSupport = false;
        }

        SQLDialect dialect = dataStore.getSQLDialect();
        if (dialect instanceof MySQLDialectBasic) {
            ((MySQLDialectBasic) dialect).setStorageEngine(storageEngine);
            ((MySQLDialectBasic) dialect).setUsePreciseSpatialOps(enhancedSpatialSupport);
            ((MySQLDialectBasic) dialect).setMySqlVersion80OrAbove(this.isMySqlVersion80OrAbove(dataStore));
        } else {
            ((MySQLDialectPrepared) dialect).setStorageEngine(storageEngine);
            ((MySQLDialectPrepared) dialect).setUsePreciseSpatialOps(enhancedSpatialSupport);
            ((MySQLDialectPrepared) dialect).setMySqlVersion80OrAbove(this.isMySqlVersion80OrAbove(dataStore));
        }

        return dataStore;
    }

    /**
     * check if the version of MySQL is greater than 5.6.
     *
     * @return {@code true} if the database is higher than 5.6
     */
    protected static boolean isMySqlVersion56OrAbove(JDBCDataStore dataStore) {
        boolean isMySQLVersion56OrAbove = false;
        try (Connection con = dataStore.getDataSource().getConnection()) {
            int major = con.getMetaData().getDatabaseMajorVersion();
            int minor = con.getMetaData().getDatabaseMinorVersion();
            isMySQLVersion56OrAbove = major > 5 || major == 5 && minor > 6;
        } catch (SQLException | IllegalStateException e) {
            dataStore.getLogger().warning("Unable to determine database version. Message: " + e.getLocalizedMessage());
        }
        return isMySQLVersion56OrAbove;
    }
    /**
     * check if the version of MySQL is 8.0 or greater. Needed to determine which syntax can be used for eg.
     * {@code ST_SRID()}
     *
     * @return {@code true} if the database varion is is 8.0 or greater
     */
    protected static boolean isMySqlVersion80OrAbove(JDBCDataStore dataStore) {
        boolean isMySQLVersion80OrAbove = false;
        try (Connection con = dataStore.getDataSource().getConnection()) {
            int major = con.getMetaData().getDatabaseMajorVersion();
            isMySQLVersion80OrAbove = major >= 8;
        } catch (SQLException | IllegalStateException e) {
            dataStore.getLogger().warning("Unable to determine database version. Message: " + e.getLocalizedMessage());
        }
        return isMySQLVersion80OrAbove;
    }
}
