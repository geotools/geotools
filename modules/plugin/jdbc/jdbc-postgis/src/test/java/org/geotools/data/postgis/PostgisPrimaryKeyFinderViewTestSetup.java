package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCPrimaryKeyFinderViewTestSetup;

public class PostgisPrimaryKeyFinderViewTestSetup extends JDBCPrimaryKeyFinderViewTestSetup {

    protected PostgisPrimaryKeyFinderViewTestSetup() {
        super(new PostGISTestSetup());
    }

    @Override
    protected void dropMetadataTable() throws Exception {
        runSafe("DROP TABLE gt_pk_metadata");
        runSafe("DROP TABLE tbl_gt_pk_metadata");
    }

    @Override
    protected void dropMetadataView() throws Exception {
        runSafe("DROP VIEW gt_pk_metadata");
    }

    @Override
    protected void dropSequencedPrimaryKeyTable() throws Exception {
        runSafe("DROP TABLE \"seqtable\"");
        runSafe("DROP SEQUENCE pksequence");
    }

    @Override
    protected void createMetadataTable() throws Exception {
        run(
                "CREATE TABLE tbl_gt_pk_metadata ( "
                        + "table_schema VARCHAR(32), "
                        + "table_name VARCHAR(32) NOT NULL, "
                        + "pk_column VARCHAR(32) NOT NULL, "
                        + "pk_column_idx INTEGER, "
                        + "pk_policy VARCHAR(32), "
                        + "pk_sequence VARCHAR(64),"
                        + "unique (table_schema, table_name, pk_column),"
                        + "check (pk_policy in ('sequence', 'assigned', 'autoincrement')))");
    }

    @Override
    protected void createMetadataView() throws Exception {
        run("CREATE VIEW gt_pk_metadata AS SELECT * FROM tbl_gt_pk_metadata");
    }

    @Override
    protected void createSequencedPrimaryKeyTable() throws Exception {
        run("CREATE TABLE \"seqtable\" ( \"key\" integer PRIMARY KEY, " + "\"name\" VARCHAR(256))");
        run("SELECT AddGeometryColumn('seqtable', 'geom', -1, 'GEOMETRY', 2)");
        run("CREATE SEQUENCE pksequence START WITH 1");

        run(
                "INSERT INTO \"seqtable\" (\"key\", \"name\",\"geom\" ) VALUES ("
                        + "(SELECT NEXTVAL('PKSEQUENCE')),'one',NULL)");
        run(
                "INSERT INTO \"seqtable\" (\"key\", \"name\",\"geom\" ) VALUES ("
                        + "(SELECT NEXTVAL('PKSEQUENCE')),'two',NULL)");
        run(
                "INSERT INTO \"seqtable\" (\"key\", \"name\",\"geom\" ) VALUES ("
                        + "(SELECT NEXTVAL('PKSEQUENCE')),'three',NULL)");

        run(
                "INSERT INTO tbl_gt_pk_metadata VALUES"
                        + "(NULL, 'seqtable', 'key', 0, 'sequence', 'PKSEQUENCE')");
    }
}
