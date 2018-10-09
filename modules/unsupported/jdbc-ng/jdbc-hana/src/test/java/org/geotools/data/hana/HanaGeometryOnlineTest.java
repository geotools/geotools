/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana;

import org.geotools.jdbc.JDBCGeometryOnlineTest;
import org.geotools.jdbc.JDBCGeometryTestSetup;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/** @author Stefan Uhrig, SAP SE */
public class HanaGeometryOnlineTest extends JDBCGeometryOnlineTest {

    /*
     * HANA does not distinguish between different geometry types on column level. Therefore, we always bind to Geometry.class.
     */

    @Override
    protected JDBCGeometryTestSetup createTestSetup() {
        return new HanaGeometryTestSetup(new HanaTestSetup());
    }

    @Override
    public void testPoint() throws Exception {
        assertEquals(Geometry.class, checkGeometryType(Point.class));
    }

    @Override
    public void testLineString() throws Exception {
        assertEquals(Geometry.class, checkGeometryType(LineString.class));
    }

    @Override
    public void testLinearRing() throws Exception {
        assertEquals(Geometry.class, checkGeometryType(LinearRing.class));
    }

    @Override
    public void testPolygon() throws Exception {
        assertEquals(Geometry.class, checkGeometryType(Polygon.class));
    }

    @Override
    public void testMultiPoint() throws Exception {
        assertEquals(Geometry.class, checkGeometryType(MultiPoint.class));
    }

    @Override
    public void testMultiLineString() throws Exception {
        assertEquals(Geometry.class, checkGeometryType(MultiLineString.class));
    }

    @Override
    public void testMultiPolygon() throws Exception {
        assertEquals(Geometry.class, checkGeometryType(MultiPolygon.class));
    }

    @Override
    public void testGeometryCollection() throws Exception {
        assertEquals(Geometry.class, checkGeometryType(GeometryCollection.class));
    }
}
