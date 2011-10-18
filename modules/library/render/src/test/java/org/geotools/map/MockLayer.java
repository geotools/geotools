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

/**
 * Mock layer class for testing MapContent and friends.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL: $
 * @version $Id: $
 */
public class MockLayer extends Layer {

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
