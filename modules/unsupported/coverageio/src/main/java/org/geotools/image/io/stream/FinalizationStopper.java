/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.io.stream;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Stops execution of {@link System#runFinalization} when a timeout is reach. This timer is
 * used in order to prevent system freeze that occurs sometime when waiting for finalization
 * in {@link UrlInputSpi#createInputStreamInstance}. If a {@code finalize()} method is blocked
 * in an I/O operation upon an interruptible channel, the channel will be closed and the finalize
 * method will receive a {@link java.nio.channels.ClosedByInterruptException}.
 * See {@link Thread#interrupt} javadoc for more details.
 * <p>
 * Most of the time, interruptions are never performed.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
final class FinalizationStopper extends TimerTask {
    /**
     * Timer for stopping execution of {@link System#runFinalization}.
     */
    private static final Timer TIMER = new Timer("Finalization stopper", true);

    /**
     * The processus to stop.
     */
    private final Thread toStop;

    /**
     * {@code true} if the thread has been interrupted at least once.
     */
    volatile boolean interrupted;

    /**
     * Stops a new task which will stops current thread after the specified timeout. The
     * interruption will be repeated until {@link UrlInputSpi#createInputStreamInstance}
     * stop them.
     */
    FinalizationStopper(final long timeout) {
        this.toStop = Thread.currentThread();
        TIMER.schedule(this, timeout, timeout);
    }

    /**
     * Stops the thread.
     */
    public void run() {
        interrupted = true;
        toStop.interrupt();
    }
}
