/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.jdbc.JDBCJoinTestSetup;

public class SQLServerJoinTestSetup extends JDBCJoinTestSetup {

    protected SQLServerJoinTestSetup() {
        super(new SQLServerTestSetup());
    }

    @Override
    protected void createJoinTable() throws Exception {
        
        run( "CREATE TABLE ftjoin ( id int, name VARCHAR(10), geom GEOMETRY)" );
        //run("CALL AddGeometryColumn('geotools', 'ftjoin', 'geom', 4326, 'GEOMETRY', 2)");
        run("ALTER TABLE ftjoin ALTER COLUMN name VARCHAR(255) COLLATE Latin1_General_CS_AS");
        run( "INSERT INTO ftjoin VALUES (0, 'zero', geometry::STGeomFromText('POLYGON ((-0.1 -0.1, -0.1 0.1, 0.1 0.1, 0.1 -0.1, -0.1 -0.1))', 4326))");
        run( "INSERT INTO ftjoin VALUES (1, 'one', geometry::STGeomFromText('POLYGON ((-1.1 -1.1, -1.1 1.1, 1.1 1.1, 1.1 -1.1, -1.1 -1.1))', 4326))");
        run( "INSERT INTO ftjoin VALUES (2, 'two', geometry::STGeomFromText('POLYGON ((-10 -10, -10 10, 10 10, 10 -10, -10 -10))', 4326))");
        run( "INSERT INTO ftjoin VALUES (3, 'three', NULL)");
    }

    @Override
    protected void dropJoinTable() throws Exception {
        runSafe("DROP TABLE ftjoin");
    }

}
