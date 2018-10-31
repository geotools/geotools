package org.geotools.jdbc;

import java.sql.SQLException;

/**
 * This test setup is so simple that subclasses may not be necessary, but just in case, we provide
 * the ability to subclass
 */
public class JDBCThreeValuedLogicTestSetup extends JDBCDelegatingTestSetup {

    public JDBCThreeValuedLogicTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    protected final void setUpData() throws Exception {
        // kill all the data
        try {
            dropAbcTable();
        } catch (SQLException e) {
        }

        // create all the data
        createAbcTable();
    }

    /**
     * Creates a table with the following schema:
     *
     * <p>abc( name: string; a : integer; b : integer; c : integer)
     *
     * <p>The table should be populated with the following data n_n_n | null | null | null a_b_c | 1
     * | 2 | 3 Where [0,1,2,3,4,5] is a byte[]
     */
    protected void createAbcTable() throws Exception {
        run("CREATE TABLE \"abc\"(\"name\" varchar(10), \"a\" int, \"b\" int, \"c\" int)");
        run("INSERT INTO \"abc\" VALUES('n_n_n', null, null, null)");
        run("INSERT INTO \"abc\" VALUES('a_b_c', 1, 2, 3)");
    }

    /** Drops the "testlob" table */
    protected void dropAbcTable() throws Exception {
        runSafe("DROP TABLE \"abc\"");
    }
}
