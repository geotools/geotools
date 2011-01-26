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
package org.geotools.data.spatialite;

import org.geotools.jdbc.JDBCBooleanTestSetup;

public class SpatiaLiteBooleanTestSetup extends JDBCBooleanTestSetup {

    protected SpatiaLiteBooleanTestSetup() {
        super(new SpatiaLiteTestSetup());
        
    }

    @Override
    protected void createBooleanTable() throws Exception {
        run( "CREATE TABLE b (fid INTEGER PRIMARY KEY, id INTEGER,boolProperty BOOLEAN)");
        run( "INSERT INTO b VALUES (0, 0, 0)");
        run( "INSERT INTO b VALUES (1, 1, 1)");
    }

    @Override
    protected void dropBooleanTable() throws Exception {
        run( "DROP TABLE b");
    }

}
