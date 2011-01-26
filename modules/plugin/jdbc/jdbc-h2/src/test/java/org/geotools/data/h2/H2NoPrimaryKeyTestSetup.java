/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.jdbc.JDBCNoPrimaryKeyTestSetup;

public class H2NoPrimaryKeyTestSetup extends JDBCNoPrimaryKeyTestSetup {

    protected H2NoPrimaryKeyTestSetup() {
        super(new H2TestSetup());
    }
    
    @Override
    protected void setUpDataStore(JDBCDataStore dataStore) {
        super.setUpDataStore(dataStore);
        
        dataStore.setDatabaseSchema( null );
    }

    @Override
    protected void createLakeTable() throws Exception {
        run("CREATE TABLE \"lake\"(\"id\" int, \"geom\" POLYGON, \"name\" varchar )");
        run("CALL AddGeometryColumn(NULL, 'lake', 'geom', 4326, 'POLYGON', 2)");
        run("INSERT INTO \"lake\" (\"id\",\"geom\",\"name\") VALUES ( 0,"
                + "ST_GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),"
                + "'muddy')");
    }

    @Override
    protected void dropLakeTable() throws Exception {
        run("CALL DropGeometryColumn(NULL, 'lake', 'geom')");
        run("DROP TABLE \"lake\"");
    }

}
