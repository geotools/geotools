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
import org.geotools.jdbc.JDBCNoPrimaryKeyTestSetup;

/** @author Stefan Uhrig, SAP SE */
public class HanaNoPrimaryKeyTestSetup extends JDBCNoPrimaryKeyTestSetup {

    private static final String TABLE = "lake";

    protected HanaNoPrimaryKeyTestSetup() {
        super(new HanaTestSetup());
    }

    @Override
    protected void createLakeTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.createTestSchema();

            String[][] cols = {
                {"id", "INT"}, {"geom", "ST_Geometry(1000004326)"}, {"name", "VARCHAR(255)"}
            };
            htu.createTestTable(TABLE, cols);

            htu.insertIntoTestTable(
                    TABLE,
                    0,
                    htu.geometry("POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))", 1000004326),
                    "muddy");
        }
    }

    @Override
    protected void dropLakeTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.dropTestTableCascade(TABLE);
        }
    }
}
