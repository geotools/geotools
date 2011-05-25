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

import java.io.IOException;
import java.sql.SQLException;

import org.geotools.jdbc.JDBCFeatureStoreTest;
import org.geotools.jdbc.JDBCTestSetup;


/**
 * FeatureStore test for MySQL.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 * @source $URL$
 */
public class DB2FeatureStoreTest extends JDBCFeatureStoreTest {
    protected JDBCTestSetup createTestSetup() {
        return new DB2TestSetup();
    }

    @Override
    public void testAddInTransaction() throws IOException {
        // Skip, DB2 does a lock escalation resulting in a table lock
    }

    @Override
    public void testExternalConnection() throws IOException, SQLException {
        // skip, never comes back
    }
}
