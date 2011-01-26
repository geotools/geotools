/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ingres;

import org.geotools.jdbc.JDBCGeometrylessTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class IngresGeometrylessTestSetup extends JDBCGeometrylessTestSetup {

    public IngresGeometrylessTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createPersonTable() throws Exception {
    	run("CREATE SEQUENCE PERSON_FID_SEQUENCE");
        run("CREATE TABLE \"person\"(\"fid\" int PRIMARY KEY WITH DEFAULT NEXT VALUE FOR PERSON_FID_SEQUENCE, " + 
        		"\"id\" int, \"name\" varchar(256), \"age\" int)");
        run("INSERT INTO \"person\" (\"id\",\"name\",\"age\") VALUES (0,'Paul',32)");
        run("INSERT INTO \"person\" (\"id\",\"name\",\"age\") VALUES (0,'Anne',40)");
    }

    @Override
    protected void dropPersonTable() throws Exception {
        run("DROP TABLE \"person\"");
        run("DROP SEQUENCE PERSON_FID_SEQUENCE");
    }

    @Override
    protected void dropZipCodeTable() throws Exception {
        run("DROP TABLE \"zipcode\"");
    }

}
