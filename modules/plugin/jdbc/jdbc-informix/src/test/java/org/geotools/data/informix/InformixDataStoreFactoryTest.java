/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.informix;

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.geotools.jdbc.JDBCDataStoreFactory;

public class InformixDataStoreFactoryTest extends TestCase {
    InformixDataStoreFactory factory;

    @Override
    protected void setUp() {
        factory = new InformixDataStoreFactory();
    }

    public void testCanProcess() {
        Map<String, Object> params = new HashMap<>();
        assertFalse(factory.canProcess(params));

        params.put(JDBCDataStoreFactory.NAMESPACE.key, "http://www.geotools.org/test");
        params.put(JDBCDataStoreFactory.DATABASE.key, "geotools");
        params.put(JDBCDataStoreFactory.DBTYPE.key, "informix-sqli");

        params.put(JDBCDataStoreFactory.HOST.key, "localhost");
        params.put(JDBCDataStoreFactory.PORT.key, "9088");
        params.put(JDBCDataStoreFactory.USER.key, "informix");
        params.put("jdbcUrl", "jdbc:informix-sqli://localhost:9088/geotest");
        assertTrue(factory.canProcess(params));
    }
}
