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

import static org.junit.Assert.assertEquals;

import java.util.Map;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCGeometryOnlineTest;
import org.geotools.jdbc.JDBCGeometryTestSetup;
import org.junit.Ignore;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

public class DuckDBGeometryOnlineTest extends JDBCGeometryOnlineTest {
    private static final String LINEAR_RING_REPORTED_AS_LINESTRING = "DuckDB reports linear rings back as line strings";

    @Override
    protected JDBCGeometryTestSetup createTestSetup() {
        return new DuckDBGeometryTestSetup();
    }

    @Override
    protected Map<String, Object> createDataStoreFactoryParams() throws Exception {
        Map<String, Object> params = super.createDataStoreFactoryParams();
        params.put(AbstractDuckDBDataStoreFactory.READ_ONLY.key, Boolean.FALSE);
        params.put(JDBCDataStoreFactory.BATCH_INSERT_SIZE.key, 1);
        return params;
    }

    @Override
    @Test
    public void testPoint() throws Exception {
        assertEquals(Geometry.class, checkGeometryType(Point.class));
    }

    @Override
    @Test
    public void testLineString() throws Exception {
        assertEquals(Geometry.class, checkGeometryType(LineString.class));
    }

    @Override
    @Test
    @Ignore(LINEAR_RING_REPORTED_AS_LINESTRING)
    public void testLinearRing() throws Exception {
        // Ignored on purpose. See @Ignore reason.
    }

    @Override
    @Test
    public void testPolygon() throws Exception {
        assertEquals(Geometry.class, checkGeometryType(Polygon.class));
    }

    @Override
    @Test
    public void testMultiPoint() throws Exception {
        assertEquals(Geometry.class, checkGeometryType(MultiPoint.class));
    }

    @Override
    @Test
    public void testMultiLineString() throws Exception {
        assertEquals(Geometry.class, checkGeometryType(MultiLineString.class));
    }

    @Override
    @Test
    public void testMultiPolygon() throws Exception {
        assertEquals(Geometry.class, checkGeometryType(MultiPolygon.class));
    }

    @Override
    @Test
    public void testGeometryCollection() throws Exception {
        // DuckDB exposes heterogeneous collections through the generic GEOMETRY binding.
        assertEquals(Geometry.class, checkGeometryType(org.locationtech.jts.geom.GeometryCollection.class));
    }
}
