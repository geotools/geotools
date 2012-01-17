package org.geotools.data.sqlserver;

import org.geotools.jdbc.JDBCSkipColumnTestSetup;

public class SQLServerSkipColumnTestSetup extends JDBCSkipColumnTestSetup {

    protected SQLServerSkipColumnTestSetup() {
        super(new SQLServerTestSetup());
    }

    @Override
    protected void createSkipColumnTable() throws Exception {
        run("CREATE TABLE \"skipcolumn\"(" //
                + "\"fid\" int identity(0, 1) primary key, " //
                + "\"id\" integer, " //
                + "\"geom\" geometry, " //
                + "\"weirdproperty\" xml," //
                + "\"name\" varchar(255))");
        run("INSERT INTO \"skipcolumn\" VALUES( 0, geometry::STGeomFromText('POINT(0 0)', 4326), null, 'GeoTools')"); 
    }

    @Override
    protected void dropSkipColumnTable() throws Exception {
        runSafe("DELETE FROM GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'skipcolumn'");
        runSafe("DROP TABLE \"skipcolumn\"");
    }
    
}
