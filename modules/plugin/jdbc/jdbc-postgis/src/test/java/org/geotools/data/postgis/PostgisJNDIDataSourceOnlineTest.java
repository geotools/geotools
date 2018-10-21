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

package org.geotools.data.postgis;

import java.util.List;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCJNDIDataSourceOnlineTest;
import org.geotools.jdbc.JDBCJNDIDataStoreFactory;
import org.geotools.jdbc.JDBCJNDITestSetup;

public class PostgisJNDIDataSourceOnlineTest extends JDBCJNDIDataSourceOnlineTest {

    protected JDBCJNDITestSetup createTestSetup() {
        return new JDBCJNDITestSetup(new PostGISTestSetup());
    }

    @Override
    protected JDBCJNDIDataStoreFactory getJNDIStoreFactory() {
        return new PostgisNGJNDIDataStoreFactory();
    }

    @Override
    protected JDBCDataStoreFactory getDataStoreFactory() {
        return new PostgisNGDataStoreFactory();
    }

    /** Make sure the JNDI factory exposes all the extra params that the non JNDI one exposes */
    public void testExtraParams() {
        List<String> baseParams = getBaseParams();
        List<String> standardParams = getParamKeys(getDataStoreFactory());
        standardParams.remove(JDBCDataStoreFactory.VALIDATECONN.key);
        standardParams.remove(JDBCDataStoreFactory.MAX_OPEN_PREPARED_STATEMENTS.key);
        standardParams.remove(PostgisNGDataStoreFactory.CREATE_DB_IF_MISSING.key);
        standardParams.remove(PostgisNGDataStoreFactory.CREATE_PARAMS.key);
        standardParams.removeAll(baseParams);
        List<String> baseJndiParams = getBaseJNDIParams();
        List<String> jndiParams = getParamKeys(getJNDIStoreFactory());
        jndiParams.removeAll(baseJndiParams);
        assertEquals(standardParams, jndiParams);
    }
}
