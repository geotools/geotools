package org.geotools.feature.collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class ClippedFeatureCollectionTest {

    DefaultFeatureCollection delegatePolygonZ;

    DefaultFeatureCollection delegateMultiLineZ;

    DefaultFeatureCollection delegateLineZ;

    @Before
    public void setUp() throws Exception {

        WKTReader reader = new WKTReader();

        // polygon
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("Polygons3D");
        tb.add("geom", Polygon.class);
        tb.add("name", String.class);
        SimpleFeatureType featureType = tb.buildFeatureType();
        delegatePolygonZ = new DefaultFeatureCollection(null, featureType);
        SimpleFeatureBuilder bPoly = new SimpleFeatureBuilder(featureType);
        Geometry geom = reader.read("POLYGON((0 0 0, 0 10000 2, 10000 10000 2, 10000 0 0, 0 0 0))");
        bPoly.add(geom);
        bPoly.add("one");
        delegatePolygonZ.add(bPoly.buildFeature("fid.1"));
        bPoly.reset();

        Geometry geom2 = reader.read("POLYGON((0 0, 0 5000, 5000 5000, 5000 0, 0 0))");
        bPoly.add(geom2);
        bPoly.add("two");
        delegatePolygonZ.add(bPoly.buildFeature("fid.2"));

        // Multi lines
        SimpleFeatureTypeBuilder tbML = new SimpleFeatureTypeBuilder();
        tbML.setName("MultiLines");
        tbML.add("geom", MultiLineString.class);
        tbML.add("name", String.class);
        SimpleFeatureType featureTypeML = tbML.buildFeatureType();
        delegateMultiLineZ = new DefaultFeatureCollection(null, featureTypeML);
        SimpleFeatureBuilder bML = new SimpleFeatureBuilder(featureTypeML);
        Geometry geomML =
                reader.read(
                        "MULTILINESTRING((1000 0 0, 1000 1000 1, 2000 1000 2, 2000 0 3), (1000 3000 0, 1000 2000 1, 2000 2000 2, 2000 3000 3))");
        bML.add(geomML);
        bML.add("one");
        delegateMultiLineZ.add(bML.buildFeature("fid.1"));

        // lines

        SimpleFeatureTypeBuilder tbL = new SimpleFeatureTypeBuilder();
        tbL.setName("Lines");
        tbL.add("geom", LineString.class);
        tbL.add("name", String.class);
        SimpleFeatureType featureTypeL = tbL.buildFeatureType();
        delegateLineZ = new DefaultFeatureCollection(null, featureTypeL);
        SimpleFeatureBuilder bL = new SimpleFeatureBuilder(featureTypeL);
        Geometry geomL = reader.read("LINESTRING(0 0 0, 10000 0 1, 10000 10000 2)");
        bL.add(geomL);
        bL.add("one");
        delegateLineZ.add(bL.buildFeature("fid.1"));
    }

    @Test
    public void testClipPolygon() throws ParseException {
        Geometry clip =
                new WKTReader()
                        .read("POLYGON((-10 -10, -10 10010, 10010 10010, 10010 -10, -10 -10))");

        ClippedFeatureCollection collection =
                new ClippedFeatureCollection(delegatePolygonZ, clip, true);
        assertSquaresMetersIdentical(collection);
    }

    @Test
    public void testClipPoly3DOnBorder() throws Exception {
        Geometry clip = new WKTReader().read("POLYGON((0 0, 0 10000, 10000 10000, 10000 0, 0 0))");
        SimpleFeatureCollection result = new ClippedFeatureCollection(delegatePolygonZ, clip, true);
        assertEquals(2, result.size());
        assertSquaresMetersIdentical(result);
    }

    @Test
    public void testClipPoly3DNewVertices() throws Exception {
        Geometry clip =
                new WKTReader().read("POLYGON((0 5000, 0 10000, 10000 10000, 10000 5000, 0 5000))");
        SimpleFeatureCollection result = new ClippedFeatureCollection(delegatePolygonZ, clip, true);
        assertEquals(1, result.size());
        try (SimpleFeatureIterator fi = result.features()) {
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
        }
    }

    @Test
    public void testClipPoly3DFullyInside() throws Exception {
        Geometry clip =
                new WKTReader()
                        .read("POLYGON((2500 2500, 2500 7500, 7500 7500, 7500 2500, 2500 2500))");
        SimpleFeatureCollection result = new ClippedFeatureCollection(delegatePolygonZ, clip, true);
        assertEquals(2, result.size());
        // check the first polygon
        SimpleFeature f = DataUtilities.first(result);
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
    }

    @Test
    public void testClipMultiLine() throws ParseException {
        Geometry clip =
                new WKTReader().read("POLYGON((900 900, 900 2100, 2100 2100, 2100 900, 900 900))");

        ClippedFeatureCollection collection =
                new ClippedFeatureCollection(delegateMultiLineZ, clip, true);
        assertEquals(1, collection.size());
        // check the first polygon
        SimpleFeature f = DataUtilities.first(collection);
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
    public void testClipLine3DIncluded() throws Exception {
        Geometry clip =
                new WKTReader()
                        .read("POLYGON((-10 -10, -10 10010, 10010 10010, 10010 -10, -10 -10))");
        ClippedFeatureCollection collection =
                new ClippedFeatureCollection(delegateLineZ, clip, true);
        assertEquals(1, collection.size());
        try (SimpleFeatureIterator fi = collection.features()) {
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
        }
    }

    @Test
    public void testClipLine3DMidFirstSegment() throws Exception {
        Geometry clip =
                new WKTReader().read("POLYGON((-10 -10, -10 10, 5000 10, 5000 -10, -10 -10))");
        ClippedFeatureCollection collection =
                new ClippedFeatureCollection(delegateLineZ, clip, true);
        assertEquals(1, collection.size());
        try (SimpleFeatureIterator fi = collection.features()) {
            // check the first polygon
            SimpleFeature f = fi.next();
            MultiLineString ml = (MultiLineString) f.getDefaultGeometry();
            assertEquals(1, ml.getNumGeometries());
            LineString ls = (LineString) ml.getGeometryN(0);
            CoordinateSequence cs = ls.getCoordinateSequence();
            assertEquals(2, cs.size());
            assertOrdinates(0, 0, 0, cs, 0);
            assertOrdinates(5000, 0, 0.5, cs, 1);
        }
    }

    @Test
    public void testClipExtractBend() throws Exception {
        Geometry clip =
                new WKTReader()
                        .read("POLYGON((5000 -10, 5000 5000, 11000 5000, 11000 -10, 5000 -10))");
        ClippedFeatureCollection collection =
                new ClippedFeatureCollection(delegateLineZ, clip, true);
        assertEquals(1, collection.size());
        try (SimpleFeatureIterator fi = collection.features()) {
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
        }
    }

    @Test
    public void testClipExtractSeparateBitsLowLine() throws Exception {
        Geometry clip =
                new WKTReader()
                        .read(
                                "POLYGON((1000 -10, 1000 10, 9000 10, 9000 -10, 8000 -10, 8000 5, 2000 5, 2000 -10, 1000 -10))");

        ClippedFeatureCollection collection =
                new ClippedFeatureCollection(delegateLineZ, clip, true);
        assertEquals(1, collection.size());
        try (SimpleFeatureIterator fi = collection.features()) {
            // check the first polygon
            SimpleFeature f = fi.next();
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
    }

    @Test
    public void testClipExtractSeparateBitsBothLines() throws Exception {
        Geometry clip =
                new WKTReader()
                        .read(
                                "POLYGON((1000 -10, 1000 5000, 11000 5000, 11000 4000, 2000 4000, 2000 -10, 1000 -10))");
        ClippedFeatureCollection collection =
                new ClippedFeatureCollection(delegateLineZ, clip, true);
        assertEquals(1, collection.size());
        SimpleFeature f = DataUtilities.first(collection);
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

    private void assertSquaresMetersIdentical(SimpleFeatureCollection result) {
        try (SimpleFeatureIterator fi = result.features()) {
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
