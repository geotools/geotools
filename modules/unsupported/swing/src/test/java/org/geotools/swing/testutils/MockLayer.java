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

package org.geotools.swing.testutils;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.Layer;

/**
 * Mock Layer class for testing.
 *
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $URL$
 */
public class MockLayer extends Layer {
    
    private ReferencedEnvelope bounds;
    
    public MockLayer() {
        this(null);
    }

    public MockLayer(ReferencedEnvelope bounds) {
        this.bounds = bounds == null ? new ReferencedEnvelope() : new ReferencedEnvelope(bounds);
    }
    
    @Override
    public ReferencedEnvelope getBounds() {
        return bounds;
    }

}
