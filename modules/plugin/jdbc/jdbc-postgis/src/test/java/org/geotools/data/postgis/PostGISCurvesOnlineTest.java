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
package org.geotools.data.postgis;

import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;
import java.util.List;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.data.store.ContentFeatureStore;
import org.geotools.geometry.jts.CircularArc;
import org.geotools.geometry.jts.CircularRing;
import org.geotools.geometry.jts.CircularString;
import org.geotools.geometry.jts.CompoundCurve;
import org.geotools.geometry.jts.CompoundCurvedGeometry;
import org.geotools.geometry.jts.CurvePolygon;
import org.geotools.geometry.jts.CurvedGeometries;
import org.geotools.geometry.jts.SingleCurvedGeometry;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.geotools.util.factory.Hints;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsEqualTo;

public class PostGISCurvesOnlineTest extends JDBCTestSupport {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new PostGISCurvesTestSetup();
    }

    @Test
    public void testSingleArc() throws Exception {
        SimpleFeature feature = getSingleFeatureByName("Single arc");
        Geometry g = (Geometry) feature.getDefaultGeometry();
        assertNotNull(g);
        assertTrue(g instanceof SingleCurvedGeometry);
        SingleCurvedGeometry<?> curved = (SingleCurvedGeometry<?>) g;
        double[] cp = curved.getControlPoints();
        assertArrayEquals(new double[] {10, 15, 15, 20, 20, 15}, cp, 0d);
        assertEquals(0.1, curved.getTolerance(), 0d);
    }

    /**
     * All write tests follow the same pattern: grab the feature we want, delete everything, insert
     * back, and run its read test again
     *
     * @throws Exception
     */
    @Test
    public void testWriteSingleArc() throws Exception {
        SimpleFeature feature = getSingleFeatureByName("Single arc");
        ContentFeatureStore fs = cleanTable("curves");
        fs.addFeatures(DataUtilities.collection(feature));
        testSingleArc();
    }

    @Test
    public void testCircularString() throws Exception {
        SimpleFeature feature = getSingleFeatureByName("Arc string");
        Geometry g = (Geometry) feature.getDefaultGeometry();
        assertNotNull(g);
        assertTrue(g instanceof SingleCurvedGeometry);
        SingleCurvedGeometry<?> curved = (SingleCurvedGeometry<?>) g;
        double[] cp = curved.getControlPoints();
        assertArrayEquals(new double[] {10, 35, 15, 40, 20, 35, 25, 30, 30, 35}, cp, 0d);
    }

    @Test
    public void testWriteCircularString() throws Exception {
        SimpleFeature feature = getSingleFeatureByName("Arc string");
        ContentFeatureStore fs = cleanTable("curves");
        fs.addFeatures(DataUtilities.collection(feature));
        testCircularString();
    }

    @Test
    public void testCompoundOpen() throws Exception {
        SimpleFeature feature = getSingleFeatureByName("Compound line string");
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
        assertArrayEquals(
                new double[] {20.0, 45.0, 23.0, 48.0, 20.0, 51.0}, cs.getControlPoints(), 0d);

        LineString ls2 = components.get(2);
        assertEquals(2, ls2.getNumPoints());
        assertEquals(new Coordinate(20, 51), ls2.getCoordinateN(0));
        assertEquals(new Coordinate(10, 51), ls2.getCoordinateN(1));
    }

    @Test
    public void testWriteCompoundOpen() throws Exception {
        SimpleFeature feature = getSingleFeatureByName("Compound line string");
        ContentFeatureStore fs = cleanTable("curves");
        fs.addFeatures(DataUtilities.collection(feature));
        testCompoundOpen();
    }

    @Test
    public void testCompoundClosed() throws Exception {
        SimpleFeature feature = getSingleFeatureByName("Closed mixed line");
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
        assertArrayEquals(new double[] {20, 78, 15, 80, 10, 78}, cs.getControlPoints(), 0d);
    }

    @Test
    public void testWriteCompoundClosed() throws Exception {
        SimpleFeature feature = getSingleFeatureByName("Closed mixed line");
        ContentFeatureStore fs = cleanTable("curves");
        fs.addFeatures(DataUtilities.collection(feature));
        testCompoundClosed();
    }

    @Test
    public void testCircle() throws Exception {
        SimpleFeature feature = getSingleFeatureByName("Circle");
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
    public void testWriteCircle() throws Exception {
        SimpleFeature feature = getSingleFeatureByName("Circle");
        ContentFeatureStore fs = cleanTable("curves");
        fs.addFeatures(DataUtilities.collection(feature));
        testCircle();
    }

    @Test
    public void testCompoundPolygon() throws Exception {
        SimpleFeature feature = getSingleFeatureByName("Compound polygon");
        Geometry g = (Geometry) feature.getDefaultGeometry();
        assertNotNull(g);
        assertTrue(g instanceof CurvePolygon);
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
        assertArrayEquals(new double[] {14, 10, 10, 14, 6, 10}, cs.getControlPoints(), 0d);
    }

    @Test
    public void testWriteCompoundPolygon() throws Exception {
        SimpleFeature feature = getSingleFeatureByName("Compound polygon");
        ContentFeatureStore fs = cleanTable("curves");
        fs.addFeatures(DataUtilities.collection(feature));
        testCompoundPolygon();
    }

    @Test
    public void testCompoundPolygonWithHole() throws Exception {
        SimpleFeature feature = getSingleFeatureByName("Compound polygon with hole");
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
        assertArrayEquals(new double[] {27, 30, 25, 27, 20, 30}, cs.getControlPoints(), 0d);

        // the inner ring
        assertTrue(p.getInteriorRingN(0) instanceof CircularRing);
        CircularRing hole = (CircularRing) p.getInteriorRingN(0);
        assertTrue(CurvedGeometries.isCircle(hole));
        CircularArc arc = hole.getArcN(0);
        assertEquals(5, arc.getRadius(), 0d);
        assertEquals(new Coordinate(15, 17), arc.getCenter());
    }

    @Test
    public void testWriteCompoundPolygonWithHole() throws Exception {
        SimpleFeature feature = getSingleFeatureByName("Compound polygon with hole");
        ContentFeatureStore fs = cleanTable("curves");
        fs.addFeatures(DataUtilities.collection(feature));
        testCompoundPolygonWithHole();
    }

    @Test
    public void testMultipolygon() throws Exception {
        SimpleFeature feature = getSingleFeatureByName("Multipolygon with curves");
        Geometry g = (Geometry) feature.getDefaultGeometry();
        assertNotNull(g);
        assertTrue(g instanceof MultiPolygon);
        MultiPolygon mp = (MultiPolygon) g;
        assertEquals(2, mp.getNumGeometries());

        Polygon p1 = (Polygon) mp.getGeometryN(0);
        assertTrue(p1.getExteriorRing() instanceof CompoundCurvedGeometry<?>);
        assertEquals(2, ((CompoundCurvedGeometry<?>) p1.getExteriorRing()).getComponents().size());
        assertEquals(1, p1.getNumInteriorRing());
        assertEquals(
                2, ((CompoundCurvedGeometry<?>) p1.getInteriorRingN(0)).getComponents().size());

        Polygon p2 = (Polygon) mp.getGeometryN(1);
        assertTrue(p2.getExteriorRing() instanceof CompoundCurvedGeometry<?>);
        assertEquals(2, ((CompoundCurvedGeometry<?>) p2.getExteriorRing()).getComponents().size());
        assertEquals(0, p2.getNumInteriorRing());
    }

    @Test
    public void testWriteMultipolygon() throws Exception {
        SimpleFeature feature = getSingleFeatureByName("Multipolygon with curves");
        ContentFeatureStore fs = cleanTable("curves");
        fs.addFeatures(DataUtilities.collection(feature));
        testMultipolygon();
    }

    @Test
    public void testMulticurve() throws Exception {
        SimpleFeature feature = getSingleFeatureByName("Multicurve");
        Geometry g = (Geometry) feature.getDefaultGeometry();
        assertNotNull(g);
        assertTrue(g instanceof MultiLineString);

        MultiLineString mls = (MultiLineString) g;

        LineString ls = (LineString) mls.getGeometryN(0);
        assertEquals(2, ls.getNumPoints());
        assertEquals(new Coordinate(0, 0), ls.getCoordinateN(0));
        assertEquals(new Coordinate(5, 5), ls.getCoordinateN(1));

        CircularString cs = (CircularString) mls.getGeometryN(1);
        assertArrayEquals(new double[] {4, 0, 4, 4, 8, 4}, cs.getControlPoints(), 0d);
    }

    @Test
    public void testWriteMulticurve() throws Exception {
        SimpleFeature feature = getSingleFeatureByName("Multicurve");
        ContentFeatureStore fs = cleanTable("curves");
        fs.addFeatures(DataUtilities.collection(feature));
        testMulticurve();
    }

    @Test
    public void testSimplify() throws Exception {
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("curves"));
        Query q = new Query();
        q.getHints().put(Hints.GEOMETRY_SIMPLIFICATION, new Double(0.1));
        ContentFeatureCollection fc = fs.getFeatures(q);
        try (SimpleFeatureIterator fi = fc.features()) {
            // check they have not been simplified
        }
    }

    @Test
    public void testClosedCircularString() throws Exception {
        SimpleFeature feature = getSingleFeatureByName("circularStrings", "Circle");
        Geometry g = (Geometry) feature.getDefaultGeometry();
        assertNotNull(g);
        assertTrue(g instanceof CircularString);
    }

    @Test
    public void testWriteClosedCircularString() throws Exception {
        SimpleFeature feature = getSingleFeatureByName("circularStrings", "Circle");
        ContentFeatureStore fs = cleanTable("circularStrings");
        fs.addFeatures(DataUtilities.collection(feature));
        testCircularString();
    }

    @Test
    public void testClosedCompoundCurve() throws Exception {
        SimpleFeature feature = getSingleFeatureByName("compoundCurves", "ClosedHalfCircle");
        Geometry g = (Geometry) feature.getDefaultGeometry();
        assertNotNull(g);
        assertTrue(g instanceof CompoundCurve);
    }

    @Test
    public void testWriteClosedCompoundCurve() throws Exception {
        SimpleFeature feature = getSingleFeatureByName("compoundCurves", "ClosedHalfCircle");
        ContentFeatureStore fs = cleanTable("compoundCurves");
        fs.addFeatures(DataUtilities.collection(feature));
        testClosedCompoundCurve();
    }

    @Test
    public void testSquareHole2Points() throws Exception {
        SimpleFeature feature = getSingleFeatureByName("SquareHole2Points");
        Geometry g = (Geometry) feature.getDefaultGeometry();
        assertNotNull(g);
        assertTrue(g instanceof Polygon);

        Polygon p = (Polygon) g;

        LineString ls = p.getExteriorRing();
        assertEquals(5, ls.getNumPoints());
        assertEquals(new Coordinate(-10, -10), ls.getCoordinateN(0));
        assertEquals(new Coordinate(-10, -8), ls.getCoordinateN(1));
        assertEquals(new Coordinate(-8, -8), ls.getCoordinateN(2));
        assertEquals(new Coordinate(-8, -10), ls.getCoordinateN(3));
        assertEquals(new Coordinate(-10, -10), ls.getCoordinateN(4));

        // check the interior ring has been normalized
        assertEquals(1, p.getNumInteriorRing());
        CircularRing hole = (CircularRing) p.getInteriorRingN(0);
        assertArrayEquals(
                new double[] {-9.0, -8.5, -9.0, -9.5, -9.0, -8.5}, hole.getControlPoints(), 1e-6);
    }

    private SimpleFeature getSingleFeatureByName(String name) throws IOException {
        return getSingleFeatureByName("curves", name);
    }

    private SimpleFeature getSingleFeatureByName(String tableName, String name) throws IOException {
        ContentFeatureSource fs = dataStore.getFeatureSource(tname(tableName));
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter = ff.equal(ff.property(aname("name")), ff.literal(name), true);
        Query q = new Query(tname("curves"), filter);
        q.getHints().put(Hints.LINEARIZATION_TOLERANCE, 0.1);
        ContentFeatureCollection fc = fs.getFeatures(q);
        assertEquals(1, fc.size());
        SimpleFeature feature = DataUtilities.first(fc);
        return feature;
    }

    private ContentFeatureStore cleanTable(String tableName) throws IOException {
        ContentFeatureStore store =
                (ContentFeatureStore) dataStore.getFeatureSource(tname(tableName));
        store.removeFeatures(Filter.INCLUDE);
        return store;
    }
}
