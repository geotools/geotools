/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.arcsde.session;

import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeRelease;
import java.io.IOException;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool.Config;
import org.geotools.arcsde.ArcSdeException;
import org.geotools.arcsde.logging.Loggers;

/**
 * Maintains <code>SeConnection</code>'s for a single set of connection properties (for instance: by
 * server, port, user and password) in a pool to recycle used connections.
 *
 * <p>We are making use of an Apache Commons ObjectPool to maintain connections. This connection
 * pool is configurable in the sense that some parameters can be passed to establish the pooling
 * policy. To pass parameters to the connection pool, you should set properties in the parameters
 * Map passed to SdeDataStoreFactory.createDataStore, which will invoke SdeConnectionPoolFactory to
 * get the SDE instance's pool singleton. That instance singleton will be created with the
 * preferences passed the first time createDataStore is called for a given SDE instance/user, if
 * subsequent calls change that preferences, they will be ignored.
 *
 * <p>The expected optional parameters that you can set up in the argument Map for createDataStore
 * are:
 *
 * <ul>
 *   <li>pool.minConnections Integer, tells the minimum number of open connections the pool will
 *       maintain opened
 *   <li>pool.maxConnections Integer, tells the maximum number of open connections the pool will
 *       create and maintain opened
 *   <li>pool.timeOut Integer, tells how many milliseconds a calling thread is guaranteed to wait
 *       before getConnection() throws an UnavailableArcSDEConnectionException
 * </ul>
 *
 * @author Gabriel Roldan
 * @version $Id$
 */
class SessionPool implements ISessionPool {

    private static final Logger LOGGER = Loggers.getLogger("org.geotools.arcsde.session");

    protected static final Level INFO_LOG_LEVEL = Level.WARNING;

    private SeConnectionFactory seConnectionFactory;

    /** this connection pool connection's parameters */
    protected ArcSDEConnectionConfig config;

    /** Apache commons-pool used to pool arcsde connections */
    private GenericObjectPool pool;

    private final Queue<Session> openSessionsNonTransactional = new LinkedList<Session>(); // new

    // ConcurrentLinkedQueue<Session>();

    /**
     * Creates a new SessionPool object for the given config.
     *
     * @param config holds connection options such as server, user and password, as well as tuning
     *     options as maximum number of connections allowed
     * @throws IOException If connection could not be established
     * @throws NullPointerException If config is null
     */
    protected SessionPool(ArcSDEConnectionConfig config) throws IOException {
        if (config == null) {
            throw new NullPointerException("parameter config can't be null");
        }

        this.config = config;
        LOGGER.fine("populating ArcSDE connection pool");

        this.seConnectionFactory = createConnectionFactory();

        final int minConnections = config.getMinConnections().intValue();
        final int maxConnections = config.getMaxConnections().intValue();
        if (minConnections > maxConnections) {
            throw new IllegalArgumentException("pool.minConnections > pool.maxConnections");
        }
        { // configure connection pool
            Config poolCfg = new Config();
            // pool upper limit
            poolCfg.maxActive = config.getMaxConnections().intValue();

            // minimum number of idle objects. MAKE SURE this is 0, otherwise the pool will start
            // trying to create connections permanently even if there's a connection failure,
            // ultimately leading to the exhaustion of resources
            poolCfg.minIdle = 0;

            // how many connections may be idle at any time? -1 = no limit. We're running an
            // eviction thread to take care of idle connections (see minEvictableIdleTimeMillis and
            // timeBetweenEvictionRunsMillis)
            poolCfg.maxIdle = -1;

            // When reached the pool upper limit, block and wait for an idle connection for maxWait
            // milliseconds before failing
            poolCfg.maxWait = config.getConnTimeOut().longValue();
            if (poolCfg.maxWait > 0) {
                poolCfg.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
            } else {
                poolCfg.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_FAIL;
            }

            // check connection health at borrowObject()?
            poolCfg.testOnBorrow = true;
            // check connection health at returnObject()?
            poolCfg.testOnReturn = false;
            // check periodically the health of idle connections and discard them if can't be
            // validated?
            poolCfg.testWhileIdle = false;

            // check health of idle connections every 30 seconds
            // /poolCfg.timeBetweenEvictionRunsMillis = 30000;

            // drop connections that have been idle for at least 5 minutes
            poolCfg.minEvictableIdleTimeMillis = 5 * 60 * 1000;

            pool = new GenericObjectPool(seConnectionFactory, poolCfg);

            LOGGER.fine("Created ArcSDE connection pool for " + config);
        }

        ISession[] preload = new ISession[minConnections];

        try {
            for (int i = 0; i < minConnections; i++) {
                preload[i] = (ISession) pool.borrowObject();
                if (i == 0) {
                    SeRelease seRelease = preload[i].getRelease();
                    String sdeDesc = seRelease.getDesc();
                    int major = seRelease.getMajor();
                    int minor = seRelease.getMinor();
                    int bugFix = seRelease.getBugFix();
                    String desc = "ArcSDE " + major + "." + minor + "." + bugFix + " " + sdeDesc;
                    LOGGER.fine("Connected to " + desc);
                }
            }

            for (int i = 0; i < minConnections; i++) {
                pool.returnObject(preload[i]);
            }
        } catch (Exception e) {
            close();
            if (e instanceof IOException) {
                throw (IOException) e;
            }
            throw (IOException) new IOException().initCause(e);
        }
    }

    /**
     * SeConnectionFactory used to create {@link ISession} instances for the pool.
     *
     * <p>Subclass may overide to customize this behaviour.
     *
     * @return SeConnectionFactory.
     */
    protected SeConnectionFactory createConnectionFactory() {
        return new SeConnectionFactory(this.config);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.arcsde.session.ISessionPool#getPoolSize()
     */
    public int getPoolSize() {
        checkOpen();
        return pool.getMaxActive();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.arcsde.session.ISessionPool#close()
     */
    public void close() {
        if (pool != null) {
            try {
                pool.close();
                pool = null;
                LOGGER.fine("SDE connection pool closed. ");
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Closing pool: " + e.getMessage(), e);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.arcsde.session.ISessionPool#isClosed()
     */
    public boolean isClosed() {
        return pool == null;
    }

    private void checkOpen() throws IllegalStateException {
        if (isClosed()) {
            throw new IllegalStateException("This session pool is closed");
        }
    }

    /** Ensures proper closure of connection pool at this object's finalization stage. */
    @Override
    @SuppressWarnings("deprecation") // finalize is deprecated in Java 9
    protected void finalize() {
        close();
    }

    /** @see org.geotools.arcsde.session.ISessionPool#getAvailableCount() */
    public synchronized int getAvailableCount() {
        checkOpen();
        return pool.getMaxActive() - pool.getNumActive();
    }

    /** @see org.geotools.arcsde.session.ISessionPool#getInUseCount() */
    public int getInUseCount() {
        checkOpen();
        return pool.getNumActive();
    }

    /** @see org.geotools.arcsde.session.ISessionPool#getSession() */
    public ISession getSession() throws IOException, UnavailableConnectionException {
        return getSession(true);
    }

    /**
     * Return the session Object to the ConnectionPool. Method must be synchronized, in order to
     * prevent SessionPool from opening more connections than in max.Connection defined under heavy
     * load
     */
    public synchronized void returnObject(Session session) throws Exception {

        openSessionsNonTransactional.remove(session);
        pool.returnObject(session);
    }

    /** @see org.geotools.arcsde.session.ISessionPool#getSession(boolean) */
    public ISession getSession(final boolean transactional)
            throws IOException, UnavailableConnectionException {
        checkOpen();
        try {
            Session connection = null;
            if (transactional) {
                LOGGER.finest("Borrowing session from pool for transactional access");
                connection = (Session) pool.borrowObject();
            } else {
                synchronized (openSessionsNonTransactional) {
                    try {
                        if (LOGGER.isLoggable(Level.FINER)) {
                            LOGGER.finer(
                                    "Grabbing session from pool on "
                                            + Thread.currentThread().getName());
                        }
                        connection = (Session) pool.borrowObject();
                        if (LOGGER.isLoggable(Level.FINER)) {
                            LOGGER.finer(
                                    "Got session from the pool on "
                                            + Thread.currentThread().getName());
                        }
                    } catch (NoSuchElementException e) {
                        if (LOGGER.isLoggable(Level.FINER)) {
                            LOGGER.finer(
                                    "No available sessions in the pool, falling back to queued session");
                        }
                        connection = openSessionsNonTransactional.remove();
                    }

                    openSessionsNonTransactional.add(connection);

                    if (LOGGER.isLoggable(Level.FINER)) {
                        LOGGER.finer(
                                "Got session from the in use queue on "
                                        + Thread.currentThread().getName());
                    }
                }
            }

            connection.markActive();
            return connection;

        } catch (NoSuchElementException e) {
            LOGGER.log(
                    Level.WARNING,
                    "Out of connections: " + e.getMessage() + ". Config: " + this.config);
            throw new UnavailableConnectionException(config.getMaxConnections(), this.config);
        } catch (SeException se) {
            ArcSdeException sdee = new ArcSdeException(se);
            LOGGER.log(Level.WARNING, "ArcSDE error getting connection for " + config, sdee);
            throw sdee;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Unknown problem getting connection: " + e.getMessage(), e);
            throw (IOException)
                    new IOException("Unknown problem fetching connection from connection pool")
                            .initCause(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.arcsde.session.ISessionPool#getConfig()
     */
    public ArcSDEConnectionConfig getConfig() {
        return this.config;
    }

    /**
     * PoolableObjectFactory intended to be used by a Jakarta's commons-pool objects pool, that
     * provides ArcSDE's SeConnections.
     *
     * @author Gabriel Roldan, Axios Engineering
     * @version $Id$
     */
    private class SeConnectionFactory extends BasePoolableObjectFactory {

        private ArcSDEConnectionConfig config;

        /** Creates a new SeConnectionFactory object. */
        public SeConnectionFactory(ArcSDEConnectionConfig config) {
            super();
            this.config = config;
        }

        /**
         * Called whenever a new instance is needed.
         *
         * <p>The implementation for this method needs to be synchronized in order to make sure no
         * two {@code SeConnection} instances are created at the same time. Otherwise, when that
         * happens under load, SeConnection's constructor uses to throw a nasty {@code
         * NegativeArraySizeException}.
         *
         * @return a newly created <code>SeConnection</code>
         * @throws SeException if the connection can't be created
         */
        @Override
        public synchronized Object makeObject() throws IOException {
            ISession seConn;
            seConn = new Session(SessionPool.this, config);
            return seConn;
        }

        @Override
        public void passivateObject(Object obj) {
            LOGGER.finest("    passivating connection " + obj);
            final Session conn = (Session) obj;
            conn.markInactive();
        }

        /**
         * is invoked in an implementation-specific fashion to determine if an instance is still
         * valid to be returned by the pool. It will only be invoked on an "activated" instance.
         *
         * @param obj an instance of {@link Session} maintained by this pool.
         * @return <code>true</code> if the connection is still alive and operative (checked by
         *     asking its user name), <code>false</code> otherwise.
         */
        @Override
        public boolean validateObject(Object obj) {
            ISession session = (ISession) obj;
            boolean valid = !session.isClosed();
            // MAKE PROPER VALIDITY CHECK HERE as for GEOT-1273
            if (valid) {
                try {
                    if (LOGGER.isLoggable(Level.FINEST)) {
                        LOGGER.finest("    Validating SDE Connection " + session);
                    }
                    /*
                     * Validate the connection's health with testServer instead of getUser. The
                     * former is lighter weight, getUser() forced a server round trip and under
                     * heavy concurrency ate about 30% the time
                     */
                    session.testServer();
                } catch (IOException e) {
                    LOGGER.info(
                            "Can't validate SeConnection, discarding it: "
                                    + session
                                    + ". Reason: "
                                    + e.getMessage());
                    valid = false;
                }
            }
            return valid;
        }

        /**
         * is invoked on every instance when it is being "dropped" from the pool (whether due to the
         * response from validateObject, or for reasons specific to the pool implementation.)
         *
         * @param obj an instance of {@link Session} maintained by this pool.
         */
        @Override
        public void destroyObject(Object obj) {
            Session conn = (Session) obj;
            conn.destroy();
        }
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder(getClass().getSimpleName());
        ret.append("[config=").append(getConfig());
        if (pool == null) {
            ret.append("[Session pool is disposed]");
        } else {
            ret.append("[ACTIVE: ");
            ret.append(pool.getNumActive() + "/" + ((GenericObjectPool) pool).getMaxActive());
            ret.append(" INACTIVE: ");
            ret.append(pool.getNumIdle() + "/" + ((GenericObjectPool) pool).getMaxIdle() + "]");
        }
        ret.append("]");
        return ret.toString();
    }
}
