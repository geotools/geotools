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
package org.geotools.data.postgis.synch;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.geotools.data.jdbc.JDBCUtils;

/**
 * A few methods shared by multiple classes
 * 
 * @author aaime
 * 
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.6.x/modules/unsupported/postgis-versioned/src/test/java/org/geotools/data/postgis/SqlTestUtils.java $
 */
public class SqlTestUtils {

    protected static void dropTable(DataSource pool, String tableName, boolean geomColumns)
            throws SQLException {
        Connection conn = null;
        Statement st = null;
        try {
            conn = pool.getConnection();
            st = conn.createStatement();
            if (geomColumns)
                st.execute("DELETE FROM GEOMETRY_COLUMNS WHERE F_TABLE_NAME = '" + tableName + "'");
            st.execute("DROP TABLE " + tableName + " CASCADE");
        } catch (SQLException e) {
            // no problem, the drop may fail
            System.out.println(e.getMessage());
        } finally {
            JDBCUtils.close(st);
            JDBCUtils.close(conn, null, null);
        }
    }

    protected static void execute(DataSource pool, String sql) throws SQLException {
        Connection conn = null;
        Statement st = null;
        try {
            conn = pool.getConnection();
            st = conn.createStatement();
            st.execute(sql);
            conn.commit();
        } finally {
            JDBCUtils.close(st);
            JDBCUtils.close(conn, null, null);
        }
    }

}
