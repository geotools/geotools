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
package org.geotools.data.oracle;

import java.util.HashMap;
import java.util.Properties;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.jdbc.JDBCConnectionLifecycleOnlineTest;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCTestSetup;

public class OracleConnectionLifecycleOnlineTest extends JDBCConnectionLifecycleOnlineTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new OracleTestSetup();
    }

    public void testLifeCycleDoubleUnwrap() {
        try {
            // Use startup SQL when connecting so the connection is
            // doubly wrapped (adding LifeCycleConnection).
            // That tests ability of OracleDialect to unwrap properly.
            Properties addStartupSql = (Properties) fixture.clone();
            addStartupSql.setProperty(JDBCDataStoreFactory.SQL_ON_BORROW.key,
                    "select sysdate from dual");
            HashMap params = createDataStoreFactoryParams();
            params.putAll(addStartupSql);
            DataStore withWrap = (JDBCDataStore) DataStoreFinder.getDataStore(params);
            if (withWrap == null) {
                throw new RuntimeException("Failed to create DataStore with startup sql");
            }
            withWrap.dispose();
        } catch (Exception e) {
            throw new RuntimeException("Connection unwrap test failed", e);
        }
    }
}
