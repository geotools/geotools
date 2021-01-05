/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.jdbc.JDBCDelegatingTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

@SuppressWarnings({
    "PMD.JUnit4TestShouldUseTestAnnotation",
    "PMD.JUnit4TestShouldUseAfterAnnotation"
}) // not a test by itself
public class PostgisPreserveTopologyTestSetup extends JDBCDelegatingTestSetup {
    public PostgisPreserveTopologyTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void setUpData() throws Exception {
        super.setUpData();
        dropSimplifyPolygonTable();
        createSimplifyPolygonTable();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        dropSimplifyPolygonTable();
    }

    protected void createSimplifyPolygonTable() throws Exception {
        run(
                "CREATE TABLE \"simplify_polygon_topology\" AS SELECT ST_GeomFromText('POLYGON(("
                        + "-10 -10, 10 -10, 10 10, -10 10, -10 -10), (-1 -1, 1 -1, 1 1, -1 1, -1 -1))'"
                        + ",4326) as \"geom\"");
    }

    protected void dropSimplifyPolygonTable() {
        runSafe("DELETE FROM GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'simplify_polygon_topology'");
        runSafe("DROP TABLE \"simplify_polygon_topology\"");
    }
}
