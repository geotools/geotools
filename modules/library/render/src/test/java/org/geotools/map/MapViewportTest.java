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
    
    private static final ReferencedEnvelope WORLD = new ReferencedEnvelope(
            150, 152, -33, -35, DefaultGeographicCRS.WGS84);

    private static final Rectangle SCREEN_2_1 = new Rectangle(200, 100);
    private static final Rectangle SCREEN_1_2 = new Rectangle(100, 200);
    
    private static final double TOL = 1.0e-6d;
    
    @Test
    public void defaultCtor() {
        MapViewport vp = new MapViewport();
        assertTrue(vp.isEmpty());
        assertFalse(vp.getCorrectAspectRatio());
        assertTrue(vp.getBounds().isEmpty());
        assertTrue(vp.getScreenArea().isEmpty());
    }
    
    @Test
    public void boundsCtor() {
        MapViewport vp = new MapViewport(WORLD);

        assertTrue(vp.isEmpty());
        assertFalse(vp.getCorrectAspectRatio());
        assertTrue(vp.getScreenArea().isEmpty());
        
        assertTrue( WORLD.boundsEquals2D(vp.getBounds(), TOL) );
    }
    
    @Test
    public void booleanCtor() {
        MapViewport vp = new MapViewport(true);
        assertTrue(vp.isEmpty());
        assertTrue(vp.getCorrectAspectRatio());
        assertTrue(vp.getBounds().isEmpty());
        assertTrue(vp.getScreenArea().isEmpty());
    }
    
    @Test
    public void fullCtor() {
        MapViewport vp = new MapViewport(WORLD, true);
        assertTrue(vp.isEmpty());
        assertTrue(vp.getCorrectAspectRatio());
        assertTrue(vp.getScreenArea().isEmpty());
        
        assertTrue( WORLD.boundsEquals2D(vp.getBounds(), TOL) );
    }
    
    @Test
    public void setBoundsNoAspectCorrection() {
        MapViewport vp = new MapViewport();
        vp.setScreenArea(SCREEN_2_1);
        vp.setBounds(WORLD);

        assertTrue( WORLD.boundsEquals2D(vp.getBounds(), TOL) );
    }


    @Test
    public void setBoundsWithAspectCorrection() {
        MapViewport vp = new MapViewport();
        vp.setCorrectAspectRatio(true);
        vp.setScreenArea(SCREEN_2_1);
        vp.setBounds(WORLD);
        
        double w = WORLD.getWidth();
        ReferencedEnvelope expected = new ReferencedEnvelope(
                WORLD.getMinX() - w/2, WORLD.getMaxX() + w/2,
                WORLD.getMinY(), WORLD.getMaxY(),
                WORLD.getCoordinateReferenceSystem());
        
        System.out.println(expected);
        
        ReferencedEnvelope actual = vp.getBounds();
        System.out.println(actual);

        assertTrue( expected.boundsEquals2D(actual, TOL) );
    }
}
