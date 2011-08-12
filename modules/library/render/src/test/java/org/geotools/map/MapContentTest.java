package org.geotools.map;

import java.util.concurrent.CountDownLatch;
import java.io.IOException;

import java.util.concurrent.TimeUnit;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.event.MapLayerListEvent;
import org.geotools.map.event.MapLayerListListener;
import org.geotools.referencing.crs.DefaultGeographicCRS;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests to ensure the consistency of MapContent and MapViewport functionality.
 * @author Jody Garnett
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
        WaitingListener listener = new WaitingListener();
        map.addMapLayerListListener(listener);
        
        listener.setExpected(WaitingListener.Type.ADDED);
        map.addLayer(new MockLayer(WORLD_ENV));
        assertTrue(listener.await(WaitingListener.Type.ADDED, LISTENER_TIMEOUT));
    }
    
    @Test
    public void removeLayerAndGetEvent() {
        MapContent map = new MapContent();
        Layer layer = new MockLayer(WORLD_ENV);
        map.addLayer(layer);
        
        WaitingListener listener = new WaitingListener();
        map.addMapLayerListListener(listener);
        listener.setExpected(WaitingListener.Type.REMOVED);

        map.removeLayer(layer);
        assertTrue(listener.await(WaitingListener.Type.REMOVED, LISTENER_TIMEOUT));
    }
    
    @Test
    public void moveLayerAndGetEvent() {
        MapContent map = new MapContent();
        Layer layer0 = new MockLayer(WORLD_ENV);
        Layer layer1 = new MockLayer(WORLD_ENV);
        map.addLayer(layer0);
        map.addLayer(layer1);
        
        WaitingListener listener = new WaitingListener();
        map.addMapLayerListListener(listener);
        listener.setExpected(WaitingListener.Type.MOVED, 2);
        
        map.moveLayer(0, 1);
        assertTrue(listener.await(WaitingListener.Type.MOVED, LISTENER_TIMEOUT));
    }
    
    private static class WaitingListener implements MapLayerListListener {
        static enum Type {
            ADDED,
            REMOVED,
            CHANGED,
            MOVED;
        }
        
        private final static int N = Type.values().length;
        CountDownLatch[] latches = new CountDownLatch[N];
        
        void setExpected(Type type) {
            setExpected(type, 1);
        }
        
        void setExpected(Type type, int count) {
            latches[type.ordinal()] = new CountDownLatch(count);
        }
        
        boolean await(Type type, long timeoutMillis) {
            int index = type.ordinal();
            if (latches[index] == null) {
                throw new IllegalStateException("Event type not expected: " + type);
            }
            
            boolean result = false;
            try {
                result = latches[index].await(timeoutMillis, TimeUnit.MILLISECONDS);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            
            return result;
        }

        @Override
        public void layerAdded(MapLayerListEvent event) {
            catchEvent(Type.ADDED);
        }

        @Override
        public void layerRemoved(MapLayerListEvent event) {
            catchEvent(Type.REMOVED);
        }

        @Override
        public void layerChanged(MapLayerListEvent event) {
            catchEvent(Type.CHANGED);
        }

        @Override
        public void layerMoved(MapLayerListEvent event) {
            catchEvent(Type.MOVED);
        }
        
        private void catchEvent(Type type) {
            int index = type.ordinal();
            if (latches[index] == null) {
                throw new IllegalStateException("Event type not expected: " + type);
            }
            latches[index].countDown();
        }
        
   }
}