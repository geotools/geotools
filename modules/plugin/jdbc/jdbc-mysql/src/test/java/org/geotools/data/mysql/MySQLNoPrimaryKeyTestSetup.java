package org.geotools.data.mysql;

import org.geotools.jdbc.JDBCNoPrimaryKeyTestSetup;

public class MySQLNoPrimaryKeyTestSetup extends JDBCNoPrimaryKeyTestSetup {
    
    public MySQLNoPrimaryKeyTestSetup() {
        super(new MySQLTestSetup());
    }

    protected void createLakeTable() throws Exception {
        run("CREATE TABLE lake(id int, "
            + "geom POLYGON, name varchar(255) ) ENGINE=InnoDB;");

        run("INSERT INTO lake (id,geom,name) VALUES ( 0,"
            + "GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326)," + "'muddy')");
    }

    protected void dropLakeTable() throws Exception {
        run("DROP TABLE lake");
    }

}
