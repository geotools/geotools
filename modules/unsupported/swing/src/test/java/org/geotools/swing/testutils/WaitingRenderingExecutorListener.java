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

import org.geotools.swing.RenderingExecutorEvent;
import org.geotools.swing.RenderingExecutorListener;


/**
 * A RenderingExecutorListener that can be set to expect specified events
 * and test if they are received.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class WaitingRenderingExecutorListener implements RenderingExecutorListener {

    public static enum EventType {
        STARTED,
        COMPLETED,
        CANCELLED,
        FAILED;
    }
    
    private static final int NTYPES = EventType.values().length;
    private CountDownLatch[] latches;
    private AtomicBoolean[] flags;

    public WaitingRenderingExecutorListener() {
        latches = new CountDownLatch[NTYPES];
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
    public void setExpected(EventType type) {
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
    public boolean await(EventType type, long timeoutMillis) {
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

    public void onRenderingStarted(RenderingExecutorEvent ev) {
        catchEvent(EventType.STARTED.ordinal());
    }

    @Override
    public void onRenderingCompleted(RenderingExecutorEvent ev) {
        catchEvent(EventType.COMPLETED.ordinal());
    }

    @Override
    public void onRenderingCancelled(RenderingExecutorEvent ev) {
        catchEvent(EventType.CANCELLED.ordinal());
    }

    @Override
    public void onRenderingFailed(RenderingExecutorEvent ev) {
        catchEvent(EventType.FAILED.ordinal());
    }
    
    /**
     * Checks if an event of the specified type has been received.
     * 
     * @param type event type
     * 
     * @return {@code true} if an event of this type has been received
     */
    public boolean eventReceived(EventType type) {
        boolean b = flags[type.ordinal()].get();
        return b;
    }

    private void catchEvent(int k) {
        flags[k].set(true);
        
        CountDownLatch latch = latches[k];
        if (latch != null) {
            latch.countDown();
        }
    }

}
