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
import org.geotools.swing.event.MapPaneEvent.Type;
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
    private MapPaneEvent[] events;

    public WaitingMapPaneListener() {
        latches = new CountDownLatch[NTYPES];
        events = new MapPaneEvent[NTYPES];
        
        flags = new AtomicBoolean[NTYPES];
        for (int i = 0; i < NTYPES; i++) {
            flags[i] = new AtomicBoolean(false);
        }
    }
    
    /**
     * Sets the listener to expect an event of the specified type.
     * 
     * @param type event type
     */
    public void setExpected(MapPaneEvent.Type type) {
        latches[type.ordinal()] = new CountDownLatch(1);
    }

    /**
     * Waits of an event of the specified type to be received.
     * 
     * @param type event type
     * @param timeoutMillis maximum waiting time
     * 
     * @return {@code true} if the event was received
     */
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
        catchEvent(MapPaneEvent.Type.NEW_MAPCONTENT.ordinal(), ev);
    }

    public void onNewRenderer(MapPaneEvent ev) {
        catchEvent(MapPaneEvent.Type.NEW_RENDERER.ordinal(), ev);
    }

    public void onDisplayAreaChanged(MapPaneEvent ev) {
        catchEvent(MapPaneEvent.Type.DISPLAY_AREA_CHANGED.ordinal(), ev);
    }

    public void onRenderingStarted(MapPaneEvent ev) {
        catchEvent(MapPaneEvent.Type.RENDERING_STARTED.ordinal(), ev);
    }

    public void onRenderingStopped(MapPaneEvent ev) {
        catchEvent(MapPaneEvent.Type.RENDERING_STOPPED.ordinal(), ev);
    }
    
    /**
     * Checks if an event of the specified type has been received.
     * 
     * @param type event type
     * 
     * @return {@code true} if an event of this type has been received
     */
    public boolean eventReceived(MapPaneEvent.Type type) {
        return flags[type.ordinal()].get();
    }

    /**
     * Retrieves the more recent event of the specified type received
     * by this listener.
     * 
     * @param type event type
     * 
     * @return the most recent event or {@code null} if none received
     */
    public MapPaneEvent getEvent(Type type) {
        return events[type.ordinal()];
    }

    private void catchEvent(int k, MapPaneEvent event) {
        flags[k].set(true);
        events[k] = event;
        
        CountDownLatch latch = latches[k];
        if (latch != null) {
            latch.countDown();
        }
        
    }

}
