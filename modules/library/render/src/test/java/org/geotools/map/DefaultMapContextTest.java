package org.geotools.map;

import java.io.IOException;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultMapContextTest {
    @Test
    public void testDispose() {
        DefaultMapContext mapContext = new DefaultMapContext();
        mapContext.dispose();

        mapContext = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mapContext.dispose();

    }

    /**
     * Test DefaultMapContext handles layers that return null bounds.
     */
    @Test
    public void testNPELayerBounds() throws IOException {

        MapLayer mapLayerBoundsNull = new MapLayer(new Layer() {
            public ReferencedEnvelope getBounds() {
                return null;
            }
        });
        DefaultMapContext mapContext = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mapContext.addLayer(mapLayerBoundsNull);
        ReferencedEnvelope layerBounds = mapContext.getLayerBounds();
        assertNull(layerBounds);
        
        ReferencedEnvelope maxBounds = mapContext.getMaxBounds();
        assertNotNull(maxBounds);
        assertEquals( DefaultGeographicCRS.WGS84, maxBounds.getCoordinateReferenceSystem() );
        assertTrue( maxBounds.isEmpty() );
        
    }
}
