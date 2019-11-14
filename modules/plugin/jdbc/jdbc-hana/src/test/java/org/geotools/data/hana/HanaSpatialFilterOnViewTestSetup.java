/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
public class HanaSpatialFilterOnViewTestSetup extends HanaTestSetupBase {

    private static final String TABLE = "tabforview";

    private static final String VIEW = "viewoftab";

    @Override
    protected void setUpData() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.createTestSchema();

            htu.dropTestView(VIEW);
            htu.dropTestTableCascade(TABLE);

            String[][] cols = {{"id", "INT"}, {"geom", "ST_Geometry(1000004326)"}};
            htu.createTestTable(TABLE, cols);

            htu.insertIntoTestTable(TABLE, 1, htu.geometry("POINT(1 2)", 1000004326));
            htu.insertIntoTestTable(TABLE, 2, htu.geometry("POINT(11 12)", 1000004326));
            htu.createTestView(VIEW, TABLE);
        }
    }
}
