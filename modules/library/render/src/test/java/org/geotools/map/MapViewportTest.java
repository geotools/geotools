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

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the viewport class.
 * 
 * @author Michael Bedward
 * @source $URL$
 */
public class MapViewportTest {
    
    // World bounds with aspect ratio 1:1
    private static final ReferencedEnvelope WORLD_1_1 = new ReferencedEnvelope(
            150, 152, -33, -35, DefaultGeographicCRS.WGS84);

    // Screen area with aspect ratio 1:1
    private static final Rectangle SCREEN_1_1 = new Rectangle(100, 100);
    // Screen area with aspect ratio 2:1
    private static final Rectangle SCREEN_2_1 = new Rectangle(200, 100);
    // Screen area with aspect ratio 1:2
    private static final Rectangle SCREEN_1_2 = new Rectangle(100, 200);
    
    private static final double TOL = 1.0e-6d;
    
    @Test
    public void defaultCtor() {
        MapViewport vp = new MapViewport();
        assertTrue(vp.isEmpty());
        assertFalse(vp.isMatchingAspectRatio());
        assertTrue(vp.getBounds().isEmpty());
        assertTrue(vp.getScreenArea().isEmpty());
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
    public void boundsChangeWithScreenArea() {
        MapViewport vp = new MapViewport(true);
        vp.setScreenArea(SCREEN_1_1);
        vp.setBounds(WORLD_1_1);
        
        vp.setScreenArea(SCREEN_2_1);
        
        ReferencedEnvelope expectedBounds = new ReferencedEnvelope(
                WORLD_1_1.getMinX(), WORLD_1_1.getMaxX() + WORLD_1_1.getWidth(),
                WORLD_1_1.getMinY(), WORLD_1_1.getMaxY(),
                WORLD_1_1.getCoordinateReferenceSystem());
        
        assertTrue( expectedBounds.boundsEquals2D(vp.getBounds(), TOL));
    }
    
    @Test
    public void getWorldToScreenTransformReturnsNullWhenNotSet() {
        MapViewport vp = new MapViewport(WORLD_1_1);
        assertNull( vp.getWorldToScreen() );
    }

    @Test
    public void getScreenToWorldTransformReturnsNullWhenNotSet() {
        MapViewport vp = new MapViewport(WORLD_1_1);
        assertNull( vp.getScreenToWorld() );
    }
}
