/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2022, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.informix;

import static org.junit.Assert.assertTrue;

import java.util.List;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCJNDIDataSourceOnlineTest;
import org.geotools.jdbc.JDBCJNDIDataStoreFactory;
import org.geotools.jdbc.JDBCJNDITestSetup;

public class InformixJNDIDataSourceOnlineTest extends JDBCJNDIDataSourceOnlineTest {

    @Override
    protected JDBCJNDITestSetup createTestSetup() {
        return new JDBCJNDITestSetup(new InformixTestSetup());
    }

    @Override
    protected JDBCJNDIDataStoreFactory getJNDIStoreFactory() {
        return new InformixJNDIDataStoreFactory();
    }

    @Override
    protected JDBCDataStoreFactory getDataStoreFactory() {
        return new InformixDataStoreFactory();
    }

    /** Make sure the JNDI factory exposes all the extra params that the non JNDI one exposes */
    @Override
    public void testExtraParams() {
        List<String> baseParams = getBaseParams();
        List<String> standardParams = getParamKeys(getDataStoreFactory());
        standardParams.remove(InformixDataStoreFactory.VALIDATECONN.key);
        standardParams.remove(InformixDataStoreFactory.MAX_OPEN_PREPARED_STATEMENTS.key);
        standardParams.remove(InformixDataStoreFactory.JDBC_URL.key);
        standardParams.removeAll(baseParams);
        List<String> baseJndiParams = getBaseJNDIParams();
        List<String> jndiParams = getParamKeys(getJNDIStoreFactory());
        assertTrue(jndiParams.contains(InformixDataStoreFactory.FETCHSIZE.key));
        assertTrue(jndiParams.contains(InformixDataStoreFactory.BATCH_INSERT_SIZE.key));
        jndiParams.removeAll(baseJndiParams);
    }
}
