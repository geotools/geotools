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
package org.geotools.data.postgis;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.geotools.data.Parameter;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.SQLDialect;
import org.geotools.util.KVP;
import org.geotools.util.logging.Logging;
import org.postgresql.jdbc.SslMode;

public class PostgisNGDataStoreFactory extends JDBCDataStoreFactory {

    static final Logger LOGGER = Logging.getLogger(PostgisNGDataStoreFactory.class);

    /** parameter for database type */
    public static final Param DBTYPE =
            new Param(
                    "dbtype",
                    String.class,
                    "Type",
                    true,
                    "postgis",
                    Collections.singletonMap(Parameter.LEVEL, "program"));

    /** enables using && in bbox queries */
    public static final Param LOOSEBBOX =
            new Param(
                    "Loose bbox",
                    Boolean.class,
                    "Perform only primary filter on bbox",
                    false,
                    Boolean.TRUE);

    /** parameter that enables estimated extends instead of exact ones */
    public static final Param ESTIMATED_EXTENTS =
            new Param(
                    "Estimated extends",
                    Boolean.class,
                    "Use the spatial index information to quickly get an estimate of the data bounds",
                    false,
                    Boolean.TRUE);

    /** parameter for database port */
    public static final Param PORT = new Param("port", Integer.class, "Port", true, 5432);

    /** parameter for database schema */
    public static final Param SCHEMA = new Param("schema", String.class, "Schema", false, "public");

    /** attempt to create the database if missing */
    public static final Param CREATE_DB_IF_MISSING =
            new Param(
                    "create database",
                    Boolean.class,
                    "Creates the database if it does not exist yet",
                    false,
                    false,
                    Param.LEVEL,
                    "advanced");

    /** attempt to create the database if missing */
    public static final Param CREATE_PARAMS =
            new Param(
                    "create database params",
                    String.class,
                    "Extra specifications appeneded to the CREATE DATABASE command",
                    false,
                    "",
                    Param.LEVEL,
                    "advanced");

    /** Wheter a prepared statements based dialect should be used, or not */
    public static final Param PREPARED_STATEMENTS =
            new Param(
                    "preparedStatements",
                    Boolean.class,
                    "Use prepared statements",
                    false,
                    Boolean.FALSE);

    /** Enables direct encoding of selected filter functions in sql */
    public static final Param ENCODE_FUNCTIONS =
            new Param(
                    "encode functions",
                    Boolean.class,
                    "set to true to have a set of filter functions be translated directly in SQL. "
                            + "Due to differences in the type systems the result might not be the same as evaluating "
                            + "them in memory, including the SQL failing with errors while the in memory version works fine. "
                            + "However this allows to push more of the filter into the database, increasing performance."
                            + "the postgis table.",
                    false,
                    Boolean.TRUE,
                    new KVP(Param.LEVEL, "advanced"));

    /** Enables usage of ST_Simplify when the queries contain geometry simplification hints */
    public static final Param SIMPLIFY =
            new Param(
                    "Support on the fly geometry simplification",
                    Boolean.class,
                    "When enabled, operations such as map rendering will pass a hint that will enable the usage of ST_Simplify",
                    false,
                    Boolean.TRUE);

    public static final Param SSL_MODE =
            new Param(
                    "SSL mode",
                    SslMode.class,
                    "The connectin SSL mode",
                    false,
                    SslMode.DISABLE,
                    new KVP(Param.OPTIONS, Arrays.asList(SslMode.values())));

    @Override
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore, Map params) {
        PostGISDialect dialect = new PostGISDialect(dataStore);
        try {
            if (Boolean.TRUE.equals(PREPARED_STATEMENTS.lookUp(params))) {
                return new PostGISPSDialect(dataStore, dialect);
            }
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(
                        Level.FINE,
                        "Failed to lookup prepared statement parameter, continuining with non prepared dialect",
                        e);
        }

        return dialect;
    }

    @Override
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        return new PostGISDialect(dataStore);
    }

    @Override
    protected String getDatabaseID() {
        return (String) DBTYPE.sample;
    }

    @Override
    public String getDisplayName() {
        return "PostGIS";
    }

    public String getDescription() {
        return "PostGIS Database";
    }

    @Override
    protected String getDriverClassName() {
        return "org.postgresql.Driver";
    }

    @Override
    protected boolean checkDBType(Map params) {
        if (super.checkDBType(params)) {
            // check for old factory
            try {
                Class.forName("org.geotools.data.postgis.PostgisDataStoreFactory");

                // old factory is around, let it handle the connection
                return false;
            } catch (ClassNotFoundException e) {
                // old factory is not around, handle this connection
                return true;
            }
        } else {
            // check for postgisng as well
            return checkDBType(params, "postgisng");
        }
    }

    protected JDBCDataStore createDataStoreInternal(JDBCDataStore dataStore, Map params)
            throws IOException {

        // setup loose bbox
        SQLDialect genericDialect = dataStore.getSQLDialect();
        PostGISDialect dialect;
        if (genericDialect instanceof PostGISPSDialect) {
            dialect = ((PostGISPSDialect) genericDialect).getDelegate();
        } else {
            dialect = (PostGISDialect) dataStore.getSQLDialect();
        }
        Boolean loose = (Boolean) LOOSEBBOX.lookUp(params);
        dialect.setLooseBBOXEnabled(loose == null || Boolean.TRUE.equals(loose));

        // check the estimated extents
        Boolean estimated = (Boolean) ESTIMATED_EXTENTS.lookUp(params);
        dialect.setEstimatedExtentsEnabled(estimated == null || Boolean.TRUE.equals(estimated));

        // check if we can encode functions in sql
        Boolean encodeFunctions = (Boolean) ENCODE_FUNCTIONS.lookUp(params);
        dialect.setFunctionEncodingEnabled(encodeFunctions == null || encodeFunctions);

        // setup the ps dialect if need be
        Boolean usePs = (Boolean) PREPARED_STATEMENTS.lookUp(params);
        if (Boolean.TRUE.equals(usePs)) {
            dataStore.setSQLDialect(new PostGISPSDialect(dataStore, dialect));
        }

        // check geometry simplification (on by default)
        Boolean simplify = (Boolean) SIMPLIFY.lookUp(params);
        dialect.setSimplifyEnabled(simplify == null || simplify);

        // encode BBOX filter with wrapping ST_Envelope (GEOT-5167)
        Boolean encodeBBOXAsEnvelope = false;
        String largeGeometriesOptimized =
                System.getProperty("org.geotools.data.postgis.largeGeometriesOptimize");
        if (largeGeometriesOptimized != null) {
            encodeBBOXAsEnvelope = largeGeometriesOptimized.toLowerCase().equals("true");
        }
        dialect.setEncodeBBOXFilterAsEnvelope(Boolean.TRUE.equals(encodeBBOXAsEnvelope));

        return dataStore;
    }

    @Override
    protected void setupParameters(Map parameters) {
        // NOTE: when adding parameters here remember to add them to PostgisNGJNDIDataStoreFactory

        super.setupParameters(parameters);
        parameters.put(DBTYPE.key, DBTYPE);
        parameters.put(SCHEMA.key, SCHEMA);
        parameters.put(LOOSEBBOX.key, LOOSEBBOX);
        parameters.put(ESTIMATED_EXTENTS.key, ESTIMATED_EXTENTS);
        parameters.put(PORT.key, PORT);
        parameters.put(SSL_MODE.key, SSL_MODE);
        parameters.put(PREPARED_STATEMENTS.key, PREPARED_STATEMENTS);
        parameters.put(MAX_OPEN_PREPARED_STATEMENTS.key, MAX_OPEN_PREPARED_STATEMENTS);
        parameters.put(ENCODE_FUNCTIONS.key, ENCODE_FUNCTIONS);
        parameters.put(SIMPLIFY.key, SIMPLIFY);
        parameters.put(CREATE_DB_IF_MISSING.key, CREATE_DB_IF_MISSING);
        parameters.put(CREATE_PARAMS.key, CREATE_PARAMS);
    }

    @Override
    protected String getValidationQuery() {
        return "select now()";
    }

    @Override
    protected String getJDBCUrl(Map params) throws IOException {
        String host = (String) HOST.lookUp(params);
        String db = (String) DATABASE.lookUp(params);
        int port = (Integer) PORT.lookUp(params);
        String url = "jdbc:postgresql" + "://" + host + ":" + port + "/" + db;
        SslMode mode = (SslMode) SSL_MODE.lookUp(params);
        if (mode != null) {
            url = url + "?sslmode=" + mode + "&binaryTransferEnable=bytea";
        }

        return url;
    }

    protected DataSource createDataSource(Map params, SQLDialect dialect) throws IOException {
        DataSource ds = super.createDataSource(params, dialect);
        JDBCDataStore store = new JDBCDataStore();

        if (Boolean.TRUE.equals(CREATE_DB_IF_MISSING.lookUp(params))) {
            // verify we can connect
            Connection cx = null;
            boolean canConnect = true;
            try {
                cx = ds.getConnection();
            } catch (SQLException e) {
                canConnect = false;
            } finally {
                store.closeSafe(cx);
            }

            if (!canConnect) {
                // get the connection params
                String host = (String) HOST.lookUp(params);
                int port = (Integer) PORT.lookUp(params);
                String db = (String) DATABASE.lookUp(params);
                String user = (String) USER.lookUp(params);
                String password = (String) PASSWD.lookUp(params);

                Statement st = null;
                try {
                    // connect to template1 instead
                    String url = "jdbc:postgresql" + "://" + host + ":" + port + "/template1";
                    cx = getConnection(user, password, url);

                    // create the database

                    String createParams = (String) CREATE_PARAMS.lookUp(params);
                    String sql =
                            "CREATE DATABASE \""
                                    + db
                                    + "\" "
                                    + (createParams == null ? "" : createParams);
                    st = cx.createStatement();
                    st.execute(sql);
                } catch (SQLException e) {
                    throw new IOException("Failed to create the target database", e);
                } finally {
                    store.closeSafe(st);
                    store.closeSafe(cx);
                }

                // if we got here the database has been created, now verify it has the postgis
                // extensions
                // and eventually try to create them
                ResultSet rs = null;
                try {
                    String url = "jdbc:postgresql" + "://" + host + ":" + port + "/" + db;
                    cx = DriverManager.getConnection(url, user, password);

                    // check we have postgis
                    st = cx.createStatement();
                    try {
                        rs = st.executeQuery("select PostGIS_version()");
                    } catch (SQLException e) {
                        // not available eh? create it
                        st.execute("create extension postgis");
                    } finally {
                        store.closeSafe(rs);
                    }
                } catch (SQLException e) {
                    throw new IOException("Failed to create the target database", e);
                } finally {
                    store.closeSafe(st);
                    store.closeSafe(cx);
                }

                // and finally re-create the connection pool
                ds = super.createDataSource(params, dialect);
            }
        }

        return ds;
    }

    private Connection getConnection(String user, String password, String url) throws SQLException {
        Connection cx;
        if (user != null) {
            cx = DriverManager.getConnection(url, user, password);
        } else {
            cx = DriverManager.getConnection(url);
        }
        return cx;
    }

    /**
     * Drops the database specified in the connection params. The database must not be in use, and
     * the user must have the necessary privileges
     */
    public void dropDatabase(Map<String, Object> params) throws IOException {
        JDBCDataStore store = new JDBCDataStore();
        // get the connection params
        String host = (String) HOST.lookUp(params);
        int port = (Integer) PORT.lookUp(params);
        String db = (String) DATABASE.lookUp(params);
        String user = (String) USER.lookUp(params);
        String password = (String) PASSWD.lookUp(params);

        Connection cx = null;
        Statement st = null;
        try {
            // connect to template1 instead
            String url = "jdbc:postgresql" + "://" + host + ":" + port + "/template1";
            cx = getConnection(user, password, url);

            // drop the database
            String sql = "DROP DATABASE \"" + db + "\"";
            st = cx.createStatement();
            st.execute(sql);
        } catch (SQLException e) {
            throw new IOException("Failed to drop the target database", e);
        } finally {
            store.closeSafe(st);
            store.closeSafe(cx);
        }
    }
}
