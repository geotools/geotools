package org.geotools.data.sqlserver;

import org.geotools.jdbc.JDBCViewTestSetup;

public class SQLServerViewTestSetup extends JDBCViewTestSetup {

    protected SQLServerViewTestSetup() {
        super(new SQLServerTestSetup());
    }

    @Override
    protected void createLakesTable() throws Exception {
        run("CREATE TABLE lakes(fid int IDENTITY(0,1) PRIMARY KEY, id int, "
                + "geom geometry, name varchar(255) )");

        run("INSERT INTO lakes (id,geom,name) VALUES ( 0,"
                + "geometry::STGeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),"
                + "'muddy')");

        run("CREATE SPATIAL INDEX _lakes_geometry_index on lakes(geom) WITH (BOUNDING_BOX = (-100, -100, 100, 100))");
    }

    @Override
    protected void dropLakesTable() throws Exception {
        runSafe("DROP TABLE \"lakes\"");
    }

    @Override
    protected void createLakesView() throws Exception {
        run("create view \"lakesview\" as select * from \"lakes\"");
    }

    @Override
    protected void dropLakesView() throws Exception {
        runSafe("DROP VIEW \"lakesview\"");
    }

    @Override
    protected void createLakesViewPk() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void dropLakesViewPk() throws Exception {
        // TODO Auto-generated method stub

    }

}
