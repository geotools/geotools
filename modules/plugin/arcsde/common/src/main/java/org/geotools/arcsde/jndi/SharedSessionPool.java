/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
 */
package org.geotools.arcsde.jndi;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import org.geotools.arcsde.logging.Loggers;
import org.geotools.arcsde.session.ArcSDEConnectionConfig;
import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.session.ISessionPool;
import org.geotools.arcsde.session.ISessionPoolFactory;
import org.geotools.arcsde.session.UnavailableConnectionException;

/**
 * A {@link ISessionPool session pool} that is not closable and hence can be shared between
 * different applications/datastores when referenced by a JNDI context.
 * 
 * 
 * @author Gabriel Roldan (OpenGeo)
 *
 * @source $URL$
 * @version $Id$
 * @since 2.5.7
 * 
 */
public final class SharedSessionPool implements ISessionPool {

    private static final Logger LOGGER = Loggers.getLogger("org.geotools.arcsde.session");

    private static final AtomicInteger instanceCounter = new AtomicInteger();

    private final int instanceNumber;

    private final ISessionPool delegate;

    private static final Map<ArcSDEConnectionConfig, SharedSessionPool> instances = Collections
            .synchronizedMap(new WeakHashMap<ArcSDEConnectionConfig, SharedSessionPool>());

    protected SharedSessionPool(final ISessionPool delegate) throws IOException {
        this.delegate = delegate;
        this.instanceNumber = instanceCounter.incrementAndGet();
    }

    public static ISessionPool getInstance(final ArcSDEConnectionConfig config,
            final ISessionPoolFactory factory) throws IOException {
        LOGGER.info("Getting shared session pool for " + config);
        if (!instances.containsKey(config)) {
            synchronized (instances) {
                if (!instances.containsKey(config)) {
                    LOGGER.info("Creating shared session pool for " + config);
                    ISessionPool pool = factory.createPool(config);
                    SharedSessionPool sharedPool = new SharedSessionPool(pool);
                    instances.put(config, sharedPool);
                    LOGGER.info("Created shared pool " + sharedPool);
                }
            }
        }

        SharedSessionPool sharedPool = instances.get(config);
        LOGGER.info("Returning shared session pool " + sharedPool);

        return sharedPool;
    }

    @Override
    protected void finalize() {
        LOGGER.info("Destroying session pool for " + getConfig());
        delegate.close();
    }

    /**
     * @see ISessionPool#close()
     */
    public void close() {
        LOGGER.info("Ignoring SessionPool close, this is a shared pool");
    }

    /**
     * @see ISessionPool#getAvailableCount()
     */
    public int getAvailableCount() {
        return delegate.getAvailableCount();
    }

    /**
     * @see ISessionPool#getConfig()
     */
    public ArcSDEConnectionConfig getConfig() {
        return delegate.getConfig();
    }

    /**
     * @see ISessionPool#getInUseCount()
     */
    public int getInUseCount() {
        return delegate.getInUseCount();
    }

    /**
     * @see ISessionPool#getPoolSize()
     */
    public int getPoolSize() {
        return delegate.getPoolSize();
    }

    /**
     * @see org.geotools.arcsde.session.ISessionPool#getSession()
     */
    public ISession getSession() throws IOException, UnavailableConnectionException {
        return getSession(true);
    }

    /**
     * @see ISessionPool#getSession(boolean)
     */
    public ISession getSession(final boolean transactional) throws IOException,
            UnavailableConnectionException {
        return delegate.getSession(transactional);
    }

    /**
     * @see ISessionPool#isClosed()
     */
    public boolean isClosed() {
        return delegate.isClosed();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append("[(").append(instanceNumber).append("), ");
        sb.append("delegate=").append(delegate).append("]");
        return sb.toString();
    }
}
