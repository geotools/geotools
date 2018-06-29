/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml3;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;

import java.util.List;
import org.geotools.geometry.jts.CircularArc;
import org.geotools.geometry.jts.CircularRing;
import org.geotools.geometry.jts.CircularString;
import org.geotools.geometry.jts.CompoundCurvedGeometry;
import org.geotools.geometry.jts.CurvePolygon;
import org.geotools.geometry.jts.CurvedGeometries;
import org.geotools.geometry.jts.CurvedGeometryFactory;
import org.geotools.geometry.jts.MultiCurve;
import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;

public class GML3CurveParsingTest extends GML3TestSupport {

    protected static final double TOLERANCE = 1e-9;

    protected Configuration gml;

    protected void setUp() throws Exception {
        GMLConfiguration configuration = new GMLConfiguration(true);
        configuration.setGeometryFactory(new CurvedGeometryFactory(TOLERANCE));
        this.gml = configuration;
    }

    public void testSingleArc() throws Exception {
        Parser p = new Parser(gml);
        Object arc = p.parse(GML3CurveParsingTest.class.getResourceAsStream("v3_2/singleArc.xml"));
        assertThat(arc, instanceOf(CircularString.class));
        CircularString cs = (CircularString) arc;
        assertArrayEquals(new double[] {10, 15, 15, 20, 20, 15}, cs.getControlPoints(), 0d);
        assertEquals(TOLERANCE, cs.getTolerance());
    }

    public void testArcString() throws Exception {
        Parser p = new Parser(gml);
        Object arc = p.parse(GML3CurveParsingTest.class.getResourceAsStream("v3_2/arcString.xml"));
        assertThat(arc, instanceOf(CircularString.class));
        CircularString cs = (CircularString) arc;
        assertArrayEquals(
                new double[] {10, 35, 15, 40, 20, 35, 25, 30, 30, 35}, cs.getControlPoints(), 0d);
        assertEquals(TOLERANCE, cs.getTolerance());
    }

    public void testCompoundOpen() throws Exception {
        Parser p = new Parser(gml);
        Object g = p.parse(GML3CurveParsingTest.class.getResourceAsStream("v3_2/compoundOpen.xml"));
        assertThat(g, instanceOf(CompoundCurvedGeometry.class));

        CompoundCurvedGeometry<?> compound = (CompoundCurvedGeometry<?>) g;
        assertEquals(TOLERANCE, compound.getTolerance());
        List<LineString> components = compound.getComponents();
        assertEquals(3, components.size());

        LineString ls1 = components.get(0);
        assertEquals(2, ls1.getNumPoints());
        assertEquals(new Coordinate(10, 45), ls1.getCoordinateN(0));
        assertEquals(new Coordinate(20, 45), ls1.getCoordinateN(1));

        CircularString cs = (CircularString) components.get(1);
        assertArrayEquals(
                new double[] {20.0, 45.0, 23.0, 48.0, 20.0, 51.0}, cs.getControlPoints(), 0d);

        LineString ls2 = components.get(2);
        assertEquals(2, ls2.getNumPoints());
        assertEquals(new Coordinate(20, 51), ls2.getCoordinateN(0));
        assertEquals(new Coordinate(10, 51), ls2.getCoordinateN(1));
    }

    public void testCompoundClosed() throws Exception {
        Parser p = new Parser(gml);
        Object g =
                p.parse(GML3CurveParsingTest.class.getResourceAsStream("v3_2/compoundClosed.xml"));
        assertThat(g, instanceOf(CompoundCurvedGeometry.class));

        CompoundCurvedGeometry<?> compound = (CompoundCurvedGeometry<?>) g;
        assertEquals(TOLERANCE, compound.getTolerance());
        List<LineString> components = compound.getComponents();
        assertEquals(2, components.size());

        LineString ls = components.get(0);
        assertEquals(4, ls.getNumPoints());
        assertEquals(new Coordinate(10, 78), ls.getCoordinateN(0));
        assertEquals(new Coordinate(10, 75), ls.getCoordinateN(1));
        assertEquals(new Coordinate(20, 75), ls.getCoordinateN(2));
        assertEquals(new Coordinate(20, 78), ls.getCoordinateN(3));

        CircularString cs = (CircularString) components.get(1);
        assertArrayEquals(new double[] {20, 78, 15, 80, 10, 78}, cs.getControlPoints(), 0d);
    }

    public void testCirclePolygon() throws Exception {
        Parser p = new Parser(gml);
        Object g =
                p.parse(GML3CurveParsingTest.class.getResourceAsStream("v3_2/circlePolygon.xml"));
        assertThat(g, instanceOf(CurvePolygon.class));

        CurvePolygon cp = (CurvePolygon) g;
        assertEquals(TOLERANCE, cp.getTolerance());
        assertEquals(0, cp.getNumInteriorRing());

        // exterior ring checks
        assertTrue(cp.getExteriorRing() instanceof CircularRing);
        CircularRing shell = (CircularRing) cp.getExteriorRing();
        assertTrue(CurvedGeometries.isCircle(shell));
        CircularArc arc = shell.getArcN(0);
        assertEquals(5, arc.getRadius(), 0d);
        assertEquals(new Coordinate(15, 150), arc.getCenter());
    }

    public void testCompoundPolygon() throws Exception {
        Parser p = new Parser(gml);
        Object g =
                p.parse(GML3CurveParsingTest.class.getResourceAsStream("v3_2/compoundPolygon.xml"));
        assertThat(g, instanceOf(CurvePolygon.class));

        CurvePolygon cp = (CurvePolygon) g;
        assertEquals(TOLERANCE, cp.getTolerance());
        assertEquals(0, cp.getNumInteriorRing());
        assertTrue(cp.getExteriorRing() instanceof CompoundCurvedGeometry<?>);
        CompoundCurvedGeometry<?> compound = (CompoundCurvedGeometry<?>) cp.getExteriorRing();
        List<LineString> components = compound.getComponents();
        assertEquals(2, components.size());

        LineString ls = components.get(0);
        assertEquals(3, ls.getNumPoints());
        assertEquals(new Coordinate(6, 10), ls.getCoordinateN(0));
        assertEquals(new Coordinate(10, 1), ls.getCoordinateN(1));
        assertEquals(new Coordinate(14, 10), ls.getCoordinateN(2));

        CircularString cs = (CircularString) components.get(1);
        assertArrayEquals(new double[] {14, 10, 10, 14, 6, 10}, cs.getControlPoints(), 0d);
    }

    public void testCompoundPolygonWithHole() throws Exception {
        Parser p = new Parser(gml);
        Object g =
                p.parse(
                        GML3CurveParsingTest.class.getResourceAsStream(
                                "v3_2/compoundPolygonWithHole.xml"));
        assertThat(g, instanceOf(CurvePolygon.class));

        CurvePolygon cp = (CurvePolygon) g;
        assertEquals(TOLERANCE, cp.getTolerance());
        assertEquals(1, cp.getNumInteriorRing());

        // exterior ring checks
        assertTrue(cp.getExteriorRing() instanceof CompoundCurvedGeometry<?>);
        CompoundCurvedGeometry<?> shell = (CompoundCurvedGeometry<?>) cp.getExteriorRing();
        List<LineString> components = shell.getComponents();
        assertEquals(2, components.size());

        LineString ls = components.get(0);
        assertEquals(7, ls.getNumPoints());
        // 20,30, 11,30, 7,22, 7,15, 11,10, 21,10, 27,30
        assertEquals(new Coordinate(20, 30), ls.getCoordinateN(0));
        assertEquals(new Coordinate(11, 30), ls.getCoordinateN(1));
        assertEquals(new Coordinate(7, 22), ls.getCoordinateN(2));
        assertEquals(new Coordinate(7, 15), ls.getCoordinateN(3));
        assertEquals(new Coordinate(11, 10), ls.getCoordinateN(4));
        assertEquals(new Coordinate(21, 10), ls.getCoordinateN(5));
        assertEquals(new Coordinate(27, 30), ls.getCoordinateN(6));

        CircularString cs = (CircularString) components.get(1);
        assertArrayEquals(new double[] {27, 30, 25, 27, 20, 30}, cs.getControlPoints(), 0d);

        // the inner ring
        assertTrue(cp.getInteriorRingN(0) instanceof CircularRing);
        CircularRing hole = (CircularRing) cp.getInteriorRingN(0);
        assertTrue(CurvedGeometries.isCircle(hole));
        CircularArc arc = hole.getArcN(0);
        assertEquals(5, arc.getRadius(), 0d);
        assertEquals(new Coordinate(15, 17), arc.getCenter());
    }

    public void testMultiSurface() throws Exception {
        Parser p = new Parser(gml);
        Object g =
                p.parse(GML3CurveParsingTest.class.getResourceAsStream("v3_2/multiSurface2.xml"));
        assertThat(g, instanceOf(org.geotools.geometry.jts.MultiSurface.class));

        org.geotools.geometry.jts.MultiSurface mp = (org.geotools.geometry.jts.MultiSurface) g;
        assertEquals(TOLERANCE, mp.getTolerance());
        assertEquals(2, mp.getNumGeometries());

        CurvePolygon p1 = (CurvePolygon) mp.getGeometryN(0);
        assertTrue(p1.getExteriorRing() instanceof CompoundCurvedGeometry<?>);
        assertEquals(2, ((CompoundCurvedGeometry<?>) p1.getExteriorRing()).getComponents().size());
        assertEquals(1, p1.getNumInteriorRing());
        assertEquals(
                2, ((CompoundCurvedGeometry<?>) p1.getInteriorRingN(0)).getComponents().size());

        CurvePolygon p2 = (CurvePolygon) mp.getGeometryN(1);
        assertTrue(p2.getExteriorRing() instanceof CompoundCurvedGeometry<?>);
        assertEquals(2, ((CompoundCurvedGeometry<?>) p2.getExteriorRing()).getComponents().size());
        assertEquals(0, p2.getNumInteriorRing());
    }

    public void testMultiCurve() throws Exception {
        Parser p = new Parser(gml);
        Object g = p.parse(GML3CurveParsingTest.class.getResourceAsStream("v3_2/multiCurve.xml"));
        assertThat(g, instanceOf(MultiCurve.class));

        MultiCurve mc = (MultiCurve) g;
        assertEquals(TOLERANCE, mc.getTolerance());

        LineString ls = (LineString) mc.getGeometryN(0);
        assertEquals(2, ls.getNumPoints());
        assertEquals(new Coordinate(0, 0), ls.getCoordinateN(0));
        assertEquals(new Coordinate(5, 5), ls.getCoordinateN(1));

        CircularString cs = (CircularString) mc.getGeometryN(1);
        assertArrayEquals(new double[] {4, 0, 4, 4, 8, 4}, cs.getControlPoints(), 0d);
    }
}
