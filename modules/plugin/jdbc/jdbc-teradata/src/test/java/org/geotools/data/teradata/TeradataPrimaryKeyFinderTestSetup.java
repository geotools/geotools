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
package org.geotools.data.teradata;

import org.geotools.jdbc.JDBCPrimaryKeyFinderTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class TeradataPrimaryKeyFinderTestSetup extends JDBCPrimaryKeyFinderTestSetup {

    public TeradataPrimaryKeyFinderTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    public TeradataTestSetup getDelegate() {
        return (TeradataTestSetup) delegate;
    }
    
    protected void createMetadataTable() throws Exception {
        run("CREATE TABLE gt_pk_metadata (" +
                "table_schema VARCHAR(32) NOT NULL, " +
                "table_name VARCHAR(32) NOT NULL, " +
                "pk_column VARCHAR(32) NOT NULL, " +
                "pk_column_idx INTEGER, " +
                "pk_policy VARCHAR(32), " +
                "pk_sequence VARCHAR(64), " +
                "unique (table_schema, table_name, pk_column)," +
                "check (pk_policy in ('sequence', 'assigned', 'autoincrement')))");
    }


    protected void dropMetadataTable() throws Exception {
        runSafe("DROP TABLE \"gt_pk_metadata\"");
    }

    protected void createSequencedPrimaryKeyTable() throws Exception {
        // not supported
    }


    protected void dropSequencedPrimaryKeyTable() throws Exception {
    }


    protected void createPlainTable() throws Exception {
        run("CREATE TABLE \"plaintable\" ( \"key1\" int, \"key2\" int, \"name\" VARCHAR(256), \"geom\" ST_GEOMETRY)");
        run("INSERT INTO SYSSPATIAL.GEOMETRY_COLUMNS (F_TABLE_CATALOG, F_TABLE_SCHEMA, F_TABLE_NAME," +
            " F_GEOMETRY_COLUMN, COORD_DIMENSION, SRID, GEOM_TYPE) VALUES ('','" 
                + fixture.getProperty("schema") + "','plaintable', 'geom', 2, " + getDelegate().getSrid4326() + ", 'GEOMETRY')");

        run("INSERT INTO \"plaintable\" VALUES (1, 2, 'one', NULL)");
        run("INSERT INTO \"plaintable\" VALUES (2, 3, 'two', NULL)");
        run("INSERT INTO \"plaintable\" VALUES (3, 4, 'three', NULL)");
    }


    protected void dropPlainTable() throws Exception {
        runSafe("DROP TABLE \"plaintable\"");
        runSafe("delete from SYSSPATIAL.GEOMETRY_COLUMNS where f_table_name='plaintable'");
    }


    protected void createAssignedSinglePkView() throws Exception {
        run("CREATE VIEW \"assignedsinglepk\" AS SELECT * FROM \"plaintable\"");
        run("INSERT INTO gt_pk_metadata VALUES('" + fixture.getProperty("schema") + "', 'assignedsinglepk', 'key1', 0, 'assigned', NULL)");
    }


    protected void dropAssignedSinglePkView() throws Exception {
        runSafe("DROP VIEW \"assignedsinglepk\"");
    }


    protected void createAssignedMultiPkView() throws Exception {
        run("CREATE VIEW \"assignedmultipk\" AS SELECT * FROM \"plaintable\"");
        run("INSERT INTO gt_pk_metadata VALUES('" + fixture.getProperty("schema") + "', 'assignedmultipk', 'key1', 0, 'assigned', NULL)");
        run("INSERT INTO gt_pk_metadata VALUES('" + fixture.getProperty("schema") + "', 'assignedmultipk', 'key2', 1, 'assigned', NULL)");

    }


    protected void dropAssignedMultiPkView() throws Exception {
        runSafe("DROP VIEW \"assignedmultipk\"");
    }

}
