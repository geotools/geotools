/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.oracle;

import static org.junit.Assert.assertArrayEquals;

import java.util.List;

import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.Hints;
import org.geotools.geometry.jts.CircularArc;
import org.geotools.geometry.jts.CircularRing;
import org.geotools.geometry.jts.CircularString;
import org.geotools.geometry.jts.CompoundCurvedGeometry;
import org.geotools.geometry.jts.CurvedGeometries;
import org.geotools.geometry.jts.SingleCurvedGeometry;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsEqualTo;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

public class OracleCurvesOnlineTest extends JDBCTestSupport {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new OracleCurvesTestSetup();
    }

    @Test
    public void testSingleArc() throws Exception {
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("curves"));
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter = ff.equal(ff.property(aname("name")), ff.literal("Arc segment"),
                true);
        Query q = new Query(tname("curves"), filter);
        q.getHints().put(Hints.LINEARIZATION_TOLERANCE, 0.1);
        ContentFeatureCollection fc = fs.getFeatures(q);
        assertEquals(1, fc.size());
        SimpleFeature feature = DataUtilities.first(fc);
        Geometry g = (Geometry) feature.getDefaultGeometry();
        assertNotNull(g);
        assertTrue(g instanceof SingleCurvedGeometry);
        SingleCurvedGeometry<?> curved = (SingleCurvedGeometry<?>) g;
        double[] cp = curved.getControlPoints();
        assertArrayEquals(new double[] { 10, 15, 15, 20, 20, 15 }, cp, 0d);
        assertEquals(0.1, curved.getTolerance(), 0d);
    }

    @Test
    public void testCircularString() throws Exception {
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("curves"));
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter = ff.equal(ff.property(aname("name")), ff.literal("Arc string"),
                true);
        ContentFeatureCollection fc = fs.getFeatures(filter);
        assertEquals(1, fc.size());
        SimpleFeature feature = DataUtilities.first(fc);
        Geometry g = (Geometry) feature.getDefaultGeometry();
        assertNotNull(g);
        assertTrue(g instanceof SingleCurvedGeometry);
        SingleCurvedGeometry<?> curved = (SingleCurvedGeometry<?>) g;
        double[] cp = curved.getControlPoints();
        assertArrayEquals(new double[] { 10, 35, 15, 40, 20, 35, 25, 30, 30, 35 }, cp, 0d);
    }

    @Test
    public void testCompoundOpen() throws Exception {
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("curves"));
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter = ff.equal(ff.property(aname("name")),
                ff.literal("Compound line string"), true);
        ContentFeatureCollection fc = fs.getFeatures(filter);
        assertEquals(1, fc.size());
        SimpleFeature feature = DataUtilities.first(fc);
        Geometry g = (Geometry) feature.getDefaultGeometry();
        assertNotNull(g);
        assertTrue(g instanceof CompoundCurvedGeometry<?>);
        CompoundCurvedGeometry<?> compound = (CompoundCurvedGeometry<?>) g;
        List<LineString> components = compound.getComponents();
        assertEquals(3, components.size());

        LineString ls1 = components.get(0);
        assertEquals(2, ls1.getNumPoints());
        assertEquals(new Coordinate(10, 45), ls1.getCoordinateN(0));
        assertEquals(new Coordinate(20, 45), ls1.getCoordinateN(1));

        CircularString cs = (CircularString) components.get(1);
        assertArrayEquals(new double[] { 20.0, 45.0, 23.0, 48.0, 20.0, 51.0 },
                cs.getControlPoints(), 0d);

        LineString ls2 = components.get(2);
        assertEquals(2, ls2.getNumPoints());
        assertEquals(new Coordinate(20, 51), ls2.getCoordinateN(0));
        assertEquals(new Coordinate(10, 51), ls2.getCoordinateN(1));
    }

    @Test
    public void testCompoundClosed() throws Exception {
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("curves"));
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter = ff.equal(ff.property(aname("name")),
                ff.literal("Closed mixed line"), true);
        ContentFeatureCollection fc = fs.getFeatures(filter);
        assertEquals(1, fc.size());
        SimpleFeature feature = DataUtilities.first(fc);
        Geometry g = (Geometry) feature.getDefaultGeometry();
        assertNotNull(g);
        assertTrue(g instanceof CompoundCurvedGeometry<?>);
        CompoundCurvedGeometry<?> compound = (CompoundCurvedGeometry<?>) g;
        List<LineString> components = compound.getComponents();
        assertEquals(2, components.size());

        LineString ls = components.get(0);
        assertEquals(4, ls.getNumPoints());
        assertEquals(new Coordinate(10, 78), ls.getCoordinateN(0));
        assertEquals(new Coordinate(10, 75), ls.getCoordinateN(1));
        assertEquals(new Coordinate(20, 75), ls.getCoordinateN(2));
        assertEquals(new Coordinate(20, 78), ls.getCoordinateN(3));

        CircularString cs = (CircularString) components.get(1);
        assertArrayEquals(new double[] { 20, 78, 15, 80, 10, 78 }, cs.getControlPoints(), 0d);
    }

    @Test
    public void testCircle() throws Exception {
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("curves"));
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter = ff.equal(ff.property(aname("name")), ff.literal("Circle"), true);
        ContentFeatureCollection fc = fs.getFeatures(filter);
        assertEquals(1, fc.size());
        SimpleFeature feature = DataUtilities.first(fc);
        Geometry g = (Geometry) feature.getDefaultGeometry();
        assertNotNull(g);
        assertTrue(g instanceof Polygon);
        Polygon p = (Polygon) g;
        assertEquals(0, p.getNumInteriorRing());

        // exterior ring checks
        assertTrue(p.getExteriorRing() instanceof CircularRing);
        CircularRing shell = (CircularRing) p.getExteriorRing();
        assertTrue(CurvedGeometries.isCircle(shell));
        CircularArc arc = shell.getArcN(0);
        assertEquals(5, arc.getRadius(), 0d);
        assertEquals(new Coordinate(15, 150), arc.getCenter());
    }

    @Test
    public void testCompoundPolygon() throws Exception {
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("curves"));
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter = ff.equal(ff.property(aname("name")),
                ff.literal("Compound polygon"), true);
        ContentFeatureCollection fc = fs.getFeatures(filter);
        assertEquals(1, fc.size());
        SimpleFeature feature = DataUtilities.first(fc);
        Geometry g = (Geometry) feature.getDefaultGeometry();
        assertNotNull(g);
        assertTrue(g instanceof Polygon);
        Polygon p = (Polygon) g;
        assertEquals(0, p.getNumInteriorRing());
        assertTrue(p.getExteriorRing() instanceof CompoundCurvedGeometry<?>);
        CompoundCurvedGeometry<?> compound = (CompoundCurvedGeometry<?>) p.getExteriorRing();
        List<LineString> components = compound.getComponents();
        assertEquals(2, components.size());

        LineString ls = components.get(0);
        assertEquals(3, ls.getNumPoints());
        assertEquals(new Coordinate(6, 10), ls.getCoordinateN(0));
        assertEquals(new Coordinate(10, 1), ls.getCoordinateN(1));
        assertEquals(new Coordinate(14, 10), ls.getCoordinateN(2));

        CircularString cs = (CircularString) components.get(1);
        assertArrayEquals(new double[] { 14, 10, 10, 14, 6, 10 }, cs.getControlPoints(), 0d);
    }

    @Test
    public void testCompoundPolygon2() throws Exception {
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("curves"));
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter = ff.equal(ff.property(aname("name")),
                ff.literal("Compound polygon 2"), true);
        ContentFeatureCollection fc = fs.getFeatures(filter);
        assertEquals(1, fc.size());
        SimpleFeature feature = DataUtilities.first(fc);
        Geometry g = (Geometry) feature.getDefaultGeometry();
        assertNotNull(g);
        assertTrue(g instanceof Polygon);
        Polygon p = (Polygon) g;
        assertEquals(0, p.getNumInteriorRing());
        assertTrue(p.getExteriorRing() instanceof CircularRing);
        CircularRing shell = (CircularRing) p.getExteriorRing();

        assertArrayEquals(new double[] { 15, 145, 20, 150, 15, 155, 10, 150, 15, 145 },
                shell.getControlPoints(), 0d);
    }

    @Test
    public void testCompoundPolygonWithHole() throws Exception {
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("curves"));
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter = ff.equal(ff.property(aname("name")),
                ff.literal("Compound polygon with hole"), true);
        ContentFeatureCollection fc = fs.getFeatures(filter);
        assertEquals(1, fc.size());
        SimpleFeature feature = DataUtilities.first(fc);
        Geometry g = (Geometry) feature.getDefaultGeometry();
        assertNotNull(g);
        assertTrue(g instanceof Polygon);
        Polygon p = (Polygon) g;
        assertEquals(1, p.getNumInteriorRing());

        // exterior ring checks
        assertTrue(p.getExteriorRing() instanceof CompoundCurvedGeometry<?>);
        CompoundCurvedGeometry<?> shell = (CompoundCurvedGeometry<?>) p.getExteriorRing();
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
        assertArrayEquals(new double[] { 27, 30, 25, 27, 20, 30 }, cs.getControlPoints(), 0d);

        // the inner ring
        assertTrue(p.getInteriorRingN(0) instanceof CircularRing);
        CircularRing hole = (CircularRing) p.getInteriorRingN(0);
        assertTrue(CurvedGeometries.isCircle(hole));
        CircularArc arc = hole.getArcN(0);
        assertEquals(5, arc.getRadius(), 0d);
        assertEquals(new Coordinate(15, 17), arc.getCenter());
    }

    @Test
    public void testMultipolygon() throws Exception {
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("curves"));
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter = ff.equal(ff.property(aname("name")),
                ff.literal("Multipolygon with curves"), true);
        ContentFeatureCollection fc = fs.getFeatures(filter);
        assertEquals(1, fc.size());
        SimpleFeature feature = DataUtilities.first(fc);
        Geometry g = (Geometry) feature.getDefaultGeometry();
        assertNotNull(g);
        assertTrue(g instanceof MultiPolygon);
        MultiPolygon mp = (MultiPolygon) g;
        assertEquals(2, mp.getNumGeometries());

        Polygon p1 = (Polygon) mp.getGeometryN(0);
        assertTrue(p1.getExteriorRing() instanceof CompoundCurvedGeometry<?>);
        assertEquals(2, ((CompoundCurvedGeometry<?>) p1.getExteriorRing()).getComponents().size());
        assertEquals(1, p1.getNumInteriorRing());
        assertEquals(2, ((CompoundCurvedGeometry<?>) p1.getInteriorRingN(0)).getComponents().size());

        Polygon p2 = (Polygon) mp.getGeometryN(1);
        assertTrue(p2.getExteriorRing() instanceof CompoundCurvedGeometry<?>);
        assertEquals(2, ((CompoundCurvedGeometry<?>) p2.getExteriorRing()).getComponents().size());
        assertEquals(0, p2.getNumInteriorRing());
    }

}
