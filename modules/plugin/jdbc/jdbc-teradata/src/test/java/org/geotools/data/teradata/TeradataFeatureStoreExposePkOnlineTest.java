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
package org.geotools.data.teradata;

import java.io.IOException;
import java.sql.SQLException;
import org.geotools.jdbc.JDBCFeatureStoreExposePkOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;

public class TeradataFeatureStoreExposePkOnlineTest extends JDBCFeatureStoreExposePkOnlineTest {

    protected JDBCTestSetup createTestSetup() {
        return new TeradataTestSetup();
    }

    public void testAddInTransaction() throws IOException {
        // TODO enable transaction isolation (non-blocking)
    }

    public void testExternalConnection() throws IOException, SQLException {
        // TODO enable transaction isolation (non-blocking)
    }
}
