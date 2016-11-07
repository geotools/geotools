package org.geotools.geopkg;

import org.geotools.jdbc.JDBCDateTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class GeoPkgDateTestSetup extends JDBCDateTestSetup {


    public GeoPkgDateTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createDateTable() throws Exception {
        run( "CREATE TABLE dates (id INTEGER PRIMARY KEY, d DATE, dt TIMESTAMP, t TIME)");
        

        run( "INSERT INTO dates VALUES (1," +
                "DATE('2009-06-28','localtime'), " +
                "strftime('%Y-%m-%d %H:%M:%S','2009-06-28 15:12:41','localtime' )," +
                "strftime('%H:%M:%S','15:12:41','localtime')  )");
        
        run( "INSERT INTO dates VALUES (2," +
                "DATE('2009-01-15','localtime'), " +
                "strftime('%Y-%m-%d %H:%M:%S','2009-01-15 13:10:12','localtime')," +
                "strftime('%H:%M:%S','13:10:12','localtime')  )");
        
        run( "INSERT INTO dates VALUES (3," +
                "DATE('2009-09-29','localtime'), " +
                "strftime('%Y-%m-%d %H:%M:%S','2009-09-29 17:54:23','localtime')," +
                "strftime('%H:%M:%S','17:54:23','localtime')  )");
        String sql = "INSERT INTO gpkg_contents (table_name, data_type, identifier, srs_id) VALUES " +
                "('dates', 'features', 'dates', 4326)";
        run(sql);
       
        
    }

    @Override
    protected void dropDateTable() throws Exception {
        ((GeoPkgTestSetup)delegate).removeTable("dates");
    }

}
