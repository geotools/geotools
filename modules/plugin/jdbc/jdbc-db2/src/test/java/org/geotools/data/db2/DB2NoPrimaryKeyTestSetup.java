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

import org.geotools.jdbc.JDBCNoPrimaryKeyTestSetup;

public class DB2NoPrimaryKeyTestSetup extends JDBCNoPrimaryKeyTestSetup {

    protected DB2NoPrimaryKeyTestSetup() {
        super(new DB2TestSetup());
    }

    protected void createLakeTable() throws Exception {
    	
    	Connection con = getDataSource().getConnection();
    	con.prepareStatement("create table "+DB2TestUtil.SCHEMA_QUOTED+
    			".\"lake\" (\"id\" int  , \"geom\" DB2GSE.ST_POLYGON , \"name\" varchar(255))").execute();
    	DB2Util.executeRegister(DB2TestUtil.SCHEMA, "lake", "geom", DB2TestUtil.SRSNAME, con);
    	
        con.prepareStatement( "INSERT INTO "+DB2TestUtil.SCHEMA_QUOTED+".\"lake\" VALUES (0," +
        		"db2gse.ST_PolyFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',"+DB2TestUtil.SRID+"),'muddy')").execute();        
        con.close();
    	
    	
    }
    

    @Override
    protected void dropLakeTable() throws Exception {
    	
    	Connection con = getDataSource().getConnection();
    	try {
    		DB2Util.executeUnRegister(DB2TestUtil.SCHEMA, "lake", "goem", con);
    		DB2TestUtil.dropTable(DB2TestUtil.SCHEMA, "lake", con);
    	} catch (SQLException e) {    		
    	}
    	
        con.close();
    }

}
