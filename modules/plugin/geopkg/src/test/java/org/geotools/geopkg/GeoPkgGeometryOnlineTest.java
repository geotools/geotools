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
package org.geotools.geopkg;

import static org.geotools.geopkg.GeoPackage.GEOMETRY_COLUMNS;
import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.geotools.jdbc.JDBCGeometryOnlineTest;
import org.geotools.jdbc.JDBCGeometryTestSetup;

public class GeoPkgGeometryOnlineTest extends JDBCGeometryOnlineTest {

    @Override
    protected JDBCGeometryTestSetup createTestSetup() {
        return new GeoPkgGeometryTestSetup();
    }

    @Override
    public void testLinearRing() throws Exception {
        // JD: GeoPkg does not do linear rings
    }

    @Override
    @SuppressWarnings("PMD.CheckResultSet")
    protected Class checkGeometryType(Class geomClass) throws Exception {
        Class result = super.checkGeometryType(geomClass);

        // check the geometry column entry matches the expected contents
        String tableName = tname("t" + geomClass.getSimpleName());
        String sql = String.format("SELECT * FROM %s WHERE table_name = '%s'", GEOMETRY_COLUMNS, tableName);
        try (Connection cx = dataStore.getDataSource().getConnection();
                Statement st = cx.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            rs.next();
            assertEquals(geomClass.getSimpleName().toUpperCase(), rs.getString("geometry_type_name"));
            assertEquals("geom", rs.getString("column_name"));
            assertEquals(4326, rs.getInt("srs_id"));
        }

        return result;
    }
}
