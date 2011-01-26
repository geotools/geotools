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
package org.geotools.data.sqlserver;

import java.io.IOException;

import org.geotools.jdbc.JDBCFeatureStoreTest;
import org.geotools.jdbc.JDBCTestSetup;

public class SQLServerFeatureStoreTest extends JDBCFeatureStoreTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new SQLServerTestSetup();
    }
    
    @Override
    public void testAddInTransaction() throws IOException {
        // does not work, see GEOT-2832
    }

}
