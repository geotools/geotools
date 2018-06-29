package org.geotools.process.vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.NameImpl;
import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.Name;

public class ClipProcessTest extends Assert {

    private SimpleFeatureSource fsMeters;
    private SimpleFeatureSource fsDegrees;
    private SimpleFeatureSource fsLines;
    private SimpleFeatureSource fsCollinear;
    private SimpleFeatureSource fsMultilines;

    @Before
    public void setUp() throws Exception {
        PropertyDataStore store = new PropertyDataStore(TestData.file(this, ""));
        fsMeters = store.getFeatureSource("squaresMeters");
        fsDegrees = store.getFeatureSource("squaresDegrees");
        fsLines = store.getFeatureSource("lines");
        fsMultilines = store.getFeatureSource("multilines");
        fsCollinear = store.getFeatureSource("collinear");
    }

    @Test
    public void testClipPoly3DIncluded() throws Exception {
        SimpleFeatureCollection features = fsMeters.getFeatures();
        ClipProcess cp = new ClipProcess();
        SimpleFeatureCollection result =
                cp.execute(
                        features,
                        new WKTReader()
                                .read(
                                        "POLYGON((-10 -10, -10 10010, 10010 10010, 10010 -10, -10 -10))"),
                        true);
        assertEquals(2, result.size());
        assertSquaresMetersIdentical(result);
    }

    @Test
    public void testClipPoly3DOnBorder() throws Exception {
        SimpleFeatureCollection features = fsMeters.getFeatures();
        ClipProcess cp = new ClipProcess();
        SimpleFeatureCollection result =
                cp.execute(
                        features,
                        new WKTReader().read("POLYGON((0 0, 0 10000, 10000 10000, 10000 0, 0 0))"),
                        true);
        assertEquals(2, result.size());
        assertSquaresMetersIdentical(result);
    }

    @Test
    public void testClipPoly3DNewVertices() throws Exception {
        SimpleFeatureCollection features = fsMeters.getFeatures();
        ClipProcess cp = new ClipProcess();
        SimpleFeatureCollection result =
                cp.execute(
                        features,
                        new WKTReader()
                                .read(
                                        "POLYGON((0 5000, 0 10000, 10000 10000, 10000 5000, 0 5000))"),
                        true);
        assertEquals(1, result.size());
        SimpleFeatureIterator fi = result.features();
        // check the first polygon
        SimpleFeature f = fi.next();
        MultiPolygon mp = (MultiPolygon) f.getDefaultGeometry();
        assertEquals(1, mp.getNumGeometries());
        Polygon p = (Polygon) mp.getGeometryN(0);
        assertEquals(0, p.getNumInteriorRing());
        LineString shell = p.getExteriorRing();
        CoordinateSequence cs = shell.getCoordinateSequence();
        assertEquals(5, cs.size());
        assertOrdinates(0, 5000, 1, cs, 0);
        assertOrdinates(0, 10000, 2, cs, 1);
        assertOrdinates(10000, 10000, 2, cs, 2);
        assertOrdinates(10000, 5000, 1, cs, 3);
        assertOrdinates(0, 5000, 1, cs, 4);
        // ensure no second
        assertFalse(fi.hasNext());
        fi.close();
    }

    @Test
    public void testClipPoly3DFullyInside() throws Exception {
        SimpleFeatureCollection features = fsMeters.getFeatures();
        ClipProcess cp = new ClipProcess();
        SimpleFeatureCollection result =
                cp.execute(
                        features,
                        new WKTReader()
                                .read(
                                        "POLYGON((2500 2500, 2500 7500, 7500 7500, 7500 2500, 2500 2500))"),
                        true);
        assertEquals(2, result.size());
        SimpleFeatureIterator fi = result.features();
        // check the first polygon
        SimpleFeature f = fi.next();
        MultiPolygon mp = (MultiPolygon) f.getDefaultGeometry();
        assertEquals(1, mp.getNumGeometries());
        Polygon p = (Polygon) mp.getGeometryN(0);
        assertEquals(0, p.getNumInteriorRing());
        LineString shell = p.getExteriorRing();
        CoordinateSequence cs = shell.getCoordinateSequence();
        assertEquals(5, cs.size());
        assertOrdinates(2500, 2500, 0.411765, cs, 0);
        assertOrdinates(2500, 7500, 1.588235, cs, 1);
        assertOrdinates(7500, 7500, 1.588235, cs, 2);
        assertOrdinates(7500, 2500, 0.411765, cs, 3);
        assertOrdinates(2500, 2500, 0.411765, cs, 4);
        fi.close();
    }

    private void assertSquaresMetersIdentical(SimpleFeatureCollection result) {
        SimpleFeatureIterator fi = result.features();
        // check the first polygon
        SimpleFeature f = fi.next();
        MultiPolygon mp = (MultiPolygon) f.getDefaultGeometry();
        assertEquals(1, mp.getNumGeometries());
        Polygon p = (Polygon) mp.getGeometryN(0);
        assertEquals(0, p.getNumInteriorRing());
        LineString shell = p.getExteriorRing();
        CoordinateSequence cs = shell.getCoordinateSequence();
        assertEquals(5, cs.size());
        assertOrdinates(0, 0, 0, cs, 0);
        assertOrdinates(0, 10000, 2, cs, 1);
        assertOrdinates(10000, 10000, 2, cs, 2);
        assertOrdinates(10000, 0, 0, cs, 3);
        assertOrdinates(0, 0, 0, cs, 4);
        // check the second
        f = fi.next();
        mp = (MultiPolygon) f.getDefaultGeometry();
        assertEquals(1, mp.getNumGeometries());
        p = (Polygon) mp.getGeometryN(0);
        assertEquals(0, p.getNumInteriorRing());
        shell = p.getExteriorRing();
        cs = shell.getCoordinateSequence();
        assertEquals(5, cs.size());
        assertOrdinates(0, 0, Double.NaN, cs, 0);
        assertOrdinates(0, 5000, Double.NaN, cs, 1);
        assertOrdinates(5000, 5000, Double.NaN, cs, 2);
        assertOrdinates(5000, 0, Double.NaN, cs, 3);
        assertOrdinates(0, 0, Double.NaN, cs, 4);
    }

    @Test
    public void testClipLine3DIncluded() throws Exception {
        SimpleFeatureCollection features = fsLines.getFeatures();
        ClipProcess cp = new ClipProcess();
        SimpleFeatureCollection result =
                cp.execute(
                        features,
                        new WKTReader()
                                .read(
                                        "POLYGON((-10 -10, -10 10010, 10010 10010, 10010 -10, -10 -10))"),
                        true);
        assertEquals(1, result.size());
        SimpleFeatureIterator fi = result.features();
        // check the first polygon
        SimpleFeature f = fi.next();
        MultiLineString ml = (MultiLineString) f.getDefaultGeometry();
        assertEquals(1, ml.getNumGeometries());
        LineString ls = (LineString) ml.getGeometryN(0);
        CoordinateSequence cs = ls.getCoordinateSequence();
        assertEquals(3, cs.size());
        assertOrdinates(0, 0, 0, cs, 0);
        assertOrdinates(10000, 0, 1, cs, 1);
        assertOrdinates(10000, 10000, 2, cs, 2);
        fi.close();
    }

    @Test
    public void testClipLine3DMidFirstSegment() throws Exception {
        SimpleFeatureCollection features = fsLines.getFeatures();
        ClipProcess cp = new ClipProcess();
        SimpleFeatureCollection result =
                cp.execute(
                        features,
                        new WKTReader()
                                .read("POLYGON((-10 -10, -10 10, 5000 10, 5000 -10, -10 -10))"),
                        true);
        assertEquals(1, result.size());
        SimpleFeatureIterator fi = result.features();
        // check the first polygon
        SimpleFeature f = fi.next();
        MultiLineString ml = (MultiLineString) f.getDefaultGeometry();
        assertEquals(1, ml.getNumGeometries());
        LineString ls = (LineString) ml.getGeometryN(0);
        CoordinateSequence cs = ls.getCoordinateSequence();
        assertEquals(2, cs.size());
        assertOrdinates(0, 0, 0, cs, 0);
        assertOrdinates(5000, 0, 0.5, cs, 1);
        fi.close();
    }

    @Test
    public void testService() throws Exception {
        VectorProcessFactory factory = new VectorProcessFactory();
        Set<Name> names = factory.getNames();
        assertFalse(names.isEmpty());
        assertTrue(names.contains(new NameImpl("vec", "Clip")));

        SimpleFeatureCollection features = fsLines.getFeatures();

        Map<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("features", features);
        arguments.put(
                "clip",
                new WKTReader().read("POLYGON((-10 -10, -10 10, 5000 10, 5000 -10, -10 -10))"));

        Map<String, Object> output =
                factory.create(new NameImpl("vec", "Clip")).execute(arguments, null);
        SimpleFeatureCollection result = (SimpleFeatureCollection) output.get("result");
        assertEquals(1, result.size());
    }

    @Test
    public void testClipCollinearLine3DMidFirstSegment() throws Exception {
        SimpleFeatureCollection features = fsCollinear.getFeatures();
        ClipProcess cp = new ClipProcess();
        SimpleFeatureCollection result =
                cp.execute(
                        features,
                        new WKTReader()
                                .read("POLYGON((-10 -10, -10 10, 5000 10, 5000 -10, -10 -10))"),
                        true);
        assertEquals(1, result.size());
        SimpleFeatureIterator fi = result.features();
        // check the first polygon
        SimpleFeature f = fi.next();
        MultiLineString ml = (MultiLineString) f.getDefaultGeometry();
        assertEquals(1, ml.getNumGeometries());
        LineString ls = (LineString) ml.getGeometryN(0);
        CoordinateSequence cs = ls.getCoordinateSequence();
        assertEquals(4, cs.size());
        assertOrdinates(0, 0, 0, cs, 0);
        assertOrdinates(1000, 0, 0.1, cs, 1);
        assertOrdinates(4000, 0, 0.4, cs, 2);
        assertOrdinates(5000, 0, 0.5, cs, 3);
        fi.close();
    }

    @Test
    public void testClipLine3DMidSecondSegment() throws Exception {
        SimpleFeatureCollection features = fsLines.getFeatures();
        ClipProcess cp = new ClipProcess();
        SimpleFeatureCollection result =
                cp.execute(
                        features,
                        new WKTReader()
                                .read(
                                        "POLYGON((9000 5000, 9000 10000, 11000 10000, 11000 5000, 9000 5000))"),
                        true);
        assertEquals(1, result.size());
        SimpleFeatureIterator fi = result.features();
        // check the first polygon
        SimpleFeature f = fi.next();
        MultiLineString ml = (MultiLineString) f.getDefaultGeometry();
        assertEquals(1, ml.getNumGeometries());
        LineString ls = (LineString) ml.getGeometryN(0);
        CoordinateSequence cs = ls.getCoordinateSequence();
        assertEquals(2, cs.size());
        assertOrdinates(10000, 5000, 1.5, cs, 0);
        assertOrdinates(10000, 10000, 2, cs, 1);
        fi.close();
    }

    @Test
    public void testClipExtractBend() throws Exception {
        SimpleFeatureCollection features = fsLines.getFeatures();
        ClipProcess cp = new ClipProcess();
        SimpleFeatureCollection result =
                cp.execute(
                        features,
                        new WKTReader()
                                .read(
                                        "POLYGON((5000 -10, 5000 5000, 11000 5000, 11000 -10, 5000 -10))"),
                        true);
        assertEquals(1, result.size());
        SimpleFeatureIterator fi = result.features();
        // check the first polygon
        SimpleFeature f = fi.next();
        MultiLineString ml = (MultiLineString) f.getDefaultGeometry();
        assertEquals(1, ml.getNumGeometries());
        LineString ls = (LineString) ml.getGeometryN(0);
        CoordinateSequence cs = ls.getCoordinateSequence();
        assertEquals(3, cs.size());
        assertOrdinates(5000, 0, 0.5, cs, 0);
        assertOrdinates(10000, 0, 1, cs, 1);
        assertOrdinates(10000, 5000, 1.5, cs, 2);
        fi.close();
    }

    @Test
    public void testClipExtractSeparateBitsLowLine() throws Exception {
        SimpleFeatureCollection features = fsLines.getFeatures();
        ClipProcess cp = new ClipProcess();
        // clip with a rotated "C"
        SimpleFeatureCollection result =
                cp.execute(
                        features,
                        new WKTReader()
                                .read(
                                        "POLYGON((1000 -10, 1000 10, 9000 10, 9000 -10, 8000 -10, 8000 5, 2000 5, 2000 -10, 1000 -10))"),
                        true);
        assertEquals(1, result.size());
        SimpleFeatureIterator fi = result.features();
        // check the first polygon
        SimpleFeature f = fi.next();
        fi.close();
        MultiLineString ml = (MultiLineString) f.getDefaultGeometry();
        assertEquals(2, ml.getNumGeometries());
        LineString ls = (LineString) ml.getGeometryN(0);
        CoordinateSequence cs = ls.getCoordinateSequence();
        assertEquals(2, cs.size());
        assertOrdinates(1000, 0, 0.1, cs, 0);
        assertOrdinates(2000, 0, 0.2, cs, 1);
        ls = (LineString) ml.getGeometryN(1);
        cs = ls.getCoordinateSequence();
        assertEquals(2, cs.size());
        assertOrdinates(8000, 0, 0.8, cs, 0);
        assertOrdinates(9000, 0, 0.9, cs, 1);
    }

    @Test
    public void testClipExtractSeparateBitsBothLines() throws Exception {
        SimpleFeatureCollection features = fsLines.getFeatures();
        ClipProcess cp = new ClipProcess();
        // clip with a rotated "L"
        SimpleFeatureCollection result =
                cp.execute(
                        features,
                        new WKTReader()
                                .read(
                                        "POLYGON((1000 -10, 1000 5000, 11000 5000, 11000 4000, 2000 4000, 2000 -10, 1000 -10))"),
                        true);
        assertEquals(1, result.size());
        SimpleFeatureIterator fi = result.features();
        // check the first polygon
        SimpleFeature f = fi.next();
        fi.close();
        MultiLineString ml = (MultiLineString) f.getDefaultGeometry();
        assertEquals(2, ml.getNumGeometries());
        LineString ls = (LineString) ml.getGeometryN(0);
        CoordinateSequence cs = ls.getCoordinateSequence();
        assertEquals(2, cs.size());
        assertOrdinates(1000, 0, 0.1, cs, 0);
        assertOrdinates(2000, 0, 0.2, cs, 1);
        ls = (LineString) ml.getGeometryN(1);
        cs = ls.getCoordinateSequence();
        assertEquals(2, cs.size());
        assertOrdinates(10000, 4000, 1.4, cs, 0);
        assertOrdinates(10000, 5000, 1.5, cs, 1);
    }

    @Test
    public void testClipMultiline() throws Exception {
        SimpleFeatureCollection features = fsMultilines.getFeatures();
        ClipProcess cp = new ClipProcess();
        // clip with a rotated "L"
        SimpleFeatureCollection result =
                cp.execute(
                        features,
                        new WKTReader()
                                .read("POLYGON((900 900, 900 2100, 2100 2100, 2100 900, 900 900))"),
                        true);
        assertEquals(1, result.size());
        SimpleFeatureIterator fi = result.features();
        // check the first polygon
        SimpleFeature f = fi.next();
        fi.close();
        MultiLineString ml = (MultiLineString) f.getDefaultGeometry();
        assertEquals(2, ml.getNumGeometries());
        LineString ls = (LineString) ml.getGeometryN(0);
        CoordinateSequence cs = ls.getCoordinateSequence();
        assertEquals(4, cs.size());
        assertOrdinates(1000, 900, 0.9, cs, 0);
        assertOrdinates(1000, 1000, 1.0, cs, 1);
        assertOrdinates(2000, 1000, 2.0, cs, 2);
        assertOrdinates(2000, 900, 2.1, cs, 3);
        ls = (LineString) ml.getGeometryN(1);
        cs = ls.getCoordinateSequence();
        assertEquals(4, cs.size());
        assertOrdinates(1000, 2100, 0.9, cs, 0);
        assertOrdinates(1000, 2000, 1.0, cs, 1);
        assertOrdinates(2000, 2000, 2.0, cs, 2);
        assertOrdinates(2000, 2100, 2.1, cs, 3);
    }

    @Test
    public void testEmptyLines() throws Exception {
        SimpleFeatureCollection features = fsLines.getFeatures();
        ClipProcess cp = new ClipProcess();
        // to reproduce the issue we need a non rectangular clip polygon,
        // whose bbox will intersect the bbox of the geometry, but whose intersection is actually
        // emtpy
        SimpleFeatureCollection result =
                cp.execute(
                        features, new WKTReader().read("POLYGON((-8 -7, -8 3, 2 3, -8 -7))"), true);
        assertEquals(0, result.size());
        SimpleFeatureIterator fi = result.features();
        assertFalse(fi.hasNext());
        fi.close();
    }

    @Test
    public void testEmptyMultiLines() throws Exception {
        SimpleFeatureCollection features = fsMultilines.getFeatures();
        ClipProcess cp = new ClipProcess();
        SimpleFeatureCollection result =
                cp.execute(
                        features,
                        new WKTReader().read("POLYGON((-10 -10, -10 -5, -5 -5, -5 -10, -10 -10))"),
                        true);
        assertEquals(0, result.size());
        SimpleFeatureIterator fi = result.features();
        assertFalse(fi.hasNext());
        fi.close();
    }

    private void assertOrdinates(double x, double y, double z, CoordinateSequence cs, int index) {
        assertEquals(x, cs.getOrdinate(index, 0), 0d);
        assertEquals(y, cs.getOrdinate(index, 1), 0d);
        double otherZ = cs.getOrdinate(index, 2);
        if (Double.isNaN(z)) {
            assertTrue(Double.isNaN(otherZ));
        } else {
            assertEquals(z, otherZ, 0d);
        }
    }
}
