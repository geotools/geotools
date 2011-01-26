package org.geotools.data.ingres;

import org.geotools.jdbc.JDBCSkipColumnTestSetup;

public class IngresSkipColumnTestSetup extends JDBCSkipColumnTestSetup {

    protected IngresSkipColumnTestSetup() {
        super(new IngresTestSetup());
    }

    @Override
    protected void createSkipColumnTable() throws Exception {
        run("CREATE TABLE \"skipcolumn\"(" //
                + "\"fid\" int primary key, " //
                + "\"id\" integer, " //
                + "\"geom\" POINT SRID 4326, " //
                + "\"weirdproperty\" NBR," //
                + "\"name\" varchar(256))");
        run("CREATE SEQUENCE skipcolumn_fid_sequence");
        
        run("INSERT INTO \"skipcolumn\" VALUES(NEXT VALUE FOR skipcolumn_fid_sequence, " +
        		"0, GeometryFromText('POINT(0 0)', 4326), null, 'GeoTools')"); 

    }

    @Override
    protected void dropSkipColumnTable() throws Exception {
        runSafe("DROP TABLE \"skipcolumn\"");
        runSafe("DROP SEQUENCE skipcolumn_fid_sequence");
    }

}
