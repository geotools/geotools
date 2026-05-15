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

public class DuckDBPreserveTopologyTestSetup extends JDBCDelegatingTestSetup {

    protected DuckDBPreserveTopologyTestSetup() {
        super(new DuckDBTestSetup());
    }

    @Override
    protected Connection getConnection() throws SQLException, IOException {
        return ((DuckDBTestSetup) delegate).getConnection();
    }

    @Override
    protected void setUpData() throws Exception {
        super.setUpData();
        dropSimplifyPolygonTable();
        createSimplifyPolygonTable();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        dropSimplifyPolygonTable();
    }

    protected void createSimplifyPolygonTable() throws Exception {
        DuckDBTestSetup setup = (DuckDBTestSetup) delegate;
        run("CREATE TABLE \"simplify_polygon_topology\" (\"geom\" GEOMETRY)");
        run("INSERT INTO \"simplify_polygon_topology\" VALUES ("
                + setup.geometryLiteral(
                        "POLYGON((-10 -10, 10 -10, 10 10, -10 10, -10 -10), (-1 -1, 1 -1, 1 1, -1 1, -1 -1))")
                + ")");
    }

    protected void dropSimplifyPolygonTable() {
        ((DuckDBTestSetup) delegate).removeTable("simplify_polygon_topology");
    }
}
