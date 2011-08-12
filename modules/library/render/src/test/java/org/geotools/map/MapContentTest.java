package org.geotools.map;

import java.io.IOException;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests to ensure the consistency of MapContent and MapViewport functionality.
 * @author Jody Garnett
 */
public class MapContentTest {
    
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
    }
    
    @Test
    public void testDispose() {
        MapContent map = new MapContent();
        map.dispose();

        map = new MapContent(DefaultGeographicCRS.WGS84);
        map.dispose();
    }

    /**
     * Test DefaultMapContext handles layers that return null bounds.
     */
    @Test
    public void testNPELayerBounds() throws IOException {
        Layer mapLayerBoundsNull = new MockLayer(null);
        MapContent map = new MapContent(DefaultGeographicCRS.WGS84);
        map.addLayer(mapLayerBoundsNull);
                
        ReferencedEnvelope maxBounds = map.getMaxBounds();
        assertNotNull(maxBounds);
        assertEquals( "wgs84", DefaultGeographicCRS.WGS84, maxBounds.getCoordinateReferenceSystem() );
        assertTrue( maxBounds.isEmpty() );
        map.dispose();
        
        map = new MapContent();
        map.addLayer(mapLayerBoundsNull);
        
        maxBounds = map.getMaxBounds();
        assertNull(maxBounds);
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

}