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
import org.geotools.jdbc.JDBCGeographyTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

/** @author Stefan Uhrig, SAP SE */
public class HanaGeographyTestSetup extends JDBCGeographyTestSetup {

    private static final String POINT_TABLE = "geopoint";

    private static final String LINE_TABLE = "geoline";

    public HanaGeographyTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createGeoPointTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.createTestSchema();

            String[][] cols = {
                {"id", "INT PRIMARY KEY"}, {"name", "VARCHAR(64)"}, {"geo", "ST_Geometry(4326)"}
            };
            htu.createRegisteredTestTable(POINT_TABLE, cols);

            htu.insertIntoTestTable(
                    POINT_TABLE,
                    htu.nextTestSequenceValueForColumn(POINT_TABLE, "id"),
                    "Town",
                    htu.geometry("POINT(-110 30)", 4326));
            htu.insertIntoTestTable(
                    POINT_TABLE,
                    htu.nextTestSequenceValueForColumn(POINT_TABLE, "id"),
                    "Forest",
                    htu.geometry("POINT(-109 29)", 4326));
            htu.insertIntoTestTable(
                    POINT_TABLE,
                    htu.nextTestSequenceValueForColumn(POINT_TABLE, "id"),
                    "London",
                    htu.geometry("POINT(0 49)", 4326));
        }
    }

    @Override
    protected void dropGeoPointTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.dropTestTableCascade(POINT_TABLE);
        }
    }

    @Override
    protected void createGeoLineTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.createTestSchema();

            String[][] cols = {
                {"id", "INT PRIMARY KEY"}, {"name", "VARCHAR(64)"}, {"geo", "ST_Geometry(4326)"}
            };
            htu.createRegisteredTestTable(LINE_TABLE, cols);

            htu.insertIntoTestTable(
                    LINE_TABLE,
                    htu.nextTestSequenceValueForColumn(LINE_TABLE, "id"),
                    "theline",
                    htu.geometry("LINESTRING(0 0, 1 1, 2 2, 3 3, 4 4)", 4326));
        }
    }

    @Override
    protected void dropGeoLineTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.dropTestTableCascade(LINE_TABLE);
        }
    }
}
