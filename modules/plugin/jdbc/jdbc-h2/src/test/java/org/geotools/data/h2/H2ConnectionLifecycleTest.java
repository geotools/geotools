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
package org.geotools.data.h2;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.jdbc.ConnectionLifecycleListener;
import org.geotools.jdbc.JDBCConnectionLifecycleOnlineTest;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.VirtualTable;
import org.junit.Test;

public class H2ConnectionLifecycleTest extends JDBCConnectionLifecycleOnlineTest {

    private static class SetVariableListener implements ConnectionLifecycleListener {

        double value;

        @Override
        public void onBorrow(JDBCDataStore store, Connection cx) throws SQLException {
            Statement st = null;
            try {
                st = cx.createStatement();
                st.execute("SET @MYVAR = " + value);
            } finally {
                store.closeSafe(st);
            }
        }

        @Override
        public void onRelease(JDBCDataStore store, Connection cx) throws SQLException {
            // nothing to do
        }

        @Override
        public void onCommit(JDBCDataStore store, Connection cx) throws SQLException {
            // nothing to do
        }

        @Override
        public void onRollback(JDBCDataStore store, Connection cx) throws SQLException {
            // nothing to do
        }
    }

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new H2TestSetup();
    }

    @Test
    public void testVariableListener() throws Exception {
        // setup a virtual table using the user variable
        VirtualTable vt =
                new VirtualTable("ft1var", "select * from  \"geotools\".\"ft1\" where \"doubleProperty\" > @MYVAR");
        dataStore.createVirtualTable(vt);

        // setup a listener that uses said variable
        SetVariableListener listener = new SetVariableListener();
        dataStore.getConnectionLifecycleListeners().add(listener);

        // set the value and test
        listener.value = 1.0;
        SimpleFeatureSource fs = dataStore.getFeatureSource("ft1var");
        assertEquals(2, fs.getCount(Query.ALL));

        listener.value = 10;
        assertEquals(0, fs.getCount(Query.ALL));
    }
}
