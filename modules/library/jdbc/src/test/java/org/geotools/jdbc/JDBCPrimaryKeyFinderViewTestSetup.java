package org.geotools.jdbc;

import java.sql.SQLException;

public abstract class JDBCPrimaryKeyFinderViewTestSetup extends JDBCDelegatingTestSetup {

    protected JDBCPrimaryKeyFinderViewTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected final void setUpData() throws Exception {
        // kill all the data
        try {
            dropMetadataView();
        } catch (SQLException e) {
        }
        try {
            dropMetadataTable();
        } catch (SQLException e) {
        }
        try {
            dropSequencedPrimaryKeyTable();
        } catch (SQLException e) {
        }

        // create all the data
        createMetadataTable();
        createSequencedPrimaryKeyTable();
        createMetadataView();
    }

    /** Drops the metadata table */
    protected abstract void dropMetadataTable() throws Exception;

    /** Drops the metadata view */
    protected abstract void dropMetadataView() throws Exception;

    /** Drops the "seqtable" table. */
    protected abstract void dropSequencedPrimaryKeyTable() throws Exception;

    /**
     * Creates the default geotools metadata table, empty.
     *
     * <p>The table is named "tbl_gt_pk_metadata". See {@link MetadataTablePrimaryKeyFinder} javadoc
     * for the expected table structure
     */
    protected abstract void createMetadataTable() throws Exception;

    /** Creates a view named 'gt_pk_metadata' which queries from 'tbl_gt_pk_metadata' */
    protected abstract void createMetadataView() throws Exception;

    /**
     * Creates a table with a primary key column, a sequence name 'pksequence", and links the two
     * using the primary key metadata table
     *
     * <p>seqtable( name:String; geom:Geometry; )
     *
     * <p>The table should be populated with the following data: "one" | NULL ; pkey = 1 "two" |
     * NULL ; pkey = 2 "three" | NULL ; pkey = 3
     */
    protected abstract void createSequencedPrimaryKeyTable() throws Exception;
}
