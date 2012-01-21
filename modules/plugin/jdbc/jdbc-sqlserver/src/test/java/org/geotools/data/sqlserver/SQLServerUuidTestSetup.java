package org.geotools.data.sqlserver;

import org.geotools.jdbc.JDBCUuidTestSetup;

public class SQLServerUuidTestSetup extends JDBCUuidTestSetup {

    protected SQLServerUuidTestSetup() {
        super(new SQLServerTestSetup());
    }

    @Override
    protected void createUuidTable() throws Exception {
        run( "CREATE TABLE \"guid\" ( \"id\" int identity(0, 1) PRIMARY KEY, \"uuidProperty\" uniqueidentifier)" );
        run( "INSERT INTO \"guid\" (\"uuidProperty\") VALUES ('" + uuid1 + "')");
        run( "INSERT INTO \"guid\" (\"uuidProperty\") VALUES ('" + uuid2 + "')");

        /*
         * A table with UUID as primary key 
         */
        run( "CREATE TABLE \"uuidt\" ( \"id\" uniqueidentifier PRIMARY KEY, \"the_geom\" geometry)" );
    }

    @Override
    protected void dropUuidTable() throws Exception {
       run("DROP TABLE \"guid\"");
       run("DROP TABLE \"uuidt\"");
    }
}
