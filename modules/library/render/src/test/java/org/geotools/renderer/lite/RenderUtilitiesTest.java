/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import junit.framework.TestCase;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class RenderUtilitiesTest extends TestCase {

    /**
     * This tests a fix to handle Geographic CRSes (such as NAD83) whose domain of validity crosses
     * the Date Line. Previously this would cause a failure.
     */
    public void testNAD83() throws Exception {
        CoordinateReferenceSystem nad83 = CRS.decode("EPSG:4269", true);
        ReferencedEnvelope re =
                new ReferencedEnvelope(new Envelope(-121.1, -121.0, 46.7, 46.8), nad83);
        double scale = RendererUtilities.calculateScale(re, 750, 600, 75);
        assertEquals(41470, scale, 1d);
    }

    public void testWGS84() throws Exception {
        CoordinateReferenceSystem wgs84 = CRS.decode("EPSG:4326", true);
        ReferencedEnvelope re =
                new ReferencedEnvelope(new Envelope(-121.1, -121.0, 46.7, 46.8), wgs84);
        double scale = RendererUtilities.calculateScale(re, 750, 600, 75);
        assertEquals(41470, scale, 1d);
    }

    public void testWorld() throws Exception {
        CoordinateReferenceSystem wgs84 = CRS.decode("EPSG:4326", true);
        ReferencedEnvelope re = new ReferencedEnvelope(new Envelope(-180, 180, -90, 90), wgs84);
        double scale = RendererUtilities.calculateScale(re, 1000, 500, 75);
        assertEquals(52830886, scale, 1);
    }

    public void testWorldTwice() throws Exception {
        CoordinateReferenceSystem wgs84 = CRS.decode("EPSG:4326", true);
        ReferencedEnvelope re = new ReferencedEnvelope(new Envelope(-360, 360, -180, 180), wgs84);
        double scale = RendererUtilities.calculateScale(re, 1000, 500, 75);
        assertEquals(52830886 * 2, scale, 1);
    }

    public void testScaleProjected() throws Exception {
        CoordinateReferenceSystem utm1N = CRS.decode("EPSG:32601");
        // valid coords for utm nord 1 start from (200000, 0)
        ReferencedEnvelope re = new ReferencedEnvelope(new Envelope(200000, 200100, 0, 100), utm1N);
        double scale = RendererUtilities.calculateScale(re, 100, 100, 2.54);
        assertEquals(100.0, scale, 0.1); // account for projection deformation
    }

    public void testScaleCartesian() throws Exception {
        ReferencedEnvelope re =
                new ReferencedEnvelope(
                        new Envelope(0, 10, 0, 10), DefaultEngineeringCRS.CARTESIAN_2D);
        double scale = RendererUtilities.calculateScale(re, 10 * 100, 10 * 100, 2.54);
        assertEquals(1.0, scale, 0.00001); // no projection deformation here!
    }

    public void testScaleGeneric() throws Exception {
        ReferencedEnvelope re =
                new ReferencedEnvelope(
                        new Envelope(0, 10, 0, 10), DefaultEngineeringCRS.GENERIC_2D);
        double scale = RendererUtilities.calculateScale(re, 10 * 100, 10 * 100, 2.54);
        assertEquals(1.0, scale, 0.00001); // no projection deformation here!
    }

    public void testScaleGenericFeet() throws Exception {
        ReferencedEnvelope re =
                new ReferencedEnvelope(
                        new Envelope(1587500, 1587510, 475000, 475010),
                        CRS.decode("EPSG:2927", true));
        double scale = RendererUtilities.calculateScale(re, 10 * 100, 10 * 100, 2.54);
        assertEquals(0.30564, scale, 0.00001); // includes some projection deformation
    }

    public void testOGCScaleProjected() throws Exception {
        ReferencedEnvelope re =
                new ReferencedEnvelope(
                        new Envelope(0, 10, 0, 10), DefaultEngineeringCRS.CARTESIAN_2D);
        int tenMetersPixels = (int) Math.round(10 / 0.00028);
        double scale = RendererUtilities.calculateOGCScale(re, tenMetersPixels, new HashMap());
        assertEquals(1.0, scale, 0.0001);
    }

    public void testOGCScaleFeet() throws Exception {
        try {
            ReferencedEnvelope re =
                    new ReferencedEnvelope(
                            new Envelope(0, 10, 0, 10), CRS.decode("EPSG:2927", true));
            int tenMetersPixels = (int) Math.round(10 / 0.00028);

            RendererUtilities.SCALE_UNIT_COMPENSATION = false;
            double scale = RendererUtilities.calculateOGCScale(re, tenMetersPixels, new HashMap());
            assertEquals(1, scale, 0.0001);

            RendererUtilities.SCALE_UNIT_COMPENSATION = true;
            scale = RendererUtilities.calculateOGCScale(re, tenMetersPixels, new HashMap());
            assertEquals(0.304803, scale, 0.0001);
        } finally {
            RendererUtilities.SCALE_UNIT_COMPENSATION = true;
        }
    }

    public void testOGCScaleGeographic() throws Exception {
        // same example as page 29 in the SLD OGC spec, but with the expected scale corrected
        // since the OGC document contains a very imprecise one
        ReferencedEnvelope re =
                new ReferencedEnvelope(new Envelope(0, 2, 0, 2), DefaultGeographicCRS.WGS84);
        double scale = RendererUtilities.calculateOGCScale(re, 600, new HashMap());
        assertEquals(1325232.03, scale, 0.01);
    }

    public void testOGCScaleAffineProjected() throws Exception {
        // 1 pixel == 500 m  =>  0.00028 m [ screen] == 500 m [world]
        // => scaleDenominator = 500/0.00028
        final AffineTransform screenToWord = AffineTransform.getScaleInstance(500, 500);
        final AffineTransform worldToScreen = screenToWord.createInverse();

        final CoordinateReferenceSystem crs = DefaultEngineeringCRS.CARTESIAN_2D;
        double scale;

        scale = RendererUtilities.calculateOGCScaleAffine(crs, worldToScreen, new HashMap());
        assertEquals(500 / 0.00028, scale, 0.0001);

        worldToScreen.rotate(1.0);
        scale = RendererUtilities.calculateOGCScaleAffine(crs, worldToScreen, new HashMap());
        assertEquals(500 / 0.00028, scale, 0.0001);

        worldToScreen.translate(100.0, 100.0);
        scale = RendererUtilities.calculateOGCScaleAffine(crs, worldToScreen, new HashMap());
        assertEquals(500 / 0.00028, scale, 0.0001);
    }

    public void testOGCScaleAffineGeographic() throws Exception {
        // 1 pixel == 0.5 degree  =>  0.00028 m [ screen] == 0.5 degree * OGC_DEGREE_TO_METERS m
        // [world]
        // => scaleDenominator = 0.5 * OGC_DEGREE_TO_METERS/0.00028
        final AffineTransform screenToWord = AffineTransform.getScaleInstance(0.5, 0.5);
        final AffineTransform worldToScreen = screenToWord.createInverse();

        final CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        double scale;

        scale = RendererUtilities.calculateOGCScaleAffine(crs, worldToScreen, new HashMap());
        assertEquals(0.5 * RendererUtilities.OGC_DEGREE_TO_METERS / 0.00028, scale, 0.0001);

        worldToScreen.rotate(1.0);
        scale = RendererUtilities.calculateOGCScaleAffine(crs, worldToScreen, new HashMap());
        assertEquals(0.5 * RendererUtilities.OGC_DEGREE_TO_METERS / 0.00028, scale, 0.0001);

        worldToScreen.translate(100.0, 100.0);
        scale = RendererUtilities.calculateOGCScaleAffine(crs, worldToScreen, new HashMap());
        assertEquals(0.5 * RendererUtilities.OGC_DEGREE_TO_METERS / 0.00028, scale, 0.0001);
    }

    public void testCreateMapEnvelope() throws Exception {
        final double offset = 10000;
        final Rectangle paintArea = new Rectangle(0, 0, 800, 600);

        final AffineTransform worldToScreen = AffineTransform.getScaleInstance(0.5, 0.5);
        worldToScreen.translate(-offset, -offset);
        AffineTransform at = new AffineTransform(worldToScreen);
        Envelope env = RendererUtilities.createMapEnvelope(paintArea, at);
        assertEnvelopeEquals(
                new Envelope(offset, offset + 1600, offset, offset + 1200), env, 0.001);
        // Test for negative world coordinates
        at.translate(2 * offset, 2 * offset);
        env = RendererUtilities.createMapEnvelope(paintArea, at);
        assertEnvelopeEquals(
                new Envelope(-offset, -offset + 1600, -offset, -offset + 1200), env, 0.001);
        // Restore to standard offset
        at.translate(-2 * offset, -2 * offset);
        at.rotate(Math.PI / 2.0, offset, offset);
        env = RendererUtilities.createMapEnvelope(paintArea, at);
        assertEnvelopeEquals(
                new Envelope(offset, offset + 1200, offset - 1600, offset), env, 0.0001);
        at = new AffineTransform(worldToScreen);
        at.rotate(Math.PI / 4.0, offset, offset);
        env = RendererUtilities.createMapEnvelope(paintArea, at);
        assertEnvelopeEquals(
                new Envelope(
                        offset,
                        offset + Math.cos(Math.PI / 4.0) * 1600 + Math.sin(Math.PI / 4.0) * 1200,
                        offset - Math.sin(Math.PI / 4.0) * 1600,
                        offset + Math.cos(Math.PI / 4.0) * 1200),
                env,
                0.0001);
    }

    private void assertEnvelopeEquals(Envelope expected, Envelope actual, double delta) {
        if (expected.equals(actual)) {
            return;
        }
        boolean equals = true;
        equals &= Math.abs(expected.getMinX() - actual.getMinX()) <= delta;
        equals &= Math.abs(expected.getMaxX() - actual.getMaxX()) <= delta;
        equals &= Math.abs(expected.getMinY() - actual.getMinY()) <= delta;
        equals &= Math.abs(expected.getMaxY() - actual.getMaxY()) <= delta;
        if (!equals) {
            failNotEquals(null, expected, actual);
        }
    }

    /**
     * The following test is from the tile module where the behavior of RenderUtilities changed
     * between 2.2. and 2.4.
     */
    public void testCenterTile() throws Exception {
        ReferencedEnvelope envelope =
                new ReferencedEnvelope(0, 36, -18, 18, DefaultGeographicCRS.WGS84);
        double scale = RendererUtilities.calculateScale(envelope, 512, 512, 72.0);
        double groundDistance = Math.hypot(36, 36) * (1852 * 60);
        double pixelDistance = Math.hypot(512, 512) * (0.0254 / 72);
        double expected = groundDistance / pixelDistance;
        assertEquals(expected, scale, expected * 0.05); // no projection deformation here!
    }

    /** Test from GEOT-5336, RenderUtilities was miss calculating the quadrants for large maps */
    public void testHemisphereCrossing() throws Exception {
        CoordinateReferenceSystem wgs84 = CRS.decode("EPSG:4326", true);

        // First cross equator and prime meridian
        ReferencedEnvelope re =
                new ReferencedEnvelope(new Envelope(-72.0, 132.0, -4.0, 70.0), wgs84);
        double scale = RendererUtilities.calculateScale(re, 1000, 500, 75);
        assertTrue(scale > 1.0d);

        // Then prime meridian in North
        re = new ReferencedEnvelope(new Envelope(-72.0, 132.0, 4.0, 70.0), wgs84);
        scale = RendererUtilities.calculateScale(re, 1000, 500, 75);
        assertTrue(scale > 1.0d);

        // Then prime meridian in South
        re = new ReferencedEnvelope(new Envelope(-72.0, 132.0, -4.0, -70.0), wgs84);
        scale = RendererUtilities.calculateScale(re, 1000, 500, 75);
        assertTrue(scale > 1.0d);

        // Then equator in West
        re = new ReferencedEnvelope(new Envelope(-72.0, -132.0, -90.0, 90.0), wgs84);
        scale = RendererUtilities.calculateScale(re, 1000, 500, 75);
        assertTrue(scale > 1.0d);

        // Then Equator in East
        re = new ReferencedEnvelope(new Envelope(72.0, 132.0, -90.0, 90.0), wgs84);
        scale = RendererUtilities.calculateScale(re, 1000, 500, 75);
        assertTrue(scale > 1.0d);
    }

    public void testSampleForCentralPoint() throws Exception {
        // create a "C" shape polygon that won't contain it's centroid
        Polygon g =
                (Polygon)
                        new WKTReader()
                                .read(
                                        "POLYGON ((-112.534433451864 43.8706532611928,-112.499157652296 44.7878240499628,-99.6587666095152 44.7878240499628,-99.7242788087131 43.2155312692142,-111.085391877449 43.099601544023,-110.744593363875 36.1862602686501,-98.6760836215473 35.9436771582516,-98.7415958207452 33.5197257879307,-111.77852346112 33.9783111823157,-111.758573671673 34.6566040234952,-113.088767445077 34.7644575726901,-113.023255245879 43.8706532611928,-112.534433451864 43.8706532611928))");
        Point p = g.getCentroid();

        assertFalse(g.contains(p));

        // test with few samples, we shouldn't hit the inside
        assertNull(RendererUtilities.sampleForInternalPoint(g, p, null, null, -1, 2));

        // up the  samples, we should hit the inside now
        assertNotNull(RendererUtilities.sampleForInternalPoint(g, p, null, null, -1, 10));
    }

    public void testFindCentralPoint() throws Exception {
        // self crossing polygon
        Polygon g = (Polygon) new WKTReader().read("POLYGON ((0 0, 0 10, 10 0, 10 10, 0 0))");
        assertNotNull(RendererUtilities.getPolygonCentroid(g));
    }
}
