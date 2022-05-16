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
import org.geotools.jdbc.JDBCGeometrylessTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

/** @author Stefan Uhrig, SAP SE */
public class HanaGeometrylessTestSetup extends JDBCGeometrylessTestSetup {

    private static final String PERSON_TABLE = "person";

    private static final String ZIPCODE_TABLE = "zipcode";

    public HanaGeometrylessTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createPersonTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.createTestSchema();

            String[][] cols = {
                {"fid", "INT PRIMARY KEY"}, {"id", "INT"}, {"name", "VARCHAR(255)"}, {"age", "INT"}
            };
            htu.createRegisteredTestTable(PERSON_TABLE, cols);

            htu.insertIntoTestTable(
                    PERSON_TABLE,
                    htu.nextTestSequenceValueForColumn(PERSON_TABLE, "fid"),
                    0,
                    "Paul",
                    32);
            htu.insertIntoTestTable(
                    PERSON_TABLE,
                    htu.nextTestSequenceValueForColumn(PERSON_TABLE, "fid"),
                    1,
                    "Anne",
                    40);
        }
    }

    @Override
    protected void dropPersonTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.dropTestTableCascade(PERSON_TABLE);
        }
    }

    @Override
    protected void dropZipCodeTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.dropTestTableCascade(ZIPCODE_TABLE);
        }
    }
}
