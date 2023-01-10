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
package org.h2gis.geotools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.jdbc.datasource.ManageableDataSource;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.h2.tools.Server;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class H2GISDataStoreFactoryTest {
    static H2GISDataStoreFactory factory;
    static Map<String, Object> params;

    @BeforeAll
    static void setUp() {
        factory = new H2GISDataStoreFactory();
        params = new HashMap<>();
        params.put(JDBCDataStoreFactory.NAMESPACE.key, "http://www.geotools.org/test");
        params.put(JDBCDataStoreFactory.DATABASE.key, "geotools");
        params.put(JDBCDataStoreFactory.DBTYPE.key, "h2gis");
    }

    @Test
    public void testCanProcess() {
        assertFalse(factory.canProcess(Collections.emptyMap()));
        assertTrue(factory.canProcess(params));
    }

    @Test
    public void testCreateDataStore() throws Exception {
        JDBCDataStore ds = null;
        try {
            ds = factory.createDataStore(params);
            assertNotNull(ds);
            assertTrue(ds.getDataSource() instanceof ManageableDataSource);
        } finally {
            if (ds != null) {
                ds.dispose();
            }
        }
    }

    @Disabled // TODO : FIXME
    @Test
    public void testTCP() throws Exception {
        // will fail on GitHub linux build, due to TCP port opening
        Assumptions.assumeFalse(Boolean.getBoolean("linux-github-build"));

        Map<String, Object> params = new HashMap<>();
        params.put(H2GISDataStoreFactory.HOST.key, "localhost");
        params.put(H2GISDataStoreFactory.DATABASE.key, "./geotools");
        params.put(H2GISDataStoreFactory.USER.key, "geotools");
        params.put(H2GISDataStoreFactory.PASSWD.key, "geotools");

        DataStore ds = null;
        try {
            ds = factory.createDataStore(params);
            try {
                ds.getTypeNames();
                fail("Should not have made a connection.");
            } catch (Exception ok) {
            }
        } finally {
            if (ds != null) {
                ds.dispose();
            }
        }

        Server server = Server.createTcpServer(new String[] {"-baseDir", "target"});
        server.start();
        try {
            while (!server.isRunning(false)) {
                Thread.sleep(100);
            }
            ds = null;
            try {
                ds = factory.createDataStore(params);
                ds.getTypeNames();
            } finally {
                if (ds != null) {
                    ds.dispose();
                }
            }
        } finally {
            server.shutdown();
        }
    }

    @Test
    public void testDefaultFetchSizeDataStore() throws Exception {
        JDBCDataStore ds = null;
        try {
            assertNull(params.get(H2GISDataStoreFactory.FETCHSIZE.key));
            ds = factory.createDataStore(params);
            assertNotNull(ds);
            assertEquals(1000, ds.getFetchSize());
        } finally {
            if (ds != null) {
                ds.dispose();
            }
        }
    }
}
