/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.h2;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.SQLDialect;


/**
 * Test harness for H2.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public class H2TestSetup extends JDBCTestSetup {
    protected void setUpData() throws Exception {
        //drop old data
        try {
            run("DROP TABLE \"geotools\".\"ft1\"; COMMIT;");
        } catch (Exception e) {
        }
        try {
            run("DROP TABLE \"geotools\".\"ft1_HATBOX\"; COMMIT;");
        } catch (Exception e) {
        }

        try {
            run("DROP TABLE \"geotools\".\"ft2\"; COMMIT;");
        } catch (Exception e) {
        }

        try {
            run("DROP SCHEMA \"geotools\"; COMMIT;");
        } catch (Exception e) {
        }

        //create some data
        String sql = "CREATE SCHEMA \"geotools\";";
        run(sql);

        sql = "CREATE TABLE \"geotools\".\"ft1\" (" + "\"id\" int AUTO_INCREMENT(1) PRIMARY KEY, "
            + "\"geometry\" BLOB COMMENT 'POINT', \"intProperty\" int, "
            + "\"doubleProperty\" double, \"stringProperty\" varchar" + ")";
        run(sql);
        sql = "INSERT INTO \"geotools\".\"ft1\" VALUES ("
            + "0,ST_GeomFromText('POINT(0 0)',4326), 0, 0.0,'zero');";
        run(sql);

        sql = "INSERT INTO \"geotools\".\"ft1\" VALUES ("
            + "1,ST_GeomFromText('POINT(1 1)',4326), 1, 1.1,'one');";
        run(sql);

        sql = "INSERT INTO \"geotools\".\"ft1\" VALUES ("
            + "2,ST_GeomFromText('POINT(2 2)',4326), 2, 2.2,'two');";
        run(sql);
        
        sql = "CALL CreateSpatialIndex('geotools', 'ft1', 'geometry', 4326)";
        run(sql);
    }

    @Override
    protected JDBCDataStoreFactory createDataStoreFactory() {
        return new H2DataStoreFactory();
    }
    
    @Override
    protected Properties createOfflineFixture() {
        Properties fixture = new Properties();
        fixture.put( "driver","org.h2.Driver");
        fixture.put( "url","jdbc:h2:target/geotools");
        fixture.put( "user","geotools");
        fixture.put( "password","geotools");
        return fixture;
    }
}
