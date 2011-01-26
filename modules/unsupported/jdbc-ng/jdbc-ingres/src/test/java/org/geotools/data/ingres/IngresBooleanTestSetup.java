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
package org.geotools.data.ingres;

import org.geotools.jdbc.JDBCBooleanTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class IngresBooleanTestSetup extends JDBCBooleanTestSetup {

    public IngresBooleanTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createBooleanTable() throws Exception {
    	run( "CREATE SEQUENCE B_ID_SEQUENCE" ); 
        run( "CREATE TABLE \"b\" ( \"id\" int PRIMARY KEY WITH DEFAULT NEXT VALUE FOR B_ID_SEQUENCE, \"boolProperty\" boolean)" );
        run( "INSERT INTO \"b\" (\"boolProperty\") VALUES (false)");
        run( "INSERT INTO \"b\" (\"boolProperty\") VALUES (true)");
    }

    @Override
    protected void dropBooleanTable() throws Exception {
        run( "DROP TABLE \"b\"");
        run( "DROP SEQUENCE B_ID_SEQUENCE" );
    }
    

}
