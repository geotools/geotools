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
import org.geotools.jdbc.JDBCAggregateTestSetup;

public class DuckDBAggregateTestSetup extends JDBCAggregateTestSetup {

    protected DuckDBAggregateTestSetup() {
        super(new DuckDBTestSetup());
    }

    @Override
    protected Connection getConnection() throws SQLException, IOException {
        return ((DuckDBTestSetup) delegate).getConnection();
    }

    @Override
    protected void createAggregateTable() throws Exception {
        DuckDBTestSetup setup = (DuckDBTestSetup) delegate;
        run("CREATE TABLE \"aggregate\" ("
                + "\"fid\" INTEGER PRIMARY KEY, "
                + "\"id\" INTEGER, "
                + "\"geom\" GEOMETRY, "
                + "\"name\" VARCHAR)");

        String polygon = setup.geometryLiteral("POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))");
        run("INSERT INTO \"aggregate\" (\"fid\", \"id\", \"geom\", \"name\") VALUES " + "(0, 0, " + polygon
                + ", 'muddy1')");
        run("INSERT INTO \"aggregate\" (\"fid\", \"id\", \"geom\", \"name\") VALUES " + "(1, 1, " + polygon
                + ", 'muddy1')");
        run("INSERT INTO \"aggregate\" (\"fid\", \"id\", \"geom\", \"name\") VALUES " + "(2, 2, " + polygon
                + ", 'muddy2')");
    }

    @Override
    protected void dropAggregateTable() throws Exception {
        ((DuckDBTestSetup) delegate).removeTable("aggregate");
    }
}
