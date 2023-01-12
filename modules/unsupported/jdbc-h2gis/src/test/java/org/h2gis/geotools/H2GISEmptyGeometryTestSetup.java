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
package org.h2gis.geotools;

import org.geotools.jdbc.JDBCEmptyGeometryTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

@SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation") // not yet a JUnit4 test
public class H2GISEmptyGeometryTestSetup extends JDBCEmptyGeometryTestSetup {

    public H2GISEmptyGeometryTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createEmptyGeometryTable() throws Exception {
        run("DROP TABLE IF EXISTS \"geotools\".\"empty\";");
        // create table schema
        run(
                "CREATE TABLE \"geotools\".\"empty\"(" //
                        + "\"fid\" serial primary key, " //
                        + "\"id\" integer, " //
                        + "\"geom_point\" GEOMETRY(POINT, 4326), " //
                        + "\"geom_linestring\" GEOMETRY(LINESTRING, 4326), " //
                        + "\"geom_polygon\" GEOMETRY(POLYGON, 4326), " //
                        + "\"geom_multipoint\" GEOMETRY(MULTIPOINT, 4326), " //
                        + "\"geom_multilinestring\" GEOMETRY(MULTILINESTRING, 4326), " //
                        + "\"geom_multipolygon\" GEOMETRY(MULTIPOLYGON, 4326), " //
                        + "\"name\" varchar" //
                        + ")");
    }

    @Override
    protected void dropEmptyGeometryTable() throws Exception {
        run("DELETE FROM geometry_columns WHERE f_table_name = 'empty'");
        run("DROP TABLE \"geotools\".\"empty\"");
    }
}
