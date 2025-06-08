/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.api.data.Parameter;
import org.geotools.geopkg.geom.GeoPkgGeomWriter;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.SQLDialect;
import org.sqlite.SQLiteConfig;
import org.sqlite.javax.SQLiteConnectionPoolDataSource;

/**
 * The GeoPackage DataStore Factory.
 *
 * @author Justin Deoliveira
 * @author Niels Charlier
 */
public class GeoPkgDataStoreFactory extends JDBCDataStoreFactory {

    /** parameter for database type */
    public static final Param DBTYPE = new Param(
            "dbtype", String.class, "Type", true, "geopkg", Collections.singletonMap(Parameter.LEVEL, "program"));

    /** parameter for database instance */
    public static final Param DATABASE =
            new Param("database", File.class, "Database", true, null, Collections.singletonMap(Param.EXT, "gpkg"));

    public static final Param READ_ONLY = new Param("read_only", Boolean.class, "Read only", false);

    public static final Param CONTENTS_ONLY =
            new Param("contents_only", Boolean.class, "Contents only", false, Boolean.TRUE);

    /** Maximum mapped memory, defaults to null */
    public static final Param MEMORY_MAP_SIZE =
            new Param("memory map size", Integer.class, "Max memory SQlite will memory map, in megabytes", false, null);

    /** base location to store database files */
    File baseDirectory = null;

    GeoPkgGeomWriter.Configuration writerConfig;

    private static int sqlLiteConnectTimeout = 60000;

    public GeoPkgDataStoreFactory() {
        this.writerConfig = new GeoPkgGeomWriter.Configuration();
    }

    public GeoPkgDataStoreFactory(GeoPkgGeomWriter.Configuration writerConfig) {
        this.writerConfig = writerConfig;
    }

    /**
     * Sets the base location to store database files.
     *
     * @param baseDirectory A directory.
     */
    public void setBaseDirectory(File baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    @Override
    protected String getDatabaseID() {
        return "geopkg";
    }

    @Override
    public String getDescription() {
        return "GeoPackage";
    }

    @Override
    protected String getDriverClassName() {
        return "org.sqlite.JDBC";
    }

    @Override
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore, Map<String, ?> params) {
        try {
            Boolean contentsOnly = (Boolean) CONTENTS_ONLY.lookUp(params);
            GeoPkgDialect dialect = (GeoPkgDialect) createSQLDialect(dataStore);
            if (contentsOnly != null) {
                dialect.setContentsOnly(contentsOnly);
            }
            return dialect;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        return new GeoPkgDialect(dataStore, writerConfig);
    }

    @Override
    protected String getValidationQuery() {
        return "SELECT 1";
    }

    @Override
    protected String getJDBCUrl(Map<String, ?> params) throws IOException {
        File db = (File) DATABASE.lookUp(params);
        if (db.getPath().startsWith("file:")) {
            db = new File(db.getPath().substring(5));
        }
        if (baseDirectory != null) {
            // check for a relative path
            if (!db.isAbsolute()) {
                db = new File(baseDirectory, db.getPath());
            }
        }
        return "jdbc:sqlite:" + db;
    }

    @Override
    protected void setupParameters(Map<String, Object> parameters) {
        super.setupParameters(parameters);

        // remove unnecessary parameters
        parameters.remove(HOST.key);
        parameters.remove(PORT.key);
        parameters.remove(SCHEMA.key);
        parameters.remove(USER.key); // sqlite has no user, just a password
        parameters.remove(MAXCONN.key);
        parameters.remove(MINCONN.key);
        parameters.remove(MAXWAIT.key);
        parameters.remove(VALIDATECONN.key);
        parameters.remove(TEST_WHILE_IDLE.key);
        parameters.remove(TIME_BETWEEN_EVICTOR_RUNS.key);
        parameters.remove(MIN_EVICTABLE_TIME.key);
        parameters.remove(EVICTOR_TESTS_PER_RUN.key);

        // replace database with File database
        parameters.put(DATABASE.key, DATABASE);
        // replace dbtype
        parameters.put(DBTYPE.key, DBTYPE);
        // add the read only flag
        parameters.put(READ_ONLY.key, READ_ONLY);
        // memory mapping
        parameters.put(MEMORY_MAP_SIZE.key, MEMORY_MAP_SIZE);
    }

    /**
     * This is left for public API compatibility but it's not as efficient as using the GeoPackage internal pool
     *
     * @param params Map of connection parameter.
     */
    @Override
    public BasicDataSource createDataSource(Map<String, ?> params) throws IOException {
        // create a datasource
        BasicDataSource dataSource = new BasicDataSource();

        // driver
        dataSource.setDriverClassName(getDriverClassName());

        // url
        dataSource.setUrl(getJDBCUrl(params));

        addConnectionProperties(dataSource, params);
        dataSource.setMinIdle(1);

        dataSource.setAccessToUnderlyingConnectionAllowed(true);

        return dataSource;
    }

    @Override
    protected DataSource createDataSource(Map<String, ?> params, SQLDialect dialect) throws IOException {
        SQLiteConfig config = setupSQLiteConfig(params);

        // use native "pool", which is actually not pooling anything (that's fast and
        // has less scalability overhead)
        SQLiteConnectionPoolDataSource ds = new SQLiteConnectionPoolDataSource(config);
        ds.setUrl(getJDBCUrl(params));

        return ds;
    }

    private static SQLiteConfig setupSQLiteConfig(Map<String, ?> params) throws IOException {
        SQLiteConfig config = new SQLiteConfig();
        config.setSharedCache(true);
        config.enableLoadExtension(true);
        Object readOnly = READ_ONLY.lookUp(params);
        if (Boolean.TRUE.equals(readOnly)) {
            config.setPragma(SQLiteConfig.Pragma.SYNCHRONOUS, "OFF");
            config.setReadOnly(true);
        }
        Object map = MEMORY_MAP_SIZE.lookUp(params);
        if (map instanceof Integer && (Integer) map >= 0) {
            int memoryMB = (Integer) map;
            config.setPragma(SQLiteConfig.Pragma.MMAP_SIZE, String.valueOf(memoryMB * 1024 * 1024));
        }
        config.setBusyTimeout(sqlLiteConnectTimeout);
        return config;
    }

    static void addConnectionProperties(BasicDataSource dataSource, Map<String, ?> configuration) throws IOException {
        SQLiteConfig config = setupSQLiteConfig(configuration);

        for (Map.Entry<Object, Object> e : config.toProperties().entrySet()) {
            dataSource.addConnectionProperty((String) e.getKey(), (String) e.getValue());
        }
    }

    @Override
    protected JDBCDataStore createDataStoreInternal(JDBCDataStore dataStore, Map<String, ?> params) throws IOException {
        dataStore.setDatabaseSchema(null);
        return dataStore;
    }

    /**
     * Sets the timeout for SQLite connections in miliseconds (for testing). Default is 60 seconds.
     *
     * @param sqlLiteConnectTimeout timeout in miliseconds
     */
    public static void setSqlLiteConnectTimeout(int sqlLiteConnectTimeout) {
        GeoPkgDataStoreFactory.sqlLiteConnectTimeout = sqlLiteConnectTimeout;
    }
}
