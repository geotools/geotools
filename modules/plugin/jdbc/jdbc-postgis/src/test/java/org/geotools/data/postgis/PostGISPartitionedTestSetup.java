package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCDelegatingTestSetup;

public class PostGISPartitionedTestSetup extends JDBCDelegatingTestSetup {

    protected PostGISPartitionedTestSetup() {
        super(new PostGISTestSetup());
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
