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
import org.geotools.jdbc.JDBCJoinTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

/** @author Stefan Uhrig, SAP SE */
public class HanaJoinTestSetup extends JDBCJoinTestSetup {

    private static final String TABLE1 = "ftjoin";

    private static final String TABLE2 = "ftjoin2";

    public HanaJoinTestSetup() {
        this(new HanaTestSetup());
    }

    public HanaJoinTestSetup(JDBCTestSetup setup) {
        super(setup);
    }

    @Override
    protected void createJoinTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.createTestSchema();

            String[][] cols1 = {
                {"id", "INT PRIMARY KEY"},
                {"name", "VARCHAR(255)"},
                {"geom", "ST_Geometry(1000004326)"},
                {"join1intProperty", "INT"}
            };
            htu.createTestTable(TABLE1, cols1);

            String[][] cols2 = {
                {"id", "INT PRIMARY KEY"},
                {"join2intProperty", "INT"},
                {"stringProperty2", "VARCHAR(255)"}
            };
            htu.createTestTable(TABLE2, cols2);

            htu.insertIntoTestTable(
                    TABLE1,
                    0,
                    "zero",
                    htu.geometry(
                            "POLYGON ((-0.1 -0.1, -0.1 0.1, 0.1 0.1, 0.1 -0.1, -0.1 -0.1))",
                            1000004326),
                    0);
            htu.insertIntoTestTable(
                    TABLE1,
                    1,
                    "one",
                    htu.geometry(
                            "POLYGON ((-1.1 -1.1, -1.1 1.1, 1.1 1.1, 1.1 -1.1, -1.1 -1.1))",
                            1000004326),
                    1);
            htu.insertIntoTestTable(
                    TABLE1,
                    2,
                    "two",
                    htu.geometry("POLYGON ((-10 -10, -10 10, 10 10, 10 -10, -10 -10))", 1000004326),
                    2);
            htu.insertIntoTestTable(TABLE1, 3, "three", null, 3);

            htu.insertIntoTestTable(TABLE2, 0, 0, "2nd zero");
            htu.insertIntoTestTable(TABLE2, 1, 1, "2nd one");
            htu.insertIntoTestTable(TABLE2, 2, 2, "2nd two");
            htu.insertIntoTestTable(TABLE2, 3, 3, "2nd three");
        }
    }

    @Override
    protected void dropJoinTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.dropTestTableCascade(TABLE1);
            htu.dropTestTableCascade(TABLE2);
        }
    }
}
