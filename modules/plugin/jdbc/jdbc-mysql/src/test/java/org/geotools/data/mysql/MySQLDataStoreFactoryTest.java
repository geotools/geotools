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
import java.util.Map;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MySQLDataStoreFactoryTest {
    MySQLDataStoreFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new MySQLDataStoreFactory();
    }

    @Test
    public void testCanProcess() throws Exception {
        Map<String, Object> params = new HashMap<>();
        Assert.assertFalse(factory.canProcess(params));

        params.put(JDBCDataStoreFactory.NAMESPACE.key, "http://www.geotools.org/test");
        params.put(JDBCDataStoreFactory.DATABASE.key, "geotools");
        params.put(JDBCDataStoreFactory.DBTYPE.key, "mysql");

        params.put(JDBCDataStoreFactory.HOST.key, "localhost");
        params.put(JDBCDataStoreFactory.PORT.key, "3306");
        params.put(JDBCDataStoreFactory.USER.key, "mysqluser");
        Assert.assertTrue(factory.canProcess(params));
    }

    /** check fix of possible NPE issue during MySQLVersion56 check (pull request #2033) */
    @Test
    public void testNoNpeOnConnectionFailure() throws Exception {
        // create a dummy JDBC store
        JDBCDataStore store = new JDBCDataStore();
        store.setSQLDialect(new MySQLDialectBasic(store));

        // Connection creation should fail since store does not
        // actually exist. Test ensures that no NPE is thrown
        // from final block of method
        try {
            factory.isMySqlVersion56OrAbove(store);
        } catch (NullPointerException e) {
            Assert.fail("an exception occured during checking of MySQL version " + e);
        }
    }
}
