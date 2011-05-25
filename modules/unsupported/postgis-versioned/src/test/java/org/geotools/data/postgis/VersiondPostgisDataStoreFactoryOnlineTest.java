/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.jdbc.datasource.DataSourceUtil;

/**
 * Test Params used by PostgisDataStoreFactory.
 * 
 * @author aaime
 * @author $Author: jive $ (last modification)
 *
 * @source $URL$
 * @version $Id$
 */
public class VersiondPostgisDataStoreFactoryOnlineTest extends TestCase {
    static VersionedPostgisDataStoreFactory factory
        = new VersionedPostgisDataStoreFactory();
    
    Map remote;
    Map local;
    
    Map remoteWithDataSource;
    DataSource source;

	private PostgisTests.Fixture f;
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
         
    	f = PostgisTests.newFixture("versioned.properties");
		remote = new HashMap();
        remote.put("dbtype","postgis-versioned");        
        remote.put("charset", "");
        remote.put("host",f.host);
        remote.put("port", f.port);
        remote.put("database", f.database);
        remote.put("user", f.user);
        remote.put("passwd", f.password);
        remote.put("namespace", f.namespace);
        
        remoteWithDataSource = new HashMap();
        remoteWithDataSource.putAll(remote);
        String url = "jdbc:postgresql" + "://" + f.host + ":" + f.port + "/" + f.database;
        this.source = DataSourceUtil.buildDefaultDataSource(
        		url, "org.postgresql.Driver", f.user, f.password, "select now()");
        remoteWithDataSource.put("Data Source", source);
        
        super.setUp();
    }

    public void testRemote() throws Exception {
        Map map = remote;
        
        assertEquals( f.database, factory.DATABASE.lookUp(map) );        
        assertEquals( "postgis-versioned", factory.DBTYPE.lookUp(map) );
        assertEquals( f.host, factory.HOST.lookUp(map) );
        assertEquals( f.namespace, factory.NAMESPACE.lookUp(map) );
        assertEquals( f.password, factory.PASSWD.lookUp(map) );
        assertEquals( f.port, factory.PORT.lookUp(map) );
        assertEquals( f.user, factory.USER.lookUp(map) );
        
        assertTrue( "canProcess", factory.canProcess(map));
        try {
            DataStore temp = factory.createDataStore(map);
            assertNotNull( "created", temp );
        }
        catch( DataSourceException expected){
        	assertTrue( expected.getMessage().startsWith("Connection failed:"));
        }               
    }    
    
    public void testRemoteWithDataSource() throws Exception {
        Map map = remoteWithDataSource;
        
        assertEquals( f.database, factory.DATABASE.lookUp(map) );        
        assertEquals( "postgis-versioned", factory.DBTYPE.lookUp(map) );
        assertEquals( f.host, factory.HOST.lookUp(map) );
        assertEquals( f.namespace, factory.NAMESPACE.lookUp(map) );
        assertEquals( f.password, factory.PASSWD.lookUp(map) );
        assertEquals( f.port, factory.PORT.lookUp(map) );
        assertEquals( f.user, factory.USER.lookUp(map) );
        assertEquals(this.source, factory.DATASOURCE.lookUp(map));
        
        assertTrue( "canProcess", factory.canProcess(map));
        try {
            DataStore temp = factory.createDataStore(map);
            assertNotNull( "created", temp );
        }
        catch( DataSourceException expected){
        	assertTrue( expected.getMessage().startsWith("Connection failed:"));
        }               
    }   
    
    public void testLookup() throws Exception {
        DataStore ds = DataStoreFinder.getDataStore(remote);
        assertNotNull(ds);
        assertTrue(ds instanceof VersionedPostgisDataStore);
    }
    
    public void testLookupWithDataSource() throws Exception {
        DataStore ds = DataStoreFinder.getDataStore(remoteWithDataSource);
        assertNotNull(ds);
        assertTrue(ds instanceof VersionedPostgisDataStore);
    }
    
    public void testVersioned() throws Exception {
        remote.put("version enable all", Boolean.TRUE);
        VersionedPostgisDataStore ds = (VersionedPostgisDataStore) DataStoreFinder.getDataStore(remote);
        assertTrue(ds.isVersioned("road"));
        assertTrue(ds.isVersioned("river"));
    }
    
    public void testVersionedWithDataSource() throws Exception {
        remote.put("version enable all", Boolean.TRUE);
        VersionedPostgisDataStore ds = (VersionedPostgisDataStore) DataStoreFinder.getDataStore(remoteWithDataSource);
        assertTrue(ds.isVersioned("road"));
        assertTrue(ds.isVersioned("river"));
    }
}
