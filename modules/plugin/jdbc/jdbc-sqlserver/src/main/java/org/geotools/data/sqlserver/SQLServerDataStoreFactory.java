/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.sqlserver;

import java.io.IOException;
import java.util.Map;

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.SQLDialect;

/**
 * DataStore factory for Microsoft SQL Server.
 *
 * @author Justin Deoliveira, OpenGEO
 *
 *
 *
 *
 * @source $URL$
 */
public class SQLServerDataStoreFactory extends JDBCDataStoreFactory {
    /** parameter for database type */
    public static final Param DBTYPE = new Param("dbtype", String.class, "Type", true, "sqlserver");

    /** parameter for using integrated security, only works on windows, ignores the user and password parameters, the current windows user account is used for login*/
    public static final Param INTSEC = new Param("Integrated Security", Boolean.class, "Login as current windows user account. Works only in windows. Ignores user and password settings.", false, new Boolean(false));

    /** parameter for using Native Paging */
    public static final Param NATIVE_PAGING = new Param("Use Native Paging", Boolean.class, "Use native paging for sql queries. For some sets of data, native paging can have a performance impact.", false, Boolean.TRUE);

    /** Metadata table providing information about primary keys **/
    public static final Param GEOMETRY_METADATA_TABLE = new Param("Geometry metadata table", String.class,
            "The optional table containing geometry metadata (geometry type and srid). Can be expressed as 'schema.name' or just 'name'", false);

    /** parameter for using WKB or Sql server binary directly. Setting to true will use WKB */
    public static final Param NATIVE_SERIALIZATION = new Param("Use native geometry serialization", Boolean.class,
            "Use native SQL Server serialization, or WKB serialization.", false, Boolean.FALSE);

    /** parameter for forcing the usage of spatial indexes in queries via sql hints */
    public static final Param FORCE_SPATIAL_INDEX = new Param("Force spatial index usage via hints", Boolean.class,
            "When enabled, spatial filters will be accompained by a WITH INDEX sql hint forcing the usage of the spatial index.", false, Boolean.FALSE);

    /** parameter for forcing the usage of spatial indexes in queries via sql hints */
    public static final Param TABLE_HINTS = new Param("Table hints", String.class,
            "These table hints will be added to every select query.", false, "");

    /** parameter for database port */
    public static final Param PORT = new Param("port", Integer.class, "Port", false);

    /** parameter for database instance */
    public static final Param INSTANCE = new Param("instance", String.class, "Instance Name", false);


    @Override
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        return new SQLServerDialect(dataStore);
    }

    @Override
    protected String getDatabaseID() {
        return (String) DBTYPE.sample;
    }

    @Override
    public String getDescription() {
        return "Microsoft SQL Server";
    }

    @Override
    protected String getDriverClassName() {
        return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    }

    @Override
    protected String getValidationQuery() {
        return "select 1";
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void setupParameters(Map parameters) {
        super.setupParameters(parameters);
        parameters.put(PORT.key, PORT);
        parameters.put(DBTYPE.key, DBTYPE);
        parameters.put(INTSEC.key, INTSEC);
        parameters.put(NATIVE_PAGING.key, NATIVE_PAGING);
        parameters.put(NATIVE_SERIALIZATION.key, NATIVE_SERIALIZATION);
        parameters.put(GEOMETRY_METADATA_TABLE.key, GEOMETRY_METADATA_TABLE);
        parameters.put(FORCE_SPATIAL_INDEX.key, FORCE_SPATIAL_INDEX);
        parameters.put(TABLE_HINTS.key, TABLE_HINTS);
        parameters.put(INSTANCE.key, INSTANCE);
    }

    /**
     *  Builds up the JDBC url in a jdbc:<database>://<host>:<port>;DatabaseName=<dbname>
     */
    @SuppressWarnings("unchecked")
    @Override
    protected String getJDBCUrl(Map params) throws IOException {
        String host = (String) HOST.lookUp(params);
        Integer port = (Integer) PORT.lookUp(params);
        String db = (String) DATABASE.lookUp(params);
        String instance = (String) INSTANCE.lookUp(params);

        String url = "jdbc:" + getDatabaseID() + "://" + host;
        if ( port != null ) {
            url += ":" + port;
        }else if (instance != null) {
            url += "\\"+instance;
        }

        if ( db != null ) {
            url += "/" + db;
        }

        Boolean intsec = (Boolean) INTSEC.lookUp(params);
        if (db != null) {
            url = url.substring(0, url.lastIndexOf("/")) + (db != null ? ";DatabaseName="+db : "");
        }

        if (intsec != null && intsec.booleanValue()) {
            url = url + ";integratedSecurity=true";
        }

        return url;
    }
    @Override
    public boolean canProcess(Map params) {

        if (!super.canProcess(params)) {
            return false; // was not in agreement with getParametersInfo
        }
        Integer port = null;
        String instance = null;
        try {
            port = (Integer) PORT.lookUp(params);
            instance = (String) INSTANCE.lookUp(params);
        } catch (IOException e) {

        }

        //we only need one or the other of these
        return (port != null || instance != null);
    }
    @Override
    protected JDBCDataStore createDataStoreInternal(JDBCDataStore dataStore, Map params)
            throws IOException {
        SQLServerDialect dialect = (SQLServerDialect) dataStore.getSQLDialect();

        // check the geometry metadata table
        String metadataTable = (String) GEOMETRY_METADATA_TABLE.lookUp(params);
        dialect.setGeometryMetadataTable(metadataTable);

        // check native paging
        Boolean useNativePaging = (Boolean) NATIVE_PAGING.lookUp(params);
        dialect.setUseOffSetLimit(useNativePaging == null || Boolean.TRUE.equals(useNativePaging));

        // check serialization format
        Boolean useNativeSerialization = (Boolean) NATIVE_SERIALIZATION.lookUp(params);
        if (useNativeSerialization != null) {
            dialect.setUseNativeSerialization(useNativeSerialization);
        }

        // check spatial index hints usage
        Boolean forceSpatialIndexes = (Boolean) FORCE_SPATIAL_INDEX.lookUp(params);
        if (forceSpatialIndexes != null) {
            dialect.setForceSpatialIndexes(forceSpatialIndexes);
        }

        String tableHints = (String) TABLE_HINTS.lookUp(params);
        if (tableHints != null) {
            dialect.setTableHints(tableHints);
        }

        return dataStore;
    }

}
