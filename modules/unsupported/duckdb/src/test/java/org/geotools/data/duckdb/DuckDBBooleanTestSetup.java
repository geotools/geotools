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

import org.geotools.jdbc.JDBCBooleanTestSetup;

public class DuckDBBooleanTestSetup extends JDBCBooleanTestSetup {

    protected DuckDBBooleanTestSetup() {
        super(new DuckDBTestSetup());
    }

    @Override
    protected void createBooleanTable() throws Exception {
        run("CREATE SEQUENCE b_fid_seq");
        run("CREATE TABLE \"b\" ("
                + "\"fid\" BIGINT PRIMARY KEY DEFAULT nextval('b_fid_seq'), "
                + "\"boolProperty\" BOOLEAN)");
        run("INSERT INTO \"b\" (\"boolProperty\") VALUES (FALSE)");
        run("INSERT INTO \"b\" (\"boolProperty\") VALUES (TRUE)");
    }

    @Override
    protected void dropBooleanTable() throws Exception {
        runSafe("DROP TABLE IF EXISTS \"b\"");
        runSafe("DROP SEQUENCE IF EXISTS b_fid_seq");
    }
}
