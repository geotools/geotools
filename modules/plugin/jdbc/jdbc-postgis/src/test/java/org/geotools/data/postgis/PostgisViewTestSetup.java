package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCViewTestSetup;

public class PostgisViewTestSetup extends JDBCViewTestSetup {

    protected PostgisViewTestSetup() {
        super(new PostGISTestSetup());
    }
    
    @Override
    protected void createLakesTable() throws Exception {
        run("CREATE TABLE \"lakes\"(\"fid\" int primary key, \"id\" int, "
                + "\"geom\" geometry, \"name\" varchar )");
        run("INSERT INTO GEOMETRY_COLUMNS VALUES('', 'public', 'lakes', 'geom', 2, '4326', 'POLYGON')");
        run("CREATE INDEX LAKES_GEOM_INDEX ON \"lakes\" USING GIST (\"geom\") ");
        
        run("INSERT INTO \"lakes\" (\"fid\", \"id\",\"geom\",\"name\") VALUES (0, 0,"
                + "GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),"
                + "'muddy')");
    }

    @Override
    protected void dropLakesTable() throws Exception {
        runSafe("DELETE FROM GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'lakes'");
        runSafe("DROP TABLE \"lakes\"");
    }

    @Override
    protected void createLakesView() throws Exception {
        run("create view \"lakesview\" as select * from \"lakes\"");
        // disabled insert to make sure views work even without geom column declarations
        // run("INSERT INTO GEOMETRY_COLUMNS VALUES('', 'public', 'lakesview', 'geom', 2, '4326', 'POLYGON')");
    }

    @Override
    protected void dropLakesView() throws Exception {
        runSafe("DELETE FROM GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'lakesview'");
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
