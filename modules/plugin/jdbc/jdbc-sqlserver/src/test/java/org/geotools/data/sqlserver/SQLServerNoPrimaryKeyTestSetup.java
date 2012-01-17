package org.geotools.data.sqlserver;

import org.geotools.jdbc.JDBCNoPrimaryKeyTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class SQLServerNoPrimaryKeyTestSetup extends JDBCNoPrimaryKeyTestSetup {

    protected SQLServerNoPrimaryKeyTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createLakeTable() throws Exception {
        run("CREATE TABLE \"lake\"(\"id\" int, "
                + "\"geom\" geometry, \"name\" varchar(255) )");
        
        run("INSERT INTO \"lake\" (\"id\",\"geom\",\"name\") VALUES (0,"
                + "geometry::STGeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),"
                + "'muddy')");
        
        // can't do this, sql server requires a primary key to use spatial indexes
        // run("CREATE SPATIAL INDEX _lake_geometry_index on lake(geom) WITH (BOUNDING_BOX = (-10, -10, 10, 10))");
    }

    @Override
    protected void dropLakeTable() throws Exception {
        runSafe("DROP TABLE \"lake\"");
    }

}
