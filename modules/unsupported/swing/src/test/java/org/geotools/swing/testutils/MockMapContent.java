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

import java.io.IOException;

import java.util.Collections;
import java.util.List;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.geotools.map.event.MapBoundsListener;
import org.geotools.map.event.MapLayerListListener;

import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Mock MapContent class for testing.
 *
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $URL$
 */
public class MockMapContent extends MapContent {
    
    private Layer layer;
    private ReferencedEnvelope bounds;

    public MockMapContent() {
    }
    
    /**
     *  Overridden to avoid spurious log messages about memory leaks.
     */
    @Override
    protected void finalize() throws Throwable {
        // does nothing
    }

    @Override
    public boolean addLayer(Layer layer) {
        this.layer = layer;
        this.bounds = layer.getBounds();
        
        return true;
    }

    @Override
    public void addMapBoundsListener(MapBoundsListener listener) {
        super.addMapBoundsListener(listener);
    }

    @Override
    public List<Layer> layers() {
        if (layer == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(layer);
    }

    @Override
    public void moveLayer(int sourcePosition, int destPosition) {
        throw new UnsupportedOperationException("should not be called");
    }

    @Override
    public boolean removeLayer(Layer layer) {
        throw new UnsupportedOperationException("should not be called");
    }

    @Override
    public void removeMapBoundsListener(MapBoundsListener listener) {
        throw new UnsupportedOperationException("should not be called");
    }

    @Override
    public void removeMapLayerListListener(MapLayerListListener listener) {
        throw new UnsupportedOperationException("should not be called");
    }

    @Override
    public synchronized void setViewport(MapViewport viewport) {
        throw new UnsupportedOperationException("should not be called");
    }

    @Override
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        if (bounds == null) {
            return null;
        }
        return bounds.getCoordinateReferenceSystem();
    }

    @Override
    public ReferencedEnvelope getMaxBounds() {
        if (bounds == null) {
            return new ReferencedEnvelope();
        }
        return new ReferencedEnvelope(bounds);
    }
    
    
}
