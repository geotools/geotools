package org.geotools.data.h2;

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCViewTestSetup;

public class H2ViewTestSetup extends JDBCViewTestSetup {
    
    protected H2ViewTestSetup() {
        super(new H2TestSetup());
        
    }
    
    @Override
    protected void setUpDataStore(JDBCDataStore dataStore) {
        super.setUpDataStore(dataStore);
        
        dataStore.setDatabaseSchema( null );
    }

    @Override
    protected void createLakesTable() throws Exception {
        run("CREATE TABLE \"lakes\"(\"fid\" int primary key, \"id\" int, \"geom\" POLYGON, \"name\" varchar )");
        run("CALL AddGeometryColumn(NULL, 'lakes', 'geom', 4326, 'POLYGON', 2)");
        run("INSERT INTO \"lakes\" (\"fid\", \"id\",\"geom\",\"name\") VALUES ( 0, 0,"
                + "ST_GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),"
                + "'muddy')");
    }

    @Override
    protected void dropLakesTable() throws Exception {
        runSafe("CALL DropGeometryColumn(NULL, 'lakes', 'geom')");
        runSafe("DROP TABLE \"lakes\"");
    }

    @Override
    protected void createLakesView() throws Exception {
        run("CREATE VIEW \"lakesview\" as select * from \"lakes\"");
        run("CALL AddGeometryColumn(NULL, 'lakesview', 'geom', 4326, 'POLYGON', 2)");
    }


    @Override
    protected void dropLakesView() throws Exception {
        run("CALL DropGeometryColumn(NULL, 'lakesview', 'geom')");
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
