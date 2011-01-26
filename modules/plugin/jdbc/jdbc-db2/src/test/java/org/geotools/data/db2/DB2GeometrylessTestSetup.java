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
package org.geotools.data.db2;

import java.sql.Connection;
import java.sql.SQLException;

import org.geotools.jdbc.JDBCGeometrylessTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class DB2GeometrylessTestSetup extends JDBCGeometrylessTestSetup {

    protected DB2GeometrylessTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createPersonTable() throws Exception {
        //set up table
    	Connection con = getDataSource().getConnection();
        con.prepareStatement("CREATE TABLE "+DB2TestUtil.SCHEMA_QUOTED+".\"person\" (\"fid\" int  generated always as identity (start with 0, increment by 1) , "
        	+ "\"id\" int , "
            + " \"name\" varchar(255), \"age\" int, PRIMARY KEY (\"fid\") )").execute();
        
        // insert data
        con.prepareStatement("INSERT INTO "+DB2TestUtil.SCHEMA_QUOTED+".\"person\"(\"id\",\"name\",\"age\") VALUES ( 0, 'Paul', 32)").execute();
        con.prepareStatement("INSERT INTO "+DB2TestUtil.SCHEMA_QUOTED+".\"person\"(\"id\",\"name\",\"age\") VALUES ( 1, 'Anne', 40)").execute();
        con.close();
    }

    @Override
    protected void dropPersonTable() throws Exception {
        Connection con = getDataSource().getConnection();
    	DB2TestUtil.dropTable(DB2TestUtil.SCHEMA, "person", con);
        con.close();
    }

    @Override
    protected void dropZipCodeTable() throws Exception {
    	Connection con = getDataSource().getConnection();
    	DB2TestUtil.dropTable(DB2TestUtil.SCHEMA, "zipcode", con);        
        con.close();
    }

}
