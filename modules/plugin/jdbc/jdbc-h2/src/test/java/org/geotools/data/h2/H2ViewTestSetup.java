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
        run("CREATE TABLE \"lakes\"(\"fid\" int primary key, \"id\" int, \"geom\" blob, \"name\" varchar )");

        run("INSERT INTO \"lakes\" (\"fid\", \"id\",\"geom\",\"name\") VALUES ( 0, 0,"
                + "ST_GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),"
                + "'muddy')");
    }

    @Override
    protected void dropLakesTable() throws Exception {
        run("DROP TABLE \"lakes\"");
    }

    @Override
    protected void createLakesView() throws Exception {
        run("CREATE VIEW \"lakesview\" as select * from \"lakes\"");
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
