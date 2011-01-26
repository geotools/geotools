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
package org.geotools.data.sqlserver;

import static org.geotools.jdbc.JDBCDataStoreFactory.DATABASE;
import static org.geotools.jdbc.JDBCDataStoreFactory.DBTYPE;
import static org.geotools.jdbc.JDBCDataStoreFactory.HOST;
import static org.geotools.jdbc.JDBCDataStoreFactory.PASSWD;
import static org.geotools.jdbc.JDBCDataStoreFactory.PORT;
import static org.geotools.jdbc.JDBCDataStoreFactory.USER;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;

public class SQLServerDataStoreFactoryTest extends JDBCTestSupport {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new SQLServerTestSetup();
    }
    
    public void testCreateDataStore() throws Exception {
        checkConnection(false);
    }
    
    public void testCreateDataStoreWithDatabase() throws Exception {
        checkConnection(true);
    }
    
    void checkConnection(boolean includedb) throws Exception {
        Properties db = new Properties();
        db.load(getClass().getResourceAsStream("factory.properties"));
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(HOST.key, db.getProperty(HOST.key));
        if (includedb) {
            params.put(DATABASE.key, db.getProperty(DATABASE.key));  
        }
        params.put(PORT.key, db.getProperty(PORT.key));
        params.put(USER.key, db.getProperty(USER.key));
        params.put(PASSWD.key, db.getProperty(PASSWD.key));

        SQLServerDataStoreFactory factory = new SQLServerDataStoreFactory();
        params.put(DBTYPE.key, factory.getDatabaseID());
        
        assertTrue(factory.canProcess(params));
        
        JDBCDataStore store = factory.createDataStore(params);
        assertNotNull(store);
        try {
            // check dialect
            assertTrue(store.getSQLDialect() instanceof SQLServerDialect);
            
            // force connection usage
            assertNotNull(store.getSchema("ft1"));
        } finally {
            store.dispose();
        }
    }


}
