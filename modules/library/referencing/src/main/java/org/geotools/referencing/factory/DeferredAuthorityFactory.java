/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.referencing.factory;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.opengis.referencing.FactoryException;
import org.geotools.factory.Hints;
import org.geotools.factory.OptionalFactory;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Loggings;
import org.geotools.resources.i18n.LoggingKeys;


/**
 * A buffered authority factory which will defer the {@linkplain #createBackingStore creation
 * of a backing store} until when first needed. This approach allow to etablish a connection to
 * a database (for example) only when first needed. In addition, the backing store can be
 * automatically disposed after a timeout and recreated when needed again.
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @todo Extends {@link BufferedAuthorityFactory} for now in order to improve the trunk stability
 *       during GEOT-1286 development, but we may revisit that after GEOT-1286 completion.
 */
public abstract class DeferredAuthorityFactory extends BufferedAuthorityFactory
                                            implements OptionalFactory
{
    /**
     * The timer for {@linkplain AbstractAuthorityFactory#dispose disposing} backing stores.
     */
    private static Timer TIMER = new Timer("GT authority factory disposer", true);

    /**
     * The task for disposing the backing store, or {@code null} if none.
     * This task will be scheduled for repeated execution by {@link #setTimeout}.
     */
    private TimerTask disposer;

    /**
     * {@code true} if the backing store was used since the last time the timer task was run.
     * A value of {@code true} means that the task must wait again. A value of {@code false}
     * means that it can dispose the backing store.
     */
    private boolean used;

    /**
     * Constructs an instance without initial backing store. Subclasses are responsible for
     * creating an appropriate backing store when the {@link #createBackingStore} method is
     * invoked.
     *
     * @param userHints An optional set of hints, or {@code null} if none.
     * @param priority The priority for this factory, as a number between
     *        {@link #MINIMUM_PRIORITY MINIMUM_PRIORITY} and
     *        {@link #MAXIMUM_PRIORITY MAXIMUM_PRIORITY} inclusive.
     *
     * @see #createBackingStore
     *
     * @since 2.2
     */
    protected DeferredAuthorityFactory(final Hints userHints, final int priority) {
        super(priority, DEFAULT_MAX);
    }

    /**
     * Constructs an instance without initial backing store. Subclasses are responsible for
     * creating an appropriate backing store when the {@link #createBackingStore} method is
     * invoked.
     *
     * @param userHints An optional set of hints, or {@code null} if none.
     * @param priority The priority for this factory, as a number between
     *        {@link #MINIMUM_PRIORITY MINIMUM_PRIORITY} and
     *        {@link #MAXIMUM_PRIORITY MAXIMUM_PRIORITY} inclusive.
     * @param maxStrongReferences The maximum number of objects to keep by strong reference.
     *
     * @see #createBackingStore
     *
     * @since 2.2
     */
    protected DeferredAuthorityFactory(final Hints userHints,
                                       final int priority,
                                       final int maxStrongReferences)
    {
        super(priority, maxStrongReferences);
    }

    /**
     * Returns {@code true} if this factory is available. The default implementation returns
     * {@code false} if {@link #createBackingStore} throws an exception.
     */
    @Override
    public boolean isAvailable() {
        return super.isAvailable();
    }

    /**
     * Returns the backing store authority factory.
     *
     * @return The backing store to uses in {@code createXXX(...)} methods.
     * @throws FactoryException if the creation of backing store failed.
     */
    @Override
    final AbstractAuthorityFactory getBackingStore() throws FactoryException {
        if (backingStore == null) {
            synchronized (this) {
                if(backingStore == null) {
                    backingStore = createBackingStore();
                    if (backingStore == null) {
                        throw new FactoryNotFoundException(Errors.format(ErrorKeys.NO_DATA_SOURCE));
                    }
                    completeHints();
                }
            }
        }
        used = true; // Tell to the disposer to wait again.
        return backingStore;
    }

    /**
     * Creates the backing store authority factory. This method is invoked the first time a
     * {@code createXXX(...)} method is invoked.
     *
     * @return The backing store to uses in {@code createXXX(...)} methods.
     * @throws FactoryNotFoundException if the backing store has not been found.
     * @throws FactoryException if the creation of backing store failed for an other reason.
     */
    protected abstract AbstractAuthorityFactory createBackingStore() throws FactoryException;

    /**
     * Returns {@code true} if this deferred factory is connected to its backing store.
     * This method returns {@code false} if no {@code createFoo} method has been invoked,
     * if the backing store has been automatically disposed after the {@linkplain #setTimeout
     * timeout} or if this factoy has been {@linkplain #dispose disposed}.
     */
    public synchronized boolean isConnected() {
        return backingStore != null;
    }

    /**
     * Set a timer for disposing the backing store after the specified amount of milliseconds of
     * inactivity. The {@link #createBackingStore} method will be responsible for creating a new
     * backing store when needed. Note that the backing store disposal can be vetoed if
     * {@link #canDisposeBackingStore} returns {@code false}.
     *
     * @param delay The minimal delay before to close the backing store. This delay is very
     *        approximative. The backing store will not be closed before, but may take as
     *        much as twice that time before to be closed.
     */
    public synchronized void setTimeout(final long delay) {
        // if we have been disposed, don't do anything
        if(TIMER == null)
            return;
        
        if (disposer != null) {
            disposer.cancel();
        }
        disposer = new Disposer();
        TIMER.schedule(disposer, delay, delay);
    }

    /**
     * Returns {@code true} if the backing store can be disposed now. This method is invoked
     * automatically after the amount of time specified by {@link #setTimeout} if the factory
     * were not used during that time. The default implementation always returns {@code true}.
     * Subclasses should override this method and returns {@code false} if they want to prevent
     * the backing store disposal under some circonstances.
     *
     * @param backingStore The backing store in process of being disposed.
     */
    protected boolean canDisposeBackingStore(final AbstractAuthorityFactory backingStore) {
        return true;
    }

    /**
     * Releases resources immediately instead of waiting for the garbage collector. This
     * method disposes the backing store regardeless of {@link #canDisposeBackingStore} value.
     */
    @Override
    public synchronized void dispose() throws FactoryException {
        if (disposer != null) {
            disposer.cancel();
            disposer = null;
        }
        super.dispose();
    }
    
    /**
     * Gets rid of the timer thread at application shutdown
     */
    public static void exit() {
        if(TIMER != null) {
            TIMER.cancel();
            TIMER = null;
        }
    }
    
    /**
     * Disposes of the backing store
     * @throws FactoryException
     */
    protected synchronized void disposeBackingStore() {
        try {
            if(backingStore != null) {
                LOGGER.log(Level.INFO, "Disposing " + getClass() + " backing store");
                backingStore.dispose();
                backingStore = null;
            }
        } catch (FactoryException exception) {
            backingStore = null;
            final LogRecord record = Loggings.format(Level.WARNING,
                    LoggingKeys.CANT_DISPOSE_BACKING_STORE);
            record.setSourceMethodName("run");
            record.setSourceClassName(Disposer.class.getName());
            record.setThrown(exception);
            record.setLoggerName(LOGGER.getName());
            LOGGER.log(record);
        }
    }

    /**
     * The task for closing the backing store after the timeout.
     */
    private final class Disposer extends TimerTask {
        public void run() {
            synchronized (DeferredAuthorityFactory.this) {
                if (used || !canDisposeBackingStore(backingStore)) {
                    used = false;
                    return;
                }
                if (cancel()) {
                    disposer = null;
                    if (backingStore != null) { 
                        disposeBackingStore();
                    }
                    // Needed in order to lets GC do its job.
                    hints.remove(Hints.DATUM_AUTHORITY_FACTORY);
                    hints.remove(Hints.CS_AUTHORITY_FACTORY);
                    hints.remove(Hints.CRS_AUTHORITY_FACTORY);
                    hints.remove(Hints.COORDINATE_OPERATION_AUTHORITY_FACTORY);
                }
            }
        }
    }
}
