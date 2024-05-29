package org.geotools.geopkg;

import org.geotools.jdbc.JDBCDateTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class GeoPkgDateTestSetup extends JDBCDateTestSetup {

    public GeoPkgDateTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createDateTable() throws Exception {

        // we are doing direct inserts into the database
        // dates MUST be in ISO-8601 date/time string in the form YYYY-MM-DDTHH:MM[:SS.SSS]Z

        // NOTE: geopackage does NOT support TIME columns - just DATE and DATETIME
        run("CREATE TABLE dates (id INTEGER PRIMARY KEY, d DATE, dt DATETIME, t TIME)");

        run(
                "INSERT INTO dates VALUES (1,"
                        + "DATE('2009-06-28','localtime'), "
                        + "'2009-06-28T15:12:41Z',"
                        + "strftime('%H:%M:%S','15:12:41','utc')  )");

        run(
                "INSERT INTO dates VALUES (2,"
                        + "DATE('2009-01-15','localtime'), "
                        + "'2009-01-15T13:10:12Z',"
                        + "strftime('%H:%M:%S','13:10:12','utc')  )");

        run(
                "INSERT INTO dates VALUES (3,"
                        + "DATE('2009-09-29','localtime'), "
                        + "'2009-09-29T17:54:23Z',"
                        + "strftime('%H:%M:%S','17:54:23','utc')  )");
        String sql =
                "INSERT INTO gpkg_contents (table_name, data_type, identifier, srs_id) VALUES "
                        + "('dates', 'features', 'dates', 4326)";
        run(sql);
    }

    @Override
    protected void dropDateTable() throws Exception {
        ((GeoPkgTestSetup) delegate).removeTable("dates");
    }
}
