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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.geotools.swing.event.MapPaneEvent;
import org.geotools.swing.event.MapPaneListener;


/**
 * A RenderingExecutorListener that can be set to expect specified events
 * and test if they are received.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class WaitingMapPaneListener implements MapPaneListener {
    private static final int NTYPES = MapPaneEvent.Type.values().length;
    private CountDownLatch[] latches;
    private AtomicBoolean[] flags;

    public WaitingMapPaneListener() {
        latches = new CountDownLatch[NTYPES];
        flags = new AtomicBoolean[NTYPES];
        for (int i = 0; i < NTYPES; i++) {
            flags[i] = new AtomicBoolean(false);
        }
    }
    
    public void setExpected(MapPaneEvent.Type type) {
        latches[type.ordinal()] = new CountDownLatch(1);
    }

    public boolean await(MapPaneEvent.Type type, long timeoutMillis) {
        CountDownLatch latch = latches[type.ordinal()];
        if (latch == null) {
            throw new IllegalStateException("latch not set for " + type);
        }

        boolean result = false;
        try {
            result = latch.await(timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            // do nothing
        } finally {
            return result;
        }
    }

    public void onNewContent(MapPaneEvent ev) {
        catchEvent(MapPaneEvent.Type.NEW_MAPCONTENT.ordinal());
    }

    public void onNewRenderer(MapPaneEvent ev) {
        catchEvent(MapPaneEvent.Type.NEW_RENDERER.ordinal());
    }

    public void onResized(MapPaneEvent ev) {
        catchEvent(MapPaneEvent.Type.PANE_RESIZED.ordinal());
    }

    public void onDisplayAreaChanged(MapPaneEvent ev) {
        catchEvent(MapPaneEvent.Type.DISPLAY_AREA_CHANGED.ordinal());
    }

    public void onRenderingStarted(MapPaneEvent ev) {
        catchEvent(MapPaneEvent.Type.RENDERING_STARTED.ordinal());
    }

    public void onRenderingStopped(MapPaneEvent ev) {
        catchEvent(MapPaneEvent.Type.RENDERING_STOPPED.ordinal());
    }
    
    public boolean gotEvent(MapPaneEvent.Type type) {
        return flags[type.ordinal()].get();
    }

    private void catchEvent(int k) {
        flags[k].set(true);
        
        CountDownLatch latch = latches[k];
        if (latch != null) {
            latch.countDown();
        }
    }

}
