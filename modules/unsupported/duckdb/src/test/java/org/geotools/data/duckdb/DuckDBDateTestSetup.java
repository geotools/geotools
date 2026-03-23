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

import org.geotools.jdbc.JDBCDateTestSetup;

public class DuckDBDateTestSetup extends JDBCDateTestSetup {

    protected DuckDBDateTestSetup() {
        super(new DuckDBTestSetup());
    }

    @Override
    protected void createDateTable() throws Exception {
        run("CREATE SEQUENCE dates_fid_seq");
        run("CREATE TABLE \"dates\" ("
                + "\"fid\" BIGINT PRIMARY KEY DEFAULT nextval('dates_fid_seq'), "
                + "\"d\" DATE, "
                + "\"dt\" TIMESTAMP, "
                + "\"t\" TIME)");
        run("INSERT INTO \"dates\" (\"d\", \"dt\", \"t\") VALUES "
                + "(DATE '2009-06-28', TIMESTAMP '2009-06-28 15:12:41', TIME '15:12:41')");
        run("INSERT INTO \"dates\" (\"d\", \"dt\", \"t\") VALUES "
                + "(DATE '2009-01-15', TIMESTAMP '2009-01-15 13:10:12', TIME '13:10:12')");
        run("INSERT INTO \"dates\" (\"d\", \"dt\", \"t\") VALUES "
                + "(DATE '2009-09-29', TIMESTAMP '2009-09-29 17:54:23', TIME '17:54:23')");
    }

    @Override
    protected void dropDateTable() throws Exception {
        runSafe("DROP TABLE IF EXISTS \"dates\"");
        runSafe("DROP SEQUENCE IF EXISTS dates_fid_seq");
    }
}
