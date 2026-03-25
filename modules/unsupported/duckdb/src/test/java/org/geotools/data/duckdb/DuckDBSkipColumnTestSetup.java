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
 *    Lesser General Public License for more details.
 */
package org.geotools.data.duckdb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.geotools.jdbc.JDBCSkipColumnTestSetup;

public class DuckDBSkipColumnTestSetup extends JDBCSkipColumnTestSetup {

    protected DuckDBSkipColumnTestSetup() {
        super(new DuckDBTestSetup());
    }

    @Override
    protected Connection getConnection() throws SQLException, IOException {
        return ((DuckDBTestSetup) delegate).getConnection();
    }

    @Override
    protected void createSkipColumnTable() throws Exception {
        DuckDBTestSetup setup = (DuckDBTestSetup) delegate;
        run("CREATE TABLE \"skipcolumn\" ("
                + "\"fid\" INTEGER PRIMARY KEY, "
                + "\"id\" INTEGER, "
                + "\"geom\" GEOMETRY, "
                + "\"weirdproperty\" UNION(num INTEGER, txt VARCHAR), "
                + "\"name\" VARCHAR)");
        run("INSERT INTO \"skipcolumn\" VALUES " + "(0, 0, " + setup.geometryLiteral("POINT(0 0)")
                + ", union_value(num := 1), 'GeoTools')");
    }

    @Override
    protected void dropSkipColumnTable() throws Exception {
        ((DuckDBTestSetup) delegate).removeTable("skipcolumn");
    }
}
