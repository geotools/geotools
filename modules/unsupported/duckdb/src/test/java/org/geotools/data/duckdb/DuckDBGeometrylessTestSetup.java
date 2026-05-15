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

import org.geotools.jdbc.JDBCGeometrylessTestSetup;

public class DuckDBGeometrylessTestSetup extends JDBCGeometrylessTestSetup {

    protected DuckDBGeometrylessTestSetup() {
        super(new DuckDBTestSetup());
    }

    @Override
    protected void createPersonTable() throws Exception {
        run("CREATE SEQUENCE person_fid_seq");
        run("CREATE TABLE \"person\" ("
                + "\"fid\" BIGINT PRIMARY KEY DEFAULT nextval('person_fid_seq'), "
                + "\"id\" INTEGER, "
                + "\"name\" VARCHAR, "
                + "\"age\" INTEGER)");
        run("INSERT INTO \"person\" (\"id\", \"name\", \"age\") VALUES (0, 'Paul', 32)");
        run("INSERT INTO \"person\" (\"id\", \"name\", \"age\") VALUES (1, 'Anne', 40)");
    }

    @Override
    protected void dropPersonTable() throws Exception {
        runSafe("DROP TABLE IF EXISTS \"person\"");
        runSafe("DROP SEQUENCE IF EXISTS person_fid_seq");
    }

    @Override
    protected void dropZipCodeTable() throws Exception {
        runSafe("DROP TABLE IF EXISTS \"zipcode\"");
    }
}
