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

package org.geotools.jdbc;

import java.sql.Connection;
import java.util.HashMap;

import org.geotools.data.DataStoreFinder;

/**
 * @author Christian Mueller
 * 
 * Abstract test class for getting a jdbc data source via JNDI lookup
 * 
 *
 *
 * @source $URL$
 */
public abstract class JDBCJNDIDataSourceTest extends JDBCTestSupport {

    @Override
    protected abstract JDBCJNDITestSetup createTestSetup();
    
    public void testJNDIDataSource() throws Exception {

        ((JDBCJNDITestSetup)setup).setupJNDIEnvironment();
        
        HashMap params = new HashMap();

        String dbtype = setup.createDataStoreFactory().getDatabaseID();
        params.put(JDBCDataStoreFactory.NAMESPACE.key, "http://www.geotools.org/test");
        params.put(JDBCDataStoreFactory.DBTYPE.key, dbtype);
        params.put(JDBCJNDIDataStoreFactory.JNDI_REFNAME.key, "ds");

        JDBCDataStore dataStore = (JDBCDataStore) DataStoreFinder.getDataStore(params);
        Connection con = dataStore.getDataSource().getConnection();
        assertTrue(con != null);
        assertFalse(con.isClosed());

        dataStore.closeSafe(con);

    }

}
