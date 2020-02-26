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
import org.geotools.data.Parameter;
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
    public static final Param DBTYPE =
            new Param(
                    "dbtype",
                    String.class,
                    "Type",
                    true,
                    "geopkg",
                    Collections.singletonMap(Parameter.LEVEL, "program"));

    /** parameter for database instance */
    public static final Param DATABASE =
            new Param(
                    "database",
                    File.class,
                    "Database",
                    true,
                    null,
                    Collections.singletonMap(Param.EXT, "gpkg"));

    public static final Param READ_ONLY = new Param("read_only", Boolean.class, "Read only", false);

    /** base location to store database files */
    File baseDirectory = null;

    GeoPkgGeomWriter.Configuration writerConfig;

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
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        return new GeoPkgDialect(dataStore, writerConfig);
    }

    @Override
    protected String getValidationQuery() {
        return "SELECT 1";
    }

    @Override
    protected String getJDBCUrl(Map params) throws IOException {
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
    protected void setupParameters(Map parameters) {
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
    }

    /**
     * This is left for public API compatibility but it's not as efficient as using the GeoPackage
     * internal pool
     *
     * @param params Map of connection parameter.
     */
    @Override
    public BasicDataSource createDataSource(Map params) throws IOException {
        // create a datasource
        BasicDataSource dataSource = new BasicDataSource();

        // driver
        dataSource.setDriverClassName(getDriverClassName());

        // url
        dataSource.setUrl(getJDBCUrl(params));

        addConnectionProperties(dataSource, params);

        dataSource.setAccessToUnderlyingConnectionAllowed(true);

        return dataSource;
    }

    @Override
    protected DataSource createDataSource(Map params, SQLDialect dialect) throws IOException {
        SQLiteConfig config = new SQLiteConfig();
        config.setSharedCache(true);
        config.enableLoadExtension(true);
        // support for encrypted databases has been ddded after 3.20.1, we'll have to
        // wait for a future release of sqlite-jdbc
        // if(password != null) {
        //     config.setPragma(SQLiteConfig.Pragma.PASSWORD, password);
        // }
        // TODO: add this and make configurable once we upgrade to a sqlitejdbc exposing mmap_size
        // config.setPragma(SQLiteConfig.Pragma.MMAP_SIZE, String.valueOf(1024 * 1024 * 1000));

        // use native "pool", which is actually not pooling anything (that's fast and
        // has less scalability overhead)
        SQLiteConnectionPoolDataSource ds = new SQLiteConnectionPoolDataSource(config);
        ds.setUrl(getJDBCUrl(params));

        return ds;
    }

    static void addConnectionProperties(BasicDataSource dataSource, Map configuration)
            throws IOException {
        SQLiteConfig config = new SQLiteConfig();
        config.setSharedCache(true);
        config.enableLoadExtension(true);
        Object synchronous = READ_ONLY.lookUp(configuration);
        if (Boolean.TRUE.equals(synchronous)) {
            config.setPragma(SQLiteConfig.Pragma.SYNCHRONOUS, "OFF");
            config.setReadOnly(true);
        }
        // config.setPragma(SQLiteConfig.Pragma.MMAP_SIZE, "268435456");

        for (Map.Entry e : config.toProperties().entrySet()) {
            dataSource.addConnectionProperty((String) e.getKey(), (String) e.getValue());
        }
    }

    @Override
    protected JDBCDataStore createDataStoreInternal(JDBCDataStore dataStore, Map params)
            throws IOException {
        dataStore.setDatabaseSchema(null);
        return dataStore;
    }
}
