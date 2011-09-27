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

package org.geotools.swing;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.swing.testutils.MockLayer;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for SingleLayerMapContent.
 * 
 * @author Michael Bedward
 * @since 8.0
 *
 * @source $URL$
 * @version $Id$
 */
public class SingleLayerMapContentTest {
    
    private static final ReferencedEnvelope WORLD = 
            new ReferencedEnvelope(150, 152, -33, -35, DefaultGeographicCRS.WGS84);

    @Test(expected=IllegalArgumentException.class)
    public void nullArgToCtorThrowsException() {
        MapContent mc = new SingleLayerMapContent(null);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void addLayerThrowsException() {
        Layer layer = new MockLayer(WORLD);
        MapContent mc = new SingleLayerMapContent(layer);
        
        Layer layer2 = new MockLayer(WORLD);
        mc.addLayer(layer2);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void removeLayerThrowsException() {
        Layer layer = new MockLayer(WORLD);
        MapContent mc = new SingleLayerMapContent(layer);

        mc.removeLayer(layer);
    }
    
    @Test
    public void disposeDoesNotCallLayerDispose() {
        final boolean[] layerDisposed = new boolean[1];
        
        Layer layer = new MockLayer(WORLD) {
            @Override
            public void dispose() {
                layerDisposed[0] = true;
            }
        };
        
        MapContent mc = new SingleLayerMapContent(layer);
        mc.dispose();
        assertFalse(layerDisposed[0]);
    }
}
