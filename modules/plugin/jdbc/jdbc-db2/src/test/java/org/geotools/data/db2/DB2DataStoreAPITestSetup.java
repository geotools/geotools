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

import org.geotools.jdbc.JDBCDataStoreAPITestSetup;


public class DB2DataStoreAPITestSetup extends JDBCDataStoreAPITestSetup {
    public DB2DataStoreAPITestSetup() {
        super(new DB2TestSetup());
    }

    protected int getInitialPrimaryKeyValue() {
        return 1;
    }

    protected void createRoadTable() throws Exception {
    	Connection con = getDataSource().getConnection();
        con.prepareStatement("CREATE TABLE "+DB2TestUtil.SCHEMA_QUOTED+".\"road\"(\"fid\" int  PRIMARY KEY not null GENERATED ALWAYS AS IDENTITY, \"id\" int, "
            + "\"geom\" db2gse.ST_LINESTRING, \"name\" varchar(255) ) ").execute();
        DB2Util.executeRegister(DB2TestUtil.SCHEMA, "road", "geom", DB2TestUtil.SRSNAME,con);
        
        String insertClause = "INSERT INTO "+DB2TestUtil.SCHEMA_QUOTED+".\"road\"(\"id\",\"geom\",\"name\")";
        con.prepareStatement(insertClause+" VALUES (0,"        		
            + "db2gse.ST_LineFromText('LINESTRING(1 1, 2 2, 4 2, 5 1)',"+DB2TestUtil.SRID+")," + "'r1')").execute();
        con.prepareStatement(insertClause+" VALUES ( 1,"
            + "db2gse.ST_LineFromText('LINESTRING(3 0, 3 2, 3 3, 3 4)',"+DB2TestUtil.SRID+")," + "'r2')").execute();
        con.prepareStatement(insertClause+" VALUES ( 2,"
            + "db2gse.ST_LineFromText('LINESTRING(3 2, 4 2, 5 3)',"+DB2TestUtil.SRID+")," + "'r3')").execute();
        con.close();
    }

    protected void createRiverTable() throws Exception {
    	Connection con = getDataSource().getConnection();
    	
    	con.prepareStatement("CREATE TABLE "+DB2TestUtil.SCHEMA_QUOTED+".\"river\"(\"fid\" int  PRIMARY KEY not null GENERATED ALWAYS AS IDENTITY, \"id\" int, "
            + "\"geom\" db2gse.ST_MULTILINESTRING, \"river\" varchar(255) , \"flow\" double )").execute();
    	DB2Util.executeRegister(DB2TestUtil.SCHEMA, "river", "geom", DB2TestUtil.SRSNAME,con);
    	
    	String insertClause = "INSERT INTO "+DB2TestUtil.SCHEMA_QUOTED+".\"river\"(\"id\",\"geom\",\"river\",\"flow\")";
    	con.prepareStatement(insertClause+"  VALUES ( 0,"
            + "db2gse.ST_MLineFromText('MULTILINESTRING((5 5, 7 4),(7 5, 9 7, 13 7),(7 5, 9 3, 11 3))',"+DB2TestUtil.SRID+"),"
            + "'rv1', 4.5)").execute();
    	con.prepareStatement(insertClause+" VALUES ( 1,"
            + "db2gse.ST_MLineFromText('MULTILINESTRING((4 6, 4 8, 6 10))',"+DB2TestUtil.SRID+")," + "'rv2', 3.0)").execute();
        con.close();
    }

    protected void createLakeTable() throws Exception {
    	Connection con = getDataSource().getConnection();
    	con.prepareStatement("CREATE TABLE "+DB2TestUtil.SCHEMA_QUOTED+".\"lake\"(\"fid\" int  PRIMARY KEY not null GENERATED ALWAYS AS IDENTITY, \"id\" int, "
            + "\"geom\" db2gse.ST_POLYGON, \"name\" varchar(255) ) ").execute();

    	String insertClause = "INSERT INTO "+DB2TestUtil.SCHEMA_QUOTED+".\"lake\"(\"id\",\"geom\",\"name\")";
    	DB2Util.executeRegister(DB2TestUtil.SCHEMA, "lake", "geom",DB2TestUtil.SRSNAME, con);
    	con.prepareStatement(insertClause+" VALUES ( 0,"
            + "db2gse.ST_PolyFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',"+DB2TestUtil.SRID+")," + "'muddy')").execute();
        con.close();
    }

    protected void dropRoadTable() throws Exception {
    	
    	Connection con = getDataSource().getConnection();    	
    	DB2Util.executeUnRegister(DB2TestUtil.SCHEMA, "road", "geom", con);
    	DB2TestUtil.dropTable(DB2TestUtil.SCHEMA, "road", con);
        con.close();
    }

    protected void dropRiverTable() throws Exception {
    	Connection con = getDataSource().getConnection();
    	DB2Util.executeUnRegister(DB2TestUtil.SCHEMA, "river", "geom", con);
    	DB2TestUtil.dropTable(DB2TestUtil.SCHEMA, "river", con);
        con.close();
    }

    protected void dropLakeTable() throws Exception {
    	Connection con = getDataSource().getConnection();
    	DB2Util.executeUnRegister(DB2TestUtil.SCHEMA, "lake", "geom", con);
    	DB2TestUtil.dropTable(DB2TestUtil.SCHEMA, "lake", con);
        con.close();
    }

    protected void dropBuildingTable() throws Exception {
    	Connection con = getDataSource().getConnection();
    	DB2Util.executeUnRegister(DB2TestUtil.SCHEMA, "building", "geom", con);
    	DB2TestUtil.dropTable(DB2TestUtil.SCHEMA, "building", con);
        con.close();
    }

    
    
    
}
