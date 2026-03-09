/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.duckdb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.geotools.data.duckdb.security.DuckDBExecutionPolicy;
import org.geotools.jdbc.JDBCPrimaryKeyFinderTestSetup;

public class DuckDBPrimaryKeyFinderTestSetup extends JDBCPrimaryKeyFinderTestSetup {

    protected DuckDBPrimaryKeyFinderTestSetup() {
        super(new DuckDBTestSetup());
    }

    @Override
    protected Connection getConnection() throws SQLException, IOException {
        return ((DuckDBTestSetup) delegate).getConnection();
    }

    @Override
    protected void dropMetadataTable() throws Exception {
        runSafe("DROP VIEW IF EXISTS \"gt_pk_metadata\"");
        runSafe("DROP TABLE IF EXISTS \"gt_pk_metadata\"");
    }

    @Override
    protected void dropAssignedSinglePkView() throws Exception {
        runSafe("DROP VIEW IF EXISTS \"assignedsinglepk\"");
        runSafe("DROP TABLE IF EXISTS \"assignedsinglepk\"");
    }

    @Override
    protected void dropAssignedMultiPkView() throws Exception {
        runSafe("DROP VIEW IF EXISTS \"assignedmultipk\"");
        runSafe("DROP TABLE IF EXISTS \"assignedmultipk\"");
    }

    @Override
    protected void dropPlainTable() throws Exception {
        ((DuckDBTestSetup) delegate).removeTable("plaintable");
    }

    @Override
    protected void dropSequencedPrimaryKeyTable() throws Exception {
        ((DuckDBTestSetup) delegate).removeTable("seqtable");
        runSafe("DROP SEQUENCE IF EXISTS \"pksequence\"");
    }

    @Override
    protected void createMetadataTable() throws Exception {
        run("CREATE TABLE \"gt_pk_metadata\" ("
                + "\"table_schema\" VARCHAR, "
                + "\"table_name\" VARCHAR NOT NULL, "
                + "\"pk_column\" VARCHAR NOT NULL, "
                + "\"pk_column_idx\" INTEGER, "
                + "\"pk_policy\" VARCHAR, "
                + "\"pk_sequence\" VARCHAR)");
    }

    @Override
    protected void createPlainTable() throws Exception {
        run("CREATE TABLE \"plaintable\" ("
                + "\"key1\" INTEGER, "
                + "\"key2\" INTEGER, "
                + "\"name\" VARCHAR, "
                + "\"geom\" GEOMETRY)");
        run("INSERT INTO \"plaintable\" VALUES (1, 2, 'one', NULL)");
        run("INSERT INTO \"plaintable\" VALUES (2, 3, 'two', NULL)");
        run("INSERT INTO \"plaintable\" VALUES (3, 4, 'three', NULL)");
    }

    @Override
    protected void createAssignedSinglePkView() throws Exception {
        run("CREATE VIEW \"assignedsinglepk\" AS SELECT * FROM \"plaintable\"");
        run("INSERT INTO \"gt_pk_metadata\" VALUES " + "(NULL, 'assignedsinglepk', 'key1', 0, 'assigned', NULL)");
    }

    @Override
    protected void createAssignedMultiPkView() throws Exception {
        run("CREATE VIEW \"assignedmultipk\" AS SELECT * FROM \"plaintable\"");
        run("INSERT INTO \"gt_pk_metadata\" VALUES " + "(NULL, 'assignedmultipk', 'key1', 0, 'assigned', NULL)");
        run("INSERT INTO \"gt_pk_metadata\" VALUES " + "(NULL, 'assignedmultipk', 'key2', 1, 'assigned', NULL)");
    }

    @Override
    protected void createSequencedPrimaryKeyTable() throws Exception {
        run("CREATE SEQUENCE \"pksequence\" START 1");
        run("CREATE TABLE \"seqtable\" ("
                + "\"key\" INTEGER PRIMARY KEY, "
                + "\"name\" VARCHAR, "
                + "\"geom\" GEOMETRY)");
        run("INSERT INTO \"seqtable\" (\"key\", \"name\", \"geom\") " + "VALUES (nextval('pksequence'), 'one', NULL)");
        run("INSERT INTO \"seqtable\" (\"key\", \"name\", \"geom\") " + "VALUES (nextval('pksequence'), 'two', NULL)");
        run("INSERT INTO \"seqtable\" (\"key\", \"name\", \"geom\") "
                + "VALUES (nextval('pksequence'), 'three', NULL)");
        run("INSERT INTO \"gt_pk_metadata\" VALUES " + "(NULL, 'seqtable', 'key', 0, 'sequence', 'pksequence')");
    }

    @Override
    protected void run(String input) throws Exception {
        withSetupSqlPolicy(() -> {
            super.run(input);
            return null;
        });
    }

    @FunctionalInterface
    private interface SqlCallable<T> {
        T call() throws Exception;
    }

    private <T> T withSetupSqlPolicy(SqlCallable<T> callable) throws Exception {
        DuckDBTestSetup setup = (DuckDBTestSetup) delegate;
        DuckDBExecutionPolicy previous = setup.beginSetupSqlPolicy();
        try {
            return callable.call();
        } finally {
            setup.endSetupSqlPolicy(previous);
        }
    }
}
