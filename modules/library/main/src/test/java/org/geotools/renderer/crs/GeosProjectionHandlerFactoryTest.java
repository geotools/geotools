package org.geotools.renderer.crs;

import static org.geotools.renderer.crs.GeosProjectionHandlerFactory.getInnerAngle;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

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
    public void testAngleSatelliteFarAway() {
        // satellite is so far away from the earth, it approximates orthographic
        assertEquals(89.635, getInnerAngle(1_000_000, 6371), 1e-3);
    }

    @Test
    public void testAngleSatelliteEarth() {
        // satellite is sitting on mount everest :-)
        assertEquals(3.043, getInnerAngle(6380, 6371), 1e-3);
    }

    @Test
    public void testValidAreaMiddle() throws Exception {
        CoordinateReferenceSystem geos = CRS.decode("AUTO:97004,9001,0,0");
        ReferencedEnvelope re = new ReferencedEnvelope(-6000000, 6000000, -6000000, 6000000, geos);

        ProjectionHandler handler =
                ProjectionHandlerFinder.getHandler(re, DefaultGeographicCRS.WGS84, false);
        Geometry validArea = handler.getValidArea();
        assertThat(validArea, Matchers.instanceOf(Polygon.class));
        Polygon validPolygon = (Polygon) validArea;
        // roughly a circle in the -80,80 range
        Envelope envelope = validPolygon.getEnvelopeInternal();
        assertEquals(-80, envelope.getMinX(), 0.5);
        assertEquals(80, envelope.getMaxX(), 0.5);
        assertEquals(-80, envelope.getMinY(), 0.5);
        assertEquals(80, envelope.getMaxY(), 0.5);
    }

    @Test
    public void testValidAreaWest() throws Exception {
        CoordinateReferenceSystem geos = CRS.decode("AUTO:97004,9001,-135,0");
        ReferencedEnvelope re = new ReferencedEnvelope(-6000000, 6000000, -6000000, 6000000, geos);

        ProjectionHandler handler =
                ProjectionHandlerFinder.getHandler(re, DefaultGeographicCRS.WGS84, false);
        Geometry validArea = handler.getValidArea();
        assertThat(validArea, Matchers.instanceOf(MultiPolygon.class));
        assertEquals(2, validArea.getNumGeometries());

        // the circle has been split and reallocated on the datelines
        Envelope e1 = validArea.getGeometryN(0).getEnvelopeInternal();
        assertEquals(-180, e1.getMinX(), 0.5);
        assertEquals(-55, e1.getMaxX(), 0.5);
        assertEquals(-80, e1.getMinY(), 0.5);
        assertEquals(80, e1.getMaxY(), 0.5);

        Envelope e2 = validArea.getGeometryN(1).getEnvelopeInternal();
        assertEquals(145, e2.getMinX(), 0.5);
        assertEquals(180, e2.getMaxX(), 0.5);
        assertEquals(-66, e2.getMinY(), 0.5); // circle is not split in half
        assertEquals(66, e2.getMaxY(), 0.5);
    }

    @Test
    public void testValidAreaEast() throws Exception {
        CoordinateReferenceSystem geos = CRS.decode("AUTO:97004,9001,135,0");
        ReferencedEnvelope re = new ReferencedEnvelope(-6000000, 6000000, -6000000, 6000000, geos);

        ProjectionHandler handler =
                ProjectionHandlerFinder.getHandler(re, DefaultGeographicCRS.WGS84, false);
        Geometry validArea = handler.getValidArea();
        assertThat(validArea, Matchers.instanceOf(MultiPolygon.class));
        assertEquals(2, validArea.getNumGeometries());

        // circle split on the dateline
        Envelope e1 = validArea.getGeometryN(0).getEnvelopeInternal();
        assertEquals(55, e1.getMinX(), 0.5);
        assertEquals(180, e1.getMaxX(), 0.5);
        assertEquals(-80, e1.getMinY(), 0.5);
        assertEquals(80, e1.getMaxY(), 0.5);

        Envelope e2 = validArea.getGeometryN(1).getEnvelopeInternal();
        assertEquals(-180, e2.getMinX(), 0.5);
        assertEquals(-145, e2.getMaxX(), 0.5);
        assertEquals(-66, e2.getMinY(), 0.5); // circle is not split in half
        assertEquals(66, e2.getMaxY(), 0.5);
    }
}
