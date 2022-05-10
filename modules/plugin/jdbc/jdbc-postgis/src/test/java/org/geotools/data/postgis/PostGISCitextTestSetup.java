package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCTestSetup;

public class PostGISCitextTestSetup extends JDBCTestSetup {

    protected PostGISCitextTestSetup() {}

    @Override
    protected void setUpData() throws Exception {
        runSafe("DROP TABLE \"users\"");
        run(
                "CREATE TABLE \"users\" (fid INT PRIMARY KEY, \"nick\" CITEXT, \"pass\" TEXT NOT NULL)");
        run("INSERT INTO \"users\" VALUES (1, 'larry',  md5(random()::text) );");
        run("INSERT INTO \"users\" VALUES (2, 'Tom',  md5(random()::text) );");
        run("INSERT INTO \"users\" VALUES (3, 'DAMIAN',  md5(random()::text) );");
    }

    @Override
    protected void setUpDataStore(JDBCDataStore dataStore) {
        super.setUpDataStore(dataStore);

        // let's work with the most common schema please
        dataStore.setDatabaseSchema("public");
    }

    @Override
    protected JDBCDataStoreFactory createDataStoreFactory() {
        return new PostgisNGDataStoreFactory();
    }
}
