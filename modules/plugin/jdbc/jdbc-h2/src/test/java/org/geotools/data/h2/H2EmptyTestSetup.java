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
import org.geotools.jdbc.JDBCEmptyTestSetup;

public class H2EmptyTestSetup extends JDBCEmptyTestSetup {

    protected H2EmptyTestSetup() {
        super(new H2TestSetup());
    }

    @Override
    protected void setUpDataStore(JDBCDataStore dataStore) {
        super.setUpDataStore(dataStore);
        
        dataStore.setDatabaseSchema( null );
    }
    
    @Override
    protected void createEmptyTable() throws Exception {
        run( "CREATE TABLE \"empty\" (id int,geom blob)");
        
    }

    @Override
    protected void dropEmptyTable() throws Exception {
        runSafe( "DROP TABLE \"empty\"" );
    }

}
