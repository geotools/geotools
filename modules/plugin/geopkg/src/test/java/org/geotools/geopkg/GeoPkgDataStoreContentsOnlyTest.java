/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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

import java.sql.SQLException;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.junit.Test;

public class GeoPkgDataStoreContentsOnlyTest extends JDBCTestSupport {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new GeoPkgDataStoreFactoryTestSetup(new GeoPkgTestSetup());
    }

    @Test
    public void testContentsOnly() throws SQLException {
        // a is not inserted to the gpkg_contents, where b is inserted
        assertFalse(dialect.includeTable("", "a", this.dataStore.getDataSource().getConnection()));
        assertTrue(dialect.includeTable("", "b", this.dataStore.getDataSource().getConnection()));
        this.dataStore.dispose();
    }
}
