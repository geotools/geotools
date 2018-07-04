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
package org.geotools.data.spatialite;

import org.geotools.jdbc.JDBCAggregateTestSetup;

public class SpatiaLiteAggregateTestSetup extends JDBCAggregateTestSetup {

    protected SpatiaLiteAggregateTestSetup() {
        super(new SpatiaLiteTestSetup());
    }

    @Override
    protected void createAggregateTable() throws Exception {
        run("CREATE TABLE aggregate (fid INTEGER PRIMARY KEY, id INTEGER)");
        run("SELECT AddGeometryColumn('aggregate','geom',4326,'POLYGON',2)");
        run("ALTER TABLE aggregate add name VARCHAR");
        run(
                "INSERT INTO aggregate VALUES (0, 0,"
                        + "GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),"
                        + "'muddy1')");
        run(
                "INSERT INTO aggregate VALUES (1, 1,"
                        + "GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),"
                        + "'muddy1')");
        run(
                "INSERT INTO aggregate VALUES (2, 2,"
                        + "GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),"
                        + "'muddy2')");
    }

    @Override
    protected void dropAggregateTable() throws Exception {
        run("DROP TABLE aggregate");
        run("DELETE FROM geometry_columns WHERE f_table_name = 'aggregate'");
    }
}
