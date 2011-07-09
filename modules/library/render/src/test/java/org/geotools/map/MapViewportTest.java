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
        ReferencedEnvelope refEnv = new ReferencedEnvelope(
                150, 152, -33, -35, DefaultGeographicCRS.WGS84);
        
        MapViewport vp = new MapViewport(refEnv);

        assertTrue(vp.isEmpty());
        assertFalse(vp.getCorrectAspectRatio());
        assertTrue(vp.getScreenArea().isEmpty());
        
        assertTrue( refEnv.boundsEquals2D(vp.getBounds(), TOL) );
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
        ReferencedEnvelope refEnv = new ReferencedEnvelope(
                150, 152, -33, -35, DefaultGeographicCRS.WGS84);

        MapViewport vp = new MapViewport(refEnv, true);
        assertTrue(vp.isEmpty());
        assertTrue(vp.getCorrectAspectRatio());
        assertTrue(vp.getScreenArea().isEmpty());
        
        assertTrue( refEnv.boundsEquals2D(vp.getBounds(), TOL) );
    }
    
    
}
