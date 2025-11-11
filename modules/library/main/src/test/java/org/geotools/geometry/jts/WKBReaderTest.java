/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.locationtech.jts.geom.CoordinateSequenceComparator;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;

public class WKBReaderTest {

    CurvedGeometryFactory curvedfactory = new CurvedGeometryFactory(Double.MAX_VALUE);
    WKBReader wkbReader = new WKBReader(curvedfactory);

    WKTReader2 wktReader = new WKTReader2(curvedfactory);

    CoordinateSequenceComparator compXY = new CoordinateSequenceComparator(2);
    CoordinateSequenceComparator compXYZ = new CoordinateSequenceComparator(3);

    @Test
    public void canReadPoint() {
        // SELECT ST_GeomFromText('POINT(1 1)')
        assertThat(
                readWkb("0101000000000000000000F03F000000000000F03F"),
                allOf(matchesExact(readWkt("POINT(1 1)"), compXY), hasProperty("SRID", is(0))));
    }

    @Test
    public void canReadPointWithSRID() {
        // SELECT ST_GeomFromText('POINT(1 1)', 4326)
        assertThat(
                readWkb("0101000020E6100000000000000000F03F000000000000F03F"),
                allOf(matchesExact(readWkt("POINT(1 1)"), compXY), hasProperty("SRID", is(4326))));
    }

    @Test
    public void canReadPointWithZ() {
        // SELECT ST_GeomFromText('POINT Z(1 1 2)')
        Point pointZ = readWkb("0101000080000000000000F03F000000000000F03F0000000000000040");
        assertThat(pointZ, allOf(matchesExact(readWkt("POINT(1 1 2)"), compXYZ), hasProperty("SRID", is(0))));
        assertThat(pointZ.getCoordinates()[0].getZ(), is(2D));
    }

    @Test
    public void canReadPointWithM() {
        // SELECT ST_GeomFromText('POINT M(1 1 200)')
        Point pointM = readWkb("0101000040000000000000F03F000000000000F03F0000000000006940");
        assertThat(pointM, allOf(matchesExact(readWkt("POINT(1 1)"), compXY), hasProperty("SRID", is(0))));
        assertThat(pointM.getCoordinates()[0].getM(), is(200D));
    }

    @Test
    public void canReadPointWithZM() {
        // SELECT ST_GeomFromText('POINT ZM(1 1 2 200)')
        Point pointZM = readWkb("01010000C0000000000000F03F000000000000F03F00000000000000400000000000006940");
        assertThat(pointZM, allOf(matchesExact(readWkt("POINT(1 1)"), compXY), hasProperty("SRID", is(0))));
        assertThat(pointZM.getCoordinates()[0].getZ(), is(2D));
        assertThat(pointZM.getCoordinates()[0].getM(), is(200D));
    }

    @Test
    public void canReadPointWithZM_OGC() {
        // test for geometry codes in (ISO/OGC 06-103r4) format
        // point zm -> 3001 = BB9
        // so update geom code    `010000C` (in canReadPointWithZM)
        Point pointZM = readWkb("01B90B0000000000000000F03F000000000000F03F00000000000000400000000000006940");
        assertThat(pointZM, allOf(matchesExact(readWkt("POINT(1 1)"), compXY), hasProperty("SRID", is(0))));
        assertThat(pointZM.getCoordinates()[0].getZ(), is(2D));
        assertThat(pointZM.getCoordinates()[0].getM(), is(200D));
    }

    @Test
    public void canReadMultiPoint() {
        // SELECT ST_GeomFromText('MULTIPOINT(1 1, 2 2)')
        assertThat(
                readWkb(
                        "0104000000020000000101000000000000000000F03F000000000000F03F010100000000000000000000400000000000000040"),
                matchesExact(readWkt("MULTIPOINT(1 1, 2 2)"), compXY));
    }

    @Test
    public void canReadLine() {
        // SELECT ST_GeomFromText('LINESTRING(1 1, 2 2)')
        assertThat(
                readWkb("010200000002000000000000000000F03F000000000000F03F00000000000000400000000000000040"),
                matchesExact(readWkt("LINESTRING(1 1, 2 2)"), compXY));
    }

    @Test
    public void canReadMultiLine() {
        // SELECT ST_GeomFromText('MULTILINESTRING((1 1, 2 2), (5 5, 6 6))')
        assertThat(
                readWkb(
                        "010500000002000000010200000002000000000000000000F03F000000000000F03F000000000000004000000000000000400102000000020000000000000000001440000000000000144000000000000018400000000000001840"),
                matchesExact(readWkt("MULTILINESTRING((1 1, 2 2), (5 5, 6 6))"), compXY));
    }

    @Test
    public void canReadMultiLineWithZ() {
        //         SELECT ST_GeomFromText('MULTILINESTRING((1 1 3, 2 2 3), (5 5 4, 6 6 4))')
        assertThat(
                readWkb(
                        "010500008002000000010200008002000000000000000000F03F000000000000F03F0000000000000840000000000000004000000000000000400000000000000840010200008002000000000000000000144000000000000014400000000000001040000000000000184000000000000018400000000000001040"),
                matchesExact(readWkt("MULTILINESTRING((1 1 3, 2 2 3), (5 5 4, 6 6 4))"), compXYZ));

        // SELECT ST_GeomFromText('MULTILINESTRING((1 1 3, 2 2 3), (5 5 4, 6 6 4))')
        // I've messed with the geometry code here `0500008` -> `ED030000` to use (ISO/OGC 06-103r4) format
        assertThat(
                readWkb(
                        "01ED03000002000000010200008002000000000000000000F03F000000000000F03F0000000000000840000000000000004000000000000000400000000000000840010200008002000000000000000000144000000000000014400000000000001040000000000000184000000000000018400000000000001040"),
                matchesExact(readWkt("MULTILINESTRING((1 1 3, 2 2 3), (5 5 4, 6 6 4))"), compXYZ));
    }

    @Test
    public void canReadPolygon() {
        // SELECT ST_GeomFromText('POLYGON((1 1, 2 2, 1 2, 1 1))')
        assertThat(
                readWkb(
                        "01030000000100000004000000000000000000F03F000000000000F03F00000000000000400000000000000040000000000000F03F0000000000000040000000000000F03F000000000000F03F"),
                matchesExact(readWkt("POLYGON((1 1, 2 2, 1 2, 1 1))"), compXY));
    }

    @Test
    public void canReadPolygonWithHole() {
        // SELECT ST_GeomFromText('POLYGON((0 0, 0 10, 10 10, 10 0, 0 0), (1 1, 2 2, 1 2, 1 1))')
        assertThat(
                readWkb(
                        "01030000000200000005000000000000000000000000000000000000000000000000000000000000000000244000000000000024400000000000002440000000000000244000000000000000000000000000000000000000000000000004000000000000000000F03F000000000000F03F00000000000000400000000000000040000000000000F03F0000000000000040000000000000F03F000000000000F03F"),
                matchesExact(readWkt("POLYGON((0 0, 0 10, 10 10, 10 0, 0 0), (1 1, 2 2, 1 2, 1 1))"), compXY));
    }

    @Test
    public void canReadMultiPolygon() {
        // SELECT ST_GeomFromText('MULTIPOLYGON(((1 1, 2 2, 1 2, 1 1)))')
        assertThat(
                readWkb(
                        "01060000000100000001030000000100000004000000000000000000F03F000000000000F03F00000000000000400000000000000040000000000000F03F0000000000000040000000000000F03F000000000000F03F"),
                matchesExact(readWkt("MULTIPOLYGON(((1 1, 2 2, 1 2, 1 1)))"), compXY));
    }

    @Test
    public void canReadCircularString() {
        // SELECT ST_GeomFromEWKT('CIRCULARSTRING(10 10, 15 15, 20 10)');
        assertThat(
                readWkb(
                        "010800000003000000000000000000244000000000000024400000000000002E400000000000002E4000000000000034400000000000002440"),
                matchesExact(readWkt("CIRCULARSTRING(10 10, 15 15, 20 10)"), null));
    }

    @Test
    public void canReadCompoudCurve() {
        // SELECT ST_GeomFromEWKT('COMPOUNDCURVE(CIRCULARSTRING(10 10, 15 15, 20 10))');
        assertThat(
                readWkb(
                        "010900000001000000010800000003000000000000000000244000000000000024400000000000002E400000000000002E4000000000000034400000000000002440"),
                matchesExact(readWkt("COMPOUNDCURVE(CIRCULARSTRING(10 10, 15 15, 20 10))"), null));
    }

    @Test
    public void canReadCurvePolygon() {
        // SELECT ST_GeomFromEWKT('CURVEPOLYGON((0 0, 0 10, 10 10, 10 0, 0 0))');
        assertThat(
                readWkb(
                        "010A000000010000000102000000050000000000000000000000000000000000000000000000000000000000000000002440000000000000244000000000000024400000000000002440000000000000000000000000000000000000000000000000"),
                // curve polygons are read as a standard polygon. they need to be run through a geometry converter
                // to convert to their curved form
                matchesExact(readWkt("POLYGON((0 0, 0 10, 10 10, 10 0, 0 0))"), compXY));
    }

    @Test
    public void canReadMultiCurve() {
        // SELECT ST_GeomFromEWKT('MULTICURVE((1 1, 2 2), (5 5, 6 6))')
        assertThat(
                readWkb(
                        "010B00000002000000010200000002000000000000000000F03F000000000000F03F000000000000004000000000000000400102000000020000000000000000001440000000000000144000000000000018400000000000001840"),
                matchesExact(readWkt("MULTILINESTRING((1 1, 2 2), (5 5, 6 6))"), compXY));
    }

    @Test
    public void canReadMultiSurface() {
        // SELECT ST_GeomFromEWKT('MULTISURFACE(((1 1, 2 2, 1 2, 1 1)))')
        assertThat(
                readWkb(
                        "010C0000000100000001030000000100000004000000000000000000F03F000000000000F03F00000000000000400000000000000040000000000000F03F0000000000000040000000000000F03F000000000000F03F"),
                matchesExact(readWkt("MULTIPOLYGON(((1 1, 2 2, 1 2, 1 1)))"), compXY));
    }

    /**
     * Returns a matcher that ensures a match using {@link Geometry#equalsExact(org.locationtech.jts.geom.Geometry) }.
     *
     * <p>If provided will also check the comparator matches which can be useful for ensuring z ordinates are set. Note
     * the comparator does not work well for {@link CurvedGeometry} which don't use the co-ordinate sequence in the way
     * this comparator expects.
     *
     * @param expected the expected geometry
     * @param comparator optional comparator of the co-ordinate sequence
     * @return geometry matcher
     */
    Matcher<Geometry> matchesExact(Geometry expected, CoordinateSequenceComparator comparator) {
        return new TypeSafeMatcher<>() {
            @Override
            protected boolean matchesSafely(Geometry geom) {
                return geom.equalsExact(expected) && (comparator == null || expected.compareTo(geom, comparator) == 0);
            }

            @Override
            public void describeTo(Description d) {
                d.appendText("Geometry matching: ").appendValue(expected);
            }
        };
    }

    @SuppressWarnings("unchecked")
    <T extends Geometry> T readWkb(String wkbHex) {
        try {
            return (T) wkbReader.read(WKBReader.hexToBytes(wkbHex));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    Geometry readWkt(String wkt) {
        try {
            return wktReader.read(wkt);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
