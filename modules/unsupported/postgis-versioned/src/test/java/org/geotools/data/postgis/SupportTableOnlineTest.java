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
package org.geotools.data.postgis;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.geotools.data.jdbc.JDBCUtils;

public class SupportTableOnlineTest extends AbstractVersionedPostgisDataTestCase {

    public SupportTableOnlineTest(String name) {
        super(name);
    }

    public void testTableCreation() throws IOException, SQLException {
        buildDataStore();
        // now the tables should be there
        Connection conn = null;
        ResultSet tables = null;
        try {
            boolean changeSets = false;
            boolean tablesChanged = false;
            boolean versionedTables = false;
            conn = pool.getConnection();
            DatabaseMetaData meta = conn.getMetaData();
            String[] tableType = { "TABLE" };
            tables = meta.getTables(null, f.schema, "%", tableType);
            while (tables.next()) {
                String tableName = tables.getString(3);
                if (tableName.equals(VersionedPostgisDataStore.TBL_CHANGESETS))
                    changeSets = true;
                if (tableName.equals(VersionedPostgisDataStore.TBL_TABLESCHANGED))
                    tablesChanged = true;
                if (tableName.equals(VersionedPostgisDataStore.TBL_VERSIONEDTABLES))
                    versionedTables = true;
            }
            tables.close();

            assertTrue(changeSets);
            assertTrue(tablesChanged);
            assertTrue(versionedTables);
        } finally {
            JDBCUtils.close(tables);
            JDBCUtils.close(conn, null, null);
        }
    }

    public void testPreexistentTables() throws IOException, SQLException {
        // first call should create them, second one should not fail
        buildDataStore();
        buildDataStore();
    }

    public void testTablesInTheWay() throws IOException, SQLException {
        Connection conn = null;
        Statement st = null;
        try {
            conn = pool.getConnection();
            st = conn.createStatement();
            st.execute("CREATE TABLE " + VersionedPostgisDataStore.TBL_CHANGESETS
                    + "(ID SERIAL, STUFF VARCHAR(20))");
            try {
                buildDataStore();
                fail("Should have failed because of the pre-existing but alone version support table");
            } catch (IOException e) {
                // ok
            }
            SqlTestUtils.dropTable(pool, VersionedPostgisDataStore.TBL_CHANGESETS, true);

            st.execute("CREATE TABLE "
                    + VersionedPostgisDataStore.TBL_TABLESCHANGED
                    + "(ID SERIAL, STUFF VARCHAR(20))");
            try {
                buildDataStore();
                fail("Should have failed because of the pre-existing but alone version support table");
            } catch (IOException e) {
                // ok
            }
            SqlTestUtils.dropTable(pool, VersionedPostgisDataStore.TBL_TABLESCHANGED, false);

            st.execute("CREATE TABLE "
                    + VersionedPostgisDataStore.TBL_VERSIONEDTABLES
                    + "(ID SERIAL, STUFF VARCHAR(20))");
            try {
                buildDataStore();
                fail("Should have failed because of the pre-existing but alone version support table");
            } catch (IOException e) {
                // ok
            }
            SqlTestUtils.dropTable(pool, VersionedPostgisDataStore.TBL_VERSIONEDTABLES, true);
        } finally {
            JDBCUtils.close(st);
            JDBCUtils.close(conn, null, null);
        }
    }
    
}
