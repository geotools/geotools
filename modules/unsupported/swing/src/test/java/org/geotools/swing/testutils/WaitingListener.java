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

import java.util.EventObject;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * An event listener that can be set to expect specified types of events
 * and test if they are received.
 * 
 * @param <E> the Enum type associated with the event class
 * @param <T> type of {@code EventObject}
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/swing/src/test/java/org/geotools/swing/testutils/WaitingMapPaneListener.java $
 * @version $Id: WaitingMapPaneListener.java 37699 2011-07-22 06:20:07Z mbedward $
 */
public abstract class WaitingListener<T extends EventObject, E extends Enum> {
    protected final int NTYPES;

    protected CountDownLatch[] latches;
    protected AtomicBoolean[] flags;
    protected EventObject[] events;
    
    public WaitingListener(int numEventTypes) {
        NTYPES = numEventTypes;
        latches = new CountDownLatch[NTYPES];
        events = new EventObject[NTYPES];
        
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
    public void setExpected(E type) {
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
    public boolean await(E type, long timeoutMillis) {
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

    /**
     * Checks if an event of the specified type has been received.
     * 
     * @param type event type
     * 
     * @return {@code true} if an event of this type has been received
     */
    public boolean eventReceived(E type) { // MapPaneEvent.Type type) {
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
    public T getEvent(E type) {
        return (T) events[type.ordinal()];
    }

    protected void catchEvent(int k, T event) {
        flags[k].set(true);
        events[k] = event;
        
        CountDownLatch latch = latches[k];
        if (latch != null) {
            latch.countDown();
        }
        
    }

}
