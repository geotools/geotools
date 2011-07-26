package org.geotools.data.h2;

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCSkipColumnTestSetup;

public class H2SkipColumnTestSetup extends JDBCSkipColumnTestSetup {

    protected H2SkipColumnTestSetup() {
        super(new H2TestSetup());
    }

    @Override
    protected void setUpDataStore(JDBCDataStore dataStore) {
        super.setUpDataStore(dataStore);
        dataStore.setDatabaseSchema(null);
    }
    
    @Override
    protected void createSkipColumnTable() throws Exception {
        run("CREATE TABLE \"skipcolumn\" (\"fid\" int AUTO_INCREMENT(1) PRIMARY KEY, "
        + "\"id\" int, \"geom\" POINT, \"weird\" array, \"name\" varchar)");
        run("CALL AddGeometryColumn(NULL, 'skipcolumn', 'geom', 4326, 'POINT', 2)");
        run("INSERT INTO \"skipcolumn\" VALUES ("
            + "0, 0, ST_GeomFromText('POINT(0 0)',4326), null, 'GeoTools')");
    }

    @Override
    protected void dropSkipColumnTable() throws Exception {
        runSafe("DELETE FROM geometry_columns WHERE f_table_name = 'skipcolumn'");
        runSafe("DROP TABLE \"skipcolumn\"");
    }

}
