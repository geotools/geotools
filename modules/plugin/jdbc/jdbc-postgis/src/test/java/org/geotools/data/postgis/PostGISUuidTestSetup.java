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
package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCUuidTestSetup;


public class PostGISUuidTestSetup extends JDBCUuidTestSetup {

    public PostGISUuidTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createUuidTable() throws Exception {
        run( "CREATE TABLE \"guid\" ( \"id\" serial PRIMARY KEY, \"uuidProperty\" uuid)" );
        run( "INSERT INTO \"guid\" (\"uuidProperty\") VALUES ('" + uuid1 + "')");
        run( "INSERT INTO \"guid\" (\"uuidProperty\") VALUES ('" + uuid2 + "')");

        /*
         * A table with UUID as primary key 
         */
        run( "CREATE TABLE \"uuidt\" ( \"id\" uuid PRIMARY KEY, \"the_geom\" geometry)" );
    }

    @Override
    protected void dropUuidTable() throws Exception {
       run("DROP TABLE \"guid\"");
       run("DROP TABLE \"uuidt\"");
    }
}
