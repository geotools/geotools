/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.teradata;

import org.geotools.jdbc.JDBCUDTTestSetup;

public class TeradataUDTTestSetup extends JDBCUDTTestSetup {

    public TeradataUDTTestSetup() {
        this(new TeradataTestSetup());
    }

    public TeradataUDTTestSetup(TeradataTestSetup setup) {
        super(setup);
    }


    protected void createUdtTable() throws Exception {
// TODO make restriction        run("CREATE TYPE foo AS text CHECK (VALUE ~ '\\\\d{2}\\\\D{2}');");
        run("CREATE TYPE foo AS varchar(4) final");
        run("CREATE TABLE udt (id integer PRIMARY KEY NOT NULL, ut foo);");
        run("INSERT INTO udt VALUES (0, '12ab');");

    }


    protected void dropUdtTable() throws Exception {
        runSafe("DROP TABLE udt");
        runSafe("DROP TYPE foo");
    }

}
