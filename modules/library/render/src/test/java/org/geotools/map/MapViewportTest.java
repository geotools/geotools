/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.*;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Unit tests for the viewport class.
 *
 * @author Michael Bedward
 */
public class MapViewportTest extends LoggerTest {

    // World bounds with aspect ratio 1:1
    private static final ReferencedEnvelope WORLD_1_1 =
            new ReferencedEnvelope(150, 152, -33, -35, DefaultGeographicCRS.WGS84);

    private static final ReferencedEnvelope BIG_WORLD_1_1 =
            new ReferencedEnvelope(140, 160, -30, -50, DefaultGeographicCRS.WGS84);

    // Screen area with aspect ratio 1:1
    private static final Rectangle SCREEN_1_1 = new Rectangle(100, 100);
    // Screen area with aspect ratio 2:1
    private static final Rectangle SCREEN_2_1 = new Rectangle(200, 100);
    // Screen area with aspect ratio 1:2
    private static final Rectangle SCREEN_1_2 = new Rectangle(100, 200);
    // Screen area with aspect ration 2:2 (or 1:1)
    private static final Rectangle SCREEN_2_2 = new Rectangle(200, 200);
    private static final double TOL = 1.0e-6d;

    @Test
    public void defaultCtor() {
        MapViewport vp = new MapViewport();

        assertFalse(vp.isMatchingAspectRatio());
        assertTrue(vp.isEmpty());
        assertTrue(vp.getBounds().isEmpty());
        assertTrue(vp.getScreenArea().isEmpty());
        assertNull(vp.getCoordinateReferenceSystem());
    }

    @Test
    public void boundsCtor() {
        MapViewport vp = new MapViewport(WORLD_1_1);

        assertTrue(vp.isEmpty());
        assertFalse(vp.isMatchingAspectRatio());
        assertTrue(vp.getScreenArea().isEmpty());

        assertTrue(WORLD_1_1.boundsEquals2D(vp.getBounds(), TOL));
    }

    @Test
    public void booleanCtor() {
        MapViewport vp = new MapViewport(true);
        assertTrue(vp.isEmpty());
        assertTrue(vp.isMatchingAspectRatio());
        assertTrue(vp.getBounds().isEmpty());
        assertTrue(vp.getScreenArea().isEmpty());
    }

    @Test
    public void fullCtor() {
        MapViewport vp = new MapViewport(WORLD_1_1, true);
        assertTrue(vp.isEmpty());
        assertTrue(vp.isMatchingAspectRatio());
        assertTrue(vp.getScreenArea().isEmpty());

        assertTrue(WORLD_1_1.boundsEquals2D(vp.getBounds(), TOL));
    }

    @Test
    public void copyCtor() {
        MapViewport vp = new MapViewport(WORLD_1_1, true);
        vp.setScreenArea(SCREEN_1_1);
        vp.setEditable(false);

        MapViewport copy = new MapViewport(vp);
        assertTrue(copy.isEditable());
        assertViewportsEqual(vp, copy);
    }

    @Test
    public void copyCtorHandlesEmtpyViewport() {
        MapViewport vp = new MapViewport();
        MapViewport copy = new MapViewport(vp);
        assertViewportsEqual(vp, copy);
    }

    /**
     * Checks if two viewports have equal attributes other than their editable setting.
     *
     * @param vp1 first viewport
     * @param vp2 second viewport
     */
    private void assertViewportsEqual(MapViewport vp1, MapViewport vp2) {
        assertEquals(vp1.getBounds(), vp2.getBounds());
        assertEquals(vp1.getScreenArea(), vp2.getScreenArea());
        assertEquals(vp1.getScreenToWorld(), vp2.getScreenToWorld());
        assertEquals(vp1.isMatchingAspectRatio(), vp2.isMatchingAspectRatio());
    }

    @Test
    public void setBoundsNoAspectCorrection() {
        MapViewport vp = new MapViewport();
        vp.setScreenArea(SCREEN_2_1);
        vp.setBounds(WORLD_1_1);

        assertTrue(WORLD_1_1.boundsEquals2D(vp.getBounds(), TOL));
    }

    @Test
    public void setBoundsWithAspectCorrection_2_1() {
        double w = WORLD_1_1.getWidth();
        ReferencedEnvelope expected =
                new ReferencedEnvelope(
                        WORLD_1_1.getMinX() - w / 2,
                        WORLD_1_1.getMaxX() + w / 2,
                        WORLD_1_1.getMinY(),
                        WORLD_1_1.getMaxY(),
                        WORLD_1_1.getCoordinateReferenceSystem());

        assertAspectCorrection(SCREEN_2_1, expected);
    }

    @Test
    public void setBoundsWithAspectCorrection_1_2() {
        double h = WORLD_1_1.getHeight();
        ReferencedEnvelope expected =
                new ReferencedEnvelope(
                        WORLD_1_1.getMinX(),
                        WORLD_1_1.getMaxX(),
                        WORLD_1_1.getMinY() - h / 2,
                        WORLD_1_1.getMaxY() + h / 2,
                        WORLD_1_1.getCoordinateReferenceSystem());

        assertAspectCorrection(SCREEN_1_2, expected);
    }

    private void assertAspectCorrection(Rectangle screenArea, ReferencedEnvelope expectedBounds) {
        MapViewport vp = new MapViewport(WORLD_1_1, true);
        vp.setScreenArea(screenArea);

        assertTrue(expectedBounds.boundsEquals2D(vp.getBounds(), TOL));
    }

    @Test
    public void settingBoundsSetsTheViewportCRS() {
        MapViewport vp = new MapViewport();
        assertFalse(
                CRS.equalsIgnoreMetadata(
                        WORLD_1_1.getCoordinateReferenceSystem(),
                        vp.getCoordinateReferenceSystem()));

        vp.setBounds(WORLD_1_1);
        assertTrue(
                CRS.equalsIgnoreMetadata(
                        WORLD_1_1.getCoordinateReferenceSystem(),
                        vp.getCoordinateReferenceSystem()));
    }

    @Test
    public void boundsChangeWithScreenArea() {
        MapViewport vp = new MapViewport(true);
        vp.setScreenArea(SCREEN_1_1);
        vp.setBounds(WORLD_1_1);

        vp.setScreenArea(SCREEN_2_1);

        ReferencedEnvelope expectedBounds =
                new ReferencedEnvelope(
                        WORLD_1_1.getMinX(),
                        WORLD_1_1.getMaxX() + WORLD_1_1.getWidth(),
                        WORLD_1_1.getMinY(),
                        WORLD_1_1.getMaxY(),
                        WORLD_1_1.getCoordinateReferenceSystem());

        assertTrue(expectedBounds.boundsEquals2D(vp.getBounds(), TOL));
        // Now check with Fixed bounds set - bounding box will change but will include the old bbox
        vp.setFixedBoundsOnResize(true);
        vp.setScreenArea(SCREEN_1_1);
        vp.setBounds(WORLD_1_1);

        vp.setScreenArea(SCREEN_2_1);

        expectedBounds =
                new ReferencedEnvelope(
                        WORLD_1_1.getMinX() - WORLD_1_1.getWidth() / 2,
                        WORLD_1_1.getMaxX() + WORLD_1_1.getWidth() / 2,
                        WORLD_1_1.getMinY(),
                        WORLD_1_1.getMaxY(),
                        WORLD_1_1.getCoordinateReferenceSystem());
        assertTrue(expectedBounds.boundsEquals2D(vp.getBounds(), TOL));
        // double size of screen - no change in BBOX
        vp.setScreenArea(SCREEN_1_1);
        vp.setBounds(WORLD_1_1);
        vp.setScreenArea(SCREEN_2_2);
        assertTrue(WORLD_1_1.boundsEquals2D(vp.getBounds(), TOL));
        // and back to small
        vp.setScreenArea(SCREEN_1_1);
        assertTrue(WORLD_1_1.boundsEquals2D(vp.getBounds(), TOL));
    }

    @Test
    public void newBoundsAreHonoured_NoAspectMatching() {
        MapViewport vp = new MapViewport(false);
        vp.setScreenArea(SCREEN_1_1);
        vp.setBounds(WORLD_1_1);

        vp.setBounds(BIG_WORLD_1_1);
        assertTrue(BIG_WORLD_1_1.boundsEquals2D(vp.getBounds(), TOL));
    }

    @Test
    public void newBoundsAreHonoured_AspectMatching() {
        MapViewport vp = new MapViewport(true);
        vp.setScreenArea(SCREEN_1_1);
        vp.setBounds(WORLD_1_1);

        vp.setBounds(BIG_WORLD_1_1);
        assertTrue(BIG_WORLD_1_1.boundsEquals2D(vp.getBounds(), TOL));
    }

    @Test
    public void coordinateTransform_MatchingAspectRatioDisabled() throws Exception {
        MapViewport vp = new MapViewport(false);

        // world and screen bounds with different aspect ratios
        final ReferencedEnvelope world = WORLD_1_1;
        final Rectangle screen = SCREEN_2_1;
        vp.setBounds(world);
        vp.setScreenArea(screen);

        double[] screenXY = {
            screen.getMinX(), screen.getMinY(), screen.getMaxX(), screen.getMaxY()
        };

        double[] worldXY = new double[screenXY.length];
        vp.getScreenToWorld().transform(screenXY, 0, worldXY, 0, screenXY.length / 2);

        assertEquals(world.getMinX(), worldXY[0], TOL);
        assertEquals(world.getMaxY(), worldXY[1], TOL);
        assertEquals(world.getMaxX(), worldXY[2], TOL);
        assertEquals(world.getMinY(), worldXY[3], TOL);
    }

    @Test
    public void coordinateTransform_MatchingAspectRatioEnabled() throws Exception {
        MapViewport vp = new MapViewport(true);

        // world and screen bounds with different aspect ratios
        final Rectangle screen = SCREEN_2_1;
        vp.setBounds(WORLD_1_1);
        vp.setScreenArea(screen);

        ReferencedEnvelope actualWorld = vp.getBounds();

        double[] screenXY = {
            screen.getMinX(), screen.getMinY(), screen.getMaxX(), screen.getMaxY()
        };

        double[] worldXY = new double[screenXY.length];
        vp.getScreenToWorld().transform(screenXY, 0, worldXY, 0, screenXY.length / 2);

        assertEquals(actualWorld.getMinX(), worldXY[0], TOL);
        assertEquals(actualWorld.getMaxY(), worldXY[1], TOL);
        assertEquals(actualWorld.getMaxX(), worldXY[2], TOL);
        assertEquals(actualWorld.getMinY(), worldXY[3], TOL);
    }

    @Test
    public void boundsCtorSetsNullWorldToScreen() {
        MapViewport vp = new MapViewport(WORLD_1_1);
        assertNull(vp.getWorldToScreen());
    }

    @Test
    public void boundsCtorSetNullScreenToWorld() {
        MapViewport vp = new MapViewport(WORLD_1_1);
        assertNull(vp.getScreenToWorld());
    }

    @Test
    public void noArgCtorThenSetBoundsGivesNullWorldToScreen() {
        MapViewport vp = new MapViewport();
        vp.setBounds(WORLD_1_1);
        assertNull(vp.getWorldToScreen());
    }

    @Test
    public void noArgCtorThenSetBoundsGivesNullScreenToWorld() {
        MapViewport vp = new MapViewport();
        vp.setBounds(WORLD_1_1);
        assertNull(vp.getWorldToScreen());
    }

    @Test
    public void newViewportIsEditableByDefault() {
        MapViewport vp = new MapViewport();
        assertTrue(vp.isEditable());
    }

    @Test
    public void setAndRetrieveEditableStatus() {
        MapViewport vp = new MapViewport();

        vp.setEditable(false);
        assertFalse(vp.isEditable());

        vp.setEditable(true);
        assertTrue(vp.isEditable());
    }

    @Test
    public void callSetBoundsWhenNonEditable() throws Exception {
        MapViewport vp = new MapViewport();
        vp.setBounds(WORLD_1_1);
        vp.setEditable(false);
        grabLogger();

        vp.setBounds(BIG_WORLD_1_1);
        String s = getLogOutput();
        assertTrue(s.contains("Ignored call to setBounds"));
        assertTrue(WORLD_1_1.boundsEquals2D(vp.getBounds(), TOL));

        releaseLogger();
    }

    @Test
    public void callSetScreenAreaWhenNonEditable() throws Exception {
        MapViewport vp = new MapViewport();
        vp.setScreenArea(SCREEN_1_1);
        vp.setEditable(false);
        grabLogger();

        vp.setScreenArea(SCREEN_2_1);
        String s = getLogOutput();
        assertTrue(s.contains("Ignored call to setScreenArea"));
        assertEquals(SCREEN_1_1, vp.getScreenArea());

        releaseLogger();
    }

    @Test
    public void callSetCoordinateReferenceSystemWhenNonEditable() throws Exception {
        final CoordinateReferenceSystem crs = WORLD_1_1.getCoordinateReferenceSystem();
        MapViewport vp = new MapViewport();
        vp.setCoordinateReferenceSystem(crs);
        vp.setEditable(false);
        grabLogger();

        vp.setCoordinateReferenceSystem(null);
        String s = getLogOutput();
        assertTrue(s.contains("Ignored call to setCoordinateReferenceSystem"));
        assertTrue(CRS.equalsIgnoreMetadata(crs, vp.getCoordinateReferenceSystem()));

        releaseLogger();
    }

    @Test
    public void callSetMatchingAspectRatioWhenNonEditable() throws Exception {
        MapViewport vp = new MapViewport();
        boolean original = vp.isMatchingAspectRatio();
        vp.setEditable(false);
        grabLogger();

        vp.setMatchingAspectRatio(!original);
        String s = getLogOutput();
        assertTrue(s.contains("Ignored call to setMatchingAspectRatio"));
        assertEquals(original, vp.isMatchingAspectRatio());

        releaseLogger();
    }
}
