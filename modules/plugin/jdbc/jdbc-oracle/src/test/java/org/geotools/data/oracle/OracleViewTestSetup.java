package org.geotools.data.oracle;

import org.geotools.jdbc.JDBCViewTestSetup;

public class OracleViewTestSetup extends JDBCViewTestSetup {
    
    protected OracleViewTestSetup() {
        super(new OracleTestSetup());
    }


    protected void createLakesTable() throws Exception {
        //set up table
        run("CREATE TABLE lakes (fid int primary key, id int, "
            + "geom MDSYS.SDO_GEOMETRY, name varchar(255))");
        run("INSERT INTO USER_SDO_GEOM_METADATA (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID)" 
                + " VALUES ('lakes','geom',MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X',-180,180,0.5), " 
                + "MDSYS.SDO_DIM_ELEMENT('Y',-90,90,0.5)), 4326)");   
        run("CREATE INDEX LAKES_GEOM_IDX ON lakes(GEOM) INDEXTYPE IS MDSYS.SPATIAL_INDEX" //
                + " PARAMETERS ('SDO_INDX_DIMS=2 LAYER_GTYPE=\"POLYGON\"')");
        
        //insert data
        run("INSERT INTO lakes (fid,id,geom,name) VALUES (0, 0,"
            + "MDSYS.SDO_GEOMETRY( 2003, 4326, NULL, SDO_ELEM_INFO_ARRAY(1,1003,1), "
            + "SDO_ORDINATE_ARRAY(12,6, 14,8, 16,6, 16,4, 14,4, 12,6)), 'muddy')");
    }
    

    @Override
    protected void dropLakesTable() throws Exception {
        runSafe( "DELETE FROM USER_SDO_GEOM_METADATA WHERE TABLE_NAME = 'LAKES'" );
        runSafe( "DROP TABLE lakes PURGE");
    }

    @Override
    protected void createLakesView() throws Exception {
        run("CREATE VIEW LAKESVIEW AS SELECT * FROM LAKES");
        run("INSERT INTO USER_SDO_GEOM_METADATA (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID)" 
                + " VALUES ('LAKESVIEW','geom',MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X',-180,180,0.5), " 
                + "MDSYS.SDO_DIM_ELEMENT('Y',-90,90,0.5)), 4326)");   
    }

    @Override
    protected void dropLakesView() throws Exception {
        runSafe( "DELETE FROM USER_SDO_GEOM_METADATA WHERE TABLE_NAME = 'LAKESVIEW'" );
        runSafe( "DROP VIEW LAKESVIEW");


    }


    @Override
    protected void createLakesViewPk() throws Exception {
        run("CREATE VIEW LAKESVIEWPK (FID, ID, GEOM, NAME UNIQUE RELY DISABLE NOVALIDATE, " +
        		"PRIMARY KEY (FID) RELY DISABLE NOVALIDATE) AS SELECT * FROM LAKES");
        run("INSERT INTO USER_SDO_GEOM_METADATA (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID)" 
                + " VALUES ('lakesviewpk','geom',MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X',-180,180,0.5), " 
                + "MDSYS.SDO_DIM_ELEMENT('Y',-90,90,0.5)), 4326)");   
        
    }


    @Override
    protected void dropLakesViewPk() throws Exception {
        runSafe( "DELETE FROM USER_SDO_GEOM_METADATA WHERE TABLE_NAME = 'LAKESVIEWPK'" );
        runSafe( "DROP VIEW LAKESVIEWPK");
    }

}
