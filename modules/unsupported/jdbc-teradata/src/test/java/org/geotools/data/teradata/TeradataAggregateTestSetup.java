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
package org.geotools.data.teradata;

import org.geotools.jdbc.JDBCAggregateTestSetup;

public class TeradataAggregateTestSetup extends JDBCAggregateTestSetup {

    protected TeradataAggregateTestSetup() {
        super(new TeradataTestSetup());
    }

    @Override
    protected void createAggregateTable() throws Exception {
        run(
                "CREATE TABLE \"aggregate\"(\"fid\" PRIMARY KEY not null generated always as identity (start with 0) integer, \"id\" int, \"geom\" st_geometry, \"name\" varchar(200) )");
        run(
                "INSERT INTO SYSSPATIAL.GEOMETRY_COLUMNS (F_TABLE_CATALOG, F_TABLE_SCHEMA, F_TABLE_NAME, F_GEOMETRY_COLUMN, COORD_DIMENSION, SRID, GEOM_TYPE) VALUES ('', '"
                        + fixture.getProperty("schema")
                        + "', 'aggregate', 'geom', 2, "
                        + getDelegate().getSrid4326()
                        + ", 'POLYGON')");

        // add the spatial index
        run(
                "CREATE MULTISET TABLE \"aggregate_geom_idx\""
                        + " (id INTEGER NOT NULL, cellid INTEGER NOT NULL) PRIMARY INDEX (cellid)");

        run(
                "INSERT INTO \"aggregate\" (\"fid\", \"id\",\"geom\",\"name\") VALUES (0, 0,'POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))', 'muddy1');");
        run(
                "INSERT INTO \"aggregate\" (\"fid\", \"id\",\"geom\",\"name\") VALUES (1, 1,'POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))', 'muddy1');");
        run(
                "INSERT INTO \"aggregate\" (\"fid\", \"id\",\"geom\",\"name\") VALUES (2, 2,'POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))', 'muddy2');");
    }

    public TeradataTestSetup getDelegate() {
        return (TeradataTestSetup) delegate;
    }

    @Override
    protected void dropAggregateTable() throws Exception {
        runSafe("DELETE FROM SYSSPATIAL.GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'aggregate'");
        runSafe("DROP TRIGGER \"aggregate_geom_mi\"");
        runSafe("DROP TRIGGER \"aggregate_geom_mu\"");
        runSafe("DROP TRIGGER \"aggregate_geom_md\"");
        runSafe("DROP TABLE \"aggregate\"");
        runSafe("DROP TABLE \"aggregate_geom_idx\"");
    }
}
