package org.h2gis.geotools;

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCSkipColumnTestSetup;

public class H2GISSkipColumnTestSetup extends JDBCSkipColumnTestSetup {

    protected H2GISSkipColumnTestSetup() {
        super(new H2GISTestSetup());
    }

    @Override
    protected void setUpDataStore(JDBCDataStore dataStore) {
        super.setUpDataStore(dataStore);
        dataStore.setDatabaseSchema(null);
    }

    @Override
    protected void createSkipColumnTable() throws Exception {
        run("CREATE TABLE \"skipcolumn\" (\"fid\" int AUTO_INCREMENT(1) PRIMARY KEY, "
                + "\"id\" int, \"geom\" GEOMETRY(POINT, 4326), \"weird\" INTEGER array, \"name\" varchar)");
        run("INSERT INTO \"skipcolumn\" VALUES (" + "0, 0, ST_GeomFromText('POINT(0 0)',4326), null, 'GeoTools')");
    }

    @Override
    protected void dropSkipColumnTable() {
        runSafe("DROP TABLE \"skipcolumn\"");
    }
}
