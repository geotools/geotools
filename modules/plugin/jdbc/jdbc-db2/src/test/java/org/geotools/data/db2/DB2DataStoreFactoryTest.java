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

import junit.framework.TestCase;

import org.geotools.jdbc.JDBCDataStoreFactory;


public class DB2DataStoreFactoryTest extends TestCase {
    DB2NGDataStoreFactory factory;

    protected void setUp() throws Exception {
        factory = new DB2NGDataStoreFactory();
    }

    public void testCanProcess() throws Exception {
        HashMap params = new HashMap();
        assertFalse(factory.canProcess(params));

        //params.put(JDBCDataStoreFactory.NAMESPACE.key, "http://www.geotools.org/test");
        params.put(JDBCDataStoreFactory.DATABASE.key, "geotools");
        params.put(JDBCDataStoreFactory.DBTYPE.key, "db2");

        params.put(JDBCDataStoreFactory.USER.key, "db2inst1");
        params.put(JDBCDataStoreFactory.PASSWD.key, "db2inst1");
        assertEquals("jdbc:db2:geotools",factory.getJDBCUrl(params) );
        
        params.put(JDBCDataStoreFactory.HOST.key, "localhost");
        params.put(JDBCDataStoreFactory.PORT.key, "50001");                
        assertEquals("jdbc:db2://localhost:50001/geotools",factory.getJDBCUrl(params) );
        
        assertTrue(factory.canProcess(params));
    }
}
