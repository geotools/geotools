/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2014, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.jdbc.JDBCViewTestSetup;

public class SpatiaLiteViewTestSetup extends JDBCViewTestSetup {

    public SpatiaLiteViewTestSetup() {
        super(new SpatiaLiteTestSetup());
    }

    @Override
    protected void createLakesTable() throws Exception {
        run("CREATE TABLE lakes(fid int primary key, id int)");
        run("SELECT AddGeometryColumn('lakes', 'geom', 4326, 'POLYGON', 2)");
        run("ALTER TABLE lakes ADD COLUMN name varchar");
        run(
                "INSERT INTO lakes (fid, id, geom,name) VALUES ( 0, 0,"
                        + "ST_GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),"
                        + "'muddy')");
    }

    @Override
    protected void createLakesView() throws Exception {
        run("CREATE VIEW IF NOT EXISTS lakesview as select * from lakes");
        run(
                "INSERT INTO views_geometry_columns VALUES ('lakesview', 'geom', 'id', 'lakes', 'geom')");
    }

    @Override
    protected void dropLakesTable() throws Exception {
        runSafe("DROP TABLE lakes");
        runSafe("DELETE FROM geometry_columns where f_table_name in ('lakes')");
    }

    @Override
    protected void dropLakesView() throws Exception {
        run("DELETE FROM views_geometry_columns WHERE view_name = 'lakesview'");
        runSafe("DROP VIEW lakesview");
    }

    @Override
    protected void createLakesViewPk() throws Exception {}

    @Override
    protected void dropLakesViewPk() throws Exception {}
}
