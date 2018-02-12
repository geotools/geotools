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
import org.geotools.jdbc.JDBCAggregateTestSetup;

/** @author Stefan Uhrig, SAP SE */
public class HanaAggregateTestSetup extends JDBCAggregateTestSetup {

    private static final String TABLE = "aggregate";

    protected HanaAggregateTestSetup() {
        super(new HanaTestSetup());
    }

    @Override
    protected void createAggregateTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.createTestSchema();

            String[][] cols = {
                {"fid", "INT PRIMARY KEY"},
                {"id", "INT"},
                {"geom", "ST_Geometry(1000004326)"},
                {"name", "VARCHAR(255)"}
            };
            htu.createRegisteredTestTable(TABLE, cols);

            htu.insertIntoTestTable(
                    TABLE,
                    htu.nextTestSequenceValueForColumn(TABLE, "fid"),
                    0,
                    htu.geometry("POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))", 1000004326),
                    "muddy1");
            htu.insertIntoTestTable(
                    TABLE,
                    htu.nextTestSequenceValueForColumn(TABLE, "fid"),
                    1,
                    htu.geometry("POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))", 1000004326),
                    "muddy1");
            htu.insertIntoTestTable(
                    TABLE,
                    htu.nextTestSequenceValueForColumn(TABLE, "fid"),
                    2,
                    htu.geometry("POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))", 1000004326),
                    "muddy2");
        }
    }

    @Override
    protected void dropAggregateTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.dropTestTableCascade(TABLE);
        }
    }
}
