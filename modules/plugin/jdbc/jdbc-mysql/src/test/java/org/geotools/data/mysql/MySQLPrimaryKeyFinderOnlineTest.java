/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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

import java.util.HashMap;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCPrimaryKeyFinderOnlineTest;
import org.geotools.jdbc.JDBCPrimaryKeyFinderTestSetup;

public class MySQLPrimaryKeyFinderOnlineTest extends JDBCPrimaryKeyFinderOnlineTest {

    @Override
    protected JDBCPrimaryKeyFinderTestSetup createTestSetup() {
        return new MySQLPrimaryKeyFinderTestSetup();
    }

    @Override
    public void testSequencedPrimaryKey() throws Exception {
        // MySQL does not do sequences
    }

    @Override
    protected HashMap createDataStoreFactoryParams() throws Exception {
        HashMap params = super.createDataStoreFactoryParams();
        params.put(JDBCDataStoreFactory.PK_METADATA_TABLE.key, "gt_pk_metadata");
        return params;
    }
}
