/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.crs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.projection.MapProjection;
import org.hamcrest.CoreMatchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

@SuppressWarnings("PMD.SimplifiableTestAssertion") // need equals with tolerance
public class OrthographicProjectionHandlerFactoryTest {

    private static final GeometryFactory GFACTORY = new GeometryFactory();
    private static DefaultGeographicCRS WGS84;
    private static Envelope WORLD = new Envelope(-7_000_000, 7_000_000, -7_000_000, 7_000_000);

    @BeforeClass
    public static void setup() throws Exception {
        WGS84 = DefaultGeographicCRS.WGS84;
        MapProjection.SKIP_SANITY_CHECKS = true;
    }

    @AfterClass
    public static void teardown() throws Exception {
        MapProjection.SKIP_SANITY_CHECKS = false;
    }

    @Test
    public void testObliqueEurope() throws Exception {
        CoordinateReferenceSystem ortho = CRS.decode("AUTO:42003,9001,11,48");
        ReferencedEnvelope re = new ReferencedEnvelope(WORLD, ortho);
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(re, WGS84, false);
        assertNotNull(handler);

        //  the valid area is a single polygon
        Geometry validArea = handler.getValidArea();
        assertNotNull(validArea);
        assertThat(validArea, CoreMatchers.instanceOf(Polygon.class));

        // polygon is complex, testing a few points that are supposed to be inside and some outside
        // ... middle bottom of shape
        assertTrue(validArea.contains(point(10, -41)));
        assertFalse(validArea.contains(point(10, -42)));
        // ... close to east dateline
        assertTrue(validArea.contains(point(-161, 42)));
        assertFalse(validArea.contains(point(-161, 41)));
        // ... close to west dateline
        assertTrue(validArea.contains(point(173, 41)));
        assertFalse(validArea.contains(point(173, 40)));
        // area towards the north-pole area is fully inside
        for (double lon = -179; lon < 180; lon += 5) {
            for (double lat = 44.9; lat < 89.7; lat += 5)
                assertTrue(
                        "Failed check with " + lon + "," + lat,
                        validArea.contains(point(lon, lat)));
        }

        // query envelope covers all norther hemisphere, and part of the south
        List<ReferencedEnvelope> envelopes = handler.getQueryEnvelopes();
        assertEquals(1, envelopes.size());
        ReferencedEnvelope envelope = envelopes.get(0);
        assertTrue(JTS.equals(new Envelope(-180, 180, -42, 90), envelope, 0.3));
    }

    @Test
    public void testObliqueAfrica() throws Exception {
        CoordinateReferenceSystem ortho = CRS.decode("AUTO:42003,9001,11,-48");
        ReferencedEnvelope re = new ReferencedEnvelope(WORLD, ortho);
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(re, WGS84, false);
        assertNotNull(handler);

        //  the valid area is a single polygon
        Geometry validArea = handler.getValidArea();
        assertNotNull(validArea);
        assertThat(validArea, CoreMatchers.instanceOf(Polygon.class));

        // polygon is complex, testing a few points that are supposed to be inside and some outside
        // ... middle top of shape
        assertTrue(validArea.contains(point(10, 41)));
        assertFalse(validArea.contains(point(10, 42)));
        // ... close to east dateline
        assertTrue(validArea.contains(point(-161, -42)));
        assertFalse(validArea.contains(point(-161, -41)));
        // ... close to west dateline
        assertTrue(validArea.contains(point(173, -41)));
        assertFalse(validArea.contains(point(173, -40)));
        // area towards the north-pole area is fully inside (mind there is a small gap)
        for (double lon = -179; lon < 180; lon += 5) {
            for (double lat = -89.7; lat < -41; lat += 5)
                assertTrue(
                        "Failed check with " + lon + "," + lat,
                        validArea.contains(point(lon, lat)));
        }

        // query envelope covers all norther emisphere, and part of the south
        List<ReferencedEnvelope> envelopes = handler.getQueryEnvelopes();
        assertEquals(1, envelopes.size());
        ReferencedEnvelope envelope = envelopes.get(0);
        assertTrue(JTS.equals(new Envelope(-180, 180, -90, 42), envelope, 0.3));
    }

    @Test
    public void testNorthPolar() throws Exception {
        CoordinateReferenceSystem ortho = CRS.decode("AUTO:42003,9001,0,90");
        ReferencedEnvelope re = new ReferencedEnvelope(WORLD, ortho);
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(re, WGS84, false);
        assertNotNull(handler);

        //  the valid area is a single polygon
        Geometry validArea = handler.getValidArea();
        assertNotNull(validArea);
        assertTrue(validArea.isRectangle());
        Envelope expectedValid = new Envelope(-180, 180, 0, 90);
        assertTrue(JTS.equals(expectedValid, validArea.getEnvelopeInternal(), 0.3));

        // query envelope is the same as valid area
        List<ReferencedEnvelope> envelopes = handler.getQueryEnvelopes();
        assertEquals(1, envelopes.size());
        ReferencedEnvelope envelope = envelopes.get(0);
        assertTrue(JTS.equals(expectedValid, envelope, 0.3));
    }

    @Test
    public void testSouthPolar() throws Exception {
        CoordinateReferenceSystem ortho = CRS.decode("AUTO:42003,9001,0,-90");
        ReferencedEnvelope re = new ReferencedEnvelope(WORLD, ortho);
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(re, WGS84, false);
        assertNotNull(handler);

        //  the valid area is a single polygon
        Geometry validArea = handler.getValidArea();
        assertNotNull(validArea);
        assertTrue(validArea.isRectangle());
        Envelope expectedValid = new Envelope(-180, 180, -90, 0);
        assertTrue(JTS.equals(expectedValid, validArea.getEnvelopeInternal(), 0.3));

        // query envelope is the same as valid area
        List<ReferencedEnvelope> envelopes = handler.getQueryEnvelopes();
        assertEquals(1, envelopes.size());
        ReferencedEnvelope envelope = envelopes.get(0);
        assertTrue(JTS.equals(expectedValid, envelope, 0.3));
    }

    @Test
    public void testEquatorialNoDateline() throws Exception {
        CoordinateReferenceSystem ortho = CRS.decode("AUTO:42003,9001,0,0");
        ReferencedEnvelope re = new ReferencedEnvelope(WORLD, ortho);
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(re, WGS84, false);
        assertNotNull(handler);

        //  the valid area is a single polygon (but slightly off the dateline and pole)
        Geometry validArea = handler.getValidArea();
        assertNotNull(validArea);
        Polygon expected = JTS.toGeometry(new Envelope(-90, 90, -90, 90));
        assertEquals(0, expected.difference(validArea).getArea(), 0.5);
        assertEquals(0, validArea.difference(expected).getArea(), 0.5);

        // query envelope is the same as valid area
        List<ReferencedEnvelope> envelopes = handler.getQueryEnvelopes();
        assertEquals(1, envelopes.size());
        assertTrue(JTS.equals(expected.getEnvelopeInternal(), envelopes.get(0), 0.1));
    }

    @Test
    public void testEquatorialWestDateline() throws Exception {
        CoordinateReferenceSystem ortho = CRS.decode("AUTO:42003,9001,150,0");
        ReferencedEnvelope re = new ReferencedEnvelope(WORLD, ortho);
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(re, WGS84, false);
        assertNotNull(handler);

        //  the valid area is two rectangles
        Geometry validArea = handler.getValidArea();
        assertNotNull(validArea);
        assertThat(validArea, CoreMatchers.instanceOf(MultiPolygon.class));
        MultiPolygon mp = (MultiPolygon) validArea;
        assertEquals(2, mp.getNumGeometries());
        Polygon p1 = (Polygon) mp.getGeometryN(0);
        Polygon p2 = (Polygon) mp.getGeometryN(1);
        // manual checks since the areas have been shrunk a little from poles/datelines to
        // avoid mathematical issues in the projection
        Polygon e1 = JTS.toGeometry(new Envelope(-180, -120, -90, 90));
        assertEquals(0, e1.difference(p1).getArea(), 0.5);
        assertEquals(0, p1.difference(e1).getArea(), 0.5);
        Polygon e2 = JTS.toGeometry(new Envelope(60, 180, -90, 90));
        assertEquals(0, e2.difference(p2).getArea(), 0.5);
        assertEquals(0, p2.difference(e2).getArea(), 0.5);

        // query envelopes are the same as valid area
        List<ReferencedEnvelope> envelopes = handler.getQueryEnvelopes();
        assertEquals(2, envelopes.size());
        assertTrue(JTS.equals(p1.getEnvelopeInternal(), envelopes.get(0), 0.1));
        assertTrue(JTS.equals(p2.getEnvelopeInternal(), envelopes.get(1), 0.1));
    }

    private Point point(double x, double y) {
        return GFACTORY.createPoint(new Coordinate(x, y));
    }
}
