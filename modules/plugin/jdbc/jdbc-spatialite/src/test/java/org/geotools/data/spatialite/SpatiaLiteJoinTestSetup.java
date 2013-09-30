/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2013, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.jdbc.JDBCJoinTestSetup;

public class SpatiaLiteJoinTestSetup extends JDBCJoinTestSetup {

    protected SpatiaLiteJoinTestSetup() {
        super(new SpatiaLiteTestSetup());
    }

    @Override
    protected void createJoinTable() throws Exception {
        run("CREATE TABLE ftjoin ( id int PRIMARY KEY, " + "name VARCHAR)");
        run("SELECT AddGeometryColumn('ftjoin','geom',4326,'GEOMETRY',2)");
        run("ALTER TABLE ftjoin add join1intProperty int)" );

        run( "INSERT INTO ftjoin VALUES (0, 'zero', GeomFromText('POLYGON ((-0.1 -0.1, -0.1 0.1, 0.1 0.1, 0.1 -0.1, -0.1 -0.1))', 4326), 0)");
        run( "INSERT INTO ftjoin VALUES (1, 'one', GeomFromText('POLYGON ((-1.1 -1.1, -1.1 1.1, 1.1 1.1, 1.1 -1.1, -1.1 -1.1))', 4326), 1)");
        run( "INSERT INTO ftjoin VALUES (2, 'two', GeomFromText('POLYGON ((-10 -10, -10 10, 10 10, 10 -10, -10 -10))', 4326), 2)");
        run( "INSERT INTO ftjoin VALUES (3, 'three', NULL, 3)");

        run( "CREATE TABLE ftjoin2(id int PRIMARY KEY, join2intProperty int, stringProperty2 varchar)");
        run( "INSERT INTO ftjoin2 VALUES (0, 0, '2nd zero')");
        run( "INSERT INTO ftjoin2 VALUES (1, 1, '2nd one')");
        run( "INSERT INTO ftjoin2 VALUES (2, 2, '2nd two')");
        run( "INSERT INTO ftjoin2 VALUES (3, 3, '2nd three')");
    }

    @Override
    protected void dropJoinTable() throws Exception {
        runSafe( "DROP TABLE ftjoin" );
        runSafe( "DROP TABLE ftjoin2" );
    }

}
