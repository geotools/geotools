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
package org.geotools.data.h2;

import org.geotools.jdbc.JDBCJoinTestSetup;

public class H2JoinTestSetup extends JDBCJoinTestSetup {

    protected H2JoinTestSetup() {
        super(new H2TestSetup());
    }

    @Override
    protected void createJoinTable() throws Exception {
        run( "CREATE TABLE \"geotools\".\"ftjoin\" ( \"id\" int, " + "\"name\" VARCHAR, \"geom\" GEOMETRY, \"join1intProperty\" int)" );
        run("CALL AddGeometryColumn('geotools', 'ftjoin', 'geom', 4326, 'GEOMETRY', 2)");
        
        run( "INSERT INTO \"geotools\".\"ftjoin\" VALUES (0, 'zero', ST_GeomFromText('POLYGON ((-0.1 -0.1, -0.1 0.1, 0.1 0.1, 0.1 -0.1, -0.1 -0.1))', 4326), 0)");
        run( "INSERT INTO \"geotools\".\"ftjoin\" VALUES (1, 'one', ST_GeomFromText('POLYGON ((-1.1 -1.1, -1.1 1.1, 1.1 1.1, 1.1 -1.1, -1.1 -1.1))', 4326), 1)");
        run( "INSERT INTO \"geotools\".\"ftjoin\" VALUES (2, 'two', ST_GeomFromText('POLYGON ((-10 -10, -10 10, 10 10, 10 -10, -10 -10))', 4326), 2)");
        run( "INSERT INTO \"geotools\".\"ftjoin\" VALUES (3, 'three', NULL, 3)");
        
        run( "CREATE TABLE \"geotools\".\"ftjoin2\"(\"id\" int, \"join2intProperty\" int, \"stringProperty2\" varchar)");
        run( "INSERT INTO \"geotools\".\"ftjoin2\" VALUES (0, 0, '2nd zero')");
        run( "INSERT INTO \"geotools\".\"ftjoin2\" VALUES (1, 1, '2nd one')");
        run( "INSERT INTO \"geotools\".\"ftjoin2\" VALUES (2, 2, '2nd two')");
        run( "INSERT INTO \"geotools\".\"ftjoin2\" VALUES (3, 3, '2nd three')");
    }

    @Override
    protected void dropJoinTable() throws Exception {
        run( "DROP TABLE \"geotools\".\"ftjoin\"" );
        run( "DROP TABLE \"geotools\".\"ftjoin2\"" );
    }

}
