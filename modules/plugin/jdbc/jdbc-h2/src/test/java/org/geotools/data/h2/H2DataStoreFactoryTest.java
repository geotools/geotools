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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.data.DataStore;
import org.geotools.data.jdbc.datasource.ManageableDataSource;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.h2.tools.Server;
import org.junit.Before;
import org.junit.Test;

public class H2DataStoreFactoryTest {
    H2DataStoreFactory factory;
    HashMap params;

    @Before
    public void setUp() throws Exception {
        factory = new H2DataStoreFactory();
        params = new HashMap();
        params.put(JDBCDataStoreFactory.NAMESPACE.key, "http://www.geotools.org/test");
        params.put(JDBCDataStoreFactory.DATABASE.key, "geotools");
        params.put(JDBCDataStoreFactory.DBTYPE.key, "h2");
    }

    @Test
    public void testCanProcess() throws Exception {
        assertFalse(factory.canProcess(Collections.EMPTY_MAP));
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

    @Test
    public void testCreateDataStoreMVCC() throws Exception {
        Map clonedParams = new HashMap(params);
        clonedParams.put(H2DataStoreFactory.MVCC.key, true);
        JDBCDataStore ds = null;
        try {
            ds = factory.createDataStore(clonedParams);
            assertNotNull(ds);
            final DataSource source = ds.getDataSource();
            assertNotNull(source);
            final DataSource wrapped = source.unwrap(DataSource.class);
            assertNotNull(wrapped);
            if (wrapped instanceof BasicDataSource) {
                final BasicDataSource basicSource = (BasicDataSource) wrapped;
                final String url = basicSource.getUrl();
                assertTrue(url.contains("MVCC=true"));
            }
        } finally {
            if (ds != null) {
                ds.dispose();
            }
        }
    }

    @Test
    public void testTCP() throws Exception {
        HashMap params = new HashMap();
        params.put(H2DataStoreFactory.HOST.key, "localhost");
        params.put(H2DataStoreFactory.DATABASE.key, "geotools");
        params.put(H2DataStoreFactory.USER.key, "geotools");
        params.put(H2DataStoreFactory.PASSWD.key, "geotools");

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
}
