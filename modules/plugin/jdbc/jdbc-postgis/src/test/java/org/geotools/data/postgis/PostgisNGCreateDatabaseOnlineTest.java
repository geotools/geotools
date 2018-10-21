/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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

import static org.geotools.data.postgis.PostgisNGDataStoreFactory.*;
import static org.geotools.jdbc.JDBCDataStoreFactory.DATABASE;
import static org.geotools.jdbc.JDBCDataStoreFactory.DBTYPE;
import static org.geotools.jdbc.JDBCDataStoreFactory.HOST;
import static org.geotools.jdbc.JDBCDataStoreFactory.PASSWD;
import static org.geotools.jdbc.JDBCDataStoreFactory.USER;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.test.OnlineTestCase;

public class PostgisNGCreateDatabaseOnlineTest extends OnlineTestCase {

    private static final String CREATE_DROP_TESTDB = "gt2_create_drop_testdb";

    @Override
    protected boolean isOnline() throws Exception {
        PostgisNGDataStoreFactory factory = new PostgisNGDataStoreFactory();
        JDBCDataStore closer = new JDBCDataStore();
        Class.forName(factory.getDriverClassName());

        // get host and port
        String host = fixture.getProperty(HOST.key);
        String port = fixture.getProperty(PORT.key);
        String user = fixture.getProperty(USER.key);
        String password = fixture.getProperty(PASSWD.key);
        String url = "jdbc:postgresql" + "://" + host + ":" + port + "/template1";

        Connection cx = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            cx = DriverManager.getConnection(url, user, password);
            st = cx.createStatement();
            rs =
                    st.executeQuery(
                            "select rolcreatedb from pg_authid where rolname = '" + user + "'");
            boolean canCreate = false;
            if (rs.next()) {
                canCreate = rs.getBoolean(1);
            }
            rs.close();

            if (!canCreate) {
                System.out.println(
                        "User " + user + " has no database creation options, skipping test");
                return false;
            }

            // creation options available, let's check if we have the postgis extension available
            // then
            rs = st.executeQuery("select * from pg_available_extensions where name = 'postgis'");
            boolean hasPostgisExtension = false;
            if (rs.next()) {
                hasPostgisExtension = true;
            }
            rs.close();

            if (!hasPostgisExtension) {
                System.out.println(
                        "This version of postgresql has no postgis extension available (as in, one that can be used by the \"create extension\" command, skipping it");
                return false;
            }

            // drop the database if available
            rs = st.executeQuery("select * from pg_database where datname = 'create_drop_testdb'");
            boolean databaseExists = rs.next();
            rs.close();
            if (databaseExists) {
                st.execute("drop database " + CREATE_DROP_TESTDB);
            }

            return true;
        } catch (SQLException e) {
            System.out.println(
                    "Failed to check if the user has database creation privileges and postgis is an available extension");
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            return false;
        } finally {
            closer.closeSafe(rs);
            closer.closeSafe(cx);
            closer.closeSafe(st);
        }
    }

    public void testCreateDrop() throws IOException, SQLException, SchemaException {
        PostgisNGDataStoreFactory factory = new PostgisNGDataStoreFactory();
        Properties db = fixture;

        Map<String, Object> params = new HashMap<String, Object>();
        params.put(HOST.key, db.getProperty(HOST.key));
        params.put(DATABASE.key, CREATE_DROP_TESTDB);
        params.put(PORT.key, db.getProperty(PORT.key));
        params.put(USER.key, db.getProperty(USER.key));
        params.put(PASSWD.key, db.getProperty(PASSWD.key));
        params.put(DBTYPE.key, "postgis");
        params.put(PostgisNGDataStoreFactory.CREATE_DB_IF_MISSING.key, true);

        // we can work with it
        assertTrue(factory.canProcess(params));

        // force database creation and check the store functions
        JDBCDataStore store = factory.createDataStore(params);
        assertNotNull(store);
        store.createSchema(
                DataUtilities.createType("test", "id:String,polygonProperty:Polygon:srid=32615"));
        store.getSchema("test");

        // now disconnect and drop
        store.dispose();
        factory.dropDatabase(params);

        // try to connect again, it must fail
        params.remove(PostgisNGDataStoreFactory.CREATE_DB_IF_MISSING.key);
        try {
            store = factory.createDataStore(params);
            store.getTypeNames();
            fail("This one should have failed, the database has just been dropped");
        } catch (Exception e) {
            // fine, it's what we expected
        }
    }

    @Override
    protected String getFixtureId() {
        return "postgis-createdrop";
    }
}
