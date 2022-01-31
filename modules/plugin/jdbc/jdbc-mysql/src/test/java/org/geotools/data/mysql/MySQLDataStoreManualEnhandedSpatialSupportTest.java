/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.mysql;

import java.util.Map;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;

/**
 * Tests that enhandedSpatialSupport is not enabled in MySQL versions < 5.6 even if
 * MySQLDataStoreFactory.ENHANCED_SPATIAL_SUPPORT is set.
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public class MySQLDataStoreManualEnhandedSpatialSupportTest extends JDBCTestSupport {
    @Override
    protected JDBCTestSetup createTestSetup() {
        return new MySQLTestSetup();
    }

    @Override
    protected Map<String, Object> createDataStoreFactoryParams() throws Exception {
        // TODO Auto-generated method stub
        Map<String, Object> params = super.createDataStoreFactoryParams();
        params.put(MySQLDataStoreFactory.ENHANCED_SPATIAL_SUPPORT.key, true);
        return params;
    }

    public void testManualEnhancedSpatialSupportDetection() throws Exception {
        // ensure that if not version 5.6 or later then PreciseSpatialOps are
        // disabled
        boolean isMySQL56 = MySQLDataStoreFactory.isMySqlVersion56OrAbove(dataStore);
        if (!isMySQL56) {
            assertFalse(((MySQLDialectBasic) dialect).getUsePreciseSpatialOps());
        }
    }
}
