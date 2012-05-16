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
package org.geotools.data.postgis;

import org.geotools.jdbc.JDBC3DTestSetup;

/**
 * 
 * 
 * @source $URL$
 */
public class PostGIS3DTestSetup extends JDBC3DTestSetup {

    protected PostGIS3DTestSetup() {
        super(new PostGISTestSetup());
    
    }

    @Override
    protected void createLine3DTable() throws Exception {
        // setup table
        run("CREATE TABLE \"line3d\"(\"fid\" serial PRIMARY KEY, \"id\" int, "
                + "\"geom\" geometry, \"name\" varchar )");
        run("INSERT INTO GEOMETRY_COLUMNS VALUES('', 'public', 'line3d', 'geom', 3, '4326', 'LINESTRING')");
        run("CREATE INDEX line3d_GEOM_IDX ON \"line3d\" USING GIST (\"geom\") ");
    
        // insert data
        run("INSERT INTO \"line3d\" (\"id\",\"geom\",\"name\") VALUES (0,"
                + "GeomFromText('LINESTRING(1 1 0, 2 2 0, 4 2 1, 5 1 1)', 4326),"
                + "'l1')");
        run("INSERT INTO \"line3d\" (\"id\",\"geom\",\"name\") VALUES (1,"
                + "GeomFromText('LINESTRING(3 0 1, 3 2 2, 3 3 3, 3 4 5)', 4326),"
                + "'l2')");
    }

    @Override
    protected void createPoint3DTable() throws Exception {
        // setup table
        run("CREATE TABLE \"point3d\"(\"fid\" serial PRIMARY KEY, \"id\" int, "
                + "\"geom\" geometry, \"name\" varchar )");
        run("INSERT INTO GEOMETRY_COLUMNS VALUES('', 'public', 'point3d', 'geom', 3, '4326', 'POINT')");
        run("CREATE INDEX POINT3D_GEOM_IDX ON \"point3d\" USING GIST (\"geom\") ");
    
        // insert data
        run("INSERT INTO \"point3d\" (\"id\",\"geom\",\"name\") VALUES (0,"
                + "GeomFromText('POINT(1 1 1)', 4326)," + "'p1')");
        run("INSERT INTO \"point3d\" (\"id\",\"geom\",\"name\") VALUES (1,"
                + "GeomFromText('POINT(3 0 1)', 4326)," + "'p2')");
    }

    @Override
    protected void dropLine3DTable() throws Exception {
        run("DELETE FROM  GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'line3d'");
        run("DROP TABLE \"line3d\"");
    }

    @Override
    protected void dropPoly3DTable() throws Exception {
        run("DELETE FROM  GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'poly3d'");
        run("DROP TABLE \"poly3d\"");
    }

    @Override
    protected void dropPoint3DTable() throws Exception {
        run("DELETE FROM  GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'point3d'");
        run("DROP TABLE \"point3d\"");
    }

}
