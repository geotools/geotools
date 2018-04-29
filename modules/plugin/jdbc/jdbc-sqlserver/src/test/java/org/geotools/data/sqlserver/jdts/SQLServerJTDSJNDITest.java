/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.sqlserver.jdts;

import static junit.framework.TestCase.fail;

import java.io.IOException;
import java.util.Properties;
import org.geotools.data.DataStoreFinder;
import org.geotools.jdbc.JDBCDataStore;
import org.junit.Before;
import org.junit.Test;

public class SQLServerJTDSJNDITest {

    private final Properties jndiProps = new Properties();

    private static final String JNDINAME = "java:comp/env/jdbc/geotools";

    @Before
    public void setUp() {
        jndiProps.put("jndiReferenceName", JNDINAME);
        jndiProps.put("schema", "dbo");
    }

    /**
     * This test should fail with an IOException because there is actually no such JNDI to connect
     * to, but the factory lookup will have succeeded before throwing the exception.
     *
     * @throws IOException always with the message: "Cannot find JNDI data source:
     *     java:comp/env/jdbc/geotools"
     */
    @Test(expected = IOException.class)
    public void testCanGetJTDSdataStore() throws IOException {
        jndiProps.put("dbtype", "jtds-sqlserver");

        // this will fail with the message: "Cannot find JNDI data source:
        // java:comp/env/jdbc/geotools"
        JDBCDataStore dataStore = (JDBCDataStore) DataStoreFinder.getDataStore(jndiProps);
        fail("Expected exception was not thrown");
    }

    /**
     * This test should fail with an IOException because there is actually no such JNDI to connect
     * to, but the factory lookup will have succeeded before * throwing the exception.
     *
     * @throws IOException always with the message: "Cannot find JNDI data source:
     *     java:comp/env/jdbc/geotools"
     */
    @Test(expected = IOException.class)
    public void testCanGetMSdataStore() throws IOException {
        jndiProps.put("dbtype", "sqlserver");

        // this will fail with the message: "Cannot find JNDI data source:
        // java:comp/env/jdbc/geotools"
        JDBCDataStore dataStore = (JDBCDataStore) DataStoreFinder.getDataStore(jndiProps);
        fail("Expected exception was not thrown");
    }
}
