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

import org.geotools.jdbc.JDBCEmptyGeometryTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

/**
 * 
 *
 */
public class DB2EmptyGeometryTestSetup extends JDBCEmptyGeometryTestSetup {

    public DB2EmptyGeometryTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }
    
    @Override
    protected void createEmptyGeometryTable() throws Exception {
    Connection con = getDataSource().getConnection();
    con.prepareStatement("CREATE TABLE "+DB2TestUtil.SCHEMA_QUOTED+".\"empty\"(\"fid\" int  PRIMARY KEY not null GENERATED ALWAYS AS IDENTITY,  "
        + "\"id\" integer, "
        + "\"geom_point\" db2gse.ST_POINT, "
        + "\"geom_linestring\" db2gse.ST_LINESTRING, "            
        + "\"geom_polygon\" db2gse.ST_POLYGON, "
        + "\"geom_multipoint\" db2gse.ST_MULTIPOINT, "
        + "\"geom_multilinestring\" db2gse.ST_MULTILINESTRING, "
        + "\"geom_multipolygon\" db2gse.ST_MULTIPOLYGON, "                    
        + "\"name\" varchar(250))"   ).execute();

    DB2Util.executeRegister(DB2TestUtil.SCHEMA, "empty", "geom_point", DB2TestUtil.SRSNAME,con);
    DB2Util.executeRegister(DB2TestUtil.SCHEMA, "empty", "geom_linestring",DB2TestUtil.SRSNAME, con);
    DB2Util.executeRegister(DB2TestUtil.SCHEMA, "empty", "geom_polygon", DB2TestUtil.SRSNAME,con);
    DB2Util.executeRegister(DB2TestUtil.SCHEMA, "empty", "geom_multipoint", DB2TestUtil.SRSNAME,con);
    DB2Util.executeRegister(DB2TestUtil.SCHEMA, "empty", "geom_linestring", DB2TestUtil.SRSNAME,con);
    DB2Util.executeRegister(DB2TestUtil.SCHEMA, "empty", "geom_multipolygon", DB2TestUtil.SRSNAME,con);

    con.close();
            
    }

    @Override
    protected void dropEmptyGeometryTable() throws Exception {
        Connection con = getDataSource().getConnection();       
        DB2Util.executeUnRegister(DB2TestUtil.SCHEMA, "empty", "geom_point", con);
        DB2Util.executeUnRegister(DB2TestUtil.SCHEMA, "empty", "geom_linestring", con);
        DB2Util.executeUnRegister(DB2TestUtil.SCHEMA, "empty", "geom_polygon", con);
        DB2Util.executeUnRegister(DB2TestUtil.SCHEMA, "empty", "geom_multipoint", con);
        DB2Util.executeUnRegister(DB2TestUtil.SCHEMA, "empty", "geom_linestring", con);
        DB2Util.executeUnRegister(DB2TestUtil.SCHEMA, "empty", "geom_multipolygon", con);
        DB2TestUtil.dropTable(DB2TestUtil.SCHEMA, "empty", con);
        con.close();
            
    }
    
}
