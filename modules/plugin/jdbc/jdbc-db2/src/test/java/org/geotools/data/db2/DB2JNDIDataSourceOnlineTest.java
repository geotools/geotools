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

package org.geotools.data.db2;

import java.util.List;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCJNDIDataSourceOnlineTest;
import org.geotools.jdbc.JDBCJNDIDataStoreFactory;
import org.geotools.jdbc.JDBCJNDITestSetup;

public class DB2JNDIDataSourceOnlineTest extends JDBCJNDIDataSourceOnlineTest {

    @Override
    protected JDBCJNDITestSetup createTestSetup() {
        return new JDBCJNDITestSetup(new DB2TestSetup());
    }

    @Override
    /** Make sure the JNDI factory exposes all the extra params that the non JNDI one exposes */
    public void testExtraParams() {
        List<String> baseParams = getBaseParams();
        List<String> standardParams = getParamKeys(getDataStoreFactory());
        standardParams.remove(JDBCDataStoreFactory.VALIDATECONN.key);
        standardParams.remove(JDBCDataStoreFactory.MAX_OPEN_PREPARED_STATEMENTS.key);
        standardParams.removeAll(baseParams);
        List<String> baseJndiParams = getBaseJNDIParams();
        List<String> jndiParams = getParamKeys(getJNDIStoreFactory());
        assertTrue(jndiParams.contains(JDBCDataStoreFactory.FETCHSIZE.key));
        assertTrue(jndiParams.contains(JDBCDataStoreFactory.BATCH_INSERT_SIZE.key));
        jndiParams.removeAll(baseJndiParams);
        assertEquals(standardParams, jndiParams);
    }

    @Override
    protected JDBCJNDIDataStoreFactory getJNDIStoreFactory() {
        return new DB2NGJNDIDataStoreFactory();
    }

    @Override
    protected JDBCDataStoreFactory getDataStoreFactory() {
        return new DB2NGDataStoreFactory();
    }
}
