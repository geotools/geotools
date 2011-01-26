package org.geotools.data.h2;

import org.geotools.jdbc.JDBCSkipColumnTestSetup;

public class H2SkipColumnTestSetup extends JDBCSkipColumnTestSetup {

    protected H2SkipColumnTestSetup() {
        super(new H2TestSetup());
    }

    @Override
    protected void createSkipColumnTable() throws Exception {
        run("CREATE TABLE \"geotools\".\"skipcolumn\" (\"fid\" int AUTO_INCREMENT(1) PRIMARY KEY, "
        + "\"id\" int, \"geom\" BLOB COMMENT 'POINT', \"weird\" array, \"name\" varchar)");
        run("INSERT INTO \"geotools\".\"skipcolumn\" VALUES ("
            + "0, 0, ST_GeomFromText('POINT(0 0)',4326), null, 'GeoTools')");
    }

    @Override
    protected void dropSkipColumnTable() throws Exception {
        runSafe("DROP TABLE \"geotools\".\"skipcolumn\"");
    }

}
