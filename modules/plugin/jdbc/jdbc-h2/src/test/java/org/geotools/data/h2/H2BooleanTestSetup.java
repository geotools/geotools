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

import org.geotools.jdbc.JDBCBooleanTestSetup;
import org.geotools.jdbc.JDBCDataStore;

public class H2BooleanTestSetup extends JDBCBooleanTestSetup {

    protected H2BooleanTestSetup() {
        super(new H2TestSetup());
    }

    @Override
    protected void setUpDataStore(JDBCDataStore dataStore) {
        super.setUpDataStore(dataStore);
        dataStore.setDatabaseSchema( null );
    }
    
    @Override
    protected void createBooleanTable() throws Exception {
        run( "CREATE TABLE \"b\" (fid int AUTO_INCREMENT(0) PRIMARY KEY, \"id\" int, \"boolProperty\" boolean)");
        run( "INSERT INTO \"b\" (\"id\",\"boolProperty\") VALUES (0, false)");
        run( "INSERT INTO \"b\" (\"id\",\"boolProperty\") VALUES (1, true)");
    }

    @Override
    protected void dropBooleanTable() throws Exception {
        run( "DROP TABLE \"b\"");
    }

}
