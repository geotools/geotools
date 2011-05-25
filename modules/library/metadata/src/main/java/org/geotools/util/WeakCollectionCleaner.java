/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

import org.geotools.util.logging.Logging;


/**
 * A thread invoking {@link Reference#clear} on each enqueded reference.
 * This is usefull only if {@code Reference} subclasses has overridden
 * their {@code clear()} method in order to perform some cleaning.
 * This thread is used by {@link WeakHashSet} and {@link WeakValueHashMap},
 * which remove their entry from the collection when {@link Reference#clear}
 * is invoked.
 *
 * @since 2.0
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class WeakCollectionCleaner extends Thread {
    /**
     * The default thread.
     */
    public static final WeakCollectionCleaner DEFAULT = new WeakCollectionCleaner();

    /**
     * List of reference collected by the garbage collector.
     * Those elements must be removed from {@link #table}.
     */
    ReferenceQueue<Object> referenceQueue = new ReferenceQueue<Object>();
    
    /**
     * Constructs and starts a new thread as a daemon. This thread will be sleeping
     * most of the time.  It will run only some few nanoseconds each time a new
     * {@link Reference} is enqueded.
     */
    private WeakCollectionCleaner() {
        super("WeakCollectionCleaner");
        setPriority(MAX_PRIORITY - 2);
        setDaemon(true);
        start();
    }
    
    public synchronized ReferenceQueue<Object> getReferenceQueue() {
        return referenceQueue;
    }

    /**
     * Loop to be run during the virtual machine lifetime.
     */
    @Override
    public void run() {
        ReferenceQueue<Object> rq;
        while ((rq = getReferenceQueue ()) != null) {
            try {
                // Block until a reference is enqueded.
                final Reference ref = rq.remove();
                if (ref == null) {
                    /*
                     * Should never happen according Sun's Javadoc ("Removes the next reference
                     * object in this queue, blocking until one becomes available."). However a
                     * null reference seems to be returned during JVM shutdown on Linux. Wait a
                     * few seconds in order to give the JVM a chance to kill this daemon thread
                     * before the logging at the sever level, and stop the loop.  We do not try
                     * to resume the loop since something is apparently going wrong and we want
                     * the user to be notified. See GEOT-1138.
                     */
                    sleep(15 * 1000L);
                    break;
                }
                ref.clear();
                // Note: To be usefull, the clear() method must have been overridden in Reference
                //       subclasses. This is what WeakHashSet.Entry and WeakHashMap.Entry do.
            } catch (InterruptedException exception) {
                // Somebody doesn't want to lets us sleep... Go back to work.
            } catch (Exception exception) {
                Logging.unexpectedException(WeakCollectionCleaner.class, "remove", exception);
            } catch (AssertionError exception) {
                Logging.unexpectedException(WeakCollectionCleaner.class, "remove", exception);
                // Do not kill the thread on assertion failure, in order to
                // keep the same behaviour as if assertions were turned off.
            }
        }
        Logging.getLogger(WeakCollectionCleaner.class).info("Weak collection cleaner stopped");
    }
    
    /**
     * Stops the cleaner thread. Calling this method is recommended in all long running applications
     * with custom class loaders (e.g., web applications).
     */
    public void exit() {
        // try to stop it gracefully
        synchronized (this) {
            referenceQueue = null;
        }
        this.interrupt();
        try {
            this.join(500);
        } catch (InterruptedException e) {

        }
        // last resort tentative to kill the cleaner thread
        if (this.isAlive())
            this.stop();
    }
}
