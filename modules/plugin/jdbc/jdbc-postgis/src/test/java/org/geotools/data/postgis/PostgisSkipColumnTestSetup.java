package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCSkipColumnTestSetup;

public class PostgisSkipColumnTestSetup extends JDBCSkipColumnTestSetup {

    protected PostgisSkipColumnTestSetup() {
        super(new PostGISTestSetup());
    }

    @Override
    protected void createSkipColumnTable() throws Exception {
        run("CREATE TABLE \"skipcolumn\"(" //
                + "\"fid\" serial primary key, " //
                + "\"id\" integer, " //
                + "\"geom\" geometry, " //
                + "\"weirdproperty\" integer[]," //
                + "\"name\" varchar)");
        run("INSERT INTO GEOMETRY_COLUMNS VALUES('', 'public', 'skipcolumn', 'geom', 2, '4326', 'POINT')");
        run("CREATE INDEX SKIPCOLUMN_GEOM_INDEX ON \"skipcolumn\" USING GIST (\"geom\") ");
        
        run("INSERT INTO \"skipcolumn\" VALUES(0, 0, GeometryFromText('POINT(0 0)', 4326), null, 'GeoTools')"); 

    }

    @Override
    protected void dropSkipColumnTable() throws Exception {
        runSafe("DELETE FROM GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'skipcolumn'");
        runSafe("DROP TABLE \"skipcolumn\"");
    }

}
