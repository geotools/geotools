/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.h2;

import java.util.Properties;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCTestSetup;

/**
 * Test harness for H2.
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
@SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation") // not yet a JUnit4 test
public class H2TestSetup extends JDBCTestSetup {

    @Override
    protected void initializeDatabase() throws Exception {
        runSafe("DROP SCHEMA \"geotools\"; COMMIT;");
        run("CREATE SCHEMA \"geotools\";");
    }

    @Override
    protected void setUpData() throws Exception {
        // drop old data
        runSafe("DROP TABLE \"geotools\".\"ft1\"; COMMIT;");
        runSafe("DROP TABLE \"geotools\".\"ft1_HATBOX\"; COMMIT;");
        runSafe("DROP TABLE \"geotools\".\"ft2\"; COMMIT;");
        runSafe("DROP TABLE \"geotools\".\"ft4\"; COMMIT;");

        runSafe("DELETE FROM geometry_columns WHERE f_table_name = 'ft1'");
        runSafe("DELETE FROM geometry_columns WHERE f_table_name = 'ft2'");
        runSafe("DELETE FROM geometry_columns WHERE f_table_name = 'ft4'");

        String sql =
                "CREATE TABLE \"geotools\".\"ft1\" ("
                        + "\"id\" int AUTO_INCREMENT(1) PRIMARY KEY, "
                        + "\"geometry\" POINT, \"intProperty\" int, "
                        + "\"doubleProperty\" double, \"stringProperty\" varchar"
                        + ")";
        run(sql);

        sql = "CALL AddGeometryColumn('geotools', 'ft1', 'geometry', 4326, 'POINT', 2)";
        run(sql);

        sql =
                "INSERT INTO \"geotools\".\"ft1\" VALUES ("
                        + "0,ST_GeomFromText('POINT(0 0)',4326), 0, 0.0,'zero');";
        run(sql);

        sql =
                "INSERT INTO \"geotools\".\"ft1\" VALUES ("
                        + "1,ST_GeomFromText('POINT(1 1)',4326), 1, 1.1,'one');";
        run(sql);

        sql =
                "INSERT INTO \"geotools\".\"ft1\" VALUES ("
                        + "2,ST_GeomFromText('POINT(2 2)',4326), 2, 2.2,'two');";
        run(sql);

        sql = "CALL CreateSpatialIndex('geotools', 'ft1', 'geometry', 4326)";
        run(sql);

        runFt4();
    }

    private void runFt4() throws Exception {
        String sql =
                "CREATE TABLE \"geotools\".\"ft4\" ("
                        + "\"id\" int AUTO_INCREMENT(1) PRIMARY KEY, "
                        + "\"geometry\" POINT, \"intProperty\" int, "
                        + "\"doubleProperty\" double, \"stringProperty\" varchar"
                        + ")";
        run(sql);

        sql = "CALL AddGeometryColumn('geotools', 'ft4', 'geometry', 4326, 'POINT', 2)";
        run(sql);

        sql =
                "INSERT INTO \"geotools\".\"ft4\" VALUES ("
                        + "0,ST_GeomFromText('POINT(0 0)',4326), 0, 0.0,'zero');";
        run(sql);

        sql =
                "INSERT INTO \"geotools\".\"ft4\" VALUES ("
                        + "1,ST_GeomFromText('POINT(1 1)',4326), 1, 1.1,'one');";
        run(sql);

        sql =
                "INSERT INTO \"geotools\".\"ft4\" VALUES ("
                        + "2,ST_GeomFromText('POINT(2 2)',4326), 1, 1.1,'one_2');";
        run(sql);

        sql =
                "INSERT INTO \"geotools\".\"ft4\" VALUES ("
                        + "3,ST_GeomFromText('POINT(3 3)',4326), 1, 1.1,'one_2');";
        run(sql);

        sql =
                "INSERT INTO \"geotools\".\"ft4\" VALUES ("
                        + "4,ST_GeomFromText('POINT(4 4)',4326), 2, 2.2,'two');";
        run(sql);

        sql =
                "INSERT INTO \"geotools\".\"ft4\" VALUES ("
                        + "5,ST_GeomFromText('POINT(5 5)',4326), 2, 2.2,'two_2');";

        run(sql);

        sql =
                "INSERT INTO \"geotools\".\"ft4\" VALUES ("
                        + "6,ST_GeomFromText('POINT(6 6)',4326), 3, 3.3,'three');";
        run(sql);

        sql = "CALL CreateSpatialIndex('geotools', 'ft4', 'geometry', 4326)";
        run(sql);
    }

    @Override
    protected JDBCDataStoreFactory createDataStoreFactory() {
        return new H2DataStoreFactory();
    }

    @Override
    protected Properties createOfflineFixture() {
        Properties fixture = new Properties();
        fixture.put("driver", "org.h2.Driver");
        fixture.put("url", "jdbc:h2:target/geotools");
        fixture.put("user", "geotools");
        fixture.put("password", "geotools");
        fixture.put("database", "target/geotools");
        return fixture;
    }
}
