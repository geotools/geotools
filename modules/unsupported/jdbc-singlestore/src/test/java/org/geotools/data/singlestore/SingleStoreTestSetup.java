/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.singlestore;

import java.sql.Connection;
import java.util.Properties;
import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCTestSetup;

/**
 * Test harness for mysql.
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public class SingleStoreTestSetup extends JDBCTestSetup {

    @Override
    protected void initializeDataSource(BasicDataSource ds, Properties db) {
        super.initializeDataSource(ds, db);

        ds.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    }

    @Override
    protected JDBCDataStoreFactory createDataStoreFactory() {
        return new SingleStoreDataStoreFactory();
    }

    @Override
    protected void setUpData() throws Exception {
        // allow time parsing in str_to_date
        run("SET GLOBAL sql_mode=(SELECT REPLACE(@@sql_mode,'NO_ZERO_IN_DATE',''));");
        run("SET GLOBAL sql_mode=(SELECT REPLACE(@@sql_mode,'NO_ZERO_DATE',''));");

        // drop old data
        try {
            run("DROP TABLE IF EXISTS ft1;");
        } catch (Exception e) {
            // e.printStackTrace();
        }

        try {
            run("DROP TABLE IF EXISTS ft2;");
        } catch (Exception e) {
            // e.printStackTrace();
        }

        try {
            run("DROP TABLE IF EXISTS ft4;");
        } catch (Exception e) {
            // e.printStackTrace();
        }
        runSafe("DELETE FROM geometry_columns");

        // create some data
        StringBuffer sb = new StringBuffer();
        // COLLATE utf8_bin is necessary to ensure case-sensitive string comparisons
        sb.append("CREATE TABLE ft1 ")
                .append("(id int PRIMARY KEY , ")
                .append("geometry GEOGRAPHYPOINT, intProperty int, ")
                .append("doubleProperty DOUBLE(8,2), stringProperty varchar(255) COLLATE utf8_bin);");
        run(sb.toString());

        // setup so that we can start counting from 0, otherwise 0 is treated as a special value
        run("SET sql_mode='NO_AUTO_VALUE_ON_ZERO';");

        sb = new StringBuffer();
        sb.append("INSERT INTO ft1 VALUES (").append("0, GEOGRAPHY_POINT(0, 0), 0, 0.0, 'zero');");
        run(sb.toString());

        sb = new StringBuffer();
        sb.append("INSERT INTO ft1 VALUES (").append("1, GEOGRAPHY_POINT(1, 1), 1, 1.1, 'one');");
        run(sb.toString());

        sb = new StringBuffer();
        sb.append("INSERT INTO ft1 VALUES (").append("2, GEOGRAPHY_POINT(2, 2), 2, 2.2, 'two');");
        run(sb.toString());

        runft4();
    }

    private void runft4() throws Exception {
        StringBuffer sb = new StringBuffer();
        // JD: COLLATE latin1_general_cs is neccesary to ensure case-sensitive string comparisons
        sb.append("CREATE TABLE ft4 (")
                .append("id int AUTO_INCREMENT PRIMARY KEY,")
                .append("geometry GEOGRAPHYPOINT, intProperty int, ")
                .append("doubleProperty double, stringProperty varchar(255) COLLATE latin1_general_cs);");
        run(sb.toString());

        sb = new StringBuffer();
        sb.append("INSERT INTO ft4 VALUES (").append("-1, GEOGRAPHY_POINT(0, 0), 0, 0.0, 'zero');");
        run(sb.toString());

        sb = new StringBuffer();
        sb.append("INSERT INTO ft4 VALUES (").append("1, GEOGRAPHY_POINT(0, 0), 1, 1.1, 'one');");
        run(sb.toString());

        sb = new StringBuffer();
        sb.append("INSERT INTO ft4 VALUES (").append("2, GEOGRAPHY_POINT(2, 2), 1, 1.1, 'one_2');");
        run(sb.toString());

        sb = new StringBuffer();
        sb.append("INSERT INTO ft4 VALUES (").append("3, GEOGRAPHY_POINT(3, 3), 1, 1.1, 'one_2');");
        run(sb.toString());

        sb = new StringBuffer();
        sb.append("INSERT INTO ft4 VALUES (").append("4, GEOGRAPHY_POINT(4, 4), 2, 2.2,'two');");
        run(sb.toString());

        sb = new StringBuffer();
        sb.append("INSERT INTO ft4 VALUES (").append("5, GEOGRAPHY_POINT(5, 5), 2, 2.2,'two_2');");
        run(sb.toString());

        sb = new StringBuffer();
        sb.append("INSERT INTO ft4 VALUES (").append("6, GEOGRAPHY_POINT(6, 6), 3, 3.3,'three');");
        run(sb.toString());
    }

    @Override
    protected void setUpDataStore(JDBCDataStore dataStore) {
        dataStore.setDatabaseSchema(null);
    }

    @Override
    protected Properties createExampleFixture() {
        Properties p = new Properties();

        p.put("driver", "com.singlestore.jdbc.Driver");
        p.put("url", "jdbc:singlestore://localhost/geotools");
        p.put("host", "localhost");
        p.put("port", "3306");
        p.put("user", "geotools");
        p.put("password", "geotools");

        return p;
    }
}
