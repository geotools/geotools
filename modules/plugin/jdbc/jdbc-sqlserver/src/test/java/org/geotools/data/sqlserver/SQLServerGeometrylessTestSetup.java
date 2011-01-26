/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.sqlserver;

import org.geotools.jdbc.JDBCGeometrylessTestSetup;

public class SQLServerGeometrylessTestSetup extends JDBCGeometrylessTestSetup {

    public SQLServerGeometrylessTestSetup() {
        super( new SQLServerTestSetup());
    }

    @Override
    protected void createPersonTable() throws Exception {
        run("CREATE TABLE person(fid int IDENTITY(0,1) PRIMARY KEY, id int, "
                + "name varchar(255), age int)");
        run("INSERT INTO person (id,name,age) VALUES (0,'Paul',32)");
        run("INSERT INTO person (id,name,age) VALUES (0,'Anne',40)");
    }

    @Override
    protected void dropPersonTable() throws Exception {
        run("DROP TABLE person");
    }

    @Override
    protected void dropZipCodeTable() throws Exception {
        run("DROP TABLE zipcode");
    }

}
