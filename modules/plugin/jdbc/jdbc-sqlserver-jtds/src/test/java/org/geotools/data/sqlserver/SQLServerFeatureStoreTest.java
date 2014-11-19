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
package org.geotools.data.sqlserver;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.geotools.data.Transaction;
import org.geotools.jdbc.JDBCFeatureStoreTest;
import org.geotools.jdbc.JDBCTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class SQLServerFeatureStoreTest extends JDBCFeatureStoreTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new SQLServerTestSetup();
    }
    
    @Override
    public void testAddInTransaction() throws IOException {
        // does not work, see GEOT-2832
    }
    
    public void testAddFeaturesUseProvidedFid() throws IOException {
        // cannot work in general since the primary column is an identity:
        // - it is not possible to insert into an indentity column unless the IDENTITY_INSERT
        //   property is set on it
        // - however if IDENTITY_INSERT is setup, then the column stops generating values and
        //   requires one to insert values manually, which breaks other tests
    }
    
    @Override
    public void testExternalConnection() throws IOException, SQLException {
        // MVCC is not enabled by default in SQL Server, to do that one has to 
        // use something like:
        // ALTER DATABASE ' + DB_NAME() + ' SET SINGLE_USER WITH ROLLBACK IMMEDIATE ;
        // ALTER DATABASE ' + DB_NAME() + ' SET READ_COMMITTED_SNAPSHOT ON;
        // ALTER DATABASE ' + DB_NAME() + ' SET MULTI_USER;'
        // However this requires having admin access to the database, so we cannot
        // assume we can run it, thus we just check if it's possible at all
        // When the above is set the test passes, verified against SQL Server 2008
        
        Connection cx = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            cx = dataStore.getConnection(Transaction.AUTO_COMMIT);
            st = cx.createStatement();
            rs = st.executeQuery("SELECT is_read_committed_snapshot_on FROM sys.databases WHERE name= db_name()");
            if(rs.next()) {
                if(rs.getBoolean(1)) {
                    super.testExternalConnection();
                }
            }
        } finally {
            dataStore.closeSafe(rs);
            dataStore.closeSafe(st);
            dataStore.closeSafe(cx);
        }
        
    }

}
