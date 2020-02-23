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

import org.geotools.map.Layer;
import org.geotools.map.MapContent;

/**
 * Used by {@linkplain DefaultRenderingExecutor} to hold a single {@code Layer} that is being passed
 * to a renderer. Calling the {@code dispose} method of this class does not dispose of the layer
 * unlike {@linkplain MapContent#dispose()}. It does not permit subsequent changes to its layer
 * list.
 *
 * @author Michael Bedward
 * @since 8.0
 * @version $Id$
 */
public class SingleLayerMapContent extends MapContent {

    /**
     * Creates a new instance to hold the given layer.
     *
     * @param layer the layer
     * @throws IllegalArgumentException if {@code layer} is {@code null}
     */
    public SingleLayerMapContent(Layer layer) {
        if (layer == null) {
            throw new IllegalArgumentException("layer must not be null");
        }
        super.addLayer(layer);
    }

    /** Throws an {@code UnsupportedOperationException} if called. */
    @Override
    public boolean addLayer(Layer layer) {
        throw new UnsupportedOperationException("Should not be called");
    }

    /** Throws an {@code UnsupportedOperationException} if called. */
    @Override
    public void moveLayer(int sourcePosition, int destPosition) {
        throw new UnsupportedOperationException("Should not be called");
    }

    /** Throws an {@code UnsupportedOperationException} if called. */
    @Override
    public boolean removeLayer(Layer layer) {
        throw new UnsupportedOperationException("Should not be called");
    }

    /** Does nothing. */
    @Override
    public void dispose() {
        // does nothing
    }

    /** Does nothing. */
    @Override
    protected void finalize() throws Throwable {
        // does nothing
    }
}
