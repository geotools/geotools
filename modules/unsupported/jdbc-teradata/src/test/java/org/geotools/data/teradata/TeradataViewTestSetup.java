package org.geotools.data.teradata;

import org.geotools.jdbc.JDBCViewTestSetup;

public class TeradataViewTestSetup extends JDBCViewTestSetup {

    protected TeradataViewTestSetup() {
        super(new TeradataTestSetup());
    }


    protected void createLakesTable() throws Exception {
        run("CREATE TABLE \"lakes\"(\"fid\" generated always as identity int primary key NOT NULL, \"id\" int, \"geom\" ST_GEOMETRY, \"name\" varchar(200) )");
        run("INSERT INTO SYSSPATIAL.GEOMETRY_COLUMNS (F_TABLE_CATALOG, F_TABLE_SCHEMA, F_TABLE_NAME, F_GEOMETRY_COLUMN, COORD_DIMENSION, SRID, GEOM_TYPE) VALUES ('', '" + fixture.getProperty("schema") + "', 'lakes', 'geom', 2, 1619, 'POLYGON')");
//         run("CREATE INDEX LAKES_GEOM_INDEX ON \"lakes\" USING GIST (\"geom\") ");

        run("INSERT INTO \"lakes\" (\"fid\", \"id\",\"geom\",\"name\") VALUES (0, 0,'POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))','muddy')");
    }


    protected void dropLakesTable() throws Exception {
        runSafe("DELETE FROM SYSSPATIAL.GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'lakes'");
        runSafe("DROP TABLE \"lakes\"");
    }


    protected void createLakesView() throws Exception {
        run("create view \"lakesview\" ( \"fid\", id, \"geom\",\"name\" )  as select * from \"lakes\"");
        // disabled insert to make sure views work even without geom column declarations
        run("INSERT INTO SYSSPATIAL.GEOMETRY_COLUMNS (F_TABLE_CATALOG, F_TABLE_SCHEMA, F_TABLE_NAME, F_GEOMETRY_COLUMN, COORD_DIMENSION, SRID, GEOM_TYPE) VALUES ('', '" + fixture.getProperty("schema") + "', 'lakesview', 'geom', 2, 1619, 'POLYGON')");
    }


    protected void dropLakesView() throws Exception {
        runSafe("DELETE FROM SYSSPATIAL.GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'lakesview'");
        runSafe("DROP VIEW \"lakesview\"");
    }


    protected void createLakesViewPk() throws Exception {
        // TODO Auto-generated method stub

    }


    protected void dropLakesViewPk() throws Exception {
        // TODO Auto-generated method stub

    }

}
