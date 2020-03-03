/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014 - 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.jts;

import static org.junit.Assert.*;

import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;

public class WKTReader2Test {

    @Test
    public void verifyWKT() throws Exception {
        String WKT =
                "LINESTRING (60 380, 60 20, 200 400, 280 20, 360 400, 420 20, 500 400, 580 20, 620 400)";
        WKTReader reader = new WKTReader2();

        Geometry geometry = reader.read(WKT);
        assertNotNull(geometry);
        assertTrue(geometry instanceof LineString);
    }

    @Test
    public void multiPoint() throws Exception {
        String WKT = "MULTIPOINT (111 -47, 110 -46.5)";
        WKTReader reader = new WKTReader2();

        Geometry geometry = reader.read(WKT);
        assertNotNull(geometry);
        assertTrue(geometry instanceof MultiPoint);
        MultiPoint mp = (MultiPoint) geometry;
        assertEquals(2, mp.getNumGeometries());
        assertEquals(new Coordinate(111, -47), mp.getGeometryN(0).getCoordinate());
        assertEquals(new Coordinate(110, -46.5), mp.getGeometryN(1).getCoordinate());
    }

    @Test
    public void multiPointWithInnerParens() throws Exception {
        String WKT = "MULTIPOINT ((111 -47), (110 -46.5))";
        WKTReader reader = new WKTReader2();

        Geometry geometry = reader.read(WKT);
        assertNotNull(geometry);
        assertTrue(geometry instanceof MultiPoint);
        MultiPoint mp = (MultiPoint) geometry;
        assertEquals(2, mp.getNumGeometries());
        assertEquals(new Coordinate(111, -47), mp.getGeometryN(0).getCoordinate());
        assertEquals(new Coordinate(110, -46.5), mp.getGeometryN(1).getCoordinate());
    }

    /** Draw a circle between the start and end point; or each group of three their after. */
    @Test
    public void circularString() throws Exception {
        String WKT =
                "CIRCULARSTRING(220268.439465645 150415.359530563, 220227.333322076 150505.561285879, 220227.353105332 150406.434743975)";
        WKTReader2 reader = new WKTReader2(0.2);

        Geometry geometry = reader.read(WKT);
        assertNotNull("parsed circularstring", geometry);
        assertTrue(geometry instanceof CircularString);
        CircularString cs1 = (CircularString) geometry;
        // System.out.println(cs1.toText());
        assertEquals("segmentized as expected", 86, cs1.getNumPoints());

        WKT =
                "CIRCULARSTRING(143.62025166838282 -30.037497356076827, 142.92857147299705 -32.75101196874403, 145.96132309891922 -34.985671061528784, 149.57565307617188 -33.41153335571289, 149.41972407584802 -29.824672680573517, 146.1209416055467 -30.19711586270431, 143.62025166838282 -30.037497356076827)";
        geometry = reader.read(WKT);
        assertNotNull("parsed circularstring ring", geometry);
        Coordinate[] array = geometry.getCoordinates();
        assertEquals("forms a ring", array[0], array[array.length - 1]);

        WKT =
                "CIRCULARSTRING(143.62025166838282 -30.037497356076827, 142.92857147299705 -32.75101196874403, 143.62025166838282 -30.037497356076827)";
        geometry = reader.read(WKT);
        assertNotNull("parsed perfect circle", geometry);
        assertEquals(11, geometry.getNumPoints());

        WKT = "CIRCULARSTRING EMPTY";
        geometry = reader.read(WKT);
        assertNotNull(geometry);
        assertTrue(geometry.isEmpty());
    }

    @Test
    public void compoundCurve() throws Exception {
        String WKT =
                "COMPOUNDCURVE((153.72942375 -27.21757040, 152.29285719 -29.23940482, 154.74034096 -30.51635287),CIRCULARSTRING(154.74034096 -30.51635287, 154.74034096 -30.51635287, 152.39926953 -32.16574411, 155.11278414 -34.08116619, 151.86720784 -35.62414508))";
        WKTReader reader = new WKTReader2();

        Geometry geometry = reader.read(WKT);
        assertNotNull(geometry);

        WKT =
                "COMPOUNDCURVE((153.72942375 -27.21757040, 152.29285719 -29.23940482, 154.74034096 -30.51635287))";
        geometry = reader.read(WKT);
        assertNotNull(geometry);

        WKT =
                "COMPOUNDCURVE(CIRCULARSTRING(154.74034096 -30.51635287, 154.74034096 -30.51635287, 152.39926953 -32.16574411, 155.11278414 -34.08116619, 151.86720784 -35.62414508))";
        geometry = reader.read(WKT);
        assertNotNull(geometry);

        WKT = "COMPOUNDCURVE EMPTY";
        geometry = reader.read(WKT);
        assertNotNull(geometry);
        assertTrue(geometry.isEmpty());
    }

    @Test
    public void curvePolygon() throws Exception {
        // perfect circle!
        WKTReader reader = new WKTReader2();
        String WKT;
        Polygon polygon;
        Geometry geometry;

        WKT =
                "CURVEPOLYGON(CIRCULARSTRING(143.62025166838282 -30.037497356076827, 142.92857147299705 -32.75101196874403, 143.62025166838282 -30.037497356076827))";
        geometry = reader.read(WKT);
        assertNotNull("read curvepolygon", geometry);
        assertTrue(geometry instanceof Polygon);
        polygon = (Polygon) geometry;
        assertTrue(polygon.getExteriorRing() instanceof CircularRing);
        assertTrue("ring", polygon.getExteriorRing().isClosed());
        assertEquals("segmented ring", 51, polygon.getExteriorRing().getNumPoints());
        assertEquals("no holes", 0, polygon.getNumInteriorRing());

        WKT =
                "CURVEPOLYGON((144.84399355252685 -31.26123924022086, 144.20551952601693 -32.27215644886158, 145.55230712890625 -33.49203872680664, 147.97080993652344 -32.03618621826172, 146.38697244992585 -31.47406391572417, 144.84399355252685 -31.26123924022086))";
        polygon = (Polygon) reader.read(WKT);
        assertTrue("ring", polygon.getExteriorRing().isClosed());
        assertEquals("no holes", 0, polygon.getNumInteriorRing());

        WKT =
                "CURVEPOLYGON("
                        + "CIRCULARSTRING(143.62025166838282 -30.037497356076827, 142.92857147299705 -32.75101196874403, 145.96132309891922 -34.985671061528784, 149.57565307617188 -33.41153335571289, 149.41972407584802 -29.824672680573517, 146.1209416055467 -30.19711586270431, 143.62025166838282 -30.037497356076827),"
                        + "(144.84399355252685 -31.26123924022086, 144.20551952601693 -32.27215644886158, 145.55230712890625 -33.49203872680664, 147.97080993652344 -32.03618621826172, 146.38697244992585 -31.47406391572417, 144.84399355252685 -31.26123924022086))";

        polygon = (Polygon) reader.read(WKT);
        assertTrue("ring", polygon.getExteriorRing().isClosed());
        assertTrue(polygon.getExteriorRing() instanceof CircularRing);
        assertEquals("one holes", 1, polygon.getNumInteriorRing());
        assertFalse(polygon.getInteriorRingN(0) instanceof CircularRing);

        WKT =
                "CURVEPOLYGON(COMPOUNDCURVE(CIRCULARSTRING(0 0,2 0, 2 1, 2 3, 4 3),(4 3, 4 5, 1 4, 0 0)), CIRCULARSTRING(1.7 1, 1.4 0.4, 1.6 0.4, 1.6 0.5, 1.7 1) )";
        polygon = (Polygon) reader.read(WKT);
        assertTrue("ring", polygon.getExteriorRing().isClosed());
        assertTrue(polygon.getExteriorRing() instanceof CompoundRing);
        assertEquals("one holes", 1, polygon.getNumInteriorRing());
        assertTrue(polygon.getInteriorRingN(0) instanceof CircularRing);
    }

    @Test
    public void testParseMulticurve() throws Exception {
        WKTReader reader = new WKTReader2();
        String WKT = "MULTICURVE EMPTY";
        MultiLineString ml = (MultiLineString) reader.read(WKT);
        assertTrue(ml.isEmpty());

        WKT = "MULTICURVE((0 0, 5 5),CIRCULARSTRING(4 0, 4 4, 8 4))";
        ml = (MultiLineString) reader.read(WKT);
        assertEquals(2, ml.getNumGeometries());
        assertTrue(ml.getGeometryN(0).getClass() == LineString.class);
        assertTrue(ml.getGeometryN(1) instanceof CircularString);

        WKT =
                "MULTICURVE((100 100, 120 120), COMPOUNDCURVE(CIRCULARSTRING(0 0, 2 0, 2 1, 2 3, 4 3),(4 3, 4 5, 1 4, 0 0)))";
        ml = (MultiLineString) reader.read(WKT);
        assertEquals(2, ml.getNumGeometries());
        assertTrue(ml.getGeometryN(0).getClass() == LineString.class);
        assertTrue(ml.getGeometryN(1) instanceof CompoundRing);
    }

    @Test
    public void testCaseInsensitive() throws Exception {
        WKTReader reader = new WKTReader2();
        assertNotNull(reader.read("POINT(1 2)"));
        assertNotNull(reader.read("Point(1 2)"));

        assertNotNull(reader.read("LINESTRING(0 2, 2 0, 8 6)"));
        assertNotNull(reader.read("LineString(0 2, 2 0, 8 6)"));
    }

    @Test
    public void testMultiSurfaceStraightPolygon() throws Exception {
        String wkt =
                "MULTISURFACE (((0 0, 1 0, 1 4, 0 0)), CURVEPOLYGON (COMPOUNDCURVE (CIRCULARSTRING (0 2, 7 5, 2 10), (2 10, 0 2)), COMPOUNDCURVE (CIRCULARSTRING (3 9, 6 5, 3 2), (3 2, 3 9))))";
        Geometry geometry = new WKTReader2().read(wkt);
        // System.out.println(geometry);
        MultiSurface ms = (MultiSurface) geometry;
        assertEquals(2, ms.getNumGeometries());
        assertFalse(ms.getGeometryN(0) instanceof CurvePolygon);
        assertTrue(ms.getGeometryN(1) instanceof CurvePolygon);
    }

    @Test
    public void testMultiSurfaceStraightPolygon2() throws Exception {
        String wkt =
                "MULTISURFACE (CURVEPOLYGON (COMPOUNDCURVE (CIRCULARSTRING (0 2, 7 5, 2 10), (2 10, 0 2)), COMPOUNDCURVE (CIRCULARSTRING (3 9, 6 5, 3 2), (3 2, 3 9))), ((0 0, 1 0, 1 4, 0 0)))";
        Geometry geometry = new WKTReader2().read(wkt);
        // System.out.println(geometry);
        MultiSurface ms = (MultiSurface) geometry;
        assertEquals(2, ms.getNumGeometries());
        assertTrue(ms.getGeometryN(0) instanceof CurvePolygon);
        assertFalse(ms.getGeometryN(1) instanceof CurvePolygon);
    }

    @Test
    public void testMultiSurfaceEmpty() throws Exception {
        String wkt =
                "MULTISURFACE (EMPTY, CURVEPOLYGON (COMPOUNDCURVE (CIRCULARSTRING (0 2, 7 5, 2 10), (2 10, 0 2)), COMPOUNDCURVE (CIRCULARSTRING (3 9, 6 5, 3 2), (3 2, 3 9))), ((0 0, 1 0, 1 4, 0 0)))";
        Geometry geometry = new WKTReader2().read(wkt);
        // System.out.println(geometry);
        MultiSurface ms = (MultiSurface) geometry;
        assertEquals(3, ms.getNumGeometries());
        assertFalse(ms.getGeometryN(0) instanceof CurvePolygon);
        assertTrue(ms.getGeometryN(1) instanceof CurvePolygon);
        assertFalse(ms.getGeometryN(2) instanceof CurvePolygon);
    }
}
