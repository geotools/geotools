/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General 
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General  License for more details.
 */
package org.geotools.data.db2;

import java.sql.Connection;
import java.sql.SQLException;

import org.geotools.jdbc.JDBCGeometryTestSetup;

public class DB2GeometryTestSetup extends JDBCGeometryTestSetup {

    protected DB2GeometryTestSetup() {
        super(new DB2TestSetup());

    }
    

    
    @Override
    protected void dropSpatialTable(String tableName) throws Exception {
    	Connection con = getDataSource().getConnection();
    	try {
    		DB2Util.executeUnRegister(DB2TestUtil.SCHEMA, tableName, "goem", con);
    		DB2TestUtil.dropTable(DB2TestUtil.SCHEMA, tableName,con);    		
    	} catch (SQLException e) {    		
    	}
    	
        con.close();
    }

}
