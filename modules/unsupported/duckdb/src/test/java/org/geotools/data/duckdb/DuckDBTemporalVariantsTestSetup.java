/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.duckdb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.geotools.jdbc.JDBCDelegatingTestSetup;

/** Online setup that adds DuckDB temporal variant columns for end-to-end temporal tests. */
public class DuckDBTemporalVariantsTestSetup extends JDBCDelegatingTestSetup {

    public static final String TABLE = "temporal_variants_online";

    protected DuckDBTemporalVariantsTestSetup() {
        super(new DuckDBTestSetup());
    }

    @Override
    protected Connection getConnection() throws SQLException, IOException {
        return ((DuckDBTestSetup) delegate).getConnection();
    }

    @Override
    protected void setUpData() throws Exception {
        super.setUpData();
        dropTemporalVariantsTable();
        createTemporalVariantsTable();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        dropTemporalVariantsTable();
    }

    protected void createTemporalVariantsTable() throws Exception {
        run("CREATE TABLE \"" + TABLE + "\" ("
                + "\"id\" INTEGER PRIMARY KEY, "
                + "\"ts_plain\" TIMESTAMP, "
                + "\"ts_s\" TIMESTAMP_S, "
                + "\"ts_ms\" TIMESTAMP_MS, "
                + "\"ts_ns\" TIMESTAMP_NS, "
                + "\"ts_tz\" TIMESTAMPTZ, "
                + "\"ts_tz_long\" TIMESTAMP WITH TIME ZONE, "
                + "\"t_plain\" TIME, "
                + "\"t_ns\" TIME_NS, "
                + "\"t_tz\" TIMETZ, "
                + "\"t_tz_long\" TIME WITH TIME ZONE"
                + ")");

        run("INSERT INTO \"" + TABLE + "\" VALUES "
                + "(1, "
                + "TIMESTAMP '2020-01-01 00:00:00', "
                + "CAST('2020-01-01 00:00:00' AS TIMESTAMP_S), "
                + "CAST('2020-01-01 00:00:00.100' AS TIMESTAMP_MS), "
                + "CAST('2020-01-01 00:00:00.100000000' AS TIMESTAMP_NS), "
                + "CAST('2020-01-01 00:00:00+00' AS TIMESTAMPTZ), "
                + "CAST('2020-01-01 00:00:00+00' AS TIMESTAMP WITH TIME ZONE), "
                + "TIME '10:00:00', "
                + "CAST('10:00:00.100000000' AS TIME_NS), "
                + "CAST('10:00:00+00' AS TIMETZ), "
                + "CAST('10:00:00+00' AS TIME WITH TIME ZONE)"
                + ")");

        run("INSERT INTO \"" + TABLE + "\" VALUES "
                + "(2, "
                + "TIMESTAMP '2020-01-02 00:00:00', "
                + "CAST('2020-01-02 00:00:00' AS TIMESTAMP_S), "
                + "CAST('2020-01-02 00:00:00.200' AS TIMESTAMP_MS), "
                + "CAST('2020-01-02 00:00:00.200000000' AS TIMESTAMP_NS), "
                + "CAST('2020-01-02 00:00:00+00' AS TIMESTAMPTZ), "
                + "CAST('2020-01-02 00:00:00+00' AS TIMESTAMP WITH TIME ZONE), "
                + "TIME '12:00:00', "
                + "CAST('12:00:00.200000000' AS TIME_NS), "
                + "CAST('12:00:00+00' AS TIMETZ), "
                + "CAST('12:00:00+00' AS TIME WITH TIME ZONE)"
                + ")");

        run("INSERT INTO \"" + TABLE + "\" VALUES "
                + "(3, "
                + "TIMESTAMP '2020-01-03 00:00:00', "
                + "CAST('2020-01-03 00:00:00' AS TIMESTAMP_S), "
                + "CAST('2020-01-03 00:00:00.300' AS TIMESTAMP_MS), "
                + "CAST('2020-01-03 00:00:00.300000000' AS TIMESTAMP_NS), "
                + "CAST('2020-01-03 00:00:00+00' AS TIMESTAMPTZ), "
                + "CAST('2020-01-03 00:00:00+00' AS TIMESTAMP WITH TIME ZONE), "
                + "TIME '14:00:00', "
                + "CAST('14:00:00.300000000' AS TIME_NS), "
                + "CAST('14:00:00+00' AS TIMETZ), "
                + "CAST('14:00:00+00' AS TIME WITH TIME ZONE)"
                + ")");
    }

    private void dropTemporalVariantsTable() {
        ((DuckDBTestSetup) delegate).removeTable(TABLE);
    }
}
