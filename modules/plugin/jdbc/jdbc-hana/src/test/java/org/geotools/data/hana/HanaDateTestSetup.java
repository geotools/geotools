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
import org.geotools.jdbc.JDBCDateTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

/** @author Stefan Uhrig, SAP SE */
public class HanaDateTestSetup extends JDBCDateTestSetup {

    private static final String TABLE = "dates";

    public HanaDateTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createDateTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.createTestSchema();

            String[][] cols = {{"d", "DATE"}, {"dt", "TIMESTAMP"}, {"t", "TIME"}};
            htu.createRegisteredTestTable(TABLE, cols);

            htu.insertIntoTestTable(TABLE, "2009-06-28", "2009-06-28 15:12:41", "15:12:41");
            htu.insertIntoTestTable(TABLE, "2009-01-15", "2009-01-15 13:10:12", "13:10:12");
            htu.insertIntoTestTable(TABLE, "2009-09-29", "2009-09-29 17:54:23", "17:54:23");
        }
    }

    @Override
    protected void dropDateTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.dropTestTableCascade(TABLE);
        }
    }
}
