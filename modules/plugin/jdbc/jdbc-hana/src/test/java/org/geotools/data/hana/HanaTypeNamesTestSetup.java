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
import org.geotools.jdbc.JDBCTypeNamesTestSetup;

/** @author Stefan Uhrig, SAP SE */
public class HanaTypeNamesTestSetup extends JDBCTypeNamesTestSetup {

    private static final String TABLE = "ftntable";

    private static final String VIEW = "ftnview";

    protected HanaTypeNamesTestSetup() {
        super(new HanaTestSetup());
    }

    @Override
    protected void createTypes() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.createTestSchema();

            String[][] cols = {
                {"id", "INT"}, {"name", "VARCHAR(255)"}, {"geom", "ST_Geometry(1000004326)"}
            };
            htu.createRegisteredTestTable(TABLE, cols);
            htu.createTestView(VIEW, TABLE, "id", "geom");
        }
    }

    @Override
    protected void dropTypes() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.dropTestView(VIEW);
            htu.dropTestTableCascade(TABLE);
        }
    }
}
