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
package org.geotools.data.sqlserver;

import java.util.Properties;

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCTestSetup;

public class SQLServerTestSetup extends JDBCTestSetup {

    @Override
    protected JDBCDataStoreFactory createDataStoreFactory() {
        return new SQLServerDataStoreFactory();
    }
    
    @Override
    protected Properties createExampleFixture() {
        Properties fixture = new Properties();
        fixture.put("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        fixture.put("url", "jdbc:sqlserver://192.168.150.138:4866");
        fixture.put("host", "192.168.150.138");
        fixture.put("database", "geotools");
        fixture.put("port", "4866");
        fixture.put("user", "geotools");
        fixture.put("password", "geotools");
        return fixture;
    }
    
    protected void setUpDataStore(JDBCDataStore dataStore) {
        super.setUpDataStore(dataStore);
        
        dataStore.setDatabaseSchema( null );
    }
    
    protected void setUpData() throws Exception {
        //drop old data
        runSafe("DROP TABLE ft1");
        

        try {
            run("DROP TABLE ft2; COMMIT;");
        } catch (Exception e) {
        }

        //create the data

        String sql = "CREATE TABLE ft1 (id int IDENTITY(1,1) PRIMARY KEY, "
            + "geometry geometry, intProperty int, "
            + "doubleProperty float, stringProperty varchar(255))";
        run(sql);
        
        //change column collation to support case-insensitive comparison
        sql = "ALTER TABLE ft1 ALTER COLUMN stringProperty VARCHAR(255) COLLATE Latin1_General_CS_AS";
        run(sql);
        
        sql = "INSERT INTO ft1 (geometry,intProperty,doubleProperty,stringProperty) VALUES ("
            + "geometry::STGeomFromText('POINT(0 0)',4326), 0, 0.0,'zero');";
        run(sql);

        sql = "INSERT INTO ft1 (geometry,intProperty,doubleProperty,stringProperty) VALUES ("
            + "geometry::STGeomFromText('POINT(1 1)',4326), 1, 1.1,'one');";
        run(sql);

        sql = "INSERT INTO ft1 (geometry,intProperty,doubleProperty,stringProperty) VALUES ("
            + "geometry::STGeomFromText('POINT(2 2)',4326), 2, 2.2,'two');";
        run(sql);
    }

}
