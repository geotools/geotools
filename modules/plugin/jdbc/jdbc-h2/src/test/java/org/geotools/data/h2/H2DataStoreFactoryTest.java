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

import java.util.Collections;
import java.util.HashMap;

import junit.framework.TestCase;

import org.geotools.data.DataStore;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.h2.tools.Server;


public class H2DataStoreFactoryTest extends TestCase {
    H2DataStoreFactory factory;
    HashMap params;
    
    protected void setUp() throws Exception {
        factory = new H2DataStoreFactory();
        params = new HashMap();
        params.put(JDBCDataStoreFactory.NAMESPACE.key, "http://www.geotools.org/test");
        params.put(JDBCDataStoreFactory.DATABASE.key, "geotools");
        params.put(JDBCDataStoreFactory.DBTYPE.key, "h2");
    }

    public void testCanProcess() throws Exception {
        assertFalse(factory.canProcess(Collections.EMPTY_MAP));
        assertTrue(factory.canProcess(params));
    }
    
    public void testCreateDataStore() throws Exception {
        JDBCDataStore ds = factory.createDataStore( params );
        assertNotNull( ds );
    }
    
    public void testTCP() throws Exception {
        HashMap params = new HashMap();
        params.put(H2DataStoreFactory.HOST.key, "localhost");
        params.put(H2DataStoreFactory.DATABASE.key, "geotools");
        params.put(H2DataStoreFactory.USER.key, "geotools");
        params.put(H2DataStoreFactory.PASSWD.key, "geotools");
        
        DataStore ds = factory.createDataStore(params);
        try {
            ds.getTypeNames();
            fail("Should not have made a connection.");
        }
        catch(Exception ok) {}
        
        Server server = Server.createTcpServer(new String[]{"-baseDir", "target"});
        server.start();
        try {
            while(!server.isRunning(false)) {
                Thread.sleep(100);
            }
            
            ds = factory.createDataStore(params);
            ds.getTypeNames();
        }
        finally {
            server.shutdown();
        }
    }
}
