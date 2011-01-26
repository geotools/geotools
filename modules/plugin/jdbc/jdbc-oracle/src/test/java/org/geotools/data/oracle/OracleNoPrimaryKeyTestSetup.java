package org.geotools.data.oracle;

import org.geotools.jdbc.JDBCNoPrimaryKeyTestSetup;

public class OracleNoPrimaryKeyTestSetup extends JDBCNoPrimaryKeyTestSetup {

    protected OracleNoPrimaryKeyTestSetup() {
        super(new OracleTestSetup());
    }

    protected void createLakeTable() throws Exception {
        //set up table
        run("CREATE TABLE lake (id int, "
            + "geom MDSYS.SDO_GEOMETRY, name varchar(255))");
        run("INSERT INTO USER_SDO_GEOM_METADATA (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID)" 
                + " VALUES ('lake','geom',MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X',-180,180,0.5), " 
                + "MDSYS.SDO_DIM_ELEMENT('Y',-90,90,0.5)), 4326)");   
        run("CREATE INDEX LAKE_GEOM_IDX ON lake(GEOM) INDEXTYPE IS MDSYS.SPATIAL_INDEX" //
                + " PARAMETERS ('SDO_INDX_DIMS=2 LAYER_GTYPE=\"POLYGON\"')");
        
        //insert data
        run("INSERT INTO lake (id,geom,name) VALUES ( 0,"
            + "MDSYS.SDO_GEOMETRY( 2003, 4326, NULL, SDO_ELEM_INFO_ARRAY(1,1003,1), "
            + "SDO_ORDINATE_ARRAY(12,6, 14,8, 16,6, 16,4, 14,4, 12,6)), 'muddy')");
    }
    

    @Override
    protected void dropLakeTable() throws Exception {
        runSafe( "DELETE FROM USER_SDO_GEOM_METADATA WHERE TABLE_NAME = 'LAKE'" );
        runSafe( "DROP TABLE lake PURGE");
    }

}
