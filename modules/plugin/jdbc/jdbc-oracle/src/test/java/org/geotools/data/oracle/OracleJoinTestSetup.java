/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.jdbc.JDBCJoinTestSetup;
    
public class OracleJoinTestSetup extends JDBCJoinTestSetup {

    public OracleJoinTestSetup() {
        super(new OracleTestSetup());
    }

    @Override
    protected void createJoinTable() throws Exception {
        String sql = "CREATE TABLE ftjoin (" 
                + "id INT PRIMARY KEY, name VARCHAR(255), geom MDSYS.SDO_GEOMETRY, join1intProperty INT)";
        run(sql);
        
        sql = "INSERT INTO USER_SDO_GEOM_METADATA (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID ) " + 
         "VALUES ('ftjoin','geom',MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X',-180,180,0.5), " + 
         "MDSYS.SDO_DIM_ELEMENT('Y',-90,90,0.5)), 4326)";
        run(sql);
        
        sql = "CREATE INDEX ftjoin_GEOMETRY_IDX ON FTJOIN(GEOM) INDEXTYPE IS MDSYS.SPATIAL_INDEX" //
                        + " PARAMETERS ('SDO_INDX_DIMS=2 LAYER_GTYPE=\"POLYGON\"')";
        run(sql);
        
        sql = "INSERT INTO ftjoin VALUES (0, 'zero', MDSYS.SDO_GEOMETRY(2003, 4326, NULL," +  
           " MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,1), " +  
           " MDSYS.SDO_ORDINATE_ARRAY(-0.1,-0.1, -0.1,0.1, 0.1,0.1, 0.1,-0.1, -0.1,-0.1)), 0)";    
        run(sql);
        
        sql = "INSERT INTO ftjoin VALUES (1, 'one', MDSYS.SDO_GEOMETRY(2003, 4326, NULL," +  
            " MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,1), " +  
            " MDSYS.SDO_ORDINATE_ARRAY(-1.1,-1.1, -1.1,1.1, 1.1,1.1, 1.1,-1.1, -1.1,-1.1)), 1)";
        run(sql);

        sql = "INSERT INTO ftjoin VALUES (2, 'two', MDSYS.SDO_GEOMETRY(2003, 4326, NULL," +  
        " MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,1), " +  
        " MDSYS.SDO_ORDINATE_ARRAY(-10,-10, -10,10, 10,10, 10,-10, -10,-10)), 2)";
        run(sql);
        
        sql = "INSERT INTO ftjoin VALUES (3, 'three', NULL, 3)";
        run(sql);
        
        run("CREATE TABLE ftjoin2( id INT PRIMARY KEY, join2intProperty INT, stringProperty2 VARCHAR(255))");
        run( "INSERT INTO ftjoin2 VALUES (0, 0, '2nd zero')");
        run( "INSERT INTO ftjoin2 VALUES (1, 1, '2nd one')");
        run( "INSERT INTO ftjoin2 VALUES (2, 2, '2nd two')");
        run( "INSERT INTO ftjoin2 VALUES (3, 3, '2nd three')");
        
    }

    @Override
    protected void dropJoinTable() throws Exception {
        runSafe("DELETE FROM USER_SDO_GEOM_METADATA WHERE TABLE_NAME = 'FTJOIN'");
        runSafe("DROP TABLE ftjoin purge");
        runSafe("DROP TABLE ftjoin2 purge");
    }

}
