package org.geotools.renderer.crs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.io.WKTReader;

public class ProjectionHandlerTest {

    static final double EPS = 1e-6;

    static CoordinateReferenceSystem WGS84;

    static CoordinateReferenceSystem UTM32N;

    static CoordinateReferenceSystem MERCATOR;

    static CoordinateReferenceSystem MERCATOR_SHIFTED;

    @BeforeClass
    public static void setup() throws Exception {
        WGS84 = DefaultGeographicCRS.WGS84;
        UTM32N = CRS.decode("EPSG:32632", true);
        MERCATOR_SHIFTED = CRS.decode("EPSG:3349", true);
        MERCATOR = CRS.decode("EPSG:3395", true);
    }

    @Test
    public void testQueryWrappingWGS84() throws Exception {
        ReferencedEnvelope wgs84Envelope = new ReferencedEnvelope(-190, 60, -90, 45, WGS84);
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(wgs84Envelope, true);

        assertNull(handler.validArea);
        List<ReferencedEnvelope> envelopes = handler.getQueryEnvelopes(DefaultGeographicCRS.WGS84);
        assertEquals(2, envelopes.size());

        ReferencedEnvelope expected = new ReferencedEnvelope(170, 180, -90, 45, WGS84);
        assertTrue(envelopes.remove(wgs84Envelope));
        assertEquals(expected, envelopes.get(0));
    }

    @Test
    public void testValidAreaMercator() throws Exception {
        ReferencedEnvelope world = new ReferencedEnvelope(-180, 180, -89.9999, 89.9999, WGS84);
        ReferencedEnvelope mercatorEnvelope = world.transform(MERCATOR_SHIFTED, true);

        // check valid area
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(mercatorEnvelope, true);
        ReferencedEnvelope va = handler.validArea;
        assertNotNull(va);
        assertEquals(WGS84, va.getCoordinateReferenceSystem());
        assertTrue(va.getMinX() <= -180.0);
        assertTrue(va.getMaxX() >= 180.0);
        assertTrue(-90 < va.getMinY());
        assertTrue(90.0 > va.getMaxY());
    }

    @Test
    public void testQueryWrappingMercatorWorld() throws Exception {
        ReferencedEnvelope world = new ReferencedEnvelope(-200, 200, -89, 89, WGS84);
        ReferencedEnvelope mercatorEnvelope = world.transform(MERCATOR_SHIFTED, true);

        // get query area, we expect just one envelope spanning the whole world
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(mercatorEnvelope, true);
        List<ReferencedEnvelope> envelopes = handler.getQueryEnvelopes(DefaultGeographicCRS.WGS84);
        assertEquals(1, envelopes.size());

        ReferencedEnvelope env = envelopes.get(0);
        assertEquals(-180.0, env.getMinX(), EPS);
        assertEquals(180.0, env.getMaxX(), EPS);
        assertEquals(-89, env.getMinY(), 0.1);
        assertEquals(89.0, env.getMaxY(), 0.1);
    }

    @Test
    public void testQueryWrappingMercatorSeparate() throws Exception {
        ReferencedEnvelope world = new ReferencedEnvelope(160, 180, -40, 40, WGS84);
        ReferencedEnvelope mercatorEnvelope = world.transform(MERCATOR, true);
        // move it so that it crosses the dateline
        mercatorEnvelope.translate(mercatorEnvelope.getWidth() / 2, 0);

        // get query area, we expect two separate query envelopes, the original and the
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(mercatorEnvelope, true);
        List<ReferencedEnvelope> envelopes = handler.getQueryEnvelopes(DefaultGeographicCRS.WGS84);
        assertEquals(2, envelopes.size());

        ReferencedEnvelope reOrig = envelopes.get(0); // original
        assertEquals(170.0, reOrig.getMinX(), EPS);
        assertEquals(190.0, reOrig.getMaxX(), EPS);

        ReferencedEnvelope reAdded = envelopes.get(1); // added
        assertEquals(-180.0, reAdded.getMinX(), EPS);
        assertEquals(-170.0, reAdded.getMaxX(), EPS);
    }

    @Test
    public void testValidAreaUTM() throws Exception {
        ReferencedEnvelope wgs84Envelope = new ReferencedEnvelope(8, 10, 40, 45, WGS84);
        ReferencedEnvelope utmEnvelope = wgs84Envelope.transform(UTM32N, true);

        // check valid area
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(utmEnvelope, true);
        ReferencedEnvelope va = handler.validArea;
        assertNotNull(va);
        assertEquals(WGS84, va.getCoordinateReferenceSystem());
        assertTrue(9 - 90 < va.getMinX() && va.getMinX() <= 9 - 3);
        assertTrue(9 + 3 <= va.getMaxX() && va.getMaxX() < 9 + 90);
        assertEquals(-90, va.getMinY(), EPS);
        assertEquals(90.0, va.getMaxY(), EPS);
    }

    @Test
    public void testQueryUTM() throws Exception {
        ReferencedEnvelope wgs84Envelope = new ReferencedEnvelope(8, 10, 40, 45, WGS84);
        ReferencedEnvelope utmEnvelope = wgs84Envelope.transform(UTM32N, true);

        // get query area, we expect just one envelope, the original one
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(utmEnvelope, true);
        ReferencedEnvelope expected = utmEnvelope.transform(WGS84, true);
        List<ReferencedEnvelope> envelopes = handler.getQueryEnvelopes(DefaultGeographicCRS.WGS84);
        assertEquals(1, envelopes.size());
        assertEquals(expected, envelopes.get(0));
    }

    @Test
    public void testWrapGeometryMercator() throws Exception {
        ReferencedEnvelope world = new ReferencedEnvelope(160, 180, -40, 40, WGS84);
        ReferencedEnvelope mercatorEnvelope = world.transform(MERCATOR, true);
        // move it so that it crosses the dateline (measures are still accurate for something
        // crossing the dateline
        mercatorEnvelope.translate(mercatorEnvelope.getWidth() / 2, 0);

        // a geometry that will cross the dateline and sitting in the same area as the
        // rendering envelope
        Geometry g = new WKTReader().read("LINESTRING(170 -40, 190 40)");

        // make sure the geometry is not wrapped
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(mercatorEnvelope, true);
        assertTrue(handler.requiresProcessing(WGS84, g));
        Geometry preProcessed = handler.preProcess(WGS84, g);
        // no cutting expected
        assertEquals(g, preProcessed);
        // transform and post process
        Geometry transformed = JTS.transform(g, CRS.findMathTransform(WGS84, MERCATOR, true));
        Geometry postProcessed = handler.postProcess(transformed);
        Envelope env = postProcessed.getEnvelopeInternal();
        // check the geometry is in the same area as the rendering envelope
        assertEquals(mercatorEnvelope.getMinX(), env.getMinX(), EPS);
        assertEquals(mercatorEnvelope.getMaxX(), env.getMaxX(), EPS);
    }

    @Test
    public void testDuplicateGeometryMercator() throws Exception {
        ReferencedEnvelope world = new ReferencedEnvelope(-180, 180, -50, 50, WGS84);
        ReferencedEnvelope mercatorEnvelope = world.transform(MERCATOR, true);

        // a geometry that will cross the dateline and sitting in the same area as the
        // rendering envelope
        Geometry g = new WKTReader().read("LINESTRING(170 -50, 190 50)");

        // make sure the geometry is not wrapped
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(mercatorEnvelope, true);
        assertTrue(handler.requiresProcessing(WGS84, g));
        Geometry preProcessed = handler.preProcess(WGS84, g);
        // no cutting expected
        assertEquals(g, preProcessed);
        // transform and post process
        Geometry transformed = JTS.transform(g, CRS.findMathTransform(WGS84, MERCATOR, true));
        Geometry postProcessed = handler.postProcess(transformed);
        // should have been duplicated in two parts
        assertTrue(postProcessed instanceof MultiLineString);
        MultiLineString mls = (MultiLineString) postProcessed;
        assertEquals(2, mls.getNumGeometries());
        // the two geometries width should be the same as 20°
        double twentyDegWidth = mercatorEnvelope.getWidth() / 18;
        assertEquals(twentyDegWidth, mls.getGeometryN(0).getEnvelopeInternal().getWidth(), EPS);
        assertEquals(twentyDegWidth, mls.getGeometryN(1).getEnvelopeInternal().getWidth(), EPS);
    }

    public void testCutGeometryUTM() throws Exception {
        ReferencedEnvelope wgs84Envelope = new ReferencedEnvelope(8, 10, 40, 45, WGS84);
        ReferencedEnvelope utmEnvelope = wgs84Envelope.transform(UTM32N, true);

        // a geometry that will definitely go outside of the UTM32N valid area
        Geometry g = new WKTReader().read("LINESTRING(-170 -40, 170, 40)");

        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(utmEnvelope, true);
        assertTrue(handler.requiresProcessing(UTM32N, g));
        Geometry preProcessed = handler.preProcess(WGS84, g);
        assertTrue(!preProcessed.equals(g));
        assertTrue(handler.validArea.contains(preProcessed.getEnvelopeInternal()));
    }

}
