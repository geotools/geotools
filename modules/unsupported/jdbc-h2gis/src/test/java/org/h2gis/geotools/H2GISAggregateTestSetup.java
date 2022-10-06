package org.h2gis.geotools;

import org.geotools.jdbc.JDBCAggregateTestSetup;

public class H2GISAggregateTestSetup extends JDBCAggregateTestSetup {
    protected H2GISAggregateTestSetup() {
        super(new H2GISTestSetup());
    }

    @Override
    protected void createAggregateTable() throws Exception {
        run(
                "CREATE TABLE \"geotools\".\"aggregate\"(\"fid\" int AUTO_INCREMENT(0) PRIMARY KEY, \"id\" int, "
                        + "\"geom\" GEOMETRY(POLYGON, 4326), \"name\" varchar )");
        run("CREATE SPATIAL INDEX IF NOT EXISTS spIdx ON \"geotools\".\"aggregate\"(\"geom\")");
        run(
                "INSERT INTO \"geotools\".\"aggregate\" (\"id\",\"geom\",\"name\") VALUES ( 0,"
                        + "ST_GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),"
                        + "'muddy1')");
        run(
                "INSERT INTO \"geotools\".\"aggregate\" (\"id\",\"geom\",\"name\") VALUES ( 1,"
                        + "ST_GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),"
                        + "'muddy1')");
        run(
                "INSERT INTO \"geotools\".\"aggregate\" (\"id\",\"geom\",\"name\") VALUES ( 2,"
                        + "ST_GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),"
                        + "'muddy2')");
    }

    @Override
    protected void dropAggregateTable() {
        runSafe("DROP TABLE \"geotools\".\"aggregate\"");
        runSafe("DROP TABLE \"geotools\".\"aggregate_HATBOX\"");
        runSafe("DROP INDEX spIdx");
    }
}
