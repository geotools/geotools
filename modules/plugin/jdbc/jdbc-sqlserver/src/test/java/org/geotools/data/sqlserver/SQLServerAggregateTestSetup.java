/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.jdbc.JDBCAggregateTestSetup;

public class SQLServerAggregateTestSetup extends JDBCAggregateTestSetup {

    public SQLServerAggregateTestSetup() {
        super(new SQLServerTestSetup());
    }

    @Override
    protected void createAggregateTable() throws Exception {
        run(
                "CREATE TABLE aggregate(fid int IDENTITY(0,1) PRIMARY KEY, id int, "
                        + "geom geometry, name varchar(255) )");

        run(
                "INSERT INTO aggregate (id,geom,name) VALUES ( 0,"
                        + "geometry::STGeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),"
                        + "'muddy1')");
        run(
                "INSERT INTO aggregate (id,geom,name) VALUES ( 1,"
                        + "geometry::STGeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),"
                        + "'muddy1')");
        run(
                "INSERT INTO aggregate (id,geom,name) VALUES ( 2,"
                        + "geometry::STGeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),"
                        + "'muddy2')");

        run(
                "CREATE SPATIAL INDEX _aggregate_geometry_index on aggregate(geom) WITH (BOUNDING_BOX = (-100, -100, 100, 100))");
    }

    @Override
    protected void dropAggregateTable() throws Exception {
        run("DROP TABLE aggregate");
    }
}
