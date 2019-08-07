/*
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
import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.jdbc.JDBCTestSetup;

/** @author Stefan Uhrig, SAP SE */
public class HanaDataStoreAPITestSetup extends JDBCDataStoreAPITestSetup {

    private static final String LAKE_TABLE = "lake";

    private static final String RIVER_TABLE = "river";

    private static final String ROAD_TABLE = "road";

    private static final String BUILDING_TABLE = "building";

    public HanaDataStoreAPITestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createLakeTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);

            String[][] cols = {
                {"fid", "INT PRIMARY KEY"},
                {"id", "INT"},
                {"geom", "ST_Geometry(1000004326)"},
                {"name", "VARCHAR(255)"}
            };
            htu.createRegisteredTable(null, LAKE_TABLE, cols);

            htu.insertIntoTable(
                    null,
                    LAKE_TABLE,
                    htu.nextSequenceValueForColumn(null, LAKE_TABLE, "fid"),
                    0,
                    htu.geometry("POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))", 1000004326),
                    "muddy");
        }
    }

    @Override
    protected void createRiverTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);

            String[][] cols = {
                {"fid", "INT PRIMARY KEY"},
                {"id", "INT"},
                {"geom", "ST_Geometry(1000004326)"},
                {"river", "VARCHAR(255)"},
                {"flow", "REAL"}
            };
            htu.createRegisteredTable(null, RIVER_TABLE, cols);

            htu.insertIntoTable(
                    null,
                    RIVER_TABLE,
                    htu.nextSequenceValueForColumn(null, RIVER_TABLE, "fid"),
                    0,
                    htu.geometry(
                            "MULTILINESTRING((5 5, 7 4), (7 5, 9 7, 13 7), (7 5, 9 3, 11 3))",
                            1000004326),
                    "rv1",
                    4.5);
            htu.insertIntoTable(
                    null,
                    RIVER_TABLE,
                    htu.nextSequenceValueForColumn(null, RIVER_TABLE, "fid"),
                    1,
                    htu.geometry("MULTILINESTRING((4 6, 4 8, 6 10))", 1000004326),
                    "rv2",
                    3.0);
        }
    }

    @Override
    protected void createRoadTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);

            String[][] cols = {
                {"fid", "INT PRIMARY KEY"},
                {"id", "INT"},
                {"geom", "ST_Geometry(1000004326)"},
                {"name", "VARCHAR(255)"}
            };
            htu.createRegisteredTable(null, ROAD_TABLE, cols);

            htu.insertIntoTable(
                    null,
                    ROAD_TABLE,
                    htu.nextSequenceValueForColumn(null, ROAD_TABLE, "fid"),
                    0,
                    htu.geometry("LINESTRING(1 1, 2 2, 4 2, 5 1)", 1000004326),
                    "r1");
            htu.insertIntoTable(
                    null,
                    ROAD_TABLE,
                    htu.nextSequenceValueForColumn(null, ROAD_TABLE, "fid"),
                    1,
                    htu.geometry("LINESTRING(3 0, 3 2, 3 3, 3 4)", 1000004326),
                    "r2");
            htu.insertIntoTable(
                    null,
                    ROAD_TABLE,
                    htu.nextSequenceValueForColumn(null, ROAD_TABLE, "fid"),
                    2,
                    htu.geometry("LINESTRING(3 2, 4 2, 5 3)", 1000004326),
                    "r3");
        }
    }

    @Override
    protected void dropBuildingTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.dropTableCascade(null, BUILDING_TABLE);
        }
    }

    @Override
    protected void dropLakeTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.dropTableCascade(null, LAKE_TABLE);
        }
    }

    @Override
    protected void dropRiverTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.dropTableCascade(null, RIVER_TABLE);
        }
    }

    @Override
    protected void dropRoadTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.dropTableCascade(null, ROAD_TABLE);
        }
    }
}
