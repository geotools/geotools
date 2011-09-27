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

import java.awt.Rectangle;
import java.io.ByteArrayOutputStream;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.logging.Logging;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import static org.junit.Assert.*;

/**
 * Unit tests for the viewport class.
 * 
 * @author Michael Bedward
 *
 * @source $URL$
 */
public class MapViewportTest {
    
    // World bounds with aspect ratio 1:1
    private static final ReferencedEnvelope WORLD_1_1 = new ReferencedEnvelope(
            150, 152, -33, -35, DefaultGeographicCRS.WGS84);
    
    private static final ReferencedEnvelope BIG_WORLD_1_1 = new ReferencedEnvelope(
            140, 160, -30, -50, DefaultGeographicCRS.WGS84);

    // Screen area with aspect ratio 1:1
    private static final Rectangle SCREEN_1_1 = new Rectangle(100, 100);
    // Screen area with aspect ratio 2:1
    private static final Rectangle SCREEN_2_1 = new Rectangle(200, 100);
    // Screen area with aspect ratio 1:2
    private static final Rectangle SCREEN_1_2 = new Rectangle(100, 200);
    
    private static final double TOL = 1.0e-6d;
    
    private static final Logger LOGGER = Logging.getLogger("org.geotools.map");
    private static Level oldLevel;
    private Handler logHandler;
    private ByteArrayOutputStream logStream;
    
    @BeforeClass
    public static void setupOnce() {
        oldLevel = LOGGER.getLevel();
        LOGGER.setLevel(Level.FINE); 
    }
    
    @AfterClass
    public static void cleanupOnce() {
        LOGGER.setLevel(oldLevel);
    }
    
    
    @Test
    public void defaultCtor() {
        MapViewport vp = new MapViewport();
        
        assertFalse(vp.isMatchingAspectRatio());
        
        assertTrue(vp.isEmpty());
        assertTrue(vp.getBounds().isEmpty());
        assertTrue(vp.getScreenArea().isEmpty());
        
        assertTrue(CRS.equalsIgnoreMetadata(MapViewport.DEFAULT_CRS, 
                vp.getCoordinateReferenceSystem()));
    }
    
    @Test
    public void boundsCtor() {
        MapViewport vp = new MapViewport(WORLD_1_1);

        assertTrue(vp.isEmpty());
        assertFalse(vp.isMatchingAspectRatio());
        assertTrue(vp.getScreenArea().isEmpty());
        
        assertTrue( WORLD_1_1.boundsEquals2D(vp.getBounds(), TOL) );
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
        
        assertTrue( WORLD_1_1.boundsEquals2D(vp.getBounds(), TOL) );
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
     * Checks if two viewports have equal attributes other than their
     * editable setting.
     * 
     * @param vp1 first viewport
     * @param vp2 second viewport
     */
    private void assertViewportsEqual(MapViewport vp1, MapViewport vp2) {
        assertEquals(vp1.getBounds(), vp2.getBounds());
        assertEquals(vp1.getScreenArea(), vp2.getScreenArea());
        assertEquals(vp1.getScreenToWorld(), vp2.getScreenToWorld());
        assertEquals(vp1.isExplicitCoordinateReferenceSystem(), vp2.isExplicitCoordinateReferenceSystem());
        assertEquals(vp1.isMatchingAspectRatio(), vp2.isMatchingAspectRatio());
    }
    
    @Test
    public void setBoundsNoAspectCorrection() {
        MapViewport vp = new MapViewport();
        vp.setScreenArea(SCREEN_2_1);
        vp.setBounds(WORLD_1_1);

        assertTrue( WORLD_1_1.boundsEquals2D(vp.getBounds(), TOL) );
    }


    @Test
    public void setBoundsWithAspectCorrection_2_1() {
        double w = WORLD_1_1.getWidth();
        ReferencedEnvelope expected = new ReferencedEnvelope(
                WORLD_1_1.getMinX() - w/2, WORLD_1_1.getMaxX() + w/2,
                WORLD_1_1.getMinY(), WORLD_1_1.getMaxY(),
                WORLD_1_1.getCoordinateReferenceSystem());

        assertAspectCorrection(SCREEN_2_1, expected);
    }

    @Test
    public void setBoundsWithAspectCorrection_1_2() {
        double h = WORLD_1_1.getHeight();
        ReferencedEnvelope expected = new ReferencedEnvelope(
                WORLD_1_1.getMinX(), WORLD_1_1.getMaxX(),
                WORLD_1_1.getMinY() - h/2, WORLD_1_1.getMaxY() + h/2,
                WORLD_1_1.getCoordinateReferenceSystem());
        
        assertAspectCorrection(SCREEN_1_2, expected);
    }
    
    private void assertAspectCorrection(Rectangle screenArea, ReferencedEnvelope expectedBounds) {
        MapViewport vp = new MapViewport(WORLD_1_1, true);
        vp.setScreenArea(screenArea);
        
        assertTrue( expectedBounds.boundsEquals2D(vp.getBounds(), TOL) );
    }
    
    @Test
    public void settingBoundsSetsTheViewportCRS() {
        MapViewport vp = new MapViewport();
        assertFalse(CRS.equalsIgnoreMetadata(
                WORLD_1_1.getCoordinateReferenceSystem(), 
                vp.getCoordinateReferenceSystem()));
        
        vp.setBounds(WORLD_1_1);
        assertTrue(CRS.equalsIgnoreMetadata(
                WORLD_1_1.getCoordinateReferenceSystem(), 
                vp.getCoordinateReferenceSystem()));
        
    }
    
    @Test
    public void boundsChangeWithScreenArea() {
        MapViewport vp = new MapViewport(true);
        vp.setScreenArea(SCREEN_1_1);
        vp.setBounds(WORLD_1_1);
        
        vp.setScreenArea(SCREEN_2_1);
        
        ReferencedEnvelope expectedBounds = new ReferencedEnvelope(
                WORLD_1_1.getMinX(), WORLD_1_1.getMaxX() + WORLD_1_1.getWidth(),
                WORLD_1_1.getMinY(), WORLD_1_1.getMaxY(),
                WORLD_1_1.getCoordinateReferenceSystem());
        
        assertTrue( expectedBounds.boundsEquals2D(vp.getBounds(), TOL) );
    }
    
    @Test
    public void newBoundsAreHonoured_NoAspectMatching() {
        MapViewport vp = new MapViewport(false);
        vp.setScreenArea(SCREEN_1_1);
        vp.setBounds(WORLD_1_1);
        
        vp.setBounds(BIG_WORLD_1_1);
        assertTrue( BIG_WORLD_1_1.boundsEquals2D(vp.getBounds(), TOL) );
    }
    
    @Test
    public void newBoundsAreHonoured_AspectMatching() {
        MapViewport vp = new MapViewport(true);
        vp.setScreenArea(SCREEN_1_1);
        vp.setBounds(WORLD_1_1);
        
        vp.setBounds(BIG_WORLD_1_1);
        assertTrue( BIG_WORLD_1_1.boundsEquals2D(vp.getBounds(), TOL) );
    }
    
    @Test
    public void boundsCtorSetsNullWorldToScreen() {
        MapViewport vp = new MapViewport(WORLD_1_1);
        assertNull( vp.getWorldToScreen() );
    }

    @Test
    public void boundsCtorSetNullScreenToWorld() {
        MapViewport vp = new MapViewport(WORLD_1_1);
        assertNull( vp.getScreenToWorld() );
    }
    
    @Test
    public void noArgCtorThenSetBoundsGivesNullWorldToScreen() {
        MapViewport vp = new MapViewport();
        vp.setBounds(WORLD_1_1);
        assertNull( vp.getWorldToScreen() );
    }
    
    @Test
    public void noArgCtorThenSetBoundsGivesNullScreenToWorld() {
        MapViewport vp = new MapViewport();
        vp.setBounds(WORLD_1_1);
        assertNull( vp.getWorldToScreen() );
    }
    
    @Test
    public void newViewportIsEditableByDefault() {
        MapViewport vp = new MapViewport();
        assertTrue( vp.isEditable() );
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
        logHandler.flush();
        String s = logStream.toString();
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
        logHandler.flush();
        String s = logStream.toString();
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
        logHandler.flush();
        String s = logStream.toString();
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
        logHandler.flush();
        String s = logStream.toString();
        assertTrue(s.contains("Ignored call to setMatchingAspectRatio"));
        assertEquals(original, vp.isMatchingAspectRatio());
        
        releaseLogger();
    }
    
    private void grabLogger() {
        logStream = new ByteArrayOutputStream();
        logHandler = new StreamHandler(logStream, new SimpleFormatter());
        logHandler.setLevel(Level.ALL);
        LOGGER.addHandler(logHandler);
        LOGGER.setUseParentHandlers(false);
    }
    
    private void releaseLogger() {
        if (logHandler != null) {
            LOGGER.removeHandler(logHandler);
            LOGGER.setUseParentHandlers(true);
        }
    }
}
