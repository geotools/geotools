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
package org.geotools.data.db2;

import java.util.HashMap;
import java.util.Map;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DB2DataStoreFactoryTest {
    DB2NGDataStoreFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new DB2NGDataStoreFactory();
    }

    @Test
    public void testCanProcess() throws Exception {
        Map<String, Object> params = new HashMap<>();
        Assert.assertFalse(factory.canProcess(params));

        // params.put(JDBCDataStoreFactory.NAMESPACE.key, "http://www.geotools.org/test");
        params.put(JDBCDataStoreFactory.DATABASE.key, "geotools");
        params.put(JDBCDataStoreFactory.DBTYPE.key, "db2");

        params.put(JDBCDataStoreFactory.USER.key, "db2inst1");
        params.put(JDBCDataStoreFactory.PASSWD.key, "db2inst1");
        Assert.assertEquals("jdbc:db2:geotools", factory.getJDBCUrl(params));

        params.put(JDBCDataStoreFactory.HOST.key, "localhost");
        params.put(JDBCDataStoreFactory.PORT.key, "50001");
        Assert.assertEquals("jdbc:db2://localhost:50001/geotools", factory.getJDBCUrl(params));

        params.put(JDBCDataStoreFactory.SCHEMA.key, "db2inst1");
        Assert.assertTrue(factory.canProcess(params));
    }
}
