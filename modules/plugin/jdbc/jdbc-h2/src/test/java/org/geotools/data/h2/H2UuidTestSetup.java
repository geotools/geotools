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

import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCUuidTestSetup;


public class H2UuidTestSetup extends JDBCUuidTestSetup {

    public H2UuidTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createUuidTable() throws Exception {
        run( "CREATE TABLE \"geotools\".\"guid\" ( \"id\" serial PRIMARY KEY, \"uuidProperty\" uuid)" );
        run( "INSERT INTO \"geotools\".\"guid\" (\"uuidProperty\") VALUES ('" + uuid1 + "')");
        run( "INSERT INTO \"geotools\".\"guid\" (\"uuidProperty\") VALUES ('" + uuid2 + "')");

        /*
         * A table with UUID as primary key 
         */
        run( "CREATE TABLE \"geotools\".\"uuidt\" ( \"id\" uuid PRIMARY KEY, \"the_geom\" POINT)" );
        run( "CALL AddGeometryColumn('geotools', 'uuidt', 'the_geom', 4326, 'POINT', 2)" );
    }

    @Override
    protected void dropUuidTable() throws Exception {
       run("DROP TABLE \"geotools\".\"guid\"");
       run("DROP TABLE \"geotools\".\"uuidt\"");
    }
}
