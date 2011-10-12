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
package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCJoinTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class PostgisJoinTestSetup extends JDBCJoinTestSetup {

    public PostgisJoinTestSetup() {
        this(new PostGISTestSetup());
    }
    
    public PostgisJoinTestSetup(JDBCTestSetup setup) {
        super(setup);
    }

    @Override
    protected void createJoinTable() throws Exception {
        run("CREATE TABLE \"ftjoin\" ( \"id\" int, " + "\"name\" VARCHAR, \"geom\" GEOMETRY)" );
        run("INSERT INTO geometry_columns VALUES ('', 'public', 'ftjoin', 'geom', 2, 4326, 'GEOMETRY')");
        
        run( "INSERT INTO \"ftjoin\" VALUES (0, 'zero', ST_GeomFromText('POLYGON ((-0.1 -0.1, -0.1 0.1, 0.1 0.1, 0.1 -0.1, -0.1 -0.1))', 4326))");
        run( "INSERT INTO \"ftjoin\" VALUES (1, 'one', ST_GeomFromText('POLYGON ((-1.1 -1.1, -1.1 1.1, 1.1 1.1, 1.1 -1.1, -1.1 -1.1))', 4326))");
        run( "INSERT INTO \"ftjoin\" VALUES (2, 'two', ST_GeomFromText('POLYGON ((-10 -10, -10 10, 10 10, 10 -10, -10 -10))', 4326))");
        run( "INSERT INTO \"ftjoin\" VALUES (3, 'three', NULL)");
    }

    @Override
    protected void dropJoinTable() throws Exception {
        run( "DROP TABLE \"ftjoin\"" );
        run( "DELETE FROM geometry_columns WHERE f_table_name = 'ftjoin'");
    }

}
