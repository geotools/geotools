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

import org.geotools.jdbc.JDBCPrimaryKeyFinderTestSetup;

public class DB2PrimaryKeyFinderTestSetup extends JDBCPrimaryKeyFinderTestSetup {

    protected DB2PrimaryKeyFinderTestSetup() {
        super(new DB2TestSetup());
    }
   
    
    @Override
    protected void createMetadataTable() throws Exception {
        Connection con = getDataSource().getConnection();
        con.prepareStatement("CREATE TABLE \""+DB2TestUtil.SCHEMA.toUpperCase()+ "\".gt_pk_metadata ( " + "table_schema VARCHAR(32) not null, "
                + "table_name VARCHAR(32) NOT NULL, " + "pk_column VARCHAR(32) NOT NULL, "
                + "pk_column_idx INTEGER, " + "pk_policy VARCHAR(32), " + "pk_sequence VARCHAR(64)," 
                + "primary key (table_schema, table_name, pk_column)" 
                + ")").execute();
        con.close();
    }

    @Override
    protected void dropMetadataTable() throws Exception {
        Connection con = getDataSource().getConnection();
        try {                
                //DB2TestUtil.dropTable(DB2TestUtil.SCHEMA, "gt_pk_metadata_table", con);
                DB2TestUtil.dropTable(DB2TestUtil.SCHEMA.toUpperCase(), "gt_pk_metadata".toUpperCase(), con);
        } catch (SQLException e) {              
        }        
        con.close();
        
    }

    @Override
    protected void createSequencedPrimaryKeyTable() throws Exception {
        Connection con = getDataSource().getConnection();
        con.prepareStatement("CREATE TABLE \""+DB2TestUtil.SCHEMA+ "\".\"seqtable\" ( \"key\" integer not null,  "
                + "\"name\" VARCHAR(256), \"geom\" db2gse.ST_GEOMETRY, primary key (\"key\") )").execute();
        
        DB2Util.executeRegister(DB2TestUtil.SCHEMA, "seqtable", "geom", DB2TestUtil.SRSNAME, con);
        
        con.prepareStatement("CREATE SEQUENCE "+getSquenceNameQuoted()+ " AS INTEGER  start with 1").execute();

        con.prepareStatement( "INSERT INTO \""+DB2TestUtil.SCHEMA+ "\".\"seqtable\" (\"key\", \"name\",\"geom\" ) VALUES (" + 
            "next value for "+getSquenceNameQuoted()+",'one',NULL)").execute();
        con.prepareStatement( "INSERT INTO \""+DB2TestUtil.SCHEMA+ "\".\"seqtable\" (\"key\", \"name\",\"geom\" ) VALUES (" +
            "next value for "+getSquenceNameQuoted()+",'two',NULL)").execute();
        con.prepareStatement( "INSERT INTO \""+DB2TestUtil.SCHEMA+ "\".\"seqtable\" (\"key\", \"name\",\"geom\" ) VALUES (" + 
             "next value for "+getSquenceNameQuoted()+",'three',NULL)").execute();

        con.prepareStatement("INSERT INTO \""+DB2TestUtil.SCHEMA.toUpperCase()+ "\".gt_pk_metadata VALUES"
                + "('"+ DB2TestUtil.SCHEMA+ "', 'seqtable', 'key', 0, 'sequence', '"+ getSquenceName()+"')").execute();
        con.close();
    }

    @Override
    protected void dropSequencedPrimaryKeyTable() throws Exception {
        Connection con = getDataSource().getConnection();
        try {                
                DB2Util.executeUnRegister(DB2TestUtil.SCHEMA, "seqtable", "geom",  con);
                DB2TestUtil.dropTable(DB2TestUtil.SCHEMA, "seqtable", con);
                DB2TestUtil.dropSequence(DB2TestUtil.SCHEMA, "pksequence", con);
        } catch (SQLException e) {              
        }        
        con.close();

    }

    @Override
    protected void createPlainTable() throws Exception {
        Connection con = getDataSource().getConnection();

        con.prepareStatement("CREATE TABLE \""+DB2TestUtil.SCHEMA+ "\".\"plaintable\" ( \"key1\" int, \"key2\" int, "
                + "\"name\" VARCHAR(256), \"geom\" db2gse.ST_GEOMETRY)").execute();
        
        DB2Util.executeRegister(DB2TestUtil.SCHEMA, "plaintable", "geom", DB2TestUtil.SRSNAME, con);
        

        con.prepareStatement("INSERT INTO \""+DB2TestUtil.SCHEMA+ "\".\"plaintable\" VALUES (1, 2, 'one', NULL)").execute();
        con.prepareStatement("INSERT INTO \""+DB2TestUtil.SCHEMA+ "\".\"plaintable\" VALUES (2, 3, 'two', NULL)").execute();
        con.prepareStatement("INSERT INTO \""+DB2TestUtil.SCHEMA+ "\".\"plaintable\" VALUES (3, 4, 'three', NULL)").execute();
        con.close();                

    }

    @Override
    protected void dropPlainTable() throws Exception {
        Connection con = getDataSource().getConnection();
        try {                
                DB2Util.executeUnRegister(DB2TestUtil.SCHEMA, "plaintable", "geom",  con);
                DB2TestUtil.dropTable(DB2TestUtil.SCHEMA, "plaintable", con);
        } catch (SQLException e) {              
        }        
        con.close();                
    }

    @Override
    protected void createAssignedSinglePkView() throws Exception {
        Connection con = getDataSource().getConnection();
        con.prepareStatement("CREATE VIEW \""+DB2TestUtil.SCHEMA+ "\".\"assignedsinglepk\" AS SELECT * FROM \""+DB2TestUtil.SCHEMA+ "\".\"plaintable\"").execute();
        con.prepareStatement("INSERT INTO \""+DB2TestUtil.SCHEMA.toUpperCase()+ "\".gt_pk_metadata VALUES"
                + "('"+DB2TestUtil.SCHEMA+ "', 'assignedsinglepk', 'key1', 0, 'assigned', NULL)").execute();
        con.close();
    }

    @Override
    protected void dropAssignedSinglePkView() throws Exception {
        Connection con = getDataSource().getConnection();
        try {                
                DB2TestUtil.dropView(DB2TestUtil.SCHEMA, "assignedsinglepk", con);
        } catch (SQLException e) {              
        }        
        con.close();
        
    }

    @Override
    protected void createAssignedMultiPkView() throws Exception {
        Connection con = getDataSource().getConnection();
        con.prepareStatement("CREATE VIEW \""+DB2TestUtil.SCHEMA+ "\".\"assignedmultipk\" AS SELECT * FROM \""+DB2TestUtil.SCHEMA+ "\".\"plaintable\"").execute();
        con.prepareStatement("INSERT INTO \""+DB2TestUtil.SCHEMA.toUpperCase()+ "\".gt_pk_metadata VALUES"
                + "('"+DB2TestUtil.SCHEMA+ "', 'assignedmultipk', 'key1', 0, 'assigned', NULL)").execute();
        con.prepareStatement("INSERT INTO \""+DB2TestUtil.SCHEMA.toUpperCase()+ "\".gt_pk_metadata VALUES"
                + "('"+DB2TestUtil.SCHEMA+ "', 'assignedmultipk', 'key2', 1, 'assigned', NULL)").execute();
        con.close();

    }

    @Override
    protected void dropAssignedMultiPkView() throws Exception {
        Connection con = getDataSource().getConnection();
        try {                
                DB2TestUtil.dropView(DB2TestUtil.SCHEMA, "assignedmultipk", con);
        } catch (SQLException e) {              
        }        
        con.close();
    }

    private String getSquenceName() {
        return "pksequence";
    }
    
    private String getSquenceNameQuoted() {
        return DB2TestUtil.SCHEMA_QUOTED+".\""+getSquenceName()+"\"";
    }

}
