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
package org.geotools.data.spatialite;

import java.io.IOException;
import java.sql.SQLException;

import org.geotools.jdbc.JDBCFeatureStoreTest;
import org.geotools.jdbc.JDBCTestSetup;

public class SpatiaLiteFeatureStoreTest extends JDBCFeatureStoreTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new SpatiaLiteTestSetup();
    }

    @Override
    public void testAddNullAttributes() throws IOException {
        //JD: as far as I can tell you can't have null geometries
        // in spatialite...
    }
    @Override
    public void testModifyNullAttributes() throws IOException {
    }
    
    @Override
    public void testAddInTransaction() throws IOException {
        // does not work, see GEOT-2832
    }
    
    @Override
    public void testExternalConnection() throws IOException, SQLException {
        //SQLite locking does not allow one connection to write while another one reads on the 
        // same table
    }
    
}
