/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana;

import java.util.List;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCJNDIDataSourceOnlineTest;
import org.geotools.jdbc.JDBCJNDIDataStoreFactory;
import org.geotools.jdbc.JDBCJNDITestSetup;

/** @author Stefan Uhrig, SAP SE */
public class HanaJNDIDataSourceOnlineTest extends JDBCJNDIDataSourceOnlineTest {

    protected JDBCJNDITestSetup createTestSetup() {
        return new JDBCJNDITestSetup(new HanaTestSetup());
    }

    @Override
    protected JDBCJNDIDataStoreFactory getJNDIStoreFactory() {
        return new HanaJNDIDataStoreFactory();
    }

    @Override
    protected JDBCDataStoreFactory getDataStoreFactory() {
        return new HanaDataStoreFactory();
    }

    /** Make sure the JNDI factory exposes all the extra params that the non JNDI one exposes */
    public void testExtraParams() {
        List<String> baseParams = getBaseParams();
        List<String> standardParams = getParamKeys(getDataStoreFactory());
        standardParams.remove(JDBCDataStoreFactory.VALIDATECONN.key);
        standardParams.remove(JDBCDataStoreFactory.MAX_OPEN_PREPARED_STATEMENTS.key);
        standardParams.remove(HanaDataStoreFactory.INSTANCE.key);
        standardParams.remove(HanaDataStoreFactory.USE_SSL.key);
        standardParams.removeAll(baseParams);
        List<String> baseJndiParams = getBaseJNDIParams();
        List<String> jndiParams = getParamKeys(getJNDIStoreFactory());
        jndiParams.removeAll(baseJndiParams);
        assertEquals(standardParams, jndiParams);
    }
}
