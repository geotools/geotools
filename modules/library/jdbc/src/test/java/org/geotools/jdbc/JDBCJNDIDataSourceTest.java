/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.geotools.data.DataAccessFactory.Param;
import org.geotools.data.DataStoreFinder;

/**
 * @author Christian Mueller
 * 
 * Abstract test class for getting a jdbc data source via JNDI lookup
 * 
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
        assertNotNull("failed to find DataStore",dataStore);
        Connection con = dataStore.getDataSource().getConnection();
        assertNotNull("No Connection returned",con );
        assertFalse(con.isClosed());

        dataStore.closeSafe(con);

    }
    
    /**
     * Make sure the JNDI factory exposes all the extra params that the non JNDI one exposes
     */
    public void testExtraParams() {
        List<String> baseParams = getBaseParams();
        List<String> standardParams = getParamKeys(getDataStoreFactory());
        standardParams.remove(JDBCDataStoreFactory.VALIDATECONN.key);
        standardParams.remove(JDBCDataStoreFactory.MAX_OPEN_PREPARED_STATEMENTS.key);
        standardParams.removeAll(baseParams);
        List<String> baseJndiParams = getBaseJNDIParams();
        List<String> jndiParams = getParamKeys(getJNDIStoreFactory());
        assertTrue(jndiParams.contains(JDBCDataStoreFactory.FETCHSIZE.key));
        jndiParams.removeAll(baseJndiParams);
        assertEquals(standardParams, jndiParams);
    }

    protected abstract JDBCJNDIDataStoreFactory getJNDIStoreFactory();

    protected abstract JDBCDataStoreFactory getDataStoreFactory();

    /**
     * Extracts the base params all non JNDI JDBC factories have 
     * @return
     */
    protected List<String> getBaseParams() {
        JDBCDataStoreFactory factory = getBaseFactory();

        return getParamKeys(factory);
    }

    protected JDBCDataStoreFactory getBaseFactory() {
        JDBCDataStoreFactory factory = new JDBCDataStoreFactory() {

            @Override
            public String getDescription() {
                // nothing to do here
                return null;
            }

            @Override
            protected String getValidationQuery() {
                // nothing to do here
                return null;
            }

            @Override
            protected String getDriverClassName() {
                // nothing to do here
                return null;
            }

            @Override
            protected String getDatabaseID() {
                // nothing to do here
                return null;
            }

            @Override
            protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
                // nothing to do here
                return null;
            }
        };
        return factory;
    }
    
    /**
     * Extracts the common params all JNDI factories have
     * @return
     */
    protected List<String> getBaseJNDIParams() {
        JDBCJNDIDataStoreFactory factory = new JDBCJNDIDataStoreFactory(getBaseFactory()) {
            
        };
        return getParamKeys(factory);
    }

    /**
     * Extracts the set of params available for a given factory
     * @param factory
     * @return
     */
    protected List<String> getParamKeys(JDBCDataStoreFactory factory) {
        Param[] params = factory.getParametersInfo();
        List<String> results = new ArrayList<String>();
        for (Param p : params) {
            results.add(p.key);
        }

        return results;
    }

}
