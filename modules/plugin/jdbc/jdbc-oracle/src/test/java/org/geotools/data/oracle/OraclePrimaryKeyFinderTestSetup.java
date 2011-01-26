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

import org.geotools.jdbc.JDBCPrimaryKeyFinderTestSetup;

public class OraclePrimaryKeyFinderTestSetup extends JDBCPrimaryKeyFinderTestSetup {

    protected OraclePrimaryKeyFinderTestSetup() {
        super(new OracleTestSetup());
    }

    @Override
    protected void createMetadataTable() throws Exception {
        run("CREATE TABLE gt_pk_metadata ( " + "table_schema VARCHAR(32), "
                + "table_name VARCHAR(32) NOT NULL, " + "pk_column VARCHAR(32) NOT NULL, "
                + "pk_column_idx INTEGER, " + "pk_policy VARCHAR(32), " + "pk_sequence VARCHAR(64)," 
                + "unique (table_schema, table_name, pk_column)," 
                + "check (pk_policy in ('sequence', 'assigned', 'autoincrement')))");
    }

    @Override
    protected void dropMetadataTable() throws Exception {
        runSafe("DROP TABLE gt_pk_metadata");
    }

    @Override
    protected void createSequencedPrimaryKeyTable() throws Exception {
        run("CREATE TABLE seqtable ( key integer PRIMARY KEY, "
                + "name VARCHAR(256), geom MDSYS.SDO_GEOMETRY)");
        run( "INSERT INTO USER_SDO_GEOM_METADATA (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID )" + 
                " VALUES ('seqtable','geom',MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X',-180,180,0.5), " + 
                "MDSYS.SDO_DIM_ELEMENT('Y',-90,90,0.5)), 4326)");
        run("CREATE SEQUENCE pksequence START WITH 1");

        run( "INSERT INTO seqtable VALUES (pksequence.NEXTVAL,'one',NULL)" );
        run( "INSERT INTO seqtable VALUES (pksequence.NEXTVAL,'two',NULL)" );
        run( "INSERT INTO seqtable VALUES (pksequence.NEXTVAL,'three',NULL)" );

        run("INSERT INTO gt_pk_metadata VALUES"
                + "(NULL, 'SEQTABLE', 'KEY', 0, 'sequence', 'PKSEQUENCE')");
    }

    @Override
    protected void dropSequencedPrimaryKeyTable() throws Exception {
        runSafe( "DROP TABLE seqtable PURGE");
        runSafe( "DELETE FROM USER_SDO_GEOM_METADATA WHERE TABLE_NAME = 'SEQTABLE'");
        runSafe( "DROP SEQUENCE pksequence");
    }

    @Override
    protected void createPlainTable() throws Exception {
        run("CREATE TABLE plaintable ( key1 int, key2 int, "
                + "name VARCHAR(256), geom MDSYS.SDO_GEOMETRY )");
        run( "INSERT INTO USER_SDO_GEOM_METADATA (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID )" + 
                " VALUES ('plaintable','geom',MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X',-180,180,0.5), " + 
                "MDSYS.SDO_DIM_ELEMENT('Y',-90,90,0.5)), 4326)");

        run("INSERT INTO plaintable VALUES (1, 2, 'one', NULL)");
        run("INSERT INTO plaintable VALUES (2, 3, 'two', NULL)");
        run("INSERT INTO plaintable VALUES (3, 4, 'three', NULL)");
    }

    @Override
    protected void dropPlainTable() throws Exception {
        runSafe("DROP TABLE plaintable");
        runSafe( "DELETE FROM USER_SDO_GEOM_METADATA WHERE TABLE_NAME = 'PLAINTABLE'");
    }

    @Override
    protected void createAssignedSinglePkView() throws Exception {
        run("CREATE VIEW assignedsinglepk AS SELECT * FROM plaintable");
        run("INSERT INTO gt_pk_metadata VALUES"
                + "(NULL, 'ASSIGNEDSINGLEPK', 'KEY1', 0, 'assigned', NULL)");
    }

    @Override
    protected void dropAssignedSinglePkView() throws Exception {
        runSafe("DROP VIEW assignedsinglepk");
    }

    @Override
    protected void createAssignedMultiPkView() throws Exception {
        run("CREATE VIEW assignedmultipk AS SELECT * FROM plaintable");
        run("INSERT INTO gt_pk_metadata VALUES"
                + "(NULL, 'ASSIGNEDMULTIPK', 'KEY1', 0, 'assigned', NULL)");
        run("INSERT INTO gt_pk_metadata VALUES"
                + "(NULL, 'ASSIGNEDMULTIPK', 'KEY2', 1, 'assigned', NULL)");

    }

    @Override
    protected void dropAssignedMultiPkView() throws Exception {
        runSafe("DROP VIEW assignedmultipk");
    }

}
