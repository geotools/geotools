package org.geotools.data.oracle;

import org.geotools.jdbc.JDBCEmptyTestSetup;

public class OracleEmptyTestSetup extends JDBCEmptyTestSetup {

    protected OracleEmptyTestSetup() {
        super(new OracleTestSetup());
    }

    @Override
    protected void createEmptyTable() throws Exception {
        String sql = "CREATE TABLE empty (" 
            + "id INT, geom MDSYS.SDO_GEOMETRY, PRIMARY KEY(id))";
        run(sql);
        sql = "CREATE SEQUENCE empty_id_seq";
        run(sql);
        
        sql = "INSERT INTO USER_SDO_GEOM_METADATA (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID ) " + 
         "VALUES ('EMPTY','GEOM',MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X',-180,180,0.5), " + 
         "MDSYS.SDO_DIM_ELEMENT('Y',-90,90,0.5)), 4326)";
        run(sql);
        
        sql = "CREATE INDEX EMPTY_GEOMETRY_IDX ON EMPTY(GEOM) INDEXTYPE IS MDSYS.SPATIAL_INDEX" //
                        + " PARAMETERS ('SDO_INDX_DIMS=2 LAYER_GTYPE=\"POINT\"')";
        run(sql);
    }

    @Override
    protected void dropEmptyTable() throws Exception {
        try {
            run("DROP TRIGGER empty_pkey_trigger");
        } catch (Exception e) {
        }
        try {
            run("DROP TABLE empty purge");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            run("DROP SEQUENCE empty_id_seq");
        } catch (Exception e) {
        }
     
        run("DELETE FROM USER_SDO_GEOM_METADATA WHERE TABLE_NAME = 'EMPTY'");
    }

}
