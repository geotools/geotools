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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author michael
 */
public class WaitingListener implements RenderingExecutorListener {

    public static enum EventType {
        STARTED,
        COMPLETED,
        CANCELLED,
        FAILED;
    }
    
    private CountDownLatch[] latches = new CountDownLatch[EventType.values().length];

    public void setExpected(EventType type) {
        latches[type.ordinal()] = new CountDownLatch(1);
    }

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
        CountDownLatch latch = latches[EventType.STARTED.ordinal()];
        if (latch != null) {
            latch.countDown();
        }
    }

    @Override
    public void onRenderingCompleted(RenderingExecutorEvent ev) {
        CountDownLatch latch = latches[EventType.COMPLETED.ordinal()];
        if (latch != null) {
            latch.countDown();
        }
    }

    @Override
    public void onRenderingCancelled(RenderingExecutorEvent ev) {
        CountDownLatch latch = latches[EventType.CANCELLED.ordinal()];
        if (latch != null) {
            latch.countDown();
        }
    }

    @Override
    public void onRenderingFailed(RenderingExecutorEvent ev) {
        CountDownLatch latch = latches[EventType.FAILED.ordinal()];
        if (latch != null) {
            latch.countDown();
        }
    }
}
