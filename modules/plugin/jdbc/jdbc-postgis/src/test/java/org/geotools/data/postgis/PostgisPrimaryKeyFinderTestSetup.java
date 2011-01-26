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
package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCPrimaryKeyFinderTestSetup;

public class PostgisPrimaryKeyFinderTestSetup extends JDBCPrimaryKeyFinderTestSetup {

    protected PostgisPrimaryKeyFinderTestSetup() {
        super(new PostGISTestSetup());
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
        run("CREATE TABLE \"seqtable\" ( \"key\" integer PRIMARY KEY, "
                + "\"name\" VARCHAR(256))");
        run("SELECT AddGeometryColumn('seqtable', 'geom', -1, 'GEOMETRY', 2)");
        run("CREATE SEQUENCE pksequence START WITH 1");

        run( "INSERT INTO \"seqtable\" (\"key\", \"name\",\"geom\" ) VALUES (" + 
            "(SELECT NEXTVAL('PKSEQUENCE')),'one',NULL)");
        run( "INSERT INTO \"seqtable\" (\"key\", \"name\",\"geom\" ) VALUES (" +
            "(SELECT NEXTVAL('PKSEQUENCE')),'two',NULL)");
        run( "INSERT INTO \"seqtable\" (\"key\", \"name\",\"geom\" ) VALUES (" + 
            "(SELECT NEXTVAL('PKSEQUENCE')),'three',NULL)");

        run("INSERT INTO gt_pk_metadata VALUES"
                + "(NULL, 'seqtable', 'key', 0, 'sequence', 'PKSEQUENCE')");
    }

    @Override
    protected void dropSequencedPrimaryKeyTable() throws Exception {
        runSafe( "DROP TABLE \"seqtable\"");
        runSafe( "DROP SEQUENCE pksequence");
    }

    @Override
    protected void createPlainTable() throws Exception {
        run("CREATE TABLE \"plaintable\" ( \"key1\" int, \"key2\" int, "
                + "\"name\" VARCHAR(256))");
        run("SELECT AddGeometryColumn('plaintable', 'geom', -1, 'GEOMETRY', 2)");

        run("INSERT INTO \"plaintable\" VALUES (1, 2, 'one', NULL)");
        run("INSERT INTO \"plaintable\" VALUES (2, 3, 'two', NULL)");
        run("INSERT INTO \"plaintable\" VALUES (3, 4, 'three', NULL)");
    }

    @Override
    protected void dropPlainTable() throws Exception {
        runSafe("DROP TABLE \"plaintable\"");
    }

    @Override
    protected void createAssignedSinglePkView() throws Exception {
        run("CREATE VIEW \"assignedsinglepk\" AS SELECT * FROM \"plaintable\"");
        run("INSERT INTO gt_pk_metadata VALUES"
                + "(NULL, 'assignedsinglepk', 'key1', 0, 'assigned', NULL)");
    }

    @Override
    protected void dropAssignedSinglePkView() throws Exception {
        runSafe("DROP VIEW \"assignedsinglepk\"");
    }

    @Override
    protected void createAssignedMultiPkView() throws Exception {
        run("CREATE VIEW \"assignedmultipk\" AS SELECT * FROM \"plaintable\"");
        run("INSERT INTO gt_pk_metadata VALUES"
                + "(NULL, 'assignedmultipk', 'key1', 0, 'assigned', NULL)");
        run("INSERT INTO gt_pk_metadata VALUES"
                + "(NULL, 'assignedmultipk', 'key2', 1, 'assigned', NULL)");

    }

    @Override
    protected void dropAssignedMultiPkView() throws Exception {
        runSafe("DROP VIEW \"assignedmultipk\"");
    }

}
