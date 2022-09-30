package org.h2gis.geotools;

import java.util.Properties;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCTestSetup;

public class H2GISTestSetup extends JDBCTestSetup {

    @Override
    protected void initializeDatabase() throws Exception {
        runSafe("DROP SCHEMA IF EXISTS \"geotools\"; COMMIT;");
        run("CREATE SCHEMA IF NOT EXISTS \"geotools\";");
        run(
                "CREATE ALIAS IF NOT EXISTS H2GIS_SPATIAL FOR 'org.h2gis.functions.factory.H2GISFunctions.load';CALL H2GIS_SPATIAL();");
    }

    @Override
    protected void setUpData() throws Exception {
        // drop old data
        runSafe("DROP TABLE \"geotools\".\"ft1\"; COMMIT;");
        runSafe("DROP TABLE \"geotools\".\"ft2\"; COMMIT;");
        runSafe("DROP TABLE \"geotools\".\"ft4\"; COMMIT;");
        run("DROP INDEX IF EXISTS \"ft1_str_index\";");

        runSafe("DELETE FROM geometry_columns WHERE f_table_name = 'ft1'");
        runSafe("DELETE FROM geometry_columns WHERE f_table_name = 'ft2'");
        runSafe("DELETE FROM geometry_columns WHERE f_table_name = 'ft4'");

        String sql =
                "CREATE TABLE \"geotools\".\"ft1\" ("
                        + "\"id\" INTEGER PRIMARY KEY, "
                        + "\"geometry\" GEOMETRY(POINT, 4326), \"intProperty\" int, "
                        + "\"doubleProperty\" double precision, \"stringProperty\" varchar"
                        + ")";
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

        sql =
                "CREATE SPATIAL INDEX IF NOT EXISTS \"ft1_sp_index\" ON \"geotools\".\"ft1\"(\"geometry\")";
        run(sql);

        runFt4();
    }

    private void runFt4() throws Exception {
        String sql =
                "CREATE TABLE \"geotools\".\"ft4\" ("
                        + "\"id\" SERIAL PRIMARY KEY, "
                        + "\"geometry\" GEOMETRY(POINT, 4326), \"intProperty\" int, "
                        + "\"doubleProperty\" double precision, \"stringProperty\" varchar"
                        + ")";
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

        sql = "CREATE SPATIAL INDEX IF NOT EXISTS spIdx ON \"geotools\".\"ft4\"(\"geometry\")";
        run(sql);
    }

    @Override
    protected JDBCDataStoreFactory createDataStoreFactory() {
        return new H2GISDataStoreFactory();
    }

    @Override
    protected Properties createOfflineFixture() {
        Properties fixture = new Properties();
        fixture.put("driver", "org.h2.Driver");
        fixture.put("url", "jdbc:h2:./target/geotools");
        fixture.put("user", "geotools");
        fixture.put("password", "geotools");
        fixture.put("database", "target/geotools");
        return fixture;
    }
}
