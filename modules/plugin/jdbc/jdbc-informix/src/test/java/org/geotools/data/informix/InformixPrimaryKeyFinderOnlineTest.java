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

import java.util.Map;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCPrimaryKeyFinderOnlineTest;
import org.geotools.jdbc.JDBCPrimaryKeyFinderTestSetup;

public class InformixPrimaryKeyFinderOnlineTest extends JDBCPrimaryKeyFinderOnlineTest {

    @Override
    protected JDBCPrimaryKeyFinderTestSetup createTestSetup() {
        return new InformixPrimaryKeyFinderTestSetup();
    }

    @Override
    public void testSequencedPrimaryKey() throws Exception {
        // Skip - sequences are not implemented in this driver (SERIAL is)
    }

    @Override
    protected Map<String, Object> createDataStoreFactoryParams() throws Exception {
        Map<String, Object> params = super.createDataStoreFactoryParams();
        params.put(JDBCDataStoreFactory.PK_METADATA_TABLE.key, "gt_pk_metadata");
        return params;
    }
}
