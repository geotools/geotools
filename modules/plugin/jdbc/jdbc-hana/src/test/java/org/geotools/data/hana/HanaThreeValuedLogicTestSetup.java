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
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCThreeValuedLogicTestSetup;

/** @author Stefan Uhrig, SAP SE */
public class HanaThreeValuedLogicTestSetup extends JDBCThreeValuedLogicTestSetup {

    private static final String TABLE = "abc";

    public HanaThreeValuedLogicTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createAbcTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.createTestSchema();

            String[][] cols = {{"name", "VARCHAR(10)"}, {"a", "INT"}, {"b", "INT"}, {"c", "INT"}};
            htu.createRegisteredTestTable(TABLE, cols);

            htu.insertIntoTestTable(TABLE, "n_n_n", null, null, null);
            htu.insertIntoTestTable(TABLE, "a_b_c", 1, 2, 3);
        }
    }

    @Override
    protected void dropAbcTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.dropTestTableCascade(TABLE);
        }
    }
}
