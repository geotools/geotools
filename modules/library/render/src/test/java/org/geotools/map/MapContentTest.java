/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Tests to ensure the consistency of MapContent and MapViewport functionality.
 * 
 * @author Jody Garnett
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class MapContentTest {
    
    // timeout period for waiting listener (see end of class)
    private static final long LISTENER_TIMEOUT = 500;
    
    private static final ReferencedEnvelope WORLD_ENV =
            new ReferencedEnvelope(149, 153, -32, -36, DefaultGeographicCRS.WGS84);
    
    private static final ReferencedEnvelope SUB_ENV =
            new ReferencedEnvelope(150, 152, -33, -35, DefaultGeographicCRS.WGS84);
    
    private static final double TOL = 1.0e-6;

    static class MockLayer extends Layer {
        ReferencedEnvelope bounds;
        
        MockLayer(ReferencedEnvelope bounds) {
            if (bounds != null) {
                this.bounds = new ReferencedEnvelope(bounds);
            }
        }

        @Override
        public ReferencedEnvelope getBounds() {
            return bounds;
        }
        
        @Override
        public void dispose() {
            preDispose();
            bounds = null;
            super.dispose();
        }
    }
    
    /**
     * Test DefaultMapContext handles layers that return null bounds.
     */
    @Test
    public void testNPELayerBounds() {
        Layer mapLayerBoundsNull = new MockLayer(null);
        MapContent map = new MapContent();
        map.addLayer(mapLayerBoundsNull);
                
        ReferencedEnvelope maxBounds = map.getMaxBounds();
        assertNotNull(maxBounds);
        assertTrue( maxBounds.isEmpty() );
    }
    
    /**
     * Calling {@link MapContent#getViewport()} initially creates a 
     * new viewport instance with default settings.
     */
    @Test
    public void getDefaultViewport() throws Exception {
        MapContent map = new MapContent();
        map.addLayer(new MockLayer(WORLD_ENV));
        MapViewport viewport = map.getViewport();
        
        assertNotNull(viewport);
        assertTrue(WORLD_ENV.boundsEquals2D(viewport.getBounds(), TOL));
        
        map.dispose();
    }
    
    @Test
    public void setNewViewportAndCheckBounds() {
        MapContent map = new MapContent();
        map.addLayer(new MockLayer(WORLD_ENV));
        
        MapViewport newViewport = new MapViewport();
        newViewport.setBounds(SUB_ENV);
        map.setViewport(newViewport);
        
        ReferencedEnvelope bounds = map.getBounds();
        assertTrue(SUB_ENV.boundsEquals2D(bounds, TOL));
    }

    @Test
    public void addLayerAndGetEvent() {
        MapContent map = new MapContent();
        WaitingMapListener listener = new WaitingMapListener();
        map.addMapLayerListListener(listener);
        
        listener.setExpected(WaitingMapListener.Type.ADDED);
        map.addLayer(new MockLayer(WORLD_ENV));
        assertTrue(listener.await(WaitingMapListener.Type.ADDED, LISTENER_TIMEOUT));
    }
    
    @Test
    public void removeLayerAndGetEvent() {
        MapContent map = new MapContent();
        Layer layer = new MockLayer(WORLD_ENV);
        map.addLayer(layer);
        
        WaitingMapListener listener = new WaitingMapListener();
        map.addMapLayerListListener(listener);
        listener.setExpected(WaitingMapListener.Type.REMOVED);

        map.removeLayer(layer);
        assertTrue(listener.await(WaitingMapListener.Type.REMOVED, LISTENER_TIMEOUT));
    }
    
    @Test
    public void moveLayerAndGetEvent() {
        MapContent map = new MapContent();
        Layer layer0 = new MockLayer(WORLD_ENV);
        Layer layer1 = new MockLayer(WORLD_ENV);
        map.addLayer(layer0);
        map.addLayer(layer1);
        
        WaitingMapListener listener = new WaitingMapListener();
        map.addMapLayerListListener(listener);
        listener.setExpected(WaitingMapListener.Type.MOVED, 2);
        
        map.moveLayer(0, 1);
        assertTrue(listener.await(WaitingMapListener.Type.MOVED, LISTENER_TIMEOUT));
    }
    
    @Test
    public void disposeMapContentAndGetLayerPreDisposeEvent() {
        MapContent map = new MapContent();
        Layer layer = new MockLayer(WORLD_ENV);
        map.addLayer(layer);
        
        WaitingMapListener listener = new WaitingMapListener();
        map.addMapLayerListListener(listener);
        listener.setExpected(WaitingMapListener.Type.PRE_DISPOSE);
        
        map.dispose();
        assertTrue(listener.await(WaitingMapListener.Type.PRE_DISPOSE, LISTENER_TIMEOUT));
    }
    
    @Test
    public void crsIsAutoSetFromFirstLayerWithNonNullCRS() {
        MapContent map = new MapContent();
        CoordinateReferenceSystem startCRS = map.getCoordinateReferenceSystem();
        
        ReferencedEnvelope envNoCRS = new ReferencedEnvelope(WORLD_ENV, null);
        Layer layerNoCRS = new MockLayer(envNoCRS);
        map.addLayer(layerNoCRS);
        assertEquals(startCRS, map.getCoordinateReferenceSystem());
        
        Layer layerWithCRS = new MockLayer(WORLD_ENV);
        map.addLayer(layerWithCRS);
        assertEquals(WORLD_ENV.getCoordinateReferenceSystem(), map.getCoordinateReferenceSystem());
    }
    
    @Test
    public void crsIsNotAutoSetIfViewportIsNotEditable() {
        MapContent map = new MapContent();
        MapViewport viewport = new MapViewport();
        viewport.setEditable(false);
        map.setViewport(viewport);
        
        Layer layerWithCRS = new MockLayer(WORLD_ENV);
        map.addLayer(layerWithCRS);

        assertEquals(MapViewport.DEFAULT_CRS, map.getCoordinateReferenceSystem());

        assertFalse(WORLD_ENV.getCoordinateReferenceSystem().equals(
                map.getCoordinateReferenceSystem()));
    }
    
    @Test
    public void crsIsNotAutoSetIfViewportHasExplicitCRS() {
        MapContent map = new MapContent();
        MapViewport viewport = new MapViewport();
        
        // Explicitly set the CRS. Even though it is the default viewport CRS
        // it will be treated as an explicit (user-set) value
        viewport.setCoordinateReferenceSystem(MapViewport.DEFAULT_CRS);

        map.setViewport(viewport);
        
        Layer layerWithCRS = new MockLayer(WORLD_ENV);
        map.addLayer(layerWithCRS);

        assertTrue(viewport.isEditable());
        
        assertEquals(MapViewport.DEFAULT_CRS, map.getCoordinateReferenceSystem());

        assertFalse(WORLD_ENV.getCoordinateReferenceSystem().equals(
                map.getCoordinateReferenceSystem()));
    }
    
    @Test
    public void crsIsAutoSetWhenGetViewportCalledBeforeAddingLayers() {
        MapContent map = new MapContent();
        
        // Call getViewport to force creation of a default viewport
        MapViewport vp = map.getViewport();
        assertEquals(MapViewport.DEFAULT_CRS, vp.getCoordinateReferenceSystem());
        
        CoordinateReferenceSystem startCRS = map.getCoordinateReferenceSystem();
        
        ReferencedEnvelope envNoCRS = new ReferencedEnvelope(WORLD_ENV, null);
        Layer layerNoCRS = new MockLayer(envNoCRS);
        map.addLayer(layerNoCRS);
        assertEquals(startCRS, map.getCoordinateReferenceSystem());
        
        Layer layerWithCRS = new MockLayer(WORLD_ENV);
        map.addLayer(layerWithCRS);
        assertEquals(WORLD_ENV.getCoordinateReferenceSystem(), vp.getCoordinateReferenceSystem());
    }
}