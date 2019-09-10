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
import org.geotools.jdbc.JDBCBooleanTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

/** @author Stefan Uhrig, SAP SE */
public class HanaBooleanTestSetup extends JDBCBooleanTestSetup {

    private static final String TABLE = "b";

    public HanaBooleanTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createBooleanTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.createTestSchema();

            String cols[][] = {{"id", "INT PRIMARY KEY"}, {"boolProperty", "BOOLEAN"}};
            htu.createRegisteredTestTable(TABLE, cols);

            htu.insertIntoTestTable(TABLE, htu.nextTestSequenceValueForColumn(TABLE, "id"), false);
            htu.insertIntoTestTable(TABLE, htu.nextTestSequenceValueForColumn(TABLE, "id"), true);
        }
    }

    @Override
    protected void dropBooleanTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.dropTestTableCascade(TABLE);
        }
    }
}
