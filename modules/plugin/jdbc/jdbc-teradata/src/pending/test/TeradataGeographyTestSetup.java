/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2010, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.jdbc.JDBCGeographyTestSetup;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.util.Version;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TeradataGeographyTestSetup extends JDBCGeographyTestSetup {

    public TeradataGeographyTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }


    protected void createGeoPointTable() throws Exception {
        run("CREATE TABLE geopoint ( id PRIMARY KEY not null generated always as identity (start with 0) integer, name VARCHAR(64), geo ST_GEOMETRY)");
        run("INSERT INTO geopoint(name, geo) VALUES ('Town', ST_GeomFromText('POINT(-110 30)'))");
        run("INSERT INTO geopoint(name, geo) VALUES ('Forest', ST_GeomFromText('POINT(-109 29)'))");
        run("INSERT INTO geopoint(name, geo) VALUES ('London', ST_GeomFromText('POINT(0 49)') )");
    }


    protected void dropGeoPointTable() throws Exception {
        runSafe("DROP TABLE geopoint");
    }


    public boolean isGeographySupportAvailable() throws Exception {
        Connection cx = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            cx = getDataSource().getConnection();
            st = cx.createStatement();
            rs = st.executeQuery("SELECT infodata FROM dbc.dbcinfo where INFOKEY='VERSION';)");
            if (rs.next()) {
                return new Version(rs.getString(1)).compareTo(new Version("13.00.0.00")) >= 0;
            } else {
                return true;
            }
        } finally {
            rs.close();
            st.close();
            cx.close();
        }
    }
}
