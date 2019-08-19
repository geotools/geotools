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
package org.geotools.geopkg;

import org.geotools.jdbc.JDBCEmptyGeometryTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class GeoPkgEmptyGeometryTestSetup extends JDBCEmptyGeometryTestSetup {

    public GeoPkgEmptyGeometryTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    String[] types = {
        "POINT", "LINESTRING", "POLYGON", "MULTIPOINT", "MULTILINESTRING", "MULTIPOLYGON"
    };

    @Override
    protected void createEmptyGeometryTable() throws Exception {
        // create table schema

        for (String type : types) {
            run(
                    "CREATE TABLE \"empty_"
                            + type.toLowerCase()
                            + "\"(" //
                            + "\"fid\" serial primary key, " //
                            + "\"id\" integer, " //
                            + "\"geom_"
                            + type.toLowerCase()
                            + "\" geometry, "
                            + "\"name\" varchar)");

            String sql =
                    "INSERT INTO gpkg_contents (table_name, data_type, identifier, srs_id) VALUES "
                            + "('empty_"
                            + type.toLowerCase()
                            + "', 'features', 'empty_"
                            + type.toLowerCase()
                            + "', 4326)";
            run(sql);

            sql =
                    "INSERT INTO gpkg_geometry_columns VALUES ('empty_"
                            + type.toLowerCase()
                            + "', 'geom_point', '"
                            + type
                            + "', 4326, 0, 0)";
            run(sql);
        }

        /*    + "CONSTRAINT enforce_geotype_geom_1 CHECK (geometrytype(geom_point) = 'POINT'::text OR geom_point IS NULL)," //
                   + "CONSTRAINT enforce_geotype_geom_2 CHECK (geometrytype(geom_linestring) = 'LINESTRING'::text OR geom_linestring IS NULL)," //
                   + "CONSTRAINT enforce_geotype_geom_3 CHECK (geometrytype(geom_polygon) = 'POLYGON'::text OR geom_polygon IS NULL)," //
                   + "CONSTRAINT enforce_geotype_geom_4 CHECK (geometrytype(geom_multipoint) = 'MULTIPOINT'::text OR geom_multipoint IS NULL)," //
                   + "CONSTRAINT enforce_geotype_geom_5 CHECK (geometrytype(geom_multilinestring) = 'MULTILINESTRING'::text OR geom_multilinestring IS NULL)," //
                   + "CONSTRAINT enforce_geotype_geom_6 CHECK (geometrytype(geom_multipolygon) = 'MULTIPOLYGON'::text OR geom_multipolygon IS NULL)" //
                + ")");
        */
    }

    @Override
    protected void dropEmptyGeometryTable() throws Exception {
        for (String type : types) {
            ((GeoPkgTestSetup) delegate).removeTable("empty_" + type.toLowerCase());
        }
    }
}
