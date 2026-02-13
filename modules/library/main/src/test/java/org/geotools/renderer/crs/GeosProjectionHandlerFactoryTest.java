package org.geotools.renderer.crs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.IntStream;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

public class GeosProjectionHandlerFactoryTest {

    @Test
    public void testValidAreaFarAway() throws Exception {
        // 1_000_000 km (expressed in meters), that is, far away
        CoordinateReferenceSystem geos = CRS.decode("AUTO:97004,9001,0,1000000000");
        ReferencedEnvelope re = new ReferencedEnvelope(-6000000, 6000000, -6000000, 6000000, geos);
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(re, DefaultGeographicCRS.WGS84, false);
        Geometry validArea = handler.getValidArea();

        assertThat(validArea, Matchers.instanceOf(Polygon.class));
        Polygon validPolygon = (Polygon) validArea;
        // roughly a circle in the -90,90 range
        Envelope envelope = validPolygon.getEnvelopeInternal();
        assertEquals(-90, envelope.getMinX(), 0.5);
        assertEquals(90, envelope.getMaxX(), 0.5);
        assertEquals(-90, envelope.getMinY(), 0.5);
        assertEquals(90, envelope.getMaxY(), 0.5);
    }

    @Test
    public void testValidAreaEarth() throws Exception {
        // 5000 meters, that is, the satellite is sitting just above the earth surface, the height of a mountain
        CoordinateReferenceSystem geos = CRS.decode("AUTO:97004,9001,0,5000");
        ReferencedEnvelope re = new ReferencedEnvelope(-6000000, 6000000, -6000000, 6000000, geos);
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(re, DefaultGeographicCRS.WGS84, false);
        Geometry validArea = handler.getValidArea();

        assertThat(validArea, Matchers.instanceOf(Polygon.class));
        Polygon validPolygon = (Polygon) validArea;
        // roughly a circle of 2.26 degrees
        Envelope envelope = validPolygon.getEnvelopeInternal();
        assertEquals(-2.3, envelope.getMinX(), 0.1);
        assertEquals(2.3, envelope.getMaxX(), 0.1);
        assertEquals(-2.3, envelope.getMinY(), 0.1);
        assertEquals(2.3, envelope.getMaxY(), 0.1);
    }

    @Test
    public void testValidAreaMiddle() throws Exception {
        CoordinateReferenceSystem geos = CRS.decode("AUTO:97004,9001,0,0");
        ReferencedEnvelope re = new ReferencedEnvelope(-6000000, 6000000, -6000000, 6000000, geos);

        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(re, DefaultGeographicCRS.WGS84, false);
        Geometry validArea = handler.getValidArea();
        assertThat(validArea, Matchers.instanceOf(Polygon.class));
        Polygon validPolygon = (Polygon) validArea;
        // roughly a circle in the -81.3,81.3 range
        Envelope envelope = validPolygon.getEnvelopeInternal();
        assertEquals(-81.3, envelope.getMinX(), 0.5);
        assertEquals(81.3, envelope.getMaxX(), 0.5);
        assertEquals(-81.3, envelope.getMinY(), 0.5);
        assertEquals(81.3, envelope.getMaxY(), 0.5);
    }

    @Test
    public void testValidAreaWest() throws Exception {
        CoordinateReferenceSystem geos = CRS.decode("AUTO:97004,9001,-135,0");
        ReferencedEnvelope re = new ReferencedEnvelope(-6000000, 6000000, -6000000, 6000000, geos);

        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(re, DefaultGeographicCRS.WGS84, false);
        Geometry validArea = handler.getValidArea();
        assertThat(validArea, Matchers.instanceOf(MultiPolygon.class));
        assertEquals(2, validArea.getNumGeometries());

        // the circle has been split and reallocated on the datelines
        List<Envelope> envelopes = getInternalEnvelopes(validArea);
        Envelope e1 = envelopes.get(0);
        assertEquals(-180, e1.getMinX(), 0.1);
        assertEquals(-53.7, e1.getMaxX(), 0.1);
        assertEquals(-81.3, e1.getMinY(), 0.1);
        assertEquals(81.3, e1.getMaxY(), 0.1);

        Envelope e2 = envelopes.get(1);
        assertEquals(143.7, e2.getMinX(), 0.1);
        assertEquals(180, e2.getMaxX(), 0.1);
        assertEquals(-77.6, e2.getMinY(), 0.1);
        assertEquals(77.6, e2.getMaxY(), 0.1);
    }

    /** Helper method to extract the envelopes of the geometries in a consistent order, for testing purposes. */
    List<Envelope> getInternalEnvelopes(Geometry g) {
        return IntStream.range(0, g.getNumGeometries())
                .mapToObj(g::getGeometryN)
                .map(Geometry::getEnvelopeInternal)
                .sorted() // ensure consistent order for testing
                .toList();
    }

    @Test
    public void testValidAreaEast() throws Exception {
        CoordinateReferenceSystem geos = CRS.decode("AUTO:97004,9001,135,0");
        ReferencedEnvelope re = new ReferencedEnvelope(-6000000, 6000000, -6000000, 6000000, geos);

        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(re, DefaultGeographicCRS.WGS84, false);
        Geometry validArea = handler.getValidArea();
        assertThat(validArea, Matchers.instanceOf(MultiPolygon.class));
        assertEquals(2, validArea.getNumGeometries());

        // circle split on the dateline
        List<Envelope> envelopes = getInternalEnvelopes(validArea);
        Envelope e1 = envelopes.get(0);
        assertEquals(-180, e1.getMinX(), 0.1);
        assertEquals(-143.7, e1.getMaxX(), 0.1);
        assertEquals(-77.6, e1.getMinY(), 0.1); // circle is not split in half
        assertEquals(77.6, e1.getMaxY(), 0.1);

        Envelope e2 = envelopes.get(1);
        assertEquals(53.7, e2.getMinX(), 0.1);
        assertEquals(180, e2.getMaxX(), 0.1);
        assertEquals(-81.3, e2.getMinY(), 0.1);
        assertEquals(81.3, e2.getMaxY(), 0.1);
    }
}
