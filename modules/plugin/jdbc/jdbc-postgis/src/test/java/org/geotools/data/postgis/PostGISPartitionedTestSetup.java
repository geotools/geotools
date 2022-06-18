package org.geotools.data.postgis;

import java.sql.Connection;
import java.sql.SQLException;
import org.geotools.jdbc.JDBCDelegatingTestSetup;
import org.geotools.util.Version;

@SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation") // not yet a JUnit4 test
public class PostGISPartitionedTestSetup extends JDBCDelegatingTestSetup {

    protected PostGISPartitionedTestSetup() {
        super(new PostGISTestSetup());
    }

    public boolean isPgsqlVersionGreaterThanEqualTo(Version v) {

        PostGISTestSetup postGISTestSetup = (PostGISTestSetup) delegate;

        boolean var =
                postGISTestSetup.pgsqlVersion != null
                        && postGISTestSetup.pgsqlVersion.compareTo(v) >= 0;
        return var;
    }

    @Override
    public boolean shouldRunTests(Connection cx) throws SQLException {
        return isPgsqlVersionGreaterThanEqualTo(new Version("11.0")) && delegate.shouldRunTests(cx);
    }

    @Override
    protected void setUpData() throws Exception {
        dropPartitionedTables();

        run(
                "CREATE TABLE customers (id integer, status TEXT, arr NUMERIC, PRIMARY KEY (id, status)) PARTITION BY LIST(status) ");
        run("CREATE TABLE cust_active PARTITION OF customers FOR VALUES IN ('ACTIVE')");
        run("CREATE TABLE cust_archived PARTITION OF customers FOR VALUES IN ('EXPIRED')");
        run("CREATE TABLE cust_others PARTITION OF customers DEFAULT");
        run(
                "INSERT INTO customers VALUES (1, 'ACTIVE', 100), (2, 'RECURRING', 20), (3, 'EXPIRED', 38), (4, 'REACTIVATED', 144)");
    }

    private void dropPartitionedTables() {
        runSafe("DROP TABLE customers cascade");
    }
}
