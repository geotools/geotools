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
package org.geotools.data.jdbc.ds;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.data.jdbc.datasource.DBCPDataSourceFactory;
import org.geotools.data.jdbc.datasource.DataSourceFinder;

public class DataSourceFinderTest extends TestCase {
    public void testDbcpFactory() throws IOException {
        assertTrue(new DBCPDataSourceFactory().isAvailable());
        DataSourceFinder.scanForPlugins();
        
        Map map = new HashMap();
        map.put(DBCPDataSourceFactory.DSTYPE.key, "DBCP");
        map.put(DBCPDataSourceFactory.DRIVERCLASS.key, "org.h2.Driver");
        map.put(DBCPDataSourceFactory.JDBC_URL.key, "jdbc:h2:mem:test_mem");
        map.put(DBCPDataSourceFactory.USERNAME.key, "admin");
        map.put(DBCPDataSourceFactory.PASSWORD.key, "");
        map.put(DBCPDataSourceFactory.MAXACTIVE.key, new Integer(10));
        map.put(DBCPDataSourceFactory.MAXIDLE.key, new Integer(0));
        
        DataSource source =  DataSourceFinder.getDataSource(map);
        assertNotNull(source);
        assertTrue(source instanceof BasicDataSource);
    }
    
//    public void testJNDIFactory() throws Exception {
        // can't make this work... there are dependencies from EJBMock to stuff
        // that's not in the maven repos
        
//        EJBMockObjectFactory ejbMock = new EJBMockObjectFactory();
//        ejbMock.initMockContextFactory();
//        Context mockContext = new MockContext();
//        InitialContext context = new InitialContext();
//        DataSource mockDataSource = new MockDataSource();
//        context.rebind("jdbc/pool", mockDataSource);
//        JNDI.init(context);
//        
//        assertTrue(new JNDIDataSourceFactory().isAvailable());
//        DataSourceFinder.scanForPlugins();
//        
//        
//        
//        Map map = new HashMap();
//        map.put(JNDIDataSourceFactory.JNDI_REFNAME.key, "jdbc/pool");
//        
//        DataSource source =  DataSourceFinder.getDataSource(map);
//        assertNotNull(source);
//        assertEquals(mockDataSource, source);
//    }
}
