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

import org.geotools.jdbc.JDBCEmptyTestSetup;


public class DB2EmptyTestSetup extends JDBCEmptyTestSetup {
    public DB2EmptyTestSetup() {
        super(new DB2TestSetup());
    }


	@Override
	protected void createEmptyTable() throws Exception {
    	Connection con = getDataSource().getConnection();
    	con.prepareStatement("CREATE TABLE "+DB2TestUtil.SCHEMA_QUOTED+".\"empty\"(\"id\" int  PRIMARY KEY not null GENERATED ALWAYS AS IDENTITY,  "
            + "\"geom\" db2gse.ST_POLYGON) ").execute();
    	DB2Util.executeRegister(DB2TestUtil.SCHEMA, "empty", "geom",DB2TestUtil.SRSNAME, con);
        con.close();
		
	}

	@Override
	protected void dropEmptyTable() throws Exception {
    	Connection con = getDataSource().getConnection();    	
    	DB2Util.executeUnRegister(DB2TestUtil.SCHEMA, "empty", "geom", con);
    	DB2TestUtil.dropTable(DB2TestUtil.SCHEMA, "empty", con);
        con.close();
		
	}

    
    
    
}
