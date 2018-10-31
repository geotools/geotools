/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.jdbc.JDBCAggregateTestSetup;

public class OracleAggregateTestSetup extends JDBCAggregateTestSetup {

    public OracleAggregateTestSetup() {
        super(new OracleTestSetup());
    }

    @Override
    protected void createAggregateTable() throws Exception {
        // set up table
        run(
                "CREATE TABLE aggregate (fid int, id int, "
                        + "geom MDSYS.SDO_GEOMETRY, name varchar(255), PRIMARY KEY (fid) )");
        run("CREATE SEQUENCE aggregate_fid_seq START WITH 0 MINVALUE 0");
        run(
                "INSERT INTO USER_SDO_GEOM_METADATA (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID)"
                        + " VALUES ('aggregate','geom',MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X',-180,180,0.5), "
                        + "MDSYS.SDO_DIM_ELEMENT('Y',-90,90,0.5)), 303104)");
        run(
                "CREATE INDEX AGGREGATE_GEOM_IDX ON aggregate(GEOM) INDEXTYPE IS MDSYS.SPATIAL_INDEX" //
                        + " PARAMETERS ('SDO_INDX_DIMS=2 LAYER_GTYPE=\"POLYGON\"')");

        // insert data
        run(
                "INSERT INTO aggregate (fid,id,geom,name) VALUES (aggregate_fid_seq.nextval, 0,"
                        + "MDSYS.SDO_GEOMETRY( 2003, 303104, NULL, SDO_ELEM_INFO_ARRAY(1,1003,1), "
                        + "SDO_ORDINATE_ARRAY(12,6, 14,8, 16,6, 16,4, 14,4, 12,6)), 'muddy1')");
        run(
                "INSERT INTO aggregate (fid,id,geom,name) VALUES (aggregate_fid_seq.nextval, 1,"
                        + "MDSYS.SDO_GEOMETRY( 2003, 303104, NULL, SDO_ELEM_INFO_ARRAY(1,1003,1), "
                        + "SDO_ORDINATE_ARRAY(12,6, 14,8, 16,6, 16,4, 14,4, 12,6)), 'muddy1')");
        run(
                "INSERT INTO aggregate (fid,id,geom,name) VALUES (aggregate_fid_seq.nextval, 2,"
                        + "MDSYS.SDO_GEOMETRY( 2003, 303104, NULL, SDO_ELEM_INFO_ARRAY(1,1003,1), "
                        + "SDO_ORDINATE_ARRAY(12,6, 14,8, 16,6, 16,4, 14,4, 12,6)), 'muddy2')");
    }

    @Override
    protected void dropAggregateTable() throws Exception {
        runSafe("DROP TRIGGER aggregate_pkey_trigger");
        runSafe("DROP SEQUENCE aggregate_fid_seq");
        runSafe("DELETE FROM USER_SDO_GEOM_METADATA WHERE TABLE_NAME = 'AGGREGATE'");
        runSafe("DROP TABLE aggregate PURGE");
    }
}
