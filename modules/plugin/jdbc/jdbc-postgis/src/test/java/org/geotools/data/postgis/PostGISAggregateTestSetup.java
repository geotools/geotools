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
package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCAggregateTestSetup;

public class PostGISAggregateTestSetup extends JDBCAggregateTestSetup {

    public PostGISAggregateTestSetup() {
        super(new PostGISTestSetup());
    }

    @Override
    protected void createAggregateTable() throws Exception {
        run(
                "CREATE TABLE \"aggregate\"(\"fid\" serial PRIMARY KEY, \"id\" int, "
                        + "\"geom\" geometry, \"name\" varchar )");
        run(
                "INSERT INTO GEOMETRY_COLUMNS VALUES('', 'public', 'aggregate', 'geom', 2, '4326', 'POLYGON')");

        if (((PostGISTestSetup) delegate).isVersion2()) {
            run("ALTER TABLE \"aggregate\" ALTER COLUMN  \"geom\" TYPE geometry(Polygon,4326);");
        }
        run("CREATE INDEX AGGREGATE_GEOM_INDEX ON \"aggregate\" USING GIST (\"geom\") ");

        // advance the sequence to 1 to compensate for hand insertions
        run("SELECT nextval(pg_get_serial_sequence('aggregate','fid'))");

        run(
                "INSERT INTO \"aggregate\" (\"fid\", \"id\",\"geom\",\"name\") VALUES (0, 0,"
                        + "ST_GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),"
                        + "'muddy1')");
        run(
                "INSERT INTO \"aggregate\" (\"fid\", \"id\",\"geom\",\"name\") VALUES (1, 1,"
                        + "ST_GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),"
                        + "'muddy1')");
        run(
                "INSERT INTO \"aggregate\" (\"fid\", \"id\",\"geom\",\"name\") VALUES (2, 2,"
                        + "ST_GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),"
                        + "'muddy2')");
    }

    @Override
    protected void dropAggregateTable() throws Exception {
        runSafe("DELETE FROM GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'aggregate'");
        runSafe("DROP TABLE \"aggregate\"");
    }
}
