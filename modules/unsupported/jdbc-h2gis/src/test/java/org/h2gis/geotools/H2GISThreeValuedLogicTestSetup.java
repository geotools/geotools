package org.h2gis.geotools;

import org.geotools.jdbc.JDBCThreeValuedLogicTestSetup;

@SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation") // not yet a JUnit4 test
public class H2GISThreeValuedLogicTestSetup extends JDBCThreeValuedLogicTestSetup {

    public H2GISThreeValuedLogicTestSetup() {
        super(new H2GISTestSetup());
    }

    @Override
    protected void createAbcTable() throws Exception {
        run(
                "CREATE TABLE \"geotools\".\"abc\"(\"name\" varchar(10), \"a\" int, \"b\" int, \"c\" int)");
        run("INSERT INTO \"geotools\".\"abc\" VALUES('n_n_n', null, null, null)");
        run("INSERT INTO \"geotools\".\"abc\" VALUES('a_b_c', 1, 2, 3)");
    }

    /** Drops the "testlob" table */
    @Override
    protected void dropAbcTable() throws Exception {
        runSafe("DROP TABLE \"geotools\".\"abc\"");
    }
}
