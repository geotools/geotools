/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.geopkg;

import java.util.Map;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCLobOnlineTest;
import org.geotools.jdbc.JDBCLobTestSetup;

public class GeoPkgLobOnlineTest extends JDBCLobOnlineTest {

    /** GeoPackage can fetch keys only if the batch insert size is 1 */
    @Override
    protected Map<String, Object> createDataStoreFactoryParams() throws Exception {
        Map<String, Object> params = super.createDataStoreFactoryParams();
        params.put(JDBCDataStoreFactory.BATCH_INSERT_SIZE.key, 1);
        return params;
    }

    @Override
    protected JDBCLobTestSetup createTestSetup() {
        return new GeoPkgLobTestSetup();
    }
}
