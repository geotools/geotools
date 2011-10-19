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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.geotools.map.event.MapBoundsEvent;
import org.geotools.map.event.MapLayerListEvent;
import org.geotools.map.event.MapAdapter;

/**
 * A listener for map bounds and layer list events which can be set to wait
 * for specific events to be received.
 *
 * @author Michael Bedward
 * @since 2.7
 * @source $URL$
 * @version $Id$
 */
class WaitingMapListener extends MapAdapter {

    static enum Type {
        ADDED, 
        REMOVED, 
        CHANGED, 
        MOVED, 
        PRE_DISPOSE,
        BOUNDS_CHANGED;
    }
    
    private static final int N = Type.values().length;
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

    @Override
    public void layerPreDispose(MapLayerListEvent event) {
        catchEvent(Type.PRE_DISPOSE);
    }

    @Override
    public void mapBoundsChanged(MapBoundsEvent event) {
        catchEvent(Type.BOUNDS_CHANGED);
    }

    private void catchEvent(Type type) {
        int index = type.ordinal();
        if (latches[index] == null) {
            throw new IllegalStateException("Event type not expected: " + type);
        }
        latches[index].countDown();
    }
    
}
