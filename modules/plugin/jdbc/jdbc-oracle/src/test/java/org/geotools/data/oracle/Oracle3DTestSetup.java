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
package org.geotools.data.oracle;

import org.geotools.jdbc.JDBC3DTestSetup;

public class Oracle3DTestSetup extends JDBC3DTestSetup {

    protected Oracle3DTestSetup() {
        super(new OracleTestSetup());
        
    }

    @Override
    protected void createLine3DTable() throws Exception {
        //set up table
        run("CREATE TABLE line3d (fid int, id int, "
            + " geom MDSYS.SDO_GEOMETRY, name VARCHAR(20), PRIMARY KEY (fid) )");
        run("CREATE SEQUENCE line3d_fid_seq START WITH 0 MINVALUE 0");
        run("INSERT INTO USER_SDO_GEOM_METADATA (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID)" 
                + " VALUES ('line3d','geom',MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X',-180,180,0.5), " 
                + "MDSYS.SDO_DIM_ELEMENT('Y',-90,90,0.5), MDSYS.SDO_DIM_ELEMENT('Z',-1000,1000,0.5)), 4326)");   
        run("CREATE INDEX LINE3D_GEOM_IDX ON LINE3D(GEOM) INDEXTYPE IS MDSYS.SPATIAL_INDEX" //
                + " PARAMETERS ('SDO_INDX_DIMS=2 LAYER_GTYPE=\"LINE\"')");
        
        // insert data
        run("INSERT INTO line3d(fid,id,geom,name) VALUES (line3d_fid_seq.nextval, 0,"
                + "MDSYS.SDO_GEOMETRY( 3002, 4326, NULL, MDSYS.SDO_ELEM_INFO_ARRAY(1,2,1), "
                + "MDSYS.SDO_ORDINATE_ARRAY(1, 1, 0, 2, 2, 0, 4, 2, 1, 5, 1, 1)), 'l1')");
        run("INSERT INTO line3d(fid,id,geom,name) VALUES (line3d_fid_seq.nextval, 1,"
                + "MDSYS.SDO_GEOMETRY( 3002, 4326, NULL, MDSYS.SDO_ELEM_INFO_ARRAY(1,2,1), "
                + "MDSYS.SDO_ORDINATE_ARRAY(3, 0, 1, 3, 2, 2, 3, 3, 3, 3, 4, 5)), 'l2')");
    }

    @Override
    protected void createPoint3DTable() throws Exception {
      //set up table
        run("CREATE TABLE point3d (fid int, id int, "
            + " geom MDSYS.SDO_GEOMETRY, name VARCHAR(20), PRIMARY KEY (fid) )");
        run("CREATE SEQUENCE point3d_fid_seq START WITH 0 MINVALUE 0");
        run("INSERT INTO USER_SDO_GEOM_METADATA (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID)" 
                + " VALUES ('point3d','geom',MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X',-180,180,0.5), " 
                + "MDSYS.SDO_DIM_ELEMENT('Y',-90,90,0.5), MDSYS.SDO_DIM_ELEMENT('Z',-1000,1000,0.5)), 4326)");
        // the data is 3d, but this is a geodetic index
        run("CREATE INDEX POINT3D_GEOM_IDX ON POINT3D(GEOM) INDEXTYPE IS MDSYS.SPATIAL_INDEX" //
                + " PARAMETERS ('SDO_INDX_DIMS=2 LAYER_GTYPE=\"POINT\"')");
        
        // insert data
        run("INSERT INTO point3d(fid, id,geom,name) VALUES (point3d_fid_seq.nextval, 0,"
                + "MDSYS.SDO_GEOMETRY(3001, 4326, NULL, MDSYS.SDO_ELEM_INFO_ARRAY(1,1,1), "
                + "MDSYS.SDO_ORDINATE_ARRAY(1, 1, 1)), 'p1')");
        run("INSERT INTO point3d(fid, id,geom,name) VALUES (point3d_fid_seq.nextval, 1,"
                + "MDSYS.SDO_GEOMETRY(3001, 4326, NULL, MDSYS.SDO_ELEM_INFO_ARRAY(1,1,1), "
                + "MDSYS.SDO_ORDINATE_ARRAY(3, 0, 1)), 'p2')");
    }

    @Override
    protected void dropLine3DTable() throws Exception {
        runSafe("DROP SEQUENCE line3d_fid_seq");
        runSafe("DROP TABLE line3d");
        runSafe("DELETE FROM USER_SDO_GEOM_METADATA WHERE TABLE_NAME='LINE3D'");
    }

    @Override
    protected void dropPoly3DTable() throws Exception {
        runSafe("DROP SEQUENCE poly3d_fid_seq");
        runSafe("DROP TABLE poly3d");
        runSafe("DELETE FROM USER_SDO_GEOM_METADATA WHERE TABLE_NAME='POLY3D'");
    }

    @Override
    protected void dropPoint3DTable() throws Exception {
        runSafe("DROP SEQUENCE point3d_fid_seq");
        runSafe("DROP TABLE point3d");
        runSafe("DELETE FROM USER_SDO_GEOM_METADATA WHERE TABLE_NAME='POINT3D'");
    }

    
    

}
