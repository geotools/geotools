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

package org.geotools.data.oracle;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCJNDIDataSourceOnlineTest;
import org.geotools.jdbc.JDBCJNDITestSetup;

public class OracleJNDIDataSourceOnlineTest extends JDBCJNDIDataSourceOnlineTest {

    @Override
    protected JDBCJNDITestSetup createTestSetup() {
        return new JDBCJNDITestSetup(new OracleTestSetup());
    }

    @Override
    protected OracleNGJNDIDataStoreFactory getJNDIStoreFactory() {
        return new OracleNGJNDIDataStoreFactory();
    }

    @Override
    protected OracleNGDataStoreFactory getDataStoreFactory() {
        return new OracleNGDataStoreFactory();
    }

    /** Make sure the JNDI factory exposes all the extra params that the non JNDI one exposes */
    @Override
    public void testExtraParams() {
        List<String> baseParams = getBaseParams();
        List<String> standardParams = getParamKeys(getDataStoreFactory());
        standardParams.remove(JDBCDataStoreFactory.VALIDATECONN.key);
        standardParams.remove(JDBCDataStoreFactory.MAX_OPEN_PREPARED_STATEMENTS.key);
        standardParams.remove(OracleNGDataStoreFactory.LOGIN_TIMEOUT.key);
        standardParams.remove(OracleNGDataStoreFactory.CONNECTION_TIMEOUT.key);
        standardParams.remove(OracleNGDataStoreFactory.OUTBOUND_CONNECTION_TIMEOUT.key);
        standardParams.removeAll(baseParams);
        List<String> baseJndiParams = getBaseJNDIParams();
        List<String> jndiParams = getParamKeys(getJNDIStoreFactory());
        jndiParams.removeAll(baseJndiParams);
        assertEquals(standardParams, jndiParams);
    }
}
