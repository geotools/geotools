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
package org.geotools.data.h2;

import org.geotools.jdbc.JDBCAggregateTestSetup;

public class H2AggregateTestSetup extends JDBCAggregateTestSetup {

    protected H2AggregateTestSetup() {
        super(new H2TestSetup());
    }

    @Override
    protected void createAggregateTable() throws Exception {
        run(
                "CREATE TABLE \"geotools\".\"aggregate\"(\"fid\" int AUTO_INCREMENT(0) PRIMARY KEY, \"id\" int, "
                        + "\"geom\" POLYGON, \"name\" varchar )");
        run("CALL AddGeometryColumn('geotools', 'aggregate', 'geom', 4326, 'POLYGON', 2)");
        run("CALL CreateSpatialIndex('geotools', 'aggregate', 'geom', 4326)");
        run(
                "INSERT INTO \"geotools\".\"aggregate\" (\"id\",\"geom\",\"name\") VALUES ( 0,"
                        + "ST_GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),"
                        + "'muddy1')");
        run(
                "INSERT INTO \"geotools\".\"aggregate\" (\"id\",\"geom\",\"name\") VALUES ( 1,"
                        + "ST_GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),"
                        + "'muddy1')");
        run(
                "INSERT INTO \"geotools\".\"aggregate\" (\"id\",\"geom\",\"name\") VALUES ( 2,"
                        + "ST_GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),"
                        + "'muddy2')");
    }

    @Override
    protected void dropAggregateTable() throws Exception {
        runSafe("DROP TABLE \"geotools\".\"aggregate\"");
        runSafe("CALL DropGeometryColumn('geotools', 'aggregate', 'geom')");
        runSafe("DROP TABLE \"geotools\".\"aggregate_HATBOX\"");
    }
}
