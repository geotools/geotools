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

import org.geotools.data.duckdb.security.DuckDBExecutionPolicy;
import org.geotools.jdbc.JDBCTypeNamesTestSetup;

/** JDBC type-names online test setup for DuckDB. */
public class DuckDBTypeNamesTestSetup extends JDBCTypeNamesTestSetup {

    private DuckDBExecutionPolicy previousPolicy;

    protected DuckDBTypeNamesTestSetup() {
        super(new DuckDBTestSetup());
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        previousPolicy = ((DuckDBTestSetup) delegate).beginSetupSqlPolicy();
    }

    @Override
    public void tearDown() throws Exception {
        DuckDBTestSetup setup = (DuckDBTestSetup) delegate;
        DuckDBExecutionPolicy toRestore = previousPolicy;
        previousPolicy = null;
        try {
            if (toRestore != null) {
                setup.endSetupSqlPolicy(toRestore);
            }
        } finally {
            super.tearDown();
        }
    }

    @Override
    protected void createTypes() throws Exception {
        run("CREATE TABLE \"ftntable\" (\"id\" INTEGER, \"name\" VARCHAR, \"geom\" GEOMETRY)");
        run("CREATE VIEW \"ftnview\" AS SELECT \"id\", \"geom\" FROM \"ftntable\"");
    }

    @Override
    protected void dropTypes() {
        ((DuckDBTestSetup) delegate).removeTable("ftnview");
        ((DuckDBTestSetup) delegate).removeTable("ftntable");
    }
}
