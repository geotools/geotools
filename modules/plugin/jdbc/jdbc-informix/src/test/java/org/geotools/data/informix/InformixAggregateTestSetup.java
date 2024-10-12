/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.informix;

import org.geotools.jdbc.JDBCAggregateTestSetup;

public class InformixAggregateTestSetup extends JDBCAggregateTestSetup {

    protected InformixAggregateTestSetup() {
        super(new InformixTestSetup());
    }

    @Override
    protected void createAggregateTable() throws Exception {
        run("CREATE TABLE aggregate(fid serial PRIMARY KEY, id integer, " + "geom ST_POLYGON, name varchar(255) );");

        run("INSERT INTO aggregate (id,geom,name) VALUES ( 0,"
                + "ST_GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',0)::ST_Polygon,"
                + "'muddy1')");
        run("INSERT INTO aggregate (id,geom,name) VALUES ( 1,"
                + "ST_GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',0)::ST_Polygon,"
                + "'muddy1')");
        run("INSERT INTO aggregate (id,geom,name) VALUES ( 2,"
                + "ST_GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',0)::ST_Polygon,"
                + "'muddy2')");
    }

    @Override
    protected void dropAggregateTable() throws Exception {
        run("DROP TABLE aggregate");
    }
}
