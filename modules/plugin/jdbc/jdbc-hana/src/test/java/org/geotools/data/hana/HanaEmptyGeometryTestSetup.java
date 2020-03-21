/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana;

import java.sql.Connection;
import org.geotools.jdbc.JDBCEmptyGeometryTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

/** @author Stefan Uhrig, SAP SE */
public class HanaEmptyGeometryTestSetup extends JDBCEmptyGeometryTestSetup {

    private static final String TABLE = "empty";

    public HanaEmptyGeometryTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createEmptyGeometryTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.createTestSchema();

            String[][] cols = {
                {"fid", "INT PRIMARY KEY"},
                {"id", "INT"},
                {"geom_point", "ST_Geometry"},
                {"geom_linestring", "ST_Geometry"},
                {"geom_polygon", "ST_Geometry"},
                {"geom_multipoint", "ST_Geometry"},
                {"geom_multilinestring", "ST_Geometry"},
                {"geom_multipolygon", "ST_Geometry"},
                {"name", "VARCHAR(255)"}
            };
            htu.createRegisteredTestTable(TABLE, cols);
        }
    }

    @Override
    protected void dropEmptyGeometryTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.dropTestTableCascade(TABLE);
        }
    }
}
