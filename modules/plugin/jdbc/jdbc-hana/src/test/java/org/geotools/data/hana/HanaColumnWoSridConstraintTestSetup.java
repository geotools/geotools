/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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

/** @author Stefan Uhrig, SAP SE */
public class HanaColumnWoSridConstraintTestSetup extends HanaTestSetupPSPooling {

    private static final String TABLE = "tabwosc";

    @Override
    protected void setUpData() throws Exception {
        try (Connection conn = getConnection()) {
            HanaVersion version = new HanaVersion(conn.getMetaData().getDatabaseProductVersion());
            if (version.getVersion() < 2 || version.getVersion() == 2 && version.getRevision() < 60) {
                // Columns without SRID constraint are supported only in HANA 2 revision 60 and
                // later
                return;
            }

            HanaTestUtil htu = new HanaTestUtil(conn, fixture);
            htu.createTestSchema();

            htu.dropTestTableCascade(TABLE);

            String[][] cols = {{"id", "INT"}, {"geom", "ST_Geometry(NULL)"}};
            htu.createTestTable(TABLE, cols);

            htu.insertIntoTestTable(TABLE, 1, htu.geometry("POINT(3 4)", 3857));
            htu.insertIntoTestTable(TABLE, 2, htu.geometry("POINT(-3 -4)", 3857));
        }
    }
}
