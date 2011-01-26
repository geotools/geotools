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
package org.geotools.data.mysql;

import org.geotools.jdbc.JDBCBooleanTestSetup;
import org.geotools.jdbc.JDBCDataStore;

public class MySQLBooleanTestSetup extends JDBCBooleanTestSetup {

    protected MySQLBooleanTestSetup() {
        super(new MySQLTestSetup());
        
    }

    @Override
    protected void setUpDataStore(JDBCDataStore dataStore) {
        super.setUpDataStore(dataStore);
        dataStore.setDatabaseSchema(null);
    }
    
    @Override
    protected void createBooleanTable() throws Exception {
        run( "CREATE TABLE b (id int AUTO_INCREMENT PRIMARY KEY, boolProperty BOOL)");
        run( "INSERT INTO b (boolProperty) VALUES (false)");
        run( "INSERT INTO b (boolProperty) VALUES (true)");
    }

    @Override
    protected void dropBooleanTable() throws Exception {
        run( "DROP TABLE b");
    }

}
