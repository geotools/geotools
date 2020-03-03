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
package org.geotools.data.mysql;

import java.util.HashMap;
import junit.framework.TestCase;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;

public class MySQLDataStoreFactoryTest extends TestCase {
    MySQLDataStoreFactory factory;

    protected void setUp() throws Exception {
        factory = new MySQLDataStoreFactory();
    }

    public void testCanProcess() throws Exception {
        HashMap params = new HashMap();
        assertFalse(factory.canProcess(params));

        params.put(JDBCDataStoreFactory.NAMESPACE.key, "http://www.geotools.org/test");
        params.put(JDBCDataStoreFactory.DATABASE.key, "geotools");
        params.put(JDBCDataStoreFactory.DBTYPE.key, "mysql");

        params.put(JDBCDataStoreFactory.HOST.key, "localhost");
        params.put(JDBCDataStoreFactory.PORT.key, "3306");
        params.put(JDBCDataStoreFactory.USER.key, "mysqluser");
        assertTrue(factory.canProcess(params));
    }

    /** check fix of possible NPE issue during MySQLVersion56 check (pull request #2033) */
    public void testNoNpeOnConnectionFailure() throws Exception {
        // create a dummy JDBC store
        JDBCDataStore store = new JDBCDataStore();
        store.setSQLDialect(new MySQLDialectBasic(store));

        // Connection creation should fail since store does not
        // actually exist. Test ensures that no NPE is thrown
        // from final block of method
        try {
            factory.isMySqlVersion56(store);
        } catch (NullPointerException e) {
            fail("an exception occured during checking of MySQL version " + e);
        }
    }
}
