package org.geotools.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;

/**
 * Tests to ensure the consistency of MapContent and MapViewport functionality.
 * @author Jody Garnett
 */
public class MapContentTest {
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
        Layer mapLayerBoundsNull = new Layer() {
            public ReferencedEnvelope getBounds() {
                return null;
            }
        };
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
}