/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.mbtiles;

import static org.geotools.jdbc.JDBCDataStoreFactory.DATASOURCE;
import static org.geotools.jdbc.JDBCDataStoreFactory.NAMESPACE;
import static org.geotools.jdbc.JDBCDataStoreFactory.PASSWD;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataUtilities;
import org.geotools.data.Parameter;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.sqlite.SQLiteConfig;
import org.sqlite.javax.SQLiteConnectionPoolDataSource;

public class MBTilesDataStoreFactory implements DataStoreFactorySpi {

    private static final String MBTILES_DBTYPE = "mbtiles";

    /** parameter for database type */
    public static final Param DBTYPE =
            new Param(
                    "dbtype",
                    String.class,
                    "Type",
                    true,
                    MBTILES_DBTYPE,
                    Collections.singletonMap(Parameter.LEVEL, "program"));

    /** optional user parameter */
    public static final Param USER =
            new Param(
                    JDBCDataStoreFactory.USER.key,
                    JDBCDataStoreFactory.USER.type,
                    JDBCDataStoreFactory.USER.description,
                    false,
                    JDBCDataStoreFactory.USER.sample);

    public static final Param DATABASE =
            new Param(
                    "database",
                    File.class,
                    "Database",
                    true,
                    null,
                    Collections.singletonMap(Param.EXT, "mbtiles"));

    @Override
    public String getDisplayName() {
        return "MBTiles with vector tiles";
    }

    @Override
    public String getDescription() {
        return getDisplayName();
    }

    @Override
    public Param[] getParametersInfo() {
        LinkedHashMap map = new LinkedHashMap();
        setupParameters(map);

        return (Param[]) map.values().toArray(new Param[map.size()]);
    }

    protected void setupParameters(Map parameters) {
        parameters.put(DBTYPE.key, DBTYPE);
        parameters.put(DATABASE.key, DATABASE);
        parameters.put(NAMESPACE.key, NAMESPACE);
        parameters.put(USER.key, USER);
        parameters.put(PASSWD.key, PASSWD);
    }

    @Override
    public boolean isAvailable() {
        try {
            // check if the sqlite-jdbc driver is in the classpath
            Class.forName("org.sqlite.JDBC");

            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException {
        return null;
    }

    @Override
    public DataStore createDataStore(Map<String, Serializable> params) throws IOException {
        // datasource
        // check if the DATASOURCE parameter was supplied, it takes precendence
        DataSource ds = (DataSource) DATASOURCE.lookUp(params);
        if (ds == null) {
            ds = createDataSource(params, true);
        }
        String namespace = (String) NAMESPACE.lookUp(params);
        return new MBTilesDataStore(namespace, new MBTilesFile(ds));
    }

    /**
     * Same as the GeoPackage data store, if you modify this, probably want to check if
     * modifications make sense there too
     */
    protected DataSource createDataSource(Map<String, Serializable> params, boolean readOnly)
            throws IOException {
        SQLiteConfig config = new SQLiteConfig();
        config.setSharedCache(true);
        config.enableLoadExtension(true);
        if (readOnly) {
            config.setReadOnly(true);
            config.setPragma(SQLiteConfig.Pragma.SYNCHRONOUS, "OFF");
        }

        // use native "pool", which is actually not pooling anything (that's fast and
        // has less scalability overhead than DBCP)
        SQLiteConnectionPoolDataSource ds = new SQLiteConnectionPoolDataSource(config);
        ds.setUrl(getJDBCUrl(params));

        return ds;
    }

    private String getJDBCUrl(Map params) throws IOException {
        File db = (File) DATABASE.lookUp(params);
        if (db.getPath().startsWith("file:")) {
            db = new File(db.getPath().substring(5));
        }
        return "jdbc:sqlite:" + db;
    }

    public boolean canProcess(Map params) {
        if (!DataUtilities.canProcess(params, getParametersInfo())) {
            return false;
        }
        return checkDBType(params);
    }

    protected final boolean checkDBType(Map params) {
        try {
            String type = (String) DBTYPE.lookUp(params);

            if (MBTILES_DBTYPE.equals(type)) {
                return true;
            }

            return false;
        } catch (IOException e) {
            return false;
        }
    }
}
