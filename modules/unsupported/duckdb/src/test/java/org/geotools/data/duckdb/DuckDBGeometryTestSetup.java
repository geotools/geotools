/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.duckdb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import org.geotools.jdbc.JDBCGeometryTestSetup;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

public class DuckDBGeometryTestSetup extends JDBCGeometryTestSetup {

    protected DuckDBGeometryTestSetup() {
        super(new DuckDBTestSetup());
    }

    @Override
    protected Connection getConnection() throws SQLException, IOException {
        return ((DuckDBTestSetup) delegate).getConnection();
    }

    // Use raw List for compatibility with both older and newer JDBCGeometryTestSetup test-jar signatures.
    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected List getGeometryClasses() {
        return Arrays.asList(
                Point.class,
                LineString.class,
                Polygon.class,
                MultiPoint.class,
                MultiLineString.class,
                MultiPolygon.class,
                Geometry.class,
                GeometryCollection.class);
    }

    @Override
    protected void dropSpatialTable(String tableName) throws Exception {
        runSafe("DROP VIEW IF EXISTS \"" + tableName + "\"");
        runSafe("DROP TABLE IF EXISTS \"" + tableName + "\"");
    }
}
