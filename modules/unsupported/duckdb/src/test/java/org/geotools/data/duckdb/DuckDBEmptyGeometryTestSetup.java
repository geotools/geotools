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
import org.geotools.jdbc.JDBCEmptyGeometryTestSetup;

public class DuckDBEmptyGeometryTestSetup extends JDBCEmptyGeometryTestSetup {

    protected DuckDBEmptyGeometryTestSetup() {
        super(new DuckDBTestSetup());
    }

    @Override
    protected Connection getConnection() throws SQLException, IOException {
        return ((DuckDBTestSetup) delegate).getConnection();
    }

    @Override
    protected void createEmptyGeometryTable() throws Exception {
        run("CREATE SEQUENCE empty_fid_seq");
        run("CREATE TABLE \"empty\" ("
                + "\"fid\" BIGINT PRIMARY KEY DEFAULT nextval('empty_fid_seq'), "
                + "\"id\" INTEGER, "
                + "\"geom_point\" GEOMETRY, "
                + "\"geom_linestring\" GEOMETRY, "
                + "\"geom_polygon\" GEOMETRY, "
                + "\"geom_multipoint\" GEOMETRY, "
                + "\"geom_multilinestring\" GEOMETRY, "
                + "\"geom_multipolygon\" GEOMETRY, "
                + "\"name\" VARCHAR)");
    }

    @Override
    protected void dropEmptyGeometryTable() throws Exception {
        runSafe("DROP TABLE IF EXISTS \"empty\"");
        runSafe("DROP SEQUENCE IF EXISTS empty_fid_seq");
    }
}
