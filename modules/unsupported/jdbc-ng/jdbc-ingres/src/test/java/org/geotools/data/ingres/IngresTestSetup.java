package org.geotools.data.ingres;

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCTestSetup;

public class IngresTestSetup extends JDBCTestSetup {

    @Override
    protected JDBCDataStoreFactory createDataStoreFactory() {
        return new IngresDataStoreFactory();
    }
    
    @Override
    protected void setUpDataStore(JDBCDataStore dataStore) {
        super.setUpDataStore(dataStore);
        dataStore.setDatabaseSchema(null);
    }
    
    @Override
    protected String typeName(String raw) {
    	return raw.toLowerCase();
    }
    
    @Override
    protected String attributeName(String raw) {
    	return raw.toLowerCase();
    }
   
    @Override
    protected void setUpData() throws Exception {
        runSafe("DROP TABLE ft1");
        runSafe("DROP TABLE ft2");

        run("CREATE TABLE ft1(" //
                + "id int primary key, " //
                + "geometry geometry SRID 4326, " //
                + "intProperty int," //
                + "doubleProperty double precision, " // 
                + "stringProperty varchar(256))");
        
        run("INSERT INTO ft1 VALUES(0, GeometryFromText('POINT(0 0)', 4326), 0, 0.0, 'zero')");
        run("INSERT INTO ft1 VALUES(1, GeometryFromText('POINT(1 1)', 4326), 1, 1.1, 'one')");
        run("INSERT INTO ft1 VALUES(2, GeometryFromText('POINT(2 2)', 4326), 2, 2.2, 'two')");
    }

    
}
