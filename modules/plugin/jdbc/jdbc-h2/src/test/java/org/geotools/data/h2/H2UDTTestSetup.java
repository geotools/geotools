/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCUDTTestSetup;

public class H2UDTTestSetup extends JDBCUDTTestSetup {

    public H2UDTTestSetup() {
        super(new H2TestSetup());
    }

    @Override
    protected void setUpDataStore(JDBCDataStore dataStore) {
        super.setUpDataStore(dataStore);
        dataStore.setDatabaseSchema( null );
    }
    
    @Override
    protected void createUdtTable() throws Exception {
        run("CREATE DOMAIN \"foo\" AS text CHECK (VALUE REGEXP '\\d{2}\\D{2}');");
        run("CREATE TABLE \"udt\" (\"id\" integer PRIMARY KEY, \"ut\" \"foo\");");
        run("INSERT INTO \"udt\" VALUES (0, '12ab');");
    }

    @Override
    protected void dropUdtTable() throws Exception {
        runSafe("DROP TABLE \"udt\"");
        runSafe("DROP DOMAIN \"foo\"");
}

}
